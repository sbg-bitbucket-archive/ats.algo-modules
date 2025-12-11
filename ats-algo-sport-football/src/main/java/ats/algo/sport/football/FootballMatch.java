package ats.algo.sport.football;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MonteCarloMatch;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballShootoutInfo;

public class FootballMatch extends MonteCarloMatch {

    private static final int timeSliceSecs = 10;
    // private static final double CORNER_GOAL_LINK_BOOST = 0.005;
    private static final int CORNER_PHASE_TIMER = 40;
    private static final double CORNER_BOOST_RATE = 0.105205 / (CORNER_PHASE_TIMER / timeSliceSecs); //
    private static final double CORNER_GOAL_LINK_BOOST = 0.0469 / (CORNER_PHASE_TIMER / timeSliceSecs);


    private static final int MAX_ITERATIONS = 5000;

    private FootballMatchState simMatchState;
    private FootballMatchFacts matchFacts;
    private FootballMatchIncident matchIncident;
    private FootballMatchPeriod matchPeriod;

    private int v3ColIndex = 0;

    /**
     * 
     * @param matchFormat
     * @param matchState
     * @param matchParams
     * @param isForParamFinder
     */
    public FootballMatch(FootballMatchFormat matchFormat, FootballMatchState matchState,
                    FootballMatchParams matchParams, boolean isForParamFinder) {
        super(matchFormat, matchState, matchParams);
        monteCarloMarkets = new FootballMatchMarketsFactory(matchState, isForParamFinder);
        this.simMatchState = (FootballMatchState) matchState.copy();
        super.isForParamFinder = isForParamFinder;
        /* Find index for v3 goal dist */
        double goalsTotal = matchParams.getGoalTotal().getMean();
        double goalsDiff = matchParams.getGoalSupremacy().getMean();

        v3ColIndex = findColForV3Dist(goalsTotal, goalsDiff);
        /*
         * create the container for holding match facts just once rather than every time playMatch is executed -
         * improves performance
         */
        this.matchFacts = new FootballMatchFacts();
        this.matchIncident = new FootballMatchIncident();
    }

    private int findColForV3Dist(double totalG, double supmG) {
        int col = 0;
        if (totalG < 2) {
            col = 1;
        } else if (totalG < 3.25) {
            if (supmG <= 1.5) {
                col = 2;
            } else {
                col = 4;
            }
        } else if (totalG > 4) {
            col = 6;

        } else { // total goal <4 >3.25
            if (supmG <= 1.5) {
                col = 3;
            } else {
                col = 5;
            }
        }
        return col;
    }

    @Override
    public MonteCarloMatch clone() {
        FootballMatch cc = new FootballMatch((FootballMatchFormat) matchFormat, (FootballMatchState) matchState,
                        (FootballMatchParams) matchParams, isForParamFinder);
        return cc;
    }

    @Override
    public void resetStatistics() {
        monteCarloMarkets = new FootballMatchMarketsFactory((FootballMatchState) matchState, isForParamFinder);
    }

