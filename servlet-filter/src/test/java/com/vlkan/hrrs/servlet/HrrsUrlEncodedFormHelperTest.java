/*
 * Copyright 2016-2024 Volkan Yazıcı
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

import com.vlkan.hrrs.api.HttpRequestPayload;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HrrsUrlEncodedFormHelperTest {

    private final HrrsUrlEncodedFormHelper urlEncodedFormHelper = new HrrsUrlEncodedFormHelper();

    @Test
    public void test_isUrlEncodedForm() {
        String[] contentTypes = new String[] {
                "application/x-www-form-urlencoded",
                " application/x-www-form-urlencoded \r\n",
                "application/x-www-form-urlencoded; charset=utf-8",
                "  application/x-www-form-urlencoded; charset=utf-8  "
        };
        for (String contentType : contentTypes) {
            String message = String.format("content-type: %s", contentType);
            assertThat(message, urlEncodedFormHelper.isUrlEncodedForm(contentType), is(true));
        }
    }

    @Test
    public void test_extractUrlEncodedFormPayload() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoding = "ASCII";
        when(request.getCharacterEncoding()).thenReturn(encoding);
        when(request.getQueryString()).thenReturn("y=y1");
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList("x", "y")));
        when(request.getParameterValues("x")).thenReturn(new String[] {"x1", "x2"});
        when(request.getParameterValues("y")).thenReturn(new String[] {"y2"});
        HttpRequestPayload payload = urlEncodedFormHelper.extractUrlEncodedFormPayload(request, null);
        assertThat(payload.getMissingByteCount(), is(0));
        assertThat(new String(payload.getBytes(), Charset.forName(encoding)), is(equalTo("x=x1&x=x2&y=y2")));
    }

}
