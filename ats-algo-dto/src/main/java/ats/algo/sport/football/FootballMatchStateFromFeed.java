package ats.algo.sport.football;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchStateFromFeed;
import ats.core.util.json.JsonUtil;

public class FootballMatchStateFromFeed extends MatchStateFromFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    private int goalsA;
    private int goalsB;

    private int yellowCardA;
    private int yellowCardB;

    private int redCardA;
    private int redCardB;

    private int cornersA;
    private int cornersB;

    private FootballMatchPeriod matchPeriod;


    /**
     * 
     * @param goalsA
     * @param goalsB
     * @param cornersA
     * @param cornersB
     * @param yellowCardA
     * @param yellowCardB
     * @param redCardA
     * @param redCardB
     */
    public FootballMatchStateFromFeed(@JsonProperty("goalsA") int goalsA, @JsonProperty("goalsB") int goalsB,
                    @JsonProperty("cornersA") int cornersA, @JsonProperty("cornersB") int cornersB,
                    @JsonProperty("yellowCardA") int yellowCardA, @JsonProperty("yellowCardB") int yellowCardB,
                    @JsonProperty("redCardA") int redCardA, @JsonProperty("redCardB") int redCardB,
                    @JsonProperty("matchPeriod") FootballMatchPeriod matchPeriod) {
        super();
        this.goalsA = goalsA;
        this.goalsB = goalsB;
        this.cornersA = cornersA;
        this.cornersB = cornersB;
        this.yellowCardA = yellowCardA;
        this.yellowCardB = yellowCardB;
        this.redCardA = redCardA;
        this.redCardB = redCardB;
        this.matchPeriod = matchPeriod;

    }


    public FootballMatchStateFromFeed copy() {
        FootballMatchStateFromFeed copied = new FootballMatchStateFromFeed(this.goalsA, this.goalsB, this.cornersA,
                        this.cornersB, this.yellowCardA, this.yellowCardB, this.redCardA, this.redCardB,
                        this.matchPeriod);
        return copied;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public int getCornersA() {
        return cornersA;
    }

    public int getCornersB() {
        return cornersB;
    }

    public int getYellowCardA() {
        return yellowCardA;
    }

    public int getYellowCardB() {
        return yellowCardB;
    }

    public int getRedCardA() {
        return redCardA;
    }

    public int getRedCardB() {
        return redCardB;
    }

    public FootballMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }


    public void setMatchPeriod(FootballMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }


    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }


    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }


    public void setYellowCardA(int yellowCardA) {
        this.yellowCardA = yellowCardA;
    }


    public void setYellowCardB(int yellowCardB) {
        this.yellowCardB = yellowCardB;
    }


    public void setRedCardA(int redCardA) {
        this.redCardA = redCardA;
    }


    public void setRedCardB(int redCardB) {
        this.redCardB = redCardB;
    }


    public void setCornersA(int cornersA) {
        this.cornersA = cornersA;
    }


    public void setCornersB(int cornersB) {
        this.cornersB = cornersB;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cornersA;
        result = prime * result + cornersB;
        result = prime * result + goalsA;
        result = prime * result + goalsB;
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
        result = prime * result + redCardA;
        result = prime * result + redCardB;
        result = prime * result + yellowCardA;
        result = prime * result + yellowCardB;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FootballMatchStateFromFeed other = (FootballMatchStateFromFeed) obj;
        if (cornersA != other.cornersA)
            return false;
        if (cornersB != other.cornersB)
            return false;
        if (goalsA != other.goalsA)
            return false;
        if (goalsB != other.goalsB)
            return false;
        if (matchPeriod != other.matchPeriod)
            return false;
        if (redCardA != other.redCardA)
            return false;
        if (redCardB != other.redCardB)
            return false;
        if (yellowCardA != other.yellowCardA)
            return false;
        if (yellowCardB != other.yellowCardB)
            return false;
        return true;
    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public String toShortString() {
        return "goals=" + goalsA + "-" + goalsB + ", corners=" + cornersA + "-" + cornersB + "yellowCards="
                        + yellowCardA + "-" + yellowCardB + "-" + "redCards=" + redCardA + "-" + redCardB + "-"
                        + "matchPeriod=" + matchPeriod;
    }
}
