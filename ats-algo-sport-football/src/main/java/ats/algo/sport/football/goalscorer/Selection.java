package ats.algo.sport.football.goalscorer;

import ats.algo.core.common.TeamId;

public class Selection {

    TeamId teamId;
    double prob;

    Selection(TeamId teamId) {
        this.teamId = teamId;
        this.prob = 0;
    }



    public String toString() {
        String s;
        s = String.format("%s. prob: %.4f", teamId.toString(), prob);
        return s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(prob);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        Selection other = (Selection) obj;
        if (Double.doubleToLongBits(prob) != Double.doubleToLongBits(other.prob))
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }



    Selection copy() {
        Selection cc = new Selection(this.teamId);
        cc.prob = this.prob;
        return cc;
    }
}
