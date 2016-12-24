package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordReader implements HttpRequestRecordReader {

    private final Base64Decoder decoder;

    private final BufferedReader reader;

    public Base64HttpRequestRecordReader(Base64Decoder decoder, InputStream inputStream) {
        this.decoder = checkNotNull(decoder, "decoder");
        this.reader = createBufferedReader(checkNotNull(inputStream, "inputStream"));
    }

    private static BufferedReader createBufferedReader(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Base64HttpRequestRecord.CHARSET);
        return new BufferedReader(inputStreamReader);
    }

    @Override
    public Iterable<HttpRequestRecord> read() {
        return new Base64HttpRequestRecordReaderIterable(decoder, reader);
    }

}
