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

package com.vlkan.hrrs.commons.jcommander.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class LoggerLevelSpecsValidator implements IParameterValidator {

    private static final String LOGGER_LEVEL_SPECS_REGEX = createLoggerLevelSpecsRegex();

    private static String createLoggerLevelSpecsRegex() {
        String packageNameRegex = "[\\p{L}_$][\\p{L}\\p{N}_$]*";
        String loggerNameRegex = String.format("%s(\\.%s)*", packageNameRegex, packageNameRegex);
        String loggerLevelRegex = "(trace|debug|info|warn|error)";
        String loggerLevelSpecRegex = String.format("(\\*|%s)=%s", loggerNameRegex, loggerLevelRegex);
        return String.format("%s(,%s)*", loggerLevelSpecRegex, loggerLevelSpecRegex);
    }

    @Override
    public void validate(String name, String loggerLevelSpecs) throws ParameterException {
        if (!loggerLevelSpecs.matches(LOGGER_LEVEL_SPECS_REGEX)) {
            String message = String.format("invalid logger level specification: %s %s", name, loggerLevelSpecs);
            throw new ParameterException(message);
        }
    }

}
