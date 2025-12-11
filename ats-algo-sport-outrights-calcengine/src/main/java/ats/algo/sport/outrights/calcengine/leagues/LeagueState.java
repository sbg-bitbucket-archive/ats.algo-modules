package ats.algo.sport.outrights.calcengine.leagues;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.calcengine.core.FixtureStatus;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.calcengine.core.AbstractState;

public class LeagueState extends AbstractState {

    private static final long serialVersionUID = 01L;
    private LeagueFormat leagueFormat;
    private Standings simulationStandings;
    private List<Fixture> uncompletedFixtures;
    private int simulationFixtureNo;
    private String topAtXmasTeamId;
    private PlayoffsMgr playoffsMgr;
    private boolean playoffTeamsKnown;

    private static final LocalDateTime xmas = LocalDateTime.parse("2018-12-25T00:00:00");

    public LeagueState(Competition competition, LeagueFormat leagueFormat) {
        super(competition);
        this.leagueFormat = leagueFormat;
        /*
         * create a list of the uncompleted fixtures that need to be simulated
         */
        Fixtures fixtures = competition.getFixtures();
        uncompletedFixtures = new ArrayList<Fixture>(fixtures.size());
        for (Fixture fixture : fixtures.list()) {
            if (!(fixture.getStatus() == FixtureStatus.COMPLETED))
                uncompletedFixtures.add(fixture);
        }
        simulationFixtureNo = 0;
        /*
         * initialise the objects that will be used during simulation runs, making a copy of any objects that will be
         * modified
         */
        Standings standings = competition.getStandings();
        simulationStandings = standings.copy();
        playoffsMgr = new PlayoffsMgr(leagueFormat);
        playoffTeamsKnown = false;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        LeagueState other = (LeagueState) matchState;
        super.setEqualTo(matchState);
        /*
         * only need to reset objects that get changed during a simulation run
         */
        this.simulationFixtureNo = other.simulationFixtureNo;
        for (Entry<String, Standing> e : other.competition.getStandings().entrySet()) {
            Standing simulationStanding = simulationStandings.get(e.getKey());
            simulationStanding.setEqualTo(e.getValue());
        }
        this.topAtXmasTeamId = other.topAtXmasTeamId;
        this.playoffTeamsKnown = false;
    }

    public Standings getSimulationStandings() {
        return simulationStandings;
    }

    public String getTopAtXmasTeamId() {
        return topAtXmasTeamId;
    }

    public static LocalDateTime getXmas() {
        return xmas;
    }

    @Override
    public Fixture nextFixture() {
        Fixture fixture = uncompletedFixtures.get(simulationFixtureNo);
        simulationFixtureNo++;
        if (fixture.getFixtureType() == FixtureType.LEAGUE)
            return fixture;
        else {
            /*
             * must be a playoff match. Initialise playoffsMgr if this is the first such match
             */
            if (!playoffTeamsKnown)
                playoffsMgr.initialiseforEndOfRegularLeague(simulationStandings);
            playoffTeamsKnown = true;
            return playoffsMgr.nextFixture(fixture);
        }

    }

    @Override
    public void updateStateForFixtureResult(Fixture fixture, FixtureResult result) {
        if (fixture.getFixtureType() == FixtureType.LEAGUE) {
            Standing standingA = simulationStandings.get(fixture.getHomeTeamID());
            Standing standingB = simulationStandings.get(fixture.getAwayTeamID());
            if (topAtXmasTeamId == null && fixture.getDateTime().isAfter(xmas))
                topAtXmasTeamId = simulationStandings.topTeamID();
            standingA.incrementMatchesPlayed();
            standingB.incrementMatchesPlayed();
            int scoreDiff = result.getScoreA() - result.getScoreB();
            if (scoreDiff > 0) {
                standingA.incrementPoints(3);
                standingA.incrementMatchesWon();
            } else if (scoreDiff < 0) {
                standingB.incrementPoints(3);
                standingB.incrementMatchesWon();
            } else {
                standingA.incrementPoints(1);
                standingB.incrementPoints(1);
                standingA.incrementMatchesDrawn();
                standingB.incrementMatchesDrawn();
            }
            standingA.incrementGoalsFor(result.getScoreA());
            standingB.incrementGoalsFor(result.getScoreB());
            standingA.incrementGoalsAgainst(result.getScoreB());
            standingB.incrementGoalsAgainst(result.getScoreA());
        } else {
            /*
             * must be a playoff match
             */
            playoffsMgr.updateStateForFixtureResult(fixture, result);
        }

    }

    @Override
    public CompetitionWarnings checkStateForErrors() {
        /*
         * check that standings and remaining fixtures are consistent.
         * 
         * Note this currently only deals with regular league fixture types.
         */
        Map<String, Integer> played = new HashMap<String, Integer>(simulationStandings.size());
        for (Entry<String, Standing> e : simulationStandings.entrySet()) {
            played.put(e.getKey(), e.getValue().getPlayed());
        }
        for (Fixture fixture : uncompletedFixtures) {
            // TODO Needs more logic for handling playoffs
            if (fixture.getFixtureType() == FixtureType.LEAGUE) {
                incrementPlayed(fixture.getHomeTeamID(), played);
                incrementPlayed(fixture.getAwayTeamID(), played);
            }
        }
        // TODO - add checks for goals scored
        Teams teams = competition.getTeams();
        int expectedPlayed = 2 * teams.size() - 2;
        List<String> warnings = new ArrayList<>();
        for (Entry<String, Integer> e : played.entrySet()) {
            if (e.getValue() != expectedPlayed)
                warnings.add(String.format("Team: %s, will play: %d, expected: %d",
                                teams.get(e.getKey()).getDisplayName(), e.getValue(), expectedPlayed));
        }
        CompetitionWarnings summary = new CompetitionWarnings();
        if (warnings.size() > 0) {
            summary.setStateOk(false);
            summary.setWarningMessages(warnings);
        } else
            summary.setStateOk(true);
        return summary;
    }

    private void incrementPlayed(String tId, Map<String, Integer> played) {
        int nPlayed = played.get(tId);
        nPlayed++;
        played.put(tId, nPlayed);

    }

    @Override
    public MatchState copy() {
        LeagueState cc = new LeagueState(competition, leagueFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public boolean competitionCompleted() {
        return simulationFixtureNo >= uncompletedFixtures.size();
    }



    public PlayoffsMgr getPlayoffsMgr() {
        return playoffsMgr;
    }



    /**
     * for unit testing only
     * 
     * @param i
     */
    void setSimulationFixtureNo(int i) {
        simulationFixtureNo = i;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uncompletedFixtures == null) ? 0 : uncompletedFixtures.hashCode());
        result = prime * result + simulationFixtureNo;
        result = prime * result + ((simulationStandings == null) ? 0 : simulationStandings.hashCode());
        result = prime * result + ((topAtXmasTeamId == null) ? 0 : topAtXmasTeamId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeagueState other = (LeagueState) obj;
        if (uncompletedFixtures == null) {
            if (other.uncompletedFixtures != null)
                return false;
        } else if (!uncompletedFixtures.equals(other.uncompletedFixtures))
            return false;
        if (simulationFixtureNo != other.simulationFixtureNo)
            return false;
        if (simulationStandings == null) {
            if (other.simulationStandings != null)
                return false;
        } else if (!simulationStandings.equals(other.simulationStandings))
            return false;
        if (topAtXmasTeamId == null) {
            if (other.topAtXmasTeamId != null)
                return false;
        } else if (!topAtXmasTeamId.equals(other.topAtXmasTeamId))
            return false;
        return true;
    }

}
