package com.vlkan.hrrs.serializer.base64;

import com.vlkan.hrrs.api.*;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord.*;

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
            int separatorIndex = line.indexOf(FIELD_SEPARATOR);
            if (separatorIndex < 0) {
                String message = String.format("could not locate the field separator (lineIndex=%d)", lineIndex);
                throw new RuntimeException(message);
            }
            String encodedRecordBytes = line.substring(separatorIndex + RECORD_SEPARATOR.length());
            byte[] recordBytes = decoder.decode(encodedRecordBytes);
            return readRecord(recordBytes);
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    private static HttpRequestRecord readRecord(byte[] recordBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(recordBytes);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        return readRecord(dataInputStream);
    }

    private static HttpRequestRecord readRecord(DataInputStream stream) throws IOException {

        // Read fields.
        String id = stream.readUTF();
        String groupName = stream.readUTF();
        long timestampMillis = stream.readLong();
        checkArgument(timestampMillis > 0, "expected: timestampMillis > 0, found: %s", timestampMillis);
        String uri = stream.readUTF();
        HttpRequestMethod method = readMethod(stream);
        List<HttpRequestHeader> headers = readHeaders(stream);
        HttpRequestPayload payload = readPayload(stream);

        // Create record.
        return ImmutableHttpRequestRecord
                .builder()
                .id(id)
                .groupName(groupName)
                .timestampMillis(timestampMillis)
                .uri(uri)
                .method(method)
                .headers(headers)
                .payload(payload)
                .build();

    }

    private static HttpRequestMethod readMethod(DataInputStream stream) throws IOException {
        String methodName = stream.readUTF();
        return HttpRequestMethod.valueOf(methodName);
    }

    private static List<HttpRequestHeader> readHeaders(DataInputStream stream) throws IOException {

        // See if there are any headers at all.
        int headerCount = stream.readInt();
        checkArgument(headerCount >= 0, "expected: headerCount >= 0, found: %s", headerCount);
        if (headerCount == 0) {
            return Collections.emptyList();
        }

        // Read headers.
        List<HttpRequestHeader> headers = new ArrayList<HttpRequestHeader>(headerCount);
        for (int headerIndex = 0; headerIndex < headerCount; headerIndex++) {
            String name = stream.readUTF();
            String value = stream.readUTF();
            ImmutableHttpRequestHeader header = ImmutableHttpRequestHeader
                    .builder()
                    .name(name)
                    .value(value)
                    .build();
            headers.add(header);
        }
        return headers;

    }

    @Nullable
    private static HttpRequestPayload readPayload(DataInputStream stream) throws IOException {

        // Check if the payload is present.
        boolean hasPayload = stream.readBoolean();
        if (!hasPayload) {
            return null;
        }

        // Read type.
        String type = stream.readUTF();

        // Read missing byte count.
        long missingByteCount = stream.readLong();
        checkArgument(missingByteCount >= 0, "expected: missingByteCount >= 0, found: %s", missingByteCount);

        // Read bytes.
        int byteCount = stream.readInt();
        checkArgument(byteCount >= 0, "expected: byteCount >= 0, found: %s", byteCount);
        byte[] bytes = new byte[byteCount];
        int readByteCount = stream.read(bytes);
        checkArgument(byteCount == readByteCount, "expected: byteCount == readByteCount, found: %s != %s", byteCount, readByteCount);

        return ImmutableHttpRequestPayload
                .builder()
                .type(type)
                .missingByteCount(missingByteCount)
                .bytes(bytes)
                .build();

    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
