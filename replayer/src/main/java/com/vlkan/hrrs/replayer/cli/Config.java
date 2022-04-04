/*
 * Copyright 2016-2022 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.replayer.cli;

import com.beust.jcommander.Parameter;
import com.vlkan.hrrs.commons.jcommander.JCommanderConfig;
import com.vlkan.hrrs.commons.jcommander.JCommanderConfigs;
import com.vlkan.hrrs.commons.jcommander.validator.*;
import com.vlkan.hrrs.replayer.http.ApacheHttpClientRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Config implements JCommanderConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    @Parameter(
            names = {"--localAddress", "-l"},
            validateWith = InetAddressValidator.class,
            description = "address to bind to when making outgoing connections")
    private String localAddress;

    public String getLocalAddress() {
        return localAddress;
    }

    @Parameter(
            names = {"--targetHost", "-th"},
            required = true,
            description = "remote HTTP server host")
    private String targetHost;

    public String getTargetHost() {
        return targetHost;
    }

    @Parameter(
            names = {"--targetPort", "-tp"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "remote HTTP server port")
    private int targetPort = 80;

    public int getTargetPort() {
        return targetPort;
    }

    @Parameter(
            names = {"--maxRequestCountPerSecond", "-r"},
            validateWith = NonZeroPositiveDoubleValidator.class,
            description = "number of concurrent requests per second")
    private double maxRequestCountPerSecond = 1;

    public double getMaxRequestCountPerSecond() {
        return maxRequestCountPerSecond;
    }

    @Parameter(
            names = {"--threadCount", "-n"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "HTTP request worker pool size")
    private int threadCount = 2;

    public int getThreadCount() {
        return threadCount;
    }

    @Parameter(
            names = {"--requestTimeoutSeconds", "-t"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "HTTP request connect/write/read timeout in seconds")
    private long requestTimeoutSeconds = 10;

    public long getRequestTimeoutSeconds() {
        return requestTimeoutSeconds;
    }

    @Parameter(
            names = {"--rampUpDurationSeconds", "-d"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "ramp up duration in seconds to reach to the maximum number of requests")
    private long rampUpDurationSeconds = 1;

    public long getRampUpDurationSeconds() {
        return rampUpDurationSeconds;
    }

    @Parameter(
            names = {"--totalDurationSeconds", "-D"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "total run duration in seconds")
    private long totalDurationSeconds = 10;

    public long getTotalDurationSeconds() {
        return totalDurationSeconds;
    }

    @Parameter(
            names = {"--replayOnce", "-1"},
            description = "exit once all the records are replayed")
    private boolean replayOnce = false;

    public boolean isReplayOnce() {
        return replayOnce;
    }

    @Parameter(
            names = {"--inputUri", "-i"},
            validateWith = UriValidator.class,
            description = "input URI for HTTP records",
            required = true)
    private URI inputUri;

    public URI getInputUri() {
        return inputUri;
    }

    @Parameter(
            names = {"--jtlOutputFile", "-oj"},
            validateWith = WritableFileValidator.class,
            description = "Apache JMeter JTL output file for test results")
    private String jtlOutputFile;

    public String getJtlOutputFile() {
        return jtlOutputFile;
    }

    @Parameter(
            names = {"--metricsOutputFile", "-om"},
            validateWith = WritableFileValidator.class,
            description = "output file to dump Dropwizard metrics")
    private String metricsOutputFile;

    public String getMetricsOutputFile() {
        return metricsOutputFile;
    }

    @Parameter(
            names = {"--metricsOutputPeriodSeconds", "-mp"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "Dropwizard metrics report frequency in seconds")
    private int metricsOutputPeriodSeconds = 10;

    public int getMetricsOutputPeriodSeconds() {
        return metricsOutputPeriodSeconds;
    }

    @Parameter(
            names = {"--loggerLevelSpecs", "-L"},
            validateWith = LoggerLevelSpecsValidator.class,
            description = "comma-separated list of loggerName=loggerLevel pairs")
    private String loggerLevelSpecs = "*=warn,com.vlkan.hrrs=info";

    public String getLoggerLevelSpecs() {
        return loggerLevelSpecs;
    }

    @Parameter(
            names = {"--redirectStrategy", "-rs"},
            description = "redirect strategy")
    private ApacheHttpClientRedirectStrategy redirectStrategy = ApacheHttpClientRedirectStrategy.DEFAULT;

    public ApacheHttpClientRedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    @Parameter(
            names = {"--help", "-h"},
            help = true,
            description = "display this help and exit")
    private boolean help;

    @Override
    public boolean isHelp() {
        return help;
    }

    public void dump() {
        LOGGER.debug("localAddress={}", localAddress);
        LOGGER.debug("targetHost={}", targetHost);
        LOGGER.debug("targetPort={}", targetPort);
        LOGGER.debug("maxRequestCountPerSecond={}", maxRequestCountPerSecond);
        LOGGER.debug("threadCount={}", threadCount);
        LOGGER.debug("requestTimeoutSeconds={}", requestTimeoutSeconds);
        LOGGER.debug("rampUpDurationSeconds={}", rampUpDurationSeconds);
        LOGGER.debug("totalDurationSeconds={}", totalDurationSeconds);
        LOGGER.debug("replayOnce={}", replayOnce);
        LOGGER.debug("inputUri={}", inputUri);
        LOGGER.debug("jtlOutputFile={}", jtlOutputFile);
        LOGGER.debug("metricsOutputFile={}", metricsOutputFile);
        LOGGER.debug("metricsOutputPeriodSeconds={}", metricsOutputPeriodSeconds);
        LOGGER.debug("loggerLevelSpecs={}", loggerLevelSpecs);
        LOGGER.debug("redirectStrategy={}", redirectStrategy);
    }

    public static Config of(String[] args) {
        return JCommanderConfigs.create(args, new Config());
    }

}
