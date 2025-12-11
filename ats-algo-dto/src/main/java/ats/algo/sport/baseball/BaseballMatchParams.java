package ats.algo.sport.baseball;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;

/**
 * 
 * @author Robert
 *
 */
public class BaseballMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam totalRuns;
    private MatchParam supremacy;

    public BaseballMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private BaseballMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    /*
     * getters and setters come in two flavours - setting and returning the matchParam or the underlying gaussian
     */
    void setTotalRuns(Gaussian skill) {
        this.totalRuns.setGaussian(skill);
    }

    Gaussian getTotalRuns() {
        return totalRuns.getGaussian();
    }

    void setSupremacy(Gaussian skill) {
        this.supremacy.setGaussian(skill);
    }

    Gaussian getSupremacy() {
        return supremacy.getGaussian();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams();
    }

    private void setDefaultParams() {
        totalRuns = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 10, 1, 0, 40, false);
        supremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, 1, 1, -15, 15, false);
        updateParamMap();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((supremacy == null) ? 0 : supremacy.hashCode());
        result = prime * result + ((totalRuns == null) ? 0 : totalRuns.hashCode());
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
        BaseballMatchParams other = (BaseballMatchParams) obj;
        if (supremacy == null) {
            if (other.supremacy != null)
                return false;
        } else if (!supremacy.equals(other.supremacy))
            return false;
        if (totalRuns == null) {
            if (other.totalRuns != null)
                return false;
        } else if (!totalRuns.equals(other.totalRuns))
            return false;
        return true;
    }

    /*
     * keep the paramMap in sync with the current set of parameters - remove any all old references and add the new ones
     */
    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("totalRuns", totalRuns);
        super.paramMap.put("supremacy", supremacy);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        totalRuns = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("totalRuns"));
        supremacy = new MatchParam(MatchParamType.BOTHDIFFERENCE, MarketGroup.NOT_SPECIFIED, delegate.get("supremacy"));
        updateParamMap();
    }
}
