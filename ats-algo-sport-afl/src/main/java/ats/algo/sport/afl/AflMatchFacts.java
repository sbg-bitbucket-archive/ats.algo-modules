package ats.algo.sport.afl;

import ats.algo.core.common.TeamId;

/**
 * container for data collected during the simulation of a match
 * 
 * @author Jin
 * 
 */
class AflMatchFacts {
    int currentPeriodNo;
    int currentPeriodGoalsA;
    int currentPeriodGoalsB;
    int currentPeriodBehindsA;
    int currentPeriodBehindsB;
    int currentPeriodPointsA;
    int currentPeriodPointsB;
    int currentOddEvenMarketForQuarterNo;
    int currentGoalsA;
    int currentGoalsB;
    int currentBehindsA;
    int currentBehindsB;
    int timeOfFirstGoal;
    int timeOfFirstGoalQuarter; // recored

    AflMatchPeriod currentMatchPeriod;

    TeamId currentFirstScoreToTen;
    TeamId currentFirstScoreToFifteen;
    TeamId currentFirstScoreToTwenty;
    TeamId nextPoint;
    TeamId nextToScoreGoal;
    TeamId nextToScoreBehind;

    TeamId firstTo3B;
    TeamId firstTo4B;
    TeamId firstTo5B;
    TeamId firstTo6B;

    TeamId firstTo3G;
    TeamId firstTo4G;
    TeamId firstTo5G;
    TeamId firstTo6G;



    void reset(int currentPeriodPointsA, int currentPeriodPointsB, int currentPeriodGoalsA, int currentPeriodGoalsB,
                    int currentPeriodNo, int currentOddEvenMarketForQuarterNo, TeamId currentFirstScoreToTen,
                    TeamId currentFirstScoreToFifteen, TeamId currentFirstScoreToTwenty, AflMatchPeriod matchPeriod,
                    int timeOfFirstGoal, int timeOfFirstGoalQuarter) {
        this.currentPeriodGoalsA = currentPeriodGoalsA;
        this.currentPeriodGoalsB = currentPeriodGoalsB;
        this.currentPeriodBehindsA = currentPeriodPointsA - 6 * currentPeriodGoalsA;
        this.currentPeriodBehindsB = currentPeriodPointsB - 6 * currentPeriodGoalsB;
        this.currentPeriodPointsA = currentPeriodPointsA;
        this.currentPeriodPointsB = currentPeriodPointsB;
        nextToScoreGoal = TeamId.UNKNOWN;
        nextToScoreBehind = TeamId.UNKNOWN;
        nextPoint = TeamId.UNKNOWN;
        this.currentFirstScoreToTen = currentFirstScoreToTen;
        this.currentFirstScoreToFifteen = currentFirstScoreToFifteen;
        this.currentFirstScoreToTwenty = currentFirstScoreToTwenty;
        this.currentPeriodNo = currentPeriodNo;
        this.currentOddEvenMarketForQuarterNo = currentOddEvenMarketForQuarterNo;
        this.currentMatchPeriod = matchPeriod;
        this.timeOfFirstGoal = timeOfFirstGoal;
        this.timeOfFirstGoalQuarter = timeOfFirstGoalQuarter;
    }


    public int getTimeOfFirstGoalQuarter() {
        return timeOfFirstGoalQuarter;
    }

    public void setTimeOfFirstGoalQuarter(int timeOfFirstGoalQuarter) {
        this.timeOfFirstGoalQuarter = timeOfFirstGoalQuarter;
    }

    public AflMatchPeriod getCurrentMatchPeriod() {
        return currentMatchPeriod;
    }

    public int getTimeOfFirstGoal() {
        return timeOfFirstGoal;
    }

    public void setTimeOfFirstGoal(int timeOfFirstGoal) {
        this.timeOfFirstGoal = timeOfFirstGoal;
    }

    public void setCurrentMatchPeriod(AflMatchPeriod currentMatchPeriod) {
        this.currentMatchPeriod = currentMatchPeriod;
    }

    public int getCurrentPointsA() {
        return currentGoalsA * 6 + currentBehindsA;
    }

    public int getCurrentPointsB() {
        return currentGoalsB * 6 + currentBehindsB;
    }

    public int getCurrentGoalsA() {
        return currentGoalsA;
    }

    public void setCurrentGoalsA(int currentGoalsA) {
        this.currentGoalsA = currentGoalsA;
    }

    public int getCurrentGoalsB() {
        return currentGoalsB;
    }

    public void setCurrentGoalsB(int currentGoalsB) {
        this.currentGoalsB = currentGoalsB;
    }

    public int getCurrentBehindsA() {
        return currentBehindsA;
    }

    public void setCurrentBehindsA(int currentBehindsA) {
        this.currentBehindsA = currentBehindsA;
    }

    public int getCurrentBehindsB() {
        return currentBehindsB;
    }

    public void setCurrentBehindsB(int currentBehindsB) {
        this.currentBehindsB = currentBehindsB;
    }

    public TeamId getFirstTo3B() {
        return firstTo3B;
    }

    public void setFirstTo3B(TeamId firstTo3B) {
        this.firstTo3B = firstTo3B;
    }

    public TeamId getFirstTo4B() {
        return firstTo4B;
    }

    public void setFirstTo4B(TeamId firstTo4B) {
        this.firstTo4B = firstTo4B;
    }

    public TeamId getFirstTo5B() {
        return firstTo5B;
    }

