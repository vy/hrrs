package com.vlkan.hrrs.replayer.cli;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vlkan.hrrs.api.HttpRequestRecord;
import com.vlkan.hrrs.replayer.executor.CloseableExecutor;
import com.vlkan.hrrs.replayer.http.HttpRequestRecordReplayer;
import com.vlkan.hrrs.replayer.logger.LoggerLevelAccessor;
import com.vlkan.hrrs.replayer.metric.MetricReporter;
import com.vlkan.hrrs.replayer.record.HttpRequestRecordStream;
import com.vlkan.hrrs.replayer.record.HttpRequestRecordStreamConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class Main implements Runnable, Closeable, HttpRequestRecordStreamConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private final Config config;

    private final CloseableExecutor closeableExecutor;

    private final HttpRequestRecordStream recordStream;

    private final HttpRequestRecordReplayer recordReplayer;

    private final MetricRegistry metricRegistry;

    private final MetricReporter metricReporter;

    private volatile boolean closed = false;

    @Inject
    public Main(
            Config config,
            CloseableExecutor closeableExecutor,
            HttpRequestRecordStream recordStream,
            HttpRequestRecordReplayer recordReplayer,
            MetricRegistry metricRegistry,
            MetricReporter metricReporter) {

        // Check arguments.
        checkNotNull(config, "config");
        checkNotNull(closeableExecutor, "closeableExecutor");
        checkNotNull(recordStream, "recordStream");
        checkNotNull(recordReplayer, "recordReplayer");
        checkNotNull(metricRegistry, "metricRegistry");
        checkNotNull(metricReporter, "metricReporter");

        // Set class fields.
        this.config = config;
        this.closeableExecutor = closeableExecutor;
        this.recordStream = recordStream;
        this.recordReplayer = recordReplayer;
        this.metricRegistry = metricRegistry;
        this.metricReporter = metricReporter;

    }

    @Override
    public void run() {
        LOGGER.debug("starting to replay");
        File inputFile = new File(config.getInputFile());
        Callable<Boolean> consumptionPredicate = createConsumptionPredicate();
        metricReporter.start();
        recordStream.consumeWhile(inputFile, consumptionPredicate, this);
        reportMetric();
    }

    private Callable<Boolean> createConsumptionPredicate() {
        return new Callable<Boolean>() {

            private final Stopwatch stopwatch = Stopwatch.createStarted();

            private final long totalDurationMillis = config.getTotalDurationSeconds() * 1000L;

            private final AtomicLong recordCounter = new AtomicLong();

            private final RateLimiter progressReportRateLimiter = RateLimiter.create(10);

            @Override
            public Boolean call() {
                long durationMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                reportProgress(durationMillis);
                return !closed && durationMillis < totalDurationMillis;
            }

            private void reportProgress(long durationMillis) {
                if (progressReportRateLimiter.tryAcquire()) {
                    float durationPercentage = 100.0f * durationMillis / totalDurationMillis;
                    long recordCount = recordCounter.incrementAndGet();
                    System.out.format(
                            "\r%.1f%% (durationMillis=%d, recordCount=%d)",
                            durationPercentage, durationMillis, recordCount);
                }
            }

        };
    }

    private void reportMetric() {
        System.out.println();
        System.out.println();
        ConsoleReporter
                .forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
                .report();
    }

    @Override
    public void consume(final HttpRequestRecord record) {
        closeableExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordReplayer.replay(record);
            }
        });
    }

    @Override
    public void close() throws IOException {
        closeableExecutor.close();
        metricReporter.close();
        closed = true;
    }

    public static void main(String[] args) throws IOException {
        Config config = Config.of(args);
        config.dump();
        MainModule mainModule = new MainModule(config);
        Injector injector = Guice.createInjector(mainModule);
        LoggerLevelAccessor loggerLevelAccessor = injector.getInstance(LoggerLevelAccessor.class);
        applyLoggerLevelSpecs(config, loggerLevelAccessor);
        Main main = injector.getInstance(Main.class);
        try {
            main.run();
        } finally {
            main.close();
        }
    }

    private static void applyLoggerLevelSpecs(Config config, LoggerLevelAccessor loggerLevelAccessor) {
        String loggerLevelSpecs = config.getLoggerLevelSpecs();
        if (loggerLevelSpecs != null) {
            for (String loggerNameAndLoggerLevel : loggerLevelSpecs.split(",")) {
                String[] fields = loggerNameAndLoggerLevel.split("=", 2);
                String loggerName = fields[0];
                String loggerLevel = fields[1];
                if ("*".equals(loggerName)) {
                    loggerLevelAccessor.setRootLevel(loggerLevel);
                } else {
                    loggerLevelAccessor.setLevel(loggerName, loggerLevel);
                }
            }
        }
    }

}
