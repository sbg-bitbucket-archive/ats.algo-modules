package ats.algo.montecarloframework;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.montecarloframework.ConcurrentCalculationManager;
import ats.algo.montecarloframework.MonteCarloMatch;

public class ConcurrentCalculationManagerTest {

    /**
     * tests the overall match Framework using three test classes: TestMatch, TestMatchState and TestStatistics
     */
    @Test
    public void test() {
        int nIterations = 1000000; // 1million
        int nConcurrent = 1;
        TestMatchFormat matchFormat = new TestMatchFormat();
        matchFormat.nSets = 5;
        TestMatchParams matchParams = new TestMatchParams();
        matchParams.probAWinsSet = 0.6;
        TestMatchState matchState = new TestMatchState(5, .6);
        TestMatch match = new TestMatch(matchFormat, matchState, matchParams);
        ConcurrentCalculationManager ccm =
                        new ConcurrentCalculationManager(nConcurrent, nIterations, (MonteCarloMatch) match, null);
        matchState.scoreA = 0;
        matchState.scoreB = 0;
        ccm.calculate();
        Markets markets = match.generateMarkets();
        System.out.print(markets.toString());
        System.out.println();
        /*
         * verify that Map interface to Markets works correctly - will get system exception if not.
         */
        for (Market market : markets) {
            String key = market.getType();
            System.out.println(key + " " + market.getMarketDescription());
        }
        assertEquals(5, markets.size());

        assertEquals(MarketCategory.GENERAL, markets.get("CS").getCategory());
        assertTrue(markets.get("CS").isValid());
        assertEquals(.115, markets.get("CS").get("1-3"), 0.005);
        assertEquals(MarketCategory.OVUN, markets.get("TS").getCategory());
        assertEquals("4.5", markets.get("TS").getLineId());
        assertEquals(.345, markets.get("TS").get("Over"), 0.005);
        Market market = markets.get("HS");
        assertEquals(MarketCategory.HCAP, market.getCategory());
        assertEquals("-1.5", market.getLineId());
        assertEquals(.475, market.get("AH"), 0.005);
        assertEquals(.683, markets.get("MW").get("A"), 0.005);
        assertEquals(.208, markets.get("TW").get("Neither"), 0.005);
        /*
         * get the specified line method for total market
         */
        Market lmBase = markets.get("TS");
        Market lm = lmBase.getMarketForLineId("4.5");
        assertTrue(lm.isValid());
        assertEquals(MarketCategory.OVUN, lm.getCategory());
        assertEquals("4.5", lm.getLineId());
        assertEquals("Over", lm.getSelectionNameOverOrA());
        assertEquals(.345, lm.get("Over"), 0.005);
        lm = lmBase.getMarketForLineId("3.5");
        assertTrue(lm.isValid());
        assertEquals(MarketCategory.OVUN, lm.getCategory());
        assertEquals("3.5", lm.getLineId());
        assertEquals("Under", lm.getSelectionNameUnderOrB());
        assertEquals(.720, lm.get("Over"), 0.005);
        lm = lmBase.getMarketForLineId("6.5");
        assertTrue(lm.isValid());
        assertEquals(.0, lm.get("Over"), 0.005);
        lm = lmBase.getMarketForLineId("-2.5");
        assertTrue(lm.isValid());
        assertEquals(MarketCategory.OVUN, lm.getCategory());
        assertEquals("-2.5", lm.getLineId());
        assertEquals("Under", lm.getSelectionNameUnderOrB());
        assertEquals(1.0, lm.get("Over"), 0.005);
        lmBase = markets.get("HS");
        lm = lmBase.getMarketForLineId("1.5");
        assertTrue(lm.isValid());
        assertEquals(MarketCategory.HCAP, lm.getCategory());
        assertEquals("1.5", lm.getLineId());
        assertEquals("AH (+1.5)", lm.getSelectionNameOverOrA());
        assertEquals(.821, lm.get("AH"), 0.005);
        lm = lmBase.getMarketForLineId("2.5");
        assertEquals(MarketCategory.HCAP, lm.getCategory());
        assertEquals("2.5", lm.getLineId());
        assertEquals("AH (+2.5)", lm.getSelectionNameOverOrA());
        assertEquals(.936, lm.get("AH"), 0.005);
        lm = lmBase.getMarketForLineId("3.5");
        assertEquals(1.0, lm.get("AH"), 0.005);
        lm = lmBase.getMarketForLineId("+1.5");
        assertEquals(MarketCategory.HCAP, lm.getCategory());
        assertEquals("+1.5", lm.getLineId());
        assertEquals("AH (+1.5)", lm.getSelectionNameOverOrA());
        assertEquals("BH (-1.5)", lm.getSelectionNameUnderOrB());
        assertEquals(.8210, lm.get("AH"), 0.005);
        assertEquals(.1790, lm.get("BH"), 0.005);

        /*
         * run another iteration to verify counts all reset correctly
         */
        match.resetStatistics();
        matchState.scoreA = 2;
        matchState.scoreB = 0;
        ccm.calculate();
        markets = match.generateMarkets();
        System.out.println("Starting score 2-0");
        // markets.print();
        // System.out.println();
        assertEquals(5, markets.size());
        assertEquals(.936, markets.get("MW").get("A"), 0.005);
    }

