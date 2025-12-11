package ats.algo.sport.cricket;

import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.core.common.TeamId;
import ats.algo.sport.cricket.CricketMatch.CricketMatchFacts;
import ats.algo.sport.cricket.CricketMatchIncidentResult.CricketMatchIncidentResultType;

public class CricketMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the current set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private TwoWayStatistic matchWinnersetsOddT20;
    private ThreeWayStatistic matchWinnersetsOddOneDay;
    private TwoWayStatistic caughtOrNotA;
    private TwoWayStatistic caughtOrNotB;
    private TotalStatistic runsATotal;
    private TotalStatistic runsBTotal;
    private TotalStatistic currentOverTotalrunsA;
    private TotalStatistic currentOverTotalrunsB;
    private NWayStatistic wicketMethodA;
    private NWayStatistic wicketMethodB;
    private int currentOverNo;
    private int currentWicketNo;
    private int overNo;
    private int cricketNo;
    private TeamId bat;

    CricketMatchMarketsFactory(CricketMatchState matchState) {
        bat = matchState.getBat();
        if (TeamId.A == bat) {
            currentOverNo = matchState.getOversA();
            currentWicketNo = matchState.getWicketsA();
        } else if (TeamId.B == bat) {
            currentOverNo = matchState.getOversB();
            currentWicketNo = matchState.getWicketsB();
        }
        overNo = ((CricketMatchFormat) matchState.getMatchFormat()).getnOversinMatch();
        cricketNo = 11;
        int maxPointScoreA = 1000;
        String thisOverSequenceId = matchState.getSequenceIdforOver(0);
        String thisWicketSequenceId = matchState.getSequenceIdforWicket(1);
        String thisWicketCaughtSequenceId = matchState.getSequenceIdforCaughtWicket(1);
        Boolean generateMarket = true;
        String fullTime = matchState.getSequenceIdForMatch();
        String marketDescription;
        if (overNo == 20)
            matchWinnersetsOddT20 = new TwoWayStatistic("FT:ML", "Match Winner", generateMarket, fullTime, "A", "B");
        if (overNo == 50)
            matchWinnersetsOddOneDay =
                            new ThreeWayStatistic("FT:AXB", "Match Result", generateMarket, fullTime, "A", "B", "Draw");
        if (TeamId.UNKNOWN == bat || matchState.isMatchCompleted() || matchState.isMatchFinished())
            generateMarket = false;
        else
            generateMarket = true;

        String[] wicketMethodSelections = {"wicket_bowled", "wicket_caught", "wicket_lbw", "wicket_run_out",
                "wicket_stumped", "wicket_other"};
        if (bat == TeamId.A) {

            runsATotal = new TotalStatistic("FT:A:OU", "Team A Total Runs", generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("Team A, Over %d Total runs ", currentOverNo);
            if (currentOverNo < overNo) {
                if (matchState.getBall() == 0)
                    currentOverTotalrunsA = new TotalStatistic("P:A:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                else {
                    marketDescription = String.format("Team A, Over %d Total runs ", currentOverNo + 1);
                    thisOverSequenceId = matchState.getSequenceIdforOver(1);
                    currentOverTotalrunsA = new TotalStatistic("P:A:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                }

            }
            if (currentWicketNo < cricketNo) {
                marketDescription = String.format("Team A Wicket %d method ", currentWicketNo + 1);
                wicketMethodA = new NWayStatistic("P:WA", marketDescription, generateMarket, thisWicketSequenceId,
                                wicketMethodSelections);
                marketDescription = String.format("Team A  Wicket %d Caught or Not ", currentWicketNo + 1);
                caughtOrNotA = new TwoWayStatistic("P:CA", marketDescription, generateMarket,
                                thisWicketCaughtSequenceId, "Caught", "No Caught");
            }

        } else if (bat == TeamId.B) {
            runsBTotal = new TotalStatistic("FT:B:OU", "Team B Total Runs", generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("Team B, Over %d Total runs ", currentOverNo);
            if (currentOverNo < overNo) {
                if (matchState.getBall() == 0)
                    currentOverTotalrunsB = new TotalStatistic("P:B:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                else {
                    marketDescription = String.format("Team B, Over %d Total runs ", currentOverNo + 1);
                    thisOverSequenceId = matchState.getSequenceIdforOver(1);
                    currentOverTotalrunsB = new TotalStatistic("P:B:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                }
            }
            if (currentWicketNo < cricketNo) {
                marketDescription = String.format("Team B Wicket %d method ", currentWicketNo + 1);
                wicketMethodB = new NWayStatistic("P:WB", marketDescription, generateMarket, thisWicketSequenceId,
                                wicketMethodSelections);
                marketDescription = String.format("Team B Wicket  %d Caught or Not ", currentWicketNo + 1);
                caughtOrNotB = new TwoWayStatistic("P:CB", marketDescription, generateMarket,
                                thisWicketCaughtSequenceId, "Caught", "No Caught");
            }
        }
    }

    void updateStats(CricketMatchState matchState, CricketMatchFacts matchFacts) {
        int runsA = matchState.getRunsA();
        int runsB = matchState.getRunsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (runsA > runsB) {
            teamId = TeamId.A;
        }
        if (runsA < runsB)
            teamId = TeamId.B;
        if (overNo == 20)
            matchWinnersetsOddT20.increment(teamId == TeamId.A);
        if (overNo == 50)
            matchWinnersetsOddOneDay.increment(teamId);
        if (!matchState.getCurrentMatchState().getcricketMatchIncidentResultType()
                        .equals(CricketMatchIncidentResultType.PREMATCH)) {
            if (bat == TeamId.A) {
                runsATotal.increment(runsA);
                int x = 0;
                if (currentOverNo < overNo) {
                    if (currentOverNo == 1) {
                        x = matchState.getRunsInOverNA()[currentOverNo - 1];
                    } else if (matchState.getRunsInOverNA()[currentOverNo] != 0)
                        x = (matchState.getRunsInOverNA()[currentOverNo] == 0) ? 0
                                        : matchState.getRunsInOverNA()[currentOverNo]
                                                        - matchState.getRunsInOverNA()[currentOverNo - 1];
                    currentOverTotalrunsA.increment(x);
                }

                if (matchFacts.currentWicketA) {
                    if (currentWicketNo <= cricketNo) {
                        wicketMethodA.increment(matchFacts.wicketMethodA);
                        caughtOrNotA.increment(matchFacts.wicketMethodA == 1);
                    }
                }

            } else if (bat == TeamId.B) {

                runsBTotal.increment(runsB);
                int x = 0;
                if (currentOverNo < overNo) {
                    if (currentOverNo == 1) {
                        x = matchState.getRunsInOverNB()[currentOverNo - 1];
                    } else
                        x = (matchState.getRunsInOverNB()[currentOverNo] == 0) ? 0
                                        : matchState.getRunsInOverNB()[currentOverNo]
                                                        - matchState.getRunsInOverNB()[currentOverNo - 1];
                    currentOverTotalrunsB.increment(x);
                }

                if (matchFacts.currentWicketB) {
                    if (currentWicketNo <= cricketNo) {
                        wicketMethodB.increment(matchFacts.wicketMethodB);
                        caughtOrNotB.increment(matchFacts.wicketMethodB == 1);
                    }
                }
            }
        }
    }

}
