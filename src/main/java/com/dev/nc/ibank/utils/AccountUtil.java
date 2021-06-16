package com.dev.nc.ibank.utils;

import com.dev.nc.ibank.models.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

/**
 * Utility class that provides helper operations to manipulate {@link Account} objects
 */
public class AccountUtil {
    private static final String UUID_REGEX = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";

    /**
     * Validates whether the given parameter matches predefined (expected) form
     *
     * @param accountNumber the parameter to be validated
     */
    public static void validateAccountNumber(String accountNumber) {
        if (!Pattern.matches(UUID_REGEX, accountNumber)) {
            // TODO: replace with custom exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number!", null);
        }
    }
}
