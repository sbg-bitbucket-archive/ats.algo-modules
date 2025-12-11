package ats.algo.sport.outrights.calcengine.leagues;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureResult;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.AbstractState;

public class LeagueState extends AbstractState {

    private static final long serialVersionUID = 01L;
    private LeagueFormat leagueFormat;
    private Standings standings;
    private Standings simulationStandings;
    private List<Fixture> uncompletedFixtures;
    private int simulationFixtureNo;
    private String topAtXmasTeamId;
    private PlayoffsMgr playoffsMgr;
    private boolean playoffsMgrInitialisedForSimulationRun;
    private LocalDateTime dateOfXmas;

    public LeagueState(Competition competition, LeagueFormat leagueFormat) {
        super(competition);
        this.leagueFormat = leagueFormat;
        this.dateOfXmas = competition.getDateOfXmas();
        /*
         * create a list of the uncompleted fixtures that need to be simulated
         */
        Fixtures fixtures = competition.getFixtures();
        uncompletedFixtures = new ArrayList<Fixture>();
        List<Fixture> completedPlayoffFixtures = new ArrayList<Fixture>();
        LocalDateTime dateOfLatestCompletedFixture = LocalDateTime.parse("2000-01-01T00:00:00");
        for (Fixture fixture : fixtures.list()) {
            if (fixture.getStatus() == OutrightsFixtureStatus.COMPLETED) {
                LocalDateTime thisFixtureDate = fixture.getDateTime();
                if (thisFixtureDate.isAfter(dateOfLatestCompletedFixture))
                    dateOfLatestCompletedFixture = thisFixtureDate;
                if (fixture.getFixtureType() != FixtureType.LEAGUE) {
                    /*
                     * must be a playoff fixture
                     */
                    completedPlayoffFixtures.add(fixture);
                }
            } else {
                uncompletedFixtures.add(fixture);
            }

        }
        if (dateOfLatestCompletedFixture.isAfter(dateOfXmas)) {
            /*
             * need to find out who the winner at xmas was
             */
            Standings xmasStandings = Standings.generateStandingsFromFixtures(competition.getTeams(), fixtures,
                            f -> f.getDateTime().isBefore(dateOfXmas));
            topAtXmasTeamId = xmasStandings.finishOrder().get(0).getTeamId();
        }
        simulationFixtureNo = 0;
        /*
         * initialise the objects that will be used during simulation runs, making a copy of any objects that will be
         * modified
         */
        standings = competition.generateStandings();
        simulationStandings = standings.copy();

        playoffsMgr = new PlayoffsMgr(leagueFormat, standings, completedPlayoffFixtures);
        playoffsMgrInitialisedForSimulationRun = false;
    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        LeagueState other = (LeagueState) matchState;
        super.setEqualTo(matchState);
        /*
         * only need to reset objects that get changed during a simulation run
         */
        this.simulationFixtureNo = other.simulationFixtureNo;
        this.standings = other.standings;
        for (Entry<String, Standing> e : this.standings.entrySet()) {
            Standing simulationStanding = simulationStandings.get(e.getKey());
            simulationStanding.setEqualTo(e.getValue());
        }
        this.topAtXmasTeamId = other.topAtXmasTeamId;
        this.playoffsMgrInitialisedForSimulationRun = other.playoffsMgrInitialisedForSimulationRun;
        this.playoffsMgr.setEqualTo(other.playoffsMgr);
    }

    public Standings getSimulationStandings() {
        return simulationStandings;
    }

    public String getTopAtXmasTeamId() {
        return topAtXmasTeamId;
    }

    public LocalDateTime getDateOfXmas() {
        return dateOfXmas;
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
            if (!playoffsMgrInitialisedForSimulationRun)
                playoffsMgr.initialiseforStartOfSimulation(simulationStandings);
            playoffsMgrInitialisedForSimulationRun = true;
            return playoffsMgr.nextFixture(fixture);
        }

    }

    @Override
    public void updateStateForFixtureResult(Fixture fixture, FixtureResult result) {
        if (fixture.getFixtureType() == FixtureType.LEAGUE) {
            Standing standingA = simulationStandings.get(fixture.getHomeTeamID());
            Standing standingB = simulationStandings.get(fixture.getAwayTeamID());
            if (topAtXmasTeamId == null && fixture.getDateTime().isAfter(dateOfXmas))
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
    public AlgoMatchState copy() {
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
