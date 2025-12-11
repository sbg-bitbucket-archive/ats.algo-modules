package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingsListEntry {

    private Standing standing;
    private int highestPossibleFinishPosn;
    private int lowestPossibleFinishPosn;
    private int manualPointsAdj;
    private int manualtieBreakAdj;



    public StandingsListEntry() {}


    public StandingsListEntry(Standing standing, int highestPossibleFinishPosn, int lowestPossibleFinishPosn,
                    int manualPointsAdj, int manualtieBreakAdj) {
        super();
        this.standing = standing;
        this.highestPossibleFinishPosn = highestPossibleFinishPosn;
        this.lowestPossibleFinishPosn = lowestPossibleFinishPosn;
        this.manualPointsAdj = manualPointsAdj;
        this.manualtieBreakAdj = manualtieBreakAdj;
    }


    public Standing getStanding() {
        return standing;
    }


    public void setStanding(Standing standing) {
        this.standing = standing;
    }


    public int getHighestPossibleFinishPosn() {
        return highestPossibleFinishPosn;
    }


    public void setHighestPossibleFinishPosn(int highestPossibleFinishPosn) {
        this.highestPossibleFinishPosn = highestPossibleFinishPosn;
    }


    public int getLowestPossibleFinishPosn() {
        return lowestPossibleFinishPosn;
    }


    public void setLowestPossibleFinishPosn(int lowestPossibleFinishPosn) {
        this.lowestPossibleFinishPosn = lowestPossibleFinishPosn;
    }


    public int getManualPointsAdj() {
        return manualPointsAdj;
    }


    public void setManualPointsAdj(int manualPointsAdj) {
        this.manualPointsAdj = manualPointsAdj;
    }


    public int getManualtieBreakAdj() {
        return manualtieBreakAdj;
    }


    public void setManualtieBreakAdj(int manualtieBreakAdj) {
        this.manualtieBreakAdj = manualtieBreakAdj;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + highestPossibleFinishPosn;
        result = prime * result + lowestPossibleFinishPosn;
        result = prime * result + manualPointsAdj;
        result = prime * result + manualtieBreakAdj;
        result = prime * result + ((standing == null) ? 0 : standing.hashCode());
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
        StandingsListEntry other = (StandingsListEntry) obj;
        if (highestPossibleFinishPosn != other.highestPossibleFinishPosn)
            return false;
        if (lowestPossibleFinishPosn != other.lowestPossibleFinishPosn)
            return false;
        if (manualPointsAdj != other.manualPointsAdj)
            return false;
        if (manualtieBreakAdj != other.manualtieBreakAdj)
            return false;
        if (standing == null) {
            if (other.standing != null)
                return false;
        } else if (!standing.equals(other.standing))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
