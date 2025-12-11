package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionsList {

    private List<CompetitionsListEntry> competitionsList;

    public CompetitionsList() {
        competitionsList = new ArrayList<>();
    }



    public void addEntry(long eventId, String name, String atsCompetitionId) {
        competitionsList.add(new CompetitionsListEntry(eventId, name, atsCompetitionId));
    }

    public List<CompetitionsListEntry> getCompetitionsList() {
        return competitionsList;
    }

    public void setCompetitionsList(List<CompetitionsListEntry> competitionsList) {
        this.competitionsList = competitionsList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitionsList == null) ? 0 : competitionsList.hashCode());
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
        CompetitionsList other = (CompetitionsList) obj;
        if (competitionsList == null) {
            if (other.competitionsList != null)
                return false;
        } else if (!competitionsList.equals(other.competitionsList))
            return false;

        return true;
    }


}
