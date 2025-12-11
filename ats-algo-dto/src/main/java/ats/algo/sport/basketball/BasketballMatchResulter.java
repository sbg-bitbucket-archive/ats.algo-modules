package ats.algo.sport.basketball;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class BasketballMatchResulter extends MatchResulter {


    public MatchResultMap generateProforma(MatchFormat matchFormat) {
        MatchResultMap result = new MatchResultMap();
        BasketballMatchFormat bmf = (BasketballMatchFormat) matchFormat;
        if (bmf.isTwoHalvesFormat()) {
            result.put("half1Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
            result.put("half2Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
        } else {
            result.put("quarter1Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
            result.put("quarter2Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
            result.put("quarter3Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
            result.put("quarter4Score", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
        }
        result.put("extraTimeScore", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "-1-0"));
        return result;
    }

    @Override
    public MatchState generateMatchStateForMatchResult(MatchState matchState, MatchResultMap matchManualResult,
                    boolean useSimpleMatchState) {

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        boolean twoHalvesFormat = ((BasketballMatchFormat) matchState.getMatchFormat()).isTwoHalvesFormat();
        int nSets;
        if (twoHalvesFormat)
            nSets = 2;
        else
            nSets = 4;
        PairOfIntegers[] setScore = new PairOfIntegers[nSets + 1];
        BasketballMatchState endMatchState = (BasketballMatchState) matchState.copy();
        if (twoHalvesFormat) {
            setScore[0] = map.get("half1Score").valueAsPairOfIntegers();
            setScore[1] = map.get("half2Score").valueAsPairOfIntegers();
            setScore[2] = map.get("extraTimeScore").valueAsPairOfIntegers();
            endMatchState.setFirstHalfPointsA(setScore[0].A);
            endMatchState.setFirstHalfPointsB(setScore[0].B);

            endMatchState.setSecondHalfPointsA(setScore[1].A);
            endMatchState.setSecondHalfPointsB(setScore[1].B);
            endMatchState.setOverTimeQuarterPointsA(setScore[2].A);
            endMatchState.setOverTimeQuarterPointsB(setScore[2].B);
            endMatchState.setPointsA(setScore[0].A + setScore[1].A + setScore[2].A);
            endMatchState.setPointsB(setScore[0].B + setScore[1].B + setScore[2].B);
        } else {
            setScore[0] = map.get("quarter1Score").valueAsPairOfIntegers();
            setScore[1] = map.get("quarter2Score").valueAsPairOfIntegers();
            setScore[2] = map.get("quarter3Score").valueAsPairOfIntegers();
            setScore[3] = map.get("quarter4Score").valueAsPairOfIntegers();
            setScore[4] = map.get("extraTimeScore").valueAsPairOfIntegers();
            endMatchState.setFirstQuarterPointsA(setScore[0].A);
            endMatchState.setFirstQuarterPointsB(setScore[0].B);
            endMatchState.setSecondQuarterPointsA(setScore[1].A);
            endMatchState.setSecondQuarterPointsB(setScore[1].B);

            endMatchState.setThirdQuarterPointsA(setScore[2].A);
            endMatchState.setThirdQuarterPointsB(setScore[2].B);
            endMatchState.setFourthQuarterPointsA(setScore[3].A);
            endMatchState.setFourthQuarterPointsB(setScore[3].B);
            endMatchState.setOverTimeQuarterPointsA(setScore[4].A);
            endMatchState.setOverTimeQuarterPointsB(setScore[4].B);

            endMatchState.setFirstHalfPointsA(setScore[0].A + setScore[1].A);
            endMatchState.setFirstHalfPointsB(setScore[0].B + setScore[1].B);

            endMatchState.setSecondHalfPointsA(setScore[2].A + setScore[3].A);
            endMatchState.setSecondHalfPointsB(setScore[2].B + setScore[3].B);

            endMatchState.setPointsA(setScore[0].A + setScore[1].A + setScore[2].A + setScore[3].A + setScore[4].A);
            endMatchState.setPointsB(setScore[0].B + setScore[1].B + setScore[2].B + setScore[3].B + setScore[4].B);
        }
        endMatchState.setMatchPeriod(BasketballMatchPeriod.MATCH_COMPLETED);
        int elapsedTimeSecs = 2900;// set over normal time

        endMatchState.setElapsedTimeSecs(elapsedTimeSecs);

        if (useSimpleMatchState) {
            MatchState simpleMatchState = endMatchState.generateSimpleMatchState();
            return simpleMatchState;
        }
        return endMatchState;
    }

}
