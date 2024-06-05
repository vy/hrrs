/*
 * Copyright 2016-2024 Volkan Yazıcı
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

package com.vlkan.hrrs.distiller.base64;

import com.google.inject.Provides;
import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordReaderSource;
import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.distiller.cli.Config;
import com.vlkan.hrrs.distiller.cli.DistillerModule;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecord;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordReader;
import com.vlkan.hrrs.serializer.base64.Base64HttpRequestRecordWriter;
import com.vlkan.hrrs.serializer.base64.JdkBase64Codec;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordReaderFileSource;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordWriterFileTarget;

import java.io.File;
import java.net.URI;

public class Base64DistillerModule extends DistillerModule {

    public Base64DistillerModule(Config config) {
        super(config);
    }

    @Provides
    public HttpRequestRecordReader<?> provideReader(Config config) {
        URI inputUri = config.getInputUri();
        File inputFile = new File(inputUri);
        HttpRequestRecordReaderSource<String> readerSource = new HttpRequestRecordReaderFileSource(inputFile, Base64HttpRequestRecord.CHARSET);
        return new Base64HttpRequestRecordReader(readerSource, JdkBase64Codec.INSTANCE);
    }

    @Provides
    public HttpRequestRecordWriter<?> provideWriter(Config config) {
        URI outputUri = config.getOutputUri();
        File outputFile = new File(outputUri);
        HttpRequestRecordWriterFileTarget writerTarget = new HttpRequestRecordWriterFileTarget(outputFile, Base64HttpRequestRecord.CHARSET);
        return new Base64HttpRequestRecordWriter(writerTarget, JdkBase64Codec.INSTANCE);
    }

}
