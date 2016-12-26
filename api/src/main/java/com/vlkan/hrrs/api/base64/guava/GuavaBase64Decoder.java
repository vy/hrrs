package com.vlkan.hrrs.api.base64.guava;

import com.vlkan.hrrs.api.base64.Base64Decoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuavaBase64Decoder implements Base64Decoder {

    public static final GuavaBase64Decoder INSTANCE = new GuavaBase64Decoder();

    private GuavaBase64Decoder() {
        // Do nothing.
    }

    @Override
    public byte[] decode(String encodedBytes) {
        checkNotNull(encodedBytes, "encodedBytes");
        return GuavaBase64.BASE_ENCODING.decode(encodedBytes);
    }

}
