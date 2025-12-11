package ats.algo.core.markets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MarketsMetaData {

    private Map<String, MarketMetaData> map;

    public MarketsMetaData() {
        map = new HashMap<String, MarketMetaData>();
    }

    public void addMarket(Market market) {
        if (market.getCategory() == MarketCategory.GENERAL)
            return;
        String mktType = market.getType();
        MarketMetaData marketMetaData = map.get(mktType);
        if (marketMetaData == null) {
            String balancedLineId = market.getBalancedLineId();
            if (balancedLineId == null)
                balancedLineId = market.generateBalancedLineId();
            marketMetaData = new MarketMetaData(market.getFullKey(), balancedLineId);
            map.put(mktType, marketMetaData);
        } else
            marketMetaData.addKey(market.getFullKey());
    }

    public String getBalancedLineForType(String mktType) {
        MarketMetaData marketMetaData = map.get(mktType);
        if (marketMetaData == null)
            return null;
        else
            return marketMetaData.getBalancedLineId();
    }

    public Set<String> getAllKeysForMarketType(String mktType) {
        MarketMetaData marketMetaData = map.get(mktType);
        if (marketMetaData == null)
            return null;
        else
            return marketMetaData.getKeysForLineMarkets();
    }

    public Set<String> getAllLineMarketTypes() {
        return map.keySet();
    }



    @Override
    public String toString() {
        return "MarketsMetaData [map=" + map + "]";
    }



    private class MarketMetaData {
        private String balancedLineId;
        private Set<String> keysForLineMarkets;

        public MarketMetaData(String fullKey, String balancedLineId) {
            keysForLineMarkets = new HashSet<String>();
            keysForLineMarkets.add(fullKey);
            this.balancedLineId = balancedLineId;
        }

        String getBalancedLineId() {
            return balancedLineId;
        }


        Set<String> getKeysForLineMarkets() {
            return keysForLineMarkets;
        }

        void addKey(String key) {
            keysForLineMarkets.add(key);
        }

        @Override
        public String toString() {
            return "MarketMetaData [balancedLineId=" + balancedLineId + ", keysForLineMarkets=" + keysForLineMarkets
                            + "]";
        }


    }



}
