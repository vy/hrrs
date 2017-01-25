package com.vlkan.hrrs.serializer;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.vlkan.hrrs.api.HttpRequestHeader;
import com.vlkan.hrrs.api.ImmutableHttpRequestHeader;

public class HttpRequestHeaderGenerator extends Generator<HttpRequestHeader> {

    public HttpRequestHeaderGenerator() {
        super(HttpRequestHeader.class);
    }

    @Override
    public HttpRequestHeader generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        int id = Math.abs(sourceOfRandomness.nextInt());
        String name = String.format("name-%d", id);
        String value = String.format("value-%d", id);
        return ImmutableHttpRequestHeader
                .builder()
                .name(name)
                .value(value)
                .build();
    }

}
