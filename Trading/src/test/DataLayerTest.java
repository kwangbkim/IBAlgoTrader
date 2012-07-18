package test;

import java.sql.Date;
import java.sql.SQLException;

import trading.datalayer.*;
import trading.datamodel.PriceType;

public class DataLayerTest 
{
	private DataRetriever dataGetter = new DataRetriever();
	
	public DataLayerTest() {}
	
	public static void main(String[] args) {
		DataLayerTest d = new DataLayerTest();
		d.runTest();
	}
	
	private void runTest() 
	{
		double d = 0;
		
		try {
			d = dataGetter.getPriceAsOf(1, PriceType.OPEN, Date.valueOf("2011-01-04"));
			System.out.println(String.format("found %f", d));
			
			dataGetter.getOpenPositions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
}