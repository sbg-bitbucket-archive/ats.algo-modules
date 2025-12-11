package ats.algo.sport.football;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.core.markets.Selection;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.core.util.log.LoggerFactory;

public class FootballMatchResultMarkets extends MatchResultMarkets {

    FootballMatchState previousMatchState;
    FootballMatchState currentMatchState;
    private static final String[] winningMarginRange = {"X", "A1", "A2", "A2", "B1", "B2", "B2"};
    private static final String[] exactTotalGoal = {"0", "1", "2+"}; // P:OUR
    private static final String[] exactTotalGoalForTeam = {"0", "1", "2", "3+"}; // FT:A:£OUR_1, FT:B:£OUR_1, P:A:OUR,
                                                                                 // P:B:OUR
    private static final String[] exactTotalGoalRange = {"0-1 goals", "2-3 goals", "4-5 goals", "6+"}; // FT:Â£OUR_2
    private static final String[] exactTotalGoalRange2 = {"0", "1", "2", "3", "4", "5", "6+"}; // FT:Â£OUR_2


    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (FootballMatchState) previousMatchState;
        this.currentMatchState = (FootballMatchState) currentMatchState;
        int goalCurrentHalfA = this.currentMatchState.getPreviousPeriodGoalsA();
        int goalCurrentHalfB = this.currentMatchState.getPreviousPeriodGoalsB();
        int goalFirstHalfA = this.currentMatchState.getFirstHalfGoalsA();
        int goalFirstHalfB = this.currentMatchState.getFirstHalfGoalsB();
        int goalSecondHalfA = this.currentMatchState.getSecondHalfGoalsA();
        int goalSecondHalfB = this.currentMatchState.getSecondHalfGoalsB();
        int goalsA = this.currentMatchState.getGoalsA();
        int goalsB = this.currentMatchState.getGoalsB();
        int periodSeqNo;
        int currentPeriodSeqNo;
        int previousPeriodSeqNo;
        int marketSeqNo;

        int currentStateSeqNo;
        int previousStateSeqNo;
        TeamId matchWinner;
        String winningSelection = null;
        List<String> winningSelections = new ArrayList<String>(2);

        switch (market.getType()) {

            case "FT:BAB":

                if (currentMatchState.isMatchCompleted()) {
                    String babWinSelection = "F:";
                    if (this.currentMatchState.didTeamScoreFirst(TeamId.A))
                        babWinSelection += "A,H:";
                    else if (this.currentMatchState.didTeamScoreFirst(TeamId.B))
                        babWinSelection += "B,H:";
                    else
                        babWinSelection += "X,H:";
                    String halfFullScore = goalFirstHalfA + "-" + goalFirstHalfB + ",C:" + goalsA + "-" + goalsB;
                    babWinSelection += halfFullScore;
                    if (market.getSelections().containsKey(babWinSelection))
                        return new CheckMarketResultedOutcome(babWinSelection);
                    else
                        return new CheckMarketResultedOutcome();
                } else
                    return null;


            case "FT:PRT": // Match winner
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    return new CheckMarketResultedOutcome("W");
                } else if (!this.currentMatchState.preMatch() && this.currentMatchState.getElapsedTimeSecs() == 0) {
                    List<String> loseSelections = new ArrayList<String>();
                    List<String> voidedSelections = new ArrayList<String>();
                    voidedSelections.add("PV");
                    return new CheckMarketResultedOutcome(loseSelections, voidedSelections, false);
                } else if (this.currentMatchState.getPeriodNo() == 2 && this.previousMatchState.getPeriodNo() == 1) {
                    List<String> loseSelections = new ArrayList<String>();
                    List<String> voidedSelections = new ArrayList<String>();
                    loseSelections.add("PL");
                    return new CheckMarketResultedOutcome(loseSelections, voidedSelections, false);
                } else
                    return null;


