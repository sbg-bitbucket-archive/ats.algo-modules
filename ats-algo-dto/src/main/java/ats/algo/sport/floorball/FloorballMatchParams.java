package ats.algo.sport.floorball;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class FloorballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    // private MatchParam homeScoreRate;
    // private MatchParam awayScoreRate;
    private MatchParam goalToal;
    private MatchParam goalSupremacy;
    private MatchParam homeLoseBoost;
    private MatchParam awayLoseBoost;
    private MatchParam powerBoostRate;
    @JsonIgnore
    private double fbMaximumTeamPoints = 20;
    // private MatchParam redCardPowerBoostRate;

    public FloorballMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private FloorballMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setPowerBoostRate(Gaussian powerBoostRate) {
        this.powerBoostRate.setGaussian(powerBoostRate);
    }

    void setHomeLoseBoost(Gaussian homeLoseBoost) {
        this.homeLoseBoost.setGaussian(homeLoseBoost);
    }

    void setAwayLoseBoost(Gaussian awayLoseBoost) {
        this.awayLoseBoost.setGaussian(awayLoseBoost);
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

    Gaussian getHomeLoseBoost() {
        return homeLoseBoost.getGaussian();
    }

    Gaussian getAwayLoseBoost() {
        return awayLoseBoost.getGaussian();
    }

    Gaussian getGoalToal() {
        return goalToal.getGaussian();
    }

    void setGoalToal(Gaussian skill) {
        this.goalToal.setGaussian(skill);
    }

    void setGoalToal(double skill, double stdDevn) {
        this.goalToal.updateGaussian(skill, stdDevn);
    }

    Gaussian getGoalSupremacy() {
        return goalSupremacy.getGaussian();
    }

    void setGoalSupremacy(Gaussian skill) {
        this.goalSupremacy.setGaussian(skill);
    }

    void setGoalSupremacy(double skill, double stdDevn) {
        this.goalSupremacy.updateGaussian(skill, stdDevn);
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        goalToal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 4.75 + 4.25, 0.05, 0,
                        fbMaximumTeamPoints * 2, false);
        goalSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 4.75 - 4.25, 0.05,
                        -fbMaximumTeamPoints, fbMaximumTeamPoints, false);
        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.15, 0.005, 0, 2,
                        false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("goalToal", goalToal);
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
        result = prime * result + ((goalToal == null) ? 0 : goalToal.hashCode());
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
        FloorballMatchParams other = (FloorballMatchParams) obj;
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
        if (goalToal == null) {
            if (other.goalToal != null)
                return false;
        } else if (!goalToal.equals(other.goalToal))
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

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        goalToal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("goalToal"));
        goalSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("goalSupremacy"));
        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("homeLoseBoost"));
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("awayLoseBoost"));
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("powerBoostRate"));
        updateParamMap();
    }

}
