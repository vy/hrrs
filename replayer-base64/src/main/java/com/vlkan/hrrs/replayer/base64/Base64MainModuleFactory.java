package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.replayer.cli.Config;
import com.vlkan.hrrs.replayer.cli.MainModule;
import com.vlkan.hrrs.replayer.cli.MainModuleFactory;

public class Base64MainModuleFactory implements MainModuleFactory {

    @Override
    public MainModule create(Config config) {
        return new Base64MainModule(config);
    }

}
