package example.algo.tennis;

import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.tennis.PlayerId;
import ats.algo.sport.tennis.TennisMatchState;

/**
 * creates instances of each of the supplied market types from the supplied params.
 * 
 * @author Geoff
 *
 */
public class ExampleTennisMarketCalcs {
    /**
     * Generates an example match winner market.
     */
    static Market generateMatchWinnerMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams) {

        Market market = new Market(MarketCategory.GENERAL, "FT:ML", matchState.getSequenceIdForMatch(), "Match Winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setSequenceId(matchState.getSequenceIdForMatch());
        /*
         * the prob of A winning the match is just set to the value of the onServePctA parameter. note the use of the
         * method getBiasAdjustedMean this returns the value of the param amended by any bias the trader may have set.
         */
        double p = matchParams.getOnServePctA().getGaussian().getBiasAdjustedMean();
        market.put("A", p);
        market.put("B", 1 - p);
        return market;
    }

    /**
     * Generates an example total games market.
     * 
     * @param matchParams
     */
    static Market generateTotalGamesMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams) {

        Market market = new Market(MarketCategory.OVUN, "FT:OU", matchState.getSequenceIdForMatch(),
                        "Match total games");
        market.setIsValid(true);
        market.setLineBase(13);
        /*
         * The key to creating a total or handicap market is creation of the "probs" array. This array holds a set of
         * probabilities where probs [n] is the probability of total games being <= lineBase+ n. So in this example
         * probs[2] = 0.032 is the probability that the total no of games will be less than or equal to 13+2 = 15. This
         * example uses fixed probs instead of calculating from a model
         */

        double[] probs = {0.002, 0.011, 0.032, 0.068, 0.129, 0.199, 0.278, 0.354, 0.406, 0.484, 0.543, 0.558, 0.594,
                0.641, 0.674, 0.713, 0.756, 0.799, 0.846, 0.891, 0.919, 0.941, 0.968, 0.986, 0.988, 0.994, 1.000};
        market.setLineProbs(probs);
        market.setLineId("22.5"); // lineId is the balanced line - i.e. the one
                                  // that has prob closest to 0.5
        double probUnder = .484; // i.e. probs[9]
                                 // (=21-13)

        market.put("Under", probUnder);
        market.put("Over", 1 - probUnder);
        market.setSelectionNameOverOrA("Over 22.5");
        market.setSelectionNameUnderOrB("Under 22.5");
        return market;
    }

    /**
     * Generates an example handicap market.
     * 
     * @param matchParams
     */
    static Market generateGamesHandicapMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams) {
        Market market = new Market(MarketCategory.HCAP, "FT:SPRD", matchState.getSequenceIdForMatch(),
                        "Match total games handicap");
        market.setIsValid(true);
        market.setLineBase(-10);
        /*
         * The key to creating a handicap market is creation of the "probs" array. This array holds a set of
         * probabilities where probs [n] is the probability of games difference being <= lineBase+ n. So in this example
         * probs[3] = 0.078 is the probability that totalGamesA-totalGamesB will be less than or equal to -10 + 3 = -7.
         * This example uses fixed probs instead of calculating from a model
         */

        double[] probs = {0.004, 0.015, 0.035, 0.078, 0.136, 0.215, 0.309, 0.386, 0.441, 0.480, 0.516, 0.553, 0.608,
                0.685, 0.777, 0.858, 0.918, 0.959, 0.981, 0.993, 0.997};
        market.setLineProbs(probs);
        market.setLineId("-0.5"); // subType is the balanced line - i.e. the
                                  // one with prob closest to 0.5
        double probB = .516; // i.e. the sum of probs[10]

        market.put("A", 1 - probB);
        market.put("B", probB);
        market.setSelectionNameOverOrA("A(-0.5)");
        market.setSelectionNameUnderOrB("B(+0.5)");
        return market;

    }

    /**
     * generates example game winner market. Uses fixed probs instead of calculating from a model
     * 
     * @param matchParams
     */
    static Market generateThisGameWinnerMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams) {
        String sequenceId = matchState.getSequenceIdForGame(0);
        Market market = null;
        if (sequenceId != null) {
            market = new Market(MarketCategory.GENERAL, "G:ML", sequenceId, "This game winner");
            market.setIsValid(true);
            market.put("A", .7);
            market.put("B", .3);
        }
        return market;
    }

    /**
     * Generates example next game winner market. Uses the contents of the MatchEngineSavedState object to show how this
     * is done. The prob of a winning next game is set to nPointsWonByA/ (nPointsWonByA + nPointsWonbyB + 1). Obviously
     * this is for illustrative purposes only - not a correct next game winner prob calculation
     * 
     * @param matchParams
     */
    static Market generateNextGameWinnerMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams,
                    ExampleSavedState savedState) {

        String sequenceId = matchState.getSequenceIdForGame(1);
        Market market = null;
        if (sequenceId != null) {
            market = new Market(MarketCategory.GENERAL, "G:ML", sequenceId,
                            String.format("Winner of next game (set %d, game %d)", matchState.getSetNo(),
                                            matchState.getGameNo() + 1));
            market.setIsValid(true);
            double nPointsWonByA = (double) savedState.getnPointsWonByA();
            double nPointsWonByB = (double) savedState.getnPointsWonByB();
            double p = nPointsWonByA / (nPointsWonByA + nPointsWonByB + 1);
            market.put("A", p);
            market.put("B", 1 - p);
        }
        return market;
    }

    /**
     * generates the market for the winner of point+2 in the current game.
     */
    static Market generatePointPlus2WinnerMarket(TennisMatchState matchState, ExampleTennisMatchParams matchParams) {
        String sequenceId = matchState.getSequenceIdForPoint(2);
        PlayerId playerId = matchState.getOnServePlayerNow();
        Market market = null;
        if (sequenceId != null && playerId != PlayerId.UNKNOWN) {
            market = new Market(MarketCategory.GENERAL, "G:PW", sequenceId,
                            String.format("Winner of point %d in current game", matchState.getPointNo() + 2));
            market.setIsValid(true);
            double onServePct = matchParams.getOnServePct(playerId).getMean();
            double probTeamAWinsPoint;
            if (matchState.getOnServeTeamNow() == TeamId.A)
                probTeamAWinsPoint = onServePct;
            else
                probTeamAWinsPoint = 1 - onServePct;
            market.put("A", probTeamAWinsPoint);
            market.put("B", 1 - probTeamAWinsPoint);
        }
        return market;
    }
}
