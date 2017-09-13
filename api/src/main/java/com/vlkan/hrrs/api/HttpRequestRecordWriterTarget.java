package com.vlkan.hrrs.api;

import java.io.Closeable;

public interface HttpRequestRecordWriterTarget<T> extends Closeable {

    void write(T value);

    void flush();

}
