package com.vlkan.hrrs.replayer.jtl;

public class JtlNullPrinter implements JtlPrinter {

    private static final JtlNullPrinter INSTANCE = new JtlNullPrinter();

    private JtlNullPrinter() {
        // Do nothing.
    }

    public static JtlNullPrinter getInstance() {
        return INSTANCE;
    }

    @Override
    public void print(long timestampMillis, String label, int statusCode, long latency, String threadName) {
        // Do nothing.
    }

    @Override
    public void close() {
        // Do nothing.
    }

}
