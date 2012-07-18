# get param
PairId <- commandArgs(TRUE)

library("RSQLite")
driver <- dbDriver("SQLite")
con <- dbConnect(driver, "/Users/Kwang/Documents/workspace/Pairs/src/database/Database.sqlite")

# get pair row
pairrow <- dbGetQuery(con, paste("select * from CommodityPairs where Id=", PairId))

desc <- pairrow[[4]][1]
lsym <- pairrow[[2]][1]
rsym <- pairrow[[3]][1]

print(paste(desc, lsym, rsym))

dailyquery <- "select Close from DailyPrices where SymbolId=? order by Date desc limit 60"

ldailyprices <- dbGetPreparedQuery(con, dailyquery, data.frame(data.frame(lsym)))
rdailyprices <- dbGetPreparedQuery(con, dailyquery, data.frame(data.frame(rsym)))

hourlyquery <- "select Close from HourlyPrices where SymbolId=? order by Date desc, Hour limit 45"

lhourlyprices <- dbGetPreparedQuery(con, hourlyquery, data.frame(data.frame(lsym)))
rhourlyprices <- dbGetPreparedQuery(con, hourlyquery, data.frame(data.frame(rsym)))

dtitle <- paste("daily : ", desc)
htitle <- paste("hourly: ", desc)
xtitle <- substr(desc, 0, 3)
ytitle <- substr(desc, 5, 10)

attach(mtcars)
par(mfrow=c(2,1))
plot(data.frame(ldailyprices, rdailyprices), main=dtitle, xlab=xtitle, ylab=ytitle)
plot(data.frame(lhourlyprices, rhourlyprices), main=htitle, xlab=xtitle, ylab=ytitle)
