package com.vlkan.hrrs.servlet;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

class HrrsIdGenerator {

    private static final Random RANDOM = new Random(System.nanoTime());

    private final int randomSuffixLength;

    HrrsIdGenerator(int randomSuffixLength) {
        checkArgument(randomSuffixLength >= 0, "randomSuffixLength >= 0, found: %s", randomSuffixLength);
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
                builder.append(encodedNumber.substring(0, availRandomSuffixLength + 1));
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
