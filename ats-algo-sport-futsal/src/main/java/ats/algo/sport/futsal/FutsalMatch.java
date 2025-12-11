package ats.algo.sport.futsal;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.futsal.FutsalMatchIncident.FutsalMatchIncidentType;

public class FutsalMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private FutsalMatchState simMatchState;
    private FutsalMatchFacts matchFacts;
    private FutsalMatchIncident matchIncident;
    private FutsalMatchIncident minorPenaltyMatchIncidentA;
    private FutsalMatchIncident minorPenaltyMatchIncidentB;
    private FutsalMatchIncident majorPenaltyMatchIncidentA;
    private FutsalMatchIncident majorPenaltyMatchIncidentB;

    int repeaterMinorA = 0;
    int repeaterMinorB = 0;
    int repeaterMajorA = 0;
    int repeaterMajorB = 0;

    public FutsalMatch(FutsalMatchFormat matchFormat, FutsalMatchState matchState, FutsalMatchParams matchParams) {

        super((MatchFormat) matchFormat, (MatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new FutsalMatchMarketsFactory(matchState);
        this.simMatchState = (FutsalMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new FutsalMatchFacts();
        this.matchIncident = new FutsalMatchIncident();
        this.minorPenaltyMatchIncidentA = new FutsalMatchIncident();
        this.minorPenaltyMatchIncidentB = new FutsalMatchIncident();
        this.majorPenaltyMatchIncidentA = new FutsalMatchIncident();
        this.majorPenaltyMatchIncidentB = new FutsalMatchIncident();

    }

    @Override
    public MonteCarloMatch clone() {
        FutsalMatch cc = new FutsalMatch((FutsalMatchFormat) matchFormat, (FutsalMatchState) matchState,
                        (FutsalMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new FutsalMatchMarketsFactory((FutsalMatchState) matchState);
    }

    private FutsalMatchFormat getFutsalMatchFormat() {
        return (FutsalMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        FutsalMatchState startingMatchState = (FutsalMatchState) matchState;
        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == FutsalMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(FutsalMatchPeriod.IN_FIRST_HALF); // start in play if not already there.

        FutsalMatchParams fustalMatchParams = (FutsalMatchParams) matchParams;
        double goalsTotal = fustalMatchParams.getGoalTotal().nextRandom();
        double goalsDiff = fustalMatchParams.getGoalSupremacy().nextRandom();
        double simHomeScoreRate = (goalsTotal + goalsDiff) / 2;
        double simAwayScoreRate = (goalsTotal - goalsDiff) / 2;
        double simHomeLoseBoost = fustalMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = fustalMatchParams.getAwayLoseBoost().nextRandom();
        double powerBoostRate = fustalMatchParams.getPowerBoostRate().nextRandom();

        matchFacts.reset(((FutsalMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((FutsalMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((FutsalMatchState) matchState).getCurrentFiveMinsEnder(),
                        ((FutsalMatchState) matchState).getFiveMinsGoalsA(),
                        ((FutsalMatchState) matchState).getFiveMinsGoalsB());

        do {
            boolean shootOutIncidentOccurred = false;

            if (!simMatchState.getMatchPeriod().equals(FutsalMatchPeriod.IN_SHOOTOUT)) {

                double timeBasedFactor = GoalDistribution.getGoalDistribution(getFutsalMatchFormat().is40MinMatch(),
                                getFutsalMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                                simMatchState.getElapsedTimeSecs(), 0); // 0 is the injure time
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
                    minorPenaltyMatchIncidentA.setIncidentSubType(FutsalMatchIncidentType.TWO_MINS_PENALTY_END);
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
                    minorPenaltyMatchIncidentB.setIncidentSubType(FutsalMatchIncidentType.TWO_MINS_PENALTY_END);
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
                    majorPenaltyMatchIncidentA.setIncidentSubType(FutsalMatchIncidentType.FIVE_MINS_PENALTY_END);
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
                    majorPenaltyMatchIncidentB.setIncidentSubType(FutsalMatchIncidentType.FIVE_MINS_PENALTY_END);
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
                    matchIncident.setIncidentSubType(FutsalMatchIncidentType.GOAL);
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
                    matchIncident.setIncidentSubType(FutsalMatchIncidentType.GOAL);
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
                        matchIncident.setIncidentSubType(FutsalMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        shootOutIncidentOccurred = true;
                    }
                } else if (currentShootOutCounter % 2 == 0 && currentShootOutCounter > 1) { // team B start shoot out
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(FutsalMatchIncidentType.GOAL);
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
        } while (simMatchState.getMatchPeriod() != FutsalMatchPeriod.MATCH_COMPLETED);

        ((FutsalMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
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
        this.monteCarloMarkets.consolidate(((FutsalMatch) match).monteCarloMarkets);
    }

}
