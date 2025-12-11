package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class AmericanfootballMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private AmericanfootballMatchIncidentType incidentSubType;
    private int injuryTimeSecs;
    private FieldPositionType fieldPosition;
    private int downRemain;

    public enum AmericanfootballMatchIncidentType {
        TOUCH_DOWN,
        FIELD_GOAL,
        SAFETY,
        CONVERSION_SCORE1,
        CONVERSION_SCORE2,
        CONVERSION_MISS,
        CONVERSION_START,

        DOWNS_CONSUME,
        DOWNS_NUMBER_RESET,

        COMBO_SETTING,

        FIELD_POSITION_SETTING,
        RESET_FIELD_BALL_INFO
    }

    public enum FieldPositionType {
        A10YARD,
        A20YARD,
        A30YARD,
        A40YARD,
        M50YARD,
        B10YARD,
        B20YARD,
        B30YARD,
        B40YARD,
        UNKNOWN
        // A5MLINE_CENTRE, B5MLINE_CENTRE
    }

    public AmericanfootballMatchIncident() {
        super();
    }

    public AmericanfootballMatchIncident(AmericanfootballMatchIncidentType incidentSubType, int elapsedTime,
                    TeamId teamId, int injuryTimeSecs) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
        this.injuryTimeSecs = injuryTimeSecs;
        this.fieldPosition = FieldPositionType.UNKNOWN;
    }

    // score incident
    public AmericanfootballMatchIncident(AmericanfootballMatchIncidentType incidentSubType, int elapsedTime,
                    TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
        this.fieldPosition = FieldPositionType.UNKNOWN;
    }

    public AmericanfootballMatchIncident(AmericanfootballMatchIncidentType eventType, int elapsedTime,
                    FieldPositionType fieldPosition) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = eventType;
        this.fieldPosition = fieldPosition;
    }

    // conversion scored incident types
    public AmericanfootballMatchIncident(TeamId teamId, AmericanfootballMatchIncidentType incidentType,
                    int incidentElapsedTimeSecs) {
        super();
        setElapsedTime(incidentElapsedTimeSecs);
        this.incidentSubType = incidentType;
        this.teamId = teamId;
        this.fieldPosition = FieldPositionType.UNKNOWN;
    }



    public AmericanfootballMatchIncident(String teamInPosession, String fieldSide, int yardIndex, int downsRemain,
                    AmericanfootballMatchIncidentType rugbyMatchIncidentType, int incidentElapsedTimeSecs) {
        super();
        setElapsedTime(incidentElapsedTimeSecs);
        this.incidentSubType = rugbyMatchIncidentType;
        if (teamInPosession.toUpperCase().equals("A")) {
            this.teamId = TeamId.A;
        } else if (teamInPosession.toUpperCase().equals("B")) {
            this.teamId = TeamId.B;
        } else {
            throw new IllegalArgumentException("Invalid team in possesion");
        }

        if (fieldSide.toUpperCase().equals("A")) {
            if (yardIndex == 1)
                this.fieldPosition = FieldPositionType.A10YARD;
            if (yardIndex == 2)
                this.fieldPosition = FieldPositionType.A20YARD;
            if (yardIndex == 3)
                this.fieldPosition = FieldPositionType.A30YARD;
            if (yardIndex == 4)
                this.fieldPosition = FieldPositionType.A40YARD;
        } else if (fieldSide.toUpperCase().equals("B")) {
            if (yardIndex == 1)
                this.fieldPosition = FieldPositionType.B10YARD;
            if (yardIndex == 2)
                this.fieldPosition = FieldPositionType.B20YARD;
            if (yardIndex == 3)
                this.fieldPosition = FieldPositionType.B30YARD;
            if (yardIndex == 4)
                this.fieldPosition = FieldPositionType.B40YARD;
        } else {
            throw new IllegalArgumentException("Invalid team in possesion");
        }
        this.downRemain = downsRemain;
    }

    @Override
    public AmericanfootballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(AmericanfootballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



    // public int getConversionPointScored() {
    // return conversionPointScored;
    // }

    public FieldPositionType getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(FieldPositionType fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public void set(AmericanfootballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }

    public int getDownRemain() {
        return downRemain;
    }

    public void setDownRemain(int downRemain) {
        this.downRemain = downRemain;
    }



    public int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

}
