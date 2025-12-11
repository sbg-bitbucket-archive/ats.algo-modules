package ats.algo.sport.fantasyexample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class FantasyExampleSportSimpleMatchState extends SimpleMatchState {


    private static final long serialVersionUID = 1L;

    private int goals;
    private int yellowCards;
    private int ptsPlayer1;
    private int ptsPlayer2;
    private int ptsPlayer3;


    @JsonCreator
    public FantasyExampleSportSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted, @JsonProperty("goals") int goals,
                    @JsonProperty("yellowCards") int yellowCards, @JsonProperty("ptsPlayer1") int ptsPlayer1,
                    @JsonProperty("ptsPlayer2") int ptsPlayer2, @JsonProperty("ptsPlayer3") int ptsPlayer3) {
        super(preMatch, matchCompleted);
        this.goals = goals;
        this.yellowCards = yellowCards;
        this.ptsPlayer1 = ptsPlayer1;
        this.ptsPlayer2 = ptsPlayer2;
        this.ptsPlayer3 = ptsPlayer3;
    }


    public int getGoals() {
        return goals;
    }


    public int getYellowCards() {
        return yellowCards;
    }


    public int getPtsPlayer1() {
        return ptsPlayer1;
    }


    public int getPtsPlayer2() {
        return ptsPlayer2;
    }


    public int getPtsPlayer3() {
        return ptsPlayer3;
    }



}
