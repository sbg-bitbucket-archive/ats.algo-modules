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

    private TotalStatistic runs4ATotal;
    private TotalStatistic runs4BTotal;

    private TotalStatistic runs6ATotal;
    private TotalStatistic runs6BTotal;

    private TotalStatistic runsAInSixOvers;
    private TotalStatistic runsBInSixOvers;
    private TotalStatistic runsAInTenOvers;
    private TotalStatistic runsBInTenOvers;
    private TotalStatistic currentOverTotalrunsA;
    private TotalStatistic currentOverTotalrunsB;
    private TotalStatistic currentBallTotalrunsA;
    private TotalStatistic currentBallTotalrunsB;
    private NWayStatistic wicketMethodA;
    private NWayStatistic wicketMethodB;
    private int currentOverNo;
    private int currentBallNo;
    private int currentWicketNo;
    private int overNo;
    private int ballNo;
    private int cricketNo;
    private int currentRunsA;
    private int currentRunsB;
    private TeamId bat;

    CricketMatchMarketsFactory(CricketMatchState matchState) {
        bat = matchState.getBat();
        if (TeamId.A == bat) {
            currentOverNo = matchState.getOversA();
            currentWicketNo = matchState.getWicketsA();
            currentBallNo = matchState.getBall() + 2;
        } else if (TeamId.B == bat) {
            currentOverNo = matchState.getOversB();
            currentWicketNo = matchState.getWicketsB();
            currentBallNo = matchState.getBall() + 2;
        }
        currentRunsA = matchState.getRunsA();
        currentRunsB = matchState.getRunsB();
        overNo = ((CricketMatchFormat) matchState.getMatchFormat()).getnOversinMatch();
        ballNo = ((CricketMatchFormat) matchState.getMatchFormat()).getnBallsinOver();
        cricketNo = 11;
        int maxPointScoreA = 1000;
        String thisOverSequenceId = matchState.getSequenceIdforOver(0);
        String thisWicketSequenceId = matchState.getSequenceIdforWicket(1);
        String thisWicketCaughtSequenceId = matchState.getSequenceIdforCaughtWicket(1);
        String thisBallSequenceId = "B" + currentBallNo;
        Boolean generateMarket = true;
        String fullTime = matchState.getSequenceIdForMatch();
        String marketDescription;
        String marketDescription2;
        if (TeamId.UNKNOWN == bat || matchState.isMatchCompleted() || matchState.isMatchFinished())
            generateMarket = false;
        else
            generateMarket = true;

        if (overNo == 20) {
            matchWinnersetsOddT20 = new TwoWayStatistic("FT:ML", "Match Winner", true, fullTime, "A", "B");
            if (bat == TeamId.A) {
                if (currentOverNo < 6)
                    runsAInSixOvers = new TotalStatistic("P:A:OU6", "Team A Total Runs After 6 overs", generateMarket,
                                    "P6", maxPointScoreA);
                if (currentOverNo < 10)
                    runsAInTenOvers = new TotalStatistic("P:A:OU10", "Team A Total Runs After 10 overs", generateMarket,
                                    "P10", maxPointScoreA);
            } else if (bat == TeamId.B) {
                if (currentOverNo < 6)
                    runsBInSixOvers = new TotalStatistic("P:B:OU6", "Team B Total Runs After 6 overs", generateMarket,
                                    "P6", maxPointScoreA);
                if (currentOverNo < 10)
                    runsBInTenOvers = new TotalStatistic("P:B:OU10", "Team B Total Runs After 10 overs", generateMarket,
                                    "P10", maxPointScoreA);
            }
        }
        if (overNo == 50) {
            matchWinnersetsOddOneDay =
                            new ThreeWayStatistic("FT:AXB", "Match Result", true, fullTime, "A", "B", "Draw");
            if (bat == TeamId.A) {
                if (currentOverNo < 15)
                    runsAInSixOvers = new TotalStatistic("P:A:OU15", "Team A Total Runs After 15 overs", generateMarket,
                                    "P15", maxPointScoreA);
                if (currentOverNo < 25)
                    runsAInTenOvers = new TotalStatistic("P:A:OU25", "Team A Total Runs After 25 overs", generateMarket,
                                    "P25", maxPointScoreA);
            } else if (bat == TeamId.B) {
                if (currentOverNo < 15)
                    runsBInSixOvers = new TotalStatistic("P:B:OU15", "Team B Total Runs After 15 overs", generateMarket,
                                    "P15", maxPointScoreA);
                if (currentOverNo < 25)
                    runsBInTenOvers = new TotalStatistic("P:B:OU25", "Team B Total Runs After 25 overs", generateMarket,
                                    "P25", maxPointScoreA);
            }
        }

        String[] wicketMethodSelections = {"wicket_bowled", "wicket_caught", "wicket_lbw", "wicket_run_out",
                "wicket_stumped", "wicket_other"};
        if (bat == TeamId.A) {
            if (currentRunsB == 0)
                runsATotal = new TotalStatistic("FT:A:OU", "Team A Total Runs ", generateMarket, fullTime,
                                maxPointScoreA);
            runs4ATotal = new TotalStatistic("FT:A:OU4", "Team A Total 4 Runs ", generateMarket, fullTime,
                            maxPointScoreA);
            runs6ATotal = new TotalStatistic("FT:A:OU6", "Team A Total 6 Runs ", generateMarket, fullTime,
                            maxPointScoreA);
            marketDescription = String.format("Team A, Over %d Total runs ", currentOverNo);
            if (currentOverNo < overNo) {
                if (currentBallNo == 0) {
                    currentOverTotalrunsA = new TotalStatistic("P:A:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                } else {
                    marketDescription = String.format("Team A, Over %d Total runs ", currentOverNo + 1);
                    thisOverSequenceId = matchState.getSequenceIdforOver(1);
                    currentOverTotalrunsA = new TotalStatistic("P:A:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                }

            }
            if (currentBallNo < overNo * ballNo) {
                marketDescription2 = String.format("Team A, Ball %d Total runs ", currentBallNo);
                currentBallTotalrunsA = new TotalStatistic("P:A:OUB", marketDescription2, generateMarket,
                                thisBallSequenceId, maxPointScoreA);
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
            if (currentRunsA == 0)
                runsBTotal = new TotalStatistic("FT:B:OU", "Team B Total Runs", generateMarket, fullTime,
                                maxPointScoreA);
            runs4BTotal = new TotalStatistic("FT:B:OU4", "Team B Total 4 Runs ", generateMarket, fullTime,
                            maxPointScoreA);
            runs6BTotal = new TotalStatistic("FT:B:OU6", "Team B Total 6 Runs ", generateMarket, fullTime,
                            maxPointScoreA);
            marketDescription = String.format("Team B, Over %d Total runs ", currentOverNo);

            if (currentOverNo < overNo) {
                if (currentBallNo == 0) {
                    currentOverTotalrunsB = new TotalStatistic("P:B:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                }

                else {
                    marketDescription = String.format("Team B, Over %d Total runs ", currentOverNo + 1);
                    thisOverSequenceId = matchState.getSequenceIdforOver(1);
                    currentOverTotalrunsB = new TotalStatistic("P:B:OU", marketDescription, generateMarket,
                                    thisOverSequenceId, maxPointScoreA);
                }
            }

            if (currentBallNo < overNo * ballNo) {
                marketDescription2 = String.format("Team B, Ball %d Total runs ", currentBallNo);
                currentBallTotalrunsB = new TotalStatistic("P:B:OUB", marketDescription2, generateMarket,
                                thisBallSequenceId, maxPointScoreA);
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
        int runs4A = matchState.getRuns4A();
        int runs4B = matchState.getRuns4B();
        int runs6A = matchState.getRuns6A();
        int runs6B = matchState.getRuns6B();
        TeamId teamId = TeamId.UNKNOWN;
        if (runsA > runsB) {
            teamId = TeamId.A;
        }
        if (runsA < runsB)
            teamId = TeamId.B;

        if (overNo == 20) {
            matchWinnersetsOddT20.increment(teamId == TeamId.A);
            int runsAOverSix = matchState.getRunsInOverNA()[5];
            int runsBOverSix = matchState.getRunsInOverNB()[5];
            int runsAOverTen = matchState.getRunsInOverNA()[9];
            int runsBOverTen = matchState.getRunsInOverNB()[9];
            if (bat == TeamId.A) {
                if (currentOverNo < 6)
                    runsAInSixOvers.increment(runsAOverSix);
                if (currentOverNo < 10)
                    runsAInTenOvers.increment(runsAOverTen);
            } else if (bat == TeamId.B) {
                if (currentOverNo < 6)
                    runsBInSixOvers.increment(runsBOverSix);
                if (currentOverNo < 10)
                    runsBInTenOvers.increment(runsBOverTen);
            }
        }
        if (overNo == 50) {
            matchWinnersetsOddOneDay.increment(teamId);
            int runsAOver15 = matchState.getRunsInOverNA()[14];
            int runsBOver15 = matchState.getRunsInOverNB()[14];
            int runsAOver25 = matchState.getRunsInOverNA()[24];
            int runsBOver25 = matchState.getRunsInOverNB()[24];
            if (bat == TeamId.A) {
                if (currentOverNo < 15)
                    runsAInSixOvers.increment(runsAOver15);
                if (currentOverNo < 25)
                    runsAInTenOvers.increment(runsAOver25);
            } else if (bat == TeamId.B) {
                if (currentOverNo < 15)
                    runsBInSixOvers.increment(runsBOver15);
                if (currentOverNo < 25)
                    runsBInTenOvers.increment(runsBOver25);
            }

        }
        if (!matchState.getCurrentMatchState().getcricketMatchIncidentResultType()
                        .equals(CricketMatchIncidentResultType.PREMATCH)) {
            if (bat == TeamId.A) {
                if (currentRunsB == 0)
                    runsATotal.increment(runsA);
                runs4ATotal.increment(runs4A);
                runs6ATotal.increment(runs6A);
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
                if (currentRunsA == 0)
                    runsBTotal.increment(runsB);
                runs4BTotal.increment(runs4B);
                runs6BTotal.increment(runs6B);
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
        if (currentBallNo < overNo * ballNo)
            if (bat == TeamId.A) {
                if (matchFacts.currentRunsA != -1)
                    currentBallTotalrunsA.increment(matchFacts.currentRunsA);
            } else if (bat == TeamId.B)
                if (matchFacts.currentRunsB != -1)
                    currentBallTotalrunsB.increment(matchFacts.currentRunsB);

    }

}
