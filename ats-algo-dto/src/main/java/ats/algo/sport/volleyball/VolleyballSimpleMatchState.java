package ats.algo.sport.volleyball;


import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class VolleyballSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private int setsA;
    private int setsB;
    private int pointsA;
    private int pointsB;
    private TeamId onServeNow;
    private Map<String, PairOfIntegers> setScores = new LinkedHashMap<String, PairOfIntegers>();

    @JsonCreator
    public VolleyballSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("setsA") int setsA,
                    @JsonProperty("setsB") int setsB, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("onServeNow") TeamId onServeNow,
                    @JsonProperty("setScores") Map<String, PairOfIntegers> setScores) {
        super(preMatch, matchCompleted);
        this.setsA = setsA;
        this.setsB = setsB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.onServeNow = onServeNow;
        this.setScores = setScores;
    }

    public VolleyballSimpleMatchState() {
        super();
        onServeNow = TeamId.UNKNOWN;
    }

    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public void setOnServeNow(TeamId onServeNow) {
        this.onServeNow = onServeNow;
    }

    public void setSetScores(Map<String, PairOfIntegers> setScores) {
        this.setScores = setScores;
    }

    public int getSetsA() {
        return setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    public int getPointsA() {
        return pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public TeamId getOnServeNow() {
        return onServeNow;
    }

    public Map<String, PairOfIntegers> getSetScores() {
        return setScores;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((onServeNow == null) ? 0 : onServeNow.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((setScores == null) ? 0 : setScores.hashCode());
        result = prime * result + setsA;
        result = prime * result + setsB;
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
        VolleyballSimpleMatchState other = (VolleyballSimpleMatchState) obj;
        if (onServeNow != other.onServeNow)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (setScores == null) {
            if (other.setScores != null)
                return false;
        } else if (!setScores.equals(other.setScores))
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        return true;
    }


}

