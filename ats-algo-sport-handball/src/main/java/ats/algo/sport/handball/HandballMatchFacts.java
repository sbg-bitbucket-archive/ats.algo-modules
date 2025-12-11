package ats.algo.sport.handball;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Geoff
 *
 */
class HandballMatchFacts {
    TeamId nextToScore;
    int currentPeriodGoalsA;
    int currentPeriodGoalsB;

    void reset(int currentPeriodGoalsA, int currentPeriodGoalsB) {
        nextToScore = TeamId.UNKNOWN;
        this.currentPeriodGoalsA = currentPeriodGoalsA;
        this.currentPeriodGoalsB = currentPeriodGoalsB;
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
