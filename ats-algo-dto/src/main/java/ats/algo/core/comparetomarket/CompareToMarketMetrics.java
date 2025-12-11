package ats.algo.core.comparetomarket;

import java.util.Map;
import java.util.TreeMap;

import ats.algo.core.MarketGroup;

import java.util.Map.Entry;

public class CompareToMarketMetrics {


    /*
     * holds the data for each market and each source
     */
    public class Stats {
        int nSources;
        double sumWeights;
        double sumProbs;
        double sumProbsSquared;

        Stats() {
            nSources = 0;
            sumWeights = 0.0;
            sumProbs = 0.0;
            sumProbsSquared = 0.0;
        }

        public double getMean() {
            if (sumWeights == 0)
                return -999;
            return sumProbs / sumWeights;
        }

        double getStdDevn() {
            if (sumWeights == 0)
                return -999;
            double mean = getMean();
            return Math.sqrt(sumProbsSquared / sumWeights - mean * mean);

        }

        public String toString() {
            return String.format(
                            "nSources: %d, sumWeights: %.3f, sumProbs: %.3f, sumProbsSquared: %.3f.  mean: %.3f, stdDevn: %.3f\n",
                            nSources, sumWeights, sumProbs, sumProbsSquared, getMean(), getStdDevn());
        }
    }



    private double minPossibleCost;
    private double marketsCost;
    /*
     * Map order for these variables is Market:Source:Selection
     */
    private Map<String, Map<String, Map<String, Double>>> minPossibleCostDetails;
    private Map<String, Map<String, Map<String, Double>>> marketsCostDetails;
    private Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> marketsCostDescriptionInfo;
    private Map<MarketGroup, GroupMetrics> groupMetricsMap;
    /*
     * marketsProbsMeans holds the mean values of the probs from the various sources for each selection. Map order is
     * Market:Selection
     */
    private Map<String, Map<String, Stats>> marketsProbsStats;

    /**
     * creates the container that will hold the generated statistics
     * 
     * @param matchedMarkets
     */
    CompareToMarketMetrics(Map<String, Map<String, MarketProbs>> matchedMarkets) {
        /*
         * initialise the cost variables to have the same structure as matchedMarkets
         */
        minPossibleCostDetails = new TreeMap<String, Map<String, Map<String, Double>>>();
        marketsCostDetails = new TreeMap<String, Map<String, Map<String, Double>>>();
        marketsProbsStats = new TreeMap<String, Map<String, Stats>>();
        groupMetricsMap = new TreeMap<MarketGroup, GroupMetrics>();
        marketsCostDescriptionInfo = new TreeMap<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>>();

        for (Entry<String, Map<String, MarketProbs>> entry : matchedMarkets.entrySet()) {
            /*
             * iterate over each market key
             */
            Map<String, MarketProbs> marketProbsForMarket = entry.getValue();
            String marketKey = entry.getKey();
            Map<String, Map<String, Double>> minPossibleCostEntry = new TreeMap<String, Map<String, Double>>();
            Map<String, Map<String, Double>> marketsCostEntry = new TreeMap<String, Map<String, Double>>();
            Map<String, Map<String, CompareToMarketMetricsItemInfo>> marketsCostDescriptionInfoEntry =
                            new TreeMap<String, Map<String, CompareToMarketMetricsItemInfo>>();
            Map<String, Stats> marketProbsStatsEntry = new TreeMap<String, Stats>();
            minPossibleCostDetails.put(marketKey, minPossibleCostEntry);
            marketsCostDetails.put(marketKey, marketsCostEntry);

            marketsCostDescriptionInfo.put(marketKey, marketsCostDescriptionInfoEntry);
            marketsProbsStats.put(marketKey, marketProbsStatsEntry);
            for (Entry<String, MarketProbs> entryForSource : marketProbsForMarket.entrySet()) {
                /*
                 * iterate over each source for given market key
                 */

                MarketProbs marketProbs = entryForSource.getValue();
                String sourceKey = entryForSource.getKey();
                /*
                 * create entry in map for each marketGroup in the list of supplied competitor prices
                 */
                MarketGroup marketGroup = marketProbs.getMarketGroup();
                if (groupMetricsMap.get(marketGroup) == null)
                    groupMetricsMap.put(marketGroup, new GroupMetrics());

                Map<String, Double> minPossibleCostSelections = new TreeMap<String, Double>();
                Map<String, Double> marketsCostSelections = new TreeMap<String, Double>();
                Map<String, CompareToMarketMetricsItemInfo> marketsCostDescriptionInfoEntrySelections =
                                new TreeMap<String, CompareToMarketMetricsItemInfo>();

                minPossibleCostEntry.put(sourceKey, minPossibleCostSelections);
                marketsCostEntry.put(sourceKey, marketsCostSelections);
                marketsCostDescriptionInfoEntry.put(sourceKey, marketsCostDescriptionInfoEntrySelections);
                for (Entry<String, Double> selectionEntry : marketProbs.getProbs().entrySet()) {
                    /*
                     * iterate over each selection for given source and market key
                     */
                    double prob = selectionEntry.getValue();
                    String selectionKey = selectionEntry.getKey();
                    Stats selectionStats = marketProbsStatsEntry.get(selectionKey);
                    if (selectionStats == null) {
                        selectionStats = new Stats();
                        marketProbsStatsEntry.put(selectionKey, selectionStats);
                    }

                    selectionStats.nSources++;
                    double weight = marketProbs.getSourceWeight();
                    selectionStats.sumWeights += weight;
                    selectionStats.sumProbs += weight * prob;
                    selectionStats.sumProbsSquared += weight * prob * prob;
                    /*
                     * save the info we will need later
                     */
                    CompareToMarketMetricsItemInfo info = new CompareToMarketMetricsItemInfo();
                    info.marketSequenceId = marketProbs.getMarketSequenceId();
                    info.lineId = marketProbs.getLineId();
                    info.weight = marketProbs.getSourceWeight();
                    info.targetProb = prob;
                    marketsCostDescriptionInfoEntrySelections.put(selectionKey, info);
                }
            }
        }

    }

