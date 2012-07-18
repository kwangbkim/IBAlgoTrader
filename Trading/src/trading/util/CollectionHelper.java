package trading.util;

import java.util.Queue;

public class CollectionHelper
{
	/**
	 * Calculates the total sum in a list of values.
	 * 
	 * @param currentValues the list of values
	 * @return the total sum of the elements
	 */
	public static Double calculateQueueSum(Queue<Double> currentValues)
	{
		Double result = Double.valueOf(0);
		for (Double c : currentValues) 
		{ 
			if (c == null) { throw new NullPointerException(); }
			result += c; 
		}
		
		return result;
	}
}