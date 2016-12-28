package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordReaderIterable implements Iterable<HttpRequestRecord> {

    private final HttpRequestRecordReaderSource source;

    private final Base64Decoder decoder;

    Base64HttpRequestRecordReaderIterable(HttpRequestRecordReaderSource source, Base64Decoder decoder) {
        this.source = checkNotNull(source, "source");
        this.decoder = checkNotNull(decoder, "decoder");
    }

    @Override
    public Iterator<HttpRequestRecord> iterator() {
        return new Base64HttpRequestRecordReaderIterator(source, decoder);
    }

}
