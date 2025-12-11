package ats.algo.sport.baseball;


import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.baseball.BaseballMatchEngine;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.baseball.BaseballMatchParams;

public class BaseballParamFindTest {

    @Test
    public void test() {
        MethodName.log();
        /*
         * test using paramFinder.setMarketPrices
         */
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchEngine matchEngine = new BaseballMatchEngine(matchFormat);
        BaseballMatchParams matchParams = (BaseballMatchParams) matchEngine.getMatchParams();
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices1(1);
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        @SuppressWarnings("unused")
        ParamFindResults results = paramFinder.calculate();
        sw.stop();
        // System.out.print("Test 1-1\n");
        // System.out.print(results.toString());
        // System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (BaseballMatchParams) paramFinder.getMatchParams();
        Gaussian skill = matchParams.getTotalRuns();
        // System.out.println(skill.getMean());
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
        // System.out.print("Test 1-2\n");
        // System.out.print(results.toString());
        // System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (BaseballMatchParams) paramFinder.getMatchParams();
        skill = matchParams.getTotalRuns();
        // // System.out.println(skill.getMean());
        assertEquals(12.774, skill.getMean(), 0.3);
        skill = matchParams.getSupremacy();
        // System.out.println(skill.getMean());

        assertEquals(3.092, skill.getMean(), 0.3);
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

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total runs", MarketCategory.OVUN, "12.5");
        tmtg.put("Over", 1.95);
        tmtg.put("Under", 1.95);
        m.addMarketPrice(tmtg);
        return m;
    }
}
