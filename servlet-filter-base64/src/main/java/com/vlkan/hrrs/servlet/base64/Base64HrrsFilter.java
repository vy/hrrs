/*
 * Copyright 2016-2023 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.servlet.base64;

import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordWriter;
import com.vlkan.hrrs.serializer.base64.JdkBase64Codec;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordWriterRotatingFileTarget;
import com.vlkan.hrrs.servlet.HrrsFilter;
import com.vlkan.rfos.RotationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class Base64HrrsFilter extends HrrsFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64HrrsFilter.class);

    private final HttpRequestRecordWriterTarget<String> writerTarget;

    private final HttpRequestRecordWriter<String> writer;

    public Base64HrrsFilter(RotationConfig rotationConfig) {
        Objects.requireNonNull(rotationConfig, "rotationConfig");
        this.writerTarget = new HttpRequestRecordWriterRotatingFileTarget(rotationConfig, Base64HttpRequestRecord.CHARSET);
        this.writer = new Base64HttpRequestRecordWriter(writerTarget, JdkBase64Codec.INSTANCE);
    }

    public HttpRequestRecordWriterTarget<String> getWriterTarget() {
        return writerTarget;
    }

    @Override
    protected HttpRequestRecordWriter<String> getWriter() {
        return writer;
    }

    @Override
    public void destroy() {
        try {
            writerTarget.close();
        } catch (IOException error) {
            LOGGER.error("failed closing writer", error);
        }
    }

}
