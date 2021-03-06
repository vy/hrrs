package com.vlkan.hrrs.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class HrrsServlet extends HttpServlet {

    public HrrsServlet() {
        // Do nothing.
    }

    private HrrsFilter getFilter() {
        HrrsFilter filter = (HrrsFilter) getServletContext().getAttribute(HrrsFilter.SERVLET_CONTEXT_ATTRIBUTE_KEY);
        return checkNotNull(filter, "filter is not initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String payload = String.format("{\"enabled\": %s}%n", getFilter().isEnabled());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.print(payload);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String enabledString = request.getParameter("enabled");
        boolean enabled = Boolean.parseBoolean(enabledString);
        getFilter().setEnabled(enabled);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.getOutputStream().close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        getFilter().flush();
    }

}
