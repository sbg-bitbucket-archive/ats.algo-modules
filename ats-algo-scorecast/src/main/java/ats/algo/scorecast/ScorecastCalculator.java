package ats.algo.scorecast;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.margining.Margining;
import ats.algo.margining.MarginingV11;
import ats.algo.scorecast.Selection.Team;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ScorecastCalculator {

    private double wincastFirstMarginPerSelection;
    private double scorecastFirstMarginPerSelection;
    private double wincastAnyMarginPerSelection;
    private double scorecastAnyMarginPerSelection;

    private boolean outputDemarginedInputPrices;

    Selection unquoted = new Selection(Team.NEITHER, "20-20", 501);

    private static Logger log = LoggerFactory.getLogger("ats.allexceptions");

    private Map<String, Selection> wincastFirst;
    private Map<String, Selection> scorecastFirst;
    private Map<String, Selection> wincastAny;
    private Map<String, Selection> scorecastAny;
    private Map<String, Selection> updatedScorecastFirst;
    private Map<String, Selection> updatedScorecastAny;

    private Map<String, Selection> finalScorecastFirst;
    private Map<String, Selection> finalScorecastAny;
    private Map<String, Selection> finalWincastFirst;
    private Map<String, Selection> finalWincastAny;

    double[] oddsLadderSCPS = {1, 1.001, 1.002, 1.004, 1.005, 1.007, 1.01, 1.02, 1.03, 1.04, 1.05, 1.06, 1.07, 1.08,
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

    double[] oddsLadderWCPS = {1, 1.001, 1.002, 1.004, 1.005, 1.007, 1.01, 1.02, 1.03, 1.04, 1.05, 1.06, 1.07, 1.08,
            1.09, 1.1, 1.11, 1.12, 1.14, 1.15, 1.16, 1.18, 1.2, 1.22, 1.25, 1.28, 1.3, 1.33, 1.35, 1.36, 1.4, 1.44,
            1.47, 1.5, 1.53, 1.55, 1.57, 1.6, 1.62, 1.65, 1.67, 1.7, 1.72, 1.73, 1.75, 1.8, 1.83, 1.85, 1.9, 1.91, 1.95,
            2, 2.05, 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.38, 2.4, 2.45, 2.5, 2.55, 2.6, 2.63, 2.65, 2.7, 2.75, 2.8, 2.85,
            2.88, 2.9, 2.95, 3, 3.05, 3.1, 3.15, 3.2, 3.25, 3.3, 3.35, 3.4, 3.45, 3.5, 3.6, 3.7, 3.75, 3.8, 3.9, 4, 4.1,
            4.2, 4.33, 4.4, 4.5, 4.6, 4.7, 4.75, 4.8, 5, 5.25, 5.5, 5.75, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 11, 12,
            13, 14, 15, 16, 17, 18, 19, 21, 23, 26, 29, 31, 34, 36, 41, 46, 51, 56, 61, 67, 71, 76, 81, 86, 91, 96, 101,
            106, 111, 116, 121, 126, 131, 136, 141, 146, 151, 156, 161, 166, 171, 176, 181, 186, 191, 196, 201, 206,
            211, 216, 221, 226, 231, 236, 241, 246, 251, 256, 261, 266, 271, 276, 281, 286, 291, 296, 301, 306, 311,
            316, 321, 326, 331, 336, 341, 346, 351, 356, 361, 366, 371, 376, 381, 386, 391, 396, 401, 406, 411, 416,
            421, 426, 431, 436, 441, 446, 451, 456, 461, 466, 471, 476, 481, 486, 491, 496, 501};

    public Map<String, Selection> getWincastFirst() {
        return finalWincastFirst;
    }

    public Map<String, Selection> getScorecastFirst() {
        return finalScorecastFirst;
    }

    public Map<String, Selection> getWincastAny() {
        return finalWincastAny;
    }

    public Map<String, Selection> getScorecastAny() {
        return finalScorecastAny;
    }

    public double getWincastFirstMarginPerSelection() {
        return wincastFirstMarginPerSelection;
    }

    public void setWincastFirstMarginPerSelection(double wincastFirstMarginPerSelection) {
        this.wincastFirstMarginPerSelection = wincastFirstMarginPerSelection;
    }

    public double getScorecastFirstMarginPerSelection() {
        return scorecastFirstMarginPerSelection;
    }

    public void setScorecastFirstMarginPerSelection(double scorecastFirstMarginPerSelection) {
        this.scorecastFirstMarginPerSelection = scorecastFirstMarginPerSelection;
    }

    public double getWincastAnyMarginPerSelection() {
        return wincastAnyMarginPerSelection;
    }

    public void setWincastAnyMarginPerSelection(double wincastAnyMarginPerSelection) {
        this.wincastAnyMarginPerSelection = wincastAnyMarginPerSelection;
    }

    public double getScorecastAnyMarginPerSelection() {
        return scorecastAnyMarginPerSelection;
    }

    public void setScorecastAnyMarginPerSelection(double scorecastAnyMarginPerSelection) {
        this.scorecastAnyMarginPerSelection = scorecastAnyMarginPerSelection;
    }

    public void setOutputDemarginedInputPrices(boolean outputDemarginedInputPrices) {
        this.outputDemarginedInputPrices = outputDemarginedInputPrices;
    }

    public ScorecastCalculator() {
        wincastFirstMarginPerSelection = 0.50; // set the defaults
        scorecastFirstMarginPerSelection = 0.50;
        wincastAnyMarginPerSelection = 1.5; // set the defaults
        scorecastAnyMarginPerSelection = 1.5;
        outputDemarginedInputPrices = false;
    }

    public ScorecastCalculator(double wincastFirstMarginPerSelection, double scorecastFirstMarginPerSelection,
                    double wincastAnyMarginPerSelection, double scorecastAnyMarginPerSelection) {
        this.wincastFirstMarginPerSelection = wincastFirstMarginPerSelection;
        this.scorecastFirstMarginPerSelection = scorecastFirstMarginPerSelection;
        this.wincastAnyMarginPerSelection = wincastAnyMarginPerSelection;
        this.scorecastAnyMarginPerSelection = scorecastAnyMarginPerSelection;
    }

    /**
     * generates the probabilities and prices for the wincast and scorecast markets from the supplied correctScore and
     * firsttoScore prices
     * 
     * @param correctScore set of prices for the correct score.
     * @param firstToScore set of prices for the first player to score in the match
     * @param firstToScoreIncludesNoScoreSelection set to true if firstToScore includes a price for a no score draw. If
     *        true then the corresponding selection name in firstToScore MUST be "No score"
     * @param averageMarginPerSelectionToApply specifies the margin to apply when converting the calculated prices back
     *        to probabilities
     */
    public void calculate(Map<String, Selection> correctScore, Map<String, Selection> firstToScore,
                    Map<String, Selection> anytimeToScore, Map<String, Selection> matchResult,
                    boolean firstToScoreIncludesNoScoreSelection) {

        log.debug("First to Score");
        for (Selection s : firstToScore.values()) {
            log.debug(s.toString());
        }
        log.debug("Anytime to score");
        for (Selection s : anytimeToScore.values()) {
            log.debug(s.toString());
        }
        log.debug("Correct score");
        for (Selection s : correctScore.values()) {
            log.debug(s.toString());
        }

        double priceNoScore = firstToScore.get("No score").getPrice();

        Map<String, Selection> updatedCorrectScore = new LinkedHashMap<String, Selection>();
        for (Selection s : correctScore.values()) {
            if (s.getName().contains("quote")) {
                log.debug("Any Unquoted = " + s.getPrice());
                unquoted.setPrice(s.getPrice());
                updatedCorrectScore.put(unquoted.getName(), unquoted);
            } else {
                updatedCorrectScore.put(s.getName(), s);
            }
        }

        /**
         * store match result prices
         */

        double priceTeamAWin = 0;
        double priceTeamBWin = 0;
        double priceDraw = 0;

        for (Selection result : matchResult.values()) {
            switch (result.getTeam()) {
                case A:
                    priceTeamAWin = result.getPrice();
                    break;
                case B:
                    priceTeamBWin = result.getPrice();
                    break;
                case NEITHER:
                    priceDraw = result.getPrice();
                    break;
            }
        }

        /*
         * create copy of firsttoScore map since may need to add an entry to it
         */
        Map<String, Selection> firstToScore2 = new LinkedHashMap<String, Selection>();
        for (Selection s : firstToScore.values())
            firstToScore2.put(s.getName(), s);

        /*
         * create copy of anytimeToScore map since may need to add an entry to it
         */
        Map<String, Selection> anytimeToScore2 = new LinkedHashMap<String, Selection>();
        for (Selection s : anytimeToScore.values())
            anytimeToScore2.put(s.getName(), s);

        log.debug("Is firstToScoreIncludesNoScoreSelection on? : " + firstToScoreIncludesNoScoreSelection);

        if (!firstToScoreIncludesNoScoreSelection) {
            /*
             * need to add in the noscore selection to make the demargining calc work
             */
            Selection correctScoreNoScore = updatedCorrectScore.get("0-0");
            if (correctScoreNoScore == null)
                throw new IllegalArgumentException("No probability supplied for final score of '0-0'");
            Selection firsttoScoreNoScore = new Selection(Team.NEITHER, "No score");
            firsttoScoreNoScore.setPrice(correctScoreNoScore.getPrice());
            firstToScore2.put(firsttoScoreNoScore.getName(), firsttoScoreNoScore);
            anytimeToScore2.put(firsttoScoreNoScore.getName(), firsttoScoreNoScore);
        }

        /*
         * demargin the prices using the entropy algo
         */
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        demargin(margining, updatedCorrectScore, 1);
        demargin(margining, firstToScore2, 1);
        double probOfNoScore = updatedCorrectScore.get("0-0").getProb();

        double expectedNumGoals = 0;

        for (Selection S : updatedCorrectScore.values()) {
            if (S.getName() == "20-20") {
            } else {
                expectedNumGoals = expectedNumGoals + numGoals(S.getName()) * S.getProb();
            }
        }

        log.debug("Expected number of goals = " + expectedNumGoals);

        demargin(margining, anytimeToScore2, 2 - probOfNoScore);

        /*
         * Handling of the heavy probability that any unquoted price might carry. Normalising the other Correct Score
         * prices to account for Any Unquoted price and then removing it from the Correct score selections
         */

        double storeAnyUnquoted = 0;

        for (Selection s : updatedCorrectScore.values()) {
            if (s.getName() == "20-20") {
                storeAnyUnquoted = s.getProb();
            }
        }

        Map<String, Selection> updatedCorrectScore2 = new LinkedHashMap<String, Selection>();

        for (Selection s : updatedCorrectScore.values()) {
            if (s.getName() == "20-20") {
            } else {
                s.setProb(s.getProb() / (1 - storeAnyUnquoted));
                updatedCorrectScore2.put(s.getName(), s);
            }
        }

        /*
         * need to adjust the firstToScoreProbs/anytimeToScoreProbs so that the probability of 'no score' equals the
         * correctScoreProb for 0-0. Otherwise the calculated scorecast probs won't sum to 1.
         */
        double probNoScorefromCorrectScore = updatedCorrectScore2.get("0-0").getProb();
        Selection firstToScoreNoScoreSelection = firstToScore2.get("No score");
        Selection anytimeToScoreNoScoreSelection = anytimeToScore2.get("No score");
        if (firstToScoreNoScoreSelection == null)
            throw new IllegalArgumentException("There is no 'No score' selection in the supplied firstToScore array");
        if (anytimeToScoreNoScoreSelection == null)
            throw new IllegalArgumentException("There is no 'No score' selection in the supplied anytimeToScore array");
        double probNoScorefromFirstToScore = firstToScoreNoScoreSelection.getProb();
        double probNoScorefromAnytimeToScore = anytimeToScoreNoScoreSelection.getProb();
        double adjustmentFactorFirst = (1 - probNoScorefromCorrectScore) / (1 - probNoScorefromFirstToScore);
        for (Selection s : firstToScore2.values())
            s.setProb(s.getProb() * adjustmentFactorFirst);
        double adjustmentFactorAny = (1 - probNoScorefromCorrectScore) / (1 - probNoScorefromAnytimeToScore);
        for (Selection s : anytimeToScore2.values())
            s.setProb(s.getProb() * adjustmentFactorAny);
        firstToScoreNoScoreSelection.setProb(probNoScorefromCorrectScore);
        anytimeToScoreNoScoreSelection.setProb(probNoScorefromCorrectScore);

        /*
         * calculate the sum of the probs of each team scoring first, for use later in generating the conditional
         * probabilities
         */
        double sumProbsFirstToScoreTeamA = 0;
        double sumProbsFirstToScoreTeamB = 0;
        for (Selection firstToScoreSelection : firstToScore2.values()) {
            switch (firstToScoreSelection.getTeam()) {
                case A:
                    sumProbsFirstToScoreTeamA += firstToScoreSelection.getProb();
                    break;
                case B:
                    sumProbsFirstToScoreTeamB += firstToScoreSelection.getProb();
                    break;
                case NEITHER:
                    break;
            }
        }

        /*
         * calculate the sum of the probs of each team scoring anytime, for use later in generating the conditional
         * probabilities
         */
        double sumProbsAnytimeToScoreTeamA = 0;
        double sumProbsAnytimeToScoreTeamB = 0;
        for (Selection anytimeToScoreSelection : anytimeToScore2.values()) {
            switch (anytimeToScoreSelection.getTeam()) {
                case A:
                    sumProbsAnytimeToScoreTeamA += anytimeToScoreSelection.getProb();
                    break;
                case B:
                    sumProbsAnytimeToScoreTeamB += anytimeToScoreSelection.getProb();
                    break;
                case NEITHER:
                    break;
            }
        }

        scorecastFirst = new LinkedHashMap<String, Selection>();
        wincastFirst = new LinkedHashMap<String, Selection>();
        scorecastAny = new LinkedHashMap<String, Selection>();
        wincastAny = new LinkedHashMap<String, Selection>();

        double probNoScore = firstToScore2.get("No score").getProb();

        for (Selection firstToScoreSelection : firstToScore2.values()) {
            /*
             * ignore the no score selection
             */
            if (!firstToScoreSelection.getName().equals("No score")) {
                Team playersTeam = firstToScoreSelection.getTeam();
                double pWinCastFirstAWins = 0;
                double pWinCastFirstDraw = 0;
                double pWinCastFirstBWins = 0;
                for (Selection correctScoreSelection : updatedCorrectScore2.values()) {
                    String name = firstToScoreSelection.getName() + " and " + correctScoreSelection.getName();
                    Selection scoreCastFirstSelection = new Selection(playersTeam, name);
                    /*
                     * Calc scorecastFirst market. calc is: prob = prob (final score) * prob(team scores first | final
                     * score) * prob (playerScoresfirst | team scores first)
                     */
                    double probPlayerScoresFirstGivenTeamScoresfirst;
                    switch (playersTeam) {
                        case A:
                            probPlayerScoresFirstGivenTeamScoresfirst =
                                            firstToScoreSelection.getProb() / sumProbsFirstToScoreTeamA;
                            break;
                        case B:
                            probPlayerScoresFirstGivenTeamScoresfirst =
                                            firstToScoreSelection.getProb() / sumProbsFirstToScoreTeamB;
                            break;
                        default:
                            throw new IllegalArgumentException("Team Id not specified");
                    }

                    // log.debug("PlayersTeam = " + playersTeam + ",
                    // correctScoreSelection.getName() = " +
                    // correctScoreSelection.getName());

                    scoreCastFirstSelection.setProb(correctScoreSelection.getProb()
                                    * probTeamScoresFirst(playersTeam, correctScoreSelection.getName())
                                    * probPlayerScoresFirstGivenTeamScoresfirst);
                    if (scoreCastFirstSelection.getProb() > 1.0e-6) {
                        /*
                         * don't add selections with zero or near zero probability
                         */
                        scorecastFirst.put(scoreCastFirstSelection.getName(), scoreCastFirstSelection);
                    }
                    /*
                     * increment the wincast probs
                     */
                    switch (matchResult(correctScoreSelection.getName())) {
                        case A:
                            pWinCastFirstAWins += scoreCastFirstSelection.getProb();
                            break;
                        case B:
                            pWinCastFirstBWins += scoreCastFirstSelection.getProb();
                            break;
                        case NEITHER:
                            pWinCastFirstDraw += scoreCastFirstSelection.getProb();
                            break;
                    }
                }
                String winCastName1 = firstToScoreSelection.getName() + " and Team A wins";
                String winCastName2 = firstToScoreSelection.getName() + " and Team B wins";
                String winCastName3 = firstToScoreSelection.getName() + " and Draw";
                /*
                 * generate the winCastFirst selections
                 */
                Selection winCastFirstSelection1 = new Selection(playersTeam, winCastName1);
                Selection winCastFirstSelection2 = new Selection(playersTeam, winCastName2);
                Selection winCastFirstSelection3 = new Selection(playersTeam, winCastName3);
                winCastFirstSelection1.setProb(pWinCastFirstAWins);
                winCastFirstSelection2.setProb(pWinCastFirstBWins);
                winCastFirstSelection3.setProb(pWinCastFirstDraw);
                wincastFirst.put(winCastFirstSelection1.getName(), winCastFirstSelection1);
                wincastFirst.put(winCastFirstSelection2.getName(), winCastFirstSelection2);
                wincastFirst.put(winCastFirstSelection3.getName(), winCastFirstSelection3);

            }
        }

        for (Selection anytimeToScoreSelection : anytimeToScore2.values()) {
            /*
             * ignore the no score selection
             */
            if (!anytimeToScoreSelection.getName().equals("No score")) {
                Team playersTeam = anytimeToScoreSelection.getTeam();
                double pWinCastAnyAWins = 0;
                double pWinCastAnyDraw = 0;
                double pWinCastAnyBWins = 0;
                for (Selection correctScoreSelection : updatedCorrectScore2.values()) {
                    // log.debug("NearFor = " + correctScoreSelection);
                    String name = anytimeToScoreSelection.getName() + " and " + correctScoreSelection.getName();
                    Selection scoreCastAnySelection = new Selection(playersTeam, name);

                    /*
                     * Calc scorecastAnytime market. calc is: prob = prob (final score) * prob(team scores anytime |
                     * final score) * prob (playerScoresAnytime | team scores Anytime)
                     */
                    double probPlayerScoresAnytimeGivenTeamScoresAnytime;
                    switch (playersTeam) {
                        case A:
                            probPlayerScoresAnytimeGivenTeamScoresAnytime =
                                            anytimeToScoreSelection.getProb() / sumProbsAnytimeToScoreTeamA;
                            break;
                        case B:
                            probPlayerScoresAnytimeGivenTeamScoresAnytime =
                                            anytimeToScoreSelection.getProb() / sumProbsAnytimeToScoreTeamB;
                            break;
                        default:
                            throw new IllegalArgumentException("Team Id not specified");
                    }

                    // log.debug("PlayersTeam = " + playersTeam + ",
                    // correctScoreSelection.getName() = " +
                    // correctScoreSelection.getName());

                    scoreCastAnySelection.setProb(correctScoreSelection.getProb()
                                    * probTeamScoresAnytime(playersTeam, correctScoreSelection.getName())
                                    * probPlayerScoresAnytimeGivenTeamScoresAnytime);
                    if (scoreCastAnySelection.getProb() > 1.0e-6) {
                        /*
                         * don't add selections with zero or near zero probability
                         */
                        scorecastAny.put(scoreCastAnySelection.getName(), scoreCastAnySelection);
                    }
                    /*
                     * increment the wincast probs
                     */
                    switch (matchResult(correctScoreSelection.getName())) {
                        case A:
                            pWinCastAnyAWins += scoreCastAnySelection.getProb();
                            break;
                        case B:
                            pWinCastAnyBWins += scoreCastAnySelection.getProb();
                            break;
                        case NEITHER:
                            pWinCastAnyDraw += scoreCastAnySelection.getProb();

                            break;
                    }
                }
                String winCastName1 = anytimeToScoreSelection.getName() + " and Team A wins";
                String winCastName2 = anytimeToScoreSelection.getName() + " and Team B wins";
                String winCastName3 = anytimeToScoreSelection.getName() + " and Draw";

                /*
                 * generate the winCastAny selections
                 */
                Selection winCastAnySelection1 = new Selection(playersTeam, winCastName1);
                Selection winCastAnySelection2 = new Selection(playersTeam, winCastName2);
                Selection winCastAnySelection3 = new Selection(playersTeam, winCastName3);
                winCastAnySelection1.setProb(pWinCastAnyAWins);
                winCastAnySelection2.setProb(pWinCastAnyBWins);
                winCastAnySelection3.setProb(pWinCastAnyDraw);
                wincastAny.put(winCastAnySelection1.getName(), winCastAnySelection1);
                wincastAny.put(winCastAnySelection2.getName(), winCastAnySelection2);
                wincastAny.put(winCastAnySelection3.getName(), winCastAnySelection3);

            }
        }

        /*
         * add the prob of no score to each of the generated markets
         */

        Selection s1 = new Selection(Team.NEITHER, "No score");
        s1.setProb(probNoScore);
        scorecastFirst.put(s1.getName(), s1);
        Selection s2 = new Selection(Team.NEITHER, "No score");
        s2.setProb(probNoScore);
        wincastFirst.put(s2.getName(), s2);
        Selection s3 = new Selection(Team.NEITHER, "No score");
        s3.setProb(probNoScore);
        scorecastAny.put(s3.getName(), s3);
        Selection s4 = new Selection(Team.NEITHER, "No score");
        s4.setProb(probNoScore);
        wincastAny.put(s4.getName(), s4);

        /*
         * create the margined prices and check the probs
         */
        double[] wincastFirstProbs = new double[wincastFirst.size()];
        double[] scorecastFirstProbs = new double[scorecastFirst.size()];
        double[] wincastAnyProbs = new double[wincastAny.size()];
        double[] scorecastAnyProbs = new double[scorecastAny.size()];
        double sumWincastFirstProbs = 0;
        double sumScorecastFirstProbs = 0;
        int i;
        i = 0;
        for (Selection s : wincastFirst.values()) {
            wincastFirstProbs[i++] = s.getProb();
            sumWincastFirstProbs += s.getProb();
        }
        i = 0;
        for (Selection s : scorecastFirst.values()) {
            scorecastFirstProbs[i++] = s.getProb();
            sumScorecastFirstProbs += s.getProb();
        }
        i = 0;
        for (Selection s : wincastAny.values()) {
            wincastAnyProbs[i++] = s.getProb();
        }
        i = 0;
        for (Selection s : scorecastAny.values()) {
            scorecastAnyProbs[i++] = s.getProb();
        }

        if (Math.abs(sumScorecastFirstProbs - 1) > 0.005 || Math.abs(sumWincastFirstProbs - 1) > 0.005)
            throw new IllegalArgumentException(String.format(
                            "Calculated probs don't sum to 1.\n   scorecastFirst: %.3f, wincastFirst: %.3f",
                            sumScorecastFirstProbs, sumWincastFirstProbs));

        for (Selection s : wincastFirst.values()) {
            s.setPrice(MarginingV11.addMargin(s.getProb(), wincastFirst.size(), wincastFirstMarginPerSelection));
        }
        for (Selection s : scorecastFirst.values()) {
            s.setPrice(MarginingV11.addMargin(s.getProb(), scorecastFirst.size(), scorecastFirstMarginPerSelection));
        }
        for (Selection s : wincastAny.values()) {
            s.setPrice(MarginingV11.addMargin(s.getProb(), wincastAny.size(), wincastAnyMarginPerSelection));
        }
        for (Selection s : scorecastAny.values()) {
            s.setPrice(MarginingV11.addMargin(s.getProb(), scorecastAny.size(), scorecastAnyMarginPerSelection));
        }

        updatedScorecastFirst = new LinkedHashMap<String, Selection>();
        updatedScorecastAny = new LinkedHashMap<String, Selection>();

        finalScorecastFirst = new LinkedHashMap<String, Selection>();
        finalScorecastAny = new LinkedHashMap<String, Selection>();
        finalWincastFirst = new LinkedHashMap<String, Selection>();
        finalWincastAny = new LinkedHashMap<String, Selection>();

        for (Selection s : scorecastFirst.values()) {
            if (s.getName().contains("20-20")) {
            } else {
                updatedScorecastFirst.put(s.getName(), s);
            }
        }

        for (Selection s : scorecastAny.values()) {
            if (s.getName().contains("20-20")) {
            } else {
                updatedScorecastAny.put(s.getName(), s);
            }
        }

        double minSC = oddsLadderSCPS[0];
        double maxSC = oddsLadderSCPS[oddsLadderSCPS.length - 1];

        double minWC = oddsLadderWCPS[0];
        double maxWC = oddsLadderWCPS[oddsLadderWCPS.length - 1];

        for (Selection end : updatedScorecastFirst.values()) {
            end.setPrice(oddsLadderChangeSC(end.getPrice(), minSC, maxSC));
            finalScorecastFirst.put(end.getName(), end);
        }
        for (Selection end : updatedScorecastAny.values()) {
            end.setPrice(oddsLadderChangeSC(end.getPrice(), minSC, maxSC));
            finalScorecastAny.put(end.getName(), end);
        }

        for (Selection end : wincastFirst.values()) {
            end.setPrice(oddsLadderChangeWC(end.getPrice(), minWC, maxWC));
            finalWincastFirst.put(end.getName(), end);
        }

        for (Selection end : wincastAny.values()) {
            end.setPrice(oddsLadderChangeWC(end.getPrice(), minWC, maxWC));
            finalWincastAny.put(end.getName(), end);
        }

        /**
         * Rules to adjust for lower limits
         **/

        /*
         * Scorecast First
         */

        for (Selection cs : correctScore.values()) {
            for (Selection cast : finalScorecastFirst.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("Correct Score First SC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        for (Selection cs : firstToScore2.values()) {
            for (Selection cast : finalScorecastFirst.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("First to Score First SC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        /*
         * Wincast First
         */

        for (Selection cs : correctScore.values()) {
            for (Selection cast : finalWincastFirst.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("Correct Score First WC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        for (Selection cast : finalWincastFirst.values()) {
            switch (cast.getTeam()) {
                case A:
                    if (cast.getPrice() <= priceTeamAWin) {
                        log.debug("Winning Prices First WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceTeamAWin));
                    }
                    break;
                case B:
                    if (cast.getPrice() <= priceTeamBWin) {
                        log.debug("Winning Prices First WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceTeamBWin));
                    }
                    break;
                case NEITHER:
                    if (cast.getPrice() <= priceDraw) {
                        log.debug("Winning Prices First WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceDraw));
                    }
                    break;
            }
        }

        /*
         * Scorecast Any
         */

        for (Selection cs : correctScore.values()) {
            for (Selection cast : finalScorecastAny.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("Correct Score Any SC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        for (Selection cs : anytimeToScore2.values()) {
            for (Selection cast : finalScorecastAny.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("First to Score Any SC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        /*
         * Wincast Any
         */

        for (Selection cs : correctScore.values()) {
            for (Selection cast : finalWincastAny.values()) {
                if (cast.getName().contains(cs.getName()) && cast.getPrice() <= cs.getPrice()) {
                    log.debug("Correct Score Any WC Change --> Name = " + cast.getName());
                    cast.setPrice(jumpLadderSC(cast.getPrice(), maxSC, cs.getPrice()));
                }
            }
        }

        for (Selection cast : finalWincastAny.values()) {
            switch (cast.getTeam()) {
                case A:
                    if (cast.getPrice() <= priceTeamAWin) {
                        log.debug("Winning Prices Any WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceTeamAWin));
                    }
                    break;
                case B:
                    if (cast.getPrice() <= priceTeamBWin) {
                        log.debug("Winning Prices Any WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceTeamBWin));
                    }
                    break;
                case NEITHER:
                    if (cast.getPrice() <= priceDraw) {
                        log.debug("Winning Prices Any WC Change --> Name = " + cast.getName());
                        cast.setPrice(jumpLadderWC(cast.getPrice(), maxWC, priceDraw));
                    }
                    break;
            }
        }

        finalWincastAny.get("No score").setPrice(priceNoScore);
        finalScorecastAny.get("No score").setPrice(priceNoScore);
        finalWincastFirst.get("No score").setPrice(priceNoScore);
        finalScorecastFirst.get("No score").setPrice(priceNoScore);

        for (Selection fsa : finalScorecastAny.values()) {
            for (Selection fsf : finalScorecastFirst.values()) {
                if (fsa.getName().contains(fsf.getName())
                                && (fsa.getName().contains("1-0") || fsa.getName().contains("0-1"))) {

                    fsa.setPrice(fsf.getPrice());
                    log.debug("Name = " + fsa.getName() + ", FSF = " + fsf.getPrice() + ", FSA = " + fsa.getPrice());
                }
            }
        }

    }

    private double jumpLadderSC(double value, double max, double floor) {
        int store = 0;
        for (int i = 0; i < oddsLadderSCPS.length; i++) {
            if (value == oddsLadderSCPS[i] && value < max) {
                store = i;
            }
        }
        log.debug("SC before = " + value + ", Floor = " + floor);
        value = oddsLadderSCPS[store + 1];

        while (value <= floor && value < max) {
            log.debug("Still increasing with Cast = " + value + " & Floor = " + floor);
            store++;
            value = oddsLadderSCPS[store];

        }
        log.debug("SC after = " + value);
        return value;
    }

    private double jumpLadderWC(double value, double max, double floor) {
        int store = 0;
        for (int i = 0; i < oddsLadderWCPS.length; i++) {
            if (value == oddsLadderWCPS[i] && value < max) {
                store = i;
            }
        }
        log.debug("WC before = " + value + ", Floor = " + floor);
        value = oddsLadderWCPS[store + 1];

        while (value <= floor && value < max) {
            log.debug("Still increasing with Cast = " + value + " & Floor = " + floor);
            store++;
            value = oddsLadderWCPS[store];
        }

        log.debug("WC after = " + value);
        return value;
    }

    private double oddsLadderChangeSC(double value, double min, double max) {
        double store = 0;
        if (value <= min) {
            store = min;
        } else if (value >= max) {
            store = max;
        } else {
            store = min;
            for (int j = 1; j < oddsLadderSCPS.length - 1; j++) {
                if (value > oddsLadderSCPS[j]) {
                    store = oddsLadderSCPS[j];
                } else {
                    value = store;
                    break;
                }
            }
        }
        value = store;
        return value;
    }

    private double oddsLadderChangeWC(double value, double min, double max) {
        // double compare = value;
        double store = 0;
        if (value <= min) {
            store = min;
        } else if (value >= max) {
            store = max;
        } else {
            store = min;
            for (int j = 1; j < oddsLadderWCPS.length - 1; j++) {
                if (value > oddsLadderWCPS[j]) {
                    store = oddsLadderWCPS[j];
                } else {
                    if (store < 3)
                        store = oddsLadderWCPS[j + 1];
                    value = store;
                    break;
                }
            }
        }
        value = store;
        return value;
    }

    /**
     * parses string like "12-7" to extract # goals scored by team
     * 
     * @param score
     * @param playersTeam must be A or B
     * @return
     */
    @SuppressWarnings("unused")
    private int calcGoalsScoredByTeam(String score, Team playersTeam) {
        int scoreA = 0;
        int scoreB = 0;

        if (playersTeam == Team.NEITHER)
            throw new IllegalArgumentException("Invalideteam");
        int index = score.indexOf("-");
        int len = score.length();
        try {
            scoreA = Integer.parseInt(score.substring(0, index));
            scoreB = Integer.parseInt(score.substring(index + 1, len));
        } catch (Exception ex) {
            log.error("Teamprob1 = " + playersTeam + ", Score = " + score + ", index = " + index);
        }
        if (playersTeam == Team.A)
            return scoreA;
        else
            return scoreB;
    }

    /**
     * returns the id of the winning team given the score
     * 
     * @param name
     * @return
     */
    private Team matchResult(String name) {
        int scoreA = 0;
        int scoreB = 0;
        int index = name.indexOf('-');
        int len = name.length();
        try {
            scoreA = Integer.parseInt(name.substring(0, index));
            scoreB = Integer.parseInt(name.substring(index + 1, len));
        } catch (Exception ex) {
            log.error("Namematch = " + name + ", index = " + index);
        }
        Team team = Team.NEITHER;
        if (scoreA > scoreB)
            team = Team.A;
        if (scoreA < scoreB)
            team = Team.B;
        return team;
    }

    /**
     * calculates the probability that specified team scores first given score of form n-m.
     * 
     * @param team
     * @param name must be of the form "n-m"
     * @return prob between 0 and 1. = 0 if A does not score
     */
    private double probTeamScoresFirst(Team team, String name) {

        int scoreA = 0;
        int scoreB = 0;

        int index = name.indexOf('-');
        int len = name.length();

        try {
            scoreA = Integer.parseInt(name.substring(0, index));
            scoreB = Integer.parseInt(name.substring(index + 1, len));
        } catch (Exception ex) {
            log.error("Teamprob = " + team + ", Name = " + name + ", index = " + index);
        }
        double prob = 0;
        switch (team) {
            case A:
                if (scoreA == 0)
                    prob = 0;
                else
                    prob = (double) scoreA / (double) (scoreA + scoreB);
                break;
            case B:
                if (scoreB == 0)
                    prob = 0;
                else
                    prob = (double) scoreB / (double) (scoreA + scoreB);
                break;
            case NEITHER:
                break;
        }
        return prob;
    }

    private double probTeamScoresAnytime(Team team, String name) {

        int scoreA = 0;
        int scoreB = 0;

        int index = name.indexOf('-');
        int len = name.length();

        try {
            scoreA = Integer.parseInt(name.substring(0, index));
            scoreB = Integer.parseInt(name.substring(index + 1, len));
        } catch (Exception ex) {
            log.error("Teamprob = " + team + ", Name = " + name + ", index = " + index);
        }
        double prob = 0;
        switch (team) {
            case A:
                if (scoreA == 0)
                    prob = 0;
                else
                    prob = 1;
                break;
            case B:
                if (scoreB == 0)
                    prob = 0;
                else
                    prob = 1;
                break;
            case NEITHER:
                break;
        }
        return prob;
    }

    /**
     * demargin the supplied prices, updating pricesMap with the calculated probs
     * 
     * @param margining the margining class to use
     * @param pricesMap the set of input prices, updated on exit with the calculated probabilities
     * @param expectedProbsTotal the total the probs are expected to sum to
     */
    private void demargin(Margining margining, Map<String, Selection> pricesMap, double expectedProbsTotal) {
        double[] prices = new double[pricesMap.size()];
        int i = 0;
        for (Selection s : pricesMap.values())
            prices[i++] = s.getPrice();
        double[] probs = margining.removeMargin(prices, expectedProbsTotal);
        i = 0;
        for (Selection s : pricesMap.values())
            s.setProb(probs[i++]);
        /*
         * output info if required
         */
        if (outputDemarginedInputPrices) {
            double[] prices2 = margining.addMargin(probs, margining.getTotalMargin());
            i = 0;
            for (Selection s : pricesMap.values()) {
                System.out.printf("%s: input price: %.2f, calc prob: %.3f, calc remargined price: %.2f\n", s.getName(),
                                s.getPrice(), s.getProb(), prices2[i]);
                i++;
            }
            // System.out.printf("Margining param: %.3f, #iterations: %d, %d,
            // min cost: %.3f, sum probs: %.3f\n\n",
            // margining.getTotalMargin(), margining.getNoIterations(),
            // margining.getNoLoops(),
            // margining.getCostAtMinimum(), sumProbs);
        }
    }

    private int numGoals(String name) {
        int scoreA = 0;
        int scoreB = 0;
        int index = name.indexOf('-');
        int len = name.length();
        try {
            scoreA = Integer.parseInt(name.substring(0, index));
            scoreB = Integer.parseInt(name.substring(index + 1, len));
        } catch (Exception ex) {
            log.error("Namematch = " + name + ", index = " + index);
        }

        return scoreA + scoreB;
    }

    //
    // static void waitForUser(String s) {
    // System.out.println(s + ". Press enter to continue");
    // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    // try {
    // in.readLine();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
}
