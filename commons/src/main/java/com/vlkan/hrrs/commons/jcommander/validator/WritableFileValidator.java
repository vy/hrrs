/*
 * Copyright 2016-2024 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.hrrs.commons.jcommander.validator;

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
