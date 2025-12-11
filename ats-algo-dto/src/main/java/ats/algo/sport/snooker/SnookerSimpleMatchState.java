package ats.algo.sport.snooker;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;

public class SnookerSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private String info;
    private int framesA;
    private int framesB;
    private TeamId serve;
    private TeamId serveInFirstSet;
    private Map<String, PairOfIntegers> frameScoreInMatchN;
    private int pointsA;
    private int pointsB;

    /**
     * 
     * @param preMatch
     * @param matchCompleted
     * @param framesA
     * @param framesB
     * @param pointsA
     * @param pointsB
     * @param serve
     * @param serveInFirstSet
     * @param frameScoreInMatchN
     */

    @JsonCreator
    public SnookerSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("framesA") int framesA,
                    @JsonProperty("framesB") int framesB, @JsonProperty("pointsA") int pointsA,
                    @JsonProperty("pointsB") int pointsB, @JsonProperty("serve") TeamId serve,
                    @JsonProperty("serveInFirstSet") TeamId serveInFirstSet,
                    @JsonProperty("frameScoreInMatchN") Map<String, PairOfIntegers> frameScoreInMatchN) {
        super(preMatch, matchCompleted);

        this.framesA = framesA;
        this.framesB = framesB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.serve = serve;
        this.serveInFirstSet = serveInFirstSet;
        this.frameScoreInMatchN = frameScoreInMatchN;
    }

    public SnookerSimpleMatchState() {
        super();
        frameScoreInMatchN = Maps.newHashMap();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFramesA() {
        return framesA;
    }

    public void setFramesA(int framesA) {
        this.framesA = framesA;
    }

    public int getFramesB() {
        return framesB;
    }

    public void setFramesB(int framesB) {
        this.framesB = framesB;
    }

    public TeamId getServe() {
        return serve;
    }

    public void setServe(TeamId serve) {
        this.serve = serve;
    }

    public TeamId getServeInFirstSet() {
        return serveInFirstSet;
    }

    public void setServeInFirstSet(TeamId serveInFirstSet) {
        this.serveInFirstSet = serveInFirstSet;
    }

    public Map<String, PairOfIntegers> getFrameScoreInMatchN() {
        return frameScoreInMatchN;
    }

    public void setFrameScoreInMatchN(Map<String, PairOfIntegers> frameScoreInMatchN) {
        this.frameScoreInMatchN = frameScoreInMatchN;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((frameScoreInMatchN == null) ? 0 : frameScoreInMatchN.hashCode());
        result = prime * result + framesA;
        result = prime * result + framesB;
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + ((serve == null) ? 0 : serve.hashCode());
        result = prime * result + ((serveInFirstSet == null) ? 0 : serveInFirstSet.hashCode());
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
        SnookerSimpleMatchState other = (SnookerSimpleMatchState) obj;
        if (frameScoreInMatchN == null) {
            if (other.frameScoreInMatchN != null)
                return false;
        } else if (!frameScoreInMatchN.equals(other.frameScoreInMatchN))
            return false;
        if (framesA != other.framesA)
            return false;
        if (framesB != other.framesB)
            return false;
        if (info == null) {
            if (other.info != null)
                return false;
        } else if (!info.equals(other.info))
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (serve != other.serve)
            return false;
        if (serveInFirstSet != other.serveInFirstSet)
            return false;
        return true;
    }
}

