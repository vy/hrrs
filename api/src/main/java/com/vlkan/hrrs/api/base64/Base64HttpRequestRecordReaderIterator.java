package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordReaderIterator implements Iterator<HttpRequestRecord> {

    private final Base64Decoder decoder;

    private final BufferedReader reader;

    private long lineIndex = -1;

    private String line;

    Base64HttpRequestRecordReaderIterator(Base64Decoder decoder, BufferedReader reader) {
        this.decoder = checkNotNull(decoder, "decoder");
        this.reader = checkNotNull(reader, "reader");
    }

    @Override
    public boolean hasNext() {
        try {
            return reader.ready() && tryReadingLine();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    private boolean tryReadingLine() throws IOException {
        line = reader.readLine();
        if (line != null) {
            lineIndex++;
            return true;
        }
        return false;
    }

    @Override
    public HttpRequestRecord next() {
        checkArgument(lineIndex >= 0, "hasNext() should have been called first");
        try {
            int separatorIndex = line.indexOf(Base64HttpRequestRecord.RECORD_SEPARATOR);
            if (separatorIndex < 0) {
                String message = String.format("could not locate the field separator (lineIndex=%d)", lineIndex);
                throw new RuntimeException(message);
            }
            String encodedRecordBytes = line.substring(separatorIndex + Base64HttpRequestRecord.RECORD_SEPARATOR.length());
            byte[] recordBytes = decoder.decode(encodedRecordBytes);
            return HttpRequestRecord.parseFrom(recordBytes);
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
