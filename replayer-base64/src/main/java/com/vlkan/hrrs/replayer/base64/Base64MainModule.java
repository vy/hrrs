package com.vlkan.hrrs.replayer.base64;

import com.vlkan.hrrs.replayer.cli.Config;
import com.vlkan.hrrs.replayer.cli.ReplayerModule;
import com.vlkan.hrrs.replayer.record.HttpRequestRecordStream;

public class Base64MainModule extends ReplayerModule {

    public Base64MainModule(Config config) {
        super(config);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(HttpRequestRecordStream.class).to(Base64HttpRequestRecordStream.class);
    }

}
