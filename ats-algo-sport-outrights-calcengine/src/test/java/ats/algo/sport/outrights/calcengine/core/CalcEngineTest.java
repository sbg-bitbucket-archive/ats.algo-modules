package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.Selection;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsResulter;
import ats.algo.sport.outrights.calcengine.leagues.LeaguePlayoffFixturesUpdater;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition2;
import ats.algo.sport.outrights.competitionsdata.TestCompetition3;
import ats.algo.sport.outrights.competitionsdata.TestCompetition4;

public class CalcEngineTest {

    @Test
    public void testOneThread() {
        MethodName.log();
        testBasicLeague(1);
    }

    @Test
    public void testSixThreads() {
        MethodName.log();
        MethodName.log();
        testBasicLeague(6);
    }

    @Test
    public void testWithStdDevn() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        competition.getRatingsFactors().setRatingsStdDevn(0.05);
        CalcEngine calcEngine = new CalcEngine(competition);
        CalcEngine.setnThreads(1);
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        @SuppressWarnings("unused")
        Market market = markets.get("C:R");
        @SuppressWarnings("unused")
        ResultedMarket resultedMarket = competition.getNewResultedMarkets().get("C:R");
        // System.out.println(market);
        // System.out.println(resultedMarket);
        Market market1 = markets.get("C:OU:T1");
        Market market2 = markets.get("C:OU:T2");
        assertTrue(market1 != null);
        assertTrue(market2 != null);
    }

    private void testBasicLeague(int nThreads) {
        Competition competition = TestCompetition.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        CalcEngine.setnThreads(nThreads);
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(markets);
        // System.out.println(resultedMarkets);
        Market market = markets.get("C:R");
        ResultedMarket resultedMarket = resultedMarkets.get("C:R");
        // System.out.println(market);
        // System.out.println(resultedMarket);
        Market market1 = markets.get("C:OU:T1");
        Market market2 = markets.get("C:OU:T2");
        assertTrue(market1 != null);
        assertTrue(market2 != null);
        assertEquals(0.218, market.get("Huddersfield"), 0.01);
        assertEquals(0.780, market.get("Swansea City"), 0.01);
        Market market20 = markets.get("C:OU:T20");
        // // System.out.println(market20);
        assertTrue(market20 != null);
        assertEquals(2, market20.getSelections().size());
        assertEquals(2, resultedMarket.getWinningSelections().size());
        assertTrue(resultedMarket.getWinningSelections().contains("West Brom"));
        assertTrue(resultedMarket.getWinningSelections().contains("Stoke City"));
        market = markets.get("C:BH");
        resultedMarket = resultedMarkets.get("C:BH");
        // System.out.println(market);
        // System.out.println(resultedMarket);
        // assertEquals(8, resultedMarket.getLosingSelections().size());
        assertEquals(4, resultedMarket.getWinningSelections().size());
        // assertTrue(resultedMarket.getLosingSelections().contains("Everton"));
        assertTrue(resultedMarket.getWinningSelections().contains("Southampton"));
        List<FcastStanding> fcastStandings = competition.getFcastStandings().finishOrder();
        // System.out.println(fcastStandings);
        FcastStanding topTeam = fcastStandings.get(0);
        assertEquals("T11", topTeam.getTeamId());
        assertEquals(99.1, topTeam.getPoints(), 0.15);
        assertEquals(107.1, topTeam.getGoalsFor(), 107.1);
        FcastStanding bottomTeam = fcastStandings.get(19);
        assertEquals("T15", bottomTeam.getTeamId());
        assertEquals(31.2, bottomTeam.getPoints(), 0.15);
        assertEquals(33.9, bottomTeam.getGoalsFor(), 107.1);
        Market market3 = markets.get("C:MTW:T10:T17");
        // System.out.println(market3);
        assertEquals(0.39, market3.get("Liverpool"), 0.01);
    }

    // @Test
    /*
     * this test needs redoing when we re-introduce a play-off league
     */
    public void playoffLeagueTest() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(markets);
        // System.out.println(resultedMarkets);
        Market market = markets.get("C:PR");
        assertEquals(6, market.getSelections().size());
        assertEquals(0.111, market.get("Bolton"), 0.008);
        assertEquals(0.361, market.get("Middlesbrough"), 0.008);
        assertEquals(0.476, market.get("Millwall"), 0.008);
        assertEquals(0.052, market.get("Reading"), 0.008);
    }

    // @Test
    public void calcWithMomentumTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        // Set stdDevn non-zero to turn on momentum etc

        /*
         * run the calcs
         */
        competition.getRatingsFactors().setRatingsStdDevn(0.0);
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        Market market = competition.getMarkets().get("C:R");
        // System.out.println(market);
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
        // System.out.println(market);
        assertEquals(3, market.getSelections().size());
        assertEquals(0.271, market.get("Huddersfield"), 0.008);
        assertEquals(0.713, market.get("Swansea City"), 0.008);
        assertEquals(0.017, market.get("Southampton"), 0.008);
    }

    @Test
    public void resultingLogicTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        // System.out.println(calcEngine.getResulter());
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(resultedMarkets);
        /*
         * check finish in top half market C:TH
         */
        Market thMkt = markets.get("C:TH");
        ResultedMarket thResultedMkt = resultedMarkets.get("C:TH");
        assertEquals(8, thMkt.getSelections().size());
        assertEquals(8, thResultedMkt.getWinningSelections().size());
        assertEquals(4, thResultedMkt.getLosingSelections().size());
        assertFalse(thResultedMkt.isFullyResulted());
        assertTrue(thMkt.getSelections().get("Leicester City") != null);
        assertTrue(thMkt.getSelections().get("West Ham") != null);
        /*
         * check finish in bottom half market. C:BH Leic city and Westham are the top and bottom ranked teams where the
         * result is still in doubt
         */
        Market bhMkt = markets.get("C:BH");
        ResultedMarket bhResultedMkt = resultedMarkets.get("C:BH");
        assertEquals(7, bhMkt.getSelections().size());
        assertEquals(4, bhResultedMkt.getWinningSelections().size());
        assertEquals(8, bhResultedMkt.getLosingSelections().size());
        assertFalse(bhResultedMkt.isFullyResulted());
        assertTrue(bhMkt.getSelections().get("Leicester City") != null);
        assertTrue(bhMkt.getSelections().get("West Ham") != null);

        /*
         * check finish in top four market C:T4
         */
        Market t4Mkt = markets.get("C:T4");
        ResultedMarket t4ResultedMkt = resultedMarkets.get("C:T4");
        assertEquals(3, t4Mkt.getSelections().size());
        assertEquals(2, t4ResultedMkt.getWinningSelections().size());
        assertEquals(15, t4ResultedMkt.getLosingSelections().size());
        assertFalse(t4ResultedMkt.isFullyResulted());
        assertTrue(t4Mkt.getSelections().get("Liverpool") != null);
        assertTrue(t4Mkt.getSelections().get("Tottenham") != null);
        assertTrue(t4Mkt.getSelections().get("Chelsea") != null);
        /*
         * check league winner market C:LW
         */
        Market lwMkt = markets.get("C:LW");
        ResultedMarket lwResultedMkt = resultedMarkets.get("C:LW");
        assertTrue(lwMkt == null);
        assertEquals(1, lwResultedMkt.getWinningSelections().size());
        assertEquals(19, lwResultedMkt.getLosingSelections().size());
        assertTrue(lwResultedMkt.isFullyResulted());
        assertTrue(lwResultedMkt.getWinningSelections().get(0).equals("Manchester City"));
        /*
         * check some season winner markets
         */
        Market mtwMkt1 = markets.get("C:MTW:T10:T11");
        ResultedMarket mtwResultedMkt1 = resultedMarkets.get("C:MTW:T10:T11");
        assertTrue(mtwMkt1 == null);
        assertTrue(mtwResultedMkt1.isFullyResulted());
        assertTrue(mtwResultedMkt1.getWinningSelections().get(0).equals("Manchester City"));
        assertTrue(mtwResultedMkt1.getLosingSelections().get(0).equals("Liverpool"));
        // Liverpool v Tottenham
        Market mtwMkt2 = markets.get("C:MTW:T10:T17");
        ResultedMarket mtwResultedMkt2 = resultedMarkets.get("C:MTW:T10:T17");
        assertTrue(mtwResultedMkt2 == null);
        assertEquals(2, mtwMkt2.getSelections().size());
        /*
         * check rock bottom market C:RB
         */
        Market rbMkt = markets.get("C:RB");
        ResultedMarket rbResultedMkt = resultedMarkets.get("C:RB");
        assertEquals(3, rbMkt.getSelections().size());
        assertEquals(0, rbResultedMkt.getWinningSelections().size());
        assertEquals(17, rbResultedMkt.getLosingSelections().size());
        assertFalse(rbResultedMkt.isFullyResulted());
        assertTrue(rbMkt.getSelections().get("Stoke City") != null);
        assertTrue(rbMkt.getSelections().get("Swansea City") != null);
        assertTrue(rbMkt.getSelections().get("West Brom") != null);
        /*
         * check relegated market C:R
         */
        Market rMkt = markets.get("C:R");
        ResultedMarket rResultedMkt = resultedMarkets.get("C:R");
        assertEquals(3, rMkt.getSelections().size());
        assertEquals(2, rResultedMkt.getWinningSelections().size());
        assertEquals(15, rResultedMkt.getLosingSelections().size());
        assertFalse(rResultedMkt.isFullyResulted());
        assertTrue(rMkt.getSelections().get("Huddersfield") != null);
        assertTrue(rMkt.getSelections().get("Southampton") != null);
        assertTrue(rMkt.getSelections().get("Swansea City") != null);
        /*
         * check stay up market C:SU
         */
        Market suMkt = markets.get("C:SU");
        ResultedMarket suResultedMkt = resultedMarkets.get("C:SU");
        /*
         * n.b. Southampton selection is not resulted but will have been removed because prob close to 1.0
         */
        assertEquals(2, suMkt.getSelections().size());
        assertEquals(15, suResultedMkt.getWinningSelections().size());
        assertEquals(2, suResultedMkt.getLosingSelections().size());
        assertFalse(suResultedMkt.isFullyResulted());
        assertTrue(suMkt.getSelections().get("Huddersfield") != null);
        assertTrue(suMkt.getSelections().get("Swansea City") != null);
        /*
         * check that Southampton has not been resulted
         */
        assertFalse(suResultedMkt.getWinningSelections().contains("Southampton"));
        assertFalse(suResultedMkt.getLosingSelections().contains("Southampton"));
        /*
         * checkwinner without M City mkt C:WOM
         */
        Market womMkt = markets.get("C:WOM");
        ResultedMarket womResultedMkt = resultedMarkets.get("C:WOM");
        assertEquals(2, womMkt.getSelections().size());
        assertEquals(0, womResultedMkt.getWinningSelections().size());
        assertEquals(17, womResultedMkt.getLosingSelections().size());
        assertFalse(womResultedMkt.isFullyResulted());
        assertTrue(womMkt.getSelections().get("Manchester United") != null);
        assertTrue(womMkt.getSelections().get("Tottenham") != null);
    }

    @Test
    public void resultingFilterTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        ResultedMarkets resultedMarkets1 = competition.getNewResultedMarkets();
        ResultedMarkets resultedMarkets2 = competition.getPreviouslyPublishedResultedMarkets();
        resultedMarkets1.equalsExTimeStamp(resultedMarkets2);
        assertTrue(resultedMarkets1.equalsExTimeStamp(resultedMarkets2));
        calcEngine.calculate();
        ResultedMarkets resultedMarkets3 = competition.getNewResultedMarkets();
        ResultedMarkets resultedMarkets4 = competition.getPreviouslyPublishedResultedMarkets();

        assertEquals(0, resultedMarkets3.getResultedMarkets().size());
        assertTrue(resultedMarkets2.equalsExTimeStamp(resultedMarkets4));
        /*
         * remove a whole market and verify gets republished
         */
        resultedMarkets4.getResultedMarkets().remove("C:BH_M");
        calcEngine.calculate();
        ResultedMarkets resultedMarkets5 = competition.getNewResultedMarkets();
        ResultedMarkets resultedMarkets6 = competition.getPreviouslyPublishedResultedMarkets();

        assertEquals(01, resultedMarkets5.getResultedMarkets().size());
        /*
         * remove a single selection and verify gets republished
         */
        List<String> selns = resultedMarkets6.getResultedMarkets().get("C:BH_M").getLosingSelections();
        selns.remove(2);
        calcEngine.calculate();
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets7 = competition.getNewResultedMarkets();
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets8 = competition.getPreviouslyPublishedResultedMarkets();
        // System.out.println(resultedMarkets7);
        // System.out.println(resultedMarkets8);
        assertEquals(01, resultedMarkets5.getResultedMarkets().size());
    }

    /**
     * uses different test competition to allow conditions to be tested that aren't generated by the TestCompetition
     * dataset
     */
    @Test
    public void xmasMarketResultingTest() {
        MethodName.log();
        Competition competition = TestCompetition3.generate();
        competition.setXmas("2018-10-30T00:00:00"); // move date to force partial resulting of xmas market
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        // System.out.println(calcEngine.getResulter());
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(resultedMarkets);
        Market xmstMkt = markets.get("C:XMST");
        ResultedMarket xmstResultedMkt = resultedMarkets.get("C:XMST");
        assertEquals(7, xmstMkt.getSelections().size());
        assertEquals(0, xmstResultedMkt.getWinningSelections().size());
        assertEquals(13, xmstResultedMkt.getLosingSelections().size());
        assertFalse(xmstResultedMkt.isFullyResulted());
        assertTrue(xmstMkt.getSelections().get("Manchester City") != null);
        assertTrue(xmstMkt.getSelections().get("Wolverhampton Wanderers") != null);
    }

    @Test
    public void seasonCompleteMarketResultingTest() {
        MethodName.log();
        Competition competition = TestCompetition3.generate();
        Fixtures fixtures = competition.getFixtures();
        /*
         * result all remaining matches as draws
         */
        for (Fixture fixture : fixtures) {
            if (fixture.getStatus() != OutrightsFixtureStatus.COMPLETED) {
                fixture.setStatus(OutrightsFixtureStatus.COMPLETED);
                fixture.setGoalsHome(0);
                fixture.setGoalsAway(0);
            }
        }
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        // System.out.println(calcEngine.getResulter());
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(resultedMarkets);
        assertEquals(0, markets.size()); // all mkts shld be resulted
    }

    @Test
    public void seasonPointsMarketResultingTest() {
        MethodName.log();
        Competition competition = TestCompetition3.generate();
        Fixtures fixtures = competition.getFixtures();
        /*
         * result all matches except one: Watford(T14) v WestHam(T13)
         */
        for (Fixture fixture : fixtures) {
            if (fixture.getStatus() != OutrightsFixtureStatus.COMPLETED && !fixture.getFixtureID().equals("F380")) {
                fixture.setStatus(OutrightsFixtureStatus.COMPLETED);
                fixture.setGoalsHome(0);
                fixture.setGoalsAway(0);
            }
        }

        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        // System.out.println(calcEngine.getResulter());
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(resultedMarkets);
        /*
         * only unresulted markets should be those where watford or west ham is a selection and result of final match
         * could influence the result
         */
        assertTrue(markets.get("C:XMST") == null);
        assertTrue(markets.get("C:OU:T14") != null);
        assertEquals(2, markets.get("C:TH").getSelections().size());
        assertTrue(markets.get("C:MTW:T9:T14") != null);
        assertTrue(resultedMarkets.get("C:MTW:T9:T14") == null);
    }

    /**
     * uses different test competition to allow conditions to be tested that aren't generated by the TestCompetition
     * dataset
     */
    @Test
    public void marketFilteringTest() {
        MethodName.log();
        Competition competition = TestCompetition3.generate();
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.calculate();
        Markets markets = competition.getMarkets();
        // System.out.println(markets);
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        // System.out.println(resultedMarkets);
        Market mtwMkt = markets.get("C:MTW:T19:T1");
        /*
         * Man City selection should have been removed because prob =1.0
         */
        assertTrue(mtwMkt.getSelections().get("Manchester City") == null);
        assertTrue(mtwMkt.getSelections().get("Cardiff City") != null);
        assertEquals(CalcEngine.NOT_ZERO, mtwMkt.getSelections().get("Cardiff City").getProb(),
                        CalcEngine.NOT_ZERO / 100.0);
    }

    /**
     * runs through the logic at the end of a league that has playoffs to verify markets and resulted markets as
     * expected
     */
    @Test
    public void playoffsCompetitionTest() {
        MethodName.log();
        Competition competition = TestCompetition4.generate();
        /**
         * set the ids of the teams involved in the playoffs
         */
        LeaguePlayoffFixturesUpdater.update(competition.getFixtures(), new LeagueMarketsResulter(competition));

        executePlayoffsTest(competition, 1, 4, 2, 18, false);
        competition.getFixtures().getByFixtureID("P1-leg1").resultFixture(1, 1);
        executePlayoffsTest(competition, 1, 4, 2, 18, false);
        competition.getFixtures().getByFixtureID("P2-leg1").resultFixture(2, 1);
        executePlayoffsTest(competition, 1, 4, 2, 18, false);
        competition.getFixtures().getByFixtureID("P1-leg2").resultFixture(3, 1);
        /*
         * complete each fixture in the playoffs and confirm winners/losers identified correctly
         */
        executePlayoffsTest(competition, 1, 3, 2, 19, false);
        competition.getFixtures().getByFixtureID("P2-leg2").resultFixture(1, 3);
        executePlayoffsTest(competition, 1, 2, 2, 20, false);
        Map<String, Selection> selections = competition.getMarkets().get("C:PR").getSelections();
        assertTrue(selections.get("Millwall") != null);
        assertTrue(selections.get("Middlesbrough") != null);
        competition.getFixtures().getByFixtureID("P3").resultFixture(3, 2);
        executePlayoffsTest(competition, 0, 0, 3, 21, true);
        List<String> winningSelns = competition.getNewResultedMarkets().get("C:PR").getWinningSelections();
        assertTrue(winningSelns.contains("Millwall"));

    }

    private void executePlayoffsTest(Competition competition, int mktsSize, int prMktSize,
                    int prResultedMktWinningSelnsSize, int prResultedMktLosingSelnsSize, boolean print) {
        competition.updateDerivedFixtures();
        CalcEngine calcEngine = new CalcEngine(competition);
        /*
         * empty previously published resultedmarkets list to ensure all resulted markets get published
         */
        competition.getPreviouslyPublishedResultedMarkets().getResultedMarkets().clear();
        calcEngine.calculate();
        CalcEngine.setnThreads(1);
        Markets markets = competition.getMarkets();
        @SuppressWarnings("unused")
        ResultedMarkets resultedMarkets = competition.getNewResultedMarkets();
        if (print) {
            // System.out.println(competition.getFixtures().listToString());
            // System.out.println(markets);
            // System.out.println(resultedMarkets);
        }
        if (mktsSize > 0) {
            Market prMkt = markets.get("C:PR");
            assertEquals(prMktSize, prMkt.getSelections().size());
        }
        ResultedMarket prResultedMkt = competition.getNewResultedMarkets().get("C:PR");
        assertEquals(prResultedMktWinningSelnsSize, prResultedMkt.getWinningSelections().size());
        assertEquals(prResultedMktLosingSelnsSize, prResultedMkt.getLosingSelections().size());
    }
}
