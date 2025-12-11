package ats.algo.sport.afl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class AflSimpleMatchState extends SimpleMatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int pointsA;
    private int pointsB;
    private int goalsA;
    private int goalsB;
    private int behindsA;
    private int behindsB;
    private AflMatchPeriod aflMatchPeriod;
    private int elapsedTimeSeconds;

    @JsonCreator
    public AflSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("goalsA") int goalsA,
                    @JsonProperty("goalsB") int goalsB, @JsonProperty("behindsA") int behindsA,
                    @JsonProperty("behindsB") int behindsB,
                    @JsonProperty("aflMatchPeriod") AflMatchPeriod aflMatchPeriod,
                    @JsonProperty("elapsedTimeSeconds") int elapsedTimeSeconds) {
        super(preMatch, matchCompleted);
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.behindsA = behindsA;
        this.behindsB = behindsB;
        this.aflMatchPeriod = aflMatchPeriod;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    public AflSimpleMatchState() {
        super();
        aflMatchPeriod = AflMatchPeriod.PREMATCH;
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

    public int getBehindsA() {
        return behindsA;
    }

    public void setBehindsA(int behindsA) {
        this.behindsA = behindsA;
    }

    public int getBehindsB() {
        return behindsB;
    }

    public void setBehindsB(int behindsB) {
        this.behindsB = behindsB;
    }

    public AflMatchPeriod getAflMatchPeriod() {
        return aflMatchPeriod;
    }

    public void setAflMatchPeriod(AflMatchPeriod aflMatchPeriod) {
        this.aflMatchPeriod = aflMatchPeriod;
    }

    public int getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public void setElapsedTimeSeconds(int elapsedTimeSeconds) {
        this.elapsedTimeSeconds = elapsedTimeSeconds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((aflMatchPeriod == null) ? 0 : aflMatchPeriod.hashCode());
        result = prime * result + behindsA;
        result = prime * result + behindsB;
        result = prime * result + elapsedTimeSeconds;
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
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
        AflSimpleMatchState other = (AflSimpleMatchState) obj;
        if (aflMatchPeriod != other.aflMatchPeriod)
            return false;
        if (behindsA != other.behindsA)
            return false;
        if (behindsB != other.behindsB)
            return false;
        if (elapsedTimeSeconds != other.elapsedTimeSeconds)
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        return true;
    }
}

