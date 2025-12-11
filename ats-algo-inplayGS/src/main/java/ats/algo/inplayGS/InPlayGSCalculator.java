package ats.algo.inplayGS;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.inplayGS.Selection.Team;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;

/**
 * Calculates nextGoalScorer and anytime goal scorer probabilities and prices. This version takes as input the PRE_MATCH
 * prices for firstGoalScorer and correctScore, as well as the in play current actual score.
 * 
 * Margining of output prices is based on the standard Amelco v11 algorithm, with the average margin per selection being
 * specified via the xxxMarginPerSelection properties. Prices are filtered via an odds ladder
 * 
 * @author Geoff
 *
 */
public class InPlayGSCalculator extends AtsBean {

    private static final boolean DEBUG_ON = false;

    private double nextGoalScorerMarginPerSelection;
    private double anytimeGoalScorerMarginPerSelection;
    private boolean reApplyInputMargin;

    private Map<String, Selection> inPlayNextGoalScorers;
    private Map<String, Selection> inPlayAnytimeGoalScorers;
    private OddsLadder oddsLadder;
    double[] defaultOddsLadder = {1.001, 1.002, 1.004, 1.005, 1.007, 1.01, 1.02, 1.03, 1.04, 1.05, 1.06, 1.07, 1.08,
            1.09, 1.1, 1.11, 1.12, 1.14, 1.15, 1.16, 1.18, 1.2, 1.22, 1.25, 1.28, 1.3, 1.33, 1.35, 1.36, 1.4, 1.44,
            1.47, 1.5, 1.53, 1.55, 1.57, 1.6, 1.62, 1.65, 1.67, 1.7, 1.72, 1.73, 1.75, 1.8, 1.83, 1.85, 1.9, 1.91, 1.95,
            2, 2.05, 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.38, 2.4, 2.45, 2.5, 2.55, 2.6, 2.63, 2.65, 2.7, 2.75, 2.8, 2.85,
            2.88, 2.9, 2.95, 3, 3.05, 3.1, 3.15, 3.2, 3.25, 3.3, 3.35, 3.4, 3.45, 3.5, 3.6, 3.7, 3.75, 3.8, 3.9, 4, 4.1,
            4.2, 4.33, 4.4, 4.5, 4.6, 4.7, 4.75, 4.8, 5, 5.25, 5.5, 5.75, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 11, 12,
            13, 14, 15, 16, 17, 18, 19, 21, 23, 26, 29, 31, 34, 36, 41, 46, 51, 56, 61, 67, 71, 76, 81, 86, 91, 96, 101,
            106, 111, 116, 121, 126, 131, 136, 141, 146, 151, 156, 161, 166, 171, 176, 181, 186, 191, 196, 201, 206,
            211, 216, 221, 226, 231, 236, 241, 246, 251, 256, 261, 266, 271, 276, 281, 286, 291, 296, 301, 306, 311,
            316, 321, 326, 331, 336, 341, 346, 351, 356, 361, 366, 371, 376, 381, 386, 391, 396, 401, 406, 411, 416,
            421, 426, 431, 436, 441, 446, 451, 456, 461, 466, 471, 476, 481, 486, 491, 496, 501, 506, 511, 516, 521,
            526, 531, 536, 541, 546, 551, 556, 561, 566, 571, 576, 581, 586, 591, 596, 601, 606, 611, 616, 621, 626,
            631, 636, 641, 646, 651, 656, 661, 666, 671, 676, 681, 686, 691, 696, 701, 706, 711, 716, 721, 726, 731,
            736, 741, 746, 751, 756, 761, 766, 771, 776, 781, 786, 791, 796, 801, 806, 811, 816, 821, 826, 831, 836,
            841, 846, 851, 856, 861, 866, 871, 876, 881, 886, 891, 896, 901, 906, 911, 916, 921, 926, 931, 936, 941,
            946, 951, 956, 961, 966, 971, 976, 981, 986, 991, 996, 1001};

    private boolean customOddsLadderInUse;

    private AllInputOutputData allInputOutputData;

    @Deprecated
    public void setOutputDemarginedInputPrices(boolean outputPrices) {
        /*
         * do nothing
         */
    }

