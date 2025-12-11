package ats.algo.sport.outrights.modelfitting;

import ats.algo.sport.outrights.calcengine.core.Team;

public class FiveThirtyEightPrediction {

    Team homeTeam;
    Team awayTeam;
    double probHomeWin;
    double probAwayWin;
    double probDraw;
    String date;

    public FiveThirtyEightPrediction(String date, Team homeTeam, Team awayTeam, double probHomeWin, double probAwayWin,
                    double probDraw) {
        super();
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probHomeWin = probHomeWin;
        this.probAwayWin = probAwayWin;
        this.probDraw = probDraw;
        double p = probHomeWin + probAwayWin + probDraw;
        if (Math.abs(p - 1.0) > 0.01001)
            throw new IllegalArgumentException("probs don't sum to one");
    }



    public Team getHomeTeam() {
        return homeTeam;
    }



    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }



    public Team getAwayTeam() {
        return awayTeam;
    }



    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }



    public double getProbHomeWin() {
        return probHomeWin;
    }



    public void setProbHomeWin(double probHomeWin) {
        this.probHomeWin = probHomeWin;
    }



    public double getProbAwayWin() {
        return probAwayWin;
    }



    public void setProbAwayWin(double probAwayWin) {
        this.probAwayWin = probAwayWin;
    }



    public double getProbDraw() {
        return probDraw;
    }



    public void setProbDraw(double probDraw) {
        this.probDraw = probDraw;
    }



    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }



    public FiveThirtyEightPrediction(Team homeTeam, Team awayTeam, double probHomeWin, double probAwayWin,
                    double probDraw) {
        super();
        this.date = "unknown date";
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probHomeWin = probHomeWin;
        this.probAwayWin = probAwayWin;
        this.probDraw = probDraw;
        double p = probHomeWin + probAwayWin + probDraw;
        if (Math.abs(p - 1.0) > 0.01001)
            throw new IllegalArgumentException("probs don't sum to one");
    }



}
