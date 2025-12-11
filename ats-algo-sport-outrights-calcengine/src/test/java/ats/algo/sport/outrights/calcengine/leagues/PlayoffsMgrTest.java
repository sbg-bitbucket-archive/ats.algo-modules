package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.outrights.calcengine.core.AbstractFormat;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.CompetitionProperties;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.competitionsdata.TestCompetition2;

public class PlayoffsMgrTest {

    @Test
    public void playoffsMgrTest() {
        MethodName.log();
        Competition competition = TestCompetition2.generate();
        AbstractFormat competitionFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
        PlayoffsMgr p = new PlayoffsMgr((LeagueFormat) competitionFormat, competition.generateStandings(),
                        new ArrayList<Fixture>());

        p.initialiseforStartOfSimulation(competition.generateStandings());
        /*
         * teams in playoff should be T22,T21,T20,T19 in that order. Playoff test sequence is/should be:
         * 
         * P1: T19 v T22 result 3-2
         * 
         * P2: T20 v T21 result 1-1
         * 
         * P3: T22 v T19 result 1-2, 19 wins overall
         * 
         * P4: T21 v 17 result 3-2
         * 
         * P5: final: T22 v T21 result 2-2, T21 wins on penalties
         * 
         * promoted: team 1
         */
        List<String> teamIDs = p.getTeamsInPlayoff();
        assertEquals("T22", teamIDs.get(0));
        assertEquals("T19", teamIDs.get(3));
        /*
         * Fixture P1
         */
        Fixture fixture = p.nextFixture(competition.getFixtures().getByFixtureID("P1-leg1"));
        // System.out.println(fixture);
        assertFalse(fixture.isMustHaveAWinner());
        assertFalse(fixture.isPlayedAtNeutralGround());
        assertEquals("T19", fixture.getHomeTeamID());
        assertEquals("T22", fixture.getAwayTeamID());
        assertEquals(null, fixture.getFirstLegFixtureID());
        p.updateStateForFixtureResult(fixture, new FixtureResult(3, 2, null));
        /*
         * Fixture P2
         */
        fixture = p.nextFixture(competition.getFixtures().getByFixtureID("P2-leg1"));
        // System.out.println(fixture);
        assertFalse(fixture.isMustHaveAWinner());
        assertFalse(fixture.isPlayedAtNeutralGround());
        assertEquals("T20", fixture.getHomeTeamID());
        assertEquals("T21", fixture.getAwayTeamID());
        assertEquals(null, fixture.getFirstLegFixtureID());
        p.updateStateForFixtureResult(fixture, new FixtureResult(1, 1, null));
        /*
         * Fixture P3
         */
        fixture = p.nextFixture(competition.getFixtures().getByFixtureID("P1-leg2"));
        // System.out.println(fixture);
        assertTrue(fixture.isMustHaveAWinner());
        assertFalse(fixture.isPlayedAtNeutralGround());
        assertEquals("T22", fixture.getHomeTeamID());
        assertEquals("T19", fixture.getAwayTeamID());
        assertEquals(3, fixture.getFirstLegScoreA());
        assertEquals(2, fixture.getFirstLegScoreB());
        p.updateStateForFixtureResult(fixture, new FixtureResult(1, 2, TeamId.A));
        /*
         * Fixture P4
         */
        fixture = p.nextFixture(competition.getFixtures().getByFixtureID("P2-leg2"));
        // System.out.println(fixture);
        assertTrue(fixture.isMustHaveAWinner());
        assertFalse(fixture.isPlayedAtNeutralGround());
        assertEquals("T21", fixture.getHomeTeamID());
        assertEquals("T20", fixture.getAwayTeamID());
        assertEquals(1, fixture.getFirstLegScoreA());
        assertEquals(1, fixture.getFirstLegScoreB());
        p.updateStateForFixtureResult(fixture, new FixtureResult(3, 2, TeamId.A));
        /*
         * Fixture P5
         */
        fixture = p.nextFixture(competition.getFixtures().getByFixtureID("P3"));
        // System.out.println(fixture);
        assertTrue(fixture.isMustHaveAWinner());
        assertTrue(fixture.isPlayedAtNeutralGround());
        assertEquals("T22", fixture.getHomeTeamID());
        assertEquals("T21", fixture.getAwayTeamID());
        assertEquals(null, fixture.getFirstLegFixtureID());
        p.updateStateForFixtureResult(fixture, new FixtureResult(2, 2, TeamId.B));
        /*
         * verify final results
         */
        assertEquals("T22", p.getPlayoffFinalistTeamAID());
        assertEquals("T21", p.getPlayoffFinalistTeamBID());
        assertEquals("T21", p.getPlayoffWinnerTeamID());
    }

}
