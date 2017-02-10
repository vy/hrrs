package com.vlkan.hrrs.serializer;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.ImmutableHttpRequestPayload;

public class HttpRequestPayloadGenerator extends Generator<HttpRequestPayload> {

    public static final int MAX_BYTE_COUNT = 1024 * 128;      // 128 KB

    public HttpRequestPayloadGenerator() {
        super(HttpRequestPayload.class);
    }

    @Override
    public HttpRequestPayload generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        long missingByteCount = sourceOfRandomness.nextLong(0, MAX_BYTE_COUNT);
        int byteCount = sourceOfRandomness.nextInt(0, MAX_BYTE_COUNT);
        byte[] bytes = new byte[byteCount];
        sourceOfRandomness.nextBytes(bytes);
        return ImmutableHttpRequestPayload
                .builder()
                .missingByteCount(missingByteCount)
                .bytes(bytes)
                .build();
    }

}
