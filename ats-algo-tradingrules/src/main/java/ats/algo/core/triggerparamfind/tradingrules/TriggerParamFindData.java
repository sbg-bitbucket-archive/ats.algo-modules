package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.TraderParamFindResultsDescription;
import ats.algo.core.tradingrules.PriceSourceWeights;

public class TriggerParamFindData {

    private MarketPricesCache marketPricesCache;
    private MatchIncidentResultCache matchIncidentResultCache;
    private ParamFindsCache paramFindsCache;
    private PriceSourceWeights priceSourceWeights;
    private MarketPricesList lastUsedMarketPricesList;
    private TraderParamFindResultsDescription lastSuccessfulTraderParamFindResultsDescription;
    private long logEntryTimer;
    private String logEntry;

    private static final long TIME_BETWEEN_LOG_ENTRIES_MS = 120000;

    public TriggerParamFindData() {
        marketPricesCache = new MarketPricesCache();
        matchIncidentResultCache = new MatchIncidentResultCache();
        paramFindsCache = new ParamFindsCache();
        priceSourceWeights = new PriceSourceWeights();
        lastUsedMarketPricesList = new MarketPricesList();
        lastSuccessfulTraderParamFindResultsDescription = new TraderParamFindResultsDescription();
        logEntryTimer = 0;
    }

    public ParamFindsCache getParamFindsCache() {
        return paramFindsCache;
    }

    public MarketPricesCache getMarketPricesCache() {
        return marketPricesCache;
    }

    public MatchIncidentResultCache getMatchIncidentResultCache() {
        return matchIncidentResultCache;
    }

    public PriceSourceWeights getPriceSourceWeights() {
        return priceSourceWeights;
    }

    public void setPriceSourceWeights(PriceSourceWeights priceSourceWeights) {
        this.priceSourceWeights = priceSourceWeights;
    }

    public MarketPricesList getLastUsedMarketPricesList() {
        return lastUsedMarketPricesList;
    }

    public void setLastUsedMarketPricesList(MarketPricesList lastUsedMarketPricesList) {
        this.lastUsedMarketPricesList = lastUsedMarketPricesList;
    }

    public TraderParamFindResultsDescription getLastSuccessfulTraderParamFindResultsDescription() {
        return lastSuccessfulTraderParamFindResultsDescription;
    }

    public void setLastSuccessfulTraderParamFindResultsDescription(
                    TraderParamFindResultsDescription lastSuccessfulTraderParamFindResultsDescription) {
        this.lastSuccessfulTraderParamFindResultsDescription = lastSuccessfulTraderParamFindResultsDescription;
    }

    public long timeElapsedSinceLastParamFind() {
        long now = System.currentTimeMillis();
        return now - paramFindsCache.getLastPfTime();
    }

    public boolean lastParamFindRedOrAmber() {
        return paramFindsCache.lastParamFindRedOrAmber();
    }

    public void triggerParamFindAsap() {
        paramFindsCache.resetLastPfTime();
    }

    void setLogEntry(String logEntry) {
        this.logEntry = logEntry;
    }

    public String getLogEntry() {
        if (logEntry == null)
            return "";
        else
            return logEntry;
    }

    /**
     * if sufficient time has elapsed since the most recent log entry then reset timer and return true;
     * 
     * @return
     */
    public boolean timeToMakeLogEntry() {
        long now = System.currentTimeMillis();
        if (now - logEntryTimer > TIME_BETWEEN_LOG_ENTRIES_MS
                        && this.timeElapsedSinceLastParamFind() > TIME_BETWEEN_LOG_ENTRIES_MS) {
            logEntryTimer = now;
            return true;
        }
        return false;
    }

}
