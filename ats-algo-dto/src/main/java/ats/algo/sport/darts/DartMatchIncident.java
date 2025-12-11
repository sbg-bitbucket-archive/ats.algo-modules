package ats.algo.sport.darts;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class DartMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    public enum DartMatchIncidentType {
        SET_PLAYER_STARTING_MATCH_AT_OCHE,
        DART_THROWN,
    }

    private DartMatchIncidentType incidentSubType;
    private int multiplier; // 0,1,2 or 3
    private int numberHit; // 0-20,25

    /**
     * Used at start of match only
     * 
     * @param elapsedTime
     * @param playerStartingMatchAtOche
     */
    public DartMatchIncident(int elapsedTime, TeamId playerStartingMatchAtOche) {
        super();
        super.setElapsedTime(elapsedTime);
        this.incidentSubType = DartMatchIncidentType.SET_PLAYER_STARTING_MATCH_AT_OCHE;
        this.multiplier = 0;
        this.numberHit = 0;
        this.teamId = playerStartingMatchAtOche;
    }

    /**
     * used each time a dart is thrown
     * 
     * @param elapsedTime
     * @param multiplier
     * @param numberHit
     */
    public DartMatchIncident(int elapsedTime, int multiplier, int numberHit) {
        super();
        super.setElapsedTime(elapsedTime);
        this.incidentSubType = DartMatchIncidentType.DART_THROWN;
        this.multiplier = multiplier;
        this.numberHit = numberHit;
        this.teamId = TeamId.UNKNOWN;
    }

    public DartMatchIncident() {
        super();
        super.setElapsedTime(0);
        this.incidentSubType = null;
        this.multiplier = 0;
        this.numberHit = 0;
        this.teamId = TeamId.UNKNOWN;;
    }

    @Override
    public DartMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(DartMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getNumberHit() {
        return numberHit;
    }

    @JsonIgnore
    public TeamId getPlayerStartingMatchAtOche() {
        return getTeamId();
    }



}
