package ats.algo.core.comparetomarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;

public class MarketPricesStatus {
    private boolean pricesOk;
    private boolean paramFindRequired;
    private int nSources;
    private int nMarkets;
    private Map<MarketGroup, GroupMetrics> groupMetricsMap;
    private List<String> warningMessages;
    private double minCostPossibleWithTheseMarketsPrices;
    private double actualCostForOurMarkets;
    private CompareToMarketMetrics compareToMarketMetrics;

    public MarketPricesStatus() {
        pricesOk = true;
        warningMessages = new ArrayList<String>();
    }

    public String toString() {
        String s = "MarketPricesStatus:\n";
        s += String.format("  Number of sources supplied: %d\n", nSources);
        s += String.format("  Total number of markets supplied: %d\n", nMarkets);
        s += String.format("  Market prices supplied Ok?: %b\n", pricesOk);
        s += String.format("  Should schedule param find? %b\n", paramFindRequired);
        s += String.format("  Min cost achievable for these marketPrices: %.3f (acceptable threshold: %.3f)\n",
                        minCostPossibleWithTheseMarketsPrices, CompareToMarket.getAmberAlertThreshold());
        s += String.format("  Actual cost for supplied markets: %.3f\n", actualCostForOurMarkets);
        s += String.format("  Group metrics:\n");
        if (groupMetricsMap != null) {
            for (Entry<MarketGroup, GroupMetrics> entry : groupMetricsMap.entrySet()) {
                String group = entry.getKey().toString();
                GroupMetrics groupMetrics = entry.getValue();
                String costStr = String.format("min cost: %.3f, ", groupMetrics.cost());
                String minPossibleCostStr = String.format("min possible cost: %.3f", groupMetrics.minPossibleCost());
                String paramFindThisGroup;
                if (groupMetrics.paramFindNeeded())
                    paramFindThisGroup = "Yes";
                else
                    paramFindThisGroup = "No";
                s += String.format("%-24s%-12s%s Param find for this group? %s\n", group, costStr, minPossibleCostStr,
                                paramFindThisGroup);
                // s+= String.format("%s (DEBUG): cumCost %.3f, cumMinCost %.3f,
                // cumWeight %.3f\n", group, groupMetrics.cumCost,
                // groupMetrics.cumMinPossibleCost, groupMetrics.cumWeight);
            }
        }

        s += toStringWarningMessages();
        return s;
    }

    /**
     * delivers only stuff generated from MarketPricesList, not from markets (which may be unreliable if bias is set)
     * 
     * @return
     */
    public String toStringMarketPricesInfo() {
        String s = "MarketPrice related status:\n";
        s += String.format("  Number of sources supplied: %d\n", nSources);
        s += String.format("  Total number of markets supplied: %d\n", nMarkets);
        s += String.format("  Market prices supplied Ok?: %b\n", pricesOk);
        s += String.format("  Min cost achievable for these marketPrices: %.3f (acceptable threshold: %.3f)\n",
                        minCostPossibleWithTheseMarketsPrices, CompareToMarket.getAmberAlertThreshold());
        s += toStringWarningMessages();
        return s;
    }

    public String toStringWarningMessages() {
        String s = "";
        for (String msg : warningMessages)
            s += "  " + msg + "\n";
        if (!s.equals(""))
            s = "Warning messages:\n" + s;
        return s;
    }

    public void addWarningMsg(String s) {
        warningMessages.add(s);
    }

    public boolean isPricesOk() {
        return pricesOk;
    }

    public void setPricesOk(boolean pricesOk) {
        this.pricesOk = pricesOk;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    public int getnSources() {
        return nSources;
    }

    public void setnSources(int nSources) {
        this.nSources = nSources;
    }

    public double getMinCostPossibleWithTheseMarketsPrices() {
        return minCostPossibleWithTheseMarketsPrices;
    }

    void setMinCostPossibleWithTheseMarketsPrices(double minCostPossibleWithTheseMarketsPrices) {
        this.minCostPossibleWithTheseMarketsPrices = minCostPossibleWithTheseMarketsPrices;
    }

    public double getActualCostForOurMarkets() {
        return actualCostForOurMarkets;
    }

    void setActualCostForOurMarkets(double actualCostForOurMarkets) {
        this.actualCostForOurMarkets = actualCostForOurMarkets;
    }

    public boolean isParamFindRequired() {
        return paramFindRequired;
    }

    void setParamFindRequired(boolean paramFindRequired) {
        this.paramFindRequired = paramFindRequired;
    }

    public int getnMarkets() {
        return nMarkets;
    }

    void setnMarkets(int nMarkets) {
        this.nMarkets = nMarkets;
    }

    public Map<MarketGroup, GroupMetrics> getGroupMetricsMap() {
        return groupMetricsMap;
    }

    void setGroupMetricsMap(Map<MarketGroup, GroupMetrics> groupMetricsMap) {
        this.groupMetricsMap = groupMetricsMap;
    }

    public CompareToMarketMetrics getCompareToMarketMetrics() {
        return compareToMarketMetrics;
    }

    public void setCompareToMarketMetrics(CompareToMarketMetrics compareToMarketMetrics) {
        this.compareToMarketMetrics = compareToMarketMetrics;
    }

}
