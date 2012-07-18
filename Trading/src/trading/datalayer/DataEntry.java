package trading.datalayer;

import java.sql.*;

public class DataEntry {

	private Connection MyConnection;
	
	/**
	 * Constructor establishes connection to database.  Connection stays open
	 * until end of program.
	 */
	public DataEntry() 
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			MyConnection = DriverManager.getConnection("jdbc:sqlite:/Users/Kwang/Investment Management/Database/Database.sqlite");
			MyConnection.setAutoCommit(true);
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println(e.getLocalizedMessage());
			throw new IllegalStateException("Class.ForName failed.");
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getLocalizedMessage());
		}		
	}
	
	/**
	 * When auto commit is off this function will commit a transaction.
	 * @throws SQLException 
	 */
	public void commitTransasction() throws SQLException
	{
		if (MyConnection.getAutoCommit() == false) { MyConnection.commit(); }
	}
	
	/**
	 * This function toggles the auto commit feature of the sqlite connection.
	 * 
	 * @param on true when auto commit will be set active.
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean on) throws SQLException
	{
		MyConnection.setAutoCommit(on);
	}
	
	/**
	 * Inserts a new non-existing row into the DailyPrices table 
	 * 
	 * @param symbolId ID of entry in symbol table.
	 * @param dt Date of the price information
	 * @param open Opening price on the day
	 * @param close Closing price on the day
	 * @param high high price of the day
	 * @param low low price of the day
	 * @throws SQLException 
	 */
	public void insertDailyPrice(int symbolId, Date dt, double open, double close, double high, double low) throws SQLException
	{
		// ensure price doesn't exist already.
		PreparedStatement psCheck = MyConnection.prepareStatement("select count(*) rowcount from DailyPrices where Date=? and SymbolId=?;");
		psCheck.setString(1, dt.toString()); 
		psCheck.setInt(2, symbolId);
		ResultSet rsCheck = psCheck.executeQuery();
		if (rsCheck.getInt("rowcount") > 0 ) { return; }  
		
		PreparedStatement ps = MyConnection.prepareStatement("insert into DailyPrices(SymbolId, Date, Open, Close, High, Low) values(?,?,?,?,?,?);");
		ps.setInt(1,symbolId);
		ps.setString(2, dt.toString());
		ps.setDouble(3, open);
		ps.setDouble(4, close);
		ps.setDouble(5, high);
		ps.setDouble(6, low);
		ps.executeUpdate();
	}
	
	/**
	 * Insert a new non-existing row into HourlyPrices table
	 * 
	 * @param symbolId id in the Symbol table
	 * @param dt date of entry
	 * @param hour hour of entry
	 * @param open opening price 
	 * @param close closing price
	 * @throws SQLException
	 */
	public void insertHourlyPrice(int symbolId, Date dt, int hour, double open, double close) throws SQLException
	{
		PreparedStatement psCheck = MyConnection.prepareStatement("select count(*) rowcount from HourlyPrices where Date=? and SymbolId=? and Hour=?;");
		psCheck.setString(1, dt.toString());
		psCheck.setInt(2, symbolId);
		psCheck.setInt(3, hour);
		ResultSet rsCheck = psCheck.executeQuery();
		if (rsCheck.getInt("rowcount") > 0 ) { return; }
		
		PreparedStatement ps = MyConnection.prepareStatement("insert into HourlyPrices(SymbolId, Date, Hour, Open, Close) values(?,?,?,?,?);");
		ps.setInt(1,symbolId);
		ps.setString(2, dt.toString());
		ps.setInt(3, hour);
		ps.setDouble(4, open);
		ps.setDouble(5, close);
		ps.executeUpdate();
	}
	
	/**
	 * Inserts a new non-existing row into the WeeklyPrices table 
	 * 
	 * @param symbolId ID of entry in symbol table.
	 * @param dt Date of the price information
	 * @param open Opening price on the week
	 * @param close Closing price on the week
	 * @param high high price of the week
	 * @param low low price of the week
	 * @throws SQLException 
	 */
	public void insertWeeklyPrice(int symbolId, Date dt, double open, double close, double high, double low) throws SQLException
	{
		// ensure price doesn't exist already.
		PreparedStatement psCheck = MyConnection.prepareStatement("select count(*) rowcount from WeeklyPrices where Date=? and SymbolId=?;");
		psCheck.setString(1, dt.toString()); 
		psCheck.setInt(2, symbolId);
		ResultSet rsCheck = psCheck.executeQuery();
		if (rsCheck.getInt("rowcount") > 0 ) { return; }  
		
		PreparedStatement ps = MyConnection.prepareStatement("insert into WeeklyPrices(SymbolId, Date, Open, Close, High, Low) values(?,?,?,?,?,?);");
		ps.setInt(1,symbolId);
		ps.setString(2, dt.toString());
		ps.setDouble(3, open);
		ps.setDouble(4, close);
		ps.setDouble(5, high);
		ps.setDouble(6, low);
		ps.executeUpdate();
	}
	
	/**
	 * Retrieves the latest date for which there is daily price information.
	 * 
	 * @return maximum date for an entry in daily prices table.
	 * @throws SQLException 
	 */
	public Date getMaxDailyPriceDate() throws SQLException
	{
		PreparedStatement sql = MyConnection.prepareStatement("select max(date) maxdate from DailyPrices;");
		
		ResultSet rs = sql.executeQuery();
		return Date.valueOf(rs.getString("maxdate"));
	}
}