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

import java.util.Random;

class HrrsIdGenerator {

    private static final Random RANDOM = new Random(System.nanoTime());

    private final int randomSuffixLength;

    HrrsIdGenerator(int randomSuffixLength) {
        if (randomSuffixLength < 0) {
            throw new IllegalArgumentException("randomSuffixLength >= 0, found: " + randomSuffixLength);
        }
        this.randomSuffixLength = randomSuffixLength;
    }

    String next() {
        StringBuilder builder = new StringBuilder();
        appendTime(builder);
        builder.append('_');
        appendRandomSuffix(builder);
        return builder.toString();
    }

    private static void appendTime(StringBuilder builder) {
        long time = System.currentTimeMillis();
        String encodedTime = encodeLong(time);
        builder.append(encodedTime);
    }

    private void appendRandomSuffix(StringBuilder builder) {
        int availRandomSuffixLength = randomSuffixLength;
        while (availRandomSuffixLength > 0) {
            long number = RANDOM.nextLong();
            long positiveNumber = number == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(number);
            String encodedNumber = encodeLong(positiveNumber);
            if (encodedNumber.length() > availRandomSuffixLength) {
                builder.append(encodedNumber, 0, availRandomSuffixLength + 1);
                availRandomSuffixLength = 0;
            } else {
                builder.append(encodedNumber);
                availRandomSuffixLength -= encodedNumber.length();
            }
        }
    }

    private static String encodeLong(long time) {
        return Long.toString(time, Character.MAX_RADIX);
    }

}
