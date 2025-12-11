package ats.algo.sport.futsal;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;

public class FutsalMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {

        MatchResultMap result = new MatchResultMap();

        result.put("firstHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("secondHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        return result;
    }

}
