package ats.algo.core.comparetomarket;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.comparetomarket.ParamFindResults;

public class ParamFindResultsTest {

    @Test
    public void test() {
        CompareToMarket.setParamFindKickoffThreshold(0.025);
        CompareToMarket.setAmberAlertThreshold(.025);
        CompareToMarket.setRedAlertThreshold(0.05);
        /*
         * generate test data set
         */
        Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoAtStart =
                        createTestMarketMetricsInfoMap(.4, .34);
        Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoAtEnd =
                        createTestMarketMetricsInfoMap(.4, .39);
        /*
         * first test
         */
        ParamFindResults results = new ParamFindResults();
        double minCostAchievable = 0.0;
        Boolean sourceDataOk = true;
        double costMetricAtStart = 0.015;
        double costMetricAtEnd = 0.003;
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.GREEN, results.getParamFindResultsStatus());
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.02;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.051;
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.AMBER, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Param find failed to find good fit - check prices"));
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.025;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.085;
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        // System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.RED, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Param find failed to find good fit. Cost above red threshold"));
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.03;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.05;

        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        // System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.AMBER, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Inconsistent inputs – check prices against market"));
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.03;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.06;
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.AMBER, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Inconsistent inputs – check prices against market"));
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Param find failed to find good fit - check prices"));
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.01;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.018;
        Map<String, CompareToMarketMetricsItemInfo> marketE1 = infoAtEnd.get("FT:ML").get("Pinnacle");
        marketE1.put("A", new CompareToMarketMetricsItemInfo("M", "", "selNameA", 1.0, 0.40, 0.36));
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        System.out.println(results.toString());
        assertFalse(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.AMBER, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Some differences with market - check prices"));
        /*
         * next test
         */
        results = new ParamFindResults();
        minCostAchievable = 0.01;
        costMetricAtStart = 0.015;
        costMetricAtEnd = 0.018;
        marketE1.put("A", new CompareToMarketMetricsItemInfo("M", "", "selNameA", 1.0, 0.40, 0.34));
        results.updateParamFindResultsForMetrics(minCostAchievable, sourceDataOk, costMetricAtStart, costMetricAtEnd,
                        infoAtStart, infoAtEnd);
        System.out.println(results.toString());
        assertTrue(results.isShouldSuspendMarkets());
        assertEquals(ParamFindResultsStatus.RED, results.getParamFindResultsStatus());
        assertTrue(results.getTraderParamFindResultsDescription().getResultSummary().getParamFindResultsSummary()
                        .contains("Large differences with market - check prices"));


    }

    /*
     * creates a test data set where the selection "FT:ML : Pinnacle : A" has the specified properties
     */
    private Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> createTestMarketMetricsInfoMap(
                    double targetProb, double ourProb) {


        Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoMap =
                        new HashMap<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>>();
        Map<String, Map<String, CompareToMarketMetricsItemInfo>> prices0 = generatePricesforMarket(targetProb, ourProb);
        Map<String, Map<String, CompareToMarketMetricsItemInfo>> prices1 = generatePricesforMarket(targetProb, ourProb);
        Map<String, Map<String, CompareToMarketMetricsItemInfo>> prices2 = generatePricesforMarket(targetProb, ourProb);
        infoMap.put("FT:OU", prices0);
        infoMap.put("FT:ML", prices1);
        infoMap.put("G:ML", prices2);
        return infoMap;
    }

    private Map<String, Map<String, CompareToMarketMetricsItemInfo>> generatePricesforMarket(double targetProb,
                    double ourProb) {
        Map<String, CompareToMarketMetricsItemInfo> marketS0 = new HashMap<String, CompareToMarketMetricsItemInfo>();
        marketS0.put("A", new CompareToMarketMetricsItemInfo("M", "", "selNameA", 1.0, 0.5, 0.5));
        marketS0.put("B", new CompareToMarketMetricsItemInfo("M", "", "selNameB", 1.0, 0.5, 0.5));

        Map<String, CompareToMarketMetricsItemInfo> marketS1 = new HashMap<String, CompareToMarketMetricsItemInfo>();
        marketS1.put("A", new CompareToMarketMetricsItemInfo("M", "", "selNameA", 1.0, targetProb, ourProb));
        marketS1.put("B", new CompareToMarketMetricsItemInfo("M", "", "selNameB", 1.0, 1 - targetProb, 1 - ourProb));

        Map<String, CompareToMarketMetricsItemInfo> marketS2 = new HashMap<String, CompareToMarketMetricsItemInfo>();
        marketS2.put("A", new CompareToMarketMetricsItemInfo("M", "", "selNameA", 1.0, 0.5, 0.5));
        marketS2.put("B", new CompareToMarketMetricsItemInfo("M", "", "selNameB", 1.0, 0.5, 0.5));

        Map<String, Map<String, CompareToMarketMetricsItemInfo>> sources =
                        new HashMap<String, Map<String, CompareToMarketMetricsItemInfo>>();
        sources.put("Source1", marketS0);
        sources.put("Pinnacle", marketS1);
        sources.put("Source3", marketS2);
        return sources;
    }

}
