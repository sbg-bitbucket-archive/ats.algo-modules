package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Standings {

    private Map<String, Standing> standings;
    private List<Standing> orderedStandings;

    public Standings() {
        standings = new HashMap<>();
        orderedStandings = new ArrayList<Standing>();
    }

    public int size() {
        return standings.size();
    }

    public Set<Entry<String, Standing>> entrySet() {
        return standings.entrySet();
    }

    public void put(String key, Standing standing) {
        standings.put(key, standing);
        orderedStandings.add(standing);
    }

    public Standing get(String key) {
        return standings.get(key);
    }

    public Collection<Standing> values() {
        return standings.values();
    }

    public Map<String, Standing> getStandings() {
        return standings;
    }

    public void setStandings(Map<String, Standing> standings) {
        this.standings = standings;
        orderedStandings.clear();
        for (Standing standing : standings.values())
            orderedStandings.add(standing);

    }

    public Standings copy() {
        Standings cc = new Standings();
        for (Entry<String, Standing> e : standings.entrySet()) {
            cc.put(e.getKey(), e.getValue().copy());
        }
        return cc;
    }

    /**
     * returns the id of the team at the top of the current set of standings
     * 
     * @return
     */
    public String topTeamID() {
        Standing highestStanding = null;
        for (Standing standing : standings.values()) {
            if (highestStanding == null)
                highestStanding = standing;
            else {
                if (standing.compareTo(highestStanding) < 0)
                    highestStanding = standing;
            }
        }
        if (highestStanding != null)
            return highestStanding.getTeamId();
        else
            return null;
    }

    /**
     * returns an array of the standings ordered by their position, with top team at index 0
     * 
     * @return
     */
    public List<Standing> finishOrder() {
        orderedStandings.sort((Standing a, Standing b) -> a.compareTo(b));
        return orderedStandings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((standings == null) ? 0 : standings.hashCode());
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
        Standings other = (Standings) obj;
        if (standings == null) {
            if (other.standings != null)
                return false;
        } else if (!standings.equals(other.standings))
            return false;
        return true;
    }

}
