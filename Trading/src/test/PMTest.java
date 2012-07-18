package test;

import trading.datamodel.OrderSide;
import trading.pm.PositionCalculator;

public class PMTest {

	public static void main(String args[])
	{
		PMTest p = new PMTest();
		p.go();
	}
	
	public PMTest()
	{
	}
	
	public void go()
	{
		System.out.println(PositionCalculator.calculateStopPrice(OrderSide.BUY, 43.17, .087));
	}
}
