package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import trading.datamodel.FinancialSecurity;
import trading.datamodel.OrderSide;
import trading.datamodel.Stock;
import trading.strategy.Trending;

public class TrendingTest
{
	private FinancialSecurity fs = new Stock(1, "IAU");

	private int longTimeFrame;
	private int shortTimeFrame;

	private int longDaysToGoBack;

	private int shortDaysToGoBack;

	private double majorMoveLong;

	private double majorMoveShort;
	
	public TrendingTest(String paramFile) throws FileNotFoundException, IOException
	{
		fs = new Stock(4, "NIB");
		
		Properties p = new Properties();
		p.load(new FileInputStream(paramFile));
		
		longTimeFrame = Integer.parseInt(p.getProperty("ma.long.timeframe"));
		shortTimeFrame = Integer.parseInt(p.getProperty("ma.short.timeframe"));
	
		longDaysToGoBack = Integer.parseInt(p.getProperty("ma.long.timegoback"));
		shortDaysToGoBack = Integer.parseInt(p.getProperty("ma.short.timegoback"));
		
		majorMoveLong = Double.parseDouble(p.getProperty("ma.long.threshold"));
		majorMoveShort = Double.parseDouble(p.getProperty("ma.short.threshold"));
		
	}
	
	private void go()
	{
		Trending t = new Trending(fs);
		t.setLongNumberDiff(longDaysToGoBack);
		t.setShortNumberDiff(shortDaysToGoBack);
		
		t.setLongThreshold(majorMoveLong);
		t.setShortThreshold(majorMoveShort);
		
		t.setLongTimeFrame(longTimeFrame);
		t.setShortTimeFrame(shortTimeFrame);
		
		OrderSide s = t.execute();
		if (s != OrderSide.NONE)
		{
			System.out.println("placed order");
		}
	}
	
	public static void main(String[] args) {
		TrendingTest t;
		try {
			t = new TrendingTest(args[0]);
			t.go();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}