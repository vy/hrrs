package com.vlkan.hrrs.replayer.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.vlkan.hrrs.replayer.cli.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

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
            required = true,
            description = "remote HTTP server port")
    private int targetPort;

    public int getTargetPort() {
        return targetPort;
    }

    @Parameter(
            names = {"--maxRequestCountPerSecond", "-r"},
            validateWith = NonZeroPositiveIntegerValidator.class,
            description = "number of concurrent requests per second")
    private int maxRequestCountPerSecond = 1;

    public int getMaxRequestCountPerSecond() {
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
            names = {"--inputFile", "-i"},
            validateWith = ReadableFileValidator.class,
            description = "input file for HTTP records, can have .gz suffix",
            required = true)
    private String inputFile;

    public String getInputFile() {
        return inputFile;
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
            names = {"--help", "-h"},
            help = true,
            description = "display this help and exit")
    private boolean help;

    public void dump() {
        LOGGER.debug("localAddress={}", localAddress);
        LOGGER.debug("targetHost={}", targetHost);
        LOGGER.debug("targetPort={}", targetPort);
        LOGGER.debug("maxRequestCountPerSecond={}", maxRequestCountPerSecond);
        LOGGER.debug("threadCount={}", threadCount);
        LOGGER.debug("requestTimeoutSeconds={}", requestTimeoutSeconds);
        LOGGER.debug("rampUpDurationSeconds={}", rampUpDurationSeconds);
        LOGGER.debug("totalDurationSeconds={}", totalDurationSeconds);
        LOGGER.debug("inputFile={}", inputFile);
        LOGGER.debug("jtlOutputFile={}", jtlOutputFile);
        LOGGER.debug("metricsOutputFile={}", metricsOutputFile);
        LOGGER.debug("metricsOutputPeriodSeconds={}", metricsOutputPeriodSeconds);
        LOGGER.debug("loggerLevelSpecs={}", loggerLevelSpecs);
    }

    public static Config of(String[] args) {
        Config config = new Config();
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(config, args);
        } catch (ParameterException error) {
            System.err.println(error.getMessage());
            System.err.println("Run with --help, -h for the list of available parameters.");
            System.exit(1);
        }
        if (config.help) {
            jCommander.usage();
            System.exit(0);
        }
        return config;
    }

}
