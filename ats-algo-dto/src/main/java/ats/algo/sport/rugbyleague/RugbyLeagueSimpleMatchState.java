package ats.algo.sport.rugbyleague;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class RugbyLeagueSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int pointsA;
    private int pointsB;
    private int elapsedTimeSecs;
    private RugbyLeagueMatchPeriod matchPeriod;

    private int pointsFirstHalfA;
    private int pointsFirstHalfB;
    private int pointsSecondHalfA;
    private int pointsSecondHalfB;

    @JsonCreator
    public RugbyLeagueSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") RugbyLeagueMatchPeriod matchPeriod,
                    @JsonProperty("elapsedTimeSecs") int elapsedTimeSecs, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("pointsFirstHalfA") int pointsFirstHalfA,
                    @JsonProperty("pointsFirstHalfB") int pointsFirstHalfB,
                    @JsonProperty("pointsSecondHalfA") int pointsSecondHalfA,
                    @JsonProperty("pointsSecondHalfB") int pointsSecondHalfB)

    {
        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;
        this.elapsedTimeSecs = elapsedTimeSecs;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.pointsFirstHalfA = pointsFirstHalfA;
        this.pointsFirstHalfB = pointsFirstHalfB;
        this.pointsSecondHalfA = pointsSecondHalfA;
        this.pointsSecondHalfB = pointsSecondHalfB;
    }



    public RugbyLeagueSimpleMatchState() {
        super();
        matchPeriod = RugbyLeagueMatchPeriod.PREMATCH;
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

    public RugbyLeagueMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(RugbyLeagueMatchPeriod matchPeriod) {
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
        RugbyLeagueSimpleMatchState other = (RugbyLeagueSimpleMatchState) obj;
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
