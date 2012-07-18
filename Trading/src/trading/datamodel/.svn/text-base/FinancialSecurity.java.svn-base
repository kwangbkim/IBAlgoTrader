package trading.datamodel;

/**
 * @author Kwang
 * Encompasses all details inherent in any financial security.
 * Including stocks, bonds, futures etc..
 */
public abstract class FinancialSecurity {
	
	protected String ticker = null;
	protected String exchange = null;
	protected String securityType = null;
	protected int Id;	
	
	/**
	 * Retrieve the id that this security corresponds to in the symbol table.
	 * @return this security's id.
	 */
	public int getId() { return Id; }
	
	/**
	 * Retrieve the ticker of this security.
	 * @return string representing ticker
	 */
	public String getTicker() { return ticker; }
	protected void setTicker(String t)
	{ 
		if (t == null) { throw new IllegalArgumentException("Ticker can't be null"); }
		ticker = t;
	}	
	
	public String getExchange() { return exchange; }
	public void setExchange(String exch) 
	{
		if (exch == null) { throw new IllegalArgumentException("Exchange can't be null"); }
		exchange = exch;
	}
	
	public void setSecurityType(String st) 
	{ 
		if (st == null) { throw new IllegalArgumentException("security type can't be null"); }
		securityType = st; 
	}
	public String getSecurityType() { return securityType; }
}
