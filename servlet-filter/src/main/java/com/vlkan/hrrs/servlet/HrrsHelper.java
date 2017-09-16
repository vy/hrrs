package com.vlkan.hrrs.servlet;

public enum HrrsHelper {;

    public static boolean isBlank(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        for (int charIndex = 0; charIndex < text.length(); charIndex++) {
            char c = text.charAt(charIndex);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

}
