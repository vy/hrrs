package com.vlkan.hrrs.distiller.cli;

import com.google.inject.AbstractModule;
import com.vlkan.hrrs.commons.logger.Log4jLoggerLevelAccessor;
import com.vlkan.hrrs.commons.logger.LoggerLevelAccessor;

import static com.google.common.base.Preconditions.checkNotNull;

public class DistillerModule extends AbstractModule {

    private final Config config;

    public DistillerModule(Config config) {
        this.config = checkNotNull(config, "config");
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        bind(LoggerLevelAccessor.class).toInstance(Log4jLoggerLevelAccessor.getInstance());
    }

}
