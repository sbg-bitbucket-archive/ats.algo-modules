package ats.algo.sport.afl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class AflMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private AflMatchIncidentType incidentSubType;
    private FieldPositionType fieldPosition;

    public enum AflMatchIncidentType {
        POINT_SCORED, // only for game simulations

        ONE_POINTS_SCORED,

        BALL_POSITION_SETTING,
        FIELD_POSITION_SETTING,
        SIX_POINTS_SCORED,
        RESET_FIELD_BALL_INFO
    }



    enum FieldPositionType {
        A50,
        A35,
        MIDFIELD,
        B50,
        B35,
        UNKNOWN
    }

    public AflMatchIncident() {
        super();
    }

    /**
     * 
     * @param incidentSubType
     * @param elapsedTime
     * @param teamId
     */
    public AflMatchIncident(AflMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        this(incidentSubType, elapsedTime, FieldPositionType.UNKNOWN, teamId);
    }

    /**
     * 
     * @param incidentSubType
     * @param elapsedTime
     * @param fieldPosition
     * @param teamId
     */
    public AflMatchIncident(AflMatchIncidentType incidentSubType, int elapsedTime, FieldPositionType fieldPosition,
                    TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.fieldPosition = fieldPosition;
        this.teamId = teamId;
    }

    public FieldPositionType getFieldPosition() {
        return fieldPosition;
    }

    public void set(AflMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }

    public AflMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(AflMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    @Deprecated
    @JsonIgnore
    public AflMatchIncidentType getMatchIncidentType() {
        return incidentSubType;
    }


}
