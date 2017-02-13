package com.vlkan.hrrs.serializer.base64;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public enum Base64HttpRequestRecord {;

    public static final Charset CHARSET = Charset.forName("US-ASCII");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss.SSSZ");

    public static final String FIELD_SEPARATOR = "\t";

    public static final String RECORD_SEPARATOR = "\n";

}
