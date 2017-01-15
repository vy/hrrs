package com.vlkan.hrrs.replayer.logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class Log4jLoggerLevelAccessor implements LoggerLevelAccessor {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jLoggerLevelAccessor.class);

    public Log4jLoggerLevelAccessor() {
        LOGGER.debug("instantiated");
    }

    @Override
    public String getRootLevel() {
        Logger logger = LogManager.getRootLogger();
        return logger.getLevel().toString();
    }

    @Override
    public void setRootLevel(String levelName) {
        checkNotNull(levelName, "levelName");
        LOGGER.debug("updating root logger level (name={})", levelName);
        Level level = Level.toLevel(levelName);
        Logger logger = LogManager.getRootLogger();
        logger.setLevel(level);
    }

    @Override
    public String getLevel(String loggerName) {
        checkNotNull(loggerName, "loggerName");
        Logger logger = LogManager.getLogger(loggerName);
        return logger == null || logger.getLevel() == null ? null : logger.getLevel().toString();
    }

    @Override
    public void setLevel(String loggerName, String levelName) {
        checkNotNull(loggerName, "loggerName");
        checkNotNull(levelName, "levelName");
        LOGGER.debug("updating logger level (loggerName={}, levelName={})", loggerName, levelName);
        Logger logger = LogManager.getLogger(loggerName);
        Level level = Level.toLevel(levelName);
        logger.setLevel(level);
    }

}
