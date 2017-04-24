package com.vlkan.hrrs.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImmutableHttpRequestHeader implements HttpRequestHeader {

    private final String name;

    private final String value;

    private ImmutableHttpRequestHeader(String name, String value) {
        this.name = checkNotNull(name, "name");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableHttpRequestHeader that = (ImmutableHttpRequestHeader) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("value", value)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String value;

        private Builder() {
            // Do nothing.
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public ImmutableHttpRequestHeader build() {
            return new ImmutableHttpRequestHeader(name, value);
        }

    }

}
