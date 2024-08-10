package com.mycompany.bulk_transfer_application;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BulkUtilsTests {

    @Test
    public void givenFloatString_testGetCentsOfEuros() {
        Integer actualResult = BulkUtils.getCentsOfEuros("123.58");
        Integer expectedResult = 12358;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void givenAlphanumericString_testGetCentsOfEuros() {

        assertThrows(NumberFormatException.class, () -> {
            BulkUtils.getCentsOfEuros("abc");
        });
        
    }

}
