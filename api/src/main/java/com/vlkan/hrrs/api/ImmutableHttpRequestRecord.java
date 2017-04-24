package com.vlkan.hrrs.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
        this.id = checkNotNull(id, "id");
        this.timestamp = checkNotNull(timestamp, "timestamp");
        this.groupName = checkNotNull(groupName, "groupName");
        this.uri = checkNotNull(uri, "uri");
        this.method = checkNotNull(method, "method");
        this.headers = checkNotNull(headers, "headers");
        this.payload = checkNotNull(payload, "payload");
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

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableHttpRequestRecord that = (ImmutableHttpRequestRecord) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(timestamp, that.timestamp) &&
                Objects.equal(groupName, that.groupName) &&
                Objects.equal(uri, that.uri) &&
                method == that.method &&
                Objects.equal(headers, that.headers) &&
                Objects.equal(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, timestamp, groupName, uri, method, headers, payload);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("id", id)
                .add("timestamp", timestamp)
                .add("groupName", groupName)
                .add("uri", uri)
                .add("method", method)
                .add("headers", headers)
                .add("payload", payload)
                .toString();
    }

    public static class Builder {

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

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder method(HttpRequestMethod method) {
            this.method = method;
            return this;
        }

        public Builder headers(List<HttpRequestHeader> headers) {
            this.headers = headers;
            return this;
        }

        public Builder payload(HttpRequestPayload payload) {
            this.payload = payload;
            return this;
        }

        public ImmutableHttpRequestRecord build() {
            return new ImmutableHttpRequestRecord(id, timestamp, groupName, uri, method, headers, payload);
        }

    }

}
