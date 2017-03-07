package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.replayer.cli.Replayer;

import java.io.IOException;

public enum Base64Replayer {;

    public static void main(String[] args) throws IOException {
        Base64ReplayerModuleFactory moduleFactory = new Base64ReplayerModuleFactory();
        Replayer.main(args, moduleFactory);
    }

}
