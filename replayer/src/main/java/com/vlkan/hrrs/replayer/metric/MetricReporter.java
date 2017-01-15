package com.vlkan.hrrs.replayer.metric;

import java.io.Closeable;

public interface MetricReporter extends Closeable {

    void start();

}
