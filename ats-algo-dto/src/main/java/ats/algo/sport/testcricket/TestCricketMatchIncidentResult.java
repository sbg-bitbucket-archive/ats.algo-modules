package ats.algo.sport.testcricket;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class TestCricketMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum CricketMatchIncidentResultType {
        PREMATCH,
        BAT,
        NORUNS,
        RUN1,
        RUN2,
        RUN3,
        RUN4,
        RUN5,
        RUN6,
        WICKET_CAUGHT,
        EXTRAS,
        BYES,
        WICKET_BOWLED,
        WICKET_LBW,
        WICKET_RUN_OUT,
        WICKET_STUMPED,
        WICKET_OTHER,
        WICKET_RUN_OUT_AND_RUN,
        MATCHWON,
        ADJUSTSCORE,
        DRAW,
        MATCH_FINISHED,
        DECLARATION,
        DAY,
        SWITCHBATMAN,
        FOLLOW_ON,
        MATCH_COMPLETED
    }

    private boolean lastPointWicket;
    private int wicketType;
    private CricketMatchIncidentResultType cricketMatchIncidentResultType;
    private TeamId teamId;
    private String cricketPlayerName;

    public TestCricketMatchIncidentResult() {
        super();
    }

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param cricketMatchIncidentResultType
     * @param teamId
     */
    public TestCricketMatchIncidentResult(boolean lastPointWicket, int wicketType,
                    CricketMatchIncidentResultType cricketMatchIncidentResultType, TeamId teamId) {
        super();
        this.lastPointWicket = lastPointWicket;
        this.wicketType = wicketType;
        this.cricketMatchIncidentResultType = cricketMatchIncidentResultType;
        this.teamId = teamId;
        this.cricketPlayerName = "Player unknown";
    }

    public TestCricketMatchIncidentResult(CricketMatchIncidentResultType cricketMatchIncidentResultType,
                    TeamId teamId) {
        super();
        this.lastPointWicket = false;
        this.wicketType = 0;
        this.cricketMatchIncidentResultType = cricketMatchIncidentResultType;
        this.teamId = teamId;
        this.cricketPlayerName = "Player unknown";
    }

    public TestCricketMatchIncidentResult(CricketMatchIncidentResultType cricketMatchIncidentResultType) {
        this.cricketMatchIncidentResultType = cricketMatchIncidentResultType;
    }

    public boolean isLastPointWicket() {
        return lastPointWicket;
    }

    public void setLastPointWicket(boolean lastPointWicket) {
        this.lastPointWicket = lastPointWicket;
    }

    public int getWicketType() {
        return wicketType;
    }

    public void setWicketType(int wicketType) {
        this.wicketType = wicketType;
    }

    public void setCricketMatchIncidentResultType(CricketMatchIncidentResultType crikectMatchIncidentResultType) {
        this.cricketMatchIncidentResultType = crikectMatchIncidentResultType;
    }

    public CricketMatchIncidentResultType getcricketMatchIncidentResultType() {
        return cricketMatchIncidentResultType;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    public String getCricketPlayerName() {
        return cricketPlayerName;
    }

    public void setCricketPlayerName(String cricketPlayerName) {
        this.cricketPlayerName = cricketPlayerName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                        + ((cricketMatchIncidentResultType == null) ? 0 : cricketMatchIncidentResultType.hashCode());
        result = prime * result + (lastPointWicket ? 1231 : 1237);
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        result = prime * result + wicketType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestCricketMatchIncidentResult other = (TestCricketMatchIncidentResult) obj;
        if (cricketMatchIncidentResultType != other.cricketMatchIncidentResultType)
            return false;
        if (lastPointWicket != other.lastPointWicket)
            return false;
        if (teamId != other.teamId)
            return false;
        if (wicketType != other.wicketType)
            return false;
        return true;
    }

}
