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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HrrsHelperTest {

    @Test
    public void test_isBlank() {
        test_isBlank(true, null, "", " ", "  ", "\t", " \t", "\n\t", "\r\n", " \t \r\n");
        test_isBlank(false, " x", "\t x", "\t x \t", "  x  ");
    }

    private void test_isBlank(boolean expectedBlank, String... texts) {
        for (String text : texts) {
            boolean actualBlank = HrrsHelper.isBlank(text);
            assertThat(actualBlank, is(expectedBlank));
        }
    }

}
