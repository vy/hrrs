package com.vlkan.hrrs.replayer.jtl;

import java.io.Closeable;

public interface JtlPrinter extends Closeable {

    void print(long timestampMillis, String label, int statusCode, long latency, String threadName);

}