    public AllInputOutputData getAllInputOutputData() {
        return allInputOutputData;
    }

    public String getAllInputOutputDataAsJson() {
        String json = JsonUtil.marshalJson(allInputOutputData);
        return json;

    }

    /**
     * gets next to score prices and probs
     * 
     * @return the calculated set of next to score goal scorer probs and prices, following a call to the calculate()
     *         method
     */
    public Map<String, Selection> getGSForNextGoal() {
        return inPlayNextGoalScorers;
    }

    /**
     * 
     * gets anytime score prices and probs
     * 
     * @return the calculated set of anytime goal scorer probs and prices, following a call to the calculate() method
     */
    public Map<String, Selection> getGSForAnytimeGoal() {
        return inPlayAnytimeGoalScorers;
    }


    /**
     * sets the target margin per selection to apply for next goal scorer market. Default if not explicitly set is 0.12
     * 
     * @param marginPerSelection must be between 0.0 and 0.2. Ignored if outside those limits
     */
    public void setNextGoalScorerMarginPerSelection(double marginPerSelection) {
        if (marginPerSelection < 0.0 || marginPerSelection > 0.2) {
            error("Invalid marginPerSelection in call to setNextGoalScorerMarginPerSelection: " + marginPerSelection);
            return;
        }
        this.nextGoalScorerMarginPerSelection = marginPerSelection;
    }

    /**
     * sets the target margin per selection to apply for anytime goal scorer market. Default if not explicitly set is
     * 0.12
     * 
     * @param marginPerSelection must be between 0.0 and 0.2. Ignored if outside those limits
     */
    public void setAnytimeGoalScorerMarginPerSelection(double marginPerSelection) {
        if (marginPerSelection < 0.0 || marginPerSelection > 0.2) {
            error("Invalid marginPerSelection in call to anytimeGoalScorerMarginPerSelection: " + marginPerSelection);
            return;
        }
        this.anytimeGoalScorerMarginPerSelection = marginPerSelection;
    }

    /**
     * sets the odds ladder to be used when adding margin to prices.
     * 
     * The default if not explicitly set is { 1.001, 1.002, 1.004, 1.005, 1.007, 1.01, 1.02, 1.03, 1.04, 1.05, 1.06,
     * 1.07, 1.08, 1.09, 1.1, 1.11, 1.12, 1.14, 1.15, 1.16, 1.18, 1.2, 1.22, 1.25, 1.28, 1.3, 1.33, 1.35, 1.36, 1.4,
     * 1.44, 1.47, 1.5, 1.53, 1.55, 1.57, 1.6, 1.62, 1.65, 1.67, 1.7, 1.72, 1.73, 1.75, 1.8, 1.83, 1.85, 1.9, 1.91,
     * 1.95, 2, 2.05, 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.38, 2.4, 2.45, 2.5, 2.55, 2.6, 2.63, 2.65, 2.7, 2.75, 2.8,
     * 2.85, 2.88, 2.9, 2.95, 3, 3.05, 3.1, 3.15, 3.2, 3.25, 3.3, 3.35, 3.4, 3.45, 3.5, 3.6, 3.7, 3.75, 3.8, 3.9, 4,
     * 4.1, 4.2, 4.33, 4.4, 4.5, 4.6, 4.7, 4.75, 4.8, 5, 5.25, 5.5, 5.75, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 11, 12,
     * 13, 14, 15, 16, 17, 18, 19, 21, 23, 26, 29, 31, 34, 36, 41, 46, 51, 56, 61, 67, 71, 76, 81, 86, 91, 96, 101, 106,
     * 111, 116, 121, 126, 131, 136, 141, 146, 151, 156, 161, 166, 171, 176, 181, 186, 191, 196, 201, 206, 211, 216,
     * 221, 226, 231, 236, 241, 246, 251, 256, 261, 266, 271, 276, 281, 286, 291, 296, 301, 306, 311, 316, 321, 326,
     * 331, 336, 341, 346, 351, 356, 361, 366, 371, 376, 381, 386, 391, 396, 401, 406, 411, 416, 421, 426, 431, 436,
     * 441, 446, 451, 456, 461, 466, 471, 476, 481, 486, 491, 496, 501, 506, 511, 516, 521, 526, 531, 536, 541, 546,
     * 551, 556, 561, 566, 571, 576, 581, 586, 591, 596, 601, 606, 611, 616, 621, 626, 631, 636, 641, 646, 651, 656,
     * 661, 666, 671, 676, 681, 686, 691, 696, 701, 706, 711, 716, 721, 726, 731, 736, 741, 746, 751, 756, 761, 766,
     * 771, 776, 781, 786, 791, 796, 801, 806, 811, 816, 821, 826, 831, 836, 841, 846, 851, 856, 861, 866, 871, 876,
     * 881, 886, 891, 896, 901, 906, 911, 916, 921, 926, 931, 936, 941, 946, 951, 956, 961, 966, 971, 976, 981, 986,
     * 991, 996, 1001 }
     * 
     * @param oddsLadder array of numbers in strictly ascending order (i.e. ladder[i+1} greater than ladder[i] for all
     *        i), starting at 1. If invalid then oddsLadder is not updated
     */
    public void setOddsLadder(double[] oddsLadder) {
        if (oddsLadder[0] < 1) {
            error("Invalid oddsLadder. ladder[0] < 1: " + oddsLadder[0]);
            return;
        }
        for (int i = 0; i < oddsLadder.length - 1; i++) {
            if (oddsLadder[i + 1] <= oddsLadder[i]) {
                error("Invalid oddsLadder. Not strictly ascending order: " + oddsLadder[i] + ", " + oddsLadder[i + 1]);
                return;
            }
        }
        this.oddsLadder = new OddsLadder(oddsLadder);
        customOddsLadderInUse = true;
        if (DEBUG_ON) {
            info("setOddsLadder invoked. new oddsLadder hashcode: " + oddsLadder.hashCode());
            StringBuilder b = new StringBuilder();
            b.append("New odds ladder data: {");
            for (double o : oddsLadder) {
                b.append(o);
                b.append(", ");
            }
            b.append(", ");
            info(b.toString());
        }
    }

