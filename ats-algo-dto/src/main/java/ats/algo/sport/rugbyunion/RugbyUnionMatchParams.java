package ats.algo.sport.rugbyunion;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class RugbyUnionMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam scoreTotal;
    private MatchParam scoreSupremacy;
    private MatchParam tryTotal;
    private MatchParam trySupremacy;
    private MatchParam homeLoseBoost;
    private MatchParam awayLoseBoost;
    private MatchParam powerBoostRate;
    @JsonIgnore
    private double rugbyMaximumPoints = 100;
    @JsonIgnore
    private double rugbyMaximumTrys = 20;
    // private MatchParam redCardPowerBoostRate;

    public RugbyUnionMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private RugbyUnionMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);

    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setPowerBoostRate(Gaussian powerBoostRate) {
        this.powerBoostRate.setGaussian(powerBoostRate);
    }

    void setPowerBoostRate(double skill, double stdDevn) {
        this.powerBoostRate.updateGaussian(skill, stdDevn);
    }

    Gaussian getPowerBoostRate() { // AsGaussian
        return powerBoostRate.getGaussian();
    }

    void setScoreTotal(Gaussian scoretotal) {
        this.scoreTotal.setGaussian(scoretotal);
    }

    void setScoreTotal(double skill, double stdDevn) {
        this.scoreTotal.updateGaussian(skill, stdDevn);
    }

    Gaussian getScoreTotal() {
        return scoreTotal.getGaussian();
    }

    void setScoreSupremacy(Gaussian scoreSupremacy) {
        this.scoreSupremacy.setGaussian(scoreSupremacy);
    }

    void setScoreSupremacy(double skill, double stdDevn) {
        this.scoreSupremacy.updateGaussian(skill, stdDevn);
    }

    Gaussian getScoreSupremacy() {
        return scoreSupremacy.getGaussian();
    }

    void setTryTotal(Gaussian trytotal) {
        this.tryTotal.setGaussian(trytotal);
    }

    void setTryTotal(double skill, double stdDevn) {
        this.tryTotal.updateGaussian(skill, stdDevn);
    }

    Gaussian getTryTotal() {
        return tryTotal.getGaussian();
    }

    void setTrySupremacy(Gaussian trysupremacy) {
        this.trySupremacy.setGaussian(trysupremacy);
    }

    void setTrySupremacy(double skill, double stdDevn) {
        this.trySupremacy.updateGaussian(skill, stdDevn);
    }

    Gaussian getTrySupremacy() {
        return trySupremacy.getGaussian();
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
        scoreTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 25 + 20, 3, 0,
                        rugbyMaximumPoints * 2, false);
        scoreSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 25 - 20, 3,
                        -rugbyMaximumPoints, rugbyMaximumPoints, false);
        tryTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 2 + 1, 0.25, 0,
                        rugbyMaximumTrys * 2, false);
        trySupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 1, 0.25,
                        -rugbyMaximumTrys, rugbyMaximumTrys, false);
        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.05, 0.001, 0, 2, false);
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.15, 0.002, 0, 2,
                        false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("scoreTotal", scoreTotal);
        super.paramMap.put("scoreSupremacy", scoreSupremacy);
        super.paramMap.put("tryTotal", tryTotal);
        super.paramMap.put("trySupremacy", trySupremacy);
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
        result = prime * result + ((scoreSupremacy == null) ? 0 : scoreSupremacy.hashCode());
        result = prime * result + ((scoreTotal == null) ? 0 : scoreTotal.hashCode());
        result = prime * result + ((trySupremacy == null) ? 0 : trySupremacy.hashCode());
        result = prime * result + ((tryTotal == null) ? 0 : tryTotal.hashCode());
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
        RugbyUnionMatchParams other = (RugbyUnionMatchParams) obj;
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
        if (scoreSupremacy == null) {
            if (other.scoreSupremacy != null)
                return false;
        } else if (!scoreSupremacy.equals(other.scoreSupremacy))
            return false;
        if (scoreTotal == null) {
            if (other.scoreTotal != null)
                return false;
        } else if (!scoreTotal.equals(other.scoreTotal))
            return false;
        if (trySupremacy == null) {
            if (other.trySupremacy != null)
                return false;
        } else if (!trySupremacy.equals(other.trySupremacy))
            return false;
        if (tryTotal == null) {
            if (other.tryTotal != null)
                return false;
        } else if (!tryTotal.equals(other.tryTotal))
            return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        scoreTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("scoreTotal"));
        scoreSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("scoreSupremacy"));
        tryTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("tryTotal"));
        trySupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("trySupremacy"));

        homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("homeLoseBoost"));
        awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, delegate.get("awayLoseBoost"));
        powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("powerBoostRate"));
        updateParamMap();
    }

}
