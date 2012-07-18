package trading.strategy.component.averages;

import java.util.List;

/**
* @author Kwang
* An exponential moving average gives more weight to later data 
* in a sequence of values.
*/
public class ExponentialMovingAverage extends MovingAverage
{
	
	/**
	* Constructor that creates and calculates an exponential moving average.
	*
	* pre:  {@code values.Size >= timeFrame * 2}
	*
	* @param values the list of values to process the exponential moving average
	* from left to right.
	* @param t the time frame to calculate the averages
	*/
	public ExponentialMovingAverage(List<Double> values, int t) 
	{
		super(values, t);		
	}
	
	/**
	* Populates the values for the EMA.
	* <pre>
	* post: {@code ema(x(t)) = [alpha * values(t)] + [(1-alpha) * ema(x(t - 1))] }
	* </pre>
	* @param values the list of values to process from left to right
	*/
	@Override protected void calculateMovingAverage(List<Double> values) 
	{
		Double smoothingConstant = new Double(2/(1 + (double)timeFrame));
				
		double initialSum = 0;
		for (int i = 0; i < timeFrame; i++) { initialSum += values.get(i); }
		
		movingAverage.add(initialSum / timeFrame);
		
		int maIndex = 0;
		double stop = values.size();
		for (int i = timeFrame; i < stop; i++)
		{
			maIndex = i - timeFrame;
			
			Double prevEma = movingAverage.get(maIndex);
			Double nextEma = prevEma + smoothingConstant * (values.get(i) - prevEma);
			
			if (prevEma == null || nextEma == null) { throw new NullPointerException("ema calc null"); }

			movingAverage.add(nextEma);
		}
	}
	
}