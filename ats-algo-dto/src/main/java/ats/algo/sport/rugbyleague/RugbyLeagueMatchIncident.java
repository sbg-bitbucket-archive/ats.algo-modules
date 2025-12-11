package ats.algo.sport.rugbyleague;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class RugbyLeagueMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private RugbyLeagueMatchIncidentType incidentSubType;
    private int injuryTimeSecs;
    private FieldPositionType fieldPosition;

    public enum RugbyLeagueMatchIncidentType {
        TRY,
        DROP_GOAL,
        CONVERSION_GOAL,
        CONVERSION_MISS,
        CONVERSION_START,
        PENALTY_START,
        PENALTY_GOAL,
        PENALTY_MISS,
        SET_INJURY_TIME,
        BALL_POSITION_SETTING,
        FIELD_POSITION_SETTING,
        RESET_FIELD_BALL_INFO,
        TEN_MINS_PENALTY_START,
        TEN_MINS_PENALTY_END,
        RED_CARD
    }

    enum FieldPositionType {
        A10MLINE_CENTRE,
        A22MLINE_CENTRE,
        A10MLINE_SIDE,
        A22MLINE_SIDE,
        HALFWAYLINE,
        B10MLINE_CENTRE,
        B22MLINE_CENTRE,
        B10MLINE_SIDE,
        B22MLINE_SIDE,
        UNKNOWN
        // A5MLINE_CENTRE, B5MLINE_CENTRE
    }

    public RugbyLeagueMatchIncident() {
        super();
    }

    public RugbyLeagueMatchIncident(RugbyLeagueMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId,
                    int injuryTimeSecs) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
        this.injuryTimeSecs = injuryTimeSecs;
        this.fieldPosition = FieldPositionType.UNKNOWN;
    }

    public RugbyLeagueMatchIncident(RugbyLeagueMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
        this.fieldPosition = FieldPositionType.UNKNOWN;
    }

    public RugbyLeagueMatchIncident(RugbyLeagueMatchIncidentType incidentSubType, int elapsedTime,
                    FieldPositionType fieldPosition) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.fieldPosition = fieldPosition;
    }

    @Override
    public RugbyLeagueMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(RugbyLeagueMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public FieldPositionType getFieldPosition() {
        return fieldPosition;
    }

    public void set(RugbyLeagueMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }

    public int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }


}
