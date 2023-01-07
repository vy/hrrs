/*
 * Copyright 2016-2023 Volkan Yazıcı
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

package com.vlkan.hrrs.commons.logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

public class Log4jLoggerLevelAccessor implements LoggerLevelAccessor {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jLoggerLevelAccessor.class);

    private static final Log4jLoggerLevelAccessor INSTANCE = new Log4jLoggerLevelAccessor();

    private Log4jLoggerLevelAccessor() {
        LOGGER.debug("instantiated");
    }

    public static Log4jLoggerLevelAccessor getInstance() {
        return INSTANCE;
    }

    @Override
    public String getRootLevel() {
        Logger logger = LogManager.getRootLogger();
        return logger.getLevel().toString();
    }

    @Override
    public void setRootLevel(String levelName) {
        Objects.requireNonNull(levelName, "levelName");
        LOGGER.debug("updating root logger level (name={})", levelName);
        Level level = Level.toLevel(levelName);
        Logger logger = LogManager.getRootLogger();
        logger.setLevel(level);
    }

    @Override
    public String getLevel(String loggerName) {
        Objects.requireNonNull(loggerName, "loggerName");
        Logger logger = LogManager.getLogger(loggerName);
        return logger == null || logger.getLevel() == null ? null : logger.getLevel().toString();
    }

    @Override
    public void setLevel(String loggerName, String levelName) {
        Objects.requireNonNull(loggerName, "loggerName");
        Objects.requireNonNull(levelName, "levelName");
        LOGGER.debug("updating logger level (loggerName={}, levelName={})", loggerName, levelName);
        Logger logger = LogManager.getLogger(loggerName);
        Level level = Level.toLevel(levelName);
        logger.setLevel(level);
    }

}
