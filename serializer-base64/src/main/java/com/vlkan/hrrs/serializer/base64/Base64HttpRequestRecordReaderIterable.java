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

package com.vlkan.hrrs.serializer.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;

import java.util.Iterator;
import java.util.Objects;

public class Base64HttpRequestRecordReaderIterable implements Iterable<HttpRequestRecord> {

    private final HttpRequestRecordReaderSource<String> source;

    private final Base64Decoder decoder;

    Base64HttpRequestRecordReaderIterable(HttpRequestRecordReaderSource<String> source, Base64Decoder decoder) {
        this.source = Objects.requireNonNull(source, "source");
        this.decoder = Objects.requireNonNull(decoder, "decoder");
    }

    @Override
    public Iterator<HttpRequestRecord> iterator() {
        return new Base64HttpRequestRecordReaderIterator(source, decoder);
    }

}
