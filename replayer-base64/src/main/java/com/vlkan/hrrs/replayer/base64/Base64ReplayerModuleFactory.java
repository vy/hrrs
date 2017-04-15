package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.replayer.cli.Config;
import com.vlkan.hrrs.replayer.cli.ReplayerModule;
import com.vlkan.hrrs.replayer.cli.ReplayerModuleFactory;

public class Base64ReplayerModuleFactory implements ReplayerModuleFactory {

    @Override
    public ReplayerModule create(Config config) {
        return new Base64ReplayerModule(config);
    }

}
