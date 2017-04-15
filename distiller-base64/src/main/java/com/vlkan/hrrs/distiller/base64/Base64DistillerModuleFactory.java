package com.vlkan.hrrs.distiller.base64;

import com.vlkan.hrrs.distiller.cli.Config;
import com.vlkan.hrrs.distiller.cli.DistillerModule;
import com.vlkan.hrrs.distiller.cli.DistillerModuleFactory;

public class Base64DistillerModuleFactory implements DistillerModuleFactory {

    @Override
    public DistillerModule create(Config config) {
        return new Base64DistillerModule(config);
    }

}
