package trading.pm;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import mather.BigDecimalHelper;

import sqlitehelper.DateHelper;
import trading.datalayer.*;
import trading.datamodel.*;

/**
 * 
 * @author Kwang
 * This class manages the all open positions for a given strategy. 
 */
public class PositionManager 
{	
	final double stalePercent = 0;
	
	private List<Position> myPositions = new ArrayList<Position>();
	private DataRetriever dataGetter = new DataRetriever();

	/**
	 * Is the percent difference in price before another order can be placed
	 * for an existing position.  For instance, if a position exists for gold at 100
	 * and the threshold == .03, then gold must get to 103 or 97 before another order
	 * can be done.
	 */
	//private double isTradeableThreshold = .03;
	
	/**
	 * Is the number of days to look back to get the recent high/low to use when calculating
	 * stops on existing positions.
	 */
	private int previousDaysExistingStop = 10;
	
	/**
	 * Stop percentages for positions that haven't moved yet
	 */
	private double openStopPercentage = .5;
	/**
	 * Stop percentages for positions that have already moved a wave
	 */
	private double existingStopPercentage = .1;
	private int daysBeforeUsingExistingStopPercentage = 20;
	private double correlationThreshold = .4;
	
	public PositionManager() {}
	
	public void addPosition(Position p) 
	{
		if (p == null) { throw new IllegalArgumentException("can't add null position"); }
		myPositions.add(p);
	}
	
