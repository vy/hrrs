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
