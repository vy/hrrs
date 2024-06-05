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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;

@NotThreadSafe
public class HttpRequestRecordWriterFileTarget implements HttpRequestRecordWriterTarget<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordWriterFileTarget.class);

    private final File file;

    private final Charset charset;

    private final BufferedWriter writer;

    public HttpRequestRecordWriterFileTarget(File file, Charset charset) {
        this.file = Objects.requireNonNull(file, "file");
        this.charset = Objects.requireNonNull(charset, "charset");
        this.writer = createWriter(file, charset);
        LOGGER.trace("instantiated (file={}, charset={})", file, charset);
    }

    private static BufferedWriter createWriter(File file, Charset charset) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
            return new BufferedWriter(outputStreamWriter);
        } catch (IOException error) {
            String message = String.format("failed opening file (file=%s, charset=%s)", file, charset);
            throw new RuntimeException(message, error);
        }
    }

    public File getFile() {
        return file;
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
        return "HttpRequestRecordWriterFileTarget{" +
                "file=" + file +
                ", charset=" + charset +
                '}';
    }

}
