package com.vlkan.hrrs.commons.jcommander;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public enum JCommanderConfigs {;

    public static <C extends JCommanderConfig> C create(String[] args, C config) {
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(config, args);
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
