package com.vlkan.hrrs.serializer;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.vlkan.hrrs.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpRequestRecordGenerator extends Generator<HttpRequestRecord> {

    private static final int MAX_GROUP_COUNT = 10;

    private static final int MAX_HEADER_COUNT = 10;

    public HttpRequestRecordGenerator() {
        super(HttpRequestRecord.class);
    }

    @Override
    public HttpRequestRecord generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {

        // Populate fields.
        String id = Long.toString(sourceOfRandomness.nextLong(), Character.MAX_RADIX);
        int groupId = Math.abs(id.hashCode()) % MAX_GROUP_COUNT;
        String groupName = String.format("group-%d", groupId);
        long timestampMillis = System.currentTimeMillis();
        String uri = String.format("/hello/%s?id=%s", groupName, id);
        HttpRequestMethod method = gen().type(HttpRequestMethod.class).generate(sourceOfRandomness, generationStatus);
        List<HttpRequestHeader> headers = generateHeaders(sourceOfRandomness, generationStatus);
        HttpRequestPayload payload = gen().make(HttpRequestPayloadGenerator.class).generate(sourceOfRandomness, generationStatus);

        // Create the record.
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

    private List<HttpRequestHeader> generateHeaders(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        int headerCount = sourceOfRandomness.nextInt(MAX_HEADER_COUNT);
        if (headerCount < 1) {
            return Collections.emptyList();
        }
        HttpRequestHeaderGenerator headerGenerator = gen().make(HttpRequestHeaderGenerator.class);
        List<HttpRequestHeader> headers = new ArrayList<HttpRequestHeader>(headerCount);
        for (int headerIndex = 0; headerIndex < headerCount; headerIndex++) {
            HttpRequestHeader header = headerGenerator.generate(sourceOfRandomness, generationStatus);
            headers.add(header);
        }
        return headers;
    }

}
