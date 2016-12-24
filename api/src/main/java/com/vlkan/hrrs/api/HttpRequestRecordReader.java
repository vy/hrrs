package com.vlkan.hrrs.api;

public interface HttpRequestRecordReader {

    Iterable<HttpRequestRecord> read();

}
