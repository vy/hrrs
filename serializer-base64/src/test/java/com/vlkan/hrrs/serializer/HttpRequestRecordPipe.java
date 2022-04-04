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

package com.vlkan.hrrs.serializer;

import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;

import javax.annotation.Nullable;
import java.io.*;

public class HttpRequestRecordPipe implements HttpRequestRecordReaderSource<String>, HttpRequestRecordWriterTarget<String> {

    private final Writer writer;

    private final BufferedReader reader;

    public HttpRequestRecordPipe(int pipeSize) {
        PipedOutputStream outputStream = new PipedOutputStream();
        this.writer = new OutputStreamWriter(outputStream);
        try {
            PipedInputStream inputStream = new PipedInputStream(outputStream, pipeSize);
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException error) {
            throw new RuntimeException("failed creating piped input stream", error);
        }
    }

    @Override
    public void write(String value) {
        try {
            writer.write(value);
        } catch (IOException error) {
            throw new RuntimeException("write failure", error);
        }
    }

    @Nullable
    @Override
    public String read() {
        try {
            return reader.readLine();
        } catch (IOException error) {
            throw new RuntimeException("read failure", error);
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
        writer.close();
        reader.close();
    }

}
