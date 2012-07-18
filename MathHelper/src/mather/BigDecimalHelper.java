package mather;

import java.math.*;

public class BigDecimalHelper {

	/**
	 * Round a double to a specific decimal place
	 * @param value number to round
	 * @param places how many decimal places
	 * @param r the type of rounding
	 * @return rounded double value
	 */
	public static double exactDecimal(double value, int places, RoundingMode r)
	{
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, r);
		
		return bd.doubleValue();
	}
}
