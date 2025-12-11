package ats.algo.sport.beachvolleyball;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchEngine;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchParams;

public class BeachVolleyballParamFindTest {

    @Test
    // @Ignore
    public void test() {
        /*
         * test using paramFinder.setMarketPrices
         */
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        BeachVolleyballMatchEngine matchEngine = new BeachVolleyballMatchEngine(matchFormat);
        BeachVolleyballMatchParams matchParams = (BeachVolleyballMatchParams) matchEngine.getMatchParams();
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices1(1);
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        ParamFindResults results = paramFinder.calculate();
        sw.stop();
        System.out.print("Test 1-1\n");
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (BeachVolleyballMatchParams) paramFinder.getMatchParams();
        Gaussian skill = matchParams.getOnServePctA();
        System.out.println(skill.getMean());
        // assertEquals(.43223, skill.getMean(), 0.01);
        /*
         * test using paramFinder.setMarketPricesList
         */
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("source1", setTestMarketPrices1(1));
        paramFinder.setMarketPricesList(marketPricesList);
        matchParams.setDefaultParams(matchFormat);
        paramFinder.setMatchParameters(matchParams);
        sw.start();
        results = paramFinder.calculate();
        sw.stop();
        System.out.print("Test 1-2\n");
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (BeachVolleyballMatchParams) paramFinder.getMatchParams();
        skill = matchParams.getOnServePctA();
        // System.out.println(skill.getMean());
        // assertEquals(.43, skill.getMean(), 0.02);
        skill = matchParams.getOnServePctB();
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
        // // assertEquals(.41, skill.getMean(), 0.02);
        // // assertEquals(.06, skill.getStdDevn(), 0.15);
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
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        BeachVolleyballMatchEngine matchEngine = new BeachVolleyballMatchEngine(matchFormat);
        BeachVolleyballMatchParams matchParams = (BeachVolleyballMatchParams) matchEngine.getMatchParams();
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices3(1);
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        ParamFindResults results = paramFinder.calculate();
        sw.stop();
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (BeachVolleyballMatchParams) paramFinder.getMatchParams();
        Gaussian skill = matchParams.getOnServePctA();
        assertEquals(.7, skill.getMean(), 0.02);
        assertEquals(.05, skill.getStdDevn(), 0.01);
    }

    /**
     * generates a couple of test markets corresponding to skill = (.4,.06)
     * 
     * @return
     */
    private MarketPrices setTestMarketPrices1(double weight) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(weight);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.3);
        ab.put("B", 3.9);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total points", MarketCategory.OVUN, "175.5");
        tmtg.put("Over", 1.95);
        tmtg.put("Under", 1.95);
        m.addMarketPrice(tmtg);
        return m;
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

    /**
     * generates a couple of test markets corresponding to skill = (.7,.04)
     * 
     * @return
     */
    private MarketPrices setTestMarketPrices3(double weight) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(weight);

        MarketPrice ab = new MarketPrice("AB", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.23);
        ab.put("B", 3.93);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("LTA", "Total games", MarketCategory.OVUN, "2.5");
        tmtg.put("Over", 2.22);
        tmtg.put("Under", 1.62);
        m.addMarketPrice(tmtg);
        return m;
    }

}
