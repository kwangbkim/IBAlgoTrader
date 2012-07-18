package com.ib.client.tws;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

/**
 * Base class that defines the gateway to the IB API.
 * Any class that interacts with TWS must extend this class.
 */
public abstract class Base extends Thread implements EWrapper {

    protected EClientSocket eClientSocket = new EClientSocket(this);
    protected final static String TWS_HOST 		= "localhost";
    protected final static int TWS_PORT 		= 7496;
    protected final static int TWS_CLIENT_ID 	= 1;
    protected final static int MAX_WAIT_COUNT 	= 15; 		// 15 secs
    protected final static int WAIT_TIME 		= 1000; 	// 1 sec

    protected void connectToTWS() {
        eClientSocket.eConnect(TWS_HOST, TWS_PORT, TWS_CLIENT_ID);
    }

    protected void disconnectFromTWS() {
        if (eClientSocket.isConnected()) {
            eClientSocket.eDisconnect();
        }
    }

    protected Order createOrder(String action, int quantity, String orderType) {
        Order order = new Order();

        order.m_action = action;
        order.m_totalQuantity = quantity;
        order.m_orderType = orderType;
        // order.m_transmit = true;

        return order;
    }

    protected Contract createContract(String symbol, String securityType, String exchange, String currency) {
        return createContract(symbol, securityType, exchange, currency, null, null, 0.0);
    }

    protected Contract createContract(String symbol, String securityType, String exchange, String currency, String expiry, String right, double strike) {
        Contract contract = new Contract();

        contract.m_symbol = symbol;
        contract.m_secType = securityType;
        contract.m_exchange = exchange;
        contract.m_currency = currency;

        if (expiry != null) {
            contract.m_expiry = expiry;
        }

        if (strike != 0.0) {
            contract.m_strike = strike;
        }

        if (right != null) {
            contract.m_right = right;
        }

        return contract;
    }
    
    public void error(String str) {
    	if (str.equals(null)) { System.out.println("null here"); }
        System.out.println("  [API.msg1] " + str);
    }

    public void error(int one, int two, String str) {
        System.out.println("  [API.msg2] " + str + " {" + one + ", " + two + "}");
    }

    public void error(Exception e) {
        System.out.println("  [API.msg3] " + e.getMessage());
    }

    public void connectionClosed() {
        System.out.println(" [API.connectionClosed] Closed connection with TWS");
    }
    
    /**
     * Default implementations for all EWrapper methods.
     */
    @Override public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {}
    @Override public void tickSize(int tickerId, int field, int size) {}
    @Override public void tickString(int tickerId, int field, String value) {}
    @Override public void tickGeneric(int tickerId, int field, double generic) {}
    @Override public void tickEFP(int tickerId, int field, double basisPoints, String formattedBasisPoints, double totalDividends, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {}
    @Override public void execDetails(int orderId, Contract contract, Execution execution) {}
    @Override public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {}
    @Override public void managedAccounts(String accountsList) {}
    @Override public void nextValidId(int orderId) {}
    @Override public void receiveFA(int faDataType, String xml) {}
    @Override public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {}
    @Override public void scannerParameters(String xml) {}
    @Override public void updateAccountTime(String timeStamp) {}
    @Override public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {}
    @Override public void updateAccountValue(String key, String value, String currency, String accountName) {}
    @Override public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {}
    @Override public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {}
    @Override public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {}
    @Override public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {}
    @Override public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {}
    @Override public void contractDetails(int reqId, ContractDetails contractDetails) {}
    @Override public void bondContractDetails(int reqId, ContractDetails contractDetails) {}
    @Override public void contractDetailsEnd(int reqId) {}
    @Override public void scannerDataEnd(int reqId) {}
    @Override public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {}
    @Override public void currentTime(long time) {}
    @Override public void fundamentalData(int reqId, String data) {}    
    @Override public void deltaNeutralValidation(int reqId, UnderComp underComp) {}
    @Override public void tickSnapshotEnd(int reqId) {}
    @Override public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {}
    @Override public void openOrderEnd() {}
    @Override public void accountDownloadEnd(String accountName) {}
    @Override public void execDetailsEnd(int reqId) {}
}
