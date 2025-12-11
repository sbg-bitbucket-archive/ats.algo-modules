package ats.algo.sport.tennis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;
import ats.algo.sport.tennis.TennisMatchState;

public class TennisMatchResultMarkets extends MatchResultMarkets {

    TennisMatchState previousMatchState;
    TennisMatchState currentMatchState;
    int marketSetNo;
    int marketGameNo;
    int marketPointNo;
    boolean matchAbandoned = false;
    private String[] setXgameYCorrectScoreList = {"A-0", "A-15", "A-30", "A-AD", "B-0", "B-15", "B-30", "B-AD"};
    private static final String PARTIAL_RESULTING = "algo.tennis.partialResulting";
    boolean partialResulting = false;

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchStateIn) {

        String clients = System.getProperty(PARTIAL_RESULTING);
        if (clients != null)
            if (clients.toLowerCase().equals("true"))
                partialResulting = true;
        this.previousMatchState = (TennisMatchState) previousMatchState;
        this.currentMatchState = (TennisMatchState) currentMatchStateIn;

        parseSequenceId(market.getSequenceId());

        if (this.currentMatchState.getMostRecentMatchIncidentResult() != null)
            matchAbandoned = (this.currentMatchState.getMostRecentMatchIncidentResult()
                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHABANDONED);

        // FIXME: wether to use the team won here to result markets is to be
        // confirmed.
        // TeamId teamWonAbandon =
        // this.currentMatchState.getMostRecentMatchIncidentResult().getAbandonMatchWinner();

        String winningSelection;
        switch (market.getType()) {
            case "FT:ML": // Match winner
            case "FT:TW": // Match winner
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);

                } else if (matchAbandoned && !market.isDoNotResultThisMarket()) {
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "FT:W1SML": // Match winner, won 1st set
                if (currentMatchState.isMatchCompleted()) {
                    PairOfIntegers score = currentMatchState.getGameScoreInSetN(0);
                    int a = score.A;
                    int b = score.B;

                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB() && a > b)
                        return new CheckMarketResultedOutcome("A Yes");
                    else if (currentMatchState.getSetsA() < currentMatchState.getSetsB() && a < b)
                        return new CheckMarketResultedOutcome("B Yes");
                    else
                        return new CheckMarketResultedOutcome("A/B No");
                }

                return null;

            case "FT:L1SML": // Match winner, lose 1st set
                if (currentMatchState.isMatchCompleted()) {
                    // parseSequenceId(market.getSequenceId());
                    PairOfIntegers score = currentMatchState.getGameScoreInSetN(0);
                    int a = score.A;
                    int b = score.B;

                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB() && a < b)
                        return new CheckMarketResultedOutcome("A Yes");
                    else if (currentMatchState.getSetsA() < currentMatchState.getSetsB() && a > b)
                        return new CheckMarketResultedOutcome("B Yes");
                    else
                        return new CheckMarketResultedOutcome("A/B No");

                }

                return null;

            case "FT:CS": // Match correct score
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB())
                        winningSelection = String.format("A %d-%d", currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB());

                    else
                        winningSelection = String.format("B %d-%d", currentMatchState.getSetsB(),
                                        currentMatchState.getSetsA());
                    return new CheckMarketResultedOutcome(winningSelection);

                } else
                    return null;

            case "FT:BF": // team break point first
                if (!currentMatchState.isBreakPointStillPossible()) {
                    winningSelection = "None";
                    switch (currentMatchState.getTeamBreakFirst().toString()) {
                        case "A":
                            winningSelection = "A";
                            break;

                        case "B":
                            winningSelection = "A";
                            break;

                        case "UNKNOWN":
                            winningSelection = "None";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:FSB": // team break point first
                if (currentMatchState.getSetNo() > marketSetNo || currentMatchState.isServingBrokenInSet(marketSetNo)) {
                    winningSelection = "None";
                    switch (currentMatchState.getFirstServingBreakInSetN()[marketSetNo - 1]) {
                        case 1:
                            winningSelection = "1";
                            break;
                        case 2:
                            winningSelection = "2";
                            break;
                        case 3:
                            winningSelection = "3";
                            break;
                        case 4:
                            winningSelection = "4";
                            break;
                        case 5:
                            winningSelection = "5";
                            break;
                        case 6:
                            winningSelection = "6";
                            break;
                        case 7:
                            winningSelection = "7";
                            break;
                        case 8:
                            winningSelection = "8";
                            break;
                        case 9:
                            winningSelection = "9";
                            break;
                        case 10:
                            winningSelection = "10";
                            break;
                        case 11:
                            winningSelection = "10";
                            break;
                        default:
                            winningSelection = "None";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:FSB:A": // Player To break serve in which game.
                if (currentMatchState.getSetNo() > marketSetNo
                                || currentMatchState.isServingBrokenInSetA(marketSetNo)) {
                    winningSelection = "None";
                    int firstBreakA = currentMatchState.getFirstServingBreakAInSetN()[marketSetNo - 1];
                    if (firstBreakA == 0 || firstBreakA > 12)
                        winningSelection = "Doesn't break serve in set";
                    else if (firstBreakA < 5)
                        winningSelection = "Games 1-4";
                    else if (firstBreakA < 9)
                        winningSelection = "Games 5-8";
                    else
                        winningSelection = "After Game 8";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

                else
                    return null;

            case "FT:FSB:B": // team break point first
                if (currentMatchState.getSetNo() > marketSetNo
                                || currentMatchState.isServingBrokenInSetB(marketSetNo)) {
                    winningSelection = "None";
                    int firstBreakA = currentMatchState.getFirstServingBreakAInSetN()[marketSetNo - 1];
                    if (firstBreakA == 0 || firstBreakA > 12)
                        winningSelection = "Doesn't break serve in set";
                    else if (firstBreakA < 5)
                        winningSelection = "Games 1-4";
                    else if (firstBreakA < 9)
                        winningSelection = "Games 5-8";
                    else
                        winningSelection = "After Game 8";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

                else
                    return null;

            case "FT:W1S:A": // A to win at least one set
                if (currentMatchState.isMatchCompleted() || currentMatchState.getSetsA() > 0) {
                    if (currentMatchState.getSetsA() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:W1S:B": // B to win at least one set
                if (currentMatchState.isMatchCompleted() || currentMatchState.getSetsB() > 0) {
                    if (currentMatchState.getSetsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:NUMSET": // No of sets played in match
                if (currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d", currentMatchState.getSetsA() + currentMatchState.getSetsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:BTW1S": // Match and Both Players to win a Set
                if (currentMatchState.isMatchCompleted()) {
                    if (market.getSelections().size() == 2) {
                        if (currentMatchState.getSetsA() > currentMatchState.getSetsB()
                                        && currentMatchState.getSetsB() != 0)
                            return new CheckMarketResultedOutcome("Player A wins the match and both players win a set");
                        else if (currentMatchState.getSetsA() < currentMatchState.getSetsB()
                                        && currentMatchState.getSetsA() != 0)
                            return new CheckMarketResultedOutcome("Player B wins the match and both players win a set");
                        else
                            return new CheckMarketResultedOutcome();

                    } else {
                        if (currentMatchState.getSetsA() > currentMatchState.getSetsB()
                                        && currentMatchState.getSetsB() != 0)
                            return new CheckMarketResultedOutcome("Player A wins the match and both players win a set");
                        else if (currentMatchState.getSetsA() > currentMatchState.getSetsB()
                                        && currentMatchState.getSetsB() == 0)
                            return new CheckMarketResultedOutcome(
                                            "Player A wins the match and not both players win a set");
                        else if (currentMatchState.getSetsA() < currentMatchState.getSetsB()
                                        && currentMatchState.getSetsA() != 0)
                            return new CheckMarketResultedOutcome("Player B wins the match and both players win a set");
                        else if (currentMatchState.getSetsA() < currentMatchState.getSetsB()
                                        && currentMatchState.getSetsA() == 0)
                            return new CheckMarketResultedOutcome(
                                            "Player B wins the match and not both players win a set");
                    }
                } else
                    return null;

            case "FT:SPRD": // Match total games handicap
            case "FT:ASPRD1":
            case "FT:ASPRD2":
            case "FT:ASPRD3":
            case "FT:ASPRD4":
            case "FT:ASPRD5":
            case "FT:ASPRD6":
                if (currentMatchState.isMatchCompleted()) {
                    int n = currentMatchState.getTotalGamesA() - currentMatchState.getTotalGamesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    List<String> scores = new ArrayList<String>();
                    String scoreA = "AH=" + Integer.toString(this.currentMatchState.getTotalGamesA());
                    String scoreB = "BH=" + Integer.toString(this.currentMatchState.getTotalGamesB());
                    scores.add(scoreA);
                    scores.add(scoreB);
                    return new CheckMarketResultedOutcome(winningSelection, n, scores);
                } else
                    return null;
            case "FT:S:ASPRD":
            case "FT:S:SPRD": // Match total sets handicap
                if (currentMatchState.isMatchCompleted()) {
                    int n = currentMatchState.getSetsA() - currentMatchState.getSetsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    List<String> scores = new ArrayList<String>();
                    String scoreA = "AH=" + Integer.toString(this.currentMatchState.getSetsA());
                    String scoreB = "BH=" + Integer.toString(this.currentMatchState.getSetsB());
                    scores.add(scoreA);
                    scores.add(scoreB);
                    return new CheckMarketResultedOutcome(winningSelection, n, scores);
                } else
                    return null;

            case "FT:OU": // Match total games
                if (currentMatchState.isMatchCompleted()) {
                    int n = currentMatchState.getTotalGamesA() + currentMatchState.getTotalGamesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else if (this.matchAbandoned) {
                    /*
                     * results markets impossible eg. below current total games
                     */
                    int n = currentMatchState.getTotalGamesA() + currentMatchState.getTotalGamesB();
                    double line = Double.parseDouble(market.getLineId());
                    if (line < n)
                        return new CheckMarketResultedOutcome("Over", n);
                    else
                        return new CheckMarketResultedOutcome();
                    // winningSelection =
                    // MarketUtilityFunctions.getWinningSelectionOvunMarket(n, );
                } else
                    return null;

            case "FT:TBIM": // Tie break played in match
                if (currentMatchState.isTieBreakAlreadyPlayedInMatch()) {
                    return new CheckMarketResultedOutcome("Yes");
                } else if (currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome("No");
                else
                    return null;

            case "P:SPRD":
                int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
                int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
                if (currentStateSeqNo <= marketSeqNo) {
                    if (currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return null;
                }

                int n = currentMatchState.getGameScoreInSetN(marketSetNo - 1).A
                                - currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
                winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());

                List<String> scores = new ArrayList<String>();
                String scoreA = "AH=" + Integer.toString(currentMatchState.getGameScoreInSetN(marketSetNo - 1).A);
                String scoreB = "BH=" + Integer.toString(currentMatchState.getGameScoreInSetN(marketSetNo - 1).B);
                scores.add(scoreA);
                scores.add(scoreB);
                return new CheckMarketResultedOutcome(winningSelection, n, scores);

            case "SG:AA": // Tie break played in match
                parseSequenceId(market.getSequenceId());

                if ((marketGameNo + 2) == this.currentMatchState.getGameNo()
                                && (marketSetNo) == this.currentMatchState.getSetNo()) {
                    if (this.currentMatchState.getLastGamePlayedOutcome() == TeamId.A
                                    && this.previousMatchState.getLastGamePlayedOutcome() == TeamId.A)
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.getLastGamePlayedOutcome() == null
                                    || this.previousMatchState.getLastGamePlayedOutcome() == null) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return new CheckMarketResultedOutcome("No");
                } else if ((marketSetNo + 1) == this.currentMatchState.getSetNo()
                                && (marketGameNo + 1) == (currentMatchState.getGameScoreInSetN(marketSetNo - 1).A
                                                + currentMatchState.getGameScoreInSetN(marketSetNo - 1).B)) {
                    if (this.currentMatchState.getLastGamePlayedOutcome() == TeamId.A
                                    && this.previousMatchState.getLastGamePlayedOutcome() == TeamId.A)
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.getLastGamePlayedOutcome() == null
                                    || this.previousMatchState.getLastGamePlayedOutcome() == null) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return new CheckMarketResultedOutcome("No");
                } else if ((marketSetNo) < this.currentMatchState.getSetNo()
                                || this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "SG:BB": // Tie break played in match
                parseSequenceId(market.getSequenceId());

                if ((marketGameNo + 2) == this.currentMatchState.getGameNo()
                                && (marketSetNo) == this.currentMatchState.getSetNo()) {
                    if (this.currentMatchState.getLastGamePlayedOutcome() == TeamId.B
                                    && this.previousMatchState.getLastGamePlayedOutcome() == TeamId.B)
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.getLastGamePlayedOutcome() == null
                                    || this.previousMatchState.getLastGamePlayedOutcome() == null) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return new CheckMarketResultedOutcome("No");

                } else if ((marketSetNo + 1) == this.currentMatchState.getSetNo()
                                && (marketGameNo + 1) == (currentMatchState.getGameScoreInSetN(marketSetNo - 1).A
                                                + currentMatchState.getGameScoreInSetN(marketSetNo - 1).B)) {
                    if (this.currentMatchState.getLastGamePlayedOutcome() == TeamId.B
                                    && this.previousMatchState.getLastGamePlayedOutcome() == TeamId.B)
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.getLastGamePlayedOutcome() == null
                                    || this.previousMatchState.getLastGamePlayedOutcome() == null) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return new CheckMarketResultedOutcome("No");
                } else if ((marketSetNo) < this.currentMatchState.getSetNo()
                                || this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

                // case "FT:ALT:S:OU":
            case "FT:S:OU": // sets total
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getSetsA() + currentMatchState.getSetsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:A": // Match total games A
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalGamesA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:TB:OU": // Match total games A
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTieBreaksCounter();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:B": // Match total games B
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalGamesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "G:ML": // Winner of game
                return checkGameOrTieBreakWinnerResulted();

            case "G:PW": // Winner of point
                return checkPointMktResulted();

            case "FT:PW5G": // First player to win 5 games in set
                return checkFirstToFiveGamesResulted();

            case "P:TBML": // Current set tie break winner
                if (this.marketSetNo < currentMatchState.getSetNo())
                    return checkOnlyTieBreakWinnerResulted();
                else
                    return null;

            case "P:TBCS": // Tie break correct score
                return checkTieBreakCorrectScoreResulted();

            case "P:ML": // Set Money Line
                return checkSetWinnerOutcome();

            case "P:CS": // set correct score
                return checkSetCorrectScoreOutcome(market.getSelectionsProbs());

            case "P:CS2": // set correct score after 2
                return checkSetCorrectScoreAfterGameOutcome(2);
            case "P:CS4": // set correct score after 4
                return checkSetCorrectScoreAfterGameOutcome(4);
            case "P:CS6": // set correct score
                return checkSetCorrectScoreAfterGameOutcome(6);

            case "P:CSG": // set correct score
                return checkSetCorrectScoreGroupOutcome();

            case "P:CSCB": // set correct score
                return checkSetCorrectScoreCombinedOutcome();
            case "P:OUNG":
            case "S:OU": // currentset over under
                return checkSetOverUnderOutCome(market);
            case "S:OU3": // currentset over under
                return checkSetOverUnderOutCome3Way(market);
            case "S:OUNG": // currentset over under
                return checkSetOverUnderOutComeNextGen(market);
            case "S:OU:A": // currentset over under
                return checkSetOverUnderOutCome(market);
            case "S:OU:B": // currentset over under
                return checkSetOverUnderOutCome(market);

            case "NP:CSCB": // set correct score
                return checkNextSetCorrectScoreCombinedOutcome();

            case "G:DEUCE": // This game reaches deuce
                return checkGameReachesDeuceResulted();

            case "G:CS": // Current game correct score
                return checkSetGameOrTieBreakCorrectScoreResulted(market.getSelectionsProbs());
            case "FT:WM:A":
                if (currentMatchState.isMatchCompleted()) {
                    int total = currentMatchState.getTotalGamesA() - currentMatchState.getTotalGamesB();
                    if (total <= 0)
                        winningSelection = "Player A 0 or Negative";
                    else if (total > 11)
                        winningSelection = "Player A 12 or more";
                    else
                        winningSelection = "Player A " + total;
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:WM:B":
                if (currentMatchState.isMatchCompleted()) {
                    int total = currentMatchState.getTotalGamesB() - currentMatchState.getTotalGamesA();
                    if (total <= 0)
                        winningSelection = "Player B 0 or Negative";
                    else if (total > 11)
                        winningSelection = "Player B 12 or more";
                    else
                        winningSelection = "Player B" + total;
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BPTW": // Both Players to win a Set
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > 0 && currentMatchState.getSetsB() > 0)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

                // case "FT:ALT:S:OU:2": // Alternative set over under market 2.5
                // if (currentMatchState.isMatchCompleted()
                // || (currentMatchState.getSetsA() + currentMatchState.getSetsB()
                // >= 2)) {
                // if ((currentMatchState.getSetsA() + currentMatchState.getSetsB()
                // > 2.5)
                // || (currentMatchState.getSetsA() == 1 &&
                // currentMatchState.getSetsB() == 1))
                // return new CheckMarketResultedOutcome("Yes");
                // else
                // return new CheckMarketResultedOutcome("No");
                // } else
                // return null;
                // case "FT:ALT:S:OU:3": // Alternative set over under market 3.5
                // if (currentMatchState.isMatchCompleted()
                // || (currentMatchState.getSetsA() + currentMatchState.getSetsB()
                // >= 3)) {
                // if ((currentMatchState.getSetsA() + currentMatchState.getSetsB()
                // > 3.5))
                // return new CheckMarketResultedOutcome("Yes");
                // else
                // return new CheckMarketResultedOutcome("No");
                // } else
                // return null;
            case "FT:ALT:S:OU": // Alternative set over under market 4.5
                double tempLine = Double.parseDouble(market.getLineId());
                if (currentMatchState.isMatchCompleted()
                                || (currentMatchState.getSetsA() + currentMatchState.getSetsB() >= tempLine)) {
                    if ((currentMatchState.getSetsA() + currentMatchState.getSetsB() > tempLine))
                        return new CheckMarketResultedOutcome("Over");
                    else
                        return new CheckMarketResultedOutcome("Under");
                } else
                    return null;

        }
        return null;
    }

    private CheckMarketResultedOutcome checkSetOverUnderOutCome(Market market) {

        int marketSeqNo = convertSeqNoToIndex(marketSetNo, 0, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo < marketSeqNo)
            return null;
        if (currentStateSeqNo == marketSeqNo) {
            int currentGameScoreA = currentMatchState.getGamesA();
            int currentGameScoreB = currentMatchState.getGamesB();
            int n = currentGameScoreA + currentGameScoreB;
            if (market.getType().substring(market.getType().length() - 1).equals("A")) {
                n = currentGameScoreA;
            } else if (market.getType().substring(market.getType().length() - 1).equals("B")) {
                n = currentGameScoreB;
            }
            if (n > Double.valueOf(market.getLineId())) {
                String winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                if (currentMatchState.isInTieBreak()) {
                    n = 13;
                }
                return new CheckMarketResultedOutcome(winningSelection, n);
            } else if (currentMatchState.isMatchCompleted()) {
                String winningSelection = "Void";
                return new CheckMarketResultedOutcome(winningSelection, n);
            } else if (n == 12 && market.getLineId().equals("12.5")) {
                String winningSelection = "Over";
                return new CheckMarketResultedOutcome(winningSelection, 13);
            } else
                return null;
        }

        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
        int n = gameScoreA + gameScoreB;
        if (market.getType().substring(market.getType().length() - 1).equals("A")) {
            n = gameScoreA;
        } else if (market.getType().substring(market.getType().length() - 1).equals("B")) {
            n = gameScoreB;
        }

        String winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
        return new CheckMarketResultedOutcome(winningSelection, n);

    }

    private CheckMarketResultedOutcome checkSetOverUnderOutCome3Way(Market market) {

        int marketSeqNo = convertSeqNoToIndex(marketSetNo, 0, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo)
            if (currentMatchState.isMatchCompleted())
                return new CheckMarketResultedOutcome(); // when void happens
            else
                return null;
        else {
            int setNo = marketSetNo - 1;
            int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
            int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
            int n = gameScoreA + gameScoreB;
            String winningSelection;
            if (n < 9) {
                winningSelection = "Under 8.5";
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (n > 10) {
                winningSelection = "Over 10.5";
                return new CheckMarketResultedOutcome(winningSelection);
            } else {
                winningSelection = "9 or 10";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }

    }

    private CheckMarketResultedOutcome checkSetOverUnderOutComeNextGen(Market market) {

        int marketSeqNo = convertSeqNoToIndex(marketSetNo, 0, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);

        List<String> partialloseSelections = new ArrayList<String>();
        List<String> partialvoidedSelections = new ArrayList<String>();
        List<String> winSelections = new ArrayList<String>();
        if (currentStateSeqNo < marketSeqNo)
            return null;
        else if (currentStateSeqNo == marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else {
                int gameScoreA = currentMatchState.getGamesA();
                int gameScoreB = currentMatchState.getGamesB();
                if (market.getSelections().containsKey("Over 4.5"))
                    if (gameScoreA >= 1 && gameScoreB >= 1)
                        partialloseSelections.add("Under 4.5");
                if (market.getSelections().containsKey("Over 5.5"))
                    if (gameScoreA >= 2 && gameScoreB >= 2)
                        partialloseSelections.add("Under 5.5");
                if (market.getSelections().containsKey("Over 6.5"))
                    if (gameScoreA >= 3 && gameScoreB >= 3) {
                        winSelections.add("Over 4.5");
                        winSelections.add("Over 5.5");
                        winSelections.add("Over 6.5");
                        return new CheckMarketResultedOutcome(winSelections);
                    }
                if (!partialloseSelections.isEmpty())
                    return new CheckMarketResultedOutcome(partialloseSelections, partialvoidedSelections, false);
                else
                    return null;
            }
        } else {
            int setNo = marketSetNo - 1;
            int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
            int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
            if (gameScoreA + gameScoreB > 4.5)
                winSelections.add("Over 4.5");
            else
                winSelections.add("Under 4.5");
            if (gameScoreA + gameScoreB > 5.5)
                winSelections.add("Over 5.5");
            else
                winSelections.add("Under 5.5");
            if (gameScoreA + gameScoreB > 6.5)
                winSelections.add("Over 6.5");
            else
                winSelections.add("Under 6.5");
            if (!winSelections.isEmpty())
                return new CheckMarketResultedOutcome(winSelections);
            else
                return new CheckMarketResultedOutcome();
        }
    }

    private CheckMarketResultedOutcome checkSetCorrectScoreOutcome(Map<String, Double> openedSelections) {
        String winningSelection;
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (matchAbandoned)
            return new CheckMarketResultedOutcome();
        if (currentStateSeqNo < marketSeqNo)
            return null;
        else if (currentStateSeqNo == marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else if (partialResulting && (marketGameNo < currentMatchState.getGameNo() - 1)) {
                /*
                 * Check some of the selections
                 */
                List<String> partialvoidedSelections = new ArrayList<String>();
                ArrayList<String> partialloseSelections = resultSelectionsForCS(currentMatchState.getGamesA(),
                                currentMatchState.getGamesB(), openedSelections);
                if (partialloseSelections != null && partialloseSelections.size() > 0)
                    return new CheckMarketResultedOutcome(partialloseSelections, partialvoidedSelections, false);
                else
                    return null;
            } else
                return null;
        }

        int setNo = marketSetNo - 1;

        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;

        if (gameScoreA > gameScoreB)
            winningSelection = String.format("Player A %d-%d", gameScoreA, gameScoreB);
        else
            winningSelection = String.format("Player B %d-%d", gameScoreB, gameScoreA);
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private ArrayList<String> resultSelectionsForCS(int pointsA, int pointsB, Map<String, Double> openedSelections) {
        ArrayList<String> selectionsResulted = new ArrayList<String>();
        String[] selectionScores = null;
        for (String selection : openedSelections.keySet()) {
            if (selection.toUpperCase().contains("PLAYER")) {

                selectionScores = selection.split("-");
                String[] teamScore = (selectionScores[0].split(" "));
                int selectionA = 999;
                int selectionB = 999;
                if (teamScore.length == 6) {
                    int teamBEndScore = 999;
                    int teamAEndScore = 999;
                    if (teamScore[1].equals("A")) {
                        if (teamScore[5].equals("Love")) {
                            teamBEndScore = 0;
                        } else {
                            teamBEndScore = Integer.valueOf(teamScore[5]);
                            switch (teamBEndScore) {
                                case 40:
                                    teamBEndScore = 3;
                                    break;
                                case 30:
                                    teamBEndScore = 2;
                                    break;
                                case 15:
                                    teamBEndScore = 1;
                                    break;
                            }
                        }
                    } else if (teamScore[1].equals("B")) {
                        if (teamScore[5].equals("Love")) {
                            teamAEndScore = 0;
                        } else {
                            teamAEndScore = Integer.valueOf(teamScore[5]);
                            switch (teamAEndScore) {
                                case 40:
                                    teamAEndScore = 3;
                                    break;
                                case 30:
                                    teamAEndScore = 2;
                                    break;
                                case 15:
                                    teamAEndScore = 1;
                                    break;
                            }
                        }
                    }
                    if (teamAEndScore < pointsA || teamBEndScore < pointsB) {
                        if (!selectionsResulted.contains(selection)) {
                            selectionsResulted.add(selection);
                        }
                    }
                } else {

                    if (teamScore[1].equals("A")) {
                        selectionA = Integer.valueOf(teamScore[2]);
                        selectionB = Integer.valueOf(selectionScores[1]);
                    } else if (teamScore[1].equals("B")) {
                        selectionB = Integer.valueOf(teamScore[2]);
                        selectionA = Integer.valueOf(selectionScores[1]);
                    }
                    if (selectionA < pointsA || selectionB < pointsB)
                        selectionsResulted.add(selection);
                }

            } else {

                selectionScores = selection.split("-");
                String[] teamScore = (selectionScores[0].split(" "));
                int selectionA = 999;
                int selectionB = 999;
                if (teamScore[0].equals("A")) {
                    selectionA = Integer.valueOf(teamScore[1]);
                    selectionB = Integer.valueOf(selectionScores[1]);
                } else if (teamScore[0].equals("B")) {
                    selectionB = Integer.valueOf(teamScore[1]);
                    selectionA = Integer.valueOf(selectionScores[1]);
                }
                if (selectionA < pointsA || selectionB < pointsB)
                    selectionsResulted.add(selection);
            }

        }
        return selectionsResulted;
    }

    private CheckMarketResultedOutcome checkSetCorrectScoreAfterGameOutcome(int game) {
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo) {
            if (currentStateSeqNo < marketSeqNo) {
                return null;
            } else if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome();
            }
            if (currentMatchState.getGameNo() == (game + 1)) {
                return new CheckMarketResultedOutcome(
                                currentMatchState.getGamesA() + "-" + currentMatchState.getGamesB());
            } else
                return null;
        }
        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
        if (currentMatchState.getGameNo() == 1) {

            if (gameScoreA == 6 && gameScoreB == 0) {
                return new CheckMarketResultedOutcome("6-0");
            } else if (gameScoreA == 0 && gameScoreB == 6) {
                return new CheckMarketResultedOutcome("0-6");
            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    private CheckMarketResultedOutcome checkSetCorrectScoreGroupOutcome() {
        String winningSelection;
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome();
            } else
                return null;
        }

        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
        if (gameScoreA + gameScoreB < 8)
            winningSelection = "6-0 or 6-1";
        else if (gameScoreA + gameScoreB < 10)
            winningSelection = "6-2 or 6-3";
        else if (gameScoreA + gameScoreB < 14)
            winningSelection = "6-4, 7-5 or 7-6";
        else
            winningSelection = "Any other result";
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkSetCorrectScoreCombinedOutcome() {
        String winningSelection;
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;

        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;

        int gameScoreLeft = 0;
        int gameScoreRight = 0;

        if (gameScoreA <= gameScoreB) {
            gameScoreLeft = gameScoreA;
            gameScoreRight = gameScoreB;
        } else {
            gameScoreLeft = gameScoreB;
            gameScoreRight = gameScoreA;
        }

        winningSelection = String.format("%d-%d", gameScoreLeft, gameScoreRight);
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkNextSetCorrectScoreCombinedOutcome() {
        String winningSelection;
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo + 1, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;

        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;

        int gameScoreLeft = 0;
        int gameScoreRight = 0;

        if (gameScoreA <= gameScoreB) {
            gameScoreLeft = gameScoreA;
            gameScoreRight = gameScoreB;
        } else {
            gameScoreLeft = gameScoreB;
            gameScoreRight = gameScoreA;
        }

        winningSelection = String.format("%d-%d", gameScoreLeft, gameScoreRight);
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkSetWinnerOutcome() {
        String winningSelection = "";

        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome();
            } else
                return null;
        }
        int setNo = marketSetNo - 1;
        if ((this.previousMatchState.isInFinalSet() && this.previousMatchState.inTieBreak)
                        || this.previousMatchState.isInSuperTieBreak()) {

            int pointsA = previousMatchState.getPointsA();
            int pointsB = previousMatchState.getPointsB();

            if (pointsA > pointsB)
                winningSelection = "A";
            else
                winningSelection = "B";

            return new CheckMarketResultedOutcome(winningSelection);

        } else {
            int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
            int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;

            if (gameScoreA > gameScoreB)
                winningSelection = "A";
            else
                winningSelection = "B";

            return new CheckMarketResultedOutcome(winningSelection);

        }
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

    private CheckMarketResultedOutcome checkTieBreakCorrectScoreResulted() {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo() - 1,
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the game was played
         */
        int pointsA = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsA();
        int pointsB = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsB();
        String winningSelection;
        if (pointsA <= 4 && pointsB <= 4)
            return new CheckMarketResultedOutcome();
        if (pointsA > pointsB && pointsB < 6)
            winningSelection = String.format("Player A %d-%d", pointsA, pointsB);
        else
            winningSelection = "Any other score";
        if (pointsA < pointsB && pointsA < 6)
            winningSelection = String.format("Player B %d-%d", pointsB, pointsA);
        else
            winningSelection = "Any other score";
        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkSetGameOrTieBreakCorrectScoreResulted(
                    Map<String, Double> openedSelections) {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo < marketSeqNo) {
            return null;
        } else if (currentStateSeqNo <= marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else if (partialResulting && (marketPointNo < currentMatchState.getPointNo() - 1)) {
                /*
                 * Check some of the selections
                 */
                if (currentMatchState.getPointNo() < 7) {
                    ArrayList<String> partialloseSelections = resultSelectionsForCS(currentMatchState.getPointsA(),
                                    currentMatchState.getPointsB(), openedSelections);
                    List<String> partialvoidedSelections = new ArrayList<String>();
                    if (partialloseSelections != null && partialloseSelections.size() > 0)
                        return new CheckMarketResultedOutcome(partialloseSelections, partialvoidedSelections, false);
                    else
                        return null;
                } else {
                    return null;
                }
            } else
                return null;
        }

        int previousStateSeqNo = convertSeqNoToIndex(((TennisMatchState) previousMatchState).getSetNo(),
                        ((TennisMatchState) previousMatchState).getGameNo(), 0);
        if (previousStateSeqNo == marketSeqNo) {
            int pointsA = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsA();
            int pointsB = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsB();
            String winningSelection = "";
            if (pointsA == 4 && pointsB == 0) {
                winningSelection = setXgameYCorrectScoreList[0];
            }
            if (pointsA == 4 && pointsB == 1) {
                winningSelection = setXgameYCorrectScoreList[1];
            }
            if (pointsA == 4 && pointsB == 2) {
                winningSelection = setXgameYCorrectScoreList[2];
            }
            if (pointsA == 5 && pointsB == 3) {
                winningSelection = setXgameYCorrectScoreList[3];
            }

            if (pointsA == 0 && pointsB == 4) {
                winningSelection = setXgameYCorrectScoreList[4];
            }
            if (pointsA == 1 && pointsB == 4) {
                winningSelection = setXgameYCorrectScoreList[5];
            }
            if (pointsA == 2 && pointsB == 4) {
                winningSelection = setXgameYCorrectScoreList[6];
            }
            if (pointsA == 3 && pointsB == 5) {
                winningSelection = setXgameYCorrectScoreList[7];
            }

            if (((TennisMatchState) currentMatchState).isNoAdvantageGameFormat()) {
                if (pointsA == 4 && pointsB == 3) {
                    winningSelection = setXgameYCorrectScoreList[3];
                }
                if (pointsA == 3 && pointsB == 4) {
                    winningSelection = setXgameYCorrectScoreList[7];
                }
            }
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
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(),
                        ((TennisMatchState) currentMatchState).getPointNo());
        if (currentStateSeqNo < marketSeqNo)
            return null; // market not yet resulted
        CheckMarketResultedOutcome outcome;
        if (currentStateSeqNo == marketSeqNo)
            /*
             * if current state point no = 7 then score must be 40-40
             */
            outcome = new CheckMarketResultedOutcome("Yes");
        else { // currentStateSeqNo > marketSeqNo
            int previousStateSeqNo = convertSeqNoToIndex(((TennisMatchState) previousMatchState).getSetNo(),
                            ((TennisMatchState) previousMatchState).getGameNo(), 7);
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
        int currentSetNo = ((TennisMatchState) currentMatchState).getSetNo();
        if (currentSetNo != marketSetNo)
            /*
             * not in correct set for this market
             */
            return null;
        int gamesA = ((TennisMatchState) currentMatchState).getGamesA();
        int gamesB = ((TennisMatchState) currentMatchState).getGamesB();
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
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(),
                        ((TennisMatchState) currentMatchState).getPointNo());
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the point was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisMatchState) previousMatchState).getSetNo(),
                        ((TennisMatchState) previousMatchState).getGameNo(),
                        ((TennisMatchState) previousMatchState).getPointNo());
        if (previousStateSeqNo == marketSeqNo) {
            String winningSelection;
            if (((TennisMatchState) currentMatchState).getLastPointPlayedOutcome() == TeamId.A)
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

    private CheckMarketResultedOutcome checkOnlyTieBreakWinnerResulted() {
        /*
         * check to see if we have gone beyond the game in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the game was played
         */
        String winningSelection;
        if (previousMatchState.isInTieBreak() && !(currentMatchState.isInTieBreak())) {
            if (((TennisMatchState) currentMatchState).getLastGamePlayedOutcome() == TeamId.A)
                winningSelection = "A";
            else
                winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        } else {
            return new CheckMarketResultedOutcome();
        }
    }

    private CheckMarketResultedOutcome checkGameOrTieBreakWinnerResulted() {
        /*
         * check to see if we have gone beyond the game in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo)
            return null;
        /*
         * check to see whether the game was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisMatchState) previousMatchState).getSetNo(),
                        ((TennisMatchState) previousMatchState).getGameNo(), 0);
        if (previousStateSeqNo == marketSeqNo) {
            String winningSelection;
            if (((TennisMatchState) currentMatchState).getLastGamePlayedOutcome() == TeamId.A)
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
