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

    Builder toBuilder();

    interface Builder {

        Builder setId(String id);

        Builder setTimestamp(Date timestamp);

        Builder setGroupName(String groupName);

        Builder setUri(String uri);

        Builder setMethod(HttpRequestMethod method);

        Builder setHeaders(List<HttpRequestHeader> headers);

        Builder setPayload(HttpRequestPayload payload);

        HttpRequestRecord build();

    }

}
