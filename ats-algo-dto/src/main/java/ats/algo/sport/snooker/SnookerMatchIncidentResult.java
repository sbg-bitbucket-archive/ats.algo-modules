package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class SnookerMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum SnookerMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        WHOISATTABLE,
        REDPOT,
        YELLOWPOT,
        GREENPOT,
        BROWNPOT,
        BLUEPOT,
        PINKPOT,
        BLACKPOT,
        FRAMEWON,
        MATCHWON,
        DRAW
    }

    private boolean playerAServedPoint;
    private boolean playerAwonFrame4;
    private SnookerMatchIncidentResultType snookerMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tennisMatchIncidentResultType
     */
    public SnookerMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonFrame4,
                    SnookerMatchIncidentResultType snookerMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonFrame4 = playerAwonFrame4;
        this.snookerMatchIncidentResultType = snookerMatchIncidentResultType;
        this.teamId = teamId;
    }

    public SnookerMatchIncidentResult() {
        super();
    }

    public SnookerMatchIncidentResult(SnookerMatchIncidentResultType snookerMatchIncidentResultType) {
        this.snookerMatchIncidentResultType = snookerMatchIncidentResultType;
    }

    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public void setPlayerAwonFrame4(boolean playerAwonFrame4) {
        this.playerAwonFrame4 = playerAwonFrame4;
    }

    public boolean isPlayerAwonFrame4() {
        return playerAwonFrame4;
    }

    public void setSnookerMatchIncidentResultType(SnookerMatchIncidentResultType snookerMatchIncidentResultType) {
        this.snookerMatchIncidentResultType = snookerMatchIncidentResultType;
    }

    public SnookerMatchIncidentResultType getSnookerMatchIncidentResultType() {
        return snookerMatchIncidentResultType;
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
        result = prime * result + (playerAServedPoint ? 1231 : 1237);
        result = prime * result + (playerAwonFrame4 ? 1231 : 1237);
        result = prime * result
                        + ((snookerMatchIncidentResultType == null) ? 0 : snookerMatchIncidentResultType.hashCode());
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
        SnookerMatchIncidentResult other = (SnookerMatchIncidentResult) obj;
        if (playerAServedPoint != other.playerAServedPoint)
            return false;
        if (playerAwonFrame4 != other.playerAwonFrame4)
            return false;
        if (snookerMatchIncidentResultType != other.snookerMatchIncidentResultType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }
}
