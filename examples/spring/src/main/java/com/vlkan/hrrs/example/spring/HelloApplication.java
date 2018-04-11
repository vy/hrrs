package com.vlkan.hrrs.example.spring;

import com.vlkan.hrrs.servlet.HrrsFilter;
import com.vlkan.hrrs.servlet.HrrsServlet;
import com.vlkan.hrrs.servlet.base64.Base64HrrsFilter;
import com.vlkan.rfos.RotationConfig;
import com.vlkan.rfos.policy.DailyRotationPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        run(args);
    }

    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(HelloApplication.class, args);
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
                .policy(DailyRotationPolicy.getInstance())
                .build();
        return new Base64HrrsFilter(rotationConfig);
    }

    @Bean
    public ServletRegistrationBean provideHrrsServlet() {
        HrrsServlet hrrsServlet = new HrrsServlet();
        return new ServletRegistrationBean(hrrsServlet, "/hrrs");
    }

}
