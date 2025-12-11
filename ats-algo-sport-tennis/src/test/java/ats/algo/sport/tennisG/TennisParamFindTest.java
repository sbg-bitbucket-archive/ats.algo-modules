package ats.algo.sport.tennisG;

import static org.junit.Assert.*;

import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchState;
import ats.algo.sport.tennisG.TennisGMatchEngine;
import ats.algo.sport.tennisG.TennisGMatchFormat.*;

public class TennisParamFindTest {

    // @Test
    public void test() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 3, true);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchEngine.setMatchState(matchState);
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices();
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        ParamFindResults results = paramFinder.calculate();
        System.out.println(results.toString());
        sw.stop();
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (TennisGMatchParams) paramFinder.getMatchParams();
        Gaussian skillA = matchParams.getOnServePctA();
        Gaussian skillB = matchParams.getOnServePctB();
        assertEquals(.07, skillA.getMean() - skillB.getMean(), 0.01);
        assertEquals(.06, skillA.getStdDevn(), 0.02);
        assertEquals(.05, skillB.getStdDevn(), 0.02);
        marketPrices = addInvalidMarkets(marketPrices);
        matchParams.setDefaultParams(matchFormat);
        paramFinder.setMatchParameters(matchParams);
        paramFinder.setMarketPrices(marketPrices);
        sw = new StopWatch();
        sw.start();
        results = paramFinder.calculate();
        sw.stop();

        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (TennisGMatchParams) paramFinder.getMatchParams();
        skillA = matchParams.getOnServePctA();
        assertEquals(.61, skillA.getMean(), 0.03);
        assertEquals(.055, skillA.getStdDevn(), 0.02);
        skillB = matchParams.getOnServePctB();
        assertEquals(.55, skillB.getMean(), 0.03);
        assertEquals(.055, skillB.getStdDevn(), 0.02);
        /*
         * test the form of the interface with markets from multiple sources
         */
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices());
        marketPricesList.put("Source2", setTestMarketPrices());
        marketPricesList.put("Source3", setTestMarketPrices());
        paramFinder.setMarketPricesList(marketPricesList);
        matchParams.setDefaultParams(matchFormat);
        paramFinder.setMatchParameters(matchParams);
        sw = new StopWatch();
        sw.start();
        results = paramFinder.calculate();
        sw.stop();
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        matchParams = (TennisGMatchParams) paramFinder.getMatchParams();
        skillA = matchParams.getOnServePctA();
        assertEquals(.61, skillA.getMean(), 0.03);
        assertEquals(.055, skillA.getStdDevn(), 0.03);
        skillB = matchParams.getOnServePctB();
        assertEquals(.54, skillB.getMean(), 0.04);
        assertEquals(.05, skillB.getStdDevn(), 0.03);

    }

    // @Test
    public void testAbSymmetry() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 3, true);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchParams.setOnServePctA(.65, 0.05);
        matchParams.setOnServePctB(.6, 0.08);
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchEngine.setMatchState(matchState);
        ParamFinder paramFinder = new ParamFinder(matchEngine);
        paramFinder.setMatchParameters(matchParams);
        MarketPrices marketPrices = setTestMarketPrices();
        paramFinder.setMarketPrices(marketPrices);
        StopWatch sw = new StopWatch();
        sw.start();
        ParamFindResults results = paramFinder.calculate();
        sw.stop();
        System.out.print(results.toString());
        System.out.printf("Time taken: %.1f\n", sw.getElapsedTimeSecs());
        TennisGMatchParams matchParams2 = (TennisGMatchParams) paramFinder.getMatchParams();
        System.out.printf("Match params\n %s", matchParams2.toString());

    }

    /**
     * generates a couple of test markets
     * 
     * @return
     */
    private MarketPrices setTestMarketPrices() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null, "M");
        ab.put("A", 1.36);
        ab.put("B", 3.40);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "20.5", "M");
        tmtg.put("Over", 1.84);
        tmtg.put("Under", 2.0);
        m.addMarketPrice(tmtg);
        return m;
    }

    private MarketPrices addInvalidMarkets(MarketPrices marketPrices) {
        MarketPrice xx = new MarketPrice("XX", "no such market", MarketCategory.GENERAL, null, "M");
        xx.put("A", 1.36);
        xx.put("B", 3.40);
        xx.setValid(true);
        xx.setCategory(MarketCategory.GENERAL);
        marketPrices.addMarketPrice(xx);

        MarketPrice atws = new MarketPrice("FT:W1S:A", "A to win at least one set", MarketCategory.GENERAL, null, "M");
        atws.put("A", 1.36); // should be "No", "Yes", so will generate no such
                             // selection error
        atws.put("B", 3.40);
        atws.setValid(true);
        atws.setCategory(MarketCategory.GENERAL);
        marketPrices.addMarketPrice(atws);

        MarketPrice btws = new MarketPrice("FT:W1S:B", "A to win at least one set", MarketCategory.GENERAL, null, "M");
        btws.put("No", 1.36); // wrong no of selections
        btws.setValid(true);
        btws.setCategory(MarketCategory.GENERAL);
        marketPrices.addMarketPrice(btws);
        return marketPrices;
    }

}
