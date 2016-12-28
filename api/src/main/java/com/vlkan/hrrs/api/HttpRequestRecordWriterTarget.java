package com.vlkan.hrrs.api;

import java.io.Closeable;

public interface HttpRequestRecordWriterTarget extends Closeable {

    void write(String value);

}
