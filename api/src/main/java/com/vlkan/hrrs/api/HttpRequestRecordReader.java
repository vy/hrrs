package com.vlkan.hrrs.api;

public interface HttpRequestRecordReader<T> {

    HttpRequestRecordReaderSource<T> getSource();

    Iterable<HttpRequestRecord> read();

}
