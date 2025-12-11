package ats.algo.sport.afl;

import java.io.Serializable;

import ats.algo.core.common.TeamId;

public class GoalInfo implements Cloneable, Serializable, Comparable<GoalInfo> {
    private static final long serialVersionUID = 1L;
    private int secs;
    private TeamId team;
    private int method; // 0 is a goal, 1 is a behind
    private int periodNoG;

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getSecs() {
        return secs;
    }

    public void setSecs(int mins) {
        this.secs = mins;
    }

    public TeamId getTeam() {
        return team;
    }

    public void setTeam(TeamId team) {
        this.team = team;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getPeriodNoG() {
        return periodNoG;
    }

    public void setPeriodNoG(int periodNo) {
        this.periodNoG = periodNo;
    }

    @Override
    public int compareTo(GoalInfo arg0) {
        return this.secs - arg0.getSecs();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + method;
        result = prime * result + secs;
        result = prime * result + ((team == null) ? 0 : team.hashCode());
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
        GoalInfo other = (GoalInfo) obj;
        if (method != other.method)
            return false;
        if (secs != other.secs)
            return false;
        if (team != other.team)
            return false;
        return true;
    }

}
