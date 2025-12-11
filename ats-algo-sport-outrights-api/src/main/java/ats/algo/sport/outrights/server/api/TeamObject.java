package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.sport.outrights.calcengine.core.Team;

/**
 * wraps the Team class with additional info needed to post an update
 * 
 * @author gicha
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamObject {

    private long eventId;
    private String competitionName;
    private Team team;

    public TeamObject() {

    }

    public TeamObject(long eventId, String competitionName, Team team) {
        super();
        this.eventId = eventId;
        this.competitionName = competitionName;
        this.team = team;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
