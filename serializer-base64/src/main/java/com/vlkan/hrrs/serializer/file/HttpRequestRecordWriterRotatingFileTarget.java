/*
 * Copyright 2016-2024 Volkan Yazıcı
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

package com.vlkan.hrrs.serializer.file;

import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.rfos.RotatingFileOutputStream;
import com.vlkan.rfos.RotationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;

@NotThreadSafe
public class HttpRequestRecordWriterRotatingFileTarget implements HttpRequestRecordWriterTarget<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordWriterRotatingFileTarget.class);

    private final RotationConfig rotationConfig;

    private final Charset charset;

    private final BufferedWriter writer;

    public HttpRequestRecordWriterRotatingFileTarget(RotationConfig rotationConfig, Charset charset) {
        this.rotationConfig = Objects.requireNonNull(rotationConfig, "rotationConfig");
        this.charset = Objects.requireNonNull(charset, "charset");
        this.writer = createWriter(rotationConfig, charset);
        LOGGER.trace("instantiated (file={}, charset={})", rotationConfig.getFile(), charset);
    }

    private static BufferedWriter createWriter(RotationConfig rotationConfig, Charset charset) {
        try {
            OutputStream outputStream = new RotatingFileOutputStream(rotationConfig);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
            return new BufferedWriter(outputStreamWriter);
        } catch (Exception error) {
            String message = String.format("failed opening file (file=%s, charset=%s)", rotationConfig.getFile(), charset);
            throw new RuntimeException(message, error);
        }
    }

    public RotationConfig getRotationConfig() {
        return rotationConfig;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public void write(String value) {
        try {
            writer.write(value);
        } catch (IOException error) {
            String message = String.format("write failure (valueLength=%d)", value.length());
            throw new RuntimeException(message, error);
        }
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException error) {
            throw new RuntimeException("flush failure", error);
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("closing");
        writer.close();
    }

    @Override
    public String toString() {
        return "HttpRequestRecordWriterRotatingFileTarget{" +
                "file=" + rotationConfig.getFile() +
                ", charset=" + charset +
                '}';
    }

}
