package com.vlkan.hrrs.api.util;

import org.immutables.value.Value;

@Value.Immutable
public interface FileRotationKey {

    long getRotationTimeMillis();

    String getRotationFileKey();

}
