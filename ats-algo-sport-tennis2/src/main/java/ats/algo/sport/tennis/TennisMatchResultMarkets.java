package ats.algo.sport.tennis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;

public class TennisMatchResultMarkets extends MatchResultMarkets {
    // /*
    // * markets to be voided when match retired, bfam-840
    // */
    List<String> marketTypeList = new ArrayList<String>(Arrays.asList());

    TennisMatchState previousMatchState;
    TennisMatchState currentMatchState;
    int marketSetNo;
    int marketGameNo;
    int marketPointNo;
    boolean matchAbandoned = false;
    boolean matchRetired = false;
    boolean matchRetireHome = false;
    private String[] setXgameYCorrectScoreList = {"Player A To Win To Love", "Player A To Win To 15",
            "Player A To Win To 30", "Player A To Win To 40", "Player B To Win To Love", "Player B To Win To 15",
            "Player B To Win To 30", "Player B To Win To 40"};
    private String[] setXgameYCorrectScoreListPS = {"A-0", "A-15", "A-30", "A-AD", "B-0", "B-15", "B-30", "B-AD"};
    private static final String PARTIAL_RESULTING = "tennis.partialResulting";
    boolean partialResulting = true;
    private Set<String> setBetting5Set =
                    new HashSet<>(Arrays.asList("A 3-0", "A 3-1", "A 3-2", "B 3-0", "B 3-1", "B 3-2"));
    private Set<String> setBetting3Set = new HashSet<>(Arrays.asList("A 2-0", "A 2-1", "B 2-0", "B 2-1"));
    private Set<String> pCSFullSelections = new HashSet<>(Arrays.asList("Player A 6-0", "Player A 6-1", "Player A 6-2",
                    "Player A 6-3", "Player A 6-4", "Player A 7-5", "Player A 7-6", "Player B 6-0", "Player B 6-1",
                    "Player B 6-2", "Player B 6-3", "Player B 6-4", "Player B 7-5", "Player B 7-6"));

