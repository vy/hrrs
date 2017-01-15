package com.vlkan.hrrs.replayer.cli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class ReadableFileValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        File file = new File(value);
        boolean valid = file.exists() && file.canRead() && file.isFile();
        if (!valid) {
            String message = String.format("expecting a readable file: %s %s", name, value);
            throw new ParameterException(message);
        }
    }

}
