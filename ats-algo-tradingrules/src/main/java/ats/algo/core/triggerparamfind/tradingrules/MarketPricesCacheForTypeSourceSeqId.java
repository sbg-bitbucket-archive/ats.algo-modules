package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.genericsupportfunctions.SimpleCache;

/**
 * cache of market prices for a particular market type and source. the key is the timestamp
 * 
 * @author gicha
 *
 */
class MarketPricesCacheForTypeSourceSeqId extends SimpleCache<Long, MarketPrice> {

    private static final int MAX_NO_PRICES_PER_SEQ_ID = 10;

    MarketPricesCacheForTypeSourceSeqId() {
        super(MAX_NO_PRICES_PER_SEQ_ID);
    }
}
