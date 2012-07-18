# date : june 2011
# this program calculates each correlation in relation to the Vanguard
# all world ETF (ticker VT)

ProcessDate <- commandArgs(TRUE)
print(ProcessDate)

# SQLite database setup
library("DBI")
library("RSQLite")
driver <- dbDriver("SQLite")
con <- dbConnect(driver, "/Users/Kwang/Investment Management/Database/Database.sqlite")

# clear out any entries for todays
deleteQuery <- paste("delete from VTCorrelations where Date = '", ProcessDate, "'", sep="")
dbSendQuery(con, deleteQuery)

# get total list of pairs
tickerList <- dbGetQuery(con, "select * from Symbol order by Id")

InsertCorrelation <- function(data)
{
	sql <- "insert into VTCorrelations(Date, SymbolId, Correlation) values(?,?,?)"
	#print(data)
	dbBeginTransaction(con)
	dbGetPreparedQuery(con, sql, data.frame(data))
	dbCommit(con)
}

# get VT prices
vtDailyPriceQuery <- "select Close from DailyPrices where SymbolId = 51 order by Date desc limit 7"
vtDailyPrices <- dbGetQuery(con, vtDailyPriceQuery)
print(vtDailyPrices)

i <- 1
while (i < nrow(tickerList))
{
	symbolId <- tickerList[i,1]
	
	#print(paste(symbolId))
	
	dailyQuery <- paste("select Close from DailyPrices where SymbolId = ", 
		symbolId,
		"order by Date desc limit 7"
	)
	dailyPrices <- dbGetQuery(con, dailyQuery)
	#print(dailyPrices)
	corr <- cor(dailyPrices, vtDailyPrices)
	
	data <- data.frame(Date=ProcessDate, SymbolId=symbolId, Correlation=round(corr,4))
	InsertCorrelation(data)
	i <- i + 1
}

dbDisconnect(con)
