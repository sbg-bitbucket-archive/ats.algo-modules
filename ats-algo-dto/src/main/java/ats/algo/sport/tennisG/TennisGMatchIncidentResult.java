package ats.algo.sport.tennisG;

import ats.algo.core.baseclasses.MatchIncidentResult;

public class TennisGMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TennisGMatchIncidentResult() {
        super();
    }

    public enum TennisMatchIncidentResultType {
        FIRSTSERVERINMATCHSET,
        POINTWONONLY,
        GAMEWONA,
        GAMEWONB,
        SETWONA,
        SETWONB,
        MATCHWONA,
        MATCHWONB
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private TennisMatchIncidentResultType tennisMatchIncidentResultType;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tennisMatchIncidentResultType
     */
    public TennisGMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }

    /**
     * constructor to use for matchStart
     * 
     * @param tennisMatchIncidentResultType
     */
    public TennisGMatchIncidentResult(TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        super();
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }

    public boolean playerAServedPoint() {
        return playerAServedPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public boolean playerAwonPoint() {
        return playerAwonPoint;
    }

    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    public TennisMatchIncidentResultType getTennisMatchIncidentResultType() {
        return tennisMatchIncidentResultType;
    }

    public void setTennisMatchIncidentResultType(TennisMatchIncidentResultType tennisMatchIncidentResultType) {
        this.tennisMatchIncidentResultType = tennisMatchIncidentResultType;
    }
}

