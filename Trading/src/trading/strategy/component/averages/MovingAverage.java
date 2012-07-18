package trading.strategy.component.averages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kwang
 * Parent class for all types of Moving Average instances.
 * Represents one immutable instance of a moving average, specific to 
 * a set of dates and price type.
 */
public abstract class MovingAverage
{
	/**
	 * Stores the calculated averages
	 */
	protected List<Double> movingAverage = new ArrayList<Double>();

	/**
	 * The number of units of time to go back to calculate averages
	 */
	protected int timeFrame;
	
	/**
	 * Constructor calculates the moving average values with given input.
	 * 
	 * @param values HashMap of values based on key of date.  Value types can be found
	 * in PriceType enum.
	 */
	public MovingAverage(List<Double> values, int t)
	{
		if (values.size() < (t * 2)) { throw new IllegalArgumentException("size:" + values.size() + ", timeframe: " + t); }
		
		timeFrame = t;
		calculateMovingAverage(values);
	}
	
	/**
	 * Populate the movingAverage member with calculated moving average values.
	 * 
	 * <pre>Values in list will be calculated in the order of which they exist.
	 * Ensure elements are added sequentially by whatever unit of time used.</pre>
	 * 
	 * @param list of double representing prices for a given interval timeframe.
	 */
	protected abstract void calculateMovingAverage(List<Double> values);
	
	/**
	 * Retrieve the calculated moving average value for a specific date.
	 * 
	 * @param i index to search for
	 * @return the moving average value at a specified date.
	 */
	public double getValueAt(int i)
	{
		
		if (movingAverage.get(i) == null) { throw new NullPointerException(String.format("no value found for key : %d", i)); }
		return movingAverage.get(i);
	}
	
	/**
	 * Retrieve the percent increase or decrease of the moving average value between
	 * two dates.
	 * 
	 * Contract : {@code begin < end}
	 * 
	 * @param s index to start from
	 * @param e index to end at
	 * @return {@code [value(e)-value(s)]/value(e)}
	 */ 
	public double getPercentDifference(int s, int e)
	{
		if (s == e) { return 0; }
		if (s > e) { throw new IllegalArgumentException("getPercentDifference : start must be >= end"); }
		
		Double begin = movingAverage.get(s);
		Double end = movingAverage.get(e);
		
		if (begin == null || end == null) { throw new NullPointerException(); }

		return (end - begin)/begin;
	}
	
	/**
	* Return the number of calculated moving average values.
	*/
	public int getTodayIndex()
	{
		return movingAverage.size() - 1;
	}
}