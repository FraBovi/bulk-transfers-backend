package com.mycompany.bulk_transfer_application;

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
        Integer actualResult = BulkUtils.getCentsOfEuros("abc");
        Integer expectedResult = 0; 
        Assertions.assertEquals(expectedResult, actualResult);
    }
    
}
