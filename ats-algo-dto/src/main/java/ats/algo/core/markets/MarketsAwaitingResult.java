package ats.algo.core.markets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ats.core.util.json.JsonUtil;

/**
 * holds the set of markets awaiting result.
 * 
 * @author Geoff
 *
 */
public class MarketsAwaitingResult implements Serializable {


    private static final long serialVersionUID = 1L;


    /*
     * key is the shortMarketKey
     */
    private Map<String, MarketGroupAwaitingResult> marketGroupsAwaitingResult;


    /**
     * class constructor
     */
    public MarketsAwaitingResult() {
        marketGroupsAwaitingResult = new HashMap<String, MarketGroupAwaitingResult>();
    }

    /**
     * protected - for copy() use only
     * 
     * @return
     */
    protected Map<String, MarketGroupAwaitingResult> getMarketGroupsAwaitingResult() {
        return marketGroupsAwaitingResult;
    }

    /**
     * protected - for copy() use only
     * 
     * @param marketGroupsAwaitingResult
     */
    protected void setMarketGroupsAwaitingResult(Map<String, MarketGroupAwaitingResult> marketGroupsAwaitingResult) {
        this.marketGroupsAwaitingResult = marketGroupsAwaitingResult;
    }


    /**
     * adds a new non derived market, creating a new entry in the map if necessary.
     * 
     * @param market
     */
    public void addNonDerivedMarket(Market market) {
        String key = market.getShortKey();
        if (!marketGroupsAwaitingResult.containsKey(key)) {
            MarketGroupAwaitingResult marketGroupAwaitingResult = new MarketGroupAwaitingResult(market);
            marketGroupsAwaitingResult.put(key, marketGroupAwaitingResult);
        } else {
            MarketGroupAwaitingResult marketGroupAwaitingResult = marketGroupsAwaitingResult.get(market.getShortKey());
            marketGroupAwaitingResult.addNonDerivedMarket(market);
        }
    }

    public void addDerivedMarket(String marketShortKey, Market market) {
        MarketGroupAwaitingResult marketGroupAwaitingResult = marketGroupsAwaitingResult.get(marketShortKey);
        marketGroupAwaitingResult.addDerivedMarket(market);
    }

    public Markets getBaseMarketsAwaitingResult() {
        Markets markets = new Markets();
        for (MarketGroupAwaitingResult marketGroupAwaitingResult : marketGroupsAwaitingResult.values()) {

            markets.addMarketWithShortKey(marketGroupAwaitingResult.getBaseMarket());
        }
        return markets;
    }

    // FIXME JIN ADDED, currently add all markets
    public Markets getAllMarketsAwaitingResult() {
        Markets markets = new Markets();
        for (MarketGroupAwaitingResult marketGroupAwaitingResult : marketGroupsAwaitingResult.values()) {
            Market baseMarket = marketGroupAwaitingResult.getBaseMarket();

            MarketGroupAwaitingResult markets2 = marketGroupsAwaitingResult.get(baseMarket.getShortKey());
            Map<String, Market> allInGroup = markets2.getAllMarkets();

            for (Map.Entry<String, Market> market : allInGroup.entrySet()) {
                markets.addMarketWithFullKey(market.getValue());
            }
        }
        return markets;
        // use stream is slower
        // Map<String, Market> map = marketGroupsAwaitingResult.entrySet().parallelStream()
        // .map(x -> ((Map.Entry<String, MarketGroupAwaitingResult>) x).getValue().getAllMarkets()
        // .entrySet())
        // .flatMap(x -> x.stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //
        // markets.setMarkets(map);
    }

    public void updateMarketsAwaitingResultSelections(String fullKey, String selectionIn) {

        MarketGroupAwaitingResult marketGroup = marketGroupsAwaitingResult.get(fullKey);
        /*
         * Only update the base market here
         */
        Market market = marketGroup.getBaseMarket();

        removeSelection(market, selectionIn);


        Map<String, Market> marketMap = marketGroup.getNonDerivedMarkets();

        for (Entry<String, Market> entryMarket : marketMap.entrySet()) {
            if (entryMarket.getKey().equals(fullKey)) {
                /* Find the selection and remove */
                removeSelection(entryMarket.getValue(), selectionIn);
            }

        }

    }

