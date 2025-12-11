package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchState;

public class DartMatchMarketsFactory extends MarketsFactory {

    private String playerAName;
    private String playerBName;

    public void setPlayerAName(String playerAName) {
        this.playerAName = playerAName;
    }

    public void setPlayerBName(String playerBName) {
        this.playerBName = playerBName;
    }

    boolean setBasedMatch;
    int nLegsPerSet;
    int nLegsOrSetsInMatch;
    int n180sASoFar; // #180s already scored in the match to date
    int n180sBSoFar;

    // stats for set based matches only
    private TwoWayStatistic matchWinnerSetBasedMatch;
    private TwoWayStatistic currentSetWinnerA;
    private TwoWayStatistic currentSetLegPlus1WinnerA;
    private TwoWayStatistic currentSetLegPlus2WinnerA;
    private TwoWayStatistic nextSetWinnerA;
    private HandicapStatistic matchLegOrSetScoreH;
    private CorrectScoreStatistic currentSetLegScore;
    private CorrectScoreStatistic nextSetLegScore;
    private HandicapStatistic currentSetLegScoreH;
    private HandicapStatistic nextSetLegScoreH;
    private TotalStatistic matchLegOrSetScoreT;
    private TotalStatistic currentSetLegScoreT;
    private TotalStatistic nextSetLegScoreT;
    private TotalStatistic matchtotalSetsA;
    private TotalStatistic matchtotalSetsB;
    private TwoWayStatistic matchTotalSetsOdd;
    private TwoWayStatistic currentSetLegScoreOdd;
    private TwoWayStatistic nextSetLegScoreOdd;
    private TotalStatistic currentSetNo180s;

    // stats for leg based matches only
    private ThreeWayStatistic matchWinnerLegBasedMatch;
    private TwoWayStatistic legPlus1WinnerA;
    private TwoWayStatistic legPlus2WinnerA;
    private TotalStatistic matchTotalLegsA;
    private TotalStatistic matchTotalLegsB;
    private TwoWayStatistic matchTotalLegsOdd;

    // stats for both
    private TwoWayStatistic currentLegCheckoutColourRed;
    private TwoWayStatistic currentLegCheckoutScoreIsOver40;
    private TwoWayStatistic currentLegHas180;
    private TwoWayStatistic nextLegCheckoutColourRed;
    private TwoWayStatistic nextLegHas180;

    private TwoWayStatistic matchHas170Checkout;
    private TwoWayStatistic matchHatTrickA;
    private TwoWayStatistic matchHatTrickB;
    private ThreeWayStatistic matchMost180s;
    private ThreeWayStatistic matchNext180;
    private CorrectScoreStatistic matchLegOrSetScore;
    private TotalStatistic matchHighestCheckout;
    private TotalStatistic matchHighestCheckoutA;
    private TotalStatistic matchHighestCheckoutB;
    private TotalStatistic currentLegCheckoutScore;
    private TotalStatistic nextLegCheckoutScore;
    private TotalStatistic nextLegNo180s;
    private TotalStatistic matchNo180s;
    private TotalStatistic matchNo180sA;
    private TotalStatistic matchNo180sB;

