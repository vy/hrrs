package com.vlkan.hrrs.replayer.record;

import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordReaderFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class HttpRequestRecordReaderSourceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordReaderSourceFactory.class);

    public HttpRequestRecordReaderSourceFactory() {
        LOGGER.debug("instantiated");
    }

    public HttpRequestRecordReaderSource<String> create(File file) {
        checkNotNull(file, "file");
        return new HttpRequestRecordReaderFileSource(file, Base64HttpRequestRecord.CHARSET);
    }

}