    private void removeSelection(Market market, String selectionIn) {
        Map<String, Double> selections = market.getSelectionsProbs();

        for (Iterator<Entry<String, Double>> it = selections.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Double> entry = it.next();
            if (entry.getKey().equals(selectionIn)) {
                it.remove();
            }
        }
    }

    public Markets getNonDerivedMarketsReadyToResult(ResultedMarkets resultedBaseMarkets) {
        Markets markets = new Markets();
        for (ResultedMarket resultedMarket : resultedBaseMarkets) {
            MarketGroupAwaitingResult marketGroupAwaitingResult =
                            marketGroupsAwaitingResult.get(resultedMarket.getShortKey());
            for (Market market : marketGroupAwaitingResult.getNonDerivedMarkets().values()) {
                markets.addMarketWithFullKey(market);
            }
        }
        return markets;
    }

    public Markets getImmediateMarketsReadyToResult(Markets baseMarkets) {
        Markets immediateMarkets = new Markets();
        for (Market market : baseMarkets) {
            MarketGroupAwaitingResult marketGroupAwaitingResult = marketGroupsAwaitingResult.get(market.getShortKey());
            for (Market market1 : marketGroupAwaitingResult.getNonDerivedMarkets().values())
                immediateMarkets.addMarketWithFullKey(market1);
        }
        return immediateMarkets;
    }

    public Map<String, Market> getDerivedMarketsAwaitingResult(String baseMarketShortKey) {
        MarketGroupAwaitingResult marketGroupAwaitingResult = marketGroupsAwaitingResult.get(baseMarketShortKey);
        return marketGroupAwaitingResult.getDerivedMarkets();
    }

    /*
     * creates a copy where the new points to the same underlying marketGroupAwaitingResult object as the old
     */
    public MarketsAwaitingResult copy() {
        MarketsAwaitingResult cc = new MarketsAwaitingResult();
        Map<String, MarketGroupAwaitingResult> ccx = cc.getMarketGroupsAwaitingResult();
        for (Entry<String, MarketGroupAwaitingResult> e : marketGroupsAwaitingResult.entrySet()) {
            String shortKey = e.getKey();
            MarketGroupAwaitingResult marketGroupAwaitingResult = e.getValue();
            ccx.put(shortKey, marketGroupAwaitingResult);
        }
        return cc;
    }

    public void removeEntry(String shortKey) {
        this.marketGroupsAwaitingResult.remove(shortKey);
    }

    public int size() {
        return marketGroupsAwaitingResult.size();
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
        // StringBuilder sb = new StringBuilder();
        // sb.append("MarketsAwaitingResult = [");
        // for (Entry<String, MarketGroupAwaitingResult> e : marketGroupsAwaitingResult.entrySet()) {
        // String shortKey = e.getKey();
        // MarketGroupAwaitingResult marketGroupAwaitingResult = e.getValue();
        // sb.append("key: " + shortKey + "[");
        // sb.append("baseMarket: " + marketGroupAwaitingResult.getBaseMarket().getFullKey());
        // sb.append("\nNon derived markets: [");
        // for (String key : marketGroupAwaitingResult.getNonDerivedMarkets().keySet())
        // sb.append(key + " ");
        // sb.append("]\n");
        // sb.append("\nDerived markets: [");
        // for (String key : marketGroupAwaitingResult.getDerivedMarkets().keySet())
        // sb.append(key + " ");
        // sb.append("]\n");
        // }
        // sb.append("]");
        // return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketGroupsAwaitingResult == null) ? 0 : marketGroupsAwaitingResult.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketsAwaitingResult other = (MarketsAwaitingResult) obj;
        if (marketGroupsAwaitingResult == null) {
            if (other.marketGroupsAwaitingResult != null)
                return false;
        } else if (!marketGroupsAwaitingResult.equals(other.marketGroupsAwaitingResult))
            return false;
        return true;
    }


}
