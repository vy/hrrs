package com.vlkan.hrrs.api;

public interface HttpRequestPayload {

    int getMissingByteCount();

    byte[] getBytes();

    Builder toBuilder();

    interface Builder {

        Builder setMissingByteCount(int missingByteCount);

        Builder setBytes(byte[] bytes);

        HttpRequestPayload build();

    }

}
