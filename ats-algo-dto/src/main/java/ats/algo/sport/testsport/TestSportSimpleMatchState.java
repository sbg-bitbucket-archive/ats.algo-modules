package ats.algo.sport.testsport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class TestSportSimpleMatchState extends SimpleMatchState {


    private static final long serialVersionUID = 1L;

    private int scoreA;
    private int scoreB;

    @JsonCreator
    public TestSportSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("scoreA") int scoreA,
                    @JsonProperty("scoreB") int scoreB) {
        super(preMatch, matchCompleted);
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }



}
