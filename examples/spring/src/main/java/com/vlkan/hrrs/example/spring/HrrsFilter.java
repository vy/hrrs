package com.vlkan.hrrs.example.spring;

import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.hrrs.api.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.api.base64.Base64HttpRequestRecordWriter;
import com.vlkan.hrrs.api.base64.guava.GuavaBase64Encoder;
import com.vlkan.hrrs.api.file.HttpRequestRecordWriterFileTarget;
import com.vlkan.hrrs.servlet.AbstractHrrsFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class HrrsFilter extends AbstractHrrsFilter {

    private final File writerFile;

    private final HttpRequestRecordWriterTarget writerTarget;

    private final HttpRequestRecordWriter writer;

    public HrrsFilter() throws IOException {
        this(File.createTempFile("hrrs-records-", ".csv"));
    }

    public HrrsFilter(File writerFile) {
        this.writerFile = checkNotNull(writerFile, "writerFile");
        this.writerTarget = new HttpRequestRecordWriterFileTarget(writerFile, Base64HttpRequestRecord.CHARSET);
        this.writer = new Base64HttpRequestRecordWriter(writerTarget, GuavaBase64Encoder.INSTANCE);
    }

    public File getWriterFile() {
        return writerFile;
    }

    @Override
    protected HttpRequestRecordWriter getWriter() {
        return writer;
    }

    @Override
    public void destroy() {
        try {
            writerTarget.close();
        } catch (IOException error) {
            throw new RuntimeException("failed closing writer", error);
        }
    }

}
