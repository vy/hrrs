package com.vlkan.hrrs.replayer.jtl;

import com.vlkan.hrrs.replayer.cli.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class JtlFilePrinter implements JtlPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JtlFilePrinter.class);

    private final PrintStream printStream;

    public JtlFilePrinter(Config config) throws FileNotFoundException {
        checkNotNull(config, "config");
        FileOutputStream outputStream = new FileOutputStream(config.getJtlOutputFile());
        this.printStream = new PrintStream(outputStream);
        printStream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        printStream.println("<testResults version=\"1.2\">");
        LOGGER.debug("instantiated (file={})", config.getJtlOutputFile());
    }

    @Override
    public void print(long timestampMillis, String label, int statusCode, long latency, String threadName) {
        String entry = String.format(
                "<httpSample t=\"%d\" lt=\"%d\" ts=\"%d\" s=\"%s\" rc=\"%d\" lb=\"%s\" tn=\"%s\"/>",
                latency,
                latency,
                timestampMillis,
                (statusCode & 200) == 200,
                statusCode,
                label,
                threadName);
        synchronized (this) {
            printStream.println(entry);
        }
    }

    @Override
    public synchronized void close() {
        LOGGER.debug("closing");
        printStream.println("</testResults>");
        printStream.close();
    }

}
