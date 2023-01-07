/*
 * Copyright 2016-2023 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.replayer.record.HttpRequestRecordStream;
import com.vlkan.hrrs.replayer.record.HttpRequestRecordStreamConsumer;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordReader;
import com.vlkan.hrrs.serializer.base64.JdkBase64Codec;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordReaderFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

public class Base64HttpRequestRecordStream implements HttpRequestRecordStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64HttpRequestRecordStream.class);

    @Override
    public void consumeWhile(URI inputUri, boolean replayOnce, Callable<Boolean> predicate, HttpRequestRecordStreamConsumer consumer) {
        checkNotNull(inputUri, "inputUri");
        checkNotNull(predicate, "predicate");
        checkNotNull(consumer, "consumer");
        LOGGER.debug("consuming (inputUri={})", inputUri);
        boolean resuming;
        do {
            File inputFile = new File(inputUri);
            HttpRequestRecordReaderSource<String> readerSource =
                    new HttpRequestRecordReaderFileSource(inputFile, Base64HttpRequestRecord.CHARSET);
            try {
                HttpRequestRecordReader<String> reader =
                        new Base64HttpRequestRecordReader(readerSource, JdkBase64Codec.INSTANCE);
                Iterator<HttpRequestRecord> iterator = reader.read().iterator();
                while ((resuming = predicate.call()) && iterator.hasNext()) {
                    HttpRequestRecord record = iterator.next();
                    consumer.consume(record);
                }
            } catch (Throwable error) {
                String message = String.format("failed consuming from record reader (inputUri=%s)", inputUri);
                throw new RuntimeException(message, error);
            } finally {
                try {
                    readerSource.close();
                } catch (IOException error) {
                    LOGGER.error("failed closing reader source (inputUri={})", inputUri);
                }
            }
        } while (!replayOnce && resuming);
    }

}
