package ats.algo.scorecast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.margining.Margining;
import ats.algo.scorecast.Selection.Team;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ScorecastCalculatorV2 {

    static boolean outputDemarginedInputPrices = false;;

    private static Logger log = LoggerFactory.getLogger("ScorecastCalculatorV2");

    /**
     * generates the probabilities and prices for the wincast and scorecast markets from the supplied correctScore and
     * firsttoScore prices
     * 
     * @param correctScore set of Selections for the correct score. Each Selection must obey the following rules:
     * 
     *        name property must be "n-m" or "n:m" to identify the score, so e.g. "3-2" or "3:2". The no score selection
     *        should be identified by either "0-0" or "0:0"
     * 
     *        team property is ignored. To avoid confusion recommend setting to NEITHER
     * 
     *        prices property must be set to the market price for that score.
     * 
     *        probability property is ignored. To avoid confusion recommend setting to -1
     * @param firstToScore set of Selections for the first player to score in the match. Each Selection must obey the
     *        following rules:
     * 
     *        name property must be set to the name of the player, e.g. "George Best" or "A. Pell". If a no score
     *        selection is included in the prices it MUST have the name "No score".
     * 
     *        team property must be set to the team that player belongs to: Team.A or Team.B
     * 
     *        prices property must be set to the market price for that selection.
     * 
     *        probability property is ignored. To avoid confusion recommend setting to -1
     * @param correctScorePricesIncludeMargin if true then the supplied prices are demargined. If false then it is
     *        assumed that the supplied prices are 100% prices and the corresponding probability can be obtained by
     *        simple inversion - i.e. prob = 1/Price
     * @param firstToScorePricesIncludeMargin if true then the supplied prices are demargined. If false then it is
     *        assumed that the supplied prices are 100% prices and the corresponding probability can be obtained by
     *        simple inversion - i.e. prob = 1/Price
     * @return a map containing two objects each of type Map<String,Selection>. and with keys "scorecast" and "wincast".
     *         Each containing the set of selections for the relevant market
     * 
     *         Selection names for the scorecast market are of the form "name and n-m", so e.g. "A.Pell and 2-3".
     *         Selection names for the wincast market are of the form "name and Team.X to win".
     * 
     *         Selection team property is set to the id of the team that that player belongs to, as per the firstToScore
     *         input Selections.
     * 
     *         Selection prob property is set to the calculated probability.
     * 
     *         Selection price property is set to -1.
     * 
     *         If an error occurs during the processing of the inputs then an error message will be logged and null will
     *         be returned
     * 
     */

    public static Map<String, Map<String, Selection>> calculate(Map<String, Selection> correctScore,
                    Map<String, Selection> firstToScore, boolean correctScorePricesIncludeMargin,
                    boolean firstToScorePricesIncludeMargin) {
        Map<String, Selection> wincast;
        Map<String, Selection> scorecast;
        log.debug(" ScorecastCalculatorV2 calculate method invoked.  Input params:");
        log.debug("correctScore");
        for (Selection s : correctScore.values()) {
            log.debug(s.toString());
        }
        log.debug("firstToScore");
        for (Selection s : firstToScore.values()) {
            log.debug(s.toString());
        }
        log.debug("correctScorePricesIncludeMargin: %b", correctScorePricesIncludeMargin);
        log.debug("firstToScorePricesIncludeMargin: %b", firstToScorePricesIncludeMargin);
        /*
         * demargin the correct score prices
         */
        Margining margining = new Margining();
        margining.setMarginingAlgoToEntropy();
        demargin(margining, correctScore, 1.0, correctScorePricesIncludeMargin);
        /*
         * demargin the first to score prices. If firsttoScore includes no score selection then the probs should sum to
         * 1.0. If not then the probs should sum to 1.0 - prob(no score)
         */
        boolean firstToScoreDoesNotIncludeNoScoreSelection = firstToScore.get("No score") == null;
        double probNoScore = extractNoScoreProb(correctScore);
        if (probNoScore == -1.0) {
            log.error("correctScore prices do not include a no score selection ('0-0' or '0:0')");
            return null;
        }
        double sumFirstToScoreProbs = 1.0;
        if (firstToScoreDoesNotIncludeNoScoreSelection)
            sumFirstToScoreProbs = 1.0 - probNoScore;
        demargin(margining, firstToScore, sumFirstToScoreProbs, firstToScorePricesIncludeMargin);

        /*
         * calculate the sum of the probs of each team scoring first, for use later in generating the conditional
         * probabilities
         */
        double sumProbsFirstToScoreTeamA = 0;
        double sumProbsFirstToScoreTeamB = 0;
        for (Selection firstToScoreSelection : firstToScore.values()) {
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

        scorecast = new LinkedHashMap<String, Selection>();
        wincast = new LinkedHashMap<String, Selection>();

        for (Selection firstToScoreSelection : firstToScore.values()) {
            /*
             * ignore the no score selection
             */
            if (!firstToScoreSelection.getName().equals("No score")) {
                Team playersTeam = firstToScoreSelection.getTeam();
                double pWinCastFirstAWins = 0;
                double pWinCastFirstDraw = 0;
                double pWinCastFirstBWins = 0;
                for (Selection correctScoreSelection : correctScore.values()) {

                    String correctScoreSelectionName = correctScoreSelection.getName();
                    PairOfIntegers score = PairOfIntegers.generateFromString(correctScoreSelectionName, "-");
                    if (score == null)
                        score = PairOfIntegers.generateFromString(correctScoreSelectionName, ":");
                    if (score == null) {
                        log.error("Cannot parse correctScoreSelection name: %s.  Must be in format 'n-m' or 'n:m'.",
                                        correctScoreSelection.getName());
                        return null;
                    }

                    String name = firstToScoreSelection.getName() + " and " + score.toScoreString();
                    Selection scoreCastFirstSelection = new Selection(playersTeam, name);
                    /*
                     * Calc scorecast market. calc is: prob = prob (final score) * prob(team scores first | final score)
                     * * prob (playerScoresfirst | team scores first)
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
                            log.error("Team id not specified for firstToScoreSelection: %s",
                                            firstToScoreSelection.getName());
                            return null;
                    }

                    // log.debug("PlayersTeam = " + playersTeam + ",
                    // correctScoreSelection.name = " +
                    // correctScoreSelection.name);

                    double probPlayersTeamScoresFirst = probTeamScoresFirst(firstToScoreSelection.getTeam(), score);
                    scoreCastFirstSelection.setProb(correctScoreSelection.getProb() * probPlayersTeamScoresFirst
                                    * probPlayerScoresFirstGivenTeamScoresfirst);
                    if (scoreCastFirstSelection.getProb() > 1.0e-6) {
                        /*
                         * don't add selections with zero or near zero probability
                         */
                        scorecast.put(scoreCastFirstSelection.getName(), scoreCastFirstSelection);
                    }
                    /*
                     * increment the wincast probs
                     */
                    Team winningTeam = matchResult(correctScoreSelectionName);
                    if (winningTeam == null) {
                        log.error("Error parsing score from correct score selection: %s.  Selection name not in format 'n-m' or 'n:m'",
                                        correctScoreSelectionName);
                        return null;
                    }
                    switch (winningTeam) {
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
                String winCastName1 = firstToScoreSelection.getName() + " and TeamA wins";
                String winCastName2 = firstToScoreSelection.getName() + " and TeamB wins";
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
                wincast.put(winCastFirstSelection1.getName(), winCastFirstSelection1);
                wincast.put(winCastFirstSelection2.getName(), winCastFirstSelection2);
                wincast.put(winCastFirstSelection3.getName(), winCastFirstSelection3);
            }
        }
        Map<String, Map<String, Selection>> results = new HashMap<>(2);
        results.put("scorecast", scorecast);
        results.put("wincast", wincast);
        return results;
    }

    private static double extractNoScoreProb(Map<String, Selection> correctScore) {
        Selection noScoreSelection = correctScore.get("0-0");
        if (noScoreSelection == null)
            noScoreSelection = correctScore.get("0:0");
        if (noScoreSelection == null)
            return -1.0;
        else
            return noScoreSelection.getProb();
    }

    /**
     * parses string like "12-7" to extract # goals scored by team
     * 
     * @param score
     * @param playersTeam must be A or B
     * @return
     */

    /**
     * returns the id of the winning team given the selection name "n:m" or "n-m". If error parsing the selection name
     * then null is returned
     * 
     * @param name
     * @return
     */
    private static Team matchResult(String name) {
        int scoreA = 0;
        int scoreB = 0;
        int index = name.indexOf('-');
        if (index == -1)
            index = name.indexOf(':');
        if (index == -1)
            return null;
        int len = name.length();
        try {
            scoreA = Integer.parseInt(name.substring(0, index));
            scoreB = Integer.parseInt(name.substring(index + 1, len));
        } catch (Exception ex) {
            return null;
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
     * @param name must be of the form "n-m" or "n:m"
     * @return prob between 0 and 1. = 0 if A does not score
     */
    private static double probTeamScoresFirst(Team team, PairOfIntegers score) {

        double prob = 0;
        switch (team) {
            case A:
                if (score.A == 0)
                    prob = 0;
                else
                    prob = (double) score.A / (double) (score.A + score.B);
                break;
            case B:
                if (score.B == 0)
                    prob = 0;
                else
                    prob = (double) score.B / (double) (score.A + score.B);
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
    private static void demargin(Margining margining, Map<String, Selection> pricesMap, double expectedProbsTotal,
                    boolean pricesIncludeMargin) {

        if (pricesIncludeMargin) {
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
                    System.out.printf("%s: input price: %.2f, calc prob: %.3f, calc remargined price: %.2f\n",
                                    s.getName(), s.getPrice(), s.getProb(), prices2[i]);
                    i++;
                }
            }
        } else {
            /*
             * prices do not include margin so avoid overhead of running the demargining algo
             */
            for (Selection s : pricesMap.values()) {
                s.setProb(1 / s.getPrice());
            }

        }
    }

}
