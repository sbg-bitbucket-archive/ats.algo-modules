package ats.algo.sport.basketball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class BasketballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private static final double basketBallMaxMean = 300;
    private MatchParam pace;
    private MatchParam adv;
    private MatchParam adjRate;
    private MatchParam q1GD;
    private MatchParam q2GD;
    private MatchParam q3GD;
    private MatchParam aLoseBoost;
    private MatchParam bLoseBoost;
    // private MatchParam redCardPowerBoostRate;

    public BasketballMatchParams() {
        setDefaultParams();

    }

    @JsonCreator
    private BasketballMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setPace(Gaussian homeScoreRate) {
        this.pace.setGaussian(homeScoreRate);
    }

    void setQ1GD(Gaussian q1GD) {
        this.q1GD.setGaussian(q1GD);
    }

    void setQ2GD(Gaussian q2GD) {
        this.q2GD.setGaussian(q2GD);
    }

    void setQ3GD(Gaussian q3GD) {
        this.q3GD.setGaussian(q3GD);
    }

    void setAdv(Gaussian awayScoreRate) {
        this.adv.setGaussian(awayScoreRate);
    }

    void setAdjRate(Gaussian adjRate) {
        this.adjRate.setGaussian(adjRate);
    }

    void setPace(double skill, double stdDevn) {
        this.pace.updateGaussian(skill, stdDevn);
    }

    void setQ1GD(double skill, double stdDevn) {
        this.q1GD.updateGaussian(skill, stdDevn);
    }

    void setQ2GD(double skill, double stdDevn) {
        this.q2GD.updateGaussian(skill, stdDevn);
    }

    void setQ3GD(double skill, double stdDevn) {
        this.q3GD.updateGaussian(skill, stdDevn);
    }

    void setAdv(double skill, double stdDevn) {
        this.adv.updateGaussian(skill, stdDevn);
    }

    void setAdjRate(double skill, double stdDevn) {
        this.adjRate.updateGaussian(skill, stdDevn);
    }

    void setALoseBoost(Gaussian aLoseBoost) {
        this.aLoseBoost.setGaussian(aLoseBoost);
    }

    void setBLoseBoost(Gaussian bLoseBoost) {
        this.bLoseBoost.setGaussian(bLoseBoost);
    }

    //
    //
    public Gaussian getAdjRate() {
        return adjRate.getGaussian();
    }

    public Gaussian getPace() {
        return pace.getGaussian();
    }

    //
    public Gaussian getQ1GD() {
        return q1GD.getGaussian();
    }

    public Gaussian getQ2GD() {
        return q2GD.getGaussian();
    }

    public Gaussian getQ3GD() {
        return q3GD.getGaussian();
    }

    public Gaussian getAdv() {
        return adv.getGaussian();
    }

    public Gaussian getALoseBoost() {
        return aLoseBoost.getGaussian();
    }

    public Gaussian getBLoseBoost() {
        return bLoseBoost.getGaussian();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        pace = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 165, 20, 0, basketBallMaxMean * 2,
                        false);
        adv = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 10, 2, -basketBallMaxMean,
                        basketBallMaxMean, false);
        adjRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, 0.25, 0.15, -.99, 1.99, false); // change
                                                                                                                   // group
                                                                                                                   // to
                                                                                                                   // stop
                                                                                                                   // being
                                                                                                                   // used
                                                                                                                   // in
                                                                                                                   // param
                                                                                                                   // finding
        q1GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, 0.25, 0.05, 0, 0.6, false);
        q2GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, 0.25, 0.05, 0, 0.6, false);
        q3GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, 0.25, 0.05, 0, 0.6, false);
        aLoseBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.05, 0.05, 0.0, 0.25,
                        true);
        bLoseBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.05, 0.05, 0.0, 0.25,
                        true);


        updateParamMap();
    }

    static final double PACE_DEFAULT_VALUE = 165.0;
    static final double ADV_DEFAULT_VALUE = 10;
    static final double ADJ_RATE_DEFAULT_VALUE = 0.25;
    static final double Q1GD__DEFAULT_VALUE = 0.25;
    static final double Q2GD_DEFAULT_VALUE = 0.25;
    static final double Q3GD_DEFAULT_VALUE = 0.25;
    static final double A_LOSE_BOST_DEFAULT_VALUE = 0.05;
    static final double B_LOSE_BOST_DEFAULT_VALUE = 0.05;


    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("pace", pace);
        super.paramMap.put("adv", adv);
        super.paramMap.put("adjRate", adjRate);
        super.paramMap.put("q1GD", q1GD);
        super.paramMap.put("q2GD", q2GD);
        super.paramMap.put("q3GD", q3GD);
        super.paramMap.put("aLoseBoost", aLoseBoost);
        super.paramMap.put("bLoseBoost", bLoseBoost);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((adjRate == null) ? 0 : adjRate.hashCode());
        result = prime * result + ((adv == null) ? 0 : adv.hashCode());
        result = prime * result + ((pace == null) ? 0 : pace.hashCode());
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
        BasketballMatchParams other = (BasketballMatchParams) obj;
        if (adjRate == null) {
            if (other.adjRate != null)
                return false;
        } else if (!adjRate.equals(other.adjRate))
            return false;
        if (adv == null) {
            if (other.adv != null)
                return false;
        } else if (!adv.equals(other.adv))
            return false;
        if (pace == null) {
            if (other.pace != null)
                return false;
        } else if (!pace.equals(other.pace))
            return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        pace = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("pace"));
        adv = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, delegate.get("adv"));
        adjRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, delegate.get("adjRate"));
        q1GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, delegate.get("q1GD"));
        q2GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, delegate.get("q2GD"));
        q3GD = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.PENALTY, delegate.get("q3GD"));
        aLoseBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("aLoseBoost"));
        bLoseBoost = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("bLoseBoost"));

        updateParamMap();
    }

}
