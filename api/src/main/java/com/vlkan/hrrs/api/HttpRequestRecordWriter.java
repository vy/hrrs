package com.vlkan.hrrs.api;

public interface HttpRequestRecordWriter {

    HttpRequestRecordTarget getTarget();

    void write(HttpRequestRecord record);

}
