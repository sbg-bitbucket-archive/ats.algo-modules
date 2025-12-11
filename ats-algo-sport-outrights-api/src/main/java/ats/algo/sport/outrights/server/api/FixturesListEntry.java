package ats.algo.sport.outrights.server.api;

import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.core.util.json.JsonUtil;

public class FixturesListEntry {

    private Fixture fixture;
    private double pHome;
    private double pAway;
    private double pDraw;


    public FixturesListEntry() {}

    public FixturesListEntry(Fixture fixture, double pHome, double pAway, double pDraw) {
        this.fixture = fixture;
        pHome = GCMath.round(pHome, 2);
        pAway = GCMath.round(pAway, 2);
        pDraw = GCMath.round(pDraw, 2);
    }

    public Fixture getFixture() {
        return fixture;
    }

    public double getpHome() {
        return pHome;
    }

    public double getpAway() {
        return pAway;
    }

    public double getpDraw() {
        return pDraw;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public void setpHome(double pHome) {
        this.pHome = pHome;
    }

    public void setpAway(double pAway) {
        this.pAway = pAway;
    }

    public void setpDraw(double pDraw) {
        this.pDraw = pDraw;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fixture == null) ? 0 : fixture.hashCode());
        long temp;
        temp = Double.doubleToLongBits(pAway);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pDraw);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pHome);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        FixturesListEntry other = (FixturesListEntry) obj;
        if (fixture == null) {
            if (other.fixture != null)
                return false;
        } else if (!fixture.equals(other.fixture))
            return false;
        if (Double.doubleToLongBits(pAway) != Double.doubleToLongBits(other.pAway))
            return false;
        if (Double.doubleToLongBits(pDraw) != Double.doubleToLongBits(other.pDraw))
            return false;
        if (Double.doubleToLongBits(pHome) != Double.doubleToLongBits(other.pHome))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
