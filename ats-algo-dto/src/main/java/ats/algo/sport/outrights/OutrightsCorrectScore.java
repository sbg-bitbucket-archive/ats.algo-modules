package ats.algo.sport.outrights;

import java.util.HashMap;
import java.util.Map;

import ats.algo.genericsupportfunctions.GCMath;

public class OutrightsCorrectScore {

    private Map<String, Double> correctScore;

    public OutrightsCorrectScore() {
        correctScore = new HashMap<>();
    }

    /**
     * adds a correct score selection to the grid
     * 
     * @param selection expected to be of the form "n-m"
     * @param prob
     */
    public void addSelection(String selection, double prob) {
        correctScore.put(selection, GCMath.round(prob, 3));
    }

    public Map<String, Double> getCorrectScore() {
        return correctScore;
    }

    public void setCorrectScore(Map<String, Double> correctScore) {
        this.correctScore = correctScore;
    }



}
