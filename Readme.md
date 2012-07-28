IBAlgoTrader
===========

Code is released under the MIT License.

This is a project I whipped up to see if an algo trading idea I came up
with had any worth (it did not, in fact it had negative worth on my bank
account).  This is all the code I used to run my strategy.

For more info check out my site at : kwangbkim.com

Prerequisites:
===========
You need an account over at IB to make all this stuff work.  See their docs for getting it set up.

Project Descriptions
===========
	Backtest
		- Has some code for backtesting strategies without executing them
	
	Data
		- This project uses a SQLite database.  I've included my database so you can see the data model I was running off of.

	IB
		- Most of this code is from the Interactive Brokers API to access their trading platform and data.

	Stuff I added
		- com/ib/client/tws/DataMiner
			- used to access IB to get data
		- com/ib/client/tws/Trader
			- not complete, but the idea is to add stuff like executing 
			  trades, getting pnl etc.
		- com/ib/client/util 

	Statistics
		- Has some statistical analysis scripts, uses the sqllite database filled from with data from the IB api
	
	Trading
		- Runs the strategy to determine what to trade
		- Also contains the database layer  

Set up
===========
	Run MathHelper/build.xml 
		- creates MathHelper/lib/mather.jra
	
	Run SQLiteHelper/build.xml
		- creates SQLiteHelper/lib/sqlitehelper.jar
	
	Run Trading/build.xml
		- creates Trading/lib/trading.jar

	Run IB/UpdatePrices to update your database with new prices
	Run IB/TrendingStrategy to get a list of what to trade.