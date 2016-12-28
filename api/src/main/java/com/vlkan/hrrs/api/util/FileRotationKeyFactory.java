package com.vlkan.hrrs.api.util;

public interface FileRotationKeyFactory {

    FileRotationKey getNextKey();

    FileRotationKey getNextKey(FileRotationKey prevKey);

}
