package example.algo.tennis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.tennis.PlayerId;

/**
 * provides an example of a AlgoMatchParams class that implements two parameters onServePctA and onServePctB
 * 
 * @author Geoff
 *
 */
public class ExampleTennisMatchParams extends AlgoMatchParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private MatchParam onServePctA;
    private MatchParam onServePctB;

    /*
     * define the standard getters and setters for the params you define so they can be used elsewhere within your
     * model. getters and setters should be either package level access only or marked with @JsonIgnore or both
     */

    @JsonIgnore
    MatchParam getOnServePctA() {
        return onServePctA;
    }

    @JsonIgnore
    void setOnServePctA(MatchParam onServePctA) {
        this.onServePctA = onServePctA;
    }

    @JsonIgnore
    MatchParam getOnServePctB() {
        return onServePctB;
    }

    @JsonIgnore
    void setOnServePctB(MatchParam onServePctB) {
        this.onServePctB = onServePctB;
    }

    /**
     * Constructor. Should create instances of each param you want to define
     */
    public ExampleTennisMatchParams() {
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
        onServePctA = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.2, 0.8, true);
        onServePctB = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.2, 0.8, true);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("onServePctA", onServePctA);
        super.paramMap.put("onServePctB", onServePctB);

    }

    /**
     * method used by the ExampleTennisMatchEngine class
     * 
     * @param playerId
     * @return
     */
    Gaussian getOnServePct(PlayerId playerId) {
        switch (playerId) {
            case A1:
            case A2:
                return this.onServePctA.getGaussian();
            case B1:
            case B2:
                return this.onServePctB.getGaussian();
            default:
                throw new IllegalArgumentException(String.format("Unexpected playerId %s", playerId.toString()));
        }
    }

}
