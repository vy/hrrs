package com.vlkan.hrrs.api;

public interface HttpRequestHeader {

    String getName();

    String getValue();

    Builder toBuilder();

    interface Builder {

        Builder setName(String name);

        Builder setValue(String value);

        HttpRequestHeader build();

    }

}
