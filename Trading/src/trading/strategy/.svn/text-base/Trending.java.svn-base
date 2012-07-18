package trading.strategy;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import trading.strategy.component.*;
import trading.strategy.component.averages.*;
import trading.datalayer.DataRetriever;
import trading.datamodel.*;
import trading.strategy.TrendType;

/**
 * 
 * @author Kwang
 * This class encapsulates an algorithm to analyze a security's trend.  It's goal is to identify
 * short term inefficiencies during long term trends.
 */
public class Trending implements Strategy
{
	private DataRetriever dataGetter = new DataRetriever();
	
	private FinancialSecurity mySecurity;
	private Date tradeDate;

	private TrendAnalyzer longTerm;
	private TrendAnalyzer shortTerm;
	
	// Strategy params
	private int longTimeFrame = 50;				// sma time frame size
	private int shortTimeFrame = 50;			// sma time frame size

	private int longTermNumberDiff = 15;		// how many days to look back to get % difference in moving average
	private int shortTermNumberDiff = 15;

	private double majorMoveLong = .04;
	private double majorMoveShort = .03;
	
	/**
	* Constructor to instantiate a strategy upon a particular security.
	*
	* @param f the FinancialSecurity the security to analyze
	* @param lt the time frame to look at for long term analysis
	* @param st the time frame to look at for short term analysis
	*/
	public Trending(FinancialSecurity f) 
	{
		if (f == null) { throw new IllegalArgumentException("Bad trending parameters"); };
		
		java.util.Date today = new java.util.Date();
		tradeDate = new Date(today.getTime());
		
		mySecurity = f;
	}
	
	private OrderSide calculateOrderSide(TrendType lng, TrendType sht)
	{
		if (lng == TrendType.BULL)
		{
			switch (sht)
			{
				case WEAKBEAR: return OrderSide.BUY;
				case WEAKBULL: return OrderSide.BUY;
				case BULL: return OrderSide.BUY;
				case NONE: return OrderSide.BUY;
			}
		}
		
		if (lng == TrendType.BEAR)
		{
			switch (sht)
			{
				case WEAKBULL: return OrderSide.SELL;
				case WEAKBEAR: return OrderSide.SELL;
				case BEAR: return OrderSide.SELL;
				case NONE: return OrderSide.SELL;
			}
		}
		
		return OrderSide.NONE;
	}
	
	/**
	* Executes the analysis and determine if an order should be placed
	* for this security.
	*
	* @return an instance of order depending on side, OrderSide.None if no orders
	 * @throws SQLException 
	*/
	public OrderSide execute()
	{
		List<Double> dailyClosePrices = getPricesForAnalysis(tradeDate, longTimeFrame * 2, PriceTimeFrame.DAY, PriceType.CLOSE);
		MovingAverage longMovingAverage = new ExponentialMovingAverage(dailyClosePrices, longTimeFrame);		
		longTerm = new TrendAnalyzer(longMovingAverage, majorMoveLong, longTermNumberDiff);
				
		List<Double> hourlyClosePrices = getPricesForAnalysis(tradeDate, shortTimeFrame * 2, PriceTimeFrame.HOUR, PriceType.CLOSE);
		MovingAverage shortMovingAverage = new SimpleMovingAverage(hourlyClosePrices, shortTimeFrame);
		shortTerm = new TrendAnalyzer(shortMovingAverage, majorMoveShort, shortTermNumberDiff);
		
		longTerm.analyze();
		shortTerm.analyze();
		
		TrendType longTrend = longTerm.getMyTrendType();
		TrendType shortTrend = shortTerm.getMyTrendType();
		
		if (longTrend== TrendType.WEAKBULL || longTrend == TrendType.WEAKBEAR) { return OrderSide.NONE; }
		
		OrderSide os = calculateOrderSide(longTrend, shortTrend);
		
		return os;
	}

	private List<Double> getPricesForAnalysis(Date endDate, int barsToGoBack, PriceTimeFrame pf, PriceType pt)
	{
		List<Double> result;
		try { result = dataGetter.getPrices(endDate, mySecurity.getId(), barsToGoBack, pf, pt); }
		catch (SQLException e) { throw new IllegalStateException(e.getMessage()); }

		return result;
	}

	public double getLongTermMarketMovePercentage() { return longTerm.getMarketMovePercentage(); }
	public double getShortTermMarketMovePercentage() { return shortTerm.getMarketMovePercentage(); }

	/**
	 * Retrieve the analyzed long term trend
	 * @return TrendType
	 */
	public TrendType getLongTermTrendType() { return longTerm.getMyTrendType(); }
	/**
	 * Retrieve the analyzed short term trend
	 * @return TrendType
	 */
	public TrendType getShortTermTrendType() { return shortTerm.getMyTrendType(); }
	
	/**
	* Set percentage difference to indicate a major trend
	* over the long term time frame.
	* 
	* @param t the threshold to use
	*/
	public void setLongThreshold(double t) { majorMoveLong = t; }
	
	/**
	* Set percentage difference to indicate a major trend
	* over the short term time frame.
	* 
	* @param t the threshold to use
	*/
	public void setShortThreshold(double t) { majorMoveShort = t; }

	/**
	* Set number of days to back when calculating percentage difference
	* in long term moving average
	* 
	* @param s the number of time units
	*/	
	public void setLongNumberDiff(int s) { longTermNumberDiff = s; }

	/**
	 * Set number of days to back when calculating long term moving average
	 * @param longTimeFrame
	 */
	public void setLongTimeFrame(int longTimeFrame) { this.longTimeFrame = longTimeFrame; }
	
	/**
	 * Set number of days to back when calculating short term moving average
	 * @param longTimeFrame
	 */
	public void setShortTimeFrame(int shortTimeFrame) { this.shortTimeFrame = shortTimeFrame; }

	/**
	* Set number of days to back when calculating percentage difference
	* in short term moving average
	* 
	* @param s the number of time units
	*/	
	public void setShortNumberDiff(int s) { shortTermNumberDiff = s; }
	
	/**
	* Set an optional parameter for the date to be used in analysis.  Used when back testing.
	*
	* @param td the trade date to use
	*/
	public void setTradeDate(Date td) { tradeDate = td; }

	public void setMajorMoveLong(double majorMoveLong) { this.majorMoveLong = majorMoveLong; }
	public void setMajorMoveShort(double majorMoveShort) { this.majorMoveShort = majorMoveShort; }
}