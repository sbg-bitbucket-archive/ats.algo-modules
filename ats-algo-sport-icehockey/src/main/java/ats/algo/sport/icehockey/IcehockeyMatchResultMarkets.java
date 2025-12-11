package ats.algo.sport.icehockey;

import java.util.ArrayList;
import java.util.List;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class IcehockeyMatchResultMarkets extends MatchResultMarkets {

    IcehockeyMatchState previousMatchState;
    IcehockeyMatchState currentMatchState;

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (IcehockeyMatchState) previousMatchState;
        this.currentMatchState = (IcehockeyMatchState) currentMatchState;
        int goalsA = this.currentMatchState.getGoalsA();
        int goalsB = this.currentMatchState.getGoalsB();
        String winningSelection = null;
        List<String> winningSelections = new ArrayList<String>();
        switch (market.getType()) {
            case "FT:AXB": // Match winner
                TeamId matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                        case B:
                            winningSelection = "B";
                        case UNKNOWN:
                            winningSelection = "Draw";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:ML":
            case "FT:ML": // Match winner
                matchWinner = this.currentMatchState.getMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                            break;
                        case B:
                            winningSelection = "B";
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("Match must be settled ");
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:5MR": // 5 mins Match winner
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                // int fiveMinsMarketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                            break;
                        case B:
                            winningSelection = "B";
                            break;
                        case UNKNOWN:
                            winningSelection = "Draw";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:5MG": // 5 mins Match winner
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                // fiveMinsMarketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                            break;
                        case B:
                            winningSelection = "B";
                            break;
                        case UNKNOWN:
                            winningSelection = "Draw";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:DBLC": // Match winner

                if (this.currentMatchState.getElapsedTimeSecs() >= 3600) {
                    int fullTimeGoalsA = this.currentMatchState.getGoalsA();
                    int fullTimeGoalsB = this.currentMatchState.getGoalsB();

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
                            winningSelection = "AX";
                        case B:
                            winningSelection = "BX";
                        case UNKNOWN:
                            winningSelection = "AB";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CS": // correct score
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getNormalTimeGoalsA(),
                                    this.currentMatchState.getNormalTimeGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FTOT:CS": // correct score
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getGoalsA(),
                                    this.currentMatchState.getGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:WM": // correct score
                if (this.currentMatchState.isMatchCompleted()) {
                    int total = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    if (total < 0)
                        winningSelection = "B " + (-total <= 4 ? -total : "5+");
                    else if (total > 0)
                        winningSelection = "A " + (total <= 4 ? total : "5+");
                    else
                        return new CheckMarketResultedOutcome();// void market
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:TFG": // time first goal
                int timeFirstGoal = this.currentMatchState.getElapsedTimeSecsFirstGoal();
                if (timeFirstGoal != 0) {
                    winningSelection = String.format("%d", 1 + timeFirstGoal / 600);
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("No Goal");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OU": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FTOT:OU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;


            case "FT:A:OU": // goals total home
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FTOT:A:OU": // goals total home
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FTOT:B:OU": // goals total home
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OT": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:B:OU": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD": // match handicap
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FTOT:SPRD": // match handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:EH": // match handicap EURO
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:NG": // next goal
                int marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                int currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                int previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastGoal() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "P:AXB": // winner in current period
                int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    int a = this.currentMatchState.getPreviousPeriodGoalsA();
                    int b = this.currentMatchState.getPreviousPeriodGoalsB();
                    if (a > b) {
                        winningSelection = "A";
                    } else if (a < b) {
                        winningSelection = "B";
                    } else {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Should not get here");
                }

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
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodGoalsA(),
                                    this.currentMatchState.getCurrentPeriodGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("period correct score not resulting correctly");
                }

            case "P:OU": // total goals in current half
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
                    throw new IllegalArgumentException("Should not get here");
                }

            case "P:BTTS": // BOTH TEAM TO SCORE PERIOR
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                boolean periodScoreMarketResulted = !(this.currentMatchState.getCurrentPeriodGoalsA() == 0
                                || this.currentMatchState.getCurrentPeriodGoalsB() == 0);

                if (currentPeriodSeqNo <= periodSeqNo && !periodScoreMarketResulted) {
                    if (this.currentMatchState.isPeriodCompleted()) {
                        return new CheckMarketResultedOutcome("No both team scored this period");
                    } else
                        return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));

                if (currentPeriodSeqNo > periodSeqNo || periodScoreMarketResulted) {
                    if (periodScoreMarketResulted)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Period both team scored resulting error");
                }

            case "OT:NG": // OVERTIME NEXT GOAL
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastGoal() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "FT:BTS": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:OE":
                if (this.currentMatchState.isMatchCompleted()) {

                    int pointtTotal = goalsA + goalsB;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:B:OE":
                if (this.currentMatchState.isMatchCompleted()) {

                    int pointtTotal = goalsB;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:A:OE":
                if (this.currentMatchState.isMatchCompleted()) {

                    int pointtTotal = goalsA;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:DNB": // Draw no bet
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("A");
                            case B:
                                return new CheckMarketResultedOutcome("B");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("Void");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;
            case "FT:A:NB": // Home no bet
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("Void");
                            case B:
                                return new CheckMarketResultedOutcome("B");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("Draw");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;

            case "FT:B:NB": // Away no bet
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("A");
                            case B:
                                return new CheckMarketResultedOutcome("Void");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("Draw");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;
            case "FT:AXBBTS": // half time both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "AY";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "DY";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "BY";
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "AN";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "DN";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "BN";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:DBLCBTS": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {

                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("HDYes");
                        winningSelections.add("DAYes");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("HAYes");
                        winningSelections.add("HDYes");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("HAYes");
                        winningSelections.add("DAYes");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("HDNo");
                        winningSelections.add("DANo");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("HANo");
                        winningSelections.add("HDNo");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("HANo");
                        winningSelections.add("DANo");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;

            case "FT:FTSC": // FIRST TEAM SCORES
                TeamId teamTo3Goals = this.currentMatchState.checkFirstTeamScored();

                if (teamTo3Goals == TeamId.UNKNOWN && !this.currentMatchState.isMatchCompleted())
                    return null;
                else if (teamTo3Goals.equals(TeamId.UNKNOWN))
                    winningSelection = "None";
                else if (teamTo3Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamTo3Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:RTG2": //
                TeamId teamTo2Goals = this.currentMatchState.checkTeamToXGoals(2, 0);

                if (teamTo2Goals == null && this.currentMatchState.isMatchCompleted())
                    winningSelection = "Neither";
                else if (teamTo2Goals == null)
                    return null;
                else if (teamTo2Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamTo2Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:RTG3": //
                TeamId teamRTo3Goals = this.currentMatchState.checkTeamToXGoals(3, 0);

                if (teamRTo3Goals == null && this.currentMatchState.isMatchCompleted())
                    winningSelection = "Neither";
                else if (teamRTo3Goals == null)
                    return null;
                else if (teamRTo3Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamRTo3Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            default:
                throw new IllegalArgumentException(
                                "Market type missing from IcehockeyMatchResultMarkets: " + market.getType());

        }
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

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }
}

