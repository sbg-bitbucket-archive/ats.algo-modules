package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident;
import ats.algo.sport.americanfootball.AmericanfootballMatchParams;
import ats.algo.sport.americanfootball.AmericanfootballMatch;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.AmericanfootballMatchIncidentType;
import ats.algo.sport.americanfootball.AmericanfootballMatchState.AmericanfootballMatchStatusType;

public class AmericanfootballMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private static final int pointsForTouchDown = 6;
    private static final int pointsForConversion = 1;
    private static final int pointsForConversion2 = 2;
    private static final int pointsForFieldGoal = 3;
    private AmericanfootballMatchState simMatchState;
    private AmericanfootballMatchFacts matchFacts;
    private AmericanfootballMatchIncident matchIncident;
    int repeaterMinorA = 0;
    int repeaterMinorB = 0;
    @SuppressWarnings("unused")
    private static final double acturalPointsForTD = pointsForTouchDown + 0.9652;

    public AmericanfootballMatch(AmericanfootballMatchFormat matchFormat, AmericanfootballMatchState matchState,
                    AmericanfootballMatchParams matchParams) {

        super((MatchFormat) matchFormat, (MatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new AmericanfootballMatchMarketsFactory(matchState);
        this.simMatchState = (AmericanfootballMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new AmericanfootballMatchFacts();
        this.matchIncident = new AmericanfootballMatchIncident();
        new AmericanfootballMatchIncident();
        new AmericanfootballMatchIncident();
    }

    @Override
    public MonteCarloMatch clone() {
        AmericanfootballMatch cc = new AmericanfootballMatch((AmericanfootballMatchFormat) matchFormat,
                        (AmericanfootballMatchState) matchState, (AmericanfootballMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new AmericanfootballMatchMarketsFactory((AmericanfootballMatchState) matchState);
    }

    private AmericanfootballMatchFormat getAmericanfootballMatchFormat() {
        return (AmericanfootballMatchFormat) matchFormat;
    }

    @SuppressWarnings("unused")
    @Override
    public void playMatch() {
        AmericanfootballMatchState startingMatchState = (AmericanfootballMatchState) matchState;
        boolean isImminent = true;

        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == AmericanfootballMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(AmericanfootballMatchPeriod.IN_FIRST_QUARTER); // start in play if not already
                                                                                        // there.

        AmericanfootballMatchParams rugbyUnionMatchParams = (AmericanfootballMatchParams) matchParams;

        double tdPointsPercentage = 0.736;
        double twoPointConversionAttemptsRate = 0.018;// 0.18;
        double twoPointsSuccessRate = 0.561904762;
        double onePointSuccessRate = 0.9621;

        double redZoneTDRate = 0.5525;

        double thirdDownConversionProb = 0.386419;
        double fourthDownConversionProb = 0.493144;
        double yard30FieldKickRate = 0.55;
        double yard20FieldKickRate = 0.7;
        double simHomeFGRate, simAwayFGRate, simHomeTouchDownRate, simAwayTouchDownRate, pointsDiff;
        double simHomePointScoreRate = 0;
        double simAwayPointScoreRate = 0;

        double pointsTotal = rugbyUnionMatchParams.getScoreTotal().nextRandom();
        pointsDiff = rugbyUnionMatchParams.getScoreSupremacy().getMean();

        simHomePointScoreRate = (pointsTotal + pointsDiff) / 2;
        simAwayPointScoreRate = (pointsTotal - pointsDiff) / 2;

        double touchDownTotal = rugbyUnionMatchParams.getTdTotal().nextRandom();
        double touchDownDiff = pointsDiff * tdPointsPercentage / 6.95;// rugbyUnionMatchParams.getTdSupremacy().nextRandom();

        simHomeTouchDownRate = (touchDownTotal + touchDownDiff) / 2;// simHomePointScoreRate * tdPointsPercentage /
                                                                    // 6.91;
        simAwayTouchDownRate = (touchDownTotal - touchDownDiff) / 2;// simAwayPointScoreRate * tdPointsPercentage /
                                                                    // 6.91;

        simHomeFGRate = (simHomePointScoreRate - simHomeTouchDownRate * 6.965) / 3;
        simAwayFGRate = (simAwayPointScoreRate - simAwayTouchDownRate * 6.965) / 3;// simAwayPointScoreRate * (1 -
                                                                                   // tdPointsPercentage) / 3;

        double adjParam = 1.1;
        double adjParam1 = 1.1;

        double temp1 = (Math.abs(pointsDiff - 2) * 2) * 0.125 + adjParam;
        adjParam1 = (1.2 > (temp1)) ? (temp1) : 1.2;
        adjParam1 = 0.35;// 0.2 for 6.5

        // double tempDiff = Math.abs(pointsDiff);
        // if(tempDiff > 3.5)
        // adjParam1 = 2.2; // for in put diff smaller than 5
        // if(pointsDiff > 4.)
        // adjParam1 = 1.9;
        // if(pointsDiff > 4.85)
        // adjParam1 = 1.2;
        // if(pointsDiff > 5.2)
        // adjParam1 = 0.9;
        // if(pointsDiff > 12 )
        // adjParam1 = 0.0;

        adjParam = adjParam1;
        double accumulatedFieldGoalDist = 0;
        double accumulatedTouchDownDist = 0;

        matchFacts.reset(((AmericanfootballMatchState) matchState).getCurrentPeriodPointsA(),
                        ((AmericanfootballMatchState) matchState).getCurrentPeriodPointsB(),
                        ((AmericanfootballMatchState) matchState).getCurrentPeriodTdA(),
                        ((AmericanfootballMatchState) matchState).getCurrentPeriodTdB(),
                        ((AmericanfootballMatchState) matchState).getSequenceIdForHalf(0),
                        ((AmericanfootballMatchState) matchState).getPointsA(),
                        ((AmericanfootballMatchState) matchState).getPointsB());

        do {
            boolean incidentOccurred = false;
            boolean timeElapseFreezeIncident = false;

            double timeBasedFactorTD = GoalDistribution.getGoalDistribution(
                            getAmericanfootballMatchFormat().is60MinMatch(),
                            getAmericanfootballMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(),
                            simMatchState.getAmericanfootballMatchStatus().getAmericanfootballMatchStatusType(), 0,
                            true); // 0

            double timeBasedFactorFG = GoalDistribution.getGoalDistribution(
                            getAmericanfootballMatchFormat().is60MinMatch(),
                            getAmericanfootballMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(),
                            simMatchState.getAmericanfootballMatchStatus().getAmericanfootballMatchStatusType(), 0,
                            false); // 0


            /* write something for checking current match status */
            if (simMatchState.getAmericanfootballMatchStatus()
                            .getAmericanfootballMatchStatusType() == AmericanfootballMatchStatusType.CONVERSION) { // If
                // conversion
                // FIXME: conversion can be two or one points, around 10% conversion will go for two points attempt
                double rc = RandomNoGenerator.nextDouble();
                double r = RandomNoGenerator.nextDouble();

                boolean mustGoTwoPointConversion = false;

                double successRateReduce = 0;
                if (simMatchState.getElapsedTimeSecs() > 3420) {
                    if (simMatchState.getAmericanfootballMatchStatus().getTeamId() == TeamId.A) {// A conversion
                        if (simMatchState.getPointsA() - simMatchState.getPointsB() == -2) {
                            mustGoTwoPointConversion = true;
                            successRateReduce = -0.;
                        }
                    } else {
                        if (simMatchState.getPointsA() - simMatchState.getPointsB() == 2) // B conversion
                        {
                            mustGoTwoPointConversion = true;
                            successRateReduce = -0.;
                        }
                    }

                }


                if ((rc < twoPointConversionAttemptsRate) || mustGoTwoPointConversion) { // 2 points conversion attempt
                    if (simMatchState.getAmericanfootballMatchStatus().getTeamId() == TeamId.A) {
                        if (r < twoPointsSuccessRate - successRateReduce) {// FIXME twoPointsSuccessRate
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_SCORE2);
                            matchIncident.setTeamId(TeamId.A);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                            if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                                matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForConversion2);
                            }
                            if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                                matchFacts.setNextToScoreDropGoal(TeamId.A);
                        } else {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_MISS);
                            matchIncident.setTeamId(TeamId.A);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                        }

                    } else if (simMatchState.getAmericanfootballMatchStatus().getTeamId() == TeamId.B) {
                        if (r < twoPointsSuccessRate - successRateReduce) {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_SCORE2);
                            matchIncident.setTeamId(TeamId.B);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                            if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                                matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForConversion2);
                            }
                            if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                                matchFacts.setNextToScoreDropGoal(TeamId.B);
                        } else {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_MISS);
                            matchIncident.setTeamId(TeamId.B);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                        }
                    } else {
                        throw new IllegalArgumentException(" Conversion team is not known ");
                    }

                } else { // 1 point conversion attempt
                    if (simMatchState.getAmericanfootballMatchStatus().getTeamId() == TeamId.A) {
                        if (r < onePointSuccessRate) {// FIXME 0.62
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_SCORE1);
                            matchIncident.setTeamId(TeamId.A);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                            if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                                matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForConversion);
                            }
                            if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                                matchFacts.setNextToScoreDropGoal(TeamId.A);
                        } else {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_MISS);
                            matchIncident.setTeamId(TeamId.A);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                        }

                    } else if (simMatchState.getAmericanfootballMatchStatus().getTeamId() == TeamId.B) {
                        if (r < onePointSuccessRate) {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_SCORE1);
                            matchIncident.setTeamId(TeamId.B);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                            if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                                matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForConversion);
                            }
                            if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                                matchFacts.setNextToScoreDropGoal(TeamId.B);
                        } else {
                            matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.CONVERSION_MISS);
                            matchIncident.setTeamId(TeamId.B);
                            incidentOccurred = true;
                            timeElapseFreezeIncident = true;
                        }
                    } else {
                        throw new IllegalArgumentException(" Conversion team is not known ");
                    }

                }
            } else if (simMatchState.getMatchPeriod().equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)
                            || simMatchState.getMatchPeriod().equals(AmericanfootballMatchPeriod.AT_EXTRA_TIME_END)) {
                // Simulate overtime ending rule.
                if (simMatchState.getFirstSidePossedTheBallExtraTime().equals(TeamId.UNKNOWN)) {
                    double rp = RandomNoGenerator.nextDouble(); // first team possess of the ball
                    TeamId pob = TeamId.A;
                    if (rp < 0.5)
                        pob = TeamId.B;

                    simMatchState.setFirstSidePossedTheBallExtraTime(pob);
                    matchIncident.setTeamId(pob);
                    timeElapseFreezeIncident = true;
                }

                // If both team scored in the overtime
                boolean bothTeamScored = false;
                if (simMatchState.getExtraTimeQuarterPointsA() > 0 && simMatchState.getExtraTimeQuarterPointsB() > 0)
                    bothTeamScored = true;

                if ((simMatchState.getElapsedTimeSecs() > 3840 || bothTeamScored)
                                && simMatchState.getSecondSidePossedTheBallExtraTime().equals(TeamId.UNKNOWN)) {
                    // After 4 minutes both team possessed the ball, or both has scored
                    if (simMatchState.getFirstSidePossedTheBallExtraTime().equals(TeamId.A))
                        simMatchState.setSecondSidePossedTheBallExtraTime(TeamId.B);
                    else
                        simMatchState.setSecondSidePossedTheBallExtraTime(TeamId.A);
                }

                // FIXME: FIXE NUMBER CHOOSEN

                double lastMinuteFieldGoalBoostHome = 0.00;
                double lastMinuteFieldGoalBoostAway = 0.00;

                double overTimeTouchDownBoostHome = 0;
                double overTimeTouchDownBoostAway = 0;

                double homeTDProb = ((simHomeTouchDownRate) * timeBasedFactorTD);
                double awayTDProb = ((simAwayTouchDownRate) * timeBasedFactorTD);

                double homeFieldGoalProb = ((simHomeFGRate) * timeBasedFactorFG);
                double awayFieldGoalProb = ((simAwayFGRate) * timeBasedFactorFG);

                homeFieldGoalProb = homeFieldGoalProb + lastMinuteFieldGoalBoostHome;
                awayFieldGoalProb = awayFieldGoalProb + lastMinuteFieldGoalBoostAway;

                if (simMatchState.getFirstSidePossedTheBallExtraTime() != TeamId.UNKNOWN
                                && simMatchState.getSecondSidePossedTheBallExtraTime() != TeamId.UNKNOWN) {
                    overTimeTouchDownBoostHome = ((simHomeTouchDownRate) * timeBasedFactorTD);
                    overTimeTouchDownBoostAway = ((simAwayTouchDownRate) * timeBasedFactorTD);
                } else if (simMatchState.getFirstSidePossedTheBallExtraTime() == TeamId.A)
                    overTimeTouchDownBoostHome = ((simHomeTouchDownRate) * timeBasedFactorTD) * 0.35;
                else if (simMatchState.getFirstSidePossedTheBallExtraTime() == TeamId.B)
                    overTimeTouchDownBoostAway = ((simAwayTouchDownRate) * timeBasedFactorTD) * 0.35;

                homeTDProb = homeTDProb + overTimeTouchDownBoostHome;
                awayTDProb = awayTDProb + overTimeTouchDownBoostAway;

                double rh = RandomNoGenerator.nextDouble();
                if (rh < homeTDProb) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.TOUCH_DOWN);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                    // simMatchState.getAmericanfootballMatchStatus()
                    // .setAmericanfootballMatchStatus(AmericanfootballMatchStatusType.CONVERSION, TeamId.A);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                        matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForTouchDown);
                    }

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.A);
                } else if (rh < (homeTDProb + homeFieldGoalProb)) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.FIELD_GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForFieldGoal);

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.A);

                } else if (rh < (homeTDProb + homeFieldGoalProb + awayTDProb)) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.TOUCH_DOWN);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    // simMatchState.getAmericanfootballMatchStatus()
                    // .setAmericanfootballMatchStatus(AmericanfootballMatchStatusType.CONVERSION, TeamId.B);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForTouchDown);

                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.B);

                } else if (rh < (homeTDProb + homeFieldGoalProb + awayTDProb + awayFieldGoalProb)) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.FIELD_GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForFieldGoal);

                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.B);
                }

            } else {
                // Normal general game simulation
                // Points difference expectation of this period of match
                accumulatedFieldGoalDist += timeBasedFactorFG;
                accumulatedTouchDownDist += timeBasedFactorTD;

                int nIterations = simMatchState.getElapsedTimeThisPeriodSecs() / timeSliceSecs;// current iterations NO
                int iterationNo = 900 / timeSliceSecs;// currentPeriodIterationNO(); assumed all 15 minutes
                                                      // quaters
                double lastMinuteFieldGoalBoostHome = 0;
                double lastMinuteFieldGoalBoostAway = 0;
                double lastMinuteTDBoostHome = 0;
                double lastMinuteTDBoostAway = 0;

                double lastMinuteTDSwithHome = 1;
                double lastMinuteFGSwithHome = 1;
                double lastMinuteTDSwithAway = 1;
                double lastMinuteFGSwithAway = 1;
                /*
                 * Mean Seaking adjustments for home and away
                 */
                double pHomeAdjFG = 0;
                double pAwayAdjFG = 0;

                double pHomeAdjTD = 0;
                double pAwayAdjTD = 0;

                // //FIXME: REIMPLEMENT THIS PART
                // if(nIterations < iterationNo/4){
                // adjParam1 = 2;
                // adjParam = 2;
                // }else if(nIterations < iterationNo/2){
                // adjParam1 = 2;
                // adjParam = 2;
                // }else{
                // adjParam1 = 0.;
                // adjParam = 0.;
                // }

                if (nIterations < iterationNo) {// nIterations<iterationNo

                    double expectedFGHome = simHomeFGRate * accumulatedFieldGoalDist;
                    double expectedFGAway = simAwayFGRate * accumulatedFieldGoalDist;

                    pHomeAdjFG = adjParam * (expectedFGHome - simMatchState.getFgA()) / iterationNo;
                    pAwayAdjFG = adjParam * (expectedFGAway - simMatchState.getFgB()) / iterationNo;

                    double expectedTDHome = simHomeTouchDownRate * accumulatedTouchDownDist;
                    double expectedTDAway = simAwayTouchDownRate * accumulatedTouchDownDist;

                    pHomeAdjTD = adjParam1 * (expectedTDHome - simMatchState.getTdA()) / iterationNo;
                    pAwayAdjTD = adjParam1 * (expectedTDAway - simMatchState.getTdB()) / iterationNo;

                    boolean homeLead = simMatchState.getPointsA() > simMatchState.getPointsB();
                    boolean awayLead = simMatchState.getPointsA() < simMatchState.getPointsB();

                    double actualPointsDiffPresent = simMatchState.getPointsA() - simMatchState.getPointsB();
                    double absActualPointsDiffPresent = Math.abs(actualPointsDiffPresent);

                    // When in the last 5 mins
                    if (simMatchState.getPeriodNo() == 4 && nIterations > 60) {
                        // When points diff in 3.5 - 6.5 and in the last 3 minutes
                        if (absActualPointsDiffPresent > 3.5 && absActualPointsDiffPresent < 6.5 && nIterations > 72) {

                            if (awayLead) {
                                lastMinuteTDBoostHome = 0.003;
                                lastMinuteFieldGoalBoostHome = 0.00;
                                lastMinuteTDSwithHome = 1;
                                lastMinuteFGSwithHome = 0;

                                lastMinuteTDBoostAway = 0.000;
                                lastMinuteFieldGoalBoostAway = 0.002;
                                lastMinuteTDSwithAway = 1;
                                lastMinuteFGSwithAway = 1;
                            } else if (homeLead) {
                                lastMinuteTDBoostHome = 0.000;
                                lastMinuteFieldGoalBoostHome = 0.002;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                lastMinuteTDSwithHome = 1;
                                lastMinuteFGSwithHome = 1;

                                lastMinuteTDBoostAway = 0.003;
                                lastMinuteFieldGoalBoostAway = 0.00;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                lastMinuteTDSwithAway = 1;
                                lastMinuteFGSwithAway = 0;
                            }

                            pHomeAdjFG = 0;
                            pAwayAdjFG = 0;
                            pHomeAdjTD = 0;
                            pAwayAdjTD = 0;

                        } else {

                            // diff in 0 - 2.5 situation
                            if (absActualPointsDiffPresent < 3.) {
                                if (awayLead) {
                                    lastMinuteTDBoostHome = 0.000;
                                    lastMinuteFieldGoalBoostHome = 0.002;
                                    lastMinuteTDSwithHome = 0;
                                    lastMinuteFGSwithHome = 1;

                                    lastMinuteTDBoostAway = 0.000;
                                    lastMinuteFieldGoalBoostAway = 0.001;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 1;

                                } else if (homeLead) {
                                    lastMinuteTDBoostHome = 0.000;
                                    lastMinuteFieldGoalBoostHome = 0.001;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 1;

                                    lastMinuteTDBoostAway = 0.000;
                                    lastMinuteFieldGoalBoostAway = 0.002;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                    lastMinuteTDSwithAway = 0;
                                    lastMinuteFGSwithAway = 1;
                                }

                                pHomeAdjFG = 0;
                                pAwayAdjFG = 0;
                                pHomeAdjTD = 0;
                                pAwayAdjTD = 0;
                            } else if (absActualPointsDiffPresent == 3) {

                                lastMinuteTDBoostHome = -0.002;
                                lastMinuteFieldGoalBoostHome = -0.002;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                lastMinuteTDSwithHome = 1;
                                lastMinuteFGSwithHome = 1;

                                lastMinuteTDBoostAway = -0.002;
                                lastMinuteFieldGoalBoostAway = -0.002;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                lastMinuteTDSwithAway = 1;
                                lastMinuteFGSwithAway = 1;

                                pHomeAdjFG = 0;
                                pAwayAdjFG = 0;
                                pHomeAdjTD = 0;
                                pAwayAdjTD = 0;
                            } else if (absActualPointsDiffPresent < 8.5) {


                                if (awayLead) {
                                    lastMinuteTDBoostHome = 0.004;
                                    lastMinuteFieldGoalBoostHome = 0.00;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 1;
                                } else if (homeLead) {
                                    lastMinuteTDBoostAway = 0.004;
                                    lastMinuteFieldGoalBoostAway = 0.000;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 1;
                                }

                                pHomeAdjFG = 0;
                                pAwayAdjFG = 0;
                                pHomeAdjTD = 0;
                                pAwayAdjTD = 0;

                            } else if (false) {// absActualPointsDiffPresent < 8.5
                                if (awayLead) {
                                    lastMinuteTDBoostHome = 0.002;
                                    lastMinuteFieldGoalBoostHome = 0.00;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 0;

                                    lastMinuteTDBoostAway = -0.002;
                                    lastMinuteFieldGoalBoostAway = -0.002;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 1;

                                } else if (homeLead) {
                                    lastMinuteTDBoostHome = -0.001;
                                    lastMinuteFieldGoalBoostHome = -0.001;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 1;

                                    lastMinuteTDBoostAway = 0.002;
                                    lastMinuteFieldGoalBoostAway = 0.00;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 0;
                                }

                                pHomeAdjFG = 0;
                                pAwayAdjFG = 0;
                                pHomeAdjTD = 0;
                                pAwayAdjTD = 0;
                            } else if (false && absActualPointsDiffPresent < 11.5) {
                                if (awayLead) {
                                    lastMinuteTDBoostHome = -0.001;
                                    lastMinuteFieldGoalBoostHome = 0.002;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 1;

                                    lastMinuteTDBoostAway = 0.00;
                                    lastMinuteFieldGoalBoostAway = 0.00;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 1;

                                } else if (homeLead) {
                                    lastMinuteTDBoostHome = 0.00;
                                    lastMinuteFieldGoalBoostHome = 0.00;// (simHomeTouchDownRate) * timeBasedFactor*2;
                                    lastMinuteTDSwithHome = 1;
                                    lastMinuteFGSwithHome = 1;

                                    lastMinuteTDBoostAway = -0.001;
                                    lastMinuteFieldGoalBoostAway = 0.002;// (simAwayTouchDownRate) * timeBasedFactor*4;
                                    lastMinuteTDSwithAway = 1;
                                    lastMinuteFGSwithAway = 1;
                                }

                                pHomeAdjFG = 0;
                                pAwayAdjFG = 0;
                                pHomeAdjTD = 0;
                                pAwayAdjTD = 0;


                            }

                        }

                    } else if (simMatchState.getPeriodNo() == 2 && nIterations > 78) {
                        /*
                         * First Half catch up boost
                         * 
                         * Both Team Score boosted
                         * 
                         */
                        if (awayLead) {
                            lastMinuteTDBoostHome = 0.00;
                            lastMinuteFieldGoalBoostHome = 0.00;
                            lastMinuteTDSwithHome = 1;
                            lastMinuteFGSwithHome = 1;

                            lastMinuteTDBoostAway = 0.002;
                            lastMinuteFieldGoalBoostAway = 0.002;
                            lastMinuteTDSwithAway = 1;
                            lastMinuteFGSwithAway = 1;

                        } else if (homeLead) {
                            lastMinuteTDBoostHome = 0.002;
                            lastMinuteFieldGoalBoostHome = 0.002;// (simHomeTouchDownRate) * timeBasedFactor*2;
                            lastMinuteTDSwithHome = 1;
                            lastMinuteFGSwithHome = 1;

                            lastMinuteTDBoostAway = 0.000;
                            lastMinuteFieldGoalBoostAway = 0.000;// (simAwayTouchDownRate) * timeBasedFactor*4;
                            lastMinuteTDSwithAway = 1;
                            lastMinuteFGSwithAway = 1;
                        }

                    }

                }

                double homeTDProb = ((simHomeTouchDownRate) * timeBasedFactorTD + pHomeAdjTD);
                double awayTDProb = ((simAwayTouchDownRate) * timeBasedFactorTD + pAwayAdjTD);

                double homeFieldGoalProb = ((simHomeFGRate) * timeBasedFactorFG + pHomeAdjFG);
                double awayFieldGoalProb = ((simAwayFGRate) * timeBasedFactorFG + pAwayAdjFG);

                homeFieldGoalProb = (homeFieldGoalProb + lastMinuteFieldGoalBoostHome) * lastMinuteFGSwithHome;
                awayFieldGoalProb = (awayFieldGoalProb + lastMinuteFieldGoalBoostAway) * lastMinuteFGSwithAway;

                // System.out.println("homeTDProb"+homeTDProb +" lastMinuteTDBoostHome"+ lastMinuteTDBoostHome);
                homeTDProb = (homeTDProb + lastMinuteTDBoostHome) * lastMinuteTDSwithHome;
                awayTDProb = (awayTDProb + lastMinuteTDBoostAway) * lastMinuteTDSwithAway;

                homeTDProb = (homeTDProb + lastMinuteTDBoostHome > 0 ? homeTDProb + lastMinuteTDBoostHome : 0)
                                * lastMinuteTDSwithHome;
                awayTDProb = (awayTDProb + lastMinuteTDBoostAway > 0 ? awayTDProb + lastMinuteTDBoostAway : 0)
                                * lastMinuteTDSwithAway;
                // System.out.println("homeTDProb"+homeTDProb +" lastMinuteTDBoostHome"+ lastMinuteTDBoostHome);
                homeFieldGoalProb = (homeFieldGoalProb > 0 ? homeFieldGoalProb : 0);
                awayFieldGoalProb = (awayFieldGoalProb > 0 ? awayFieldGoalProb : 0);


                double rh = RandomNoGenerator.nextDouble();

                if (rh < homeTDProb) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.TOUCH_DOWN);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                    matchFacts.addPointsCompare(TeamId.A, 6);

                    simMatchState.getAmericanfootballMatchStatus().setAmericanfootballMatchStatus(
                                    AmericanfootballMatchStatusType.CONVERSION, TeamId.A);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                        matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForTouchDown);
                    }

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.A);
                } else if (rh < (homeTDProb + homeFieldGoalProb)) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.FIELD_GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    matchFacts.addPointsCompare(TeamId.A, 3);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.A, pointsForFieldGoal);

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.A);

                } else if (rh < (homeTDProb + homeFieldGoalProb + awayTDProb)) {

                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.TOUCH_DOWN);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    matchFacts.addPointsCompare(TeamId.B, 6);

                    simMatchState.getAmericanfootballMatchStatus().setAmericanfootballMatchStatus(
                                    AmericanfootballMatchStatusType.CONVERSION, TeamId.B);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForTouchDown);

                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.B);

                } else if (rh < (homeTDProb + homeFieldGoalProb + awayTDProb + awayFieldGoalProb)) {
                    matchIncident.setIncidentSubType(AmericanfootballMatchIncidentType.FIELD_GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    matchFacts.addPointsCompare(TeamId.B, 3);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, pointsForFieldGoal);

                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.B);
                }
            }

            if (incidentOccurred) {
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            }

            if (!timeElapseFreezeIncident) {
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);
            }
        } while (simMatchState.getMatchPeriod() != AmericanfootballMatchPeriod.MATCH_COMPLETED);
        ((AmericanfootballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    }

    // enum FieldPositionType {
    // A10YARD, A20YARD, A30YARD, A40YARD, M50YARD, B10YARD, B20YARD, B30YARD, B40YARD, UNKNOWN
    // // A5MLINE_CENTRE, B5MLINE_CENTRE
    // }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((AmericanfootballMatch) match).monteCarloMarkets);
    }

}