	/**
	 * Determine if a position in incubation state is no longer valid.  Return an order to close it
	 * out if it is.  
	 * 
	 * @param symbolId the symbol to check
	 * @param priceMovement the current price movement of the long term moving average
	 * @param threshold the threshold before a move is considered major
	 * @return Order to close out, null if none
	 */
	public Order checkStalePosition(int symbolId, double priceMovement, double threshold)
	{	
		if (Math.abs(priceMovement) < (threshold * stalePercent))
		{
			return closePosition(symbolId);
		}
		
		try {
			double correlation = dataGetter.getVTCorrelation(symbolId);
			if (Math.abs(correlation) > .7) 
			{ 
				return closePosition(symbolId); 
			}
		} catch (SQLException e) {
			System.out.println("checkStalePosition: error retrieving correlation : " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Close an existing position out.
	 * 
	 * @param: the ticker of the position
	 * @return: an Order that closes out the position
	 */
	public Order closePosition(int symbolId)
	{
		int totalQty = 0;
		OrderSide s = OrderSide.NONE;
		FinancialSecurity fs = null;
		
		for (Position curr : myPositions)
		{
			if (curr.getSecurity().getId() == symbolId)
			{
				fs = curr.getSecurity();
				s = OrderSide.getOppositeSide(curr.getSide());
				totalQty += curr.getQuantity();
			}
		}
		
		if (fs != null)
			return new Order(fs, "SMART", 0, OrderType.MARKET, s, totalQty);
		else
			return null;
	}
	
	/**
	* Create a list of stop orders for currently held positions
	*
	* @param stopPercentage the percentage price above or below to close each position
	* 
	* @return list of orders that will can close all positions if stop price is hit
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public List<Order> createStopOrders() throws SQLException, ParseException
	{
		List<Order> stopOrders = new ArrayList<Order>();
		updateStops();
		
		for (Position p : myPositions)
		{
			OrderSide opp = OrderSide.getOppositeSide(p.getSide());
			Order ord = new Order(p.getSecurity(), "SMART", p.getStopPrice(), OrderType.STOP, opp, p.getQuantity());
			stopOrders.add(ord);
		}
		
		return stopOrders;
	}
	
	/**
	 * Determine if a new position can be traded.  
	 * Contract:
	 *	Must not have a current position in it.
	 *	Must have low correlation with world stock ETF.	
	 *
	 * @param symbolId : the symbol to check
	 * @return true if this position can be added to this class
	 */
	public boolean isTradeable(int symbolId)
	{
		for (Position curr : myPositions)
		{
			if (curr.getSecurity().getId() == symbolId) { return false; }
		}
		
		double correlation = 0;
		try {
			correlation = dataGetter.getVTCorrelation(symbolId);
			System.out.println("corr: " + correlation);
		} catch (SQLException e) {
			System.out.println("error retrieving correlation : " + e.getMessage());
		}
		
		if (correlation > correlationThreshold) { return false; }
		
		return true;
	}
	
	/**
	* Determine how many positions are held for a specified ticker
	* 
	* @param symbolId the id of the symbol to check
	* @return {@code int >= 0} 
	*/
	public int numberOfPositions(int symbolId)
	{
		int result = 0;
		for (Position curr : myPositions)
		{
			if (curr.getSecurity().getId() == symbolId) { result++; }
		}
		
		return result;
	}
	
	/**
	 * Print the list of open positions 
	 */
	public void printPositions()
	{
		System.out.println("--------------");
		System.out.println("My Positions:");
		
		for (Position p : myPositions) { System.out.println(p.toString()); }
	}
	
	/**
	 * Update the stop prices of all open positions
	 * 
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public void updateStops() throws SQLException, ParseException
	{
		for (Position p : myPositions)
		{
			double peak = 0;
			if (p.getSide() == OrderSide.BUY) {
				peak = dataGetter.getMaxPrice(p.getSecurity().getId(), DateHelper.getPreviousDay(previousDaysExistingStop), PriceType.HIGH);
			} else if (p.getSide() == OrderSide.SELL) {
				peak = dataGetter.getMaxPrice(p.getSecurity().getId(), DateHelper.getPreviousDay(previousDaysExistingStop), PriceType.LOW);
			} else { throw new IllegalStateException("update stops: position must be buy or sell;"); }
			
			int daysBetween = DateHelper.daysBetween(p.getOpenDate(), DateHelper.getTodaysDate());
			double newStop;
			if (daysBetween >= daysBeforeUsingExistingStopPercentage) {
				newStop = PositionCalculator.calculateStopPrice(p.getSide(), peak, existingStopPercentage);				
			} else {
				newStop = PositionCalculator.calculateStopPrice(p.getSide(), p.getOpenPrice(), openStopPercentage);				
			}

			p.setStopPrice(BigDecimalHelper.exactDecimal(newStop, 2, RoundingMode.CEILING));
		}
	}

	/**
	 * Set the tradeable threshold
	 */
	//public void setIsTradeableThreshold(double itt) { isTradeableThreshold = itt; }
	
	/**
	* Retrieve the list of positions
	* 
	* @return list of Positions
	*/
	public List<Position> getPositions() { return myPositions; }
	/**
	* Set the list of positions 
	* 
	* @param newList : the list to set
	*/
	public void setPositions(List<Position> newList) { myPositions = newList; }
	
	/**
	 * Calculate the current unrealized pnl of all existing positions
	 * 
	 * @return the profit loss
	 * @throws SQLException
	 */
	public double getUnrealizedPnl() throws SQLException
	{
		double result = 0;
		
		for (Position curr : myPositions)
		{
			double lastClose = dataGetter.getLastPrice(curr.getSecurity().getId(), PriceType.CLOSE);
			double pnl = (lastClose - curr.getOpenPrice()) * curr.getQuantity(); 
			if (curr.getSide() == OrderSide.SELL) { pnl = pnl * -1; }
			result += pnl;
		}
		
		return result;
	}
	
	/**
	 * Set the number of days to look back when calculating existing stops based on recent high/lows
	 */
	public void setCorrelationThreshold(double c) { correlationThreshold = c; }
	public void setDaysBeforeUsingExistingStopPercentage(int p) { daysBeforeUsingExistingStopPercentage = p; }
	public void setExistingStopPercentage(double p) { existingStopPercentage = p; }	
	public void setOpenStopPercentage(double p) { openStopPercentage = p; }
	public void setPreviousDaysExistingStop(int p) { previousDaysExistingStop = p; }
}