/*
 * Copyright 2016-2022 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.serializer.base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public enum Base64HttpRequestRecord {;

    public static final Charset CHARSET = StandardCharsets.US_ASCII;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss.SSSZ");

    public static final String FIELD_SEPARATOR = "\t";

    public static final String RECORD_SEPARATOR = "\n";

}
