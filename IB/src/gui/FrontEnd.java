package gui;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class FrontEnd extends JPanel {

	/**
	 * Added by compiler.
	 */
	private static final long serialVersionUID = 6843024927043272294L;

	public FrontEnd(OpenTradeTableModel tm) 
	{
		super(new GridLayout(1,0));
		
		final JTable table = new JTable(tm);
		DefaultTableCellRenderer left = new DefaultTableCellRenderer();
		left.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumn("Date").setCellRenderer(left);
		table.getColumn("Pair").setCellRenderer(left);
		table.getColumn("pnl").setCellRenderer(left);
		table.getColumn("LOpen").setCellRenderer(left);
		table.getColumn("ROpen").setCellRenderer(left);
		table.getColumn("LQty").setCellRenderer(left);
		table.getColumn("RQty").setCellRenderer(left);
		table.getColumn("LTCoef").setCellRenderer(left);
		table.getColumn("STCoef").setCellRenderer(left);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);	
	}
	
	public FrontEnd(TradeListTableModel tm) 
	{
		super(new GridLayout(1,0));
		
		final JTable table = new JTable(tm);
		DefaultTableCellRenderer left = new DefaultTableCellRenderer();
		left.setHorizontalAlignment(SwingConstants.LEFT);

		table.getColumn("Date").setCellRenderer(left);
		table.getColumn("PairId").setCellRenderer(left);
		table.getColumn("Pair").setCellRenderer(left);
		table.getColumn("LTCoef").setCellRenderer(left);
		table.getColumn("STCoef").setCellRenderer(left);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);	
	}
}
