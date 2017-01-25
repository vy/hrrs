package com.vlkan.hrrs.replayer.record;

import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordReader;
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class HttpRequestRecordReaderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordReaderFactory.class);

    public HttpRequestRecordReaderFactory() {
        LOGGER.debug("instantiated");
    }

    public HttpRequestRecordReader create(HttpRequestRecordReaderSource<String> source) {
        checkNotNull(source, "source");
        return new Base64HttpRequestRecordReader(source, GuavaBase64Decoder.getInstance());
    }

}
