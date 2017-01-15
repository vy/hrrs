package com.vlkan.hrrs.replayer.record;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.vlkan.hrrs.replayer.util.Throwables.throwCheckedException;

@Singleton
public class HttpRequestRecordStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordStream.class);

    private final HttpRequestRecordReaderFactory readerFactory;

    private final HttpRequestRecordReaderSourceFactory readerSourceFactory;

    @Inject
    public HttpRequestRecordStream(
            HttpRequestRecordReaderFactory readerFactory,
            HttpRequestRecordReaderSourceFactory readerSourceFactory) {
        this.readerFactory = checkNotNull(readerFactory, "readerFactory");
        this.readerSourceFactory = checkNotNull(readerSourceFactory, "readerSourceFactory");
        LOGGER.debug("instantiated");
    }

    public void consumeWhile(File file, Callable<Boolean> predicate, HttpRequestRecordStreamConsumer consumer) {
        LOGGER.debug("consuming (file={})", file);
        boolean resuming = false;
        do {
            HttpRequestRecordReaderSource readerSource = readerSourceFactory.create(file);
            try {
                HttpRequestRecordReader reader = readerFactory.create(readerSource);
                Iterator<HttpRequestRecord> iterator = reader.read().iterator();
                while ((resuming = predicate.call()) && iterator.hasNext()) {
                    HttpRequestRecord record = iterator.next();
                    consumer.consume(record);
                }
            } catch (Exception error) {
                throwCheckedException(error);
            } finally {
                try {
                    readerSource.close();
                } catch (IOException error) {
                    throwCheckedException(error);
                }
            }
        } while (resuming);
    }

}
