package ats.algo.sport.bandy;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchIncidentType;

public class BandyMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private BandyMatchState simMatchState;
    private BandyMatchFacts matchFacts;
    private BandyMatchIncident matchIncident;
    private BandyMatchIncident majorPenaltyMatchIncidentA;
    private BandyMatchIncident majorPenaltyMatchIncidentB;
    private BandyMatchIncident tenMinsPenaltyMatchIncidentA;
    private BandyMatchIncident tenMinsPenaltyMatchIncidentB;

    int repeaterMajorA = 0;
    int repeaterMajorB = 0;
    int repeaterTenMinsA = 0;
    int repeaterTenMinsB = 0;

    public BandyMatch(BandyMatchFormat matchFormat, BandyMatchState matchState, BandyMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new BandyMatchMarketsFactory(matchState);
        this.simMatchState = (BandyMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new BandyMatchFacts();
        this.matchIncident = new BandyMatchIncident();
        this.majorPenaltyMatchIncidentA = new BandyMatchIncident();
        this.majorPenaltyMatchIncidentB = new BandyMatchIncident();
        this.tenMinsPenaltyMatchIncidentA = new BandyMatchIncident();
        this.tenMinsPenaltyMatchIncidentB = new BandyMatchIncident();

    }

    @Override
    public MonteCarloMatch clone() {
        BandyMatch cc = new BandyMatch((BandyMatchFormat) matchFormat, (BandyMatchState) matchState,
                        (BandyMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new BandyMatchMarketsFactory((BandyMatchState) matchState);
    }

    private BandyMatchFormat getBandyMatchFormat() {
        return (BandyMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        BandyMatchState startingMatchState = (BandyMatchState) matchState;
        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == BandyMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(BandyMatchPeriod.IN_FIRST_HALF); // start in play if not already there.

        BandyMatchParams bandyMatchParams = (BandyMatchParams) matchParams;
        double pointsTotal = bandyMatchParams.getTotalScoreRate().nextRandom();
        double pointsDiff = bandyMatchParams.getSupremacyScoreRate().nextRandom();
        double simHomeScoreRate = (pointsTotal + pointsDiff) / 2;
        double simAwayScoreRate = (pointsTotal - pointsDiff) / 2;

        // double simHomeScoreRate = bandyMatchParams.getTotalScoreRate().nextRandom();
        // double simAwayScoreRate = bandyMatchParams.getSupremacyScoreRate().nextRandom();


        double simHomeLoseBoost = bandyMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = bandyMatchParams.getAwayLoseBoost().nextRandom();
        double powerBoostRate = bandyMatchParams.getPowerBoostRate().nextRandom();

        matchFacts.reset(((BandyMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((BandyMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((BandyMatchState) matchState).getCurrentFiveMinsEnder(),
                        ((BandyMatchState) matchState).getFiveMinsGoalsA(),
                        ((BandyMatchState) matchState).getFiveMinsGoalsB());

        do {
            boolean shootOutIncidentOccurred = false;

            if (!simMatchState.getMatchPeriod().equals(BandyMatchPeriod.IN_SHOOTOUT)) {

                double timeBasedFactor = GoalDistribution.getGoalDistribution(getBandyMatchFormat().is90MinMatch(),
                                getBandyMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
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
                    tenMinsPenaltyMatchIncidentA.setIncidentSubType(BandyMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                boolean resetTenMinsSinBinBNeed = false;
                if (simMatchState.getTenMinsSinBinB() > 0) {
                    repeaterTenMinsB = repeaterCount(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetTenMinsSinBinBNeed = isSinBinNeedReset(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    tenMinsPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    tenMinsPenaltyMatchIncidentB.setIncidentSubType(BandyMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                /*
                 * 2 and 5 mins penalties
                 * 
                 */


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
                    majorPenaltyMatchIncidentA.setIncidentSubType(BandyMatchIncidentType.MAJOR_PENALTY_END);
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
                    majorPenaltyMatchIncidentB.setIncidentSubType(BandyMatchIncidentType.MAJOR_PENALTY_END);
                }

                if (simMatchState.getMajorSinBinA() + simMatchState.getTenMinsSinBinA() == 1) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getMajorSinBinA() + simMatchState.getTenMinsSinBinA() >= 2) {
                    awayScoreRate = awayScoreRate * (powerBoostRate + 1.15);
                }
                if (simMatchState.getMajorSinBinB() + simMatchState.getTenMinsSinBinB() == 1) {
                    homeScoreRate = homeScoreRate * (powerBoostRate + 1);

                } else if (simMatchState.getMajorSinBinB() + simMatchState.getTenMinsSinBinB() >= 2) {

                    homeScoreRate = homeScoreRate * (powerBoostRate + 1.15);

                }

                if (simMatchState.getGoalsA() < simMatchState.getGoalsB())
                    homeScoreRate *= (1 + simHomeLoseBoost);
                if (simMatchState.getGoalsA() > simMatchState.getGoalsB())
                    awayScoreRate *= (1 + simAwayLoseBoost);

                double rh = RandomNoGenerator.nextDouble();
                boolean incidentOccurred = false;
                if (rh < homeScoreRate) {
                    matchIncident.setIncidentSubType(BandyMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.A);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.A);
                    if (simMatchState.getElapsedTimeSecs() <= matchFacts.getCurrentFiveMinsEnder())
                        matchFacts.addFiveMinsGoalsA();

                } else if (rh < (awayScoreRate + homeScoreRate)) {
                    matchIncident.setIncidentSubType(BandyMatchIncidentType.GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;

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


                if (resetMajorSinBinANeed) { // will cause problem for goals
                    for (int i = 0; i < repeaterMajorA; i++) {

                        if (repeaterMajorA > 2) // in case goal happens same time
                            repeaterMajorA = 2;

                        majorPenaltyMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                        simMatchState.updateStateForIncident(majorPenaltyMatchIncidentA, false);
                    }
                    repeaterMajorA = 0;
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
                double homeScoreRate = GoalDistribution.getGoalDistribution(getBandyMatchFormat().is90MinMatch(),
                                getBandyMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                                simMatchState.getElapsedTimeSecs(), 0); // 0 is the injure time
                double awayScoreRate = GoalDistribution.getGoalDistribution(getBandyMatchFormat().is90MinMatch(),
                                getBandyMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                                simMatchState.getElapsedTimeSecs(), 0); // 0 is the injure time

                double rh = RandomNoGenerator.nextDouble();

                int currentShootOutCounter = simMatchState.getShootOutTimeCounter() + 1;

                if (currentShootOutCounter % 2 == 1) { // team A start shoot out
                    if (rh < homeScoreRate) {
                        matchIncident.setIncidentSubType(BandyMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        shootOutIncidentOccurred = true;
                    }
                } else if (currentShootOutCounter % 2 == 0 && currentShootOutCounter > 1) { // team B start shoot out
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(BandyMatchIncidentType.GOAL);
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
        } while (simMatchState.getMatchPeriod() != BandyMatchPeriod.MATCH_COMPLETED);

        ((BandyMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
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
        this.monteCarloMarkets.consolidate(((BandyMatch) match).monteCarloMarkets);
    }

}
