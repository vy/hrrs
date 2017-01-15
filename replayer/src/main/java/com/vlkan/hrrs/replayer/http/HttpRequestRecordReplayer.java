package com.vlkan.hrrs.replayer.http;

import com.vlkan.hrrs.api.HttpRequestRecord;

public interface HttpRequestRecordReplayer {

    void replay(HttpRequestRecord record);

}
