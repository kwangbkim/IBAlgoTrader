package trading.datamodel;

import java.sql.Date;

/**
 * 
 * @author Kwang
 *
 * Models two securities that can be traded and analyzed as a pair.
 */
public class TradeablePair {

	private int PairId;
	private String Pair;
	
	private double LeftQuantity;
	private double RightQuantity;
	private double LeftOpenPrice;
	private double RightOpenPrice;	
	private double LongTermCoefficient;
	private double ShortTermCoefficient;
	private double PnL;
	
	private Date TradeDate;
	
	public TradeablePair(int pairId)
	{
		PairId = pairId;
	}
	
	public int getPairId() { return PairId; }

	public void setLeftQuantity(double leftQuantity) { LeftQuantity = leftQuantity; }
	public double getLeftQuantity() { return LeftQuantity; }
 
	public void setRightQuantity(double rightQuantity) { RightQuantity = rightQuantity; }
	public double getRightQuantity() { return RightQuantity; }

	public void setRightOpenPrice(double rightOpenPrice) { RightOpenPrice = rightOpenPrice; }
	public double getRightOpenPrice() { return RightOpenPrice; }

	public void setLongTermCoefficient(double longTermCoefficient) { LongTermCoefficient = longTermCoefficient; }
	public double getLongTermCoefficient() { return LongTermCoefficient; }

	public void setShortTermCoefficient(double shortTermCoefficient) { ShortTermCoefficient = shortTermCoefficient; }
	public double getShortTermCoefficient() { return ShortTermCoefficient; }

	public void setLeftOpenPrice(double leftOpenPrice) { LeftOpenPrice = leftOpenPrice; }
	public double getLeftOpenPrice() { return LeftOpenPrice;}

	public void setTradeDate(Date tradeDate) { TradeDate = tradeDate; }
	public Date getTradeDate() { return TradeDate; }

	public void setPnL(double pnL) { PnL = pnL; }
	public double getPnL() { return PnL; }

	public void setPair(String pair) { Pair = pair; }
	public String getPair() { return Pair; }
}
