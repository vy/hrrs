package com.vlkan.hrrs.servlet;

import com.vlkan.hrrs.api.HttpRequestPayload;
import com.vlkan.hrrs.api.ImmutableHttpRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HrrsUrlEncodedFormHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HrrsUrlEncodedFormHelper.class);

    public HrrsUrlEncodedFormHelper() {
        // Do nothing.
    }

    public boolean isUrlEncodedForm(String contentType) {
        if (contentType == null) {
            return false;
        }
        int semicolon = contentType.indexOf(';');
        if (semicolon >= 0) {
            contentType = contentType.substring(0, semicolon).trim();
        } else {
            contentType = contentType.trim();
        }
        return "application/x-www-form-urlencoded".equals(contentType);
    }

    public HttpRequestPayload extractUrlEncodedFormPayload(HttpServletRequest request, String defaultEncoding) {
        byte[] encodedFormParameterBytes = encodeFormParameters(request, defaultEncoding);
        return ImmutableHttpRequestPayload
                .newBuilder()
                .setMissingByteCount(0)
                .setBytes(encodedFormParameterBytes)
                .build();
    }

    private byte[] encodeFormParameters(HttpServletRequest request, String defaultEncoding) {
        try {
            String encoding = request.getCharacterEncoding();
            if (encoding == null) {
                encoding = defaultEncoding;
            }
            Map<String, List<String>> formParameters = parseFormParameters(request, encoding);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, encoding);
            boolean firstFormParameter = true;
            for (Map.Entry<String, List<String>> formParameter : formParameters.entrySet()) {
                String formParameterName = formParameter.getKey();
                String encodedFormParameterName = URLEncoder.encode(formParameterName, encoding);
                for (String formParameterValue : formParameter.getValue()) {
                    String encodedFormParameterValue = URLEncoder.encode(formParameterValue, encoding);
                    if (firstFormParameter) {
                        firstFormParameter = false;
                    } else {
                        writer.write('&');
                    }
                    writer.write(encodedFormParameterName);
                    writer.write('=');
                    writer.write(encodedFormParameterValue);
                }
            }
            writer.close();
            return outputStream.toByteArray();
        } catch (Throwable error) {
            LOGGER.error("failed to encode form parameters", error);
            return new byte[0];
        }
    }

    private Map<String, List<String>> parseFormParameters(HttpServletRequest request, String encoding) {
        Map<String, List<String>> queryParameters = parseQueryParameters(request, encoding);
        Map<String, List<String>> formParameters = Collections.emptyMap();
        Enumeration<String> parametersEnum = request.getParameterNames();
        while (parametersEnum.hasMoreElements()) {
            String name = parametersEnum.nextElement();
            List<String> queryParameterValues = queryParameters.get(name);
            String[] parameterValues = request.getParameterValues(name);
            List<String> formParameterValues = new LinkedList<String>();
            for (String parameterValue : parameterValues) {
                if (queryParameterValues == null || !queryParameterValues.contains(parameterValue)) {
                    formParameterValues.add(parameterValue);
                }
            }
            if (formParameters.isEmpty()) {
                formParameters = new LinkedHashMap<String, List<String>>();
            }
            formParameters.put(name, formParameterValues);
        }
        return formParameters;
    }

    private Map<String, List<String>> parseQueryParameters(HttpServletRequest request, String encoding) {
        String queryString = request.getQueryString();
        if (HrrsHelper.isBlank(queryString)) {
            return Collections.emptyMap();
        }
        String[] entries = queryString.split("&");
        if (entries.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        for (String entry : entries) {
            try {
                int splitterIndex = entry.indexOf('=');
                String key;
                String value;
                if (splitterIndex < 0) {
                    key = URLDecoder.decode(entry, encoding);
                    value = null;
                } else {
                    String encodedKey = entry.substring(0, splitterIndex);
                    key = URLDecoder.decode(encodedKey, encoding);
                    String encodedValue = entry.substring(splitterIndex + 1);
                    value = URLDecoder.decode(encodedValue, encoding);
                }
                List<String> values = map.get(key);
                if (values == null) {
                    values = new LinkedList<String>();
                    map.put(key, values);
                }
                values.add(value);
            } catch (UnsupportedEncodingException error) {
                String message = String.format("failed to read query parameter (entry=%s)", entry);
                LOGGER.error(message, error);
            }
        }
        return map;
    }

}
