package ats.algo.sport.fantasyexample;

import java.io.Serializable;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;

/**
 * provides an example of a AlgoMatchParams class that implements two parameters onServePctA and onServePctB
 * 
 * @author Geoff
 *
 */
public class FantasyExampleSportMatchParams extends AlgoMatchParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private MatchParam player1;
    private MatchParam player2;
    private MatchParam player3;

    /*
     * define the standard getters and setters for the params you define so they can be used elsewhere within your
     * model. getters and setters should be either package level access only or marked with @JsonIgnore or both
     */

    public MatchParam getPlayer1() {
        return player1;
    }

    public MatchParam getPlayer2() {
        return player2;
    }

    public MatchParam getPlayer3() {
        return player3;
    }

    /**
     * Constructor. Should create instances of each param you want to define
     */
    public FantasyExampleSportMatchParams() {
        super();
        setDefaultParams();
    }

    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        /*
         * add any entries you need for params that you define. In this example default params do not depend on the
         * match format
         */
        setDefaultParams();
    }

    private void setDefaultParams() {
        player1 = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 6, 1, 0, 20, false);
        player2 = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 6, 1, 0, 20, false);
        player3 = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 6, 1, 0, 20, false);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("Patrick Bamford", player1);
        super.paramMap.put("Steven Defour", player2);
        super.paramMap.put("Ryan Bertrand", player3);

    }

}
