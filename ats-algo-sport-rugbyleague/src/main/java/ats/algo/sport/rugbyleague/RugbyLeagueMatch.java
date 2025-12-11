package ats.algo.sport.rugbyleague;

import java.util.ArrayList;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.FieldPositionType;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.RugbyLeagueMatchIncidentType;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;
import ats.algo.sport.rugbyleague.RugbyLeagueMatch;

public class RugbyLeagueMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private RugbyLeagueMatchState simMatchState;
    private RugbyLeagueMatchFacts matchFacts;
    private RugbyLeagueMatchIncident matchIncident;
    private RugbyLeagueMatchIncident minorPenaltyMatchIncidentA;
    private RugbyLeagueMatchIncident minorPenaltyMatchIncidentB;

    int repeaterMinorA = 0;
    int repeaterMinorB = 0;

    public RugbyLeagueMatch(RugbyLeagueMatchFormat matchFormat, RugbyLeagueMatchState matchState,
                    RugbyLeagueMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new RugbyLeagueMatchMarketsFactory(matchState);
        this.simMatchState = (RugbyLeagueMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new RugbyLeagueMatchFacts();
        this.matchIncident = new RugbyLeagueMatchIncident();
        this.minorPenaltyMatchIncidentA = new RugbyLeagueMatchIncident();
        this.minorPenaltyMatchIncidentB = new RugbyLeagueMatchIncident();
    }

    @Override
    public MonteCarloMatch clone() {
        RugbyLeagueMatch cc = new RugbyLeagueMatch((RugbyLeagueMatchFormat) matchFormat,
                        (RugbyLeagueMatchState) matchState, (RugbyLeagueMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new RugbyLeagueMatchMarketsFactory((RugbyLeagueMatchState) matchState);
    }

    private RugbyLeagueMatchFormat getRugbyLeagueMatchFormat() {
        return (RugbyLeagueMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        RugbyLeagueMatchState startingMatchState = (RugbyLeagueMatchState) matchState;
        boolean isImminent = true;

        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == RugbyLeagueMatchPeriod.PREMATCH)
            simMatchState.setMatchPeriod(RugbyLeagueMatchPeriod.IN_FIRST_HALF); // start in play if not already there.

        RugbyLeagueMatchParams rugbyLeagueMatchParams = (RugbyLeagueMatchParams) matchParams;
        double conversionScoreRate = GoalDistribution.getConversionRate();

        double pointsTotal = rugbyLeagueMatchParams.getScoreTotal().nextRandom();
        double pointsDiff = rugbyLeagueMatchParams.getScoreSupremacy().nextRandom();
        double simHomePointScoreRate = (pointsTotal + pointsDiff) / 2;
        double simAwayPointScoreRate = (pointsTotal - pointsDiff) / 2;

        double tryTotal = rugbyLeagueMatchParams.getTryTotal().nextRandom();
        double tryDiff = rugbyLeagueMatchParams.getTrySupremacy().nextRandom();
        double simHomeTryRate = (tryTotal + tryDiff) / 2;
        double simAwayTryRate = (tryTotal - tryDiff) / 2;
        // double simHomeTryRate = rugbyLeagueMatchParams.getHomeTryRate().nextRandom();
        // double simAwayTryRate = rugbyLeagueMatchParams.getAwayTryRate().nextRandom();

        double simHomeLoseBoost = rugbyLeagueMatchParams.getHomeLoseBoost().nextRandom();
        double simAwayLoseBoost = rugbyLeagueMatchParams.getAwayLoseBoost().nextRandom();
        double powerBoostRate = rugbyLeagueMatchParams.getPowerBoostRate().nextRandom();

        matchFacts.reset(((RugbyLeagueMatchState) matchState).getCurrentPeriodPointsA(),
                        ((RugbyLeagueMatchState) matchState).getCurrentPeriodPointsB(),
                        ((RugbyLeagueMatchState) matchState).getCurrentPeriodTrysA(),
                        ((RugbyLeagueMatchState) matchState).getCurrentPeriodTrysB());

        do {
            boolean shootOutIncidentOccurred = false;
            boolean incidentOccurred = false;
            boolean resetMinorSinBinANeed = false;
            boolean resetMinorSinBinBNeed = false;

            double timeBasedFactor = GoalDistribution.getGoalDistribution(getRugbyLeagueMatchFormat().is80MinMatch(),
                            getRugbyLeagueMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs(),
                            simMatchState.getRugbyLeagueMatchStatus().getRugbyLeagueMatchStatus(), 0); // 0 is the
                                                                                                       // injure
                                                                                                       // time

            /* write something for checking rugby match status */
            if (simMatchState.getRugbyLeagueMatchStatus()
                            .getRugbyLeagueMatchStatus() == RugbyLeagueMatchStatusType.CONVERSION) { // If conversion

                // FIXME: needed to know conversion position

                double r = RandomNoGenerator.nextDouble();

                if (simMatchState.getRugbyLeagueMatchStatus().getTeamId() == TeamId.A) {
                    if (r < timeBasedFactor) {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.CONVERSION_GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        incidentOccurred = true;
                        if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                            matchFacts.addPointsCurrentPeriod(TeamId.A, 2);
                        }
                        if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                            matchFacts.setNextToScoreDropGoal(TeamId.A);
                    } else {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.CONVERSION_MISS);
                        matchIncident.setTeamId(TeamId.A);
                        incidentOccurred = true;
                    }

                } else if (simMatchState.getRugbyLeagueMatchStatus().getTeamId() == TeamId.B) {
                    if (r < timeBasedFactor) {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.CONVERSION_GOAL);
                        matchIncident.setTeamId(TeamId.B);
                        incidentOccurred = true;
                        if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                            matchFacts.addPointsCurrentPeriod(TeamId.B, 2);
                        }
                        if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                            matchFacts.setNextToScoreDropGoal(TeamId.B);
                    } else {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.CONVERSION_MISS);
                        matchIncident.setTeamId(TeamId.B);
                        incidentOccurred = true;
                    }
                } else {
                    throw new IllegalArgumentException(" Conversion team is not known ");
                }
                // This function is now executing in the match state
                // simMatchState.getRugbyLeagueMatchStatus().resetRugbyLeagueMatchStatus();
            } else if (simMatchState.getRugbyLeagueMatchStatus()
                            .getRugbyLeagueMatchStatus() == RugbyLeagueMatchStatusType.PENALTY) {
                // If penalty
                double r = RandomNoGenerator.nextDouble();
                if (simMatchState.getRugbyLeagueMatchStatus().getTeamId() == TeamId.A) {
                    if (r < timeBasedFactor) {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.PENALTY_GOAL);
                        matchIncident.setTeamId(TeamId.A);
                        incidentOccurred = true;
                        if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                            matchFacts.addPointsCurrentPeriod(TeamId.A, 3);
                        }
                        if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                            matchFacts.setNextToScoreDropGoal(TeamId.A);
                    } else {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.PENALTY_MISS);
                        matchIncident.setTeamId(TeamId.A);
                        incidentOccurred = true;
                    }
                } else if (simMatchState.getRugbyLeagueMatchStatus().getTeamId() == TeamId.B) {
                    if (r < timeBasedFactor) {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.PENALTY_GOAL);
                        matchIncident.setTeamId(TeamId.B);
                        incidentOccurred = true;
                        if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                            matchFacts.addPointsCurrentPeriod(TeamId.B, 3);
                        }
                        if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                            matchFacts.setNextToScoreDropGoal(TeamId.B);
                    } else {
                        matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.PENALTY_MISS);
                        matchIncident.setTeamId(TeamId.B);
                        incidentOccurred = true;
                    }
                } else {
                    throw new IllegalArgumentException(" Penalty team is not known ");
                }
                // simMatchState.getRugbyLeagueMatchStatus().resetRugbyLeagueMatchStatus();
            } else {
                double homeTryRate = (simHomeTryRate) * timeBasedFactor;
                double awayTryRate = (simAwayTryRate) * timeBasedFactor;

                double homeDropGoalRate = (simHomePointScoreRate
                                - (4 * simHomeTryRate + simHomeTryRate * 2 * conversionScoreRate)) * timeBasedFactor;
                double awayDropGoalRate = (simAwayPointScoreRate
                                - (4 * simAwayTryRate + simAwayTryRate * 2 * conversionScoreRate)) * timeBasedFactor;
                /*
                 * Set Ball and Field position infos
                 */
                BallPosition ballPosition = simMatchState.getBallPosition();
                /*
                 * adjust point score rate for the REST of MATCH
                 * 
                 * PointScoreRate - ThisTimeSlotPointExp + OriginalThisTimePointExp
                 */
                if (ballPosition.ballHoldingNow == TeamId.A) {
                    if (ballPosition.fieldPosition == FieldPositionType.B22MLINE_CENTRE) {
                        homeTryRate = Math.sqrt(homeTryRate);
                        homeDropGoalRate = Math.sqrt(homeDropGoalRate);
                        simHomePointScoreRate = (simHomePointScoreRate - homeTryRate * 4 - homeDropGoalRate
                                        + simHomePointScoreRate * timeBasedFactor);
                    } else if (ballPosition.fieldPosition == FieldPositionType.B10MLINE_CENTRE) {
                        homeDropGoalRate = Math.sqrt(homeDropGoalRate);
                        simHomePointScoreRate = (simHomePointScoreRate - homeTryRate * 4 - homeDropGoalRate
                                        + simHomePointScoreRate * timeBasedFactor);
                    }
                } else if (ballPosition.ballHoldingNow == TeamId.B) {
                    if (ballPosition.fieldPosition == FieldPositionType.A22MLINE_CENTRE) {
                        awayTryRate = Math.sqrt(awayTryRate);
                        awayDropGoalRate = Math.sqrt(awayDropGoalRate);
                        simAwayPointScoreRate = (simAwayPointScoreRate - awayTryRate * 4 - awayDropGoalRate
                                        + simAwayPointScoreRate * timeBasedFactor);
                    } else if (ballPosition.fieldPosition == FieldPositionType.A10MLINE_CENTRE) {
                        awayDropGoalRate = Math.sqrt(awayDropGoalRate);
                        simAwayPointScoreRate = (simAwayPointScoreRate - awayTryRate * 4 - awayDropGoalRate
                                        + simAwayPointScoreRate * timeBasedFactor);
                    }
                }
                // if position unknown do nothing
                if (isImminent) {
                    simMatchState.getBallPosition().resetBallPosition();
                    isImminent = false;
                }
                /*
                 * 10 mins sin bin
                 */
                if (simMatchState.getTenMinsSinBinA() > 0) {
                    /*
                     * reset sin bin for A
                     */
                    repeaterMinorA = repeaterCount(simMatchState.getTenMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinANeed = isSinBinNeedReset(simMatchState.getTenMinsSinBinTimer_A(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentA.setTeamId(TeamId.A);
                    minorPenaltyMatchIncidentA.setIncidentSubType(RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                if (simMatchState.getTenMinsSinBinB() > 0) {
                    /*
                     * reset sin bin for B
                     */
                    repeaterMinorB = 0;
                    repeaterMinorB = repeaterCount(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs());
                    resetMinorSinBinBNeed = isSinBinNeedReset(simMatchState.getTenMinsSinBinTimer_B(),
                                    simMatchState.getElapsedTimeSecs()); // two mins
                    minorPenaltyMatchIncidentB.setTeamId(TeamId.B);
                    minorPenaltyMatchIncidentB.setIncidentSubType(RugbyLeagueMatchIncidentType.TEN_MINS_PENALTY_END);
                }

                if ((simMatchState.getTenMinsSinBinA()) + simMatchState.getRedCardA() == 1) {
                    awayTryRate = awayTryRate * (powerBoostRate + 1);
                } else if (simMatchState.getTenMinsSinBinA() + simMatchState.getRedCardA() >= 2) {
                    awayTryRate = awayTryRate * (powerBoostRate + 1.15);
                }
                if (simMatchState.getTenMinsSinBinB() + simMatchState.getRedCardB() == 1) {
                    homeTryRate = homeTryRate * (powerBoostRate + 1);
                } else if (simMatchState.getTenMinsSinBinB() + simMatchState.getRedCardB() >= 2) {
                    homeTryRate = homeTryRate * (powerBoostRate + 1.15);
                }

                if (simMatchState.getPointsA() < simMatchState.getPointsB())
                    homeTryRate *= (1 + simHomeLoseBoost);
                if (simMatchState.getPointsA() > simMatchState.getPointsB())
                    awayTryRate *= (1 + simAwayLoseBoost);

                double rh = RandomNoGenerator.nextDouble();
                if (rh < homeTryRate) {
                    matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.TRY);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    simMatchState.getRugbyLeagueMatchStatus()
                                    .setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType.CONVERSION, TeamId.A);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                        matchFacts.addPointsCurrentPeriod(TeamId.A, 4);
                    }

                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.A);
                } else if (rh < (homeTryRate + homeDropGoalRate)) {
                    matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.DROP_GOAL);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.A, 1);

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.A);

                } else if (rh < (awayTryRate + homeTryRate + homeDropGoalRate)) {
                    matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.TRY);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    simMatchState.getRugbyLeagueMatchStatus()
                                    .setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType.CONVERSION, TeamId.B);

                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, 4);
                    if (matchFacts.getNextToScoreTry() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreTry(TeamId.B);

                } else if (rh < (awayTryRate + homeTryRate + homeDropGoalRate + awayDropGoalRate)) {
                    matchIncident.setIncidentSubType(RugbyLeagueMatchIncidentType.DROP_GOAL);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addPointsCurrentPeriod(TeamId.B, 1);

                    if (matchFacts.getNextToScoreDropGoal() == TeamId.UNKNOWN)
                        matchFacts.setNextToScoreDropGoal(TeamId.B);
                }
            }

            if (incidentOccurred) {
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            }

            if (resetMinorSinBinANeed) {
                if (repeaterMinorA > simMatchState.getTenMinsSinBinA()) // in case goal happens same time
                    repeaterMinorA = simMatchState.getTenMinsSinBinA();

                for (int i = 0; i < repeaterMinorA; i++) {
                    minorPenaltyMatchIncidentA.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(minorPenaltyMatchIncidentA, false);
                }
                repeaterMinorA = 0;
            }
            if (resetMinorSinBinBNeed) {
                for (int i = 0; i < repeaterMinorB; i++) {

                    if (repeaterMinorB > simMatchState.getTenMinsSinBinB()) // in case goal happens same time
                        repeaterMinorB = simMatchState.getTenMinsSinBinB();

                    minorPenaltyMatchIncidentB.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(minorPenaltyMatchIncidentB, false);
                }
                repeaterMinorB = 0;
            }

            if (!shootOutIncidentOccurred) {
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);
            }
        } while (simMatchState.getMatchPeriod() != RugbyLeagueMatchPeriod.MATCH_COMPLETED);

        ((RugbyLeagueMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
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
        this.monteCarloMarkets.consolidate(((RugbyLeagueMatch) match).monteCarloMarkets);
    }

}
