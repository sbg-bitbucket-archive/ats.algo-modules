package ats.algo.core.triggerparamfind.tradingrules;

import java.util.ArrayList;
import java.util.List;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.genericsupportfunctions.SimpleCache;

/**
 * the key for elements in this cache is the sequenceId
 * 
 * @author gicha
 *
 */
class MarketPricesCacheForTypeSource {

    private static final int MAX_NO_SEQ_IDs_PER_MARKET_TYPE = 8;

    private SimpleCache<String, MarketPricesCacheForTypeSourceSeqId> cache;

    MarketPricesCacheForTypeSource() {
        cache = new SimpleCache<String, MarketPricesCacheForTypeSourceSeqId>(MAX_NO_SEQ_IDs_PER_MARKET_TYPE);
    }

    SimpleCache<String, MarketPricesCacheForTypeSourceSeqId> getCache() {
        return cache;
    }


    void add(long timeStamp, MarketPrice marketPrice) {
        String seqId = marketPrice.getSequenceId();
        MarketPricesCacheForTypeSourceSeqId marketPricesCacheForTypeSourceSeqId = cache.get(seqId);
        if (marketPricesCacheForTypeSourceSeqId == null) {
            marketPricesCacheForTypeSourceSeqId = new MarketPricesCacheForTypeSourceSeqId();
            cache.add(seqId, marketPricesCacheForTypeSourceSeqId);
        }
        marketPricesCacheForTypeSourceSeqId.add(timeStamp, marketPrice);
    }

    /**
     * gets at most one market for each type/seq_id
     * 
     * @param cutOffTime
     * @return
     */
    List<MarketPrice> getLatestMarketPrices(long cutOffTime) {
        List<MarketPrice> marketPrices = new ArrayList<MarketPrice>();
        for (String seqId : cache.keyList()) {
            MarketPricesCacheForTypeSourceSeqId seqIdCache = cache.get(seqId);
            Long priceTime = seqIdCache.mostRecentKey();
            if (priceTime != null) {
                long lPriceTime = (long) priceTime;
                if (lPriceTime > cutOffTime) {
                    marketPrices.add(seqIdCache.get(lPriceTime));
                }
            }
        }
        return marketPrices;
    }
}
