package com.vlkan.hrrs.servlet;

import com.google.protobuf.ByteString;
import com.vlkan.hrrs.api.HttpRequestMethod;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.HttpRequestRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HrrsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HrrsFilter.class);

    private static final HrrsIdGenerator ID_GENERATOR = new HrrsIdGenerator(4);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("instantiated");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isRequestRecordable(request)) {
            ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            TeeServletInputStream teeServletInputStream = new TeeServletInputStream(httpRequest.getInputStream(), requestOutputStream, getMaxRecordablePayloadByteCount());
            HttpServletRequest teeRequest = new HrrsHttpServletRequestWrapper(httpRequest, teeServletInputStream);
            chain.doFilter(teeRequest, response);
            long totalPayloadByteCount = teeServletInputStream.getByteCount();
            byte[] recordedPayloadBytes = requestOutputStream.toByteArray();
            HttpRequestRecord record = createRecord(httpRequest, recordedPayloadBytes, totalPayloadByteCount);
        }
    }

    private boolean isRequestRecordable(ServletRequest request) {
        return request instanceof HttpServletRequest && isRequestRecordable((HttpServletRequest) request);
    }

    /**
     * Checks if the given HTTP request is recordable.
     */
    protected boolean isRequestRecordable(HttpServletRequest request) {
        return true;
    }

    private HttpRequestRecord createRecord(HttpServletRequest request, byte[] recordedPayloadBytes, long totalPayloadByteCount) {
        String id = generateId(request);
        HttpRequestMethod method = HttpRequestMethod.valueOf(request.getMethod());
        HttpRequestPayload payload = createPayload(request, recordedPayloadBytes, totalPayloadByteCount);
        return HttpRequestRecord
                .newBuilder()
                .setId(id)
                .setTimestamp(System.currentTimeMillis())
                .setUri(request.getRequestURI())
                .setMethod(method)
                .setPayload(payload)
                .build();
    }

    private HttpRequestPayload createPayload(HttpServletRequest request, byte[] recordedPayloadBytes, long totalPayloadByteCount) {
        String contentType = request.getContentType();
        long missingByteCount = totalPayloadByteCount - recordedPayloadBytes.length;
        return HttpRequestPayload
                .newBuilder()
                .setType(contentType)
                .setData(ByteString.copyFrom(recordedPayloadBytes))
                .setMissingByteCount(missingByteCount)
                .build();
    }

    /**
     * Maximum amount of bytes that can be recorded per request.
     */
    protected long getMaxRecordablePayloadByteCount() {
        return Long.MAX_VALUE;
    }

    /**
     * Generates a unique identifier for the given request.
     */
    protected String generateId(HttpServletRequest request) {
        return ID_GENERATOR.next();
    }

    @Override
    public void destroy() {
        LOGGER.info("destroyed");
    }

}
