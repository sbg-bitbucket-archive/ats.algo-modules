package ats.algo.sport.americanfootball;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Jin
 * 
 */
class AmericanfootballMatchFacts {
    int currentPeriodTrysA;
    int currentPeriodTrysB;
    int currentPeriodPointsA;
    int currentPeriodPointsB;
    int comparePointsA;
    int comparePointsB;

    TeamId nextPoint;
    TeamId nextToScoreTry;
    TeamId nextToScoreDropGoal;
    String halfIndicator;

    void reset(int currentPeriodPointsA, int currentPeriodPointsB, int currentPeriodTrysA, int currentPeriodTrysB,
                    String halfIndicator, int comparePA, int comparePB) {
        this.currentPeriodTrysA = currentPeriodTrysA;
        this.currentPeriodTrysB = currentPeriodTrysB;
        this.currentPeriodPointsA = currentPeriodPointsA;
        this.currentPeriodPointsB = currentPeriodPointsB;
        nextToScoreTry = TeamId.UNKNOWN;
        nextToScoreDropGoal = TeamId.UNKNOWN;
        nextPoint = TeamId.UNKNOWN;
        this.halfIndicator = halfIndicator;
        this.comparePointsA = comparePA;
        this.comparePointsB = comparePB;
    }



    public int getComparePointsA() {
        return comparePointsA;
    }



    public int getComparePointsB() {
        return comparePointsB;
    }



    public String getHalfIndicator() {
        return halfIndicator;
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
            } else if (i == 6) {
                currentPeriodTrysA++;
                currentPeriodPointsA += 6;
            }
        } else if (teamId == TeamId.B) {
            if (i == 3) {
                currentPeriodPointsB += 3;
            } else if (i == 6) {
                currentPeriodTrysB++;
                currentPeriodPointsB += 6;
            }
        }

        if (nextPoint == TeamId.UNKNOWN)
            nextPoint = teamId;
    }

    public void addPointsCompare(TeamId teamId, int i) {
        if (teamId == TeamId.A) {
            comparePointsA += i;
        } else if (teamId == TeamId.B) {
            comparePointsB += i;
        }
    }

}
