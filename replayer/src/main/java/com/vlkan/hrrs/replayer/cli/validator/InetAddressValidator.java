package com.vlkan.hrrs.replayer.cli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressValidator implements IParameterValidator {

    @Override
    public void validate(String name, String address) throws ParameterException {
        try {
            // noinspection ResultOfMethodCallIgnored
            InetAddress.getByName(address);
        } catch (UnknownHostException error) {
            String message = String.format("invalid Internet Protocol (IP) address: %s %s", name, address);
            throw new ParameterException(message, error);
        }
    }

}
