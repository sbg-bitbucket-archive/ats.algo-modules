package ats.algo.core.tradingrules;

import java.util.HashMap;
import java.util.Map;

public class PriceSourceWeights {

    private Map<String, Double> map;
    private double defaultWeight = 1.0;

    public PriceSourceWeights() {
        map = new HashMap<String, Double>();
    }

    /**
     * sets the weight for this source
     * 
     * @param source "default" sets the default weight for unrecognised price sources
     * @param weight
     */
    public void setPriceSourceWeight(String source, double weight) {
        if (source.toLowerCase().equals("default"))
            defaultWeight = weight;
        else
            map.put(source, (Double) weight);
    }

    /**
     * gets the weight for this source. returns the default if not recognised.
     * 
     * @param source
     * @return
     */
    public double getPriceSourceWeight(String source) {
        Double weight = map.get(source);
        if (weight == null)
            return defaultWeight;
        else
            return weight;
    }

    @Override
    public String toString() {
        return "PriceSourceWeights [map=" + map + ", defaultWeight=" + defaultWeight + "]";
    }



}
