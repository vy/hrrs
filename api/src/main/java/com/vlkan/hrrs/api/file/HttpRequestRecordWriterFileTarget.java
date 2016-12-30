package com.vlkan.hrrs.api.file;

import com.google.common.base.MoreObjects;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

@NotThreadSafe
public class HttpRequestRecordWriterFileTarget implements HttpRequestRecordWriterTarget {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordWriterFileTarget.class);

    private final File file;

    private final Charset charset;

    private final BufferedWriter writer;

    public HttpRequestRecordWriterFileTarget(File file, Charset charset) {
        this.file = checkNotNull(file, "file");
        this.charset = checkNotNull(charset, "charset");
        this.writer = createWriter(file, charset);
        LOGGER.trace("instantiated (file={}, charset={})", file, charset);
    }

    private static BufferedWriter createWriter(File file, Charset charset) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
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
    public void close() throws IOException {
        LOGGER.trace("closing");
        writer.close();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("file", file)
                .add("charset", charset)
                .toString();
    }

}
