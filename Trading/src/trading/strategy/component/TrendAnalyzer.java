package trading.strategy.component;

import java.math.RoundingMode;
import mather.BigDecimalHelper;
import trading.strategy.TrendType;
import trading.strategy.component.averages.*;

public class TrendAnalyzer {
	
	private TrendType myTrendType;
	private MovingAverage movingAverage;
	
	private double marketMovePercentage;
	private double majorMoveThreshold;
	private int numberDaysGoBack;
	
	public TrendAnalyzer(MovingAverage ma, double mmt, int ndgb)
	{
		if (ma == null)
		{
			throw new IllegalArgumentException("TrendAnalyzer ctor: params null or zero");
		}
		
		movingAverage = ma;
		majorMoveThreshold = mmt;
		numberDaysGoBack = ndgb;
	}
	
	/**
	 * Analyze the trend using the moving average along its parameters.
	 *
	 * @return the type of trend over the indicated period
	 */
	public void analyze()
	{
		int today = movingAverage.getTodayIndex();
		int pastIndex = today - numberDaysGoBack;
		
		double percentChange = movingAverage.getPercentDifference(pastIndex, today);
		double roundedPc = BigDecimalHelper.exactDecimal(percentChange, 4, RoundingMode.CEILING);
		System.out.println("% move: " + roundedPc);
		
		marketMovePercentage = roundedPc;
		
		setMyTrendType(getTrendType(roundedPc));
	}
	
	/**
	 * get the percent price movement of the moving average.
	 * @return double representing price movement
	 */
	public double getMarketMovePercentage() { return marketMovePercentage; }
	
	/**
	* Get the type of trend based on how much the price has moved
	* <pre>
	* {@code -100 < percentChange < 100}
	* </pre>
	* @param threshold the threshold to use differentiate trends
	* @param percentChange how far price has moved as a percentage
	* @return TrendEnum type 
	*/
	private TrendType getTrendType(double pc)
	{
		if (pc >= majorMoveThreshold) { return TrendType.BULL; }
		if (pc <= -majorMoveThreshold) { return TrendType.BEAR; }
		if (pc < majorMoveThreshold && pc > 0) { return TrendType.WEAKBULL; }
		if (pc > -majorMoveThreshold && pc < 0) { return TrendType.WEAKBEAR; }
		if (pc == 0) { return TrendType.NONE; }
		else { throw new IllegalStateException(String.format("Could not find a trend type, thresh:%d pc:%d", majorMoveThreshold, pc)); }
	}

	private void setMyTrendType(TrendType mtt) { this.myTrendType = mtt; }
	public void setMajorMoveThreshold(double mmt) { this.majorMoveThreshold = mmt; }
	public TrendType getMyTrendType() { return myTrendType; }
}
