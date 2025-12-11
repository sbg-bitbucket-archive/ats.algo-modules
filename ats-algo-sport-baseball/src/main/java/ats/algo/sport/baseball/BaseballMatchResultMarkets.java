package ats.algo.sport.baseball;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.sport.baseball.BaseballMatchState;

public class BaseballMatchResultMarkets extends MatchResultMarkets {
    BaseballMatchState previousMatchState;
    BaseballMatchState currentMatchState;
    int marketInningNo;
    private String[] winningMarginList = {"-4+", "-3", "-2", "-1", "1", "2", "3", "4+"};

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (BaseballMatchState) previousMatchState;
        this.currentMatchState = (BaseballMatchState) currentMatchState;
        int currentInning = ((BaseballMatchState) currentMatchState).getInning() + 1;
        int[][] runsInInning = ((BaseballMatchState) currentMatchState).getRunsInInningsN();
        int runsA = this.currentMatchState.getRunsA();
        int runsB = this.currentMatchState.getRunsB();
        int currentInningRunsA = 0;
        int currentInningRunsB = 0;
        parseSequenceId(market.getSequenceId());
        if (marketInningNo == 1) {
            currentInningRunsA = runsInInning[0][0];
            currentInningRunsB = runsInInning[1][0];
        } else {
            currentInningRunsA = runsInInning[0][marketInningNo - 1] - runsInInning[0][marketInningNo - 2];
            currentInningRunsB = runsInInning[1][marketInningNo - 1] - runsInInning[1][marketInningNo - 2];
        }

        String winningSelection = null;
        int runsTotal;
        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:AXB":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else if (this.currentMatchState.getRunsA() < this.currentMatchState.getRunsB())
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:A:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    runsTotal = this.currentMatchState.getRunsA();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:B:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    runsTotal = this.currentMatchState.getRunsB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    runsTotal = this.currentMatchState.getRunsB() + this.currentMatchState.getRunsA();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    runsTotal = this.currentMatchState.getRunsA() - this.currentMatchState.getRunsB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:OU3":
                if (currentInning > 3) {
                    runsTotal = runsInInning[0][2] + runsInInning[1][2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:OU5":
                if (currentInning > 5) {
                    runsTotal = runsInInning[0][4] + runsInInning[1][4];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:OU7":
                if (currentInning > 7) {
                    runsTotal = runsInInning[0][6] + runsInInning[1][6];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:A:OU3":
                if (currentInning > 3) {
                    runsTotal = runsInInning[0][2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:A:OU5":
                if (currentInning > 5) {
                    runsTotal = runsInInning[0][4];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:A:OU7":
                if (currentInning > 7) {
                    runsTotal = runsInInning[0][6];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:B:OU3":
                if (currentInning > 3) {
                    runsTotal = runsInInning[1][2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:B:OU5":
                if (currentInning > 5) {
                    runsTotal = runsInInning[1][4];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "PB::OU7":
                if (currentInning > 7) {
                    runsTotal = runsInInning[1][6];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:AXB3":
                if (currentInning > 3) {
                    if (runsInInning[0][2] > runsInInning[1][2])
                        winningSelection = "A";
                    else if (runsInInning[0][2] < runsInInning[1][2])
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:AXB5":
                if (currentInning > 5) {
                    if (runsInInning[0][4] > runsInInning[1][4])
                        winningSelection = "A";
                    else if (runsInInning[0][4] < runsInInning[1][4])
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:AXB7":
                if (currentInning > 7) {
                    if (runsInInning[0][6] > runsInInning[1][6])
                        winningSelection = "A";
                    else if (runsInInning[0][6] < runsInInning[1][6])
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:SPRD3":
                if (currentInning > 3) {
                    runsTotal = runsInInning[0][2] - runsInInning[1][2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:SPRD5":
                if (currentInning > 5) {
                    runsTotal = runsInInning[0][4] - runsInInning[1][4];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:SPRD7":
                if (currentInning > 7) {
                    runsTotal = runsInInning[0][6] - runsInInning[1][6];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:OT":
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (runsInInning[0][8] == runsInInning[1][8]) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

                else
                    return null;
            case "FT:WM":
                int winingMargingIndicator;
                if (this.currentMatchState.isMatchCompleted()) {

                    if (runsA > runsB) {
                        winingMargingIndicator = (runsA <= (runsB + 3)) ? runsA - runsB + 3 : 7;

                    } else if (runsA < runsB) {
                        winingMargingIndicator = (runsA <= (runsB - 4)) ? 0 : 4 - runsB + runsA;
                    } else {
                        winingMargingIndicator = -1;
                    }
                    winningSelection = winningMarginList[winingMargingIndicator];
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:AXB":
                if (currentInning > marketInningNo) {
                    if (currentInningRunsA > currentInningRunsB)
                        winningSelection = "A";
                    else if (currentInningRunsA < currentInningRunsB)
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "P:OU":
                if (currentInning > marketInningNo) {
                    runsTotal = currentInningRunsA + currentInningRunsB;
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "P:SPRD":
                if (currentInning > marketInningNo) {
                    runsTotal = currentInningRunsA - currentInningRunsB;
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            default:
                throw new IllegalArgumentException(
                                "Market type missing from BasetballMatchResultMarkets: " + market.getType());

        }
    }

    private void parseSequenceId(String sequenceId) {
        marketInningNo = 1;
        String[] bits = sequenceId.split("\\.");
        // System.out.println(bits[0]);
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, bits[0].length());
            marketInningNo = Integer.parseInt(setNo);
        }
    }

}
