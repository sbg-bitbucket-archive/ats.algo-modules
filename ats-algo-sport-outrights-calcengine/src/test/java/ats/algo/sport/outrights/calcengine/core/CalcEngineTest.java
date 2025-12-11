package ats.algo.sport.outrights.calcengine.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition2;

public class CalcEngineTest {

    @Test
    public void testOneThread() {
        testBasicLeague(1);
    }

    @Test
    public void testSixThreads() {
        testBasicLeague(6);
    }

    private void testBasicLeague(int nThreads) {
        Competition competition = TestCompetition.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.N_THREADS = nThreads;
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        System.out.println(markets);
        Market market = markets.get("C:R");
        ResultedMarket resultedMarket = competition.getResultedMarkets().get("C:R");
        System.out.println(market);
        System.out.println(resultedMarket);
        assertEquals(3, market.getSelections().size());
        assertEquals(0.218, market.get("Huddersfield"), 0.008);
        assertEquals(0.780, market.get("Swansea City"), 0.008);
        assertEquals(15, resultedMarket.getLosingSelections().size());
        assertEquals(2, resultedMarket.getWinningSelections().size());
        assertTrue(resultedMarket.getLosingSelections().contains("Arsenal"));
        assertTrue(resultedMarket.getWinningSelections().contains("West Brom"));
        assertTrue(resultedMarket.getWinningSelections().contains("Stoke City"));
        market = competition.getMarkets().get("C:BH");
        resultedMarket = competition.getResultedMarkets().get("C:BH");
        System.out.println(market);
        System.out.println(resultedMarket);
        assertEquals(8, resultedMarket.getLosingSelections().size());
        assertEquals(4, resultedMarket.getWinningSelections().size());
        assertTrue(resultedMarket.getLosingSelections().contains("Everton"));
        assertTrue(resultedMarket.getWinningSelections().contains("Southampton"));
    }

    @Test
    public void playoffLeagueTest() {
        Competition competition = TestCompetition2.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        ResultedMarkets resultedMarkets = competition.getResultedMarkets();
        System.out.println(markets);
        System.out.println(resultedMarkets);
        Market market = markets.get("C:PR");
        assertEquals(4, market.getSelections().size());
        assertEquals(0.111, market.get("Bolton"), 0.008);
        assertEquals(0.361, market.get("Middlesbrough"), 0.008);
        assertEquals(0.476, market.get("Millwall"), 0.008);
        assertEquals(0.052, market.get("Reading"), 0.008);
    }


    // @Test
    public void calcWithMomentumTest() {
        Competition competition = TestCompetition.generate();
        // Set stdDevn non-zero to turn on momentum etc

        /*
         * run the calcs
         */
        competition.getRatingsFactors().setRatingsStdDevn(0.0);
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        Market market = competition.getMarkets().get("C:R");
        System.out.println(market);
        assertEquals(3, market.getSelections().size());
        assertEquals(0.218, market.get("Huddersfield"), 0.008);
        assertEquals(0.780, market.get("Swansea City"), 0.008);
        assertEquals(0.002, market.get("Southampton"), 0.008);
        /*
         * repeat with non zero std devn
         */
        competition.getRatingsFactors().setRatingsStdDevn(0.01);
        calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        market = competition.getMarkets().get("C:R");
        System.out.println(market);
        assertEquals(3, market.getSelections().size());
        assertEquals(0.271, market.get("Huddersfield"), 0.008);
        assertEquals(0.713, market.get("Swansea City"), 0.008);
        assertEquals(0.017, market.get("Southampton"), 0.008);
    }

}
