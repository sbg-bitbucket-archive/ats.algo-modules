package ats.algo.sport.badminton;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;


public class BadmintonMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {
        MatchResultMap result = new MatchResultMap();
        int pointsRegularGames = ((BadmintonMatchFormat) matchFormat).getnPointInRegularGame();
        int pointsFinalGames = ((BadmintonMatchFormat) matchFormat).getMaxPointInFinalGame();
        int gamesInMatch = ((BadmintonMatchFormat) matchFormat).getnGamesInMatch();
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

    @Override
    public MatchState generateMatchStateForMatchResult(MatchState matchState, MatchResultMap matchManualResult,
                    boolean useSimpleMatchState) {

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int nSets = ((BadmintonMatchFormat) matchState.getMatchFormat()).getnGamesInMatch();
        PairOfIntegers[] setScore = new PairOfIntegers[nSets];
        setScore[0] = map.get("set1Score").valueAsPairOfIntegers();
        setScore[1] = map.get("set2Score").valueAsPairOfIntegers();
        if (nSets == 3) {
            if (map.get("set3Score") != null)
                setScore[2] = map.get("set3Score").valueAsPairOfIntegers();
            else
                nSets--;
        }
        if (nSets == 5) {
            if (map.get("set5Score") != null)
                setScore[4] = map.get("set5Score").valueAsPairOfIntegers();
            else
                nSets--;
            if (map.get("set4Score") != null)
                setScore[3] = map.get("set4Score").valueAsPairOfIntegers();
            else
                nSets--;
        }
        int setsA = 0;
        int setsB = 0;
        int gamesA = 0;
        int gamesB = 0;
        BadmintonMatchState endMatchState = (BadmintonMatchState) matchState.copy();
        for (int i = 0; i < nSets; i++) {
            int nA = setScore[i].A;
            int nB = setScore[i].B;
            endMatchState.setPointScoreInGameN(i, nA, nB);
            if (nA > nB)
                setsA++;
            else
                setsB++;
            gamesA += nA;
            gamesB += nB;
        }


        PairOfIntegers[] gameScoreInGameN = new PairOfIntegers[nSets];
        for (int i = 0; i < nSets; i++) {
            gameScoreInGameN[i] = new PairOfIntegers();
            gameScoreInGameN[i].A = setScore[i].A;
            gameScoreInGameN[i].B = setScore[i].B;
        }
        endMatchState.setGameScoreInGameN(gameScoreInGameN);
        endMatchState.setGamesA(setsA);
        endMatchState.setGamesB(setsB);;
        endMatchState.setPointsA(gamesA);
        endMatchState.setPointsB(gamesB);

        if (useSimpleMatchState) {
            MatchState simpleMatchState = endMatchState.generateSimpleMatchState();
            return simpleMatchState;
        }
        return endMatchState;
    }

}
