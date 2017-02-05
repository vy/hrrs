package com.vlkan.hrrs.replayer.record;

import java.util.concurrent.Callable;

public interface HttpRequestRecordStream {

    void consumeWhile(String inputUri, Callable<Boolean> predicate, HttpRequestRecordStreamConsumer consumer);

}
