package ats.algo.sport.floorball;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.floorball.FloorballMatchIncident.FloorballMatchIncidentType;

public class FloorballMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private FloorballMatchState simMatchState;
    private FloorballMatchFacts matchFacts;
    private FloorballMatchIncident matchIncident;
    private FloorballMatchIncident minorPenaltyMatchIncidentA;
    private FloorballMatchIncident minorPenaltyMatchIncidentB;
    private FloorballMatchIncident majorPenaltyMatchIncidentA;
    private FloorballMatchIncident majorPenaltyMatchIncidentB;
    private FloorballMatchIncident tenMinsPenaltyMatchIncidentA;
    private FloorballMatchIncident tenMinsPenaltyMatchIncidentB;

    int repeaterMinorA = 0;
    int repeaterMinorB = 0;
    int repeaterMajorA = 0;
    int repeaterMajorB = 0;
    int repeaterTenMinsA = 0;
    int repeaterTenMinsB = 0;

    public FloorballMatch(FloorballMatchFormat matchFormat, FloorballMatchState matchState,
                    FloorballMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new FloorballMatchMarketsFactory(matchState);
        this.simMatchState = (FloorballMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new FloorballMatchFacts();
        this.matchIncident = new FloorballMatchIncident();
        this.minorPenaltyMatchIncidentA = new FloorballMatchIncident();
        this.minorPenaltyMatchIncidentB = new FloorballMatchIncident();
        this.majorPenaltyMatchIncidentA = new FloorballMatchIncident();
        this.majorPenaltyMatchIncidentB = new FloorballMatchIncident();
        this.tenMinsPenaltyMatchIncidentA = new FloorballMatchIncident();
        this.tenMinsPenaltyMatchIncidentB = new FloorballMatchIncident();

    }

    @Override
    public MonteCarloMatch clone() {
        FloorballMatch cc = new FloorballMatch((FloorballMatchFormat) matchFormat, (FloorballMatchState) matchState,
                        (FloorballMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new FloorballMatchMarketsFactory((FloorballMatchState) matchState);
    }

    private FloorballMatchFormat getFloorballMatchFormat() {
        return (FloorballMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        FloorballMatchState startingMatchState = (FloorballMatchState) matchState;
        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == FloorballMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(FloorballMatchPeriod.IN_FIRST_PERIOD); // start in play if not already there.

        FloorballMatchParams floorballMatchParams = (FloorballMatchParams) matchParams;
        double goalsTotal = floorballMatchParams.getGoalToal().nextRandom();
        double goalsDiff = floorballMatchParams.getGoalSupremacy().nextRandom();

        double simHomeScoreRate = (goalsTotal + goalsDiff) / 2;
        double simAwayScoreRate = (goalsTotal - goalsDiff) / 2;
        double simHomeLoseBoost = floorballMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = floorballMatchParams.getAwayLoseBoost().nextRandom();
        double powerBoostRate = floorballMatchParams.getPowerBoostRate().nextRandom();

        matchFacts.reset(((FloorballMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((FloorballMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((FloorballMatchState) matchState).getCurrentFiveMinsEnder(),
                        ((FloorballMatchState) matchState).getFiveMinsGoalsA(),
                        ((FloorballMatchState) matchState).getFiveMinsGoalsB());

        do {
            boolean shootOutIncidentOccurred = false;

            if (!simMatchState.getMatchPeriod().equals(FloorballMatchPeriod.IN_SHOOTOUT)) {

                double timeBasedFactor = GoalDistribution.getGoalDistribution(getFloorballMatchFormat().is50MinMatch(),
                                getFloorballMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                                simMatchState.getElapsedTimeSecs(), 0); // 0 is the injure time
                double homeScoreRate = (simHomeScoreRate) * timeBasedFactor;
                double awayScoreRate = (simAwayScoreRate) * timeBasedFactor;
                /*
                 * ten mins penalties
                 * 
                 */

                boolean resetTenMinsSinBinANeed = false;
                if (simMatchState.getTenMinsSinBinA() > 0) {
                    repeaterTenMinsA = repeaterCount(simMatchState.getTenMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetTenMinsSinBinANeed = isSinBinNeedReset(simMatchState.getTenMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    tenMinsPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    tenMinsPenaltyMatchIncidentA.setIncidentSubType(FloorballMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                boolean resetTenMinsSinBinBNeed = false;
                if (simMatchState.getTenMinsSinBinB() > 0) {
                    repeaterTenMinsB = repeaterCount(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetTenMinsSinBinBNeed = isSinBinNeedReset(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    tenMinsPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    tenMinsPenaltyMatchIncidentB.setIncidentSubType(FloorballMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                /*
                 * 2 and 5 mins penalties
                 * 
                 */

                boolean resetMinorSinBinANeed = false;

                if (simMatchState.getMinorSinBinA() > 0) {
                    /*
                     * reset sin bin for A
                     * 
                     */
                    repeaterMinorA = repeaterCount(simMatchState.getMinorSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinANeed = isSinBinNeedReset(simMatchState.getMinorSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    minorPenaltyMatchIncidentA.setIncidentSubType(FloorballMatchIncidentType.MINOR_PENALTY_END);
                }

                boolean resetMinorSinBinBNeed = false;

                if (simMatchState.getMinorSinBinB() > 0) {
                    /*
                     * reset sin bin for B
                     * 
                     */
                    repeaterMinorB = 0;
                    repeaterMinorB = repeaterCount(simMatchState.getMinorSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinBNeed = isSinBinNeedReset(simMatchState.getMinorSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    minorPenaltyMatchIncidentB.setIncidentSubType(FloorballMatchIncidentType.MINOR_PENALTY_END);
                }

                boolean resetMajorSinBinANeed = false;
                if (simMatchState.getMajorSinBinA() > 0) {
                    /*
                     * reset major sin bin for A
                     * 
                     */
                    repeaterMajorA = repeaterCount(simMatchState.getMajorSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMajorSinBinANeed = isSinBinNeedReset(simMatchState.getMajorSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    majorPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    majorPenaltyMatchIncidentA.setIncidentSubType(FloorballMatchIncidentType.MAJOR_PENALTY_END);
                }

                boolean resetMajorSinBinBNeed = false;
                if (simMatchState.getMajorSinBinB() > 0) {
                    /*
                     * reset major sin bin for A
                     * 
                     */
                    repeaterMajorB = repeaterCount(simMatchState.getMajorSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMajorSinBinBNeed = isSinBinNeedReset(simMatchState.getMajorSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    majorPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    majorPenaltyMatchIncidentB.setIncidentSubType(FloorballMatchIncidentType.MAJOR_PENALTY_END);
                }

                if ((simMatchState.getMinorSinBinA() + simMatchState.getMajorSinBinA())
                                + simMatchState.getTenMinsSinBinA() == 1) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getMinorSinBinA() + simMatchState.getMajorSinBinA()
                                + simMatchState.getTenMinsSinBinA() >= 2) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1.15);
                }
                if ((simMatchState.getMinorSinBinB() + simMatchState.getMajorSinBinB())
                                + simMatchState.getTenMinsSinBinB() == 1) {
                    homeScoreRate = homeScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getMinorSinBinB() + simMatchState.getMajorSinBinB()
                                + simMatchState.getTenMinsSinBinB() >= 2) {

                    homeScoreRate = homeScoreRate * (powerBoostRate + 1.15);

                }

                if (simMatchState.getGoalsA() < simMatchState.getGoalsB())
                    homeScoreRate *= (1 + simHomeLoseBoost);
                if (simMatchState.getGoalsA() > simMatchState.getGoalsB())
                    awayScoreRate *= (1 + simAwayLoseBoost);

                double rh = RandomNoGenerator.nextDouble();
                boolean incidentOccurred = false;
                if (rh < homeScoreRate) {
                    matchIncident.setIncidentSubType(FloorballMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchState.getMinorSinBinB() > 0) { // if need to reset minor sin bin
                        resetMinorSinBinBNeed = true;

                        if (simMatchState.getMinorSinBinB() > repeaterMinorB)
                            repeaterMinorB++;
                    }

                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.A);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.A);
                    if (simMatchState.getElapsedTimeSecs() <= matchFacts.getCurrentFiveMinsEnder())
                        matchFacts.addFiveMinsGoalsA();

                } else if (rh < (awayScoreRate + homeScoreRate)) {
                    matchIncident.setIncidentSubType(FloorballMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

                    if (simMatchState.getMinorSinBinA() > 0) { // if need to reset minor sin bin

                        resetMinorSinBinANeed = true;
                        if (simMatchState.getMinorSinBinA() > repeaterMinorA)
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
                    if (repeaterMinorA > simMatchState.getMinorSinBinA()) // in case goal happens same time
                        repeaterMinorA = simMatchState.getMinorSinBinA();

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

                        if (repeaterMinorB > simMatchState.getMinorSinBinB()) // in case goal happens same time
                            repeaterMinorB = simMatchState.getMinorSinBinB();

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

                /*
                 * ten mins send off reset()
                 */

                if (resetTenMinsSinBinANeed) {
                    for (int i = 0; i < repeaterTenMinsA; i++) {

                        if (repeaterTenMinsA > simMatchState.getTenMinsSinBinA()) // in case goal happens same time
                            repeaterTenMinsA = simMatchState.getTenMinsSinBinA();

                        tenMinsPenaltyMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(tenMinsPenaltyMatchIncidentA, false);
                    }
                    repeaterTenMinsA = 0;
                }

                if (resetTenMinsSinBinBNeed) {
                    for (int i = 0; i < repeaterTenMinsB; i++) {

                        if (repeaterTenMinsB > simMatchState.getTenMinsSinBinB()) // in case goal happens same time
                            repeaterTenMinsB = simMatchState.getTenMinsSinBinB();

                        tenMinsPenaltyMatchIncidentB.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(tenMinsPenaltyMatchIncidentB, false);
                    }
                    repeaterTenMinsB = 0;
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
                        matchIncident.setIncidentSubType(FloorballMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        shootOutIncidentOccurred = true;
                    }
                } else if (currentShootOutCounter % 2 == 0 && currentShootOutCounter > 1) { // team B start shoot out
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(FloorballMatchIncidentType.GOAL);
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
        } while (simMatchState.getMatchPeriod() != FloorballMatchPeriod.MATCH_COMPLETED);

        ((FloorballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
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
        this.monteCarloMarkets.consolidate(((FloorballMatch) match).monteCarloMarkets);
    }

}
