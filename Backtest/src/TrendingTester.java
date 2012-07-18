import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import sqlitehelper.*;

import trading.pm.PositionCalculator;
import trading.pm.PositionManager;
import trading.strategy.*;
import trading.datalayer.DataRetriever;
import trading.datamodel.*;

public class TrendingTester {

	private double accountValue = 10000;
	private int numberOfTrades = 0;
	
	private double stopThreshold;
	
	private int longTermDiff;
	private int shortTermDiff;
	
	private DataRetriever dataGetter = new DataRetriever();
	private PositionManager positionManager = new PositionManager();
	private FinancialSecurity mySecurity;
	private MarketSimulator marketSim = new MarketSimulator();
	
	public TrendingTester(int lng, int sht, double s, int sym, String tick)
	{
		mySecurity = new Stock(sym, tick);
		stopThreshold = s;
	}
	
	public void setTrendParams(int lt, int st)
	{
		longTermDiff = lt;
		shortTermDiff = st;
	}
	
	public static void main(String[] args) 
	{
		int symId = Integer.parseInt(args[0]);
		String ticker = args[1];
		
		Date startDate = Date.valueOf(args[2]);
		int lt = Integer.parseInt(args[3]);
		int st = Integer.parseInt(args[4]);
		double stp = Double.parseDouble(args[5]);
		int ldiff = Integer.parseInt(args[6]);
		int sdiff = Integer.parseInt(args[7]);
		
		TrendingTester t = new TrendingTester(lt, st, stp, symId, ticker);
		t.setTrendParams(ldiff, sdiff);
		
		try {
			t.RunTest(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void RunTest(Date startdt) throws ParseException
	{
		Date currentdt = startdt;
		Date today = DateHelper.getTodaysDate();
		
		while (currentdt.before(today))
		{
			try {
				closeLosingPositions(currentdt);
				placeOrders(currentdt);
				positionManager.updateStops();
			} catch (SQLException e1) {
				System.out.println("sql : can't get price for " + currentdt.toString());				
			} finally {
				currentdt = DateHelper.getNextDay(currentdt);
			}
		}
		
		System.out.println("total pnl: " + (accountValue - 10000));
		
		double unrealized = 0; 
		try {
			unrealized = positionManager.getUnrealizedPnl();
			System.out.println(String.format("unrealized pnl: %f", unrealized));
		} catch (SQLException e) {
			System.out.println("can't get unrealized pnl");
		}
		System.out.println("% pnl: " + (accountValue - 10000)/10000);
		System.out.println("# trades: " + numberOfTrades);
		
	}
	
	private void placeOrders(Date dt) throws ParseException, SQLException
	{
		Trending trnd = new Trending(mySecurity);
		trnd.setLongNumberDiff(longTermDiff);
		trnd.setShortNumberDiff(shortTermDiff);
		trnd.setTradeDate(dt);
		
		OrderSide side = trnd.execute();
		double closePrice = dataGetter.getPriceAsOf(mySecurity.getId(), PriceType.CLOSE, dt);
					
		if (!positionManager.isTradeable(mySecurity.getId())) { return; }
		
		if (side != OrderSide.NONE)
		{
			int posQty = PositionCalculator.calculatePositionSize(accountValue, .1, closePrice); 
			Order ord = new Order(mySecurity, "SMART", 1, OrderType.LIMIT, side, posQty);
			Position pos = marketSim.placeOrder(ord, dt);
			positionManager.addPosition(pos);
			printNewPosition(pos, DateHelper.getNextDay(dt));
			numberOfTrades++;
		}
	}
	
	private void closeLosingPositions(Date dt) throws SQLException
	{
		List<Position> stillAlive = new ArrayList<Position>();
		
		for (Position p : positionManager.getPositions())
		{
			double stop = p.getStopPrice();
			
			if (p.getSide() == OrderSide.BUY)
			{
				double low = dataGetter.getPriceAsOf(p.getSecurity().getId(), PriceType.LOW, dt);
				if (low <= stop) 
				{ 
					p.setLastClosePrice(low);
					printClosePosition(p, dt);
					accountValue = accountValue - ((p.getOpenPrice() - low) * p.getQuantity()); 
				}
				else { stillAlive.add(p); }
			}
			else if (p.getSide() == OrderSide.SELL)
			{
				double high = dataGetter.getPriceAsOf(p.getSecurity().getId(), PriceType.HIGH, dt);
				if (high >= stop) 
				{
					p.setLastClosePrice(high);
					printClosePosition(p, dt);
					accountValue = accountValue - ((high - p.getOpenPrice()) * p.getQuantity()); 
				}
				else { stillAlive.add(p); }
			}
		}
		
		positionManager.setPositions(stillAlive);
	}
	
	private void printNewPosition(Position p, Date dt)
	{
		StringBuilder b = new StringBuilder();
		String side = p.getSide() == OrderSide.BUY ? "BUY" : "SELL";
		b.append(dt.toString() + " ");
		b.append("new position - " + p.getSecurity().getTicker());
		b.append(", " + side);
		b.append(", qty: " + p.getQuantity());
		b.append(", open price: " + p.getOpenPrice());
		
		System.out.println(b.toString());
	}
	
	private void printClosePosition(Position p, Date dt)
	{
		StringBuilder b = new StringBuilder();
		String side = p.getSide() == OrderSide.BUY ? "BUY" : "SELL";
		b.append(dt.toString() + " ");
		b.append("close position : " + p.getSecurity().getTicker());
		b.append(", " + side);
		b.append(", qty: " + p.getQuantity());
		b.append(", close price: " + p.getLastClosePrice());
		b.append(", pnl: " + p.getCurrentPnl());
		
		System.out.println(b.toString());
	}
}
