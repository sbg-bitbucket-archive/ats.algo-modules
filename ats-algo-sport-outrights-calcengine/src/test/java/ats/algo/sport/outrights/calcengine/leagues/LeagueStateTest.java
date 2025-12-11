package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.leagues.LeagueState;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class LeagueStateTest {

    @Test
    public void copyTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3));
        LeagueState state2 = (LeagueState) state.copy();
        assertEquals(state, state2);
    }

    @Test
    public void setEqualToTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3));
        LeagueState state2 = (LeagueState) state.copy();
        for (Standing standing : state2.getSimulationStandings().values())
            standing.setPoints(245);
        assertFalse(state.equals(state2));
        state2.setEqualTo(state);
        assertTrue(state.equals(state2));
        state2.setSimulationFixtureNo(345);
        assertFalse(state.equals(state2));
        state2.setEqualTo(state);
        assertTrue(state.equals(state2));
    }

    @Test
    public void competitionCompletedTest() {
        MethodName.log();

        Competition competition = TestCompetition.generate();
        int nNotCompleted = 0;
        for (Fixture fixture : competition.getFixtures())
            if (fixture.getStatus().equals(OutrightsFixtureStatus.PRE_MATCH))
                nNotCompleted++;
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3));
        @SuppressWarnings("unused")
        int nFixtures = competition.getFixtures().size();
        // System.out.println(nFixtures);
        int nUpdates = 0;
        FixtureResult result = new FixtureResult(3, 2, null);
        while (!state.competitionCompleted()) {
            // System.out.println(nUpdates);
            Fixture fixture = state.nextFixture();
            state.updateStateForFixtureResult(fixture, result);
            nUpdates++;
        }
        assertEquals(nNotCompleted, nUpdates);
    }

    @Test
    public void updateStateForFixtureResultTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3));
        Fixture fixture = competition.getFixtures().get(0);
        Fixture fixture2 = fixture.copy();
        FixtureResult result = new FixtureResult(3, 2, null);
        state.updateStateForFixtureResult(fixture2, result);
        Standing standingBeforeA = competition.generateStandings().get(fixture.getHomeTeamID());
        Standing standingBeforeB = competition.generateStandings().get(fixture.getAwayTeamID());
        Standing standingAfterA = state.getSimulationStandings().get(fixture.getHomeTeamID());
        Standing standingAfterB = state.getSimulationStandings().get(fixture.getAwayTeamID());

        assertEquals(standingBeforeA.getPlayed() + 1, standingAfterA.getPlayed());
        assertEquals(standingBeforeA.getWon() + 1, standingAfterA.getWon());
        assertEquals(standingBeforeA.getGoalsFor() + 3, standingAfterA.getGoalsFor());
        assertEquals(standingBeforeA.getGoalsAgainst() + 2, standingAfterA.getGoalsAgainst());
        assertEquals(standingBeforeA.getPoints() + 3, standingAfterA.getPoints());

        assertEquals(standingBeforeB.getPlayed() + 1, standingAfterB.getPlayed());
        assertEquals(standingBeforeB.getWon(), standingAfterB.getWon());
        assertEquals(standingBeforeB.getGoalsFor() + 2, standingAfterB.getGoalsFor());
        assertEquals(standingBeforeB.getGoalsAgainst() + 3, standingAfterB.getGoalsAgainst());
        assertEquals(standingBeforeB.getPoints(), standingAfterB.getPoints());

    }
}
