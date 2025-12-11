package ats.algo.sport.rugbyleague;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Jin
 * 
 */
class RugbyLeagueMatchFacts {
    int currentPeriodTrysA;
    int currentPeriodTrysB;
    int currentPeriodPointsA;
    int currentPeriodPointsB;
    TeamId nextPoint;
    TeamId nextToScoreTry;
    TeamId nextToScoreDropGoal;

    void reset(int currentPeriodPointsA, int currentPeriodPointsB, int currentPeriodTrysA, int currentPeriodTrysB) {
        this.currentPeriodTrysA = currentPeriodTrysA;
        this.currentPeriodTrysB = currentPeriodTrysB;
        this.currentPeriodPointsA = currentPeriodPointsA;
        this.currentPeriodPointsB = currentPeriodPointsB;
        nextToScoreTry = TeamId.UNKNOWN;
        nextToScoreDropGoal = TeamId.UNKNOWN;
        nextPoint = TeamId.UNKNOWN;
    }

    public TeamId getNextToScoreTry() {
        return nextToScoreTry;
    }

    public void setNextToScoreTry(TeamId nextToScoreGoal) {
        this.nextToScoreTry = nextToScoreGoal;
    }

    public TeamId getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(TeamId nextPoint) {
        this.nextPoint = nextPoint;
    }

    public TeamId getNextToScoreDropGoal() {
        return nextToScoreDropGoal;
    }

    public void setNextToScoreDropGoal(TeamId nextToScoreBehind) {
        this.nextToScoreDropGoal = nextToScoreBehind;
    }

    public int getCurrentPeriodTrysA() {

        return currentPeriodTrysA;
    }

    public int getCurrentPeriodTrysB() {

        return currentPeriodTrysB;
    }

    public int getCurrentPeriodPointsA() {
        return currentPeriodPointsA;
    }

    public int getCurrentPeriodPointsB() {
        return currentPeriodPointsB;
    }

    public int getTrysTotalCurrentPeriod() {

        return currentPeriodTrysA + currentPeriodTrysB;
    }

    public int getPointsTotalCurrentPeriod() {
        return currentPeriodPointsA + currentPeriodPointsB;
    }

    public boolean getBothTeamToScoreCurrentPeriod() {

        return (currentPeriodPointsA > 0 && currentPeriodPointsB > 0);
    }

    public TeamId getWinnerCurrentPeriod() {
        if (currentPeriodPointsA > currentPeriodPointsB) {
            return TeamId.A;
        } else if (currentPeriodPointsA < currentPeriodPointsB) {
            return TeamId.B;
        } else {
            return TeamId.UNKNOWN;
        }
    }

    public void addPointsCurrentPeriod(TeamId teamId, int i) {
        if (teamId == TeamId.A) {
            if (i == 3) {
                currentPeriodPointsA += 3;
            } else {
                currentPeriodTrysA++;
                currentPeriodPointsA += 5;
            }
        } else if (teamId == TeamId.B) {
            if (i == 3) {
                currentPeriodPointsB += 3;
            } else {
                currentPeriodTrysB++;
                currentPeriodPointsB += 5;
            }
        }

        if (nextPoint == TeamId.UNKNOWN)
            nextPoint = teamId;
    }

}
