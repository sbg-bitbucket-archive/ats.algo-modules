package ats.algo.sport.outrights.calcengine.core;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;

public abstract class AbstractMarketsResulter {

    /*
     * Set thresholds to be less than 1 in 64,000. to be outside this threshold zero or all monte carlo trials must have
     * passed
     */
    public static final double ZERO_THRESHOLD = 1.0e-5;
    public static final double ONE_THRESHOLD = 1.0 - ZERO_THRESHOLD;

    /**
     * 
     * @param standings
     * @param markets - modified on output. some selections may get removed
     * @param resultedMarkets - modified on output -
     */
    public abstract ResultedMarkets execute(Markets markets);


    /**
     * removes from newResultedMarkets any that are unchanged from previouslyResultedMarkets
     * 
     * @param newResultedMarkets - on exit updates to remove any previously published
     * @param previouslyResultedMarkets - on exit updates to add any new/modified resulted markets
     */
    public static void filterAlreadyPublished(ResultedMarkets newResultedMarkets,
                    ResultedMarkets previouslyResultedMarkets) {

        Set<String> keysToRemove = new HashSet<>();
        for (Entry<String, ResultedMarket> e : previouslyResultedMarkets.getResultedMarkets().entrySet()) {
            ResultedMarket newResultedMarket = newResultedMarkets.getResultedMarkets().get(e.getKey());
            if (e.getValue().equals(newResultedMarket)) {
                /*
                 * no change from what was previously published, so add to set of those to be removed
                 */
                keysToRemove.add(e.getKey());
            }
        }
        /*
         * remove those resultedMarkets whee there is no change
         */
        for (String key : keysToRemove)
            newResultedMarkets.getResultedMarkets().remove(key);
        /*
         * add any that are left to previouslyResultedMarkets
         */
        for (Entry<String, ResultedMarket> e : newResultedMarkets.getResultedMarkets().entrySet()) {
            previouslyResultedMarkets.getResultedMarkets().put(e.getKey(), e.getValue());
        }
    }
}
