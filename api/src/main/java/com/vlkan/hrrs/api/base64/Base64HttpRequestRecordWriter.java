package com.vlkan.hrrs.api.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordTarget;
import com.vlkan.hrrs.api.HttpRequestRecordWriter;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.vlkan.hrrs.api.base64.Base64HttpRequestRecord.FIELD_SEPARATOR;
import static com.vlkan.hrrs.api.base64.Base64HttpRequestRecord.RECORD_SEPARATOR;

public class Base64HttpRequestRecordWriter implements HttpRequestRecordWriter {

    private final Base64Encoder encoder;

    private final HttpRequestRecordTarget target;

    public Base64HttpRequestRecordWriter(HttpRequestRecordTarget target, Base64Encoder encoder) {
        this.target = checkNotNull(target, "target");
        this.encoder = checkNotNull(encoder, "encoder");
    }

    @Override
    public HttpRequestRecordTarget getTarget() {
        return target;
    }

    @Override
    public synchronized void write(HttpRequestRecord record) {
        try {
            target.write(record.getId());
            target.write(FIELD_SEPARATOR);
            byte[] recordBytes = record.toByteArray();
            String encodedRecordBytes = encoder.encode(recordBytes);
            target.write(encodedRecordBytes);
            target.write(RECORD_SEPARATOR);
        } catch (Throwable error) {
            String message = String.format("record serialization failure (id=%s)", record.getId());
            throw new RuntimeException(message, error);
        }
    }

}
