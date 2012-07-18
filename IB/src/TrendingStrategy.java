import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import trading.datalayer.DataRetriever;
import trading.datamodel.*;
import trading.execution.*;
import trading.pm.*;
import trading.strategy.Trending;

public class TrendingStrategy {

	private OrderManager orderManager = new OrderManager();
	private PositionManager positionManager = new PositionManager();
	private DataRetriever dataGetter = new DataRetriever();

	/**
 	* Strategy parameters
 	*/
	double accountValue;
	double correlationThreshold;
	double percentAccountPerPosition;
	
	int longTimeFrame;
	int shortTimeFrame;
	
	int longDaysToGoBack;
	int shortDaysToGoBack;
	
	double majorMoveLong;
	double majorMoveShort;
	
	//double isTradeableThreshold;
	
	double openStopPercentage;
	double existingStopPercentage;
	
	int previousDaysExistingStop;
	int daysBeforeUseExistingPercentage;
	/*	*/
	
	public TrendingStrategy(String paramFile) throws SQLException, FileNotFoundException, IOException 
	{
		initializeParameters(paramFile);
		initializePositionManager();
	}
	
	private void runAlgorithm() throws SQLException, ParseException
	{
		List<FinancialSecurity> securityList = dataGetter.getSecurities();
		
		for (FinancialSecurity fs : securityList) { createOrder(fs); }
		List<Order> stopOrders = positionManager.createStopOrders();
		
		positionManager.printPositions();
		orderManager.printOrders();
		printStops(stopOrders);
	}
	
	private void printStops(List<Order> orders)
	{
		System.out.println("----------");
		System.out.println("stops:");
		
		for (Order ord : orders) { System.out.println(ord.toString()); }
	}
	
	private void createOrder(FinancialSecurity fs) throws SQLException
	{
		Trending trnd = getNewTrending(fs);
		
		System.out.println("Analyzing " + fs.getTicker());
		
		OrderSide side = trnd.execute();

		double targetPrice;
		try {
			targetPrice = dataGetter.getLastPrice(fs.getId(), PriceType.CLOSE);
		} catch (SQLException e) {
			System.out.println("no price for : " + fs.getTicker());
			return;
		}
		
		Order staleClose = positionManager.checkStalePosition(fs.getId(), trnd.getLongTermMarketMovePercentage(), majorMoveLong);	
		
		if (staleClose != null) {
			orderManager.addOrder(staleClose);
		} 
		else if (side != OrderSide.NONE && positionManager.isTradeable(fs.getId()))  {
			int posQty = PositionCalculator.calculatePositionSize(accountValue, percentAccountPerPosition, targetPrice);
			Order ord = new Order(fs, "SMART", targetPrice, OrderType.LIMIT, side, posQty);
			ord.setStopPrice(openStopPercentage);

			orderManager.addOrder(ord);	
		}
	}
	
	private Trending getNewTrending(FinancialSecurity fs)
	{
		Trending trnd = new Trending(fs);
		
		trnd.setLongTimeFrame(longTimeFrame);
		trnd.setShortTimeFrame(shortTimeFrame);
		
		trnd.setLongNumberDiff(longDaysToGoBack);
		trnd.setShortNumberDiff(shortDaysToGoBack); 
		
		trnd.setMajorMoveLong(majorMoveLong);
		trnd.setMajorMoveShort(majorMoveShort);
		
		return trnd;
	}

	private void initializeParameters(String paramFile) throws FileNotFoundException, IOException
	{
		Properties p = new Properties();
		p.load(new FileInputStream(paramFile));
		
		accountValue = Integer.parseInt(p.getProperty("accountValue"));
		percentAccountPerPosition = Double.parseDouble(p.getProperty("percentAccount"));
		
		longTimeFrame = Integer.parseInt(p.getProperty("ma.long.timeframe"));
		shortTimeFrame = Integer.parseInt(p.getProperty("ma.short.timeframe"));
		
		openStopPercentage = Double.parseDouble(p.getProperty("stop.open"));
		existingStopPercentage = Double.parseDouble(p.getProperty("stop.existing"));
		
		longDaysToGoBack = Integer.parseInt(p.getProperty("ma.long.timegoback"));
		shortDaysToGoBack = Integer.parseInt(p.getProperty("ma.short.timegoback"));
		
		majorMoveLong = Double.parseDouble(p.getProperty("ma.long.threshold"));
		majorMoveShort = Double.parseDouble(p.getProperty("ma.short.threshold"));

		//isTradeableThreshold = Double.parseDouble(p.getProperty("pm.tradeableThreshold"));
		previousDaysExistingStop = Integer.parseInt(p.getProperty("stop.previousDaysExisting"));
		daysBeforeUseExistingPercentage = Integer.parseInt(p.getProperty("stop.daysBeforeUseExistingPercentage"));
		
		correlationThreshold = Double.parseDouble(p.getProperty("corr.Coefficient"));
	}
		
	private void initializePositionManager() throws SQLException
	{
		positionManager.setPositions(dataGetter.getOpenPositions());
		//positionManager.setIsTradeableThreshold(isTradeableThreshold);
		positionManager.setPreviousDaysExistingStop(previousDaysExistingStop);
		positionManager.setOpenStopPercentage(openStopPercentage);
		positionManager.setExistingStopPercentage(existingStopPercentage);
		positionManager.setDaysBeforeUsingExistingStopPercentage(daysBeforeUseExistingPercentage);
		positionManager.setCorrelationThreshold(correlationThreshold);
	}
	
	public static void main(String args[])
	{
		try {
			TrendingStrategy ts = new TrendingStrategy(args[0]);
			ts.runAlgorithm();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
