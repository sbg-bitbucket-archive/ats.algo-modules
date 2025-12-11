package ats.algo.montecarloframework;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;

/**
 * implements a simple best of n match format
 * 
 * @author Geoff
 *
 */
class TestMatchState extends MatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class MatchEventOutcome {
        boolean isMatchWinner;
        int scoreA;
        int scoreB;
    }

    protected double probAWinsSet;
    protected int scoreA;
    protected int scoreB;
    protected int nSetsInMatch;
    private int winningScore;
    private MatchEventOutcome outcome;

    public TestMatchState(int nSetsInMatch, double probAWinsSet) {
        this.nSetsInMatch = nSetsInMatch;
        this.probAWinsSet = probAWinsSet;
        winningScore = nSetsInMatch / 2 + 1;
        outcome = new MatchEventOutcome();
    }

    @Override
    public void setEqualTo(MatchState cc) {
        this.probAWinsSet = ((TestMatchState) cc).probAWinsSet;
        this.nSetsInMatch = ((TestMatchState) cc).nSetsInMatch;
        this.scoreA = ((TestMatchState) cc).scoreA;
        this.scoreB = ((TestMatchState) cc).scoreB;
    }


    public MatchEventOutcome updateStateForEvent(boolean AWinsSet) {
        if (AWinsSet)
            scoreA++;
        else
            scoreB++;
        outcome.scoreA = scoreA;
        outcome.scoreB = scoreB;
        outcome.isMatchWinner = (scoreA == winningScore) || (scoreB == winningScore);
        return outcome;
    }

    public MatchIncidentResult updateStateForEvent(MatchIncident matchIncident) {
        return null;
    }

    @Override
    public MatchState copy() {
        return null;
    }


    @Override
    public MatchIncidentPrompt getNextPrompt() {
        return null;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        return null;
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
        return false;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    public GamePeriod getGamePeriod() {
        return null;
    }

    @Override
    public boolean preMatch() {

        return false;
    }

    @Override
    public int secsLeftInCurrentPeriod() {

        return 0;
    }

    @Override
    public MatchState generateSimpleMatchState() {
        return this;
    }

}


