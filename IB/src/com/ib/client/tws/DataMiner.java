package com.ib.client.tws;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.ib.client.Contract;
import com.ib.client.util.RequestManager;
import com.ib.client.util.Translator;

import sqlitehelper.*;

import trading.datamodel.*;
import trading.datalayer.*;

/**
 * @author Kwang
 * Retrieves pricing information from TWS for all securities listed in database.
 * Saves the information back to database.
 */
public class DataMiner extends Base {

	// singleton classes
	private RequestManager Requestor = RequestManager.getInstance();
	private DataEntry dataEnterer = new DataEntry();
	private DataRetriever dataGetter = new DataRetriever();
	
	private FinancialSecurity CurrentSecurity;
	private boolean isCurrentRequestDone = false;
	private PriceTimeFrame currentTimeFrame = PriceTimeFrame.DAY;
	
	private Date RequestEndDate = DateHelper.getRequestEndDate();
	
	List<FinancialSecurity> tickerList;
	
	public DataMiner()
	{	
		
		try 
		{
			connectToTWS();
			tickerList = dataGetter.getSecurities();
		} 
		catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
		catch (Throwable t)
		{
			System.out.println("Error connecting to TWS : " + t.getLocalizedMessage());
		}
	}
	
	/**
	 * Bring pricing information up to date.
	 */
	public void updateData()
	{
		int numDaysToGoBack = daysSinceLastUpdate();
		if (numDaysToGoBack == 0) { return; }
		
		requestPricingData(numDaysToGoBack, "1 day");
		
		currentTimeFrame = PriceTimeFrame.HOUR;
		requestPricingData(numDaysToGoBack, "1 hour");
	}
	
	public void disconnect() { disconnectFromTWS(); }
	
	/**
	 * update the database with prices from previous days.
	 * @throws SQLException
	 * @throws InterruptedException, SQLException, TimeoutException
	 * 	*/
	private void requestPricingData(int daysToGoBack, String barType)
	{		
		try
		{					
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd 18:00:00");
			String tomorrow = f.format(RequestEndDate);
					
			for (FinancialSecurity sec : tickerList)
			{
				String ticker = sec.getTicker();
				String typ = Translator.getSecurityType(sec.getSecurityType());
				String exch = sec.getExchange();
				
				Contract contract = createContract(ticker, typ, exch, "USD");
				CurrentSecurity = new Stock(sec.getId(), ticker);
				
				// request daily historical data
				int requestId = Requestor.getNextRequestId();
				String numDays = String.format("%d D", daysToGoBack);
	            eClientSocket.reqHistoricalData(requestId, contract, tomorrow, numDays, barType, "TRADES", 1, 1);
	
	            // wait for request to complete.  send error if it takes too long
	            System.out.println("Getting : " + ticker);
	            int waitCount = 0;
	            while (!isCurrentRequestDone)
	            {
	            	if (waitCount > 15) { throw new TimeoutException(); }
	            	sleep(WAIT_TIME * 40); // 3 second wait
	            	waitCount++;
	            }
	            
	            sleep(WAIT_TIME);
	            isCurrentRequestDone = false;
	            System.out.println(String.format("Retrieved %s", ticker));
			}
		}
		catch (InterruptedException i) 
		{ 
			System.out.println("retrieveTodaysData() Interrupted exception: " + i.getMessage());
		}
		catch (TimeoutException t)
		{
			System.out.println("retrieveTodaysData() Price request timed out: " +  t.getMessage());
		}
	}
	
    /**
     * Receive data from IB and insert into database.
     */
    @Override public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps)  
    {
    	try 
    	{
    		System.out.println(date + " o:" + open + " h:" + high + " l:" + low + " c:" + close);
    		
    		if (date.indexOf("finished") >= 0) 
    		{
    			isCurrentRequestDone = true;
    			return;
    		}
	    	
	    	Date lastHistoricalDate = DateHelper.convertStringToDate(date, "yyyymmdd");
	
			switch (currentTimeFrame)
			{
				case DAY:
	    			dataEnterer.insertDailyPrice(CurrentSecurity.getId(), lastHistoricalDate, open, close, high, low); 
					break;
				case HOUR:
	    			int hour = DateHelper.getHourFromDateString(date);
	    			dataEnterer.insertHourlyPrice(CurrentSecurity.getId(), lastHistoricalDate, hour, open, close);
					break;
				case WEEK:
					dataEnterer.insertWeeklyPrice(CurrentSecurity.getId(), lastHistoricalDate, open, close, high, low); 
					break;
	    	}
	    	
    	}
    	catch (SQLException e) 
		{
			System.out.println("Historical daily insert failed: " + CurrentSecurity.toString());
			System.out.println(e.getLocalizedMessage());
		}
    	catch (Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
	/**
	 * @return the number of days since the last price update.
	 * @throws SQLException
	 */
	private int daysSinceLastUpdate() 
	{
		try
		{
			Date lastUpdate = dataEnterer.getMaxDailyPriceDate();
			Calendar l = new GregorianCalendar();
			l.setTime(lastUpdate);
			
			Calendar t = new GregorianCalendar();
			t.setTime(RequestEndDate);
			long diff = (t.getTimeInMillis() - l.getTimeInMillis()) / 86400000;
			int s = (int)diff;
			
			return s;
		}
		catch (SQLException e) 
		{ 
			System.out.println("retrieveHistoricalData() SQL exception: " + e.getMessage());
			return -1;
		}
	}
	
    /**
     * Sync next order ID with TWS.
     */
    public void nextValidId(int orderId) { Requestor.initializeOrderId(orderId); }
}