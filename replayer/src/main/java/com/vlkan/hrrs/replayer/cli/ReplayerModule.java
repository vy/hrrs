package com.vlkan.hrrs.replayer.cli;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vlkan.hrrs.commons.logger.Log4jLoggerLevelAccessor;
import com.vlkan.hrrs.commons.logger.LoggerLevelAccessor;
import com.vlkan.hrrs.replayer.executor.CloseableExecutor;
import com.vlkan.hrrs.replayer.executor.RateLimitedExecutor;
import com.vlkan.hrrs.replayer.http.ApacheHttpRequestRecordReplayer;
import com.vlkan.hrrs.replayer.http.HttpRequestRecordReplayer;
import com.vlkan.hrrs.replayer.jtl.JtlFilePrinter;
import com.vlkan.hrrs.replayer.jtl.JtlNullPrinter;
import com.vlkan.hrrs.replayer.jtl.JtlPrinter;
import com.vlkan.hrrs.replayer.metric.MetricFileReporter;
import com.vlkan.hrrs.replayer.metric.MetricNullReporter;
import com.vlkan.hrrs.replayer.metric.MetricReporter;

import javax.inject.Singleton;
import java.io.FileNotFoundException;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReplayerModule extends AbstractModule {

    private final Config config;

    public ReplayerModule(Config config) {
        this.config = checkNotNull(config, "config");
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        bind(CloseableExecutor.class).to(RateLimitedExecutor.class);
        bind(HttpRequestRecordReplayer.class).to(ApacheHttpRequestRecordReplayer.class);
        bind(LoggerLevelAccessor.class).toInstance(Log4jLoggerLevelAccessor.getInstance());
    }

    @Provides
    @Singleton
    public MetricRegistry provideMetricRegistry() {
        return new MetricRegistry();
    }

    @Provides
    @Singleton
    public JtlPrinter provideJtlPrinter(Config config) throws FileNotFoundException {
        return config.getJtlOutputFile() == null
                ? JtlNullPrinter.getInstance()
                : new JtlFilePrinter(config);
    }

    @Provides
    @Singleton
    public MetricReporter provideMetricReporter(Config config, MetricRegistry metricRegistry) {
        return config.getMetricsOutputFile() == null
                ? MetricNullReporter.getInstance()
                : new MetricFileReporter(config, metricRegistry);
    }

}
