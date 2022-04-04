/*
 * Copyright 2016-2022 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.serializer.base64.guava;

import com.vlkan.hrrs.serializer.base64.Base64Decoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuavaBase64Decoder implements Base64Decoder {

    private static final GuavaBase64Decoder INSTANCE = new GuavaBase64Decoder();

    private GuavaBase64Decoder() {
        // Do nothing.
    }

    public static GuavaBase64Decoder getInstance() {
        return INSTANCE;
    }

    @Override
    public byte[] decode(String encodedBytes) {
        checkNotNull(encodedBytes, "encodedBytes");
        return GuavaBase64.BASE_ENCODING.decode(encodedBytes);
    }

}
