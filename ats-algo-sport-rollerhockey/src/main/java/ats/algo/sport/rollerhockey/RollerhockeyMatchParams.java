package ats.algo.sport.rollerhockey;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class RollerhockeyMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam goalTotal;
    private MatchParam goalSupremacy;
    private MatchParam homeLoseBoost;
    private MatchParam awayLoseBoost;
    private MatchParam powerBoostRate;
    // private MatchParam redCardPowerBoostRate;

    public RollerhockeyMatchParams() {
        super();
        setDefaultParams();
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setPowerBoostRate(Gaussian powerBoostRate) {
        this.powerBoostRate.setGaussian(powerBoostRate);
    }

    void setGoalTotal(Gaussian homeScoreRate) {
        this.goalTotal.setGaussian(homeScoreRate);

    }

    void setGoalSupremacy(Gaussian awayScoreRate) {
        this.goalSupremacy.setGaussian(awayScoreRate);
    }

    void setHomeLoseBoost(Gaussian homeLoseBoost) {
        this.homeLoseBoost.setGaussian(homeLoseBoost);
    }

    void setAwayLoseBoost(Gaussian awayLoseBoost) {
        this.awayLoseBoost.setGaussian(awayLoseBoost);
    }

    void setGoalTotal(double skill, double stdDevn) {
        this.goalTotal.updateGaussian(skill, stdDevn);
    }

    void setGoalSupremacy(double skill, double stdDevn) {
        this.goalSupremacy.updateGaussian(skill, stdDevn);
    }

    void setHomeLoseBoost(double skill, double stdDevn) {
        this.homeLoseBoost.updateGaussian(skill, stdDevn);
    }

    void setAwayLoseBoost(double skill, double stdDevn) {
        this.awayLoseBoost.updateGaussian(skill, stdDevn);
    }

    Gaussian getPowerBoostRate() { // AsGaussian
        return powerBoostRate.getGaussian();
    }

    Gaussian getGoalTotal() {
        return goalTotal.getGaussian();
    }

    Gaussian getGoalSupremacy() {
        return goalSupremacy.getGaussian();
    }

    Gaussian getHomeLoseBoost() {
        return homeLoseBoost.getGaussian();
    }

    Gaussian getAwayLoseBoost() {
        return awayLoseBoost.getGaussian();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        goalTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 5, 0.05, 0, 40, false);
        goalSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 4, 0.05, -20, 20,
                        false);
        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.15, 0.005, 0, 2,
                        false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("goalTotal", goalTotal);
        super.paramMap.put("goalSupremacy", goalSupremacy);
        super.paramMap.put("homeLoseBoost", homeLoseBoost);
        super.paramMap.put("awayLoseBoost", awayLoseBoost);
        super.paramMap.put("powerBoostRate", powerBoostRate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((awayLoseBoost == null) ? 0 : awayLoseBoost.hashCode());
        result = prime * result + ((goalSupremacy == null) ? 0 : goalSupremacy.hashCode());
        result = prime * result + ((goalTotal == null) ? 0 : goalTotal.hashCode());
        result = prime * result + ((homeLoseBoost == null) ? 0 : homeLoseBoost.hashCode());
        result = prime * result + ((powerBoostRate == null) ? 0 : powerBoostRate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RollerhockeyMatchParams other = (RollerhockeyMatchParams) obj;
        if (awayLoseBoost == null) {
            if (other.awayLoseBoost != null)
                return false;
        } else if (!awayLoseBoost.equals(other.awayLoseBoost))
            return false;
        if (goalSupremacy == null) {
            if (other.goalSupremacy != null)
                return false;
        } else if (!goalSupremacy.equals(other.goalSupremacy))
            return false;
        if (goalTotal == null) {
            if (other.goalTotal != null)
                return false;
        } else if (!goalTotal.equals(other.goalTotal))
            return false;
        if (homeLoseBoost == null) {
            if (other.homeLoseBoost != null)
                return false;
        } else if (!homeLoseBoost.equals(other.homeLoseBoost))
            return false;
        if (powerBoostRate == null) {
            if (other.powerBoostRate != null)
                return false;
        } else if (!powerBoostRate.equals(other.powerBoostRate))
            return false;
        return true;
    }

}
