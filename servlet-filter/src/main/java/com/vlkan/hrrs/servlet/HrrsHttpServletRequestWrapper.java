package com.vlkan.hrrs.servlet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HrrsHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final ServletInputStream inputStream;

    HrrsHttpServletRequestWrapper(HttpServletRequest request, TeeServletInputStream inputStream) {
        super(request);
        this.inputStream = inputStream;
    }

    @Override
    public ServletInputStream getInputStream() {
        return inputStream;
    }

}
