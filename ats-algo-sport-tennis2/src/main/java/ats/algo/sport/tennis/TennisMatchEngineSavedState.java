package ats.algo.sport.tennis;

import java.io.Serializable;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;

public class TennisMatchEngineSavedState extends MatchEngineSavedState implements Serializable {

    private static final long serialVersionUID = 1L;

    private GenericMatchParams lastSeenMatchParams;
    private GenericMatchParams intraGameUpdatedMatchParams;

    public TennisMatchEngineSavedState() {
        super();
        lastSeenMatchParams = (new TennisMatchParams()).generateGenericMatchParams();
        intraGameUpdatedMatchParams = (new TennisMatchParams()).generateGenericMatchParams();
    }

    TennisMatchParams getIntraGameUpdatedTennisMatchParams() {
        return (TennisMatchParams) intraGameUpdatedMatchParams.generateXxxMatchParams();
    }

    void setIntraGameUpdatedTennisMatchParams(TennisMatchParams tmpMatchParams) {
        this.intraGameUpdatedMatchParams = tmpMatchParams.generateGenericMatchParams();
    }

    TennisMatchParams getLastSeenTennisMatchParams() {
        return (TennisMatchParams) lastSeenMatchParams.generateXxxMatchParams();
    }

    void setLastSeenTennisMatchParams(TennisMatchParams lastSeenMatchParams) {
        this.lastSeenMatchParams = lastSeenMatchParams.generateGenericMatchParams();
    }

    public GenericMatchParams getLastSeenMatchParams() {
        return lastSeenMatchParams;
    }

    public void setLastSeenMatchParams(GenericMatchParams lastSeenMatchParams) {
        this.lastSeenMatchParams = lastSeenMatchParams;
    }

    public GenericMatchParams getIntraGameUpdatedMatchParams() {
        return intraGameUpdatedMatchParams;
    }

    public void setIntraGameUpdatedMatchParams(GenericMatchParams intraGameUpdatedMatchParams) {
        this.intraGameUpdatedMatchParams = intraGameUpdatedMatchParams;
    }



}
