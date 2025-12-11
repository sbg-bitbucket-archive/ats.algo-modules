package ats.algo.sport.tennis;


import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;
import ats.core.util.json.JsonUtil;

public class TennisMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum TennisMatchIncidentResultType {
        FIRSTSERVERINMATCHSET,
        MATCHABANDONED,
        POINTWONONLY,
        POWERPOINTSTART,
        GAMEWONA,
        GAMEWONB,
        SETWON,
        MATCHWONA,
        MATCHWONB,
        INJURY_STATUS_REVERSE,
        FAULT,
        POINT_START,
        CHALLENGER_BALLMARK,
        DELAY,
        PREMATCH
    }

    private boolean teamAServedPoint;
    private int playerIdServedPoint;
    private boolean teamAWonPoint;
    private TennisMatchIncidentResultType tennisMatchIncidentResultType;
    private TeamId abandonMatchWinner = TeamId.UNKNOWN;

    public TennisMatchIncidentResult() {
        super();
    }

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tennisMatchIncidentResultType
     */
    public TennisMatchIncidentResult(boolean teamAServedPoint, boolean teamAWonPoint, int playerIdServedPoint,
                    TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        super();
        this.teamAServedPoint = teamAServedPoint;
        this.playerIdServedPoint = playerIdServedPoint;
        this.teamAWonPoint = teamAWonPoint;
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }

    /**
     * constructor to use for matchStart
     * 
     * @param tennisMatchIncidentResultType
     */
    public TennisMatchIncidentResult(TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        super();
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }


    public boolean isTeamAServedPoint() {
        return teamAServedPoint;
    }

    public void setTeamAServedPoint(boolean teamAServedPoint) {
        this.teamAServedPoint = teamAServedPoint;
    }

    public boolean isTeamAWonPoint() {
        return teamAWonPoint;
    }



    public TeamId getAbandonMatchWinner() {
        return abandonMatchWinner;
    }

    public void setAbandonMatchWinner(TeamId abandonMatchWinner) {
        this.abandonMatchWinner = abandonMatchWinner;
    }

    public int getPlayerIdServedPoint() {
        return playerIdServedPoint;
    }

    public void setPlayerIdServedPoint(int playerServedPoint) {
        this.playerIdServedPoint = playerServedPoint;
    }

    public void setTeamAWonPoint(boolean teamAWonPoint) {
        this.teamAWonPoint = teamAWonPoint;
    }

    public TennisMatchIncidentResultType getTennisMatchIncidentResultType() {
        return tennisMatchIncidentResultType;
    }

    public void setTennisMatchIncidentResultType(TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }



    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // return "TennisMatchIncidentResult [teamAServedPoint=" + teamAServedPoint + ", playerIdServedPoint="
        // + playerIdServedPoint + ", teamAWonPoint=" + teamAWonPoint + ", tennisMatchIncidentResultType="
        // + tennisMatchIncidentResultType + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + playerIdServedPoint;
        result = prime * result + (teamAServedPoint ? 1231 : 1237);
        result = prime * result + (teamAWonPoint ? 1231 : 1237);
        result = prime * result
                        + ((tennisMatchIncidentResultType == null) ? 0 : tennisMatchIncidentResultType.hashCode());
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
        TennisMatchIncidentResult other = (TennisMatchIncidentResult) obj;
        if (playerIdServedPoint != other.playerIdServedPoint)
            return false;
        if (teamAServedPoint != other.teamAServedPoint)
            return false;
        if (teamAWonPoint != other.teamAWonPoint)
            return false;
        if (tennisMatchIncidentResultType != other.tennisMatchIncidentResultType)
            return false;
        return true;
    }

    /**
     * use isTeamAServedPoint() instead
     * 
     * @return
     */
    @Deprecated
    @JsonIgnore
    public boolean playerAServedPoint() {
        return teamAServedPoint;
    }

    /**
     * use isTeamAWonPoint() instead
     * 
     * @return
     */
    @Deprecated
    @JsonIgnore
    public boolean playerAwonPoint() {
        return teamAWonPoint;
    }


    /**
     * use getPlayerIdServedPoint instead
     * 
     * @return
     */
    @Deprecated
    @JsonIgnore
    public int getPlayerServedPoint() {
        return playerIdServedPoint;
    }


    /**
     * use isTeamAServerdPoint() instead
     * 
     * @return
     */
    @Deprecated
    @JsonIgnore
    public boolean isPlayerAServedPoint() {
        return teamAServedPoint;
    }

    /**
     * use isTeamAWonPoint() instead
     * 
     * @return
     */
    @Deprecated
    @JsonIgnore
    public boolean isPlayerAwonPoint() {
        return teamAWonPoint;
    }

    public TennisMatchIncidentResult clone() {
        TennisMatchIncidentResult clone = new TennisMatchIncidentResult();
        clone.setAbandonMatchWinner(this.getAbandonMatchWinner());
        clone.setTennisMatchIncidentResultType(this.tennisMatchIncidentResultType);
        return clone;
    }


}

