package ats.algo.algomanager;

import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.core.AtsBean;

public abstract class AbstractPriceCalculator extends AtsBean {

    public abstract PriceCalcResponse calculate(PriceCalcRequest request);

}
