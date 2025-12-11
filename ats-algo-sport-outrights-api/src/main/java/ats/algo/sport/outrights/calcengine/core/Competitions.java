package ats.algo.sport.outrights.calcengine.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Competitions {

    private Map<Long, Competition> competitions;

    public Competitions() {
        competitions = new HashMap<>();
    }

    public void put(long l, Competition competition) {
        competitions.put(l, competition);
    }

    public Collection<Competition> values() {
        return competitions.values();
    }


    public void setCompetitions(Map<Long, Competition> competitions) {
        this.competitions = competitions;
    }

    public Competition get(long eventID) {
        return competitions.get(eventID);
    }

    public Map<Long, Competition> getCompetitions() {
        return competitions;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitions == null) ? 0 : competitions.hashCode());
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
        Competitions other = (Competitions) obj;
        if (competitions == null) {
            if (other.competitions != null)
                return false;
        } else if (!competitions.equals(other.competitions))
            return false;
        return true;
    }


}
