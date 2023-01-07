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
