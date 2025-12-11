package ats.algo.sport.icehockey;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;

public class IcehockeyMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {

        MatchResultMap result = new MatchResultMap();

        result.put("goalsFirstPeriod", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("goalsSecondPeriod", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("goalsThirdPeriod", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        if (((IcehockeyMatchFormat) matchFormat).getExtraTimeMinutes() > 0) {
            result.put("extraTimeGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        }

        return result;
    }

}
