package com.vlkan.hrrs.api;

public interface HttpRequestRecordReader {

    HttpRequestRecordSource getSource();

    Iterable<HttpRequestRecord> read();

}
