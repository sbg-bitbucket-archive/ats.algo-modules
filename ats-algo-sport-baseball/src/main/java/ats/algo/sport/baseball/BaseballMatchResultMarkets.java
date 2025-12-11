package ats.algo.sport.baseball;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.sport.baseball.BaseballMatchState;

public class BaseballMatchResultMarkets extends MatchResultMarkets {
    BaseballMatchState previousMatchState;
    BaseballMatchState currentMatchState;
    int marketInningNo;
    private String[] winningMarginList = {"-4+", "-3", "-2", "-1", "1", "2", "3", "4+"};
    private String[] doubleResultList = {"AA", "AB", "BA", "BB", "XA", "XB"};

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
            case "FT:HSH":
                if (this.currentMatchState.isMatchCompleted()) {
                    int normalTimeRunsA = runsInInning[0][8];
                    int normalTimeRunsB = runsInInning[1][8];
                    int firstHalfRunsA = ((BaseballMatchState) currentMatchState).getFirstHalfRunsA();
                    int firstHalfRunsB = ((BaseballMatchState) currentMatchState).getFirstHalfRunsB();
                    int secondHalfRunsA = normalTimeRunsA - firstHalfRunsA;
                    int secondHalfRunsB = normalTimeRunsB - firstHalfRunsB;

                    if (firstHalfRunsA + firstHalfRunsB > secondHalfRunsA + secondHalfRunsB)
                        winningSelection = "H1";
                    else if (firstHalfRunsA + firstHalfRunsB < secondHalfRunsA + secondHalfRunsB)
                        winningSelection = "H2";
                    else
                        winningSelection = "X";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:T:HSH":
                if (this.currentMatchState.isMatchCompleted()) {
                    int normalTimeRunsA = runsInInning[0][8];
                    int normalTimeRunsB = runsInInning[1][8];
                    int firstHalfRunsA = ((BaseballMatchState) currentMatchState).getFirstHalfRunsA();
                    int firstHalfRunsB = ((BaseballMatchState) currentMatchState).getFirstHalfRunsB();
                    int secondHalfRunsA = normalTimeRunsA - firstHalfRunsA;
                    int secondHalfRunsB = normalTimeRunsB - firstHalfRunsB;

                    if ((firstHalfRunsA > secondHalfRunsA ? firstHalfRunsA
                                    : secondHalfRunsA) > (firstHalfRunsB > secondHalfRunsB ? firstHalfRunsB
                                                    : secondHalfRunsB))
                        winningSelection = "A";
                    else if ((firstHalfRunsA > secondHalfRunsA ? firstHalfRunsA
                                    : secondHalfRunsA) < (firstHalfRunsB > secondHalfRunsB ? firstHalfRunsB
                                                    : secondHalfRunsB))
                        winningSelection = "B";
                    else
                        winningSelection = "X";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:DNB":
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else if (this.currentMatchState.getRunsA() < this.currentMatchState.getRunsB())
                        winningSelection = "B";
                    else
                        return new CheckMarketResultedOutcome();

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:LEAD":
                int marketId = Integer.valueOf(market.getSequenceId().substring(1));
                if (this.currentMatchState.getInning() > marketId) {
                    int[][] runsInInningsN = this.currentMatchState.getRunsInInningsN();
                    int runsAtemp = runsInInningsN[0][marketId - 1];
                    int runsBtemp = runsInInningsN[1][marketId - 1];
                    if (runsAtemp == runsBtemp)
                        return new CheckMarketResultedOutcome();
                    else if (runsAtemp < runsBtemp)
                        winningSelection = "B";
                    else
                        winningSelection = "A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    int[][] runsInInningsN = this.currentMatchState.getRunsInInningsN();
                    int runsAtemp = runsInInningsN[0][8];
                    int runsBtemp = runsInInningsN[1][8];
                    if (runsAtemp == runsBtemp)
                        return new CheckMarketResultedOutcome();
                    else if (runsAtemp < runsBtemp)
                        winningSelection = "B";
                    else
                        winningSelection = "A";
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

            case "FT:AXB$OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    boolean over = runsA + runsB > 10.5;
                    if (runsA > runsB) {
                        if (over)
                            winningSelection = "AOver";
                        else
                            winningSelection = "AUnder";
                    } else if (runsA < runsB) {
                        if (over)
                            winningSelection = "BOver";
                        else
                            winningSelection = "BUnder";
                    } else {
                        if (over)
                            winningSelection = "XOver";
                        else
                            winningSelection = "XUnder";
                    }
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
            case "FT:R$1":
                int currentRuns = this.currentMatchState.getRunsA() + this.currentMatchState.getRunsB();
                if (this.currentMatchState.getInning() > 0 || currentRuns > 0) {
                    if (currentRuns == 0)
                        return new CheckMarketResultedOutcome("No");
                    else
                        return new CheckMarketResultedOutcome("Yes");
                }
                return null;


            case "FT:TR1":
                if (this.currentMatchState.getRunsA() == 1 && this.currentMatchState.getRunsB() == 0) {
                    return new CheckMarketResultedOutcome("No");
                } else if (this.currentMatchState.getRunsA() == 0 && this.currentMatchState.getRunsB() == 1)
                    return new CheckMarketResultedOutcome("Yes");
                else
                    return null;

            case "FT:HTFT":
                if (this.currentMatchState.isMatchCompleted()) {
                    int halfTimeRunsA = runsInInning[0][4];
                    int halfTimeRunsB = runsInInning[1][4];
                    int fullTimeRunsA = this.currentMatchState.getRunsA();
                    int fullTimeRunsB = this.currentMatchState.getRunsB();

                    if (halfTimeRunsA > halfTimeRunsB && fullTimeRunsA > fullTimeRunsB) { // aa
                        winningSelection = doubleResultList[0];

                    } else if (halfTimeRunsA > halfTimeRunsB && fullTimeRunsA < fullTimeRunsB) {
                        winningSelection = doubleResultList[1];
                    } else if (halfTimeRunsA < halfTimeRunsB && fullTimeRunsA > fullTimeRunsB) { // BA
                        winningSelection = doubleResultList[2];
                    } else if (halfTimeRunsA < halfTimeRunsB && fullTimeRunsA < fullTimeRunsB) { // BB
                        winningSelection = doubleResultList[3];
                    } else if (halfTimeRunsA == halfTimeRunsB && fullTimeRunsA > fullTimeRunsB) { // XA
                        winningSelection = doubleResultList[4];
                    } else if (halfTimeRunsA == halfTimeRunsB && fullTimeRunsA < fullTimeRunsB) // XB
                        winningSelection = doubleResultList[5];
                    else {
                        return new CheckMarketResultedOutcome();
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:LTTS":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getTeamScoreLast() == TeamId.A)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:R1W":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getTeamScoreFirst() == (this.currentMatchState
                                    .getRunsA() > this.currentMatchState.getRunsB() ? TeamId.A : TeamId.B))
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:FTTS":
                if (this.currentMatchState.getRunsA() == 1 && this.currentMatchState.getRunsB() == 0) {
                    return new CheckMarketResultedOutcome("A");
                } else if (this.currentMatchState.getRunsA() == 0 && this.currentMatchState.getRunsB() == 1)
                    return new CheckMarketResultedOutcome("B");
                else
                    return null;

            case "FT:BR1":
                if (this.currentMatchState.getRunsA() == 1 && this.currentMatchState.getRunsB() == 0) {
                    return new CheckMarketResultedOutcome("Yes");
                } else if (this.currentMatchState.getRunsA() == 0 && this.currentMatchState.getRunsB() == 1)
                    return new CheckMarketResultedOutcome("No");
                else
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
            case "P:B:OU7":
                if (currentInning > 7) {
                    runsTotal = runsInInning[1][6];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:AXB:1":
            case "P:AXB:2":
            case "P:AXB:3":
            case "P:AXB:4":
            case "P:AXB:5":
            case "P:AXB:6":
            case "P:AXB:7":
            case "P:AXB:8":
            case "P:AXB:9":
                parseMarketId(market.getType());
                if (currentInning > marketInningNo) {
                    if (runsInInning[0][marketInningNo - 1] > runsInInning[1][marketInningNo - 1])
                        winningSelection = "A";
                    else if (runsInInning[0][marketInningNo - 1] < runsInInning[1][marketInningNo - 1])
                        winningSelection = "B";
                    else
                        winningSelection = "D";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:RTR7":
                if (this.currentMatchState.getRunsA() >= 7 && this.currentMatchState.getRunsB() < 7) {
                    return new CheckMarketResultedOutcome("A");
                }
                if (this.currentMatchState.getRunsB() >= 7 && this.currentMatchState.getRunsA() < 7) {
                    return new CheckMarketResultedOutcome("B");
                }
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome("X");
                }
                return null;
            case "FT:RTR3":
                if (this.currentMatchState.getRunsA() >= 3 && this.currentMatchState.getRunsB() < 3) {
                    return new CheckMarketResultedOutcome("A");
                }
                if (this.currentMatchState.getRunsB() >= 3 && this.currentMatchState.getRunsA() < 3) {
                    return new CheckMarketResultedOutcome("B");
                }
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome("X");
                }
                return null;
            case "FT:RTR5":
                if (this.currentMatchState.getRunsA() >= 5 && this.currentMatchState.getRunsB() < 5) {
                    return new CheckMarketResultedOutcome("A");
                }
                if (this.currentMatchState.getRunsB() >= 5 && this.currentMatchState.getRunsA() < 5) {
                    return new CheckMarketResultedOutcome("B");
                }
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome("X");
                }
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

    private void parseMarketId(String marketType) {
        marketInningNo = Integer.parseInt(marketType.substring(marketType.length() - 1, marketType.length()));
    }

}
