package trading.execution;

import java.util.ArrayList;
import java.util.List;

import trading.datamodel.*;

/**
 * 
 * @author Kwang : This class encapsulates the market orders to be sent off for a given trading day. 
 * Orders are created by the strategy class.
 *
 */
public class OrderManager {
	
	private List<Order> myOrders = new ArrayList<Order>();
	
	public OrderManager() {}
	
	/**
	* Combines multiple orders for the same symbol into one order.  Order type is Market.
	* contract: symbol, side are consistent
	*
	* @param orders : the list of orders to aggregate
	* @return Order
	*/
	public static Order aggregateOrders (List<Order> orders)
	{
		if (orders.size() == 0) { return null; }
		
		int totalQty = 0;
		int symbolId = orders.get(0).getSymbolId();
		
		FinancialSecurity fs = orders.get(0).getSecurity();
		String exch = orders.get(0).getExchange();
		OrderSide side = orders.get(0).getOrderSide();
		
		for (Order o : orders)
		{
			if (o.getSymbolId() != symbolId || o.getOrderSide() != side) { throw new IllegalArgumentException("aggregateOrders : side, symbol must be consistent"); }
			
			totalQty += o.getQuantity();
		}
		
		return new Order(fs, exch, 0, OrderType.MARKET, side, totalQty);
	}
	
	/**
	* Add an order to the collection
	*
	* @param o the order to add
	*/
	public void addOrder(Order o)
	{
		if (o == null) { throw new IllegalArgumentException("can't add null order."); }
		myOrders.add(o);
	}
	
	/**
	* Add a list of orders to the existing order collection
	*
	* @param orders the list of orders to add
	*/
	public void addListOrders(List<Order> orders)
	{
		myOrders.addAll(orders);
	}
	
	/**
	* Retrieve the list of orders
	*/
	public List<Order> getOrders() { return myOrders; }
	
	/**
	* Print out all the orders currently held.
	*/
	public void printOrders()
	{
		System.out.println("----------");
		System.out.println("new orders");
		
		for (Order ord : myOrders) { System.out.println(ord.toString()); }	
	}
	
}					