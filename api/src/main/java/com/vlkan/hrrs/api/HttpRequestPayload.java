package com.vlkan.hrrs.api;

public interface HttpRequestPayload {

    long getMissingByteCount();

    byte[] getBytes();

}
