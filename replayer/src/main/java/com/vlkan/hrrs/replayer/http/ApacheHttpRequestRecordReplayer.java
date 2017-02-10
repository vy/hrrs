package com.vlkan.hrrs.replayer.http;

import com.codahale.metrics.MetricRegistry;
import com.vlkan.hrrs.api.HttpRequestHeader;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.replayer.cli.Config;
import com.vlkan.hrrs.replayer.jtl.JtlPrinter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class ApacheHttpRequestRecordReplayer implements HttpRequestRecordReplayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheHttpRequestRecordReplayer.class);

    private final MetricRegistry metricRegistry;

    private final JtlPrinter jtlPrinter;

    private final HttpHost httpHost;

    private final CloseableHttpClient httpClient;

    @Inject
    public ApacheHttpRequestRecordReplayer(
            Config config,
            ApacheHttpClientFactory httpClientFactory,
            MetricRegistry metricRegistry,
            JtlPrinter jtlPrinter) {

        // Check arguments.
        checkNotNull(config, "config");
        checkNotNull(httpClientFactory, "httpClientFactory");
        checkNotNull(metricRegistry, "metricRegistry");
        checkNotNull(jtlPrinter, "jtlPrinter");

        // Set class fields.
        this.metricRegistry = metricRegistry;
        this.jtlPrinter = jtlPrinter;
        this.httpHost = new HttpHost(config.getTargetHost(), config.getTargetPort());
        this.httpClient = httpClientFactory.create();
        LOGGER.debug("instantiated");

    }

    @Override
    public void replay(HttpRequestRecord record) {
        LOGGER.trace("replaying record (id={})", record.getId());
        long startTimeMillis = System.currentTimeMillis();
        try {
            int statusCode = execute(record);
            long stopTimeMillis = System.currentTimeMillis();
            long durationMillis = stopTimeMillis - startTimeMillis;
            report(record, statusCode, durationMillis);
        } catch (Throwable error) {
            String message = String.format("failed replaying record (id=%s)", record.getId());
            LOGGER.error(message, error);
        }
    }

    private int execute(HttpRequestRecord record) throws IOException {
        HttpUriRequest httpUriRequest = createHttpUriRequest(record);
        CloseableHttpResponse httpResponse = httpClient.execute(httpHost, httpUriRequest);
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                entity.getContent().close();
            }
            return httpResponse.getStatusLine().getStatusCode();
        } catch (SocketTimeoutException error) {
            return HttpStatus.SC_GATEWAY_TIMEOUT;
        } finally {
            httpResponse.close();
        }
    }

    private void report(HttpRequestRecord record, int statusCode, long durationMillis) {
        String threadName = Thread.currentThread().getName();
        jtlPrinter.print(record.getTimestampMillis(), record.getGroupName(), statusCode, durationMillis, threadName);
        metricRegistry.timer("__all__").update(durationMillis, TimeUnit.MILLISECONDS);
        metricRegistry.timer("__all__." + statusCode).update(durationMillis, TimeUnit.MILLISECONDS);
        metricRegistry.timer(record.getGroupName()).update(durationMillis, TimeUnit.MILLISECONDS);
        metricRegistry.timer(record.getGroupName() + "." + statusCode).update(durationMillis, TimeUnit.MILLISECONDS);
        LOGGER.trace(
                "replayed record (id={}, method={}, groupName={}, statusCode={}, durationMillis={})",
                record.getId(),
                record.getMethod(),
                record.getGroupName(),
                statusCode,
                durationMillis);
    }

    private HttpUriRequest createHttpUriRequest(HttpRequestRecord record) {
        HttpUriRequest request;
        switch (record.getMethod()) {

            case DELETE:
                request = new HttpDelete(record.getUri());
                break;

            case GET:
                request = new HttpGet(record.getUri());
                break;

            case HEAD:
                request = new HttpHead(record.getUri());
                break;

            case OPTIONS:
                request = new HttpOptions(record.getUri());
                break;

            case PATCH:
                HttpPatch httpPatch = new HttpPatch(record.getUri());
                addHttpRequestPayload(record, httpPatch);
                request = httpPatch;
                break;

            case POST:
                HttpPost httpPost = new HttpPost(record.getUri());
                addHttpRequestPayload(record, httpPost);
                request = httpPost;
                break;

            case PUT:
                HttpPut httpPut = new HttpPut(record.getUri());
                addHttpRequestPayload(record, httpPut);
                request = httpPut;
                break;

            case TRACE:
                request = new HttpTrace(record.getUri());
                break;

            default:
                throw new UnsupportedOperationException("unknown method: " + record.getMethod());

        }
        addHttpUriRequestHeaders(request, record);
        return request;
    }

    private static void addHttpRequestPayload(HttpRequestRecord record, HttpEntityEnclosingRequest request) {
        HttpRequestPayload payload = record.getPayload();
        byte[] payloadBytes = payload.getBytes();
        if (payloadBytes.length > 0) {
            ByteArrayEntity payloadEntity = new ByteArrayEntity(payloadBytes);
            request.setEntity(payloadEntity);
        }
    }

    private void addHttpUriRequestHeaders(HttpUriRequest request, HttpRequestRecord record) {
        for (HttpRequestHeader header : record.getHeaders()) {
            request.setHeader(header.getName(), header.getValue());
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.debug("closing");
        httpClient.close();
        jtlPrinter.close();
    }

}
