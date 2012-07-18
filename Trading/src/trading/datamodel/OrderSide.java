package trading.datamodel;

public enum OrderSide 
{ 
	BUY, 
	SELL, 
	NONE;
	
	public static OrderSide getOppositeSide(OrderSide s)
	{
		switch (s)
		{
			case BUY: return OrderSide.SELL;
			case SELL: return OrderSide.BUY;
			default: throw new IllegalArgumentException("getOppositeSide: unrecognized parameter");
		}	
	}
}
