package ats.algo.sport.outrights;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;

public class OutrightsMatchParams extends MatchParams {

    private static final long serialVersionUID = 1L;
    private MatchParam dummyParam;

    public OutrightsMatchParams() {
        setDefaultParams();
    }

    @JsonCreator
    private OutrightsMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    public MatchParam getDummyParam() {
        return dummyParam;
    }

    public void setDummyParam(MatchParam dummyParam) {
        this.dummyParam = dummyParam;
    }



    public void updateParamsGivenMatchIncidentResult(MatchIncidentResult matchIncidentResult) {}



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dummyParam == null) ? 0 : dummyParam.hashCode());
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
        OutrightsMatchParams other = (OutrightsMatchParams) obj;
        if (dummyParam == null) {
            if (other.dummyParam != null)
                return false;
        } else if (!dummyParam.equals(other.dummyParam))
            return false;
        return true;
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        setDefaultParams();
    }

    public void setDefaultParams() {
        dummyParam = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.5, 0.05, 0, 1, true);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("dummyParam", dummyParam);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate2) {
        super.applyParams(delegate2);
        Map<String, Object> delegate = (Map<String, Object>) delegate2.get("paramMap");
        dummyParam = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, delegate.get("dummyParam"));
        updateParamMap();
    }
}
