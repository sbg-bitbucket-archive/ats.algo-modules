package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.core.util.json.JsonUtil;

public class Fixtures implements Iterable<Fixture> {

    private List<Fixture> fixtures;
    /*
     * Following structures are all to allow efficient access to fixtures for different purposes
     * 
     * fixturesTeamMap is map from homeTeamId, awayTeamId -> Fixture
     * 
     * fixturesIdsMap is fixtureId -> fixture
     * 
     * fixturesTypeMap is FixtureType -> List<Fixture>
     */
    private Map<String, Map<String, Fixture>> fixturesTeamsMap;
    private Map<String, Fixture> fixturesIdsMap;
    private Map<FixtureType, List<Fixture>> fixturesTypeMap;

    public Fixtures() {
        fixtures = new ArrayList<>();
        initialiseMaps();
    }

    private void initialiseMaps() {
        fixturesTeamsMap = new HashMap<>();
        fixturesIdsMap = new HashMap<>();
        fixturesTypeMap = new EnumMap<>(FixtureType.class);
        for (FixtureType t : FixtureType.values()) {
            List<Fixture> l = new ArrayList<>();
            fixturesTypeMap.put(t, l);
        }
    }

    public List<Fixture> list() {
        return fixtures;
    }

    public int size() {
        return fixtures.size();
    }

    public void add(Fixture fixture) {
        fixtures.add(fixture);
        addToMaps(fixture);
    }

    private void addToMaps(Fixture fixture) {
        String homeId = fixture.getHomeTeamID();
        Map<String, Fixture> m = fixturesTeamsMap.get(homeId);
        if (m == null) {
            m = new HashMap<String, Fixture>();
            fixturesTeamsMap.put(homeId, m);
        }
        m.put(fixture.getAwayTeamID(), fixture);
        fixturesIdsMap.put(fixture.getFixtureID(), fixture);
        fixturesTypeMap.get(fixture.getFixtureType()).add(fixture);
    }

    public Fixture get(int i) {
        return fixtures.get(i);
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
        initialiseMaps();
        fixtures.forEach(f -> addToMaps(f));

    }

    @JsonIgnore
    public Fixture getFixtureByTeamIds(String idHome, String idAway) {
        Map<String, Fixture> m = fixturesTeamsMap.get(idHome);
        if (m == null)
            return null;
        return m.get(idAway);
    }

    @Override
    public Iterator<Fixture> iterator() {
        return fixtures.iterator();
    }

    public Fixture getByEventID(long eventID) {
        for (Fixture fixture : fixtures)
            if (fixture.getEventID() == eventID)
                return fixture;
        return null;
    }

    /**
     * 
     * @param fixtureID
     * @return
     */
    public Fixture getByFixtureID(String fixtureID) {
        return fixturesIdsMap.get(fixtureID);
    }

    /**
     * 
     * @param fixtureType
     * @return
     */
    public List<Fixture> getFixturesForType(FixtureType fixtureType) {
        return fixturesTypeMap.get(fixtureType);
    }

    /**
     * find the loser over two legs
     * 
     * @param leg1
     * @param leg2
     * @return teamId of winningTeam, or null if not decided
     */
    public String winnerOverTwoLegs(String fixtureId) {
        Fixture leg2 = getByFixtureID(fixtureId);
        if (leg2.getStatus() == OutrightsFixtureStatus.COMPLETED) {
            Fixture leg1 = getByFixtureID(leg2.getFirstLegFixtureID());
            if (leg1.getStatus() == OutrightsFixtureStatus.COMPLETED) {
                int goalDiff = (leg1.getGoalsHome() + leg2.getGoalsAway())
                                - (leg1.getGoalsAway() + leg2.getGoalsHome());
                String teamId = null;
                if (goalDiff > 0)
                    teamId = leg1.getHomeTeamID();
                else if (goalDiff < 0)
                    teamId = leg1.getAwayTeamID();
                return teamId;
            }
        }
        return null;
    }

    public String loserOverTwoLegs(String fixtureId) {
        Fixture leg2 = getByFixtureID(fixtureId);
        if (leg2.getStatus() == OutrightsFixtureStatus.COMPLETED) {
            Fixture leg1 = getByFixtureID(leg2.getFirstLegFixtureID());
            if (leg1.getStatus() == OutrightsFixtureStatus.COMPLETED) {
                int goalDiff = (leg1.getGoalsHome() + leg2.getGoalsAway())
                                - (leg1.getGoalsAway() + leg2.getGoalsHome());
                String teamId = null;
                if (goalDiff > 0)
                    teamId = leg1.getAwayTeamID();
                else if (goalDiff < 0)
                    teamId = leg1.getHomeTeamID();
                return teamId;
            }
        }
        return null;
    }

    /**
     * updates the list of fixtures using data sourced from ATS
     * 
     * @param atsFixtures
     */
    public void updateEventIds(List<ATSFixture> atsFixtures) {
        for (ATSFixture atsFixture : atsFixtures) {
            Fixture fixture = this.getFixtureByTeamIds(atsFixture.getHomeTeamId(), atsFixture.getAwayTeamId());
            if (fixture != null) {
                /*
                 * found a match so update the fields in fixture from thos in atsFixture
                 */
                fixture.setDate(atsFixture.getDate());
                fixture.setEventID(atsFixture.getEventID());
            }
        }
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public String listToString() {
        StringBuilder b = new StringBuilder();
        fixtures.forEach(f -> b.append(f).append("\n"));
        return b.toString();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fixtures == null) ? 0 : fixtures.hashCode());
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
        Fixtures other = (Fixtures) obj;
        if (fixtures == null) {
            if (other.fixtures != null)
                return false;
        } else if (!fixtures.equals(other.fixtures))
            return false;
        return true;
    }

}
