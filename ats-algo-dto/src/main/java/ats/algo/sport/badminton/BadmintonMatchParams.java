package ats.algo.sport.badminton;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.genericsupportfunctions.Gaussian;

public class BadmintonMatchParams extends MatchParams {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MatchParam onServePctA;

    public BadmintonMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private BadmintonMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    Gaussian getOnServePctA() {
        return onServePctA.getGaussian();
    }

    void setOnServePctA(Gaussian skill) {
        this.onServePctA.setGaussian(skill);
    }

    void setOnServePctA(double p, double stdDevn) {
        this.onServePctA.updateGaussian(p, stdDevn);
    }

    public void updateParamsGivenMatchIncidentResult(int pointDifference) {
        double mean = onServePctA.getGaussian().getMean();
        double sd = 0.025d;
        int pd = Math.abs(pointDifference);
        if (pd > 3 && pd < 7)
            sd = 0.025D;
        else if (pd > 7)
            sd = 0.05D;
        else
            sd = 0;
        if (pointDifference > 0)
            mean += sd * sd / mean;
        else
            mean -= sd * sd / mean;

        onServePctA.getGaussian().setMean(mean);
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .45, 0.04, 0, 1, true);
        updateParamMap();
    }

    public void setDefaultParams() {
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, .45, 0.04, 0, 1, true);
        updateParamMap();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((onServePctA == null) ? 0 : onServePctA.hashCode());
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
        BadmintonMatchParams other = (BadmintonMatchParams) obj;
        if (onServePctA == null) {
            if (other.onServePctA != null)
                return false;
        } else if (!onServePctA.equals(other.onServePctA))
            return false;
        return true;
    }

    /*
     * keep the paramMap in sync with the current set of parameters - remove any all old references and add the new ones
     */
    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onServePctA", onServePctA);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate.get("onServePctA"));
        updateParamMap();
    }
}
