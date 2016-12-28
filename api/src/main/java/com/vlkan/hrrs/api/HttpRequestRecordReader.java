package com.vlkan.hrrs.api;

public interface HttpRequestRecordReader {

    HttpRequestRecordReaderSource getSource();

    Iterable<HttpRequestRecord> read();

}
