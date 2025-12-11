package ats.algo.sport.football;

import java.util.List;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.football.goalscorer.NextAnytimeGsCalculator;
import ats.core.util.CollectionsUtil;

public class FootballMatchMarketsFactory extends MarketsFactory {

    private static final boolean GENERATE_GS_MARKETS = true;

    private boolean isForParamFinder;

    /**
     * Baba
     **/
    // private NWayStatistic halfDoubleChanceAnd2MoreGoals;
    // private String[] halfDoubleChanceAnd2MoreGoalsList = {"AX up to 2 goals",
    // "BX up to 2 goals", "AB up to 2 goals",
    // "AX 2 or more goals", "BX up 2 or more goals", "AB 2 or more goals"};
    private TwoWayStatistic firstHWinAndScore23;
    private TwoWayStatistic firstHWinBndScore23;
    private TwoWayStatistic homeWinNotToNil;
    private TwoWayStatistic awayWinNotToNil;
    private TwoWayStatistic halfOfFirstGoal;
    private TwoWayStatistic halfOfFirstGoalA;
    private TwoWayStatistic halfOfFirstGoalB;
    /**
     * Jin working section
     **/
    private ThreeWayStatistic firstCornerTeam;
    private boolean firstCornerMarket;
    // private EuroHandicapStatistic euroCornerHandicap;
    // private EuroHandicapStatistic halfEuroCornerHandicap;
    // private ThreeWayStatistic halfMostCorners;
    // private TotalStatistic halfHomeCornerOU;
    // private TotalStatistic halfAwayCornerOU;
    private ThreeWayStatistic halfCornerWinner;

    private boolean extraTimePossible;
    private boolean preMatchNow = true;
    private TwoWayStatistic toWinInET;
    private ThreeWayStatistic extraTimeResult;
    private TwoWayStatistic eitherTeamWinBothHalves;
    private TotalStatistic extraTimeOU;
    private CorrectScoreStatistic extraTimeCorrectGoalScore;
    private ThreeWayStatistic extraTimeHalfResult;
    private TotalStatistic extraTimeHalfOU;

    private boolean createRaceTo2GoalsMarket;
    private ThreeWayStatistic raceTo2Goals;
    private boolean createRaceTo3GoalsMarket;
    private ThreeWayStatistic raceTo3Goals;
    private ThreeWayStatistic firstTeamToScore;
    private ThreeWayStatistic lastTeamToScore;
    private ThreeWayStatistic firstTeamToScoreHalf;

    private TwoWayStatistic homeWinToNil;
    private TwoWayStatistic awayWinToNil;

    private TwoWayStatistic homeWinToNil1Half;
    private TwoWayStatistic awayWinToNil1Half;

    private TwoWayStatistic homeWin23Goals;
    private TwoWayStatistic awayWin23Goals;

    private boolean thirtyWinnerMarket;
    private ThreeWayStatistic thirtyMinsWinner;
    private boolean sixtyWinnerMarket;
    private ThreeWayStatistic sixtyMinsWinner;
    private boolean seventyFiveWinnerMarket;
    private ThreeWayStatistic seventyFiveMinsWinner;
    private TwoWayStatistic goalScoredTenMinutes;
    private TwoWayStatistic goalScoredFifteenMinutes;

    private boolean goalNotHappendYet = false;
    private NWayStatistic timeOfFirstGoal;
    private boolean goalHomeNotHappendYet = false;
    private NWayStatistic timeOfFirstGoalHome;
    private boolean goalAwayNotHappendYet = false;
    private NWayStatistic timeOfFirstGoalAway;
    private String[] timeOfFirstGoalList =
                    {"0-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-FT", "No Goal"};

    private TwoWayStatistic homeTeamToScore;
    private TwoWayStatistic awayTeamToScore;
    private TwoWayStatistic homeTeamToScore2;
    private TwoWayStatistic awayTeamToScore2;
    private TwoWayStatistic homeTeamToScore3;
    private TwoWayStatistic awayTeamToScore3;
    boolean htts2h = false;
    private TwoWayStatistic homeTeamToScoreBothHalves;
    boolean atts2h = false;
    private TwoWayStatistic awayTeamToScoreBothHalves;
    boolean htts1h = false;
    private TwoWayStatistic homeTeamToScoreEitherHalves;
    boolean atts1h = false;
    private TwoWayStatistic awayTeamToScoreEitherHalves;

    private boolean cardNotHappendYet = false;
    private NWayStatistic timeOfFirstCard;
    private String[] timeOfFirstCardList =
                    {"0-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-90", "No Card"};
    private boolean cornerNotHappendYet = false;
    private NWayStatistic timeOfFirstCorner;
    private String[] timeOfFirstCornerList =
                    {"0-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-90", "No Corner"};
    private boolean fifteenMinsWinnerMarket;
    /*** Jin working section ends ****/

    /**
     * Robert working section
     **/
    private TotalStatistic halfTimegoalsTotalA;
    private TotalStatistic halfTimegoalsTotalB;
    private TwoWayStatistic halfTimeBothTeamToScore;
    private TwoWayStatistic halfTimeBothTeamToScore2;
    private EuroHandicapStatistic halfTimeMatchWinnerEuroHandicap;
    private ThreeWayStatistic noOfTeamsScore;
    private ThreeWayStatistic highestScoreHalf;
    private TwoWayStatistic goalsTotalOddEven;
    private TwoWayStatistic halfTimeGoalsTotalOddEven;
    private TwoWayStatistic goalsTotalHomeOddEven;
    private TwoWayStatistic halfTimeGoalsTotalHomeOddEven;
    private TwoWayStatistic goalsTotalAwayOddEven;
    private TwoWayStatistic halfTimeGoalsTotalAwayOddEven;
    private ThreeWayStatistic highestScoreHalfHome;
    private ThreeWayStatistic highestScoreHalfAway;
    private TotalStatistic goalsTotalAsian;
    private HandicapStatistic goalsHandicapAsian;
    private TotalStatistic halfTimeGoalsTotalAsian;
    private HandicapStatistic halfTimeGoalsHandicapAsian;
    private TwoWayStatistic homeCleanSheet;
    private TwoWayStatistic awayCleanSheet;
    private TwoWayStatistic homeWinBothHalves;
    private TwoWayStatistic awayWinBothHalves;
    private TwoWayStatistic homeWinEitherHalves;
    private TwoWayStatistic awayWinEitherHalves;

    /*** Robert working section ends ****/
    private CorrectScoreStatistic correctCornerScore;
    private CorrectScoreStatistic halfTimeCorrectCornerScore;

    private ThreeWayStatistic raceTo3Corners;
    private ThreeWayStatistic raceTo5Corners;
    private ThreeWayStatistic raceTo7Corners;
    private ThreeWayStatistic raceTo9Corners;
    private ThreeWayStatistic raceTo11Corners;
    private ThreeWayStatistic raceTo13Corners;
    private ThreeWayStatistic raceTo15Corners;
    private TwoWayStatistic raceTo3CornersTwoWay;
    private TwoWayStatistic raceTo5CornersTwoWay;
    private TwoWayStatistic raceTo7CornersTwoWay;
    private TwoWayStatistic raceTo9CornersTwoWay;
    private TwoWayStatistic raceTo11CornersTwoWay;
    private TwoWayStatistic raceTo13CornersTwoWay;
    private TwoWayStatistic raceTo15CornersTwoWay;
    private int currentCornerA;
    private int currentCornerB;
    private ThreeWayStatistic halfWithMostCorners;
    private ThreeWayStatistic halfWithMostCards;
    private TwoWayStatistic comeFromBehindAndDraw;
    private TwoWayStatistic comeFromBehindAndWin;
    private TwoWayStatistic comeFromBehindAndWinDraw;
    private TwoWayStatistic leadHalfTimeFailToWin;
    private TwoWayStatistic scoreFirstFailToWin;

    /**
     * Tibo working section
     **/
    private NWayStatistic totalGoalsHome;
    private NWayStatistic totalGoalsAway;
    private NWayStatistic totalGoalRangeCurrentHalf;
    private NWayStatistic totalGoalRangeCurrentHalfHome;
    private NWayStatistic totalGoalRangeCurrentHalfAway;
    private TwoWayStatistic awayTeamWinFromBehind;
    private TwoWayStatistic homeTeamWinFromBehind;
    private TwoWayStatistic homeTeamLeadAtAnyTime;
    private TwoWayStatistic awayTeamLeadAtAnyTime;
    private TwoWayStatistic goalInBothHalves;
    private NWayStatistic firstHalfSecondHalfResults;
    private NWayStatistic firstHalfSecondsHalfScores;
    private NWayStatistic firstHalfNormalTimeScores;
    /*** Tibo working section ends ****/

    /*** Chaff working section starts ***/
    /*** Chaff working section ends ****/

    private EuroHandicapStatistic matchWinnerEuroHandicap;
    private ThreeWayStatistic matchWinner;
    private TwoWayStatistic matchWinnerFTETP;
    private ThreeWayStatistic matchWinnerCurrentHalf;
    private ThreeWayStatistic fiveMinsWinner;
    private TotalStatistic fiveMinutesCornerOU;
    private TwoWayStatistic fiveMinutesBooking;

    private TwoWayStatistic goalScoredFiveMinutes;
    private TwoWayStatistic goalScoredTwentyMinutes;
    private TwoWayStatistic goalScoredThirtyMinutes;
    private TwoWayStatistic goalScoredSixtyMinutes;
    private TwoWayStatistic goalScoredSeventyFiveMinutes;
    private ThreeWayStatistic tenMinsWinner;
    private TotalStatistic tenMinutesCornerOU;
    private TwoWayStatistic tenMinutesBooking;
    private ThreeWayStatistic fifteenMinsWinner;
    private TotalStatistic fifteenMinutesCornerOU;
    private TwoWayStatistic fifteenMinutesBooking;
    private CorrectScoreStatistic correctGoalScore;
    private CorrectScoreStatistic halfTimeCorrectGoalScore;
    private CorrectScoreStatistic secondhalfTimeCorrectGoalScore;

    private TotalStatistic goalsTotal;
    private ThreeWayStatistic bookingsWinner;
    private TotalStatistic bookingsTotal;
    private TotalStatistic yellowATotal;
    private TotalStatistic yellowBTotal;
    private TotalStatistic cardsTotal;
    private TotalStatistic cardsTotalA;
    private TotalStatistic cardsTotalB;
    private TotalStatistic goalsTotalA;
    private TotalStatistic goalsTotalB;
    private HandicapStatistic goalsHandicap;
    private HandicapStatistic bookingsHandicap;
    private HandicapStatistic cardHandicap;
    private HandicapStatistic goalsHandicapCurrentHalf;
    private ThreeWayStatistic nextGoal;
    private TotalStatistic goalsTotalCurrentHalf;
    // private ThreeWayStatistic cornerMatchWinner;
    private TotalStatistic cornersTotal;
    private TotalStatistic cornersTotalAsian;
    // private TotalStatistic cornersTotalA;
    // private TotalStatistic cornersTotalB;
    private HandicapStatistic cornersHandicap;
    private HandicapStatistic cornersHandicapCurrentHalf;
    private ThreeWayStatistic nextCorner;
    private TotalStatistic cornersTotalCurrentHalf;
    // private ThreeWayStatistic cornerMatchWinnerCurrentHalf;
    private NWayStatistic halfTimeFullTimeResult;
    private NWayStatistic winningMargin;
    private NWayStatistic totalGoalR;
    private NWayStatistic totalGoalR2;

    // Robin working section
    private TwoWayStatistic firstHalfHomeTeamToWinBTTS;
    private TwoWayStatistic firstHalfAwayTeamToWinBTTS;
    private TwoWayStatistic firstHalfDrawBTTS;
    private TwoWayStatistic firstHalfHomeTeamToWinBTNTS;
    private TwoWayStatistic firstHalfAwayTeamToWinBTNTS;
    private TwoWayStatistic firstHalfDrawBTNTS;
    private TwoWayStatistic secondHalfHomeTeamToWinBTTS;
    private TwoWayStatistic secondHalfAwayTeamToWinBTTS;
    private TwoWayStatistic secondHalfDrawBTTS;
    private TwoWayStatistic secondHalfHomeTeamToWinBTNTS;
    private TwoWayStatistic secondHalfAwayTeamToWinBTNTS;
    private TwoWayStatistic secondHalfDrawBTNTS;

    // private TwoWayStatistic firstHalfHomeOrDrawBTTS; //FTBTTS1HDCHD
    // private TwoWayStatistic firstHalfAwayOrDrawBTTS; //FTBTTS1HDCAD
    // private TwoWayStatistic firstHalfHomeOrAwayBTTS; //FTBTTS1HDCHA
    // private TwoWayStatistic firstHalfHomeOrDrawBTNTS;
    // private TwoWayStatistic firstHalfAwayOrDrawBTNTS;
    // private TwoWayStatistic firstHalfHomeOrAwayBTNTS;

    // cards markets
    // private TwoWayStatistic totalCardsOddEven;
    // private TwoWayStatistic totalYellowCardsOddEven;

    // end robin working section

    private TwoWayStatistic bothTeamToScore;
    private static final int maxNoGoalsPerTeam = 40;
    private static final int maxNoCornersPerTeam = 150;
    private static final int maxNoYellowCardsPerTeam = 50;
    private static final int maxNoRedCardsPerTeam = 50;
    private static final int maxNoBookingPointsPerTeam = 800;
    private int periodNo;
    private static final String[] halTimeFullTimeResult =
                    {"A/A", "A/X", "A/B", "X/A", "X/X", "X/B", "B/A", "B/X", "B/B"};
    private static final String[] winningMarginRange = {"X", "A1", "A2", "A>2", "B1", "B2", "B>2"};
    private static final String[] exactTotalGoal = {"0", "1", "2+"}; // P:OUR
    private static final String[] exactTotalGoalForTeam = {"0", "1", "2", "3+"}; // FT:A:£OUR_1, FT:B:£OUR_1, P:A:OUR,
                                                                                 // P:B:OUR
    private static final String[] exactTotalGoalRange = {"0-1 goals", "2-3 goals", "4-5 goals", "6+"}; // FT:Â£OUR_2
    private static final String[] exactTotalGoalRange2 = {"0", "1", "2", "3", "4", "5", "6+"}; // FT:Â£OUR_2
    private boolean allowGenerateAllSelection = false;
    private String periodSequenceId;
    // >>>> Half time scores generation variables
    // list of "a1-b1/a2-b2" goals
    private static String[] firstHalfSecondHalfScoreSelections;
    // list of "a1-b1/a1+2-b1+2" goals
    private static String[] firstHalNormalTimeScoreSelections;
    // 4D maps : [a1 -> [a2 -> [b1 -> [b2 -> index of score in
    // firstHalfSecondHalfScoreSelections]]]]
    private static Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> firstHalfSecondHalfScoreSelectionIndexMap =
                    CollectionsUtil.newMap();
    // 4D maps : [a1 -> [a2 -> [b1 -> [b2 -> index of score in
    // firstHalNormalTimeScoreSelections]]]]
    private static Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> firstHalfNormalTimeScoreSelectionIndexMap =
                    CollectionsUtil.newMap();
    private static int maxFirstHalfGoals = 10;
    private static int maxSecondHalfGoals = 10;
    private static int maxNormatlTimeGoals = maxFirstHalfGoals + maxSecondHalfGoals;
    private static double correctScoreLimit = 0.0001;
    private static boolean prematch;
    // <<<<< Half time scores generation variables

