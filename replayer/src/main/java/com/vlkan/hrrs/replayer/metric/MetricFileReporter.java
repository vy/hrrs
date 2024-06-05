/*
 * Copyright 2016-2024 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.replayer.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.vlkan.hrrs.replayer.cli.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

public class MetricFileReporter extends Thread implements MetricReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricFileReporter.class);

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();

    private final Config config;

    private final MetricRegistry metricRegistry;

    public MetricFileReporter(Config config, MetricRegistry metricRegistry) {
        this.config = checkNotNull(config, "config");
        this.metricRegistry = checkNotNull(metricRegistry, "metricRegistry");
        setName(createName());
        LOGGER.debug(
                "instantiated (file={}, periodSeconds={})",
                config.getMetricsOutputFile(), config.getMetricsOutputPeriodSeconds());
    }

    private static String createName() {
        String className = MetricFileReporter.class.getSimpleName();
        int instanceId = INSTANCE_COUNTER.getAndIncrement();
        return String.format("%s-%s", className, instanceId);
    }

    @Override
    public void start() {
        LOGGER.debug("starting");
        super.start();
    }

    @Override
    public void run() {
        File file = new File(config.getMetricsOutputFile());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try (PrintStream printStream = new PrintStream(fileOutputStream)) {
                run(printStream);
            }
        } catch (IOException error) {
            String message = String.format("failed opening metric output file (file=%s)", file);
            LOGGER.error(message, error);
        }
    }

    private void run(PrintStream printStream) {
        long sleepPeriodMillis = config.getMetricsOutputPeriodSeconds() * 1000;
        ConsoleReporter consoleReporter = ConsoleReporter
                .forRegistry(metricRegistry)
                .outputTo(printStream)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        while (true) {
            try {
                Thread.sleep(sleepPeriodMillis);
                consoleReporter.report();
            } catch (InterruptedException ignored) {
                interrupt();
                LOGGER.trace("interrupted");
                break;
            }
        }
    }

    @Override
    public void close() {
        LOGGER.debug("closing");
        interrupt();
    }

}
