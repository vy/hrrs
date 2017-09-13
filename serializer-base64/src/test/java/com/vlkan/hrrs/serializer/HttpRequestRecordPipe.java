package com.vlkan.hrrs.serializer;

import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;

import javax.annotation.Nullable;
import java.io.*;

public class HttpRequestRecordPipe implements HttpRequestRecordReaderSource<String>, HttpRequestRecordWriterTarget<String> {

    private final PipedOutputStream outputStream;

    private final Writer writer;

    private final PipedInputStream inputStream;

    private final BufferedReader reader;

    public HttpRequestRecordPipe(int pipeSize) {
        this.outputStream = new PipedOutputStream();
        this.writer = new OutputStreamWriter(outputStream);
        try {
            this.inputStream = new PipedInputStream(outputStream, pipeSize);
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
