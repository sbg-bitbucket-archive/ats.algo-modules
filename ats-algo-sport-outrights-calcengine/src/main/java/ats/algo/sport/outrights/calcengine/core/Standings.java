package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.core.util.json.JsonUtil;

import java.util.Set;
import java.util.function.Predicate;

public class Standings {

    private Map<String, Standing> standings;
    private List<Standing> orderedStandings;

    public Standings() {
        standings = new HashMap<>();
        orderedStandings = new ArrayList<Standing>();
    }

    public static Standings generateStandingsFromFixtures(Teams teams, Fixtures fixtures) {
        return generateStandingsFromFixtures(teams, fixtures, f -> true);
    }

    public static Standings generateStandingsFromFixtures(Teams teams, Fixtures fixtures, Predicate<Fixture> include) {
        Standings standings = new Standings();
        teams.getTeams().forEach((k, v) -> {
            Standing standing = new Standing(v.getTeamID(), 0, 0, 0, 0, 0, v.getManualPointsAdjustment());
            standing.setManualTieBreakAdjustment(v.getManualTieBreakAdjustment());
            standings.put(v.getTeamID(), standing);
        });

        fixtures.getFixtures().forEach((fixture) -> {
            if (fixture.getFixtureType() == FixtureType.LEAGUE
                            && fixture.getStatus() == OutrightsFixtureStatus.COMPLETED && include.test(fixture)) {
                Standing standingA = standings.get(fixture.getHomeTeamID());
                Standing standingB = standings.get(fixture.getAwayTeamID());
                standingA.incrementMatchesPlayed();
                standingB.incrementMatchesPlayed();
                int goalsH = fixture.getGoalsHome();
                int goalsA = fixture.getGoalsAway();
                int scoreDiff = goalsH - goalsA;
                if (scoreDiff > 0) {
                    standingA.incrementPoints(3);
                    standingA.incrementMatchesWon();
                } else if (scoreDiff < 0) {
                    standingB.incrementPoints(3);
                    standingB.incrementMatchesWon();
                } else {
                    standingA.incrementPoints(1);
                    standingB.incrementPoints(1);
                    standingA.incrementMatchesDrawn();
                    standingB.incrementMatchesDrawn();
                }
                standingA.incrementGoalsFor(goalsH);
                standingB.incrementGoalsFor(goalsA);
                standingA.incrementGoalsAgainst(goalsA);
                standingB.incrementGoalsAgainst(goalsH);
            }
        });
        return standings;
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

    public Standings sortOrder() {
        orderedStandings.sort((Standing a, Standing b) -> a.compareTo(b));
        return this;
    }

    public List<Standing> getOrderedStandings() {
        return orderedStandings;
    }

    public void setOrderedStandings(List<Standing> orderedStandings) {
        this.orderedStandings = orderedStandings;
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

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
