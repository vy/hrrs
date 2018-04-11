package com.vlkan.hrrs.servlet.base64;

import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.api.HttpRequestRecordWriterTarget;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordWriter;
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Encoder;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordWriterRotatingFileTarget;
import com.vlkan.hrrs.servlet.HrrsFilter;
import com.vlkan.rfos.RotationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HrrsFilter extends HrrsFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64HrrsFilter.class);

    private final HttpRequestRecordWriterTarget<String> writerTarget;

    private final HttpRequestRecordWriter<String> writer;

    public Base64HrrsFilter(RotationConfig rotationConfig) {
        checkNotNull(rotationConfig, "rotationConfig");
        this.writerTarget = new HttpRequestRecordWriterRotatingFileTarget(rotationConfig, Base64HttpRequestRecord.CHARSET);
        this.writer = new Base64HttpRequestRecordWriter(writerTarget, GuavaBase64Encoder.getInstance());
    }

    public HttpRequestRecordWriterTarget<String> getWriterTarget() {
        return writerTarget;
    }

    @Override
    protected HttpRequestRecordWriter<String> getWriter() {
        return writer;
    }

    @Override
    public void destroy() {
        try {
            writerTarget.close();
        } catch (IOException error) {
            LOGGER.error("failed closing writer", error);
        }
    }

}
