package ats.algo.core.comparetomarket;

import java.io.Serializable;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.markets.MarketCategory;
import ats.core.util.json.JsonUtil;

/**
 * Holds a market price (including margin) obtained from a third party and to be used for param finding
 *
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean valid; // set to false if this market is not valid. Only
                           // used by param finding if true

    private String type;
    private String marketDescription; // text string useful for toString method,
                                      // but not used by param Finder
    private MarketCategory category; // "OVUN", "HCAP" or "GENERAL"
    private String lineId; // if "OVUN" or "HCAP" then the line number
                           // associated with this market in string format
                           // e.g. "4.5"
    private Map<String, Double> selections;
    private String sequenceId;
    private double marketWeight;
    private long timeStamp;
    private volatile String sanityCheckFailureReason;

    /**
     * 
     * @param type
     * @param marketDescription
     * @param category
     * @param lineId
     * @param sequenceId
     */
    public MarketPrice(String type, String marketDescription, MarketCategory category, String lineId,
                    String sequenceId) {
        this.type = type;
        this.marketDescription = marketDescription;
        this.category = category;
        this.lineId = lineId;
        this.valid = true;
        this.sequenceId = sequenceId;
        this.marketWeight = 1;
        selections = new TreeMap<String, Double>(); // holds price NOT prob
    }

    public MarketPrice(String type, String marketDescription, MarketCategory category) {
        this(type, marketDescription, category, null, "M");
    }

    public MarketPrice(String type, String marketDescription, MarketCategory category, String lineId) {
        this(type, marketDescription, category, lineId, "M");
    }

    public MarketPrice() {
        /*
         * required for json deserialization
         */
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarketDescription() {
        return marketDescription;
    }

    public void setMarketDescription(String marketDescription) {
        this.marketDescription = marketDescription;
    }

    public MarketCategory getCategory() {
        return category;
    }

    public void setCategory(MarketCategory category) {
        this.category = category;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public Map<String, Double> getSelections() {
        return selections;
    }

    public void setSelections(Map<String, Double> selections) {
        this.selections = selections;
    }

    /**
     * returns the default seq id ("M") if property has not been set
     * 
     * @return
     */
    public String getSequenceId() {
        if (sequenceId != null)
            return sequenceId;
        else
            return "M";
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public double getMarketWeight() {
        return marketWeight;
    }

    public void setMarketWeight(double marketWeight) {
        this.marketWeight = marketWeight;
    }

    public int size() {
        return selections.size();
    }

    public Set<Entry<String, Double>> entrySet() {
        return selections.entrySet();
    }

    public Double get(String selectionName) {
        return selections.get(selectionName);
    }

    public void put(String key, Double price) {
        selections.put(key, price);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * gets the reason a previous call to
     * 
     * @return
     */
    @JsonIgnore
    public String getSanityCheckFailureReason() {
        return sanityCheckFailureReason;
    }



    private static final double MAX_MARGIN_PER_SELECTION = 0.08;

    /**
     * checks selection prices for any obvious errors. Prices rejected if either:
     * 
     * - any selection price = 1.0
     * 
     * - average margin across all selections is > MAX_MARGIN_PER_SELECTION
     * 
     * property sanityCheckFailureReason is set if test fails
     * 
     * @return true if ok.
     */
    public boolean sanityCheckPrice() {
        double totalMargin = -1.0;
        for (double selectionPrice : selections.values()) {
            if (selectionPrice == 1.0) {
                sanityCheckFailureReason = "Price sanity check failed. One or more prices = 1.0";
                return false;
            }
            totalMargin += 1 / selectionPrice;
        }
        // boolean coral = true;
        // double totalMargin2 = 0;
        // if(coral){
        // for (double selectionPrice : selections.values()) {
        // if (selectionPrice == 1.0) {
        // sanityCheckFailureReason = "Price sanity check failed. One or more prices = 1.0";
        // return false;
        // }
        // totalMargin2 += (1/selectionPrice-SamDemarginAlgo.GetCoralMarginReverse(1/selectionPrice, 0.06, 0.015));
        // }
        //
        // System.out.println("Current Method Found Margin Per Selection "+totalMargin/selections.size()+" Corals Method
        // Found Margin Per Selection "+totalMargin2/selections.size());
        // }



        if (totalMargin < 0.0) {
            sanityCheckFailureReason =
                            String.format("Price sanity check failed. Total margin: %.3f is < 0.0", totalMargin);
            return false;
        }
        if (totalMargin > (selections.size() * MAX_MARGIN_PER_SELECTION)) {
            sanityCheckFailureReason = String.format(
                            "Price sanity check failed. Average margin per selection: %.3f exceeds max allowed: %.2f",
                            totalMargin / selections.size(), MAX_MARGIN_PER_SELECTION);
            return false;
        } else {
            sanityCheckFailureReason = null;
            return true;
        }
    }



    static String pricesFirstLineFormat = "%-16s%-8s%s";
    static String pricesSelectionsFormat = "%-60s%-32s%s\n";

    // @Override
    // public String toString() {
    //
    // String result = "Valid: " + valid + "|MktWeight:" + marketWeight +
    // "|Type: " + type + "|Description: " + marketDescription + "|Category: " +
    // category + "|LineId: " + lineId + "|SequenceId: "
    // + sequenceId + "|\n";
    // String marketStr = String.format(pricesFirstLineFormat, this.getKey(),
    // lineId, marketDescription);
    // if (!valid) {
    // result += String.format(pricesSelectionsFormat, marketStr, "No valid
    // selections given score", "");
    // } else {
    // int nSelections = selections.size();
    // String[] selection = new String[nSelections];
    // double[] prices = new double[nSelections];
    // int i = 0;
    // for (Map.Entry<String, Double> sEntry : selections.entrySet()) {
    // String sKey = sEntry.getKey();
    // selection[i] = sKey;
    // prices[i] = sEntry.getValue();
    // i++;
    // }
    // double[] probs = MarginingV10.removeMargin(prices);
    // switch (nSelections) {
    // case 2:
    // String selections = String.format("%s / %s", selection[0], selection[1]);
    // String price0 = String.format("%.2f(%.2f)", prices[0], probs[0]);
    // String price1 = String.format("%.2f(%.2f)", prices[1], probs[1]);
    // String pricesStr = String.format("%-12s/ %s", price0, price1);
    // result += String.format(pricesSelectionsFormat, marketStr, selections,
    // pricesStr);
    // break;
    // case 3:
    // selections = String.format("%s / %s / %s", selection[0], selection[1],
    // selection[2]);
    // price0 = String.format("%.2f(%.2f)", prices[0], probs[0]);
    // price1 = String.format("%.2f(%.2f)", prices[1], probs[1]);
    // String price2 = String.format("%.2f(%.2f)", prices[2], probs[2]);
    // pricesStr = String.format("%-12s/ %-12s/ %s", price0, price1, price2);
    // result += String.format(pricesSelectionsFormat, marketStr, selections,
    // pricesStr);
    // break;
    // default:
    // selections = selection[0];
    // pricesStr = String.format("%.2f(%.2f)", prices[0], probs[0]);
    // result += String.format(pricesSelectionsFormat, marketStr, selections,
    // prices);
    // for (int j = 1; j < selection.length; j++) {
    // selections = selection[j];
    // pricesStr = String.format("%.2f(%.2f)", prices[j], probs[j]);
    // result += String.format(pricesSelectionsFormat, "", selections,
    // pricesStr);
    // }
    // break;
    // }
    // }
    // return result;
    // }


    /**
     * gets the unique key associated with this marketPrice
     *
     * @return
     */
    @JsonIgnore
    public String getKey() {
        String key;
        String seqId;
        seqId = this.sequenceId;
        if (seqId == null)
            seqId = "M";
        key = this.getType() + "_" + seqId;
        return key;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
        result = prime * result + ((marketDescription == null) ? 0 : marketDescription.hashCode());
        long temp;
        temp = Double.doubleToLongBits(marketWeight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((selections == null) ? 0 : selections.hashCode());
        result = prime * result + ((sequenceId == null) ? 0 : sequenceId.hashCode());
        result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (valid ? 1231 : 1237);
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
        MarketPrice other = (MarketPrice) obj;
        if (category != other.category)
            return false;
        if (lineId == null) {
            if (other.lineId != null)
                return false;
        } else if (!lineId.equals(other.lineId))
            return false;
        if (marketDescription == null) {
            if (other.marketDescription != null)
                return false;
        } else if (!marketDescription.equals(other.marketDescription))
            return false;
        if (Double.doubleToLongBits(marketWeight) != Double.doubleToLongBits(other.marketWeight))
            return false;
        if (selections == null) {
            if (other.selections != null)
                return false;
        } else if (!selections.equals(other.selections))
            return false;
        if (sequenceId == null) {
            if (other.sequenceId != null)
                return false;
        } else if (!sequenceId.equals(other.sequenceId))
            return false;
        if (timeStamp != other.timeStamp)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (valid != other.valid)
            return false;
        return true;
    }



}
