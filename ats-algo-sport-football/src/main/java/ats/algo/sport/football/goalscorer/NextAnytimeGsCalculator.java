package ats.algo.sport.football.goalscorer;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.common.PlayerInfo;
import ats.algo.core.common.PlayerStatus;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;
import ats.core.AtsBean;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;



/**
 * Calculates nextGoalScorer and anytime goal scorer probabilities
 *
 */
public class NextAnytimeGsCalculator extends AtsBean {

    private static final boolean DEBUG_ON = false;
    private static final int PROBS_DECIMAL_PLACES = 4;

    /**
     * Calculates the next and anytime goal scorer markets
     * 
     * @param csMarket
     * @param matchState
     * @param matchParams
     * @return
     */
    public static Map<String, Market> calculate(Market csMarket, FootballMatchState matchState,
                    FootballMatchParams matchParams) {

        Logger log = LoggerFactory.getLogger(NextAnytimeGsCalculator.class);
        /*
         * log all the input data
         */
        if (DEBUG_ON) {
            log.info("calculateGsMarkets input data:");
            log.info("Correct score mkt:\n" + csMarket);
            log.info("FootballMatchState:\n" + matchState);
            log.info("FootballMatchParams:\n" + matchParams);
        }
        /*
         * get the currentScore into a form in which we can use it and calc probs of next to score
         */

        ScoreInfo currentScoreInfo = new ScoreInfo(matchState.getGoalsA(), matchState.getGoalsB(), 1.0);
        Map<String, ScoreInfo> currentInPlayCorrectScoreInfo = generateScoreInfoMap(csMarket, currentScoreInfo);

        double probNeitherTeamToScore = 0.0;
        @SuppressWarnings("unused")
        double probNextToScoreTeamA = 0.0;
        @SuppressWarnings("unused")
        double probNextToScoreTeamB = 0.0;
        for (ScoreInfo scoreInfo : currentInPlayCorrectScoreInfo.values()) {
            double goalsA = (double) scoreInfo.goalsToGoA(currentScoreInfo);
            double goalsB = (double) scoreInfo.goalsToGoB(currentScoreInfo);
            if (goalsA == 0 && goalsB == 0) {
                probNeitherTeamToScore += scoreInfo.prob;
            } else {
                probNextToScoreTeamA += scoreInfo.prob * (goalsA / (goalsA + goalsB));
                probNextToScoreTeamB += scoreInfo.prob * (goalsB / (goalsA + goalsB));
            }
        }
        /*
         * generate the structures to hold the teams and the probs of each player scoring given that the team scores and
         * they are on the pitch;
         */
        Map<String, Double> playersA = getProbsForPlayersOnPitch(TeamId.A, matchState.getTeamSheet(),
                        matchParams.getIndividualParams());
        Map<String, Double> playersB = getProbsForPlayersOnPitch(TeamId.B, matchState.getTeamSheet(),
                        matchParams.getIndividualParams());

        /*
         * initialise the structures to hold the calculated selection probs
         */
        Map<String, Selection> nextToScore = new LinkedHashMap<String, Selection>(22);
        Map<String, Selection> anytimeScore = new LinkedHashMap<String, Selection>(22);
        for (String key : playersA.keySet()) {
            nextToScore.put(key, new Selection(TeamId.A));
            anytimeScore.put(key, new Selection(TeamId.A));
        }
        for (String key : playersB.keySet()) {
            nextToScore.put(key, new Selection(TeamId.B));
            anytimeScore.put(key, new Selection(TeamId.B));
        }
        if (DEBUG_ON) {
            log.debug(String.format("    Prob no score: %.3f", probNeitherTeamToScore));
            log.debug(String.format("    Prob teamA scores next: %.3f", probNextToScoreTeamA));
            log.debug(String.format("    Prob teamB scores next: %.3f", probNextToScoreTeamB));
        }
        /*
         * do the calcs
         */
        for (Entry<String, Selection> e : nextToScore.entrySet()) {
            /*
             * loop for each player
             */
            String player = e.getKey();

            Selection nextGoalSelection = e.getValue();
            Selection anytimeGoalSelection = anytimeScore.get(player);
            TeamId playerTeam = nextGoalSelection.teamId;
            double probPlayerScoresGivenTeamScores;
            if (playerTeam == TeamId.A)
                probPlayerScoresGivenTeamScores = playersA.get(player);
            else
                probPlayerScoresGivenTeamScores = playersB.get(player);
            nextGoalSelection.prob = 0;
            anytimeGoalSelection.prob = 0;
            double probPlayersTeamScoresNext = 0;
            for (ScoreInfo scoreInfo : currentInPlayCorrectScoreInfo.values()) {
                double goalsToGoA = scoreInfo.goalsToGoA(currentScoreInfo);
                double goalsToGoB = scoreInfo.goalsToGoB(currentScoreInfo);
                double goalsToGo;
                double probPlayersTeamScoresNextGivenScore;
                if (goalsToGoA + goalsToGoB == 0) {
                    goalsToGo = 0;
                    probPlayersTeamScoresNextGivenScore = 0;
                } else if (playerTeam == TeamId.A) {
                    goalsToGo = goalsToGoA;
                    probPlayersTeamScoresNextGivenScore = goalsToGoA / (goalsToGoA + goalsToGoB);
                } else {
                    goalsToGo = goalsToGoB;
                    probPlayersTeamScoresNextGivenScore = goalsToGoB / (goalsToGoA + goalsToGoB);
                }
                double probThisScore = scoreInfo.prob;
                probPlayersTeamScoresNext += probThisScore * probPlayersTeamScoresNextGivenScore;

                double probAnyTimeScore =
                                probThisScore * (1 - Math.pow(1 - probPlayerScoresGivenTeamScores, goalsToGo));
                if (DEBUG_ON) {
                    System.out.printf("Player: %s, Score: %s, probScore: %.3f, probPlayerScoresAnyTime|score: %.3f\n",
                                    player, scoreInfo.toString(), probThisScore, probAnyTimeScore);
                }
                anytimeGoalSelection.prob += probAnyTimeScore;
            } // end each score for loop
            nextGoalSelection.prob = probPlayersTeamScoresNext * probPlayerScoresGivenTeamScores;
            if (DEBUG_ON)
                log.info("Player: %s, probPlayerScoresNextGoal: %.3f, probPlayerScoresAnytime: %.3f", player,
                                nextGoalSelection.prob, anytimeGoalSelection.prob);

        }
        /*
         * generate the output markets and selections. Need to remove from anytime markets any players who have already
         * scored
         */
        String[] playersAlreadyScored = matchState.getPlayerScoringGoalN();
        String goalSequenceId = matchState.getGoalSequenceId();
        String ngsDescription;
        String noScoreDescription;
        if (goalSequenceId.equals("G1")) {
            ngsDescription = "First to score";
            noScoreDescription = "No score";
        } else {
            ngsDescription = "Next to score";
            noScoreDescription = "No further score";
        }
        Market nextGoalScorerMkt = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:NS", goalSequenceId,
                        ngsDescription);
        Market anytimeGoalScorerMkt = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "FT:AGS", "M",
                        "To score anytime in match");
        nextGoalScorerMkt.setIsValid(true);
        anytimeGoalScorerMkt.setIsValid(true);
        probNeitherTeamToScore = GCMath.round(probNeitherTeamToScore, PROBS_DECIMAL_PLACES);
        nextGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
        anytimeGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
        for (Entry<String, Selection> e : nextToScore.entrySet()) {
            String playerId = e.getKey();
            double nextToScoreProb = GCMath.round(e.getValue().prob, PROBS_DECIMAL_PLACES);
            double anytimeScoreProb = GCMath.round(anytimeScore.get(playerId).prob, PROBS_DECIMAL_PLACES);
            String selectionName;
            selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
            nextGoalScorerMkt.put(selectionName, nextToScoreProb);
            if (!alreadyScored(playersAlreadyScored, playerId))
                anytimeGoalScorerMkt.put(selectionName, anytimeScoreProb);
        }
        Map<String, Market> opMarkets = new HashMap<String, Market>(2);
        opMarkets.put(nextGoalScorerMkt.getShortKey(), nextGoalScorerMkt);
        opMarkets.put(anytimeGoalScorerMkt.getShortKey(), anytimeGoalScorerMkt);
        if (DEBUG_ON)
            log.info("Calculation completed");
        return opMarkets;
    }

    private static boolean alreadyScored(String[] playersAlreadyScored, String playerId) {
        for (String l : playersAlreadyScored) {
            if (l == null)
                return false;
            if (l.equals(playerId))
                return true;
        }
        return false;
    }



    /**
     * generates the map of probs for players on the pitch for a given team
     * 
     * @param teamId the team we are generating the probs for
     * @param teamSheet list who is on the pitch
     * @param individualParams lists the relative skills for each player
     * @return
     */
    private static Map<String, Double> getProbsForPlayersOnPitch(TeamId teamId, TeamSheet teamSheet,
                    Map<String, MatchParam> individualParams) {
        MatchParamType targetType;
        if (teamId == TeamId.A)
            targetType = MatchParamType.A;
        else
            targetType = MatchParamType.B;

        Map<String, Double> players = new HashMap<String, Double>(11);
        double sumLikelihood = 0;
        for (Entry<String, MatchParam> e : individualParams.entrySet()) {
            String playerId = e.getKey();
            MatchParam matchParam = e.getValue();
            if (matchParam.getMatchParameterType() == targetType) {
                PlayerInfo playerInfo = teamSheet.getTeamSheetMap().get(playerId);

                if (playerInfo.getPlayerStatus() == PlayerStatus.PLAYING) {
                    double playerLikelihood = matchParam.getGaussian().getMean();
                    sumLikelihood += playerLikelihood;
                    players.put(playerId, playerLikelihood);
                }
            }
        }
        /*
         * convert relative likelihoods to probabilities
         */
        Map<String, Double> players2 = new HashMap<String, Double>(11);
        for (Entry<String, Double> e : players.entrySet()) {
            players2.put(e.getKey(), e.getValue() / sumLikelihood);
        }
        return players2;
    }


    /**
     * creates the map of ScoreInfo objects that define the possible scores that might occur and the prob of each
     * 
     * @param correctScore
     * @param correctScoreInfo
     * @return
     */
    private static Map<String, ScoreInfo> generateScoreInfoMap(Market csMkt, ScoreInfo correctScoreInfo) {
        Map<String, ScoreInfo> preMatchCorrectScoreInfo = new HashMap<String, ScoreInfo>();
        double sumCorrectScoreProbs = 0;
        for (Entry<String, Double> e : csMkt.getSelectionsProbs().entrySet()) {
            String key = e.getKey();
            double prob = e.getValue();
            ScoreInfo scoreInfo = new ScoreInfo(key, prob);
            if (scoreInfo.consistentWith(correctScoreInfo)) {
                preMatchCorrectScoreInfo.put(key, scoreInfo);
                sumCorrectScoreProbs += prob;
            }
        }
        /*
         * refactor the correctScoreGrid to sum to exactly 1, allowing for any selections that have been excluded
         * because not consistent with current score
         */
        if (sumCorrectScoreProbs == 0.0)
            throw new IllegalArgumentException("No valid correctScore input prices given the input current Score");
        for (ScoreInfo scoreInfo : preMatchCorrectScoreInfo.values())
            scoreInfo.prob /= sumCorrectScoreProbs;
        return preMatchCorrectScoreInfo;
    }

}