    @Override
    public void playMatch() {
        FootballMatchState startingMatchState = (FootballMatchState) matchState;

        simMatchState.setEqualTo(matchState);
        matchPeriod = simMatchState.getMatchPeriod();
        /*
         * start in play if pre-match
         */
        // if (matchPeriod == FootballMatchPeriod.PREMATCH)
        // simMatchState.setMatchPeriod(FootballMatchPeriod.IN_FIRST_HALF);
        FootballMatchParams footballMatchParams = (FootballMatchParams) matchParams;
        FootballMatchFormat footballMatchFormat = (FootballMatchFormat) matchFormat;
        boolean is80MinMatch = footballMatchFormat.is80MinMatch();



        double goalTimer = 0;
        TeamId cornerServerTeam = TeamId.UNKNOWN;
        double cornerTimer = 0; // 20 seconds corner timer

        if (startingMatchState.getElapsedTimeAtLastCornerSecs() != -1) {
            // System.out.println(startingMatchState.getElapsedTimeAtLastCornerSecs());
            int timeDifference = startingMatchState.getElapsedTimeSecs()
                            - startingMatchState.getElapsedTimeAtLastCornerSecs();
            if (timeDifference <= CORNER_PHASE_TIMER * 2) {
                cornerTimer = CORNER_PHASE_TIMER * 2 - timeDifference;
                cornerServerTeam = startingMatchState.getTeamScoringLastCorner();
            }
            // System.out.println("corner timmer "+ cornerTimer);
        }
        double goalsTotal = footballMatchParams.getGoalTotal().getBiasAdjustedMean() - 0.5 > 0
                        ? footballMatchParams.getGoalTotal().getBiasAdjustedMean() - 0.5
                        : 0.01;
        double goalsDiff = footballMatchParams.getGoalSupremacy().getBiasAdjustedMean();
        double goalsDevn = footballMatchParams.getGoalsDevn().getBiasAdjustedMean();
        double simHomeScoreRate = RandomNoGenerator.nextNormal((goalsTotal + goalsDiff) / 2, goalsDevn);
        double simAwayScoreRate = RandomNoGenerator.nextNormal((goalsTotal - goalsDiff) / 2, goalsDevn);
        if (simHomeScoreRate < 0.0)
            simHomeScoreRate = 0.0;
        if (simAwayScoreRate < 0.0)
            simAwayScoreRate = 0.0;
        /*
         * just get the means for the minor model params rather than normally distributed RV
         */
        double goalsDiffAdjFactor = footballMatchParams.getTargetGoalBoost().getBiasAdjustedMean();
        double simHomeLoseBoost = footballMatchParams.getHomeLoseBoost().getBiasAdjustedMean();
        double simAwayLoseBoost = footballMatchParams.getAwayLoseBoost().getBiasAdjustedMean();
        double cornersTotal = footballMatchParams.getCornerTotal().nextRandom();
        double cornersDiff = footballMatchParams.getCornerSupremacy().nextRandom();
        double simHomeCornerRate = (cornersTotal + cornersDiff) / 2;
        double simAwayCornerRate = (cornersTotal - cornersDiff) / 2;
        double cardsTotal = footballMatchParams.getCardTotal().nextRandom();
        double cardsDiff = footballMatchParams.getCardSupremacy().nextRandom();
        double simHomeCardRate = (cardsTotal + cardsDiff) / 2;
        double simAwayCardRate = (cardsTotal - cardsDiff) / 2;
        double simRedCardProbRate = footballMatchParams.getRedCardProb().nextRandom();
        double simRedCardHomeProbRate = simRedCardProbRate * simHomeCardRate;
        double simRedCardAwayProbRate = simRedCardProbRate * simAwayCardRate;
        double simYellowCardHomeScoreRate = simHomeCardRate - simRedCardHomeProbRate;
        double simYellowCardAwayScoreRate = simAwayCardRate - simRedCardAwayProbRate;

        double timeBasedFactor = GoalDistribution.getGoalDistribution(matchPeriod, simMatchState.getElapsedTimeSecs(),
                        simMatchState.getInjuryTimeSecs(), v3ColIndex, true); // v3
        // true,
        // System.out.println("----------------------" + timeBasedFactor + "--" + simHomeScoreRate + "---"
        // + simAwayScoreRate);
        double timeBasedFactorCard = GoalDistribution.getCardDistribution(is80MinMatch, matchPeriod,
                        simMatchState.getElapsedTimeSecs(), simMatchState.getInjuryTimeSecs());

        double timeBasedFactorCorner = GoalDistribution.getCornerDistribution(is80MinMatch, matchPeriod,
                        simMatchState.getElapsedTimeSecs(), simMatchState.getInjuryTimeSecs());

        matchFacts.reset(startingMatchState.getCurrentPeriodGoalsA(), startingMatchState.getCurrentPeriodGoalsB(),
                        startingMatchState.getCurrentPeriodCornersA(), startingMatchState.getCurrentPeriodCornersB(),
                        startingMatchState.getFiveMinsNo(), startingMatchState.getPeriodNo());
        int simCount = 0;
        do {
            if (matchPeriod.equals(FootballMatchPeriod.IN_SHOOTOUT)) { // get team started shootout first
                int counter = simMatchState.getShootOutTimeCounter() / 2;
                double shootOutProb = GoalDistribution.getCardDistributionShootOut(counter);

                double homeScoreRate = shootOutProb;
                double awayScoreRate = shootOutProb;
                double rh = RandomNoGenerator.nextDouble();

                matchIncident.setIncidentSubType(FootballMatchIncidentType.SHOOTOUT_MISS);
                FootballShootoutInfo penaltyStatus = simMatchState.getPenaltyInfo();
                if (penaltyStatus.getStartedPenalty() == TeamId.UNKNOWN) {
                    // random the start shooting
                    boolean rs = RandomNoGenerator.nextBool();
                    if (rs)
                        penaltyStatus.setStartedPenalty(TeamId.A);
                    else
                        penaltyStatus.setStartedPenalty(TeamId.B);
                }

                if (penaltyStatus.getShootingNext() == TeamId.A) { // team A start shoot out
                    matchIncident.setTeamId(TeamId.A);
                    if (rh < homeScoreRate) {
                        matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                    }
                    matchIncident.setTeamId(TeamId.A);
                } else if (penaltyStatus.getShootingNext() == TeamId.B) { // team B start
                    // shoot out
                    matchIncident.setTeamId(TeamId.B);
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                    }
                    matchIncident.setTeamId(TeamId.B);

                }

                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            } else if (simMatchState.getPenaltyStatus().equals(TeamId.A)
                            || simMatchState.getPenaltyStatus().equals(TeamId.B)) {

                double homeScoreRate = 0.75;
                double awayScoreRate = 0.75;
                double rh = RandomNoGenerator.nextDouble();
                matchIncident.setIncidentSubType(FootballMatchIncidentType.PENALTY_MISSED);
                if (simMatchState.getPenaltyStatus().equals(TeamId.A)) { // A penalty
                    matchIncident.setTeamId(TeamId.A);
                    if (rh < homeScoreRate) {
                        matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.A);
                    }
                } else if (simMatchState.getPenaltyStatus().equals(TeamId.B)) { // B penalty // shoot out
                    matchIncident.setTeamId(TeamId.B);
                    if (rh < awayScoreRate) {
                        matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                        matchIncident.setTeamId(TeamId.B);
                    }
                }
                matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                simMatchState.updateStateForIncident(matchIncident, false);
            } else {

                int elapsedTimeSecs = simMatchState.getElapsedTimeSecs();
                int injuryTimeSecs = simMatchState.getInjuryTimeSecs();
                if (elapsedTimeSecs % 60 == 0) {
                    timeBasedFactor = GoalDistribution.getGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs,
                                    v3ColIndex, true); // v3
                                                       // true,

                    timeBasedFactorCard = GoalDistribution.getCardDistribution(is80MinMatch, matchPeriod,
                                    elapsedTimeSecs, injuryTimeSecs);

                    timeBasedFactorCorner = GoalDistribution.getCornerDistribution(is80MinMatch, matchPeriod,
                                    elapsedTimeSecs, injuryTimeSecs);
                }
                double homeCornerRate = (1 / (1 + (CORNER_BOOST_RATE + CORNER_BOOST_RATE * CORNER_BOOST_RATE)))
                                * simHomeCornerRate * timeBasedFactorCorner;//
                double awayCornerRate = (1 / (1 + (CORNER_BOOST_RATE + CORNER_BOOST_RATE * CORNER_BOOST_RATE)))
                                * simAwayCornerRate * timeBasedFactorCorner;//

