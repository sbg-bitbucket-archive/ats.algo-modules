package ats.algo.sport.bandy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class BandySimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int goalsA;
    private int goalsB;

    private int goalsFirstHalfA;
    private int goalsFirstHalfB;
    private int goalsSecondHalfA;
    private int goalsSecondHalfB;

    private BandyMatchPeriod matchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public BandySimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") BandyMatchPeriod matchPeriod,
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
        this.goalsFirstHalfB = goalsSecondHalfA;
        this.goalsSecondHalfA = goalsSecondHalfA;
        this.goalsSecondHalfB = goalsSecondHalfB;
    }

    public BandySimpleMatchState() {
        super();
        matchPeriod = BandyMatchPeriod.PREMATCH;
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

    public void setMatchPeriod(BandyMatchPeriod matchPeriod) {
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

    public BandyMatchPeriod getMatchPeriod() {
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
        BandySimpleMatchState other = (BandySimpleMatchState) obj;
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

