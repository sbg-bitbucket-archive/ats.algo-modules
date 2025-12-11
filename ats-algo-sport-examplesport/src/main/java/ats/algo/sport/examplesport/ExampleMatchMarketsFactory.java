package ats.algo.sport.examplesport;

import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.examplesport.ExampleMatch.ExampleMatchFacts;

public class ExampleMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Geoff
     *
     */

    private ThreeWayStatistic matchWinnerLegsEven;
    private TwoWayStatistic matchWinnerLegsOdd;
    private CorrectScoreStatistic correctLegScore;
    private TotalStatistic legsTotal;
    private TotalStatistic legsTotalA;
    private TotalStatistic legsTotalB;
    private HandicapStatistic legsHandicap;
    private TwoWayStatistic legWinnerCurrent;
    private TwoWayStatistic legWinnerNext;
    private TwoWayStatistic legWinnerNPlus2;

    private boolean evenNoLegs;

    ExampleMatchMarketsFactory(int nLegsInMatch) {
        int maxLegScore = nLegsInMatch / 2 + 1;
        evenNoLegs = 2 * (nLegsInMatch / 2) == nLegsInMatch;
        if (evenNoLegs)
            matchWinnerLegsEven = new ThreeWayStatistic("AB", "Match winner", true, "M", "A", "B", "Draw");
        else
            matchWinnerLegsOdd = new TwoWayStatistic("AB", "Match winner", true, "M", "A", "B");
        correctLegScore = new CorrectScoreStatistic("LB", "Match leg correct score", true, "M", maxLegScore);
        legsTotal = new TotalStatistic("LT", "Match total games", true, "M", nLegsInMatch);
        legsTotalA = new TotalStatistic("LTA", "Match total games", true, "M", maxLegScore);
        legsTotalB = new TotalStatistic("LTB", "Match total games", true, "M", maxLegScore);
        legsHandicap = new HandicapStatistic("LH", "Match handicap", true, "M", nLegsInMatch);
        legWinnerCurrent = new TwoWayStatistic("LWC", "Leg winner - current leg", true, "M", "A", "B");
        legWinnerNext = new TwoWayStatistic("LWC", "Leg winner - next leg", true, "M", "A", "B");
        legWinnerNPlus2 = new TwoWayStatistic("LWC", "Leg winner - leg+2", true, "M", "A", "B");
    }

    void updateStats(ExampleMatchState matchState, ExampleMatchFacts matchFacts) {
        int legsA = matchState.getLegsA();
        int legsB = matchState.getLegsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (legsA > legsB)
            teamId = TeamId.A;
        if (legsA < legsB)
            teamId = TeamId.B;
        if (evenNoLegs)
            matchWinnerLegsEven.increment(teamId);
        else
            matchWinnerLegsOdd.increment(teamId == TeamId.A);
        correctLegScore.increment(legsA, legsB);
        legsTotal.increment(legsA + legsB);
        legsTotalA.increment(legsA);
        legsTotalB.increment(legsB);
        legsHandicap.increment(legsA - legsB);
        legWinnerCurrent.increment(matchFacts.legWinnerCurrentIsA);
        if (matchFacts.legNextIsPlayed)
            legWinnerNext.increment(matchFacts.legWinnerNextIsA);
        if (matchFacts.legNPlus2IsPlayed)
            legWinnerNPlus2.increment(matchFacts.legWinnerNPlus2IsA);
    }

}
