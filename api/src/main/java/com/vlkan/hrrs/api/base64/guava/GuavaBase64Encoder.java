package com.vlkan.hrrs.api.base64.guava;

import com.vlkan.hrrs.api.base64.Base64Encoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuavaBase64Encoder implements Base64Encoder {

    private static final GuavaBase64Encoder INSTANCE = new GuavaBase64Encoder();

    private GuavaBase64Encoder() {
        // Do nothing.
    }

    public static GuavaBase64Encoder getInstance() {
        return INSTANCE;
    }

    @Override
    public String encode(byte[] bytes) {
        checkNotNull(bytes, "bytes");
        return GuavaBase64.BASE_ENCODING.encode(bytes);
    }

}
