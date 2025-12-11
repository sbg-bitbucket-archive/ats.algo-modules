package ats.algo.core.markets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.common.SupportedSportType;
import ats.core.util.json.JsonUtil;

/**
 * Holds a set of Market objects
 * 
 * @author Geoff *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Markets implements Iterable<Market>, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Market> markets;

    private transient MarketsMetaData marketsMetaData;


    /**
     * Default constructor
     */
    public Markets() {
        markets = new TreeMap<String, Market>();
    }

    /**
     * get the map of market objects. The key is formed from the combination of market type and sequenceID
     * 
     * @return
     */
    public Map<String, Market> getMarkets() {
        return markets;
    }

    /**
     * set the map of market objects. The key is formed from the combination of market type and sequenceID
     * 
     * @param markets
     */
    public void setMarkets(Map<String, Market> markets) {
        this.markets = markets;
    }

    /**
     * sets the metaData for this set of markets. MetaData is volatile, so not serialized
     * 
     * @return
     */
    @JsonIgnore
    public MarketsMetaData getMarketsMetaData() {
        return marketsMetaData;
    }

    /**
     * gets the metaData for this set of markets. MetaData is volatile, so not serialized
     * 
     * @param marketsMetaData
     */
    @JsonIgnore
    public void setMarketsMetaData(MarketsMetaData marketsMetaData) {
        this.marketsMetaData = marketsMetaData;
    }

    /**
     * kept for backwards compatibility - use addMarketWithShortKey instead and sequence Id
     * 
     * @param market
     */
    @Deprecated
    public void addMarket(Market market) {
        addMarketWithShortKey(market);
    }

    /**
     * adds a market to the set of markets using the key constructed from type and sequence Id
     * 
     * @param market
     */
    public void addMarketWithShortKey(Market market) {
        if (market != null) {
            String key = market.getShortKey();
            markets.put(key, market);
        }
    }

    /**
     * adds a market to the set of markets using the key constructed from type, sequence Id and lineId
     */
    public void addMarketWithFullKey(Market market) {
        String key = market.getFullKey();
        markets.put(key, market);
    }

    /**
     * remove market for specified marketType where sequenceId is assumed to be "M"
     * 
     * @param marketType
     */
    public void removeMarket(String marketType) {
        removeMarket(marketType, "M");
    }

    /**
     * remove market for specified marketType and sequenceId
     * 
     * @param marketType
     * @param sequenceId
     */
    public void removeMarket(String marketType, String sequenceId) {
        String key = marketType + "_" + sequenceId;
        markets.remove(key);
    }

    /**
     * gets market for specified marketType where sequenceId is assumed to be "M"
     * 
     * @param marketType
     * @return
     */
    @JsonIgnore
    public Market get(String marketType) {
        return get(marketType, "M");
    }

    /**
     * gets market for specified marketType and sequenceId (may be for any lineId if OVUN or HANDICAP type market)
     * 
     * @param marketType
     * @param sequenceId
     * @return
     */
    @JsonIgnore
    public Market get(String marketType, String sequenceId) {
        for (Market market : markets.values()) {
            if (market.getType().equals(marketType) && market.getSequenceId().equals(sequenceId))
                return market;
        }
        return null;
    }

    @JsonIgnore
    public Market get(String marketType, String lineId, String sequenceId) {
        String key = marketType + "#" + lineId + "_" + sequenceId;
        return markets.get(key);
    }

    /**
     * 
     * @param key
     * @return
     */
    @JsonIgnore
    public Market getMarketForFullKey(String key) {
        return markets.get(key);
    }

    /**
     * 
     * @param marketType
     * @param sequenceId
     * @return
     */
    @JsonIgnore
    public Market getMarketForBalancedLine(String marketType, String sequenceId) {
        for (Market market : markets.values()) {
            if (marketType.equals(market.getType()) && sequenceId.equals(market.getSequenceId())) {
                return market.getMarketForLineId(market.generateBalancedLineId());
            }
        }
        return null;
    }

    /**
     * 
     * @param marketType
     * @param sequenceId
     * @param dLineId
     * @return
     */
    @JsonIgnore
    public Market getMarketForLineId(String marketType, String sequenceId, String lineId) {
        for (Market market : markets.values()) {
            if (marketType.equals(market.getType()) && sequenceId.equals(market.getSequenceId())) {
                return market.getMarketForLineId(lineId);
            }
        }
        return null;
    }

    /**
     * 
     * makes a copy if itself
     * 
     * @return the new copy
     */
    public Markets copy() {
        Markets marketsCopy = new Markets();
        for (Entry<String, Market> entry : markets.entrySet()) {
            Market market = entry.getValue();
            marketsCopy.addMarketWithShortKey(market.copy());
        }
        return marketsCopy;
    }

    /**
     * returns no of Market objects in Markets
     * 
     * @return
     */
    public int size() {
        return markets.size();
    }

    /**
     * adds all the specified markets to this object
     * 
     * @param value
     */
    public void putAll(Markets value) {
        markets.putAll(value.markets);
    }

    @JsonIgnore
    @Deprecated
    public void setSupportedSportType(SupportedSportType type) {
        /*
         * do nothing - just needed to avoid BS tennis model run time error
         */
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // String s = "Markets [eventId=" + eventId + ", requestId=" + requestId
        // + ", requestTime=" + requestTime
        // + ", eventTier=" + eventTier + ", supportedSportType=" +
        // supportedSportType
        // + ", fatalCalcError=" + fatalCalcError + ", markets= [";
        // for (Entry<String, Market> e : markets.entrySet()) {
        // s += "\n" + e.getKey() + ": " + e.getValue().toString();
        // }
        // s += "]\n";
        // return s;
    }

    @Override
    public Iterator<Market> iterator() {
        return markets.values().iterator();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
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
        Markets other = (Markets) obj;
        if (markets == null) {
            if (other.markets != null)
                return false;
        } else if (!markets.equals(other.markets))
            return false;
        return true;
    }

    public void addDerivedMarkets(DataToGenerateStaticMarkets dataToGenerateStaticMarkets) {
        List<List<Market>> newMktsList = new ArrayList<List<Market>>();
        /*
         * first generate all the derived markets
         */
        for (Market market : markets.values()) {
            List<Market> derivedMarkets =
                            market.generateAllDerivedMarketsSpecifiedByTradingRules(dataToGenerateStaticMarkets);
            newMktsList.add(derivedMarkets);
        }
        /*
         * add to this set of markets
         */
        for (List<Market> derivedMarkets : newMktsList) {
            for (Market market : derivedMarkets) {
                markets.put(market.getShortKey(), market);
            }
        }

    }

    /**
     * generates a set containing the keys for all of the markets in this object
     * 
     * @return the set of keys.
     */
    @JsonIgnore
    public Set<String> getMarketKeys() {
        return new HashSet<String>(markets.keySet());
    }

}
