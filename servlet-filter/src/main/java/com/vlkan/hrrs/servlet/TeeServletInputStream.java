package com.vlkan.hrrs.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeeServletInputStream extends ServletInputStream {

    private final ServletInputStream servletInputStream;

    private final OutputStream outputStream;

    private final long maxByteCount;

    private final AtomicLong byteCountReference = new AtomicLong();

    TeeServletInputStream(ServletInputStream servletInputStream, OutputStream outputStream, long maxByteCount) {
        this.servletInputStream = servletInputStream;
        this.outputStream = outputStream;
        this.maxByteCount = maxByteCount;
    }

    public long getByteCount() {
        return byteCountReference.get();
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
        if (value != -1 && byteCountReference.incrementAndGet() < maxByteCount) {
            outputStream.write(value);
        }
        return value;
    }

}
