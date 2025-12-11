package ats.algo.sport.testcricket;

import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.testcricket.TestCricketMatch.TestCricketMatchFacts;
import ats.algo.sport.testcricket.TestCricketMatchIncidentResult.CricketMatchIncidentResultType;
import ats.algo.core.MarketGroup;
import ats.algo.core.common.TeamId;

public class TestCricketMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the current set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private ThreeWayStatistic matchWinnersetsOdd;
    private TwoWayStatistic caughtOrNotA;
    private TwoWayStatistic caughtOrNotB;
    private TwoWayStatistic wicketTeamAPlayerAOrB;
    private TwoWayStatistic wicketTeamBPlayerAOrB;
    private TotalStatistic runsATotal;
    private TotalStatistic runsBTotal;
    private TotalStatistic runsATotalInning;
    private TotalStatistic runsATotalWicketOne;
    private TotalStatistic runsATotalWicketTwo;
    private TotalStatistic runsAPlayer1TotalWicket;
    private TotalStatistic runsAPlayer2TotalWicket;
    private TotalStatistic runsATotalPerOver;
    private TotalStatistic runsBTotalInning;
    private TotalStatistic runsBTotalWicketOne;
    private TotalStatistic runsBTotalWicketTwo;
    private TotalStatistic runsBPlayer1TotalWicket;
    private TotalStatistic runsBPlayer2TotalWicket;
    private TotalStatistic runsBTotalPerOver;
    private NWayStatistic wicketMethodA;
    private NWayStatistic wicketMethodB;
    private int currentOverNo;
    private int currentWicketNo;
    private int currentInningANo;
    private int currentInningBNo;
    private int cricketNo;
    private TeamId bat;
    String thisPlayerAName;
    String thisPlayerBName;
    int thisPlayerANo;
    int thisPlayerBNo;

    TestCricketMatchMarketsFactory(TestCricketMatchState matchState) {
        bat = matchState.getBat();
        if (TeamId.A == bat) {
            currentOverNo = matchState.getOversA();
            currentWicketNo = matchState.getWicketsA();
        } else if (TeamId.B == bat) {
            currentOverNo = matchState.getOversB();
            currentWicketNo = matchState.getWicketsB();
        }
        if (currentWicketNo == 10)
            currentWicketNo = 0;
        currentInningANo = matchState.getInningsA();
        currentInningBNo = matchState.getInningsB();

        thisPlayerAName = matchState.getOnFieldPlayerName()[0];
        thisPlayerBName = matchState.getOnFieldPlayerName()[1];
        thisPlayerANo = matchState.getCricketPlayerNo(bat)[0];
        thisPlayerBNo = matchState.getCricketPlayerNo(bat)[1];
        cricketNo = 10;
        int maxPointScoreA = 4000;
        String thisWicketSequenceId = String.format("W%d", currentWicketNo);
        String thisWicketCaughtSequenceId = String.format("C%d", currentWicketNo);
        String thisOverSequenceId = String.format("O%d", currentOverNo);
        Boolean generateMarket = true;
        String fullTime = "M";
        String marketDescription;
        matchWinnersetsOdd = new ThreeWayStatistic("FT:ML", "Match Result", MarketGroup.GOALS, generateMarket, fullTime,
                        "A", "B", "Draw");
        if (TeamId.UNKNOWN == bat || matchState.isMatchCompleted() || matchState.isMatchFinished())
            generateMarket = false;
        else
            generateMarket = true;

        String[] wicketMethodSelections = {"wicket_bowled", "wicket_caught", "wicket_lbw", "wicket_run_out",
                "wicket_stumped", "wicket_other"};
        if (bat == TeamId.A) {

            runsATotal = new TotalStatistic("FT:OU:A", "Team A Total Runs", MarketGroup.GOALS, generateMarket, fullTime,
                            maxPointScoreA);
            marketDescription = "Inning " + currentInningANo + " Total A runs";
            String thisInningSequenceId = String.format("I%d", currentInningANo);
            if (currentInningANo < 3)
                runsATotalInning = new TotalStatistic("P:OU:A", marketDescription, MarketGroup.GOALS, generateMarket,
                                thisInningSequenceId, maxPointScoreA);
            thisWicketSequenceId = "W" + currentWicketNo;

            runsATotalWicketOne = new TotalStatistic("W:OU:A", "Wicket " + thisWicketSequenceId + " Total Runs",
                            MarketGroup.GOALS, generateMarket, thisWicketSequenceId, maxPointScoreA);
            runsAPlayer1TotalWicket = new TotalStatistic("W:OU:P1", thisPlayerAName + " Total Runs", MarketGroup.GOALS,
                            generateMarket, thisPlayerAName, maxPointScoreA);
            runsAPlayer2TotalWicket = new TotalStatistic("W:OU:P2", thisPlayerBName + " Total Runs", MarketGroup.GOALS,
                            generateMarket, thisPlayerBName, maxPointScoreA);
            thisWicketSequenceId = "W" + (currentWicketNo + 1);
            runsATotalWicketTwo = new TotalStatistic("W:OU:A", "Wicket " + thisWicketSequenceId + " Total Runs",
                            MarketGroup.GOALS, generateMarket, thisWicketSequenceId, maxPointScoreA);

            marketDescription = String.format("Team A Wicket %d method ", currentWicketNo + 1);
            wicketMethodA = new NWayStatistic("P:WA", marketDescription, MarketGroup.GOALS, generateMarket,
                            thisWicketSequenceId, wicketMethodSelections);
            marketDescription = String.format("Team A  Wicket %d Caught or Not ", currentWicketNo + 1);
            caughtOrNotA = new TwoWayStatistic("P:CA", marketDescription, MarketGroup.GOALS, generateMarket,
                            thisWicketCaughtSequenceId, "Caught", "No Caught");
            marketDescription = "Next Player Out";
            wicketTeamAPlayerAOrB = new TwoWayStatistic("P:AB", marketDescription, MarketGroup.GOALS, generateMarket,
                            thisWicketCaughtSequenceId, thisPlayerAName, thisPlayerBName);
            if (matchState.getBallsA() > 0) {
                thisOverSequenceId = "O" + (currentOverNo + 1);
                marketDescription = String.format("Over %d A Total runs ", currentOverNo + 1);
                runsATotalPerOver = new TotalStatistic("P:OU:A", "Over " + (currentOverNo + 1) + " Total Runs",
                                MarketGroup.GOALS, generateMarket, thisOverSequenceId, maxPointScoreA);
            } else {
                marketDescription = String.format("Over %d A Total runs ", currentOverNo);
                runsATotalPerOver = new TotalStatistic("P:OU:A", "Over " + currentOverNo + " Total Runs",
                                MarketGroup.GOALS, generateMarket, thisOverSequenceId, maxPointScoreA);
            }

        } else if (bat == TeamId.B) {
            runsBTotal = new TotalStatistic("FT:OU:B", "Team B Total Runs", MarketGroup.GOALS, generateMarket, fullTime,
                            maxPointScoreA);
            marketDescription = "Inning " + currentInningBNo + " Total B runs";
            String thisInningSequenceId = String.format("I%d", currentInningBNo);
            if (currentInningBNo < 3)
                runsBTotalInning = new TotalStatistic("P:OU:B", marketDescription, MarketGroup.GOALS, generateMarket,
                                thisInningSequenceId, maxPointScoreA);

            thisWicketSequenceId = "W" + currentWicketNo;
            runsBTotalWicketOne = new TotalStatistic("W:OU:B", "Wicket " + thisWicketSequenceId + " Total Runs",
                            MarketGroup.GOALS, generateMarket, thisWicketSequenceId, maxPointScoreA);
            runsBPlayer1TotalWicket = new TotalStatistic("W:OU:P1", thisPlayerAName + " Total Runs", MarketGroup.GOALS,
                            generateMarket, thisPlayerAName, maxPointScoreA);
            runsBPlayer2TotalWicket = new TotalStatistic("W:OU:P2", thisPlayerBName + " Total Runs", MarketGroup.GOALS,
                            generateMarket, thisPlayerBName, maxPointScoreA);
            thisWicketSequenceId = "W" + (currentWicketNo + 1);
            runsBTotalWicketTwo = new TotalStatistic("W:OU:B", "Wicket " + thisWicketSequenceId + " Total Runs",
                            MarketGroup.GOALS, generateMarket, thisWicketSequenceId, maxPointScoreA);
            marketDescription = "Who is out next A/B";
            wicketTeamBPlayerAOrB = new TwoWayStatistic("P:AB", marketDescription, MarketGroup.GOALS, generateMarket,
                            thisWicketCaughtSequenceId, thisPlayerAName, thisPlayerBName);
            marketDescription = String.format("Over %d B Total runs ", currentOverNo);
            if (matchState.getBallsB() > 0) {
                thisOverSequenceId = "O" + (currentOverNo + 1);
                marketDescription = String.format("Over %d B Total runs ", currentOverNo + 1);
                runsBTotalPerOver = new TotalStatistic("P:OU:B", "Over " + (currentOverNo + 1) + " Total Runs",
                                MarketGroup.GOALS, generateMarket, thisOverSequenceId, maxPointScoreA);
            } else {
                marketDescription = String.format("Over %d B Total runs ", currentOverNo);
                runsBTotalPerOver = new TotalStatistic("P:OU:B", "Over " + currentOverNo + " Total Runs",
                                MarketGroup.GOALS, generateMarket, thisOverSequenceId, maxPointScoreA);
            }
            if (currentWicketNo < cricketNo) {
                marketDescription = String.format("Team B Wicket %d method ", currentWicketNo + 1);
                wicketMethodB = new NWayStatistic("P:WB", marketDescription, MarketGroup.GOALS, generateMarket,
                                thisWicketSequenceId, wicketMethodSelections);
                marketDescription = String.format("Team B Wicket %d Caught or Not ", currentWicketNo + 1);
                caughtOrNotB = new TwoWayStatistic("P:CB", marketDescription, MarketGroup.GOALS, generateMarket,
                                thisWicketCaughtSequenceId, "Caught", "No Caught");
            }
        }
    }

    void updateStats(TestCricketMatchState matchState, TestCricketMatchFacts matchFacts) {
        int runsA = matchState.getRunsA();
        int runsB = matchState.getRunsB();
        int totalrunsA = matchState.getInningsOneRunsA();
        int totalrunsB = matchState.getInningsOneRunsB();
        if (currentInningANo == 2) {
            totalrunsA = runsA - totalrunsA;
        }
        if (currentInningBNo == 2) {
            totalrunsB = runsB - totalrunsB;
        }

        TeamId teamId = TeamId.UNKNOWN;
        if (runsA > runsB)
            teamId = TeamId.A;
        if (runsA < runsB)
            teamId = TeamId.B;
        int na100 = matchState.getRunsPerWicketA1()[currentInningANo - 1][currentWicketNo];
        int na00 = matchState.getRunsPerWicketA()[currentInningANo - 1][thisPlayerANo];
        int na01 = matchState.getRunsPerWicketA()[currentInningANo - 1][currentWicketNo + 1];
        int na101 = matchState.getRunsPerWicketA1()[currentInningANo - 1][currentWicketNo + 1];
        int nb100 = matchState.getRunsPerWicketB1()[currentInningBNo - 1][currentWicketNo];
        int nb00 = matchState.getRunsPerWicketB()[currentInningBNo - 1][thisPlayerANo];
        int nb01 = matchState.getRunsPerWicketB()[currentInningBNo - 1][currentWicketNo + 1];
        int nb101 = matchState.getRunsPerWicketB1()[currentInningBNo - 1][currentWicketNo + 1];
        if (matchFacts.isDraw)
            matchWinnersetsOdd.increment(TeamId.UNKNOWN);
        else
            matchWinnersetsOdd.increment(teamId);
        if (!matchState.getCurrentMatchState().getcricketMatchIncidentResultType()
                        .equals(CricketMatchIncidentResultType.PREMATCH)) {
            if (bat == TeamId.A) {
                int runsPerOverA = matchState.getRunsInOverNA()[currentInningANo - 1][currentOverNo - 1];
                if (currentOverNo != 1) {
                    runsPerOverA = matchState.getRunsInOverNA()[currentInningANo - 1][currentOverNo]
                                    - matchState.getRunsInOverNA()[currentInningANo - 1][currentOverNo - 1];
                }
                runsATotal.increment(runsA);
                if (currentInningANo < 3)
                    runsATotalInning.increment(totalrunsA);
                runsATotalWicketOne.increment(na100);
                runsATotalWicketTwo.increment(na101);
                runsAPlayer1TotalWicket.increment(na00);
                runsAPlayer2TotalWicket.increment(na01);
                if (matchFacts.wicketPlayerA != null)
                    if (matchFacts.wicketPlayerA.equals(thisPlayerAName))
                        wicketTeamAPlayerAOrB.increment(true);
                    else
                        wicketTeamAPlayerAOrB.increment(false);
                if (runsPerOverA >= 0)
                    runsATotalPerOver.increment(runsPerOverA);
                if (matchFacts.currentWicketA) {
                    wicketMethodA.increment(matchFacts.wicketMethodA);
                    caughtOrNotA.increment(matchFacts.wicketMethodA == 1);
                }

            } else if (bat == TeamId.B) {
                int runsPerOverB = matchState.getRunsInOverNB()[currentInningBNo - 1][currentOverNo - 1];
                if (currentOverNo != 1) {
                    runsPerOverB = matchState.getRunsInOverNB()[currentInningBNo - 1][currentOverNo]
                                    - matchState.getRunsInOverNB()[currentInningBNo - 1][currentOverNo - 1];
                }
                runsBTotal.increment(runsB);
                if (currentInningBNo < 3)
                    runsBTotalInning.increment(totalrunsB);
                runsBTotalWicketOne.increment(nb100);
                runsBTotalWicketTwo.increment(nb101);
                runsBPlayer1TotalWicket.increment(nb00);
                runsBPlayer2TotalWicket.increment(nb01);
                if (matchFacts.wicketPlayerB != null)
                    if (matchFacts.wicketPlayerB.equals(thisPlayerAName))
                        wicketTeamBPlayerAOrB.increment(true);
                    else
                        wicketTeamBPlayerAOrB.increment(false);
                if (runsPerOverB >= 0)
                    runsBTotalPerOver.increment(runsPerOverB);
                if (matchFacts.currentWicketB && currentWicketNo <= cricketNo) {
                    wicketMethodB.increment(matchFacts.wicketMethodB);
                    caughtOrNotB.increment(matchFacts.wicketMethodB == 1);
                }
            }
        }
    }

}
