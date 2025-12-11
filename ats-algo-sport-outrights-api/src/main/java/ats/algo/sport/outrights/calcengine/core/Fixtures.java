package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ats.core.util.json.JsonUtil;

public class Fixtures implements Iterable<Fixture> {

    private List<Fixture> fixtures;

    public Fixtures() {
        fixtures = new ArrayList<>();
    }

    public List<Fixture> list() {
        return fixtures;
    }

    public int size() {
        return fixtures.size();
    }

    public void add(Fixture fixture) {
        fixtures.add(fixture);

    }

    public Fixture get(int i) {
        return fixtures.get(i);
    }



    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    @Override
    public Iterator<Fixture> iterator() {
        return fixtures.iterator();
    }

    public Fixture getByEventID(long eventID) {
        // TODO - improve efficiency of this method
        for (Fixture fixture : fixtures)
            if (fixture.getEventID() == eventID)
                return fixture;
        return null;
    }

    /**
     * not very efficient - only for testing
     * 
     * @param fixtureID
     * @return
     */
    public Fixture getByFixtureID(String fixtureID) {
        for (Fixture fixture : fixtures)
            if (fixture.getFixtureID().equals(fixtureID))
                return fixture;
        return null;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
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
