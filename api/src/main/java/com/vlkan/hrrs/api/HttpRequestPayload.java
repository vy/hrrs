package com.vlkan.hrrs.api;

import org.immutables.value.Value;

@Value.Immutable
public interface HttpRequestPayload {

    long getMissingByteCount();

    byte[] getBytes();

}
