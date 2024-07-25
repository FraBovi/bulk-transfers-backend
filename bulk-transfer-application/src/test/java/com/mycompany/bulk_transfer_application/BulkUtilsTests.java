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
        // FIXME: I believe that you don't have to throw an exception if a non-numerical
        // value has been passed in. If you think that this behavior is fine, add your
        // custom exception and assert that has been thrown indeed in this test.
        Assertions.assertEquals(expectedResult, actualResult);
    }

}
