package ats.algo.sport.testsport;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;

public class TestSportMatchState extends MatchState {

    private static final long serialVersionUID = 1L;

    private int winningScore;
    private int scoreA;
    private int scoreB;


    TestSportMatchState() {}

    TestSportMatchState(TestSportMatchFormat format) {
        winningScore = format.getNoGamesInMatch();
        scoreA = 0;
        scoreB = 0;
    }



    public int getWinningScore() {
        return winningScore;
    }



    public void setWinningScore(int nGames) {
        this.winningScore = nGames;
    }



    public int getScoreA() {
        return scoreA;
    }



    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }



    public int getScoreB() {
        return scoreB;
    }



    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }



    @Override
    public MatchState copy() {
        TestSportMatchState state = new TestSportMatchState();
        state.winningScore = this.winningScore;
        state.scoreA = this.scoreA;
        state.scoreB = this.scoreB;
        return state;
    }

    @Override
    public void setEqualTo(MatchState other) {
        this.winningScore = ((TestSportMatchState) other).winningScore;
        this.scoreA = ((TestSportMatchState) other).scoreA;
        this.scoreB = ((TestSportMatchState) other).scoreB;
    }

    @Override
    @JsonIgnore
    public MatchIncidentPrompt getNextPrompt() {
        if (!isMatchCompleted())
            return new MatchIncidentPrompt("Enter game winner", "A");
        else
            return new MatchIncidentPrompt("Match over");
    }

    @Override
    @JsonIgnore
    public MatchIncident getMatchIncident(String response) {
        switch (response.toUpperCase()) {
            case "A":
                return TestSportMatchIncident.generateIncident(TeamId.A);
            case "B":
                return TestSportMatchIncident.generateIncident(TeamId.B);
            default:
                return null;
        }
    }

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        return null;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        return null;
    }

    @Override
    public boolean isMatchCompleted() {
        return scoreA == winningScore || scoreB == winningScore;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    public GamePeriod getGamePeriod() {
        if (preMatch())
            return GamePeriod.PREMATCH;
        if (isMatchCompleted())
            return GamePeriod.POSTMATCH;
        return GamePeriod.PERIOD1;
    }

    @Override
    public boolean preMatch() {
        return scoreA + scoreB == 0;
    }

    @Override
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    public MatchState generateSimpleMatchState() {
        return new TestSportSimpleMatchState(preMatch(), isMatchCompleted(), scoreA, scoreB);
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        if (matchIncident.getClass() == TestSportMatchIncident.class) {
            if (matchIncident.getIncidentSubType().equals("GAME_WINNER")) {
                if (matchIncident.getTeamId() == TeamId.A)
                    scoreA++;
                else
                    scoreB++;
            }
        }
        return new TestSportMatchIncidentResult();
    }

}
