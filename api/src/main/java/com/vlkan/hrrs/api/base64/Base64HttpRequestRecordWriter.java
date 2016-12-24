package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecord;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordWriter implements HttpRequestRecordWriter {

    private final Base64Encoder encoder;

    private final OutputStream outputStream;

    public Base64HttpRequestRecordWriter(Base64Encoder encoder, OutputStream outputStream) {
        this.encoder = checkNotNull(encoder, "encoder");
        this.outputStream = checkNotNull(outputStream, "outputStream");
    }

    @Override
    public synchronized void write(HttpRequestRecord record) {
        try {
            record.getIdBytes().writeTo(outputStream);
            outputStream.write(Base64HttpRequestRecord.FIELD_SEPARATOR_BYTES);
            byte[] recordBytes = record.toByteArray();
            String encodedRecordBytes = encoder.encode(recordBytes);
            outputStream.write(encodedRecordBytes.getBytes(Base64HttpRequestRecord.CHARSET));
            outputStream.write(Base64HttpRequestRecord.RECORD_SEPARATOR_BYTES);
        } catch (IOException error) {
            String message = String.format("record serialization failure (id=%s)", record.getId());
            throw new RuntimeException(message, error);
        }
    }

}
