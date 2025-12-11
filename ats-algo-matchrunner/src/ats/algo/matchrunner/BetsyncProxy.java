package ats.algo.matchrunner;

import java.io.Serializable;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.core.util.json.JsonUtil;

/**
 * This class mirrors FeedBetsyncDto class used within ATS. The purpose is to ensure that any json serialization errors
 * get picked up in MatchRunner at an early state in testing
 * 
 * @author gicha
 *
 */
public class BetsyncProxy implements Serializable {

    private static final long serialVersionUID = 1L;

    GenericMatchParams matchParams = null;
    MatchState matchState = null;
    SimpleMatchState simpleMatchState = null;
    MatchIncident matchIncident = null;

    public GenericMatchParams getMatchParams() {
        return matchParams;
    }

    public void setMatchParams(GenericMatchParams matchParams) {
        this.matchParams = matchParams;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public SimpleMatchState getSimpleMatchState() {
        return simpleMatchState;
    }

    public void setSimpleMatchState(SimpleMatchState simpleMatchState) {
        this.simpleMatchState = simpleMatchState;
    }

    public MatchIncident getMatchIncident() {
        return matchIncident;
    }

    public void setMatchIncident(MatchIncident matchIncident) {
        this.matchIncident = matchIncident;
    }

    public static void checkSerializationOk(Object o) {
        BetsyncProxy p = new BetsyncProxy();
        if (o instanceof GenericMatchParams)
            p.setMatchParams((GenericMatchParams) o);
        if (o instanceof MatchState) {
            MatchState s = (MatchState) o;
            p.setMatchState(s);
            p.setSimpleMatchState((SimpleMatchState) s.generateSimpleMatchState());
        }
        if (o instanceof SimpleMatchState)
            p.setSimpleMatchState((SimpleMatchState) o);
        if (o instanceof MatchIncident)
            p.setMatchIncident((MatchIncident) o);
        String json = JsonUtil.marshalJson(p, true);
        // System.out.println(json);
        JsonUtil.unmarshalJson(json, BetsyncProxy.class);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchIncident == null) ? 0 : matchIncident.hashCode());
        result = prime * result + ((matchParams == null) ? 0 : matchParams.hashCode());
        result = prime * result + ((matchState == null) ? 0 : matchState.hashCode());
        result = prime * result + ((simpleMatchState == null) ? 0 : simpleMatchState.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BetsyncProxy other = (BetsyncProxy) obj;
        if (matchIncident == null) {
            if (other.matchIncident != null)
                return false;
        } else if (!matchIncident.equals(other.matchIncident))
            return false;
        if (matchParams == null) {
            if (other.matchParams != null)
                return false;
        } else if (!matchParams.equals(other.matchParams))
            return false;
        if (matchState == null) {
            if (other.matchState != null)
                return false;
        } else if (!matchState.equals(other.matchState))
            return false;
        if (simpleMatchState == null) {
            if (other.simpleMatchState != null)
                return false;
        } else if (!simpleMatchState.equals(other.simpleMatchState))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BetsyncProxy [matchParams=" + matchParams + ", matchState=" + matchState + ", simpleMatchState="
                        + simpleMatchState + ", matchIncident=" + matchIncident + "]";
    }



}
