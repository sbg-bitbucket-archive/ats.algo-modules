package ats.algo.sport.tennis;

import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;

public class TennisMatchMarketsFactory extends MarketsFactory {
    private TwoWayStatistic matchWinner;
    private NWayStatistic bothTeamToWinASet;
    private TwoWayStatistic bothTeamToWinSet;
    private CorrectScoreStatistic correctSetScore;
    private CorrectScoreStatistic correctSetScoreafter2;
    private CorrectScoreStatistic correctSetScoreafter4;
    private CorrectScoreStatistic correctSetScoreafter6;

    private CorrectScoreStatistic correctNextSetScoreafter2;
    private CorrectScoreStatistic correctNextSetScoreafter4;
    private CorrectScoreStatistic correctNextSetScoreafter6;
    private TotalStatistic gamesTotal;
    private TotalStatistic setsTotal;
    // private ThreeWayOverUnder gamesTotalThreeWay;
    private TotalStatistic gamesTotalA;
    private TotalStatistic gamesTotalB;
    private HandicapStatistic gamesHandicap;
    private HandicapStatistic alternativeGamesHandicap1;
    private HandicapStatistic alternativeGamesHandicap2;
    private HandicapStatistic alternativeGamesHandicap3;
    private HandicapStatistic alternativeGamesHandicap4;
    private HandicapStatistic alternativeGamesHandicap5;
    private HandicapStatistic alternativeGamesHandicap6;
    private HandicapStatistic alternativeSetHandicap;
    private TwoWayStatistic tieBreakPlayed;
    private NWayStatistic noSetsPlayed;
    private TwoWayStatistic winASetA;
    private TwoWayStatistic winASetB;
    private TwoWayStatistic raceToFiveGames;
    private TwoWayStatistic thisGameWinner;
    private TwoWayStatistic nextGameWinner;
    private TwoWayStatistic gamePlusTwoWinner;
    private TwoWayStatistic thisGameToDeuce;
    private TwoWayStatistic nextGameToDeuce;
    private TwoWayStatistic gamePlusTwoToDeuce;
    private TwoWayStatistic thisGamePointPlusTwo;
    private TwoWayStatistic thisGamePointPlusThree;
    private TwoWayStatistic nextGamePointOne;
    private TwoWayStatistic nextGamePointTwo;
    private TwoWayStatistic tieBreakWinner;
    private NWayStatistic tieBreakScore;
    private CorrectScoreStatistic thisSetGamesScore;
    private CorrectScoreStatistic thisSetGamesScoreCombined;
    private CorrectScoreStatistic nextSetGamesScoreCombined;

    private TwoWayStatistic thisSetWinner;
    private TwoWayStatistic thisGameNextPoint;

    private TwoWayStatistic playerAToWinSetXGameYAndXGameYPlusOne;

    private TwoWayStatistic playerBToWinSetXGameYAndXGameYPlusOne;

    private HandicapStatistic setGamesHandicap;

    private HandicapStatistic setsHandicap;
    private TwoWayStatistic matchGameOddEven;
    private TwoWayStatistic setGameOddEven;
    private TotalStatistic currentSetGamesTotal;
    private TotalStatistic currentSetGamesA;
    private TotalStatistic currentSetGamesB;
    private TotalStatistic nextSetGames;
    private TwoWayStatistic nextSetGamesOddEven;
    private TwoWayStatistic willBeTieBreakInCurrentSet;
    private TwoWayStatistic willBeTieBreakInNextSet;
    private TotalStatistic tiebreakOrStandardGameTotalPoints;
    private TotalStatistic totalTieBreaks;
    private NWayStatistic teamBreakPointFirst;
    private NWayStatistic firstServingBreakInSetX;
    private NWayStatistic gameOfFirstBreakServicePlayerAInSetX;
    private NWayStatistic gameOfFirstBreakServicePlayerBInSetX;
    private String[] teamBreakPointFirstSelectionsList = {"A", "B", "None"};
    private String[] firstServingBreakInSetXSelectionsList =
                    {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "None"};
    private String[] gameOfFirstBreakServiceSelectionsList =
                    {"Games 1-4 ", "Games 5-8 ", "After Game 8 ", "Player doesn't break serve "};
    // private TotalStatistic tieBreakGamesTotalPoints;
    // private CorrectScoreStatistic currentSetScoreAfterXGames; // unsure
    // market
    private NWayStatistic setTotalGames3Way;
    private NWayStatistic correctScoreSetGroupsAnyPlayer;
    private NWayStatistic playerAExactGamesWonMargin;
    private NWayStatistic playerBExactGamesWonMargin;
    private NWayStatistic setXgameYCorrectScore;

    // private TwoWayStatistic alternateTotalSet2;
    // private TwoWayStatistic alternateTotalSet3;
    // private TwoWayStatistic alternateTotalSet4;

    private String[] playerExactGamesWonMarginListA = {"Player A 0 or Negative", "Player A 1", "Player A 2",
            "Player A 3", "Player A 4", "Player A 5", "Player A 6", "Player A 7", "Player A 8", "Player A 9",
            "Player A 10", "Player A 11", "Player A 12 or More"};
    private String[] playerExactGamesWonMarginListB = {"Player B 0 or Negative", "Player B 1", "Player B 2",
            "Player B 3", "Player B 4", "Player B 5", "Player B 6", "Player B 7", "Player B 8", "Player B 9",
            "Player B 10", "Player B 11", "Player B 12 or More"};
    private String[] correctScoreSetGroupsAnyPlayerList = {"6-0 or 6-1", "6-2 or 6-3", "6-4 or 7-5 or 7-6"};
    private String[] correctScoreSetGroupsAnyPlayerListFinal =
                    {"6-0 or 6-1", "6-2 or 6-3", "6-4, 7-5 or 7-6", "Any other result"};
    private String[] setTotalGames3WayList = {"Under 8.5", "9 or 10", "Over 10.5"};
    private String[] setXgameYCorrectScoreList = {"A-0", "A-15", "A-30", "A-AD", "B-0", "B-15", "B-30", "B-AD"};

