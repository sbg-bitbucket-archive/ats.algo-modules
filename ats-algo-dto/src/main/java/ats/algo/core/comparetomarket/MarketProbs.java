package ats.algo.core.comparetomarket;

import java.util.TreeMap;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.markets.MarketCategory;

/**
 * Container to hold the selection probabilities and other data from a single market sourced from a third party
 * comparison.
 * 
 * @author Geoff
 * 
 */
public class MarketProbs {
    private String marketType;
    private String sequenceId;
    private MarketGroup marketGroup;
    private MarketCategory marketCategory;
    private String lineId;
    private double marketWeight;
    private double sourceWeight;
    private TreeMap<String, Double> probs;

    public MarketProbs() {
        probs = new TreeMap<String, Double>();
    }

    String getMarketType() {
        return marketType;
    }

    void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    String getMarketSequenceId() {
        return sequenceId;
    }

    void setMarketSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public MarketCategory getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(MarketCategory marketType) {
        this.marketCategory = marketType;
    }

    String getLineId() {
        return lineId;
    }

    void setLineId(String subType) {
        this.lineId = subType;
    }

    double getMarketWeight() {
        return marketWeight;
    }

    void setMarketWeight(double marketWeight) {
        this.marketWeight = marketWeight;
    }


    public double getSourceWeight() {
        return sourceWeight;
    }

    public void setSourceWeight(double sourceWeight) {
        this.sourceWeight = sourceWeight;
    }

    TreeMap<String, Double> getProbs() {
        return probs;
    }

    void setProbs(TreeMap<String, Double> probs) {
        this.probs = probs;
    }

    double getProbForSelection(String selection) {
        return probs.get(selection);
    }

    void putProbForSelection(String selection, double prob) {
        probs.put(selection, prob);
    }

    MarketGroup getMarketGroup() {
        return marketGroup;
    }

    void setMarketGroup(MarketGroup marketGroup) {
        this.marketGroup = marketGroup;
    }



    public String toString() {
        String key = marketType + "_" + sequenceId;
        String s = String.format(
                        "key: %s, group: %s, category: %s, lineId: %s, sourceWeight: %.1f, marketWeight: %.1f\n", key,
                        marketGroup, marketCategory, lineId, sourceWeight, marketWeight);
        for (Entry<String, Double> e : this.getProbs().entrySet())
            s += String.format("  Selection: %s, prob: %.3f\n", e.getKey(), e.getValue());
        return s;
    }

    // private static final double MIN_ACCEPTABLE_PROB = 0.05;
    // private static final double MAX_ACCEPTABLE_PROB = 0.95;


}
