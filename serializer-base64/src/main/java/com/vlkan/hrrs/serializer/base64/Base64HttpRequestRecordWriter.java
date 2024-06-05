/*
 * Copyright 2016-2024 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.serializer.base64;

import com.vlkan.hrrs.api.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.*;

public class Base64HttpRequestRecordWriter implements HttpRequestRecordWriter<String> {

    private final Base64Encoder encoder;

    private final HttpRequestRecordWriterTarget<String> target;

    public Base64HttpRequestRecordWriter(HttpRequestRecordWriterTarget<String> target, Base64Encoder encoder) {
        this.target = Objects.requireNonNull(target, "target");
        this.encoder = Objects.requireNonNull(encoder, "encoder");
    }

    @Override
    public HttpRequestRecordWriterTarget<String> getTarget() {
        return target;
    }

    @Override
    public void write(HttpRequestRecord record) {
        try {
            synchronized (this) {
                target.write(record.getId());
                target.write(FIELD_SEPARATOR);
                target.write(DATE_FORMAT.format(record.getTimestamp()));
                target.write(FIELD_SEPARATOR);
                target.write(record.getGroupName());
                target.write(FIELD_SEPARATOR);
                target.write(record.getMethod().toString());
                target.write(FIELD_SEPARATOR);
                byte[] recordBytes = writeRecord(record);
                String encodedRecordBytes = encoder.encode(recordBytes);
                target.write(encodedRecordBytes);
                target.write(RECORD_SEPARATOR);
            }
        } catch (Throwable error) {
            String message = String.format("record serialization failure (id=%s)", record.getId());
            throw new RuntimeException(message, error);
        }
    }

    private byte[] writeRecord(HttpRequestRecord record) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        writeRecord(record, dataOutputStream);
        dataOutputStream.flush();
        return outputStream.toByteArray();
    }

    private static void writeRecord(HttpRequestRecord record, DataOutputStream stream) throws IOException {
        stream.writeUTF(record.getUri());
        writeHeaders(record.getHeaders(), stream);
        writePayload(record.getPayload(), stream);
    }

    private static void writeHeaders(List<HttpRequestHeader> headers, DataOutputStream stream) throws IOException {
        int headerCount = headers.size();
        stream.writeInt(headerCount);
        for (HttpRequestHeader header : headers) {
            stream.writeUTF(header.getName());
            stream.writeUTF(header.getValue());
        }
    }

    private static void writePayload(HttpRequestPayload payload, DataOutputStream stream) throws IOException {
        stream.writeInt(payload.getMissingByteCount());
        stream.writeInt(payload.getBytes().length);
        stream.write(payload.getBytes());
    }

}
