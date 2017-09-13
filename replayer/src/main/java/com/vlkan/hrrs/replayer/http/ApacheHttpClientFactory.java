package com.vlkan.hrrs.replayer.http;

import com.vlkan.hrrs.replayer.cli.Config;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class ApacheHttpClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheHttpClientFactory.class);

    private final Config config;

    @Inject
    public ApacheHttpClientFactory(Config config) {
        this.config = checkNotNull(config, "config");
        LOGGER.debug(
                "instantiated (threadCount={}, timeoutSeconds={}, localAddress={})",
                config.getThreadCount(), config.getRequestTimeoutSeconds(), config.getLocalAddress());
    }

    public CloseableHttpClient create() {
        RequestConfig requestConfig = createRequestConfig();
        HttpClientConnectionManager connectionManager = createConnectionManager();
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager);
        setRedirectStrategy(httpClientBuilder);
        return httpClientBuilder.build();
    }

    private RequestConfig createRequestConfig() {
        int requestTimeoutMillis = (int) (config.getRequestTimeoutSeconds() * 1000);
        RequestConfig.Builder builder = RequestConfig
                .custom()
                .setConnectTimeout(requestTimeoutMillis)
                .setConnectionRequestTimeout(requestTimeoutMillis)
                .setSocketTimeout(requestTimeoutMillis);
        setLocalAddress(builder);
        return builder.build();
    }

    private void setLocalAddress(RequestConfig.Builder builder) {
        String localAddress = config.getLocalAddress();
        if (localAddress != null) {
            try {
                InetAddress localInetAddress = InetAddress.getByName(localAddress);
                builder.setLocalAddress(localInetAddress);
            } catch (UnknownHostException error) {
                String message = String.format("failed reading IP address (localAddress=%s)", localAddress);
                throw new RuntimeException(message, error);
            }
        }
    }

    private HttpClientConnectionManager createConnectionManager() {
        SocketConfig socketConfig = createSocketConfig();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getThreadCount());
        connectionManager.setDefaultMaxPerRoute(config.getThreadCount());
        connectionManager.setDefaultSocketConfig(socketConfig);
        return connectionManager;
    }

    private static SocketConfig createSocketConfig() {
        return SocketConfig
                .custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .build();
    }

    private void setRedirectStrategy(HttpClientBuilder httpClientBuilder) {
        RedirectStrategy redirectStrategy = config.getRedirectStrategy().getImplementation();
        if (redirectStrategy == null) {
            httpClientBuilder.disableRedirectHandling();
        } else {
            httpClientBuilder.setRedirectStrategy(redirectStrategy);
        }
    }

}
