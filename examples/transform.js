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
