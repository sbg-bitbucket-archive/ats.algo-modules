package ats.algo.sport.basketball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BasketballMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private BasketballMatchIncidentType incidentSubType;
    private FieldPositionType fieldPositionType;

    public enum BasketballMatchIncidentType {
        POINT_SCORED, // only for game simulations
        FIELD_INFO_SETTING,

        TWO_POINTS_SCORED,
        THREE_POINTS_SCORED,

        ONE_FREETHROW_STARTED,
        TWO_FREETHROW_STARTED,
        THREE_FREETHROW_STARTED,

        FIRST_FREETHROW_MISSED,
        SECOND_FREETHROW_MISSED,
        THIRD_FREETHROW_MISSED,

        FIRST_FREETHROW_SCORED,
        SECOND_FREETHROW_SCORED,
        THIRD_FREETHROW_SCORED,
    }

    public enum FieldPositionType {
        HOME_THPLINE,
        AWAY_THPLINE,
        HOME_TWPLINE,
        AWAY_TWPLINE,
        HOME_THSLINE,
        AWAY_THSLINE,
        UNKNOWN
    }

    public BasketballMatchIncident() {
        super();
    }

    public BasketballMatchIncident(BasketballMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;

    }

    public BasketballMatchIncident(BasketballMatchIncidentType incidentSubType, int incidentElapsedTimeSecs,
                    TeamId teamId, FieldPositionType fieldPositionType2) {
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = incidentElapsedTimeSecs;
        this.teamId = teamId;
        this.fieldPositionType = fieldPositionType2;
    }

    @Override
    public BasketballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BasketballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void set(BasketballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }

    public FieldPositionType getFieldPositionType() {
        return fieldPositionType;
    }

    public void setFieldPositionType(FieldPositionType fieldPositionType) {
        this.fieldPositionType = fieldPositionType;
    }
}
