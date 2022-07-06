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

package com.vlkan.hrrs.serializer.file;

import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

@NotThreadSafe
public class HttpRequestRecordReaderFileSource implements HttpRequestRecordReaderSource<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordReaderFileSource.class);

    private final File file;

    private final Charset charset;

    private final BufferedReader reader;

    public HttpRequestRecordReaderFileSource(File file, Charset charset) {
        this.file = Objects.requireNonNull(file, "file");
        this.charset = Objects.requireNonNull(charset, "charset");
        this.reader = createReader(file, charset);
        LOGGER.trace("instantiated (file={}, charset={})", file, charset);
    }

    private static BufferedReader createReader(File file, Charset charset) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                InputStream readerInputStream = isGzipped(file)
                        ? new GZIPInputStream(fileInputStream)
                        : fileInputStream;
                InputStreamReader inputStreamReader = new InputStreamReader(readerInputStream, charset);
                return new BufferedReader(inputStreamReader);
            } catch (IOException error) {
                fileInputStream.close();
                throw error;
            }
        } catch (IOException error) {
            String message = String.format("failed opening file (file=%s, charset=%s)", file, charset);
            throw new RuntimeException(message, error);
        }
    }

    private static boolean isGzipped(File file) {
        return file.getAbsolutePath().matches(".*\\.[gG][zZ]$");
    }

    public File getFile() {
        return file;
    }

    public Charset getCharset() {
        return charset;
    }

    @Nullable
    @Override
    public String read() {
        try {
            return reader.readLine();
        } catch (IOException error) {
            String message = String.format("failed reading line (file=%s)", file);
            throw new RuntimeException(message, error);
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("closing");
        reader.close();
    }

    @Override
    public String toString() {
        return "HttpRequestRecordReaderFileSource{" +
                "file=" + file +
                ", charset=" + charset +
                '}';
    }

}
