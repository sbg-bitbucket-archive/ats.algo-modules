package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsResulter.ResultStanding;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition2;
import ats.algo.sport.outrights.competitionsdata.TestCompetition3;

public class LeagueMarketsResulterTest {

    LeagueMarketsResulter resulter;
    Fixtures fixtures;

    private void standardTestInit() {
        Competition competition = TestCompetition.generate();
        competition.getFixtures().get(366).setDate("2019-01-01T14:30:00"); // put one fixture into 2019 to test the xmas
        competition.setXmas("2018-12-25T00:00:00");
        // System.out.println(competition.getDateOfXmas());
        resulter = new LeagueMarketsResulter(competition);
        // System.out.println(competition.getFixtures());
        // System.out.println(resulter);
    }

    @Test
    public void constructorTest() {
        MethodName.log();
        standardTestInit();
        assertEquals(36, resulter.resultStandingsTeamNameMap.get("Newcastle").nPlayedNow);
        assertEquals(41, resulter.resultStandingsTeamNameMap.get("Newcastle").pointsNow);
        assertEquals(47, resulter.resultStandingsTeamNameMap.get("Newcastle").seasonEnd.maxPoints);
        assertEquals(44, resulter.resultStandingsTeamNameMap.get("Newcastle").xmas.maxPoints);
        assertEquals(3, resulter.resultStandingsTeamNameMap.get("Liverpool").seasonEnd.highestPossiblePosn);
        assertEquals(5, resulter.resultStandingsTeamNameMap.get("Liverpool").seasonEnd.lowestPossiblePosn);
        assertEquals(9, resulter.resultStandingsTeamNameMap.get("Brighton").seasonEnd.highestPossiblePosn);
        assertEquals(16, resulter.resultStandingsTeamNameMap.get("Brighton").seasonEnd.lowestPossiblePosn);
        assertEquals(16, resulter.resultStandingsTeamNameMap.get("Swansea City").seasonEnd.highestPossiblePosn);
        assertEquals(20, resulter.resultStandingsTeamNameMap.get("Swansea City").seasonEnd.lowestPossiblePosn);
        assertEquals(9, resulter.resultStandingsTeamNameMap.get("Newcastle").xmas.highestPossiblePosn);
        assertEquals(16, resulter.resultStandingsTeamNameMap.get("Newcastle").xmas.lowestPossiblePosn);
    }