                double homeScoreRate =
                                (adjustedScoreRate(simHomeScoreRate, simMatchState.getGoalsA(), goalsDiffAdjFactor)
                                                - CORNER_GOAL_LINK_BOOST * simHomeCornerRate) * timeBasedFactor;
                double awayScoreRate =
                                (adjustedScoreRate(simAwayScoreRate, simMatchState.getGoalsB(), goalsDiffAdjFactor)
                                                - CORNER_GOAL_LINK_BOOST * simAwayCornerRate) * timeBasedFactor;
                /**
                 * If now we are in corner boost period
                 */
                if (cornerTimer == 0) {
                    // do nothing
                } else if (cornerTimer <= CORNER_PHASE_TIMER && cornerTimer > 0) {
                    // next corner rate increase, home goal rate increase, away
                    // goal rate become 0 in next 20 seconds
                    cornerTimer -= timeSliceSecs;
                    if (cornerServerTeam == TeamId.A) {
                        homeCornerRate = homeCornerRate + CORNER_BOOST_RATE;
                        homeScoreRate = homeScoreRate + CORNER_GOAL_LINK_BOOST;
                        awayScoreRate = 0;
                        awayCornerRate = 0;
                    } else if (cornerServerTeam == TeamId.B) {
                        awayCornerRate = awayCornerRate + CORNER_BOOST_RATE;
                        awayScoreRate = awayScoreRate + CORNER_GOAL_LINK_BOOST;
                        homeScoreRate = 0;
                        homeCornerRate = 0;
                    }
                } else if (cornerTimer <= CORNER_PHASE_TIMER * 2 && cornerTimer > CORNER_PHASE_TIMER) {
                    cornerTimer -= timeSliceSecs;
                    homeCornerRate = 0;
                    homeScoreRate = 0;
                    awayScoreRate = 0;
                    awayCornerRate = 0;
                }


