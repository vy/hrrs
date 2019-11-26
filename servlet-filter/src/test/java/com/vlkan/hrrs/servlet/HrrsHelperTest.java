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
