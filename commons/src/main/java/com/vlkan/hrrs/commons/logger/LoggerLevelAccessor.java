package com.vlkan.hrrs.commons.logger;

public interface LoggerLevelAccessor {

    String getRootLevel();

    void setRootLevel(String levelName);

    String getLevel(String loggerName);

    void setLevel(String loggerName, String levelName);

}
