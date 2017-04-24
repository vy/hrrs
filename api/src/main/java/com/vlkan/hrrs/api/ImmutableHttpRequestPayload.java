package com.vlkan.hrrs.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ImmutableHttpRequestPayload implements HttpRequestPayload {

    private final long missingByteCount;

    private final byte[] bytes;

    private ImmutableHttpRequestPayload(long missingByteCount, byte[] bytes) {
        checkArgument(missingByteCount >= 0, "expecting: missingByteCount >= 0, found: %s", missingByteCount);
        this.missingByteCount = missingByteCount;
        this.bytes = checkNotNull(bytes, "bytes");
    }

    @Override
    public long getMissingByteCount() {
        return missingByteCount;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableHttpRequestPayload that = (ImmutableHttpRequestPayload) o;
        return missingByteCount == that.missingByteCount &&
                Objects.equal(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(missingByteCount, bytes);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("missingByteCount", missingByteCount)
                .add("byteCount", bytes.length)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private long missingByteCount;

        private byte[] bytes;

        private Builder() {
            // Do nothing.
        }

        public Builder missingByteCount(long missingByteCount) {
            this.missingByteCount = missingByteCount;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public ImmutableHttpRequestPayload build() {
            return new ImmutableHttpRequestPayload(missingByteCount, bytes);
        }

    }

}
