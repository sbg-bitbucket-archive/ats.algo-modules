package ats.algo.core.comparetomarket;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
import java.util.TreeMap;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.PriceSourceWeights;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;

/**
 * Holds the collection of competitor market prices used for param finding. Each entry represents data from a single
 * market pricing source. Structure is sourceName.
 *
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPricesList extends AtsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean generateDetailedParamFindLog;



    public Map<String, MarketPrices> getMarketPricesList() {
        return marketPricesList;
    }

    public void setMarketPricesList(Map<String, MarketPrices> marketPricesList) {
        this.marketPricesList = marketPricesList;
    }



    /**
     * if true then detailed log will be created when param finding against this marketPricesList
     * 
     * @return
     */
    public boolean isGenerateDetailedParamFindLog() {
        return generateDetailedParamFindLog;
    }

    /**
     * if true then detailed log will be created when param finding against this marketPricesList
     * 
     * @param generateDetailedParamFindLog
     */
    public void setGenerateDetailedParamFindLog(boolean generateDetailedParamFindLog) {
        this.generateDetailedParamFindLog = generateDetailedParamFindLog;
    }



    private Map<String, MarketPrices> marketPricesList;

    public MarketPricesList() {
        marketPricesList = new TreeMap<String, MarketPrices>();
        generateDetailedParamFindLog = false;
    }

    /**
     * adds a market price to the list of market Prices
     * 
     * @param marketType
     * @param sourceName
     * @param marketPrice
     */
    public void addMarketPrice(String sourceName, MarketPrice marketPrice) {
        MarketPrices marketPrices = marketPricesList.get(sourceName);
        if (marketPrices == null) {
            marketPrices = new MarketPrices();
            marketPricesList.put(sourceName, marketPrices);
        }
        marketPrices.addMarketPrice(marketPrice);


    }


    public boolean containsKey(Object key) {
        return marketPricesList.containsKey(key);
    }


    public Set<java.util.Map.Entry<String, MarketPrices>> entrySet() {
        return marketPricesList.entrySet();
    }


    public MarketPrices get(Object key) {
        return marketPricesList.get(key);
    }

    public MarketPrices put(String key, MarketPrices value) {
        return marketPricesList.put(key, value);
    }

    public int size() {
        return marketPricesList.size();
    }

    public Collection<MarketPrices> values() {
        return marketPricesList.values();
    }



    /**
     * checks the supplied sequenceId on each supplied market and if need be converts to the correct format using the
     * method matchState.convertSequenceIdToAlgoMgrStd.
     * 
     * If matchState.convertSequenceIdToAlgoMgrStd returns "Sx" then the market this price refers to is invalid and is
     * removed from marketPricesList. e.g. e.g. "G13" in tennis will refer to the tie break not the game winner market
     * 
     * @param matchState
     */
    public void convertSequenceIdsToAlgoMgrStdAndFilter(MatchState matchState) {
        for (MarketPrices marketPrices : marketPricesList.values()) {
            HashMap<String, MarketPrice> updatedMarketPrices = new HashMap<String, MarketPrice>();
            for (MarketPrice marketPrice : marketPrices) {
                String sequenceId = marketPrice.getSequenceId();
                String newSequenceId = matchState.convertSequenceIdToAlgoMgrStd(sequenceId);
                if (newSequenceId != null) {
                    updatedMarketPrices.put(marketPrice.getKey(), marketPrice);
                    marketPrice.setSequenceId(newSequenceId);
                    String s = String.format("Supplied sequence id: %s for market %s converted to AlgoMgr standard: %s",
                                    sequenceId, marketPrice.getType(), newSequenceId);
                    debug(s);
                }
            }

            /**
             * need to correct the keys for any sequence Id's that have changed or filter out those where seqId = "Sx"
             */
            for (Entry<String, MarketPrice> entry : updatedMarketPrices.entrySet()) {
                String oldKey = entry.getKey();
                MarketPrice marketPrice = entry.getValue();
                marketPrices.removeMarketPrice(oldKey);
                if (marketPrice.getSequenceId().equals("Sx")) {
                    String s = String.format(
                                    "market %s with sequenceId 'Sx' not valid given score.  Removed from marketPricesList",
                                    marketPrice.getType());
                    debug(s);;
                } else {
                    marketPrices.addMarketPrice(marketPrice);
                }
            }
            // debug (String.format("No of sequenceId's converted: %d", updatedMarketPrices.size()));
            for (Entry<String, MarketPrice> entry : updatedMarketPrices.entrySet()) {
                MarketPrice marketPrice = entry.getValue();
                debug(marketPrice.toString());
            }
        }
    }

    /**
     * remove from this marketPricesList any sources that have zero weight
     */
    public void filterOutZeroWeightSources() {
        Set<String> zeroWeightSources = new HashSet<String>();
        for (Entry<String, MarketPrices> e : marketPricesList.entrySet()) {
            if (e.getValue().getSourceWeight() == 0.0) {
                zeroWeightSources.add(e.getKey());
            }
        }
        for (String key : zeroWeightSources)
            marketPricesList.remove(key);
    }



    public void filterOutUnwantedPrices(Set<String> marketTypesToFilterOut) {
        for (MarketPrices marketPrices : marketPricesList.values()) {
            for (MarketPrice marketPrice : marketPrices) {
                if (marketTypesToFilterOut.contains(marketPrice.getType()))
                    marketPrice.setValid(false);
            }
        }

    }



    /**
     * set the weights for each source and market Type in marketPricesList from the supplied list of priceSource
     * weights. Default value is sourceWeight = 1 , marketWeight = 1;
     * 
     * @param priceSourceWeights
     */
    public void setWeights(PriceSourceWeights priceSourceWeights, Map<String, Double> marketWeights) {
        Double defaultMarketWeight = 1.0;
        for (Entry<String, MarketPrices> entry : marketPricesList.entrySet()) {
            String key = entry.getKey().toUpperCase();
            MarketPrices marketPrices = entry.getValue();
            marketPrices.setSourceWeight(priceSourceWeights.getPriceSourceWeight(key));
            for (MarketPrice marketPrice : marketPrices) {
                String marketType = marketPrice.getType();
                Double marketWeight = marketWeights.get(marketType);
                if (marketWeight == null)
                    marketWeight = defaultMarketWeight;
                else {
                    marketPrice.setMarketWeight(marketWeight);
                }
            }
        }

    }

    public boolean pricesSame(MarketPricesList otherMarketPricesList) {
        if (otherMarketPricesList == null) {
            return false;
        }
        for (Entry<String, MarketPrices> e : this.getMarketPricesList().entrySet()) {
            MarketPrices thisMarketPrices = e.getValue();
            MarketPrices otherMarketPrices = otherMarketPricesList.getMarketPricesList().get(e.getKey());
            if (otherMarketPrices == null)
                return false;
            for (Entry<String, MarketPrice> e2 : thisMarketPrices.getMarketPrices().entrySet()) {
                MarketPrice thisMarketPrice = e2.getValue();
                MarketPrice otherMarketPrice = otherMarketPrices.getMarketPrices().get(e2.getKey());
                if (otherMarketPrice == null)
                    return false;
                if (thisMarketPrice.getLineId() == null) {
                    if (otherMarketPrice.getLineId() != null)
                        return false;
                } else {
                    if (!thisMarketPrice.getLineId().equals(otherMarketPrice.getLineId()))
                        return false;
                }
                if (thisMarketPrice.getSequenceId() == null) {
                    if (otherMarketPrice.getSequenceId() != null)
                        return false;
                } else {
                    if (!thisMarketPrice.getSequenceId().equals(otherMarketPrice.getSequenceId()))
                        return false;
                }
                Map<String, Double> otherSelections = otherMarketPrice.getSelections();
                for (Entry<String, Double> e3 : thisMarketPrice.getSelections().entrySet()) {
                    Double thisPrice = e3.getValue();
                    Double otherPrice = otherSelections.get(e3.getKey());
                    if (!thisPrice.equals(otherPrice))
                        return false;
                }
            }
        }
        return true;
    }


    @Override
    public String toString() {

        return JsonUtil.marshalJson(this);
        // String s = "MarketPricesList [eventId=" + eventId + ", requestId=" + requestId + ", marketPricesList=[\n";
        //
        // for (Entry<String, MarketPrices> e : marketPricesList.entrySet()) {
        // s += e.getKey() + "= [" + e.getValue().toString();
        // }
        // s += "]\n";
        // return s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketPricesList == null) ? 0 : marketPricesList.hashCode());
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
        MarketPricesList other = (MarketPricesList) obj;
        if (marketPricesList == null) {
            if (other.marketPricesList != null)
                return false;
        } else if (!marketPricesList.equals(other.marketPricesList))
            return false;
        return true;
    }

}
