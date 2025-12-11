package ats.algo.core.triggerparamfind.tradingrules;

import java.util.HashMap;
import java.util.Map;

/**
 * cache of Market prices. Key is the priceSource
 * 
 * @author gicha
 *
 */
class MarketPricesCacheForType {

    private Map<String, MarketPricesCacheForTypeSource> cacheForType;

    MarketPricesCacheForType() {
        cacheForType = new HashMap<String, MarketPricesCacheForTypeSource>();
    }

    Map<String, MarketPricesCacheForTypeSource> getCache() {
        return cacheForType;
    }


}
