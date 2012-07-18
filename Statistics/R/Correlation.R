# date : spring 2011
# this program is for the old commodity pairs correlation strategy.  it
# calculates each correlation against each other security.

ProcessDate <- commandArgs(TRUE)

# SQLite database setup
library("DBI")
library("RSQLite")
driver <- dbDriver("SQLite")
con <- dbConnect(driver, "/Users/Kwang/Documents/workspace/Pairs/src/database/Database.sqlite")

# get total list of pairs
CommodityPairsList <- dbGetQuery(con, "select * from CommodityPairs order by Id")

InsertCorrelation <- function(data)
{
	sql <- "insert into Correlations(PairId, Date, LongTermCoefficient, ShortTermCoefficient) values(?,?,?,?)"
	#print(data)
	dbBeginTransaction(con)
	dbGetPreparedQuery(con, sql, data.frame(data))
	dbCommit(con)
}

for (i in CommodityPairsList[[1]])
{
	leftSymbolId <- CommodityPairsList[[2]][i]
	rightSymbolId <- CommodityPairsList[[3]][i]
	
	print(paste(leftSymbolId, rightSymbolId))
	
	leftDailyQuery <- paste("select Close from DailyPrices where SymbolId = ", 
		leftSymbolId,
		"order by Date desc limit 60"
	)
	leftDailyPrices <- dbGetQuery(con, leftDailyQuery)
	print(leftDailyPrices)
	
	rightDailyQuery <- paste("select Close from DailyPrices where SymbolId = ", 
		rightSymbolId,
		"order by Date desc limit 60"
	)
	rightDailyPrices <- dbGetQuery(con, rightDailyQuery)
	print(rightDailyPrices)
	ltc <- cor(leftDailyPrices, rightDailyPrices)
	
	leftHourlyQuery <- paste("select Close from HourlyPrices where SymbolId = ", 
		leftSymbolId,
		"order by Date desc, Hour desc Limit 45"
	)
	leftHourlyPrices <- dbGetQuery(con, leftHourlyQuery)
	
	rightHourlyQuery <- paste("select Close from HourlyPrices where SymbolId = ", 
		rightSymbolId,
		"order by Date desc, Hour desc Limit 45"
	)
	rightHourlyPrices <- dbGetQuery(con, rightHourlyQuery)
	stc <- cor(leftHourlyPrices, rightHourlyPrices)
	
	pid <- CommodityPairsList[[1]][i]
	data <- data.frame(pairid=pid, date=ProcessDate, ltc=round(ltc,4), stc=round(stc,4))
	InsertCorrelation(data)
}

dbDisconnect(con)
