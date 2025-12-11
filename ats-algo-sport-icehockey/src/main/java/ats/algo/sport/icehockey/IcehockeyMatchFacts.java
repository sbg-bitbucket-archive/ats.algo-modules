package ats.algo.sport.icehockey;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Geoff
 * 
 */
class IcehockeyMatchFacts {
    TeamId nextToScore;
    int currentPeriodGoalsA;
    int currentPeriodGoalsB;
    int currentFiveMinsEnder;
    int fiveMinsGoalsA;
    int fiveMinsGoalsB;
    TeamId firstTeamToScore;

    void reset(int currentPeriodGoalsA, int currentPeriodGoalsB, int currentFiveMinsEnder, int fiveMinsGoalsA,
                    int fiveMinsGoalsB) {
        nextToScore = TeamId.UNKNOWN;
        this.currentPeriodGoalsA = currentPeriodGoalsA;
        this.currentPeriodGoalsB = currentPeriodGoalsB;
        this.currentFiveMinsEnder = currentFiveMinsEnder;
        this.fiveMinsGoalsA = fiveMinsGoalsA;
        this.fiveMinsGoalsB = fiveMinsGoalsB;
        this.firstTeamToScore = TeamId.UNKNOWN;
    }

    TeamId getNextToScore() {
        return nextToScore;
    }

    void setNextToScore(TeamId nextToScore) {
        this.nextToScore = nextToScore;
    }

    public int getCurrentPeriodGoalsA() {

        return currentPeriodGoalsA;
    }

    public int getCurrentPeriodGoalsB() {

        return currentPeriodGoalsB;
    }



    public int getCurrentFiveMinsEnder() {
        return currentFiveMinsEnder;
    }

    public void setCurrentFiveMinsEnder(int currentFiveMinsEnder) {
        this.currentFiveMinsEnder = currentFiveMinsEnder;
    }

    public int getFiveMinsGoalsA() {
        return fiveMinsGoalsA;
    }

    public void addFiveMinsGoalsA() {
        this.fiveMinsGoalsA++;
    }

    public int getFiveMinsGoalsB() {
        return fiveMinsGoalsB;
    }

    public void addFiveMinsGoalsB() {
        this.fiveMinsGoalsB++;
    }

    public int getGoalsTotalCurrentPeriod() {

        return currentPeriodGoalsA + currentPeriodGoalsB;
    }

    public boolean getBothTeamToScoreCurrentPeriod() {

        return (currentPeriodGoalsA > 0 && currentPeriodGoalsB > 0);
    }

    public TeamId getWinnerCurrentPeriod() {
        if (currentPeriodGoalsA > currentPeriodGoalsB) {
            return TeamId.A;
        } else if (currentPeriodGoalsA < currentPeriodGoalsB) {
            return TeamId.B;
        } else {
            return TeamId.UNKNOWN;
        }
    }

    public void addScoreCurrentPeriod(TeamId teamId) {
        if (teamId == TeamId.A)
            currentPeriodGoalsA++;
        else if (teamId == TeamId.B)
            currentPeriodGoalsB++;

    }

}
