package ats.algo.sport.cricket;


import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.cricket.CricketMatchEngine;
import ats.algo.sport.cricket.CricketMatchFormat;
import ats.algo.sport.cricket.CricketMatchParams;
import ats.algo.sport.cricket.CricketMatchIncident.CricketMatchIncidentType;

public class CricketParamFindTest {

    @Test
    public void test() {
        MethodName.log();
        /*
         * test using paramFinder.setMarketPrices
         */
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        CricketMatchEngine matchEngine = new CricketMatchEngine(matchFormat);
        CricketMatchParams matchParams = (CricketMatchParams) matchEngine.getMatchParams();
        CricketMatchState matchState = new CricketMatchState(matchFormat);
        CricketMatchIncident cricketMatchIncident = new CricketMatchIncident();
        cricketMatchIncident.set(CricketMatchIncidentType.BATFIRST, TeamId.A);

        matchState.updateStateForIncident(cricketMatchIncident, false);
        // System.out.println(matchState);
        cricketMatchIncident.set(CricketMatchIncidentType.RUN1, TeamId.A);

        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices1(1);
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        @SuppressWarnings("unused")
        ParamFindResults results = paramFinder.calculate();
        // System.out.print("Test 1-1\n");
        // System.out.print(results.toString());
        // System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (CricketMatchParams) paramFinder.getMatchParams();
        Gaussian skill = matchParams.getTeamAScoreRate();
        // System.out.println(skill.getMean());
        assertEquals(skill.getMean(), 171.162, 3.0);
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
        ab.put("A", 1.7);
        ab.put("B", 2.2);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU:A", "Total points", MarketCategory.OVUN, "175.5");
        tmtg.put("Over", 1.95);
        tmtg.put("Under", 1.95);
        m.addMarketPrice(tmtg);
        return m;
    }

}
