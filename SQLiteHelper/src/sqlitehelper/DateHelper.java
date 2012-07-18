package sqlitehelper;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {

	/**
	 * Reset date to an arbitrary value.
	 */
	public static Date ResetDate()
	{
		return Date.valueOf("1999-01-01");
	}
	
	/**
	 * convert a string to java.sql.date
	 * @param d
	 * <pre> acceptable formats
	 * yyyymmdd
	 * </pre>
	 * @return
	 */
	public static Date convertStringToDate(String date, String format)
	{
		if (format.equals("yyyymmdd"))
		{
			StringBuilder sb = new StringBuilder();
	    	sb.append(date.substring(0, 4));
	    	sb.append("-");
	    	sb.append(date.substring(4, 6));
	    	sb.append("-");
	    	sb.append(date.substring(6, 8));
	    	return Date.valueOf(sb.toString());
		}
    	
    	return null;
	}
	
	/**
	 * calculate the number of days between two dates
	 * 
	 * @param d1 previous date
	 * @param d2 later date
	 */
	public static int daysBetween(Date d1, Date d2)
	{
	     return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * Checks if a day is today
	 * 
	 * @param prev : java.sql.date possible previous day
	 * @param current : java.sql.date date to check against
	 * @return true if dt = current day.  in yyyy-mm-dd form
	 */
	public static boolean isToday(Date dt)	
	{
		Calendar c = Calendar.getInstance();
		Date today = new Date(c.getTimeInMillis());	
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String t = f.format(today);
		String lh = f.format(dt);
		if (t.equals(lh)) { return true; }
		
		return false;
	}
	
	public static Date getRequestEndDate()
	{
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.HOUR, 0);		
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		return new Date(c.getTimeInMillis());
	}
	
    /**
     * Extract the hour from a string date format : yyyymmdd hh:mm:ss
     * @param dt : date in string format
     * @return : hour
     */
    public static int getHourFromDateString(String dt)
    {
    	return Integer.valueOf(dt.substring(10,12));
    }

	/**
	 * Add one day to the given parameter
	 * @param dt : date to add
	 * @return : dt + 1 day
	 * @throws ParseException 
	 */
	public static Date getNextDay(Date dt) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt.toString()));
		c.add(Calendar.DATE, 1);
		
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) 
		{
			c.add(Calendar.DATE, 2);
		}
		
		return Date.valueOf(sdf.format(c.getTime()));
	}
	
	/**
	 * Subtract a specified number of days 
	 * 
	 * @param num : number of days to go back
	 * 
	 * @return : dt - num
	 * @throws ParseException 
	 */
	public static Date getPreviousDay(int num) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = getTodaysDate();
		
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt.toString()));
		c.add(Calendar.DATE, (num * -1));
		
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) 
		{
			c.add(Calendar.DATE, 2);
		}
		
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) 
		{
			c.add(Calendar.DATE, 1);
		}
		
		return Date.valueOf(sdf.format(c.getTime()));
	}
	
	/**
	 * Subtract a specified number of days 
	 * 
	 * @param dt : date to subtract from
	 * @param num : number of days to go back
	 * 
	 * @return : dt - num
	 * @throws ParseException 
	 */
	public static Date getPreviousDay(Date dt, int num) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt.toString()));
		c.add(Calendar.DATE, (num * -1));
		
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) 
		{
			c.add(Calendar.DATE, 2);
		}
		
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) 
		{
			c.add(Calendar.DATE, 1);
		}
		
		return Date.valueOf(sdf.format(c.getTime()));
	}
	
	/**
	 * Get today's date
	 * 
	 * @return today's date
	 */
	public static Date getTodaysDate()	
	{
		Calendar c = Calendar.getInstance();
		Date today = new Date(c.getTimeInMillis());	
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String t = f.format(today);
		
		return Date.valueOf(t);
	}
}
