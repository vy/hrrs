package com.vlkan.hrrs.api;

import java.util.Date;
import java.util.List;

public interface HttpRequestRecord {

    String getId();

    Date getTimestamp();

    String getGroupName();

    String getUri();

    HttpRequestMethod getMethod();

    List<HttpRequestHeader> getHeaders();

    HttpRequestPayload getPayload();

}
