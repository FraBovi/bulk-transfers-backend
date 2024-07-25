package com.mycompany.bulk_transfer_application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BulkUtilsTests {

    @Test
    public void givenFloatString_testGetCentsOfEuros() {
        Integer actualResult = BulkUtils.getCentsOfEuros("123.58");
        Integer expectedResult = 12358;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void givenAlphanumericString_testGetCentsOfEuros() {
        // FIXME: I believe that you don't have to throw an exception if a non-numerical
        // value has been passed in. If you think that this behavior is fine, add your
        // custom exception and assert that has been thrown indeed in this test.
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            BulkUtils.getCentsOfEuros("abc");
        });
        String expectedMessage = "NumberFormatException";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
