package trading.datamodel;

import java.math.RoundingMode;
import java.sql.Date;

import mather.BigDecimalHelper;

public class Position
{	
	private FinancialSecurity mySecurity;
	private Date openDate;
	private int quantity;
	private double openPrice;
	private double lastClosePrice;
	private double currentPnl;
	private double stopPrice;
	private OrderSide side;
	
	public Position(FinancialSecurity f, Date od, int q, double o, OrderSide s)
	{
		setSecurity(f);
		setQuantity(q);
		setOpenPrice(o);
		side = s;
		setOpenDate(od);
	}

	private void calculatePnl()
	{
		double pnl;
		pnl = (lastClosePrice - openPrice) * quantity;
		
		if (side == OrderSide.SELL)
		{
			pnl = pnl * -1;
		}
		
		currentPnl = pnl;
	}
	
	public double getLastClosePrice() { return lastClosePrice; }
	public void setLastClosePrice(double l)
	{
		if (l <= 0 ) { throw new IllegalArgumentException("last close must be > 0"); }
		lastClosePrice = l;
		calculatePnl();
	}
	
	public Date getOpenDate() { return openDate; }
	private void setOpenDate(Date od) { openDate = od; }
	
	public double getOpenPrice() { return openPrice; }
	private void setOpenPrice(double o)
	{
		if (o <= 0) { throw new IllegalArgumentException("open must be > 0"); }
		openPrice = o;
	}
	
	public int getQuantity() { return quantity; }
	private void setQuantity(int q) 
	{ 
		if (q <= 0) { throw new IllegalArgumentException("quantity must be > 0"); }
		quantity = q;
	}
	
	public FinancialSecurity getSecurity() { return mySecurity; }
	private void setSecurity(FinancialSecurity f)
	{
		if (f == null) { throw new IllegalArgumentException("security can't be null"); }
		if (f.getTicker() == null) {throw new IllegalArgumentException("ticker can't be null"); }
		
		mySecurity = f;
	}
	
	public OrderSide getSide() { return side; }
	
	public void setStopPrice(double stopPrice) { this.stopPrice = stopPrice; }
	public double getStopPrice() { return stopPrice; }

	public double getCurrentPnl() { return currentPnl; }

	/**
 	* Prints the object in the format : Date Ticker Side Quantity OpenPrice StopPrice LastClose Pnl
 	*/
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(openDate.toString() + " ");
		sb.append(mySecurity.getTicker() + " ");
		sb.append(side == OrderSide.BUY ? "Buy " : "Sell " + " ");
		sb.append(quantity + " ");
		sb.append("fillprice: " + openPrice + " ");
		sb.append("stop: " + stopPrice + " ");
		sb.append("last close: " + lastClosePrice + " ");
		sb.append("pnl: " + BigDecimalHelper.exactDecimal(currentPnl, 2, RoundingMode.CEILING) + " ");
		
		return sb.toString();
	}
}