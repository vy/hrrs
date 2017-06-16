package com.vlkan.hrrs.replayer.record;

import java.net.URI;
import java.util.concurrent.Callable;

public interface HttpRequestRecordStream {

    void consumeWhile(URI inputUri, boolean replayOnce, Callable<Boolean> predicate, HttpRequestRecordStreamConsumer consumer);

}
