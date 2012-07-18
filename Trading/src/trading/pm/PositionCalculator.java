package trading.pm;

import trading.datamodel.OrderSide;

public class PositionCalculator 
{
	public static int calculatePositionSize(double accountValue, double percentage, double price) 
	{ 
		double capital = accountValue * percentage; 
		return (int) (capital/price);
	}
	
	public static double calculateStopPrice(OrderSide os, double openPrice, double percentage) 
	{ 
		if (os == OrderSide.BUY) 
		{
			return openPrice - (openPrice * percentage);
		}
		else if (os == OrderSide.SELL)
		{
			return openPrice + (openPrice * percentage);
		}
		
		throw new IllegalArgumentException("can't calculate stop price for orderside: NONE");
	}
}