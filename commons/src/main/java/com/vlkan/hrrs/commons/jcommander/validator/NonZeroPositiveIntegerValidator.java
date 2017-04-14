package com.vlkan.hrrs.commons.jcommander.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NonZeroPositiveIntegerValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {
        int n = Integer.parseInt(value);
        if (n <= 0) {
            String message = String.format("expecting a non-zero positive integer: %s %d", name, n);
            throw new ParameterException(message);
        }
    }

}
