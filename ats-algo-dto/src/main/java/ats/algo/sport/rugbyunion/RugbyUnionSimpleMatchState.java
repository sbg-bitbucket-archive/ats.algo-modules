package ats.algo.sport.rugbyunion;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;

public class RugbyUnionSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int pointsA;
    private int pointsB;
    private int elapsedTimeSecs;
    private RugbyUnionMatchPeriod matchPeriod;

    private int pointsFirstHalfA;
    private int pointsFirstHalfB;
    private int pointsSecondHalfA;
    private int pointsSecondHalfB;

    @JsonCreator
    public RugbyUnionSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") RugbyUnionMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSecs") int elapsedTimeSecs,
                    @JsonProperty("pointsFirstHalfA") int pointsFirstHalfA,
                    @JsonProperty("pointsFirstHalfB") int pointsFirstHalfB,
                    @JsonProperty("pointsSecondHalfA") int pointsSecondHalfA,
                    @JsonProperty("pointsSecondHalfB") int pointsSecondHalfB)

    {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.pointsFirstHalfA = pointsFirstHalfA;
        this.pointsFirstHalfB = pointsFirstHalfB;
        this.pointsSecondHalfA = pointsSecondHalfA;
        this.pointsSecondHalfB = pointsSecondHalfB;
        this.pointsA = pointsFirstHalfA + pointsSecondHalfA;
        this.pointsB = pointsFirstHalfB + pointsSecondHalfB;
    }


    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        if ((getPointsFirstHalfA() > (-1)) && (getPointsFirstHalfB() > (-1))) {
            String goalsAFP = Integer.toString(getPointsFirstHalfA());
            String goalsBFP = Integer.toString(getPointsFirstHalfB());
            resultMap.put("pointsFirstHalf", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsAFP + "-" + goalsBFP));
        }

        if ((getPointsSecondHalfA() > (-1)) && (getPointsSecondHalfB() > (-1))) {
            String goalsASP = Integer.toString(getPointsSecondHalfA());
            String goalsBSP = Integer.toString(getPointsSecondHalfB());
            resultMap.put("pointsSecondHalf", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsASP + "-" + goalsBSP));
        }

        if ((getPointsA() > (-1)) && (getPointsB() > (-1))) {
            String goalsATP = Integer.toString(getPointsFirstHalfA() + getPointsSecondHalfA());
            String goalsBTP = Integer.toString(getPointsFirstHalfB() + getPointsSecondHalfB());
            resultMap.put("points", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsATP + "-" + goalsBTP));
        }

        return resultMap;
    }

    @Override
    public RugbyUnionSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        RugbyUnionMatchState fms = new RugbyUnionMatchState(matchFormat);

        Map<String, MatchResultElement> map = result.getMap();

        boolean fPeriodGoals = map.get("pointsFirstHalf") != null;
        boolean sPeriodGoals = map.get("pointsSecondHalf") != null;

        int firstPeriodGoalsA = -1;
        int firstPeriodGoalsB = -1;
        int secondPeriodGoalsA = -1;
        int secondPeriodGoalsB = -1;

        if (fPeriodGoals && sPeriodGoals) {
            firstPeriodGoalsA = map.get("pointsFirstHalf").valueAsPairOfIntegersCheckingNegatives().A;
            firstPeriodGoalsB = map.get("pointsFirstHalf").valueAsPairOfIntegersCheckingNegatives().B;

            secondPeriodGoalsA = map.get("pointsSecondHalf").valueAsPairOfIntegersCheckingNegatives().A;
            secondPeriodGoalsB = map.get("pointsSecondHalf").valueAsPairOfIntegersCheckingNegatives().B;

            fms.setPointsFirstHalfA(firstPeriodGoalsA);
            fms.setPointsSecondHalfA(secondPeriodGoalsA);
            fms.setPointsFirstHalfB(firstPeriodGoalsB);
            fms.setPointsSecondHalfB(secondPeriodGoalsB);
        }

        fms.setMatchPeriod(RugbyUnionMatchPeriod.MATCH_COMPLETED);
        RugbyUnionSimpleMatchState sms = (RugbyUnionSimpleMatchState) fms.generateSimpleMatchState();
        return sms;
    }



    public RugbyUnionSimpleMatchState() {
        super();
        matchPeriod = RugbyUnionMatchPeriod.PREMATCH;
    }



    public int getPointsA() {
        return pointsA;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    public RugbyUnionMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(RugbyUnionMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public int getPointsFirstHalfA() {
        return pointsFirstHalfA;
    }

    public void setPointsFirstHalfA(int pointsFirstHalfA) {
        this.pointsFirstHalfA = pointsFirstHalfA;
    }

    public int getPointsFirstHalfB() {
        return pointsFirstHalfB;
    }

    public void setPointsFirstHalfB(int pointsFirstHalfB) {
        this.pointsFirstHalfB = pointsFirstHalfB;
    }

    public int getPointsSecondHalfA() {
        return pointsSecondHalfA;
    }

    public void setPointsSecondHalfA(int pointsSecondHalfA) {
        this.pointsSecondHalfA = pointsSecondHalfA;
    }

    public int getPointsSecondHalfB() {
        return pointsSecondHalfB;
    }

    public void setPointsSecondHalfB(int pointsSecondHalfB) {
        this.pointsSecondHalfB = pointsSecondHalfB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSecs;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + pointsFirstHalfA;
        result = prime * result + pointsFirstHalfB;
        result = prime * result + pointsSecondHalfA;
        result = prime * result + pointsSecondHalfB;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RugbyUnionSimpleMatchState other = (RugbyUnionSimpleMatchState) obj;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (pointsFirstHalfA != other.pointsFirstHalfA)
            return false;
        if (pointsFirstHalfB != other.pointsFirstHalfB)
            return false;
        if (pointsSecondHalfA != other.pointsSecondHalfA)
            return false;
        if (pointsSecondHalfB != other.pointsSecondHalfB)
            return false;
        return true;
    }

}
