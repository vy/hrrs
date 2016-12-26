package com.vlkan.hrrs.api.util;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;

public class CountingBufferedFileWriter extends BufferedWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountingBufferedFileWriter.class);

    private final AtomicLong byteCounter = new AtomicLong(0);

    private final File file;

    private final Writer writer;

    private final BufferedWriterListener listener;

    private CountingBufferedFileWriter(File file, Writer writer, BufferedWriterListener listener) {
        super(writer);
        this.file = file;
        this.writer = writer;
        this.listener = listener;
    }

    public CountingBufferedFileWriter(File file, BufferedWriterListener listener) {
        this(file, createFileWriter(checkNotNull(file, "file")), checkNotNull(listener, "listener"));
    }

    private static Writer createFileWriter(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return new OutputStreamWriter(fileOutputStream, "US-ASCII");
        } catch (IOException error) {
            String message = String.format("failed opening (file=%s)", file);
            throw new RuntimeException(message, error);
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        super.write(cbuf, off, len);
        long byteCount = byteCounter.addAndGet(len);
        listener.onBufferedWrite(byteCount);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void close() {
        closeSuper();
        closeWriter();
    }

    private void closeWriter() {
        try {
            writer.close();     // Close the file stream.
        } catch (IOException error) {
            LOGGER.error("failed closing the writer", error);
        }
    }

    private void closeSuper() {
        try {
            super.close();      // Flush the buffered writer.
        } catch (IOException error) {
            LOGGER.error("failed closing the parent writer", error);
        }
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("file", file)
                .add("byteCount", byteCounter.get())
                .toString();
    }

}
