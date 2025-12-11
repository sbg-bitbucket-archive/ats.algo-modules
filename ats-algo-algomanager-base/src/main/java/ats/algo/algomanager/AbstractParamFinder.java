package ats.algo.algomanager;

import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.core.AtsBean;

public abstract class AbstractParamFinder extends AtsBean {
    public abstract ParamFindResponse calculate(ParamFindRequest request);
}
