
package ats.algo.sport.afl;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class AflMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private static final double aflPointsMaxMean = 250;
    @JsonIgnore
    private static final double aflGoalsMaxMean = 40;
    private MatchParam totalScoreRate;
    private MatchParam supremacyScoreRate;
    private MatchParam totalGoalRate;
    private MatchParam supremacyGoalRate;
    // private MatchParam redCardPowerBoostRate;

    public AflMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private AflMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */

    void setTotalScoreRate(Gaussian totalScoreRate) {
        this.totalScoreRate.setGaussian(totalScoreRate);

    }

    Gaussian getTotalGoalRate() {
        return totalGoalRate.getGaussian();
    }

    void setTotalGoalRate(Gaussian totalGoalRate) {
        this.totalGoalRate.setGaussian(totalGoalRate);
    }

    Gaussian getSupremacyGoalRate() {
        return supremacyGoalRate.getGaussian();
    }

    void setSupremacyGoalRate(Gaussian supremacyGoalRate) {
        this.supremacyGoalRate.setGaussian(supremacyGoalRate);
    }

    void setSupremacyScoreRate(Gaussian supremacyScoreRate) {
        this.supremacyScoreRate.setGaussian(supremacyScoreRate);
    }

    void setTotalScoreRate(double skill, double stdDevn) {
        this.totalScoreRate.updateGaussian(skill, stdDevn);
    }

    void setSupremacyScoreRate(double skill, double stdDevn) {
        this.supremacyScoreRate.updateGaussian(skill, stdDevn);
    }

    Gaussian getTotalScoreRate() {
        return totalScoreRate.getGaussian();
    }

    Gaussian getSupremacyScoreRate() {
        return supremacyScoreRate.getGaussian();
    }


    // @Override
    // public void setEqualTo(MatchParams matchParams) {
    // super.setEqualTo(matchParams);
    // this.setTotalScoreRate(((AflMatchParams) matchParams).getTotalScoreRate());
    // this.setSupremacyScoreRate(((AflMatchParams) matchParams).getSupremacyScoreRate());
    // this.setTotalGoalRate(((AflMatchParams) matchParams).getTotalGoalRate());
    // this.setSupremacyGoalRate(((AflMatchParams) matchParams).getSupremacyGoalRate());
    // }

    // @JsonIgnore
    // private static final String totalScoreRateKey = "totalScoreRate";
    // @JsonIgnore
    // private static final String supremacyScoreRateKey = "supremacyScoreRate";
    //
    // @JsonIgnore
    // private static final String totalGoalRateKey = "totalGoalRate";
    // @JsonIgnore
    // private static final String supremacyGoalRateKey = "supremacyGoalRate";

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams(); // defaults don't depend on match format for now
    }

    private void setDefaultParams() {

        totalScoreRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 180.5, 15, 0,
                        aflPointsMaxMean * 2, false);
        supremacyScoreRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 5, 15,
                        -aflPointsMaxMean, aflPointsMaxMean, false);
        totalGoalRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 26, 2.6, 0,
                        aflGoalsMaxMean * 2, false);
        supremacyGoalRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 1, 2.6,
                        -aflGoalsMaxMean, aflGoalsMaxMean, false);
        updateParamMap();
    }

    /*
     * keep the paramMap in sync with the current set of parameters - remove any all old references and add the new ones
     */
    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("totalScoreRate", totalScoreRate);
        super.paramMap.put("supremacyScoreRate", supremacyScoreRate);
        super.paramMap.put("totalGoalRate", totalGoalRate);
        super.paramMap.put("supremacyGoalRate", supremacyGoalRate);
    }



    /**
     * this function is passed to the momentum updating logic in . In this case it is a very simple function, since x[0]
     * is the prob that A wins a Leg
     * 
     * @param x
     * @return
     */
    /*
     * private double probAWinsLegGivenX(double[] x) { return x[0]; }
     */



    // @Override
    // public LinkedHashMap<String, MatchParam> getParamMap() {
    //
    // LinkedHashMap<String, MatchParam> map = new LinkedHashMap<String, MatchParam>();
    // MatchParam cc = totalScoreRate.copy();
    // map.put(cc.getParamName(), cc);
    // cc = supremacyScoreRate.copy();
    // map.put(cc.getParamName(), cc);
    // cc = totalGoalRate.copy();
    // map.put(cc.getParamName(), cc);
    // cc = supremacyGoalRate.copy();
    // map.put(cc.getParamName(), cc);
    //
    // return map;
    // }
    //
    // @Override
    // public String setFromMap(LinkedHashMap<String, MatchParam> map) {
    // MatchParam p;
    // String paramInError = "param-4*stdDevn must be >0: ";
    // try {
    // paramInError += totalScoreRateKey;
    // p = map.get(totalScoreRateKey);
    // if (paramIsOk(p.getGaussian())) {
    // this.setTotalScoreRate(p.getGaussian());
    // } else
    // throw new Exception();
    // paramInError += supremacyScoreRateKey;
    // p = map.get(supremacyScoreRateKey);
    // if (paramDiffIsOk(p.getGaussian())) {
    // this.setSupremacyScoreRate(p.getGaussian());
    // } else
    // throw new Exception();
    // paramInError += totalGoalRateKey;
    // p = map.get(totalGoalRateKey);
    // if (paramIsOk(p.getGaussian()) && tryRateIsOk(p.getGaussian(), totalScoreRate)) {
    // this.setTotalGoalRate(p.getGaussian());
    // } else
    // throw new Exception();
    // paramInError += supremacyGoalRateKey;
    // p = map.get(supremacyGoalRateKey);
    // if (paramDiffIsOk(p.getGaussian())) {
    // this.setSupremacyGoalRate(p.getGaussian());
    // } else
    // throw new Exception();
    // } catch (Exception e) {
    // return paramInError;
    // }
    // return null;
    // }



    @Override

    @SuppressWarnings("unchecked")
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        totalScoreRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("totalScoreRate"));
        supremacyScoreRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("supremacyScoreRate"));
        totalGoalRate = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED,
                        delegate.get("totalGoalRate"));
        supremacyGoalRate = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED,
                        delegate.get("supremacyGoalRate"));
        updateParamMap();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((supremacyGoalRate == null) ? 0 : supremacyGoalRate.hashCode());
        result = prime * result + ((supremacyScoreRate == null) ? 0 : supremacyScoreRate.hashCode());
        result = prime * result + ((totalGoalRate == null) ? 0 : totalGoalRate.hashCode());
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
        AflMatchParams other = (AflMatchParams) obj;
        if (supremacyGoalRate == null) {
            if (other.supremacyGoalRate != null)
                return false;
        } else if (!supremacyGoalRate.equals(other.supremacyGoalRate))
            return false;
        if (supremacyScoreRate == null) {
            if (other.supremacyScoreRate != null)
                return false;
        } else if (!supremacyScoreRate.equals(other.supremacyScoreRate))
            return false;
        if (totalGoalRate == null) {
            if (other.totalGoalRate != null)
                return false;
        } else if (!totalGoalRate.equals(other.totalGoalRate))
            return false;
        if (totalScoreRate == null) {
            if (other.totalScoreRate != null)
                return false;
        } else if (!totalScoreRate.equals(other.totalScoreRate))
            return false;
        return true;
    }

}