    /**
     * if set to true then the margin that was spread across the input prices is re-applied to the output prices.
     * Otherwise the specified margin per selection is used
     * 
     * @param reApplyInputMargin
     */
    public void setReApplyInputMargin(boolean reApplyInputMargin) {
        this.reApplyInputMargin = reApplyInputMargin;
    }

    /**
     * default constructor
     */
    public InPlayGSCalculator() {
        this(0.12);
    }

    /**
     * sets the default margin that gets applied per selection to both the nextGoal and anytime Gaol markets. These can
     * also be set individually if desired via the respective setters
     * 
     * @param marginPerSelection must be between 0.0 and 0.2
     */
    public InPlayGSCalculator(double marginPerSelection) {
        this.setNextGoalScorerMarginPerSelection(marginPerSelection);
        this.setAnytimeGoalScorerMarginPerSelection(marginPerSelection);
        this.oddsLadder = new OddsLadder(defaultOddsLadder);
        customOddsLadderInUse = false;
        reApplyInputMargin = true;
    }

    /**
     * Generates prices for next to score and anytime scorer markets from the supplied input data.
     * 
     * The pre-match params must be the most recent that were available before kick-off, and all three must have been
     * collected at the same time.
     * 
     * if in play the currentInPlay params must reflect the current live state of the match.
     * 
     * If pre-match (i.e. matchClockSecs=0) then the currentInPlay params are ignored, so may be set to null values
     * 
     * @param preMatchTeamSheet Contains an entry for every player in either team. The player names are used as the keys
     *        to the map and must match exactly the player names used to identify the selections in the nextToScore
     *        prices map. the entry for each player indicates whether they are a)expected to play at the start of the
     *        match (PLAYING) or b) on the bench at the start of the match (ON_THE_BENCH)
     * @param preMatchFirstToScore Current in-play prices for either the first player to score in the match, if no goal
     *        already scored, or the next player to score if one or more goals has been scored. Player names are used as
     *        the keys to the map and must match exactly the player names used to identify the selections in the
     *        teamSheet map.
     * @param preMatchCorrectScore - the current in play correct score prices *
     * @param currentInPlayTeamSheet Contains an entry for every player in either team. The player names are used as the
     *        keys to the map and must match exactly the player names used to identify the selections in the nextToScore
     *        prices map. the entry for each player indicates whether they are a)playing (PLAYING), b) sent
     *        off(SENT_OFF), c) substituted (SUBSTITUTED) or d) yet to play and may come on(ON_THE_BENCH).
     * @param matchDurationSecs The duration of the match in secs - e.g. 5400 for a 90 minute match. does not include
     *        injury time
     * @param matchClockSecs The current match time in secs, ignoring any injury time played in the previous half, if
     *        any. e.g. if playing two halves of 45 mins each, and now 3 mins 34 secs into the second half then
     *        matchClockSecs should be set to 45x60+3x60+34=2914
     * @param currentInPlayCorrectScore the current in play correct score prices
     * @param currentInPlayScore the current score in format "n-m"
     */

