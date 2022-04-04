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

package com.vlkan.hrrs.distiller.cli;

import com.beust.jcommander.Parameter;
import com.vlkan.hrrs.commons.jcommander.JCommanderConfig;
import com.vlkan.hrrs.commons.jcommander.JCommanderConfigs;
import com.vlkan.hrrs.commons.jcommander.validator.LoggerLevelSpecsValidator;
import com.vlkan.hrrs.commons.jcommander.validator.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Config implements JCommanderConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

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
            names = {"--outputUri", "-o"},
            validateWith = UriValidator.class,
            description = "output URI for HTTP records",
            required = true)
    private URI outputUri;

    public URI getOutputUri() {
        return outputUri;
    }

    @Parameter(
            names = {"--scriptUri", "-s"},
            validateWith = UriValidator.class,
            description = "input URI for script file",
            required = true)
    private URI scriptUri;

    public URI getScriptUri() {
        return scriptUri;
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

    @Override
    public boolean isHelp() {
        return help;
    }

    public void dump() {
        LOGGER.debug("inputUri = {}", inputUri);
        LOGGER.debug("outputUri = {}", outputUri);
        LOGGER.debug("scriptUri = {}", scriptUri);
        LOGGER.debug("loggerLevelSpecs={}", loggerLevelSpecs);
    }

    public static Config of(String[] args) {
        return JCommanderConfigs.create(args, new Config());
    }

}
