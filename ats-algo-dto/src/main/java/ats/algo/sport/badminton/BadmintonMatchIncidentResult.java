package ats.algo.sport.badminton;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class BadmintonMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum BadmintonMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        POINTWON,
        GAMEWON,
        MATCHWON,
        MATCHABANDONED
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private BadmintonMatchIncidentResultType badmintonMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param badmintonMatchIncidentResultType
     */
    public BadmintonMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    BadmintonMatchIncidentResultType badmintonMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.badmintonMatchIncidentResultType = badmintonMatchIncidentResultType;
        this.teamId = teamId;
    }

    public BadmintonMatchIncidentResult() {
        super();
    }

    public BadmintonMatchIncidentResult(BadmintonMatchIncidentResultType badmintonMatchIncidentResultType) {
        super();
        this.badmintonMatchIncidentResultType = badmintonMatchIncidentResultType;
        this.playerAServedPoint = false;
        this.playerAwonPoint = false;
        this.teamId = TeamId.UNKNOWN;
    }

    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    public boolean isPlayerAwonPoint() {
        return playerAwonPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    public void setBadmintonMatchIncidentResultType(BadmintonMatchIncidentResultType badmintonMatchIncidentResultType) {
        this.badmintonMatchIncidentResultType = badmintonMatchIncidentResultType;
    }

    public BadmintonMatchIncidentResultType getBadmintonMatchIncidentResultType() {
        return badmintonMatchIncidentResultType;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((badmintonMatchIncidentResultType == null) ? 0
                        : badmintonMatchIncidentResultType.hashCode());
        result = prime * result + (playerAServedPoint ? 1231 : 1237);
        result = prime * result + (playerAwonPoint ? 1231 : 1237);
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        BadmintonMatchIncidentResult other = (BadmintonMatchIncidentResult) obj;
        if (badmintonMatchIncidentResultType != other.badmintonMatchIncidentResultType)
            return false;
        if (playerAServedPoint != other.playerAServedPoint)
            return false;
        if (playerAwonPoint != other.playerAwonPoint)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }



}