    public void calculate(Map<String, PlayerStatus> preMatchTeamSheet, Map<String, Selection> preMatchFirstToScore,
                    Map<String, Selection> preMatchCorrectScore, int matchDurationSecs, int matchClockSecs,
                    Map<String, PlayerStatus> currentInPlayTeamSheet, Map<String, Selection> currentInPlayCorrectScore,
                    String currentInPlayScore) {
        allInputOutputData = new AllInputOutputData();
        allInputOutputData.setPreMatchTeamSheet(preMatchTeamSheet);
        allInputOutputData.setPreMatchFirstToScore(preMatchFirstToScore);
        allInputOutputData.setPreMatchCorrectScore(preMatchCorrectScore);
        allInputOutputData.setMatchDurationSecs(matchDurationSecs);
        allInputOutputData.setMatchClockSecs(matchClockSecs);
        allInputOutputData.setCurrentInPlayTeamSheet(currentInPlayTeamSheet);
        allInputOutputData.setCurrentInPlayCorrectScore(currentInPlayCorrectScore);
        allInputOutputData.setCurrentInPlayScore(currentInPlayScore);
        allInputOutputData.setAnytimeGoalScorerMarginPerSelection(anytimeGoalScorerMarginPerSelection);
        allInputOutputData.setNextGoalScorerMarginPerSelection(matchClockSecs);
        allInputOutputData.setOddsLadder(oddsLadder);
        allInputOutputData.setReApplyInputMargin(reApplyInputMargin);


        boolean preMatch = matchClockSecs == 0;
        /*
         * log all the input data
         */
        info("Calculate method invoked");
        if (DEBUG_ON) {
            info("INPUT DATA:");
            info("  matchDurationSecs: %d, matchClockSecs: %d", matchDurationSecs, matchClockSecs);
            info("  preMatchTeamSheet:");
            for (Entry<String, PlayerStatus> e : preMatchTeamSheet.entrySet()) {
                info("    " + e.getKey() + ": " + e.getValue().toString());
            }
            info("  preMatchFirstToScore:");
            for (Entry<String, Selection> e : preMatchFirstToScore.entrySet()) {
                info("    key: " + e.getKey() + ", value: " + e.getValue().toString());
            }
            info("  preMatchCorrectScore:" + preMatchCorrectScore);
            for (Entry<String, Selection> e : preMatchCorrectScore.entrySet()) {
                info("    key: " + e.getKey() + ", value: " + e.getValue().toString());
            }
            if (preMatch) {
                info("  Pre-match so inPlay params ignored");
            } else {
                info("  currentInPlayTeamSheet:");
                for (Entry<String, PlayerStatus> e : currentInPlayTeamSheet.entrySet()) {
                    info("    " + e.getKey() + ": " + e.getValue().toString());
                }
                info("  currentInPlayCorrectScore:" + currentInPlayCorrectScore);
                for (Entry<String, Selection> e : currentInPlayCorrectScore.entrySet()) {
                    info("    key: " + e.getKey() + ", value: " + e.getValue().toString());
                }
                info("  currentInPlayScore: " + currentInPlayScore);
            }
            info("  reApplyingInputMargin: " + this.reApplyInputMargin);
            if (!this.reApplyInputMargin) {
                info("  nextGoalScorerMarginPerSelection set to: " + nextGoalScorerMarginPerSelection);
                info("  anytimeGoalScorerMarginPerSelection set to: " + anytimeGoalScorerMarginPerSelection);
            }
            if (customOddsLadderInUse)
                info("  Will use custom odds ladder.  Odds ladder hash code: " + oddsLadder.hashCode());
            else
                info("  Will use default odds ladder");
            info("END OF INPUT DATA");
        }

        /*
         * demargin the prices
         */
        double totalMargin = MarginManager.demargin(preMatchFirstToScore, 1.0);
        MarginManager.demargin(preMatchCorrectScore, 1.0);
        if (matchClockSecs == 0) {
            currentInPlayCorrectScore = preMatchCorrectScore;
            currentInPlayScore = "0-0";
        } else {
            MarginManager.demargin(currentInPlayCorrectScore, 1.0);
        }

        if (DEBUG_ON) {
            /*
             * remargin the probs to verify that they are being calculated correctly
             */
            checkMarginingOk(preMatchFirstToScore, totalMargin);
        }

        /*
         * sort the players into their respective teams generate the conditional probs
         */
        Map<String, PlayerData> playersTeamA = new HashMap<String, PlayerData>();
        Map<String, PlayerData> playersTeamB = new HashMap<String, PlayerData>();
        generateConditionalProbsForEachPlayerScoring(preMatch, preMatchTeamSheet, preMatchFirstToScore, playersTeamA,
                        playersTeamB);

        /*
         * get the currentScore into a form in which we can use it and calc probs of next to score
         */
        ScoreInfo currentScoreInfo = new ScoreInfo(currentInPlayScore, 1.0);
        Map<String, ScoreInfo> currentInPlayCorrectScoreInfo =
                        generateScoreInfoMap(currentInPlayCorrectScore, currentScoreInfo);
        if (DEBUG_ON) {
            info("Current score " + currentScoreInfo);
            for (ScoreInfo scoreInfo : currentInPlayCorrectScoreInfo.values())
                info("  Correct score probs: " + scoreInfo);
        }
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
        if (DEBUG_ON) {
            /*
             * log all the data calculated so far - will be used to derive all the prices we need.
             */
            debug("Intermediate calculated data:");
            debug("  conditionalProbs Team A:");
            for (Entry<String, PlayerData> e : playersTeamA.entrySet())
                debug("    " + e.getKey() + ": " + e.getValue().toString());
            debug("  conditionalProbs Team B:");
            for (Entry<String, PlayerData> e : playersTeamB.entrySet())
                debug("    " + e.getKey() + ": " + e.getValue().toString());


            debug("  Next team to score probs, calculated from CorrectScorePrices:");
            debug(String.format("    Prob no score: %.3f", probNeitherTeamToScore));
            debug(String.format("    Prob teamA scores next: %.3f", probNextToScoreTeamA));
            debug(String.format("    Prob teamB scores next: %.3f", probNextToScoreTeamB));
        }

        /*
         * create the data structures to hold the output data
         */
        inPlayNextGoalScorers = new LinkedHashMap<String, Selection>();
        inPlayAnytimeGoalScorers = new LinkedHashMap<String, Selection>();

        for (Entry<String, Selection> e : preMatchFirstToScore.entrySet()) {
            String player = e.getKey();
            Selection selection = e.getValue();
            inPlayNextGoalScorers.put(player, new Selection(selection.team, selection.name, 0));
            inPlayAnytimeGoalScorers.put(player, new Selection(selection.team, selection.name, 0));
        }

        /*
         * do the calcs
         */
        for (Entry<String, Selection> e : inPlayNextGoalScorers.entrySet()) {
            /*
             * loop for each player
             */
            String player = e.getKey();

            Selection nextGoalSelection = e.getValue();
            Selection anytimeGoalSelection = inPlayAnytimeGoalScorers.get(player);
            if (player.equals("No Score")) {
                nextGoalSelection.prob = probNeitherTeamToScore;
                anytimeGoalSelection.prob = probNeitherTeamToScore;
            } else {
                Team playerTeam = nextGoalSelection.team;
                PlayerData playerData;
                if (playerTeam == Team.A)
                    playerData = playersTeamA.get(player);
                else
                    playerData = playersTeamB.get(player);
                if (playerData == null)
                    throw new IllegalArgumentException("InconsistentSourceData for player:" + player);
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
                    } else if (playerTeam == Team.A) {
                        goalsToGo = goalsToGoA;
                        probPlayersTeamScoresNextGivenScore = goalsToGoA / (goalsToGoA + goalsToGoB);
                    } else {
                        goalsToGo = goalsToGoB;
                        probPlayersTeamScoresNextGivenScore = goalsToGoB / (goalsToGoA + goalsToGoB);
                    }
                    double probThisScore = scoreInfo.prob;
                    probPlayersTeamScoresNext += probThisScore * probPlayersTeamScoresNextGivenScore;


                    if (DEBUG_ON) {
                        System.out.printf(
                                        "Player: %s, Score: %s, goalstoGoA: %.0f, goalstoGoB: %.0f, probPlayersTeamScoresNext: %.3f probThisScore: %.3f, playerData.probPreMatchScoresGivenTeamScores: %.3f\n",
                                        player, scoreInfo.toString(), goalsToGoA, goalsToGoB,
                                        probPlayersTeamScoresNextGivenScore, probThisScore,
                                        playerData.probScoresGivenTeamScoresAndOnPitch);
                    }

                    double probAnyTimeScore = probThisScore
                                    * (1 - Math.pow(1 - playerData.probScoresGivenTeamScoresAndOnPitch, goalsToGo));
                    if (DEBUG_ON)
                        System.out.printf(
                                        "Player: %s, Score: %s, probScore: %.3f, probPlayerScoresAnyTime|score: %.3f\n",
                                        player, scoreInfo.toString(), probThisScore, probAnyTimeScore);
                    anytimeGoalSelection.prob += probAnyTimeScore;
                } // end each score for loop
                nextGoalSelection.prob = probPlayersTeamScoresNext * playerData.probScoresGivenTeamScoresAndOnPitch;
                if (DEBUG_ON)
                    info("Player: %s, probPlayerScoresNextGoal: %.3f, probPlayerScoresAnytime: %.3f", player,
                                    nextGoalSelection.prob, anytimeGoalSelection.prob);
            }
        }
        /*
         * if in play then only show selections for players on the pitch
         */
        if (!preMatch) {
            filterSelectionsForPlayersOnPitch(inPlayNextGoalScorers, currentInPlayTeamSheet);
            filterSelectionsForPlayersOnPitch(inPlayAnytimeGoalScorers, currentInPlayTeamSheet);
        }
        /*
         * set the MarginedPrices
         */
        if (this.reApplyInputMargin) {
            MarginManager.remargin(inPlayNextGoalScorers, totalMargin, oddsLadder, 150);
            MarginManager.remargin(inPlayAnytimeGoalScorers, totalMargin, oddsLadder, 120);
        } else {
            MarginManager.setMarginedPrices(inPlayNextGoalScorers, nextGoalScorerMarginPerSelection, oddsLadder);
            MarginManager.setMarginedPrices(inPlayAnytimeGoalScorers, anytimeGoalScorerMarginPerSelection, oddsLadder);
        }
        allInputOutputData.setInPlayNextGoalScorers(inPlayNextGoalScorers);
        allInputOutputData.setInPlayAnytimeGoalScorers(inPlayAnytimeGoalScorers);
        info("Calculate method finished");
    }

    /**
     * if in play remove selections for players who are not on the pitch and refactor probs to sum to 1
     * 
     * @param inPlayNextGoalScorers2
     * @param currentInPlayTeamSheet
     */
    private void filterSelectionsForPlayersOnPitch(Map<String, Selection> selectionMap,
                    Map<String, PlayerStatus> currentInPlayTeamSheet) {
        for (Entry<String, PlayerStatus> e : currentInPlayTeamSheet.entrySet()) {
            String player = e.getKey();
            PlayerStatus status = e.getValue();
            if (status != PlayerStatus.PLAYING)
                selectionMap.remove(player);
        }
        double sumProbs = 0;
        for (Selection s : selectionMap.values()) {
            if (!(s.getTeam() == Team.NEITHER))

                sumProbs += s.prob;
        }
        for (Selection s : selectionMap.values()) {
            if (s.getTeam() != Team.NEITHER)
                s.prob /= sumProbs;
        }
    }

    /**
     * creates the conditional prob of player scoring given team scores and given on the pitch
     * 
     * @param preMatchTeamSheet
     * @param preMatchFirstToScore
     * @param playersTeamA
     * @param playersTeamB
     */
    private void generateConditionalProbsForEachPlayerScoring(boolean preMatch,
                    Map<String, PlayerStatus> preMatchTeamSheet, Map<String, Selection> preMatchFirstToScore,
                    Map<String, PlayerData> playersTeamA, Map<String, PlayerData> playersTeamB) {
        for (Entry<String, Selection> e : preMatchFirstToScore.entrySet()) {
            String playerName = e.getKey();
            Selection selection = e.getValue();
            PlayerData playerData =
                            new PlayerData(preMatchTeamSheet.get(playerName) == PlayerStatus.PLAYING, selection.prob);
            switch (selection.team) {
                case A:
                    playersTeamA.put(playerName, playerData);
                    break;
                case B:
                    playersTeamB.put(playerName, playerData);
                    break;
                case NEITHER:
                default:
                    break;
            }
        }
        /*
         * refactor the probs within each team to be the conditional prob that player scores given that their team
         * scores
         */
        refactorProbsforGivenTeamScores(playersTeamA, preMatch);
        refactorProbsforGivenTeamScores(playersTeamB, preMatch);
    }

    private void checkMarginingOk(Map<String, Selection> preMatchFirstToScore, double totalMargin) {
        Map<String, Selection> preMatchFirstToScore2 = new HashMap<String, Selection>(preMatchFirstToScore.size());
        for (Entry<String, Selection> e : preMatchFirstToScore.entrySet()) {
            Selection s2 = e.getValue().copy();
            preMatchFirstToScore2.put(e.getKey(), s2);
        }

        MarginManager.remargin(preMatchFirstToScore2, totalMargin, oddsLadder, 150);
        for (Entry<String, Selection> e : preMatchFirstToScore.entrySet()) {
            String key = e.getKey();
            Selection sIn = e.getValue();
            Selection sOut = preMatchFirstToScore2.get(key);
            System.out.printf("%s: input price: %.2f, calc prob: %.3f, calc remargined price: %.2f\n", sIn.name,
                            sIn.price, sIn.prob, sOut.price);
        }
    }

    /**
     * creates the map of ScoreInfo objects that define the possible scores that might occur and the prob of each
     * 
     * @param correctScore
     * @param correctScoreInfo
     * @return
     */
    private Map<String, ScoreInfo> generateScoreInfoMap(Map<String, Selection> correctScore,
                    ScoreInfo correctScoreInfo) {
        Map<String, ScoreInfo> preMatchCorrectScoreInfo = new HashMap<String, ScoreInfo>();
        double sumCorrectScoreProbs = 0;
        for (Entry<String, Selection> e : correctScore.entrySet()) {
            String key = e.getKey();
            double prob = e.getValue().prob;
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

    /*
     * arbitrary boost for increasing the prob of player scoring if not in the starting line up.
     */
    private static final double BOOST_FOR_NOT_ON_PITCH = 3.0;

    /**
     * refactor the first to score probs to become player to score|teamscores and on the pitch
     * 
     * @param playersTeam
     */
    private void refactorProbsforGivenTeamScores(Map<String, PlayerData> playersTeam, boolean preMatch) {
        double sumProbs = 0;
        double boost;
        if (preMatch)
            boost = 1;
        else
            boost = BOOST_FOR_NOT_ON_PITCH;
        for (Entry<String, PlayerData> e : playersTeam.entrySet()) {
            PlayerData playerData = e.getValue();
            if (!playerData.inStartingLineUp) {
                playerData.probScoresGivenTeamScoresAndOnPitch *= boost;
            }
            sumProbs += playerData.probScoresGivenTeamScoresAndOnPitch;
        }
        if (sumProbs == 0)
            throw new IllegalArgumentException("Invalid input data.  Player probs sum to zero");
        for (Entry<String, PlayerData> e : playersTeam.entrySet())
            e.getValue().probScoresGivenTeamScoresAndOnPitch /= sumProbs;
    }



}
