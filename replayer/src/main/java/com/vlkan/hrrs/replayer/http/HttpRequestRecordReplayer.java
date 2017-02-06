package com.vlkan.hrrs.replayer.http;

import com.vlkan.hrrs.api.HttpRequestRecord;

import java.io.Closeable;

public interface HttpRequestRecordReplayer extends Closeable {

    void replay(HttpRequestRecord record);

}
