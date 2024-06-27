package com.mycompany.bulk_transfer_application;

public class BulkUtils {

    /**
	 * The function converts a String that represents a value in euros to
	 * an Integer that represents the same value in cents of euros
	 * 
	 * @param euros String value in euros
	 * @return integer value representing cents of euros of the input param
	 */
	// FIXME: this could be moved somewhere else in a "utils" class and file.
	public static Integer getCentsOfEuros(String euros) {
        // TODO: probably you can convert those values to floating point numbers and multiply by 100.
        // even if it works, let's write it in a cleaner way.
		Float centsOfEuros = Float.parseFloat(euros) * 100;
		
		return centsOfEuros.intValue();
	}
    
}
