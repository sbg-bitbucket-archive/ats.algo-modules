package ats.algo.sport.ufc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultMap;

public class UfcMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private UfcMatchFormat matchFormat;
    private boolean prematch;
    private boolean matchCompleted;
    @JsonIgnore
    private TeamId winnningTeam;
    @JsonIgnore
    private int roundPlayed;
    @JsonIgnore
    private boolean decision;
    @JsonIgnore
    private boolean firstHalf;
    @JsonIgnore
    private UfcMatchPeriod matchPeriod;

    public TeamId getWinnningTeam() {
        return winnningTeam;
    }

    public void setWinnningTeam(TeamId winnningTeam) {
        this.winnningTeam = winnningTeam;
    }

    public int getRoundPlayed() {
        return roundPlayed;
    }

    public void setRoundPlayed(int roundPlayed) {
        this.roundPlayed = roundPlayed;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public boolean isFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(boolean firstHalf) {
        this.firstHalf = firstHalf;
    }

    public UfcMatchState() {
        super();
        matchFormat = new UfcMatchFormat();
        prematch = true;
        matchCompleted = false;
        firstHalf = false;
        matchPeriod = UfcMatchPeriod.PREMATCH;
    }

    public UfcMatchState(UfcMatchFormat matchFormat) {
        super();
        this.matchFormat = matchFormat;
        prematch = true;
        matchCompleted = false;
        firstHalf = false;
        matchPeriod = UfcMatchPeriod.PREMATCH;
    }

    @Override
    public MatchIncidentPrompt getNextPrompt() {
        return new MatchIncidentPrompt("Start or End");
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        return new UfcMatchIncident();
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
        return matchCompleted;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    @Override
    public GamePeriod getGamePeriod() {
        return GamePeriod.PREMATCH;
    }

    @Override
    public boolean preMatch() {
        return prematch;
    }

    @Override
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    public AlgoMatchState copy() {
        UfcMatchState cc = new UfcMatchState((UfcMatchFormat) this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    @Override
    public SimpleMatchState generateSimpleMatchState() {
        return new UfcSimpleMatchState(this.prematch, this.isMatchCompleted(), this.isDecision(), this.winnningTeam,
                        this.roundPlayed, this.firstHalf);
    }

    public String getSequenceIdForGame(int num) {
        return num + "";
    }

    public String getSequenceIdForRound(int num) {
        return num + 0.5 + "";
    }

    public String getSequenceIdForMatch() {
        return "M";
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchManualResult) {
        Map<String, MatchResultElement> map = matchManualResult.getMap();
        UfcMatchState ufcMatchState = new UfcMatchState();
        int roundPlayer = Integer.valueOf(map.get("roundPlayed").getValue());
        TeamId winnningTeam = TeamId.UNKNOWN;
        if (map.get("winningTeam").getValue().toUpperCase().equals(TeamId.A.toString())) {
            winnningTeam = TeamId.A;
        }
        if (map.get("winningTeam").getValue().toUpperCase().equals(TeamId.B.toString())) {
            winnningTeam = TeamId.B;
        }
        boolean decision = Boolean.valueOf(map.get("decision").getValue());
        boolean firstHalf = Boolean.valueOf(map.get("firstHalf").getValue());
        ufcMatchState.setRoundPlayed(roundPlayer);
        ufcMatchState.setWinnningTeam(winnningTeam);
        ufcMatchState.setDecision(decision);
        ufcMatchState.setFirstHalf(firstHalf);
        return ufcMatchState;
    }
}
