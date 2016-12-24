package com.vlkan.hrrs.api.base64;

import java.nio.charset.Charset;

enum Base64HttpRequestRecord {;

    public static Charset CHARSET = Charset.forName("US-ASCII");

    public static String FIELD_SEPARATOR = "\t";

    public static byte[] FIELD_SEPARATOR_BYTES = FIELD_SEPARATOR.getBytes(CHARSET);

    public static String RECORD_SEPARATOR = "\n";

    public static byte[] RECORD_SEPARATOR_BYTES = RECORD_SEPARATOR.getBytes(CHARSET);

}
