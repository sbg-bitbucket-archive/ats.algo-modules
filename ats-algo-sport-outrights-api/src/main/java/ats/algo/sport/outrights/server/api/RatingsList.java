package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class RatingsList {
    long eventID;
    private String competitionName;
    private List<RatingsListEntry> ratings;

    public RatingsList() {}

    public RatingsList(long eventID, String competitionName, Teams teams) {
        this.eventID = eventID;
        this.competitionName = competitionName;
        ratings = new ArrayList<>(teams.size());
        for (Team team : teams.values()) {
            ratings.add(new RatingsListEntry(team));
        }
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public List<RatingsListEntry> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingsListEntry> ratings) {
        this.ratings = ratings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitionName == null) ? 0 : competitionName.hashCode());
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RatingsList other = (RatingsList) obj;
        if (competitionName == null) {
            if (other.competitionName != null)
                return false;
        } else if (!competitionName.equals(other.competitionName))
            return false;
        if (eventID != other.eventID)
            return false;
        if (ratings == null) {
            if (other.ratings != null)
                return false;
        } else if (!ratings.equals(other.ratings))
            return false;
        return true;
    }


}