                if (goalTimer > 0) {
                    // next corner rate increase, home goal rate increase, away
                    // goal rate become 0 in next 20 seconds
                    goalTimer = -timeSliceSecs;
                    homeCornerRate = 0;
                    homeScoreRate = 0;
                    awayScoreRate = 0;
                    awayCornerRate = 0;
                }

                double homeYellowCardRate = simYellowCardHomeScoreRate * timeBasedFactorCard;
                double awayYellowCardRate = simYellowCardAwayScoreRate * timeBasedFactorCard;
                double homeRedCardRate = simRedCardHomeProbRate * timeBasedFactorCard;
                double awayRedCardRate = simRedCardAwayProbRate * timeBasedFactorCard;
                int goalsA = simMatchState.getGoalsA();
                int goalsB = simMatchState.getGoalsB();
                // System.out.println(homeScoreRate+"--"+awayScoreRate+"--"+homeCornerRate+"--"+awayCornerRate);
                if (goalsA < goalsB)
                    homeScoreRate *= (1 + simHomeLoseBoost);
                if (goalsA > goalsB)
                    awayScoreRate *= (1 + simAwayLoseBoost);
                /*
                 * ensure all rates are >= 0
                 */
                if (homeScoreRate < 0.0)
                    homeScoreRate = 0.0;
                if (awayScoreRate < 0.0)
                    awayScoreRate = 0.0;
                if (homeCornerRate < 0.0)
                    homeCornerRate = 0.0;
                if (awayCornerRate < 0.0)
                    awayCornerRate = 0.0;
                if (homeYellowCardRate < 0.0)
                    homeYellowCardRate = 0.0;
                if (awayYellowCardRate < 0.0)
                    awayYellowCardRate = 0.0;
                if (homeRedCardRate < 0.0)
                    homeRedCardRate = 0.0;
                if (awayRedCardRate < 0.0)
                    awayRedCardRate = 0.0;

