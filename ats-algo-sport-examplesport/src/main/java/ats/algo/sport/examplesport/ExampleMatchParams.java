package ats.algo.sport.examplesport;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;

public class ExampleMatchParams extends AlgoMatchParams {

    private static final long serialVersionUID = 1L;

    private MatchParam probAWinsLeg;

    public ExampleMatchParams() {
        probAWinsLeg = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.6, 0.05, 0.2, 0.8,
                        false);
        super.paramMap.put("probAWinsLeg", probAWinsLeg);
    }

    public MatchParam getProbAWinsLeg() {
        return probAWinsLeg;
    }

    public void setProbAWinsLeg(MatchParam probAWinsLeg) {
        this.probAWinsLeg = probAWinsLeg;
    }

    public void setProbAWinsLeg(double p, double stdDevn) {
        this.probAWinsLeg.updateGaussian(p, stdDevn);
    }


    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        this.probAWinsLeg.updateGaussian(0.6, 0.05);
    }


}