    double getMinPossibleCost() {
        return minPossibleCost;
    }

    void setMinPossibleCost(double minPossibleCost) {
        this.minPossibleCost = minPossibleCost;
    }

    double getMarketsCost() {
        return marketsCost;
    }

    Map<MarketGroup, GroupMetrics> getGroupMetricsMap() {
        return groupMetricsMap;
    }

    void setMarketsCost(double marketsCost) {
        this.marketsCost = marketsCost;
    }

    Map<String, Map<String, Map<String, Double>>> getMinPossibleCostDetails() {
        return minPossibleCostDetails;
    }

    Map<String, Map<String, Map<String, Double>>> getMarketsCostDetails() {
        return marketsCostDetails;
    }

    public Map<String, Map<String, Stats>> getMarketsProbsStats() {
        return marketsProbsStats;
    }



    public Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> getMarketsCostDescriptionInfo() {
        return marketsCostDescriptionInfo;
    }

    public void setMarketsCostDescriptionInfo(
                    Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> marketsCostDescriptionInfo) {
        this.marketsCostDescriptionInfo = marketsCostDescriptionInfo;
    }

    public String toString() {
        String s = "CompareToMarketMetrics\n";
        s += "----------------------\n";
        s += String.format("Cost: %.3f, Min possible cost: %.3f.  Details:\n", marketsCost, minPossibleCost);
        s += convert3LevelMapToString(minPossibleCostDetails);
        s += String.format("Markets cost: %.3f.  Details:\n", marketsCost);
        s += convert3LevelMapToString(marketsCostDetails);
        s += "Markets probability stats\n";
        for (Entry<String, Map<String, Stats>> e : marketsProbsStats.entrySet()) {
            s += String.format(" Market: %s\n", e.getKey());
            for (Entry<String, Stats> e2 : e.getValue().entrySet()) {
                s += String.format("   Selection: %s, %s", e2.getKey(), e2.getValue().toString());
            }
        }
        s += "Group metrics\n";
        for (Entry<MarketGroup, GroupMetrics> entry : groupMetricsMap.entrySet()) {
            String group = entry.getKey().toString();
            GroupMetrics groupMetrics = entry.getValue();
            s += String.format("Group: %s:  cost: %.3f, min possible cost: %.3f\n", group, groupMetrics.cost(),
                            groupMetrics.minPossibleCost());
        }

        s += "\n";
        return s;
    }

    private String convert3LevelMapToString(Map<String, Map<String, Map<String, Double>>> map) {
        String s = "";
        for (Entry<String, Map<String, Map<String, Double>>> e : map.entrySet()) {
            s += String.format("  Market: %s\n", e.getKey());
            for (Entry<String, Map<String, Double>> e2 : e.getValue().entrySet()) {
                s += String.format("   Source: %s\n", e2.getKey());
                for (Entry<String, Double> e3 : e2.getValue().entrySet()) {
                    s += String.format("     Selection: %s, probDiffToTarget: %.3f\n", e3.getKey(), e3.getValue());
                }
            }
        }
        return s;
    }

}
