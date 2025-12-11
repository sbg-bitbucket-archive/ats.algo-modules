package ats.algo.sport.basketball;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Geoff
 * 
 */
class BasketballMatchFacts {
    int currentPeriodGoalsA;
    int currentPeriodGoalsB;
    int currentPeriodNo;

    void reset(int currentPeriodGoalsA, int currentPeriodGoalsB, int currentPeriodNo) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
        this.currentPeriodGoalsB = currentPeriodGoalsB;
        this.currentPeriodNo = currentPeriodNo;
    }



    public int getCurrentPeriodNo() {
        return currentPeriodNo;
    }

    public void setCurrentPeriodNo(int currentPeriodNo) {
        this.currentPeriodNo = currentPeriodNo;
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

    // added by Robin to cater for multiple points moves
    public void addScoreCurrentPeriod(TeamId teamId, int points) {
        if (teamId == TeamId.A)
            currentPeriodGoalsA += points;
        else if (teamId == TeamId.B)
            currentPeriodGoalsB += points;

    }

}
