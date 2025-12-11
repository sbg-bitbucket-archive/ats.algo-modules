package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FcastStandings {

    private Map<String, FcastStanding> standings;
    private List<FcastStanding> orderedStandings;

    public FcastStandings() {
        standings = new HashMap<>();
        orderedStandings = new ArrayList<FcastStanding>();
    }


    public int size() {
        return standings.size();
    }



    /**
     * returns an array of the standings ordered by their position, with top team at index 0
     * 
     * @return
     */
    public List<FcastStanding> finishOrder() {
        orderedStandings.sort((FcastStanding a, FcastStanding b) -> a.compareTo(b));
        return orderedStandings;
    }

    /**
     * returns the Fcaststandings ordered by their position, with top team at index 0
     * 
     * @return
     */
    public FcastStandings sortOrder() {
        orderedStandings.sort((FcastStanding a, FcastStanding b) -> a.compareTo(b));
        return this;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderedStandings == null) ? 0 : orderedStandings.hashCode());
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
        FcastStandings other = (FcastStandings) obj;
        if (orderedStandings == null) {
            if (other.orderedStandings != null)
                return false;
        } else if (!orderedStandings.equals(other.orderedStandings))
            return false;
        if (standings == null) {
            if (other.standings != null)
                return false;
        } else if (!standings.equals(other.standings))
            return false;
        return true;
    }


    public void put(String key, FcastStanding fcastStanding) {
        standings.put(key, fcastStanding);
        orderedStandings.add(fcastStanding);
    }


    public Map<String, FcastStanding> getStandings() {
        return standings;
    }


    public void setStandings(Map<String, FcastStanding> standings) {
        this.standings = standings;
    }


    public List<FcastStanding> getOrderedStandings() {
        return orderedStandings;
    }


    public void setOrderedStandings(List<FcastStanding> orderedStandings) {
        this.orderedStandings = orderedStandings;
    }


    public void updateTargetPoints(String teamID, double targetPoints) {
        FcastStanding fcastStanding = standings.get(teamID);
        fcastStanding.setTargetPoints(targetPoints);
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this.sortOrder());
    }


}
