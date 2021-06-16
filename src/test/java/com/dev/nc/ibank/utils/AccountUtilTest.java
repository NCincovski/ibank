package com.dev.nc.ibank.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class AccountUtilTest {
    @Test
    void validateAccountNumber_Valid() {
        String s = "123e4567-e89b-12d3-a456-9AC7CBDCEE52";
        AccountUtil.validateAccountNumber(s);
    }

    @Test
    void validateAccountNumber_Invalid() {
        String s = "123e4567-h89b-12d3-a456";
        final ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            AccountUtil.validateAccountNumber(s);
        });
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains("Invalid account number!"));
    }
}