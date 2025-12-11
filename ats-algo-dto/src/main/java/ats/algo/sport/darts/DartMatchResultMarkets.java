package ats.algo.sport.darts;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class DartMatchResultMarkets extends MatchResultMarkets {
    DartMatchState previousMatchState;
    DartMatchState currentMatchState;
    boolean setBasedMatch;
    int marketSetNo;
    int marketLegNo;
    Map<Integer, String> boardNumber = new HashMap<Integer, String>();

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        boardNumber.put(1, "GREEN");
        boardNumber.put(2, "RED");
        boardNumber.put(3, "RED");
        boardNumber.put(4, "GREEN");
        boardNumber.put(5, "GREEN");
        boardNumber.put(6, "GREEN");
        boardNumber.put(7, "RED");
        boardNumber.put(8, "RED");
        boardNumber.put(9, "GREEN");
        boardNumber.put(10, "RED");
        boardNumber.put(11, "GREEN");
        boardNumber.put(12, "RED");
        boardNumber.put(13, "RED");
        boardNumber.put(14, "RED");
        boardNumber.put(15, "GREEN");
        boardNumber.put(16, "GREEN");
        boardNumber.put(17, "GREEN");
        boardNumber.put(18, "RED");
        boardNumber.put(19, "GREEN");
        boardNumber.put(20, "RED");
        boardNumber.put(25, "GREEN");
        boardNumber.put(0, "NOCOLOUR");
        this.previousMatchState = (DartMatchState) previousMatchState;
        this.currentMatchState = (DartMatchState) currentMatchState;
        int setsA = this.currentMatchState.getPlayerAScore().getSets();
        int setsB = this.currentMatchState.getPlayerBScore().getSets();
        int legsA = this.currentMatchState.getPlayerAScore().getLegs();
        int legsB = this.currentMatchState.getPlayerBScore().getLegs();
        int A180 = this.currentMatchState.getPlayerAScore().getN180s();
        int B180 = this.currentMatchState.getPlayerBScore().getN180s();
        int preA180 = this.previousMatchState.getPlayerAScore().getN180s();
        int preB180 = this.previousMatchState.getPlayerBScore().getN180s();
        int chkA = this.currentMatchState.getPlayerAScore().getHighestCheckout();
        int chkB = this.currentMatchState.getPlayerBScore().getHighestCheckout();

        int[][] setLeg = this.currentMatchState.getSetLeg();
        int[][] setLegCheckOutScoreA = this.currentMatchState.getSetLegCheckOutScoreA();
        int[][] setLeg180A = this.currentMatchState.getSetLeg180A();
        int[][] setLegCheckOutScoreB = this.currentMatchState.getSetLegCheckOutScoreB();
        int[][] setLeg180B = this.currentMatchState.getSetLeg180B();

        DartMatchFormat matchFormat = this.currentMatchState.getMatchFormat();
        TeamId matchWinner = this.currentMatchState.getMatchOutcome();
        boolean isHatTrick;
        parseSequenceId(market.getSequenceId());
        int nLegsPerSet = matchFormat.getnLegsPerSet();
        setBasedMatch = nLegsPerSet > 1;
        String winningSelection;
        if (setBasedMatch) {
            switch (market.getType()) {
                case "FT:ML": // Match winner
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (setsA > setsB)
                            winningSelection = "A";
                        else
                            winningSelection = "B";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:CS": // Match set correct score
                    if (this.currentMatchState.isMatchCompleted()) {
                        winningSelection = String.format("%d-%d", setsA, setsB);
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:OE": // Match set correct score
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (isOdd(setsA + setsB))
                            winningSelection = "Odd";
                        else
                            winningSelection = "Even";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:SPRD": // Match handicap
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA - setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA + setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = A180 + B180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = A180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = B180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:M180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (A180 > B180)
                            winningSelection = "A";
                        else if (A180 < B180)
                            winningSelection = "B";
                        else
                            winningSelection = "Neither";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkA + chkB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkA;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:170CHK": // Match check out 170
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean chkA170 = this.currentMatchState.getPlayerAScore().getHighestCheckout() == 170;
                        boolean chkB170 = this.currentMatchState.getPlayerBScore().getHighestCheckout() == 170;
                        if (chkA170 || chkB170)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:9DFN": // Match check out 9 dart
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean matchHas9DartFinish = this.currentMatchState.getMatchHas9DartFinish();
                        if (matchHas9DartFinish)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:A:HTRK": // Match hat trick A
                    isHatTrick = matchWinner == TeamId.A && (A180 > B180) && (chkA > chkB);
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (isHatTrick)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:B:HTRK": // Match hat trick B
                    isHatTrick = matchWinner == TeamId.B && (B180 > A180) && (chkB > chkA);
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean chkAhas9DartFinish = this.currentMatchState.getMatchHas9DartFinish();
                        if (chkAhas9DartFinish)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:N180": // Match next 180
                    if (marketSetNo == A180 + B180) {
                        if (A180 > preA180)
                            winningSelection = "A";
                        else if (B180 > preB180)
                            winningSelection = "B";
                        else
                            winningSelection = "Neither";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome("Neither");
                    else
                        return null;
                case "G:ML": // Set leg Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLegCheckOutScoreA[setsA + setsB][0]
                    // + setLegCheckOutScoreB[setsA + setsB][0]);
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0)
                            winningSelection = "A";
                        else
                            winningSelection = "B";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "A";
                        else if (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "B";
                        else
                            winningSelection = "Void";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;

                case "G:1180": // Set leg Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLegCheckOutScoreA[setsA + setsB][0]
                    // + setLegCheckOutScoreB[setsA + setsB][0]);
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if ((setLeg180A[setsA + setsB][legsA + legsB - 1]
                                        + setLeg180B[setsA + setsB][legsA + legsB - 1]) > 0)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else if (setLeg180A[marketSetNo - 1][marketLegNo - 1]
                                        + setLeg180B[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;

                case "G:OU180": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        int n = setLeg180A[setsA + setsB][legsA + legsB - 1]
                                        + setLeg180B[setsA + setsB][legsA + legsB - 1];

                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else {
                            int n = setLeg180A[marketSetNo - 1][marketLegNo - 1]
                                            + setLeg180B[marketSetNo - 1][marketLegNo - 1];
                            winningSelection =
                                            MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:CHKC": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0) {
                            String checkoutColour =
                                            boardNumber.get(setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] / 2);
                            winningSelection = checkoutColour;
                        } else {
                            String checkoutColour =
                                            boardNumber.get(setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1] / 2);
                            winningSelection = checkoutColour;
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else {
                            if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0) {
                                String checkoutColour = boardNumber
                                                .get(setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] / 2);
                                winningSelection = checkoutColour;
                            } else {
                                String checkoutColour = boardNumber
                                                .get(setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] / 2);
                                winningSelection = checkoutColour;
                            }
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:OUCHK": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {

                        int n = setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1]
                                        + setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1];
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);

                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0)) {
                            winningSelection = "Void";
                            return new CheckMarketResultedOutcome(winningSelection);
                        } else {
                            int n = setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1]
                                            + setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1];
                            winningSelection =
                                            MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                            return new CheckMarketResultedOutcome(winningSelection, n);
                        }

                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;

                case "G:OUCHK40": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0) {
                            if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 40.5)
                                winningSelection = "Over 40.5";
                            else
                                winningSelection = "Under 40.5";
                        } else {
                            if (setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1] > 40.5)
                                winningSelection = "Over 40.5";
                            else
                                winningSelection = "Under 40.5";
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0)) {
                            winningSelection = "Void";
                        } else {
                            if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0) {
                                if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 40.5)
                                    winningSelection = "Over 40.5";
                                else
                                    winningSelection = "Under 40.5";
                            } else {
                                if (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] > 40.5)
                                    winningSelection = "Over 40.5";
                                else
                                    winningSelection = "Under 40.5";
                            }
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:ML": // Set Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLeg[setsA + setsB][0] +
                    // setLeg[setsA + setsB][1]);
                    if ((setsA + setsB + 1) > marketSetNo) {
                        if (setLeg[marketSetNo - 1][0] > setLeg[marketSetNo - 1][1])
                            winningSelection = "A";
                        else
                            winningSelection = "B";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:CS": // Set Money Line
                    if ((setsA + setsB + 1) > marketSetNo) {
                        winningSelection =
                                        String.format("%d-%d", setLeg[marketSetNo - 1][0], setLeg[marketSetNo - 1][1]);
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:SPRD": // Set Money Line
                    if ((setsA + setsB + 1) > marketSetNo) {
                        int n = setLeg[marketSetNo - 1][0] - setLeg[marketSetNo - 1][1];
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:OU": // Set Money Line
                    if ((setsA + setsB + 1) > marketSetNo) {
                        int n = setLeg[marketSetNo - 1][0] + setLeg[marketSetNo - 1][1];
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:OE": // Set Money Line
                    if ((setsA + setsB + 1) > marketSetNo) {
                        int n = setLeg[marketSetNo - 1][0] + setLeg[marketSetNo - 1][1];
                        if (isOdd(n))
                            winningSelection = "Odd";
                        else
                            winningSelection = "Even";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "P:OU180": // Set Money Line
                    if ((setsA + setsB + 1) > marketSetNo) {
                        int sum = 0;
                        for (int x = 0; x < setLeg180A[0].length; x++) {
                            sum += setLeg180A[marketSetNo - 1][x];
                            sum += setLeg180B[marketSetNo - 1][x];
                        }
                        winningSelection =
                                        MarketUtilityFunctions.getWinningSelectionOvunMarket(sum, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, sum);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                default:
                    return null;

            }
        } else {
            switch (market.getType()) {
                case "FT:ML": // Match winner
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (setsA > setsB)
                            winningSelection = "A";
                        else if (setsA < setsB)
                            winningSelection = "B";
                        else
                            winningSelection = "Draw";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:AXB": // Match winner
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (setsA > setsB)
                            winningSelection = "A";
                        else if (setsA < setsB)
                            winningSelection = "B";
                        else
                            winningSelection = "Draw";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:CS": // Match set correct score
                    if (this.currentMatchState.isMatchCompleted()) {
                        winningSelection = String.format("%d-%d", setsA, setsB);
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "G:ML": // Set leg Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLegCheckOutScoreA[setsA + setsB][0]
                    // + setLegCheckOutScoreB[setsA + setsB][0]);
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0)
                            winningSelection = "A";
                        else
                            winningSelection = "B";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "A";
                        else if (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "B";
                        else
                            winningSelection = "Void";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:SPRD": // Match handicap
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA - setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "P:OE": // Set Money Line
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setLeg[0][0] + setLeg[0][1];
                        if (isOdd(n))
                            winningSelection = "Odd";
                        else
                            winningSelection = "Even";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:1180": // Set leg Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLegCheckOutScoreA[setsA + setsB][0]
                    // + setLegCheckOutScoreB[setsA + setsB][0]);
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if ((setLeg180A[setsA + setsB][legsA + legsB - 1]
                                        + setLeg180B[setsA + setsB][legsA + legsB - 1]) > 0)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else if (setLeg180A[marketSetNo - 1][marketLegNo - 1]
                                        + setLeg180B[marketSetNo - 1][marketLegNo - 1] > 0)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;

                case "G:OU180": // Set leg Money Line
                    // System.out.println(setsA + "--" + setsB + "--" + legsA + "--"
                    // + legsB + "--" + marketSetNo + "--"
                    // + marketLegNo + "--" + setLegCheckOutScoreA[setsA + setsB][0]
                    // + setLegCheckOutScoreB[setsA + setsB][0]);
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        int n = setLeg180A[setsA + setsB][legsA + legsB - 1]
                                        + setLeg180B[setsA + setsB][legsA + legsB - 1];

                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else {
                            int n = setLeg180A[marketSetNo - 1][marketLegNo - 1]
                                            + setLeg180B[marketSetNo - 1][marketLegNo - 1];
                            winningSelection =
                                            MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:CHKC": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0) {
                            String checkoutColour =
                                            boardNumber.get(setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] / 2);
                            winningSelection = checkoutColour;
                        } else {
                            String checkoutColour =
                                            boardNumber.get(setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1] / 2);
                            winningSelection = checkoutColour;
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0))
                            winningSelection = "Void";
                        else {
                            if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0) {
                                String checkoutColour = boardNumber
                                                .get(setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] / 2);
                                winningSelection = checkoutColour;
                            } else {
                                String checkoutColour = boardNumber
                                                .get(setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] / 2);
                                winningSelection = checkoutColour;
                            }
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "G:OUCHK": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {

                        int n = setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1]
                                        + setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1];
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);

                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0)) {
                            winningSelection = "Void";
                            return new CheckMarketResultedOutcome(winningSelection);
                        } else {
                            int n = setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1]
                                            + setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1];
                            winningSelection =
                                            MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                            return new CheckMarketResultedOutcome(winningSelection, n);
                        }

                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;

                case "G:OUCHK40": // Set leg Money Line
                    if (marketSetNo == (setsA + setsB + 1) && marketLegNo == (legsA + legsB)) {
                        if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 0) {
                            if (setLegCheckOutScoreA[setsA + setsB][legsA + legsB - 1] > 40.5)
                                winningSelection = "Over 40.5";
                            else
                                winningSelection = "Under 40.5";
                        } else {
                            if (setLegCheckOutScoreB[setsA + setsB][legsA + legsB - 1] > 40.5)
                                winningSelection = "Over 40.5";
                            else
                                winningSelection = "Under 40.5";
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if ((setsA + setsB + 1) > marketSetNo && (legsA + legsB) == 0) {
                        if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] == 0
                                        && (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] == 0)) {
                            winningSelection = "Void";
                        } else {
                            if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 0) {
                                if (setLegCheckOutScoreA[marketSetNo - 1][marketLegNo - 1] > 40.5)
                                    winningSelection = "Over 40.5";
                                else
                                    winningSelection = "Under 40.5";
                            } else {
                                if (setLegCheckOutScoreB[marketSetNo - 1][marketLegNo - 1] > 40.5)
                                    winningSelection = "Over 40.5";
                                else
                                    winningSelection = "Under 40.5";
                            }
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome();
                    else
                        return null;
                case "FT:OE": // Match set correct score
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (isOdd(setsA + setsB))
                            winningSelection = "Odd";
                        else
                            winningSelection = "Even";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:SPRD": // Match handicap
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA - setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA + setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OUS": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = A180 + B180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = A180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OU180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = B180;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:M180": // Match total 180
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (A180 > B180)
                            winningSelection = "A";
                        else if (A180 < B180)
                            winningSelection = "B";
                        else
                            winningSelection = "Neither";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkA + chkB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:A:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkA;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:B:OUCHK": // Match total checkout
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = chkB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "FT:170CHK": // Match check out 170
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean chkA170 = this.currentMatchState.getPlayerAScore().getHighestCheckout() == 170;
                        boolean chkB170 = this.currentMatchState.getPlayerBScore().getHighestCheckout() == 170;
                        if (chkA170 || chkB170)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:9DFN": // Match check out 9 dart
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean matchHas9DartFinish = this.currentMatchState.getMatchHas9DartFinish();
                        if (matchHas9DartFinish)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:A:HTRK": // Match hat trick A
                    isHatTrick = matchWinner == TeamId.A && (A180 > B180) && (chkA > chkB);
                    if (this.currentMatchState.isMatchCompleted()) {
                        if (isHatTrick)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:B:HTRK": // Match hat trick B
                    isHatTrick = matchWinner == TeamId.B && (B180 > A180) && (chkB > chkA);
                    if (this.currentMatchState.isMatchCompleted()) {
                        boolean chkAhas9DartFinish = this.currentMatchState.getMatchHas9DartFinish();
                        if (chkAhas9DartFinish)
                            winningSelection = "Yes";
                        else
                            winningSelection = "No";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                case "FT:N180": // Match next 180
                    if (marketSetNo == A180 + B180) {
                        if (A180 > preA180)
                            winningSelection = "A";
                        else if (B180 > preB180)
                            winningSelection = "B";
                        else
                            winningSelection = "Neither";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome("Neither");
                    else
                        return null;
                case "G:OU": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA + setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "G:A:OU": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsA;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                case "G:B:OU": // Match total games
                    if (this.currentMatchState.isMatchCompleted()) {
                        int n = setsB;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else
                        return null;
                default:
                    return null;
            }
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
        marketLegNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, bits[0].length());
            marketSetNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketLegNo = Integer.parseInt(bits[1]);
    }

    /**
     * returns true of n odd
     * 
     * @param n
     * @return
     */
    boolean isOdd(int n) {
        return 2 * (n / 2) != n;
    }
}
