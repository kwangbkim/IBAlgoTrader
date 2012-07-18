package gui;

import trading.datamodel.TradeablePair;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

public class TradeListTableModel extends AbstractTableModel {

	/**
	 * Generated by compiler. 
	 */
	private static final long serialVersionUID = 4452186237413815882L;
	
	private String[] columnNames = {"Ticker", "Side", "Quantity", "Price"};
	private Object[][] data = new Object[50][4];
	private int rowCount = 0;
	
	public TradeListTableModel(Hashtable<Integer, TradeablePair> input)
	{
		fillData(input);
	}
	
	@Override
	public int getColumnCount() { return columnNames.length; }
	public String getColumnName(int col) { return columnNames[col]; }
	
	@Override
	public int getRowCount() { return rowCount; }

	@Override
	public Object getValueAt(int row, int col) { return data[row][col]; }

	public Class<?> getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	
	/**
	 * Converts and fills the table model with results of database query.  
	 * @param h : hashtable that DatabaseMiner creates.
	 */
	private void fillData(Hashtable<Integer, TradeablePair> h)
	{
		Iterator<Integer> keys = h.keySet().iterator();
		
		while (keys.hasNext())
		{
			Object k = keys.next();
			TradeablePair t = h.get(k);
			
			data[rowCount][0] = t.getTradeDate();
			data[rowCount][1] = t.getPairId();
			data[rowCount][2] = t.getPair();
			data[rowCount][3] = t.getLongTermCoefficient();
			
			rowCount++;
		}
	}
	
}
