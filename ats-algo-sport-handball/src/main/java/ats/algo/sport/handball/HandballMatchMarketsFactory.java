package ats.algo.sport.handball;

import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;

public class HandballMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Football if we
     * need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Jin
     * 
     */
    // private ThreeWayStatistic matchDoubleChance;
    private ThreeWayStatistic periodDoubleChance;
    private ThreeWayStatistic matchWinner;
    private ThreeWayStatistic firstHalfWinner;
    private ThreeWayStatistic secondHalfWinner;
    private ThreeWayStatistic drawNoBet;
    private TwoWayStatistic periodDrwaNoBet;
    private TotalStatistic goalsTotal;
    private TotalStatistic currentHalfGoalsTotal;
    private TotalStatistic goalsTotalH;
    private TotalStatistic goalsTotalA;
    private HandicapStatistic goalsHandicap;
    private HandicapStatistic currentHalfGoalsHandicap;
    private TwoWayStatistic nextGoal;
    private static final int maxNoGoalsPerTeam = 100;
    private static int HALFTIMEINSECONDS = 1200;
    boolean creat2nHalfMarkets = false;

    HandballMatchMarketsFactory(HandballMatchState matchState) {
        /* Starting point to generate markets */
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String goalSequenceId = matchState.getSequenceIdForGoal(0);
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        HandballMatchFormat fMatchFormat = (HandballMatchFormat) matchState.getMatchFormat();
        HALFTIMEINSECONDS = fMatchFormat.getNormalTimeMinutes() * 60 / 2;

        matchWinner = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, true, "M", "A", "B", "Draw");

        goalsTotal = new TotalStatistic("FT:OU", "Total Goals", MarketGroup.GOALS, true, matchSequenceId,
                        2 * maxNoGoalsPerTeam);

        goalsTotalH = new TotalStatistic("FT:A:OU", "Home Team Totals ", MarketGroup.GOALS, true, matchSequenceId,
                        maxNoGoalsPerTeam);
        goalsTotalA = new TotalStatistic("FT:B:OU", "Away Team Totals ", MarketGroup.GOALS, true, matchSequenceId,
                        maxNoGoalsPerTeam);
        goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap", MarketGroup.GOALS, true, matchSequenceId,
                        maxNoGoalsPerTeam);
        drawNoBet = new ThreeWayStatistic("FT:DNB", "Draw No Bet", MarketGroup.GOALS, true, matchSequenceId, "A", "B",
                        "Draw");

        String marketDescription = String.format("Next Goal (Goal No. %d)", matchState.getGoalNo());

        nextGoal = new TwoWayStatistic("OT:NG", marketDescription, true, goalSequenceId, "A", "B");

        int periodNo = matchState.getPeriodNo();
        Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Total Goals";
                generateMarket = true;
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "Second Half Total Goals";
                generateMarket = true;
                break;

            case 3: // extra time
                marketDescription = "extra time market suspend";
                generateMarket = false;
                break;

            case 4:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;
            case 6:
                marketDescription = "Over time periods end, all market suspend";
                generateMarket = false;
                break;

            case 7:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;

            default:
                marketDescription = "Pre match";
                generateMarket = false;
        }

        currentHalfGoalsTotal = new TotalStatistic("P:OU", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam);

        // matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Match Double Chance", generateMarket, matchSequenceId,
        // "AX",
        // "BX", "AB");
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Draw No Bet";
                generateMarket = true;
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "Second Half Draw No Bet";
                generateMarket = true;
                break;

            case 4:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;
            case 6:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        periodDrwaNoBet = new TwoWayStatistic("P:DNB", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, "A", "B");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Double Chance";
                generateMarket = true;
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "Second Half Double Chance";
                generateMarket = true;
                break;

            case 4:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;
            case 6:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        periodDoubleChance = new ThreeWayStatistic("P:DBLC", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, "AX", "BX", "AB");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Result";
                generateMarket = true;
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "Second Half Result";
                generateMarket = false;
                break;

            case 4:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;
            case 6:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        firstHalfWinner = new ThreeWayStatistic("P:AXB", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, "A", "B", "Draw");

        creat2nHalfMarkets = matchState.getElapsedTimeSecs() > HALFTIMEINSECONDS
                        && matchState.getElapsedTimeSecs() < matchState.getNormalHalfsSecs() * 2;
        secondHalfWinner = new ThreeWayStatistic("P:AXB", "Second Half Result", MarketGroup.GOALS, creat2nHalfMarkets,
                        String.format("P%d", 2), "A", "B", "Draw");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Handicap" + "";
                generateMarket = true;
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "Second Half Handicap";
                generateMarket = true;
                break;

            case 4:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
                break;
            case 6:
                marketDescription = "Shoot out all market suspend";
                generateMarket = false;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        currentHalfGoalsHandicap = new HandicapStatistic("P:SPRD", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam);

    }

    void updateStats(HandballMatchState matchState, HandballMatchFacts matchFacts) {
        int goalsA = matchState.getGoalsA();
        int goalsB = matchState.getGoalsB();

        // int goalsA = (int) matchState.getSimuGoalsA();
        // int goalsB = (int) matchState.getSimuGoalsB();

        TeamId periodWinnerID = TeamId.UNKNOWN;
        TeamId teamId = TeamId.UNKNOWN;
        if (goalsA > goalsB)
            teamId = TeamId.A;
        if (goalsA < goalsB)
            teamId = TeamId.B;
        matchWinner.increment(teamId);
        // int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        // int normalTimeGoalsB = matchState.getNormalTimeGoalsB();

        // if (normalTimeGoalsA >= normalTimeGoalsB) {
        // teamId = TeamId.A;
        // matchDoubleChance.increment(teamId);
        // }
        // if (normalTimeGoalsA <= normalTimeGoalsB) {
        // teamId = TeamId.B;
        // matchDoubleChance.increment(teamId);
        // }
        // if (normalTimeGoalsA < normalTimeGoalsB || normalTimeGoalsA > normalTimeGoalsB) {
        // teamId = TeamId.UNKNOWN;
        // matchDoubleChance.increment(teamId);
        // }

        if (goalsA > goalsB) {
            teamId = TeamId.A;
            drawNoBet.increment(teamId);
        }
        if (goalsA < goalsB) {
            teamId = TeamId.B;
            drawNoBet.increment(teamId);
        }
        if (goalsA == goalsB) {
            teamId = TeamId.UNKNOWN;
            drawNoBet.increment(teamId);
        }
        // correctGoalScore.increment(goalsA, goalsB);
        goalsTotal.increment(goalsA + goalsB);
        goalsTotalH.increment(goalsA);
        goalsTotalA.increment(goalsB);
        goalsHandicap.increment(goalsA - goalsB);

        if (matchFacts.getNextToScore() != TeamId.UNKNOWN)
            nextGoal.increment(matchFacts.getNextToScore() == TeamId.A);

        int periodGoalsA = matchFacts.getCurrentPeriodGoalsA();
        int periodGoalsB = matchFacts.getCurrentPeriodGoalsB();

        if (periodGoalsA != periodGoalsB)
            periodDrwaNoBet.increment(periodGoalsA > periodGoalsB);

        currentHalfGoalsTotal.increment(periodGoalsA + periodGoalsB);

        if (periodGoalsA > periodGoalsB) {
            periodWinnerID = TeamId.A;
        } else if (periodGoalsA < periodGoalsB) {
            periodWinnerID = TeamId.B;
        } else {
            // periodWinnerID = TeamId.UNKNOWN;
        }
        if (periodGoalsA >= periodGoalsB) {
            teamId = TeamId.A;
            periodDoubleChance.increment(teamId);
        }
        if (periodGoalsA <= periodGoalsB) {
            teamId = TeamId.B;
            periodDoubleChance.increment(teamId);
        }
        if (periodGoalsA < periodGoalsB || periodGoalsA > periodGoalsB) {
            teamId = TeamId.UNKNOWN;
            periodDoubleChance.increment(teamId);
        }
        currentHalfGoalsHandicap.increment(periodGoalsA - periodGoalsB);
        firstHalfWinner.increment(periodWinnerID);

        if (creat2nHalfMarkets) {
            int period2GoalsA = matchState.getGoalsSecondHalfA();
            int period2GoalsB = matchState.getGoalsSecondHalfB();
            periodWinnerID = TeamId.UNKNOWN;
            if (period2GoalsA > period2GoalsB) {
                periodWinnerID = TeamId.A;
            } else if (period2GoalsA < period2GoalsB) {
                periodWinnerID = TeamId.B;
            } else {
                // periodWinnerID = TeamId.UNKNOWN;
            }
            secondHalfWinner.increment(periodWinnerID);
        }

    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        /*
         * Derive the no of sets played in match from the set score market
         */
        @SuppressWarnings("unused")
        HandballMatchState icehockeyMatchState = (HandballMatchState) matchState;
        Market sourceMarket = markets.get("FT:AXB");
        double[] probs = {0, 0, 0}; // A , B, X
        for (Entry<String, Double> e : sourceMarket.getSelectionsProbs().entrySet()) {
            String selectionName = e.getKey();
            double prob = e.getValue();
            probs[parseNameForDoubleChanceResult(selectionName)] = prob;
        }

        Market market = new Market(MarketCategory.GENERAL, "FT:DBLC", "M", "Double Chance");

        market.setIsValid(((HandballMatchState) matchState).getElapsedTimeSecs() < 3000);
        market.put("AX", probs[0] + probs[2]);
        market.put("BX", probs[1] + probs[2]);
        market.put("AB", probs[0] + probs[1]);
        markets.addMarketWithShortKey(market);
        /*
         * Derive other markets
         */

    }

    /**
     *  
     * 
     *  
     *            
     *  
     */
    private int parseNameForDoubleChanceResult(String s) {
        int value = 3;
        switch (s) {
            case "A":
                value = 0;
                break;
            case "B":
                value = 1;
                break;
            case "Draw":
                value = 2;
                break;
        }
        return value;
    }

}
