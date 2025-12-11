package ats.algo.sport.fieldhockey;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class FieldhockeySimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;

    private int goalsA;
    private int goalsB;
    private int elapsedTimeSecs;
    private FieldhockeyMatchPeriod matchPeriod;

    private int goalsFirstHalfA;
    private int goalsFirstHalfB;
    private int goalsSecondHalfA;
    private int goalsSecondHalfB;

    @JsonCreator
    public FieldhockeySimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") FieldhockeyMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSecs") int elapsedTimeSecs, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("goalsFirstHalfA") int goalsFirstHalfA,
                    @JsonProperty("goalsFirstHalfB") int goalsFirstHalfB,
                    @JsonProperty("goalsSecondHalfA") int goalsSecondHalfA,
                    @JsonProperty("goalsSecondHalfB") int goalsSecondHalfB) {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.goalsFirstHalfA = goalsFirstHalfA;
        this.goalsFirstHalfB = goalsFirstHalfB;
        this.goalsSecondHalfA = goalsSecondHalfA;
        this.goalsSecondHalfB = goalsSecondHalfB;
    }

    public FieldhockeySimpleMatchState() {
        super();
        matchPeriod = FieldhockeyMatchPeriod.PREMATCH;
    }



    public FieldhockeySimpleMatchState(boolean preMatch, boolean matchCompleted) {
        super(preMatch, matchCompleted);
        // TODO Auto-generated constructor stub
    }



    public FieldhockeySimpleMatchState(Source source, boolean preMatch, boolean matchCompleted) {
        super(source, preMatch, matchCompleted);
        // TODO Auto-generated constructor stub
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

    public int getElapsedTimeSecs() {
        return elapsedTimeSecs;
    }

    public void setElapsedTimeSecs(int elapsedTimeSecs) {
        this.elapsedTimeSecs = elapsedTimeSecs;
    }

    public FieldhockeyMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(FieldhockeyMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    public int getGoalsFirstHalfA() {
        return goalsFirstHalfA;
    }

    public void setGoalsFirstHalfA(int goalsFirstHalfA) {
        this.goalsFirstHalfA = goalsFirstHalfA;
    }

    public int getGoalsFirstHalfB() {
        return goalsFirstHalfB;
    }

    public void setGoalsFirstHalfB(int goalsFirstHalfB) {
        this.goalsFirstHalfB = goalsFirstHalfB;
    }

    public int getGoalsSecondHalfA() {
        return goalsSecondHalfA;
    }

    public void setGoalsSecondHalfA(int goalsSecondHalfA) {
        this.goalsSecondHalfA = goalsSecondHalfA;
    }

    public int getGoalsSecondHalfB() {
        return goalsSecondHalfB;
    }

    public void setGoalsSecondHalfB(int goalsSecondHalfB) {
        this.goalsSecondHalfB = goalsSecondHalfB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + elapsedTimeSecs;
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
        FieldhockeySimpleMatchState other = (FieldhockeySimpleMatchState) obj;
        if (elapsedTimeSecs != other.elapsedTimeSecs)
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
