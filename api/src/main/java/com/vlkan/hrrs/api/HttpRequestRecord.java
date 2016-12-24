package com.vlkan.hrrs.api;

import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
public interface HttpRequestRecord {

    String getId();

    long getTimestamp();

    String getUri();

    HttpRequestMethod getMethod();

    Map<String, String> getParameters();

    Map<String, String> getHeaders();

    HttpRequestPayload getPayload();

}
