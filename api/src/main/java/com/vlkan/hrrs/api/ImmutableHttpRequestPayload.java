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

package com.vlkan.hrrs.api;

import java.util.Arrays;
import java.util.Objects;

public class ImmutableHttpRequestPayload implements HttpRequestPayload {

    private final int missingByteCount;

    private final byte[] bytes;

    private ImmutableHttpRequestPayload(int missingByteCount, byte[] bytes) {
        if (missingByteCount < 0) {
            throw new IllegalArgumentException("expecting: missingByteCount >= 0, found: " + missingByteCount);
        }
        this.missingByteCount = missingByteCount;
        this.bytes = Objects.requireNonNull(bytes, "bytes");
    }

    @Override
    public int getMissingByteCount() {
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
                Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missingByteCount, Arrays.hashCode(bytes));
    }

    @Override
    public String toString() {
        return "ImmutableHttpRequestPayload{" +
                "missingByteCount=" + missingByteCount +
                ", byteCount=" + bytes.length +
                '}';
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.missingByteCount = missingByteCount;
        builder.bytes = bytes;
        return builder;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements HttpRequestPayload.Builder {

        private int missingByteCount;

        private byte[] bytes;

        private Builder() {
            // Do nothing.
        }

        @Override
        public Builder setMissingByteCount(int missingByteCount) {
            this.missingByteCount = missingByteCount;
            return this;
        }

        @Override
        public Builder setBytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        @Override
        public ImmutableHttpRequestPayload build() {
            return new ImmutableHttpRequestPayload(missingByteCount, bytes);
        }

    }

}
