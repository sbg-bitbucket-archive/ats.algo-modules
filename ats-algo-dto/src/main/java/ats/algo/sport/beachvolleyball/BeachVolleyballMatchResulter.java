package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;

public class BeachVolleyballMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {
        MatchResultMap result = new MatchResultMap();
        int pointsRegularGames = ((BeachVolleyballMatchFormat) matchFormat).getnPointInRegularSet();
        int pointsFinalGames = ((BeachVolleyballMatchFormat) matchFormat).getnPointInFinalSet();
        int gamesInMatch = ((BeachVolleyballMatchFormat) matchFormat).getnSetsInMatch();
        if (gamesInMatch == 3) {
            result.put("set1Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsRegularGames, "-1-0"));
            result.put("set2Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsRegularGames, "-1-0"));
            result.put("set3Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsFinalGames, "-1-0"));
        } else if (gamesInMatch == 5) {
            result.put("set1Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsRegularGames, "-1-0"));
            result.put("set2Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsRegularGames, "-1-0"));
            result.put("set3Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsFinalGames, "-1-0"));
            result.put("set4Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsRegularGames, "-1-0"));
            result.put("set5Score",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, pointsFinalGames, "-1-0"));
        }
        return result;
    }

}
