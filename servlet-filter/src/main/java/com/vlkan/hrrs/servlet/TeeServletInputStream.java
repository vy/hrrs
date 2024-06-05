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

package com.vlkan.hrrs.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class TeeServletInputStream extends ServletInputStream {

    private final ServletInputStream servletInputStream;

    private final OutputStream outputStream;

    private final int maxByteCount;

    private volatile int byteCount;

    TeeServletInputStream(ServletInputStream servletInputStream, OutputStream outputStream, int maxByteCount) {
        this.servletInputStream = servletInputStream;
        this.outputStream = outputStream;
        this.maxByteCount = maxByteCount;
        this.byteCount = 0;
    }

    public int getByteCount() {
        return byteCount;
    }

    @Override
    public boolean isFinished() {
        return servletInputStream.isFinished();
    }

    @Override
    public boolean isReady() {
        return servletInputStream.isReady();
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        Objects.requireNonNull(readListener, "readListener");
        servletInputStream.setReadListener(readListener);
    }

    @Override
    public int read() throws IOException {
        int value = servletInputStream.read();
        int localByteCount = byteCount;
        if (value != -1 && localByteCount < maxByteCount) {
            outputStream.write(value);
            localByteCount++;
        }
        byteCount = localByteCount;
        return value;
    }

}
