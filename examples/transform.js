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

/**
 * Remove `Host` and `Content-Length` headers.
 */
function sanitizeHeaders(oldHeaders) {
    var newHeaders = [];
    for (var oldHeaderIndex = 0; oldHeaderIndex < oldHeaders.length; oldHeaderIndex++) {
        var oldHeader = oldHeaders[oldHeaderIndex];
        var oldHeaderName = oldHeader.getName();
        var allowed =
            !oldHeaderName.equalsIgnoreCase("host") &&
            !oldHeaderName.equalsIgnoreCase("content-length");
        if (allowed) {
            newHeaders.push(oldHeader);
        }
    }
    return newHeaders;
}

function transform(input) {
    var newHeaders = sanitizeHeaders(input.getHeaders());
    return input.toBuilder().setHeaders(newHeaders).build();
}
