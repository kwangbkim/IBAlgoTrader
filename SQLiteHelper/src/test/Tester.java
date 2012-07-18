package test;

import java.sql.Date;
import java.text.ParseException;

import sqlitehelper.DateHelper;

@SuppressWarnings(value = { "all" })
public class Tester {
	
	public Tester() {}
	
	public static void main(String[] args) {
		Tester t = new Tester();
		try {
			t.previousTest();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isTodayTest(Date dt)
	{
		return DateHelper.isToday(dt);
	}
	
	private void previousTest() throws ParseException
	{
		System.out.println(DateHelper.getPreviousDay(10).toString());
	}
}