    /**
     * constructor
     * 
     * @param nLegsPerSet
     * @param nLegsOrSetsInMatch
     */
    public DartMatchMarketsFactory(DartMatchState matchState) {
        super();
        DartMatchFormat matchFormat = matchState.getMatchFormat();
        int nLegsPerSet = matchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = matchFormat.getnLegsOrSetsInMatch();
        playerAName = "playerA";
        playerBName = "playerB";
        setBasedMatch = nLegsPerSet > 1;
        this.nLegsPerSet = nLegsPerSet;
        this.nLegsOrSetsInMatch = nLegsOrSetsInMatch;
        int maxSetScore = nLegsOrSetsInMatch / 2 + 1;
        int maxLegScore = nLegsPerSet / 2 + 1;
        int currentSet = matchState.getSetNo();
        int currentLeg = matchState.getLegNo();
        int current180s = matchState.get180No();
        String current180Id = String.format("N%d", current180s);
        String marketDescription;
        String setSequenceId = matchState.getSequenceIdforSet(0);
        String legSequenceId = matchState.getSequenceIdForLeg(0);
        String generateMarket = matchState.getSequenceIdForMatch();
        boolean generate;
        if (setBasedMatch) {
            matchWinnerSetBasedMatch = new TwoWayStatistic("FT:ML", "Match Winner", true, generateMarket, "A", "B");
            marketDescription = String.format("Set %d Winner", currentSet);
            currentSetWinnerA = new TwoWayStatistic("P:ML", marketDescription, true, setSequenceId, "A", "B");

            marketDescription = String.format("Set %d Winner", currentSet + 1);
            setSequenceId = matchState.getSequenceIdforSet(1);
            nextSetWinnerA = new TwoWayStatistic("P:ML", marketDescription, true, setSequenceId, "A", "B");
            generate = matchState.isLegMayBePlayed(currentLeg + 1);
            marketDescription = String.format("Set %d Leg %d Winner", currentSet, currentLeg + 1);
            legSequenceId = matchState.getSequenceIdForLeg(1);
            currentSetLegPlus1WinnerA =
                            new TwoWayStatistic("G:ML", marketDescription, generate, legSequenceId, "A", "B");
            generate = matchState.isLegMayBePlayed(currentLeg + 2);
            marketDescription = String.format("Set %d Leg %d Winner", currentSet, currentLeg + 2);
            legSequenceId = matchState.getSequenceIdForLeg(2);
            currentSetLegPlus2WinnerA =
                            new TwoWayStatistic("G:ML", marketDescription, generate, legSequenceId, "A", "B");
            matchLegOrSetScore = new CorrectScoreStatistic("FT:CS", "Match - Set Correct Score", true, generateMarket,
                            maxSetScore + 1);
            matchLegOrSetScoreH = new HandicapStatistic("FT:SPRD", "Match Handicap - Sets", true, generateMarket,
                            nLegsOrSetsInMatch);
            matchLegOrSetScoreT = new TotalStatistic("FT:OUS", "Match Total - Sets", true, generateMarket,
                            nLegsOrSetsInMatch + 1);
            marketDescription = String.format("Set %d correct leg score ", currentSet);
            setSequenceId = matchState.getSequenceIdforSet(0);
            currentSetLegScore =
                            new CorrectScoreStatistic("P:CS", marketDescription, true, setSequenceId, maxLegScore + 1);
            marketDescription = String.format("Set %d Leg handicap ", currentSet);
            currentSetLegScoreH = new HandicapStatistic("P:SPRD", marketDescription, true, setSequenceId, nLegsPerSet);
            marketDescription = String.format("Set %d Total legs ", currentSet);
            currentSetLegScoreT = new TotalStatistic("P:OU", marketDescription, true, setSequenceId, nLegsPerSet + 1);
            generate = matchState.isSetMayBePlayed(currentSet + 1);
            marketDescription = String.format("Set %d correct leg score ", currentSet + 1);
            setSequenceId = matchState.getSequenceIdforSet(1);
            nextSetLegScore = new CorrectScoreStatistic("P:CS", marketDescription, generate, setSequenceId,
                            nLegsPerSet + 1);
            marketDescription = String.format("Set %d Leg handicap ", currentSet + 1);
            nextSetLegScoreH = new HandicapStatistic("P:SPRD", marketDescription, generate, setSequenceId, nLegsPerSet);
            marketDescription = String.format("Set %d Total legs ", currentSet + 1);
            nextSetLegScoreT = new TotalStatistic("P:OU", marketDescription, generate, setSequenceId, nLegsPerSet + 1);
            matchtotalSetsA =
                            new TotalStatistic("FT:A:OUS", "Match Total Sets A", true, generateMarket, maxSetScore + 1);
            matchtotalSetsB =
                            new TotalStatistic("FT:B:OUS", "Match Total Sets B", true, generateMarket, maxSetScore + 1);
            matchTotalSetsOdd =
                            new TwoWayStatistic("FT:OE", "Match Total Sets Odd", true, generateMarket, "Odd", "Even");
            marketDescription = String.format("Set %d Legs Odd ", currentSet);
            setSequenceId = matchState.getSequenceIdforSet(0);
            currentSetLegScoreOdd = new TwoWayStatistic("P:OE", marketDescription, true, setSequenceId, "Odd", "Even");
            marketDescription = String.format("Set %d Legs Odd ", currentSet + 1);
            setSequenceId = matchState.getSequenceIdforSet(1);
            nextSetLegScoreOdd = new TwoWayStatistic("P:OE", marketDescription, generate, setSequenceId, "Odd", "Even");
            marketDescription = String.format("Set %d Total no of 180's ", currentSet);
            setSequenceId = matchState.getSequenceIdforSet(0);
            currentSetNo180s =
                            new TotalStatistic("P:OU180", marketDescription, true, setSequenceId, 4 * nLegsPerSet + 1);
            generate = matchState.isLegMayBePlayed(currentLeg + 1);
            marketDescription = String.format("Set %d Leg %d checkout colour Red", currentSet, currentLeg + 1);
            legSequenceId = matchState.getSequenceIdForLeg(1);
            nextLegCheckoutColourRed =
                            new TwoWayStatistic("G:CHKC", marketDescription, generate, legSequenceId, "Red", "Green");
            marketDescription = String.format("Set %d Leg %d at least one 180", currentSet, currentLeg + 1);
            legSequenceId = matchState.getSequenceIdForLeg(1);
            nextLegHas180 = new TwoWayStatistic("G:1180", marketDescription, generate, legSequenceId, "Yes", "No");

            marketDescription = String.format("Set %d Leg %d Checkout Score", currentSet, currentLeg + 1);
            legSequenceId = matchState.getSequenceIdForLeg(1);
            nextLegCheckoutScore = new TotalStatistic("G:OUCHK", marketDescription, generate, legSequenceId, 171);

            marketDescription = String.format("Set %d Leg %d no of 180s", currentSet, currentLeg + 1);
            legSequenceId = matchState.getSequenceIdForLeg(1);
            nextLegNo180s = new TotalStatistic("G:OU180", marketDescription, generate, legSequenceId, 6);
            // markets valid whether set or leg based

            marketDescription = String.format("Set %d Leg %d checkout score under 40.5", currentSet, currentLeg);
            currentLegCheckoutScoreIsOver40 = new TwoWayStatistic("G:OUCHK40", marketDescription, true, legSequenceId,
                            "Over 40.5", "Under 40.5");
            marketDescription = String.format("Set %d Leg %d checkout colour Red", currentSet, currentLeg);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegCheckoutColourRed =
                            new TwoWayStatistic("G:CHKC", marketDescription, true, legSequenceId, "Red", "Green");
            marketDescription = String.format("Set %d Leg %d at least one 180", currentSet, currentLeg);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegHas180 = new TwoWayStatistic("G:1180", marketDescription, true, legSequenceId, "Yes", "No");
            marketDescription = String.format("Set %d Leg %d Checkout Score", currentSet, currentLeg);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegCheckoutScore = new TotalStatistic("G:OUCHK", marketDescription, true, legSequenceId, 171);

        } else // leg based market
        {
            if (!isOdd(nLegsOrSetsInMatch))
                matchWinnerLegBasedMatch =
                                new ThreeWayStatistic("FT:AXB", "Match result", true, generateMarket, "A", "B", "Draw");
            else
                matchWinnerSetBasedMatch = new TwoWayStatistic("FT:ML", "Match Winner", true, generateMarket, "A", "B");
            matchLegOrSetScore = new CorrectScoreStatistic("FT:CS", "Match - Leg Correct Score", true, generateMarket,
                            nLegsOrSetsInMatch + 1);
            matchLegOrSetScoreH = new HandicapStatistic("G:SPRD", "Match Handicap - Legs", true, generateMarket,
                            nLegsOrSetsInMatch);
            matchLegOrSetScoreT = new TotalStatistic("G:OU", "Match Total - Legs", true, generateMarket,
                            nLegsOrSetsInMatch + 1);
            matchTotalLegsA = new TotalStatistic("G:A:OU", "Match Total Legs A", true, generateMarket, maxSetScore + 1);
            matchTotalLegsB = new TotalStatistic("G:B:OU", "Match Total Legs B", true, generateMarket, maxSetScore + 1);
            matchTotalLegsOdd =
                            new TwoWayStatistic("P:OE", "Match Total Legs Odd", true, generateMarket, "Odd", "Even");
            generate = matchState.isSetMayBePlayed(currentSet + 1);
            marketDescription = String.format("Leg %d checkout colour Red", currentSet + 1);
            legSequenceId = matchState.getSequenceIdForSetLeg(1);
            nextLegCheckoutColourRed =
                            new TwoWayStatistic("G:CHKC", marketDescription, generate, legSequenceId, "Red", "Green");
            marketDescription = String.format("Leg %d at least one 180", currentSet + 1);
            legSequenceId = matchState.getSequenceIdForSetLeg(1);
            nextLegHas180 = new TwoWayStatistic("G:1180", marketDescription, generate, legSequenceId, "Yes", "No");

            marketDescription = String.format("Leg %d Checkout Score", currentSet + 1);
            legSequenceId = matchState.getSequenceIdForSetLeg(1);
            nextLegCheckoutScore = new TotalStatistic("G:OUCHK", marketDescription, generate, legSequenceId, 171);

            marketDescription = String.format("Leg %d no of 180s", currentSet + 1);
            legSequenceId = matchState.getSequenceIdForSetLeg(1);
            nextLegNo180s = new TotalStatistic("G:OU180", marketDescription, generate, legSequenceId, 6);


            generate = matchState.isSetMayBePlayed(currentSet + 1);
            marketDescription = String.format("Leg %d Winner", currentSet + 1);
            legSequenceId = matchState.getSequenceIdForSetLeg(1);
            legPlus1WinnerA = new TwoWayStatistic("G:ML", marketDescription, generate, legSequenceId, "A", "B");
            generate = matchState.isSetMayBePlayed(currentSet + 2);
            marketDescription = String.format("Leg %d Winner", currentSet + 2);
            legSequenceId = matchState.getSequenceIdForSetLeg(2);
            legPlus2WinnerA = new TwoWayStatistic("G:ML", marketDescription, generate, legSequenceId, "A", "B");
            marketDescription = String.format("Leg %d checkout colour Red", currentSet);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegCheckoutColourRed =
                            new TwoWayStatistic("G:CHKC", marketDescription, true, legSequenceId, "Red", "Green");

            marketDescription = String.format("Leg %d checkout score under 40.5", currentSet);
            currentLegCheckoutScoreIsOver40 = new TwoWayStatistic("G:OUCHK40", marketDescription, true, legSequenceId,
                            "Over 40.5", "Under 40.5");

            marketDescription = String.format("Leg %d at least one 180", currentSet);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegHas180 = new TwoWayStatistic("G:1180", marketDescription, true, legSequenceId, "Yes", "No");
            marketDescription = String.format("Leg %d Checkout Score", currentSet);
            legSequenceId = matchState.getSequenceIdForLeg(0);
            currentLegCheckoutScore = new TotalStatistic("G:OUCHK", marketDescription, true, legSequenceId, 171);

        }

        matchHas170Checkout = new TwoWayStatistic("FT:170CHK", "Match - 170 checkout  by either player", true,
                        generateMarket, "Yes", "No");
        matchHighestCheckout = new TotalStatistic("FT:OUCHK", "Match highest checkout", true, generateMarket, 171);
        matchHighestCheckoutA =
                        new TotalStatistic("FT:A:OUCHK", "Match highest checkout playerA", true, generateMarket, 171);
        matchHighestCheckoutB =
                        new TotalStatistic("FT:B:OUCHK", "Match highest checkout playerB", true, generateMarket, 171);
        matchMost180s = new ThreeWayStatistic("FT:M180", "Match Most 180's ", true, generateMarket, "A", "B",
                        "Neither");
        marketDescription = String.format("Next Player to score 180 No.%d  in match", current180s);
        matchNext180 = new ThreeWayStatistic("FT:N180", marketDescription, true, current180Id, "A", "B", "Neither");
        matchNo180s = new TotalStatistic("FT:OU180", "Match Total no of 180's", true, generateMarket,
                        nLegsPerSet * nLegsOrSetsInMatch * 8 + 1);
        matchNo180sA = new TotalStatistic("FT:A:OU180", "Match Total no of 180's Player A", true, generateMarket,
                        nLegsPerSet * nLegsOrSetsInMatch * 4 + 1);
        matchNo180sB = new TotalStatistic("FT:B:OU180", "Match Total no of 180's Player B", true, generateMarket,
                        nLegsPerSet * nLegsOrSetsInMatch * 4 + 1);
        matchHatTrickA = new TwoWayStatistic("FT:A:HTRK", "Hat trick " + playerAName, true, generateMarket, "Yes",
                        "No");
        matchHatTrickB = new TwoWayStatistic("FT:B:HTRK", "Hat trick " + playerBName, true, generateMarket, "Yes",
                        "No");
    }

