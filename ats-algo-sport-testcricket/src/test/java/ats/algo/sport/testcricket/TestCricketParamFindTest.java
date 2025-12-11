package ats.algo.sport.testcricket;

import org.junit.Test;
import org.junit.Ignore;

import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.sport.testcricket.TestCricketMatchEngine;
import ats.algo.sport.testcricket.TestCricketMatchFormat;
import ats.algo.sport.testcricket.TestCricketMatchParams;

public class TestCricketParamFindTest {

    @Test
    // @Ignore
    public void test() {
        /*
         * test using paramFinder.setMarketPrices
         */
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchEngine matchEngine = new TestCricketMatchEngine(matchFormat);
        TestCricketMatchParams matchParams = (TestCricketMatchParams) matchEngine.getMatchParams();
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        /*
         * test using paramFinder.setMarketPricesList
         */
        // System.out.println(skill.getMean());
        // assertEquals(.43, skill.getMean(), 0.02);
        // System.out.println(skill.getMean());

        // assertEquals(.335, skill.getMean(), 0.02);
        // /*
        // * test using a pair of sources
        // */
        // Map <String, MarketPrices> marketPricesList2 = new TreeMap <String,
        // MarketPrices>() ;
        // marketPricesList2.put("source1", setTestMarketPrices1(5));
        // marketPricesList2.put("source2", setTestMarketPrices2(0.05));
        // paramFinder.setMarketPricesList(marketPricesList2);
        // matchParams.setDefaultParams(matchFormat);
        // paramFinder.setMatchParameters(matchParams);
        // sw.start();
        // results = paramFinder.calculate();
        // sw.stop();
        //
        // System.out.print("Test 1-3\n");
        // System.out.print(results.toString());
        // System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        // matchParams = (VolleyballMatchParams) paramFinder.getMatchParams();
        // skill = matchParams.getOnServePctA();
        //// assertEquals(.41, skill.getMean(), 0.02);
        //// assertEquals(.06, skill.getStdDevn(), 0.15);
        // /*
        // * test using a pair of sources - switching the weights
        // */
        // Map <String, MarketPrices> marketPricesList3 = new TreeMap <String,
        // MarketPrices>() ;
        // marketPricesList3.put("source1", setTestMarketPrices1(0.05));
        // marketPricesList3.put("source2", setTestMarketPrices2(5.0));
        // paramFinder.setMarketPricesList(marketPricesList3);
        // matchParams.setDefaultParams(matchFormat);
        // paramFinder.setMatchParameters(matchParams);
        // sw.start();
        // results = paramFinder.calculate();
        // sw.stop();
        // System.out.print("Test 1-4\n");
        // System.out.print(results.toString());
        // System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        // matchParams = (VolleyballMatchParams) paramFinder.getMatchParams();
        // skill = matchParams.getOnServePctA();
        // assertEquals(.7, skill.getMean(), 0.02);
        // assertEquals(.05, skill.getStdDevn(), 0.015);
    }

    @Test
    /*
     * check behaviour when line market is out of line
     */
    @Ignore
    public void test2() {
        /*
         * test using paramFinder.setMarketPrices
         */
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchEngine matchEngine = new TestCricketMatchEngine(matchFormat);
        TestCricketMatchParams matchParams = (TestCricketMatchParams) matchEngine.getMatchParams();
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
    }

    /**
     * generates a couple of test markets corresponding to skill = (.7,.04)
     * 
     * @return
     */
    // private MarketPrices setTestMarketPrices2(double weight) {
    // MarketPrices m = new MarketPrices();
    // m.setWeightForThisSource(weight);
    //
    // MarketPrice ab = new MarketPrice("AB", "Match winner",
    // MarketType.GENERAL, null);
    // ab.put("A", 1.23);
    // ab.put("B", 3.93);
    // m.put("AB", ab);
    //
    // MarketPrice tmtg = new MarketPrice("LT", "Total games", MarketType.OVUN,
    // "2.5");
    // tmtg.put("Over", 2.22);
    // tmtg.put("Under", 1.62);
    // m.put("LT", tmtg);
    // return m;
    // }

}
