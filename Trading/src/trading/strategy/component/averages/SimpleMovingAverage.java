package trading.strategy.component.averages;

import java.util.LinkedList;
import java.util.List;
import trading.util.CollectionHelper;

/**
* @author Kwang
* A simple moving average is a sequence of calculations done on a
* time separated list of values.  A specified time frame is used to determine
* how many values are to be added and averaged per SMA element.
*/
public class SimpleMovingAverage extends MovingAverage {
	
	/**
	* Constructor that creates and calculates a simple moving average 
	* based on a time-separated list of values and a time frame.
	*
	* pre:  {@code values.Size >= timeFrame * 2}
	* post: a List of moving average values
	*
	* @param values the list of values to process the simple moving average
	* from left to right.
	* @param t the time frame to calculate the averages
	*/
	public SimpleMovingAverage(List<Double> values, int t) 
	{
		super(values, t);	
	}

	/**
	 * Calculate the values for a simple moving average. 
	 * <pre>
	 * Contract:
	 * post: {@code movingAverage(x) = (Sum of previous timeFrame values) / timeFrame }
	 * </pre>
	 * @param values the list of values to process from left to right
	 */
	@Override protected void calculateMovingAverage(List<Double> values) {		
		LinkedList<Double> currentValues = new LinkedList<Double>();
		for (int i = 0; i < timeFrame; i++) { currentValues.add(values.get(i)); }
		
		int stop = values.size();
		for (int i = timeFrame; i <= stop; i++)
		{
			Double sma = CollectionHelper.calculateQueueSum(currentValues) / timeFrame;
			movingAverage.add(sma);
			currentValues.poll();
			
			if (i >= stop) break;
			Double nextVal = values.get(i);
			currentValues.addLast(nextVal);
		}
	}

}
