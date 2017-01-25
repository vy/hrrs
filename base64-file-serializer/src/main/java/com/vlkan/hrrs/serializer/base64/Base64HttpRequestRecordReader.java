package com.vlkan.hrrs.serializer.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordReader implements HttpRequestRecordReader<String> {

    private final HttpRequestRecordReaderSource<String> source;

    private final Base64Decoder decoder;

    public Base64HttpRequestRecordReader(HttpRequestRecordReaderSource<String> source, Base64Decoder decoder) {
        this.source = checkNotNull(source, "source");
        this.decoder = checkNotNull(decoder, "decoder");
    }

    @Override
    public HttpRequestRecordReaderSource<String> getSource() {
        return source;
    }

    @Override
    public Iterable<HttpRequestRecord> read() {
        return new Base64HttpRequestRecordReaderIterable(source, decoder);
    }

}
