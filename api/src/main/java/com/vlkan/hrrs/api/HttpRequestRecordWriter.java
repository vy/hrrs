package com.vlkan.hrrs.api;

public interface HttpRequestRecordWriter {

    HttpRequestRecordWriterTarget getTarget();

    void write(HttpRequestRecord record);

}
