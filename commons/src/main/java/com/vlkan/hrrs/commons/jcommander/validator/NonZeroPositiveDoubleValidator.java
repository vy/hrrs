package com.vlkan.hrrs.commons.jcommander.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NonZeroPositiveDoubleValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {
        double d = Double.parseDouble(value);
        if (d <= 0) {
            String message = String.format("expecting a non-zero positive double: %s %s", name, value);
            throw new ParameterException(message);
        }
    }

}
