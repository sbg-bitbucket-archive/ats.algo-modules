package ats.algo.sport.football;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;

    public enum FootballMatchIncidentType {
        GOAL,
        POSSIBLE_GOAL,
        GOAL_NOT_CONFIRMED,
        CORNER,
        POSSIBLE_CORNER,
        CORNER_NOT_CONFIRMED,
        DANGEROUS_ATTACK,
        YELLOW_CARD,
        POSSIBLE_YELLOW_CARD,
        YELLOW_CARD_NOT_CONFIRMED,
        RED_CARD,
        POSSIBLE_RED_CARD,
        RED_CARD_NOT_CONFIRMED,
        POSSIBLE_SUB,
        SUB_NOT_CONFIRMED,
        SET_INJURY_TIME,
        SHOOTOUT_START,
        SHOOTOUT_MISS,
        PENALTY,
        PENALTY_MISSED,
        POSSIBLE_PENALTY,
        PENALTY_NOT_CONFIRMED,
        PENALTY_CONFIRMED,
        SHOT_ON_TARGET,
        SHOT_OFF_TARGET,
        FREE_KICK,
        GOAL_KICK,
        THROWIN,
        ATTACK,
        SAFE
    }

    private FootballMatchIncidentType incidentSubType;
    private int injuryTimeSecs;

    public FootballMatchIncident() {
        super();
    }

    /**
     * Constructor for all incident types except setting injury time
     * 
     * @param incidentType
     * @param elapsedTime
     */
    public FootballMatchIncident(FootballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        super();
        this.elapsedTimeSecs = elapsedTime;
        this.incidentSubType = incidentType;
        this.teamId = teamId;
    }

    /**
     * Constructor for the SET_INJURY_TIME incident type
     * 
     * @param incidentType
     * @param elapsedTime
     * @param injuryTimeSecs
     */
    public FootballMatchIncident(FootballMatchIncidentType incidentType, int elapsedTime, int injuryTimeSecs) {
        super();
        this.elapsedTimeSecs = elapsedTime;
        this.incidentSubType = incidentType;
        this.injuryTimeSecs = injuryTimeSecs;
    }

    @Override
    public FootballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }


    public void setIncidentSubType(FootballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    int getInjuryTimeSecs() {
        return injuryTimeSecs;
    }

    void set(FootballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;

    }

    void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

    @JsonIgnore
    public FootballMatchStateFromFeed getFootballMatchStateFromFeed() {
        return (FootballMatchStateFromFeed) super.getMatchStateFromFeed();
    }

    @JsonIgnore
    public void setFootballMatchStateFromFeed(FootballMatchStateFromFeed footballMatchStateFromFeed) {
        super.setMatchStateFromFeed(footballMatchStateFromFeed);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + injuryTimeSecs;
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
        FootballMatchIncident other = (FootballMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (injuryTimeSecs != other.injuryTimeSecs)
            return false;
        return true;
    }

}
