package ats.algo.sport.outrights.calcengine.leagues;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.leagues.LeagueState;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class LeagueStateTest {

    @Test
    public void copyTest() {
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE));
        LeagueState state2 = (LeagueState) state.copy();
        assertEquals(state, state2);
    }

    @Test
    public void setEqualToTest() {
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE));
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
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE));
        int nFixtures = competition.getFixtures().size();
        System.out.println(nFixtures);
        int nUpdates = 0;
        FixtureResult result = new FixtureResult(3, 2, null);
        while (!state.competitionCompleted()) {
            System.out.println(nUpdates);
            Fixture fixture = state.nextFixture();
            state.updateStateForFixtureResult(fixture, result);
            nUpdates++;
        }
        assertEquals(nFixtures, nUpdates);
    }

    @Test
    public void updateStateForFixtureResultTest() {
        Competition competition = TestCompetition.generate();
        LeagueState state = new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE));
        Fixture fixture = competition.getFixtures().get(0);
        Fixture fixture2 = fixture.copy();
        FixtureResult result = new FixtureResult(3, 2, null);
        state.updateStateForFixtureResult(fixture2, result);
        Standing standingBeforeA = competition.getStandings().get(fixture.getHomeTeamID());
        Standing standingBeforeB = competition.getStandings().get(fixture.getAwayTeamID());
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
