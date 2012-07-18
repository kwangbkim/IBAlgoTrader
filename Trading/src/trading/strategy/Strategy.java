package trading.strategy;

import trading.datamodel.*;

/**
 * 
 * @author Kwang
 * Each algorithm implements this interface as it must analyze a security and determine
 * if a trade must be placed.
 */
public interface Strategy
{
	OrderSide execute();
}