    /**
     * converts counts to team id
     * 
     * @param countA
     * @param countB
     * @return
     */
    TeamId convertCountsToTeamId(int countA, int countB) {
        TeamId player;
        if (countA > countB)
            player = TeamId.A;
        else {
            if (countA < countB)
                player = TeamId.B;
            else
                player = TeamId.UNKNOWN;
        }
        return player;
    }

    /**
     * returns true of n odd
     * 
     * @param n
     * @return
     */
    boolean isOdd(int n) {
        return 2 * (n / 2) != n;
    }

    /**
     * updates the stats with the results of the just completed run of DartMatch
     * 
     * @param simulationMatchState
     * @param matchFacts
     */
    void updateStats(DartMatchState matchState, DartMatchFacts matchFacts) {
        int setsA = matchState.getPlayerAScore().sets;
        int setsB = matchState.getPlayerBScore().sets;
        TeamId matchWinner = matchState.getMatchOutcome();
        if (setBasedMatch) {
            /*
             * stats for set based matches only
             */
            matchWinnerSetBasedMatch.increment(matchWinner == TeamId.A);
            currentSetWinnerA.increment(matchFacts.currentSetWinner == TeamId.A);
            currentSetLegPlus1WinnerA.increment(matchFacts.currentSetLegPlus1Winner == TeamId.A);
            currentSetLegPlus2WinnerA.increment(matchFacts.currentSetLegPlus2Winner == TeamId.A);
            if (matchFacts.nextSetWinner != TeamId.UNKNOWN) {
                nextSetWinnerA.increment(matchFacts.nextSetWinner == TeamId.A);
                nextSetLegScore.increment(matchFacts.nextSetLegScoreA, matchFacts.nextSetLegScoreB);
                nextSetLegScoreH.increment(matchFacts.nextSetLegScoreA - matchFacts.nextSetLegScoreB);
                nextSetLegScoreT.increment(matchFacts.nextSetLegScoreA + matchFacts.nextSetLegScoreB);
                nextSetLegScoreOdd.increment(isOdd(matchFacts.nextSetLegScoreA + matchFacts.nextSetLegScoreB));
            }
            currentSetLegScore.increment(matchFacts.currentSetLegScoreA, matchFacts.currentSetLegScoreB);
            currentSetLegScoreH.increment(matchFacts.currentSetLegScoreA - matchFacts.currentSetLegScoreB);

            currentSetLegScoreT.increment(matchFacts.currentSetLegScoreA + matchFacts.currentSetLegScoreB);
            matchtotalSetsA.increment(setsA);
            matchtotalSetsB.increment(setsB);
            matchTotalSetsOdd.increment(isOdd(setsA + setsB));
            currentSetLegScoreOdd.increment(isOdd(matchFacts.currentSetLegScoreA + matchFacts.currentSetLegScoreB));
            currentSetNo180s.increment(matchFacts.currentSetCount180sA + matchFacts.currentSetCount180sB);
        } else {
            /*
             * stats for leg based matches only
             */
            if (!isOdd(nLegsOrSetsInMatch))
                matchWinnerLegBasedMatch.increment(matchWinner);
            else if (matchWinner == TeamId.A)
                matchWinnerSetBasedMatch.increment(true);
            else if (matchWinner == TeamId.B)
                matchWinnerSetBasedMatch.increment(false);
            if (matchFacts.nextLegWinner != TeamId.UNKNOWN)
                legPlus1WinnerA.increment(matchFacts.nextLegWinner == TeamId.A);
            if (matchFacts.legPlus2Winner != TeamId.UNKNOWN)
                legPlus2WinnerA.increment(matchFacts.legPlus2Winner == TeamId.A);
            matchTotalLegsA.increment(setsA);
            matchTotalLegsB.increment(setsB);
            matchTotalLegsOdd.increment(isOdd(setsA + setsB));
        }

        /*
         * stats for both
         */
        matchLegOrSetScoreH.increment(setsA - setsB);
        matchLegOrSetScoreT.increment(setsA + setsB);
        currentLegCheckoutColourRed.increment(matchFacts.currentLegCheckoutColour == Colour.RED);
        currentLegCheckoutScoreIsOver40.increment(matchFacts.currentLegCheckoutScore > 40);
        currentLegHas180.increment(matchFacts.currentLegNo180s > 0);
        if (matchFacts.nextLegWinner != TeamId.UNKNOWN) {
            nextLegCheckoutColourRed.increment(matchFacts.nextLegCheckoutColour == Colour.RED);
            nextLegHas180.increment(matchFacts.nextLegNo180s > 0);
        }

        matchHas170Checkout.increment(
                        matchFacts.matchHighestCheckoutScoreA == 170 || matchFacts.matchHighestCheckoutScoreB == 170);

        TeamId id = TeamId.UNKNOWN;
        if (matchFacts.matchCount180sA > matchFacts.matchCount180sB)
            id = TeamId.A;
        else if (matchFacts.matchCount180sB > matchFacts.matchCount180sA)
            id = TeamId.B;
        matchMost180s.increment(id);
        matchNext180.increment(matchFacts.matchNextToScore180);
        matchLegOrSetScore.increment(setsA, setsB);
        if (matchFacts.matchHighestCheckoutScoreA > matchFacts.matchHighestCheckoutScoreB)
            matchHighestCheckout.increment(matchFacts.matchHighestCheckoutScoreA);
        else
            matchHighestCheckout.increment(matchFacts.matchHighestCheckoutScoreB);
        if (matchFacts.matchHighestCheckoutScoreA > 0)
            matchHighestCheckoutA.increment(matchFacts.matchHighestCheckoutScoreA);
        if (matchFacts.matchHighestCheckoutScoreB > 0)
            matchHighestCheckoutB.increment(matchFacts.matchHighestCheckoutScoreB);
        currentLegCheckoutScore.increment(matchFacts.currentLegCheckoutScore);
        nextLegCheckoutScore.increment(matchFacts.nextLegCheckoutScore);
        nextLegNo180s.increment(matchFacts.nextLegNo180s);
        matchNo180s.increment(matchFacts.matchCount180sA + matchFacts.matchCount180sB);
        matchNo180sA.increment(matchFacts.matchCount180sA);
        matchNo180sB.increment(matchFacts.matchCount180sB);
        boolean isHatTrick;
        isHatTrick = matchWinner == TeamId.A && (matchFacts.matchCount180sA > matchFacts.matchCount180sB)
                        && (matchFacts.matchHighestCheckoutScoreA > matchFacts.matchHighestCheckoutScoreB);
        matchHatTrickA.increment(isHatTrick);
        isHatTrick = matchWinner == TeamId.B && (matchFacts.matchCount180sB > matchFacts.matchCount180sA)
                        && (matchFacts.matchHighestCheckoutScoreB > matchFacts.matchHighestCheckoutScoreA);
        matchHatTrickB.increment(isHatTrick);
    }

}
