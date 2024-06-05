/*
 * Copyright 2016-2023 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.example.spring;

import com.vlkan.hrrs.servlet.HrrsFilter;
import com.vlkan.hrrs.servlet.HrrsServlet;
import com.vlkan.hrrs.servlet.base64.Base64HrrsFilter;
import com.vlkan.rfos.RotationConfig;
import com.vlkan.rfos.policy.ByteMatchingRotationPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    public HrrsFilter provideHrrsFilter() {
        String tmpPathname = System.getProperty("java.io.tmpdir");
        String file = new File(tmpPathname, "hrrs-spring-records.csv").getAbsolutePath();
        String filePattern = new File(tmpPathname, "hrrs-spring-records-%d{yyyyMMdd-HHmmss-SSS}.csv").getAbsolutePath();
        RotationConfig rotationConfig = RotationConfig
                .builder()
                .file(file)
                .filePattern(filePattern)
                .policy(new ByteMatchingRotationPolicy((byte) '\n', 50_000))
                .build();
        return new Base64HrrsFilter(rotationConfig);
    }

    @Bean
    public ServletRegistrationBean<?> provideHrrsServlet() {
        HrrsServlet hrrsServlet = new HrrsServlet();
        return new ServletRegistrationBean<>(hrrsServlet, "/hrrs");
    }

}
