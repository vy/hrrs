package com.vlkan.hrrs.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(readListener, "readListener");
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
