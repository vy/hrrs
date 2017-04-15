package com.vlkan.hrrs.distiller.base64;

import com.vlkan.hrrs.distiller.cli.Distiller;

import java.io.IOException;

public enum Base64Distiller {;

    public static void main(String[] args) throws IOException {
        Base64DistillerModuleFactory moduleFactory = new Base64DistillerModuleFactory();
        Distiller.main(args, moduleFactory);
    }

}
