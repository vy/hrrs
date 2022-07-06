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

package com.vlkan.hrrs.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class HrrsServlet extends HttpServlet {

    public HrrsServlet() {
        // Do nothing.
    }

    private HrrsFilter getFilter() {
        HrrsFilter filter = (HrrsFilter) getServletContext().getAttribute(HrrsFilter.SERVLET_CONTEXT_ATTRIBUTE_KEY);
        return Objects.requireNonNull(filter, "filter");
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
