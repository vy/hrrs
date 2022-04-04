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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vlkan.hrrs.api.*;
import com.vlkan.hrrs.commons.logger.LoggerLevelAccessor;
import com.vlkan.hrrs.commons.logger.LoggerLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URI;

public class Distiller implements Runnable, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Distiller.class);

    private final Config config;

    private final HttpRequestRecordReader<?> reader;

    private final HttpRequestRecordWriter<?> writer;

    private interface Transformer {

        HttpRequestRecord transform(HttpRequestRecord input);

    }

    @Inject
    public Distiller(
            Config config,
            HttpRequestRecordReader<?> reader,
            HttpRequestRecordWriter<?> writer) {
        this.config = config;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {

        // Read the transformation function.
        Transformer transformer = createTransformer();

        // Initialize statistics.
        long totalRecordCount = 0;
        long ignoredRecordCount = 0;
        long changedRecordCount = 0;

        // Perform distillation.
        for (HttpRequestRecord input : reader.read()) {
            totalRecordCount++;
            LOGGER.trace("transforming (id={})", input.getId());
            HttpRequestRecord output = transformer.transform(input);
            if (output == null) {
                ignoredRecordCount++;
            } else {
                if (input != output) {
                    changedRecordCount++;
                }
                writer.write(output);
            }
        }

        // Report statistics.
        LOGGER.info("totalRecordCount = {}", totalRecordCount);
        String ignoredRecordPercentage = String.format("%.1f%%", (100.0f * ignoredRecordCount / totalRecordCount));
        LOGGER.info("ignoredRecordCount = {} ({})", ignoredRecordCount, ignoredRecordPercentage);
        String changedRecordPercentage = String.format("%.1f%%", (100.0f * changedRecordCount / totalRecordCount));
        LOGGER.info("changedRecordCount = {} ({})", changedRecordCount, changedRecordPercentage);

    }

    private Transformer createTransformer() {
        URI scriptUri = config.getScriptUri();
        File scriptFile = new File(scriptUri);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            FileReader scriptFileReader = new FileReader(scriptFile);
            engine.eval(scriptFileReader);
            final Invocable invocable = (Invocable) engine;
            return createTransformer(invocable);
        } catch (FileNotFoundException error) {
            String message = String.format("failed opening script file (uri=%s)", scriptUri);
            throw new RuntimeException(message, error);
        } catch (ScriptException error) {
            String message = String.format("failed executing script file (uri=%s)", scriptUri);
            throw new RuntimeException(message, error);
        }
    }

    private static Transformer createTransformer(final Invocable invocable) {
        return input -> {
            try {
                Object output = invocable.invokeFunction("transform", input);
                return (HttpRequestRecord) output;
            } catch (Throwable error) {
                throw new RuntimeException("failed invoking transform function", error);
            }
        };
    }

    @Override
    public void close() {

        // Close the reader source.
        try {
            reader.getSource().close();
        } catch (IOException error) {
            String message = String.format("failed closing reader source (inputUri=%s)", config.getInputUri());
            LOGGER.error(message, error);
        }

        // Close the writer source.
        try {
            writer.getTarget().close();
        } catch (IOException error) {
            String message = String.format("failed closing writer target (outputUri=%s)", config.getOutputUri());
            LOGGER.error(message, error);
        }

    }

    public static void main(String[] args, DistillerModuleFactory moduleFactory) {
        Config config = Config.of(args);
        config.dump();
        DistillerModule module = moduleFactory.create(config);
        Injector injector = Guice.createInjector(module);
        LoggerLevelAccessor loggerLevelAccessor = injector.getInstance(LoggerLevelAccessor.class);
        LoggerLevels.applyLoggerLevelSpecs(config.getLoggerLevelSpecs(), loggerLevelAccessor);
        try (Distiller distiller = injector.getInstance(Distiller.class)) {
            distiller.run();
        }
    }

}
