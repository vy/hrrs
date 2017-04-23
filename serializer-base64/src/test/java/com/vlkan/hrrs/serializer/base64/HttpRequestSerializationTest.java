package com.vlkan.hrrs.serializer.base64;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.api.HttpRequestRecordReader;
import com.vlkan.hrrs.api.HttpRequestRecordWriter;
import com.vlkan.hrrs.serializer.HttpRequestPayloadGenerator;
import com.vlkan.hrrs.serializer.HttpRequestRecordGenerator;
import com.vlkan.hrrs.serializer.HttpRequestRecordPipe;
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Decoder;
import com.vlkan.hrrs.serializer.base64.guava.GuavaBase64Encoder;
import org.junit.runner.RunWith;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class HttpRequestSerializationTest {

    @Property
    public void should_write_and_read(@From(HttpRequestRecordGenerator.class) HttpRequestRecord record) {
        HttpRequestRecordPipe pipe = new HttpRequestRecordPipe(HttpRequestPayloadGenerator.MAX_BYTE_COUNT * 8);
        HttpRequestRecordReader<String> reader = new Base64HttpRequestRecordReader(pipe, GuavaBase64Decoder.getInstance());
        HttpRequestRecordWriter<String> writer = new Base64HttpRequestRecordWriter(pipe, GuavaBase64Encoder.getInstance());
        writer.write(record);
        pipe.flush();
        Iterator<HttpRequestRecord> iterator = reader.read().iterator();
        assertThat(iterator.hasNext()).isTrue();
        HttpRequestRecord readRecord = iterator.next();
        assertThat(readRecord).isEqualTo(record);
    }

}
