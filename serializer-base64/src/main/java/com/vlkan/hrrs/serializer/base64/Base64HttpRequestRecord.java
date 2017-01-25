package com.vlkan.hrrs.serializer.base64;

import java.nio.charset.Charset;

public enum Base64HttpRequestRecord {;

    public static final Charset CHARSET = Charset.forName("US-ASCII");

    public static final String FIELD_SEPARATOR = "\t";

    public static final String RECORD_SEPARATOR = "\n";

}
