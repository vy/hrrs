package com.vlkan.hrrs.commons.jcommander.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.net.URI;
import java.net.URISyntaxException;

public class UriValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            new URI(value);
        } catch (URISyntaxException error) {
            String message = String.format("invalid URI: %s %s", name, value);
            throw new ParameterException(message, error);
        }
    }

}
