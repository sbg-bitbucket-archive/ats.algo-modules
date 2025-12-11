package ats.algo.sport.cricket;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.sport.cricket.CricketMatchIncidentResult.CricketMatchIncidentResultType;

public class CricketMatchResultMarkets extends MatchResultMarkets {
    CricketMatchState previousMatchState;
    CricketMatchState currentMatchState;
    int marketSetNo;
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
        this.previousMatchState = (CricketMatchState) previousMatchState;
        this.currentMatchState = (CricketMatchState) currentMatchState;
        parseSequenceId(market.getSequenceId());
        int currentOver;
        int currentWicket;
        int currentBall;
        int runsTotal;
        boolean serveA = TeamId.A == this.currentMatchState.getBat();
        if (serveA) {
            currentOver = this.currentMatchState.getOversA();
            currentWicket = this.currentMatchState.getWicketsA() + 1;
            currentBall = this.currentMatchState.getBall() + 1;
        } else {
            currentOver = this.currentMatchState.getOversB();
            currentWicket = this.currentMatchState.getWicketsB() + 1;
            currentBall = this.currentMatchState.getBall() + 1;
        }

        String winningSelection = null;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:AXB":
                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else if (this.currentMatchState.getRunsA() < this.currentMatchState.getRunsB())
                        winningSelection = "B";
                    else
                        winningSelection = "Draw";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:A:OU":
                if (this.currentMatchState.getBallsA() > 0 && this.currentMatchState.getBallsB() >= 0 && !serveA) {
                    runsTotal = this.currentMatchState.getRunsA();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRunsA();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FT:B:OU":
                if (this.currentMatchState.getBallsA() >= 0 && this.currentMatchState.getBallsB() > 0 && serveA) {
                    runsTotal = this.currentMatchState.getRunsB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRunsB();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:A:OU4":
                if (this.currentMatchState.getBallsA() > 0 && this.currentMatchState.getBallsB() >= 0 && !serveA) {
                    runsTotal = this.currentMatchState.getRuns4A();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRuns4A();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FT:B:OU4":
                if (this.currentMatchState.getBallsA() >= 0 && this.currentMatchState.getBallsB() > 0 && serveA) {
                    runsTotal = this.currentMatchState.getRuns4B();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRuns4B();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "FT:A:OU6":
                if (this.currentMatchState.getBallsA() > 0 && this.currentMatchState.getBallsB() >= 0 && !serveA) {
                    runsTotal = this.currentMatchState.getRuns4A();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRuns4A();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FT:B:OU6":
                if (this.currentMatchState.getBallsA() >= 0 && this.currentMatchState.getBallsB() > 0 && serveA) {
                    runsTotal = this.currentMatchState.getRuns6B();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (this.currentMatchState.isMatchFinished()) {
                    runsTotal = this.currentMatchState.getRuns6B();

                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else
                    return null;
            case "P:A:OU":
                if (serveA && currentOver > marketSetNo) {
                    if (marketSetNo == 1) {
                        runsTotal = this.currentMatchState.getRunsInOverNA()[0];
                    } else
                        runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1]
                                        - this.currentMatchState.getRunsInOverNA()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getRunsA() > 0 && marketSetNo == 20) {
                    runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1]
                                    - this.currentMatchState.getRunsInOverNA()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getWicketsA() == 10
                                && marketSetNo == this.currentMatchState.getOversA()) {
                    runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1]
                                    - this.currentMatchState.getRunsInOverNA()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getWicketsA() == 10
                                && marketSetNo == this.currentMatchState.getOversA() + 1)
                    return new CheckMarketResultedOutcome();
                else if (this.currentMatchState.isMatchFinished())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "P:A:OU6":
            case "P:A:OU10":
            case "P:A:OU15":
            case "P:A:OU25":
                if (serveA && currentOver > marketSetNo) {
                    runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getRunsA() > 0 && marketSetNo == 20) {
                    runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getWicketsA() == 10
                                && marketSetNo == this.currentMatchState.getOversA()) {
                    runsTotal = this.currentMatchState.getRunsInOverNA()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (!serveA && this.currentMatchState.getWicketsA() == 10
                                && marketSetNo == this.currentMatchState.getOversA() + 1)
                    return new CheckMarketResultedOutcome();
                else if (this.currentMatchState.isMatchFinished())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "P:B:OU":
                if (!serveA && currentOver > marketSetNo) {
                    if (marketSetNo == 1)
                        runsTotal = this.currentMatchState.getRunsInOverNB()[0];
                    else
                        runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1]
                                        - this.currentMatchState.getRunsInOverNB()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getRunsB() > 0 && marketSetNo == 20) {
                    runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1]
                                    - this.currentMatchState.getRunsInOverNB()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getWicketsB() == 10
                                && marketSetNo == this.currentMatchState.getOversB()) {
                    runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1]
                                    - this.currentMatchState.getRunsInOverNB()[marketSetNo - 2];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getWicketsB() == 10
                                && marketSetNo == this.currentMatchState.getOversB() + 1)
                    return new CheckMarketResultedOutcome();
                else if (this.currentMatchState.isMatchFinished())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "P:B:OU6":
            case "P:B:OU10":
            case "P:B:OU15":
            case "P:B:OU25":
                if (!serveA && currentOver > marketSetNo) {
                    runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getRunsB() > 0 && marketSetNo == 20) {
                    runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getWicketsB() == 10
                                && marketSetNo == this.currentMatchState.getOversB()) {
                    runsTotal = this.currentMatchState.getRunsInOverNB()[marketSetNo - 1];
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(runsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, runsTotal);
                } else if (serveA && this.currentMatchState.getWicketsB() == 10
                                && marketSetNo == this.currentMatchState.getOversB() + 1)
                    return new CheckMarketResultedOutcome();
                else if (this.currentMatchState.isMatchFinished())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "P:WA": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getCurrentMatchState().isLastPointWicket()) {
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 0)
                            winningSelection = String.format("%s", "WICKET_BOWLED");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 1)
                            winningSelection = String.format("%s", "WICKET_CAUGHT");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 2)
                            winningSelection = String.format("%s", "WICKET_LBW");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 3)
                            winningSelection = String.format("%s", "WICKET_RUN_OUT");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 4)
                            winningSelection = String.format("%s", "WICKET_STUMPED");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 5)
                            winningSelection = String.format("%s", "WICKET_OTHER");
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return new CheckMarketResultedOutcome();
                }

                if (currentWicket > (marketSetNo) || marketSetNo == 10) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "WICKET_BOWLED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "WICKET_CAUGHT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "WICKET_LBW");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "WICKET_STUMPED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "WICKET_OTHER");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (!serveA && this.currentMatchState.getRunsA() > 0) {
                    return new CheckMarketResultedOutcome();
                }
                return null;

            }
            case "P:WB": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getCurrentMatchState().isLastPointWicket()) {
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 0)
                            winningSelection = String.format("%s", "WICKET_BOWLED");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 1)
                            winningSelection = String.format("%s", "WICKET_CAUGHT");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 2)
                            winningSelection = String.format("%s", "WICKET_LBW");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 3)
                            winningSelection = String.format("%s", "WICKET_RUN_OUT");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 4)
                            winningSelection = String.format("%s", "WICKET_STUMPED");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 5)
                            winningSelection = String.format("%s", "WICKET_OTHER");
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return new CheckMarketResultedOutcome();
                }
                if (currentWicket > (marketSetNo) || marketSetNo == 10) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "WICKET_BOWLED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "WICKET_CAUGHT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "WICKET_LBW");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "WICKET_STUMPED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "WICKET_OTHER");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (serveA && this.currentMatchState.getRunsB() > 0) {
                    return new CheckMarketResultedOutcome();
                }
                return null;
            }
            case "P:CA": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();

                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getCurrentMatchState().isLastPointWicket()) {
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 0)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 1)
                            winningSelection = String.format("%s", "Yes");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 2)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 3)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 4)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 5)
                            winningSelection = String.format("%s", "NO");
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return new CheckMarketResultedOutcome();
                }
                if (currentWicket > (marketSetNo) || marketSetNo == 10) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "YES");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "NO");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (!serveA && this.currentMatchState.getRunsA() > 0) {
                    return new CheckMarketResultedOutcome();
                }
                return null;
            }
            case "P:A:OUB": {
                if (currentBall > marketSetNo || this.currentMatchState.isMatchCompleted()) {
                    int currentRuns = this.currentMatchState.getRunsA() - this.previousMatchState.getRunsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(currentRuns,
                                    market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, currentRuns);
                } else
                    return null;
            }
            case "P:B:OUB": {
                if (currentBall > marketSetNo || this.currentMatchState.isMatchCompleted()) {
                    int currentRuns = this.currentMatchState.getRunsB() - this.previousMatchState.getRunsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(currentRuns,
                                    market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, currentRuns);
                } else
                    return null;
            }
            case "P:CB": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();

                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getCurrentMatchState().isLastPointWicket()) {
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 0)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 1)
                            winningSelection = String.format("%s", "Yes");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 2)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 3)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 4)
                            winningSelection = String.format("%s", "NO");
                        if (this.currentMatchState.getCurrentMatchState().getWicketType() == 5)
                            winningSelection = String.format("%s", "NO");
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return new CheckMarketResultedOutcome();
                }
                if (currentWicket > (marketSetNo) || marketSetNo == 10) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "YES");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT_AND_RUN))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "NO");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (serveA && this.currentMatchState.getRunsB() > 0) {
                    return new CheckMarketResultedOutcome();
                }
                return null;

            }
            default:
                throw new IllegalArgumentException(
                                "Market type missing from CricketMatchResultMarkets: " + market.getType());
        }

    }

    private void parseSequenceId(String sequenceId) {
        marketSetNo = 0;
        marketPointNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, bits[0].length());
            marketSetNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketPointNo = Integer.parseInt(bits[1]);
    }

}
