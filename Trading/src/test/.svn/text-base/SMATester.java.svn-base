package test;

import java.util.ArrayList;
import java.util.List;

import trading.strategy.component.averages.*;

/**
 * @author Kwang Kim
 * This class tests the trading.analytics package
 */
public class SMATester {
	private SimpleMovingAverage sma;
	
	@SuppressWarnings("unused")
	private ExponentialMovingAverage ema;

	@SuppressWarnings("unused")
	private VolatilityMovingAverage vma;
	
	public SMATester() 
	{
		List<Double> values = new ArrayList<Double>();
		values.add(23.59);
		values.add(24.89);
		values.add(21.23);
		values.add(22.44);
		values.add(24.59);
		values.add(19.23);
		values.add(23.10);
		values.add(25.52);
		values.add(23.54);
		values.add(22.23);
		values.add(21.39);
		values.add(22.68);
		values.add(22.59);
		values.add(23.25);
		values.add(21.66);
		values.add(25.59);
		values.add(26.69);
		values.add(26.66);
		values.add(27.88);
		values.add(26.76);

		sma = new SimpleMovingAverage(values, 10);
		//ema = new ExponentialMovingAverage(values, 10);
	}
	
	public void printResults()
	{
		for (int i = 0; i <= 10; i++) {
			double val = sma.getValueAt(i);
			System.out.println(String.format("val %d: %f", i, val));
		}
		
		System.out.println(String.format("diff 0-4 : %f", sma.getPercentDifference(0, 4)));
		System.out.println(String.format("diff 2-8 : %f", sma.getPercentDifference(2, 8)));
		System.out.println(String.format("diff 4-9 : %f", sma.getPercentDifference(4, 9)));
		System.out.println(String.format("diff 0-10 : %f", sma.getPercentDifference(0, 10)));
	}
	
	public static void main(String[] args) {
		SMATester tester = new SMATester();
		tester.printResults();
	}
}