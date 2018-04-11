package com.vlkan.hrrs.example.jaxrs;

import com.vlkan.hrrs.servlet.base64.Base64HrrsFilter;
import com.vlkan.rfos.RotationConfig;
import com.vlkan.rfos.policy.DailyRotationPolicy;

import java.io.File;

public class CustomBase64HrrsFilter extends Base64HrrsFilter {

    public CustomBase64HrrsFilter() {
        super(createRotationConfig());
    }

    private static RotationConfig createRotationConfig() {
        String tmpPathname = System.getProperty("java.io.tmpdir");
        String file = new File(tmpPathname, "hrrs-jaxrs-records.csv").getAbsolutePath();
        String filePattern = new File(tmpPathname, "hrrs-jaxrs-records-%d{yyyyMMdd-HHmmss-SSS}.csv").getAbsolutePath();
        return RotationConfig
                .builder()
                .file(file)
                .filePattern(filePattern)
                .policy(DailyRotationPolicy.getInstance())
                .build();
    }

}
