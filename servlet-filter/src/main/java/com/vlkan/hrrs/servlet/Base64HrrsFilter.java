package com.vlkan.hrrs.servlet;

import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.hrrs.api.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.api.base64.Base64HttpRequestRecordWriter;
import com.vlkan.hrrs.api.base64.guava.GuavaBase64Encoder;
import com.vlkan.hrrs.api.file.HttpRequestRecordWriterFileTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HrrsFilter extends HrrsFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64HrrsFilter.class);

    private final File writerTargetFile;

    private final HttpRequestRecordWriterTarget writerTarget;

    private final HttpRequestRecordWriter writer;

    public Base64HrrsFilter(File writerTargetFile) {
        this.writerTargetFile = checkNotNull(writerTargetFile, "writerTargetFile");
        this.writerTarget = new HttpRequestRecordWriterFileTarget(writerTargetFile, Base64HttpRequestRecord.CHARSET);
        this.writer = new Base64HttpRequestRecordWriter(writerTarget, GuavaBase64Encoder.getInstance());
    }

    public File getWriterTargetFile() {
        return writerTargetFile;
    }

    public HttpRequestRecordWriterTarget getWriterTarget() {
        return writerTarget;
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
            String message = String.format("failed closing writer (writerTargetFile=%s)", writerTargetFile);
            LOGGER.error(message, error);
        }
    }

}
