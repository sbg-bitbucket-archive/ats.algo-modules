package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class BeachVolleyballMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum BeachVolleyballMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        POINTWON,
        SETWON,
        MATCHWON
    }

    public BeachVolleyballMatchIncidentResult() {
        super();
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private BeachVolleyballMatchIncidentResultType beachVolleyballMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tennisMatchIncidentResultType
     */
    public BeachVolleyballMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    BeachVolleyballMatchIncidentResultType beachVolleyballMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.beachVolleyballMatchIncidentResultType = beachVolleyballMatchIncidentResultType;
        this.teamId = teamId;
    }

    public BeachVolleyballMatchIncidentResult(
                    BeachVolleyballMatchIncidentResultType beachVolleyballMatchIncidentResultType) {
        this.beachVolleyballMatchIncidentResultType = beachVolleyballMatchIncidentResultType;
    }

    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public boolean isPlayerAwonPoint() {
        return playerAwonPoint;
    }

    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    public BeachVolleyballMatchIncidentResultType getVolleyballMatchIncidentResultType() {
        return beachVolleyballMatchIncidentResultType;
    }

    public void setVolleyballMatchIncidentResultType(
                    BeachVolleyballMatchIncidentResultType beachVolleyballMatchIncidentResultType) {
        this.beachVolleyballMatchIncidentResultType = beachVolleyballMatchIncidentResultType;
    }

    public BeachVolleyballMatchIncidentResultType getBeachVolleyballMatchIncidentResultType() {
        return beachVolleyballMatchIncidentResultType;
    }

    public void setBeachVolleyballMatchIncidentResultType(
                    BeachVolleyballMatchIncidentResultType beachVolleyballMatchIncidentResultType) {
        this.beachVolleyballMatchIncidentResultType = beachVolleyballMatchIncidentResultType;
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
        result = prime * result + ((beachVolleyballMatchIncidentResultType == null) ? 0
                        : beachVolleyballMatchIncidentResultType.hashCode());
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
        BeachVolleyballMatchIncidentResult other = (BeachVolleyballMatchIncidentResult) obj;
        if (beachVolleyballMatchIncidentResultType != other.beachVolleyballMatchIncidentResultType)
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
