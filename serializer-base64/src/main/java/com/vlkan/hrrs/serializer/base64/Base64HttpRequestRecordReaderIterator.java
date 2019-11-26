package com.vlkan.hrrs.serializer.base64;

import com.vlkan.hrrs.api.*;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.DATE_FORMAT;
import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.FIELD_SEPARATOR;

@NotThreadSafe
public class Base64HttpRequestRecordReaderIterator implements Iterator<HttpRequestRecord> {

    private final HttpRequestRecordReaderSource<String> source;

    private final Base64Decoder decoder;

    private long lineIndex = -1;

    private String line;

    Base64HttpRequestRecordReaderIterator(HttpRequestRecordReaderSource<String> source, Base64Decoder decoder) {
        this.source = checkNotNull(source, "source");
        this.decoder = checkNotNull(decoder, "decoder");
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
        checkArgument(lineIndex >= 0, "hasNext() should have been called first");
        try {
            String[] fields = line.split(FIELD_SEPARATOR, 5);
            checkArgument(fields.length == 5, "insufficient field count (fieldCount=%s)", fields.length);
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
        checkArgument(headerCount >= 0, "expected: headerCount >= 0, found: %s", headerCount);
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
        checkArgument(missingByteCount >= 0, "expected: missingByteCount >= 0, found: %s", missingByteCount);

        // Read bytes.
        int byteCount = stream.readInt();
        checkArgument(byteCount >= 0, "expected: byteCount >= 0, found: %s", byteCount);
        byte[] bytes = new byte[byteCount];
        int readByteCount = Math.max(0, stream.read(bytes));
        checkArgument(byteCount == readByteCount, "expected: %s == readByteCount, found: %s", byteCount, readByteCount);

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