    private Set<String> pCSFullSelectionsFast4 = new HashSet<>(Arrays.asList("Player A 4-0", "Player A 4-1",
                    "Player A 4-2", "Player A 4-3", "Player B 4-0", "Player B 4-1", "Player B 4-2", "Player B 4-3"));
    private boolean walkOverMatch = false;
    private boolean cancelledMatch = false;
    boolean ppbMarkets = false;
    boolean vanillaMarkets = false;
    boolean psMarkets = false;
    boolean fastFour = false;
    private static final String CLIENT_MARKETS = "clientMarkets";

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchStateIn,
                    MatchState currentMatchStateIn) {

        vanillaMarkets = false;
        psMarkets = false;
        ppbMarkets = false;
        double line;
        String clients = System.getProperty(CLIENT_MARKETS);
        if (clients != null) {
            if (clients.toLowerCase().equals("ppb")) {
                ppbMarkets = true;
                marketTypeList.add("FT:ML");
                marketTypeList.add("FT:CS");
                marketTypeList.add("P:ML");
                marketTypeList.add("P:CS");
                marketTypeList.add("FT:W1S:A");
                marketTypeList.add("FT:W1S:B");
            }
            if (clients.toLowerCase().equals("van"))
                vanillaMarkets = true;
            if (clients.toLowerCase().equals("pkr"))
                psMarkets = true;
        }

        String partialResultingString = System.getProperty(PARTIAL_RESULTING);
        if (partialResultingString != null)
            if (partialResultingString.toLowerCase().equals("true"))
                partialResulting = true;
        this.previousMatchState = (TennisMatchState) previousMatchStateIn;
        this.currentMatchState = (TennisMatchState) currentMatchStateIn;
        int nSetInMatch = (currentMatchState.getnSetsInMatch() + 1) / 2;

        fastFour = this.currentMatchState.getTournamentLevel().equals(TournamentLevel.FAST4);
        parseSequenceId(market.getSequenceId());

        if (this.currentMatchState.getMostRecentMatchIncidentResult() != null)
            matchAbandoned = (this.currentMatchState.getMostRecentMatchIncidentResult()
                            .getTennisMatchIncidentResultType() == TennisMatchIncidentResultType.MATCHABANDONED);

        if (this.currentMatchState.getAbandonMatchIncident() != null) {
            walkOverMatch = this.currentMatchState.getAbandonMatchIncident()
                            .getIncidentSubType() == AbandonMatchIncidentType.WALKOVER;
            matchRetired = (this.currentMatchState.getAbandonMatchIncident()
                            .getIncidentSubType() == AbandonMatchIncidentType.RETIREMENT);
            matchRetireHome = this.currentMatchState.getAbandonMatchIncident().getTeamId() == TeamId.A;
            cancelledMatch = this.currentMatchState.getAbandonMatchIncident()
                            .getIncidentSubType() == AbandonMatchIncidentType.CANCELLATION;
        }

        String winningSelection;
        if (matchRetired) {
            if (inList(market.getType(), marketTypeList)) {
                // do nothing, resulting as normal
            } else {
                return new CheckMarketResultedOutcome();
            }
        } else if (cancelledMatch || walkOverMatch) {
            return new CheckMarketResultedOutcome();
        }

        switch (market.getType()) {
            case "FT:ML": // Match winner
                if (matchRetired) {
                    if (matchRetireHome)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

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
                /*
                 * Check some of the selections TEMP FIX not do partial
                 */
                if (matchRetired) {
                    if (currentMatchState.getnSetsInMatch() == 5) {
                        ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB(), setBetting5Set);
                        if (loseSelections != null && loseSelections.size() > 0)
                            return new CheckMarketResultedOutcome(loseSelections, true, true);
                        else
                            return new CheckMarketResultedOutcome();
                    } else {
                        ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB(), setBetting3Set);
                        if (loseSelections != null && loseSelections.size() > 0)
                            return new CheckMarketResultedOutcome(loseSelections, true, true);
                        else
                            return new CheckMarketResultedOutcome();
                    }
                }

                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB())
                        winningSelection = String.format("A %d-%d", currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB());

                    else
                        winningSelection = String.format("B %d-%d", currentMatchState.getSetsB(),
                                        currentMatchState.getSetsA());
                    return new CheckMarketResultedOutcome(winningSelection);

                } else if (partialResulting) {
                    if (currentMatchState.getnSetsInMatch() == 5) {
                        ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB(), setBetting5Set);
                        if (loseSelections != null && loseSelections.size() > 0)
                            return new CheckMarketResultedOutcome(loseSelections, false);
                    } else {
                        ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getSetsA(),
                                        currentMatchState.getSetsB(), setBetting3Set);
                        if (loseSelections != null && loseSelections.size() > 0)
                            return new CheckMarketResultedOutcome(loseSelections, false);
                    }
                }
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
                    int firstBreakB = currentMatchState.getFirstServingBreakBInSetN()[marketSetNo - 1];
                    if (firstBreakB == 0 || firstBreakB > 12)
                        winningSelection = "Doesn't break serve in set";
                    else if (firstBreakB < 5)
                        winningSelection = "Games 1-4";
                    else if (firstBreakB < 9)
                        winningSelection = "Games 5-8";
                    else
                        winningSelection = "After Game 8";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

                else
                    return null;

            case "FT:W1S:A": // A to win at least one set
                if (matchRetired)
                    return new CheckMarketResultedOutcome();
                if (currentMatchState.isMatchCompleted() || currentMatchState.getSetsA() > 0) {
                    if (currentMatchState.getSetsA() > 0)
                        winningSelection = "A Yes";
                    else
                        winningSelection = "A No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:W1S:B": // B to win at least one set
                if (matchRetired)
                    return new CheckMarketResultedOutcome();
                if (currentMatchState.isMatchCompleted() || currentMatchState.getSetsB() > 0) {
                    if (currentMatchState.getSetsB() > 0)
                        winningSelection = "B Yes";
                    else
                        winningSelection = "B No";
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
                    if (matchRetired)
                        return new CheckMarketResultedOutcome();
                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB()
                                    && currentMatchState.getSetsB() != 0)
                        return new CheckMarketResultedOutcome("Player A wins the match and both players win a set");
                    else if (currentMatchState.getSetsA() > currentMatchState.getSetsB()
                                    && currentMatchState.getSetsB() == 0)
                        return new CheckMarketResultedOutcome("Player A wins the match and not both players win a set");
                    else if (currentMatchState.getSetsA() < currentMatchState.getSetsB()
                                    && currentMatchState.getSetsA() != 0)
                        return new CheckMarketResultedOutcome("Player B wins the match and both players win a set");
                    else if (currentMatchState.getSetsA() < currentMatchState.getSetsB()
                                    && currentMatchState.getSetsA() == 0)
                        return new CheckMarketResultedOutcome("Player B wins the match and not both players win a set");

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
                } else {
                    int currentLine = 0;
                    int currentSet = currentMatchState.getSetsA() + currentMatchState.getSetsB() + 1;

                    int remainingScore = 0;
                    int possibleMinLine = 0;
                    int finalScoreOfSet = 6;
                    if (Double.valueOf(market.getLineId()) > 0) {
                        if (currentMatchState.getGamesA() >= 5)
                            finalScoreOfSet = 7;
                        remainingScore = finalScoreOfSet - currentMatchState.getGamesB()
                                        + Math.max(currentMatchState.getnSetsInMatch() - currentSet, 0) * 6;
                        currentLine = (currentMatchState.getGamesA() + currentMatchState.getTotalGamesA())
                                        - (currentMatchState.getTotalGamesB() + currentMatchState.getGamesB());
                        if (remainingScore < currentLine + Double.valueOf(market.getLineId())) {
                            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(possibleMinLine,
                                            market.getLineId());
                            return new CheckMarketResultedOutcome(winningSelection, possibleMinLine, true);
                        }
                    } else if (Double.valueOf(market.getLineId()) < 0) {
                        if (currentMatchState.getGamesB() >= 5)
                            finalScoreOfSet = 7;
                        remainingScore = finalScoreOfSet - currentMatchState.getGamesA()
                                        + Math.max(currentMatchState.getnSetsInMatch() - currentSet, 0) * 6;
                        currentLine = (currentMatchState.getGamesB() + currentMatchState.getTotalGamesB())
                                        - (currentMatchState.getTotalGamesA() + currentMatchState.getGamesA());
                        if (remainingScore < currentLine - Double.valueOf(market.getLineId())) {
                            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(possibleMinLine,
                                            market.getLineId());
                            return new CheckMarketResultedOutcome(winningSelection, possibleMinLine, true);
                        }
                    }
                    return null;
                }
            case "FT:S:ASPRD":
            case "FT:S:SPRD": // Match total sets handicap
                int set = currentMatchState.getSetsA() - currentMatchState.getSetsB();
                line = Double.parseDouble(market.getLineId());
                int outcome;
                if (set > 0) {
                    if ((set + line) > nSetInMatch) {
                        outcome = nSetInMatch;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(outcome,
                                        market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, outcome, true);
                    }
                } else if (set < 0) {
                    if (Math.abs(set + line) > nSetInMatch) {
                        outcome = (-1) * nSetInMatch;
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(outcome,
                                        market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, outcome, true);
                    }

                } else {
                    set = Math.max(currentMatchState.getSetsA(), currentMatchState.getSetsB());
                    if (line < 0) {
                        if (Math.abs(line) + set > nSetInMatch) {
                            outcome = (-1) * nSetInMatch;
                            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(outcome,
                                            market.getLineId());

                            return new CheckMarketResultedOutcome(winningSelection, outcome, true);
                        }
                    } else if (line > 0) {
                        if (Math.abs(line) + set > nSetInMatch) {
                            outcome = nSetInMatch;
                            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(outcome,
                                            market.getLineId());

                            return new CheckMarketResultedOutcome(winningSelection, outcome, true);
                        }
                    }
                }
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
                    line = Double.parseDouble(market.getLineId());
                    if (line < n)
                        return new CheckMarketResultedOutcome("Over", n);
                    else
                        return new CheckMarketResultedOutcome();
                    // winningSelection =
                    // MarketUtilityFunctions.getWinningSelectionOvunMarket(n, );
                } else {
                    int currentLine = currentMatchState.getTotalGamesA() + currentMatchState.getTotalGamesB();
                    int currentSet = currentMatchState.getSetsA() + currentMatchState.getSetsB() + 1;
                    int remainingScore = 0;
                    int possibleMinLine = 0;
                    if (fastFour) {
                        int finalSetGames = 0;
                        if (currentMatchState.isInSuperTieBreak()) // only count in at tie break
                            finalSetGames = 1;
                        else
                            remainingScore = Math.min(currentMatchState.getGamesA(), currentMatchState.getGamesB()) + 4;// for
                                                                                                                        // current
                                                                                                                        // set

                        int remaningMinSetExcludeTBSet = nSetInMatch
                                        - Math.max(currentMatchState.getSetsA(), currentMatchState.getSetsB()) - 1;
                        possibleMinLine = currentLine + remainingScore + remaningMinSetExcludeTBSet * 4 + finalSetGames;
                    } else {
                        if (currentMatchState.getGamesA() >= 5 && currentMatchState.getGamesB() >= 5) {
                            remainingScore = Math.min(currentMatchState.getGamesA(), currentMatchState.getGamesB()) + 7;
                        } else
                            remainingScore = Math.min(currentMatchState.getGamesA(), currentMatchState.getGamesB()) + 6;
                        possibleMinLine = currentLine + remainingScore + Math.max(nSetInMatch - currentSet, 0) * 6;
                    }

                    if (possibleMinLine > Double.valueOf(market.getLineId())) {
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(possibleMinLine,
                                        market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, possibleMinLine, true);
                    } else
                        return null;
                }
            case "FT:A:DFOU":
                if (currentMatchState.isMatchCompleted()) {
                    int n = currentMatchState.getTotalDoubleFault(TeamId.A);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:B:DFOU":
                if (currentMatchState.isMatchCompleted()) {
                    int n = currentMatchState.getTotalDoubleFault(TeamId.B);
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
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
                    } else {
                        int currentLine = Math.abs(currentMatchState.getGamesA() - currentMatchState.getGamesB());
                        int remainingScore = 0;
                        int possibleMinLine = 0;
                        if (currentMatchState.getGamesA() >= 5 && currentMatchState.getGamesB() >= 5) {
                            remainingScore = 7 - Math.min(currentMatchState.getGamesA(), currentMatchState.getGamesB());
                        } else
                            remainingScore = 6 - Math.min(currentMatchState.getGamesA(), currentMatchState.getGamesB());
                        possibleMinLine = currentLine + remainingScore;
                        if (possibleMinLine < Math.abs(Double.valueOf(market.getLineId()))) {
                            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(possibleMinLine,
                                            market.getLineId());
                            return new CheckMarketResultedOutcome(winningSelection, possibleMinLine, true);
                        } else
                            return null;
                    }
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

            case "P:RTSG":
                marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
                int gamesA = ((TennisMatchState) currentMatchState).getGamesA();
                int gamesB = ((TennisMatchState) currentMatchState).getGamesB();
                currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                                gamesA > gamesB ? gamesA : gamesB, ((TennisMatchState) currentMatchState).getPointNo());

                if (currentStateSeqNo <= marketSeqNo) {
                    if (currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome();
                    } else
                        return null;
                }
                winningSelection = (gamesA > gamesB ? "Player A" : "Player B");
                return new CheckMarketResultedOutcome(winningSelection);

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
                } else {
                    int currentSetA = currentMatchState.getSetsA();
                    int currentSetB = currentMatchState.getSetsB();
                    int possibleMinLine = Math.min(currentSetA, currentSetB) + nSetInMatch;
                    if (possibleMinLine > Double.valueOf(market.getLineId())) {
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(possibleMinLine,
                                        market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, possibleMinLine, true);
                    } else
                        return null;
                }

            case "FT:OU:A": // Match total games A
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalGamesA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    n = currentMatchState.getTotalGamesA() + currentMatchState.getGamesA();
                    double marketLineId = Double.parseDouble(market.getLineId());
                    if (n > marketLineId) {
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n, true);
                    } else {
                        return null;
                    }
                }
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
                } else {
                    n = currentMatchState.getTotalGamesB() + currentMatchState.getGamesB();
                    double marketLineId = Double.parseDouble(market.getLineId());
                    if (n > marketLineId) {
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n, true);
                    } else {
                        return null;
                    }
                }
            case "G:ML": // Winner of game
                return checkGameOrTieBreakWinnerResulted();

            case "G:PW": // Winner of point
                return checkPointMktResulted();

            case "G:TBPW": // Winner of point
                return checkTiebreakPointMktResulted();

            case "FT:PW5G": // First player to win 5 games in set
                return checkFirstToFiveGamesResulted();

            case "P:TBML": // Current set tie break winner
                if (this.marketSetNo < currentMatchState.getSetNo())
                    return checkOnlyTieBreakWinnerResulted();
                else
                    return null;

            case "P:TBCS": // Tie break correct score
                return checkTieBreakCorrectScoreResulted();

            case "FT:CTBCS": // Tie break correct score
                return checkSuperTieBreakCorrectScoreResulted(market);

            case "P:ML": // Set Money Line
                return checkSetWinnerOutcome();

            case "P:CS": // set correct score
                return checkSetCorrectScoreOutcome(market);

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

            case "G:CS1": // Current game correct score
                return checkGameOrTieBreakCorrectScoreResulted(market.getSelectionsProbs().keySet());

            case "G:CS": // Current game correct score
                return checkSetGameOrTieBreakCorrectScoreResulted(market.getSelectionsProbs().keySet());
            case "FT:WM:A":
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > currentMatchState.getSetsB()) {
                        int total = currentMatchState.getTotalGamesA() - currentMatchState.getTotalGamesB();
                        if (total <= 0)
                            winningSelection = "Player A By 0 or Negative";
                        else if (total > 11)
                            winningSelection = "Player A By 12 or more";
                        else
                            winningSelection = "Player A By " + total;
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        return new CheckMarketResultedOutcome("Player B won");
                    }
                } else
                    return null;

            case "FT:WM:B":
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsB() > currentMatchState.getSetsA()) {
                        int total = currentMatchState.getTotalGamesB() - currentMatchState.getTotalGamesA();
                        if (total <= 0)
                            winningSelection = "Player B By 0 or Negative";
                        else if (total > 11)
                            winningSelection = "Player B By 12 or more";
                        else
                            winningSelection = "Player B By " + total;
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        return new CheckMarketResultedOutcome("Player A won");
                    }
                } else
                    return null;
            case "FT:BPTW": // Both Players to win a Set
                boolean resulted = currentMatchState.getSetsA() != 0 && currentMatchState.getSetsB() != 0;
                if (currentMatchState.isMatchCompleted()) {
                    if (currentMatchState.getSetsA() > 0 && currentMatchState.getSetsB() > 0)
                        return new CheckMarketResultedOutcome("Yes");
                    else
                        return new CheckMarketResultedOutcome("No");
                } else {
                    if (resulted) {
                        return new CheckMarketResultedOutcome("Yes");
                    } else
                        return null;
                }
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

            case "FT:AML": // Player with most aces
                if (currentMatchState.isMatchCompleted()) {
                    if ((currentMatchState.getTotalAcesA() > currentMatchState.getTotalAcesB())) {
                        return new CheckMarketResultedOutcome("Player A");
                    } else if ((currentMatchState.getTotalAcesA() > currentMatchState.getTotalAcesB())) {
                        return new CheckMarketResultedOutcome("Player B");
                    }
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "FT:AN": // Player with Next ace
                if ((currentMatchState.getTotalAcesA() + currentMatchState.getTotalAcesB()) >= marketSetNo) {
                    if ((currentMatchState.getTotalAcesA() > previousMatchState.getTotalAcesA())) {
                        return new CheckMarketResultedOutcome("Player A");
                    }
                    if ((currentMatchState.getTotalAcesB() > previousMatchState.getTotalAcesB())) {
                        return new CheckMarketResultedOutcome("Player B");
                    }
                } else if (currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                } else
                    return null;

            case "FT:AOU": // Match total games
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalAcesA() + currentMatchState.getTotalAcesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:A:AOU": // Match total games
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalAcesA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;
            case "FT:B:AOU": // Match total games
                if (currentMatchState.isMatchCompleted()) {
                    n = currentMatchState.getTotalAcesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            default:
                throw new IllegalArgumentException(
                                "Market type missing from TennisMatchResultMarkets: " + market.getType());

        }

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
                return new CheckMarketResultedOutcome(winningSelection, n, true);
            } else if (currentMatchState.isMatchCompleted()) {
                String winningSelection = "Void";
                return new CheckMarketResultedOutcome(winningSelection, n, true);
            } else if (n == 12 && market.getLineId().equals("12.5")) {
                String winningSelection = "Over";
                return new CheckMarketResultedOutcome(winningSelection, 13, true);
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
        return new CheckMarketResultedOutcome(winningSelection, n, true);

    }

    private CheckMarketResultedOutcome checkSetOverUnderOutCome3Way(Market market) {

        int marketSeqNo = convertSeqNoToIndex(marketSetNo, 0, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (currentStateSeqNo <= marketSeqNo) {
            if (currentMatchState.isMatchCompleted())
                return new CheckMarketResultedOutcome(); // when void happens
            else { // Early resulting if number of games reaches above 10 since winning selection is now known
                int gameScoreA = currentMatchState.getGamesA();
                int gameScoreB = currentMatchState.getGamesB();
                if (gameScoreA + gameScoreB > 10 && currentStateSeqNo == marketSeqNo) {
                    String winningSelection = "Over 10.5";
                    return new CheckMarketResultedOutcome(winningSelection);
                }
            }
            return null;
        } else {
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

        List<String> loseSelections = new ArrayList<String>();
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
                        loseSelections.add("Under 4.5");
                if (market.getSelections().containsKey("Over 5.5"))
                    if (gameScoreA >= 2 && gameScoreB >= 2)
                        loseSelections.add("Under 5.5");
                if (market.getSelections().containsKey("Over 6.5"))
                    if (gameScoreA >= 3 && gameScoreB >= 3) {
                        winSelections.add("Over 4.5");
                        winSelections.add("Over 5.5");
                        winSelections.add("Over 6.5");
                        return new CheckMarketResultedOutcome(winSelections);
                    }
                if (!loseSelections.isEmpty())
                    return new CheckMarketResultedOutcome(loseSelections, false);
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

    private CheckMarketResultedOutcome checkSetCorrectScoreOutcome(Market market) {
        String winningSelection;
        /*
         * check to see if we have gone beyond the set in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(), 0, 0);
        if (matchRetired) {
            if (currentStateSeqNo == marketSeqNo) {
                ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getGamesA(),
                                currentMatchState.getGamesB(), pCSFullSelections);
                if (fastFour)
                    loseSelections = resultSelectionsForCS(currentMatchState.getGamesA(), currentMatchState.getGamesB(),
                                    pCSFullSelectionsFast4);
                System.out.println(loseSelections);
                if (loseSelections != null && loseSelections.size() > 0)
                    return new CheckMarketResultedOutcome(loseSelections, true, true);
                else
                    return new CheckMarketResultedOutcome();
            } else {
                return new CheckMarketResultedOutcome();
            }
        }
        if (currentStateSeqNo < marketSeqNo)
            return null;
        else if (currentStateSeqNo == marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else if (partialResulting && (marketGameNo < currentMatchState.getGameNo() - 1)) {
                /*
                 * Check some of the selections TEMP FIX not do partial
                 */
                ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getGamesA(),
                                currentMatchState.getGamesB(), market.getSelections().keySet());
                if (loseSelections != null && loseSelections.size() > 0)
                    return new CheckMarketResultedOutcome(loseSelections, false);
                // else
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

    private ArrayList<String> resultSelectionsForCS(int pointsA, int pointsB, Set<String> openedSelections) {
        ArrayList<String> selectionsResulted = new ArrayList<String>();
        String[] selectionScores = null;
        for (String selection : openedSelections) {
            if (selection.toUpperCase().contains("PLAYER") && !selection.toUpperCase().contains("ANY OTHER")) {

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
                        if (!selectionsResulted.contains(selection.toString())) {
                            selectionsResulted.add(selection);
                        }
                    }
                } else {
                    if (teamScore[1].equals("A")) {
                        selectionA = Integer.valueOf(teamScore[2]);
                        String[] temp = selectionScores[1].split(" ");
                        String value = temp.length == 1 ? temp[0] : temp[temp.length - 1];
                        selectionB = Integer.valueOf(value);
                        // System.out.println(selectionA + ", " + value + ", " + selectionB);
                    } else if (teamScore[1].equals("B")) {
                        selectionB = Integer.valueOf(teamScore[2]);
                        String[] temp = selectionScores[1].split(" ");
                        String value = temp.length == 1 ? temp[0] : temp[temp.length - 1];
                        selectionA = Integer.valueOf(value);
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
                if (currentMatchState.getGamesA() > currentMatchState.getGamesB()) {
                    return new CheckMarketResultedOutcome(
                                    "Player A " + currentMatchState.getGamesA() + "-" + currentMatchState.getGamesB());
                } else if (currentMatchState.getGamesA() < currentMatchState.getGamesB()) {
                    return new CheckMarketResultedOutcome(
                                    "Player B " + currentMatchState.getGamesB() + "-" + currentMatchState.getGamesA());
                } else {
                    if (currentMatchState.isMatchCompleted()) {
                        int totalSetsPlayed = currentMatchState.getSetsA() + currentMatchState.getSetsB();
                        int gamesA = currentMatchState.getGameScoreInSetN(totalSetsPlayed - 1).A;
                        int gamesB = currentMatchState.getGameScoreInSetN(totalSetsPlayed - 1).B;
                        if (gamesA > gamesB) {
                            return new CheckMarketResultedOutcome("Player A + " + gamesA + "-" + gamesB);
                        } else {
                            return new CheckMarketResultedOutcome("Player B + " + gamesB + "-" + gamesA);
                        }
                    }
                    return new CheckMarketResultedOutcome(
                                    currentMatchState.getGamesA() + "-" + currentMatchState.getGamesB());
                }
            } else
                return null;
        }
        int setNo = marketSetNo - 1;
        int gameScoreA = currentMatchState.getGameScoreInSetN(setNo).A;
        int gameScoreB = currentMatchState.getGameScoreInSetN(setNo).B;
        if (currentMatchState.getGameNo() == 1) {

            if (gameScoreA == 6 && gameScoreB == 0) {
                return new CheckMarketResultedOutcome("Player A 6-0");
            } else if (gameScoreA == 0 && gameScoreB == 6) {
                return new CheckMarketResultedOutcome("Player B 6-0");
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
            winningSelection = "6-4 or 7-5 or Other result";
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
        if (currentStateSeqNo < marketSeqNo) {
            if ((currentStateSeqNo + convertSeqNoToIndex(1, 0, 0)) == marketSeqNo) {
                if (matchRetired) {
                    return new CheckMarketResultedOutcome();
                }
            }
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome();
            } else
                return null;
        } else if (currentStateSeqNo == marketSeqNo) {
            if (matchRetired) {
                if (matchRetireHome)
                    winningSelection = "A";
                else
                    winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (currentMatchState.isMatchCompleted()) {
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
    // MethodName.print();
    // String sequenceId = "S1.3.12";
    // parseSequenceId(sequenceId);
    // sequenceId = "S1.3";
    // parseSequenceId(sequenceId);
    // sequenceId = "";
    // parseSequenceId(sequenceId);
    // }

    private CheckMarketResultedOutcome checkSuperTieBreakCorrectScoreResulted(Market market) {
        /*
         * check to see whether the game was played
         */
        int pointsA = ((TennisMatchState) currentMatchState).getSuperTieBreak().getPointsA();
        int pointsB = ((TennisMatchState) currentMatchState).getSuperTieBreak().getPointsB();

        if (currentMatchState.isMatchCompleted()) {
            String wonTeam = "";
            if (pointsA > 10 || pointsB > 10)
                wonTeam = pointsA > pointsB ? "Player A Any Other score" : "Player B Any Other score";
            else
                wonTeam = (pointsA > pointsB ? ("Player A " + pointsA + "-" + pointsB)
                                : ("Player B " + pointsB + "-" + pointsA));

            return new CheckMarketResultedOutcome(wonTeam); // when void happens
        } else if (partialResulting) {
            /*
             * Check some of the selections
             */
            ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getPointsA(),
                            currentMatchState.getPointsB(), market.getSelectionsProbs().keySet());
            if (loseSelections != null && loseSelections.size() > 0)
                return new CheckMarketResultedOutcome(loseSelections, false);
            else
                return null;
        } else
            return null;

        //
        //
        //
        //
        //
        //
        // String winningSelection;
        // if (pointsA <= 4 && pointsB <= 4)
        // return new CheckMarketResultedOutcome();
        // if (pointsA > pointsB) {
        // if (pointsB < 9)
        // winningSelection = String.format(teamOrPlayer + "A %d-%d", pointsA,
        // pointsB);
        // else
        // winningSelection = teamOrPlayer + " A Any other score";
        // } else {
        // if (pointsA < 9)
        // winningSelection = String.format(teamOrPlayer + "B %d-%d", pointsB,
        // pointsA);
        // else
        // winningSelection = teamOrPlayer + " B Any other score";
        // }
        //
        // return new CheckMarketResultedOutcome(winningSelection);
    }

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
        if (pointsA > pointsB) {
            if (pointsB < 6)
                winningSelection = String.format("Player A %d-%d", pointsA, pointsB);
            else
                winningSelection = "Player A Any other Score";
        } else {
            if (pointsA < 6)
                winningSelection = String.format("Player B %d-%d", pointsB, pointsA);
            else
                winningSelection = "Player B Any other Score";
        }

        return new CheckMarketResultedOutcome(winningSelection);
    }

    private CheckMarketResultedOutcome checkGameOrTieBreakCorrectScoreResulted(Set<String> openedSelections) {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo < marketSeqNo) {
            return null;
        } else if (currentStateSeqNo == marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else if ((marketPointNo < currentMatchState.getPointNo() - 1) && partialResulting) {
                /*
                 * Check some of the selections
                 */
                ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getPointsA(),
                                currentMatchState.getPointsB(), openedSelections);
                if (loseSelections != null && loseSelections.size() > 0)
                    return new CheckMarketResultedOutcome(loseSelections, false);
                else
                    return null;
            } else
                return null;
        }
        /*
         * check to see whether the game was played
         */
        int previousStateSeqNo = convertSeqNoToIndex(((TennisMatchState) previousMatchState).getSetNo(),
                        ((TennisMatchState) previousMatchState).getGameNo(), 0);
        if (previousStateSeqNo == marketSeqNo) {
            int pointsA = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsA();
            int pointsB = ((TennisMatchState) currentMatchState).getLastGameOrTieBreakPointsB();
            String winningSelection = String.format("%d-%d", pointsA, pointsB);
            return new CheckMarketResultedOutcome(winningSelection);
        } else {
            /*
             * game was never played so return void
             */
            return new CheckMarketResultedOutcome();
        }
    }

    private CheckMarketResultedOutcome checkSetGameOrTieBreakCorrectScoreResulted(Set<String> openedSelections) {
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo < marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // When Void happens
            } else {
                return null;
            }
        } else if (currentStateSeqNo == marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // when void happens
            } else if (partialResulting && (marketPointNo < currentMatchState.getPointNo() - 1)) {
                /*
                 * Check some of the selections
                 */
                int pointsA = ((TennisMatchState) currentMatchState).getPointsA();
                int pointsB = ((TennisMatchState) currentMatchState).getPointsB();
                if (pointsA >= 3 && pointsB >= 3) {
                    return null;
                } else {
                    ArrayList<String> loseSelections = resultSelectionsForCS(currentMatchState.getPointsA(),
                                    currentMatchState.getPointsB(), openedSelections);
                    if (loseSelections != null && loseSelections.size() > 0)
                        return new CheckMarketResultedOutcome(loseSelections, false);
                    else
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
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[0];
                } else {
                    winningSelection = setXgameYCorrectScoreList[0];

                }
            }
            if (pointsA == 4 && pointsB == 1) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[1];
                } else {
                    winningSelection = setXgameYCorrectScoreList[1];

                }
            }
            if (pointsA == 4 && pointsB == 2) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[2];
                } else {
                    winningSelection = setXgameYCorrectScoreList[2];

                }
            }
            if (pointsA == 5 && pointsB == 3) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[3];
                } else {
                    winningSelection = setXgameYCorrectScoreList[3];

                }
            }

            if (pointsA == 0 && pointsB == 4) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[4];
                } else {
                    winningSelection = setXgameYCorrectScoreList[4];

                }
            }
            if (pointsA == 1 && pointsB == 4) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[5];
                } else {
                    winningSelection = setXgameYCorrectScoreList[5];

                }
            }
            if (pointsA == 2 && pointsB == 4) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[6];
                } else {
                    winningSelection = setXgameYCorrectScoreList[6];

                }
            }
            if (pointsA == 3 && pointsB == 5) {
                if (psMarkets) {
                    winningSelection = setXgameYCorrectScoreListPS[7];
                } else {
                    winningSelection = setXgameYCorrectScoreList[7];

                }
            }

            if (((TennisMatchState) currentMatchState).isNoAdvantageGameFormat()) {
                if (pointsA == 4 && pointsB == 3) {
                    if (psMarkets) {
                        winningSelection = setXgameYCorrectScoreListPS[3];
                    } else {
                        winningSelection = setXgameYCorrectScoreList[3];

                    }
                }
                if (pointsA == 3 && pointsB == 4) {
                    if (psMarkets) {
                        winningSelection = setXgameYCorrectScoreListPS[7];
                    } else {
                        winningSelection = setXgameYCorrectScoreList[7];

                    }
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

    private CheckMarketResultedOutcome checkTiebreakPointMktResulted() {
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
                winningSelection = "Player A";
            else
                winningSelection = "Player B";
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
        if (previousMatchState.isInSuperTieBreak() && currentMatchState.isMatchCompleted()) {
            if (currentMatchState.getSetsA() > currentMatchState.getSetsB()) {
                winningSelection = "A";
            } else {
                winningSelection = "B";
            }

            return new CheckMarketResultedOutcome(winningSelection);

        } else if (previousMatchState.isInTieBreak()) {
            if (!(currentMatchState.isInTieBreak())) {
                if (((TennisMatchState) currentMatchState).getLastGamePlayedOutcome() == TeamId.A) {
                    winningSelection = "A";
                } else {
                    winningSelection = "B";
                }
                return new CheckMarketResultedOutcome(winningSelection);
            } else if (currentMatchState.isInSuperTieBreak()) {
                if (((TennisMatchState) currentMatchState).getLastGamePlayedOutcome() == TeamId.B) {
                    winningSelection = "B";
                } else {
                    winningSelection = "A";
                }
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        return new CheckMarketResultedOutcome();
    }

    private CheckMarketResultedOutcome checkGameOrTieBreakWinnerResulted() {
        /*
         * check to see if we have gone beyond the game in question
         */
        int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentStateSeqNo = convertSeqNoToIndex(((TennisMatchState) currentMatchState).getSetNo(),
                        ((TennisMatchState) currentMatchState).getGameNo(), 0);
        if (currentStateSeqNo <= marketSeqNo) {
            if (currentMatchState.isMatchCompleted()) {
                return new CheckMarketResultedOutcome(); // When Void happens
            } else {
                return null;
            }
        }
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

    private boolean inList(String marketType, List<String> marketTypeList) {
        if (marketTypeList == null)
            return false;
        for (String marketType2 : marketTypeList)
            if (marketType.equals(marketType2))
                return true;
        return false;
    }
}