            case "FT:A:Â£OUR_2": // aggregated goals
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsA()) {
                        case 0:
                        case 1:
                            winningSelections.add("0-1");
                            break;
                        case 2:
                        case 3:
                            winningSelections.add("2-3");
                            break;
                        default:
                            winningSelections.add("4+");
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                }

            case "FT:B:Â£OUR_2": // aggregated goals
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsB()) {
                        case 0:
                        case 1:
                            winningSelections.add("0-1");
                            break;
                        case 2:
                        case 3:
                            winningSelections.add("2-3");
                            break;
                        default:
                            winningSelections.add("4+");
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                }


            case "FT:A:MLTG": // multi goal home
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsA()) {
                        case 0:
                            winningSelections.add("0");
                        case 1:
                            winningSelections.add("1-2");
                            winningSelections.add("1-3");
                            break;
                        case 2:
                        case 3:
                            winningSelections.add("2-3");
                            break;
                        case 4:
                            winningSelections.add("4+");

                            break;
                        default:
                            winningSelections.add("4+");
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                }

            case "FT:B:MLTG": // multi goal AWAY
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsB()) {
                        case 0:
                            winningSelections.add("0");
                        case 1:
                            winningSelections.add("1-2");
                            winningSelections.add("1-3");
                            break;
                        case 2:
                        case 3:
                            winningSelections.add("2-3");
                            break;
                        case 4:
                            winningSelections.add("4+");

                            break;
                        default:
                            winningSelections.add("4+");
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                }

            case "FT:AXB": // Match winner
            case "FT:MW": // Match winner
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("A");
                            case B:
                                return new CheckMarketResultedOutcome("B");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("X");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;

            case "FT:AXBTTSOU": // Match winner
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    boolean bothScored =
                                    (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0);
                    double goalsLine = Double.parseDouble(market.getLineId());
                    int acturalGoals = this.currentMatchState.getGoalNo() - 1;

                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                if (!bothScored) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Home to win, Not Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Home to win, Not Both teams will score and there will be less than X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Home to win, Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Home to win, Both teams will score and there will be less than X goals");
                                }
                            case B:
                                if (!bothScored) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Away to win, Not Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Away to win, Not Both teams will score and there will be less than X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Away to win, Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Away to win, Both teams will score and there will be less than X goals");
                                }

                            case UNKNOWN:
                                if (!bothScored) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Draw, Not Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Draw, Not Both teams will score and there will be less than X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome(
                                                        "Draw, Both teams will score and there will be X or more goals");
                                    else
                                        return new CheckMarketResultedOutcome(
                                                        "Draw, Both teams will score and there will be less than X goals");
                                }
                            default:
                                break;
                        }
                    }
                    return new CheckMarketResultedOutcome();
                } else
                    return null;
            case "FT:HFPOU":
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    double goalsLine = Double.parseDouble(market.getLineId());
                    int acturalGoals = this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB();

                    int goalA1H = this.currentMatchState.getFirstHalfGoalsA();
                    int goalB1H = this.currentMatchState.getFirstHalfGoalsB();

                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/H and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/H and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/H and less then X goals");
                                }
                            case B:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/A and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/A and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/A and less then X goals");
                                }

                            case UNKNOWN:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/D and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/D and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/D and less then X goals");
                                }
                            default:
                                break;
                        }
                    }
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "FT:HFOU": // Match winner
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    double goalsLine = Double.parseDouble(market.getLineId());
                    int acturalGoals = this.currentMatchState.getGoalNo() - 1;

                    int goalA1H = this.currentMatchState.getFirstHalfGoalsA();
                    int goalB1H = this.currentMatchState.getFirstHalfGoalsB();

                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/H and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/H and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/H and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/H and less then X goals");
                                }
                            case B:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/A and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/A and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/A and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/A and less then X goals");
                                }

                            case UNKNOWN:
                                if (goalA1H > goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("H/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("H/D and less then X goals");
                                } else if (goalA1H == goalB1H) {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("D/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("D/D and less then X goals");
                                } else {
                                    if (acturalGoals >= goalsLine)
                                        return new CheckMarketResultedOutcome("A/D and X or more match goals");
                                    else
                                        return new CheckMarketResultedOutcome("A/D and less then X goals");
                                }
                            default:
                                break;
                        }
                    }
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "FT:ETWBH": // Match winner
                if (this.currentMatchState.isFirstHalfCompleted() && this.currentMatchState
                                .getFirstHalfGoalsA() == this.currentMatchState.getFirstHalfGoalsB())
                    return new CheckMarketResultedOutcome("No");

                if (this.currentMatchState.isMatchCompleted()) {
                    TeamId firstHalfWinner = TeamId.UNKNOWN;
                    if (this.currentMatchState.getFirstHalfGoalsA() > this.currentMatchState.getFirstHalfGoalsB())
                        firstHalfWinner = TeamId.A;
                    else if (this.currentMatchState.getFirstHalfGoalsA() < this.currentMatchState.getFirstHalfGoalsB())
                        firstHalfWinner = TeamId.B;

                    TeamId secondHalfWinner = TeamId.UNKNOWN;
                    if (this.currentMatchState.getSecondHalfGoalsA() > this.currentMatchState.getSecondHalfGoalsB())
                        secondHalfWinner = TeamId.A;
                    else if (this.currentMatchState.getSecondHalfGoalsA() < this.currentMatchState
                                    .getSecondHalfGoalsB())
                        secondHalfWinner = TeamId.B;

                    if (firstHalfWinner != TeamId.UNKNOWN && secondHalfWinner == firstHalfWinner) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else {
                        return new CheckMarketResultedOutcome("No");
                    }
                } else
                    return null;

            case "FT:TQ": // Match winner
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("A");
                            case B:
                                return new CheckMarketResultedOutcome("B");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("X");
                            default:
                                break;
                        }
                    }
                    return null;
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
                                return new CheckMarketResultedOutcome("X");
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
                                return new CheckMarketResultedOutcome("X");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;
            case "FT:HOF": // Double chance
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    TeamId firstHalfWinner = TeamId.UNKNOWN;
                    if (matchWinner == TeamId.A || firstHalfWinner == TeamId.A) {
                        winningSelections.add("Home team to win at half time OR full time");
                    }

                    if (matchWinner == TeamId.B || firstHalfWinner == TeamId.B) {
                        winningSelections.add("Away team to win at half time OR full time");
                    }

                    if (matchWinner == TeamId.UNKNOWN || firstHalfWinner == TeamId.UNKNOWN) {
                        winningSelections.add("Draw at half time OR full time");
                    }

                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;
            case "FT:DBLC": // Double chance
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                winningSelections.add("AB");
                                winningSelections.add("AX");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case B:
                                winningSelections.add("AB");
                                winningSelections.add("XB");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case UNKNOWN:
                                winningSelections.add("AX");
                                winningSelections.add("XB");
                                return new CheckMarketResultedOutcome(winningSelections);
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;
            case "FT:DBLC$3HCP": // Double chance and handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    goalsA = this.currentMatchState.getGoalsA();
                    goalsB = this.currentMatchState.getGoalsB();
                    int handicapGoals = Integer.valueOf(market.getLineId());

                    if (goalsA + handicapGoals > goalsB - handicapGoals
                                    || goalsA + handicapGoals < goalsB - handicapGoals)
                        winningSelections.add("AHBH");
                    if (goalsA + handicapGoals > goalsB - handicapGoals
                                    || goalsA + handicapGoals == goalsB - handicapGoals)
                        winningSelections.add("AHXH");
                    if (goalsA + handicapGoals == goalsB - handicapGoals
                                    || goalsA + handicapGoals < goalsB - handicapGoals)
                        winningSelections.add("BHXH ");
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;
            case "FT:75MR": // 75 mins Match winner
                String previous = ((FootballMatchState) previousMatchState).getSequenceIdForXXMinsResult(75);
                String current = ((FootballMatchState) currentMatchState).getSequenceIdForXXMinsResult(75);

                if (!previous.equals(current)) {
                    int indicator = Integer.parseInt(previous.substring(1));
                    matchWinner = this.currentMatchState.findMatchWinnerForPeriod(indicator, 15); // 1st
                                                                                                  // period,
                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;
            case "FT:60MR": // 60 mins Match winner
                // previous = ((FootballMatchState) previousMatchState).getSequenceIdForXXMinsResult(60);
                // current = ((FootballMatchState) currentMatchState).getSequenceIdForXXMinsResult(60);
                if (((FootballMatchState) currentMatchState).getElapsedTimeSecs() > 3600) {
                    // int indicator = Integer.parseInt(previous.substring(1));
                    matchWinner = this.currentMatchState.findMatchWinnerForPeriod(0, 12); // 1st
                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;
            case "FT:30MR": // 30 mins Match winner
                previous = ((FootballMatchState) previousMatchState).getSequenceIdForXXMinsResult(30);
                current = ((FootballMatchState) currentMatchState).getSequenceIdForXXMinsResult(30);

                if (!previous.equals(current)) {
                    int indicator = Integer.parseInt(previous.substring(1));
                    matchWinner = this.currentMatchState.findMatchWinnerForPeriod(indicator, 6); // 1st
                                                                                                 // period,

                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;
            case "FT:15MR": // 15 mins Match winner
                String previousFifteen = ((FootballMatchState) previousMatchState).getSequenceIdForFifteenMinsResult();
                String currentFifteen = ((FootballMatchState) currentMatchState).getSequenceIdForFifteenMinsResult();

                if (!previousFifteen.equals(currentFifteen)) {
                    int fifteenIndicator = Integer.parseInt(previousFifteen.substring(1));
                    matchWinner = this.currentMatchState.findMatchWinnerForPeriod(fifteenIndicator, 2); // 1st
                                                                                                        // period,

                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;

            case "FT:10MR": // 10 mins Match winner
                String previousTen = ((FootballMatchState) previousMatchState).getSequenceIdForTenMinsResult();
                String currentTen = ((FootballMatchState) currentMatchState).getSequenceIdForTenMinsResult();

                if (!previousTen.equals(currentTen)) {
                    int fifteenIndicator = Integer.parseInt(previousTen.substring(1));
                    matchWinner = this.currentMatchState.findMatchWinnerForPeriod(fifteenIndicator, 1); // 1st
                                                                                                        // period,

                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;

            case "FT:5MR": // 5 mins Match winner
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome("X");
                        default:
                            break;
                    }
                    return null;
                } else
                    return null;

            case "FT:5MC": // 5 mins corner over under
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                int fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());

                if (matchWinner != null) {
                    int n = this.currentMatchState.cornerInXXMins(fiveMinutes, TeamId.UNKNOWN, 1);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);

                } else
                    return null;

            case "FT:5MB": // 5 mins booking over under
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());
                boolean booking = ((FootballMatchState) currentMatchState).bookingNotYetHappend(fiveMinutes, 5,
                                TeamId.UNKNOWN);
                int fiveMinutesPeriod = ((FootballMatchState) currentMatchState).getFiveMinsNo();
                if (fiveMinutesPeriod > fiveMinutes || booking) {
                    if (!booking)
                        return new CheckMarketResultedOutcome("No");
                    else
                        return new CheckMarketResultedOutcome("Yes");

                } else
                    return null;

            case "FT:10MB": // 5 mins booking yes or no
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());
                booking = ((FootballMatchState) currentMatchState).bookingNotYetHappend(fiveMinutes, 10,
                                TeamId.UNKNOWN);

                fiveMinutesPeriod = ((FootballMatchState) currentMatchState).getFiveMinsNo() / 2;
                if (fiveMinutesPeriod > fiveMinutes || booking) {
                    if (!booking)
                        return new CheckMarketResultedOutcome("No");
                    else
                        return new CheckMarketResultedOutcome("Yes");

                } else
                    return null;

            case "FT:15MB": // 5 mins booking yes or no
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());
                booking = ((FootballMatchState) currentMatchState).bookingNotYetHappend(fiveMinutes, 15,
                                TeamId.UNKNOWN);

                fiveMinutesPeriod = ((FootballMatchState) currentMatchState).getFiveMinsNo() / 3;
                if (fiveMinutesPeriod > fiveMinutes || booking) {
                    if (!booking)
                        return new CheckMarketResultedOutcome("No");
                    else
                        return new CheckMarketResultedOutcome("Yes");

                } else
                    return null;

            case "FT:10MC": // 5 mins corner over under
                matchWinner = this.currentMatchState.getTenMinsMatchWinner();
                fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());

                if (matchWinner != null) {
                    int n = this.currentMatchState.cornerInXXMins(fiveMinutes, TeamId.UNKNOWN, 2);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);

                } else
                    return null;

            case "FT:15MC": // 15 mins corner over under
                matchWinner = this.currentMatchState.getFifteenMinsMatchWinner();
                fiveMinutes = convertGoalSeqIdToIndex(market.getSequenceId());

                if (matchWinner != null) {
                    int n = this.currentMatchState.cornerInXXMins(fiveMinutes, TeamId.UNKNOWN, 3);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);

                } else
                    return null;

            case "FT:5MG": // 5 mins goal scored
                Boolean ifGoal = this.currentMatchState.getFiveMinsIfGoal();
                if (ifGoal != null) {
                    if (ifGoal)
                        return new CheckMarketResultedOutcome("A");
                    if (!ifGoal)
                        return new CheckMarketResultedOutcome("B");
                    return null;
                } else
                    return null;


            case "FT:60MG": // 5 mins goal scored
                if (this.currentMatchState.getElapsedTimeSecs() > 3600) {
                    if (this.currentMatchState.getGoalNo() > 1)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else {
                    if (this.currentMatchState.getGoalNo() > 1)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return null;
                }

            case "FT:75MG": //
                if (this.currentMatchState.getElapsedTimeSecs() > 4500) {
                    if (this.currentMatchState.getGoalNo() > 1)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else {
                    if (this.currentMatchState.getGoalNo() > 1)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return null;
                }

            case "FT:10MG": // 10 mins goal scored
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                ifGoal = this.currentMatchState.getXXMinsIfGoal(marketSeqNo, 10);
                if (ifGoal != null) {
                    if (ifGoal)
                        return new CheckMarketResultedOutcome("A");
                    if (!ifGoal)
                        return new CheckMarketResultedOutcome("B");
                    return null;
                } else
                    return null;

            case "FT:15MG": // 15 mins goal scored
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                ifGoal = this.currentMatchState.getXXMinsIfGoal(marketSeqNo, 15);
                if (ifGoal != null) {
                    if (ifGoal)
                        return new CheckMarketResultedOutcome("A");
                    if (!ifGoal)
                        return new CheckMarketResultedOutcome("B");
                    return null;
                } else
                    return null;

            case "FT:20MG": // 20 mins goal scored
                ifGoal = this.currentMatchState.getXXMinsIfGoal(0, 20);
                if (ifGoal != null) {
                    if (ifGoal)
                        return new CheckMarketResultedOutcome("Yes");
                    if (!ifGoal)
                        return new CheckMarketResultedOutcome("No");
                    return null;
                } else
                    return null;

            case "FT:30MG": // 20 mins goal scored
                ifGoal = this.currentMatchState.getXXMinsIfGoal(0, 30);
                if (ifGoal != null) {
                    if (ifGoal)
                        return new CheckMarketResultedOutcome("Yes");
                    if (!ifGoal)
                        return new CheckMarketResultedOutcome("No");
                    return null;
                } else
                    return null;

            case "FT:CS": // correct score
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getGoalsA(),
                                    this.currentMatchState.getGoalsB());
                } else
                    return null;
                switch (winningSelection) {
                    case "0-0":
                    case "0-1":
                    case "0-2":
                    case "0-3":
                    case "0-4":
                    case "0-5":
                    case "0-6":
                    case "1-0":
                    case "1-1":
                    case "1-2":
                    case "1-3":
                    case "1-4":
                    case "1-5":
                    case "1-6":
                    case "2-0":
                    case "2-1":
                    case "2-2":
                    case "2-3":
                    case "2-4":
                    case "2-5":
                    case "2-6":
                    case "3-0":
                    case "3-1":
                    case "3-2":
                    case "3-3":
                    case "3-4":
                    case "3-5":
                    case "3-6":
                    case "4-0":
                    case "4-1":
                    case "4-2":
                    case "4-3":
                    case "4-4":
                    case "4-5":
                    case "4-6":
                    case "5-0":
                    case "5-1":
                    case "5-2":
                    case "5-3":
                    case "5-4":
                    case "5-5":
                    case "5-6":
                    case "6-0":
                    case "6-1":
                    case "6-2":
                    case "6-3":
                    case "6-4":
                    case "6-5":
                    case "6-6":
                        return new CheckMarketResultedOutcome(winningSelection);
                    default:
                        return new CheckMarketResultedOutcome("Any Other");
                }

            case "FT:CSDS": // correct score
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getGoalsA(),
                                    this.currentMatchState.getGoalsB());
                    return generateResultOutcome(winningSelection);
                } else
                    return null;

            case "FT:CCS": // correct score
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCornersA(),
                                    this.currentMatchState.getCornersB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:A:OU": // goals total home
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:B:OU": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD": // match handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:3HCP": // match handicap EURO
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = -this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:NG": // next goal
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getGoalSequenceId());
                if (currentStateSeqNo <= marketSeqNo) {
                    if (this.currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getGoalSequenceId());
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastGoal() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return new CheckMarketResultedOutcome();
                    // warnin("Only one goal should be scored in each matchState change");
                }

            case "FT:HF": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "H/H";
                    else if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "D/H";
                    else if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "A/H";
                    else if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "H/D";
                    else if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "D/D";
                    else if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "A/D";
                    else if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "H/A";
                    else if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "D/A";
                    else if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                                    .getPreviousPeriodGoalsB())
                        winningSelection = "A/A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:WM": // winning margine
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() == 0)
                        winningSelection = winningMarginRange[0];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() == 1)
                        winningSelection = winningMarginRange[1];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() == 2)
                        winningSelection = winningMarginRange[2];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() > 2)
                        winningSelection = winningMarginRange[3];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() == -1)
                        winningSelection = winningMarginRange[4];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() == -2)
                        winningSelection = winningMarginRange[5];
                    if (this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB() < -2)
                        winningSelection = winningMarginRange[6];
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:Â£OUR_2": // goals total home range
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) {
                        case 0:
                        case 1:
                            winningSelection = exactTotalGoalRange[0];
                            break;
                        case 2:
                        case 3:
                            winningSelection = exactTotalGoalRange[1];
                            break;
                        case 4:
                        case 5:
                            winningSelection = exactTotalGoalRange[2];
                            break;
                        default:
                            winningSelection = exactTotalGoalRange[3];
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                }

            case "FT:Â£OUR_1":
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) {
                        case 0:
                            winningSelection = exactTotalGoalRange2[0];
                            break;
                        case 1:
                            winningSelection = exactTotalGoalRange2[1];
                            break;
                        case 2:
                            winningSelection = exactTotalGoalRange2[2];
                            break;
                        case 3:
                            winningSelection = exactTotalGoalRange2[3];
                            break;
                        case 4:
                            winningSelection = exactTotalGoalRange2[4];
                            break;
                        case 5:
                            winningSelection = exactTotalGoalRange2[5];
                            break;
                        default:
                            winningSelection = exactTotalGoalRange2[6];
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                }

            case "P:£CS_2": // goals total home range
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    int home = this.currentMatchState.getFirstHalfGoalsA();
                    int away = this.currentMatchState.getFirstHalfGoalsB();
                    String homeString = home >= 3 ? "3+" : String.valueOf(home);
                    String awayString = away >= 3 ? "3+" : String.valueOf(away);
                    winningSelection = homeString + "-" + awayString;
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    int home = this.currentMatchState.getSecondHalfGoalsA();
                    int away = this.currentMatchState.getSecondHalfGoalsB();
                    String homeString = home >= 3 ? "3+" : String.valueOf(home);
                    String awayString = away >= 3 ? "3+" : String.valueOf(away);
                    winningSelection = homeString + "-" + awayString;
                    return new CheckMarketResultedOutcome(winningSelection);
                }

            case "FT:£CS_2": // goals total home range
                if (this.currentMatchState.isMatchCompleted()) {
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();
                    String homeString = home >= 5 ? "5+" : String.valueOf(home);
                    String awayString = away >= 5 ? "5+" : String.valueOf(away);
                    winningSelection = homeString + "-" + awayString;
                    return new CheckMarketResultedOutcome(winningSelection);
                }
            case "FT:A:£OUR_1": // goals total home range
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsA()) {
                        case 0:
                            winningSelection = exactTotalGoalForTeam[0];
                            break;
                        case 1:
                            winningSelection = exactTotalGoalForTeam[1];
                            break;
                        case 2:
                            winningSelection = exactTotalGoalForTeam[2];
                            break;
                        default:
                            winningSelection = exactTotalGoalForTeam[3];
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                }
            case "FT:B:£OUR_1": // goals total home range
                if (this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getGoalsB()) {
                        case 0:
                            winningSelection = exactTotalGoalForTeam[0];
                            break;
                        case 1:
                            winningSelection = exactTotalGoalForTeam[1];
                            break;
                        case 2:
                            winningSelection = exactTotalGoalForTeam[2];
                            break;
                        default:
                            winningSelection = exactTotalGoalForTeam[3];
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                }
            case "P:OUR": // 1st / 2nd Half Total Goals
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()
                                || "H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    winningSelection = selectWinningRangeSelection(this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:£OUR_2": // 1st / 2nd Half Total Goals
            case "P:OUR_2":
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    winningSelection = (this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB() >= 3 ? "3+"
                                                    : String.valueOf(this.currentMatchState.getFirstHalfGoalsA()
                                                                    + this.currentMatchState.getFirstHalfGoalsB()));
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    winningSelection = (this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB() >= 3 ? "3+"
                                                    : String.valueOf(this.currentMatchState.getSecondHalfGoalsA()
                                                                    + this.currentMatchState.getSecondHalfGoalsB()));
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:A:£OUR_1": // Half Home Team Total Goals 1
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    if (this.currentMatchState.getFirstHalfGoalsA() == 0)
                        return new CheckMarketResultedOutcome("0");
                    else if (this.currentMatchState.getFirstHalfGoalsA() == 1)
                        return new CheckMarketResultedOutcome("1");
                    else if (this.currentMatchState.getFirstHalfGoalsA() == 2)
                        return new CheckMarketResultedOutcome("2");
                    else if (this.currentMatchState.getFirstHalfGoalsA() >= 3)
                        return new CheckMarketResultedOutcome("3");
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getSecondHalfGoalsA() == 0)
                        return new CheckMarketResultedOutcome("0");
                    else if (this.currentMatchState.getSecondHalfGoalsA() == 1)
                        return new CheckMarketResultedOutcome("1");
                    else if (this.currentMatchState.getSecondHalfGoalsA() == 2)
                        return new CheckMarketResultedOutcome("2");
                    else if (this.currentMatchState.getSecondHalfGoalsA() >= 3)
                        return new CheckMarketResultedOutcome("3");
                } else
                    return null;


            case "P:B:£OUR_1": // Half Home Team Total Goals 1
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    if (this.currentMatchState.getFirstHalfGoalsB() == 0)
                        return new CheckMarketResultedOutcome("0");
                    else if (this.currentMatchState.getFirstHalfGoalsB() == 1)
                        return new CheckMarketResultedOutcome("1");
                    else if (this.currentMatchState.getFirstHalfGoalsB() == 2)
                        return new CheckMarketResultedOutcome("2");
                    else if (this.currentMatchState.getFirstHalfGoalsB() >= 3)
                        return new CheckMarketResultedOutcome("3");
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getSecondHalfGoalsB() == 0)
                        return new CheckMarketResultedOutcome("0");
                    else if (this.currentMatchState.getSecondHalfGoalsB() == 1)
                        return new CheckMarketResultedOutcome("1");
                    else if (this.currentMatchState.getSecondHalfGoalsB() == 2)
                        return new CheckMarketResultedOutcome("2");
                    else if (this.currentMatchState.getSecondHalfGoalsB() >= 3)
                        return new CheckMarketResultedOutcome("3");
                } else
                    return null;

            case "FT:£OUR_4":
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection =
                                    (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() >= 5 ? "5+"
                                                    : String.valueOf(this.currentMatchState.getGoalsA()
                                                                    + this.currentMatchState.getGoalsB()));
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:£OUR_3":
                if (this.currentMatchState.isMatchCompleted()) {

                    if (this.currentMatchState.getGoalNo() < 1)
                        return new CheckMarketResultedOutcome("0-1");
                    else if (this.currentMatchState.getGoalNo() < 3)
                        return new CheckMarketResultedOutcome("2-3");
                    else
                        return new CheckMarketResultedOutcome("4+");
                } else
                    return null;


            case "P:A:OUR": // 1st / 2nd Home Half Total Goals
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()
                                || "H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    winningSelection = selectWinningRangeSelectionForTeam(
                                    this.currentMatchState.getPreviousPeriodGoalsA());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:B:OUR": // 1st / 2nd Half Away Total Goals
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()
                                || "H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    winningSelection = selectWinningRangeSelectionForTeam(
                                    this.currentMatchState.getPreviousPeriodGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTS": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:WFB:H": // Home Team Win from Behind
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && this.currentMatchState.wasTeamBehind(TeamId.A))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:WFB:A": // Away Team Win from Behind
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsB() > this.currentMatchState.getGoalsA()
                                    && this.currentMatchState.wasTeamBehind(TeamId.B))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:LAT:H": // Home Team Lead at Anytime
                if (this.currentMatchState.isMatchCompleted()
                                || this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()) {
                    if (this.currentMatchState.wasTeamLeading(TeamId.A))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:LAT:A": // Away Team Lead at Anytime
                if (this.currentMatchState.isMatchCompleted()
                                || this.currentMatchState.getGoalsB() > this.currentMatchState.getGoalsA()) {
                    if (this.currentMatchState.wasTeamLeading(TeamId.B))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:GBH": // Goal in Both Halves
                if (this.currentMatchState.isMatchCompleted()
                                || this.currentMatchState.isFirstHalfCompleted()
                                                && this.currentMatchState.getFirstHalfGoals().size() == 0
                                || this.currentMatchState.getFirstHalfGoals().size() >= 1
                                                && this.currentMatchState.getSecondHalfGoals().size() >= 1) {
                    if (this.currentMatchState.getFirstHalfGoals().size() >= 1
                                    && this.currentMatchState.getSecondHalfGoals().size() >= 1)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:HWMCR": // Half With Most Corners
                int firstHalfCorners = this.currentMatchState.getFirstHalfCorners().size();
                int secondHalfCorners = this.currentMatchState.getSecondHalfCorners().size();
                if (this.currentMatchState.isMatchCompleted() || firstHalfCorners < secondHalfCorners) {
                    if (firstHalfCorners > secondHalfCorners) {
                        winningSelection = "First Half";
                    } else if (firstHalfCorners < secondHalfCorners) {
                        winningSelection = "Second Half";
                    } else {
                        winningSelection = "X";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:HWMCD": // Half With Most Cards
                int firstHalfCards = this.currentMatchState.getFirstHalfYellowCards().size()
                                + this.currentMatchState.getFirstHalfRedCards().size();
                int secondHalfCards = this.currentMatchState.getSecondHalfYellowCards().size()
                                + this.currentMatchState.getSecondHalfRedCards().size();
                if (this.currentMatchState.isMatchCompleted() || firstHalfCards < secondHalfCards) {
                    if (firstHalfCards > secondHalfCards) {
                        winningSelection = "First Half";
                    } else if (firstHalfCards < secondHalfCards) {
                        winningSelection = "Second Half";
                    } else {
                        winningSelection = "X";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CFBD": // To Come From Behind and Draw
                if (this.currentMatchState.isMatchCompleted()) {
                    if (goalsA == goalsB && goalsA + goalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CFBW": // To Come From Behind and Win
                if (this.currentMatchState.isMatchCompleted()) {
                    if (goalsA > goalsB && this.currentMatchState.wasTeamBehind(TeamId.A)
                                    || goalsB > goalsA && this.currentMatchState.wasTeamBehind(TeamId.B))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CFBWD": // To Come From Behind and Win or Draw
                if (this.currentMatchState.isMatchCompleted()) {
                    if (goalsA >= goalsB && this.currentMatchState.wasTeamBehind(TeamId.A)
                                    || goalsB >= goalsA && this.currentMatchState.wasTeamBehind(TeamId.B))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:LHTL": // Lead at Half Time & Fail to Win
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.isFirstHalfCompleted()
                                && this.currentMatchState.wasTeamFirstHalfLeader(TeamId.UNKNOWN)) {
                    if (goalsA < goalsB && this.currentMatchState.wasTeamFirstHalfLeader(TeamId.A)
                                    || goalsB < goalsA && this.currentMatchState.wasTeamFirstHalfLeader(TeamId.B))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:FGL": // Score First & Fail to Win
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.didTeamScoreFirst(TeamId.A) && goalsA <= goalsB
                                    || this.currentMatchState.didTeamScoreFirst(TeamId.B) && goalsB <= goalsA)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:HTFT": // 1st half Result/2nd half result
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.wasTeamFirstHalfLeader(TeamId.A))
                        winningSelection = "A/";
                    if (this.currentMatchState.wasTeamFirstHalfLeader(TeamId.UNKNOWN))
                        winningSelection = "X/";
                    if (this.currentMatchState.wasTeamFirstHalfLeader(TeamId.B))
                        winningSelection = "B/";
                    if (this.currentMatchState.wasTeamSecondHalfLeader(TeamId.A))
                        winningSelection += "A";
                    if (this.currentMatchState.wasTeamSecondHalfLeader(TeamId.UNKNOWN))
                        winningSelection += "X";
                    if (this.currentMatchState.wasTeamSecondHalfLeader(TeamId.B))
                        winningSelection += "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:FHSHCS": // 1st half Correct Score/2nd half Correct Score
                if (this.currentMatchState.isMatchCompleted()) {
                    int firstHalfGoalsA = goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.A);
                    int firstHalfGoalsB = goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.B);
                    int secondHalfGoalsA = goalScoredForTeam(this.currentMatchState.getSecondHalfGoals(), TeamId.A);
                    int secondHalfGoalsB = goalScoredForTeam(this.currentMatchState.getSecondHalfGoals(), TeamId.B);
                    winningSelection = firstHalfGoalsA + "-" + firstHalfGoalsB + "/" + secondHalfGoalsA + "-"
                                    + secondHalfGoalsB;
                    return new CheckMarketResultedOutcome(winningSelection);
                }
                return null;
            case "FT:FHNTCS": // Score at HT/Score at FT
                if (this.currentMatchState.isMatchCompleted()) {
                    int firstHalfGoalsA = goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.A);
                    int firstHalfGoalsB = goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.B);
                    winningSelection = firstHalfGoalsA + "-" + firstHalfGoalsB + "/"
                                    + this.currentMatchState.getGoalsA() + "-" + this.currentMatchState.getGoalsB();
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    Map<String, Selection> selections = market.getSelections();
                    previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getGoalSequenceId());
                    currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getGoalSequenceId());
                    ArrayList<String> loseSelections = new ArrayList<>();
                    if (previousStateSeqNo < currentStateSeqNo) {
                        if (this.currentMatchState.isFirstHalfCompleted()) {
                            for (String key : selections.keySet()) {
                                String[] scores = key.split("/");
                                String[] finalScores = scores[1].split("-");
                                int finalScoreA = Integer.parseInt(finalScores[0]);
                                int finalScoreB = Integer.parseInt(finalScores[1]);
                                if (finalScoreA < goalsA || finalScoreB < goalsB) {
                                    loseSelections.add(key);
                                }
                            }
                            if (loseSelections != null && loseSelections.size() > 0) {
                                for (String loseSelection : loseSelections) {
                                    selections.remove(loseSelection);
                                }
                                market.setSelections(selections);
                                return new CheckMarketResultedOutcome(loseSelections, false);
                            } else
                                return null;
                        } else {
                            int firstHalfGoalsA =
                                            goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.A);
                            int firstHalfGoalsB =
                                            goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.B);
                            for (String key : selections.keySet()) {
                                String[] scores = key.split("/");
                                String[] halfScores = scores[0].split("-");
                                int halfScoreA = Integer.parseInt(halfScores[0]);
                                int halfScoreB = Integer.parseInt(halfScores[1]);
                                if (halfScoreA < firstHalfGoalsA || halfScoreB < firstHalfGoalsB) {
                                    loseSelections.add(key);
                                }
                            }
                            if (loseSelections != null && loseSelections.size() > 0) {
                                for (String loseSelection : loseSelections) {
                                    selections.remove(loseSelection);
                                }
                                market.setSelections(selections);
                                return new CheckMarketResultedOutcome(loseSelections, false);
                            } else
                                return null;
                        }
                    } else
                        return null;
                }
            case "FT:AXB$OU": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AOver";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "XOver";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BOver";
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AUnder";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "XUnder";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BUnder";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:AXBOOU": // ASFD
                double goalsline = Double.parseDouble(market.getLineId());
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (goalsline))
                        winningSelections.add("Home team to win OR " + String.valueOf(goalsline)
                                        + " more goals to be scored in the match");
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (goalsline))
                        winningSelections.add("Draw OR " + String.valueOf(goalsline)
                                        + " more goals to be scored in the match");
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (goalsline))
                        winningSelections.add("Away team to win OR more than " + String.valueOf(goalsline)
                                        + " goals to be scored in the match");
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (goalsline))
                        winningSelections.add("Home team to win OR less than " + String.valueOf(goalsline)
                                        + " goals to be scored in the match");
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (goalsline))
                        winningSelections.add("Draw OR less than " + String.valueOf(goalsline)
                                        + " goals to be scored in the match");
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    || (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (goalsline))
                        winningSelections.add("Away team to win OR less than " + String.valueOf(goalsline)
                                        + " goals to be scored in the match");
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;
            case "FT:ABOU": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AOver";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BOver";
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AUnder";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BUnder";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:MBTSC": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "AYes";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "XYes";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && (this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "BYes";
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "ANo";
                    if (this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "XNo";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()
                                    && !(this.currentMatchState.getGoalsA() > 0
                                                    && this.currentMatchState.getGoalsB() > 0))
                        winningSelection = "BNo";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTS$OU": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    Double line = Double.valueOf(market.getLineId());

                    if ((this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) > line)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:A2PLUS": // home to win and two more goals half time
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getFirstHalfGoalsA() + this.currentMatchState.getFirstHalfGoalsB() >= 2)
                                    && (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:B2PLUS": // away to win and two more goals half time
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getFirstHalfGoalsA() + this.currentMatchState.getFirstHalfGoalsB() >= 2)
                                    && (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:A2PLUS2": // home to win and two more goals half time
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB() >= 2)
                                    && (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB()))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:B2PLUS2": // away to win and two more goals half time
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB() >= 2)
                                    && (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB()))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OTSOV:3": // One team to score and Over 2.5
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalNo() - 1
                                    || this.currentMatchState.getGoalsB() == this.currentMatchState.getGoalNo() - 1)
                                    && (this.currentMatchState.getGoalsA()
                                                    + this.currentMatchState.getGoalsB()) > (2.5))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OTSUD:3": // One team to score and Over 2.5
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getGoalsA() == this.currentMatchState.getGoalNo() - 1
                                    || this.currentMatchState.getGoalsB() == this.currentMatchState.getGoalNo() - 1)
                                    && (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) < (3))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:OU": // total goals in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "P:A:OU": // total goals in current half home
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "P:B:OU": // total goals in current half away
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "P:SPRD": // goals handicap in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    - this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }

            case "P:AXB": // goals handicap in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    - this.currentMatchState.getPreviousPeriodGoalsB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

            case "P:A:WTN": // period win to nil A
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int a = this.currentMatchState.getPreviousPeriodGoalsA();
                    int b = this.currentMatchState.getPreviousPeriodGoalsB();
                    if (a > b && b == 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

            case "P:B:WTN": // period win to nil A
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int a = this.currentMatchState.getPreviousPeriodGoalsA();
                    int b = this.currentMatchState.getPreviousPeriodGoalsB();
                    if (a < b && a == 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

            case "P:CS": // goals correct score in current half
            case "P:CSDS": // goals correct score in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getPreviousPeriodGoalsA(),
                                    this.currentMatchState.getPreviousPeriodGoalsB());
                } else if (currentPeriodSeqNo > periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodGoalsA(),
                                    this.currentMatchState.getCurrentPeriodGoalsB());
                } else {
                    return null;
                }

                switch (winningSelection) {
                    case "0-0":
                    case "0-1":
                    case "0-2":
                    case "1-0":
                    case "1-1":
                    case "1-2":
                    case "2-0":
                    case "2-1":
                    case "2-2":
                        return new CheckMarketResultedOutcome(winningSelection);
                    default:
                        return new CheckMarketResultedOutcome("Any Other");
                }

            case "P:CCS": // goals correct score in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getPreviousPeriodCornersA(),
                                    this.currentMatchState.getPreviousPeriodCornersB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (currentPeriodSeqNo > periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodCornersA(),
                                    this.currentMatchState.getCurrentPeriodCornersB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }
            case "FT:CAXB:ROB":
            case "FT:CAXB": // Corner Match winner
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA() - this.currentMatchState.getCornersB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "C:AXB1": // Corner Match winner
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodCornersA()
                                    - this.currentMatchState.getPreviousPeriodCornersB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }
            case "P:CSPRD": // goals handicap in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodCornersA()
                                    - this.currentMatchState.getPreviousPeriodCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "P:COU": // total goals in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodCornersA()
                                    + this.currentMatchState.getPreviousPeriodCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "FT:COU": // Corner Match winner
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA() + this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:ACOU": // Asian Corner Match winner
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA() + this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionAouMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:A:CTOT": // Corner Match winner
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:A:COU": // Corner A Totals
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:B:COU": // Corner B Totals
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:CHCP": // Corner Match winner
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = this.currentMatchState.getCornersA() - this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:CNC": // next goal
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getCornerSequenceId());
                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getCornerSequenceId());
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastCorner() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    LoggerFactory.getLogger(FootballMatchResultMarkets.class)
                                    .info("Next corner market could not know who scored the corner for " + marketSeqNo);

                    return new CheckMarketResultedOutcome();
                }

            case "FT:A:BOU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = (this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB()) * 10
                                    + (this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB())
                                                    * 25;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:BCOU": // TOTAL CARDS
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB()
                                    + this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:A:BTOT": // TOTAL CARDS TEAM A
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getRedCardsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:B:BTOT": // TOTAL CARDS TEAM B
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getYellowCardsB() + this.currentMatchState.getRedCardsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:BCSPRD": // match handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = (this.currentMatchState.getYellowCardsA() - this.currentMatchState.getYellowCardsB())
                                    + (this.currentMatchState.getRedCardsA() - this.currentMatchState.getRedCardsB());
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:A:BYOU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getYellowCardsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:B:BYOU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getYellowCardsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:BSPRD": // match handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = (this.currentMatchState.getYellowCardsA() - this.currentMatchState.getYellowCardsB()) * 10
                                    + (this.currentMatchState.getRedCardsA() - this.currentMatchState.getRedCardsB())
                                                    * 25;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:BAXB": // MOST BOOKINGS
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = (this.currentMatchState.getYellowCardsA() - this.currentMatchState.getYellowCardsB()) * 10
                                    + (this.currentMatchState.getRedCardsA() - this.currentMatchState.getRedCardsB())
                                                    * 25;
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BOU": // MOST BOOKINGS
                if (this.currentMatchState.isMatchCompleted()) {

                    int n = (this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB()) * 10
                                    + (this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB())
                                                    * 25;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:BRED": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB()) > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:AHCP": // Asian handcap
                if (this.currentMatchState.isMatchCompleted()) {
                    String sequenceId = market.getSequenceId();
                    String s = sequenceId.substring(1, sequenceId.length());
                    String[] index = s.split("-");

                    int ahcpLine = Integer.parseInt(index[0]) - Integer.parseInt(index[1]);
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    String line = market.getLineId();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionAhcpMarket(n - ahcpLine, line);
                    return new CheckMarketResultedOutcome(winningSelection, n - ahcpLine);
                } else
                    return null;
                /**
                 * Jin work start
                 **/
            case "FT:FC": // both team to score
                if (this.currentMatchState.isMatchCompleted()
                                || this.currentMatchState.getFirstCorner() != TeamId.UNKNOWN) {

                    if (this.currentMatchState.getFirstCorner().equals(TeamId.A))
                        winningSelection = "A";
                    else if (this.currentMatchState.getFirstCorner().equals(TeamId.B))
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CEHCP": // match handicap EURO
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getCornersA() - this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "P:CMC": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getCurrentPeriodCornersA()
                                    - this.currentMatchState.getCurrentPeriodCornersB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }
            case "P:CEHCP": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getCornersA() - this.currentMatchState.getCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }

            case "P:A:COU": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodCornersA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }

            case "P:B:COU": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodCornersB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    return null;
                }
            case "P:CAXB:ROB":
            case "P:CAXB": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getCurrentPeriodCornersA()
                                    - this.currentMatchState.getCurrentPeriodCornersB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

                /**
                 * new working section
                 **/
            case "P:BTS": // half time both team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (currentPeriodSeqNo > periodSeqNo) {
                    if (this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

            case "P:AXB$OU": // half time both team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AOver";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "XOver";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BOver";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "AUnder";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "XUnder";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "BUnder";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:AXB$BTS": // half time both team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "AY";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "DY";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "BY";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && !(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "AN";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() == this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && !(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "DN";
                    if (this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                    .getPreviousPeriodGoalsB()
                                    && !(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0))
                        winningSelection = "BN";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:BTSOU": // half time both team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    if ((this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "YOver";
                    if ((this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "YUnder";
                    if (!(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) > (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "NOver";
                    if (!(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && (this.currentMatchState.getPreviousPeriodGoalsA()
                                                    + this.currentMatchState.getPreviousPeriodGoalsB()) < (Double
                                                                    .valueOf(market.getLineId())))
                        winningSelection = "NUnder";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:DBLC": // half time Double chance
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    matchWinner = this.currentMatchState.getCurrentPeriodMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                winningSelections.add("AB");
                                winningSelections.add("AX");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case B:
                                winningSelections.add("AB");
                                winningSelections.add("BX");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case UNKNOWN:
                                winningSelections.add("AX");
                                winningSelections.add("BX");
                                return new CheckMarketResultedOutcome(winningSelections);
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;

            case "P:DNB": // half time Draw no bet
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    matchWinner = this.currentMatchState.getCurrentPeriodMatchWinner();
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

            case "P:HNB": // half time Draw no bet
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    matchWinner = this.currentMatchState.getCurrentPeriodMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("Void");
                            case B:
                                return new CheckMarketResultedOutcome("B");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("X");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;

            case "P:ANB": // half time Draw no bet
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    matchWinner = this.currentMatchState.getCurrentPeriodMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                return new CheckMarketResultedOutcome("A");
                            case B:
                                return new CheckMarketResultedOutcome("Void");
                            case UNKNOWN:
                                return new CheckMarketResultedOutcome("X");
                            default:
                                break;
                        }
                    }
                    return null;
                } else
                    return null;

            case "P:3HCP": // half time match handicap EURO
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    - this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:DBLC$BTS": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {

                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXYes");
                        winningSelections.add("BXYes");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("AXYes");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("BXYes");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXNo");
                        winningSelections.add("BXNo");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("AXNo");
                    }
                    if (!(this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0)
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("BXNo");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;
            case "FT:DBLCRNG": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {
                    int goalA = this.currentMatchState.getGoalsA();
                    int goalB = this.currentMatchState.getGoalsB();
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (goalA + goalB == 2 || goalA + goalB == 3) {
                        switch (matchWinner) {
                            case A:
                                winningSelections.add("AB2-3");
                                winningSelections.add("AX2-3");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case B:
                                winningSelections.add("AB2-3");
                                winningSelections.add("BX2-3");
                                return new CheckMarketResultedOutcome(winningSelections);
                            case UNKNOWN:
                                winningSelections.add("AX2-3");
                                winningSelections.add("BX2-3");
                                return new CheckMarketResultedOutcome(winningSelections);
                            default:
                                break;
                        }
                    }
                    return new CheckMarketResultedOutcome();// resulting
                } else
                    return null;
            case "FT:DBLC$OU": // both team to score
                if (this.currentMatchState.isMatchCompleted()) {

                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) > (Double
                                    .valueOf(market.getLineId())) && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) > (Double
                                    .valueOf(market.getLineId())) && TeamId.A == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("AXOver");
                    }
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) > (Double
                                    .valueOf(market.getLineId())) && TeamId.B == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) < (Double
                                    .valueOf(market.getLineId())) && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXUnder");
                        winningSelections.add("XBUnder");
                    }
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) < (Double
                                    .valueOf(market.getLineId())) && TeamId.A == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("AXUnder");
                    }
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB()) < (Double
                                    .valueOf(market.getLineId())) && TeamId.B == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("XBUnder");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;
            case "P:DBLCBTS": // both team to score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {

                    matchWinner = this.currentMatchState.getPreviousPeriodMatchWinner();
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXYes");
                        winningSelections.add("BXYes");
                    }
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("AXYes");
                    }
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("BXYes");
                    }
                    if (!(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXNo");
                        winningSelections.add("BXNo");
                    }
                    if (!(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("AXNo");
                    }
                    if (!(this.currentMatchState.getPreviousPeriodGoalsA() > 0
                                    && this.currentMatchState.getPreviousPeriodGoalsB() > 0)
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("BXNo");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else if (currentPeriodSeqNo > periodSeqNo) {

                    matchWinner = this.currentMatchState.getCurrentPeriodMatchWinner();
                    if (this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXYes");
                        winningSelections.add("BXYes");
                    }
                    if (this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0 && TeamId.A == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("AXYes");
                    }
                    if (this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0 && TeamId.B == matchWinner) {
                        winningSelections.add("ABYes");
                        winningSelections.add("BXYes");
                    }
                    if (!(this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0)
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXNo");
                        winningSelections.add("BXNo");
                    }
                    if (!(this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0)
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("AXNo");
                    }
                    if (!(this.currentMatchState.getCurrentPeriodGoalsA() > 0
                                    && this.currentMatchState.getCurrentPeriodGoalsB() > 0)
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABNo");
                        winningSelections.add("BXNo");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;

            case "FT:HSH": // Highest score half

                if (this.currentMatchState.isMatchCompleted()) {
                    int a = this.currentMatchState.getFirstHalfGoalsA();
                    int b = this.currentMatchState.getFirstHalfGoalsB();
                    int c = this.currentMatchState.getSecondHalfGoalsA();
                    int d = this.currentMatchState.getSecondHalfGoalsB();

                    if ((a + b) > (c + d)) {
                        return new CheckMarketResultedOutcome("H1");
                    } else if ((a + b) == (c + d)) {
                        return new CheckMarketResultedOutcome("Equal");
                    } else {
                        return new CheckMarketResultedOutcome("H2");
                    }
                } else
                    return null;

            case "FT:NTS": // Number of teams to score
                if (goalsA > 0 && goalsB > 0)
                    return new CheckMarketResultedOutcome("2");
                if (this.currentMatchState.isMatchCompleted()) {

                    if ((goalsA > 0 && goalsB == 0) || (goalsA == 0 && goalsB > 0))
                        return new CheckMarketResultedOutcome("1");
                    if (goalsA == 0 && goalsB == 0)
                        return new CheckMarketResultedOutcome("0");
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
            case "P:OE": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    int pointtTotal = goalCurrentHalfA + goalCurrentHalfB;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:A:OE": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    int pointtTotal = goalCurrentHalfA;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:B:OE": //
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                if (previousPeriodSeqNo == periodSeqNo) {
                    int pointtTotal = goalCurrentHalfB;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:ATG": // total goals in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:ATG": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "P:AHCP": // goals handicap in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    - this.currentMatchState.getPreviousPeriodGoalsB();
                    String line = market.getLineId();
                    int ahcpLine = 0;// market.getAhcpLine();
                    // line = line.substring(4);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionAhcpMarket(n - ahcpLine, line);
                    return new CheckMarketResultedOutcome(winningSelection, n - ahcpLine);
                } else
                    return null;

            case "FT:A:HSH": // Highest score half
                if (this.currentMatchState.isMatchCompleted()) {
                    if (goalFirstHalfA > goalSecondHalfA)
                        return new CheckMarketResultedOutcome("First Half");
                    else if (goalFirstHalfA == goalSecondHalfA)
                        return new CheckMarketResultedOutcome("X");
                    else if (goalFirstHalfA < goalSecondHalfA)
                        return new CheckMarketResultedOutcome("Second Half");
                } else
                    return null;

            case "FT:B:HSH": // Highest score half
                if (this.currentMatchState.isMatchCompleted()) {
                    if (goalFirstHalfB > goalSecondHalfB)
                        return new CheckMarketResultedOutcome("First Half");
                    else if (goalFirstHalfB == goalSecondHalfB)
                        return new CheckMarketResultedOutcome("X");
                    else if (goalFirstHalfB < goalSecondHalfB)
                        return new CheckMarketResultedOutcome("Second Half");
                } else
                    return null;

            case "FT:A:CLS": // homw clean sheet
                if (this.currentMatchState.getGoalsB() > 0)
                    return new CheckMarketResultedOutcome("No");
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsB() == 0
                                    && this.currentMatchState.getGoalsA() >= this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:B:CLS": // away clean sheet
                if (this.currentMatchState.getGoalsA() > 0)
                    return new CheckMarketResultedOutcome("No");
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() == 0
                                    && this.currentMatchState.getGoalsA() <= this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:A:BH": //

                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo >= 2) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() <= this.currentMatchState
                                    .getPreviousPeriodGoalsB())
                        return new CheckMarketResultedOutcome("No");
                    else if (this.currentMatchState.isMatchCompleted()) {
                        if ((goalsA - goalCurrentHalfA) > (goalsB - goalCurrentHalfB))
                            return new CheckMarketResultedOutcome("Yes");
                        else
                            return new CheckMarketResultedOutcome("No");
                    }
                }

                if (this.currentMatchState.isMatchCompleted()
                                && this.previousMatchState.getMatchPeriod().equals(FootballMatchPeriod.PREMATCH)) {

                    PairOfIntegers firstHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[0];
                    PairOfIntegers secondHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[1];

                    if ((firstHalfScore.A > firstHalfScore.B) && secondHalfScore.A > secondHalfScore.B) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else
                        return new CheckMarketResultedOutcome("No");
                }
            case "FT:B:BH": //

                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo >= 2) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() >= this.currentMatchState
                                    .getPreviousPeriodGoalsB())
                        return new CheckMarketResultedOutcome("No");
                    else if (this.currentMatchState.isMatchCompleted()) {
                        if ((goalsA - goalCurrentHalfA) < (goalsB - goalCurrentHalfB))
                            return new CheckMarketResultedOutcome("Yes");
                        else
                            return new CheckMarketResultedOutcome("No");
                    }
                }

                if (this.currentMatchState.isMatchCompleted()
                                && this.previousMatchState.getMatchPeriod().equals(FootballMatchPeriod.PREMATCH)) {

                    PairOfIntegers firstHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[0];
                    PairOfIntegers secondHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[1];

                    if ((firstHalfScore.B > firstHalfScore.A) && secondHalfScore.B > secondHalfScore.A) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else
                        return new CheckMarketResultedOutcome("No");
                }

            case "FT:A:EH": //

                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo >= 2) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() > this.currentMatchState
                                    .getPreviousPeriodGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.isMatchCompleted()) {
                        if ((goalsA - goalCurrentHalfA) > (goalsB - goalCurrentHalfB))
                            return new CheckMarketResultedOutcome("Yes");
                        else
                            return new CheckMarketResultedOutcome("No");
                    }
                }

                if (this.currentMatchState.isMatchCompleted()
                                && this.previousMatchState.getMatchPeriod().equals(FootballMatchPeriod.PREMATCH)) {

                    PairOfIntegers firstHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[0];
                    PairOfIntegers secondHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[1];

                    if ((firstHalfScore.A > firstHalfScore.B) || secondHalfScore.A > secondHalfScore.B) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else
                        return new CheckMarketResultedOutcome("No");
                }
                return null;

            case "FT:B:EH": //

                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo >= 2) {
                    if (this.currentMatchState.getPreviousPeriodGoalsA() < this.currentMatchState
                                    .getPreviousPeriodGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else if (this.currentMatchState.isMatchCompleted()) {
                        if ((goalsA - goalCurrentHalfA) < (goalsB - goalCurrentHalfB))
                            return new CheckMarketResultedOutcome("Yes");
                        else
                            return new CheckMarketResultedOutcome("No");
                    }
                }

                if (this.currentMatchState.isMatchCompleted()
                                && this.previousMatchState.getMatchPeriod().equals(FootballMatchPeriod.PREMATCH)) {

                    PairOfIntegers firstHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[0];
                    PairOfIntegers secondHalfScore = this.currentMatchState.getGoalScoreInPeriodN()[1];

                    if ((firstHalfScore.B > firstHalfScore.A) || secondHalfScore.B > secondHalfScore.A) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else
                        return new CheckMarketResultedOutcome("No");
                }
                return null;
            case "FT:RTC":
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                return checkMktRaceToCorners(marketSeqNo);

            case "FT:RTCTW":
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                return checkMktRaceToCornersTwoWay(marketSeqNo);
            /*** new working section ends ****/

            case "FT:OTYN": // extra time winner
                if (this.currentMatchState.isMatchCompleted()) {
                    matchWinner = this.currentMatchState.getMatchWinner();
                    FootballMatchPeriod lastPeriod = this.previousMatchState.getMatchPeriod();
                    if (lastPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                                    || lastPeriod.equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF))
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");

                } else
                    return null;

                /** Jin work section start */
            case "OT:AXB": //
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());

                switch (currentPeriodSeqNo) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return null;
                    case 5: // penalty
                        winningSelection = "X";
                        new CheckMarketResultedOutcome(winningSelection);
                    case 6:
                        if ((previousPeriodSeqNo == 3 || previousPeriodSeqNo == 4)
                                        && this.currentMatchState.isMatchCompleted())// entered
                                                                                     // extra
                                                                                     // time
                        {
                            int n = this.currentMatchState.getCurrentPeriodCornersA()
                                            - this.currentMatchState.getCurrentPeriodCornersB();
                            if (n > 0)
                                winningSelection = "A";
                            else if (n < 0)
                                winningSelection = "B";
                            else
                                winningSelection = "X";
                            return new CheckMarketResultedOutcome(winningSelection);
                        } else
                            return null;// never entered extra time
                    default:
                        break;
                }

            case "OT:OU": // total goals in current half
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                if (!((FootballMatchState) currentMatchState).isExtraTimeMarketReadyToResult()) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                int n = this.currentMatchState.getExtraTimeFHGoalsA() + this.currentMatchState.getExtraTimeFHGoalsB()
                                + this.currentMatchState.getExtraTimeSHGoalsA()
                                + this.currentMatchState.getExtraTimeSHGoalsB();
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

            case "ET:CS": // correct score
                if (((FootballMatchState) currentMatchState).isExtraTimeMarketReadyToResult()) {
                    winningSelection = String.format("%d-%d",
                                    this.currentMatchState.getExtraTimeFHGoalsA()
                                                    + this.currentMatchState.getExtraTimeSHGoalsA(),
                                    this.currentMatchState.getExtraTimeFHGoalsB()
                                                    + this.currentMatchState.getExtraTimeSHGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "OTHT:AXB": // goals handicap in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 3) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (previousPeriodSeqNo == periodSeqNo) {
                    n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    - this.currentMatchState.getPreviousPeriodGoalsB();
                    if (n > 0)
                        winningSelection = "A";
                    else if (n < 0)
                        winningSelection = "B";
                    else
                        winningSelection = "X";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return null;
                }

            case "OTHT:OU": // total goals in current half
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                if (!((FootballMatchState) currentMatchState).isExtraTimeMarketReadyToResult()) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                n = this.currentMatchState.getPreviousPeriodGoalsA() + this.currentMatchState.getPreviousPeriodGoalsB();
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, n);

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
                TeamId teamTo3Goals = this.currentMatchState.checkTeamToXGoals(3, 0);

                if (teamTo3Goals == null && this.currentMatchState.isMatchCompleted())
                    winningSelection = "Neither";
                else if (teamTo3Goals == null)
                    return null;
                else if (teamTo3Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamTo3Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:FTSC": // FIRST TEAM SCORES
                teamTo3Goals = this.currentMatchState.checkFirstTeamScored();

                if (teamTo3Goals == TeamId.UNKNOWN && !this.currentMatchState.isMatchCompleted())
                    return null;
                else if (teamTo3Goals.equals(TeamId.UNKNOWN))
                    winningSelection = "None";
                else if (teamTo3Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamTo3Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            case "P:1HFTSC": // FIRST TEAM SCORES
                if (this.currentMatchState.getMatchPeriod() == FootballMatchPeriod.AT_HALF_TIME) {
                    teamTo3Goals = this.currentMatchState.checkFirstTeamScored();

                    if (teamTo3Goals.equals(TeamId.UNKNOWN))
                        winningSelection = "None";
                    else if (teamTo3Goals.equals(TeamId.A))
                        winningSelection = "A";
                    else if (teamTo3Goals.equals(TeamId.B))
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                }

            case "FT:LTS": // FIRST TEAM SCORES
                teamTo3Goals = this.currentMatchState.checkLastTeamScored();

                if (!this.currentMatchState.isMatchCompleted())
                    return null;
                else if (teamTo3Goals.equals(TeamId.UNKNOWN))
                    winningSelection = "None";
                else if (teamTo3Goals.equals(TeamId.A))
                    winningSelection = "A";
                else if (teamTo3Goals.equals(TeamId.B))
                    winningSelection = "B";

                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:WTN:H": // homw win to nil
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsB() == 0
                                    && this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else if (this.currentMatchState.getGoalsB() > 0) {
                    return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:AWN": // home win to nil
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsB() == 0
                                    && this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else if (this.currentMatchState.getGoalsB() > 0) {
                    return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:BWN": // Away win to nil
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() == 0
                                    && this.currentMatchState.getGoalsB() > this.currentMatchState.getGoalsA())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else if (this.currentMatchState.getGoalsA() > 0) {
                    return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:A:TW23": // home win score 2 or 3
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() == 2
                                    || this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() == 3)
                                    && this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:B:TW23": // away win score 2 or 3
                if (this.currentMatchState.isMatchCompleted()) {
                    if ((this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() == 2
                                    || this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() == 3)
                                    && this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:WNTN:H": // home win not to nil
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsB() != 0
                                    && this.currentMatchState.getGoalsB() < this.currentMatchState.getGoalsA())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:WNTN:A": // away win not to nil
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() != 0
                                    && this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB())
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:H1G": // half of first goal
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getGoalNo() > 1) {
                    if (this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB() == 0)
                        return new CheckMarketResultedOutcome();
                    else if (this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB() > 0)
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in first half");
                    else
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in second half");
                } else
                    return null;

            case "FT:A:H1G": // half of first goal A
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getGoalsA() > 0) {
                    if (this.currentMatchState.getGoalsA() == 0)
                        return new CheckMarketResultedOutcome();
                    else if (this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB() > 0)
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in first half");
                    else
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in second half");
                } else
                    return null;

            case "FT:B:H1G": // half of first goal B
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getGoalsB() > 0) {
                    if (this.currentMatchState.getGoalsB() == 0)
                        return new CheckMarketResultedOutcome();
                    else if (this.currentMatchState.getFirstHalfGoalsB() > 0)
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in first half");
                    else
                        return new CheckMarketResultedOutcome(
                                        "The first goal in the match will be scored in second half");
                } else
                    return null;

            case "FT:TOFG": // time of first goal
                if (this.currentMatchState.getGoalNo() > 1 || this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.checkTimeFirstGoal(TeamId.UNKNOWN, 10)) {
                        case 0:
                            winningSelection = "0-10";
                            break;
                        case 1:
                            winningSelection = "11-20";
                            break;
                        case 2:
                            winningSelection = "21-30";
                            break;
                        case 3:
                            winningSelection = "31-40";
                            break;
                        case 4:
                            winningSelection = "41-50";
                            break;
                        case 5:
                            winningSelection = "51-60";
                            break;
                        case 6:
                            winningSelection = "61-70";
                            break;
                        case 7:
                            winningSelection = "71-80";
                            break;
                        case 8:
                            winningSelection = "81-FT";
                            break;
                        case 9:
                            winningSelection = "No Goal";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:TOFG:H": // time of first goal
                if (this.currentMatchState.getGoalsA() > 0 || this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.checkTimeFirstGoal(TeamId.A, 10)) {
                        case 0:
                            winningSelection = "0-10";
                            break;
                        case 1:
                            winningSelection = "11-20";
                            break;
                        case 2:
                            winningSelection = "21-30";
                            break;
                        case 3:
                            winningSelection = "31-40";
                            break;
                        case 4:
                            winningSelection = "41-50";
                            break;
                        case 5:
                            winningSelection = "51-60";
                            break;
                        case 6:
                            winningSelection = "61-70";
                            break;
                        case 7:
                            winningSelection = "71-80";
                            break;
                        case 8:
                            winningSelection = "81-91";
                            break;
                        case 9:
                            winningSelection = "No Goal";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:TOFG:A": // time of first goal
                if (this.currentMatchState.getGoalsB() > 0 || this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.checkTimeFirstGoal(TeamId.B, 10)) {
                        case 0:
                            winningSelection = "0-10";
                            break;
                        case 1:
                            winningSelection = "11-20";
                            break;
                        case 2:
                            winningSelection = "21-30";
                            break;
                        case 3:
                            winningSelection = "31-40";
                            break;
                        case 4:
                            winningSelection = "41-50";
                            break;
                        case 5:
                            winningSelection = "51-60";
                            break;
                        case 6:
                            winningSelection = "61-70";
                            break;
                        case 7:
                            winningSelection = "71-80";
                            break;
                        case 8:
                            winningSelection = "81-91";
                            break;
                        case 9:
                            winningSelection = "No Goal";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:HTTS": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsA() > 0) {
                    if (this.currentMatchState.getGoalsA() != 0)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:ATTS": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsB() > 0) {
                    if (this.currentMatchState.getGoalsB() != 0)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:HTTS2": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsA() > 1) {
                    if (this.currentMatchState.getGoalsA() > 1)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:ATTS2": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsB() > 1) {
                    if (this.currentMatchState.getGoalsB() > 1)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:HTTS3": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsA() > 2) {
                    if (this.currentMatchState.getGoalsA() > 2)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:ATTS3": // home to score
                if (this.currentMatchState.isMatchCompleted() || this.currentMatchState.getNormalTimeGoalsB() > 2) {
                    if (this.currentMatchState.getGoalsB() > 2)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:A:TTS2H": // home to score BOTH HALVES
                if (this.currentMatchState.isMatchCompleted() || (this.currentMatchState.isFirstHalfCompleted()
                                && goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.A) == 0)) {
                    if (!this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome("No");
                    if (this.currentMatchState.getGoalsA() > 2)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:B:TTS2H": // AWAY to score BOTH HALVES
                if (this.currentMatchState.isMatchCompleted() || (this.currentMatchState.isFirstHalfCompleted()
                                && goalScoredForTeam(this.currentMatchState.getFirstHalfGoals(), TeamId.B) == 0)) {
                    if (!this.currentMatchState.isMatchCompleted())
                        return new CheckMarketResultedOutcome("No");
                    if (this.currentMatchState.getGoalsB() > 2)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:HTTS1H": // home to score EITHER HALVES
                if (this.currentMatchState.isMatchCompleted() || (this.currentMatchState.getNormalTimeGoalsA() > 0)) {
                    if (this.currentMatchState.getGoalsA() > 0)
                        return new CheckMarketResultedOutcome("A");
                    else
                        return new CheckMarketResultedOutcome("B");
                } else
                    return null;

            case "FT:ATTS1H": // AWAY to score EITHER HALVES
                if (this.currentMatchState.isMatchCompleted() || (this.currentMatchState.getNormalTimeGoalsB() > 0)) {
                    if (this.currentMatchState.getGoalsB() > 0)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else
                    return null;

            case "FT:TOFBR": // time of first booking
                if (this.currentMatchState.getFirstCardTimeSlot() != -1 || this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getFirstCardTimeSlot()) {
                        case 0:
                            winningSelection = "0-10";
                            break;
                        case 1:
                            winningSelection = "11-20";
                            break;
                        case 2:
                            winningSelection = "21-30";
                            break;
                        case 3:
                            winningSelection = "31-40";
                            break;
                        case 4:
                            winningSelection = "41-50";
                            break;
                        case 5:
                            winningSelection = "51-60";
                            break;
                        case 6:
                            winningSelection = "61-70";
                            break;
                        case 7:
                            winningSelection = "71-80";
                            break;
                        case 8:
                            winningSelection = "81-91";
                            break;
                        case -1:
                            winningSelection = "No Card";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CFTSC": // time of first corner
                if (this.currentMatchState.getFirstCornerTimeSlot() != -1
                                || this.currentMatchState.isMatchCompleted()) {
                    switch (this.currentMatchState.getFirstCornerTimeSlot()) {
                        case 0:
                            winningSelection = "0-10";
                            break;
                        case 1:
                            winningSelection = "11-20";
                            break;
                        case 2:
                            winningSelection = "21-30";
                            break;
                        case 3:
                            winningSelection = "31-40";
                            break;
                        case 4:
                            winningSelection = "41-50";
                            break;
                        case 5:
                            winningSelection = "51-60";
                            break;
                        case 6:
                            winningSelection = "61-70";
                            break;
                        case 7:
                            winningSelection = "71-80";
                            break;
                        case 8:
                            winningSelection = "81-91";
                            break;
                        case -1:
                            winningSelection = "No Corner";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:A:TW23": // 1st / 2nd Half Total Goals
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    int goalAfirstH = this.currentMatchState.getFirstHalfGoalsA();
                    int goalBfirstH = this.currentMatchState.getFirstHalfGoalsB();
                    if (goalAfirstH > goalBfirstH && (goalAfirstH + goalBfirstH == 2 || goalAfirstH + goalBfirstH == 3))
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    int goalA2H = this.currentMatchState.getSecondHalfGoalsA();
                    int goalB2H = this.currentMatchState.getSecondHalfGoalsB();
                    if (goalA2H > goalB2H && (goalA2H + goalB2H == 2 || goalA2H + goalB2H == 3))
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");

                } else
                    return null;

            case "P:B:TW23": // 1st / 2nd Half Total Goals
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    int goalAfirstH = this.currentMatchState.getFirstHalfGoalsA();
                    int goalBfirstH = this.currentMatchState.getFirstHalfGoalsB();
                    if (goalAfirstH < goalBfirstH && (goalAfirstH + goalBfirstH == 2 || goalAfirstH + goalBfirstH == 3))
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {
                    int goalA2H = this.currentMatchState.getSecondHalfGoalsA();
                    int goalB2H = this.currentMatchState.getSecondHalfGoalsB();
                    if (goalA2H < goalB2H && (goalA2H + goalB2H == 2 || goalA2H + goalB2H == 3))
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");

                } else
                    return null;

            case "FT:BTSOOU":
                if (this.currentMatchState.isMatchCompleted()) {
                    int goalA = this.currentMatchState.getGoalsA();
                    int goalB = this.currentMatchState.getGoalsB();
                    double line = Double.valueOf(market.getLineId());
                    if ((goalA > 0 && goalB > 0) || (goalA + goalB >= line))
                        winningSelections.add("GGorOV");
                    if (!(goalA > 0 && goalB > 0) || (goalA + goalB < line))
                        winningSelections.add("NGorUN");
                    if (!(goalA > 0 && goalB > 0) || (goalA + goalB >= line))
                        winningSelections.add("NGorOV");
                    if ((goalA > 0 && goalB > 0) || (goalA + goalB < line))
                        winningSelections.add("GGorUN");
                    return new CheckMarketResultedOutcome(winningSelections);



                } else
                    return null;

            case "FT:BTSAOU":
                if (this.currentMatchState.isMatchCompleted()) {
                    int goalA = this.currentMatchState.getGoalsA();
                    int goalB = this.currentMatchState.getGoalsB();
                    double line = Double.valueOf(market.getLineId());
                    if ((goalA > 0 && goalB > 0) && (goalA + goalB >= line))
                        return new CheckMarketResultedOutcome(
                                        "Both teams to score and X or more goals to be scored in the match");
                    if (!(goalA > 0 && goalB > 0) && (goalA + goalB >= line))
                        return new CheckMarketResultedOutcome(
                                        "Not both teams to score and X or more goals to be scored in the match");
                    if ((goalA > 0 && goalB > 0) && (goalA + goalB < line))
                        return new CheckMarketResultedOutcome(
                                        "Both teams to score and X or less goals to be scored in the match");
                    if (!(goalA > 0 && goalB > 0) && (goalA + goalB < line))
                        return new CheckMarketResultedOutcome(
                                        "Not both teams to score and less than X goals to be scored in the match");
                    else
                        return new CheckMarketResultedOutcome("No");

                } else
                    return null;

            case "P:DBLCOU": // double chance and OU 1.5
                if ("H1".equals(market.getSequenceId()) && this.currentMatchState.isFirstHalfCompleted()) {
                    if (this.currentMatchState.getFirstHalfGoalsA() > this.currentMatchState.getFirstHalfGoalsB())
                        matchWinner = TeamId.A;
                    else if (this.currentMatchState.getFirstHalfGoalsA() < this.currentMatchState.getFirstHalfGoalsB())
                        matchWinner = TeamId.B;
                    else
                        matchWinner = TeamId.UNKNOWN;
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("AXOver");
                    }
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXUnder");
                        winningSelections.add("XBUnder");
                    }
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("AXUnder");
                    }
                    if ((this.currentMatchState.getFirstHalfGoalsA()
                                    + this.currentMatchState.getFirstHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("XBUnder");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else if ("H2".equals(market.getSequenceId()) && this.currentMatchState.isMatchCompleted()) {

                    if (this.currentMatchState.getSecondHalfGoalsA() > this.currentMatchState.getSecondHalfGoalsB())
                        matchWinner = TeamId.A;
                    else if (this.currentMatchState.getSecondHalfGoalsA() < this.currentMatchState
                                    .getSecondHalfGoalsB())
                        matchWinner = TeamId.B;
                    else
                        matchWinner = TeamId.UNKNOWN;

                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("AXOver");
                    }
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) > (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABOver");
                        winningSelections.add("XBOver");
                    }
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.UNKNOWN == matchWinner) {
                        winningSelections.add("AXUnder");
                        winningSelections.add("XBUnder");
                    }
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.A == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("AXUnder");
                    }
                    if ((this.currentMatchState.getSecondHalfGoalsA()
                                    + this.currentMatchState.getSecondHalfGoalsB()) < (Double
                                                    .valueOf(market.getLineId()))
                                    && TeamId.B == matchWinner) {
                        winningSelections.add("ABUnder");
                        winningSelections.add("XBUnder");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else
                    return null;

                /** Jin work section ends */


                /*** Chaff work section starts ***/
                /*** Chaff work section ends ***/

            case "FT:BTTS1HH":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA > firstHalfGoalsB && firstHalfGoalsA > 0 && firstHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTTS1HA":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA < firstHalfGoalsB && firstHalfGoalsA > 0 && firstHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTTS1HX":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA == firstHalfGoalsB && firstHalfGoalsA > 0 && firstHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTNTS1HH":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA > firstHalfGoalsB && (firstHalfGoalsA == 0 || firstHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTNTS1HA":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA < firstHalfGoalsB && (firstHalfGoalsA == 0 || firstHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTNTS1HX":
                if (this.currentMatchState.isFirstHalfCompleted()) {
                    int firstHalfGoalsA = this.currentMatchState.getFirstHalfGoalsA();
                    int firstHalfGoalsB = this.currentMatchState.getFirstHalfGoalsB();
                    if (firstHalfGoalsA == firstHalfGoalsB && (firstHalfGoalsA == 0 || firstHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:BTTS2HH":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    // int goalsA = this.currentMatchState.getGoalsA();
                    // int goalsB = this.currentMatchState.getGoalsB(); //match goals

                    if (goalsA > goalsB && secondHalfGoalsA > 0 && secondHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTTS2HA":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    if (goalsA < goalsB && secondHalfGoalsA > 0 && secondHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:BTTS2HX":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    if (goalsA == goalsB && secondHalfGoalsA > 0 && secondHalfGoalsB > 0)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:BTNTS2HH":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    if (goalsA > goalsB && (secondHalfGoalsA == 0 || secondHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTNTS2HA":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    if (goalsA < goalsB && (secondHalfGoalsA == 0 || secondHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:BTNTS2HX":
                if (this.currentMatchState.isMatchCompleted()) {
                    int secondHalfGoalsA = this.currentMatchState.getSecondHalfGoalsA();
                    int secondHalfGoalsB = this.currentMatchState.getSecondHalfGoalsA();
                    if (goalsA == goalsB && (secondHalfGoalsA == 0 || secondHalfGoalsB == 0))
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:1HCOMBO":
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 1) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (currentPeriodSeqNo > 1) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    if (home1sthalf > away1sthalf && away1sthalf > 0) {
                        winningSelections.add("Home team to win at half time and both team to score in first half");
                    }

                    if (home1sthalf == away1sthalf && away1sthalf > 0) {
                        winningSelections.add("Draw at half time and both team to score in first half");
                    }

                    if (home1sthalf < away1sthalf && home1sthalf > 0) {
                        winningSelections.add("Away team to win at half time and both team to score in first half");
                    }

                    if (home1sthalf > away1sthalf && away1sthalf == 0) {
                        winningSelections
                                        .add("Home team to win at half time and both teams not to score in first half");
                    }

                    if (home1sthalf == away1sthalf && away1sthalf == 0) {
                        winningSelections.add("Draw at half time and both teams not to score in first half");
                    }

                    if (home1sthalf < away1sthalf && home1sthalf == 0) {
                        winningSelections
                                        .add("Away team to win at half time and both teams not to score in first half");
                    }

                    if (home1sthalf > away1sthalf && (home1sthalf == 2 || home1sthalf == 3)) {
                        winningSelections.add("Home team to win at half time and score 2 - 3 goals at half time");
                    }

                    if (home1sthalf < away1sthalf && (away1sthalf == 2 || away1sthalf == 3)) {
                        winningSelections.add("Away team to win at half time and score 2 - 3 goals at half time");
                    }

                    return new CheckMarketResultedOutcome(winningSelections);

                } else {
                    return null;
                }


            case "P:DBCS":

                if (!this.currentMatchState.isMatchCompleted()) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                if (periodSeqNo == 1) {
                    int homeP = this.currentMatchState.getFirstHalfGoalsA();
                    int awayP = this.currentMatchState.getFirstHalfGoalsB();
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();
                    if (homeM >= awayM && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Home team win or draw and both team to score in first half");
                    }

                    if ((homeM != awayM) && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Home or Away team will win and both team to score in first half");
                    }

                    if ((homeM <= awayM) && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Away team win or draw and both team to score in first half");
                    }

                    if ((homeM >= awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Home team win or draw and not both team will score in first half");
                    }

                    if ((homeM > awayM || homeM < awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Home or Away team to win and not both team will score in first half");
                    }

                    if ((homeM <= awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Away win or draw and not both team will score in first half");
                    }

                    if ((homeM >= awayM) && (homeP + awayP >= 2)) {
                        winningSelections.add("Home win or draw and 2 or more goals will be scored in first half");
                    }

                    if ((homeM > awayM || homeM < awayM) && (homeP + awayP >= 2)) {
                        winningSelections.add("Home or Away team win and 2 or more goals will be scored in first half");
                    }

                    if ((homeM <= awayM) && (homeP + awayP >= 2)) {
                        winningSelections.add("Away team win or draw and 2 or more goals will be scored in first half");
                    }

                    return new CheckMarketResultedOutcome(winningSelections);

                } else if (periodSeqNo == 2) {

                    int homeP = this.currentMatchState.getSecondHalfGoalsA();
                    int awayP = this.currentMatchState.getSecondHalfGoalsB();
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();
                    if (homeM >= awayM && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Home team win or draw and both team to score in second half");
                    }

                    if ((homeM != awayM) && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Home or Away team will win and both team to score in second half");
                    }

                    if ((homeM <= awayM) && (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Away team win or draw and both team to score in second half");
                    }

                    if ((homeM >= awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Home team win or draw and not both team will score in second half");
                    }

                    if ((homeM > awayM || homeM < awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Home or Away team to win and not both team will score in second half");
                    }

                    if ((homeM <= awayM) && (!(homeP > 0 && awayP > 0))) {
                        winningSelections.add("Away win or draw and not both team will score in second half");
                    }

                    if ((homeM >= awayM) && (homeP + awayP >= 2)) {
                        winningSelections.add("Home win or draw and 2 or more goals will be scored in second half");
                    }

                    if ((homeM > awayM || homeM < awayM) && (homeP + awayP >= 2)) {
                        winningSelections
                                        .add("Home or Away team win and 2 or more goals will be scored in second half");
                    }

                    if ((homeM <= awayM) && (homeP + awayP >= 2)) {
                        winningSelections
                                        .add("Away team win or draw and 2 or more goals will be scored in second half");
                    }

                    return new CheckMarketResultedOutcome(winningSelections);



                } else {
                    return null;
                }

            case "FT:2HCOMBO":
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getPeriodSequenceId());
                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getPeriodSequenceId());
                if (currentPeriodSeqNo <= 2) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (currentPeriodSeqNo > 2) {
                    int home2ndhalf = this.currentMatchState.getSecondHalfGoalsA();
                    int away2ndhalf = this.currentMatchState.getSecondHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();
                    if (home > away && home2ndhalf > 0 && away2ndhalf > 0) {
                        winningSelections.add("Home team will win and both team to score in second half");
                    }

                    if (home == away && home2ndhalf > 0 && away2ndhalf > 0) {
                        winningSelections.add("Match will end in a draw and both team to score in second half");
                    }

                    if (home < away && home2ndhalf > 0 && away2ndhalf > 0) {
                        winningSelections.add("Away team will win and both team to score in second half");
                    }

                    if (home > away && !(home2ndhalf > 0 && away2ndhalf > 0)) {
                        winningSelections.add("Home team will win but not both team will score in second half");
                    }

                    if (home == away && !(home2ndhalf > 0 && away2ndhalf > 0)) {
                        winningSelections.add("Match will end in a draw and no team will score in second half");
                    }

                    if (home < away && !(home2ndhalf > 0 && away2ndhalf > 0)) {
                        winningSelections.add("Away team will win but not both team will score in second half");
                    }

                    if (home > away && (home2ndhalf > 2)) {
                        winningSelections.add("Home team will win and 2 or more goals will be scored in second half");
                    }

                    if (home < away && (away2ndhalf > 2)) {
                        winningSelections.add("Away team will win and 2 or more goals will be scored in second half");
                    }

                    return new CheckMarketResultedOutcome(winningSelections);

                } else {
                    return null;
                }


            case "FT:CORNEROVER2":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersA() + this.currentMatchState.getCornersB();
                    if (nCorners > 2) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:CORNEROVER15":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersA() + this.currentMatchState.getCornersB();
                    if (nCorners > 15) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:ACORNER":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersA();
                    if (nCorners > 0) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:BCORNER":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersB();
                    if (nCorners > 0) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:COE":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersA() + this.currentMatchState.getCornersB();
                    if (nCorners % 2 == 0) {
                        winningSelections.add("Even");
                    } else {
                        winningSelections.add("Odd");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:A:COE":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersA();
                    if (nCorners % 2 == 0) {
                        winningSelections.add("Even");
                    } else {
                        winningSelections.add("Odd");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:B:COE":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getCornersB();
                    if (nCorners % 2 == 0) {
                        winningSelections.add("Even");
                    } else {
                        winningSelections.add("Odd");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:CARDSYELLOWOVER1":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB();
                    if (nCorners > 0) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:CARDSYELLOWOVER8":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCorners = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB();
                    if (nCorners > 7) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:CARDSYESNO":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCards = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB()
                                    + this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB();

                    if (nCards > 0) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:CARDSODDEVEN":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCards = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB()
                                    + this.currentMatchState.getRedCardsA() + this.currentMatchState.getRedCardsB();

                    if (nCards % 2 == 0) {
                        winningSelections.add("Even");
                    } else {
                        winningSelections.add("Odd");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:YCARDSODDEVEN":
                if (this.currentMatchState.isMatchCompleted()) {
                    int nCards = this.currentMatchState.getYellowCardsA() + this.currentMatchState.getYellowCardsB();

                    if (nCards % 2 == 0) {
                        winningSelections.add("Even");
                    } else {
                        winningSelections.add("Odd");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }


            case "FT:HHTHFT2":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf > away1sthalf && home > away && (home + away >= 2)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:AHTAFT2":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf < away1sthalf && home < away && (home + away >= 2)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:WTTS":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() == 0 && this.currentMatchState.getGoalsB() == 0) {
                        return new CheckMarketResultedOutcome("None");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() > 0) {
                        return new CheckMarketResultedOutcome("Both");
                    }
                    if (this.currentMatchState.getGoalsA() > 0 && this.currentMatchState.getGoalsB() == 0) {
                        return new CheckMarketResultedOutcome("A");
                    }
                    if (this.currentMatchState.getGoalsA() == 0 && this.currentMatchState.getGoalsB() > 0) {
                        return new CheckMarketResultedOutcome("B");
                    }

                }

            case "FT:TWOBTSOU":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();
                    double line = 2.5;
                    if (home > away || (home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("1/GG/Ov");
                    }
                    if (home < away || (home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("2/GG/Ov");
                    }
                    if (home == away || (home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("X/GG/Ov");
                    }
                    if (home > away || (home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("1/GG/Un");
                    }
                    if (home > away || (home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("2/GG/Un");
                    }
                    if (home > away || (home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("X/GG/Un");
                    }
                    if (home > away || !(home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("1/NG/Ov");
                    }
                    if (home < away || !(home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("2/NG/Ov");
                    }
                    if (home == away || !(home > 0 && away > 0) || (home + away) > line) {
                        winningSelections.add("X/NG/Ov");
                    }
                    if (home > away || !(home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("1/NG/Un");
                    }
                    if (home > away || !(home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("2/NG/Un");
                    }
                    if (home > away || !(home > 0 && away > 0) || (home + away) < line) {
                        winningSelections.add("X/NG/Un");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:DBLCHTFT":
                if (this.currentMatchState.isMatchCompleted()) {
                    int homeP = this.currentMatchState.getFirstHalfGoalsA();
                    int awayP = this.currentMatchState.getFirstHalfGoalsB();
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();

                    if (homeM >= awayM && homeP >= awayP) {
                        winningSelections.add("1X/1X");
                    }
                    if (homeM != awayM && homeP >= awayP) {
                        winningSelections.add("1X/12");
                    }
                    if (homeM <= awayM && homeP >= awayP) {
                        winningSelections.add("1X/X2");
                    }
                    if (homeM >= awayM && homeP != awayP) {
                        winningSelections.add("12/1X");
                    }
                    if (homeM != awayM && homeP != awayP) {
                        winningSelections.add("12/12");
                    }
                    if (homeM <= awayM && homeP != awayP) {
                        winningSelections.add("12/X2");
                    }
                    if (homeM >= awayM && homeP <= awayP) {
                        winningSelections.add("X2/1X");
                    }
                    if (homeM != awayM && homeP <= awayP) {
                        winningSelections.add("X2/12");
                    }
                    if (homeM <= awayM && homeP <= awayP) {
                        winningSelections.add("X2/X2");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:HHTHFT3":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf > away1sthalf && home > away && (home + away >= 3)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:AHTAFT3":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf < away1sthalf && home < away && (home + away >= 3)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:HHTHFT4":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf > away1sthalf && home > away && (home + away >= 4)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:AHTAFT4":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf < away1sthalf && home < away && (home + away >= 4)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:HHTHFT2H":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf > away1sthalf && home > away && (home1sthalf + away1sthalf >= 2)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:AHTAFT2H":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home1sthalf < away1sthalf && home < away && (home1sthalf + away1sthalf >= 2)) {
                        winningSelections.add("Yes");
                    } else {
                        winningSelections.add("No");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:HOFGA":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home2ndhalf = this.currentMatchState.getSecondHalfGoalsA();
                    // int away2ndhalf = this.currentMatchState.getSecondHalfGoalsB();
                    int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    // int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();

                    if (home1sthalf > 0) {
                        winningSelections.add("1st Home Goal in First Half");
                    } else if (home1sthalf == 0 && home2ndhalf > 0) {
                        winningSelections.add("1st Home Goal in Second Half");
                    } else if ((home1sthalf + home2ndhalf) == 0) {
                        winningSelections.add("No Home Goal");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }
            case "FT:HOFGB":
                if (this.currentMatchState.isMatchCompleted()) {
                    // int home2ndhalf = this.currentMatchState.getSecondHalfGoalsA();
                    int away2ndhalf = this.currentMatchState.getSecondHalfGoalsB();
                    // int home1sthalf = this.currentMatchState.getFirstHalfGoalsA();
                    int away1sthalf = this.currentMatchState.getFirstHalfGoalsB();

                    if (away1sthalf > 0) {
                        winningSelections.add("1st Away Goal in First Half");
                    } else if (away1sthalf == 0 && away2ndhalf > 0) {
                        winningSelections.add("1st Away Goal in Second Half");
                    } else if ((away1sthalf + away2ndhalf) == 0) {
                        winningSelections.add("No Away Goal");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }


            case "FT:CM":
                if (this.currentMatchState.isMatchCompleted()) {
                    int home = this.currentMatchState.getGoalsA();
                    int away = this.currentMatchState.getGoalsB();

                    if (home > away || (home + away) >= 3) {
                        winningSelections.add("Home team to win OR 3 or more goals will be scored in the match");
                    }
                    if (home == away || (home + away) >= 3) {
                        winningSelections
                                        .add("Match will end in a draw OR 3 or more goals will be scored in the match");
                    }
                    if (home < away || (home + away) >= 3) {
                        winningSelections.add("Away team to win OR 3 or more goals will be scored in the match");
                    }
                    if (home > away || (home + away) < 2.5) {
                        winningSelections.add("Home team to win OR not more than 2 goals will be scored in the match");
                    }
                    if (home == away && (home + away) < 2.5) {
                        winningSelections.add(
                                        "Match will end in a draw OR not more than 2 goals will be scored in the match");
                    }
                    if (home < away && (home + away) < 2.5) {
                        winningSelections.add("Away team to win OR not more than 2 goals will be scored in the match");
                    }
                    if (home > away || (home > 0 && away > 0)) {
                        winningSelections.add("Home team will win OR both team will score");
                    }
                    if (home == away || (home > 0 && away > 0)) {
                        winningSelections.add("Match will end in a draw OR both team will score");
                    }
                    if (home < away || (home > 0 && away > 0)) {
                        winningSelections.add("Away team will win OR both team will score");
                    }
                    if (home > away || !(home > 0 && away > 0)) {
                        winningSelections.add("Home team will win OR not both team will score in the match");
                    }
                    if (home == away || !(home > 0 && away > 0)) {
                        winningSelections.add("Match will end in a draw OR not both team will score in the match");
                    }
                    if (home < away || !(home > 0 && away > 0)) {
                        winningSelections.add("Away team will win OR not both team will score in the match");
                    }
                    if (home > away || (home + away) < 3.5) {
                        winningSelections
                                        .add("Home team will win OR not more than 3 goals will be scored in the match");
                    }
                    if (home < away || ((home + away) < 3.5)) {
                        winningSelections
                                        .add("Away team will win OR not more than 3 goals will be scored in the match");
                    }
                    if (home == away || (home + away) < 3.5) {
                        winningSelections.add(
                                        "Match will end in a draw OR not more than 3 goals will be scored in the match");
                    }
                    if (home == away || (home + away) >= 2) {
                        winningSelections
                                        .add("Match will end in a draw OR 2 or more goals will be scored in the match");
                    }
                    if (home == away || (home + away) < 1.5) {
                        winningSelections.add(
                                        "Match will end in a draw OR not more than 1 goal will be scored in the match");
                    }
                    if (home > away || (home + away) < 1.5) {
                        winningSelections.add("Home team will win OR not more than 1 goal will be scored in the match");
                    }
                    if (home < away || (home + away) < 1.5) {
                        winningSelections.add("Away team will win OR not more than 1 goal will be scored in the match");
                    }
                    if ((home + away < 1.5) || (home > 0 && away > 0)) {
                        winningSelections.add(
                                        "Both team will score OR not more than 1 goal will be scored in the match");
                    }
                    if (home > away || (home + away) >= 2) {
                        winningSelections.add("Home team will win OR 2 or more goals will be scored in the match");
                    }
                    if (home < away || (home + away) >= 2) {
                        winningSelections.add("Away team will win OR 2 or more goals will be scored in the match");
                    }
                    if (home == away && (home + away) >= 2) {
                        winningSelections
                                        .add("Match will end in a draw OR 2 or more goals will be scored in the match");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }


            case "FT:CM15":
                if (this.currentMatchState.isMatchCompleted()) {
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();

                    if ((homeM + awayM) > 1.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Over " + 1.5);
                    }
                    if ((homeM + awayM) > 1.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Over " + 1.5);
                    }
                    if ((homeM + awayM) > 1.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Over " + 1.5);
                    }
                    if ((homeM + awayM) < 1.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Under " + 1.5);
                    }
                    if ((homeM + awayM) < 1.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Under " + 1.5);
                    }
                    if ((homeM + awayM) < 1.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Under " + 1.5);
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:CM25":
                if (this.currentMatchState.isMatchCompleted()) {
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();

                    if ((homeM + awayM) > 2.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Over " + 2.5);
                    }
                    if ((homeM + awayM) > 2.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Over " + 2.5);
                    }
                    if ((homeM + awayM) > 2.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Over " + 2.5);
                    }
                    if ((homeM + awayM) < 2.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Under " + 2.5);
                    }
                    if ((homeM + awayM) < 2.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Under " + 2.5);
                    }
                    if ((homeM + awayM) < 2.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Under " + 2.5);
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            case "FT:CM35":
                if (this.currentMatchState.isMatchCompleted()) {
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();

                    if ((homeM + awayM) > 3.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Over " + 3.5);
                    }
                    if ((homeM + awayM) > 3.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Over " + 3.5);
                    }
                    if ((homeM + awayM) > 3.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Over " + 3.5);
                    }
                    if ((homeM + awayM) < 3.5 || (homeM > awayM)) {
                        winningSelections.add("Home or Under " + 3.5);
                    }
                    if ((homeM + awayM) < 3.5 || (homeM == awayM)) {
                        winningSelections.add("Draw or Under " + 3.5);
                    }
                    if ((homeM + awayM) < 3.5 || (homeM < awayM)) {
                        winningSelections.add("Away or Under " + 3.5);
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }


            case "FT:CM2":
                if (this.currentMatchState.isMatchCompleted()) {
                    int homeM = this.currentMatchState.getGoalsA();
                    int awayM = this.currentMatchState.getGoalsB();

                    int homeP = this.currentMatchState.getFirstHalfGoalsA();
                    int awayP = this.currentMatchState.getFirstHalfGoalsB();

                    int home2P = this.currentMatchState.getSecondHalfGoalsA();
                    int away2P = this.currentMatchState.getSecondHalfGoalsB();

                    if ((homeP > awayP) || (homeM > awayM)) {
                        winningSelections.add(" Both team to score in second half OR not more ");
                    }
                    if ((homeP == awayP) || (homeM == awayM)) {
                        winningSelections.add("There should be draw at half time or at full time");
                    }
                    if ((homeP < awayP) || (homeM < awayM)) {
                        winningSelections.add("Away team to win at half time or at full time");
                    }
                    if ((homeP > awayP) || (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Home team to win at half time OR both team to score at half time");
                    }
                    if ((homeP == awayP) || (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Draw to happen at half time OR both team will score at half time");
                    }
                    if ((homeP < awayP) || (homeP > 0 && awayP > 0)) {
                        winningSelections.add("Away team to win at half time OR both team to score at half time");
                    }
                    if ((homeP > awayP) || (homeP == 0 && awayP == 0)) {
                        winningSelections.add("Home team to win at half time OR no goal will be score at half time");
                    }
                    if ((homeP == awayP) || (homeP == 0 && awayP == 0)) {
                        winningSelections.add("Draw to happen at half time OR no goal will be score at half time");
                    }
                    if ((homeP < awayP) || (homeP == 0 && awayP == 0)) {
                        winningSelections.add("Away team to win at half time OR no goal will be score at half time");
                    }
                    if ((homeP > awayP) || (homeP + awayP >= 2)) {
                        winningSelections.add(
                                        "Home team to win at half time OR 2 goals and above will be scored at half time");
                    }
                    if ((homeP == awayP) || (homeP + awayP >= 2)) {
                        winningSelections.add(
                                        "First half to end in a draw OR 2 goals and above will be scored at half time");
                    }
                    if ((homeP < awayP) || (homeP + awayP >= 2)) {
                        winningSelections.add(
                                        "Away team to win at half time OR 2 goals and above will be scored at half time");
                    }
                    if ((homeP > awayP) || (homeP == 0 && awayP == 0)) {
                        winningSelections.add("Home team to win at half time OR no goal will be score at half time");
                    }
                    if ((homeP < awayP) || (homeP == 0 && awayP == 0)) {
                        winningSelections.add("Away team to win at half time OR no goal will be score at half time");
                    }
                    if ((homeP > awayP) || (homeP + awayP <= 1)) {
                        winningSelections.add(
                                        "Home team to win at half time OR not more than 1 goal will be scored at half time");
                    }
                    if ((homeP < awayP) || (homeP + awayP <= 1)) {
                        winningSelections.add(
                                        "Away team to win at half time OR not more than 1 goal will be scored at half time");
                    }
                    if ((homeP + awayP <= 1) || (homeP > 0 && awayP > 0)) {
                        winningSelections.add(
                                        "Both team to score at half time OR not more than 1 goal will be scored at half time");
                    }
                    if ((home2P > away2P) || (home2P > 0 && away2P > 0)) {
                        winningSelections.add("Home team to win in second half OR both team to score in second half");
                    }
                    if ((home2P == away2P) || (home2P > 0 && away2P > 0)) {
                        winningSelections.add("Draw to happen in second half OR both team to score in second half");
                    }
                    if ((home2P < away2P) || (home2P > 0 && away2P > 0)) {
                        winningSelections.add("Away team to win in second half OR both team to score in second half");
                    }
                    if ((home2P > away2P) || (home2P == 0 && away2P == 0)) {
                        winningSelections
                                        .add("Home team to win in second half OR no goal will be score in second half");
                    }
                    if ((home2P == away2P) || (home2P == 0 && away2P == 0)) {
                        winningSelections.add("Draw to happen in second half OR no goal will be score in second half");
                    }
                    if ((home2P < away2P) || (home2P == 0 && away2P == 0)) {
                        winningSelections.add(
                                        "Away team to win in second half OR no goal will be scored in second half");
                    }


                    if ((home2P > away2P) || (home2P + away2P >= 2)) {
                        winningSelections.add(
                                        "Home team to win in second half OR 2 goals and above will be scored in second half");
                    }
                    if ((home2P == away2P) || (home2P + away2P >= 2)) {
                        winningSelections.add(
                                        "Draw to happen in second half OR 2 goals and above will be scored in second half");
                    }
                    if ((home2P < away2P) || (home2P + away2P >= 2)) {
                        winningSelections.add(
                                        "Away team to win in second half OR 2 goals and above will be scored in second half");
                    }
                    if ((home2P > away2P) || (home2P == 0 && away2P == 0)) {
                        winningSelections
                                        .add("Home team to win in second half OR no goal will be score in second half");
                    }
                    if ((home2P < away2P) || (home2P == 0 && away2P == 0)) {
                        winningSelections.add(
                                        "Away team to win in second half OR no goal will be scored in second half");
                    }
                    if ((home2P > away2P) || (home2P + away2P <= 1)) {
                        winningSelections.add(
                                        "Home team to win at second half  OR not nore than 1 goal will be scored at second half");
                    }
                    if ((home2P > away2P) || (home2P + away2P <= 1)) {
                        winningSelections.add(
                                        "Away team to win at second half OR not more than 1 goal will be scored at second half");
                    }
                    if ((home2P > 0 && away2P > 0) || (home2P + away2P <= 1)) {
                        winningSelections.add(
                                        "Both team to score in second half OR not more than 1 goal will be scored in second half");
                    }
                    return new CheckMarketResultedOutcome(winningSelections);
                } else {
                    return null;
                }

            default:
                throw new IllegalArgumentException(
                                "Market type missing from FootballMatchResultMarkets: " + market.getType());

        }
    }

    /**
     * Converts string of form "Pnn" to integer
     * 
     * @param periodSequenceId
     * @return
     */
    int convertPeriodSeqIdToIndex(String sequenceId) {
        int index;
        String s = sequenceId.substring(1, 2);
        index = Integer.parseInt(s);
        return index;
    }

    int convertGoalSeqIdToIndex(String sequenceId) {
        int index;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }

    private String selectWinningRangeSelection(int goals) {
        String winningSelection = null;
        if (goals == 0)
            winningSelection = exactTotalGoal[0];
        else if (goals == 1)
            winningSelection = exactTotalGoal[1];
        else
            winningSelection = exactTotalGoal[2];
        return winningSelection;

    }

    private String selectWinningRangeSelectionForTeam(int goals) {
        String winningSelection = null;
        if (goals == 0)
            winningSelection = exactTotalGoalForTeam[0];
        else if (goals == 1)
            winningSelection = exactTotalGoalForTeam[1];
        else if (goals == 2)
            winningSelection = exactTotalGoalForTeam[2];
        else
            winningSelection = exactTotalGoalForTeam[3];
        return winningSelection;
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }

    private int goalScoredForTeam(List<TeamId> goals, TeamId a) {
        int scored = 0;
        for (TeamId teamScored : goals) {
            if (teamScored.equals(a))
                scored++;
        }
        return scored;
    }

    private CheckMarketResultedOutcome checkMktRaceToCorners(int marketPointNo) {
        if (this.currentMatchState.getCornersA() == marketPointNo
                        && this.currentMatchState.getCornersB() < marketPointNo) {
            return new CheckMarketResultedOutcome("A");
        }
        if (this.currentMatchState.getCornersA() < marketPointNo
                        && this.currentMatchState.getCornersB() == marketPointNo) {
            return new CheckMarketResultedOutcome("B");
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome("Neither");
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkMktRaceToCornersTwoWay(int marketPointNo) {
        if (this.currentMatchState.getCornersA() == marketPointNo
                        && this.currentMatchState.getCornersB() < marketPointNo) {
            return new CheckMarketResultedOutcome("A");
        }
        if (this.currentMatchState.getCornersA() < marketPointNo
                        && this.currentMatchState.getCornersB() == marketPointNo) {
            return new CheckMarketResultedOutcome("B");
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome generateResultOutcome(String winningSelection) {
        switch (winningSelection) {
            case "0-0":
            case "0-1":
            case "0-2":
            case "0-3":
            case "0-4":
            case "0-5":
            case "0-6":
            case "1-0":
            case "1-1":
            case "1-2":
            case "1-3":
            case "1-4":
            case "1-5":
            case "2-0":
            case "2-1":
            case "2-2":
            case "2-3":
            case "2-4":
            case "3-0":
            case "3-1":
            case "3-2":
            case "3-3":
            case "4-0":
            case "4-1":
            case "4-2":
            case "5-0":
            case "5-1":
            case "6-0":
            case "6-6":
                return new CheckMarketResultedOutcome(winningSelection);
            default:
                return new CheckMarketResultedOutcome("Any Other");
        }
    }
}
