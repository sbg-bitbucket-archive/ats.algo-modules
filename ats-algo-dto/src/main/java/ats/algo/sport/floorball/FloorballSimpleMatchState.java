package ats.algo.sport.floorball;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class FloorballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int goalsA;
    private int goalsB;

    private int goalsFirstPeriodA;
    private int goalsFirstPeriodB;
    private int goalsSecondPeriodA;
    private int goalsSecondPeriodB;
    private int goalsThirdPeriodA;
    private int goalsThirdPeriodB;

    private FloorballMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public FloorballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") FloorballMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("goalsFirstPeriodA") int goalsFirstPeriodA,
                    @JsonProperty("goalsFirstPeriodB") int goalsFirstPeriodB,
                    @JsonProperty("goalsSecondPeriodA") int goalsSecondPeriodA,
                    @JsonProperty("goalsSecondPeriodB") int goalsSecondPeriodB,
                    @JsonProperty("goalsThirdPeriodA") int goalsThirdPeriodA,
                    @JsonProperty("goalsThirdPeriodB") int goalsThirdPeriodB) {
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
    }

    public FloorballSimpleMatchState() {
        super();
        matchPeriod = FloorballMatchPeriod.PREMATCH;
    }

    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }

    public void setGoalsFirstPeriodA(int goalsFirstPeriodA) {
        this.goalsFirstPeriodA = goalsFirstPeriodA;
    }

    public void setGoalsFirstPeriodB(int goalsFirstPeriodB) {
        this.goalsFirstPeriodB = goalsFirstPeriodB;
    }

    public void setGoalsSecondPeriodA(int goalsSecondPeriodA) {
        this.goalsSecondPeriodA = goalsSecondPeriodA;
    }

    public void setGoalsSecondPeriodB(int goalsSecondPeriodB) {
        this.goalsSecondPeriodB = goalsSecondPeriodB;
    }

    public void setGoalsThirdPeriodA(int goalsThirdPeriodA) {
        this.goalsThirdPeriodA = goalsThirdPeriodA;
    }

    public void setGoalsThirdPeriodB(int goalsThirdPeriodB) {
        this.goalsThirdPeriodB = goalsThirdPeriodB;
    }

    public void setMatchPeriod(FloorballMatchPeriod matchPeriod) {
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

    public FloorballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public int getGoalsFirstPeriodA() {
        return goalsFirstPeriodA;
    }

    public int getGoalsFirstPeriodB() {
        return goalsFirstPeriodB;
    }

    public int getGoalsSecondPeriodA() {
        return goalsSecondPeriodA;
    }

    public int getGoalsSecondPeriodB() {
        return goalsSecondPeriodB;
    }

    public int getGoalsThirdPeriodA() {
        return goalsThirdPeriodA;
    }

    public int getGoalsThirdPeriodB() {
        return goalsThirdPeriodB;
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
        FloorballSimpleMatchState other = (FloorballSimpleMatchState) obj;
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
        if (matchPeriod != other.matchPeriod)
            return false;
        return true;
    }
}

