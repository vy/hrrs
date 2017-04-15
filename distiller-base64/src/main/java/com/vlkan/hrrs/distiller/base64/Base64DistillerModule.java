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
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Decoder;
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Encoder;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordReaderFileSource;
import com.vlkan.hrrs.serializer.file.HttpRequestRecordWriterFileTarget;

import java.io.File;
import java.net.URI;

public class Base64DistillerModule extends DistillerModule {

    public Base64DistillerModule(Config config) {
        super(config);
    }

    @Provides
    public HttpRequestRecordReader provideReader(Config config) {
        URI inputUri = config.getInputUri();
        File inputFile = new File(inputUri);
        HttpRequestRecordReaderSource<String> readerSource = new HttpRequestRecordReaderFileSource(inputFile, Base64HttpRequestRecord.CHARSET);
        return new Base64HttpRequestRecordReader(readerSource, GuavaBase64Decoder.getInstance());
    }

    @Provides
    public HttpRequestRecordWriter provideWriter(Config config) {
        URI outputUri = config.getOutputUri();
        File outputFile = new File(outputUri);
        HttpRequestRecordWriterFileTarget writerTarget = new HttpRequestRecordWriterFileTarget(outputFile, Base64HttpRequestRecord.CHARSET);
        return new Base64HttpRequestRecordWriter(writerTarget, GuavaBase64Encoder.getInstance());
    }

}
