package ats.algo.sport.tennisG;


import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.sport.tennisG.TennisGMatchState;

public class TennisGMatchResultMarkets extends MatchResultMarkets {

    TennisGMatchState previousMatchState;
    TennisGMatchState currentMatchState;
    int marketSetNo;
    int marketGameNo;
    int marketPointNo;

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (TennisGMatchState) previousMatchState;
        this.currentMatchState = (TennisGMatchState) currentMatchState;

        parseSequenceId(market.getSequenceId());

        String winningSelection;
        switch (market.getType()) {
            case "FT:ML": // Match winner
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    if (((TennisGMatchState) currentMatchState).getSetsA() > ((TennisGMatchState) currentMatchState)
                                    .getSetsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);

                } else
                    return null;

            case "FT:W1S:A": // A to win at least one set
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    if (((TennisGMatchState) currentMatchState).getSetsA() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:W1S:B": // B to win at least one set
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    if (((TennisGMatchState) currentMatchState).getSetsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CS": // Match set correct score
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", ((TennisGMatchState) currentMatchState).getSetsA(),
                                    ((TennisGMatchState) currentMatchState).getSetsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:NUMSET": // No of sets played in match
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    winningSelection = String.format("%d", ((TennisGMatchState) currentMatchState).getSetsA()
                                    + ((TennisGMatchState) currentMatchState).getSetsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:SPRD": // Match total games handicap
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    int n = ((TennisGMatchState) currentMatchState).getSetsA()
                                    - ((TennisGMatchState) currentMatchState).getSetsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU": // Match total games
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    int n = ((TennisGMatchState) currentMatchState).getSetsA()
                                    + ((TennisGMatchState) currentMatchState).getSetsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:TBIM": // Tie break played in match
                if (((TennisGMatchState) currentMatchState).isTieBreakAlreadyPlayedInMatch()) {
                    return new CheckMarketResultedOutcome("Yes");
                } else if (((TennisGMatchState) currentMatchState).isMatchCompleted())
                    return new CheckMarketResultedOutcome("No");
                else
                    return null;

            case "FT:OU:A": // Match total games A
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    int n = ((TennisGMatchState) currentMatchState).getSetsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:B": // Match total games B
                if (((TennisGMatchState) currentMatchState).isMatchCompleted()) {
                    int n = ((TennisGMatchState) currentMatchState).getSetsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "G:ML": // Winner of game
                return checkGameOrTieBreakWinnerResulted();

            case "G:PW": // Winner of point within game
                return checkPointMktResulted();

            case "FT:PW5G": // First player to win 5 games in set
                return checkFirstToFiveGamesResulted();

            case "P:TBML": // Current set tie break winner
                return checkGameOrTieBreakWinnerResulted();

            case "P:TBCS": // Tie break correct score
                return checkGameOrTieBreakCorrectScoreResulted();

            case "G:DEUCE": // game reaches deuce
                return checkGameReachesDeuceResulted();

            case "G:CS": // game correct score
                return checkGameOrTieBreakCorrectScoreResulted();
        }
        return null;
    }

    /**
     * parses the sequence id into the local variables marketSetNo, marketGameNo and marketPointNo sets to 0 if not
     * present
     * 
     * @param sequenceId
     */
    private void parseSequenceId(String sequenceId) {
        marketSetNo = 0;
        marketGameNo = 0;
        marketPointNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, 2);
            marketSetNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketGameNo = Integer.parseInt(bits[1]);
        if (bits.length >= 3)
            marketPointNo = Integer.parseInt(bits[2]);
    }

    // @Test
    // public void test() {
    // String sequenceId = "S1.3.12";
    // parseSequenceId(sequenceId);
    // sequenceId = "S1.3";
    // parseSequenceId(sequenceId);
    // sequenceId = "";
    // parseSequenceId(sequenceId);
    // }

    private CheckMarketResultedOutcome checkGameOrTieBreakCorrectScoreResulted() {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) currentMatchState).getSetNo(),
                        ((TennisGMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the game was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) previousMatchState).getSetNo(),
                        ((TennisGMatchState) previousMatchState).getGameNo(), 0);
        if (previousStateSeqNo == marketSeqNo) {
            int pointsA = ((TennisGMatchState) currentMatchState).getLastGameOrTieBreakPointsA();
            int pointsB = ((TennisGMatchState) currentMatchState).getLastGameOrTieBreakPointsB();
            String winningSelection = String.format("%d-%d", pointsA, pointsB);
            return new CheckMarketResultedOutcome(winningSelection);
        } else {
            /*
             * game was never played so return void
             */
            return new CheckMarketResultedOutcome();
        }
    }

    private CheckMarketResultedOutcome checkGameReachesDeuceResulted() {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 7);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) currentMatchState).getSetNo(),
                        ((TennisGMatchState) currentMatchState).getGameNo(),
                        ((TennisGMatchState) currentMatchState).getPointNo());
        if (currentStateSeqNo < marketSeqNo)
            return null; // market not yet resulted
        CheckMarketResultedOutcome outcome;
        if (currentStateSeqNo == marketSeqNo)
            /*
             * if current state point no = 7 then score must be 40-40
             */
            outcome = new CheckMarketResultedOutcome("Yes");
        else { // currentStateSeqNo > marketSeqNo
            int previousStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) previousMatchState).getSetNo(),
                            ((TennisGMatchState) previousMatchState).getGameNo(), 7);
            if (previousStateSeqNo == marketSeqNo) {
                /*
                 * previous game was played but did not reach 40-40
                 */
                outcome = new CheckMarketResultedOutcome("No");
            } else {
                /*
                 * game was never played
                 */
                outcome = new CheckMarketResultedOutcome();
            }
        }
        return outcome;
    }



    private CheckMarketResultedOutcome checkFirstToFiveGamesResulted() {
        int currentSetNo = ((TennisGMatchState) currentMatchState).getSetNo();
        if (currentSetNo != marketSetNo)
            /*
             * not in correct set for this market
             */
            return null;
        int gamesA = ((TennisGMatchState) currentMatchState).getGamesA();
        int gamesB = ((TennisGMatchState) currentMatchState).getGamesB();
        if (gamesA < 5 && gamesB < 5)
            return null; // market not yet resulted
        String winningSelection;
        if (gamesA == 5)
            winningSelection = "A";
        else // gamesB must be 5
            winningSelection = "B";
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkPointMktResulted() {
        /*
         * check to see if we have gone beyond the point in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, marketPointNo);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) currentMatchState).getSetNo(),
                        ((TennisGMatchState) currentMatchState).getGameNo(),
                        ((TennisGMatchState) currentMatchState).getPointNo());
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the point was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) previousMatchState).getSetNo(),
                        ((TennisGMatchState) previousMatchState).getGameNo(),
                        ((TennisGMatchState) previousMatchState).getPointNo());
        if (previousStateSeqNo == marketSeqNo) {
            String winningSelection;
            if (((TennisGMatchState) currentMatchState).getLastPointPlayedOutcome() == TeamId.A)
                winningSelection = "A";
            else
                winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        } else {
            /*
             * point was never played
             */
            return new CheckMarketResultedOutcome();
        }
    }

    private CheckMarketResultedOutcome checkGameOrTieBreakWinnerResulted() {
        /*
         * check to see if we have gone beyond the game in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) currentMatchState).getSetNo(),
                        ((TennisGMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the game was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisGMatchState) previousMatchState).getSetNo(),
                        ((TennisGMatchState) previousMatchState).getGameNo(), 0);
        if (previousStateSeqNo == marketSeqNo) {
            String winningSelection;
            if (((TennisGMatchState) currentMatchState).getLastGamePlayedOutcome() == TeamId.A)
                winningSelection = "A";
            else
                winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        } else {
            /*
             * game was never played so return void
             */
            return new CheckMarketResultedOutcome();
        }
    }

    /**
     * returns an index which can be used for comparisons of order sequence. Assumes that games never exceeds 1000 in a
     * match
     * 
     * @param setNo
     * @param gameNo
     * @param pointNo
     * @return
     */
    private int convertSeqNoToIndex(int setNo, int gameNo, int pointNo) {
        return 1000000 * setNo + 1000 * gameNo + pointNo;
    }
}
