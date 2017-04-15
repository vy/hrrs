package com.vlkan.hrrs.distiller.cli;

public interface DistillerModuleFactory {

    DistillerModule create(Config config);

}
