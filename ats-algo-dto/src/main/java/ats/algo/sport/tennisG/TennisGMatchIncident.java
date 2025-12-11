package ats.algo.sport.tennisG;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class TennisGMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;

    public enum TennisMatchIncidentType {
        MATCH_STARTING,
        POINT_WON
    }

    private TennisMatchIncidentType matchIncidentType;
    private TeamId serverAtStartOfMatch;
    private TeamId pointWinner;

    /**
     * constructor without params is used to create an instance of the class which is then updated many times - avoids
     * creating a new instance every iteration of the monte carlo model
     */
    public TennisGMatchIncident() {
        super();
        this.matchIncidentType = null;
        this.serverAtStartOfMatch = null;
        this.pointWinner = null;
    }

    /**
     * Constructor with params can be used to save a line of code when not worried about performance overhead of
     * instantiating new object for every incident
     * 
     * @param elapsedTime time in seconds since start of match
     * @param matchIncidentType
     * @param playerId if incidentType is MATCH_STARTING then the id of the first player to serve in the match. If
     *        incident type is POINT_WON then the id of the player winning the point
     */
    public TennisGMatchIncident(int elapsedTime, TennisMatchIncidentType matchIncidentType, TeamId playerId) {
        setTennisMatchIncident(elapsedTime, matchIncidentType, playerId);
    }

    /**
     * 
     * @param elapsedTime time in seconds since start of match
     * @param matchIncidentType
     * @param playerId if incidentType is MATCH_STARTING then the id of the first player to serve in the match. If
     *        incident type is POINT_WON then the id of the player winning the point
     */
    public void setTennisMatchIncident(int elapsedTime, TennisMatchIncidentType matchIncidentType, TeamId playerId) {
        super.setElapsedTime(elapsedTime);
        this.matchIncidentType = matchIncidentType;
        switch (matchIncidentType) {
            case MATCH_STARTING:
                serverAtStartOfMatch = playerId;
                pointWinner = TeamId.UNKNOWN;
                break;
            case POINT_WON:
                serverAtStartOfMatch = TeamId.UNKNOWN;
                pointWinner = playerId;
                break;
        }
    }

    @Override
    public TennisMatchIncidentType getIncidentSubType() {
        return matchIncidentType;
    }


    public TeamId getPointWinner() {
        return pointWinner;
    }

    public TeamId getServerAtStartOfMatch() {
        return serverAtStartOfMatch;
    }

    public void setServerAtStartOfMatch(TeamId serverAtStartOfMatch) {
        this.serverAtStartOfMatch = serverAtStartOfMatch;
    }

    public void setPointWinner(TeamId pointWinner) {
        this.pointWinner = pointWinner;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchIncidentType == null) ? 0 : matchIncidentType.hashCode());
        result = prime * result + ((pointWinner == null) ? 0 : pointWinner.hashCode());
        result = prime * result + ((serverAtStartOfMatch == null) ? 0 : serverAtStartOfMatch.hashCode());
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
        TennisGMatchIncident other = (TennisGMatchIncident) obj;
        if (matchIncidentType != other.matchIncidentType)
            return false;
        if (pointWinner != other.pointWinner)
            return false;
        if (serverAtStartOfMatch != other.serverAtStartOfMatch)
            return false;
        return true;
    }

    public void setType(TennisMatchIncidentType incidentType) {
        this.matchIncidentType = incidentType;
    }

    public TennisMatchIncidentType getType() {
        return matchIncidentType;
    }
}
