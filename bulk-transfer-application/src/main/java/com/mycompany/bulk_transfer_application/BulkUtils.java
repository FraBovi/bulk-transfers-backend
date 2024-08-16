package com.mycompany.bulk_transfer_application;

import com.mycompany.bulk_transfer_application.exception.AmountFormatException;

public class BulkUtils {

    /**
     * The function converts a String that represents a value in euros to
     * an Integer that represents the same value in cents of euros
     * 
     * @param euros String value in euros
     * @return integer value representing cents of euros of the input param
     */
    public static Integer getCentsOfEuros(String euros) throws NumberFormatException {
        Float centsOfEuros = 0f;
        centsOfEuros = Float.parseFloat(euros) * 100;

        return centsOfEuros.intValue();

    }
}
