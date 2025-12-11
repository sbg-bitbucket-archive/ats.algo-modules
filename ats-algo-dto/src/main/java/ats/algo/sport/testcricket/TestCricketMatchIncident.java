package ats.algo.sport.testcricket;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class TestCricketMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private CricketMatchIncidentType incidentSubType;
    private String cricketPlayerName;

    public enum CricketMatchIncidentType {
        BATFIRST,
        NORUNS,
        RUN1,
        RUN2,
        RUN3,
        RUN4,
        RUN5,
        RUN6,
        AJDUSTSCORE1,
        AJDUSTSCORE2,
        AJDUSTSCORE3,
        AJDUSTSCORE4,
        AJDUSTSCORE5,
        AJDUSTSCORE6,
        AJDUSTSCORE7,
        WICKET_CAUGHT,
        EXTRAS,
        EXTRAS1,
        EXTRAS2,
        EXTRAS3,
        EXTRAS4,
        EXTRAS5,
        BYES1,
        BYES2,
        BYES3,
        BYES4,
        BYES5,
        BYES6,
        BYES7,
        EXTRAS6,
        WICKET_BOWLED,
        WICKET_LBW,
        WICKET_RUN_OUT,
        WICKET_STUMPED,
        WICKET_OTHER,
        WICKET_RUN_OUT_AND_RUN1,
        WICKET_RUN_OUT_AND_RUN2,
        WICKET_RUN_OUT_AND_RUN3,
        WICKET_RUN_OUT_AND_RUN4,
        WICKET_RUN_OUT_AND_RUN5,
        WICKET_RUN_OUT_AND_RUN6,
        OVEREND,
        DECLARATION,
        FOLLOW_ON,
        DAY,
        SWITCHBATMAN,
        MATCH_COMPLETED,
        MATCH_FINISHED
    }

    public TestCricketMatchIncident() {
        super();
        this.incidentSubType = null;
        this.teamId = TeamId.UNKNOWN;
        this.cricketPlayerName = "Player unknown";
    }

    public void set(CricketMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    public void set(CricketMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
        this.teamId = TeamId.UNKNOWN;
    }

    public void setCricketMatchEventType(CricketMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    public String getCricketPlayerName() {
        return cricketPlayerName;
    }

    public void setCricketPlayerName(String cricketPlayerName) {
        this.cricketPlayerName = cricketPlayerName;
    }

    @Override
    public CricketMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setType(CricketMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        TestCricketMatchIncident other = (TestCricketMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
