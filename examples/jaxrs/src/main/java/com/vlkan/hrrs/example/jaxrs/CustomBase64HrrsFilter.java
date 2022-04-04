/*
 * Copyright 2016-2022 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

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