    public void setFirstTo5B(TeamId firstTo5B) {
        this.firstTo5B = firstTo5B;
    }

    public TeamId getFirstTo6B() {
        return firstTo6B;
    }

    public void setFirstTo6B(TeamId firstTo6B) {
        this.firstTo6B = firstTo6B;
    }

    public TeamId getFirstTo3G() {
        return firstTo3G;
    }

    public void setFirstTo3G(TeamId firstTo3G) {
        this.firstTo3G = firstTo3G;
    }

    public TeamId getFirstTo4G() {
        return firstTo4G;
    }

    public void setFirstTo4G(TeamId firstTo4G) {
        this.firstTo4G = firstTo4G;
    }

    public TeamId getFirstTo5G() {
        return firstTo5G;
    }

    public void setFirstTo5G(TeamId firstTo5G) {
        this.firstTo5G = firstTo5G;
    }

    public TeamId getFirstTo6G() {
        return firstTo6G;
    }

    public void setFirstTo6G(TeamId firstTo6G) {
        this.firstTo6G = firstTo6G;
    }

    public TeamId getCurrentFirstScoreToFifteen() {
        return currentFirstScoreToFifteen;
    }

    public void setCurrentFirstScoreToFifteen(TeamId currentFirstScoreToFifteen) {
        this.currentFirstScoreToFifteen = currentFirstScoreToFifteen;
    }

    public TeamId getCurrentFirstScoreToTwenty() {
        return currentFirstScoreToTwenty;
    }

    public void setCurrentFirstScoreToTwenty(TeamId currentFirstScoreToTwenty) {
        this.currentFirstScoreToTwenty = currentFirstScoreToTwenty;
    }

    public TeamId getCurrentFirstScoreToTen() {
        return currentFirstScoreToTen;
    }

    public void setCurrentFirstScoreToTen(TeamId currentFirstScoreToTen) {
        this.currentFirstScoreToTen = currentFirstScoreToTen;
    }

    public int getCurrentOddEvenMarketForQuarterNo() {
        return currentOddEvenMarketForQuarterNo;
    }

    public void setCurrentOddEvenMarketForQuarterNo(int currentOddEvenMarketForQuarterNo) {
        this.currentOddEvenMarketForQuarterNo = currentOddEvenMarketForQuarterNo;
    }

    public int getCurrentPeriodNo() {
        return currentPeriodNo;
    }

    public void setCurrentPeriodNo(int currentPeriodNo) {
        this.currentPeriodNo = currentPeriodNo;
    }

    public TeamId getNextToScoreGoal() {
        return nextToScoreGoal;
    }

    public void setNextToScoreGoal(TeamId nextToScoreGoal) {
        this.nextToScoreGoal = nextToScoreGoal;
    }

    public TeamId getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(TeamId nextPoint) {
        this.nextPoint = nextPoint;
    }

    public TeamId getNextToScoreBehind() {
        return nextToScoreBehind;
    }

    public void setNextToScoreBehind(TeamId nextToScoreBehind) {
        this.nextToScoreBehind = nextToScoreBehind;
    }

    public int getCurrentPeriodGoalsA() {

        return currentPeriodGoalsA;
    }

    public int getCurrentPeriodGoalsB() {

        return currentPeriodGoalsB;
    }

    public int getCurrentPeriodPointsA() {
        return currentPeriodPointsA;
    }

    public int getCurrentPeriodPointsB() {
        return currentPeriodPointsB;
    }

    public int getGoalsTotalCurrentPeriod() {

        return currentPeriodGoalsA + currentPeriodGoalsB;
    }

    public int getPointsTotalCurrentPeriod() {
        return currentPeriodGoalsA * 6 + currentPeriodGoalsB * 6 + currentPeriodBehindsA + currentPeriodBehindsB;
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

    public void addPointsCurrentPeriod(TeamId teamId, int i) {
        if (teamId == TeamId.A) {
            if (i == 1) {
                currentPeriodBehindsA++;
                currentPeriodPointsA++;
            } else {
                currentPeriodGoalsA++;
                currentPeriodPointsA += 6;
            }
        } else if (teamId == TeamId.B) {
            if (i == 1) {
                currentPeriodBehindsB++;
                currentPeriodPointsB++;
            } else {
                currentPeriodGoalsB++;
                currentPeriodPointsB += 6;
            }
        }

        if (nextPoint == TeamId.UNKNOWN)
            nextPoint = teamId;
    }

    public void restFirstToFlags(TeamId firstTo3B2, TeamId firstTo4B2, TeamId firstTo5B2, TeamId firstTo6B2,
                    TeamId firstTo3G2, TeamId firstTo4G2, TeamId firstTo5G2, TeamId firstTo6G2, int currentGoalsA,
                    int currentGoalsB, int currentBehindsA, int currentBehindsB) {
        this.firstTo3B = firstTo3B2;
        this.firstTo4B = firstTo4B2;
        this.firstTo5B = firstTo5B2;
        this.firstTo6B = firstTo6B2;

        this.firstTo3G = firstTo3G2;
        this.firstTo4G = firstTo4G2;
        this.firstTo5G = firstTo5G2;
        this.firstTo6G = firstTo6G2;

        this.currentGoalsA = currentGoalsA;
        this.currentGoalsB = currentGoalsB;
        this.currentBehindsA = currentBehindsA;
        this.currentBehindsB = currentBehindsB;

    }

}
