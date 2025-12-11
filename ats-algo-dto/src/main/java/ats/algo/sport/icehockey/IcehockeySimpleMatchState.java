package ats.algo.sport.icehockey;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class IcehockeySimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int goalsA;
    private int goalsB;

    private int goalsFirstPeriodA;
    private int goalsFirstPeriodB;
    private int goalsSecondPeriodA;
    private int goalsSecondPeriodB;
    private int goalsThirdPeriodA;
    private int goalsThirdPeriodB;

    private int minorSinBinA;
    private int majorSinBinA;
    private int minorSinBinB;
    private int majorSinBinB;

    private IcehockeyMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public IcehockeySimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") IcehockeyMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("goalsFirstPeriodA") int goalsFirstPeriodA,
                    @JsonProperty("goalsFirstPeriodB") int goalsFirstPeriodB,
                    @JsonProperty("goalsSecondPeriodA") int goalsSecondPeriodA,
                    @JsonProperty("goalsSecondPeriodB") int goalsSecondPeriodB,
                    @JsonProperty("goalsThirdPeriodA") int goalsThirdPeriodA,
                    @JsonProperty("goalsThirdPeriodB") int goalsThirdPeriodB,
                    @JsonProperty("minorSinBinA") int minorSinBinA, @JsonProperty("majorSinBinA") int majorSinBinA,
                    @JsonProperty("minorSinBinB") int minorSinBinB, @JsonProperty("majorSinBinB") int majorSinBinB) {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.goalsFirstPeriodA = goalsFirstPeriodA;
        this.goalsFirstPeriodB = goalsFirstPeriodB;
        this.goalsSecondPeriodA = goalsSecondPeriodA;
        this.goalsSecondPeriodB = goalsSecondPeriodB;
        this.goalsThirdPeriodA = goalsThirdPeriodA;
        this.goalsThirdPeriodB = goalsThirdPeriodB;
        this.minorSinBinA = minorSinBinA;
        this.majorSinBinA = majorSinBinA;
        this.minorSinBinB = minorSinBinB;
        this.majorSinBinB = majorSinBinB;
    }

    public IcehockeySimpleMatchState() {
        super();
        matchPeriod = IcehockeyMatchPeriod.PREMATCH;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    public int getGoalsFirstPeriodA() {
        return goalsFirstPeriodA;
    }

    public void setGoalsFirstPeriodA(int goalsFirstPeriodA) {
        this.goalsFirstPeriodA = goalsFirstPeriodA;
    }

    public int getGoalsFirstPeriodB() {
        return goalsFirstPeriodB;
    }

    public void setGoalsFirstPeriodB(int goalsFirstPeriodB) {
        this.goalsFirstPeriodB = goalsFirstPeriodB;
    }

    public int getGoalsSecondPeriodA() {
        return goalsSecondPeriodA;
    }

    public void setGoalsSecondPeriodA(int goalsSecondPeriodA) {
        this.goalsSecondPeriodA = goalsSecondPeriodA;
    }

    public int getGoalsSecondPeriodB() {
        return goalsSecondPeriodB;
    }

    public void setGoalsSecondPeriodB(int goalsSecondPeriodB) {
        this.goalsSecondPeriodB = goalsSecondPeriodB;
    }

    public int getGoalsThirdPeriodA() {
        return goalsThirdPeriodA;
    }

    public void setGoalsThirdPeriodA(int goalsThirdPeriodA) {
        this.goalsThirdPeriodA = goalsThirdPeriodA;
    }

    public int getGoalsThirdPeriodB() {
        return goalsThirdPeriodB;
    }

    public void setGoalsThirdPeriodB(int goalsThirdPeriodB) {
        this.goalsThirdPeriodB = goalsThirdPeriodB;
    }

    public IcehockeyMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(IcehockeyMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    public int getMinorSinBinA() {
        return minorSinBinA;
    }

    public void setMinorSinBinA(int minorSinBinA) {
        this.minorSinBinA = minorSinBinA;
    }

    public int getMajorSinBinA() {
        return majorSinBinA;
    }

    public void setMajorSinBinA(int majorSinBinA) {
        this.majorSinBinA = majorSinBinA;
    }

    public int getMinorSinBinB() {
        return minorSinBinB;
    }

    public void setMinorSinBinB(int minorSinBinB) {
        this.minorSinBinB = minorSinBinB;
    }

    public int getMajorSinBinB() {
        return majorSinBinB;
    }

    public void setMajorSinBinB(int majorSinBinB) {
        this.majorSinBinB = majorSinBinB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + goalsFirstPeriodA;
        result = prime * result + goalsFirstPeriodB;
        result = prime * result + goalsSecondPeriodA;
        result = prime * result + goalsSecondPeriodB;
        result = prime * result + goalsThirdPeriodA;
        result = prime * result + goalsThirdPeriodB;
        result = prime * result + majorSinBinA;
        result = prime * result + majorSinBinB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + minorSinBinA;
        result = prime * result + minorSinBinB;
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
        IcehockeySimpleMatchState other = (IcehockeySimpleMatchState) obj;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (goalsFirstPeriodA != other.goalsFirstPeriodA)
            return false;
        if (goalsFirstPeriodB != other.goalsFirstPeriodB)
            return false;
        if (goalsSecondPeriodA != other.goalsSecondPeriodA)
            return false;
        if (goalsSecondPeriodB != other.goalsSecondPeriodB)
            return false;
        if (goalsThirdPeriodA != other.goalsThirdPeriodA)
            return false;
        if (goalsThirdPeriodB != other.goalsThirdPeriodB)
            return false;
        if (majorSinBinA != other.majorSinBinA)
            return false;
        if (majorSinBinB != other.majorSinBinB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (minorSinBinA != other.minorSinBinA)
            return false;
        if (minorSinBinB != other.minorSinBinB)
            return false;
        return true;
    }



}

