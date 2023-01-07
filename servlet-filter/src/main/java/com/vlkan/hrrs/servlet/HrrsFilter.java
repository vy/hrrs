/*
 * Copyright 2016-2023 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

/*
 * Copyright 2016-2023 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.servlet;

import com.vlkan.hrrs.api.HttpRequestHeader;
import com.vlkan.hrrs.api.HttpRequestMethod;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.hrrs.api.ImmutableHttpRequestHeader;
import com.vlkan.hrrs.api.ImmutableHttpRequestPayload;
import com.vlkan.hrrs.api.ImmutableHttpRequestRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public abstract class HrrsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HrrsFilter.class);

    public static final String SERVLET_CONTEXT_ATTRIBUTE_KEY = HrrsFilter.class.getCanonicalName();

    public static final String DEFAULT_FORM_PARAMETER_ENCODING = StandardCharsets.US_ASCII.name();

    public static final int DEFAULT_MAX_RECORDABLE_PAYLOAD_BYTE_COUNT = 10 * 1024 * 1024;

    private final HrrsIdGenerator idGenerator;

    private final HrrsUrlEncodedFormHelper urlEncodedFormHelper;

    private volatile boolean enabled = false;

    private ServletContext servletContext = null;

    public HrrsFilter() {
        this(new HrrsIdGenerator(4), new HrrsUrlEncodedFormHelper());
    }

    public HrrsFilter(HrrsIdGenerator idGenerator, HrrsUrlEncodedFormHelper urlEncodedFormHelper) {
        this.idGenerator = idGenerator;
        this.urlEncodedFormHelper = urlEncodedFormHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isRequestRecordable(request)) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpRequestPayload payload = createPayloadUsingFormParameters(httpRequest);
            if (payload == null) {
                ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
                TeeServletInputStream teeServletInputStream = new TeeServletInputStream(
                        httpRequest.getInputStream(),
                        requestOutputStream,
                        getMaxRecordablePayloadByteCount());
                HttpServletRequest teeRequest = new HrrsHttpServletRequestWrapper(httpRequest, teeServletInputStream);
                chain.doFilter(teeRequest, response);
                payload = createPayloadUsingInputStream(requestOutputStream, teeServletInputStream);
            } else {
                chain.doFilter(request, response);
            }
            HttpRequestRecord record = createRecord(httpRequest, payload);
            HttpRequestRecord filteredRecord = filterRecord(record);
            if (filteredRecord != null) {
                getWriter().write(filteredRecord);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isRequestRecordable(ServletRequest request) {
        return enabled && request instanceof HttpServletRequest && isRequestRecordable((HttpServletRequest) request);
    }

    /**
     * Checks if the given HTTP request is recordable.
     */
    protected boolean isRequestRecordable(HttpServletRequest ignored) {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        LOGGER.trace("switched state (enabled={})", enabled);
    }

    private HttpRequestRecord createRecord(HttpServletRequest request, HttpRequestPayload payload) {
        String id = createRequestId(request);
        Date timestamp = new Date();
        String groupName = createRequestGroupName(request);
        String uri = createRequestUri(request);
        HttpRequestMethod method = HttpRequestMethod.valueOf(request.getMethod());
        List<HttpRequestHeader> headers = createHeaders(request);
        return ImmutableHttpRequestRecord
                .newBuilder()
                .setId(id)
                .setTimestamp(timestamp)
                .setGroupName(groupName)
                .setUri(uri)
                .setMethod(method)
                .setHeaders(headers)
                .setPayload(payload)
                .build();
    }

    protected String createRequestUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        boolean blankQueryString = HrrsHelper.isBlank(queryString);
        return blankQueryString ? uri : String.format("%s?%s", uri, queryString);
    }

    private List<HttpRequestHeader> createHeaders(HttpServletRequest request) {
        List<HttpRequestHeader> headers = Collections.emptyList();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getHeader(name);
            ImmutableHttpRequestHeader header = ImmutableHttpRequestHeader
                    .builder()
                    .setName(name)
                    .setValue(value)
                    .build();
            if (headers.isEmpty()) {
                headers = new ArrayList<>();
            }
            headers.add(header);
        }
        return headers;
    }

    @Nullable
    private HttpRequestPayload createPayloadUsingFormParameters(HttpServletRequest request) {
        boolean urlEncodedForm = urlEncodedFormHelper.isUrlEncodedForm(request.getContentType());
        if (!urlEncodedForm) {
            return null;
        }
        String defaultFormParameterEncoding = getDefaultFormParameterEncoding();
        return urlEncodedFormHelper.extractUrlEncodedFormPayload(request, defaultFormParameterEncoding);
    }

    private HttpRequestPayload createPayloadUsingInputStream(
            ByteArrayOutputStream outputStream,
            TeeServletInputStream teeServletInputStream) {
        byte[] recordedPayloadBytes = outputStream.toByteArray();
        int totalPayloadByteCount = teeServletInputStream.getByteCount();
        int missingByteCount = totalPayloadByteCount - recordedPayloadBytes.length;
        return ImmutableHttpRequestPayload
                .newBuilder()
                .setMissingByteCount(missingByteCount)
                .setBytes(recordedPayloadBytes)
                .build();
    }

    /**
     * In the absence of a valid {@code Content-Type}, encoding to be
     * used as a fallback while URL encoding/decoding form parameters.
     * Defaults to {@link HrrsFilter#DEFAULT_FORM_PARAMETER_ENCODING}.
     */
    protected String getDefaultFormParameterEncoding() {
        return DEFAULT_FORM_PARAMETER_ENCODING;
    }

    /**
     * Maximum amount of bytes that can be recorded per request.
     * Defaults to {@link HrrsFilter#DEFAULT_MAX_RECORDABLE_PAYLOAD_BYTE_COUNT}.
     */
    protected int getMaxRecordablePayloadByteCount() {
        return DEFAULT_MAX_RECORDABLE_PAYLOAD_BYTE_COUNT;
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
    protected String createRequestId(HttpServletRequest ignored) {
        return idGenerator.next();
    }

    /**
     * Filter the given record prior to writing.
     * @return the modified record or null to exclude the record
     */
    protected HttpRequestRecord filterRecord(HttpRequestRecord record) {
        return record;
    }

    abstract protected HttpRequestRecordWriter<?> getWriter();

    public void flush() {
        HttpRequestRecordWriter<?> writer = getWriter();
        HttpRequestRecordWriterTarget<?> target = writer.getTarget();
        target.flush();
    }

    @Override
    public synchronized void init(FilterConfig filterConfig) {
        if (servletContext != null) {
            throw new IllegalStateException("servlet context is already initialized");
        }
        servletContext = filterConfig.getServletContext();
        Object prevAttribute = servletContext.getAttribute(SERVLET_CONTEXT_ATTRIBUTE_KEY);
        if (prevAttribute != null) {
            throw new IllegalStateException("servlet context attribute is already initialized");
        }
        servletContext.setAttribute(SERVLET_CONTEXT_ATTRIBUTE_KEY, this);
        LOGGER.trace("initialized");
    }

    @Override
    public synchronized void destroy() {
        if (servletContext == null) {
            throw new IllegalStateException("servlet context is not initialized");
        }
        servletContext.removeAttribute(SERVLET_CONTEXT_ATTRIBUTE_KEY);
        LOGGER.trace("destroyed");
    }

}
