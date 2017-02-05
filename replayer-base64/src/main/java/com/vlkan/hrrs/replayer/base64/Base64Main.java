package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.replayer.cli.Main;

import java.io.IOException;

public enum Base64Main {;

    public static void main(String[] args) throws IOException {
        Base64MainModuleFactory moduleFactory = new Base64MainModuleFactory();
        Main.main(args, moduleFactory);
    }

}