    private String[] playerTieBreakCorrectScoreList = {"Player A 7-0", "Player A 7-1", "Player A 7-2", "Player A 7-3",
            "Player A 7-4", "Player A 7-5", "Player B 7-0", "Player B 7-1", "Player B 7-2", "Player B 7-3",
            "Player B 7-4", "Player B 7-5", "Any other score"};
    private int nSetsInMatch;
    private int minToWin = 2;
    int setX = 0;
    int gameY = 0;
    boolean setXgamYPossible = false;
    boolean ppbMarkets = false;
    int setsInMatch = 0;
    private static final String TENNIS_MARKETS_TO_BE_CREATED_FOR_CLIENT = "algo.tennis.marketsToBeCreatedForClient";

    public TennisMatchMarketsFactory(int nSetsInMatch, TennisMatchState matchState) {
        super();
        this.setCorrectScoreDisplayHasTeamId(true);
        this.setsInMatch = matchState.getNoSetsInMatch();
        String clients = System.getProperty(TENNIS_MARKETS_TO_BE_CREATED_FOR_CLIENT);
        if (clients != null)
            if (clients.toLowerCase().equals("paddypower"))
                ppbMarkets = true;

        boolean inFinalSetTieBreak =
                        matchState.isInFinalSet() && (matchState.isInTieBreak() || matchState.isInSuperTieBreak());
        boolean psMarkets = false;
        boolean iptlMatch = ((TennisMatchFormat) matchState.getMatchFormat()).getTournamentLevel()
                        .equals(TournamentLevel.IPTL);
        int setNo = matchState.getSetNo();
        int gameNo = matchState.getGameNo();
        int pointNo = matchState.getPointNo();

        int maxSetScore = nSetsInMatch / 2 + 1;
        int maxGamesInASet = 30;
        this.nSetsInMatch = nSetsInMatch;
        if (nSetsInMatch == 5)
            minToWin = 3;
        /*
         * Set the characteristics of each market
         */
        matchWinner = new TwoWayStatistic("FT:ML", "Match Betting", true, "M", "A", "B");
        String[] matchNbothWinASet = {"Player A wins the match and both players win a set",
                "Player A wins the match and not both players win a set",
                "Player B wins the match and both players win a set",
                "Player B wins the match and not both players win a set"};
        bothTeamToWinASet = new NWayStatistic("FT:BTW1S", "Match and Both Players to win a Set", true, "M",
                        matchNbothWinASet);

        correctSetScore = new CorrectScoreStatistic("FT:CS", "Set Betting", !iptlMatch, "M", maxSetScore + 1);

        // allow for long final set
        gamesTotal = new TotalStatistic("FT:OU", "Total Games", !inFinalSetTieBreak, "M", 200);
        setsTotal = new TotalStatistic("FT:S:OU", "Total Sets", true, "M", 100);

        gamesTotalA = new TotalStatistic("FT:OU:A", "Player A Total Games", true, "M", 100);
        gamesTotalB = new TotalStatistic("FT:OU:B", "Player B Total Games", true, "M", 100);
        gamesHandicap = new HandicapStatistic("FT:SPRD", "Game Handicap", !inFinalSetTieBreak, "M",
                        nSetsInMatch * 12 + 1);
        setsHandicap = new HandicapStatistic("FT:S:SPRD", "Sets Handicap", true, "M", nSetsInMatch * 2);

        tieBreakPlayed = new TwoWayStatistic("FT:TBIM", "Tie Break In Match",
                        !matchState.isTieBreakAlreadyPlayedInMatch(), "M", "Yes", "No");

        String[] selections = new String[nSetsInMatch - maxSetScore + 1];
        for (int i = 0; i < nSetsInMatch - maxSetScore + 1; i++) {
            selections[i] = (i + maxSetScore) + " sets";
        }
        // selections[0] = "2 sets";
        // selections[1] = "3 sets";
        // selections[2] = "4 sets";
        // selections[3] = "5 sets";
        noSetsPlayed = new NWayStatistic("FT:NUMSET", "No of Sets to be Played in Match", !iptlMatch, "M", selections);
        // if (nSetsInMatch == 3) {
        // String[] selections = new String[2];
        // selections[0] = "2 sets";
        // selections[1] = "3 sets";
        // noSetsPlayed = new NWayStatistic("FT:NUMSET", "No of Sets to be
        // Played in Match", !iptlMatch, "M",
        // selections);
        // }
        //
        // if (nSetsInMatch == 5) {
        // String[] selections = new String[3];
        // selections[0] = "3 sets";
        // selections[1] = "4 sets";
        // selections[2] = "5 sets";
        // noSetsPlayed = new NWayStatistic("FT:NUMSET", "No of Sets to be
        // Played in Match", !iptlMatch, "M",
        // selections);
        // }

        winASetA = new TwoWayStatistic("FT:W1S:A", "A to win at least one set", !matchState.isAlreadyWonASetA(), "M",
                        "Yes", "No");

        winASetB = new TwoWayStatistic("FT:W1S:B", "B to win at least one set", !matchState.isAlreadyWonASetB(), "M",
                        "Yes", "No");

        String thisSetSequenceId = String.format("S%d", setNo);
        String marketDescription = String.format("First player to win 5 games in set %d", setNo);
        raceToFiveGames = new TwoWayStatistic("FT:PW5G", marketDescription,
                        !matchState.isAlreadyOver4GamesInCurrentSet(), thisSetSequenceId, "A", "B");

        String thisGameSequenceId = String.format("%s.%d", thisSetSequenceId, gameNo);
        Boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo);
        // changed for displaying without player serving chosen
        // Boolean generateMarket = matchState.isGameMayBePlayed(gameNo);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo);
        thisGameWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, thisGameSequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d to go to Deuce", setNo, gameNo);

