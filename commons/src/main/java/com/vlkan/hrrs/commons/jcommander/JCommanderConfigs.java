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

package com.vlkan.hrrs.commons.jcommander;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public enum JCommanderConfigs {;

    public static <C extends JCommanderConfig> C create(String[] args, C config) {
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(config);
            jCommander.parse(args);
        } catch (ParameterException error) {
            System.err.println(error.getMessage());
            System.err.println("Run with --help, -h for the list of available parameters.");
            System.exit(1);
        }
        if (config.isHelp()) {
            jCommander.usage();
            System.exit(0);
        }
        return config;
    }

}