    /**
     * 
     * @param matchState
     * @param isForParamFinder
     */
    FootballMatchMarketsFactory(FootballMatchState matchState, boolean isForParamFinder) {
        this.isForParamFinder = isForParamFinder;
        int elapsedTimeSec = matchState.getElapsedTimeSecs();
        periodNo = matchState.getPeriodNo();
        periodSequenceId = matchState.getPeriodSequenceId();
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        this.setCorrectScoreLimit(correctScoreLimit);
        String fiveMinsSequenceId = matchState.getSequenceIdForFiveMinsResult();
        String tenMinsSequencdId = matchState.getSequenceIdForTenMinsResult();
        String fifteenMinsSequenceId = matchState.getSequenceIdForFifteenMinsResult();
        extraTimePossible = matchState.getExtraHalfSecs() != 0;
        boolean isNotShootOut = matchState.getMatchPeriod() != FootballMatchPeriod.IN_SHOOTOUT;
        boolean isNormalTimeMatchFinished = !matchState.isNormalTimeMatchCompleted();
        String goalId = matchState.getGoalsA() + "-" + matchState.getGoalsB();
        /*
         * Add here any markets that might be needed for param finding
         */
        matchWinner = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        "A", "B", "X");
        goalsTotal = new TotalStatistic("FT:OU", "Total Goals", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        2 * maxNoGoalsPerTeam);
        goalsHandicapAsian = new HandicapStatistic("FT:AHCP", "Asian Handicap", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "S" + goalId, maxNoGoalsPerTeam);
        bothTeamToScore = new TwoWayStatistic("FT:BTS", "Both Teams to Score", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        cornersTotal = new TotalStatistic("FT:COU", "Total Corners", MarketGroup.CORNERS, isNormalTimeMatchFinished,
                        "M", 2 * maxNoCornersPerTeam);
        cornersHandicap = new HandicapStatistic("FT:CHCP", "Corner handicap", MarketGroup.CORNERS,
                        isNormalTimeMatchFinished, "M", 2 * maxNoCornersPerTeam);

        bookingsTotal = new TotalStatistic("FT:BOU", "Total Booking Points", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished, "M", maxNoBookingPointsPerTeam);
        bookingsHandicap = new HandicapStatistic("FT:BSPRD", "Booking Handicap", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished, "M", maxNoBookingPointsPerTeam);

        cardsTotal = new TotalStatistic("FT:BCOU", "Total cards", MarketGroup.BOOKINGS, isNormalTimeMatchFinished, "M",
                        2 * (maxNoYellowCardsPerTeam + maxNoRedCardsPerTeam));
        prematch = matchState.preMatch();
        if (prematch) {

            cardsTotalA = new TotalStatistic("FT:A:BTOT", "Total cards Team A", MarketGroup.BOOKINGS,
                            isNormalTimeMatchFinished, "M", 2 * (maxNoYellowCardsPerTeam + maxNoRedCardsPerTeam));
            cardsTotalB = new TotalStatistic("FT:B:BTOT", "Total cards Team B", MarketGroup.BOOKINGS,
                            isNormalTimeMatchFinished, "M", 2 * (maxNoYellowCardsPerTeam + maxNoRedCardsPerTeam));
        }

        cardHandicap = new HandicapStatistic("FT:BCSPRD", "Card Handicap", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished, "M", maxNoYellowCardsPerTeam + maxNoRedCardsPerTeam);
        /*
         * end of param finding markets. Do not create anything else if param finding
         */
        if (isForParamFinder)
            return;


        correctCornerScore = new CorrectScoreStatistic("FT:CCS", "Correct corner Score", MarketGroup.CORNERS,
                        isNormalTimeMatchFinished, "M", maxNoCornersPerTeam);

        halfTimeCorrectCornerScore = new CorrectScoreStatistic("P:CCS", "First Half Correct Corner Score",
                        MarketGroup.CORNERS, periodNo < 3, String.format("H%d", periodNo), maxNoCornersPerTeam / 2);

        bookingsWinner = new ThreeWayStatistic("FT:BAXB", "Most Bookings", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished, "M", "A", "B", "X");
        matchWinnerFTETP = new TwoWayStatistic("FT:TQ", "To Qualify", MarketGroup.GOALS, true, "M", "A", "B");

        correctGoalScore = new CorrectScoreStatistic("FT:CS", "Correct Score", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", maxNoGoalsPerTeam);


        yellowATotal = new TotalStatistic("FT:A:BYOU", "Team A Total Yellow Cards", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished && prematch, "M", maxNoYellowCardsPerTeam);
        yellowBTotal = new TotalStatistic("FT:B:BYOU", "Team B Total yellow cards", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished && prematch, "M", maxNoYellowCardsPerTeam);

        goalsTotalA = new TotalStatistic("FT:A:Â£OUR_1", "Total Goals Home Team", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", maxNoGoalsPerTeam);
        goalsTotalB = new TotalStatistic("FT:B:OU", "Total Goals Away Team", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", maxNoGoalsPerTeam);
        goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        maxNoGoalsPerTeam);
        matchWinnerEuroHandicap = new EuroHandicapStatistic("FT:3HCP", "Euro Handicap (full time only)",
                        isNormalTimeMatchFinished, "M", maxNoGoalsPerTeam);
        bothTeamToScore = new TwoWayStatistic("FT:BTS", "Both Teams to Score", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        homeTeamWinFromBehind = new TwoWayStatistic("FT:WFB:H", "Home Team Win from Behind", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        awayTeamWinFromBehind = new TwoWayStatistic("FT:WFB:A", "Away Team Win from Behind", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        homeTeamLeadAtAnyTime = new TwoWayStatistic("FT:LAT:H", "Home Team Lead at Anytime", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        awayTeamLeadAtAnyTime = new TwoWayStatistic("FT:LAT:A", "Away Team Lead at Anytime", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        goalInBothHalves = new TwoWayStatistic("FT:GBH", "Goal in Both Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        halfWithMostCorners = new ThreeWayStatistic("FT:HWMCR", "Half With Most Corners", MarketGroup.CORNERS,
                        isNormalTimeMatchFinished, "M", "First Half", "Second Half", "X");
        halfWithMostCards = new ThreeWayStatistic("FT:HWMCD", "Half With Most Cards", MarketGroup.BOOKINGS,
                        isNormalTimeMatchFinished, "M", "First Half", "Second Half", "X");
        comeFromBehindAndDraw = new TwoWayStatistic("FT:CFBD", "To Come From Behind and Draw", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        comeFromBehindAndWin = new TwoWayStatistic("FT:CFBW", "To Come From Behind and Win", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        comeFromBehindAndWinDraw = new TwoWayStatistic("FT:CFBWD", "To Come From Behind and Win or Draw",
                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");
        leadHalfTimeFailToWin = new TwoWayStatistic("FT:LHTL", "Lead at Half Time & Fail to Win", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        scoreFirstFailToWin = new TwoWayStatistic("FT:FGL", "Score First & Fail to Win", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfHomeTeamToWinBTTS =
                        new TwoWayStatistic("FTBTTS2HH", "Second Half - Home Team to Win & Both Teams to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfAwayTeamToWinBTTS =
                        new TwoWayStatistic("FTBTTS2HA", "Second Half - Away Team to Win & Both Teams to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfDrawBTTS = new TwoWayStatistic("FTBTTS2HX", "Second Half - Draw & Both Teams to Score",
                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfHomeTeamToWinBTNTS =
                        new TwoWayStatistic("FTBTNTS2HH", "Second Half - Home Team to Win & Both Teams Not to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfAwayTeamToWinBTNTS =
                        new TwoWayStatistic("FTBTNTS2HA", "Second Half - Away Team to Win & Both Teams Not to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        secondHalfDrawBTNTS = new TwoWayStatistic("FTBTNTS2HX", "Second Half - Draw & Both Teams Not to Score",
                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        if (periodNo < 2) {
            firstHalfSecondHalfResults = new NWayStatistic("FT:HTFT", "First Half / Second Half Results",
                            MarketGroup.GOALS, isNormalTimeMatchFinished, "M", halTimeFullTimeResult);
            generateFirstHalfSecondHalfScoreSelections();
            generateFirstHalfNormalTimeScoreSelections();
            firstHalfSecondsHalfScores = new NWayStatistic("FT:FHSHCS",
                            "First Half Correct Score / Second Half Correct Score", MarketGroup.GOALS,
                            isNormalTimeMatchFinished, "M", firstHalfSecondHalfScoreSelections);
            firstHalfNormalTimeScores = new NWayStatistic("FT:FHNTCS", "Score at Half Time / Score at Full Time",
                            MarketGroup.GOALS, isNormalTimeMatchFinished, "M", firstHalNormalTimeScoreSelections);
        }

        String fiveMinutesIndicator = matchState.getSequenceIdForFiveMinsResult();
        int fiveMinsNo = matchState.getFiveMinsNo();

        String fiveMinsWindow = fiveMinsNo * 5 + " - " + 5 * (fiveMinsNo + 1) + " ";
        String tenMinsWindow = ((int) fiveMinsNo / 2) * 10 + " - " + 10 * (((int) fiveMinsNo / 2) + 1) + " ";
        String fifteenMinsWindow = ((int) fiveMinsNo / 3) * 15 + " - " + 15 * (((int) fiveMinsNo / 3) + 1) + " ";

        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", fiveMinsWindow + "Match Result (5 Min) Period",
                        MarketGroup.GOALS, isNotShootOut, fiveMinsSequenceId, "A", "B", "X");

        fiveMinutesCornerOU = new TotalStatistic("FT:5MC", fiveMinsWindow + "Corner Over Under (5 Min) Period",
                        MarketGroup.CORNERS, isNotShootOut, fiveMinsSequenceId, maxNoCornersPerTeam / 10);
        boolean bookingAlready = !matchState.bookingNotYetHappend(Integer.parseInt(fiveMinutesIndicator.substring(1)),
                        5, TeamId.UNKNOWN);

        fiveMinutesBooking = new TwoWayStatistic("FT:5MB", fiveMinsWindow + "Five Minutes Booking ",
                        MarketGroup.BOOKINGS, isNotShootOut && bookingAlready, fiveMinsSequenceId, "Yes", "No");

        goalScoredFiveMinutes = new TwoWayStatistic("FT:5MG", fiveMinsWindow + "Goal Scored (5 Min)", MarketGroup.GOALS,
                        isNotShootOut, fiveMinsSequenceId, "Yes", "No");

        goalScoredTwentyMinutes = new TwoWayStatistic("FT:20MG", "Goal Scored (20 Min)", MarketGroup.GOALS,
                        elapsedTimeSec < 1200, "M", "Yes", "No");

        goalScoredThirtyMinutes = new TwoWayStatistic("FT:30MG", "Goal Scored (30 Min)", MarketGroup.GOALS,
                        elapsedTimeSec < 1800, "M", "Yes", "No");

        goalScoredSixtyMinutes = new TwoWayStatistic("FT:60MG", "Goal Scored (60 Min)", MarketGroup.GOALS,
                        elapsedTimeSec < 3600, "M", "Yes", "No");

        goalScoredSeventyFiveMinutes = new TwoWayStatistic("FT:75MG", "Goal Scored (75 Min)", MarketGroup.GOALS,
                        elapsedTimeSec < 4500, "M", "Yes", "No");

        String tenMinutesIndicator = matchState.getSequenceIdForTenMinsResult();

        boolean bookingAlready10 = !matchState.bookingNotYetHappend(Integer.parseInt(fiveMinutesIndicator.substring(1)),
                        10, TeamId.UNKNOWN);

        tenMinutesBooking = new TwoWayStatistic("FT:10MB", tenMinsWindow + "Ten Minutes Booking ", MarketGroup.BOOKINGS,
                        isNotShootOut && bookingAlready10, tenMinutesIndicator, "Yes", "No");

        String fifteenMinutesIndicator = matchState.getSequenceIdForFifteenMinsResult();
        boolean bookingAlready15 =
                        !matchState.bookingNotYetHappend(Integer.parseInt(fifteenMinutesIndicator.substring(1)), 15,
                                        TeamId.UNKNOWN);

        fifteenMinutesBooking = new TwoWayStatistic("FT:15MB", fifteenMinsWindow + "Fifteen Minutes Booking ",
                        MarketGroup.BOOKINGS, isNotShootOut && bookingAlready15, fifteenMinutesIndicator, "Yes", "No");

        tenMinsWinner = new ThreeWayStatistic("FT:10MR", tenMinsWindow + "Match Result (10 Min) Period",
                        MarketGroup.GOALS, isNotShootOut, tenMinsSequencdId, "A", "B", "X");

        tenMinutesCornerOU = new TotalStatistic("FT:10MC", tenMinsWindow + "Corner Over Under (10 Min) Period",
                        MarketGroup.CORNERS, isNotShootOut, tenMinutesIndicator, maxNoCornersPerTeam / 5);

        goalScoredTenMinutes = new TwoWayStatistic("FT:10MG", tenMinsWindow + "Goal Scored (10 Min)", MarketGroup.GOALS,
                        isNotShootOut, tenMinsSequencdId, "Yes", "No");

        // String fifteenMinutesIndicator =
        // matchState.getSequenceIdForFifteenMinsResult();
        goalScoredFifteenMinutes = new TwoWayStatistic("FT:15MG", fifteenMinsWindow + "Goal Scored (15 Min)",
                        MarketGroup.GOALS, isNotShootOut, fifteenMinutesIndicator, "Yes", "No");
        fifteenMinsWinnerMarket = matchState.getElapsedTimeSecs() <= 900;

        fifteenMinsWinner = new ThreeWayStatistic("FT:15MR", fifteenMinsWindow + "Match Result (15 Min)",
                        MarketGroup.GOALS, fifteenMinsWinnerMarket, fifteenMinsSequenceId, "A", "B", "X");

        fifteenMinutesCornerOU = new TotalStatistic("FT:15MC", fifteenMinsWindow + "Corner Over Under (15 Min) Period",
                        MarketGroup.CORNERS, isNotShootOut, fifteenMinsSequenceId, maxNoCornersPerTeam / 3);

        String thirtyMinutesIndicator = matchState.getSequenceIdForXXMinsResult(30);
        String sixtyMinutesIndicator = "F0";// matchState.getSequenceIdForXXMinsResult(60);
        String seventyFiveMinutesIndicator = matchState.getSequenceIdForXXMinsResult(75);
        int extraHalfSecs = matchState.getExtraHalfSecs();
        if (extraHalfSecs == 0) {
            seventyFiveWinnerMarket =
                            ((5400 - (Integer.parseInt(seventyFiveMinutesIndicator.substring(1)) + 1) * 4500) > 0);
            sixtyWinnerMarket = matchState.getElapsedTimeSecs() < 3600;// ((5400
                                                                       // -
                                                                       // (Integer.parseInt(sixtyMinutesIndicator.substring(1))
                                                                       // + 1)
                                                                       // *
                                                                       // 3600)
                                                                       // > 0);
            thirtyWinnerMarket = matchState.getElapsedTimeSecs() <= 1800;// ((5400
                                                                         // -
                                                                         // (Integer.parseInt(sixtyMinutesIndicator.substring(1))
                                                                         // +
                                                                         // 1)
                                                                         // *
                                                                         // 1800)
                                                                         // >
                                                                         // 0);
        } else {
            seventyFiveWinnerMarket = ((5400
                            + extraHalfSecs * 2 > (Integer.parseInt(seventyFiveMinutesIndicator.substring(1)) + 1)
                                            * 4500));
            sixtyWinnerMarket = matchState.getElapsedTimeSecs() < 3600;// ((5400
            // + extraHalfSecs * 2 >
            // (Integer.parseInt(sixtyMinutesIndicator.substring(1) + 1) *
            // 3600)));
            thirtyWinnerMarket = matchState.getElapsedTimeSecs() <= 1800;// ((5400
            // + extraHalfSecs * 2 >
            // (Integer.parseInt(sixtyMinutesIndicator.substring(1) + 1) *
            // 1800)));

        }
        thirtyMinsWinner = new ThreeWayStatistic("FT:30MR", "Match Result After 30 Min", MarketGroup.GOALS,
                        thirtyWinnerMarket, thirtyMinutesIndicator, "A", "B", "X");

        sixtyMinsWinner = new ThreeWayStatistic("FT:60MR", "Match Result After 60 Min", MarketGroup.GOALS,
                        matchState.getElapsedTimeSecs() < 3600, sixtyMinutesIndicator, "A", "B", "X");

        seventyFiveMinsWinner = new ThreeWayStatistic("FT:75MR", "Match Result After 75 Min", MarketGroup.GOALS,
                        seventyFiveWinnerMarket, seventyFiveMinutesIndicator, "A", "B", "X");

        String goalSequenceId = matchState.getGoalSequenceId();
        String marketDescription = String.format("Next team to score (goal no %d)", matchState.getGoalNo());
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, MarketGroup.GOALS, isNormalTimeMatchFinished,
                        goalSequenceId, "A", "B", "M3");


        if (periodNo < 2)
            halfTimeFullTimeResult = new NWayStatistic("FT:HF", "Half Time/ Full Time", MarketGroup.GOALS,
                            isNormalTimeMatchFinished, periodSequenceId, halTimeFullTimeResult);
        winningMargin = new NWayStatistic("FT:WM", "Winning Margin", MarketGroup.GOALS, isNormalTimeMatchFinished, "M",
                        winningMarginRange);
        totalGoalR = new NWayStatistic("FT:Â£OUR_2", "Total Goal Range", MarketGroup.GOALS, isNormalTimeMatchFinished,
                        "M", exactTotalGoalRange);


        totalGoalR2 = new NWayStatistic("FT:Â£OUR_1", "Total Goal Range", MarketGroup.GOALS, isNormalTimeMatchFinished,
                        "M", exactTotalGoalRange2);

        totalGoalsHome = new NWayStatistic("FT:A:£OUR_1", "Home Team Total Goals", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", exactTotalGoalForTeam);
        totalGoalsAway = new NWayStatistic("FT:B:£OUR_1", "Away Team Total Goals", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", exactTotalGoalForTeam);

        // robin working section
        firstHalfHomeTeamToWinBTTS =
                        new TwoWayStatistic("FTBTTS1HH", "First Half - Home Team to Win & Both Teams to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        firstHalfAwayTeamToWinBTTS =
                        new TwoWayStatistic("FTBTTS1HA", "First Half - Away Team to Win & Both Teams to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        firstHalfDrawBTTS = new TwoWayStatistic("FTBTTS1HX", "First Half - Draw & Both Teams to Score",
                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        firstHalfHomeTeamToWinBTNTS =
                        new TwoWayStatistic("FTBTNTS1HH", "First Half - Home Team to Win & Both Teams Not to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        firstHalfAwayTeamToWinBTNTS =
                        new TwoWayStatistic("FTBTNTS1HA", "First Half - Away Team to Win & Both Teams Not to Score",
                                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        firstHalfDrawBTNTS = new TwoWayStatistic("FTBTNTS1HX", "First Half - Draw & Both Teams Not to Score",
                        MarketGroup.GOALS, isNormalTimeMatchFinished, "M", "Yes", "No");

        Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Total Goals";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Total Goals";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        goalsTotalCurrentHalf = new TotalStatistic("P:OU", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam / 2);
        totalGoalRangeCurrentHalf = new NWayStatistic("P:OUR", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, periodSequenceId, exactTotalGoal);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Handicap";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Handicap";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        goalsHandicapCurrentHalf = new HandicapStatistic("P:SPRD", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam / 2);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Total Goals Home";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Total Goals Home";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        totalGoalRangeCurrentHalfHome = new NWayStatistic("P:A:OUR", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, periodSequenceId, exactTotalGoalForTeam);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Total Goals Away";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Total Goals Away";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        totalGoalRangeCurrentHalfAway = new NWayStatistic("P:B:OUR", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, periodSequenceId, exactTotalGoalForTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Match Result";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Match Result";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }

        matchWinnerCurrentHalf = new ThreeWayStatistic("P:AXB", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, "A", "B", "X");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Correct Score";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Correct Score";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeCorrectGoalScore = new CorrectScoreStatistic("P:CS", "First Half Correct Score", MarketGroup.GOALS,
                        periodNo < 2, String.format("H%d", 1), maxNoGoalsPerTeam / 2);
        secondhalfTimeCorrectGoalScore = new CorrectScoreStatistic("P:CS", "Second Half Correct Score",
                        MarketGroup.GOALS, periodNo < 3, String.format("H%d", 2), maxNoGoalsPerTeam / 2);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First Half Total Corners";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second Half Total Corners";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        cornersTotalCurrentHalf = new TotalStatistic("P:COU", marketDescription, MarketGroup.CORNERS, generateMarket,
                        periodSequenceId, maxNoCornersPerTeam);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half corners handicap";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half corners handicap";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        cornersHandicapCurrentHalf = new HandicapStatistic("P:CSPRD", marketDescription, MarketGroup.CORNERS,
                        generateMarket, periodSequenceId, maxNoCornersPerTeam);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half corners winner";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half corners winner";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        // cornerMatchWinnerCurrentHalf = new ThreeWayStatistic("P:CAXB", marketDescription, MarketGroup.CORNERS,
        // generateMarket, periodSequenceId, "A", "B", "X");

        String cornerSequenceId = matchState.getCornerSequenceId();
        String cornerMarketDescription = String.format("Next Corner (corner no %d)", matchState.getCornerNo());
        // cornerMatchWinner = new ThreeWayStatistic("FT:CAXB", "Corner Match winner", MarketGroup.CORNERS,
        // isNormalTimeMatchFinished, "M", "A", "B", "X");

        cornersTotalAsian = new TotalStatistic("FT:ACOU", "Asian Total Corners", MarketGroup.CORNERS,
                        isNormalTimeMatchFinished, "M", 2 * maxNoCornersPerTeam);
        // cornersTotalA = new TotalStatistic("FT:A:CTOT", "Home Team Total Corners ", MarketGroup.CORNERS,
        // isNormalTimeMatchFinished, "M", maxNoCornersPerTeam);
        // cornersTotalB = new TotalStatistic("FT:B:COU", "Away Team Total Corners", MarketGroup.CORNERS,
        // isNormalTimeMatchFinished, "M", maxNoCornersPerTeam);

        nextCorner = new ThreeWayStatistic("FT:CNC", cornerMarketDescription, MarketGroup.CORNERS,
                        isNormalTimeMatchFinished, cornerSequenceId, "A", "B", "M6");

        /**
         * Robert working section
         **/
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Total Goals Home Team";
                generateMarket = true;
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Total Goals Home Team";
                generateMarket = true;
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimegoalsTotalA = new TotalStatistic("P:A:OU", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam / 2);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Total Goals Away Team";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Total Goals Away Team";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimegoalsTotalB = new TotalStatistic("P:B:OU", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam / 2);
        // switch (periodNo) {
        // case 0: // pre-match
        // case 1: // first half
        // marketDescription = "First half Both team to Score";
        // break;
        // case 2: // half time
        // case 3: // second half
        // marketDescription = "Second half Both team to Score";
        // break;
        // default:
        // marketDescription = "";
        // generateMarket = false;
        // }
        halfTimeBothTeamToScore = new TwoWayStatistic("P:BTS", "First Half Both Team To Score", MarketGroup.GOALS,
                        periodNo <= 1, periodSequenceId, "Yes", "No");

        halfTimeBothTeamToScore2 = new TwoWayStatistic("P:BTS", "Second Half Both Team To Score", MarketGroup.GOALS,
                        periodNo <= 2, "H2", "Yes", "No");
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Euro Handicap";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Euro Handicap";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeMatchWinnerEuroHandicap = new EuroHandicapStatistic("P:3HCP", marketDescription, generateMarket,
                        periodSequenceId, maxNoGoalsPerTeam / 2);
        noOfTeamsScore = new ThreeWayStatistic("FT:NTS", "No of Team to Score", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "0", "1", "2");
        highestScoreHalf = new ThreeWayStatistic("FT:HSH", "Highest Score Half", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "First Half", "Second Half", "X");
        goalsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Goals Even/Odd", MarketGroup.GOALS, generateMarket, "M",
                        "Odd", "Even");
        goalsTotalHomeOddEven = new TwoWayStatistic("FT:A:OE", "Home Total Goals Even/Odd", MarketGroup.GOALS,
                        generateMarket, "M", "Odd", "Even");
        goalsTotalAwayOddEven = new TwoWayStatistic("FT:B:OE", "Away Total Goals Even/Odd", MarketGroup.GOALS,
                        generateMarket, "M", "Odd", "Even");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Total Goals Even/Odd";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Total Goals Even/Odd";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeGoalsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, MarketGroup.GOALS, generateMarket,
                        periodSequenceId, "Odd", "Even");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Home Total Goals Even/Odd";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Home Total Goals Even/Odd";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeGoalsTotalHomeOddEven = new TwoWayStatistic("P:A:OE", marketDescription, MarketGroup.GOALS,
                        generateMarket, periodSequenceId, "Odd", "Even");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Away Total Goals Even/Odd";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Away Total Goals Even/Odd";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeGoalsTotalAwayOddEven = new TwoWayStatistic("P:B:OE", marketDescription, MarketGroup.GOALS,
                        generateMarket, periodSequenceId, "Odd", "Even");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Away Highest Score Half";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Away Highest Score Half";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        highestScoreHalfAway = new ThreeWayStatistic("FT:B:HSH", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "First Half", "Second Half", "X");
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Home Highest Score Half";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Home Highest Score Half";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        highestScoreHalfHome = new ThreeWayStatistic("FT:A:HSH", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "First Half", "Second Half", "X");

        goalsTotalAsian = new TotalStatistic("FT:ATG", "Asian Total Goals", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", 2 * maxNoGoalsPerTeam);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Asian Handicap";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Asian Handicap";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeGoalsHandicapAsian = new HandicapStatistic("P:AHCP", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, periodSequenceId + goalId, maxNoGoalsPerTeam / 2);
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half Asian Total Goals";
                break;
            case 2: // half time
            case 3: // second half
                marketDescription = "Second half Asian Total Goals";
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        halfTimeGoalsTotalAsian = new TotalStatistic("P:ATG", marketDescription, MarketGroup.GOALS,
                        isNormalTimeMatchFinished, periodSequenceId + goalId, maxNoGoalsPerTeam / 2);
        homeCleanSheet = new TwoWayStatistic("FT:A:CLS", "Home Team Clean Sheet", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsB() == 0), "M", "Yes", "No");
        awayCleanSheet = new TwoWayStatistic("FT:B:CLS", "Away  Team Clean Sheet", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsA() == 0), "M", "Yes", "No");

        homeWinEitherHalves = new TwoWayStatistic("FT:A:EH", "Home Team Win Either Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        awayWinEitherHalves = new TwoWayStatistic("FT:B:EH", "Away Team Win Either Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                generateMarket = true;
                break;
            case 2: // half time
                if (matchState.getPreviousPeriodGoalsA() > matchState.getPreviousPeriodGoalsB()) {
                    generateMarket = false;
                }
            case 3: // second half
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        homeWinEitherHalves = new TwoWayStatistic("FT:A:EH", "Home Team Win Either Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                generateMarket = true;
                break;
            case 2: // half time
                if (matchState.getPreviousPeriodGoalsA() < matchState.getPreviousPeriodGoalsB()) {
                    generateMarket = false;
                }
            case 3: // second half
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        awayWinEitherHalves = new TwoWayStatistic("FT:B:EH", "Away Team Win Either Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                generateMarket = true;
                break;
            case 2: // half time
                if (matchState.getPreviousPeriodGoalsA() <= matchState.getPreviousPeriodGoalsB()) {
                    generateMarket = false;
                }
            case 3: // second half
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        homeWinBothHalves = new TwoWayStatistic("FT:A:BH", "Home Team Win Both Halves", MarketGroup.GOALS,
                        isNormalTimeMatchFinished, "M", "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                generateMarket = true;
                break;
            case 2: // half time
                if (matchState.getPreviousPeriodGoalsA() >= matchState.getPreviousPeriodGoalsB()) {
                    generateMarket = false;
                }
            case 3: // second half
                break;
            default:
                marketDescription = "";
                generateMarket = false;
        }
        awayWinBothHalves = new TwoWayStatistic("FT:B:BH", "Away Team Win Both Halves", MarketGroup.GOALS,
                        generateMarket, "M", "Yes", "No");

        currentCornerA = matchState.getCornersA();
        currentCornerB = matchState.getCornersB();
        String raceSequenceId = "R";
        int raceIndex = 3;

        if (currentCornerA < 3 && currentCornerB < 3) {
            raceTo3Corners = new ThreeWayStatistic("FT:RTC", "Race To 3 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo3CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 3 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 5;
        if (currentCornerA < 5 && currentCornerB < 5) {
            raceTo5Corners = new ThreeWayStatistic("FT:RTC", "Race To 5 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo5CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 5 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 7;
        if (currentCornerA < 7 && currentCornerB < 7) {
            raceTo7Corners = new ThreeWayStatistic("FT:RTC", "Race To 7 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo7CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 7 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 9;
        if (currentCornerA < 9 && currentCornerB < 9) {
            raceTo9Corners = new ThreeWayStatistic("FT:RTC", "Race To 9 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo9CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 9 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 11;
        if (currentCornerA < 11 && currentCornerB < 11) {
            raceTo11Corners = new ThreeWayStatistic("FT:RTC", "Race To 11 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo11CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 11 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 13;
        if (currentCornerA < 13 && currentCornerB < 13) {
            raceTo13Corners = new ThreeWayStatistic("FT:RTC", "Race To 13 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo13CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 13 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }
        raceIndex = 15;
        if (currentCornerA < 15 && currentCornerB < 15) {
            raceTo15Corners = new ThreeWayStatistic("FT:RTC", "Race To 15 Corners", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B", "Neither");
            raceTo15CornersTwoWay = new TwoWayStatistic("FT:RTCTW", "Race To 15 Corners - 2 Way", MarketGroup.CORNERS,
                            isNormalTimeMatchFinished, raceSequenceId + raceIndex, "A", "B");
        }

        /*** Robert working section ends ****/

        /**
         * Jin working section
         **/
        firstCornerMarket = matchState.getFirstCorner() == TeamId.UNKNOWN;
        firstCornerTeam = new ThreeWayStatistic("FT:FC", "First Corner", MarketGroup.CORNERS, firstCornerMarket,
                        cornerSequenceId, "A", "B", "X");

        // euroCornerHandicap = new EuroHandicapStatistic("FT:CEHCP", "Corner Euro Handicap", isNormalTimeMatchFinished,
        // "M", 2 * maxNoCornersPerTeam);
        // String string = "";
        // String string2 = "";
        // String string3 = "";
        String string4 = "";
        String string5 = "";
        String extraTimeSequenceId = null;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "First half most corners";
                // string = "First half Corner Euro Handicap";
                // string2 = "First Half Home Team Corner Over/Under";
                // string3 = "First Half Away Team Corner Over/Under";
                string4 = "First Half Corner Winner";
                string5 = "Extra Time 1st Half";
                extraTimeSequenceId = "H1";
                break;
            case 2: // half time
                marketDescription = "Second half most corners";
                // string = "Second half Corner Euro Handicap";
                // string2 = "Second Half Home Team Corner Over/Under";
                // string3 = "Second Half Away Team Corner Over/Under";
                string4 = "Second Half Corner Winner";
                string5 = "Extra Time 1st Half";
                extraTimeSequenceId = "H1";
                break;
            case 3: // 1st half extra time
                string5 = "Extra Time 1st Half";
                extraTimeSequenceId = "H1";
                break;
            case 4: // 2nd half extra time
                string5 = "Extra Time 2nd Half";
                extraTimeSequenceId = "H2";
                break;

            default:
                marketDescription = "";
                generateMarket = false;
        }
        // halfEuroCornerHandicap = new EuroHandicapStatistic("P:CEHCP", string, isNormalTimeMatchFinished,
        // periodSequenceId, maxNoCornersPerTeam);
        // halfMostCorners = new ThreeWayStatistic("P:CMC", marketDescription,
        // MarketGroup.CORNERS,
        // isNormalTimeMatchFinished, periodSequenceId, "A", "B", "X");

        // halfHomeCornerOU = new TotalStatistic("P:A:COU", string2, MarketGroup.CORNERS, isNormalTimeMatchFinished,
        // periodSequenceId, maxNoCornersPerTeam / 2);

        // halfAwayCornerOU = new TotalStatistic("P:B:COU", string3, MarketGroup.CORNERS, isNormalTimeMatchFinished,
        // periodSequenceId, maxNoCornersPerTeam / 2);

        halfCornerWinner = new ThreeWayStatistic("P:CAXB", string4, MarketGroup.CORNERS, isNormalTimeMatchFinished,
                        periodSequenceId, "A", "B", "X");

        toWinInET = new TwoWayStatistic("FT:OTYN", "To Win In ET", MarketGroup.GOALS, extraTimePossible, "M", "A", "B");

        boolean stillCreatABBH = true;
        if (((FootballMatchState) matchState).getPeriodNo() > 1)
            if (((FootballMatchState) matchState).getFirstHalfGoalsA() == ((FootballMatchState) matchState)
                            .getFirstHalfGoalsB())
                stillCreatABBH = false;

        eitherTeamWinBothHalves = new TwoWayStatistic("FT:ETWBH", "Either Team Win Both Halves", MarketGroup.GOALS,
                        stillCreatABBH, "M", "Yes", "No");

        extraTimeResult = new ThreeWayStatistic("ET:AXB", "Extra Time Result", MarketGroup.GOALS,
                        extraTimePossible && isNotShootOut, "M", "A", "B", "X");

        preMatchNow = matchState.getMatchPeriod().equals(FootballMatchPeriod.PREMATCH);

        extraTimeOU = new TotalStatistic("ET:OU", "Extra Time Over/Unders", MarketGroup.GOALS,
                        extraTimePossible && isNotShootOut && (!preMatchNow), "M", 2 * maxNoGoalsPerTeam / 2);

        extraTimeCorrectGoalScore = new CorrectScoreStatistic("ET:CS", "Extra Time Correct Score", MarketGroup.GOALS,
                        extraTimePossible && isNotShootOut && (!preMatchNow), "M", maxNoGoalsPerTeam / 2);

        extraTimeHalfResult = new ThreeWayStatistic("ET:AXB", string5 + " Result", MarketGroup.GOALS,
                        extraTimePossible && isNotShootOut, extraTimeSequenceId, "A", "B", "X");

        extraTimeHalfOU = new TotalStatistic("OTHT:OU", string5 + "Over/Unders", MarketGroup.GOALS,
                        extraTimePossible && isNotShootOut && (!preMatchNow), extraTimeSequenceId,
                        maxNoGoalsPerTeam / 2);

        // need to check if race to 2 goals
        createRaceTo2GoalsMarket =
                        (matchState.checkTeamToXGoals(2, 0) == null && !matchState.isNormalTimeMatchCompleted());

        raceTo2Goals = new ThreeWayStatistic("FT:RTG2", "Race To 2 Goals", MarketGroup.GOALS, createRaceTo2GoalsMarket,
                        "M", "A", "B", "Neither");

        createRaceTo3GoalsMarket =
                        (matchState.checkTeamToXGoals(3, 0) == null && !matchState.isNormalTimeMatchCompleted());

        raceTo3Goals = new ThreeWayStatistic("FT:RTG3", "Race To 3 Goals", MarketGroup.GOALS, createRaceTo3GoalsMarket,
                        "M", "A", "B", "Neither");

        firstTeamToScore = new ThreeWayStatistic("FT:FTSC", "First Team To Score", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted() && matchState.getGoalNo() == 1), "M", "A", "B",
                        "None");

        firstTeamToScoreHalf = new ThreeWayStatistic("P:1HFTSC", "First Half First Team To Score", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted() && matchState.getGoalNo() == 1 && periodNo < 2),
                        periodSequenceId, "A", "B", "None");

        lastTeamToScore = new ThreeWayStatistic("FT:LTS", "Last Team To Score", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted()), "M", "A", "B", "Neither");

        homeWinToNil = new TwoWayStatistic("FT:AWN", "Home Win To Nil", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsB() == 0), "M", "Yes", "No");
        awayWinToNil = new TwoWayStatistic("FT:BWN", "Away Win To Nil", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsA() == 0), "M", "Yes", "No");

        homeWinToNil1Half = new TwoWayStatistic("P:A:WTN", "Home Win To Nil Half", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsB() == 0), periodSequenceId,
                        "Yes", "No");
        awayWinToNil1Half = new TwoWayStatistic("P:B:WTN", "Away Win To Nil Half", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted() && (matchState.getGoalsA() == 0), periodSequenceId,
                        "Yes", "No");

        homeWinNotToNil = new TwoWayStatistic("FT:WNTN:H", "Home Win Not To Nil", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted(), "M", "Yes", "No");
        awayWinNotToNil = new TwoWayStatistic("FT:WNTN:A", "Away Win Not To Nil", MarketGroup.GOALS,
                        !matchState.isNormalTimeMatchCompleted(), "M", "Yes", "No");
        boolean goalNotScored = matchState.getGoalNo() == 1;

        halfOfFirstGoal = new TwoWayStatistic("FT:H1G", "Half of first goal", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted()) && goalNotScored, "M",
                        "The first goal in the match will be scored in first half",
                        "The first goal in the match will be scored in second half");
        boolean goalNotScoredHome = matchState.getGoalsA() == 0;
        halfOfFirstGoalA = new TwoWayStatistic("FT:A:H1G", "Half of first goal Home", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted()) && goalNotScoredHome, "M",
                        "The first goal in the match will be scored in first half",
                        "The first goal in the match will be scored in second half");
        boolean goalNotScoredAway = matchState.getGoalsB() == 0;
        halfOfFirstGoalB = new TwoWayStatistic("FT:B:H1G", "Half of first goal Away", MarketGroup.GOALS,
                        (!matchState.isNormalTimeMatchCompleted()) && goalNotScoredAway, "M",
                        "The first goal in the match will be scored in first half",
                        "The first goal in the match will be scored in second half");

        homeWin23Goals = new TwoWayStatistic("FT:A:TW23", "Home Win and between 2-3 goals scored in match",
                        MarketGroup.GOALS, !matchState.isNormalTimeMatchCompleted(), "M", "Yes", "No");

        awayWin23Goals = new TwoWayStatistic("FT:B:TW23", "Away Win and between 2-3 goals scored in match",
                        MarketGroup.GOALS, !matchState.isNormalTimeMatchCompleted(), "M", "Yes", "No");

        goalNotHappendYet = matchState.getGoalNo() == 1 && !matchState.isNormalTimeMatchCompleted();
        timeOfFirstGoal = new NWayStatistic("FT:TOFG", "Time of First Goal", MarketGroup.GOALS, goalNotHappendYet, "M",
                        timeOfFirstGoalList);
        goalHomeNotHappendYet = matchState.getGoalsA() == 0 && !matchState.isNormalTimeMatchCompleted();
        timeOfFirstGoalHome = new NWayStatistic("FT:TOFG:H", "Time of First Goal Home", MarketGroup.GOALS,
                        goalNotHappendYet, "M", timeOfFirstGoalList);
        goalAwayNotHappendYet = matchState.getGoalsB() == 0 && !matchState.isNormalTimeMatchCompleted();
        timeOfFirstGoalAway = new NWayStatistic("FT:TOFG:A", "Time of First Goal Away", MarketGroup.GOALS,
                        goalNotHappendYet, "M", timeOfFirstGoalList);

        /** weekends */
        homeTeamToScore = new TwoWayStatistic("FT:HTTS", "Home Team To Score", MarketGroup.GOALS,
                        matchState.getGoalsA() == 0 && isNormalTimeMatchFinished, "M", "Yes", "No");

        awayTeamToScore = new TwoWayStatistic("FT:ATTS", "Away Team To Score", MarketGroup.GOALS,
                        matchState.getGoalsB() == 0 && isNormalTimeMatchFinished, "M", "Yes", "No");

        homeTeamToScore2 = new TwoWayStatistic("FT:HTTS2", "Home Team To Score 2+ Goals", MarketGroup.GOALS,
                        matchState.getGoalsA() < 2 && isNormalTimeMatchFinished, "M", "Yes", "No");

        awayTeamToScore2 = new TwoWayStatistic("FT:ATTS2", "Away Team To Score 2+ Goals", MarketGroup.GOALS,
                        matchState.getGoalsB() < 2 && isNormalTimeMatchFinished, "M", "Yes", "No");

        homeTeamToScore3 = new TwoWayStatistic("FT:HTTS3", "Home Team To Score 3+ Goals", MarketGroup.GOALS,
                        matchState.getGoalsA() < 3 && isNormalTimeMatchFinished, "M", "Yes", "No");

        awayTeamToScore3 = new TwoWayStatistic("FT:ATTS3", "Away Team To Score 3+ Goals", MarketGroup.GOALS,
                        matchState.getGoalsB() < 3 && isNormalTimeMatchFinished, "M", "Yes", "No");

        htts2h = ((matchState.isFirstHalfCompleted()
                        && goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.A) == 0)
                        || isNormalTimeMatchFinished); // resulting
                                                       // B

        homeTeamToScoreBothHalves = new TwoWayStatistic("FT:A:TTS2H", "Home Team To Score Both Halves",
                        MarketGroup.GOALS, htts2h, "M", "Yes", "No");
        atts2h = ((matchState.isFirstHalfCompleted()
                        && goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.B) == 0)
                        || isNormalTimeMatchFinished); // resulting
                                                       // B

        awayTeamToScoreBothHalves = new TwoWayStatistic("FT:B:TTS2H", "Away Team To Score Both Halves",
                        MarketGroup.GOALS, atts2h, "M", "Yes", "No");

        htts1h = ((matchState.getGoalsA() > 0) || isNormalTimeMatchFinished); // resulting
                                                                              // A

        homeTeamToScoreEitherHalves = new TwoWayStatistic("FT:HTTS1H", "Home Team To Score Either Halves",
                        MarketGroup.GOALS, htts1h, "M", "Yes", "No");
        atts1h = ((matchState.getGoalsA() > 0) || isNormalTimeMatchFinished); // resulting
                                                                              // A
        awayTeamToScoreEitherHalves = new TwoWayStatistic("FT:ATTS1H", "Away Team To Score Either Halves",
                        MarketGroup.GOALS, atts1h, "M", "Yes", "No");

        cardNotHappendYet = matchState.getFirstCardTimeSlot() == -1;
        timeOfFirstCard = new NWayStatistic("FT:TOFBR", "Time of First Card", MarketGroup.BOOKINGS, cardNotHappendYet,
                        "M", timeOfFirstCardList);
        cornerNotHappendYet = matchState.getFirstCornerTimeSlot() == -1;
        timeOfFirstCorner = new NWayStatistic("FT:CFTSC", "Time of First Corner", MarketGroup.CORNERS,
                        cornerNotHappendYet, "M", timeOfFirstCornerList);/*** Jin working section ends ****/
        /*** Jin working section ends ****/
        /*** Jin working section ends ****/
        String string6 = "1st Half Home Team To Win and Score 2-3 Goals";
        String string7 = "1st Half Away Team To Win and Score 2-3 Goals";
        if (periodNo < 2) {
            // do nothing
        } else if (periodNo < 3) {
            string6 = "2nd Half Home Team To Win and Score 2-3 Goals";
            string7 = "2nd Half Away Team To Win and Score 2-3 Goals";
        }

        firstHWinAndScore23 = new TwoWayStatistic("P:A:TW23", string6, MarketGroup.GOALS, periodNo < 3,
                        periodSequenceId, "Yes", "No");

        firstHWinBndScore23 = new TwoWayStatistic("P:B:TW23", string7, MarketGroup.GOALS, periodNo < 3,
                        periodSequenceId, "Yes", "No");

    }

    private int goalScoredForTeam(List<TeamId> firstHalfGoals, TeamId a) {
        int scored = 0;
        for (TeamId teamScored : firstHalfGoals) {
            if (teamScored.equals(a))
                scored++;
        }
        return scored;
    }

    void updateStats(FootballMatchState matchState, FootballMatchFacts matchFacts) {

        /*
         * first process the data that is needed for param finds
         */
        int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        int normalTimeGoalsB = matchState.getNormalTimeGoalsB();
        int normalTimeCornersA = matchState.getNormalTimeCornersA();
        int normalTimeCornersB = matchState.getNormalTimeCornersB();

        int goalsAfirstHalf = matchState.getFirstHalfGoalsA();
        int goalsBfirstHalf = matchState.getFirstHalfGoalsB();
        int goalsASecondHalf = matchState.getSecondHalfGoalsA();
        int goalsBSecondHalf = matchState.getSecondHalfGoalsB();
        int yellowCardA = matchState.getYellowCardsA();
        int yellowCardB = matchState.getYellowCardsB();
        int redCardA = matchState.getRedCardsA();
        int redCardB = matchState.getRedCardsB();

        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        matchWinner.increment(teamId);
        goalsTotal.increment(normalTimeGoalsA + normalTimeGoalsB);
        goalsHandicapAsian.increment(normalTimeGoalsA - normalTimeGoalsB);
        if (normalTimeGoalsA > 0 && normalTimeGoalsB > 0)
            bothTeamToScore.increment(true);
        else
            bothTeamToScore.increment(false);
        cornersTotal.increment(normalTimeCornersA + normalTimeCornersB);
        cornersHandicap.increment(normalTimeCornersA - normalTimeCornersB);
        bookingsTotal.increment(yellowCardA * 10 + yellowCardB * 10 + redCardA * 25 + redCardB * 25);
        bookingsHandicap.increment(yellowCardA * 10 - yellowCardB * 10 + redCardA * 25 - redCardB * 25);
        cardsTotal.increment(yellowCardA + yellowCardB + redCardA + redCardB);
        if (prematch) {
            cardsTotalA.increment(yellowCardA + redCardA);
            cardsTotalB.increment(yellowCardB + redCardB);
        }
        cardHandicap.increment(yellowCardA - yellowCardB + redCardA - redCardB);

        if (this.isForParamFinder)
            return;
        /*
         * now process everything else
         */

        int goalsA = matchState.getGoalsA();
        int goalsB = matchState.getGoalsB();

        int goalCurrentHalfA = matchFacts.getCurrentPeriodGoalsA();
        int goalCurrentHalfB = matchFacts.getCurrentPeriodGoalsB();
        int cornerCurrentHalfA = matchFacts.getCurrentPeriodCornersA();
        int cornerCurrentHalfB = matchFacts.getCurrentPeriodCornersB();

        @SuppressWarnings("unused")
        TeamId teamCornerId = TeamId.UNKNOWN;
        TeamId teamBookingsId = TeamId.UNKNOWN;

        if (normalTimeCornersA > normalTimeCornersB)
            teamCornerId = TeamId.A;
        if (normalTimeCornersA < normalTimeCornersB)
            teamCornerId = TeamId.B;
        if ((yellowCardA * 10 + redCardA * 25) > (yellowCardB * 10 + redCardB * 25))
            teamBookingsId = TeamId.A;
        if ((yellowCardA * 10 + redCardA * 25) < (yellowCardB * 10 + redCardB * 25))
            teamBookingsId = TeamId.B;

        TeamId toQualify = matchState.getTwoLegsMatchWinner();
        if (toQualify != null)
            if (toQualify.equals(TeamId.A))
                matchWinnerFTETP.increment(true);
            else if (toQualify.equals(TeamId.B))
                matchWinnerFTETP.increment(false);

        matchWinnerEuroHandicap.increment(normalTimeGoalsA - normalTimeGoalsB);
        correctGoalScore.increment(normalTimeGoalsA, normalTimeGoalsB);

        bookingsWinner.increment(teamBookingsId);
        yellowATotal.increment(yellowCardA);
        yellowBTotal.increment(yellowCardB);


        goalsTotalA.increment(normalTimeGoalsA);
        goalsTotalB.increment(normalTimeGoalsB);
        incrementNWayStatistic2(totalGoalsHome, goalsA);
        incrementNWayStatistic2(totalGoalsAway, goalsB);
        incrementNWayStatistic2(totalGoalRangeCurrentHalf, goalCurrentHalfA + goalCurrentHalfB);
        incrementNWayStatistic2(totalGoalRangeCurrentHalfHome, goalCurrentHalfA);
        incrementNWayStatistic2(totalGoalRangeCurrentHalfAway, goalCurrentHalfB);
        goalsHandicap.increment(normalTimeGoalsA - normalTimeGoalsB);
        nextGoal.increment(matchFacts.getNextToScore());
        // goalsTotalCurrentHalf.increment(matchFacts.getGoalsTotalCurrentHalf());
        goalsHandicapCurrentHalf.increment(goalCurrentHalfA - goalCurrentHalfB);
        halfTimeCorrectGoalScore.increment(goalCurrentHalfA, goalCurrentHalfB);
        secondhalfTimeCorrectGoalScore.increment(matchState.getSecondHalfGoalsA(), matchState.getSecondHalfGoalsB());

        if (normalTimeGoalsA > 0 && normalTimeGoalsB > 0)
            bothTeamToScore.increment(true);
        else
            bothTeamToScore.increment(false);
        homeTeamWinFromBehind.increment(normalTimeGoalsA > normalTimeGoalsB && matchState.wasTeamBehind(TeamId.A));
        awayTeamWinFromBehind.increment(normalTimeGoalsB > normalTimeGoalsA && matchState.wasTeamBehind(TeamId.B));
        homeTeamLeadAtAnyTime.increment(matchState.wasTeamLeading(TeamId.A));
        awayTeamLeadAtAnyTime.increment(matchState.wasTeamLeading(TeamId.B));
        goalInBothHalves.increment(
                        matchState.getFirstHalfGoals().size() >= 1 && matchState.getSecondHalfGoals().size() >= 1);

        List<TeamId> goalsList = matchState.getFirstHalfGoals();
        int homeGoalList = 0;
        int awayGoalList = 0;
        for (int i = 0; i < goalsList.size(); i++) {
            if (goalsList.get(i) == TeamId.A)
                homeGoalList++;
            if (goalsList.get(i) == TeamId.B)
                awayGoalList++;
        }

        goalsList = matchState.getSecondHalfGoals();
        int homeGoalListSecond = 0;
        int awayGoalListSecond = 0;
        for (int i = 0; i < goalsList.size(); i++) {
            if (goalsList.get(i) == TeamId.A)
                homeGoalListSecond++;
            if (goalsList.get(i) == TeamId.B)
                awayGoalListSecond++;
        }

        incrementHighestHalf(highestScoreHalfHome, homeGoalList, homeGoalListSecond);
        incrementHighestHalf(highestScoreHalfAway, awayGoalList, awayGoalListSecond);


        incrementHighestHalf(highestScoreHalf, matchState.getFirstHalfGoals().size(),
                        matchState.getSecondHalfGoals().size());
        TeamId hsf = TeamId.UNKNOWN;
        if (((goalsAfirstHalf + goalsBfirstHalf) > (goalsASecondHalf + goalsBSecondHalf)))
            hsf = TeamId.A;
        else if ((goalsAfirstHalf + goalsBfirstHalf) < (goalsASecondHalf + goalsBSecondHalf))
            hsf = TeamId.B;
        highestScoreHalf.increment(hsf);
        incrementHighestHalf(halfWithMostCorners, matchState.getFirstHalfCorners().size(),
                        matchState.getSecondHalfCorners().size());

        int firstHalfCards = matchState.getFirstHalfYellowCards().size() + matchState.getFirstHalfRedCards().size();
        int secondHalfCards = matchState.getSecondHalfYellowCards().size() + matchState.getSecondHalfRedCards().size();
        incrementHighestHalf(halfWithMostCards, firstHalfCards, secondHalfCards);

        comeFromBehindAndDraw.increment((matchState.wasTeamBehind(TeamId.A) && goalsA == goalsB)
                        || (matchState.wasTeamBehind(TeamId.B) && goalsB == goalsA));
        comeFromBehindAndWin.increment((matchState.wasTeamBehind(TeamId.A) && goalsA > goalsB)
                        || (matchState.wasTeamBehind(TeamId.B) && goalsB > goalsA));
        comeFromBehindAndWinDraw.increment((matchState.wasTeamBehind(TeamId.A) && goalsA >= goalsB)
                        || (matchState.wasTeamBehind(TeamId.B) && goalsB >= goalsA));
        leadHalfTimeFailToWin.increment((matchState.wasTeamFirstHalfLeader(TeamId.A) && goalsA <= goalsB)
                        || (matchState.wasTeamFirstHalfLeader(TeamId.B) && goalsB <= goalsA));
        scoreFirstFailToWin.increment((matchState.didTeamScoreFirst(TeamId.A) && goalsA <= goalsB)
                        || (matchState.didTeamScoreFirst(TeamId.B) && goalsB <= goalsA));

        // Corner market
        cornersTotalCurrentHalf.increment(matchFacts.getCornersTotalCurrentHalf());
        // cornerMatchWinner.increment(teamCornerId);
        cornersTotalAsian.increment(normalTimeCornersA + normalTimeCornersB);
        // cornersTotalA.increment(normalTimeCornersA);
        // cornersTotalB.increment(normalTimeCornersB);
        nextCorner.increment(matchFacts.getNextToCorner());
        cornersHandicapCurrentHalf.increment(cornerCurrentHalfA - cornerCurrentHalfB);
        teamId = TeamId.UNKNOWN;
        teamCornerId = TeamId.UNKNOWN;

        if (cornerCurrentHalfA > cornerCurrentHalfB)
            teamCornerId = TeamId.A;
        if (cornerCurrentHalfA < cornerCurrentHalfB)
            teamCornerId = TeamId.B;

        // cornerMatchWinnerCurrentHalf.increment(teamCornerId);

        // half&&full time market
        if (periodNo < 2) {
            if (goalsA > goalsB && goalCurrentHalfA > goalCurrentHalfB)
                halfTimeFullTimeResult.increment(0);
            if (goalsA == goalsB && goalCurrentHalfA > goalCurrentHalfB)
                halfTimeFullTimeResult.increment(1);
            if (goalsA < goalsB && goalCurrentHalfA > goalCurrentHalfB)
                halfTimeFullTimeResult.increment(2);
            if (goalsA > goalsB && goalCurrentHalfA == goalCurrentHalfB)
                halfTimeFullTimeResult.increment(3);
            if (goalsA == goalsB && goalCurrentHalfA == goalCurrentHalfB)
                halfTimeFullTimeResult.increment(4);
            if (goalsA < goalsB && goalCurrentHalfA == goalCurrentHalfB)
                halfTimeFullTimeResult.increment(5);
            if (goalsA > goalsB && goalCurrentHalfA < goalCurrentHalfB)
                halfTimeFullTimeResult.increment(6);
            if (goalsA == goalsB && goalCurrentHalfA < goalCurrentHalfB)
                halfTimeFullTimeResult.increment(7);
            if (goalsA < goalsB && goalCurrentHalfA < goalCurrentHalfB)
                halfTimeFullTimeResult.increment(8);
        }
        if (periodNo < 2) {
            // First select a range: 1st, 2nd, or 3rd range of the selections,
            // then select the right one
            int winIndex = 0;
            if (matchState.wasTeamFirstHalfLeader(TeamId.UNKNOWN))
                winIndex = 3;
            else if (matchState.wasTeamFirstHalfLeader(TeamId.B))
                winIndex = 6;
            if (matchState.wasTeamSecondHalfLeader(TeamId.UNKNOWN))
                winIndex++;
            else if (matchState.wasTeamSecondHalfLeader(TeamId.B))
                winIndex += 2;
            firstHalfSecondHalfResults.increment(winIndex);
        }

        if (periodNo < 3) {
            int firstHalfGoalsA = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.A);
            int firstHalfGoalsB = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.B);
            int secondHalfGoalsA = goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.A);
            int secondHalfGoalsB = goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.B);

            if (firstHalfGoalsA > firstHalfGoalsB && secondHalfGoalsA > secondHalfGoalsB
                            || firstHalfGoalsA < firstHalfGoalsB && secondHalfGoalsA < secondHalfGoalsB)
                eitherTeamWinBothHalves.increment(true);
            else if (firstHalfGoalsA != firstHalfGoalsB && secondHalfGoalsA != secondHalfGoalsB)
                eitherTeamWinBothHalves.increment(false);

            // both teams scored in second half - robin working section
            // we are using the FullTime goals here as per the requirements
            // document
            if (goalsA > goalsB) // home team win 1h
            {
                secondHalfAwayTeamToWinBTTS.increment(false);
                secondHalfDrawBTTS.increment(false);
                secondHalfAwayTeamToWinBTNTS.increment(false);
                secondHalfDrawBTNTS.increment(false);

                if (secondHalfGoalsA > 0 && secondHalfGoalsB > 0) {
                    secondHalfHomeTeamToWinBTTS.increment(true);
                    secondHalfHomeTeamToWinBTNTS.increment(false);
                } else {
                    secondHalfHomeTeamToWinBTTS.increment(false);
                    secondHalfHomeTeamToWinBTNTS.increment(true);
                }
            } else if (goalsA < goalsB) // away team win 1h
            {
                secondHalfHomeTeamToWinBTTS.increment(false);
                secondHalfDrawBTTS.increment(false);
                secondHalfHomeTeamToWinBTNTS.increment(false);
                secondHalfDrawBTNTS.increment(false);

                if (secondHalfGoalsA > 0 && secondHalfGoalsB > 0) {
                    secondHalfAwayTeamToWinBTTS.increment(true);
                    secondHalfAwayTeamToWinBTNTS.increment(false);
                } else {
                    secondHalfAwayTeamToWinBTTS.increment(false);
                    secondHalfAwayTeamToWinBTNTS.increment(true);
                }
            } else // draw 1h
            {
                secondHalfHomeTeamToWinBTTS.increment(false);
                secondHalfAwayTeamToWinBTTS.increment(false);
                secondHalfHomeTeamToWinBTNTS.increment(false);
                secondHalfAwayTeamToWinBTTS.increment(false);

                if (secondHalfGoalsA > 0 && secondHalfGoalsB > 0) {
                    secondHalfDrawBTTS.increment(true);
                    secondHalfDrawBTNTS.increment(false);
                } else {
                    secondHalfDrawBTTS.increment(false);
                    secondHalfDrawBTNTS.increment(true);
                }
            }

        }

        if (periodNo < 2) {
            int firstHalfGoalsA = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.A);
            int firstHalfGoalsB = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.B);
            int secondHalfGoalsA = goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.A);
            int secondHalfGoalsB = goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.B);

            if (firstHalfGoalsA > firstHalfGoalsB && secondHalfGoalsA > secondHalfGoalsB
                            || firstHalfGoalsA < firstHalfGoalsB && secondHalfGoalsA < secondHalfGoalsB)
                eitherTeamWinBothHalves.increment(true);
            else if (firstHalfGoalsA != firstHalfGoalsB && secondHalfGoalsA != secondHalfGoalsB)
                eitherTeamWinBothHalves.increment(false);

            if (matchState.getGoalNo() > 1) {
                if (firstHalfGoalsA + firstHalfGoalsB > 0)
                    halfOfFirstGoal.increment(true);
                else if ((secondHalfGoalsA + secondHalfGoalsB > 0))
                    halfOfFirstGoal.increment(false);
            }

            if (matchState.getNormalTimeGoalsA() >= 1) {
                if (firstHalfGoalsA > 0)
                    halfOfFirstGoalA.increment(true);
                else
                    halfOfFirstGoalA.increment(false);
            }

            if (matchState.getNormalTimeGoalsB() >= 1) {
                if (firstHalfGoalsB > 0)
                    halfOfFirstGoalB.increment(true);
                else
                    halfOfFirstGoalB.increment(false);
            }

            Integer firstHalfSecondsHalfScoreIndex = getFirstHalfSecondHalfScoreSelectionIndex(firstHalfGoalsA,
                            firstHalfGoalsB, secondHalfGoalsA, secondHalfGoalsB);
            if (firstHalfSecondsHalfScoreIndex != null) {
                firstHalfSecondsHalfScores.increment(firstHalfSecondsHalfScoreIndex);
            }
            Integer firstHalfNormalTimeScoreIndex =
                            getFirstHalfNormalTimeScoreSelectionIndex(firstHalfGoalsA, firstHalfGoalsB, goalsA, goalsB);
            if (firstHalfNormalTimeScoreIndex != null) {
                firstHalfNormalTimeScores.increment(firstHalfNormalTimeScoreIndex);
            }

            // both teams scored in first half - robin working section
            if (firstHalfGoalsA > firstHalfGoalsB) // home team win 1h
            {
                firstHalfAwayTeamToWinBTTS.increment(false);
                firstHalfDrawBTTS.increment(false);
                firstHalfAwayTeamToWinBTNTS.increment(false);
                firstHalfDrawBTNTS.increment(false);

                if (firstHalfGoalsA > 0 && firstHalfGoalsB > 0) {
                    firstHalfHomeTeamToWinBTTS.increment(true);
                    firstHalfHomeTeamToWinBTNTS.increment(false);
                } else {
                    firstHalfHomeTeamToWinBTTS.increment(false);
                    firstHalfHomeTeamToWinBTNTS.increment(true);
                }
            } else if (firstHalfGoalsA < firstHalfGoalsB) // away team win 1h
            {
                firstHalfHomeTeamToWinBTTS.increment(false);
                firstHalfDrawBTTS.increment(false);
                firstHalfHomeTeamToWinBTNTS.increment(false);
                firstHalfDrawBTNTS.increment(false);

                if (firstHalfGoalsA > 0 && firstHalfGoalsB > 0) {
                    firstHalfAwayTeamToWinBTTS.increment(true);
                    firstHalfAwayTeamToWinBTNTS.increment(false);
                } else {
                    firstHalfAwayTeamToWinBTTS.increment(false);
                    firstHalfAwayTeamToWinBTNTS.increment(true);
                }
            } else // draw 1h
            {
                firstHalfHomeTeamToWinBTTS.increment(false);
                firstHalfAwayTeamToWinBTTS.increment(false);
                firstHalfHomeTeamToWinBTNTS.increment(false);
                firstHalfAwayTeamToWinBTTS.increment(false);

                if (firstHalfGoalsA > 0 && firstHalfGoalsB > 0) {
                    firstHalfDrawBTTS.increment(true);
                    firstHalfDrawBTNTS.increment(false);
                } else {
                    firstHalfDrawBTTS.increment(false);
                    firstHalfDrawBTNTS.increment(true);
                }
            }

        }
        if (goalsA - goalsB == 0)
            winningMargin.increment(0);
        if (goalsA - goalsB == 1)
            winningMargin.increment(1);
        if (goalsA - goalsB == 2)
            winningMargin.increment(2);
        if (goalsA - goalsB > 2)
            winningMargin.increment(3);
        if (goalsA - goalsB == -1)
            winningMargin.increment(4);
        if (goalsA - goalsB == -2)
            winningMargin.increment(5);
        if (goalsA - goalsB < -2)
            winningMargin.increment(6);

        if (goalsA + goalsB <= 1)
            totalGoalR.increment(0);
        if (goalsA + goalsB == 2 || goalsA + goalsB == 3)
            totalGoalR.increment(1);
        if (goalsA + goalsB >= 4)
            totalGoalR.increment(2);

        if (goalsA + goalsB == 0)
            totalGoalR2.increment(0);
        else if (goalsA + goalsB == 1)
            totalGoalR2.increment(1);
        else if (goalsA + goalsB == 2)
            totalGoalR2.increment(2);
        else if (goalsA + goalsB == 3)
            totalGoalR2.increment(3);
        else if (goalsA + goalsB == 4)
            totalGoalR2.increment(4);
        else if (goalsA + goalsB == 5)
            totalGoalR2.increment(5);
        else
            totalGoalR2.increment(6);

        boolean isNotShootOut = matchFacts.getFiveMinutesSequenceNo() < 24;
        if (isNotShootOut) {
            int[] fiveMinutesGoalA = matchState.getFiveMinsGoalA();
            int[] fiveMinutesGoalB = matchState.getFiveMinsGoalB();
            int currentFiveMinsArrayPosition = matchFacts.getFiveMinutesSequenceNo();
            int currentFifteenMinsArrayPosition = (currentFiveMinsArrayPosition) / 3;
            int currentTenMinsArrayPosition = (currentFiveMinsArrayPosition) / 2;
            int current30MinsArrayPosition = (currentFiveMinsArrayPosition) / 6;
            int current60MinsArrayPosition = (currentFiveMinsArrayPosition) / 10;
            int current75MinsArrayPosition = (currentFiveMinsArrayPosition) / 15;

            int fifteenMinsGoalsB = 0;
            int fifteenMinsGoalsA = 0;
            int fiveMinsGoalsA;
            int fiveMinsGoalsB;
            int tenMinsGoalsA = 0;
            int tenMinsGoalsB = 0;

            int thirtyMinsGoalsB = 0;
            int thirtyMinsGoalsA = 0;
            int sixtyMinsGoalsB = 0;
            int sixtyMinsGoalsA = 0;
            int seventyFiveMinsGoalsB = 0;
            int seventyFiveMinsGoalsA = 0;

            TeamId fiveMinsTeamId = TeamId.UNKNOWN;
            TeamId tenMinsTeamId = TeamId.UNKNOWN;
            TeamId fifteenMinsTeamId = TeamId.UNKNOWN;
            TeamId thirtyMinsTeamId = TeamId.UNKNOWN;
            TeamId sixtyMinsTeamId = TeamId.UNKNOWN;
            TeamId seventyFiveMinsTeamId = TeamId.UNKNOWN;

            fiveMinsGoalsA = fiveMinutesGoalA[currentFiveMinsArrayPosition];
            fiveMinsGoalsB = fiveMinutesGoalB[currentFiveMinsArrayPosition];

            if (fiveMinsGoalsA > fiveMinsGoalsB)
                fiveMinsTeamId = TeamId.A;
            if (fiveMinsGoalsA < fiveMinsGoalsB)
                fiveMinsTeamId = TeamId.B;
            fiveMinsWinner.increment(fiveMinsTeamId);

            int fiveMinsCorner = matchState.cornerInXXMins(matchFacts.getFiveMinutesSequenceNo(), TeamId.UNKNOWN, 1);
            fiveMinutesCornerOU.increment(fiveMinsCorner);

            fiveMinutesBooking.increment(
                            matchState.bookingNotYetHappend(matchFacts.getFiveMinutesSequenceNo(), 5, TeamId.UNKNOWN));

            tenMinutesBooking.increment(
                            matchState.bookingNotYetHappend(matchFacts.getFiveMinutesSequenceNo(), 10, TeamId.UNKNOWN));

            fifteenMinutesBooking.increment(
                            matchState.bookingNotYetHappend(matchFacts.getFiveMinutesSequenceNo(), 15, TeamId.UNKNOWN));
            int tenMinsCorner =
                            matchState.cornerInXXMins((matchFacts.getFiveMinutesSequenceNo() / 2), TeamId.UNKNOWN, 2);
            tenMinutesCornerOU.increment(tenMinsCorner);

            int fifteenMinsCorner =
                            matchState.cornerInXXMins((matchFacts.getFiveMinutesSequenceNo() / 3), TeamId.UNKNOWN, 3);
            fifteenMinutesCornerOU.increment(fifteenMinsCorner);

            goalScoredFiveMinutes.increment((fiveMinsGoalsA + fiveMinsGoalsB) > 0);
            int twentysGoalA = fiveMinutesGoalA[0] + fiveMinutesGoalA[1] + fiveMinutesGoalA[2] + fiveMinutesGoalA[3];
            int twentysGoalB = fiveMinutesGoalB[0] + fiveMinutesGoalB[1] + fiveMinutesGoalB[2] + fiveMinutesGoalB[3];
            goalScoredTwentyMinutes.increment((twentysGoalA + twentysGoalB) > 0);

            int thirtysGoalA = twentysGoalA + fiveMinutesGoalA[4] + fiveMinutesGoalA[5];
            int thirtysGoalB = twentysGoalB + fiveMinutesGoalB[4] + fiveMinutesGoalB[5];
            goalScoredThirtyMinutes.increment((thirtysGoalA + thirtysGoalB) > 0);

            int sixtyGoalA = fiveMinutesGoalA[0] + fiveMinutesGoalA[1] + fiveMinutesGoalA[2] + fiveMinutesGoalA[3]
                            + fiveMinutesGoalA[4] + fiveMinutesGoalA[5] + fiveMinutesGoalA[6] + fiveMinutesGoalA[7]
                            + fiveMinutesGoalA[8] + fiveMinutesGoalA[9] + fiveMinutesGoalA[10] + fiveMinutesGoalA[11];
            int sixtyGoalB = fiveMinutesGoalB[0] + fiveMinutesGoalB[1] + fiveMinutesGoalB[2] + fiveMinutesGoalB[3]
                            + fiveMinutesGoalB[4] + fiveMinutesGoalB[5] + fiveMinutesGoalB[6] + fiveMinutesGoalB[7]
                            + fiveMinutesGoalB[8] + fiveMinutesGoalB[9] + fiveMinutesGoalB[10] + fiveMinutesGoalB[11];

            goalScoredSixtyMinutes.increment((sixtyGoalA + sixtyGoalB) > 0);

            int seventyfiveGoalA = sixtyGoalA + fiveMinutesGoalA[12] + fiveMinutesGoalA[13] + fiveMinutesGoalA[14];
            int seventyfiveGoalB = sixtyGoalB + fiveMinutesGoalB[12] + fiveMinutesGoalB[13] + fiveMinutesGoalB[14];
            goalScoredSeventyFiveMinutes.increment((seventyfiveGoalA + seventyfiveGoalB) > 0);

            if (seventyFiveWinnerMarket) {
                for (int i = 0; i < 15; i++)
                    seventyFiveMinsGoalsA += fiveMinutesGoalA[i + 15 * current75MinsArrayPosition];
                for (int i = 0; i < 15; i++)
                    seventyFiveMinsGoalsB += fiveMinutesGoalB[i + 15 * current75MinsArrayPosition];
            }

            if (sixtyWinnerMarket) {
                for (int i = 0; i < 12; i++)
                    sixtyMinsGoalsA += fiveMinutesGoalA[i + 12 * current60MinsArrayPosition];
                for (int i = 0; i < 12; i++)
                    sixtyMinsGoalsB += fiveMinutesGoalB[i + 12 * current60MinsArrayPosition];
            }

            if (thirtyWinnerMarket) {
                for (int i = 0; i < 6; i++)
                    thirtyMinsGoalsA += fiveMinutesGoalA[i + 6 * current30MinsArrayPosition];
                for (int i = 0; i < 6; i++)
                    thirtyMinsGoalsB += fiveMinutesGoalB[i + 6 * current30MinsArrayPosition];
            }

            for (int i = 0; i < 3; i++)
                fifteenMinsGoalsA += fiveMinutesGoalA[i + 3 * currentFifteenMinsArrayPosition];
            for (int i = 0; i < 3; i++)
                fifteenMinsGoalsB += fiveMinutesGoalB[i + 3 * currentFifteenMinsArrayPosition];
            for (int i = 0; i < 2; i++)
                tenMinsGoalsA += fiveMinutesGoalA[i + 2 * currentTenMinsArrayPosition];
            for (int i = 0; i < 2; i++)
                tenMinsGoalsB += fiveMinutesGoalB[i + 2 * currentTenMinsArrayPosition];

            goalScoredTenMinutes.increment((tenMinsGoalsA + tenMinsGoalsB) > 0);
            goalScoredFifteenMinutes.increment((fifteenMinsGoalsA + fifteenMinsGoalsB) > 0);

            if (seventyFiveMinsGoalsA < seventyFiveMinsGoalsB)
                seventyFiveMinsTeamId = TeamId.B;
            else if (seventyFiveMinsGoalsA > seventyFiveMinsGoalsB)
                seventyFiveMinsTeamId = TeamId.A;
            seventyFiveMinsWinner.increment(seventyFiveMinsTeamId);

            if (sixtyMinsGoalsA < sixtyMinsGoalsB)
                sixtyMinsTeamId = TeamId.B;
            else if (sixtyMinsGoalsA > sixtyMinsGoalsB)
                sixtyMinsTeamId = TeamId.A;
            sixtyMinsWinner.increment(sixtyMinsTeamId);

            if (thirtyWinnerMarket) {
                if (thirtyMinsGoalsA < thirtyMinsGoalsB)
                    thirtyMinsTeamId = TeamId.B;
                else if (thirtyMinsGoalsA > thirtyMinsGoalsB)
                    thirtyMinsTeamId = TeamId.A;
                thirtyMinsWinner.increment(thirtyMinsTeamId);
            }
            if (fifteenMinsWinnerMarket) {
                if (fifteenMinsGoalsA < fifteenMinsGoalsB)
                    fifteenMinsTeamId = TeamId.B;
                if (fifteenMinsGoalsA > fifteenMinsGoalsB)
                    fifteenMinsTeamId = TeamId.A;
                fifteenMinsWinner.increment(fifteenMinsTeamId);
            }
            if (tenMinsGoalsA < tenMinsGoalsB)
                tenMinsTeamId = TeamId.B;
            if (tenMinsGoalsA > tenMinsGoalsB)
                tenMinsTeamId = TeamId.A;
            tenMinsWinner.increment(tenMinsTeamId);
        }

        /**
         * Jin working section
         **/
        int halfGoalsA = 0;
        int halfGoalsB = 0;
        if (periodNo < 2) {
            halfGoalsA = matchState.getFirstHalfGoalsA();
            halfGoalsB = matchState.getFirstHalfGoalsB();
        } else if (periodNo == 2) {
            halfGoalsA = matchState.getSecondHalfGoalsA();
            halfGoalsB = matchState.getSecondHalfGoalsB();
        }

        if (periodNo <= 2) { // only increment this market before fulltime
            if (goalCurrentHalfA > goalCurrentHalfB && ((goalCurrentHalfA + goalCurrentHalfB) == 2
                            || (goalCurrentHalfA + goalCurrentHalfB) == 3))
                firstHWinAndScore23.increment(true);
            else
                firstHWinAndScore23.increment(false);

            if (halfGoalsA < halfGoalsB && ((halfGoalsA + halfGoalsB) == 2 || (halfGoalsA + halfGoalsB) == 3))
                firstHWinBndScore23.increment(true);
            else
                firstHWinBndScore23.increment(false);
        }

        goalsTotalCurrentHalf.increment(goalCurrentHalfA + goalCurrentHalfB);
        teamId = TeamId.UNKNOWN;
        if (goalCurrentHalfA > goalCurrentHalfB) {
            teamId = TeamId.A;
        } else if (goalCurrentHalfA < goalCurrentHalfB) {
            teamId = TeamId.B;
        }

        matchWinnerCurrentHalf.increment(teamId);

        if (firstCornerMarket)
            firstCornerTeam.increment(matchState.getFirstCorner());
        correctCornerScore.increment(normalTimeCornersA, normalTimeCornersB);
        halfTimeCorrectCornerScore.increment(cornerCurrentHalfA, cornerCurrentHalfB);
        // euroCornerHandicap.increment(matchState.getCornersA() - matchState.getCornersB());

        // halfEuroCornerHandicap.increment(matchFacts.getCurrentPeriodCornersA() -
        // matchFacts.getCurrentPeriodCornersB());

        if (matchFacts.getCurrentPeriodCornersA() > matchFacts.getCurrentPeriodCornersB())
            teamId = TeamId.A;
        else if (matchFacts.getCurrentPeriodCornersA() < matchFacts.getCurrentPeriodCornersB())
            teamId = TeamId.B;
        else
            teamId = TeamId.UNKNOWN;
        // halfMostCorners.increment(teamId);
        halfCornerWinner.increment(teamId);

        // halfHomeCornerOU.increment(matchFacts.getCurrentPeriodCornersA());
        // halfAwayCornerOU.increment(matchFacts.getCurrentPeriodCornersB());

        // uncomment below for the extra time markets
        if (extraTimePossible) {// firstly needed to be extra time possible
            if (normalTimeGoalsA == normalTimeGoalsB && goalsA != goalsB && matchState.getShootOutTimeCounter() == 0)
                toWinInET.increment(true);
            else
                toWinInET.increment(false);

            if (normalTimeGoalsA == normalTimeGoalsB) {
                if (matchState.getShootOutTimeCounter() != 0)
                    extraTimeResult.increment(TeamId.UNKNOWN);
                else if (goalsA > goalsB)
                    extraTimeResult.increment(TeamId.A);
                else if (goalsA < goalsB)
                    extraTimeResult.increment(TeamId.B);
            }

            if (matchState.isExtraTimeEnteredFlag())// only increment if entered
                                                    // extra time
            {
                int eA = matchState.getExtraTimeFHGoalsA() + matchState.getExtraTimeSHGoalsA();
                int eB = matchState.getExtraTimeFHGoalsB() + matchState.getExtraTimeSHGoalsB();
                extraTimeOU.increment(eA + eB);
                extraTimeCorrectGoalScore.increment(eA, eB);

                if (matchFacts.getStartingPeriodNo() <= 3) {
                    if (matchState.getExtraTimeFHGoalsA() > matchState.getExtraTimeFHGoalsB())
                        extraTimeHalfResult.increment(TeamId.A);
                    else if (matchState.getExtraTimeFHGoalsA() < matchState.getExtraTimeFHGoalsB())
                        extraTimeHalfResult.increment(TeamId.B);
                    else
                        extraTimeHalfResult.increment(TeamId.UNKNOWN);

                    extraTimeHalfOU.increment(matchState.getExtraTimeFHGoalsA() + matchState.getExtraTimeFHGoalsB());

                } else if (matchFacts.getStartingPeriodNo() == 4) {
                    if (matchState.getExtraTimeSHGoalsA() > matchState.getExtraTimeSHGoalsB())
                        extraTimeHalfResult.increment(TeamId.A);
                    else if (matchState.getExtraTimeSHGoalsA() < matchState.getExtraTimeSHGoalsB())
                        extraTimeHalfResult.increment(TeamId.B);
                    else
                        extraTimeHalfResult.increment(TeamId.UNKNOWN);

                    extraTimeHalfOU.increment(matchState.getExtraTimeSHGoalsA() + matchState.getExtraTimeSHGoalsB());

                }

            }
        }

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
        TeamId firstTeamScore = matchState.checkFirstTeamScored();
        firstTeamToScore.increment(firstTeamScore);

        TeamId firstTeamScoreFirstHalf = matchState.checkFirstTeamScoredFirstHalf();
        firstTeamToScoreHalf.increment(firstTeamScoreFirstHalf);

        firstTeamScore = matchState.checkLastTeamScored();
        lastTeamToScore.increment(firstTeamScore);

        if (normalTimeGoalsA > normalTimeGoalsB && normalTimeGoalsB == 0)
            homeWinToNil.increment(true);
        else
            homeWinToNil.increment(false);

        if (matchFacts.getStartingPeriodNo() < 2) {
            homeWinToNil1Half.increment(halfGoalsA > halfGoalsB && halfGoalsB == 0);
            awayWinToNil1Half.increment(halfGoalsA < halfGoalsB && halfGoalsA == 0);
        } else if (matchFacts.getStartingPeriodNo() < 3) {
            homeWinToNil1Half.increment(halfGoalsA > halfGoalsB && halfGoalsB == 0);
            awayWinToNil1Half.increment(halfGoalsA < halfGoalsB && halfGoalsA == 0);
        }

        if (normalTimeGoalsA < normalTimeGoalsB && normalTimeGoalsA == 0)
            awayWinToNil.increment(true);
        else
            awayWinToNil.increment(false);

        if (normalTimeGoalsA > normalTimeGoalsB && normalTimeGoalsB != 0)
            homeWinNotToNil.increment(true);
        else
            homeWinNotToNil.increment(false);

        if (normalTimeGoalsA > normalTimeGoalsB
                        && (normalTimeGoalsB + normalTimeGoalsA == 2 || normalTimeGoalsB + normalTimeGoalsA == 3))
            homeWin23Goals.increment(true);
        else
            homeWin23Goals.increment(false);

        if (normalTimeGoalsA < normalTimeGoalsB
                        && (normalTimeGoalsB + normalTimeGoalsA == 2 || normalTimeGoalsB + normalTimeGoalsA == 3))
            awayWin23Goals.increment(true);
        else
            awayWin23Goals.increment(false);

        if (normalTimeGoalsB > normalTimeGoalsA && normalTimeGoalsA != 0)
            awayWinNotToNil.increment(true);
        else
            awayWinNotToNil.increment(false);

        if (this.goalNotHappendYet) {
            timeOfFirstGoal.increment(matchState.checkTimeFirstGoal(TeamId.UNKNOWN, 10));
        }
        if (this.goalHomeNotHappendYet) {
            timeOfFirstGoalHome.increment(matchState.checkTimeFirstGoal(TeamId.A, 10));
        }
        if (this.goalAwayNotHappendYet) {
            timeOfFirstGoalAway.increment(matchState.checkTimeFirstGoal(TeamId.B, 10));
        }

        if (matchFacts.getStartingPeriodNo() < 3) {
            homeTeamToScore.increment(matchState.getNormalTimeGoalsA() > 0);
            awayTeamToScore.increment(matchState.getNormalTimeGoalsB() > 0);
            homeTeamToScore2.increment(matchState.getNormalTimeGoalsA() > 1);
            awayTeamToScore2.increment(matchState.getNormalTimeGoalsB() > 1);
            homeTeamToScore3.increment(matchState.getNormalTimeGoalsA() > 2);
            awayTeamToScore3.increment(matchState.getNormalTimeGoalsB() > 2);
        }

        if (htts2h) {
            boolean temp = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.A) > 0
                            && goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.A) > 0;
            homeTeamToScoreBothHalves.increment(temp);
        }
        if (atts2h) {
            boolean temp = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.B) > 0
                            && goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.B) > 0;
            awayTeamToScoreBothHalves.increment(temp);
        }

        if (htts1h) {
            boolean temp = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.A) > 0
                            || goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.A) > 0;
            homeTeamToScoreEitherHalves.increment(temp);
        }
        if (atts1h) {
            boolean temp = goalScoredForTeam(matchState.getFirstHalfGoals(), TeamId.B) > 0
                            || goalScoredForTeam(matchState.getSecondHalfGoals(), TeamId.B) > 0;
            awayTeamToScoreEitherHalves.increment(temp);
        }

        if (cornerNotHappendYet) {
            int secs = matchState.getFirstCornerTimeSlot();
            if (secs != -1)
                timeOfFirstCorner.increment(secs);
            else
                timeOfFirstCorner.increment(9);
        }

        if (cardNotHappendYet) {
            int secs = matchState.getFirstCardTimeSlot();
            if (secs != -1)
                timeOfFirstCard.increment(secs);
            else
                timeOfFirstCard.increment(9);
        }
        /*** Jin working section ends ****/

        /**
         * Robert working section
         **/
        halfTimegoalsTotalA.increment(goalCurrentHalfA);
        halfTimegoalsTotalB.increment(goalCurrentHalfB);
        if (periodNo <= 1)
            if (goalCurrentHalfA > 0 && goalCurrentHalfB > 0)
                halfTimeBothTeamToScore.increment(true);
            else
                halfTimeBothTeamToScore.increment(false);

        if (periodNo <= 2)
            if (matchState.getSecondHalfGoalsA() > 0 && (matchState.getSecondHalfGoalsB() > 0))
                halfTimeBothTeamToScore2.increment(true);
            else
                halfTimeBothTeamToScore2.increment(false);

        halfTimeMatchWinnerEuroHandicap.increment(goalCurrentHalfA - goalCurrentHalfB);

        if (goalsA > 0 && goalsB > 0)
            noOfTeamsScore.increment(TeamId.UNKNOWN);
        if ((goalsA > 0 && goalsB == 0) || (goalsA == 0 && goalsB > 0))
            noOfTeamsScore.increment(TeamId.B);
        if (goalsA == 0 && goalsB == 0)
            noOfTeamsScore.increment(TeamId.A);
        goalsTotalOddEven.increment(isOdd(goalsA + goalsB));
        goalsTotalHomeOddEven.increment(isOdd(goalsA));
        goalsTotalAwayOddEven.increment(isOdd(goalsB));
        halfTimeGoalsTotalOddEven.increment(isOdd(goalCurrentHalfA + goalCurrentHalfB));
        halfTimeGoalsTotalHomeOddEven.increment(isOdd(goalCurrentHalfA));
        halfTimeGoalsTotalAwayOddEven.increment(isOdd(goalCurrentHalfB));
        goalsTotalAsian.increment(normalTimeGoalsA + normalTimeGoalsB);

        halfTimeGoalsTotalAsian.increment(goalCurrentHalfA + goalCurrentHalfB);
        halfTimeGoalsHandicapAsian.increment(goalCurrentHalfA - goalCurrentHalfB);
        if (normalTimeGoalsA >= normalTimeGoalsB && normalTimeGoalsB == 0)
            homeCleanSheet.increment(true);
        else
            homeCleanSheet.increment(false);

        if (normalTimeGoalsA <= normalTimeGoalsB && normalTimeGoalsA == 0)
            awayCleanSheet.increment(true);
        else
            awayCleanSheet.increment(false);



        if (goalsAfirstHalf > goalsBfirstHalf && goalsASecondHalf > goalsBSecondHalf)
            homeWinBothHalves.increment(true);
        else
            homeWinBothHalves.increment(false);

        if (goalsAfirstHalf < goalsBfirstHalf && goalsASecondHalf < goalsBSecondHalf)
            awayWinBothHalves.increment(true);
        else
            awayWinBothHalves.increment(false);


        if (goalsAfirstHalf > goalsBfirstHalf || goalsASecondHalf > goalsBSecondHalf)
            homeWinEitherHalves.increment(true);
        else
            homeWinEitherHalves.increment(false);
        if (goalsAfirstHalf < goalsBfirstHalf || goalsASecondHalf < goalsBSecondHalf)
            awayWinEitherHalves.increment(true);
        else
            awayWinEitherHalves.increment(false);

        TeamId teamToCorners;
        int indexCorner = 3;
        if (!matchState.isExtraTimeEnteredFlag()) {
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo3Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo3Corners.increment(TeamId.A);
                            raceTo3CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo3Corners.increment(TeamId.B);
                            raceTo3CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }
            indexCorner = 5;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo5Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo5Corners.increment(TeamId.A);
                            raceTo5CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo5Corners.increment(TeamId.B);
                            raceTo5CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }
            indexCorner = 7;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo7Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo7Corners.increment(TeamId.A);
                            raceTo7CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo7Corners.increment(TeamId.B);
                            raceTo7CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }

            indexCorner = 9;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo9Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo9Corners.increment(TeamId.A);
                            raceTo9CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo9Corners.increment(TeamId.B);
                            raceTo9CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }

            indexCorner = 11;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo11Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo11Corners.increment(TeamId.A);
                            raceTo11CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo11Corners.increment(TeamId.B);
                            raceTo11CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }

            indexCorner = 13;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo13Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo13Corners.increment(TeamId.A);
                            raceTo13CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo13Corners.increment(TeamId.B);
                            raceTo13CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }
            indexCorner = 15;
            if (currentCornerA < indexCorner && currentCornerB < indexCorner) {
                teamToCorners = matchState.checkTeamToXCorners(indexCorner, 0);
                if (teamToCorners == null)
                    raceTo15Corners.increment(TeamId.UNKNOWN);
                else
                    switch (teamToCorners) {
                        case A:
                            raceTo15Corners.increment(TeamId.A);
                            raceTo15CornersTwoWay.increment(true);
                            break;
                        case B:
                            raceTo15Corners.increment(TeamId.B);
                            raceTo15CornersTwoWay.increment(false);
                            break;
                        default:
                            break;
                    }
            }
        }

        /*** Robert working section ends ****/
        matchState.setGoalInfo(null);
    }

    private void incrementNWayStatistic2(NWayStatistic nWayStatistic, int currentValue) {
        if (currentValue < 1)
            nWayStatistic.increment(0);
        if (currentValue == 1)
            nWayStatistic.increment(1);
        if (currentValue == 2)
            nWayStatistic.increment(2);
        if (currentValue >= 3)
            nWayStatistic.increment(2);
    }

    @Override
    public void addDerivedMarkets(Markets markets, MatchState matchState, MatchParams matchParams) {
        String sequenceID = "";
        int lineProbLength = 50;
        if (!((FootballMatchState) matchState).isNormalTimeMatchCompleted()) {

            Market csSourceMarket = markets.get("FT:CS");
            Map<String, Double> csGrid = csSourceMarket.getSelectionsProbs();
            Market csSourcePeriodMarket = markets.get("P:CS", periodSequenceId);
            Map<String, Double> csGridPeriod = csSourcePeriodMarket.getSelectionsProbs();
            Market csSource2PeriodMarket = markets.get("P:CS", String.format("H%d", 2));
            Map<String, Double> csGrid2Period = csSource2PeriodMarket.getSelectionsProbs();
            /*
             * corner markets
             */
            Market csCornerMarket = markets.get("FT:CCS");
            Map<String, Double> cscGrid = csCornerMarket.getSelectionsProbs();
            Market csConerePeriodMarket = markets.get("P:CCS", periodSequenceId);
            Map<String, Double> cscGridPeriod = csConerePeriodMarket.getSelectionsProbs();
            double[][] cScoreGrid;
            cScoreGrid = creatScoreGrid(cscGrid);
            double[][] cScoreGridPeriod;
            cScoreGridPeriod = creatScoreGrid(cscGridPeriod);
            String marketDescription = "A";
            String string2 = "";
            String string3 = "";
            Market euroCornerHandicap = new Market(MarketCategory.HCAPEU, MarketGroup.CORNERS, "FT:CEHCP", "M",
                            "Corner Euro Handicap");
            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription = "First half Corner Euro Handicap";
                    string2 = "First Half Home Team Corner Over/Under";
                    string3 = "First Half Away Team Corner Over/Under";
                    break;
                case 2: // half time
                    marketDescription = "Second half Corner Euro Handicap";
                    string2 = "Second Half Home Team Corner Over/Under";
                    string3 = "Second Half Away Team Corner Over/Under";
                    break;
                case 3: // 1st half extra time
                    break;
                case 4: // 2nd half extra time
                    break;
                default:
                    marketDescription = "";
            }
            sequenceID = periodSequenceId;
            Market euroHalfCornerHandicap = new Market(MarketCategory.HCAPEU, MarketGroup.CORNERS, "P:CEHCP",
                            sequenceID, marketDescription);

            euroHalfCornerHandicap.setIsValid(true);
            euroHalfCornerHandicap.setSequenceId(sequenceID);
            euroCornerHandicap.setIsValid(true);

            double[][] ovunProbs = new double[lineProbLength][3];
            double[] lineProbs = new double[lineProbLength];
            double temp = 0.5;
            int lineOvun = 0;
            for (int i = 0; i < lineProbLength; i++) {
                ovunProbs[i] = generatOVUNDrawProbs(cScoreGrid, i - lineProbLength / 2);
                lineProbs[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][2] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][2] - 0.5);
                    lineOvun = i;
                }
                if (Math.abs(ovunProbs[i][2] - ovunProbs[i][0]) < temp) {
                    temp = Math.abs(ovunProbs[i][2] - ovunProbs[i][0]);
                    lineOvun = i;
                }
            }
            marketDescription = "AH";
            euroCornerHandicap.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "EH";
            euroCornerHandicap.put(marketDescription, ovunProbs[lineOvun][1]);
            marketDescription = "BH";
            euroCornerHandicap.put(marketDescription, ovunProbs[lineOvun][2]);
            euroCornerHandicap.setLineId(String.valueOf(lineProbLength / 2 - lineOvun) + ".0");
            euroCornerHandicap.setBalancedLine(lineProbLength / 2 - lineOvun - 0.5);
            euroCornerHandicap.setLineBase(0 - lineProbLength / 2);
            euroCornerHandicap.setLineProbs(lineProbs);
            markets.addMarketWithFullKey(euroCornerHandicap);
            ovunProbs = new double[lineProbLength][3];
            lineProbs = new double[lineProbLength];
            temp = 0.5;
            lineOvun = 0;
            for (int i = 0; i < ovunProbs.length; i++) {
                ovunProbs[i] = generatOVUNDrawProbs(cScoreGridPeriod, i - lineProbLength / 2);
                lineProbs[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][2] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][2] - 0.5);
                    lineOvun = i;
                }
                if (Math.abs(ovunProbs[i][2] - ovunProbs[i][0]) < temp) {
                    temp = Math.abs(ovunProbs[i][2] - ovunProbs[i][0]);
                    lineOvun = i;
                }
            }
            marketDescription = "AH";
            euroHalfCornerHandicap.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "EH";
            euroHalfCornerHandicap.put(marketDescription, ovunProbs[lineOvun][1]);
            marketDescription = "BH";
            euroHalfCornerHandicap.put(marketDescription, ovunProbs[lineOvun][2]);
            euroHalfCornerHandicap.setLineId(String.valueOf(lineProbLength / 2 - lineOvun) + ".0");
            euroHalfCornerHandicap.setBalancedLine(lineProbLength / 2 - lineOvun - 0.5);
            euroHalfCornerHandicap.setLineBase(0 - lineProbLength / 2);
            euroHalfCornerHandicap.setLineProbs(lineProbs);
            markets.addMarketWithFullKey(euroHalfCornerHandicap);


            /*** Jin moved corner winner down here ****/

            Market market = new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "P:CAXB", periodSequenceId,
                            "Period Corner Winner");
            market.setIsValid(true);
            double[] probs = {0, 0, 0, 0, 0, 0};
            probs = generateWinnerProbsFromScoreGrid(cScoreGridPeriod);
            market.put("A", probs[0]);
            market.put("X", probs[1]);
            market.put("B", probs[2]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "FT:CAXB", "M", "Match Corner Winner");
            market.setIsValid(true);
            probs = generateWinnerProbsFromScoreGrid(cScoreGrid);
            market.put("A", probs[0]);
            market.put("X", probs[1]);
            market.put("B", probs[2]);
            markets.addMarketWithShortKey(market);

            Market cornersTotalA = new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "FT:A:CTOT", "M",
                            "Home Team Total Corners");
            Market cornersTotalB = new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "FT:B:COU", "M",
                            "Away Team Total Corners");
            cornersTotalA.setIsValid(true);
            cornersTotalB.setIsValid(true);
            ovunProbs = new double[lineProbLength][3];
            lineProbs = new double[lineProbLength];
            double[] lineProbsB = new double[lineProbLength];
            temp = 0.5;
            lineOvun = 0;
            for (int i = 0; i < ovunProbs.length; i++) {
                ovunProbs[i][0] = generatHomeOVUNProbs(cScoreGrid, i);
                lineProbs[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][0] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][0] - 0.5);
                    lineOvun = i;
                }
            }
            marketDescription = "Over";
            cornersTotalA.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "Under";
            cornersTotalA.put(marketDescription, 1 - ovunProbs[lineOvun][0]);
            cornersTotalA.setLineId(String.valueOf(lineOvun) + ".5");
            cornersTotalA.setBalancedLine(lineOvun + 0.5);
            cornersTotalA.setLineBase(0);
            cornersTotalA.setLineProbs(lineProbs);
            markets.addMarketWithFullKey(cornersTotalA);

            temp = 0.5;
            lineOvun = 0;
            for (int i = 0; i < ovunProbs.length; i++) {
                ovunProbs[i][0] = generatAwayOVUNProbs(cScoreGrid, i);
                lineProbsB[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][0] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][0] - 0.5);
                    lineOvun = i;
                }
            }
            marketDescription = "Over";
            cornersTotalB.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "Under";
            cornersTotalB.put(marketDescription, 1 - ovunProbs[lineOvun][0]);
            cornersTotalB.setLineId(String.valueOf(lineOvun) + ".5");
            cornersTotalB.setBalancedLine(lineOvun + 0.5);
            cornersTotalB.setLineBase(0);
            cornersTotalB.setLineProbs(lineProbsB);
            markets.addMarketWithFullKey(cornersTotalB);

            Market halfHomeCornerOU =
                            new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "P:A:COU", periodSequenceId, string2);
            Market halfAwayCornerOU =
                            new Market(MarketCategory.OVUN, MarketGroup.CORNERS, "P:B:COU", periodSequenceId, string3);
            halfHomeCornerOU.setIsValid(true);
            halfAwayCornerOU.setIsValid(true);

            ovunProbs = new double[lineProbLength][3];
            lineProbs = new double[lineProbLength];
            temp = 0.5;
            lineOvun = 0;
            for (int i = 0; i < ovunProbs.length; i++) {
                ovunProbs[i][0] = generatHomeOVUNProbs(cScoreGridPeriod, i);
                lineProbs[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][0] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][0] - 0.5);
                    lineOvun = i;
                }
            }
            marketDescription = "Over";
            halfHomeCornerOU.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "Under";
            halfHomeCornerOU.put(marketDescription, 1 - ovunProbs[lineOvun][0]);
            halfHomeCornerOU.setLineId(String.valueOf(lineOvun) + ".5");
            halfHomeCornerOU.setBalancedLine(lineOvun + 0.5);
            halfHomeCornerOU.setLineBase(0);
            halfHomeCornerOU.setLineProbs(lineProbs);
            markets.addMarketWithFullKey(halfHomeCornerOU);

            temp = 0.5;
            lineOvun = 0;
            for (int i = 0; i < ovunProbs.length; i++) {
                ovunProbs[i][0] = generatAwayOVUNProbs(cScoreGridPeriod, i);
                lineProbs[i] = 1 - ovunProbs[i][0];
                if (Math.abs(ovunProbs[i][0] - 0.5) < temp) {
                    temp = Math.abs(ovunProbs[i][0] - 0.5);
                    lineOvun = i;
                }
            }
            marketDescription = "Over";
            halfAwayCornerOU.put(marketDescription, ovunProbs[lineOvun][0]);
            marketDescription = "Under";
            halfAwayCornerOU.put(marketDescription, 1 - ovunProbs[lineOvun][0]);
            halfAwayCornerOU.setLineId(String.valueOf(lineOvun) + ".5");
            halfAwayCornerOU.setBalancedLine(lineOvun + 0.5);
            halfAwayCornerOU.setLineBase(0);
            halfAwayCornerOU.setLineProbs(lineProbs);
            markets.addMarketWithFullKey(halfAwayCornerOU);


            double[][] scoreGrid;
            scoreGrid = creatScoreGrid(csGrid);
            double[][] scoreGridPeriod;
            scoreGridPeriod = creatScoreGrid(csGridPeriod);
            double[][] scoreGridSecondHalf;
            scoreGridSecondHalf = creatScoreGrid(csGrid2Period);
            // double[] probs = {0, 0, 0, 0, 0, 0};
            Market axbMarket = markets.get("FT:AXB");
            Map<String, Double> matchResult = axbMarket.getSelectionsProbs();
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DNB", "M", "Draw no bet");
            market.setIsValid(true);
            marketDescription = "A";
            probs[0] = matchResult.get("A") / (matchResult.get("A") + matchResult.get("B"));
            market.put(marketDescription, probs[0]);
            marketDescription = "B";
            probs[1] = matchResult.get("B") / (matchResult.get("A") + matchResult.get("B"));
            market.put(marketDescription, probs[1]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:A:NB", "M", "Home no bet");
            market.setIsValid(true);
            probs[0] = matchResult.get("X") / (matchResult.get("B") + matchResult.get("X"));
            market.put("X", probs[0]);
            probs[1] = matchResult.get("B") / (matchResult.get("X") + matchResult.get("B"));
            market.put("B", probs[1]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:B:NB", "M", "Away no bet");
            market.setIsValid(true);
            probs[0] = matchResult.get("X") / (matchResult.get("A") + matchResult.get("X"));
            market.put("X", probs[0]);
            probs[1] = matchResult.get("A") / (matchResult.get("X") + matchResult.get("A"));
            market.put("A", probs[1]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DBLC", "M", "Double Chance");
            market.setIsValid(true);
            marketDescription = "AX";
            probs[0] = matchResult.get("A") + matchResult.get("X");
            market.put(marketDescription, probs[0]);
            marketDescription = "AB";
            probs[1] = matchResult.get("A") + matchResult.get("B");
            market.put(marketDescription, probs[1]);
            marketDescription = "BX";
            probs[2] = matchResult.get("B") + matchResult.get("X");
            market.put(marketDescription, probs[2]);
            markets.addMarketWithShortKey(market);

            Market sourceMarket = markets.get("FT:OU");
            String balancedLineId = sourceMarket.getLineId();
            double dBalanceLineId = Double.parseDouble(balancedLineId);
            String line = balancedLineId;
            double bLine = dBalanceLineId;

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:WTTS", "M", "Which Team To Score?");
            market.setIsValid(true);
            double[] whichTeamToScoreProbs = {0, 0, 0, 0};
            whichTeamToScoreProbs = generateWinnerProbsFromScoreGridForWTTS(scoreGrid);
            market.put("A", whichTeamToScoreProbs[0]);
            market.put("B", whichTeamToScoreProbs[1]);
            market.put("Both teams", whichTeamToScoreProbs[2]);
            market.put("None", whichTeamToScoreProbs[3]);
            markets.addMarketWithShortKey(market);

            if (bLine > 1 && bLine <= 3) {
                for (double startLine = 1.5; startLine <= 4.5; startLine++) {

                    String dLine = String.valueOf(startLine);
                    sequenceID = "F" + (int) startLine;
                    market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:AXB$OU", sequenceID,
                                    "Match Result and Over/Under");
                    market.setLineProbs(sourceMarket.getLineProbs());
                    market.setIsValid(true);
                    probs = generateMatchResultAndOVUNProbs(scoreGrid, startLine);
                    marketDescription = "BOver";
                    market.put(marketDescription, probs[0]);
                    marketDescription = "XOver";
                    market.put(marketDescription, probs[1]);
                    marketDescription = "AOver";
                    market.put(marketDescription, probs[2]);
                    marketDescription = "BUnder";
                    market.put(marketDescription, probs[3]);
                    marketDescription = "XUnder";
                    market.put(marketDescription, probs[4]);
                    marketDescription = "AUnder";
                    market.put(marketDescription, probs[5]);
                    market.setLineId(dLine);
                    markets.addMarketWithShortKey(market);
                }
            } else if (bLine > 3) {
                int sequence = 1;
                for (double startLine = bLine - 1; startLine <= bLine + 2; startLine++) {
                    String dLine = String.valueOf(startLine);
                    sequenceID = "F" + (sequence++);
                    market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:AXB$OU", sequenceID,
                                    "Match Result and Over/Under");
                    market.setIsValid(true);
                    market.setLineProbs(sourceMarket.getLineProbs());
                    probs = generateMatchResultAndOVUNProbs(scoreGrid, startLine);
                    marketDescription = "BOver";
                    market.put(marketDescription, probs[0]);
                    marketDescription = "XOver";
                    market.put(marketDescription, probs[1]);
                    marketDescription = "AOver";
                    market.put(marketDescription, probs[2]);
                    marketDescription = "BUnder";
                    market.put(marketDescription, probs[3]);
                    marketDescription = "XUnder";
                    market.put(marketDescription, probs[4]);
                    marketDescription = "AUnder";
                    market.put(marketDescription, probs[5]);
                    market.setLineId(dLine);
                    markets.addMarketWithShortKey(market);
                }
            }
            // handicap double chance
            sequenceID = "3";
            market = new Market(MarketCategory.HCAP, MarketGroup.GOALS, "FT:DBLC$3HCP", "M",
                            "Soccer Handicap Double Chance");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateDBLCHCPProbs(scoreGrid, 3);
            market.put("AHBH", probs[0]);
            market.put("AHXH", probs[1]);
            market.put("BHXH", probs[2]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);


            // cs
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CSFLEX", "M",
                            "Soccer Correct Score Permutations");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateCSPermutationsProbs(scoreGrid);
            market.put("0-0", probs[0]);
            market.put("1-0", probs[1]);
            market.put("2-0", probs[2]);
            market.put("3-0", probs[3]);
            market.put("4-0", probs[4]);
            market.put("5+-0", probs[5]);
            market.put("2-1", probs[6]);
            market.put("3-1", probs[7]);
            market.put("4-1", probs[8]);
            market.put("5+-1", probs[9]);
            market.put("3-2", probs[10]);
            market.put("4-2", probs[11]);
            market.put("5+-2", probs[12]);
            market.put("4-3", probs[13]);
            market.put("5+-3", probs[14]);
            market.put("5+-4", probs[15]);
            market.put("1-1", probs[16]);
            market.put("2-2", probs[17]);
            market.put("3-3", probs[18]);
            market.put("4-4", probs[19]);
            market.put("5+-5+", probs[20]);
            market.put("4-5+", probs[21]);
            market.put("3-4", probs[22]);
            market.put("3-5+", probs[23]);
            market.put("2-3", probs[24]);
            market.put("2-4", probs[25]);
            market.put("2-5+", probs[26]);
            market.put("1-2", probs[27]);
            market.put("1-3", probs[28]);
            market.put("1-4", probs[29]);
            market.put("1-5+", probs[30]);
            market.put("0-1", probs[31]);
            market.put("0-2", probs[32]);
            market.put("0-3", probs[33]);
            market.put("0-4", probs[34]);
            market.put("0-5+", probs[35]);
            markets.addMarketWithShortKey(market);


            // cs2
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:£CS_2", periodSequenceId,
                            "Soccer Correct Score Permutations");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generatePeriodCSPermutationsProbs(scoreGridPeriod);
            market.put("0-0", probs[0]);
            market.put("1-0", probs[1]);
            market.put("2-0", probs[2]);
            market.put("3+-0", probs[3]);
            market.put("2-1", probs[4]);
            market.put("3+-1", probs[5]);
            market.put("3+-2", probs[6]);
            market.put("1-1", probs[7]);
            market.put("2-2", probs[8]);
            market.put("3+-3+", probs[9]);
            market.put("2-3+", probs[10]);
            market.put("1-2", probs[11]);
            market.put("1-3+", probs[12]);
            market.put("0-1", probs[13]);
            market.put("0-2", probs[14]);
            market.put("0-3+", probs[15]);
            markets.addMarketWithShortKey(market);

            // P:£OUR_2
            String periodInfo = "";
            if (periodSequenceId == "H1")
                periodInfo = "First ";
            else if (periodSequenceId == "H2")
                periodInfo = "Second ";

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:£OUR_2", periodSequenceId,
                            periodInfo + "Half Total Goals 2");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generatePeriodTotalGoalsRangeProbs(scoreGridPeriod);
            market.put("0", probs[0]);
            market.put("1", probs[1]);
            market.put("2", probs[2]);
            market.put("3+", probs[3]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);

            // p our home/away
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:A:£OUR_1", periodSequenceId,
                            periodInfo + "Half Home Team Total Goals 1");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generatePeriodTotalGoalsRangeProbsTeam(scoreGridPeriod, 1);
            market.put("0", probs[0]);
            market.put("1", probs[1]);
            market.put("2", probs[2]);
            market.put("3+", probs[3]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);


            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:B:£OUR_1", periodSequenceId,
                            periodInfo + "Half Away Team Total Goals 1");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generatePeriodTotalGoalsRangeProbsTeam(scoreGridPeriod, 2);
            market.put("0", probs[0]);
            market.put("1", probs[1]);
            market.put("2", probs[2]);
            market.put("3+", probs[3]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);

            // FT:£OUR_4
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:£OUR_4", "M", "Total Goals 4");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateTotalGoalsRange4Probs(scoreGridPeriod);
            market.put("0", probs[0]);
            market.put("1", probs[1]);
            market.put("2", probs[2]);
            market.put("3", probs[3]);
            market.put("4", probs[4]);
            market.put("5+", probs[5]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);

            // FT:£OUR_3
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:£OUR_3", "M", "Total Goals 3");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateTotalGoalsRange3Probs(scoreGrid);
            market.put("0-1", probs[0]);
            market.put("2-3", probs[1]);
            market.put("4+", probs[2]);
            market.setLineId(sequenceID);
            markets.addMarketWithShortKey(market);

            // FT:AXBOOU

            sequenceID = "3.5";
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:AXBOOU", sequenceID,
                            "Match Result OR Total goals");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateMatchResultOrOVUNProbs(scoreGrid, 3.5);
            marketDescription = "AOver";
            market.put("Home team to win OR " + String.valueOf(3.5) + " more goals to be scored in the match",
                            probs[0]);
            marketDescription = "XOver";
            market.put("Draw OR " + String.valueOf(3.5) + " more goals to be scored in the match", probs[1]);
            marketDescription = "BOver";
            market.put("Away team to win OR " + String.valueOf(3.5) + " more goals to be scored in the match",
                            probs[2]);
            marketDescription = "AUnder";
            market.put("Home team to win OR less than " + String.valueOf(3.5) + " goals to be scored in the match",
                            probs[3]);
            marketDescription = "XUnder";
            market.put("Draw OR less than " + String.valueOf(3.5) + " goals to be scored in the match", probs[4]);
            marketDescription = "BUnder";
            market.put("Away team to win OR less than " + String.valueOf(3.5) + " goals to be scored in the match",
                            probs[5]);
            market.setLineId("3.5");
            markets.addMarketWithShortKey(market);

            // FT:HOF

            sequenceID = "M";
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:HOF", sequenceID,
                            "Half time result OR full time result");
            market.setIsValid(true);
            // Market axbMarket = markets.get("FT:AXB");
            Map<String, Double> axb = axbMarket.getSelectionsProbs();
            Market axbPeriodMarket = markets.get("P:AXB", periodSequenceId);
            Map<String, Double> axbPeriod = axbPeriodMarket.getSelectionsProbs();

            // probs = {0,0};//generateBothTeamScoreOrOVUNProbs(scoreGrid);
            market.put("Home team to win at half time OR full time", axb.get("A") + axbPeriod.get("A"));
            market.put("Draw at half time OR full time", axb.get("X") + axbPeriod.get("X"));
            market.put("Away team to win at half time OR full time", axb.get("B") + axbPeriod.get("B"));
            markets.addMarketWithShortKey(market);

            // FT:HFPOU
            String dLine = String.valueOf(2.5);
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:HFPOU", "M",
                            "half time full time and goals in the first half");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);

            if (((FootballMatchState) matchState).getPeriodNo() < 2) {
                probs = generateHFPOUProbs(scoreGridPeriod, scoreGrid, 2.5);// FIXME
                marketDescription = "H/h and X or more match goals";
                market.put(marketDescription, probs[0]);
                marketDescription = "A/A and X or more match goals";
                market.put(marketDescription, probs[1]);
                marketDescription = "H/D and X or more match goals";
                market.put(marketDescription, probs[2]);
                marketDescription = "H/A and X or more match goals";
                market.put(marketDescription, probs[3]);
                marketDescription = "A/D and X or more match goals";
                market.put(marketDescription, probs[4]);
                marketDescription = "A/H and X or more match goals";
                market.put(marketDescription, probs[5]);
                marketDescription = "D/D and X or more match goals";
                market.put(marketDescription, probs[6]);
                marketDescription = "D/H and X or more match goals";
                market.put(marketDescription, probs[7]);
                marketDescription = "D/A and X or more match goals";
                market.put(marketDescription, probs[8]);
                marketDescription = "H/H and less then X goals";
                market.put(marketDescription, probs[9]);
                marketDescription = "A/A and less then X goals";
                market.put(marketDescription, probs[10]);
                marketDescription = "H/D and less then X goals";
                market.put(marketDescription, probs[11]);
                marketDescription = "H/A and less then X goals";
                market.put(marketDescription, probs[12]);
                marketDescription = "A/D and less then X goals";
                market.put(marketDescription, probs[13]);
                marketDescription = "A/H and less then X goals";
                market.put(marketDescription, probs[14]);
                marketDescription = "D/D and less then X goals";
                market.put(marketDescription, probs[15]);
                marketDescription = "D/H and less then X goals";
                market.put(marketDescription, probs[16]);
                marketDescription = "D/A and less then X goals";
                market.put(marketDescription, probs[17]);
                market.setLineId(dLine);
                market.setValid(true);
                markets.addMarketWithShortKey(market);

                // private TwoWayStatistic firstHalfHomeOrDrawBTTS;
                // //FTBTTS1HDCHD
                // private TwoWayStatistic firstHalfAwayOrDrawBTTS;
                // //FTBTTS1HDCAD
                // private TwoWayStatistic firstHalfHomeOrAwayBTTS;
                // //FTBTTS1HDCHA
                // private TwoWayStatistic firstHalfHomeOrDrawBTNTS;
                // private TwoWayStatistic firstHalfAwayOrDrawBTNTS;
                // private TwoWayStatistic firstHalfHomeOrAwayBTNTS;

                //
                // market = new Market(MarketCategory.GENERAL,
                // MarketGroup.GOALS, "FTBTTS1HDCHD", "M",
                // "First Half - Double Chance & Both Teams to Score");
                // marketDescription = "Home or Away to Win First Half and
                // BTTS";

                // GET FH SCOREGRID
                // GET SH SCOREGRID

                probs = generateFirstHalfComboOdds(scoreGridPeriod);
                // Market axbPeriodMarket = markets.get("P:AXB",
                // periodSequenceId);
                // Map<String, Double> axbPeriod =
                // axbPeriodMarket.getSelectionsProbs();
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:1HCOMBO", "M",
                                "1st Half Combo (First Half Win Special");
                marketDescription = "Home team to win at half time and both team to score in first half";
                market.put(marketDescription, probs[0]);
                marketDescription = "Away team to win at half time and both team to score in first half";
                market.put(marketDescription, probs[1]);
                marketDescription = "Draw at half time and both team to score in first half";
                market.put(marketDescription, probs[2]);
                marketDescription = "Home team to win at half time and both teams not to score in first half";
                market.put(marketDescription, probs[3]);
                marketDescription = "Away team to win at half time and both teams not to score in first half";
                market.put(marketDescription, probs[4]);
                marketDescription = "Draw at half time and both teams not to score in first half";
                market.put(marketDescription, probs[5]);
                marketDescription = "Home team to win at half time and score 2 - 3 goals at half time";
                market.put(marketDescription, probs[6]);
                marketDescription = "Away team to win at half time and score 2 - 3 goals at half time";
                market.put(marketDescription, probs[7]);
                market.setValid(true);
                markets.addMarketWithShortKey(market);

                // robin added markets

                probs = generateHalfOfFirstGoalOdds(scoreGridPeriod, scoreGridSecondHalf, TeamId.A);
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HOFGA", "M",
                                "Half of 1st Home Goal");
                market.setValid(true);
                marketDescription = "No Home Goal";
                market.put(marketDescription, probs[0]);
                marketDescription = "1st Home Goal in Second Half";
                market.put(marketDescription, probs[1]);
                marketDescription = "1st Home Goal in First Half";
                market.put(marketDescription, probs[2]);
                markets.addMarketWithShortKey(market);

                probs = generateHalfOfFirstGoalOdds(scoreGridPeriod, scoreGridSecondHalf, TeamId.B);
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HOFGB", "M",
                                "Half of 1st Away Goal");
                market.setValid(true);
                marketDescription = "No Away Goal";
                market.put(marketDescription, probs[0]);
                marketDescription = "1st Away Goal in Second Half";
                market.put(marketDescription, probs[1]);
                marketDescription = "1st Away Goal in First Half";
                market.put(marketDescription, probs[2]);
                markets.addMarketWithShortKey(market);

            }

            probs = generateSecondHalfComboOdds(scoreGridSecondHalf, scoreGrid);
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:2HCOMBO", "M",
                            "2nd Half Combo (Second Half Win Special");
            market.setValid(true);
            marketDescription = "Home team will win and both team to score in second half";
            market.put(marketDescription, probs[0]);
            marketDescription = "Match will end in a draw and both team to score in second half";
            market.put(marketDescription, probs[1]);
            marketDescription = "Away team will win and both team to score in second half";
            market.put(marketDescription, probs[2]);
            marketDescription = "Home team will win but not both team will score in second half";
            market.put(marketDescription, probs[3]);
            marketDescription = "Match will end in a draw and no team will score in second half";
            market.put(marketDescription, probs[4]);
            marketDescription = "Away team will win but not both team will score in second half";
            market.put(marketDescription, probs[5]);
            marketDescription = "Home team will win and 2 or more goals will be scored in second half";
            market.put(marketDescription, probs[6]);
            marketDescription = "Away team will win and 2 or more goals will be scored in second half";
            market.put(marketDescription, probs[7]);
            markets.addMarketWithShortKey(market);

            // ft:axbttsou:
            double[] probs2 = generateTeamToWinAndOneTeamToScoreOUProbs(scoreGrid, 2.5);
            String dLine2 = String.valueOf(2.5);
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:AXBTTSOU", "M",
                            "To win, only 1 team to score and total goals (O/U)");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            marketDescription = "Home to win, Not Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[0]);
            marketDescription = "Away to win, Not Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[1]);
            marketDescription = "Home to win, Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[2]);
            marketDescription = "Away to win, Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[3]);
            marketDescription = "Draw, Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[4]);
            marketDescription = "Draw, Not Both teams will score and there will be X or more goals";
            market.put(marketDescription, probs2[5]);
            marketDescription = "Home to win, Not Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[6]);
            marketDescription = "Away to win, Not Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[7]);
            marketDescription = "Home to win, Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[8]);
            marketDescription = "Away to win, Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[9]);
            marketDescription = "Draw, Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[10]);
            marketDescription = "Draw, Not Both teams will score and there will be less than X goals";
            market.put(marketDescription, probs2[11]);
            market.setLineId(dLine2);
            markets.addMarketWithShortKey(market);

            dLine = String.valueOf(2.5);
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:HFOU", "M",
                            "half time full time and total goals");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            double[] halfTimeResult = {0, 0, 0};

            if (((FootballMatchState) matchState).getPeriodNo() < 2) {
                sourceMarket = markets.get("P:AXB", periodSequenceId);
                halfTimeResult[0] = sourceMarket.get("A");
                halfTimeResult[1] = sourceMarket.get("X");
                halfTimeResult[2] = sourceMarket.get("B");

                probs = generateHFOUProbs(scoreGrid, 2.5, halfTimeResult);
                marketDescription = "H/D and X or more match goals";
                market.put(marketDescription, probs[0]);
                marketDescription = "H/A and X or more match goals";
                market.put(marketDescription, probs[1]);
                marketDescription = "A/D and X or more match goals";
                market.put(marketDescription, probs[2]);
                marketDescription = "A/H and X or more match goals";
                market.put(marketDescription, probs[3]);
                marketDescription = "D/D and X or more match goals";
                market.put(marketDescription, probs[4]);
                marketDescription = "D/H and X or more match goals";
                market.put(marketDescription, probs[5]);
                marketDescription = "D/A and X or more match goals";
                market.put(marketDescription, probs[6]);
                marketDescription = "H/H and less then X goals";
                market.put(marketDescription, probs[7]);
                marketDescription = "A/A and less then X goals";
                market.put(marketDescription, probs[8]);
                marketDescription = "H/D and less then X goals";
                market.put(marketDescription, probs[9]);
                marketDescription = "H/A and less then X goals";
                market.put(marketDescription, probs[10]);
                marketDescription = "A/D and less then X goals";
                market.put(marketDescription, probs[11]);
                marketDescription = "A/H and less then X goals";
                market.put(marketDescription, probs[12]);
                marketDescription = "D/D and less then X goals";
                market.put(marketDescription, probs[13]);
                marketDescription = "D/H and less then X goals";
                market.put(marketDescription, probs[14]);
                marketDescription = "D/A and less then X goals";
                market.put(marketDescription, probs[15]);
                market.setLineId(dLine);
                markets.addMarketWithShortKey(market);
            }
            // FT:BTSOOU
            dLine = String.valueOf(2.5);
            sequenceID = periodSequenceId;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BTSAOU", dLine,
                            "Both teams to score and Total goals");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateBothTeamScoreAndTotalProbs(scoreGrid, 2.5);
            marketDescription = "Both teams to score and X or more goals to be scored in the match";
            market.put(marketDescription, probs[0]);
            marketDescription = "Not both teams to score and X or more goals to be scored in the match";
            market.put(marketDescription, probs[1]);
            marketDescription = "Both teams to score and X or less goals to be scored in the match";
            market.put(marketDescription, probs[2]);
            marketDescription = "Not both teams to score and less than X goals to be scored in the match";
            market.put(marketDescription, probs[3]);
            market.setLineId(dLine);
            markets.addMarketWithShortKey(market);


            // FT:BTSOOU
            dLine = String.valueOf(2.5);
            sequenceID = periodSequenceId;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BTSOOU", dLine,
                            "Both teams to score OR Total goals");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateBothTeamScoreOrTotalProbs(scoreGrid, 2.5);
            marketDescription = "GGorOV";
            market.put(marketDescription, probs[0]);
            marketDescription = "NGorUN";
            market.put(marketDescription, probs[1]);
            marketDescription = "GGorUN";
            market.put(marketDescription, probs[2]);
            marketDescription = "NGorOV";
            market.put(marketDescription, probs[3]);
            market.setLineId(dLine);
            markets.addMarketWithShortKey(market);

            // FYI - I have absolutely no respect for this - but the client
            // wants it
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CORNEROVER2", "M",
                            "Will there be more than 2 corners?");
            market.setIsValid(true);
            market.put("Yes", 0.975);
            market.put("No", 0.075);// trueProbability is 2.5% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CORNEROVER15", "M",
                            "Will there be more than 15 corners?");
            market.setIsValid(true);
            market.put("Yes", 0.075); // trueProbability is 2.5% on average
            market.put("No", 0.975);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:ACORNER", "M",
                            "Will Home Team have a corner?");
            market.setIsValid(true);
            market.put("Yes", 0.98);
            market.put("No", 0.05); // trueProbability is 3.2% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BCORNER", "M",
                            "Will Away Team have a corner?");
            market.setIsValid(true);
            market.put("Yes", 0.98);
            market.put("No", 0.05); // trueProbability is 3.2% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:COE", "M", "Total Corners Odd/Even");
            market.setIsValid(true);
            market.put("Odd", 0.50);
            market.put("Even", 0.50); // trueProbability is 50% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:A:COE", "M",
                            "Total Home Corners Odd/Even");
            market.setIsValid(true);
            market.put("Odd", 0.50);
            market.put("Even", 0.50); // trueProbability is 50% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:B:COE", "M",
                            "Total Away Corners Odd/Even");
            market.setIsValid(true);
            market.put("Odd", 0.50);
            market.put("Even", 0.50); // trueProbability is 50% on average
            markets.addMarketWithShortKey(market);

            // Cards Markets
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CARDSYELLOWOVER1", "M",
                            "Will there be a Yellow Card shown?");
            market.setIsValid(true);
            market.put("Yes", 0.975);
            market.put("No", 0.06); // trueProbability is 3.823% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CARDSYELLOWOVER8", "M",
                            "Will there be 8 or more Yellow Cards?");
            market.setIsValid(true);
            market.put("Yes", 0.075); // trueProbability is 3.20% on average
            market.put("No", 0.975);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CARDSYESNO", "M",
                            "Will there be a Card shown?");
            market.setIsValid(true);
            market.put("Yes", 0.975);
            market.put("No", 0.060); // trueProbability is 3.7029% on average
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CARDSODDEVEN", "M",
                            "Total Cards Odd/Even");
            market.setIsValid(true);
            market.put("Even", 0.51); // trueProbability is 51% on average
            market.put("Odd", 0.49);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:YCARDSODDEVEN", "M",
                            "Total Yellow Cards Odd/Even");
            market.setIsValid(true);
            market.put("Even", 0.51); // trueProbability is 51% on average
            market.put("Odd", 0.49);
            markets.addMarketWithShortKey(market);

            // end of static corner/cards markets

            // P:DBLCOU
            dLine = String.valueOf(1.5);
            sequenceID = periodSequenceId;
            market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "P:DBLCOU", sequenceID,
                            "Double Chance and 2 or more goals");
            market.setLineProbs(sourceMarket.getLineProbs());
            market.setIsValid(true);
            probs = generateDoubleChanceAndOuProbs(scoreGridPeriod, 1.5);
            marketDescription = "AXOver";
            market.put(marketDescription, probs[0]);
            marketDescription = "XBOver";
            market.put(marketDescription, probs[1]);
            marketDescription = "ABOver";
            market.put(marketDescription, probs[2]);
            marketDescription = "AXUnder";
            market.put(marketDescription, probs[3]);
            marketDescription = "XBUnder";
            market.put(marketDescription, probs[4]);
            marketDescription = "ABUnder";
            market.put(marketDescription, probs[5]);
            market.setLineId(dLine);
            markets.addMarketWithShortKey(market);
            // ft:abou 1.5 2.5 3.5

            sourceMarket = markets.get("FT:OU");
            balancedLineId = sourceMarket.getLineId();
            dBalanceLineId = Double.parseDouble(balancedLineId);
            line = balancedLineId;
            bLine = dBalanceLineId;

            double[] probs4 = {0, 0, 0, 0};
            for (double startLine = 1.5; startLine <= 4.5; startLine++) {

                dLine = String.valueOf(startLine);
                sequenceID = "F" + startLine;
                market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:ABOU", sequenceID,
                                "Match Winner and Total Goals");
                market.setLineProbs(sourceMarket.getLineProbs());
                market.setIsValid(true);

                probs4 = generateMatchWinnerAndOVUNProbs(scoreGrid, startLine);

                marketDescription = "BOver";
                market.put(marketDescription, probs4[0]);
                marketDescription = "BUnder";
                market.put(marketDescription, probs4[1]);
                marketDescription = "AOver";
                market.put(marketDescription, probs4[2]);
                marketDescription = "AUnder";
                market.put(marketDescription, probs4[3]);
                market.setLineId(dLine);
                markets.addMarketWithShortKey(market);
            }


            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:MBTSC", "M",
                            "Match Result and both team to score");
            market.setIsValid(true);
            probs = generateMatchResultAndBTSProbs(scoreGrid);
            marketDescription = "BYes";
            market.put(marketDescription, probs[0]);
            marketDescription = "XYes";
            market.put(marketDescription, probs[1]);
            marketDescription = "AYes";
            market.put(marketDescription, probs[2]);
            marketDescription = "BNo";
            market.put(marketDescription, probs[3]);
            marketDescription = "XNo";
            market.put(marketDescription, probs[4]);
            marketDescription = "ANo";
            market.put(marketDescription, probs[5]);
            markets.addMarketWithShortKey(market);

            // for (double startLine = 2.5; startLine <= 3.5; startLine++) {
            // sequenceID = "F" + startLine;
            // market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS,
            // "FT:BTSOU", "M",
            // "Both team to score and Over/Under");
            // market.setIsValid(true);
            // probs = generateBTSAndOVUNProbs(scoreGrid, startLine);
            // marketDescription = "YOver";
            // market.put(marketDescription, probs[0]);
            // marketDescription = "NOver" ;
            // market.put(marketDescription, probs[1]);
            // marketDescription = "YUnder" ;
            // market.put(marketDescription, probs[2]);
            // marketDescription = "NUnder" ;
            // market.put(marketDescription, probs[3]);
            // market.setLineId(line);
            // markets.addMarketWithShortKey(market);
            // }
            /*
             * both team to score x goals and above will be scored in the match yes or no
             */
            // for (double startLine = 2.5; startLine <= 3.5; startLine++) {

            // P:A2PLUS, P:B2PLUS
            if (((FootballMatchState) matchState).getPeriodNo() < 2) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:A2PLUS", "M",
                                "Home team to win and 2 or more goals scored at half time");
                market.setIsValid(true);
                market.setLineId(String.valueOf(2.5));
                marketDescription = "Yes";
                probs = generatOVUNProbs(scoreGridPeriod, 2.5);
                probs[0] *= axbMarket.get("A");
                probs[1] = 1 - probs[0];
                market.put(marketDescription, probs[0]);
                marketDescription = "No";
                market.put(marketDescription, 1 - probs[0]);
                markets.addMarketWithShortKey(market);

                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:B2PLUS", "M",
                                "Away team to win and 2 or more goals scored at half time");
                market.setIsValid(true);
                market.setLineId(String.valueOf(2.5));
                marketDescription = "Yes";
                probs = generatOVUNProbs(scoreGridPeriod, 2.5);
                probs[0] *= axbMarket.get("B");
                probs[1] = 1 - probs[0];
                market.put(marketDescription, probs[0]);
                marketDescription = "No";
                market.put(marketDescription, 1 - probs[0]);
                markets.addMarketWithShortKey(market);
            } else if (((FootballMatchState) matchState).getPeriodNo() == 2) {

                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:A2PLUS2", "M",
                                "Home team to win and 2 or more goals scored at second half");
                market.setIsValid(true);
                market.setLineId(String.valueOf(2.5));
                marketDescription = "Yes";
                probs = generatOVUNProbs(scoreGridPeriod, 2.5);
                probs[0] *= axbMarket.get("A");
                probs[1] = 1 - probs[0];
                market.put(marketDescription, probs[0]);
                marketDescription = "No";
                market.put(marketDescription, 1 - probs[0]);
                markets.addMarketWithShortKey(market);

                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:B2PLUS2", "M",
                                "Away team to win and 2 or more goals scored at second half");
                market.setIsValid(true);
                market.setLineId(String.valueOf(2.5));
                marketDescription = "Yes";
                probs = generatOVUNProbs(scoreGridPeriod, 2.5);
                probs[0] *= axbMarket.get("B");
                probs[1] = 1 - probs[0];
                market.put(marketDescription, probs[0]);
                marketDescription = "No";
                market.put(marketDescription, 1 - probs[0]);
                markets.addMarketWithShortKey(market);
            }
            // FT:BTSOU:3
            // Market ouMarket = markets.get("FT:OU").getMarketForLineId("2.5");
            // double over25 = ouMarket.get("Over");

            // Market btsMarket = markets.get("FT:BTS");
            // double bts = btsMarket.get("Yes");
            sequenceID = "F" + 2.5;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BTS$OU", "M",
                            "Both team to score and 3 goals and above will be scored in the match");
            market.setIsValid(true);
            market.setLineId(String.valueOf(2.5));
            probs = generateBTSAndOVUNProbs(scoreGrid, 2.5);
            // marketDescription = "Yes";
            // market.put(marketDescription, over25 * bts);
            // marketDescription = "No";
            // market.put(marketDescription, 1 - over25 * bts);

            marketDescription = "Yes";
            market.put(marketDescription, probs[0]);
            marketDescription = "No";
            market.put(marketDescription, 1 - probs[0]);
            markets.addMarketWithShortKey(market);

            // Market ouMarket35 =
            // markets.get("FT:OU").getMarketForLineId("3.5");
            // double over35 = ouMarket35.get("Over");
            sequenceID = "F" + 3.5;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BTS$OU", "M",
                            "Both team to score and 4 goals and above will be scored in the match");
            market.setIsValid(true);
            market.setLineId(String.valueOf(3.5));
            probs = generateBTSAndOVUNProbs(scoreGrid, 3.5);
            marketDescription = "Yes";
            market.put(marketDescription, probs[0]);
            marketDescription = "No";
            market.put(marketDescription, 1 - probs[0]);
            markets.addMarketWithShortKey(market);
            // }
            // ONE TEAM TO SCORE MORE THAN THREE
            sequenceID = "F" + 2.5;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:OTSOV:3", "M",
                            "One team to score and 3 goals and above will be scored in the match");
            market.setIsValid(true);
            market.setLineId(String.valueOf(2.5));
            probs = generateOTSAndOVUNProbs(scoreGrid, 2.5, true);
            marketDescription = "Yes";
            market.put(marketDescription, probs[0]);
            marketDescription = "No";
            market.put(marketDescription, probs[1]);
            markets.addMarketWithShortKey(market);

            // ONE TEAM TO SCORE UNDER 3
            sequenceID = "F" + 2.5;
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:OTSUD:3", "M",
                            "One team to score and not more than 2 goals Or less will be scored in the match");
            market.setIsValid(true);
            market.setLineId(String.valueOf(2.5));
            probs = generateOTSAndOVUNProbs(scoreGrid, 2.5, false);
            marketDescription = "YES";
            market.put(marketDescription, probs[0]);
            marketDescription = "NO";
            market.put(marketDescription, probs[1]);
            markets.addMarketWithShortKey(market);

            if (((FootballMatchState) matchState).preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.BOOKINGS, "FT:BRED", "M", "Red Card");
                market.setIsValid(true);
                probs[0] = ((FootballMatchParams) matchParams).getRedCardProb().getMean();
                marketDescription = "Yes";
                market.setSelectionNameOverOrA(marketDescription);
                market.put(marketDescription, probs[0]);
                probs[1] = 1 - probs[0];
                marketDescription = "No";
                market.setSelectionNameUnderOrB(marketDescription);
                market.put(marketDescription, probs[1]);
                markets.addMarketWithShortKey(market);
            }
            /**
             * new working section
             **/
            sourceMarket = markets.get("P:AXB", periodSequenceId);
            matchResult = sourceMarket.getSelectionsProbs();
            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription = "First half Double Chance";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription = "Second half  Double Chance";
                    break;
                default:
                    marketDescription = "";
            }
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DBLC", periodSequenceId,
                            marketDescription);
            market.setIsValid(true);
            marketDescription = "AX";
            probs[0] = matchResult.get("A") + matchResult.get("X");
            market.put(marketDescription, probs[0]);
            marketDescription = "AB";
            probs[1] = matchResult.get("A") + matchResult.get("B");
            market.put(marketDescription, probs[1]);
            marketDescription = "BX";
            probs[2] = matchResult.get("B") + matchResult.get("X");
            market.put(marketDescription, probs[2]);
            markets.addMarketWithShortKey(market);

            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription = "First half Draw no bet";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription = "Second half Draw no bet";
                    break;
                default:
                    marketDescription = "";
            }
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DNB", periodSequenceId,
                            marketDescription);
            market.setIsValid(true);
            probs[0] = matchResult.get("A") / (matchResult.get("A") + matchResult.get("B"));
            market.put("A", probs[0]);
            probs[1] = matchResult.get("B") / (matchResult.get("A") + matchResult.get("B"));
            market.put("B", probs[1]);
            markets.addMarketWithShortKey(market);
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DBLC$BTS", "M",
                            "Double chance and both team to score");
            market.setIsValid(true);
            probs = generateDoubleChanceAndBTSProbs(scoreGrid);
            marketDescription = "XBYes";
            market.put(marketDescription, probs[0]);
            marketDescription = "AXYes";
            market.put(marketDescription, probs[1]);
            marketDescription = "ABYes";
            market.put(marketDescription, probs[2]);
            marketDescription = "XBNo";
            market.put(marketDescription, probs[3]);
            marketDescription = "AXNo";
            market.put(marketDescription, probs[4]);
            marketDescription = "ABNo";
            market.put(marketDescription, probs[5]);
            markets.addMarketWithShortKey(market);

            // double probA = axbMarket.get("A");
            // double probB = axbMarket.get("B");
            // double probD = axbMarket.get("X");

            for (double startLine = 1.5; startLine <= 4.5; startLine++) {

                dLine = String.valueOf(startLine);
                sequenceID = "D" + startLine;
                market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:DBLC$OU", sequenceID,
                                "Double Chance and Over/Under");
                market.setLineProbs(sourceMarket.getLineProbs());
                market.setIsValid(true);
                probs = generateDoubleChanceAndOuProbs(scoreGrid, startLine);

                // ouMarket =
                // markets.get("FT:OU").getMarketForLineId(Double.toString(startLine));
                // double over = ouMarket.get("Over");
                // double under = ouMarket.get("Under");

                marketDescription = "AXOver";
                market.put(marketDescription, probs[0]);
                marketDescription = "XBOver";
                market.put(marketDescription, probs[1]);
                marketDescription = "ABOver";
                market.put(marketDescription, probs[2]);
                marketDescription = "AXUnder";
                market.put(marketDescription, probs[3]);
                marketDescription = "XBUnder";
                market.put(marketDescription, probs[4]);
                marketDescription = "ABUnder";
                market.put(marketDescription, probs[5]);
                market.setLineId(dLine);
                markets.addMarketWithShortKey(market);
            }

            /// only 2 - 3 goals..
            double[] probsDBLCRNG = {0, 0, 0};
            double[] probsDBLCRNG1 = {0, 0, 0};
            double endLine = 3.5;
            for (double startLine = 1.5; startLine <= 3.5; startLine++) {

                dLine = String.valueOf(startLine);
                sequenceID = "D" + startLine;
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DBLCRNG", "M",
                                "Double Chance and 2-3 Goals Scored in the Match");
                market.setLineProbs(sourceMarket.getLineProbs());
                market.setIsValid(true);
                probsDBLCRNG1 = generateDoubleChanceAndWithInProbs(scoreGrid, startLine, endLine);
                probsDBLCRNG[0] += probsDBLCRNG1[0];
                probsDBLCRNG[1] += probsDBLCRNG1[1];
                probsDBLCRNG[2] += probsDBLCRNG1[2];
            }
            marketDescription = "AX2-3";
            market.put(marketDescription, probsDBLCRNG[0]);
            marketDescription = "BX2-3";
            market.put(marketDescription, probsDBLCRNG[1]);
            marketDescription = "AB2-3";
            market.put(marketDescription, probsDBLCRNG[2]);
            // market.setLineId(dLine);
            markets.addMarketWithShortKey(market);

            if (periodNo < 2) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DBLCBTS", "H1",
                                "First Half Double Chance and Both Team To Score");
                market.setIsValid(true);
                probs = generateDoubleChanceAndBTSProbs(scoreGridPeriod);
                marketDescription = "XBYes";
                market.put(marketDescription, probs[0]);
                marketDescription = "AXYes";
                market.put(marketDescription, probs[1]);
                marketDescription = "ABYes";
                market.put(marketDescription, probs[2]);
                marketDescription = "XBNo";
                market.put(marketDescription, probs[3]);
                marketDescription = "AXNo";
                market.put(marketDescription, probs[4]);
                marketDescription = "ABNo";
                market.put(marketDescription, probs[5]);
                markets.addMarketWithShortKey(market);
            }

            if (periodNo < 3) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DBLCBTS", "H2",
                                "Second Half Double Chance and Both Team To Score");
                market.setIsValid(true);
                probs = generateDoubleChanceAndBTSProbs(scoreGridSecondHalf);
                marketDescription = "XBYes";
                market.put(marketDescription, probs[0]);
                marketDescription = "AXYes";
                market.put(marketDescription, probs[1]);
                marketDescription = "ABYes";
                market.put(marketDescription, probs[2]);
                marketDescription = "XBNo";
                market.put(marketDescription, probs[3]);
                marketDescription = "AXNo";
                market.put(marketDescription, probs[4]);
                marketDescription = "ABNo";
                market.put(marketDescription, probs[5]);
                markets.addMarketWithShortKey(market);
            }
            /**
             * Double chance halftime/fulltime
             */
            if (matchState.preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DBLCHTFT", "M",
                                "Double Chance Half Time Full Time");
                probs = generateDCHTFTProbs(scoreGridPeriod, scoreGridSecondHalf);
                market.setIsValid(true);
                marketDescription = "1X/1X";
                market.put(marketDescription, probs[0]);
                marketDescription = "1X/12";
                market.put(marketDescription, probs[1]);
                marketDescription = "1X/X2";
                market.put(marketDescription, probs[2]);
                marketDescription = "12/1X";
                market.put(marketDescription, probs[3]);
                marketDescription = "12/12";
                market.put(marketDescription, probs[4]);
                marketDescription = "12/X2";
                market.put(marketDescription, probs[5]);
                marketDescription = "X2/1X";
                market.put(marketDescription, probs[6]);
                marketDescription = "X2/12";
                market.put(marketDescription, probs[7]);
                marketDescription = "X2/X2";
                market.put(marketDescription, probs[8]);
                markets.addMarketWithShortKey(market);
            }


            /**
             * Home team to win (FT) or Both team to score/ or Over, line = 2.5, NG in selections means not both team
             * scores
             */
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:TWOBTSOU", "M",
                            "Team To Win Or Both Team To Score Or Over Under 2.5");
            probs = generateTWOBTSOUProbs(scoreGrid, 2.5);
            market.setIsValid(true);
            marketDescription = "1/GG/Ov";
            market.put(marketDescription, probs[0]);
            marketDescription = "2/GG/Ov";
            market.put(marketDescription, probs[1]);
            marketDescription = "X/GG/Ov";
            market.put(marketDescription, probs[2]);
            marketDescription = "1/GG/Un";
            market.put(marketDescription, probs[3]);
            marketDescription = "2/GG/Un";
            market.put(marketDescription, probs[4]);
            marketDescription = "X/GG/Un";
            market.put(marketDescription, probs[5]);
            marketDescription = "1/NG/Ov";
            market.put(marketDescription, probs[6]);
            marketDescription = "2/NG/Ov";
            market.put(marketDescription, probs[7]);
            marketDescription = "X/NG/Ov";
            market.put(marketDescription, probs[8]);
            marketDescription = "1/NG/Un";
            market.put(marketDescription, probs[9]);
            marketDescription = "2/NG/Un";
            market.put(marketDescription, probs[10]);
            marketDescription = "X/NG/Un";
            market.put(marketDescription, probs[11]);
            markets.addMarketWithShortKey(market);



            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription = "First half Home no bet";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription = "Second half Home no bet";
                    break;
                default:
                    marketDescription = "";
            }

            if (periodNo <= 2) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:HNB", periodSequenceId,
                                marketDescription);
                market.setIsValid(true);
                probs[0] = matchResult.get("X") / (matchResult.get("X") + matchResult.get("B"));
                market.put("X", probs[0]);
                probs[1] = matchResult.get("B") / (matchResult.get("X") + matchResult.get("B"));
                market.put("B", probs[1]);
                markets.addMarketWithShortKey(market);
            }
            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription = "First half Away no bet";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription = "Second half Away no bet";
                    break;
                default:
                    marketDescription = "";
            }
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:ANB", periodSequenceId,
                            marketDescription);
            market.setIsValid(true);
            probs[0] = matchResult.get("A") / (matchResult.get("X") + matchResult.get("A"));
            market.put("A", probs[0]);
            probs[1] = matchResult.get("X") / (matchResult.get("X") + matchResult.get("A"));
            market.put("X", probs[1]);
            markets.addMarketWithShortKey(market);

            String marketDescription1;
            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription1 = "First Half Match Result and Over/Under";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription1 = "Second Half Match Result and Over/Under";
                    break;
                default:
                    marketDescription1 = "";
            }

            sourceMarket = markets.get("P:OU", periodSequenceId);
            balancedLineId = sourceMarket.getLineId();
            dBalanceLineId = Double.parseDouble(balancedLineId);
            line = balancedLineId;
            bLine = dBalanceLineId;

            for (double startLine = 1.5; startLine <= 1.5; startLine++) {

                dLine = String.valueOf(startLine);
                sequenceID = periodSequenceId + "." + (int) startLine;
                market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "P:AXB$OU", sequenceID, marketDescription1);
                market.setLineProbs(sourceMarket.getLineProbs());
                market.setIsValid(true);
                probs = generateMatchResultAndOVUNProbs(scoreGridPeriod, startLine);
                marketDescription = "BOver";
                market.put(marketDescription, probs[0]);
                marketDescription = "XOver";
                market.put(marketDescription, probs[1]);
                marketDescription = "AOver";
                market.put(marketDescription, probs[2]);
                marketDescription = "BUnder";
                market.put(marketDescription, probs[3]);
                marketDescription = "XUnder";
                market.put(marketDescription, probs[4]);
                marketDescription = "AUnder";
                market.put(marketDescription, probs[5]);
                market.setLineId(dLine);
                markets.addMarketWithShortKey(market);
            }

            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription1 = "First Half Match Result and Both Team To Score";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription1 = "Second Half Match Result and Both Team To Score";
                    break;
                default:
                    marketDescription1 = "";
            }
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:AXB$BTS", periodSequenceId,
                            marketDescription1);
            market.setIsValid(true);
            probs = generateMatchResultAndBTSProbs(scoreGridPeriod);
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

            switch (periodNo) {
                case 0: // pre-match
                case 1: // first half
                    marketDescription1 = "First half Both team to score and Over/Under";
                    break;
                case 2: // half time
                case 3: // second half
                    marketDescription1 = "Second half Both team to score and Over/Under";
                    break;
                default:
                    marketDescription1 = "";
            }
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:BTSOU", periodSequenceId,
                            marketDescription1);
            market.setIsValid(true);
            bLine = 2.5;
            probs = generateBTSAndOVUNProbs(scoreGridPeriod, bLine);
            marketDescription = "YOver";
            market.put(marketDescription, probs[0]);
            marketDescription = "NOver";
            market.put(marketDescription, probs[1]);
            marketDescription = "YUnder";
            market.put(marketDescription, probs[2]);
            marketDescription = "NUnder";
            market.put(marketDescription, probs[3]);
            market.setLineId(line);
            markets.addMarketWithShortKey(market);

            // boolean stillCreatABBH = true;
            // if (((FootballMatchState) matchState).getPeriodNo() > 1)
            // if (((FootballMatchState) matchState).getFirstHalfGoalsA() ==
            // ((FootballMatchState) matchState)
            // .getFirstHalfGoalsB())
            // stillCreatABBH = false;
            // if (stillCreatABBH) {
            // market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS,
            // "FT:AB:BH", "M",
            // "Either team win both halfs");
            // market.setIsValid(true);
            //
            //
            // Market sourceMarketPAXB = markets.get("P:AXB", periodSequenceId);
            // Market sourceMarketP2AxB = markets.get("P:AXB", "H2");
            // Map<String, Double> matchResultAXB =
            // sourceMarketP2AxB.getSelectionsProbs();
            // Map<String, Double> matchResultPAXB =
            // sourceMarketPAXB.getSelectionsProbs();
            // double p1 = matchResultAXB.get("A") * matchResultPAXB.get("A");
            // double p2 = matchResultAXB.get("B") * matchResultPAXB.get("B");
            //
            // marketDescription = "Yes";
            // market.put(marketDescription, p1 + p2);
            // marketDescription = "No";
            // market.put(marketDescription, 1 - (p1 + p2));
            // markets.addMarketWithShortKey(market);
            // }

            /******/
            if (((FootballMatchState) matchState).preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DBCS", String.format("H%d", 1),
                                "1st Half DC Combo FIRST HALF DOUBLE CHANCE SPECIAL(COMBO)");
                market.setIsValid(true);
                probs = generateHalfDCComboDoubleChanceSpecialProbs(scoreGridPeriod, scoreGrid);
                marketDescription = "Home team win or draw and both team to score in first half";
                market.put(marketDescription, probs[0]);
                marketDescription = "Home or Away team will win and both team to score in first half";
                market.put(marketDescription, probs[1]);
                marketDescription = "Away team win or draw and both team to score in first half";
                market.put(marketDescription, probs[2]);
                marketDescription = "Home team win or draw and not both team will score in first half";
                market.put(marketDescription, probs[3]);
                marketDescription = "Home or Away team to win and not both team will score in first half";
                market.put(marketDescription, probs[4]);
                marketDescription = "Away win or draw and not both team will score in first half";
                market.put(marketDescription, probs[5]);
                marketDescription = "Home win or draw and 2 or more goals will be scored in first half";
                market.put(marketDescription, probs[6]);
                marketDescription = "Home or Away team win and 2 or more goals will be scored in first half";
                market.put(marketDescription, probs[7]);
                marketDescription = "Away team win or draw and 2 or more goals will be scored in first half";
                market.put(marketDescription, probs[8]);
                markets.addMarketWithShortKey(market);
            }

            if (((FootballMatchState) matchState).preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "P:DBCS", String.format("H%d", 2),
                                "2nd Half DC Combo FIRST HALF DOUBLE CHANCE SPECIAL(COMBO)");
                market.setIsValid(true);
                probs = generateHalfDCComboDoubleChanceSpecialProbs(scoreGridSecondHalf, scoreGrid);
                marketDescription = "Home team win or draw and both team to score in second half";
                market.put(marketDescription, probs[0]);
                marketDescription = "Home or Away team will win and both team to score in second half";
                market.put(marketDescription, probs[1]);
                marketDescription = "Away team win or draw and both team to score in second half";
                market.put(marketDescription, probs[2]);
                marketDescription = "Home team win or draw and not both team will score in second half";
                market.put(marketDescription, probs[3]);
                marketDescription = "Home or Away team to win and not both team will score in second half";
                market.put(marketDescription, probs[4]);
                marketDescription = "Away win or draw and not both team will score in second half";
                market.put(marketDescription, probs[5]);
                marketDescription = "Home win or draw and 2 or more goals will be scored in second half";
                market.put(marketDescription, probs[6]);
                marketDescription = "Home or Away team win and 2 or more goals will be scored in second half";
                market.put(marketDescription, probs[7]);
                marketDescription = "Away team win or draw and 2 or more goals will be scored in second half";
                market.put(marketDescription, probs[8]);
                markets.addMarketWithShortKey(market);
            }
            // 3 half time combo
            if (((FootballMatchState) matchState).preMatch()) {
                probs = generateHalfDCComboSpecialProbs(scoreGridPeriod, scoreGrid);
                marketDescription =
                                "Home team to win at half time and full time and there will be 2 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HHTHFT2", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[0]);
                market.put("No", 1 - probs[0]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Away team to win at half time and full time and there will be 2 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:AHTAFT2", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[1]);
                market.put("No", 1 - probs[1]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Home team to win at half time and full time and there will be 3 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HHTHFT3", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[2]);
                market.put("No", 1 - probs[2]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Away team to win at half time and full time and there will be 3 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:AHTAFT3", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[3]);
                market.put("No", 1 - probs[3]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Home team to win at half time and full time and there will be 4 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HHTHFT4", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[4]);
                market.put("No", 1 - probs[4]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Away team to win at half time and full time and there will be 4 or more goals in the match";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:AHTAFT4", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[5]);
                market.put("No", 1 - probs[5]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Home team to win at half time and full time and 2 or more goals will be scored at half time";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:HHTHFT2H", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[6]);
                market.put("No", 1 - probs[6]);
                markets.addMarketWithShortKey(market);
                marketDescription =
                                "Away team to win at half time and full time and 2 or more goals will be scored at half time";
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:AHTAFT2H", "M", marketDescription);
                market.setIsValid(true);
                market.put("Yes", probs[7]);
                market.put("No", 1 - probs[7]);
                markets.addMarketWithShortKey(market);
                markets.addMarketWithShortKey(market);
            }

            if (((FootballMatchState) matchState).preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CM", "M", "CHANCE MIX");
                market.setIsValid(true);
                probs = generateChangeMixProbs(scoreGrid);
                marketDescription = "Home team to win OR 3 or more goals will be scored in the match";
                market.put(marketDescription, probs[0]);
                marketDescription = "Match will end in a draw OR 3 or more goals will be scored in the match";
                market.put(marketDescription, probs[1]);
                marketDescription = "Away team to win OR 3 or more goals will be scored in the match";
                market.put(marketDescription, probs[2]);
                marketDescription = "Home team to win OR not more than 2 goals will be scored in the match";
                market.put(marketDescription, probs[3]);
                marketDescription = "Match will end in a draw OR not more than 2 goals will be scored in the match";
                market.put(marketDescription, probs[4]);
                marketDescription = "Away team to win OR not more than 2 goals will be scored in the match";
                market.put(marketDescription, probs[5]);
                marketDescription = "Home team will win OR both team will score";
                market.put(marketDescription, probs[6]);
                marketDescription = "Match will end in a draw OR both team will score";
                market.put(marketDescription, probs[7]);
                marketDescription = "Away team will win OR both team will score";
                market.put(marketDescription, probs[8]);
                marketDescription = "Home team will win OR not both team will score in the match";
                market.put(marketDescription, probs[9]);
                marketDescription = "Match will end in a draw OR not both team will score in the match";
                market.put(marketDescription, probs[10]);
                marketDescription = "Away team will win OR not both team will score in the match";
                market.put(marketDescription, probs[11]);
                marketDescription = "Home team will win OR not more than 3 goals will be scored in the match";
                market.put(marketDescription, probs[12]);
                marketDescription = "Away team will win OR not more than 3 goals will be scored in the match";
                market.put(marketDescription, probs[13]);
                marketDescription = "Match will end in a draw OR not more than 3 goals will be scored in the match";
                market.put(marketDescription, probs[14]);
                marketDescription = "Match will end in a draw OR 2 or more goals will be scored in the match";
                market.put(marketDescription, probs[15]);
                marketDescription = "Match will end in a draw OR not more than 1 goal will be scored in the match";
                market.put(marketDescription, probs[16]);
                marketDescription = "Home team will win OR not more than 1 goal will be scored in the match";
                market.put(marketDescription, probs[17]);
                marketDescription = "Away team will win OR not more than 1 goal will be scored in the match";
                market.put(marketDescription, probs[18]);
                marketDescription = "Both team will score OR not more than 1 goal will be scored in the match";
                market.put(marketDescription, probs[19]);
                marketDescription = "Home team will win OR 2 or more goals will be scored in the match";
                market.put(marketDescription, probs[20]);
                marketDescription = "Away team will win OR 2 or more goals will be scored in the match";
                market.put(marketDescription, probs[21]);
                marketDescription = "Match will end in a draw OR 2 or more goals will be scored in the match";
                market.put(marketDescription, probs[22]);
                markets.addMarketWithShortKey(market);
            }
            /**
             * Chance mix markets
             */

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CM15", "M", "CHANCE MIX FULL TIME 1.5");
            market.setIsValid(true);
            probs = generateChangeMixWithLineProbs(scoreGrid, 1.5);
            marketDescription = "Home or Over " + 1.5;
            market.put(marketDescription, probs[0]);
            marketDescription = "Draw or Over " + 1.5;
            market.put(marketDescription, probs[1]);
            marketDescription = "Away or Over " + 1.5;
            market.put(marketDescription, probs[2]);
            marketDescription = "Home or Under " + 1.5;
            market.put(marketDescription, probs[3]);
            marketDescription = "Draw or Under " + 1.5;
            market.put(marketDescription, probs[4]);
            marketDescription = "Away or Under " + 1.5;
            market.put(marketDescription, probs[5]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CM25", "M", "CHANCE MIX FULL TIME 2.5");
            market.setIsValid(true);
            probs = generateChangeMixWithLineProbs(scoreGrid, 2.5);
            marketDescription = "Home or Over " + 2.5;
            market.put(marketDescription, probs[0]);
            marketDescription = "Draw or Over " + 2.5;
            market.put(marketDescription, probs[1]);
            marketDescription = "Away or Over " + 2.5;
            market.put(marketDescription, probs[2]);
            marketDescription = "Home or Under " + 2.5;
            market.put(marketDescription, probs[3]);
            marketDescription = "Draw or Under " + 2.5;
            market.put(marketDescription, probs[4]);
            marketDescription = "Away or Under " + 2.5;
            market.put(marketDescription, probs[5]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CM35", "M", "CHANCE MIX FULL TIME 3.5");
            market.setIsValid(true);
            probs = generateChangeMixWithLineProbs(scoreGrid, 3.5);
            marketDescription = "Home or Over " + 3.5;
            market.put(marketDescription, probs[0]);
            marketDescription = "Draw or Over " + 3.5;
            market.put(marketDescription, probs[1]);
            marketDescription = "Away or Over " + 3.5;
            market.put(marketDescription, probs[2]);
            marketDescription = "Home or Under " + 3.5;
            market.put(marketDescription, probs[3]);
            marketDescription = "Draw or Under " + 3.5;
            market.put(marketDescription, probs[4]);
            marketDescription = "Away or Under " + 3.5;
            market.put(marketDescription, probs[5]);
            markets.addMarketWithShortKey(market);

            if (((FootballMatchState) matchState).preMatch()) {
                market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:CM2", "M", "HALF TIME CHANCE MIX");
                market.setIsValid(true);
                probs = generateHalfTimeChangeMixProbs(scoreGridPeriod, scoreGridSecondHalf);
                marketDescription = "Home team to win at first half or at full time";
                market.put(marketDescription, probs[0]);
                marketDescription = "There should be draw at half time or at full time";
                market.put(marketDescription, probs[1]);
                marketDescription = "Away team to win at half time or at full time";
                market.put(marketDescription, probs[2]);
                marketDescription = "Home team to win at half time OR both team to score at half time";
                market.put(marketDescription, probs[3]);
                marketDescription = "Draw to happen at half time OR both team will score at half time";
                market.put(marketDescription, probs[4]);
                marketDescription = "Away team to win at half time OR both team to score at half time";
                market.put(marketDescription, probs[5]);
                marketDescription = "Home team to win at half time OR no goal will be score at half time";
                market.put(marketDescription, probs[6]);
                marketDescription = "Draw to happen at half time OR no goal will be score at half time";
                market.put(marketDescription, probs[7]);
                marketDescription = "Away team to win at half time OR no goal will be score at half time";
                market.put(marketDescription, probs[8]);
                marketDescription = "Home team to win at half time OR 2 goals and above will be scored at half time";
                market.put(marketDescription, probs[9]);
                marketDescription = "First half to end in a draw OR 2 goals and above will be scored at half time";
                market.put(marketDescription, probs[10]);
                marketDescription = "Away team to win at half time OR 2 goals and above will be scored at half time";
                market.put(marketDescription, probs[11]);
                marketDescription = "Home team to win at half time OR no goal will be score at half time";
                market.put(marketDescription, probs[12]);
                marketDescription = "Away team to win at half time OR no goal will be score at half time";
                market.put(marketDescription, probs[13]);
                marketDescription = "Home team to win at half time OR not more than 1 goal will be scored at half time";
                market.put(marketDescription, probs[14]);
                marketDescription = "Away team to win at half time OR not more than 1 goal will be scored at half time";
                market.put(marketDescription, probs[15]);
                marketDescription =
                                "Both team to score at half time OR not more than 1 goal will be scored at half time";
                market.put(marketDescription, probs[16]);
                marketDescription = "Home team to win in second half OR both team to score in second half";
                market.put(marketDescription, probs[17]);
                marketDescription = "Draw to happen in second half OR both team to score in second half";
                market.put(marketDescription, probs[18]);
                marketDescription = "Away team to win in second half OR both team to score in second half";
                market.put(marketDescription, probs[19]);
                marketDescription = "Home team to win in second half OR no goal will be score in second half";
                market.put(marketDescription, probs[20]);
                marketDescription = "Draw to happen in second half OR no goal will be score in second half";
                market.put(marketDescription, probs[21]);
                marketDescription = "Away team to win in second half OR no goal will be scored in second half";
                market.put(marketDescription, probs[22]);
                marketDescription =
                                "Home team to win in second half OR 2 goals and above will be scored in second half";
                market.put(marketDescription, probs[23]);
                marketDescription = "Draw to happen in second half OR 2 goals and above will be scored in second half";
                market.put(marketDescription, probs[24]);
                marketDescription =
                                "Away team to win in second half OR 2 goals and above will be scored in second half";
                market.put(marketDescription, probs[25]);
                marketDescription = "Home team to win in second half OR no goal will be score in second half";
                market.put(marketDescription, probs[26]);
                marketDescription = "Away team to win in second half OR no goal will be scored in second half";
                market.put(marketDescription, probs[27]);
                marketDescription =
                                "Home team to win at second half OR not nore than 1 goal will be scored at second half";
                market.put(marketDescription, probs[28]);
                marketDescription =
                                "Away team to win at second half OR not more than 1 goal will be scored at second half";
                market.put(marketDescription, probs[29]);
                marketDescription =
                                "Both team to score in second half OR not more than 1 goal will be scored in second half";
                market.put(marketDescription, probs[30]);
                markets.addMarketWithShortKey(market);
            }

            // FIXME: CREATED TO TEST PARTIAL RESULTING LOGIC

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:PRT", "M",
                            "Partial Resulting Test Market");
            if (periodNo <= 1) {
                market.setIsValid(true);
                marketDescription = "W";
                market.put(marketDescription, 1.0);
                marketDescription = "PL";
                market.put(marketDescription, 1.0);
                marketDescription = "PV";
                market.put(marketDescription, 1.0);
                marketDescription = "L";
                market.put(marketDescription, 1.0);
            } else if (periodNo <= 2) {
                market.setIsValid(true);
                marketDescription = "W";
                market.put(marketDescription, 0.5);
                marketDescription = "L";
                market.put(marketDescription, 0.5);
            }
            markets.addMarketWithShortKey(market);


            /*** new working section ends ****/

            markets.addMarketWithShortKey(market);
            Market csMarket = markets.get("FT:CS");
            if (GENERATE_GS_MARKETS) {
                Map<String, Market> gsMarkets = NextAnytimeGsCalculator.calculate(csMarket,
                                (FootballMatchState) matchState, (FootballMatchParams) matchParams);
                for (Market gsMarket : gsMarkets.values())
                    markets.addMarketWithShortKey(gsMarket);
            }

            /* Luke's request */
            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:A:MLTG", "M", "Multigoals Home Team");
            market.setIsValid(true);
            probs = generateChangeMultigoalsTeamProbs(scoreGrid, "H");
            marketDescription = "0";
            market.put(marketDescription, probs[0]);
            marketDescription = "1-2";
            market.put(marketDescription, probs[1]);
            marketDescription = "1-3";
            market.put(marketDescription, probs[2]);
            marketDescription = "2-3";
            market.put(marketDescription, probs[3]);
            marketDescription = "4+";
            market.put(marketDescription, probs[4]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:B:MLTG", "M", "Multigoals Away Team");
            market.setIsValid(true);
            probs = generateChangeMultigoalsTeamProbs(scoreGrid, "A");
            marketDescription = "0";
            market.put(marketDescription, probs[0]);
            marketDescription = "1-2";
            market.put(marketDescription, probs[1]);
            marketDescription = "1-3";
            market.put(marketDescription, probs[2]);
            marketDescription = "2-3";
            market.put(marketDescription, probs[3]);
            marketDescription = "4+";
            market.put(marketDescription, probs[4]);
            markets.addMarketWithShortKey(market);

            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:A:Â£OUR_2", "M",
                            "Home Team Total Goals (Aggregated)");
            market.setIsValid(true);
            probs = generateTotalAggregatedTeamProbs(scoreGrid, "H");
            marketDescription = "0-1";
            market.put(marketDescription, probs[0]);
            marketDescription = "2-3";
            market.put(marketDescription, probs[1]);
            marketDescription = "4+";
            market.put(marketDescription, probs[2]);
            markets.addMarketWithShortKey(market);


            market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:B:Â£OUR_2", "M",
                            "Away Team Total Goals (Aggregated)");
            market.setIsValid(true);
            probs = generateTotalAggregatedTeamProbs(scoreGrid, "A");
            marketDescription = "0-1";
            market.put(marketDescription, probs[0]);
            marketDescription = "2-3";
            market.put(marketDescription, probs[1]);
            marketDescription = "4+";
            market.put(marketDescription, probs[2]);
            markets.addMarketWithShortKey(market);
        }

    }

    private double[] generateTotalAggregatedTeamProbs(double[][] scoreGrid, String homeaway) {
        double[] probs = new double[3];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (homeaway == "A") // if it is for the away team
                {
                    if (away == 0 || away == 1)
                        probs[0] += scoreGrid[home][away];
                    else if (away == 2 || away == 3)
                        probs[1] += scoreGrid[home][away];
                    else if (away > 3)
                        probs[2] += scoreGrid[home][away];
                } else {
                    if (home == 0 || home == 1)
                        probs[0] += scoreGrid[home][away];
                    else if (home == 2 || home == 3)
                        probs[1] += scoreGrid[home][away];
                    else if (home > 3)
                        probs[2] += scoreGrid[home][away];
                }
            }
        }
        return probs;
    }

    private double[] generateChangeMultigoalsTeamProbs(double[][] scoreGrid, String homeaway) {
        double[] probs = new double[5];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (homeaway == "A") // if it is for the away team
                {
                    if (away == 0)
                        probs[0] += scoreGrid[home][away];
                    if (away >= 1 && away <= 2)
                        probs[1] += scoreGrid[home][away];
                    if (away >= 1 && away <= 3)
                        probs[2] += scoreGrid[home][away];
                    if (away >= 2 && away <= 3)
                        probs[3] += scoreGrid[home][away];
                    if (away >= 4)
                        probs[4] += scoreGrid[home][away];
                } else {
                    if (home == 0)
                        probs[0] += scoreGrid[home][away];
                    if (home >= 1 && home <= 2)
                        probs[1] += scoreGrid[home][away];
                    if (home >= 1 && home <= 3)
                        probs[2] += scoreGrid[home][away];
                    if (home >= 2 && home <= 3)
                        probs[3] += scoreGrid[home][away];
                    if (home >= 4)
                        probs[4] += scoreGrid[home][away];
                }
            }
        }
        return probs;
    }

    private double[] generatePeriodTotalGoalsRangeProbsTeam(double[][] scoreGrid, int i) {
        double[] probs = new double[4];
        if (i == 1)
            for (int home = 0; home < scoreGrid.length; home++) {
                for (int away = 0; away < scoreGrid.length; away++) {
                    if (home == 0)
                        probs[0] += scoreGrid[home][away];
                    else if (home == 1)
                        probs[1] += scoreGrid[home][away];
                    else if (home == 2)
                        probs[2] += scoreGrid[home][away];
                    else if (home >= 3)
                        probs[3] += scoreGrid[home][away];
                }
            }

        if (i == 2)
            for (int home = 0; home < scoreGrid.length; home++) {
                for (int away = 0; away < scoreGrid.length; away++) {
                    if (away == 0)
                        probs[0] += scoreGrid[home][away];
                    else if (away == 1)
                        probs[1] += scoreGrid[home][away];
                    else if (away == 2)
                        probs[2] += scoreGrid[home][away];
                    else if (away >= 3)
                        probs[3] += scoreGrid[home][away];
                }
            }

        return probs;
    }

    private double[] generateTotalGoalsRange3Probs(double[][] scoreGrid) {
        double[] probs = new double[3];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home + away == 0 || home + away == 1)
                    probs[0] += scoreGrid[home][away];
                else if (home + away == 2 || home + away == 3)
                    probs[1] += scoreGrid[home][away];
                else if (home + away >= 4)
                    probs[2] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateTotalGoalsRange4Probs(double[][] scoreGrid) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home + away == 0)
                    probs[0] += scoreGrid[home][away];
                else if (home + away == 1)
                    probs[1] += scoreGrid[home][away];
                else if (home + away == 2)
                    probs[2] += scoreGrid[home][away];
                else if (home + away == 3)
                    probs[3] += scoreGrid[home][away];
                else if (home + away == 4)
                    probs[4] += scoreGrid[home][away];
                else if (home + away >= 5)
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generatePeriodTotalGoalsRangeProbs(double[][] scoreGrid) {
        double[] probs = new double[4];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home + away == 0)
                    probs[0] += scoreGrid[home][away];
                else if (home + away == 1)
                    probs[1] += scoreGrid[home][away];
                else if (home + away == 2)
                    probs[2] += scoreGrid[home][away];
                else if (home + away >= 3)
                    probs[3] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generatePeriodCSPermutationsProbs(double[][] scoreGrid) {
        double[] probs = new double[16];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home == 0 && away == 0)
                    probs[0] += scoreGrid[home][away];
                else if (home == 1 && away == 0)
                    probs[1] += scoreGrid[home][away];
                else if (home == 2 && away == 0)
                    probs[2] += scoreGrid[home][away];
                else if (home >= 3 && away == 0)
                    probs[3] += scoreGrid[home][away];


                else if (home == 2 && away == 1)
                    probs[4] += scoreGrid[home][away];
                else if (home >= 3 && away == 1)
                    probs[5] += scoreGrid[home][away];


                else if (home >= 3 && away == 2)
                    probs[6] += scoreGrid[home][away];


                else if (home == 1 && away == 1)
                    probs[7] += scoreGrid[home][away];
                else if (home == 2 && away == 2)
                    probs[8] += scoreGrid[home][away];
                else if (home >= 3 && away >= 3)
                    probs[9] += scoreGrid[home][away];


                else if (home == 2 && away >= 3)
                    probs[10] += scoreGrid[home][away];

                else if (home == 1 && away == 2)
                    probs[11] += scoreGrid[home][away];
                else if (home == 1 && away >= 3)
                    probs[12] += scoreGrid[home][away];

                else if (home == 0 && away == 1)
                    probs[13] += scoreGrid[home][away];
                else if (home == 0 && away == 2)
                    probs[14] += scoreGrid[home][away];
                else if (home == 0 && away >= 3)
                    probs[15] += scoreGrid[home][away];
            }
        }
        return probs;
    }



    private double[] generateCSPermutationsProbs(double[][] scoreGrid) {
        double[] probs = new double[36];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home == 0 && away == 0)
                    probs[0] += scoreGrid[home][away];
                else if (home == 1 && away == 0)
                    probs[1] += scoreGrid[home][away];
                else if (home == 2 && away == 0)
                    probs[2] += scoreGrid[home][away];
                else if (home == 3 && away == 0)
                    probs[3] += scoreGrid[home][away];
                else if (home == 4 && away == 0)
                    probs[4] += scoreGrid[home][away];
                else if (home >= 5 && away == 0)
                    probs[5] += scoreGrid[home][away];

                else if (home == 2 && away == 1)
                    probs[6] += scoreGrid[home][away];
                else if (home == 3 && away == 1)
                    probs[7] += scoreGrid[home][away];
                else if (home == 4 && away == 1)
                    probs[8] += scoreGrid[home][away];
                else if (home >= 5 && away == 1)
                    probs[9] += scoreGrid[home][away];

                else if (home == 3 && away == 2)
                    probs[10] += scoreGrid[home][away];
                else if (home == 4 && away == 2)
                    probs[11] += scoreGrid[home][away];
                else if (home >= 5 && away == 2)
                    probs[12] += scoreGrid[home][away];

                else if (home == 4 && away == 3)
                    probs[13] += scoreGrid[home][away];
                else if (home >= 5 && away == 3)
                    probs[14] += scoreGrid[home][away];

                else if (home >= 5 && away == 4)
                    probs[15] += scoreGrid[home][away];

                else if (home == 1 && away == 1)
                    probs[16] += scoreGrid[home][away];
                else if (home == 2 && away == 2)
                    probs[17] += scoreGrid[home][away];
                else if (home == 3 && away == 3)
                    probs[18] += scoreGrid[home][away];
                else if (home == 4 && away == 4)
                    probs[19] += scoreGrid[home][away];


                else if (home >= 5 && away >= 5)
                    probs[20] += scoreGrid[home][away];
                else if (home == 4 && away >= 5)
                    probs[21] += scoreGrid[home][away];
                else if (home == 3 && away == 4)
                    probs[22] += scoreGrid[home][away];
                else if (home == 3 && away >= 5)
                    probs[23] += scoreGrid[home][away];

                else if (home == 2 && away == 3)
                    probs[24] += scoreGrid[home][away];
                else if (home == 2 && away == 4)
                    probs[25] += scoreGrid[home][away];
                else if (home == 2 && away >= 5)
                    probs[26] += scoreGrid[home][away];

                else if (home == 1 && away == 2)
                    probs[27] += scoreGrid[home][away];
                else if (home == 1 && away == 3)
                    probs[28] += scoreGrid[home][away];
                else if (home == 1 && away == 4)
                    probs[29] += scoreGrid[home][away];
                else if (home == 1 && away >= 5)
                    probs[30] += scoreGrid[home][away];

                else if (home == 0 && away == 1)
                    probs[31] += scoreGrid[home][away];
                else if (home == 0 && away == 2)
                    probs[32] += scoreGrid[home][away];
                else if (home == 0 && away == 3)
                    probs[33] += scoreGrid[home][away];
                else if (home == 0 && away == 4)
                    probs[34] += scoreGrid[home][away];
                else if (home == 0 && away >= 5)
                    probs[35] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateTWOBTSOUProbs(double[][] scoreGrid, double line) {
        double[] probs = new double[12];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away || (home > 0 && away > 0) || (home + away) > line)
                    probs[0] += scoreGrid[home][away];
                if (home < away || (home > 0 && away > 0) || (home + away) > line)
                    probs[1] += scoreGrid[home][away];
                if (home == away || (home > 0 && away > 0) || (home + away) > line)
                    probs[2] += scoreGrid[home][away];

                if (home > away || (home > 0 && away > 0) || (home + away) < line)
                    probs[3] += scoreGrid[home][away];
                if (home > away || (home > 0 && away > 0) || (home + away) < line)
                    probs[4] += scoreGrid[home][away];
                if (home > away || (home > 0 && away > 0) || (home + away) < line)
                    probs[5] += scoreGrid[home][away];

                if (home > away || !(home > 0 && away > 0) || (home + away) > line)
                    probs[6] += scoreGrid[home][away];
                if (home < away || !(home > 0 && away > 0) || (home + away) > line)
                    probs[7] += scoreGrid[home][away];
                if (home == away || !(home > 0 && away > 0) || (home + away) > line)
                    probs[8] += scoreGrid[home][away];

                if (home > away || !(home > 0 && away > 0) || (home + away) < line)
                    probs[9] += scoreGrid[home][away];
                if (home > away || !(home > 0 && away > 0) || (home + away) < line)
                    probs[10] += scoreGrid[home][away];
                if (home > away || !(home > 0 && away > 0) || (home + away) < line)
                    probs[11] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateChangeMixWithLineProbs(double[][] scoreGrid, double line) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away || (home + away) > line)
                    probs[0] += scoreGrid[home][away];
                if (home == away || (home + away) > line)
                    probs[1] += scoreGrid[home][away];
                if (home < away || (home + away) > line)
                    probs[2] += scoreGrid[home][away];
                if (home > away || (home + away) < line)
                    probs[3] += scoreGrid[home][away];
                if (home == away || (home + away) < line)
                    probs[4] += scoreGrid[home][away];
                if (home < away || (home + away) < line)
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateWinnerProbsFromScoreGrid(double[][] cScoreGridPeriod) {
        double[] probs = new double[3];
        for (int home = 0; home < cScoreGridPeriod.length; home++) {
            for (int away = 0; away < cScoreGridPeriod.length; away++) {
                if (home > away)
                    probs[0] += cScoreGridPeriod[home][away];
                if (home == away)
                    probs[1] += cScoreGridPeriod[home][away];
                if (home < away)
                    probs[2] += cScoreGridPeriod[home][away];
            }
        }
        return probs;
    }

    private double[] generateWinnerProbsFromScoreGridForWTTS(double[][] cScoreGridPeriod) {
        double[] probs = new double[4];
        for (int home = 0; home < cScoreGridPeriod.length; home++) {
            for (int away = 0; away < cScoreGridPeriod.length; away++) {
                if (home > away && away == 0) {
                    probs[0] += cScoreGridPeriod[home][away];
                }
                if (home < away && home == 0) {
                    probs[1] += cScoreGridPeriod[home][away];
                }
                if (home > 0 && away > 0) {
                    probs[2] += cScoreGridPeriod[home][away];
                }
                if (home == 0 && away == 0) {
                    probs[3] += cScoreGridPeriod[home][away];
                }
            }
        }
        return probs;
    }

    private double[] generateHalfTimeChangeMixProbs(double[][] scoreGridPeriod1, double[][] scoreGridPeriod2) {
        double[] probs = new double[31];
        // double sum = 0.0;
        // for (int homeM = 0; homeM < scoreGrid.length; homeM++) {
        // for (int awayM = 0; awayM < scoreGrid.length; awayM++) {
        // sum += scoreGridPeriod[homeM][awayM];
        // }
        // }

        for (int home1 = 0; home1 < scoreGridPeriod1.length; home1++) {
            for (int away1 = 0; away1 < scoreGridPeriod1.length; away1++) {
                for (int home2 = 0; home2 < scoreGridPeriod2.length; home2++) {
                    for (int away2 = 0; away2 < scoreGridPeriod2.length; away2++) {
                        int home = home1 + home2;
                        int away = away1 + away2;

                        if (home1 > away1 || home > away)
                            probs[0] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 == away1 || home == away)
                            probs[1] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || home < away)
                            probs[2] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];


                        if (home1 > away1 || (home1 > 0 && away1 > 0))
                            probs[3] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 == away1 || (home1 > 0 && away1 > 0))
                            probs[4] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || (home1 > 0 && away1 > 0))
                            probs[5] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home1 > away1 || (home1 + away1 == 0))
                            probs[6] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 == away1 || (home1 + away1 == 0))
                            probs[7] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || (home1 + away1 == 0))
                            probs[8] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home1 > away1 || (home1 + away1 >= 2))
                            probs[9] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 == away1 || (home1 + away1 >= 2))
                            probs[10] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || (home1 + away1 >= 2))
                            probs[11] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home1 > away1 || !(home1 > 0 && away1 > 0))
                            probs[12] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || !(home1 > 0 && away1 > 0))
                            probs[13] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 > away1 || (home1 + away1 <= 1))
                            probs[14] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home1 < away1 || (home1 + away1 <= 1))
                            probs[15] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if ((home1 > 0 && away1 > 0) || (home1 + away1 <= 1))
                            probs[16] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home2 > away2 || (home2 > 0 && away2 > 0))
                            probs[17] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 == away2 || (home2 > 0 && away2 > 0))
                            probs[18] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 < away2 || (home2 > 0 && away2 > 0))
                            probs[19] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];


                        if (home2 > away2 || (home2 == 0 && away2 == 0))
                            probs[20] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 == away2 || (home2 == 0 && away2 == 0))
                            probs[21] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 < away2 || (home2 == 0 && away2 == 0))
                            probs[22] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];


                        if (home2 > away2 || (home2 + away2 >= 2))
                            probs[23] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 == away2 || (home2 + away2 >= 2))
                            probs[24] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 < away2 || (home2 + away2 >= 2))
                            probs[25] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home2 > away2 || (home2 + away2 == 0))
                            probs[26] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if (home2 < away2 || (home2 + away2 == 0))
                            probs[27] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home2 > away2 || (home2 + away2 <= 1))
                            probs[28] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                        if (home2 < away2 || (home2 + away2 <= 1))
                            probs[29] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];
                        if ((home2 > 0 && away2 > 0) || (home2 + away2 <= 1))
                            probs[30] += scoreGridPeriod1[home1][away1] * scoreGridPeriod2[home2][away2];

                    }
                }

            }
        }
        return probs;
    }

    private double[] generateChangeMixProbs(double[][] scoreGrid) {
        double[] probs = new double[23];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away || (home + away) >= 3)
                    probs[0] += scoreGrid[home][away];
                if (home == away || (home + away) >= 3)
                    probs[1] += scoreGrid[home][away];
                if (home < away || (home + away) >= 3)
                    probs[2] += scoreGrid[home][away];
                if (home < away || (home + away) < 2.5)
                    probs[3] += scoreGrid[home][away];
                if (home == away || (home + away) < 2.5)
                    probs[4] += scoreGrid[home][away];
                if (home < away || (home + away) < 2.5)
                    probs[5] += scoreGrid[home][away];
                if (home > away || (home > 0 && away > 0))
                    probs[6] += scoreGrid[home][away];
                if (home == away || (home > 0 && away > 0))
                    probs[7] += scoreGrid[home][away];
                if (home < away || (home > 0 && away > 0))
                    probs[8] += scoreGrid[home][away];
                if (home > away || !(home > 0 && away > 0))
                    probs[9] += scoreGrid[home][away];
                if (home == away || !(home > 0 && away > 0))
                    probs[10] += scoreGrid[home][away];
                if (home < away || !(home > 0 && away > 0))
                    probs[11] += scoreGrid[home][away];
                if (home > away || (home + away) < 3.5)
                    probs[12] += scoreGrid[home][away];
                if (home < away || (home + away) < 3.5)
                    probs[13] += scoreGrid[home][away];
                if (home == away || (home + away) < 3.5)
                    probs[14] += scoreGrid[home][away];
                if (home == away || (home + away) >= 2)
                    probs[15] += scoreGrid[home][away];
                if (home == away || (home + away) < 1.5)
                    probs[16] += scoreGrid[home][away];
                if (home > away || (home + away) < 1.5)
                    probs[17] += scoreGrid[home][away];
                if (home < away || (home + away) < 1.5)
                    probs[18] += scoreGrid[home][away];
                if ((home + away < 1.5) || (home > 0 && away > 0))
                    probs[19] += scoreGrid[home][away];
                if (home > away || (home + away) >= 2)
                    probs[20] += scoreGrid[home][away];
                if (home < away || (home + away) >= 2)
                    probs[21] += scoreGrid[home][away];
                if (home == away || (home + away) >= 2)
                    probs[22] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    /**
     * Generating an array that each of the entry is a yes or no markets
     */
    private double[] generateHalfDCComboSpecialProbs(double[][] scoreGridSecondHalf, double[][] scoreGrid) {
        double[] probs = new double[8];

        for (int homeM = 0; homeM < scoreGrid.length; homeM++) {
            for (int awayM = 0; awayM < scoreGrid.length; awayM++) {
                for (int homeP = 0; homeP < scoreGridSecondHalf.length; homeP++) {
                    for (int awayP = 0; awayP < scoreGridSecondHalf.length; awayP++) {
                        if (homeM >= homeP && awayM >= awayP) {

                            if ((homeM + awayM) >= 2 && (homeP > awayP) && (homeM > awayM)) {
                                probs[0] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }
                            if ((homeM + awayM) >= 2 && (homeP < awayP) && (homeM < awayM)) {
                                probs[1] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }
                            if ((homeM + awayM) >= 3 && (homeP > awayP) && (homeM > awayM)) {
                                probs[2] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }
                            if ((homeM + awayM) >= 3 && (homeP < awayP) && (homeM < awayM)) {
                                probs[3] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }

                            if ((homeM + awayM) >= 4 && (homeP > awayP) && (homeM > awayM)) {
                                probs[4] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }

                            if ((homeM + awayM) >= 4 && (homeP < awayP) && (homeM < awayM)) {
                                probs[5] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }

                            if ((homeP > awayP) && (homeM > awayM) && (homeM + awayM - homeP - awayP >= 2)) {
                                probs[6] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }

                            if ((homeP < awayP) && (homeM < awayM) && (homeM + awayM - homeP - awayP >= 2)) {
                                probs[7] += scoreGrid[homeM][awayM] * scoreGridSecondHalf[homeP][awayP];
                            }

                        }
                    }
                }

            }
        }
        return probs;
    }

    private double[] generateSecondHalfComboOdds(double[][] scoreGridPeriod, double[][] scoreGrid) {
        double[] probs = new double[8];

        for (int homeM = 0; homeM < scoreGrid.length; homeM++) {
            for (int awayM = 0; awayM < scoreGrid.length; awayM++) {
                for (int homeP = 0; homeP < scoreGridPeriod.length; homeP++) {
                    for (int awayP = 0; awayP < scoreGridPeriod.length; awayP++) {
                        if (homeM >= homeP && awayM >= awayP) {

                            if (homeM > awayM && (homeP > 0 && awayP > 0)) {
                                probs[0] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM == awayM) && (homeP > 0 && awayP > 0)) {
                                probs[1] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM < awayM) && (homeP > 0 && awayP > 0)) {
                                probs[2] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM > awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[3] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM == awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[4] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM < awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[5] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM > awayM) && (homeP + awayP >= 2)) {
                                probs[6] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM < awayM) && (homeP + awayP >= 2)) {
                                probs[7] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                        }
                    }
                }

            }
        }
        return probs;
    }

    private double[] generateHalfDCComboDoubleChanceSpecialProbs(double[][] scoreGridPeriod, double[][] scoreGrid) {
        double[] probs = new double[9];

        for (int homeM = 0; homeM < scoreGrid.length; homeM++) {
            for (int awayM = 0; awayM < scoreGrid.length; awayM++) {
                for (int homeP = 0; homeP < scoreGridPeriod.length; homeP++) {
                    for (int awayP = 0; awayP < scoreGridPeriod.length; awayP++) {
                        if (homeM >= homeP && awayM >= awayP) {

                            if (homeM >= awayM && (homeP > 0 && awayP > 0)) {
                                probs[0] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM != awayM) && (homeP > 0 && awayP > 0)) {
                                probs[1] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM <= awayM) && (homeP > 0 && awayP > 0)) {
                                probs[2] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }
                            if ((homeM >= awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[3] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM > awayM || homeM < awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[4] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM <= awayM) && (!(homeP > 0 && awayP > 0))) {
                                probs[5] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM >= awayM) && (homeP + awayP >= 2)) {
                                probs[6] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM > awayM || homeM < awayM) && (homeP + awayP >= 2)) {
                                probs[7] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                            if ((homeM <= awayM) && (homeP + awayP >= 2)) {
                                probs[8] += scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awayP];
                            }

                        }
                    }
                }

            }
        }
        return probs;
    }

    /* Double Chance Halftime Fulltime */
    private double[] generateDCHTFTProbs(double[][] scoreGrid1h, double[][] scoreGrid2h) {
        double[] probs = new double[9];

        for (int home1 = 0; home1 < scoreGrid1h.length; home1++) {
            for (int away1 = 0; away1 < scoreGrid1h.length; away1++) {
                for (int home2 = 0; home2 < scoreGrid2h.length; home2++) {
                    for (int away2 = 0; away2 < scoreGrid2h.length; away2++) {

                        int homeM = home1 + home2;
                        int awayM = away1 + away2;

                        if (homeM >= awayM && home1 >= away1) {
                            probs[0] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }
                        if (homeM != awayM && home1 >= away1) {
                            probs[1] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }
                        if (homeM <= awayM && home1 >= away1) {
                            probs[2] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                        if (homeM >= awayM && home1 != away1) {
                            probs[3] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                        if (homeM != awayM && home1 != away1) {
                            probs[4] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                        if (homeM <= awayM && home1 != away1) {
                            probs[5] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }


                        if (homeM >= awayM && home1 <= away1) {
                            probs[6] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                        if (homeM != awayM && home1 <= away1) {
                            probs[7] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                        if (homeM <= awayM && home1 <= away1) {
                            probs[8] += scoreGrid1h[home1][away1] * scoreGrid2h[home2][away2];
                        }

                    }
                }

            }
        }
        return probs;
    }

    @SuppressWarnings("unused")
    private double[][] creatScoreGridSecondHalf(double[][] scoreGrid, double[][] scoreGridPeriod) {
        double[][] scoreGridOut = new double[30][30];
        Double sumProb = 0.0;
        // normalising
        // FIXME: CORRECT ME
        for (int homeM = 0; homeM < scoreGridPeriod.length; homeM++) {
            for (int awayM = 0; awayM < scoreGridPeriod.length; awayM++) {
                for (int homeP = 0; homeP < scoreGridPeriod.length; homeP++) {
                    for (int awaP = 0; awaP < scoreGridPeriod.length; awaP++) {
                        if (homeM >= homeP && awayM >= awaP) {
                            scoreGridOut[homeM - homeP][awayM - awaP] +=
                                            scoreGrid[homeM][awayM] * scoreGridPeriod[homeP][awaP];
                            sumProb += scoreGridOut[homeM - homeP][awayM - awaP];
                        }
                    }
                }

            }
        }
        return scoreGridOut;
    }

    private double[] generatOVUNProbs(double[][] scoreGridPeriod, double d) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGridPeriod.length; home++) {
            for (int away = 0; away < scoreGridPeriod.length; away++) {
                if ((home + away) >= d)
                    probs[0] += scoreGridPeriod[home][away];
                if ((home + away) < d)
                    probs[1] += scoreGridPeriod[home][away];
            }
        }
        return probs;
    }

    private double[] generatOVUNDrawProbs(double[][] scoreGridPeriod, double d) {
        double[] probs = new double[3];
        for (int home = 0; home < scoreGridPeriod.length; home++) {
            for (int away = 0; away < scoreGridPeriod.length; away++) {
                if ((home - away) > d)
                    probs[0] += scoreGridPeriod[home][away];
                if ((home - away) == d)
                    probs[1] += scoreGridPeriod[home][away];
                if ((home - away) < d)
                    probs[2] += scoreGridPeriod[home][away];
            }
        }
        return probs;
    }

    private double generatHomeOVUNProbs(double[][] scoreGridPeriod, double d) {
        double prob = 0.0;
        for (int home = 0; home < scoreGridPeriod.length; home++) {
            for (int away = 0; away < scoreGridPeriod.length; away++) {
                if (home > d)
                    prob += scoreGridPeriod[home][away];
            }
        }
        return prob;
    }

    private double generatAwayOVUNProbs(double[][] scoreGridPeriod, double d) {
        double prob = 0.0;
        for (int home = 0; home < scoreGridPeriod.length; home++) {
            for (int away = 0; away < scoreGridPeriod.length; away++) {
                if (away > d)
                    prob += scoreGridPeriod[home][away];
            }
        }
        return prob;
    }

    private double[] generateMatchResultOrOVUNProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away || (home + away) >= bLine)
                    probs[0] += scoreGrid[home][away];
                if (home == away || (home + away) >= bLine)
                    probs[1] += scoreGrid[home][away];
                if (home < away || (home + away) >= bLine)
                    probs[2] += scoreGrid[home][away];
                if (home > away || (home + away) < bLine)
                    probs[3] += scoreGrid[home][away];
                if (home == away && (home + away) < bLine)
                    probs[4] += scoreGrid[home][away];
                if (home < away && (home + away) < bLine)
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateDBLCHCPProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[3];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {

                int homeHcp = (int) (home + bLine);
                int awayHcp = (int) (away - bLine);

                if (homeHcp > away || home < awayHcp)
                    probs[0] += scoreGrid[home][away];
                if (home == away || home < awayHcp)
                    probs[1] += scoreGrid[home][away];
                if (homeHcp > away || home == away)
                    probs[2] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateBothTeamScoreAndTotalProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[4];

        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                // Both teams to score OR X or more goals to be scored in the
                // match
                if (home > 0 && away > 0 && (home + away) >= bLine)
                    probs[0] += scoreGrid[home][away];
                // Not both teams to score OR X OR more goals to be scored in
                // the match
                if (!(home > 0 && away > 0) && (home + away) >= bLine)
                    probs[1] += scoreGrid[home][away];
                // Both teams to score OR X or less goals to be scored in the
                // match
                if (home > 0 && away > 0 && (home + away) < bLine)
                    probs[2] += scoreGrid[home][away];
                // Not both teams to score OR less than X goals to be scored in
                // the match
                if (!(home > 0 && away > 0) && (home + away) < bLine)
                    probs[3] += scoreGrid[home][away];
            }
        }

        return probs;
    }

    private double[] generateBothTeamScoreOrTotalProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[4];

        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                // Both teams to score OR X or more goals to be scored in the
                // match
                if (home > 0 && away > 0 || (home + away) >= bLine)
                    probs[0] += scoreGrid[home][away];
                // Not both teams to score OR X OR more goals to be scored in
                // the match
                if (!(home > 0 && away > 0) || (home + away) < bLine)
                    probs[1] += scoreGrid[home][away];
                // Both teams to score OR X or less goals to be scored in the
                // match
                if (home > 0 && away > 0 || (home + away) >= bLine)
                    probs[2] += scoreGrid[home][away];
                // Not both teams to score OR less than X goals to be scored in
                // the match
                if (!(home > 0 && away > 0) || (home + away) < bLine)
                    probs[3] += scoreGrid[home][away];
            }
        }

        return probs;
    }

    private double[] generateHFPOUProbs(double[][] scoreGridPeriod, double[][] scoreGrid, double bLine) {
        double[] probs = new double[18];

        for (int homeHalf = 0; homeHalf < scoreGridPeriod.length; homeHalf++) {
            for (int awayHalf = 0; awayHalf < scoreGridPeriod.length; awayHalf++) {

                for (int home = 0; home < scoreGrid.length; home++) {
                    for (int away = 0; away < scoreGrid.length; away++) {
                        // H/H and X or more 1st half goals
                        if (homeHalf > awayHalf && home > away && (homeHalf + awayHalf) >= bLine)
                            probs[0] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/A and X or more 1st half goals
                        if (homeHalf < awayHalf && home < away && (homeHalf + awayHalf) >= bLine)
                            probs[1] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // H/D and X or more 1st half goals
                        if (homeHalf > awayHalf && home == away && (homeHalf + awayHalf) >= bLine)
                            probs[2] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // H/A and X or more 1st half goals
                        if (homeHalf > awayHalf && home < away && (homeHalf + awayHalf) >= bLine)
                            probs[3] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/D and X or more 1st half goals
                        if (homeHalf < awayHalf && home == away && (homeHalf + awayHalf) >= bLine)
                            probs[4] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/H and X or more 1st half goals
                        if (homeHalf < awayHalf && home > away && (homeHalf + awayHalf) >= bLine)
                            probs[5] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/D and X or more 1st half goals
                        if (homeHalf == awayHalf && home == away && (homeHalf + awayHalf) >= bLine)
                            probs[6] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/H and X or more 1st half goals
                        if (homeHalf == awayHalf && home > away && (homeHalf + awayHalf) >= bLine)
                            probs[7] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/A and X or more 1st half goals
                        if (homeHalf == awayHalf && home < away && (homeHalf + awayHalf) >= bLine)
                            probs[8] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // H/H and less then X 1st half goals
                        if (homeHalf > awayHalf && home > away && (homeHalf + awayHalf) >= bLine)
                            probs[9] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/A and less then X 1st half goals
                        if (homeHalf < awayHalf && home < away && (homeHalf + awayHalf) < bLine)
                            probs[10] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // H/D and less then X 1st half goals
                        if (homeHalf > awayHalf && home == away && (homeHalf + awayHalf) < bLine)
                            probs[11] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // H/A and less then X 1st half goals
                        if (homeHalf > awayHalf && home < away && (homeHalf + awayHalf) < bLine)
                            probs[12] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/D and less then X 1st half goals
                        if (homeHalf < awayHalf && home == away && (homeHalf + awayHalf) < bLine)
                            probs[13] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // A/H and less then X 1st half goals
                        if (homeHalf < awayHalf && home > away && (homeHalf + awayHalf) < bLine)
                            probs[14] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/D and less then X 1st half goals
                        if (homeHalf == awayHalf && home == away && (homeHalf + awayHalf) < bLine)
                            probs[15] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/H and less then X 1st half goals
                        if (homeHalf == awayHalf && home > away && (homeHalf + awayHalf) < bLine)
                            probs[16] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];
                        // D/A and less then X 1st half goals
                        if (homeHalf == awayHalf && home < away && (homeHalf + awayHalf) < bLine)
                            probs[17] += scoreGrid[home][away] * scoreGridPeriod[homeHalf][awayHalf];

                    }
                }
            }
        }
        return probs;
    }

    private double[] generateHalfOfFirstGoalOdds(double[][] firstHalf, double[][] secondHalf, TeamId teamId) {
        double[] probs = new double[3];

        for (int homeHalf = 0; homeHalf < firstHalf.length; homeHalf++) {
            for (int awayHalf = 0; awayHalf < firstHalf.length; awayHalf++) {

                for (int homeSecond = 0; homeSecond < secondHalf.length; homeSecond++) {
                    for (int awaySecond = 0; awaySecond < secondHalf.length; awaySecond++) {
                        if (teamId == TeamId.A) {
                            if (homeHalf == 0 && homeSecond == 0) {
                                probs[0] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            } else if (homeHalf == 0 && homeSecond > 0) {
                                probs[1] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            } else if (homeHalf > 0) {
                                probs[2] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            }
                        } else if (teamId == TeamId.B) {
                            if (awayHalf == 0 && awaySecond == 0) {
                                probs[0] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            } else if (awayHalf == 0 && awaySecond > 0) {
                                probs[1] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            } else if (awayHalf > 0) {
                                probs[2] += firstHalf[homeHalf][awayHalf] * secondHalf[homeSecond][awaySecond];
                            }
                        }
                    }
                }
            }
        }
        return probs;
    }

    private double[] generateDoubleChanceAndWithInProbs(double[][] scoreGrid, double startLine, double endLine) {
        // i.e : double chance and 2-3 goals scored in match, ax, bx, ab
        double[] probs = new double[3];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if ((home < away || home == away) && (home + away) >= startLine && (home + away) <= endLine) // ax
                    probs[0] += scoreGrid[home][away];
                if ((home > away || home == away) && (home + away) >= startLine && (home + away) <= endLine)// bx
                    probs[1] += scoreGrid[home][away];
                if ((home < away || home > away) && (home + away) >= startLine && (home + away) <= endLine)// ab
                    probs[2] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateMatchResultAndOVUNProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home < away && (home + away) >= bLine)
                    probs[0] += scoreGrid[home][away];
                if (home == away && (home + away) >= bLine)
                    probs[1] += scoreGrid[home][away];
                if (home > away && (home + away) >= bLine)
                    probs[2] += scoreGrid[home][away];
                if (home < away && (home + away) < bLine)
                    probs[3] += scoreGrid[home][away];
                if (home == away && (home + away) < bLine)
                    probs[4] += scoreGrid[home][away];
                if (home > away && (home + away) < bLine)
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateMatchWinnerAndOVUNProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[4];// a or b times over or under
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home < away && (home + away) > bLine)
                    probs[0] += scoreGrid[home][away]; // away and over
                if (home < away && (home + away) < bLine) // away and under
                    probs[1] += scoreGrid[home][away];
                if (home > away && (home + away) > bLine) // home and over
                    probs[2] += scoreGrid[home][away];
                if (home > away && (home + away) < bLine) // home and under
                    probs[3] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateBTSAndOVUNProbs(double[][] scoreGrid, double bLine) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if ((home > 0 && away > 0) && (home + away) >= bLine)
                    probs[0] += scoreGrid[home][away];
                if (!(home > 0 && away > 0) && (home + away) >= bLine)
                    probs[1] += scoreGrid[home][away];
                if ((home > 0 && away > 0) && (home + away) < bLine)
                    probs[2] += scoreGrid[home][away];
                if (!(home > 0 && away > 0) && (home + away) < bLine)
                    probs[3] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateOTSAndOVUNProbs(double[][] scoreGrid, double bLine, boolean overTrue) {
        double[] probs = new double[6];
        if (overTrue) {
            for (int home = 0; home < scoreGrid.length; home++) {
                for (int away = 0; away < scoreGrid.length; away++) {
                    if ((home > 0 && away == 0) && (home == 0 && away > 0) && (home + away) >= bLine)
                        probs[0] += scoreGrid[home][away];
                    probs[1] = 1 - probs[0];
                }
            }
            probs[1] = 1 - probs[0];

        } else {
            for (int home = 0; home < scoreGrid.length; home++) {
                for (int away = 0; away < scoreGrid.length; away++) {
                    if ((home > 0 && away == 0) && (home == 0 && away > 0) && (home + away) < bLine)
                        probs[0] += scoreGrid[home][away];
                }
            }

            probs[1] = 1 - probs[0];
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

    private double[] generateHFOUProbs(double[][] scoreGrid, double line, double[] halfTimeResult) {
        double[] probs = new double[16];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home == away && (home + away >= line)) {
                    probs[0] += scoreGrid[home][away];
                    probs[2] += scoreGrid[home][away];
                    probs[4] += scoreGrid[home][away];
                }
                if (home > away && (home + away >= line)) {
                    probs[3] += scoreGrid[home][away];
                    probs[5] += scoreGrid[home][away];
                }
                if ((home < away) && (home + away >= line)) {
                    probs[1] += scoreGrid[home][away];
                    probs[6] += scoreGrid[home][away];
                }
                if (home > away && !(home + away < line)) {
                    probs[7] += scoreGrid[home][away];
                    probs[12] += scoreGrid[home][away];
                    probs[14] += scoreGrid[home][away];
                }
                if (home == away && !(home + away < line)) {
                    probs[9] += scoreGrid[home][away];
                    probs[11] += scoreGrid[home][away];
                    probs[13] += scoreGrid[home][away];
                }
                if ((home < away) && !(home + away < line)) {
                    probs[8] += scoreGrid[home][away];
                    probs[10] += scoreGrid[home][away];
                    probs[15] += scoreGrid[home][away];
                }
            }
        }

        probs[0] *= halfTimeResult[0];
        probs[1] *= halfTimeResult[0];
        probs[2] *= halfTimeResult[2];
        probs[3] *= halfTimeResult[2];
        probs[4] *= halfTimeResult[1];
        probs[5] *= halfTimeResult[1];
        probs[6] *= halfTimeResult[1];
        probs[7] *= halfTimeResult[0];
        probs[8] *= halfTimeResult[2];
        probs[9] *= halfTimeResult[0];
        probs[10] *= halfTimeResult[0];
        probs[11] *= halfTimeResult[2];
        probs[12] *= halfTimeResult[2];
        probs[13] *= halfTimeResult[1];
        probs[14] *= halfTimeResult[1];
        probs[15] *= halfTimeResult[1];
        return probs;
    }

    private double[] generateDoubleChanceAndOuProbs(double[][] scoreGrid, double line) {
        double[] probs = new double[6];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home >= away && (home + away > line))
                    probs[0] += scoreGrid[home][away];
                if (home <= away && (home + away > line))
                    probs[1] += scoreGrid[home][away];
                if ((home > away || home < away) && (home + away > line))
                    probs[2] += scoreGrid[home][away];
                if (home >= away && !(home + away > line))
                    probs[3] += scoreGrid[home][away];
                if (home <= away && !(home + away > line))
                    probs[4] += scoreGrid[home][away];
                if ((home > away || home < away) && !(home + away > line))
                    probs[5] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateTeamToWinAndOneTeamToScoreOUProbs(double[][] scoreGrid, double line) {
        double[] probs = new double[12];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away && (home + away >= line) && (!(home > 0 && away > 0)))// HOME
                                                                                      // win,
                                                                                      // NOT
                                                                                      // both
                                                                                      // score,
                                                                                      // x
                                                                                      // or
                    probs[0] += scoreGrid[home][away];
                if (home < away && (home + away >= line) && (!(home > 0 && away > 0)))// AWAY
                                                                                      // win,
                                                                                      // NOT
                                                                                      // both
                                                                                      // score,
                                                                                      // x
                                                                                      // or
                                                                                      // more
                                                                                      // goals
                    probs[1] += scoreGrid[home][away];
                if (home > away && (home + away >= line) && (home > 0) && (away > 0))// home
                                                                                     // win,
                                                                                     // both
                                                                                     // score,
                                                                                     // x
                                                                                     // or
                                                                                     // more
                                                                                     // goals
                    probs[2] += scoreGrid[home][away];
                if (home < away && (home + away >= line) && (home > 0) && (away > 0))// away
                                                                                     // win,
                                                                                     // both
                                                                                     // score,
                                                                                     // x
                                                                                     // or
                                                                                     // more
                                                                                     // goals
                    probs[3] += scoreGrid[home][away];
                if (home == away && (home + away >= line) && (home > 0) && (away > 0))// draw,
                                                                                      // both
                                                                                      // score,
                                                                                      // x
                                                                                      // or
                                                                                      // more
                                                                                      // goals
                    probs[4] += scoreGrid[home][away];
                if (home == away && (home + away >= line) && (!(home > 0 && away > 0)))// draw,not
                                                                                       // both
                                                                                       // score,
                                                                                       // x
                                                                                       // or
                                                                                       // more
                                                                                       // goals
                    probs[5] += scoreGrid[home][away];


                if (home > away && (home + away < line) && (!(home > 0 && away > 0)))
                    probs[6] += scoreGrid[home][away];
                if (home < away && (home + away < line) && (!(home > 0 && away > 0)))
                    probs[7] += scoreGrid[home][away];
                if (home > away && (home + away < line) && (home > 0 && away > 0))
                    probs[8] += scoreGrid[home][away];
                if (home < away && (home + away < line) && (home > 0 && away > 0))
                    probs[9] += scoreGrid[home][away];
                if (home == away && (home + away < line) && (home > 0 && away > 0))
                    probs[10] += scoreGrid[home][away];
                if (home == away && (home + away < line) && (!(home > 0 && away > 0)))
                    probs[11] += scoreGrid[home][away];
            }
        }
        return probs;
    }

    private double[] generateFirstHalfComboOdds(double[][] scoreGrid) {
        double[] probs = new double[8];
        for (int home = 0; home < scoreGrid.length; home++) {
            for (int away = 0; away < scoreGrid.length; away++) {
                if (home > away) {
                    if (home > 0 && away > 0) {
                        probs[0] += scoreGrid[home][away];
                    } else {
                        probs[3] += scoreGrid[home][away];
                    }
                    if (home == 3 || home == 2) {
                        probs[6] += scoreGrid[home][away];
                    }
                } else if (home < away) {
                    if (home > 0 && away > 0) {
                        probs[1] += scoreGrid[home][away];
                    } else {
                        probs[4] += scoreGrid[home][away];
                    }
                    if (away == 3 || away == 2) {
                        probs[7] += scoreGrid[home][away];
                    }
                } else if (home == away) {
                    if (home > 0 && away > 0) {
                        probs[2] += scoreGrid[home][away];
                    } else {
                        probs[5] += scoreGrid[home][away];
                    }
                }
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

    private void generateFirstHalfSecondHalfScoreSelections() {
        List<String> scores = CollectionsUtil.newList();
        int currentScoreIndex = 0;
        for (int firstHalfGoalsA = 0; firstHalfGoalsA <= maxFirstHalfGoals; firstHalfGoalsA++) {
            for (int firstHalfGoalsB = 0; firstHalfGoalsB <= maxFirstHalfGoals; firstHalfGoalsB++) {
                if (firstHalfGoalsA + firstHalfGoalsB <= maxFirstHalfGoals) {
                    for (int secondHalfGoalsA = 0; secondHalfGoalsA <= maxSecondHalfGoals; secondHalfGoalsA++) {
                        for (int secondHalfGoalsB = 0; secondHalfGoalsB <= maxSecondHalfGoals; secondHalfGoalsB++) {
                            if (secondHalfGoalsA + secondHalfGoalsB <= maxSecondHalfGoals
                                            && firstHalfGoalsA + firstHalfGoalsB + secondHalfGoalsA
                                                            + secondHalfGoalsB <= maxNormatlTimeGoals) {
                                scores.add(firstHalfGoalsA + "-" + firstHalfGoalsB + "/" + secondHalfGoalsA + "-"
                                                + secondHalfGoalsB);
                                putIndexInMap(firstHalfSecondHalfScoreSelectionIndexMap, firstHalfGoalsA,
                                                firstHalfGoalsB, secondHalfGoalsA, secondHalfGoalsB, currentScoreIndex);
                                currentScoreIndex++;
                            }
                        }
                    }
                }
            }
        }
        firstHalfSecondHalfScoreSelections = scores.toArray(new String[scores.size()]);;
    }

    private void generateFirstHalfNormalTimeScoreSelections() {
        List<String> scores = CollectionsUtil.newList();
        int currentScoreIndex = 0;
        for (int firstHalfGoalsA = 0; firstHalfGoalsA <= maxFirstHalfGoals; firstHalfGoalsA++) {
            for (int firstHalfGoalsB = 0; firstHalfGoalsB <= maxFirstHalfGoals; firstHalfGoalsB++) {
                if (firstHalfGoalsA + firstHalfGoalsB <= maxFirstHalfGoals) {
                    for (int secondHalfGoalsA = 0; secondHalfGoalsA <= maxSecondHalfGoals; secondHalfGoalsA++) {
                        for (int secondHalfGoalsB = 0; secondHalfGoalsB <= maxSecondHalfGoals; secondHalfGoalsB++) {
                            if (secondHalfGoalsA + secondHalfGoalsB <= maxSecondHalfGoals
                                            && firstHalfGoalsA + firstHalfGoalsB + secondHalfGoalsA
                                                            + secondHalfGoalsB <= maxNormatlTimeGoals) {
                                int normalTimeGoalsA = firstHalfGoalsA + secondHalfGoalsA;
                                int normalTimeGoalsB = firstHalfGoalsB + secondHalfGoalsB;
                                scores.add(firstHalfGoalsA + "-" + firstHalfGoalsB + "/" + normalTimeGoalsA + "-"
                                                + normalTimeGoalsB);
                                putIndexInMap(firstHalfNormalTimeScoreSelectionIndexMap, firstHalfGoalsA,
                                                firstHalfGoalsB, normalTimeGoalsA, normalTimeGoalsB, currentScoreIndex);
                                currentScoreIndex++;
                            }
                        }
                    }
                }
            }
        }
        firstHalNormalTimeScoreSelections = scores.toArray(new String[scores.size()]);
    }

    /**
     * This methods puts the given valueToStore in the provided fourDMap, initialising inner maps if required 4D maps :
     * [akey -> [bkey -> [ckey -> [dkey -> value]]]]
     */

    private void putIndexInMap(Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> fourDMap, int aKey,
                    int bKey, int cKey, int dKey, int valueToStore) {
        Map<Integer, Map<Integer, Map<Integer, Integer>>> threeDInnerMap = fourDMap.get(aKey);
        if (threeDInnerMap == null) {
            threeDInnerMap = CollectionsUtil.newMap();
            fourDMap.put(aKey, threeDInnerMap);
        }
        Map<Integer, Map<Integer, Integer>> twoDInnerMap = threeDInnerMap.get(bKey);
        if (twoDInnerMap == null) {
            twoDInnerMap = CollectionsUtil.newMap();
            threeDInnerMap.put(bKey, twoDInnerMap);
        }
        Map<Integer, Integer> lastInnerMap = twoDInnerMap.get(cKey);
        if (lastInnerMap == null) {
            lastInnerMap = CollectionsUtil.newMap();
            twoDInnerMap.put(cKey, lastInnerMap);
        }
        lastInnerMap.put(dKey, valueToStore);
    }

    /**
     * This methods gets the value stored in the provided fourDMap, returning null if this could not be found 4D maps :
     * [akey -> [bkey -> [ckey -> [dkey -> value]]]]
     */
    private Integer getIndexFromMap(Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> fourDMap, int aKey,
                    int bKey, int cKey, int dKey) {
        Map<Integer, Map<Integer, Map<Integer, Integer>>> threeDInnerMap = fourDMap.get(aKey);
        if (threeDInnerMap == null) {
            return null;
        }
        Map<Integer, Map<Integer, Integer>> twoDInnerMap = threeDInnerMap.get(bKey);
        if (twoDInnerMap == null) {
            return null;
        }
        Map<Integer, Integer> lastInnerMap = twoDInnerMap.get(cKey);
        if (lastInnerMap == null) {
            return null;
        }
        return lastInnerMap.get(dKey);
    }

    private Integer getFirstHalfSecondHalfScoreSelectionIndex(int aKey, int bKey, int cKey, int dKey) {
        return getIndexFromMap(firstHalfSecondHalfScoreSelectionIndexMap, aKey, bKey, cKey, dKey);
    }

    private Integer getFirstHalfNormalTimeScoreSelectionIndex(int aKey, int bKey, int cKey, int dKey) {
        return getIndexFromMap(firstHalfNormalTimeScoreSelectionIndexMap, aKey, bKey, cKey, dKey);
    }

    private void incrementHighestHalf(ThreeWayStatistic threeWayStatistic, int firstHalfValue, int secondHalfValue) {
        if (firstHalfValue > secondHalfValue)
            threeWayStatistic.increment(TeamId.A);
        else if (firstHalfValue < secondHalfValue)
            threeWayStatistic.increment(TeamId.B);
        else
            threeWayStatistic.increment(TeamId.UNKNOWN);
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }
}