                double r = RandomNoGenerator.nextDouble();
                boolean incidentOccurred = false;
                if (r > (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate + homeYellowCardRate
                                + awayYellowCardRate + homeRedCardRate + awayRedCardRate)) {
                    // do nothing
                } else if (r < homeScoreRate) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                    goalTimer = 10;
                    cornerTimer = 0;
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.A);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.A);
                } else if (r < (homeScoreRate + awayScoreRate)) { // i
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.GOAL);
                    goalTimer = 10;
                    cornerTimer = 0;
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    if (matchFacts.getNextToScore() == TeamId.UNKNOWN)
                        matchFacts.setNextToScore(TeamId.B);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addScoreCurrentPeriod(TeamId.B);
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.CORNER);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                    goalTimer = 0;
                    cornerTimer = CORNER_PHASE_TIMER * 2; // 20 seconds corner time span
                    cornerServerTeam = TeamId.A;
                    // next corner rate increase, home goal rate increas, away
                    // goal rate become 0 in next 20 seconds
                    if (matchFacts.getNextToCorner() == TeamId.UNKNOWN)
                        matchFacts.setNextToCorner(TeamId.A);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addCornerCurrentPeriod(TeamId.A);
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.CORNER);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                    goalTimer = 0;
                    cornerTimer = CORNER_PHASE_TIMER * 2; // 20 seconds corner time span
                    cornerServerTeam = TeamId.B;
                    if (matchFacts.getNextToCorner() == TeamId.UNKNOWN)
                        matchFacts.setNextToCorner(TeamId.B);
                    if (simMatchState.getPeriodNo() == startingMatchState.getPeriodNo())
                        matchFacts.addCornerCurrentPeriod(TeamId.B);
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate + homeYellowCardRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.YELLOW_CARD);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate + homeYellowCardRate
                                + awayYellowCardRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.YELLOW_CARD);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate + homeYellowCardRate
                                + awayYellowCardRate + homeRedCardRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.RED_CARD);
                    matchIncident.setTeamId(TeamId.A);
                    incidentOccurred = true;
                } else if (r < (homeScoreRate + awayScoreRate + homeCornerRate + awayCornerRate + homeYellowCardRate
                                + awayYellowCardRate + homeRedCardRate + awayRedCardRate)) {
                    matchIncident.setIncidentSubType(FootballMatchIncidentType.RED_CARD);
                    matchIncident.setTeamId(TeamId.B);
                    incidentOccurred = true;
                }

                if (incidentOccurred) {
                    matchIncident.setElapsedTime(simMatchState.getElapsedTimeSecs());
                    simMatchState.updateStateForIncident(matchIncident, false);
                }
                simMatchState.incrementSimulationElapsedTime(timeSliceSecs);

                // if(simMatchState.isNormalTimeMatchCompleted()){
                // matchFacts.setNormalTimeGoalsA(simMatchState.getGoalsA());
                // matchFacts.setNormalTimeGoalsB(simMatchState.getGoalsB());
                // }
                if (simCount == MAX_ITERATIONS)
                    throw new IllegalStateException("Max allowed iterations exceeded ");
            }
            matchPeriod = simMatchState.getMatchPeriod();
            simCount++;

        } while (simMatchState.getMatchPeriod() != FootballMatchPeriod.MATCH_COMPLETED);
        ((FootballMatchMarketsFactory) monteCarloMarkets).updateStats(simMatchState, matchFacts);
    }

    /*
     * calculate the adjusted score rate to apply to the
     */
    private double adjustedScoreRate(double simScoreRate, int goals, double goalsDiffAdjFactor) {
        double adjFactor = 1 + (simScoreRate - goals) * goalsDiffAdjFactor;
        /*
         * cap the adjustment factor to be between 0.2 and 1.8
         */
        if (adjFactor < 0.2)
            adjFactor = 0.2;
        if (adjFactor > 1.8)
            adjFactor = 1.8;
        return simScoreRate * adjFactor;
    }

    @Override
    public void consolidateStats(MonteCarloMatch match) {
        this.monteCarloMarkets.consolidate(((FootballMatch) match).monteCarloMarkets);
    }

}
