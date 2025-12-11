package ats.algo.sport.testsport;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.MatchParams;

/**
 * provides an example of a MatchParams class that implements two parameters onServePctA and onServePctB
 * 
 * @author Geoff
 *
 */
public class TestSportMatchParams extends MatchParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private MatchParam pctAWinsGame;

    /*
     * define the standard getters and setters for the params you define so they can be used elsewhere within your
     * model. getters and setters should be either package level access only or marked with @JsonIgnore or both
     */


    /**
     * Constructor. Should create instances of each param you want to define
     */
    public TestSportMatchParams() {
        setDefaultParams();
    }



    MatchParam getPctAWinsGame() {
        return pctAWinsGame;
    }


    void setPctAWinsGame(MatchParam pctAWinsGame) {
        this.pctAWinsGame = pctAWinsGame;
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
        pctAWinsGame = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.65, 0.05, 0.01, 0.99, true);
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("pctAWinsGame", pctAWinsGame);

    }

    @JsonCreator
    /**
     * no need to amend this
     * 
     * @param delegate
     */
    public TestSportMatchParams(Map<String, Object> delegate) {
        super(delegate);
        applyParams(delegate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyParams(Map<String, Object> delegate) {
        super.applyParams(delegate);
        /*
         * this method is called during the deserialisation of objects from json. Include similar entries for any params
         * you define
         */
        Map<String, Object> delegate2 = (Map<String, Object>) delegate.get("paramMap");
        pctAWinsGame = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, delegate2.get("pctAWinsGame"));
        updateParamMap();

    }

}
