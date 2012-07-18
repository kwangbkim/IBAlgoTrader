package trading.datamodel;

import java.math.RoundingMode;

import mather.BigDecimalHelper;
import trading.pm.PositionCalculator;

public class Order
{
	
	private FinancialSecurity securityToTrade;
	private String exchange;
	private double targetPrice;
	private int orderQuantity;
	private OrderType orderType;
	private OrderSide orderSide;
	
	/**
	 * Optional parameter for when you want a stop.
	 */
	private double stopPrice;
	
	public Order(FinancialSecurity f, String exch, double tp, OrderType ot, OrderSide s, int qty)
	{
		setFinancialSecurity(f);
		setExchange(exch);
		setTargetPrice(tp);
		orderType = ot;
		setOrderSide(s);
		setQuantity(qty);
	}
	
	public int getSymbolId() { return securityToTrade.getId(); }
	public FinancialSecurity getSecurity() { return securityToTrade; }
	
	/**
	 * Retrieve the exchange this security is traded on.
	 * @return string of the exchange code
	 */
	public String getExchange() { return exchange; }
	private void setExchange(String exch) 
	{ 
		if (exch == null) { throw new IllegalArgumentException("parameter can't be null"); }
		exchange = exch; 
	}
	
	private void setFinancialSecurity(FinancialSecurity f)
	{
		if (f == null) { throw new IllegalArgumentException("parameter can't be null"); }
		if (f.getTicker() == null) { throw new IllegalArgumentException("no ticker is parameter"); }
		
		securityToTrade = f;		
	}
	
	/**
	 * Retrieve the number of shares to be traded 
	 */
	public int getQuantity() { return orderQuantity; }
	private void setQuantity(int qty)
	{
		if (qty < 0) { throw new IllegalArgumentException("qty must be > 0"); }
		orderQuantity = qty;
	}

	/**
	 * Set the stop price as a percentage of the target price
	 * @param percentage the percentage above or below the target price 
	 */
	public void setStopPrice(double percentage) 
	{
		double stop = PositionCalculator.calculateStopPrice(orderSide, targetPrice, percentage);
		double roundedStop = BigDecimalHelper.exactDecimal(stop, 2, RoundingMode.CEILING);
		this.stopPrice = roundedStop; 
	}
	public double getStopPrice() { return stopPrice; }
	
	/**
	 * Retrieve the opening price of this security.  If order type is market it will set to limit automatically.
	 * @return double representing opening price.
	 */
	public double getTargetPrice() { return targetPrice; }
	private void setTargetPrice(double price) 
	{ 
		if (price == 0) { orderType = OrderType.MARKET; }
		else if (orderType == OrderType.MARKET) { orderType = OrderType.LIMIT; }
		targetPrice = price; 
	}
	
	/**
	* Retrieve the ticker of the security.
	* @return String representing symbol on market.
	*/
	public String getTicker() { return securityToTrade.getTicker(); }
	
	/**
	* Retrieve the order type used to execute this order
	* @return instance of OrderType
	*/ 
	public OrderType getOrderType() { return orderType; }

	private void setOrderSide(OrderSide orderSide) 
	{
		if (orderSide == OrderSide.NONE) { throw new IllegalArgumentException("set order side: can't be null"); }
		this.orderSide = orderSide; 
	}
	public OrderSide getOrderSide() { return orderSide; }
	
	/**
	 * Print the order in the format:
	 * Symbol Quantity Side Price 
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getTicker() + " ");
		sb.append(orderSide == OrderSide.BUY ? "Buy " : "Sell ");
		sb.append(orderQuantity + " ");
		sb.append("target: " + targetPrice + " ");
		sb.append("stop: " + stopPrice);
		return sb.toString();
	}

}