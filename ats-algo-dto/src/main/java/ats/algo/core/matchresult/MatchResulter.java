package ats.algo.core.matchresult;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;

public class MatchResulter {

    /**
     * Should be overriden by any sport that supports manual resulting of markets If not overridden then returns a map
     * with just one property: "notSupportedForThisSport".
     * 
     * @return
     */
    public MatchResultMap generateProforma(MatchFormat matchFormat) {
        MatchResultMap result = new MatchResultMap();
        result.put("notSupportedForThisSport", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 0, "0"));
        return result;

    }

    /**
     * Should be overridden by any class that supports manual resulting of markets. If not overridden then an unchanged
     * matchState object is returned
     * 
     * Creates a new MatchState object which reflects the properties contained in matchManualResult
     * 
     * @param startMatchState - the current matchState at the time the method is called - will generally be the
     *        pre-match state.
     * @param matchResultMap
     * @return the MatchState at the end of the match with as much as possible of the matchState filled in
     */
    public MatchState generateMatchStateForMatchResult(MatchState startMatchState, MatchResultMap matchResultMap,
                    boolean convertToSimpleMatchState) {
        return startMatchState;
    }

}
