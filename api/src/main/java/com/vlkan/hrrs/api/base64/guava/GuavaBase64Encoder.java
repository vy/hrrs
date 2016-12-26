package com.vlkan.hrrs.api.base64.guava;

import com.vlkan.hrrs.api.base64.Base64Encoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuavaBase64Encoder implements Base64Encoder {

    public static final GuavaBase64Encoder INSTANCE = new GuavaBase64Encoder();

    private GuavaBase64Encoder() {
        // Do nothing.
    }

    @Override
    public String encode(byte[] bytes) {
        checkNotNull(bytes, "bytes");
        return GuavaBase64.BASE_ENCODING.encode(bytes);
    }

}
