package ats.algo.sport.football;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Geoff
 *
 */
class FootballMatchFacts {
    TeamId nextToScore;
    int currentPeriodGoalsA;
    int currentPeriodGoalsB;
    TeamId nextToCorner;
    int currentPeriodCornersA;
    int currentPeriodCornersB;
    int fiveMinutesSequenceNo;
    int startingPeriodNo;

    // // Five minutes markets
    // private TreeMap<String, String> fiveMinutesGoalMap = new TreeMap<String, String>();
    // private int fiveMinsGoalsA;
    // private int fiveMinsGoalsB;


    void reset(int currentPeriodGoalsA, int currentPeriodGoalsB, int currentPeriodCornersA, int currentPeriodCornersB,
                    int fiveMinutesSequenceNo, int startingPeriodNo) {
        nextToScore = TeamId.UNKNOWN;
        this.currentPeriodGoalsA = currentPeriodGoalsA;
        this.currentPeriodGoalsB = currentPeriodGoalsB;
        nextToCorner = TeamId.UNKNOWN;
        this.currentPeriodCornersA = currentPeriodCornersA;
        this.currentPeriodCornersB = currentPeriodCornersB;
        this.fiveMinutesSequenceNo = fiveMinutesSequenceNo;
        this.startingPeriodNo = startingPeriodNo;
    }


    public int getStartingPeriodNo() {
        return startingPeriodNo;
    }


    public void setStartingPeriodNo(int startingPeriodNo) {
        this.startingPeriodNo = startingPeriodNo;
    }


    public int getFiveMinutesSequenceNo() {
        return fiveMinutesSequenceNo;
    }

    TeamId getNextToScore() {
        return nextToScore;
    }

    void setNextToScore(TeamId nextToScore) {
        this.nextToScore = nextToScore;
    }

    public TeamId getNextToCorner() {
        return nextToCorner;
    }

    public void setNextToCorner(TeamId nextToCorner) {
        this.nextToCorner = nextToCorner;
    }

    public int getCornersTotalCurrentHalf() {

        return currentPeriodCornersA + currentPeriodCornersB;
    }

    public int getGoalsTotalCurrentHalf() {

        return currentPeriodGoalsA + currentPeriodGoalsB;
    }


    public int getCurrentPeriodGoalsA() {
        return currentPeriodGoalsA;
    }


    public void setCurrentPeriodGoalsA(int currentPeriodGoalsA) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
    }


    public int getCurrentPeriodGoalsB() {
        return currentPeriodGoalsB;
    }


    public void setCurrentPeriodGoalsB(int currentPeriodGoalsB) {
        this.currentPeriodGoalsB = currentPeriodGoalsB;
    }

    public int getCurrentPeriodCornersA() {
        return currentPeriodCornersA;
    }


    public void setCurrentPeriodCornersA(int currentPeriodCornersA) {
        this.currentPeriodCornersA = currentPeriodCornersA;
    }


    public int getCurrentPeriodCornersB() {
        return currentPeriodCornersB;
    }


    public void setCurrentPeriodCornersB(int currentPeriodCornersB) {
        this.currentPeriodCornersB = currentPeriodCornersB;
    }


    public void addScoreCurrentPeriod(TeamId teamId) {
        if (teamId == TeamId.A)
            currentPeriodGoalsA++;
        else if (teamId == TeamId.B)
            currentPeriodGoalsB++;
    }

    public void addCornerCurrentPeriod(TeamId teamId) {
        if (teamId == TeamId.A)
            currentPeriodCornersA++;
        else if (teamId == TeamId.B)
            currentPeriodCornersB++;

    }

}
