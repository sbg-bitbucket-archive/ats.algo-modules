/**
 * 
 */
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
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * @author Robert
 *
 */
public class GsCalculator {
    private static final boolean DEBUG_ON = false;

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

        Logger log = LoggerFactory.getLogger("InPlayGSCalculator");
        log.info("calculateGsMarkets method invoked. INPUT DATA:");
        log.info("Correct score mkt:\n" + csMarket);
        log.info("FootballMatchState:\n" + matchState);
        log.info("FootballMatchParams:\n" + matchParams);
        /*
         * get the currentScore into a form in which we can use it and calc probs of next to score
         */
        ScoreInfo currentScoreInfo = new ScoreInfo(matchState.getGoalsA(), matchState.getGoalsB(), 1.0);
        Map<String, ScoreInfo> currentInPlayCorrectScoreInfo = generateScoreInfoMap(csMarket, currentScoreInfo);

        double probNeitherTeamToScore = 0.0;
        double probNeitherTeamToScore1 = 0.0;
        double probNextToScoreTeamA = 0.0;
        double probNextToScoreTeamB = 0.0;
        for (ScoreInfo scoreInfo : currentInPlayCorrectScoreInfo.values()) {
            double goalsA = (double) scoreInfo.goalsToGoA(currentScoreInfo);
            double goalsB = (double) scoreInfo.goalsToGoB(currentScoreInfo);
            if (goalsA == 0 && goalsB == 0) {
                probNeitherTeamToScore += scoreInfo.prob;
            } else {
                // log.debug("goal------" + goalsA + "---" + goalsB + "---" + scoreInfo.prob);
                probNextToScoreTeamA += scoreInfo.prob * (goalsA / (goalsA + goalsB));
                probNextToScoreTeamB += scoreInfo.prob * (goalsB / (goalsA + goalsB));
            }
        }
        /*
         * generate the structures to hold the teams and the probs of each player scoring given that the team scores and
         * they are on the pitch;
         */
        Map<String, Double> playersA;
        Map<String, Double> playersB;
        if (matchState.preMatch()) {
            playersA = getProbsForAllPlayers(TeamId.A, matchState.getTeamSheet(), matchParams.getIndividualParams());
            playersB = getProbsForAllPlayers(TeamId.B, matchState.getTeamSheet(), matchParams.getIndividualParams());
        } else {
            playersA = getProbsForPlayersOnPitch(TeamId.A, matchState.getTeamSheet(),
                            matchParams.getIndividualParams());
            playersB = getProbsForPlayersOnPitch(TeamId.B, matchState.getTeamSheet(),
                            matchParams.getIndividualParams());
        }


        // log.debug(playersA1);
        // log.debug(playersB1);
        /*
         * initialise the structures to hold the calculated selection probs
         */
        Map<String, Selection> nextToScore = new LinkedHashMap<String, Selection>(34);


        Map<String, Selection> anytimeScore = new LinkedHashMap<String, Selection>(34);
        for (String key : playersA.keySet())
            nextToScore.put(key, new Selection(TeamId.A));
        for (String key : playersA.keySet())
            anytimeScore.put(key, new Selection(TeamId.A));
        for (String key : playersB.keySet())
            nextToScore.put(key, new Selection(TeamId.B));
        for (String key : playersB.keySet())
            anytimeScore.put(key, new Selection(TeamId.B));

        log.debug(String.format("    Prob no score: %.3f", probNeitherTeamToScore));
        log.debug(String.format("    Prob teamA scores next: %.3f", probNextToScoreTeamA));
        log.debug(String.format("    Prob teamB scores next: %.3f", probNextToScoreTeamB));
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
        Market firstGoalScorerMkt = null;
        if (matchState.preMatch()) {
            firstGoalScorerMkt =
                            new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:FS", "M", "First to score");
            firstGoalScorerMkt.setIsValid(true);
            firstGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
            for (Entry<String, Selection> e : nextToScore.entrySet()) {
                String playerId = e.getKey();
                double nextToScoreProb = e.getValue().prob;
                String selectionName;
                selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
                firstGoalScorerMkt.put(selectionName, nextToScoreProb);

            }
        }

        Market lastGoalScorerMkt = null;
        if (matchState.preMatch()) {
            lastGoalScorerMkt =
                            new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:LS", "M", "Last to score");
            lastGoalScorerMkt.setIsValid(true);
            lastGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
            for (Entry<String, Selection> e : nextToScore.entrySet()) {
                String playerId = e.getKey();
                double nextToScoreProb = e.getValue().prob;
                String selectionName;
                selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
                lastGoalScorerMkt.put(selectionName, nextToScoreProb);

            }
        }

