package ats.algo.sport.handball;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.handball.HandballMatchIncident.HandballMatchIncidentType;

public class HandballMatch extends MonteCarloMatch {

    private static final int timeSliceSecs = 10;
    int repeaterMinorA = 0;
    int repeaterMinorB = 0;
    private HandballMatchIncident twoMinsSuspensionMatchIncidentA;
    private HandballMatchIncident twoMinsSuspensionMatchIncidentB;

    private HandballMatchState simMatchState;
    private HandballMatchFacts matchFacts;
    private HandballMatchIncident matchIncident;

    public HandballMatch(HandballMatchFormat matchFormat, HandballMatchState matchState,
                    HandballMatchParams matchParams) {
        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new HandballMatchMarketsFactory((HandballMatchState) matchState);
        this.simMatchState = (HandballMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new HandballMatchFacts();
        this.matchIncident = new HandballMatchIncident();
        this.twoMinsSuspensionMatchIncidentA = new HandballMatchIncident();
        this.twoMinsSuspensionMatchIncidentB = new HandballMatchIncident();
    }

    @Override
    public MonteCarloMatch clone() {
        HandballMatch cc = new HandballMatch((HandballMatchFormat) matchFormat, (HandballMatchState) matchState,
                        (HandballMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new HandballMatchMarketsFactory((HandballMatchState) matchState);

    }

    private HandballMatchFormat getHandballMatchFormat() {
        return (HandballMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        HandballMatchState startingMatchState = (HandballMatchState) matchState;
        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == HandballMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(HandballMatchPeriod.IN_FIRST_HALF); // start in play if not already there.
        HandballMatchParams handballMatchParams = (HandballMatchParams) matchParams;
        double powerBoostRate = handballMatchParams.getPowerBoostRate().nextRandom();
        double scoreToTal = handballMatchParams.getTotalScoreRate().nextRandom();
        double scoreDiff = handballMatchParams.getSupremacyScoreRate().nextRandom();
        double simHomeScoreRate = (scoreToTal + scoreDiff) / 2;
        double simAwayScoreRate = (scoreToTal - scoreDiff) / 2;

        double simHomeLoseBoost = 0;// handballMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = 0;// handballMatchParams.getAwayLoseBoost().nextRandom();
        /*
         * Adjustment rate hard coded to be 0.7
         */
        double adjParam = 0.7;

        matchFacts.reset(((HandballMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((HandballMatchState) matchState).getCurrentPeriodGoalsB());

        do {
            // matchFacts.resetperiod( simMatchState,(HandballMatchState) matchState);

            double timeBasedFactor = GoalDistribution.getGoalDistribution(getHandballMatchFormat().is50MinMatch(),
                            simMatchState.getMatchPeriod(), simMatchState.getElapsedTimeSecs());
            double homeScoreRate = (simHomeScoreRate) * timeBasedFactor;
            double awayScoreRate = (simAwayScoreRate) * timeBasedFactor;
            boolean resetTwoMinsSuspensionA = false;

            int nIterations = simMatchState.getElapsedTimeThisPeriodSecs() / timeSliceSecs;// current iterations NO
            // elapsedTimePeriod.
            double actualPointsDiffPresent =
                            simMatchState.getCurrentPeriodGoalsA() - simMatchState.getCurrentPeriodGoalsB();

            int iterationNoInPeriod = 1800 / timeSliceSecs;;
            // currentPeriodIterationNO(simMatchState.getMatchPeriod(),
            // simMatchState.getNormalHalfsSecs(), true);

            double expectedPointsDiff = (simHomeScoreRate - simAwayScoreRate) * 0.5
                            * ((double) nIterations / (double) iterationNoInPeriod);

            double pAdjust = adjParam * ((double) (expectedPointsDiff - actualPointsDiffPresent))
                            / (iterationNoInPeriod);

            homeScoreRate = homeScoreRate + pAdjust;
            awayScoreRate = awayScoreRate - pAdjust;

            if (simMatchState.getTwoMinsSuspensionNumberA() > 0) {
                repeaterMinorA = repeaterCount(simMatchState.getTwoMinsSuspensionA(),
                                simMatchState.getElapsedTimeSecs());
                resetTwoMinsSuspensionA = isSinBinNeedReset(simMatchState.getTwoMinsSuspensionA(),
                                simMatchState.getElapsedTimeSecs()); // two mins
                twoMinsSuspensionMatchIncidentA.setTeamId(TeamId.A);
                twoMinsSuspensionMatchIncidentA.setIncidentSubType(HandballMatchIncidentType.TWO_MINS_SUSPENSION_END);
            }

            boolean resetTwoMinsSuspensionB = false;

            if (simMatchState.getTwoMinsSuspensionNumberB() > 0) {
                repeaterMinorB = 0;
                repeaterMinorB = repeaterCount(simMatchState.getTwoMinsSuspensionB(),
                                simMatchState.getElapsedTimeSecs());
                resetTwoMinsSuspensionB = isSinBinNeedReset(simMatchState.getTwoMinsSuspensionB(),
                                simMatchState.getElapsedTimeSecs()); // two mins
                twoMinsSuspensionMatchIncidentB.setTeamId(TeamId.B);
                twoMinsSuspensionMatchIncidentB.setIncidentSubType(HandballMatchIncidentType.TWO_MINS_SUSPENSION_END);
            }

            if ((simMatchState.getTwoMinsSuspensionNumberA() == 1)) {
                awayScoreRate = awayScoreRate * (powerBoostRate + 1);
            } else if (simMatchState.getTwoMinsSuspensionNumberA() >= 2) {
                awayScoreRate = awayScoreRate * (powerBoostRate + 1.15);
            }
            if ((simMatchState.getTwoMinsSuspensionNumberB() == 1)) {
                homeScoreRate = homeScoreRate * (powerBoostRate + 1);
            } else if (simMatchState.getTwoMinsSuspensionNumberB() >= 2) {
                homeScoreRate = homeScoreRate * (powerBoostRate + 1.15);
            }

            if (simMatchState.getGoalsA() < simMatchState.getGoalsB())
                homeScoreRate *= (1 + simHomeLoseBoost);
            if (simMatchState.getGoalsA() > simMatchState.getGoalsB())
                awayScoreRate *= (1 + simAwayLoseBoost);

            double rh = RandomNoGenerator.nextDouble();

            // testing section start
            // double ra = RandomNoGenerator.nextDouble();

            // testing section end

            boolean incidentOccurred = false;

            if (rh < homeScoreRate) {
                matchIncident.setIncidentSubType(HandballMatchIncidentType.GOAL);
                matchIncident.setTeamId(TeamId.A);
                incidentOccurred = true;
                if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                    matchFacts.setNextToScore(TeamId.A);
                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    matchFacts.addScoreCurrentPeriod(TeamId.A);
            } else if (rh < (awayScoreRate + homeScoreRate)) {
                matchIncident.setIncidentSubType(HandballMatchIncidentType.GOAL);
                matchIncident.setTeamId(TeamId.B);
                incidentOccurred = true;
                if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                    matchFacts.setNextToScore(TeamId.B);
                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    matchFacts.addScoreCurrentPeriod(TeamId.B);
            }
            if (incidentOccurred) {
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            }

            if (resetTwoMinsSuspensionA) { // will cause problem for goals

                for (int i = 0; i < repeaterMinorA; i++) {
                    twoMinsSuspensionMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(twoMinsSuspensionMatchIncidentA, false);
                }
                repeaterMinorA = 0;
            }

            if (resetTwoMinsSuspensionB) { // will cause problem for goals
                for (int i = 0; i < repeaterMinorB; i++) {
                    if (repeaterMinorB > 2) // in case goal happens same time
                        repeaterMinorB = 2;
                    twoMinsSuspensionMatchIncidentB.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(twoMinsSuspensionMatchIncidentB, false);
                }
                repeaterMinorB = 0;
            }

            simMatchState.incrementSimulationElapsedTime(timeSliceSecs);

        } while (simMatchState.getMatchPeriod() != HandballMatchPeriod.MATCH_COMPLETED);
        ((HandballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    }

    private int repeaterCount(ArrayList<Integer> arrayList, int elapsedTimeSecs) {
        int repeater = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) < elapsedTimeSecs)
                repeater++;
        }

        return repeater;
    }

    private boolean isSinBinNeedReset(ArrayList<Integer> arrayList, int elapsedTimeSecs) {
        boolean reset = false;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) < elapsedTimeSecs)
                reset = true;
        }

        return reset;
    }

    // private int currentPeriodIterationNO(HandballMatchPeriod matchPeriod, int halfSeconds, boolean twoHalvesFormat) {
    // int iterationNO = 0;
    // if (!matchPeriod.equals(HandballMatchPeriod.IN_EXTRATIME_PERIOD))
    // if (halfSeconds == 1800) {
    // if (twoHalvesFormat)
    // iterationNO = 1800 / timeSliceSecs;
    // } else {
    // iterationNO = 1500 / timeSliceSecs;
    // }
    // else {
    // iterationNO = 300 / timeSliceSecs;
    // }
    //
    // return iterationNO;
    // }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((HandballMatch) match).monteCarloMarkets);
    }

}
