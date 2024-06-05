/*
 * Copyright 2016-2024 Volkan Yazıcı
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

package com.vlkan.hrrs.api;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ImmutableHttpRequestRecord implements HttpRequestRecord {

    private final String id;

    private final Date timestamp;

    private final String groupName;

    private final String uri;

    private final HttpRequestMethod method;

    private final List<HttpRequestHeader> headers;

    private final HttpRequestPayload payload;

    private ImmutableHttpRequestRecord(
            String id,
            Date timestamp,
            String groupName,
            String uri,
            HttpRequestMethod method,
            List<HttpRequestHeader> headers,
            HttpRequestPayload payload) {
        this.id = Objects.requireNonNull(id, "id");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        this.groupName = Objects.requireNonNull(groupName, "groupName");
        this.uri = Objects.requireNonNull(uri, "uri");
        this.method = Objects.requireNonNull(method, "method");
        this.headers = Objects.requireNonNull(headers, "headers");
        this.payload = Objects.requireNonNull(payload, "payload");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public HttpRequestMethod getMethod() {
        return method;
    }

    @Override
    public List<HttpRequestHeader> getHeaders() {
        return headers;
    }

    @Override
    public HttpRequestPayload getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableHttpRequestRecord that = (ImmutableHttpRequestRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(uri, that.uri) &&
                method == that.method &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, groupName, uri, method, headers, payload);
    }

    @Override
    public String toString() {
        return "ImmutableHttpRequestRecord{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", groupName='" + groupName + '\'' +
                ", uri='" + uri + '\'' +
                ", method=" + method +
                ", headers=" + headers +
                ", payload=" + payload +
                '}';
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.timestamp = timestamp;
        builder.groupName = groupName;
        builder.uri = uri;
        builder.method = method;
        builder.headers = headers;
        builder.payload = payload;
        return builder;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements HttpRequestRecord.Builder {

        private String id;

        private Date timestamp;

        private String groupName;

        private String uri;

        private HttpRequestMethod method;

        private List<HttpRequestHeader> headers;

        private HttpRequestPayload payload;

        private Builder() {
            // Do nothing.
        }

        @Override
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public Builder setGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        @Override
        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        @Override
        public Builder setMethod(HttpRequestMethod method) {
            this.method = method;
            return this;
        }

        @Override
        public Builder setHeaders(List<HttpRequestHeader> headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public Builder setPayload(HttpRequestPayload payload) {
            this.payload = payload;
            return this;
        }

        @Override
        public ImmutableHttpRequestRecord build() {
            return new ImmutableHttpRequestRecord(id, timestamp, groupName, uri, method, headers, payload);
        }

    }

}
