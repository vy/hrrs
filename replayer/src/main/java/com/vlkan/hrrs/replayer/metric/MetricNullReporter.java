package com.vlkan.hrrs.replayer.metric;

public class MetricNullReporter implements MetricReporter {

    private static final MetricNullReporter INSTANCE = new MetricNullReporter();

    private MetricNullReporter() {
        // Do nothing.
    }

    public static MetricNullReporter getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        // Do nothing.
    }

    @Override
    public void close() {
        // Do nothing.
    }

}
