package ats.algo.sport.icehockey;

import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;

public class IcehockeyMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Icehockey if we
     * need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Geoff
     * 
     */
    private TwoWayStatistic puckLine;
    private TwoWayStatistic moneyLine;
    @SuppressWarnings("unused")
    private ThreeWayStatistic overtimeWinner;
    @SuppressWarnings("unused")
    private ThreeWayStatistic overtimeDouleChance;

    private ThreeWayStatistic matchWinner;
    private ThreeWayStatistic fiveMinsWinner;
    // private ThreeWayStatistic matchDoubleChance;
    private CorrectScoreStatistic correctGoalScore;
    private CorrectScoreStatistic correctGoalScoreOT;
    private TotalStatistic goalsTotal;
    private TotalStatistic matchGoalsTotal;
    private TotalStatistic goalsTotalA;
    private TotalStatistic goalsTotalB;
    private TotalStatistic goalsTotalFTOTA;
    private TotalStatistic goalsTotalFTOTB;
    private TwoWayStatistic periodBothTeamToScore;
    private TwoWayStatistic fiveMinsIfGoal;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic goalsHandicap;
    private HandicapStatistic matchGoalsHandicap;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic overTimeNextGoal;
    private TotalStatistic goalsTotalCurrentPeriod;
    private ThreeWayStatistic periodWinner;
    private CorrectScoreStatistic periodCorrectGoalScore;
    private NWayStatistic timeOfFirstGoal;
    private TwoWayStatistic goalsTotalOddEven;
    private TwoWayStatistic goalsTotalOddEvenA;
    private TwoWayStatistic goalsTotalOddEvenB;

    private TwoWayStatistic bothTeamToScore;
    private TwoWayStatistic drawNoBet;
    private TwoWayStatistic homeNoBet;
    private TwoWayStatistic awayNoBet;
    private ThreeWayStatistic firstTeamToScore;
    private boolean createRaceTo2GoalsMarket;
    private ThreeWayStatistic raceTo2Goals;
    private boolean createRaceTo3GoalsMarket;
    private ThreeWayStatistic raceTo3Goals;

    private TwoWayStatistic overTimePossible;
    private static final int maxNoGoalsPerTeam = 40;
    private boolean allowGenerateAllSelection = false;
    private boolean overTime = false;
    private int marketsCreatedForPeriod = 1;

    IcehockeyMatchMarketsFactory(IcehockeyMatchState matchState) {
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        int currentTimeInSecs = matchState.getElapsedTimeSecs();
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String fiveMinsSequenceId = matchState.getSequenceIdForFiveMinsResult();
        String goalSequenceId = matchState.getSequenceIdForGoal(0);
        boolean isNormalTimeMatchFinished = !matchState.isNormalTimeMatchCompleted();
        int periodNo = matchState.getPeriodNo();
        Boolean generateMarketFT = false;
        Boolean generateMarketFTOT = false;
        Boolean generateMarketOT = false;
        Boolean inMatchNormalTime = true;
        boolean overTimePenaltyPossible = false;
        // Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 2: // second period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 3: // third period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 4: // extra time
                generateMarketFT = false;
                generateMarketFTOT = true;
                generateMarketOT = true;
                break;
            default:
                generateMarketFT = false;
                generateMarketFTOT = false;
                inMatchNormalTime = false;
                generateMarketOT = false;
        }
        IcehockeyMatchFormat clazz = (IcehockeyMatchFormat) ((IcehockeyMatchState) matchState).getMatchFormat();
        overTime = clazz.getExtraTimeMinutes() > 0;
        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0))
            overTimePenaltyPossible = true;

        matchWinner = new ThreeWayStatistic("FT:AXB", "Match Winner", generateMarketFT, matchSequenceId, "A", "B",
                        "Draw");

        puckLine = new TwoWayStatistic("FT:ML", "Money Line", overTimePenaltyPossible, matchSequenceId, "A", "B");
        moneyLine = new TwoWayStatistic("FTOT:ML", "Moneyline", overTimePenaltyPossible, matchSequenceId, "A", "B");
        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", generateMarketFT && overTimePenaltyPossible,
                        matchSequenceId, "Yes", "No");

        // if (!matchState.isNormalTimeMatchCompleted())
        // generateMarketFT = true;
        int fiveMinutesNo = matchState.getFiveMinsNo();
        String fiveMinutesTimeWindow = fiveMinutesNo * 5 + " - " + (fiveMinutesNo + 1) * 5;

        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", fiveMinutesTimeWindow + " Min Result",
                        generateMarketFT && inMatchNormalTime, fiveMinsSequenceId, "A", "B", "Draw");
        fiveMinsIfGoal = new TwoWayStatistic("FT:5MG", fiveMinutesTimeWindow + " Min Goal - Yes/No",
                        generateMarketFT && inMatchNormalTime, periodSequenceId, "Yes", "No");

        // matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", generateMarketFT&&inMatchNormalTime,
        // matchSequenceId, "AX",
        // "BX", "AB");
        correctGoalScore = new CorrectScoreStatistic("FT:CS", "Correct Score (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);
        correctGoalScoreOT = new CorrectScoreStatistic("FTOT:CS", "Correct Score (Including OT)", generateMarketFTOT,
                        matchSequenceId, maxNoGoalsPerTeam);
        String[] timeOfFirstGoalSelections = {"No Goal", "1-10", "11-20", "21-30", "31-40", "41-50", "51-60"};

        timeOfFirstGoal = new NWayStatistic("FT:TFG", "Time of 1st Goal", generateMarketFT && inMatchNormalTime,
                        matchSequenceId, timeOfFirstGoalSelections);
        // (String key, String description, Boolean isValidGivenMatchState, String sequenceId,
        // String[] selections)

        goalsTotal = new TotalStatistic("FT:OU", "Total Goals (full time only)", generateMarketFT && inMatchNormalTime,
                        matchSequenceId, 2 * maxNoGoalsPerTeam);
        if (overTime)
            matchGoalsTotal = new TotalStatistic("FTOT:OU", "Total Goals (full time & over time)",
                            generateMarketFT && inMatchNormalTime, matchSequenceId, 2 * maxNoGoalsPerTeam);
        goalsTotalA = new TotalStatistic("FT:A:OU", "Home Team Totals (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);

        goalsTotalB = new TotalStatistic("FT:B:OU", "Away Team Totals (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);

        goalsTotalFTOTA = new TotalStatistic("FTOT:A:OU", "Home Team Totals", generateMarketFT && inMatchNormalTime,
                        matchSequenceId, maxNoGoalsPerTeam);

        goalsTotalFTOTB = new TotalStatistic("FTOT:B:OU", "Away Team Totals", generateMarketFT && inMatchNormalTime,
                        matchSequenceId, maxNoGoalsPerTeam);

        goalsHandicap = new HandicapStatistic("FT:SPRD", "Goal Spread (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);
        if (overTime)
            matchGoalsHandicap = new HandicapStatistic("FTOT:SPRD", "Goal Spread (full time & over time)",
                            generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);
        matchWinnerEuroHandicap = new EuroHandicapStatistic("FT:EH", "Euro Handicap (full time only)",
                        generateMarketFT && inMatchNormalTime, matchSequenceId, maxNoGoalsPerTeam);

        String marketDescription = String.format("Next Goal ");
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, generateMarketFT, goalSequenceId, "A", "B",
                        "No goal");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();


        // switch (periodNo) {
        // case 0: // pre-match
        // case 1:
        // marketDescription = "1st Period Both Team To Score";
        // break;
        // // case 2: //half time
        // case 2: // second half
        // marketDescription = "2nd Period Both Team To Score";
        // break;
        //
        // case 3: // second half
        // marketDescription = "3rd Period Both Team To Score";
        // break;
        // default:
        // marketDescription = "";
        // }
        String marketSequenceId = String.format("P%d", 1);
        boolean creatPeriodMarkets = false;
        if (currentTimeInSecs <= 18 * 60) {
            marketDescription = "1st Period Both Team To Score";
            marketSequenceId = String.format("P%d", 1);
            marketsCreatedForPeriod = 1;
            creatPeriodMarkets = true;
        } else if (currentTimeInSecs <= 38 * 60) {
            marketDescription = "2nd Period Both Team To Score";
            marketSequenceId = String.format("P%d", 2);
            marketsCreatedForPeriod = 2;
            creatPeriodMarkets = true;
        } else if (currentTimeInSecs <= 58 * 60) {
            marketDescription = "3rd Period Both Team To Score";
            marketSequenceId = String.format("P%d", 3);
            marketsCreatedForPeriod = 3;
            creatPeriodMarkets = true;
        }

        generateMarketFT = (matchState.getMatchPeriod() != IcehockeyMatchPeriod.AT_FIRST_PERIOD_END
                        && matchState.getMatchPeriod() != IcehockeyMatchPeriod.AT_SECOND_PERIOD_END
                        && matchState.getMatchPeriod() != IcehockeyMatchPeriod.IN_EXTRA_TIME
                        && matchState.getMatchPeriod() != IcehockeyMatchPeriod.IN_SHOOTOUT
                        && (matchState.getCurrentPeriodGoalsA() == 0 || matchState.getCurrentPeriodGoalsB() == 0));
        periodBothTeamToScore = new TwoWayStatistic("P:BTTS", marketDescription, creatPeriodMarkets, marketSequenceId,
                        "Yes", "No");

        if (currentTimeInSecs <= 18 * 60) {
            marketDescription = "1st Period Correct Score";
            marketSequenceId = String.format("P%d", 1);
        } else if (currentTimeInSecs <= 38 * 60) {
            marketDescription = "2nd Period Correct Score";
            marketSequenceId = String.format("P%d", 2);
        } else if (currentTimeInSecs <= 58 * 60) {
            marketDescription = "3rd Period Correct Score";
            marketSequenceId = String.format("P%d", 3);
        }
        periodCorrectGoalScore = new CorrectScoreStatistic("P:CS", marketDescription, creatPeriodMarkets,
                        marketSequenceId, maxNoGoalsPerTeam);



        if (currentTimeInSecs <= 18 * 60) {
            marketDescription = "1st Period Winner";
            marketSequenceId = String.format("P%d", 1);
        } else if (currentTimeInSecs <= 38 * 60) {
            marketDescription = "2nd Period Winner";
            marketSequenceId = String.format("P%d", 2);
        } else if (currentTimeInSecs <= 58 * 60) {
            marketDescription = "3rd Period Winner";
            marketSequenceId = String.format("P%d", 3);
        }
        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription, creatPeriodMarkets, marketSequenceId, "A", "B",
                        "Draw");

        if (currentTimeInSecs <= 18 * 60) {
            marketDescription = "1st period total goals";
            marketSequenceId = String.format("P%d", 1);
        } else if (currentTimeInSecs <= 38 * 60) {
            marketDescription = "2nd period total goals";
            marketSequenceId = String.format("P%d", 2);
        } else if (currentTimeInSecs <= 58 * 60) {
            marketDescription = "3rd period total goals";
            marketSequenceId = String.format("P%d", 3);
        }
        goalsTotalCurrentPeriod = new TotalStatistic("P:OU", marketDescription, creatPeriodMarkets, marketSequenceId,
                        maxNoGoalsPerTeam);


        /*
         * Extra time markets
         */
        /*
         * new market for ML model
         */

        marketDescription = String.format("OT - Next Goal");
        overTimeNextGoal = new ThreeWayStatistic("OT:NG", marketDescription, generateMarketOT, goalSequenceId, "A", "B",
                        "No goal");
        Boolean generateMarket = true;
        bothTeamToScore = new TwoWayStatistic("FT:BTS", "Both Teams to Score", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        drawNoBet = new TwoWayStatistic("FT:DNB", "Draw no bet", MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "A",
                        "B");
        homeNoBet = new TwoWayStatistic("FT:A:NB", "Home no bet", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        "Draw", "B");
        awayNoBet = new TwoWayStatistic("FT:B:NB", "Away no bet", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        "A", "Draw");
        goalsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Goals Even/Odd", MarketGroup.GOALS, generateMarket, "M",
                        "Odd", "Even");
        goalsTotalOddEvenA = new TwoWayStatistic("FT:A:OE", "Home Team Total Goals Even/Odd", MarketGroup.GOALS,
                        generateMarket, "M", "Odd", "Even");
        goalsTotalOddEvenB = new TwoWayStatistic("FT:B:OE", "Away Team Total Goals Even/Odd", MarketGroup.GOALS,
                        generateMarket, "M", "Odd", "Even");
        firstTeamToScore = new ThreeWayStatistic("FT:FTSC", "First Team To Score", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted() && matchState.getGoalNo() == 1), "M", "A", "B",
                        "None");

        createRaceTo2GoalsMarket =
                        (matchState.checkTeamToXGoals(2, 0) == null && !matchState.isNormalTimeMatchCompleted());

        raceTo2Goals = new ThreeWayStatistic("FT:RTG2", "Race To 2 Goals", MarketGroup.GOALS, createRaceTo2GoalsMarket,
                        "M", "A", "B", "Neither");

        createRaceTo3GoalsMarket =
                        (matchState.checkTeamToXGoals(3, 0) == null && !matchState.isNormalTimeMatchCompleted());
        raceTo3Goals = new ThreeWayStatistic("FT:RTG3", "Race To 3 Goals", MarketGroup.GOALS, createRaceTo2GoalsMarket,
                        "M", "A", "B", "Neither");
    }

    void updateStats(IcehockeyMatchState matchState, IcehockeyMatchFacts matchFacts) {

        int goalsA = matchState.getGoalsA();
        int goalsB = matchState.getGoalsB();

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0)) {
            // System.out.println("increment at "+goalsA+" "+goalsB);

            puckLine.increment(goalsA > goalsB);
        }
        if (goalsA > goalsB)
            moneyLine.increment(true);
        else if (goalsA < goalsB)
            moneyLine.increment(false);

        int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        int normalTimeGoalsB = matchState.getNormalTimeGoalsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            matchWinner.increment(teamId);
        if (teamId.equals(TeamId.A)) {
            drawNoBet.increment(true);
            awayNoBet.increment(true);
        }
        if (teamId.equals(TeamId.B)) {
            drawNoBet.increment(false);
            homeNoBet.increment(false);
        }
        if (teamId.equals(TeamId.UNKNOWN)) {
            homeNoBet.increment(true);
            awayNoBet.increment(false);
        }
        int fiveMinsGoalsA = matchFacts.getFiveMinsGoalsA();
        int fiveMinsGoalsB = matchFacts.getFiveMinsGoalsB();
        teamId = TeamId.UNKNOWN;
        if (fiveMinsGoalsA > fiveMinsGoalsB)
            teamId = TeamId.A;
        if (fiveMinsGoalsA < fiveMinsGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsWinner.increment(teamId);
        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsIfGoal.increment((fiveMinsGoalsA + fiveMinsGoalsB) > 0);

        if (normalTimeGoalsA == normalTimeGoalsB) {
            overTimePossible.increment(true);
        } else {
            overTimePossible.increment(false);
        }
        if (normalTimeGoalsA > 0 && normalTimeGoalsB > 0)
            bothTeamToScore.increment(true);
        else
            bothTeamToScore.increment(false);

        int timeSlotScored = (int) Math.floor(matchState.getElapsedTimeSecsFirstGoal() / 600);
        boolean timeResidual = ((int) matchState.getElapsedTimeSecsFirstGoal() % 600) == 0;
        int timeFlagStart = 0;

        if (matchState.getElapsedTimeSecsFirstGoal() == 0 || matchState.getElapsedTimeSecsFirstGoal() > 3600) {
            timeFlagStart = 0;
        } else {
            if (timeResidual) {
                timeFlagStart = timeSlotScored;
            } else {
                timeFlagStart = timeSlotScored + 1;
            }
        }
        timeOfFirstGoal.increment(timeFlagStart);

        correctGoalScore.increment(normalTimeGoalsA, normalTimeGoalsB);
        correctGoalScoreOT.increment(goalsA, goalsB);
        goalsTotal.increment(normalTimeGoalsA + normalTimeGoalsB);
        if (overTime)
            matchGoalsTotal.increment(goalsA + goalsB);
        goalsTotalA.increment(normalTimeGoalsA);
        goalsTotalB.increment(normalTimeGoalsB);

        goalsTotalFTOTA.increment(goalsA);
        goalsTotalFTOTB.increment(goalsB);

        goalsTotalOddEven.increment(isOdd(goalsA + goalsB));
        goalsTotalOddEvenA.increment(isOdd(goalsA));
        goalsTotalOddEvenB.increment(isOdd(goalsB));
        goalsHandicap.increment(normalTimeGoalsA - normalTimeGoalsB);
        if (overTime)
            matchGoalsHandicap.increment(goalsA - goalsB);
        matchWinnerEuroHandicap.increment(normalTimeGoalsA - normalTimeGoalsB);
        nextGoal.increment(matchFacts.getNextToScore());
        overTimeNextGoal.increment(matchFacts.getNextToScore());


        int periodGoalsA = 0;
        int periodGoalsB = 0;
        if (marketsCreatedForPeriod == 1) {
            periodBothTeamToScore
                            .increment(matchState.getGoalsFirstPeriodA() > 0 && matchState.getGoalsFirstPeriodB() > 0);// matchFacts.getBothTeamToScoreCurrentPeriod()

            periodGoalsA = matchState.getGoalsFirstPeriodA();
            periodGoalsB = matchState.getGoalsFirstPeriodB();

        } else if (marketsCreatedForPeriod == 2) {
            periodBothTeamToScore.increment(
                            matchState.getGoalsSecondPeriodA() > 0 && matchState.getGoalsSecondPeriodB() > 0);// matchFacts.getBothTeamToScoreCurrentPeriod()

            periodGoalsA = matchState.getGoalsSecondPeriodA();
            periodGoalsB = matchState.getGoalsSecondPeriodB();

        } else if (marketsCreatedForPeriod == 3) {
            periodBothTeamToScore
                            .increment(matchState.getGoalsThirdPeriodA() > 0 && matchState.getGoalsThirdPeriodB() > 0);// matchFacts.getBothTeamToScoreCurrentPeriod()

            periodGoalsA = matchState.getGoalsThirdPeriodA();
            periodGoalsB = matchState.getGoalsThirdPeriodB();
        }

        periodCorrectGoalScore.increment(periodGoalsA, periodGoalsB);
        teamId = TeamId.UNKNOWN;
        if (periodGoalsA > periodGoalsB)
            teamId = TeamId.A;
        if (periodGoalsA < periodGoalsB)
            teamId = TeamId.B;

        periodWinner.increment(teamId);

        goalsTotalCurrentPeriod.increment(periodGoalsA + periodGoalsB);


        TeamId firstTeamScore = matchFacts.firstTeamToScore;
        firstTeamToScore.increment(firstTeamScore);

        if (createRaceTo2GoalsMarket) {
            TeamId teamTo2Goals = matchState.checkTeamToXGoals(2, 0);
            if (teamTo2Goals == null)
                raceTo2Goals.increment(TeamId.UNKNOWN);
            else
                switch (teamTo2Goals) {
                    case A:
                        raceTo2Goals.increment(TeamId.A);
                        break;
                    case B:
                        raceTo2Goals.increment(TeamId.B);
                        break;
                    default:
                        break;
                }
        }

        if (createRaceTo3GoalsMarket) {
            TeamId teamTo3Goals = matchState.checkTeamToXGoals(3, 0);
            if (teamTo3Goals == null)
                raceTo3Goals.increment(TeamId.UNKNOWN);
            else
                switch (teamTo3Goals) {
                    case A:
                        raceTo3Goals.increment(TeamId.A);
                        break;
                    case B:
                        raceTo3Goals.increment(TeamId.B);
                        break;
                    default:
                        break;
                }
        }
        /* cj added current period winner */
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        /*
         * Derive the no of sets played in match from the set score market
         */
        if (((IcehockeyMatchState) matchState).getMatchPeriod().equals(IcehockeyMatchPeriod.IN_EXTRA_TIME)
                        && ((IcehockeyMatchState) matchState).getMatchPeriod()
                                        .equals(IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END)
                        && ((IcehockeyMatchState) matchState).getMatchPeriod()
                                        .equals(IcehockeyMatchPeriod.AT_SHOOTOUT_END)
                        && ((IcehockeyMatchState) matchState).getMatchPeriod()
                                        .equals(IcehockeyMatchPeriod.IN_SHOOTOUT)) {
            Market sourceMarket = markets.get("FT:AXB");
            double[] probs = {0, 0, 0}; // A , B, X
            for (Entry<String, Double> e : sourceMarket.getSelectionsProbs().entrySet()) {
                String selectionName = e.getKey();
                double prob = e.getValue();
                probs[parseNameForDoubleChanceResult(selectionName)] = prob;
            }

            Market market = new Market(MarketCategory.GENERAL, "FT:DBLC", "M", "Double Chance");
            market.setIsValid(true);
            market.put("AX", probs[0] + probs[2]);
            market.put("BX", probs[1] + probs[2]);
            market.put("AB", probs[0] + probs[1]);
            markets.addMarketWithShortKey(market);

            /*
             * Derive other markets
             */
        }

        /* Winning margin market creation */

        Market csSourceMarket = markets.get("FTOT:CS");
        Map<String, Double> csGrid = csSourceMarket.getSelectionsProbs();
        double[][] scoreGrid;
        scoreGrid = creatScoreGrid(csGrid);
        Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:WM", "M", "Winning Margin");
        market.setIsValid(true);
        double[] probs = generateWinningMarginProbsFromScoreGrid(scoreGrid);
        market.put("A 1", probs[0]);
        market.put("A 2", probs[1]);
        market.put("A 3", probs[2]);
        market.put("A 4", probs[3]);
        market.put("A 5+", probs[4]);
        market.put("B 1", probs[5]);
        market.put("B 2", probs[6]);
        market.put("B 3", probs[7]);
        market.put("B 4", probs[8]);
        market.put("B 5+", probs[9]);
        markets.addMarketWithShortKey(market);

        String marketDescription = "Match Result and Both Team To Score";
        market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:AXBBTS", "M", marketDescription);
        market.setIsValid(true);
        probs = generateMatchResultAndBTSProbs(scoreGrid);
        marketDescription = "BY";
        market.put(marketDescription, probs[0]);
        marketDescription = "DY";
        market.put(marketDescription, probs[1]);
        marketDescription = "AY";
        market.put(marketDescription, probs[2]);
        marketDescription = "BN";
        market.put(marketDescription, probs[3]);
        marketDescription = "DN";
        market.put(marketDescription, probs[4]);
        marketDescription = "AN";
        market.put(marketDescription, probs[5]);
        markets.addMarketWithShortKey(market);

        market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DBLCBTS", "M",
                        "Double chance and both team to score");
        market.setIsValid(true);
        probs = generateDoubleChanceAndBTSProbs(scoreGrid);
        marketDescription = "DAYes";
        market.put(marketDescription, probs[0]);
        marketDescription = "HDYes";
        market.put(marketDescription, probs[1]);
        marketDescription = "HAYes";
        market.put(marketDescription, probs[2]);
        marketDescription = "DANo";
        market.put(marketDescription, probs[3]);
        marketDescription = "HDNo";
        market.put(marketDescription, probs[4]);
        marketDescription = "HANo";
        market.put(marketDescription, probs[5]);
        markets.addMarketWithShortKey(market);
    }


    private double[] generateDoubleChanceAndBTSProbs(double[][] scoreGrid) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home <= away && (home > 0 && away > 0))
                    probs[0] += scoreGrid[home][away];
                if (home >= away && (home > 0 && away > 0))
                    probs[1] += scoreGrid[home][away];
                if ((home > away || home < away) && (home > 0 && away > 0))
                    probs[2] += scoreGrid[home][away];
                if (home <= away && !(home > 0 && away > 0))
                    probs[3] += scoreGrid[home][away];
                if (home >= away && (!(home > 0 && away > 0)))
                    probs[4] += scoreGrid[home][away];
                if ((home > away || home < away) && !(home > 0 && away > 0))
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateMatchResultAndBTSProbs(double[][] scoreGrid) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home < away && (home > 0 && away > 0))
                    probs[0] += scoreGrid[home][away];
                if (home == away && (home > 0 && away > 0))
                    probs[1] += scoreGrid[home][away];
                if (home > away && (home > 0 && away > 0))
                    probs[2] += scoreGrid[home][away];
                if (home < away && (home == 0 || away == 0))
                    probs[3] += scoreGrid[home][away];
                if (home == away && (home == 0 || away == 0))
                    probs[4] += scoreGrid[home][away];
                if (home > away && (home == 0 || away == 0))
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateWinningMarginProbsFromScoreGrid(double[][] scoreGrid) {
        double[] probs = new double[10];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home - away == 1)
                    probs[0] += scoreGrid[home][away];
                else if (home - away == 2)
                    probs[1] += scoreGrid[home][away];
                else if (home - away == 3)
                    probs[2] += scoreGrid[home][away];
                else if (home - away == 4)
                    probs[3] += scoreGrid[home][away];
                else if (home - away >= 5)
                    probs[4] += scoreGrid[home][away];
                else if (-home + away == 1)
                    probs[5] += scoreGrid[home][away];
                else if (-home + away == 2)
                    probs[6] += scoreGrid[home][away];
                else if (-home + away == 3)
                    probs[7] += scoreGrid[home][away];
                else if (-home + away == 4)
                    probs[8] += scoreGrid[home][away];
                else if (-home + away >= 5)
                    probs[9] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    /**
     * 
     * @param csGrid as generated for the "FT:CS" market
     * @return SG return normalised correct score grid
     */
    private double[][] creatScoreGrid(Map<String, Double> csGrid) {
        double[][] scoreGrid = new double[30][30];
        Double normTemp = 0.0;
        // normalising

        for (Map.Entry<String, Double> entry : csGrid.entrySet())
            normTemp += entry.getValue();
        for (Map.Entry<String, Double> entry : csGrid.entrySet()) {
            String scores = entry.getKey();
            String[] tempScores = scores.split("-");
            int scoreA = Integer.parseInt(tempScores[0]);
            int scoreB = Integer.parseInt(tempScores[1]);
            if (!(normTemp >= 0 && normTemp <= 0))
                scoreGrid[scoreA][scoreB] = entry.getValue() / normTemp;
        }
        return scoreGrid;
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
            case "D":
                value = 2;
                break;
        }
        return value;
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }
}
