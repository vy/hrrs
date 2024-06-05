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

package com.vlkan.hrrs.commons.logger;

public enum LoggerLevels {;

    public static void applyLoggerLevelSpecs(String loggerLevelSpecs, LoggerLevelAccessor loggerLevelAccessor) {
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
