package ats.algo.sport.afl;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.afl.AflMatchIncident.AflMatchIncidentType;
import ats.algo.sport.afl.AflMatchIncident.FieldPositionType;

public class AflMatch extends MonteCarloMatch {

    /**
     * @author Jin
     * 
     */
    private static final int timeSliceSecs = 10;
    private AflMatchState simMatchState;
    private AflMatchFacts matchFacts;
    private AflMatchIncident matchIncident;

    public AflMatch(AflMatchFormat matchFormat, AflMatchState matchState, AflMatchParams matchParams) {

        super((MatchFormat) matchFormat, (AlgoMatchState) matchState, (MatchParams) matchParams);
        monteCarloMarkets = new AflMatchMarketsFactory(matchState);
        this.simMatchState = (AflMatchState) matchState.copy();
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new AflMatchFacts();
        this.matchIncident = new AflMatchIncident();

    }

    @Override
    public MonteCarloMatch clone() {
        AflMatch cc = new AflMatch((AflMatchFormat) matchFormat, (AflMatchState) matchState,
                        (AflMatchParams) matchParams);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new AflMatchMarketsFactory((AflMatchState) matchState);
    }

    private AflMatchFormat getAflMatchFormat() {
        return (AflMatchFormat) matchFormat;
    }

    @Override
    public void playMatch() {
        AflMatchState startingMatchState = (AflMatchState) matchState;
        boolean isImminent = true;

        simMatchState.setEqualTo(matchState);
        if (simMatchState.getMatchPeriod() == AflMatchPeriod.PREMATCH) {
            simMatchState.setMatchPeriod(AflMatchPeriod.IN_FIRST_PERIOD);
        }
        AflMatchParams aflMatchParams = (AflMatchParams) matchParams;
        double simHomeGoalScoreRate = 0;
        double simAwayGoalScoreRate = 0;
        double simHomePointScoreRate = 0;
        double simAwayPointScoreRate = 0;
        double goalToTal = 0;
        double scoreToTal = 0;
        double scoreDiff = 0;
        double goalDiff = 0;

        do {
            scoreToTal = aflMatchParams.getTotalScoreRate().nextRandom();
            scoreDiff = aflMatchParams.getSupremacyScoreRate().nextRandom();
            simHomePointScoreRate = (scoreToTal + scoreDiff) / 2;
            simAwayPointScoreRate = (scoreToTal - scoreDiff) / 2;
            goalToTal = aflMatchParams.getTotalGoalRate().nextRandom();
            goalDiff = aflMatchParams.getSupremacyGoalRate().nextRandom();
            simHomeGoalScoreRate = (goalToTal + goalDiff) / 2;
            simAwayGoalScoreRate = (goalToTal - goalDiff) / 2;
        } while (simHomePointScoreRate < simHomeGoalScoreRate * 6 || simAwayPointScoreRate < simAwayGoalScoreRate * 6);

        // double simHomeBehindScoreRate = simHomePointScoreRate - 6 * simHomeGoalScoreRate;
        // double simAwayBehindScoreRate = simAwayPointScoreRate - 6 * simAwayGoalScoreRate;

        matchFacts.reset(((AflMatchState) matchState).getCurrentPeriodPointsA(),
                        ((AflMatchState) matchState).getCurrentPeriodPointsB(),
                        ((AflMatchState) matchState).getCurrentPeriodGoalsA(),
                        ((AflMatchState) matchState).getCurrentPeriodGoalsB(),
                        ((AflMatchState) matchState).getPeriodNo(),
                        ((AflMatchState) matchState).generatingOddEvenMarketForQuarterNo(),
                        ((AflMatchState) matchState)
                                        .checkListForScoreXXPoints(((AflMatchState) matchState).getPeriodNo(), 10),
                        ((AflMatchState) matchState)
                                        .checkListForScoreXXPoints(((AflMatchState) matchState).getPeriodNo(), 15),
                        ((AflMatchState) matchState)
                                        .checkListForScoreXXPoints(((AflMatchState) matchState).getPeriodNo(), 20),
                        ((AflMatchState) matchState).getMatchPeriod(),
                        ((AflMatchState) matchState).getTimeOfFirstGoalMatch(),
                        ((AflMatchState) matchState).getTimeOfFirstGoalQuarter());

        matchFacts.restFirstToFlags(((AflMatchState) matchState).getFirstTo3B(),
                        ((AflMatchState) matchState).getFirstTo4B(), ((AflMatchState) matchState).getFirstTo5B(),
                        ((AflMatchState) matchState).getFirstTo6B(), ((AflMatchState) matchState).getFirstTo3G(),
                        ((AflMatchState) matchState).getFirstTo4G(), ((AflMatchState) matchState).getFirstTo5G(),
                        ((AflMatchState) matchState).getFirstTo6G(), ((AflMatchState) matchState).getGoalsA(),
                        ((AflMatchState) matchState).getGoalsB(), ((AflMatchState) matchState).getBehindsA(),
                        ((AflMatchState) matchState).getBehindsB());

        // matchFacts.resetFirstScoring(((AflMatchState) matchState).getFirstScoringPlay2Quarter(),((AflMatchState)
        // matchState).getFirstScoringPlay3Quarter()
        // ,((AflMatchState) matchState).getFirstScoringPlay4Quarter());

        do {
            boolean freeShootIncidentOccurred = false;
            double timeBasedFactor = GoalDistribution.getGoalDistribution(getAflMatchFormat().getNormalTimeMinutes(),
                            getAflMatchFormat().getExtraTimeMinutes(), simMatchState.getMatchPeriod(),
                            simMatchState.getElapsedTimeSecs()); // 0 is the injure
                                                                 // time
            double homeGoalRate = simHomeGoalScoreRate * timeBasedFactor;
            double awayGoalRate = simAwayGoalScoreRate * timeBasedFactor;

            double homeBehindRate = (simHomePointScoreRate - 6 * simHomeGoalScoreRate) * timeBasedFactor;
            double awayBehindRate = (simAwayPointScoreRate - 6 * simAwayGoalScoreRate) * timeBasedFactor;

            BallPosition ballPosition = simMatchState.getBallPosition();

            /*
             * adjust point score rate for the REST of MATCH
             * 
             * PointScoreRate - ThisTimeSlotPointExp + OriginalThisTimePointExp
             */
            if (ballPosition.ballHoldingNow == TeamId.A) {
                if (ballPosition.fieldPosition == FieldPositionType.B35) {
                    homeGoalRate = Math.sqrt(homeGoalRate);
                    homeBehindRate = Math.sqrt(homeBehindRate);
                    simHomePointScoreRate = (simHomePointScoreRate - homeGoalRate * 6 - homeBehindRate
                                    + simHomePointScoreRate * timeBasedFactor);
                } else if (ballPosition.fieldPosition == FieldPositionType.B50) {
                    homeBehindRate = Math.sqrt(homeBehindRate);
                    simHomePointScoreRate = (simHomePointScoreRate - homeGoalRate * 6 - homeBehindRate
                                    + simHomePointScoreRate * timeBasedFactor);
                }
            } else if (ballPosition.ballHoldingNow == TeamId.B) {
                if (ballPosition.fieldPosition == FieldPositionType.A35) {
                    awayGoalRate = Math.sqrt(awayGoalRate);
                    awayBehindRate = Math.sqrt(awayBehindRate);
                    simAwayPointScoreRate = (simAwayPointScoreRate - awayGoalRate * 6 - awayBehindRate
                                    + simAwayPointScoreRate * timeBasedFactor);
                } else if (ballPosition.fieldPosition == FieldPositionType.A50) {
                    awayBehindRate = Math.sqrt(awayBehindRate);
                    simAwayPointScoreRate = (simAwayPointScoreRate - awayGoalRate * 6 - awayBehindRate
                                    + simAwayPointScoreRate * timeBasedFactor);
                }
            }
            // if position unknown do nothing

            if (isImminent) {
                simMatchState.getBallPosition().resetBallPosition();
                isImminent = false;
            }

            double rh = RandomNoGenerator.nextDouble();

            boolean incidentOccurred = false;
            if (rh < homeBehindRate) {
                matchIncident.setIncidentSubType(AflMatchIncidentType.ONE_POINTS_SCORED);
                matchIncident.setTeamId(TeamId.A);
                incidentOccurred = true;

                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo()) {
                    matchFacts.addPointsCurrentPeriod(TeamId.A, 1);
                }

                if (matchFacts.getNextToScoreBehind() == TeamId.UNKNOWN)
                    matchFacts.setNextToScoreBehind(TeamId.A);

            } else if (rh < (awayBehindRate + homeBehindRate)) {
                matchIncident.setIncidentSubType(AflMatchIncidentType.ONE_POINTS_SCORED);
                matchIncident.setTeamId(TeamId.B);
                incidentOccurred = true;

                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    matchFacts.addPointsCurrentPeriod(TeamId.B, 1);

                if (matchFacts.getNextToScoreBehind() == TeamId.UNKNOWN)
                    matchFacts.setNextToScoreBehind(TeamId.B);

            } else if (rh < (awayBehindRate + homeBehindRate + homeGoalRate)) {
                matchIncident.setIncidentSubType(AflMatchIncidentType.SIX_POINTS_SCORED);
                matchIncident.setTeamId(TeamId.A);

                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    if (((AflMatchState) matchState).getTimeOfFirstGoalQuarter() == -1
                                    && matchFacts.getTimeOfFirstGoalQuarter() == -1) {
                        matchFacts.setTimeOfFirstGoalQuarter(simMatchState.getElapsedTimeThisPeriodSecs());
                    }
                incidentOccurred = true;
                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    matchFacts.addPointsCurrentPeriod(TeamId.A, 6);

                if (matchFacts.getNextToScoreGoal() == TeamId.UNKNOWN)
                    matchFacts.setNextToScoreGoal(TeamId.A);

            } else if (rh < (awayBehindRate + homeBehindRate + homeGoalRate + awayGoalRate)) {
                matchIncident.setIncidentSubType(AflMatchIncidentType.SIX_POINTS_SCORED);
                matchIncident.setTeamId(TeamId.B);

                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())// is the current quarter?
                    if (((AflMatchState) matchState).getTimeOfFirstGoalQuarter() == -1
                                    && matchFacts.getTimeOfFirstGoalQuarter() == 1) {
                        matchFacts.setTimeOfFirstGoalQuarter(simMatchState.getElapsedTimeThisPeriodSecs());
                    }
                incidentOccurred = true;
                if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                    matchFacts.addPointsCurrentPeriod(TeamId.B, 6);

                if (matchFacts.getNextToScoreGoal() == TeamId.UNKNOWN)
                    matchFacts.setNextToScoreGoal(TeamId.B);
            }

            if (simMatchState.getMatchPeriod() == AflMatchPeriod.AT_FIRST_PERIOD_END
                            || simMatchState.getMatchPeriod() == AflMatchPeriod.AT_SECOND_PERIOD_END
                            || simMatchState.getMatchPeriod() == AflMatchPeriod.AT_THIRD_PERIOD_END
                            || simMatchState.getMatchPeriod() == AflMatchPeriod.AT_FULL_TIME
                            || simMatchState.getMatchPeriod() == AflMatchPeriod.AT_EXTRA_PERIOD_END)
                incidentOccurred = false;

            if (incidentOccurred) {
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            }

            if (!freeShootIncidentOccurred) {
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);
            }
        } while (simMatchState.getMatchPeriod() != AflMatchPeriod.MATCH_COMPLETED);

        ((AflMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((AflMatch) match).monteCarloMarkets);
    }

}
