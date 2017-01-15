package com.vlkan.hrrs.replayer.record;

import com.vlkan.hrrs.api.HttpRequestRecord;

public interface HttpRequestRecordStreamConsumer {

    void consume(HttpRequestRecord record);

}
