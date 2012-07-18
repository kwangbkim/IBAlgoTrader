package com.ib.client.tws;

public class Trader extends Base {

	//private final String AccountName = "U995379";	// real
	private final String AccountName = "DU103053";	// test
	
	private double AccountValue; 
	
	public Trader()
	{
		try
		{
			requestAccountUpdate();
		}
		catch (InterruptedException e)
		{
			System.out.println("failed to update account: " + e.getLocalizedMessage());
		}
	}
	
	private void requestAccountUpdate() throws InterruptedException
	{
		int waitCount = 15;
		eClientSocket.reqAccountUpdates(false, AccountName);
		
        while (AccountValue == 0 && waitCount < MAX_WAIT_COUNT) {
            sleep(WAIT_TIME); // Pause for 1 second
            waitCount++;
        }
	}
	
	@Override public void updateAccountValue(String key, String value, String currency, String accountName)  
	{
		if (key == "NetLiquidation" && accountName == AccountName)
		{
			AccountValue = Double.parseDouble(value);
		}
	}
	
}
