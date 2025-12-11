package ats.algo.montecarloframework;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;

class TestMatchParams extends MatchParams {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    double probAWinsSet;

    /*
     * can be empty for this test
     */
    // @Override
    // public MatchParams copy() {
    // return null;
    // }
    @Override
    public void setEqualTo(MatchParams matchParams) {}

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {}

    @Override
    public void applyParams(Map<String, Object> delegate) {

    }
}
