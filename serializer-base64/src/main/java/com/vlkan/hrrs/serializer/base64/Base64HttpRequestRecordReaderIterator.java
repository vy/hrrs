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

import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.DATE_FORMAT;
import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.FIELD_SEPARATOR;

@NotThreadSafe
public class Base64HttpRequestRecordReaderIterator implements Iterator<HttpRequestRecord> {

    private final HttpRequestRecordReaderSource<String> source;

    private final Base64Decoder decoder;

    private long lineIndex = -1;

    private String line;

    Base64HttpRequestRecordReaderIterator(HttpRequestRecordReaderSource<String> source, Base64Decoder decoder) {
        this.source = Objects.requireNonNull(source, "source");
        this.decoder = Objects.requireNonNull(decoder, "decoder");
    }

    @Override
    public boolean hasNext() {
        line = source.read();
        if (line != null) {
            lineIndex++;
            return true;
        }
        return false;
    }

    @Override
    public HttpRequestRecord next() {
        if (lineIndex < 0) {
            throw new RuntimeException("hasNext() should have been called first");
        }
        try {
            String[] fields = line.split(FIELD_SEPARATOR, 5);
            if (fields.length != 5) {
                throw new RuntimeException("insufficient field count: " + fields.length);
            }
            String id = fields[0];
            Date timestamp = DATE_FORMAT.parse(fields[1]);
            String groupName = fields[2];
            HttpRequestMethod method = HttpRequestMethod.valueOf(fields[3]);
            String encodedRecordBytes = fields[4];
            byte[] recordBytes = decoder.decode(encodedRecordBytes);
            return readRecord(id, timestamp, groupName, method, recordBytes);
        } catch (Throwable error) {
            String message = String.format("failed parsing record (lineIndex=%d)", lineIndex);
            throw new RuntimeException(message, error);
        }
    }

    private static HttpRequestRecord readRecord(String id, Date timestamp, String groupName, HttpRequestMethod method, byte[] recordBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(recordBytes);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        return readRecord(id, timestamp, groupName, method, dataInputStream);
    }

    private static HttpRequestRecord readRecord(String id, Date timestamp, String groupName, HttpRequestMethod method, DataInputStream stream) throws IOException {

        // Read fields.
        String uri = stream.readUTF();
        List<HttpRequestHeader> headers = readHeaders(stream);
        HttpRequestPayload payload = readPayload(stream);

        // Create record.
        return ImmutableHttpRequestRecord
                .newBuilder()
                .setId(id)
                .setGroupName(groupName)
                .setTimestamp(timestamp)
                .setUri(uri)
                .setMethod(method)
                .setHeaders(headers)
                .setPayload(payload)
                .build();

    }

    private static List<HttpRequestHeader> readHeaders(DataInputStream stream) throws IOException {

        // See if there are any headers at all.
        int headerCount = stream.readInt();
        if (headerCount < 0) {
            throw new RuntimeException("expected: headerCount >= 0, found: " + headerCount);
        }
        if (headerCount == 0) {
            return Collections.emptyList();
        }

        // Read headers.
        List<HttpRequestHeader> headers = new ArrayList<>(headerCount);
        for (int headerIndex = 0; headerIndex < headerCount; headerIndex++) {
            String name = stream.readUTF();
            String value = stream.readUTF();
            ImmutableHttpRequestHeader header = ImmutableHttpRequestHeader
                    .builder()
                    .setName(name)
                    .setValue(value)
                    .build();
            headers.add(header);
        }
        return headers;

    }

    private static HttpRequestPayload readPayload(DataInputStream stream) throws IOException {

        // Read missing byte count.
        int missingByteCount = stream.readInt();
        if (missingByteCount < 0) {
            throw new RuntimeException("expected: missingByteCount >= 0, found: " + missingByteCount);
        }

        // Read bytes.
        int byteCount = stream.readInt();
        if (byteCount < 0) {
            throw new RuntimeException("expected: byteCount >= 0, found: " + byteCount);
        }
        byte[] bytes = new byte[byteCount];
        int readByteCount = Math.max(0, stream.read(bytes));
        if (byteCount != readByteCount) {
            String message = String.format("expected: %s == readByteCount, found: %s", byteCount, readByteCount);
            throw new RuntimeException(message);
        }

        return ImmutableHttpRequestPayload
                .newBuilder()
                .setMissingByteCount(missingByteCount)
                .setBytes(bytes)
                .build();

    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
