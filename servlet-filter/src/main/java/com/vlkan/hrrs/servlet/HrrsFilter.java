package com.vlkan.hrrs.servlet;

import com.google.protobuf.ByteString;
import com.vlkan.hrrs.api.HttpRequestMethod;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class HrrsFilter implements Filter {

    private static final HrrsIdGenerator ID_GENERATOR = new HrrsIdGenerator(4);

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
            getWriter().write(record);
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
        String id = createRequestId(request);
        String groupName = createRequestGroupName(request);
        String uri = createRequestUri(request);
        HttpRequestMethod method = HttpRequestMethod.valueOf(request.getMethod());
        HttpRequestPayload payload = createPayload(request, recordedPayloadBytes, totalPayloadByteCount);
        return HttpRequestRecord
                .newBuilder()
                .setId(id)
                .setGroupName(groupName)
                .setTimestampMillis(System.currentTimeMillis())
                .setUri(uri)
                .setMethod(method)
                .setPayload(payload)
                .build();
    }

    private static String createRequestUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        boolean blankQueryString = queryString == null || queryString.matches("^\\s*$");
        return blankQueryString ? uri : String.format("%s?%s", uri, queryString);
    }

    private HttpRequestPayload createPayload(HttpServletRequest request, byte[] recordedPayloadBytes, long totalPayloadByteCount) {
        String contentType = request.getContentType();
        long missingByteCount = totalPayloadByteCount - recordedPayloadBytes.length;
        return HttpRequestPayload
                .newBuilder()
                .setType(contentType)
                .setMissingByteCount(missingByteCount)
                .setBytes(ByteString.copyFrom(recordedPayloadBytes))
                .build();
    }

    /**
     * Maximum amount of bytes that can be recorded per request.
     */
    protected long getMaxRecordablePayloadByteCount() {
        return Long.MAX_VALUE;
    }

    /**
     * Create a group name for the given request.
     *
     * Group names are used to group requests and later on are used
     * as identifiers while reporting statistics in the replayer.
     * It is strongly recommended to use group names similar to Java
     * package names.
     */
    protected String createRequestGroupName(HttpServletRequest request) {
        String requestUri = createRequestUri(request);
        return requestUri
                .replaceFirst("\\?.*", "")      // Replace query parameters.
                .replaceFirst("^/", "")         // Replace the initial slash.
                .replaceAll("/", ".");          // Replace all slashes with dots.
    }

    /**
     * Creates a unique identifier for the given request.
     */
    protected String createRequestId(HttpServletRequest request) {
        return ID_GENERATOR.next();
    }

    abstract protected HttpRequestRecordWriter getWriter();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing.
    }

    @Override
    public void destroy() {
        // Do nothing.
    }

}
