package trading.strategy.component.averages;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import trading.util.CollectionHelper;

/**
	@author Kwang
	A volatility moving average uses two sequences, measuring the average
	difference between each tuple.
*/
public class VolatilityMovingAverage extends MovingAverage
{
		
	/**
	* Constructor that creates a moving average using the distance between
	* corresponding values of two data sets.
	*
	* @param open sequence of numbers that represents beginning values
	* @param close sequence of numbers that represents closing values
	* @param t the time frame to calculate values
	*/
	public VolatilityMovingAverage(List<Double> open, List<Double> close, int t)
	{
		super(open, t);
		calculateVolatilityMovingAverage(open, close);
	}

	@Override protected void calculateMovingAverage(List<Double> values) {}
	
	/**
	* Calculate the values for a volatility moving average.
	* <pre>
	* pre:  {@code open.size >= timeFrame * 2}
	*		{@code open.size = close.size}
	* </pre>	
	* @param open open values
	* @param close close values
	*/
	private void calculateVolatilityMovingAverage(List<Double> open, List<Double> close)
	{
		Queue<Double> currentValues = new LinkedList<Double>();
		for (int i = 0; i < timeFrame; i++) { currentValues.add(Math.abs(open.get(i) - close.get(i))); }
		
		int stop = open.size();
		for (int i = timeFrame; i < stop; i++)
		{
			Double vma = CollectionHelper.calculateQueueSum(currentValues) / timeFrame;
			movingAverage.add(vma);
			currentValues.poll();
			
			Double nextVal = Math.abs(open.get(i) - close.get(i));
			if (nextVal == null) { throw new NullPointerException(); }
			currentValues.add(nextVal);
		}
	}
	
}