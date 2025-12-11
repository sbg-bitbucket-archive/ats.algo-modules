package ats.algo.sport.rollerhockey;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident.RollerhockeyMatchIncidentType;

public class RollerhockeyMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private RollerhockeyMatchState simMatchState;
    private RollerhockeyMatchFacts matchFacts;
    private RollerhockeyMatchIncident matchIncident;
    private RollerhockeyMatchIncident minorPenaltyMatchIncidentA;
    private RollerhockeyMatchIncident minorPenaltyMatchIncidentB;
    private RollerhockeyMatchIncident majorPenaltyMatchIncidentA;
    private RollerhockeyMatchIncident majorPenaltyMatchIncidentB;

    int repeaterMinorA = 0;
    int repeaterMinorB = 0;
    int repeaterMajorA = 0;
    int repeaterMajorB = 0;

    public RollerhockeyMatch(RollerhockeyMatchFormat matchFormat, RollerhockeyMatchState matchState,
                    RollerhockeyMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new RollerhockeyMatchMarketsFactory(matchState);
        this.simMatchState = (RollerhockeyMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new RollerhockeyMatchFacts();
        this.matchIncident = new RollerhockeyMatchIncident();
        this.minorPenaltyMatchIncidentA = new RollerhockeyMatchIncident();
        this.minorPenaltyMatchIncidentB = new RollerhockeyMatchIncident();
        this.majorPenaltyMatchIncidentA = new RollerhockeyMatchIncident();
        this.majorPenaltyMatchIncidentB = new RollerhockeyMatchIncident();

    }

    @Override
    public MonteCarloMatch clone() {
        RollerhockeyMatch cc = new RollerhockeyMatch((RollerhockeyMatchFormat) matchFormat,
                        (RollerhockeyMatchState) matchState, (RollerhockeyMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new RollerhockeyMatchMarketsFactory((RollerhockeyMatchState) matchState);
    }

    private RollerhockeyMatchFormat getRollerhockeyMatchFormat() {
        return (RollerhockeyMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        RollerhockeyMatchState startingMatchState = (RollerhockeyMatchState) matchState;
        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == RollerhockeyMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(RollerhockeyMatchPeriod.IN_FIRST_HALF); // start in play if not already there.

        RollerhockeyMatchParams rollerhockeyMatchParams = (RollerhockeyMatchParams) matchParams;
        double goalsTotal = rollerhockeyMatchParams.getGoalTotal().nextRandom();
        double goalsDiff = rollerhockeyMatchParams.getGoalSupremacy().nextRandom();
        double simHomeScoreRate = (goalsTotal + goalsDiff) / 2;
        double simAwayScoreRate = (goalsTotal - goalsDiff) / 2;
        double simHomeLoseBoost = rollerhockeyMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = rollerhockeyMatchParams.getAwayLoseBoost().nextRandom();
        double powerBoostRate = rollerhockeyMatchParams.getPowerBoostRate().nextRandom();

        matchFacts.reset(((RollerhockeyMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((RollerhockeyMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((RollerhockeyMatchState) matchState).getCurrentFiveMinsEnder(),
                        ((RollerhockeyMatchState) matchState).getFiveMinsGoalsA(),
                        ((RollerhockeyMatchState) matchState).getFiveMinsGoalsB());

        do {
            boolean shootOutIncidentOccurred = false;

            if (!simMatchState.getMatchPeriod().equals(RollerhockeyMatchPeriod.IN_SHOOTOUT)) {

                double timeBasedFactor =
                                GoalDistribution.getGoalDistribution(getRollerhockeyMatchFormat().is40MinMatch(),
                                                getRollerhockeyMatchFormat().getExtraTimeMinutes(),
                                                simMatchState.getMatchPeriod(), simMatchState.getElapsedTimeSecs(), 0); // 0
                                                                                                                        // is
                                                                                                                        // the
                                                                                                                        // injure
                                                                                                                        // time
                double homeScoreRate = (simHomeScoreRate) * timeBasedFactor;
                double awayScoreRate = (simAwayScoreRate) * timeBasedFactor;

                /*
                 * 2 and 5 mins penalties
                 * 
                 */

                boolean resetMinorSinBinANeed = false;

                if (simMatchState.getTwoMinsSinBinA() > 0) {
                    /*
                     * reset sin bin for A
                     * 
                     */
                    repeaterMinorA = repeaterCount(simMatchState.getTwoMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinANeed = isSinBinNeedReset(simMatchState.getTwoMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    minorPenaltyMatchIncidentA.setIncidentSubType(RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_END);
                }

                boolean resetMinorSinBinBNeed = false;

                if (simMatchState.getTwoMinsSinBinB() > 0) {
                    /*
                     * reset sin bin for B
                     * 
                     */
                    repeaterMinorB = 0;
                    repeaterMinorB = repeaterCount(simMatchState.getTwoMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinBNeed = isSinBinNeedReset(simMatchState.getTwoMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    minorPenaltyMatchIncidentB.setIncidentSubType(RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_END);
                }

                boolean resetMajorSinBinANeed = false;
                if (simMatchState.getTwoMinsSinBinA() > 0) {
                    /*
                     * reset major sin bin for A
                     * 
                     */
                    repeaterMajorA = repeaterCount(simMatchState.getFiveMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMajorSinBinANeed = isSinBinNeedReset(simMatchState.getFiveMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    majorPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    majorPenaltyMatchIncidentA.setIncidentSubType(RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_END);
                }

                boolean resetMajorSinBinBNeed = false;
                if (simMatchState.getTwoMinsSinBinB() > 0) {
                    /*
                     * reset major sin bin for A
                     * 
                     */
                    repeaterMajorB = repeaterCount(simMatchState.getFiveMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMajorSinBinBNeed = isSinBinNeedReset(simMatchState.getFiveMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    majorPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    majorPenaltyMatchIncidentB.setIncidentSubType(RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_END);
                }

                if ((simMatchState.getTwoMinsSinBinA()) + simMatchState.getFiveMinsSinBinA() == 1) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getTwoMinsSinBinA() + simMatchState.getFiveMinsSinBinA() >= 2) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1.15);
                }
                if ((simMatchState.getFiveMinsSinBinB() + simMatchState.getTwoMinsSinBinB()) == 1) {
                    homeScoreRate = homeScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getFiveMinsSinBinB() + simMatchState.getTwoMinsSinBinB() >= 2) {

                    homeScoreRate = homeScoreRate * (powerBoostRate + 1.15);

                }

                if (simMatchState.getGoalsA() < simMatchState.getGoalsB())
                    homeScoreRate *= (1 + simHomeLoseBoost);
                if (simMatchState.getGoalsA() > simMatchState.getGoalsB())
                    awayScoreRate *= (1 + simAwayLoseBoost);

                double rh = RandomNoGenerator.nextDouble();
                boolean incidentOccurred = false;
                if (rh < homeScoreRate) {
                    matchIncident.setIncidentSubType(RollerhockeyMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchState.getTwoMinsSinBinB() > 0) { // if need to reset minor sin bin
                        resetMinorSinBinBNeed = true;

                        if (simMatchState.getTwoMinsSinBinB() > repeaterMinorB)
                            repeaterMinorB++;
                    }

                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.A);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.A);
                    if (simMatchState.getElapsedTimeSecs() <= matchFacts.getCurrentFiveMinsEnder())
                        matchFacts.addFiveMinsGoalsA();

                } else if (rh < (awayScoreRate + homeScoreRate)) {
                    matchIncident.setIncidentSubType(RollerhockeyMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    if (simMatchState.getTwoMinsSinBinA() > 0) { // if need to reset minor sin bin

                        resetMinorSinBinANeed = true;
                        if (simMatchState.getTwoMinsSinBinA() > repeaterMinorA)
                            repeaterMinorA++;
                    }

                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.B);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.B);
                    if (simMatchState.getElapsedTimeSecs() <= matchFacts.getCurrentFiveMinsEnder())
                        matchFacts.addFiveMinsGoalsB();
                }
                if (incidentOccurred) {
                    matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(matchIncident, false);
                }

                if (resetMinorSinBinANeed) { // will cause problem for goals
                    if (repeaterMinorA > simMatchState.getTwoMinsSinBinA()) // in case goal happens same time
                        repeaterMinorA = simMatchState.getTwoMinsSinBinA();

                    for (int i = 0; i < repeaterMinorA; i++) {
                        minorPenaltyMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(minorPenaltyMatchIncidentA, false);
                    }
                    repeaterMinorA = 0;
                }

                if (resetMajorSinBinANeed) { // will cause problem for goals
                    for (int i = 0; i < repeaterMajorA; i++) {

                        if (repeaterMajorA > 2) // in case goal happens same time
                            repeaterMajorA = 2;

                        majorPenaltyMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(majorPenaltyMatchIncidentA, false);
                    }
                    repeaterMajorA = 0;
                }

                if (resetMinorSinBinBNeed) { // will cause problem for goals
                    for (int i = 0; i < repeaterMinorB; i++) {

                        if (repeaterMinorB > simMatchState.getTwoMinsSinBinB()) // in case goal happens same time
                            repeaterMinorB = simMatchState.getTwoMinsSinBinB();

                        minorPenaltyMatchIncidentB.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(minorPenaltyMatchIncidentB, false);
                    }
                    repeaterMinorB = 0;
                }

                if (resetMajorSinBinBNeed) { // will cause problem for goals
                    for (int i = 0; i < repeaterMajorB; i++) {

                        if (repeaterMajorB > 2) // in case goal happens same time
                            repeaterMajorB = 2;

                        majorPenaltyMatchIncidentB.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(majorPenaltyMatchIncidentB, false);
                    }
                    repeaterMajorB = 0;
                }


            } else {
                /*
                 * Below is for shootout situation
                 * 
                 */
                double homeScoreRate = 0.5;
                double awayScoreRate = 0.5;
                double rh = RandomNoGenerator.nextDouble();

                int currentShootOutCounter = simMatchState.getShootOutTimeCounter() + 1;

                if (currentShootOutCounter % 2 == 1) { // team A start shoot out
                    if (rh < homeScoreRate) {
                        matchIncident.setIncidentSubType(RollerhockeyMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        shootOutIncidentOccurred = true;
                    }
                } else if (currentShootOutCounter % 2 == 0 && currentShootOutCounter > 1) { // team B start shoot out
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(RollerhockeyMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.B);
                        shootOutIncidentOccurred = true;
                    }
                }

                if (shootOutIncidentOccurred) {
                    matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(matchIncident, false);
                }

            }
            if (!shootOutIncidentOccurred) {
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);
            }
        } while (simMatchState.getMatchPeriod() != RollerhockeyMatchPeriod.MATCH_COMPLETED);

        ((RollerhockeyMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
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

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((RollerhockeyMatch) match).monteCarloMarkets);
    }

}
