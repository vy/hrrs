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
