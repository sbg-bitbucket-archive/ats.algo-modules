package ats.algo.core.triggerparamfind.tradingrules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;

/**
 * Cache of MarketPrices. The structure is: marketType.sourceName.timeStamp.
 * 
 * @author gicha
 *
 */
public class MarketPricesCache {

    static final int DELAY_POST_INCIDENT_FOR_PRICES_TO_UPDATE = 1000;

    private Map<String, MarketPricesCacheForType> cache;



    public MarketPricesCache() {
        cache = new HashMap<String, MarketPricesCacheForType>();
    }


    /**
     * adds this set of prices to the cache
     * 
     * @param marketPricesList
     */
    public void addPricesToCache(MarketPricesList marketPricesList, long timeStamp) {
        for (Entry<String, MarketPrices> e : marketPricesList.entrySet()) {
            String sourceName = e.getKey();
            for (MarketPrice marketPrice : e.getValue().getMarketPrices().values()) {
                addCacheEntry(sourceName, marketPrice, timeStamp);
            }
        }
    }

    private void addCacheEntry(String sourceName, MarketPrice marketPrice, long now) {
        String marketType = marketPrice.getType();
        MarketPricesCacheForType marketPricesCacheForType = cache.get(marketType);
        if (marketPricesCacheForType == null) {
            marketPricesCacheForType = new MarketPricesCacheForType();
            cache.put(marketType, marketPricesCacheForType);
        }
        Map<String, MarketPricesCacheForTypeSource> mapMarketPricesCacheForTypeSource =
                        marketPricesCacheForType.getCache();
        MarketPricesCacheForTypeSource marketPricesCacheForTypeSource =
                        mapMarketPricesCacheForTypeSource.get(sourceName);
        if (marketPricesCacheForTypeSource == null) {
            marketPricesCacheForTypeSource = new MarketPricesCacheForTypeSource();
            mapMarketPricesCacheForTypeSource.put(sourceName, marketPricesCacheForTypeSource);
        }
        marketPricesCacheForTypeSource.add(now, marketPrice);
    }


    @SuppressWarnings("unused")
    private static final long PRICES_TIMEOUT = 60000L;

    /**
     * gets the most recent set of marketPrices. Prices older than PRICES_TIMEOUT are ignored.
     * 
     * @return
     */
    public MarketPricesList getMostRecentPrices() {
        // TODO = GC put this logic back in
        // long now = System.currentTimeMillis();
        // return getPricesSinceTime(now - PRICES_TIMEOUT);
        return getPricesSinceTime(0);
    }


    /**
     * gets the most recent set of marketPrices received since the last matchIncident was received. MarketPrices with an
     * earlier timestamp are ignored
     * 
     * @return
     */
    public MarketPricesList getPricesSinceTime(long cutOffTime) {
        MarketPricesList marketPricesList = new MarketPricesList();
        for (Entry<String, MarketPricesCacheForType> e : cache.entrySet()) {
            MarketPricesCacheForType marketPricesCacheForType = e.getValue();
            for (Entry<String, MarketPricesCacheForTypeSource> e2 : marketPricesCacheForType.getCache().entrySet()) {
                String sourceName = e2.getKey();
                MarketPricesCacheForTypeSource marketPricesCacheForTypeSource = e2.getValue();
                List<MarketPrice> marketPrices = marketPricesCacheForTypeSource.getLatestMarketPrices(cutOffTime);
                for (MarketPrice marketPrice : marketPrices) {
                    marketPricesList.addMarketPrice(sourceName, marketPrice);
                }
            }
        }
        return marketPricesList;
    }
}
