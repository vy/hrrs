package com.vlkan.hrrs.api;

public interface HttpRequestRecordWriter<T> {

    HttpRequestRecordWriterTarget<T> getTarget();

    void write(HttpRequestRecord record);

}
