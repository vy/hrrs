package com.vlkan.hrrs.api;

import org.immutables.value.Value;

@Value.Immutable
public interface HttpRequestHeader {

    String getName();

    String getValue();

}
