package ats.algo.sport.outrights.calcengine.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ATSFixture {
    private String date;
    private String homeTeamId;
    private String awayTeamId;
    private long eventID;

    public ATSFixture(String date, String homeTeamId, String awayTeamId, long eventID) {
        super();
        this.date = date;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.eventID = eventID;
    }

    public String getDate() {
        return date;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public long getEventID() {
        return eventID;
    }

}