    /**
     * this test verifies that an essential property of random no generator is not affected by multithreading - that
     * variance behaves as p(1-p)/N
     */
    // @Test - don't run this test regularly unless we move to new machine
    // architecture
    public void testRandomNoProperties() {
        testRandomNoPropertiesForNThreads(1);
        testRandomNoPropertiesForNThreads(4);
    }

    void testRandomNoPropertiesForNThreads(int nConcurrent) {
        StopWatch sw = new StopWatch();
        int nIterations = 5000; // 50K
        TestMatchFormat matchFormat = new TestMatchFormat();
        matchFormat.nSets = 3;
        TestMatchParams matchParams = new TestMatchParams();
        matchParams.probAWinsSet = 0.6;
        TestMatchState matchState = new TestMatchState(3, .6);
        MatchEngineThreadPool matchEngineThreadPool = new MatchEngineThreadPool(nConcurrent - 1);

        TestMatch match = new TestMatch(matchFormat, matchState, matchParams);
        ConcurrentCalculationManager ccm = new ConcurrentCalculationManager(nConcurrent, nIterations,
                        (MonteCarloMatch) match, matchEngineThreadPool.getExecutorCompletionService());
        double probAWinsSet = .6;
        matchState.probAWinsSet = probAWinsSet;
        matchState.scoreA = 0;
        matchState.scoreB = 0;
        double expectedProb = probAWinsSet * probAWinsSet; // prob of 2-0
        double expectedVariance = expectedProb * (1 - expectedProb) / nIterations;
        double expectedSD = Math.sqrt(expectedVariance);
        double[] mcProb = new double[1000];
        double sumProb = 0;
        double sumSquaredProb = 0;
        Markets markets;
        sw.start();
        for (int i = 0; i < 500; i++) {
            ccm.calculate();
            markets = match.generateMarkets();
            double prob = markets.get("CS").get("2-0");
            mcProb[i] = prob;
            sumProb += prob;
            sumSquaredProb += prob * prob;
        }
        sw.stop();
        // System.out.printf("testRandomNoProperties. nThreads: %d, elapsed
        // time: %.1f\n", nConcurrent,
        // sw.getElapsedTimeSecs());
        double measuredProb = sumProb / 500;
        double measuredVar = sumSquaredProb / 500 - measuredProb * measuredProb;
        double measuredSD = Math.sqrt(measuredVar);
        assertEquals(expectedProb, measuredProb, 2.56 * expectedSD);
        assertEquals(expectedSD, measuredSD, 0.0005);
    }
}
