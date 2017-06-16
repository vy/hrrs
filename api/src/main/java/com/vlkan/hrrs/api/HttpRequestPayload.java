package com.vlkan.hrrs.api;

public interface HttpRequestPayload {

    long getMissingByteCount();

    byte[] getBytes();

    Builder toBuilder();

    interface Builder {

        Builder setMissingByteCount(long missingByteCount);

        Builder setBytes(byte[] bytes);

        HttpRequestPayload build();

    }

}
