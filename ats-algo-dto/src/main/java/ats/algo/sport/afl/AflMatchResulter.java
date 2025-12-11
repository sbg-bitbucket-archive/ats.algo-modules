package ats.algo.sport.afl;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;


public class AflMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {
        MatchResultMap result = new MatchResultMap();
        result.put("q1Point", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 300, "0-0"));
        result.put("q1Goal", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "0-0"));
        result.put("q2Point", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 300, "0-0"));
        result.put("q2Goal", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "0-0"));
        result.put("q3Point", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 300, "0-0"));
        result.put("q3Goal", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "0-0"));
        result.put("q4Point", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 300, "0-0"));
        result.put("q4Goal", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "0-0"));

        if (((AflMatchFormat) matchFormat).getExtraTimeMinutes() > 0) {
            result.put("qExtraTimePoint", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 300, "0-0"));
            result.put("qExtratimeGoal", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 100, "0-0"));
        }
        return result;
    }

    @Override
    public MatchState generateMatchStateForMatchResult(MatchState matchState, MatchResultMap matchManualResult,
                    boolean useSimpleMatchState) {

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int extraTimeMinutes = ((AflMatchFormat) matchState.getMatchFormat()).getExtraTimeMinutes();// EXTRA
                                                                                                    // TIME
                                                                                                    // INDICATOR
        int nPairs = 8;
        if (extraTimeMinutes > 0)
            nPairs = 10;

        PairOfIntegers[] teamScore = new PairOfIntegers[nPairs];
        teamScore[0] = map.get("q1Point").valueAsPairOfIntegers();
        teamScore[1] = map.get("q1Goal").valueAsPairOfIntegers();
        teamScore[2] = map.get("q2Point").valueAsPairOfIntegers();
        teamScore[3] = map.get("q2Goal").valueAsPairOfIntegers();
        teamScore[4] = map.get("q3Point").valueAsPairOfIntegers();
        teamScore[5] = map.get("q3Goal").valueAsPairOfIntegers();
        teamScore[6] = map.get("q4Point").valueAsPairOfIntegers();
        teamScore[7] = map.get("q4Goal").valueAsPairOfIntegers();
        if (extraTimeMinutes > 0) {
            teamScore[8] = map.get("qExtraTimePoint").valueAsPairOfIntegers();
            teamScore[9] = map.get("qExtratimeGoal").valueAsPairOfIntegers();
        }

        int pointsTotalA = 0;
        int pointsTotalB = 0;
        int goalsTotalA = 0;
        int goalsTotalB = 0;
        // int q1GoalsA = 0;
        // int q1GoalsB = 0;
        // int q2pointsA = 0;
        // int q2PointsB = 0;
        // int q2GoalsA = 0;
        // int q2GoalsB = 0;
        // int q3pointsA = 0;
        // int q3PointsB = 0;
        // int q3GoalsA = 0;
        // int q3GoalsB = 0;
        // int q4pointsA = 0;
        // int q4PointsB = 0;
        // int q4GoalsA = 0;
        // int q4GoalsB = 0;
        // int qExtraPointsA = 0;
        // int qExtraPointsB = 0;
        // int qExtraGoalsA = 0;
        // int qExtraGoalsB = 0;
        AflMatchState endMatchState = (AflMatchState) matchState.copy();
        for (int i = 0; i < nPairs; i++) {
            int qPointsA = teamScore[i].A;
            int qPointsB = teamScore[i].B;
            pointsTotalA += qPointsA;
            pointsTotalB += qPointsB;
            i++;
            int qGoalsA = teamScore[i].A;
            int qGoalsB = teamScore[i].B;
            goalsTotalA += qGoalsA;
            goalsTotalB += qGoalsB;
            System.out.println(" PointsA " + qPointsA + " qGoalsA " + qGoalsA + " PointsB " + qPointsB + " qGoalsB "
                            + qGoalsB);
            switch (i / 2) {
                case 0:
                    endMatchState.setGoalQ1A(qGoalsA);
                    endMatchState.setBehindQ1A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ1B(qGoalsB);
                    endMatchState.setBehindQ1B(qPointsB - qGoalsB * 6);
                    break;
                case 1:
                    endMatchState.setGoalQ2A(qGoalsA);
                    endMatchState.setBehindQ2A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ2B(qGoalsB);
                    endMatchState.setBehindQ2B(qPointsB - qGoalsB * 6);
                    break;
                case 2:
                    endMatchState.setGoalQ3A(qGoalsA);
                    endMatchState.setBehindQ3A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ3B(qGoalsB);
                    endMatchState.setBehindQ3B(qPointsB - qGoalsB * 6);
                    break;
                case 3:
                    endMatchState.setGoalQ4A(qGoalsA);
                    endMatchState.setBehindQ4A(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQ4B(qGoalsB);
                    endMatchState.setBehindQ4B(qPointsB - qGoalsB * 6);
                    break;
                case 4:
                    endMatchState.setGoalQEA(qGoalsA);
                    endMatchState.setBehindQEA(qPointsA - qGoalsA * 6);
                    endMatchState.setGoalQEB(qGoalsB);
                    endMatchState.setBehindQEB(qPointsB - qGoalsB * 6);
                    break;
            }
        }
        endMatchState.setPointsA(pointsTotalA);
        endMatchState.setPointsB(pointsTotalB);
        endMatchState.setGoalsA(goalsTotalA);
        endMatchState.setGoalsB(goalsTotalB);
        if (teamScore.length == 8) {
            endMatchState.setElapsedTimeSecs(4800);// was 4800,0
        } else {
            endMatchState.setElapsedTimeSecs(5100);// was 5100,1

        }
        endMatchState.setMannulResulting(true);
        endMatchState.setMatchPeriod(AflMatchPeriod.MATCH_COMPLETED);
        if (useSimpleMatchState) {
            MatchState simpleMatchState = endMatchState.generateSimpleMatchState();
            return simpleMatchState;
        }
        return endMatchState;
    }

}
