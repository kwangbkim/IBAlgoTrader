import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

import sqlitehelper.*;

import trading.datalayer.DataRetriever;
import trading.datamodel.Order;
import trading.datamodel.Position;
import trading.datamodel.PriceType;

public class MarketSimulator {

	private DataRetriever dataGetter = new DataRetriever();
	
	public MarketSimulator() {}
	
	public Position placeOrder(Order d, Date dt) throws ParseException, SQLException
	{
		Date next = DateHelper.getNextDay(dt);
		double open = dataGetter.getPriceAsOf(d.getSymbolId(), PriceType.OPEN, next);
		
		Position result = new Position(d.getSecurity(), dt, d.getQuantity(), open, d.getOrderSide());
		return result;
	}
}