        boolean thisGameDeuced = matchState.getPointNo() > 6;
        thisGameToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, ppbMarkets && !thisGameDeuced,
                        thisGameSequenceId, "Yes", "No");

        // generateMarket = !matchState.isPreMatch();
        generateMarket = matchState.isGameMayBePlayed(gameNo);
        String sequenceId = String.format("S%d", setNo);
        thisSetWinner = new TwoWayStatistic("P:ML", "Set " + setNo + " Winner", true, sequenceId, "A", "B");
        thisSetGamesScore = new CorrectScoreStatistic("P:CS", "Set " + setNo + " Correct Score", generateMarket,
                        sequenceId, maxGamesInASet + 1, "Player ");

        thisSetGamesScoreCombined = new CorrectScoreStatistic("P:CSCB", "Set " + setNo + " Correct Score Combined",
                        ppbMarkets, sequenceId, maxGamesInASet + 1);

        nextSetGamesScoreCombined = new CorrectScoreStatistic("NP:CSCB",
                        "Set " + (setNo + 1) + " (Next Set) Correct Score Combined", matchState.isMatchCompleted(),
                        sequenceId, maxGamesInASet + 1);

        String nextGameSequenceId = String.format("%s.%d", thisSetSequenceId, gameNo + 1);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo + 1);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo + 1);
        nextGameWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, nextGameSequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d to go to Deuce", setNo, gameNo + 1);
        nextGameToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, generateMarket, nextGameSequenceId, "Yes",
                        "No");

        sequenceId = String.format("%s.%d", thisSetSequenceId, gameNo + 2);
        marketDescription = String.format("Set %d Game %d Winner", setNo, gameNo + 2);
        gamePlusTwoWinner = new TwoWayStatistic("G:ML", marketDescription, generateMarket, sequenceId, "A", "B");

        marketDescription = String.format("Set %d Game %d to go to Deuce", setNo, gameNo + 2);
        gamePlusTwoToDeuce = new TwoWayStatistic("G:DEUCE", marketDescription, generateMarket, sequenceId, "Yes", "No");

        sequenceId = String.format("%s.%d", thisGameSequenceId, pointNo + 1);
        generateMarket = !matchState.isPreMatch() && matchState.isPointMayBePlayed(pointNo + 1);
        marketDescription = String.format("Set %d Game %d Point %d Winner", setNo, gameNo, pointNo + 1);
        thisGamePointPlusTwo = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");
        sequenceId = String.format("%s.%d", thisGameSequenceId, pointNo);

        generateMarket = !matchState.isPreMatch() && matchState.isPointMayBePlayed(pointNo);
        marketDescription = String.format(" Next Point Set %d Game %d Point %d Winner", setNo, gameNo, pointNo);
        thisGameNextPoint = new TwoWayStatistic("G:PW", marketDescription, ppbMarkets, sequenceId, "A", "B");

        setX = matchState.getSetNo();
        gameY = matchState.getGameNo();
        setXgamYPossible = matchState.isGameMayBePlayed(gameY + 1);
        sequenceId = "S" + String.format("%s.%d", setX, gameY);
        playerAToWinSetXGameYAndXGameYPlusOne =
                        new TwoWayStatistic("SG:AA",
                                        "Player A to win set " + setX + " Game " + gameY + " & to win set " + setX
                                                        + " Game " + (gameY + 1),
                                        setXgamYPossible, sequenceId, "Yes", "No");
        playerBToWinSetXGameYAndXGameYPlusOne =
                        new TwoWayStatistic("SG:BB",
                                        "Player B to win set " + setX + " Game " + gameY + " & to win set " + setX
                                                        + " Game " + (gameY + 1),
                                        setXgamYPossible, sequenceId, "Yes", "No");

        sequenceId = String.format("%s.%d", thisGameSequenceId, pointNo + 2);
        generateMarket = !matchState.isPreMatch() && matchState.isPointMayBePlayed(pointNo + 2);
        marketDescription = String.format("Set %d Game %d Point %d Winner", setNo, gameNo, pointNo + 2);
        thisGamePointPlusThree = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = String.format("S%d.%d.%d", setNo, gameNo + 1, 1);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo + 1);
        marketDescription = String.format("Set %d Game %d Point %d Winner", setNo, gameNo + 1, 1);
        nextGamePointOne = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = String.format("S%d.%d.%d", setNo, gameNo + 1, 2);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo + 1);
        marketDescription = String.format("Set %d Game %d Point %d Winner", setNo, gameNo + 1, 2);
        nextGamePointTwo = new TwoWayStatistic("G:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = String.format("S%d.%d", setNo, gameNo);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo);
        marketDescription = String.format("Set %d Game %d Correct Score", setNo, gameNo);

        sequenceId = String.format("S%d", setNo);
        generateMarket = matchState.isInTieBreak();
        marketDescription = String.format("Set %d Tie break Winner", setNo);

        String tbSequenceId = String.format("S%d", setNo); // tie break sequence
                                                           // id hardcoded.
        tieBreakWinner = new TwoWayStatistic("P:TBML", marketDescription, generateMarket, tbSequenceId, "A", "B");
        marketDescription = String.format("Set %d Tie break correct score", setNo);
        tieBreakScore = new NWayStatistic("P:TBCS", marketDescription, ppbMarkets, tbSequenceId,
                        playerTieBreakCorrectScoreList);
        marketDescription = "Match Game Odd Even";
        matchGameOddEven = new TwoWayStatistic("FT:G:OE", marketDescription, psMarkets, "M", "A", "B");
        setGameOddEven = new TwoWayStatistic("S:G:OE", "Current Set Game Odd Even", psMarkets, "M", "A", "B");

        sequenceId = String.format("S%d", setNo);
        currentSetGamesTotal = new TotalStatistic("S:OU", "Current Set Total Games", ppbMarkets, sequenceId, 100);
        currentSetGamesA = new TotalStatistic("S:OU:A", "Current Set Total Games A", ppbMarkets, sequenceId, 100);
        currentSetGamesB = new TotalStatistic("S:OU:B", "Current Set Total Games B", ppbMarkets, sequenceId, 100);
        // FIXME: CHECK WHEN SHOULD CREAT THIS MARKET
        if (matchState.isBreakPointStillPossible())
            teamBreakPointFirst = new NWayStatistic("FT:BF", "Team Break Point First", ppbMarkets, "M",
                            teamBreakPointFirstSelectionsList);

        boolean creatfirstServingBreakInSetX = !matchState.isServingBrokenInSet(setNo);
        boolean creatfirstServingBreakAInSetX = !matchState.isServingBrokenInSetA(setNo);
        boolean creatfirstServingBreakBInSetX = !matchState.isServingBrokenInSetB(setNo);

        firstServingBreakInSetX = new NWayStatistic("FT:FSB", "Team Break Point First",
                        creatfirstServingBreakInSetX && ppbMarkets, sequenceId, firstServingBreakInSetXSelectionsList);

        gameOfFirstBreakServicePlayerAInSetX = new NWayStatistic("FT:FSB:A",
                        "Player A Game Of First Break Service In " + setNo + " Set",
                        creatfirstServingBreakAInSetX && ppbMarkets, sequenceId, gameOfFirstBreakServiceSelectionsList);

        gameOfFirstBreakServicePlayerBInSetX = new NWayStatistic("FT:FSB:B",
                        "Player B Game Of First Break Service In " + setNo + " Set",
                        creatfirstServingBreakBInSetX && ppbMarkets, sequenceId, gameOfFirstBreakServiceSelectionsList);

        nextSetGames = new TotalStatistic("FT:NS:OU", "Next Set Games", false, "M", 100);
        nextSetGamesOddEven = new TwoWayStatistic("FT:NS:OE", "Next Set Game Odd Even", psMarkets, "M", "A", "B");
        willBeTieBreakInCurrentSet =
                        new TwoWayStatistic("FT:S:TB", "Will Be Tie Break In Current Set", psMarkets, "M", "A", "B");
        willBeTieBreakInNextSet =
                        new TwoWayStatistic("FT:NS:TB", "Will Be Tie Break In Next Set", psMarkets, "M", "A", "B");
        // tieBreakGamesTotalPoints = new TotalStatistic("FT:G:OU", "Tie Break
        // Game Total Points", psMarkets, "M", 20);
        if (matchState.isInTieBreak())
            marketDescription = String.format("Tiebreak Game Total Points", setNo, gameNo);
        else
            marketDescription = String.format("Standard Game Total Points", setNo, gameNo);

        sequenceId = String.format("S%d.%d", setNo, gameNo);
        tiebreakOrStandardGameTotalPoints = new TotalStatistic("FT:G:OU", marketDescription, psMarkets, sequenceId, 30);
        totalTieBreaks = new TotalStatistic("FT:TB:OU", "Total Tie Breaks", ppbMarkets, "M", 60);

        sequenceId = String.format("S%d", setNo);
        marketDescription = String.format("Set %d Game Handicap", setNo);
        setGamesHandicap = new HandicapStatistic("P:SPRD", marketDescription, ppbMarkets, sequenceId, 13);

        /**
         * Robert attempt
         */
        marketDescription = String.format("Set %d Total Games 3 Way", setNo);
        setTotalGames3Way =
                        new NWayStatistic("S:OU3", marketDescription, ppbMarkets, sequenceId, setTotalGames3WayList);
        marketDescription = String.format("Set %d Correct Score Groups Any Player", setNo);
        if (!matchState.isTieBreakinFinalSet() && matchState.isInFinalSet())
            correctScoreSetGroupsAnyPlayer = new NWayStatistic("P:CSG", marketDescription, ppbMarkets, sequenceId,
                            correctScoreSetGroupsAnyPlayerListFinal);
        else
            correctScoreSetGroupsAnyPlayer = new NWayStatistic("P:CSG", marketDescription, ppbMarkets, sequenceId,
                            correctScoreSetGroupsAnyPlayerList);

        marketDescription = "Player A Exact Games Won Margin";
        playerAExactGamesWonMargin = new NWayStatistic("FT:WM:A", marketDescription, ppbMarkets, "M",
                        playerExactGamesWonMarginListA);
        marketDescription = "Player B Exact Games Won Margin";
        playerBExactGamesWonMargin = new NWayStatistic("FT:WM:B", marketDescription, ppbMarkets, "M",
                        playerExactGamesWonMarginListB);
        bothTeamToWinSet = new TwoWayStatistic("FT:BPTW", "Both player to win a set", ppbMarkets, "M", "Yes", "No");

        // if (setsInMatch == 3)
        // alternateTotalSet2 = new TwoWayStatistic("FT:ALT:S:OU:2",
        // "Alternative Total Sets 2.5",
        // ppbMarkets && (matchState.getSetsA() + matchState.getSetsB() < (3 -
        // 1)), "M", "Over",
        // "Under");
        // else {
        // alternateTotalSet3 = new TwoWayStatistic("FT:ALT:S:OU:3",
        // "Alternative Total Sets 3.5",
        // ppbMarkets && (matchState.getSetsA() + matchState.getSetsB() < 3),
        // "M", "Over", "Under");
        // alternateTotalSet4 = new TwoWayStatistic("FT:ALT:S:OU:4",
        // "Alternative Total Sets 4.5",
        // ppbMarkets && (matchState.getSetsA() + matchState.getSetsB() < 4),
        // "M", "Over", "Under");
        // }

        generateMarket = gameNo < 3;
        marketDescription = String.format("Set %d Correct Score after 2", setNo);
        correctSetScoreafter2 = new CorrectScoreStatistic("P:CS2", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 2);
        generateMarket = gameNo < 5;
        marketDescription = String.format("Set %d Correct Score after 4", setNo);
        correctSetScoreafter4 = new CorrectScoreStatistic("P:CS4", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 4);
        generateMarket = gameNo < 7;
        marketDescription = String.format("Set %d Correct Score after 6", setNo);
        correctSetScoreafter6 = new CorrectScoreStatistic("P:CS6", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 6);

        sequenceId = String.format("S%d", setNo + 1);
        generateMarket = setNo < matchState.getNoSetsInMatch();
        marketDescription = String.format("Set %d Correct Score after 2", setNo + 1);
        correctNextSetScoreafter2 = new CorrectScoreStatistic("P:CS2", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 2);
        marketDescription = String.format("Set %d Correct Score after 4", setNo + 1);
        correctNextSetScoreafter4 = new CorrectScoreStatistic("P:CS4", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 4);
        marketDescription = String.format("Set %d Correct Score after 6", setNo + 1);
        correctNextSetScoreafter6 = new CorrectScoreStatistic("P:CS6", marketDescription, ppbMarkets && generateMarket,
                        sequenceId, 6);
        sequenceId = String.format("S%d.%d", setNo, gameNo);
        generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(gameNo);
        marketDescription = String.format("Set %d Game %d Correct Score ", setNo, gameNo);
        setXgameYCorrectScore = new NWayStatistic("G:CS", marketDescription, generateMarket, sequenceId,
                        setXgameYCorrectScoreList);

        alternativeGamesHandicap1 = new HandicapStatistic("FT:ASPRD1", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
        alternativeGamesHandicap2 = new HandicapStatistic("FT:ASPRD2", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
        alternativeGamesHandicap3 = new HandicapStatistic("FT:ASPRD3", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
        alternativeGamesHandicap4 = new HandicapStatistic("FT:ASPRD4", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
        alternativeGamesHandicap5 = new HandicapStatistic("FT:ASPRD5", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
        alternativeGamesHandicap6 = new HandicapStatistic("FT:ASPRD6", "Alternative Game Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);

        alternativeSetHandicap = new HandicapStatistic("FT:S:ASPRD", "Alternative Set Handicap",
                        ppbMarkets && !inFinalSetTieBreak, "M", nSetsInMatch * 12 + 1);
    }

    @Override
    public void addDerivedMarkets(Markets markets, MatchState matchState, MatchParams matchParams) {

        if (!((TennisMatchState) matchState).isMatchCompleted()
                        && (!((TennisMatchState) matchState).isPreMatch() || ppbMarkets)) {//
            String sequenceId = String.format("S%d", ((TennisMatchState) matchState).getSetNo());
            String string = "P:ML_" + sequenceId;
            Market mlSourceMarket = markets.get("FT:ML");// win match
            Market setSourceMarket = markets.getMarketForFullKey(string);// win
                                                                         // match

            Map<String, Double> mlMap = mlSourceMarket.getSelectionsProbs();
            Map<String, Double> setMap = setSourceMarket.getSelectionsProbs();

            double probWA = mlMap.get("A") / (mlMap.get("A") + mlMap.get("B"));
            double probWB = mlMap.get("B") / (mlMap.get("A") + mlMap.get("B"));

            probWA = probWA * (setMap.get("A"));
            probWB = probWB * (setMap.get("B"));

            Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:W1SML", "M",
                            "Win 1st Set and Win Match");
            market.setIsValid(true);
            market.put("A Yes", probWA);
            market.put("B Yes", probWB);
            markets.addMarketWithShortKey(market);

            probWA = probWA * ((1 - setMap.get("A")));
            probWB = probWB * ((1 - setMap.get("B")));

            Market market2 = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:L1SML", "M",
                            "Lose 1st Set and Win Match");
            market2.setIsValid(true);
            market2.put("A Yes", probWA);
            market2.put("B Yes", probWB);
            markets.addMarketWithShortKey(market2);

        }

        if (ppbMarkets) {
            /** alternative lines for ppb **/
            Market setsOU = markets.get("FT:S:OU");// ORGINAL TOTAL SETS MARKET
            double line = Double.parseDouble(setsOU.getLineId());

            if (setsInMatch == 3) {
                // no alternative line
            } else {
                Market market = new Market(MarketCategory.OVUN, MarketGroup.GOALS, "FT:ALT:S:OU", "M",
                                "Alternative Lines");
                if (line == 3.5) {
                    market.setLineId("4.5");
                    market.setIsValid(true);
                    double yesProb = setsOU.getMarketForLineId("4.5").get("Over");
                    market.put("Over", yesProb);
                    market.put("Under", 1 - yesProb);
                } else if (line == 4.5) {
                    market.setLineId("3.5");
                    market.setIsValid(true);
                    double yesProb = setsOU.getMarketForLineId("3.5").get("Over");
                    market.put("Over", yesProb);
                    market.put("Under", 1 - yesProb);
                }

                markets.addMarketWithShortKey(market);

            }
            int x = ((TennisMatchState) matchState).getSetNo();
            int a = ((TennisMatchState) matchState).getGamesA();
            int b = ((TennisMatchState) matchState).getGamesB();
            Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "S:OUNG", "S" + x,
                            "Set " + x + " Total Games Over/Under(Next Gen)");

            market.setIsValid(true);
            market.put("Over 6.5", 0.6);
            market.put("Under 6.5", 0.4);
            if (a >= 1 && b >= 1) {
            } else {
                market.put("Over 4.5", 0.4);
                market.put("Under 4.5", 0.6);
            }

            if (a >= 2 && b >= 2) {
            } else {
                market.put("Over 5.5", 0.5);
                market.put("Under 5.5", 0.5);
            }
            if (a >= 3 && b >= 3) {
                market.setIsValid(false);
            }
            markets.addMarketWithShortKey(market);

        }

    }

    public void updateStats(TennisMatchState matchState, TennisMatchFacts matchFacts) {
        boolean iptlMatch = ((TennisMatchFormat) matchState.getMatchFormat()).getTournamentLevel()
                        .equals(TournamentLevel.IPTL);

        matchWinner.increment(matchState.getSetsA() > matchState.getSetsB());
        correctSetScore.increment(matchState.getSetsA(), matchState.getSetsB());
        int currentSetNo = matchFacts.acturalSetNo - 1;
        PairOfIntegers SetGameScores = matchState.getGameScoreInSetN(currentSetNo);
        thisSetGamesScore.increment(SetGameScores.A, SetGameScores.B);
        setsTotal.increment(matchState.getSetsA() + matchState.getSetsB());
        int scoreLeft = 0;
        int scoreRight = 0;
        if (SetGameScores.A <= SetGameScores.B) {
            scoreLeft = SetGameScores.A;
            scoreRight = SetGameScores.B;
        } else {
            scoreLeft = SetGameScores.B;
            scoreRight = SetGameScores.A;
        }
        if (matchFacts.mayBeNextSet) {
            PairOfIntegers nextSetGameScores = matchState.getGameScoreInSetN(currentSetNo + 1);
            scoreLeft = 0;
            scoreRight = 0;
            if (nextSetGameScores.A <= nextSetGameScores.B) {
                scoreLeft = SetGameScores.A;
                scoreRight = SetGameScores.B;
            } else {
                scoreLeft = SetGameScores.B;
                scoreRight = SetGameScores.A;
            }
            nextSetGamesScoreCombined.increment(scoreLeft, scoreRight);
        }

        thisSetGamesScoreCombined.increment(scoreLeft, scoreRight);
        thisSetWinner.increment(SetGameScores.A > SetGameScores.B);
        if (!iptlMatch) {
            int noSetsPlayedSelectionId = matchState.getSetsA() + matchState.getSetsB() - minToWin;
            noSetsPlayed.increment(noSetsPlayedSelectionId);
        }
        int totalGamesA = 0;
        int totalGamesB = 0;
        int totalSetsA = 0;
        int totalSetsB = 0;
        boolean tbPlayed = false;
        for (int i = 0; i < nSetsInMatch; i++) {
            totalGamesA += matchState.getGameScoreInSetN(i).A;
            totalGamesB += matchState.getGameScoreInSetN(i).B;
            tbPlayed = tbPlayed || (matchState.getGameScoreInSetN(i).A + matchState.getGameScoreInSetN(i).B == 13);

            if (matchState.getGameScoreInSetN(i).A > matchState.getGameScoreInSetN(i).B) {
                totalSetsA++;
            } else {
                totalSetsB++;
            }
        }

        if (matchState.getSetsA() > matchState.getSetsB() && matchState.getSetsB() > 0)
            bothTeamToWinASet.increment(0);
        else if (matchState.getSetsA() > matchState.getSetsB() && matchState.getSetsB() == 0)
            bothTeamToWinASet.increment(1);
        else if (matchState.getSetsA() < matchState.getSetsB() && matchState.getSetsA() > 0)
            bothTeamToWinASet.increment(2);
        else if (matchState.getSetsA() < matchState.getSetsB() && matchState.getSetsA() == 0)
            bothTeamToWinASet.increment(3);

        gamesTotal.increment(totalGamesA + totalGamesB);
        gamesTotalA.increment(totalGamesA);
        gamesTotalB.increment(totalGamesB);
        gamesHandicap.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap1.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap2.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap3.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap4.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap5.increment(totalGamesA - totalGamesB);
        alternativeGamesHandicap6.increment(totalGamesA - totalGamesB);

        PairOfIntegers setGameScores = matchState.getGameScoreInSetN(currentSetNo);
        alternativeSetHandicap.increment(setGameScores.A - setGameScores.B);
        setGamesHandicap.increment(setGameScores.A - setGameScores.B);

        /**
         * Robert attempt
         */
        correctSetScoreafter2.increment(matchFacts.gameScoreAAfter2, matchFacts.gameScoreBAfter2);
        correctSetScoreafter4.increment(matchFacts.gameScoreAAfter4, matchFacts.gameScoreBAfter4);
        if ((matchFacts.gameScoreAAfter6 + matchFacts.gameScoreBAfter6) == 6)
            correctSetScoreafter6.increment(matchFacts.gameScoreAAfter6, matchFacts.gameScoreBAfter6);

        correctNextSetScoreafter2.increment(matchFacts.gameScoreAAfter2, matchFacts.gameScoreBAfter2);
        correctNextSetScoreafter4.increment(matchFacts.gameScoreAAfter4, matchFacts.gameScoreBAfter4);
        if ((matchFacts.gameScoreAAfter6 + matchFacts.gameScoreBAfter6) == 6)
            correctNextSetScoreafter6.increment(matchFacts.gameScoreAAfter6, matchFacts.gameScoreBAfter6);

        int totalSetGameScores = setGameScores.A + setGameScores.B;
        if (totalSetGameScores < 9)
            setTotalGames3Way.increment(0);
        else if (totalSetGameScores > 10)
            setTotalGames3Way.increment(2);
        else
            setTotalGames3Way.increment(1);
        if (totalSetGameScores < 8)
            correctScoreSetGroupsAnyPlayer.increment(0);
        else if (totalSetGameScores < 10)
            correctScoreSetGroupsAnyPlayer.increment(1);
        else if (totalSetGameScores < 14)
            correctScoreSetGroupsAnyPlayer.increment(2);
        else
            correctScoreSetGroupsAnyPlayer.increment(3);

        if (totalGamesA < totalGamesB)
            playerAExactGamesWonMargin.increment(0);
        else if (totalGamesA - totalGamesB >= 12)
            playerAExactGamesWonMargin.increment(12);
        else
            playerAExactGamesWonMargin.increment(totalGamesA - totalGamesB);

        if (totalGamesB < totalGamesA)
            playerBExactGamesWonMargin.increment(0);
        else if (totalGamesB - totalGamesA >= 12)
            playerBExactGamesWonMargin.increment(12);
        else
            playerBExactGamesWonMargin.increment(totalGamesB - totalGamesA);
        if (matchState.getSetsA() > 0 && matchState.getSetsB() > 0)
            bothTeamToWinSet.increment(true);
        else
            bothTeamToWinSet.increment(false);
        setsHandicap.increment(totalSetsA - totalSetsB);
        if ((totalGamesA + totalGamesB) % 2 == 0)
            matchGameOddEven.increment(true);
        else
            matchGameOddEven.increment(false);

        if ((SetGameScores.A + SetGameScores.B) % 2 == 0)
            setGameOddEven.increment(true);
        else
            setGameOddEven.increment(false);

        currentSetGamesTotal.increment(SetGameScores.A + SetGameScores.B);
        currentSetGamesA.increment(SetGameScores.A);
        currentSetGamesB.increment(SetGameScores.B);

        if (matchFacts.mayBeNextSet) {
            PairOfIntegers NextSetGameScores = matchState.getGameScoreInSetN(currentSetNo + 1);
            if (NextSetGameScores.A + NextSetGameScores.B != 0) {
                nextSetGames.increment(NextSetGameScores.A + NextSetGameScores.B);
                if ((NextSetGameScores.A + NextSetGameScores.B) % 2 == 0)
                    nextSetGamesOddEven.increment(true);
                else
                    nextSetGamesOddEven.increment(false);
            }
            // FIXME: ADD IF TIE BREAK POSSIBLE
            // Will Be Tie Break In Current Set
            if ((matchState.getGameScoreInSetN(currentSetNo + 1).A
                            + matchState.getGameScoreInSetN(currentSetNo + 1).B == 13)) {
                willBeTieBreakInNextSet.increment(true);
            } else {
                willBeTieBreakInNextSet.increment(false);
            }

        }
        int firstBreak = matchState.getFirstServingBreakInSetN()[matchFacts.setX - 1];
        int firstBreakA = matchState.getFirstServingBreakAInSetN()[matchFacts.setX - 1];
        int firstBreakB = matchState.getFirstServingBreakBInSetN()[matchFacts.setX - 1];
        if (firstBreak == 0 || firstBreak > 12)
            firstServingBreakInSetX.increment(12);
        else
            firstServingBreakInSetX.increment(firstBreak - 1);

        if (firstBreakA == 0 || firstBreakA > 12)
            gameOfFirstBreakServicePlayerAInSetX.increment(3);
        else if (firstBreakA < 5)
            gameOfFirstBreakServicePlayerAInSetX.increment(0);
        else if (firstBreakA < 9)
            gameOfFirstBreakServicePlayerAInSetX.increment(1);
        else
            gameOfFirstBreakServicePlayerAInSetX.increment(2);

        if (firstBreakB == 0 || firstBreakB > 12)
            gameOfFirstBreakServicePlayerBInSetX.increment(3);
        else if (firstBreakB < 5)
            gameOfFirstBreakServicePlayerBInSetX.increment(0);
        else if (firstBreakB < 9)
            gameOfFirstBreakServicePlayerBInSetX.increment(1);
        else
            gameOfFirstBreakServicePlayerBInSetX.increment(2);
        // FIXME: ADD IF TIE BREAK POSSIBLE
        // Will Be Tie Break In Current Set
        if ((matchState.getGameScoreInSetN(currentSetNo).A + matchState.getGameScoreInSetN(currentSetNo).B == 13)) {
            willBeTieBreakInCurrentSet.increment(true);
        } else {
            willBeTieBreakInCurrentSet.increment(false);
        }

        if (matchFacts.isBreakPointPossible) {
            if (matchState.getTeamBreakFirst().equals(TeamId.A))
                teamBreakPointFirst.increment(0);
            else if (matchState.getTeamBreakFirst().equals(TeamId.B))
                teamBreakPointFirst.increment(1);
            else
                teamBreakPointFirst.increment(2);
        }
        /////
        tieBreakPlayed.increment(tbPlayed);

        if (setXgamYPossible) {
            // if(matchFacts.setXGameYAndXGameYPlusOneWinner==TeamId.UNKNOWN||matchFacts.setXGameYAndXGameYWinner==TeamId.UNKNOWN)
            // System.out.println("warning");

            if (matchFacts.setXGameYAndXGameYWinner == TeamId.A
                            && matchFacts.setXGameYAndXGameYPlusOneWinner == TeamId.A)// &&matchFacts.setXGameYAndXGameYWinner==TeamId.A
                playerAToWinSetXGameYAndXGameYPlusOne.increment(true);
            else
                playerAToWinSetXGameYAndXGameYPlusOne.increment(false);

            if (matchFacts.setXGameYAndXGameYWinner == TeamId.B
                            && matchFacts.setXGameYAndXGameYPlusOneWinner == TeamId.B)// &&matchFacts.setXGameYAndXGameYWinner==TeamId.A
                playerBToWinSetXGameYAndXGameYPlusOne.increment(true);
            else
                playerBToWinSetXGameYAndXGameYPlusOne.increment(false);

        }

        totalTieBreaks.increment(matchState.getTieBreaksCounter());

        if (!matchFacts.inTieBreak)
            tiebreakOrStandardGameTotalPoints.increment(matchFacts.thisGameScoreA + matchFacts.thisGameScoreB);
        else
            tiebreakOrStandardGameTotalPoints.increment(matchFacts.tieBreakScoreA + matchFacts.tieBreakScoreB);
        winASetA.increment(matchState.getSetsA() > 0);
        winASetB.increment(matchState.getSetsB() > 0);

        thisGameNextPoint.increment(matchFacts.thisGamePointEqualOneWinnerIsA);
        // if (this.setsInMatch == 3)
        // alternateTotalSet2.increment(matchState.getSetsA() +
        // matchState.getSetsB() > 2.5);
        // else {
        // alternateTotalSet3.increment(matchState.getSetsA() +
        // matchState.getSetsB() > 3.5);
        // alternateTotalSet4.increment(matchState.getSetsA() +
        // matchState.getSetsB() > 4.5);
        // }
        if (matchFacts.neitherPlayerHasOverFourGamesInThisSet)
            raceToFiveGames.increment(matchFacts.firstToFiveGamesInThisSetIsA);
        if (!matchFacts.preMatch) {
            if (matchFacts.inTieBreak) {
                tieBreakWinner.increment(matchFacts.tieBreakWinnerIsA);
                if (matchFacts.tieBreakScoreA > matchFacts.tieBreakScoreB) {
                    if (matchFacts.tieBreakScoreB < 6)
                        tieBreakScore.increment(matchFacts.tieBreakScoreB);
                    else
                        tieBreakScore.increment(12);
                }
                if (matchFacts.tieBreakScoreA < matchFacts.tieBreakScoreB) {
                    if (matchFacts.tieBreakScoreA < 6)
                        tieBreakScore.increment(matchFacts.tieBreakScoreA + 6);
                    else
                        tieBreakScore.increment(12);
                }

            } else {
                thisGameWinner.increment(matchFacts.thisGameWinnerIsA);
                thisGameToDeuce.increment(matchFacts.thisGametoDeuce);

                thisGamePointPlusTwo.increment(matchFacts.thisGamePointPlusOneWinnerIsA);
                thisGamePointPlusThree.increment(matchFacts.thisGamePointPlusTwoWinnerIsA);
                if (matchFacts.thisGameScoreA == 4 && matchFacts.thisGameScoreB == 0) {
                    setXgameYCorrectScore.increment(0);
                }
                if (matchFacts.thisGameScoreA == 4 && matchFacts.thisGameScoreB == 1) {
                    setXgameYCorrectScore.increment(1);
                }
                if (matchFacts.thisGameScoreA == 4 && matchFacts.thisGameScoreB == 2) {
                    setXgameYCorrectScore.increment(2);
                }
                if (matchFacts.thisGameScoreA == 5 && matchFacts.thisGameScoreB == 3) {
                    setXgameYCorrectScore.increment(3);
                }

                if (matchFacts.thisGameScoreA == 0 && matchFacts.thisGameScoreB == 4) {
                    setXgameYCorrectScore.increment(4);
                }
                if (matchFacts.thisGameScoreA == 1 && matchFacts.thisGameScoreB == 4) {
                    setXgameYCorrectScore.increment(5);
                }
                if (matchFacts.thisGameScoreA == 2 && matchFacts.thisGameScoreB == 4) {
                    setXgameYCorrectScore.increment(6);
                }
                if (matchFacts.thisGameScoreA == 3 && matchFacts.thisGameScoreB == 5) {
                    setXgameYCorrectScore.increment(7);
                }
                if (matchFacts.mayBeNextGame) {
                    nextGameWinner.increment(matchFacts.nextGameWinnerIsA);
                    if (matchFacts.nextGamePlayed)
                        nextGameToDeuce.increment(matchFacts.nextGameToDeuce);
                    nextGamePointOne.increment(matchFacts.nextGamePointOneWinnerIsA);
                    nextGamePointTwo.increment(matchFacts.nextGamePointTwoWinnerIsA);
                }
                if (matchFacts.mayBeGamePlus2) {
                    gamePlusTwoWinner.increment(matchFacts.gamePlusTwoWinnerIsA);
                    if (matchFacts.gamePlusTwoPlayed)
                        gamePlusTwoToDeuce.increment(matchFacts.gamePlusTwoToDeuce);
                }
            }
        }
    }

}
