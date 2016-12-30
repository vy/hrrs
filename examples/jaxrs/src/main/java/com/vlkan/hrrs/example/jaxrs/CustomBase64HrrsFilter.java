package com.vlkan.hrrs.example.jaxrs;

import com.vlkan.hrrs.servlet.Base64HrrsFilter;

import java.io.File;
import java.io.IOException;

public class CustomBase64HrrsFilter extends Base64HrrsFilter {

    public CustomBase64HrrsFilter() throws IOException {
        super(File.createTempFile("hrrs-jaxrs-records-", ".csv"));
    }

}
