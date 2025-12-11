package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.competitionsdata.TestCompetition2;

public class LeaguePlayoffFixturesUpdaterTest {

    @Test
    public void checkPlayoffTagTest() {
        MethodName.log();
        String s = LeaguePlayoffFixturesUpdater.checkPlayoffTag("xyz");
        // System.out.println(s);
        assertTrue(s != null);
        s = LeaguePlayoffFixturesUpdater.checkPlayoffTag("6v2");
        // System.out.println(s);
        assertTrue(s == null);
    }

    @Test
    public void checkPlayoffFixtureIdTest() {
        MethodName.log();
        String s;
        s = LeaguePlayoffFixturesUpdater.checkPlayoffFixtureId("P2-Leg1", 1);
        // System.out.println(s);
        assertTrue(s == null);
        s = LeaguePlayoffFixturesUpdater.checkPlayoffFixtureId("P2-leg1", 2);
        // System.out.println(s);
        assertTrue(s != null);
        s = LeaguePlayoffFixturesUpdater.checkPlayoffFixtureId("P2-legx", 1);
        // System.out.println(s);
        assertTrue(s != null);
    }

    @Test
    public void validateFixtureTest() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        for (Fixture fixture : competition.getFixtures()) {
            String s = LeaguePlayoffFixturesUpdater.validateFixture(fixture);
            if (s != null) {
                // System.out.println(s);
                fail();
            }
        }
    }

    /**
     * all regular league matches resulted
     */
    @Test
    public void updateTest() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        Fixtures fixtures = competition.getFixtures();
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(competition);
        // System.out.println(resulter);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        // System.out.println(fixtures.listToString());
        Fixture fixture = fixtures.getByFixtureID("P1-leg1");
        assertEquals("T19", fixture.getHomeTeamID());
        assertEquals("T22", fixture.getAwayTeamID());
        fixture = fixtures.getByFixtureID("P2-leg2");
        assertEquals("T21", fixture.getHomeTeamID());
        assertEquals("T20", fixture.getAwayTeamID());
        fixture = fixtures.getByFixtureID("P3");
        assertEquals(null, fixture.getHomeTeamID());
        assertEquals(null, fixture.getAwayTeamID());
    }

    /**
     * all but one regular league matches resulted. posn 1,2,3 contested. all others settled
     */
    @Test
    public void updateTest2() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        Fixtures fixtures = competition.getFixtures();
        Fixture fixture = fixtures.getFixtureByTeamIds("T23", "T22");
        fixture.setStatus(OutrightsFixtureStatus.PRE_MATCH);
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(competition);
        // System.out.println(resulter);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        // System.out.println(fixtures.listToString());
        fixture = fixtures.getByFixtureID("P1-leg1");
        assertEquals("T19", fixture.getHomeTeamID());
        assertEquals(null, fixture.getAwayTeamID()); // posn 3 -so should be null
        fixture = fixtures.getByFixtureID("P2-leg2");
        assertEquals("T21", fixture.getHomeTeamID());
        assertEquals("T20", fixture.getAwayTeamID());
        fixture = fixtures.getByFixtureID("P3");
        assertEquals(null, fixture.getHomeTeamID());
        assertEquals(null, fixture.getAwayTeamID());
    }

    /**
     * unresult a previously resulted match and viery teamId cleared from playoff fixture
     */
    @Test
    public void updateTest3() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        Fixtures fixtures = competition.getFixtures();
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(competition);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        /*
         * unresult one match and verify that the teamId is cleared in the playoff fixtures
         */
        Fixture fixture = fixtures.getFixtureByTeamIds("T23", "T22");
        fixture.setStatus(OutrightsFixtureStatus.PRE_MATCH);
        resulter = new LeagueMarketsResulter(competition);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        fixture = fixtures.getByFixtureID("P1-leg1");
        assertEquals("T19", fixture.getHomeTeamID());
        assertEquals(null, fixture.getAwayTeamID()); // posn 3 -so should be null
    }

    /**
     * result 3 of the four playoff legs and verify one team set in the final
     */
    @Test
    public void updateTest4() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        Fixtures fixtures = competition.getFixtures();
        /*
         * set the id's of the teams in the playoffs
         */
        LeagueMarketsResulter resulter = new LeagueMarketsResulter(competition);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        /*
         * result 3 of the 4 playoffs
         */
        fixtures.getByFixtureID("P1-leg1").resultFixture(1, 0);
        fixtures.getByFixtureID("P1-leg2").resultFixture(1, 3);
        fixtures.getByFixtureID("P2-leg1").resultFixture(1, 0);
        // System.out.println(fixtures.listToString());
        resulter = new LeagueMarketsResulter(competition);
        LeaguePlayoffFixturesUpdater.update(fixtures, resulter);
        Fixture fixture = fixtures.getByFixtureID("P3");
        assertEquals("T19", fixture.getHomeTeamID());
        assertEquals(null, fixture.getAwayTeamID());
    }

}
