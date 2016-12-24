package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;

import java.io.BufferedReader;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordReaderIterable implements Iterable<HttpRequestRecord> {

    private final Base64Decoder decoder;

    private final BufferedReader reader;

    Base64HttpRequestRecordReaderIterable(Base64Decoder decoder, BufferedReader reader) {
        this.decoder = checkNotNull(decoder, "decoder");
        this.reader = checkNotNull(reader, "reader");
    }

    @Override
    public Iterator<HttpRequestRecord> iterator() {
        return new Base64HttpRequestRecordReaderIterator(decoder, reader);
    }

}