        Market twoMoreGoalScorerMkt =
                        new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:S2OM", "M", "Score 2 or More");
        twoMoreGoalScorerMkt.setIsValid(true);
        noScoreDescription = "No player score 2 more";
        double sum = 0.0;
        int countScorePlayer;
        for (Entry<String, Selection> e : nextToScore.entrySet()) {
            countScorePlayer = 0;
            String playerId = e.getKey();
            double nextToScoreProb = e.getValue().prob;
            double twoMoreScoreProb = (1 - Math.pow(nextToScoreProb, 10)) / (1 - nextToScoreProb) - 1 - nextToScoreProb;
            String selectionName;
            selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
            if (matchState.getPlayerScoringGoalN().length >= 1)
                for (int i = 0; i < matchState.getPlayerScoringGoalN().length; i++) {
                    String scorePlayerId = matchState.getPlayerScoringGoalN(i);
                    if (scorePlayerId.equals(playerId))
                        countScorePlayer++;
                }
            if (countScorePlayer == 0) {
                twoMoreGoalScorerMkt.put(selectionName, twoMoreScoreProb);
                sum += twoMoreScoreProb;
            } else if (countScorePlayer == 1) {
                twoMoreGoalScorerMkt.put(selectionName, nextToScoreProb);
                sum += twoMoreScoreProb;
            } else {
                twoMoreGoalScorerMkt.put(selectionName, 1.0);
                sum += twoMoreScoreProb;
            }

        }
        probNeitherTeamToScore1 = 1 - sum;
        twoMoreGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore1);
        Market hatTrickScorerMkt =
                        new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:HATR", "M", "Hat trick");
        hatTrickScorerMkt.setIsValid(true);
        noScoreDescription = "No player hat trick";
        sum = 0.0;

        for (Entry<String, Selection> e : nextToScore.entrySet()) {
            countScorePlayer = 0;
            String playerId = e.getKey();
            double nextToScoreProb = e.getValue().prob;
            double hatTrickProb = Math.pow(nextToScoreProb, 3);
            String selectionName;
            selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
            hatTrickScorerMkt.put(selectionName, hatTrickProb);
            if (matchState.getPlayerScoringGoalN().length >= 1)
                for (int i = 0; i < matchState.getPlayerScoringGoalN().length; i++) {
                    String scorePlayerId = matchState.getPlayerScoringGoalN(i);
                    if (scorePlayerId.equals(playerId))
                        countScorePlayer++;
                }
            if (countScorePlayer == 0) {
                hatTrickScorerMkt.put(selectionName, hatTrickProb);
                sum += hatTrickProb;
            } else if (countScorePlayer == 1) {
                hatTrickScorerMkt.put(selectionName, nextToScoreProb * nextToScoreProb);
                sum += nextToScoreProb * nextToScoreProb;
            } else if (countScorePlayer == 2) {
                hatTrickScorerMkt.put(selectionName, nextToScoreProb);
                sum += nextToScoreProb;
            } else
                hatTrickScorerMkt.put(selectionName, 1.0);
        }
        probNeitherTeamToScore1 = 1 - sum;
        hatTrickScorerMkt.put(noScoreDescription, probNeitherTeamToScore1);


        Market homeFirstGoalScorerMkt = null;
        if (matchState.preMatch()) {
            homeFirstGoalScorerMkt = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:A:FS", "M",
                            "Home First to score");
            homeFirstGoalScorerMkt.setIsValid(true);
            noScoreDescription = "No score";
            homeFirstGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
            for (Entry<String, Selection> e : nextToScore.entrySet()) {
                String playerId = e.getKey();
                double nextToScoreProb = e.getValue().prob;
                String selectionName;
                selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
                if (matchState.getTeamSheet().getTeamSheetMap().get(playerId).getTeamId() == TeamId.A)
                    homeFirstGoalScorerMkt.put(selectionName, nextToScoreProb);
            }
        }

        Market awayFirstGoalScorerMkt = null;
        if (matchState.preMatch()) {
            awayFirstGoalScorerMkt = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:B:FS", "M",
                            "Away First to score");
            awayFirstGoalScorerMkt.setIsValid(true);
            noScoreDescription = "No score";
            awayFirstGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
            for (Entry<String, Selection> e : nextToScore.entrySet()) {
                String playerId = e.getKey();
                double nextToScoreProb = e.getValue().prob;
                String selectionName;
                selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
                if (matchState.getTeamSheet().getTeamSheetMap().get(playerId).getTeamId() == TeamId.B)
                    awayFirstGoalScorerMkt.put(selectionName, nextToScoreProb);
            }
        }

        Market anytimeGoalScorerMkt = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "G:AGS", "M",
                        "To score anytime in match");
        nextGoalScorerMkt.setIsValid(true);
        anytimeGoalScorerMkt.setIsValid(true);
        noScoreDescription = "No score";
        nextGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
        anytimeGoalScorerMkt.put(noScoreDescription, probNeitherTeamToScore);
        for (Entry<String, Selection> e : nextToScore.entrySet()) {
            String playerId = e.getKey();
            double nextToScoreProb = e.getValue().prob;
            String selectionName;
            selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
            nextGoalScorerMkt.put(selectionName, nextToScoreProb);
        }

        for (Entry<String, Selection> e : anytimeScore.entrySet()) {
            String playerId = e.getKey();
            double anytimeScoreProb = anytimeScore.get(playerId).prob;
            // log.debug("-----------" + playerId + "--------" + anytimeScoreProb);
            String selectionName;
            selectionName = matchState.getTeamSheet().getTeamSheetMap().get(playerId).getSelectionName();
            if (!alreadyScored(playersAlreadyScored, playerId))
                anytimeGoalScorerMkt.put(selectionName, anytimeScoreProb);
        }

        Map<String, Market> opMarkets = new HashMap<String, Market>(2);

        if (firstGoalScorerMkt != null) {
            opMarkets.put(firstGoalScorerMkt.getShortKey(), firstGoalScorerMkt);
            opMarkets.put(homeFirstGoalScorerMkt.getShortKey(), homeFirstGoalScorerMkt);
            opMarkets.put(awayFirstGoalScorerMkt.getShortKey(), awayFirstGoalScorerMkt);
            opMarkets.put(lastGoalScorerMkt.getShortKey(), lastGoalScorerMkt);
        } else
            opMarkets.put(nextGoalScorerMkt.getShortKey(), nextGoalScorerMkt);
        opMarkets.put(anytimeGoalScorerMkt.getShortKey(), anytimeGoalScorerMkt);
        opMarkets.put(twoMoreGoalScorerMkt.getShortKey(), twoMoreGoalScorerMkt);
        opMarkets.put(hatTrickScorerMkt.getShortKey(), hatTrickScorerMkt);
        return opMarkets;
    }

    private static boolean alreadyScored(String[] playersAlreadyScored, String playerId) {
        for (String l : playersAlreadyScored)
            if (l.equals(playerId))
                return true;
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
     * generates the map of probs for players on the pitch for a given team
     * 
     * @param teamId the team we are generating the probs for
     * @param teamSheet list who is on the pitch
     * @param individualParams lists the relative skills for each player
     * @return
     */
    private static Map<String, Double> getProbsForAllPlayers(TeamId teamId, TeamSheet teamSheet,
                    Map<String, MatchParam> individualParams) {
        MatchParamType targetType;
        if (teamId == TeamId.A)
            targetType = MatchParamType.A;
        else
            targetType = MatchParamType.B;

        Map<String, Double> players = new HashMap<String, Double>(17);
        double sumLikelihood = 0;
        for (Entry<String, MatchParam> e : individualParams.entrySet()) {
            String playerId = e.getKey();
            MatchParam matchParam = e.getValue();
            if (matchParam.getMatchParameterType() == targetType) {
                PlayerInfo playerInfo = teamSheet.getTeamSheetMap().get(playerId);
                if (playerInfo.getPlayerStatus() == PlayerStatus.PLAYING) {
                    double playerLikelihood = matchParam.getGaussian().getMean();
                    // System.out.println(targetType + "-------" + playerLikelihood);
                    sumLikelihood += playerLikelihood;
                    players.put(playerId, playerLikelihood);
                }
                if (playerInfo.getPlayerStatus() == PlayerStatus.ON_THE_BENCH) {
                    double playerLikelihood = matchParam.getGaussian().getMean();
                    // System.out.println(targetType + "-------" + playerLikelihood+"---"+sumLikelihood);
                    players.put(playerId, playerLikelihood);
                }
            }
        }
        /*
         * convert relative likelihoods to probabilities
         */
        Map<String, Double> players2 = new HashMap<String, Double>(17);
        for (Entry<String, Double> e : players.entrySet()) {
            // System.out.println("-------------------------------------:" + e.getKey() + "--" + e.getValue() + "--"
            // + sumLikelihood);
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
