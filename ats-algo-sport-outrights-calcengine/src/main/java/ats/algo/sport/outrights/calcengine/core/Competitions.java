package ats.algo.sport.outrights.calcengine.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ats.algo.sport.outrights.server.api.CompetitionsList;

public class Competitions implements Iterable<Competition> {

    private Map<Long, Competition> competitions;

    public Competitions() {
        competitions = new HashMap<>();
    }

    public void add(Competition competition) {
        competitions.put(competition.getEventID(), competition);
    }

    public Collection<Competition> values() {
        return competitions.values();
    }

    public Competition get(long eventID) {
        return competitions.get(eventID);
    }

    public void changeEventId(Competition competition, long newEventId) {
        long oldEventId = competition.getEventID();
        competition.setEventID(newEventId);
        competitions.remove(oldEventId);
        competitions.put(newEventId, competition);
    }

    public void updateCompetition(Competition oldCompetition, Competition newCompetition) {
        long eventId = oldCompetition.getEventID();
        newCompetition.setEventID(eventId);
        competitions.put(eventId, newCompetition);

    }

    public Map<Long, Competition> getCompetitions() {
        return competitions;
    }

    public CompetitionsList generateCompetitionsList() {
        CompetitionsList list = new CompetitionsList();
        competitions.forEach((k, v) -> {
            list.addEntry(v.getEventID(), v.getName(), v.getAtsCompetitionID());
        });
        return list;
    }

    @Override
    public Iterator<Competition> iterator() {
        return competitions.values().iterator();
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
