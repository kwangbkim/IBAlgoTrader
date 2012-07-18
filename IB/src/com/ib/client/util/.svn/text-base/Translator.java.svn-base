package com.ib.client.util;
/**
 * Translates my security types from the database into the corresponding IB security type
 * used to trade and get price information.
 */
public class Translator 
{
	public static String getSecurityType(String st)
	{
		if (st.equals("ETF")) return "STK"; 
		if (st.equals("Stock")) return "STK";
		
		throw new IllegalArgumentException("Cant translate " + st);	
	}
}