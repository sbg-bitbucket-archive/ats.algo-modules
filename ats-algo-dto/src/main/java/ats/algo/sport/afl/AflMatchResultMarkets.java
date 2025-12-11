
package ats.algo.sport.afl;

import java.util.ArrayList;
import java.util.List;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class AflMatchResultMarkets extends MatchResultMarkets {

    AflMatchState previousMatchState;
    AflMatchState currentMatchState;

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (AflMatchState) previousMatchState;
        this.currentMatchState = (AflMatchState) currentMatchState;
        String winningSelection = null;
        String lastPeriodNo = null;
        String periodNo = this.currentMatchState.getSequenceIdForPeriod(0);
        if (this.currentMatchState.getPeriodNo() > 1)
            lastPeriodNo = this.previousMatchState.getSequenceIdForPeriod(0);
        switch (market.getType()) {
            case "P:TOFG": // time of first goal quarter
                int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                if (periodNo.equals(market.getSequenceId()) && this.currentMatchState.getTimeOfFirstGoalQuarter() != -1)
                    return new CheckMarketResultedOutcome(timeOfFirstGoal());
                else if (this.currentMatchState.getPeriodNo() > periodSeqNo)
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "FT:TOFG": // time of first goal
                if (this.currentMatchState.getTimeOfFirstGoalMatch() != -1
                                && !this.currentMatchState.isNormalTimeMatchCompleted()) {

                    if (this.currentMatchState.getTimeOfFirstGoalMatch() < 60)
                        winningSelection = "0.00-1.00";
                    else if (this.currentMatchState.getTimeOfFirstGoalMatch() < 120)
                        winningSelection = "1.01-2.00";
                    else if (this.currentMatchState.getTimeOfFirstGoalMatch() < 180)
                        winningSelection = "2.01-3.00";
                    else if (this.currentMatchState.getTimeOfFirstGoalMatch() < 240)
                        winningSelection = "3.01-4.00";
                    else if (this.currentMatchState.getTimeOfFirstGoalMatch() < 300)
                        winningSelection = "4.01-5.00";
                    else
                        winningSelection = "5.01+";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else
                    return null;
            case "HT:W": // half time leader market
                if (this.currentMatchState.getPeriodNo() > 2) {
                    int firstHalfPointsA =
                                    this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ2PointsA();
                    int firstHalfPointsB =
                                    this.currentMatchState.getQ1PointsB() + this.currentMatchState.getQ2PointsB();


                    if (firstHalfPointsA > firstHalfPointsB)
                        winningSelection = "Team A";
                    else if (firstHalfPointsA < firstHalfPointsB)
                        winningSelection = "Team B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:HTFT": // full time half time resulting

                int firstHalfPointsA = this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ2PointsA();
                int firstHalfPointsB = this.currentMatchState.getQ1PointsB() + this.currentMatchState.getQ2PointsB();

                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    TeamId matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                if (firstHalfPointsA > firstHalfPointsB)
                                    winningSelection = "Team A / Team A";
                                else if (firstHalfPointsA < firstHalfPointsB)
                                    winningSelection = "Team B / Team A";
                                else if (firstHalfPointsA == firstHalfPointsB)
                                    winningSelection = "Any Other Result";
                                break;
                            case B:
                                if (firstHalfPointsA > firstHalfPointsB)
                                    winningSelection = "Team A / Team B";
                                else if (firstHalfPointsA < firstHalfPointsB)
                                    winningSelection = "Team B / Team B";
                                else if (firstHalfPointsA == firstHalfPointsB)
                                    winningSelection = "Any Other Result";
                                break;
                            case UNKNOWN:
                                winningSelection = "Any Other Result";
                                break;

                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "P:E:W": // third quarter time leader
                if (this.currentMatchState.getPeriodNo() > 3) {
                    if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB())
                        winningSelection = "Team A";
                    else if (this.currentMatchState.getPointsA() < this.currentMatchState.getPointsB())
                        winningSelection = "Team B";
                    else
                        return new CheckMarketResultedOutcome();
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:OU:B": // goals BEHIND
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getBehindsA() + this.currentMatchState.getBehindsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:OU:B:A": // goals BEHIND
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getBehindsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:OU:B:B": // goals BEHIND
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getBehindsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:G": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:OU:G:A": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:OU:G:B": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:BTT:40": // goals total away
                if (this.currentMatchState.getPointsA() >= 40 && this.currentMatchState.getPointsB() >= 40) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:50": // goals total away
                if (this.currentMatchState.getPointsA() >= 50 && this.currentMatchState.getPointsB() >= 50) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:60": // goals total away
                if (this.currentMatchState.getPointsA() >= 60 && this.currentMatchState.getPointsB() >= 60) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:70": // goals total away
                if (this.currentMatchState.getPointsA() >= 70 && this.currentMatchState.getPointsB() >= 70) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:80": // goals total away
                if (this.currentMatchState.getPointsA() >= 80 && this.currentMatchState.getPointsB() >= 80) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:90": // goals total away
                if (this.currentMatchState.getPointsA() >= 90 && this.currentMatchState.getPointsB() >= 90) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTT:100": // goals total away
                if (this.currentMatchState.getPointsA() >= 100 && this.currentMatchState.getPointsB() >= 100) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:HSH": // high score half
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ2PointsA()
                                    + this.currentMatchState.getQ1PointsB()
                                    + this.currentMatchState.getQ2PointsB() > this.currentMatchState.getQ3PointsA()
                                                    + this.currentMatchState.getQ4PointsA()
                                                    + this.currentMatchState.getQ3PointsB()
                                                    + this.currentMatchState.getQ4PointsB()) {
                        winningSelection = "First Half";
                    } else if (this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ2PointsA()
                                    + this.currentMatchState.getQ1PointsB()
                                    + this.currentMatchState.getQ2PointsB() < this.currentMatchState.getQ3PointsA()
                                                    + this.currentMatchState.getQ4PointsA()
                                                    + this.currentMatchState.getQ3PointsB()
                                                    + this.currentMatchState.getQ4PointsB()) {
                        winningSelection = "Second Half";
                    } else
                        return new CheckMarketResultedOutcome();
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:HSQ": // high score half
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int q1 = this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ1PointsB();
                    int q2 = this.currentMatchState.getQ2PointsA() + this.currentMatchState.getQ2PointsB();
                    int q3 = this.currentMatchState.getQ3PointsA() + this.currentMatchState.getQ3PointsB();
                    int q4 = this.currentMatchState.getQ4PointsA() + this.currentMatchState.getQ4PointsB();
                    if (q1 > q2 && q1 > q3 && q1 > q4) {
                        winningSelection = "First Quarter";
                    } else if (q2 > q1 && q2 > q3 && q2 > q4) {
                        winningSelection = "Second Quarter";
                    } else if (q3 > q1 && q3 > q2 && q3 > q4) {
                        winningSelection = "Third Quarter";
                    } else if (q4 > q1 && q4 > q3 && q4 > q2) {
                        winningSelection = "Fourth Quarter";
                    } else
                        return new CheckMarketResultedOutcome();
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:FTTS:15": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.getPointsA() >= 15 || this.currentMatchState.getPointsB() >= 15) {
                    if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                        winningSelection = "Team A";
                    } else {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:FTTS:25": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.getPointsA() >= 25 || this.currentMatchState.getPointsB() >= 25) {
                    if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                        winningSelection = "Team A";
                    } else {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:FTTS:35": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.getPointsA() >= 35 || this.currentMatchState.getPointsB() >= 35) {
                    if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                        winningSelection = "Team A";
                    } else {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:FTTS:50": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.getPointsA() >= 50 || this.currentMatchState.getPointsB() >= 50) {
                    if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                        winningSelection = "Team A";
                    } else {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:FT3B": // goals total away
                if (!this.currentMatchState.getFirstTo3B().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo3B().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo3B().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT4B": // goals total away
                if (!this.currentMatchState.getFirstTo4B().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo4B().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo4B().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT5B": // goals total away
                if (!this.currentMatchState.getFirstTo5B().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo5B().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo5B().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT6B": // goals total away
                if (!this.currentMatchState.getFirstTo6B().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo6B().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo6B().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "FTOT:FT3G": // goals total away
                if (!this.currentMatchState.getFirstTo3G().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo3G().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo3G().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT4G": // goals total away
                if (!this.currentMatchState.getFirstTo4G().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo4G().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo4G().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT5G": // goals total away
                if (!this.currentMatchState.getFirstTo5G().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo5G().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo5G().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;
            case "FTOT:FT6G": // goals total away
                if (!this.currentMatchState.getFirstTo6G().equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getFirstTo6G().equals(TeamId.A)) {
                        winningSelection = "Team A";
                    } else if (this.currentMatchState.getFirstTo6G().equals(TeamId.B)) {
                        winningSelection = "Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isMatchCompleted())
                    return new CheckMarketResultedOutcome();
                else
                    return null;

            case "P:FTTS:10": // first team to score 10 points in quarter
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                TeamId teamFirstTen = this.currentMatchState.checkListForScoreXXPoints(periodSeqNo, 10);
                if (teamFirstTen.equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getPeriodNo() > periodSeqNo)
                        return new CheckMarketResultedOutcome();
                    return null;
                }

                if (teamFirstTen.equals(TeamId.A)) {
                    winningSelection = "Team A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (teamFirstTen.equals(TeamId.B)) {
                    winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.getPeriodNo() > periodSeqNo) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "P:FTTS:15": // first team to score 10 points in quarter
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                teamFirstTen = this.currentMatchState.checkListForScoreXXPoints(periodSeqNo, 15);
                if (teamFirstTen.equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getPeriodNo() > periodSeqNo)
                        return new CheckMarketResultedOutcome();
                    return null;
                }

                if (teamFirstTen.equals(TeamId.A)) {
                    winningSelection = "Team A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (teamFirstTen.equals(TeamId.B)) {
                    winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.getPeriodNo() > periodSeqNo) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "P:FTTS:20": // first team to score 10 points in quarter
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                teamFirstTen = this.currentMatchState.checkListForScoreXXPoints(periodSeqNo, 20);
                if (teamFirstTen.equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getPeriodNo() > periodSeqNo)
                        return new CheckMarketResultedOutcome();
                    return null;
                }

                if (teamFirstTen.equals(TeamId.A)) {
                    winningSelection = "Team A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (teamFirstTen.equals(TeamId.B)) {
                    winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.getPeriodNo() > periodSeqNo) {
                    return new CheckMarketResultedOutcome();
                } else if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "P:LTSQ": // last team to score in quarter
                int lastScoringCurrentQuarter =
                                checkWhetherToResultLastScoringMarket(previousMatchState, currentMatchState);
                if (!(lastPeriodNo == null)) {
                    if (lastPeriodNo.equals(market.getSequenceId())) {
                        if (lastScoringCurrentQuarter != -1) {
                            if (lastScoringCurrentQuarter == 0) {
                                winningSelection = "Team A";
                            } else if (lastScoringCurrentQuarter == 1) {
                                winningSelection = "Team A";
                            } else if (lastScoringCurrentQuarter == 2) {
                                winningSelection = "Team B";
                            } else if (lastScoringCurrentQuarter == 3) {
                                winningSelection = "Team B";
                            } else
                                return new CheckMarketResultedOutcome();
                            return new CheckMarketResultedOutcome(winningSelection);
                        } else
                            return null;
                    } else
                        return null;
                } else
                    return null;

            case "FT:PLUS:24": // last team to score in quarter
                if (((AflMatchState) currentMatchState).isMatchCompleted()) {
                    boolean winA = ((AflMatchState) currentMatchState)
                                    .getPointsA() > ((AflMatchState) currentMatchState).getPointsB() + 24.5;
                    boolean winB = ((AflMatchState) currentMatchState)
                                    .getPointsB() > ((AflMatchState) currentMatchState).getPointsA() + 24.5;

                    if (winA) {
                        winningSelection = "Team A +24.5";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (winB) {
                        winningSelection = "Team B +24.5";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        List<String> winningSelections = new ArrayList<String>(2);
                        winningSelections.add("Team A +24.5");
                        winningSelections.add("Team B +24.5");
                        return new CheckMarketResultedOutcome(winningSelections);
                    }

                } else
                    return null;

            case "FT:PLUS:39": // last team to score in quarter

                if (((AflMatchState) currentMatchState).isMatchCompleted()) {
                    boolean winA = ((AflMatchState) currentMatchState)
                                    .getPointsA() > ((AflMatchState) currentMatchState).getPointsB() + 39.5;
                    boolean winB = ((AflMatchState) currentMatchState)
                                    .getPointsB() > ((AflMatchState) currentMatchState).getPointsA() + 39.5;

                    if (winA) {
                        winningSelection = "Team A +39.5";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else if (winB) {
                        winningSelection = "Team B +39.5";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        List<String> winningSelections = new ArrayList<String>(2);
                        winningSelections.add("Team A +39.5");
                        winningSelections.add("Team B +39.5");
                        return new CheckMarketResultedOutcome(winningSelections);
                    }

                } else
                    return null;

            case "FT:PLUS:24:B": // last team to score in quarter

                if (currentMatchState.isMatchCompleted()) {
                    boolean win = ((AflMatchState) currentMatchState).getPointsB()
                                    + 24.5 > ((AflMatchState) currentMatchState).getPointsA();
                    if (win) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:PLUS:39:B": // last team to score in quarter

                if (currentMatchState.isMatchCompleted()) {
                    boolean win = ((AflMatchState) currentMatchState).getPointsB()
                                    + 39.5 > ((AflMatchState) currentMatchState).getPointsA();
                    if (win) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:LTTS": // last team to score in quarter
                if (((AflMatchState) currentMatchState).isNormalTimeMatchCompleted()
                                && !((AflMatchState) currentMatchState).isMannulResulting()) {
                    TeamId lastScoringTeam = ((AflMatchState) currentMatchState).checkListForTeamLastScoreInQuarter(
                                    ((AflMatchState) currentMatchState).getGoalInfoList(), -1);
                    if (lastScoringTeam == TeamId.A) {
                        winningSelection = "Team A";
                    } else if (lastScoringTeam == TeamId.B) {
                        winningSelection = "Team B";
                    } else {
                        throw new IllegalArgumentException("No Team Has Scored in The Match!");
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "P:FTTS": // first team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                TeamId firstScoreTeamCurrentQuarter = TeamId.UNKNOWN;
                if (this.currentMatchState.getPeriodNo() < periodSeqNo)
                    return null;
                else
                    firstScoreTeamCurrentQuarter = ((AflMatchState) currentMatchState)
                                    .checkListForFirstScoreTeamForQuarter(periodSeqNo);

                if ((periodSeqNo > 4 && periodSeqNo < 2) || firstScoreTeamCurrentQuarter.equals(TeamId.UNKNOWN)) {
                    if (this.currentMatchState.getPeriodNo() > periodSeqNo)
                        return new CheckMarketResultedOutcome();
                    /*
                     * only generating markets for 2 3 4 quarters
                     */
                    return null;
                }

                if (firstScoreTeamCurrentQuarter.equals(TeamId.A)) {
                    winningSelection = "Team A";
                } else if (firstScoreTeamCurrentQuarter.equals(TeamId.B)) {
                    winningSelection = "Team B";
                } else if (this.currentMatchState.getPeriodNo() > periodSeqNo) {
                    return new CheckMarketResultedOutcome();
                }
                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:FTTS": // first team to score

                if (((AflMatchState) currentMatchState).getPointsA()
                                + ((AflMatchState) currentMatchState).getPointsB() <= 0) {
                    /*
                     * only generating markets for 2 3 4 quarters
                     */
                    return null;
                }
                firstScoreTeamCurrentQuarter =
                                ((AflMatchState) currentMatchState).checkListForFirstScoreTeamForQuarter(-1);
                if (firstScoreTeamCurrentQuarter.equals(TeamId.A)) {
                    winningSelection = "Team A";
                } else if (firstScoreTeamCurrentQuarter.equals(TeamId.B)) {
                    winningSelection = "Team B";
                }
                return new CheckMarketResultedOutcome(winningSelection);

            case "P:LSQ": // last scoring play 1/2/3/4 quarter
                lastScoringCurrentQuarter =
                                checkWhetherToResultLastScoringMarket(previousMatchState, currentMatchState);
                if (!(lastPeriodNo == null)) {
                    if (lastPeriodNo.equals(market.getSequenceId())) {
                        if (lastScoringCurrentQuarter != -1) {
                            if (lastScoringCurrentQuarter == 0) {
                                winningSelection = "Team A Goal";
                            } else if (lastScoringCurrentQuarter == 1) {
                                winningSelection = "Team A Behind";
                            } else if (lastScoringCurrentQuarter == 2) {
                                winningSelection = "Team B Goal";
                            } else if (lastScoringCurrentQuarter == 3) {
                                winningSelection = "Team B Behind";
                            } else if (lastScoringCurrentQuarter == 4) {
                                winningSelection = "No Score";
                            }

                            // else if (lastScoringCurrentQuarter == 4) {
                            // winningSelection = "No Score";
                            // }
                            return new CheckMarketResultedOutcome(winningSelection);
                        } else
                            return null;
                    } else
                        return null;
                } else
                    return null;

            case "FT:LSQ": // last scoring play match

                if (currentMatchState.isMatchCompleted()) {
                    lastScoringCurrentQuarter = ((AflMatchState) currentMatchState).checkListForLastScore(-1);

                    if (lastScoringCurrentQuarter == 0) {
                        winningSelection = "Team A Goal";
                    } else if (lastScoringCurrentQuarter == 1) {
                        winningSelection = "Team A Behind";
                    } else if (lastScoringCurrentQuarter == 2) {
                        winningSelection = "Team B Goal";
                    } else if (lastScoringCurrentQuarter == 3) {
                        winningSelection = "Team B Behind";
                    } else if (lastScoringCurrentQuarter == 4) {
                        winningSelection = "No Score";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:E:OE": // PERIOD ODD EVEN
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (2 == periodSeqNo) {
                    int a = this.currentMatchState.getQ2PointsA() + this.currentMatchState.getQ1PointsA();
                    int b = this.currentMatchState.getQ2PointsB() + this.currentMatchState.getQ1PointsB();
                    if ((a + b) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (3 == periodSeqNo) {
                    int a = this.currentMatchState.getQ3PointsA() + this.currentMatchState.getQ2PointsA()
                                    + this.currentMatchState.getQ1PointsA();
                    int b = this.currentMatchState.getQ3PointsB() + this.currentMatchState.getQ2PointsB()
                                    + this.currentMatchState.getQ1PointsB();
                    if ((a + b) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (4 == periodSeqNo) {
                    int a = this.currentMatchState.getQ4PointsA() + this.currentMatchState.getQ3PointsA()
                                    + this.currentMatchState.getQ2PointsA() + this.currentMatchState.getQ1PointsA();
                    int b = this.currentMatchState.getQ4PointsB() + this.currentMatchState.getQ3PointsB()
                                    + this.currentMatchState.getQ2PointsB() + this.currentMatchState.getQ1PointsB();
                    if ((a + b) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                }

            case "P:OE": // PERIOD ODD EVEN
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= 1) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int a = this.currentMatchState.getQ1PointsA();
                int b = this.currentMatchState.getQ1PointsB();
                if ((a + b) % 2 == 0) {
                    winningSelection = "Even";
                } else {
                    winningSelection = "Odd";
                }

                return new CheckMarketResultedOutcome(winningSelection);


            case "HT:SPRD": // PERIOD ODD EVEN
                int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                if (currentHalfSeqNo <= halfSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int n1 = 0;
                if (halfSeqNo == 1) {
                    n1 = this.currentMatchState.getQ1PointsA() - this.currentMatchState.getQ1PointsB()
                                    + this.currentMatchState.getQ2PointsA() - this.currentMatchState.getQ2PointsB();
                } else {
                    n1 = this.currentMatchState.getQ3PointsA() - this.currentMatchState.getQ3PointsB()
                                    + this.currentMatchState.getQ4PointsA() - this.currentMatchState.getQ4PointsB();
                }

                winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n1, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n1);

            case "P:SPRD": // PERIOD ODD EVEN
                periodSeqNo = this.currentMatchState.getPeriodNo();
                GamePeriod period = this.currentMatchState.getGamePeriod();
                n1 = 0;
                if (period == GamePeriod.FIRST_QUARTER || period == GamePeriod.SECOND_QUARTER
                                || period == GamePeriod.THIRD_QUARTER || period == GamePeriod.FOURTH_QUARTER) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                } else if ((periodSeqNo > 1) && (this.previousMatchState
                                .getPeriodNo() == convertPeriodSeqIdToIndex(market.getSequenceId()))) {

                    if (this.previousMatchState.getPeriodNo() == 1)
                        n1 = this.currentMatchState.getQ1PointsA() - this.currentMatchState.getQ1PointsB();
                    else if (this.previousMatchState.getPeriodNo() == 2)
                        n1 = this.currentMatchState.getQ2PointsA() - this.currentMatchState.getQ2PointsB();
                    else if (this.previousMatchState.getPeriodNo() == 3)
                        n1 = this.currentMatchState.getQ3PointsA() - this.currentMatchState.getQ3PointsB();
                    else if (this.currentMatchState.isMatchCompleted())
                        n1 = this.currentMatchState.getQ4PointsA() - this.currentMatchState.getQ4PointsB();
                    else
                        return null;
                } else
                    return null;


                winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n1, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n1);

            case "FT:OE": // FT POINTS ODD EVEN
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if ((this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB()) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:QBQ": // quarter by quarter leader

                String quarterLeader = "Draw At End Of Any Quarter";

                if (this.currentMatchState.getPeriodNo() > 1 && this.currentMatchState.isPeriodCompleted()) {

                    int q1A = this.currentMatchState.getPointsA();
                    int q1B = this.currentMatchState.getPointsB();

                    if (q1A == q1B)
                        return new CheckMarketResultedOutcome(quarterLeader);
                }

                if (this.currentMatchState.getPeriodNo() > 2 && this.currentMatchState.isPeriodCompleted()) {

                    int q2A = this.currentMatchState.getPointsA();
                    int q2B = this.currentMatchState.getPointsB();

                    if (q2A == q2B)
                        return new CheckMarketResultedOutcome(quarterLeader);
                }

                if (this.currentMatchState.getPeriodNo() > 3 && this.currentMatchState.isPeriodCompleted()) {

                    int q3A = this.currentMatchState.getPointsA();
                    int q3B = this.currentMatchState.getPointsB();

                    if (q3A == q3B)
                        return new CheckMarketResultedOutcome(quarterLeader);
                }

                if (this.currentMatchState.isNormalTimeMatchCompleted()) {

                    quarterLeader = "Draw At End Of Any Quarter";
                    int q1A = this.currentMatchState.getQ1PointsA();
                    int q1B = this.currentMatchState.getQ1PointsB();
                    int q2A = this.currentMatchState.getQ2PointsA() + q1A;
                    int q2B = this.currentMatchState.getQ2PointsB() + q1B;
                    int q3A = this.currentMatchState.getQ3PointsA() + q2A;
                    int q3B = this.currentMatchState.getQ3PointsB() + q2B;
                    int q4A = this.currentMatchState.getQ4PointsA() + q3A;
                    int q4B = this.currentMatchState.getQ4PointsB() + q3B;

                    // { "HHHH", "HHHA", "HHAH", "HHAA", "HAHH", "HAHA", "HAAH","HAAA",
                    // "AHHH","AHHA","AHAH","AHAA","AAHH","AAHA","AAAH","AAAA","Draw At End Of Any Quarter" };
                    if (q1A > q1B) {
                        if (q2A > q2B) { // HH
                            if (q3A > q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team A / Team A / Team A / Team A"; // HHHH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team A / Team A / Team A / Team B"; // HHHA
                                }
                            } else if (q3A < q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team A / Team A / Team B / Team A"; // HHAH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team A / Team A / Team B / Team B"; // HHAA
                                }
                            }

                        } else if (q2A < q2B) { // HA
                            if (q3A > q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team A / Team B / Team A / Team A"; // HAHH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team A / Team B / Team A / Team B"; // HAHA
                                }
                            } else if (q3A < q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team A / Team B / Team B / Team A"; // HAAH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team A / Team B / Team B / Team B"; // HAAA
                                }
                            }
                        }

                    } else if (q1A < q1B) { // A
                        if (q2A > q2B) { // AH
                            if (q3A > q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team B / Team A / Team A / Team A"; // AHHH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team B / Team A / Team A / BTeam B"; // AHHA
                                }
                            } else if (q3A < q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team B / Team A / Team B / Team A"; // AHAH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team B / Team A / Team B / Team B"; // AHAA
                                }
                            }

                        } else if (q2A < q2B) { // AA
                            if (q3A > q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team B / Team B / Team A / Team A"; // AAHH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team B / Team B / Team A / Team B"; // AAHA
                                }
                            } else if (q3A < q3B) {
                                if (q4A > q4B) {
                                    quarterLeader = "Team B / Team B / Team B / Team A"; // AAAH
                                } else if (q4A < q4B) {
                                    quarterLeader = "Team B / Team B / Team B / Team B"; // AAAA
                                }
                            }
                        }
                    }
                    return new CheckMarketResultedOutcome(quarterLeader);

                } else
                    return null;

            case "FT:A:OE": // FT POINTS ODD EVEN A
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if ((this.currentMatchState.getPointsA()) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:B:OE": // FT POINTS ODD EVEN B
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if ((this.currentMatchState.getPointsB()) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:FSQ": // first scoring play 2/3/4 quarter
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int firstScoringCurrentQuarter =
                                checkWhetherToResultFirstScoringMarket(previousMatchState, currentMatchState);
                if (periodNo.equals(market.getSequenceId())) {
                    if (firstScoringCurrentQuarter != 0) {
                        if (firstScoringCurrentQuarter == 1) {
                            winningSelection = "Team A Goal";
                        } else if (firstScoringCurrentQuarter == 2) {
                            winningSelection = "Team A Behind";
                        } else if (firstScoringCurrentQuarter == 3) {
                            winningSelection = "Team B Goal";
                        } else if (firstScoringCurrentQuarter == 4) {
                            winningSelection = "Team B Behind";
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else
                        return null;
                } else if (this.currentMatchState.getPeriodNo() > periodSeqNo) {
                    winningSelection = "No Score";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:FSQ": // first scoring play 2/3/4 quarter
                firstScoringCurrentQuarter = ((AflMatchState) currentMatchState).checkListForFirstScoreTeamMethod();
                TeamId firstScoringTeam = ((AflMatchState) currentMatchState).checkListForFirstScoreTeam();
                if (firstScoringCurrentQuarter != 0) {
                    if (firstScoringTeam == TeamId.A) {
                        if (firstScoringCurrentQuarter == 1)
                            winningSelection = "Team A Goal";
                        else if (firstScoringCurrentQuarter == 2)
                            winningSelection = "Team A Behind";
                    } else if (firstScoringTeam == TeamId.B) {
                        if (firstScoringCurrentQuarter == 1)
                            winningSelection = "Team B Goal";
                        else if (firstScoringCurrentQuarter == 2)
                            winningSelection = "Team B Behind";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:WM2": // winning margin 1-24
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    if (i >= 1 && i <= 24) {
                        winningSelection = "Team A 1-24";
                    } else if (i >= 25) {
                        winningSelection = "Team A 25+";
                    } else if (i <= -1 && i >= -24) {
                        winningSelection = "Team B 1-24";
                    } else if (i <= -25) {
                        winningSelection = "Team B 25+";
                    } else if (i == 0) {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:TB15": // tribet margin
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    if (i > 15) {
                        winningSelection = "Team A win by more than 15.5 points";
                    } else if (i <= -15) {
                        winningSelection = "Team B win by more than 15.5 points";
                    } else if (i == 0) {
                        return new CheckMarketResultedOutcome();
                    } else {
                        winningSelection = "Either Team to win by less than 15.5 points";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FTOT:TB24": // tribet margin
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    if (i > 24) {
                        winningSelection = "Team A win by more than 24.5 points";
                    } else if (i <= -24) {
                        winningSelection = "Team B win by more than 24.5 points";
                    } else if (i == 0) {
                        return new CheckMarketResultedOutcome();
                    } else {
                        winningSelection = "Either Team to win by less than 24.5 points";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FTOT:TB39": // tribet margin
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    if (i > 39) {
                        winningSelection = "Team A win by more than 39.5 points";
                    } else if (i <= -39) {
                        winningSelection = "Team B win by more than 39.5 points";
                    } else if (i == 0) {
                        return new CheckMarketResultedOutcome();
                    } else {
                        winningSelection = "Either Team to win by less than 39.5 points";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FTOT:WM": // winning margin
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    if (i >= 1 && i <= 39) {
                        winningSelection = "Team A 1-39";
                    } else if (i >= 40) {
                        winningSelection = "Team A 40+";
                    } else if (i <= -1 && i >= -39) {
                        winningSelection = "Team B 1-39";
                    } else if (i <= -40) {
                        winningSelection = "Team B 40+";
                    } else if (i == 0) {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:AXB": // Match winner
                TeamId matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "Team A";
                        case B:
                            winningSelection = "Team B";
                        case UNKNOWN:
                            winningSelection = "Draw";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:ML": // Match winner
                matchWinner = this.currentMatchState.getMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "Team A";
                            break;
                        case B:
                            winningSelection = "Team B";
                            break;
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome();
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:NG": // next goal
                int marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                int currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo || this.currentMatchState.isMannulResulting()) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted() && !this.currentMatchState.isMannulResulting()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                int previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastGoal() == TeamId.A)
                        winningSelection = "Team A";
                    else
                        winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "FTOT:NB": // next goal
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForBehind(0));
                if (currentStateSeqNo <= marketSeqNo || this.currentMatchState.isMannulResulting()) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted() && !this.currentMatchState.isMannulResulting()) {
                        return new CheckMarketResultedOutcome("No Behind");
                    } else
                        return null;
                }

                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForBehind(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastBehind() == TeamId.A)
                        winningSelection = "Team A";
                    else
                        winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one behind should be scored in each matchState change");
                }

            case "FTOT:NP": // next point
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForPoint(0));
                if (currentStateSeqNo <= marketSeqNo || this.currentMatchState.isMannulResulting()) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted() && !this.currentMatchState.isMannulResulting()) {
                        return new CheckMarketResultedOutcome("No Point");
                    } else
                        return null;
                }

                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPoint(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastPoint() == TeamId.A)
                        winningSelection = "Team A";
                    else
                        winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one behind should be scored in each matchState change");
                }

            case "FT:DBLC": // Match winner

                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int fullTimeGoalsA = this.currentMatchState.getPointsA();
                    int fullTimeGoalsB = this.currentMatchState.getPointsB();

                    TeamId matchDoubleChance = TeamId.UNKNOWN;
                    if (fullTimeGoalsA >= fullTimeGoalsB) {
                        matchDoubleChance = TeamId.A;
                    }
                    if (fullTimeGoalsA <= fullTimeGoalsB) {
                        matchDoubleChance = TeamId.B;
                    }
                    if (fullTimeGoalsA > fullTimeGoalsB || fullTimeGoalsA < fullTimeGoalsB) {
                        matchDoubleChance = TeamId.UNKNOWN;
                    }
                    switch (matchDoubleChance) {
                        case A:
                            winningSelection = "Team A / Draw";
                        case B:
                            winningSelection = "Team B / Draw";
                        case UNKNOWN:
                            winningSelection = "Team A / Team B";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CS": // correct score
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getPointsA(),
                                    this.currentMatchState.getPointsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OU": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:P:A": // goals total home
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OT": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (this.currentMatchState.getPointsA() == this.currentMatchState.getPointsB()) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OU:P:B": // POINTS total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD": // match handicap
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:EH": // match handicap EURO
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "P:W": // winner in current period
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                a = this.currentMatchState.getAPointsForPeriod(periodSeqNo);
                b = this.currentMatchState.getBPointsForPeriod(periodSeqNo);
                if (a > b) {
                    winningSelection = "Team A";
                } else if (a < b) {
                    winningSelection = "Team B";
                } else {
                    return new CheckMarketResultedOutcome();
                }

                return new CheckMarketResultedOutcome(winningSelection);

            case "P:W:AM": // winner in current period
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                a = this.currentMatchState.getAPointsForPeriod(periodSeqNo);
                b = this.currentMatchState.getBPointsForPeriod(periodSeqNo);
                if (a > b) {
                    winningSelection = "Team A";
                } else if (a < b) {
                    winningSelection = "Team B";
                } else {
                    winningSelection = "Draw";
                }

                return new CheckMarketResultedOutcome(winningSelection);

            case "P:CS": // correct score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this PERIOD is still progressing
                     */
                    return null;
                }
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodPointsA(),
                                    this.currentMatchState.getCurrentPeriodPointsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("period correct score not resulting correctly");
                }

            case "P:OU:G": // total goals in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    // throw new IllegalArgumentException("Resulting market P:OU:G error, match period sequence ID is
                    // not"
                    // + " matching the market sequence ID."+ " Match state period sequence ID is"+currentPeriodSeqNo+"
                    // ,"
                    // + " but market sequence ID is "+periodSeqNo+".");
                    return null; // Temp fix this for Coral /Ladbroke
                }

                // case "P:E:OU:P": // total points in current quater
            case "P:OU:P":
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo || currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                int n = this.currentMatchState.getTotalAPointsForPeriod(periodSeqNo)
                                + this.currentMatchState.getTotalBPointsForPeriod(periodSeqNo);
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

            // case "P:OU:P": // total points in 1 quater
            case "P:OU":
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= 1) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                n = this.currentMatchState.getQ1PointsA() + this.currentMatchState.getQ1PointsB();
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

            // case "P:E:A:OU:P": // total points in current quater
            case "P:OU:A":
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                n = this.currentMatchState.getTotalAPointsForPeriod(periodSeqNo);

                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

            // case "P:E:B:OU:P": // total points in current quater
            case "P:OU:B":
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                n = this.currentMatchState.getTotalBPointsForPeriod(periodSeqNo);

                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

            case "P:BTTS": // BOTH TEAM TO SCORE PERIOR
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                boolean periodScoreMarketResulted = !(this.currentMatchState.getCurrentPeriodPointsA() == 0
                                || this.currentMatchState.getCurrentPeriodPointsB() == 0);

                if (currentPeriodSeqNo <= periodSeqNo && !periodScoreMarketResulted) {
                    if (this.currentMatchState.isPeriodCompleted()) {
                        return new CheckMarketResultedOutcome("No both team scored this period");
                    } else
                        return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));

                if (currentPeriodSeqNo > periodSeqNo || periodScoreMarketResulted) {
                    if (periodScoreMarketResulted)
                        winningSelection = "Team A";
                    else
                        winningSelection = "Team B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Period both team scored resulting error");
                }
            case "FT:SPRD_DR1":
            case "FT:SPRD_DR2":
            case "FT:OU_DR3":
            case "FT:SPRD_DR4":
                // TODO - add resulting logic for dynamic range markets
                return null;

            default:
                throw new IllegalArgumentException(
                                "Market type missing from AflMatchResultMarkets: " + market.getType());

        }
    }

    private int checkListForLastScore(List<GoalInfo> goalInfoList, int periodNo) {
        int lastScoreInPeriodSeconds = -1;
        TeamId team = TeamId.UNKNOWN;
        int scoreMethod = -1;
        for (GoalInfo item : goalInfoList) {
            if (item.getPeriodNoG() == periodNo) {
                lastScoreInPeriodSeconds = item.getSecs();
                team = item.getTeam();
                scoreMethod = item.getMethod();
            }
        }

        if (scoreMethod == 0 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 0;
        } else if (scoreMethod == 1 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 1;
        } else if (scoreMethod == 0 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 2;
        } else if (scoreMethod == 1 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 3;
        } else {
            lastScoreInPeriodSeconds = 4; // No score in period
        }

        return lastScoreInPeriodSeconds;
    }

    private int checkWhetherToResultLastScoringMarket(MatchState previousMatchState2, MatchState currentMatchState2) {
        int lastScoringResult = -1;

        if (((AflMatchState) currentMatchState2).getPeriodNo() > ((AflMatchState) previousMatchState2).getPeriodNo()
                        && ((AflMatchState) currentMatchState2).getPeriodNo() != 0) {
            lastScoringResult = checkListForLastScore(((AflMatchState) previousMatchState2).getGoalInfoList(),
                            ((AflMatchState) previousMatchState2).getPeriodNo());
        }
        return lastScoringResult;
    }

    private int checkWhetherToResultFirstScoringMarket(MatchState previousMatchState2, MatchState currentMatchState2) {
        int scoringResult = 0;
        switch (((AflMatchState) currentMatchState2).getPeriodNo()) {
            case 0:
            case 1:
                break;
            case 2:
                if (((AflMatchState) currentMatchState2).getFirstScoringPlay2Quarter() != 0
                                || ((AflMatchState) currentMatchState2).getMatchPeriod()
                                                .equals(AflMatchPeriod.AT_SECOND_PERIOD_END))
                    return ((AflMatchState) currentMatchState2).getFirstScoringPlay2Quarter();
            case 3:
                if (((AflMatchState) currentMatchState2).getFirstScoringPlay3Quarter() != 0
                                || ((AflMatchState) currentMatchState2).getMatchPeriod()
                                                .equals(AflMatchPeriod.AT_THIRD_PERIOD_END))
                    return ((AflMatchState) currentMatchState2).getFirstScoringPlay3Quarter();
            case 4:
                if (((AflMatchState) currentMatchState2).getFirstScoringPlay4Quarter() != 0
                                || ((AflMatchState) currentMatchState2).getMatchPeriod()
                                                .equals(AflMatchPeriod.AT_FULL_TIME))
                    return ((AflMatchState) currentMatchState2).getFirstScoringPlay4Quarter();
        }
        return scoringResult;
    }

    private String timeOfFirstGoal() {
        String selection = null;
        if (this.currentMatchState
                        .getTimeOfFirstGoalQuarter() < (60 + (this.currentMatchState.getPeriodNo() - 1) * 1200))
            selection = "0.00-1.00";
        else if (this.currentMatchState
                        .getTimeOfFirstGoalQuarter() < (120 + (this.currentMatchState.getPeriodNo() - 1) * 1200))
            selection = "1.01-2.00";
        else if (this.currentMatchState
                        .getTimeOfFirstGoalQuarter() < (180 + (this.currentMatchState.getPeriodNo() - 1) * 1200))
            selection = "2.01-3.00";
        else if (this.currentMatchState
                        .getTimeOfFirstGoalQuarter() < (240 + (this.currentMatchState.getPeriodNo() - 1) * 1200))
            selection = "3.01-4.00";
        else if (this.currentMatchState
                        .getTimeOfFirstGoalQuarter() < (300 + (this.currentMatchState.getPeriodNo() - 1) * 1200))
            selection = "4.01-5.00";
        else
            selection = "5.01+";
        return selection;

    }

    /**
     * Converts string of form "Pnn" to integer
     * 
     * @param periodSequenceId
     * @return
     */
    private int convertPeriodSeqIdToIndex(String sequenceId) {
        int index = 0;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }

    private int convertGoalSeqIdToIndex(String sequenceId) {
        int index = 0;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }
}
