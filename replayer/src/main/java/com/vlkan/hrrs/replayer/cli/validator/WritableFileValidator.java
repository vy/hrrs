package com.vlkan.hrrs.replayer.cli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.io.IOException;

public class WritableFileValidator implements IParameterValidator {

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void validate(String name, String value) throws ParameterException {

        boolean invalid = false;
        File file = new File(value);

        if (file.exists()) {
            invalid = !file.canWrite() || !file.isFile();
        } else {
            try {
                file.createNewFile();
                file.delete();
            } catch (IOException error) {
                invalid = true;
            }
        }

        if (invalid) {
            String message = String.format("expecting a writable file: %s %s", name, value);
            throw new ParameterException(message);
        }

    }

}
