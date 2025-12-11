package ats.algo.sport.testcricket;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.sport.testcricket.TestCricketMatchIncidentResult.CricketMatchIncidentResultType;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;

public class TestCricketMatchResultMarkets extends MatchResultMarkets {
    TestCricketMatchState previousMatchState;
    TestCricketMatchState currentMatchState;
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
        this.previousMatchState = (TestCricketMatchState) previousMatchState;
        this.currentMatchState = (TestCricketMatchState) currentMatchState;
        int currentWicket;
        int runsTotal;
        boolean serveA = TeamId.A == this.currentMatchState.getBat();
        if (serveA) {
            currentWicket = this.currentMatchState.getWicketsA() + 1;
        } else {
            currentWicket = this.currentMatchState.getWicketsB() + 1;
        }

        String winningSelection = null;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchFinished()) {
                    if (this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType()
                                    .equals(TestCricketMatchIncidentResult.CricketMatchIncidentResultType.DRAW))
                        winningSelection = "D";
                    else if (this.currentMatchState.getRunsA() > this.currentMatchState.getRunsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:OU:A":
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
            case "FT:OU:B":
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

            case "P:WA": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if ((currentWicket > (marketSetNo + 1) && serveA) || marketSetNo == 9) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "WICKET_BOWLED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "WICKET_CAUGHT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "WICKET_LBW");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "WICKET_STUMPED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "WICKET_OTHER");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
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
                } else if (!serveA && this.currentMatchState.getRunsA() > 0) {
                    return new CheckMarketResultedOutcome();
                }
                return null;

            }
            case "P:WB": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if ((currentWicket > (marketSetNo + 1) && !serveA) || marketSetNo == 9) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "WICKET_BOWLED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "WICKET_CAUGHT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "WICKET_LBW");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "WICKET_RUN_OUT");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "WICKET_STUMPED");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "WICKET_OTHER");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (serveA && this.currentMatchState.getRunsB() > 0) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
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
                return null;
            }
            case "P:CA": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if (currentWicket > (marketSetNo + 1) || marketSetNo == 9) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "YES");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "NO");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (!serveA && this.currentMatchState.getRunsA() > 0) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
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
                return null;
            }
            case "P:CB": {
                CricketMatchIncidentResultType wicketMethod =
                                this.currentMatchState.getCurrentMatchState().getcricketMatchIncidentResultType();
                if (currentWicket > (marketSetNo + 1) || marketSetNo == 9) {
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_CAUGHT))
                        winningSelection = String.format("%s", "YES");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_BOWLED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_LBW))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_RUN_OUT))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_STUMPED))
                        winningSelection = String.format("%s", "NO");
                    if (wicketMethod.equals(CricketMatchIncidentResultType.WICKET_OTHER))
                        winningSelection = String.format("%s", "NO");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (serveA && this.currentMatchState.getRunsB() > 0) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
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
                return null;
            }
            default:
                throw new IllegalArgumentException(
                                "Market type missing from TestCricketMatchResultMarkets: " + market.getType());
        }

    }

}
