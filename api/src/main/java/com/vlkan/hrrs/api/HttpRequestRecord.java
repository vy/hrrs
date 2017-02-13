package com.vlkan.hrrs.api;

import org.immutables.value.Value;

import java.util.Date;
import java.util.List;

@Value.Immutable
public interface HttpRequestRecord {

    String getId();

    Date getTimestamp();

    String getGroupName();

    String getUri();

    HttpRequestMethod getMethod();

    List<HttpRequestHeader> getHeaders();

    HttpRequestPayload getPayload();

}
