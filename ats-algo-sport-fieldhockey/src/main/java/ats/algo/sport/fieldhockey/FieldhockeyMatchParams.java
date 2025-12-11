package ats.algo.sport.fieldhockey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class FieldhockeyMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam totalScoreRate;
    private MatchParam supremacyScoreRate;
    private MatchParam homeLoseBoost;
    private MatchParam awayLoseBoost;
    private MatchParam powerBoostRate;
    // private MatchParam redCardPowerBoostRate;
    @JsonIgnore
    private double fhMaximumPoints = 20;

    public FieldhockeyMatchParams() {
        super();
        setDefaultParams();
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setPowerBoostRate(Gaussian powerBoostRate) {
        this.powerBoostRate.setGaussian(powerBoostRate);
    }

    void setTotalScoreRate(Gaussian homeScoreRate) {
        this.totalScoreRate.setGaussian(homeScoreRate);

    }

    void setSupremacyScoreRate(Gaussian awayScoreRate) {
        this.supremacyScoreRate.setGaussian(awayScoreRate);
    }

    void setHomeLoseBoost(Gaussian homeLoseBoost) {
        this.homeLoseBoost.setGaussian(homeLoseBoost);
    }

    void setAwayLoseBoost(Gaussian awayLoseBoost) {
        this.awayLoseBoost.setGaussian(awayLoseBoost);
    }

    public void setTotalScoreRate(double skill, double stdDevn) {
        this.totalScoreRate.updateGaussian(skill, stdDevn);
    }

    void setSupremacyScoreRate(double skill, double stdDevn) {
        this.supremacyScoreRate.updateGaussian(skill, stdDevn);
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

    Gaussian getTotalScoreRate() {
        return totalScoreRate.getGaussian();
    }

    Gaussian getSupremacyScoreRate() {
        return supremacyScoreRate.getGaussian();
    }

    // MatchParam getHomeLoseBoost() {
    // return homeLoseBoost;
    // }

    Gaussian getHomeLoseBoost() {
        return homeLoseBoost.getGaussian();
    }

    // MatchParam getAwayLoseBoost() {
    // return awayLoseBoost;
    // }

    Gaussian getAwayLoseBoost() {
        return awayLoseBoost.getGaussian();
    }

    @JsonIgnore
    private static final String totalScoreRateKey = "totalScoreRate";
    @JsonIgnore
    private static final String supremacyScoreRateKey = "supremacyScoreRate";
    @JsonIgnore
    private static final String homeLoseBoostKey = "homeLoseBoost";
    @JsonIgnore
    private static final String awayLoseBoostKey = "awayLoseBoost";
    @JsonIgnore
    private static final String powerBoostRateKey = "powerBoost";

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        totalScoreRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 4.5, 0.5, 0,
                        fhMaximumPoints * 2, false);
        supremacyScoreRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 0.5, 0.5,
                        -fhMaximumPoints, fhMaximumPoints, false);
        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.15, 0.005, 0, 2,
                        false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("totalScoreRate", totalScoreRate);
        super.paramMap.put("supremacyScoreRate", supremacyScoreRate);
        super.paramMap.put("homeLoseBoost", homeLoseBoost);
        super.paramMap.put("awayLoseBoost", awayLoseBoost);
        super.paramMap.put("powerBoostRate", powerBoostRate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((awayLoseBoost == null) ? 0 : awayLoseBoost.hashCode());
        result = prime * result + ((homeLoseBoost == null) ? 0 : homeLoseBoost.hashCode());
        result = prime * result + ((powerBoostRate == null) ? 0 : powerBoostRate.hashCode());
        result = prime * result + ((supremacyScoreRate == null) ? 0 : supremacyScoreRate.hashCode());
        result = prime * result + ((totalScoreRate == null) ? 0 : totalScoreRate.hashCode());
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
        FieldhockeyMatchParams other = (FieldhockeyMatchParams) obj;
        if (awayLoseBoost == null) {
            if (other.awayLoseBoost != null)
                return false;
        } else if (!awayLoseBoost.equals(other.awayLoseBoost))
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
        if (supremacyScoreRate == null) {
            if (other.supremacyScoreRate != null)
                return false;
        } else if (!supremacyScoreRate.equals(other.supremacyScoreRate))
            return false;
        if (totalScoreRate == null) {
            if (other.totalScoreRate != null)
                return false;
        } else if (!totalScoreRate.equals(other.totalScoreRate))
            return false;
        return true;
    }

}
