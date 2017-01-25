package com.vlkan.hrrs.serializer;

import com.google.common.net.MediaType;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.ImmutableHttpRequestPayload;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class HttpRequestPayloadGenerator extends Generator<HttpRequestPayload> {

    public static final int MAX_BYTE_COUNT = 1024 * 128;      // 128 KB

    private static Set<String> MEDIA_TYPES = populateMediaTypes();

    public HttpRequestPayloadGenerator() {
        super(HttpRequestPayload.class);
    }

    private static Set<String> populateMediaTypes() {
        Set<String> mediaTypes = new HashSet<String>();
        for (Field field : MediaType.class.getDeclaredFields()) {
            if (MediaType.class.equals(field.getType())) {
                try {
                    MediaType mediaType = (MediaType) field.get(null);
                    mediaTypes.add(mediaType.toString());
                } catch (IllegalAccessException error) {
                    String message = String.format("failed getting media type (field=%s)", field.getName());
                    throw new RuntimeException(message, error);
                }
            }
        }
        return mediaTypes;
    }

    @Override
    public HttpRequestPayload generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        String type = sourceOfRandomness.choose(MEDIA_TYPES);
        long missingByteCount = sourceOfRandomness.nextLong(0, MAX_BYTE_COUNT);
        int byteCount = sourceOfRandomness.nextInt(0, MAX_BYTE_COUNT);
        byte[] bytes = new byte[byteCount];
        sourceOfRandomness.nextBytes(bytes);
        return ImmutableHttpRequestPayload
                .builder()
                .type(type)
                .missingByteCount(missingByteCount)
                .bytes(bytes)
                .build();
    }

}
