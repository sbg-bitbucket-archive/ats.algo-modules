package ats.algo.core.comparetomarket;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

/**
 * implements a set of Market prices which are to be used for param finding
 *
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPrices implements Iterable<MarketPrice>, Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, MarketPrice> marketPrices;
    private double sourceWeight;

    public MarketPrices() {
        sourceWeight = 1;
        marketPrices = new TreeMap<String, MarketPrice>();
    }

    public Map<String, MarketPrice> getMarketPrices() {
        return marketPrices;
    }

    public void setMarketPrices(Map<String, MarketPrice> marketPrices) {
        this.marketPrices = marketPrices;
    }



    /**
     * adds a market to the set of markets, generating the unique key
     *
     * @param marketPrice
     */
    public void addMarketPrice(MarketPrice marketPrice) {
        String key = marketPrice.getKey();
        marketPrices.put(key, marketPrice);
    }

    /**
     * remove market for specified marketType and sequenceId
     *
     * @param marketType
     * @param sequenceId
     */
    public void removeMarketPrice(String marketType, String sequenceId) {
        String key = marketType + "_" + sequenceId;
        marketPrices.remove(key);
    }

    /**
     * remove price associated with this key
     *
     * @param key
     */
    public void removeMarketPrice(String key) {
        marketPrices.remove(key);
    }

    /**
     * gets market for specified marketType and sequenceId
     *
     * @param marketType
     * @param sequenceId
     * @return
     */
    public MarketPrice get(String marketType, String sequenceId) {
        String key = marketType + "_" + sequenceId;
        return marketPrices.get(key);
    }

    /**
     * gets market for specified key
     *
     * @param key
     * @return
     */
    public MarketPrice get(String key) {
        return marketPrices.get(key);
    }

    /**
     * returns the weight that should be given to the prices emanating from this source
     *
     * @return
     */
    public double getSourceWeight() {
        return sourceWeight;
    }

    /**
     * sets the weight that should be applied to prices emanating from this source during the param finding process
     * absolute values don't matter - what is important is relative size. e.g. a source with a weight of 2 will have
     * twice the effect on param finding than a source with weight 1. if set to 0 this source is ignored.
     *
     * @param sourceWeight
     */
    public void setSourceWeight(double sourceWeight) {
        this.sourceWeight = sourceWeight;
    }

    /**
     * 
     * @param marketType
     * @return
     */
    public boolean contains(String marketType) {
        for (MarketPrice marketPrice : marketPrices.values()) {
            if (marketPrice.getType().equals(marketType))
                return true;
        }
        return false;
    }

    // /**
    // * Outputs all markets and selections in formatted form to printable string
    // */
    // @Override
    // public String toString() {
    // String marketStr = String.format(MarketPrice.pricesFirstLineFormat, "key", "lineId", "marketDescription");
    // String hdrString = String.format(MarketPrice.pricesSelectionsFormat, marketStr, "Selections",
    // "Probabilities(Prices)");
    // String result = hdrString;
    // String marketHdrStr = String.format(MarketPrice.pricesFirstLineFormat, "---", "------", "-----------",
    // "-----------------");
    // hdrString = String.format(MarketPrice.pricesSelectionsFormat, marketHdrStr, "----------",
    // "---------------------");
    // result += hdrString;
    //
    // for (Map.Entry<String, MarketPrice> entry : marketPrices.entrySet()) {
    // MarketPrice mkt = entry.getValue();
    // result += mkt.toString();
    // }
    // result += "\n";
    // return result;
    // }



    @Override
    public Iterator<MarketPrice> iterator() {
        return marketPrices.values().iterator();
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // String s = "sourceWeight=" + sourceWeight + " marketPrices=[";
        // for (MarketPrice marketPrice : marketPrices.values()) {
        // s += "\n" + marketPrice.toString();
        // }
        // s += "]\n";
        // return s;
    }

    public int size() {
        return marketPrices.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketPrices == null) ? 0 : marketPrices.hashCode());
        long temp;
        temp = Double.doubleToLongBits(sourceWeight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        MarketPrices other = (MarketPrices) obj;
        if (marketPrices == null) {
            if (other.marketPrices != null)
                return false;
        } else if (!marketPrices.equals(other.marketPrices))
            return false;
        if (Double.doubleToLongBits(sourceWeight) != Double.doubleToLongBits(other.sourceWeight))
            return false;
        return true;
    }



}