    @Test
    public void constructorWhenAllFixturesResultedTest() {
        MethodName.log();
        /*
         * result all fixtures except Watford vs West Ham
         */
        Competition competition = TestCompetition3.generate(); // use comp where all fixtures were created
        Fixtures fixtures = competition.getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getStatus() != OutrightsFixtureStatus.COMPLETED && !fixture.getFixtureID().equals("F380")) {
                fixture.setStatus(OutrightsFixtureStatus.COMPLETED);
                fixture.setGoalsHome(0);
                fixture.setGoalsAway(0);
            }
        }

        resulter = new LeagueMarketsResulter(competition);
        // System.out.println("One match (Watford vs West Ham) still to be resulted");
        assertEquals(8, resulter.resultStandingsTeamNameMap.get("Manchester United").seasonEnd.highestPossiblePosn);
        assertEquals(9, resulter.resultStandingsTeamNameMap.get("Manchester United").seasonEnd.lowestPossiblePosn);
        assertEquals(7, resulter.resultStandingsTeamNameMap.get("Watford").seasonEnd.highestPossiblePosn);
        assertEquals(11, resulter.resultStandingsTeamNameMap.get("Watford").seasonEnd.lowestPossiblePosn);
        /*
         * result the final fixture. Posns should now all be final with no diffs between lowest and highest finish posn
         */
        for (Fixture fixture : fixtures) {
            if (fixture.getFixtureID().equals("F380")) {
                fixture.setStatus(OutrightsFixtureStatus.COMPLETED);
                fixture.setGoalsHome(0);
                fixture.setGoalsAway(0);
            }
        }
        resulter = new LeagueMarketsResulter(competition);
        // System.out.println("All matches resulted");
        // System.out.println("Standings:");
        // Teams teams = competition.getTeams();
        // Standings standings = competition.generateStandings();
        // standings.finishOrder().forEach(s -> System.out.println(teams.get(s.getTeamId()).getDisplayName() + ": " +
        // s));
        // System.out.println(resulter.toString());
        assertEquals(8, resulter.resultStandingsTeamNameMap.get("Manchester United").seasonEnd.highestPossiblePosn);
        assertEquals(8, resulter.resultStandingsTeamNameMap.get("Manchester United").seasonEnd.lowestPossiblePosn);
        assertEquals(9, resulter.resultStandingsTeamNameMap.get("Watford").seasonEnd.highestPossiblePosn);
        assertEquals(9, resulter.resultStandingsTeamNameMap.get("Watford").seasonEnd.lowestPossiblePosn);
        for (ResultStanding r : resulter.resultStandings) {
            assertEquals(r.seasonEnd.lowestPossiblePosn, r.seasonEnd.highestPossiblePosn);
            assertEquals(r.xmas.lowestPossiblePosn, r.xmas.highestPossiblePosn);
        }
    }

    @Test
    public void inTopNTest() {
        MethodName.log();
        standardTestInit();
        assertTrue(resulter.inTopN("Manchester City", 1, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.inTopN("Chelsea", 5, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.inTopN("Chelsea", 4, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.inTopN("Manchester United", 3, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.inTopN("Manchester United", 2, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.inTopN("Leicester City", 15, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.inTopN("Leicester City", 14, r -> resulter.asAtSeasonEnd(r)));
    }

    @Test
    public void notInTopNTest() {
        MethodName.log();
        standardTestInit();
        assertFalse(resulter.notInTopN("Manchester City", 1, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.notInTopN("Chelsea", 2, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.notInTopN("Chelsea", 3, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.notInTopN("Manchester United", 1, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.notInTopN("Manchester United", 2, r -> resulter.asAtSeasonEnd(r)));
        assertTrue(resulter.notInTopN("Leicester City", 7, r -> resulter.asAtSeasonEnd(r)));
        assertFalse(resulter.notInTopN("Leicester City", 8, r -> resulter.asAtSeasonEnd(r)));
    }

    @Test
    public void seasonMarketWinnerLoserTest() {
        MethodName.log();
        standardTestInit();
        Set<String> selns = new HashSet<>(2);
        selns.add("Leicester City");
        selns.add("Chelsea");
        assertTrue(resulter.seasonMarketWinner("Chelsea", selns));
        assertFalse(resulter.seasonMarketWinner("Leicester City", selns));
        assertTrue(resulter.seasonMarketLoser("Leicester City", selns));
        assertFalse(resulter.seasonMarketLoser("Chelsea", selns));
        selns.remove("Chelsea");
        /*
         * Leicester City has 44 points now, potential max of 50. West Ham has 38 points now, potential max of 44.
         * Should not be able to result this market since WestHam could still beat LC
         * 
         */
        selns.add("West Ham");
        assertFalse(resulter.seasonMarketWinner("West Ham", selns));
        assertFalse(resulter.seasonMarketWinner("Leicester City", selns));
        assertFalse(resulter.seasonMarketLoser("Leicester City", selns));
        assertFalse(resulter.seasonMarketLoser("West Ham", selns));
        selns.remove("West Ham");
        /*
         * Leicester City has 44 points now, potential max of 50. Huddersfield has potential max of 42. Should be able
         * to result this market
         * 
         */
        selns.add("Huddersfield");
        assertFalse(resulter.seasonMarketWinner("Huddersfield", selns));
        assertTrue(resulter.seasonMarketWinner("Leicester City", selns));
        assertFalse(resulter.seasonMarketLoser("Leicester City", selns));
        assertTrue(resulter.seasonMarketLoser("Huddersfield", selns));
        selns.remove("Huddersfield");
        /*
         * Leicester City has will finish between 8 and 15th, swansea between 16 and 20th Should be able to result this
         * market
         * 
         */
        selns.add("Swansea City");
        assertFalse(resulter.seasonMarketWinner("Swansea City", selns));
        assertTrue(resulter.seasonMarketWinner("Leicester City", selns));
        assertFalse(resulter.seasonMarketLoser("Leicester City", selns));
        assertTrue(resulter.seasonMarketLoser("Swansea City", selns));
    }

    @Test
    public void inTopNExSelectionsTest() {
        MethodName.log();
        standardTestInit();
        /*
         * chelsea is on 69 pts now, max of 72 at xmas; Man U is 77 points now as is Tottenham
         */
        assertFalse(resulter.inTopNExSelections("Chelsea", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.inTopNExSelections("Chelsea", 4, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertFalse(resulter.inTopNExSelections("Manchester United", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.inTopNExSelections("Manchester United", 2, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.inTopNExSelections("Manchester United", 3, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.inTopNExSelections("Leicester City", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> testExcluder(s, "Huddersfield")));
        assertFalse(resulter.inTopNExSelections("Watford", 14, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.inTopNExSelections("Watford", 15, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));

    }

    @Test
    public void notInTopNExSelectionsTest() {
        MethodName.log();
        standardTestInit();
        /*
         * excl Man nCity only tottenham or Liverpool can come second
         */
        assertFalse(resulter.notInTopNExSelections("Manchester United", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertFalse(resulter.notInTopNExSelections("Tottenham", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.notInTopNExSelections("Liverpool", 1, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertFalse(resulter.notInTopNExSelections("Watford", 8, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
        assertTrue(resulter.notInTopNExSelections("Watford", 7, r -> resulter.asAtSeasonEnd(r),
                        s -> resulter.excludeManCity(s)));
    }

    /**
     * excludes all selections except those matchng otherSelName
     * 
     * @param selName
     * @param otherSelName
     * @return
     */
    private boolean testExcluder(String selName, String otherSelName) {
        return (!selName.equals("otherSelName"));
    }

    private void playoffTestInit() {
        Competition competition = TestCompetition2.generate();
        /*
         * in Test competition 2, all regular season matches are completed with the order being
         * T24,T23,T22,T21,T20,T19,T6...
         * 
         * T23 and T22 are on 52 and 49 points respectively. Change one of their fixtures to be pre-match so it is
         * uncertain which will be in the playoffs
         */
        Fixtures fixtures = competition.getFixtures();
        /*
         * modify one fixture Millwall v Barnsley to make teams finishing 2/3th uncertain. all other finishing positions
         * are settled
         */
        Fixture fixture = fixtures.getFixtureByTeamIds("T23", "T22");
        fixture.setStatus(OutrightsFixtureStatus.PRE_MATCH);
        // System.out.println("FIXTURES");
        // System.out.println(fixtures);
        // System.out.println("STANDINGS");
        // System.out.println(competition.generateStandings());
        // System.out.println("TEAMS");
        // System.out.println(competition.getTeams());
        resulter = new LeagueMarketsResulter(competition);
        // System.out.println(resulter);
    }

    @Test
    public void playOffLeagueConstructorTest() {
        MethodName.log();
        playoffTestInit();
        assertEquals(1, resulter.resultStandingsTeamNameMap.get("Bristol City").seasonEnd.highestPossiblePosn);
        assertEquals(2, resulter.resultStandingsTeamNameMap.get("Bristol City").seasonEnd.lowestPossiblePosn);
        assertEquals(1, resulter.resultStandingsTeamNameMap.get("Barnsley").seasonEnd.highestPossiblePosn);
        assertEquals(3, resulter.resultStandingsTeamNameMap.get("Barnsley").seasonEnd.lowestPossiblePosn);
        assertEquals(2, resulter.resultStandingsTeamNameMap.get("Millwall").seasonEnd.highestPossiblePosn);
        assertEquals(3, resulter.resultStandingsTeamNameMap.get("Millwall").seasonEnd.lowestPossiblePosn);
        assertEquals(4, resulter.resultStandingsTeamNameMap.get("Bolton").seasonEnd.highestPossiblePosn);
        assertEquals(4, resulter.resultStandingsTeamNameMap.get("Bolton").seasonEnd.lowestPossiblePosn);
    }

    @Test
    public void inPlayOffTest() {
        MethodName.log();
        playoffTestInit();
        assertFalse(resulter.inPlayOffs("Bristol City")); // finish 1 or 2
        assertTrue(resulter.notInPlayOffs("Bristol City")); // finish 1 or 2
        assertFalse(resulter.inPlayOffs("Barnsley")); // finish 1-3
        assertFalse(resulter.notInPlayOffs("Barnsley")); // finish 1-3
        assertFalse(resulter.inPlayOffs("Millwall")); // finish 2-3
        assertFalse(resulter.notInPlayOffs("Millwall")); // finish 2-3
        assertTrue(resulter.inPlayOffs("Bolton")); // finish 4
        assertFalse(resulter.notInPlayOffs("Bolton")); // finish 4
        assertFalse(resulter.inPlayOffs("QPR")); // finish 7
        assertTrue(resulter.notInPlayOffs("QPR")); // finish 7
    }

    @Test
    public void promotedTest() {
        MethodName.log();
        playoffTestInit();
        assertFalse(resulter.inPlayOffs("Bristol City")); // finish 1 or 2
        assertTrue(resulter.notInPlayOffs("Bristol City")); // finish 1 or 2
        assertFalse(resulter.inPlayOffs("Barnsley")); // finish 1-3
        assertFalse(resulter.notInPlayOffs("Barnsley")); // finish 1-3
        assertFalse(resulter.inPlayOffs("Millwall")); // finish 2-3
        assertFalse(resulter.notInPlayOffs("Millwall")); // finish 2-3
        assertTrue(resulter.inPlayOffs("Bolton")); // finish 4
        assertFalse(resulter.notInPlayOffs("Bolton")); // finish 4
        assertFalse(resulter.inPlayOffs("QPR")); // finish 7
        assertTrue(resulter.notInPlayOffs("QPR")); // finish 7
    }

    /*
     * result the playoff matches and verify that the playoffs winner gets promoted
     */
    @Test
    public void promotedTest2() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        Fixtures fixtures = competition.getFixtures();
        LeaguePlayoffFixturesUpdater.update(fixtures, new LeagueMarketsResulter(competition));
        fixtures.getByFixtureID("P1-leg1").resultFixture(1, 0);
        fixtures.getByFixtureID("P1-leg2").resultFixture(1, 3);
        fixtures.getByFixtureID("P2-leg1").resultFixture(1, 0);
        fixtures.getByFixtureID("P2-leg2").resultFixture(2, 0);
        LeaguePlayoffFixturesUpdater.update(fixtures, new LeagueMarketsResulter(competition));
        fixtures.getByFixtureID("P3").resultFixture(1, 0);
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(competition);
        // System.out.println("FIXTURES");
        // System.out.println(fixtures.listToString());
        resulter = new LeagueMarketsResulter(competition);
        // System.out.println(resulter);
        assertTrue(resulter.promoted("Reading")); // won play-off
        assertFalse(resulter.notPromoted("Reading"));
        assertFalse(resulter.promoted("Bolton")); // lost play-off final
        assertTrue(resulter.notPromoted("Bolton"));

    }

}
