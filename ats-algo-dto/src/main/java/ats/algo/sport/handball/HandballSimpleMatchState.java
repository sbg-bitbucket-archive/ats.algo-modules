package ats.algo.sport.handball;



import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;

public class HandballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int goalsA;
    private int goalsB;

    private int goalsFirstHalfA;
    private int goalsFirstHalfB;
    private int goalsSecondHalfA;
    private int goalsSecondHalfB;

    private HandballMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public HandballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") HandballMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("goalsFirstHalfA") int goalsFirstHalfA,
                    @JsonProperty("goalsFirstHalfB") int goalsFirstHalfB,
                    @JsonProperty("goalsSecondHalfA") int goalsSecondHalfA,
                    @JsonProperty("goalsSecondHalfB") int goalsSecondHalfB) {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.goalsFirstHalfA = goalsFirstHalfA;
        this.goalsFirstHalfB = goalsFirstHalfB;
        this.goalsSecondHalfA = goalsSecondHalfA;
        this.goalsSecondHalfB = goalsSecondHalfB;
    }

    public HandballSimpleMatchState() {
        super();
        matchPeriod = HandballMatchPeriod.PREMATCH;
    }

    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    public void setGoalsFirstHalfA(int goalsFirstHalfA) {
        this.goalsFirstHalfA = goalsFirstHalfA;
    }

    public void setGoalsFirstHalfB(int goalsFirstHalfB) {
        this.goalsFirstHalfB = goalsFirstHalfB;
    }

    public void setGoalsSecondHalfA(int goalsSecondHalfA) {
        this.goalsSecondHalfA = goalsSecondHalfA;
    }

    public void setGoalsSecondHalfB(int goalsSecondHalfB) {
        this.goalsSecondHalfB = goalsSecondHalfB;
    }

    public void setMatchPeriod(HandballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public HandballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public int getGoalsFirstHalfA() {
        return goalsFirstHalfA;
    }


    public int getGoalsFirstHalfB() {
        return goalsFirstHalfB;
    }

    public int getGoalsSecondHalfA() {
        return goalsSecondHalfA;
    }

    public int getGoalsSecondHalfB() {
        return goalsSecondHalfB;
    }

    @Override
    public MatchResultMap generateResultMapFromSimpleMatchState(MatchFormat format) {
        MatchResultMap resultMap = new MatchResultMap();
        HandballMatchFormat handballFormat = (HandballMatchFormat) format;

        if ((getGoalsFirstHalfA() > (-1)) && (getGoalsFirstHalfB() > (-1))) {
            String goalsAFH = Integer.toString(getGoalsFirstHalfA());
            String goalsBFH = Integer.toString(getGoalsFirstHalfB());
            resultMap.put("firstHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsAFH + "-" + goalsBFH));
        }

        if ((getGoalsSecondHalfA() > (-1)) && (getGoalsSecondHalfB() > (-1))) {
            String goalsASH = Integer.toString(getGoalsSecondHalfA());
            String goalsBSH = Integer.toString(getGoalsSecondHalfB());
            resultMap.put("secondHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50,
                            goalsASH + "-" + goalsBSH));
        }

        if ((getGoalsA() > (-1)) && (getGoalsB() > (-1)) && handballFormat.getExtraTimeMinutes() > 0) {
            String goalsA = Integer.toString(getGoalsA() - (getGoalsSecondHalfA() + getGoalsFirstHalfA()));
            String goalsB = Integer.toString(getGoalsB() - (getGoalsSecondHalfB() + getGoalsFirstHalfB()));
            resultMap.put("extraTimeGoals",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, goalsA + "-" + goalsB));
        } else {
            resultMap.put("extraTimeGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        }

        return resultMap;
    }

    @Override
    public HandballSimpleMatchState generateMatchStateFromMatchResultMap(MatchResultMap result,
                    MatchFormat matchFormat) {
        HandballMatchState fms = new HandballMatchState(matchFormat);

        Map<String, MatchResultElement> map = result.getMap();

        boolean fHalfGoals = map.get("firstHalfGoals") != null;
        boolean sHalfGoals = map.get("secondHalfGoals") != null;
        boolean eTGoals = map.get("extraTimeGoals") != null;

        if (fHalfGoals && sHalfGoals) {
            if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {
                int firstHalfGoalsA = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives().A;
                int firstHalfGoalsB = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives().B;

                int secondHalfGoalsA = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives().A;
                int secondHalfGoalsB = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives().B;

                if (eTGoals) {
                    int extraTimeGoalsA = map.get("extraTimeGoals").valueAsPairOfIntegersCheckingNegatives().A;
                    int extraTimeGoalsB = map.get("extraTimeGoals").valueAsPairOfIntegersCheckingNegatives().B;

                    fms.setGoalsA(firstHalfGoalsA + secondHalfGoalsA + extraTimeGoalsA);
                    fms.setGoalsB(firstHalfGoalsB + secondHalfGoalsB + extraTimeGoalsB);
                } else {
                    fms.setGoalsA(firstHalfGoalsA + secondHalfGoalsA);
                    fms.setGoalsB(firstHalfGoalsB + secondHalfGoalsB);
                }
            }
        }

        fms.setMatchPeriod(HandballMatchPeriod.MATCH_COMPLETED);
        HandballSimpleMatchState sms = fms.generateSimpleMatchState();
        return sms;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + goalsFirstHalfA;
        result = prime * result + goalsFirstHalfB;
        result = prime * result + goalsSecondHalfA;
        result = prime * result + goalsSecondHalfB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
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
        HandballSimpleMatchState other = (HandballSimpleMatchState) obj;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (goalsFirstHalfA != other.goalsFirstHalfA)
            return false;
        if (goalsFirstHalfB != other.goalsFirstHalfB)
            return false;
        if (goalsSecondHalfA != other.goalsSecondHalfA)
            return false;
        if (goalsSecondHalfB != other.goalsSecondHalfB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        return true;
    }
}

