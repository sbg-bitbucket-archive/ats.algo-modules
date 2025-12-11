package ats.algo.sport.outrights.calcengine.core;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;

/**
 * MatchParams are not used in Outrights since there is nothing the trader can directly modify or control. So this is
 * just an empty class
 * 
 * @author gicha
 *
 */
public final class AbstractParams extends MatchParams {

    private static final long serialVersionUID = 1L;

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {}

}
