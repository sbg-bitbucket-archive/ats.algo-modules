package ats.algo.sport.handball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class HandballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam totalScoreRate;
    private MatchParam supremacyScoreRate;
    private double hbMaximumPoints = 200;
    private MatchParam powerBoostRate;
    // private MatchParam redCardPowerBoostRate;

    public HandballMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private HandballMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    @JsonIgnore
    public void setPowerBoostRate(Gaussian powerBoostRate) {
        this.powerBoostRate.setGaussian(powerBoostRate);
    }

    @JsonIgnore
    public void setTotalScoreRate(Gaussian totalScoreRate) {
        this.totalScoreRate.setGaussian(totalScoreRate);

    }

    @JsonIgnore
    public void setSupremacyScoreRate(Gaussian supremacyScoreRate) {
        this.supremacyScoreRate.setGaussian(supremacyScoreRate);
    }

    @JsonIgnore
    public void setTotalScoreRate(double skill, double stdDevn) {
        this.totalScoreRate.updateGaussian(skill, stdDevn);
    }

    @JsonIgnore
    public void setSupremacyScoreRate(double skill, double stdDevn) {
        this.supremacyScoreRate.updateGaussian(skill, stdDevn);
    }

    @JsonIgnore
    public Gaussian getPowerBoostRate() { // AsGaussian
        return powerBoostRate.getGaussian();
    }

    @JsonIgnore
    public Gaussian getTotalScoreRate() {
        return totalScoreRate.getGaussian();
    }

    @JsonIgnore
    public Gaussian getSupremacyScoreRate() {
        return supremacyScoreRate.getGaussian();
    }


    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        totalScoreRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 50, 5, 0,
                        hbMaximumPoints * 2, false);
        supremacyScoreRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 5, 0.5,
                        -hbMaximumPoints, hbMaximumPoints, false);
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 1, 0.05, 0, 2, false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("totalScoreRate", totalScoreRate);
        super.paramMap.put("supremacyScoreRate", supremacyScoreRate);
        super.paramMap.put("powerBoostRate", powerBoostRate);
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        HandballMatchParams other = (HandballMatchParams) obj;
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

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        totalScoreRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("totalScoreRate"));
        supremacyScoreRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("supremacyScoreRate"));
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("powerBoostRate"));
        updateParamMap();
    }

}
