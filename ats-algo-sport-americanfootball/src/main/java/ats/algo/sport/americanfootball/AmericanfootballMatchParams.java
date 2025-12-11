package ats.algo.sport.americanfootball;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class AmericanfootballMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam scoreTotal;
    private MatchParam scoreSupremacy;
    private MatchParam tdTotal;
    private MatchParam tdSupremacy;
    // private MatchParam homeLoseBoost;
    // private MatchParam awayLoseBoost;
    // private MatchParam powerBoostRate;
    @JsonIgnore
    private double nflMaximumPoints = 120;
    @JsonIgnore
    private double nflMaximumTds = 20;
    // private MatchParam redCardPowerBoostRate;

    public AmericanfootballMatchParams() {
        super();
        setDefaultParams();
    }



    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    // void setPowerBoostRate(Gaussian powerBoostRate) {
    // this.powerBoostRate.setParam(powerBoostRate);
    // }
    //
    // public void setPowerBoostRate(double skill, double stdDevn) {
    // this.powerBoostRate.setParam(skill, stdDevn);
    // }
    //
    // public Gaussian getPowerBoostRate() { // AsGaussian
    // return powerBoostRate.getParam();
    // }

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

    void setTdTotal(Gaussian trytotal) {
        this.tdTotal.setGaussian(trytotal);
    }

    void setTdTotal(double skill, double stdDevn) {
        this.tdTotal.updateGaussian(skill, stdDevn);
    }

    Gaussian getTdTotal() {
        return tdTotal.getGaussian();
    }

    void setTdSupremacy(Gaussian trysupremacy) {
        this.tdSupremacy.setGaussian(trysupremacy);
    }

    void setTdSupremacy(double skill, double stdDevn) {
        this.tdSupremacy.updateGaussian(skill, stdDevn);
    }

    Gaussian getTdSupremacy() {
        return tdSupremacy.getGaussian();
    }


    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {
        scoreTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 52, 3, 0,
                        nflMaximumPoints * 2, false);
        scoreSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 3.3, 0.5,
                        -nflMaximumPoints, nflMaximumPoints, false);
        tdTotal = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 5, 0.45, 0, nflMaximumTds * 2,
                        false);
        tdSupremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 0, 0.25, -nflMaximumTds,
                        nflMaximumTds, false);
        updateParamMap();
        // homeLoseBoost = new MatchParam(MatchParamType.A, MarketGroup.GOALS, homeLoseBoostKey, 0.05, 0.001);
        // awayLoseBoost = new MatchParam(MatchParamType.B, MarketGroup.GOALS, awayLoseBoostKey, 0.05, 0.001);
        // powerBoostRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.GOALS, powerBoostKey, 0.15, 0.002);
    }

    /*
     * keep the paramMap in sync with the current set of parameters - remove any all old references and add the new ones
     */
    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("scoreTotal", scoreTotal);
        super.paramMap.put("scoreSupremacy", scoreSupremacy);
        super.paramMap.put("tdTotal", tdTotal);
        super.paramMap.put("tdSupremacy", tdSupremacy);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((scoreSupremacy == null) ? 0 : scoreSupremacy.hashCode());
        result = prime * result + ((scoreTotal == null) ? 0 : scoreTotal.hashCode());
        result = prime * result + ((tdSupremacy == null) ? 0 : tdSupremacy.hashCode());
        result = prime * result + ((tdTotal == null) ? 0 : tdTotal.hashCode());
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
        AmericanfootballMatchParams other = (AmericanfootballMatchParams) obj;
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
        if (tdSupremacy == null) {
            if (other.tdSupremacy != null)
                return false;
        } else if (!tdSupremacy.equals(other.tdSupremacy))
            return false;
        if (tdTotal == null) {
            if (other.tdTotal != null)
                return false;
        } else if (!tdTotal.equals(other.tdTotal))
            return false;
        return true;
    }

}
