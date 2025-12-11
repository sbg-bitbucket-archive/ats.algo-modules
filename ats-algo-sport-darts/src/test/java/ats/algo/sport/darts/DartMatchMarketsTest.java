package ats.algo.sport.darts;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.darts.Colour;
import ats.algo.sport.darts.DartMatchFacts;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchMarketsFactory;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.LegResult;
import ats.algo.sport.darts.LegWinResult;
import ats.algo.sport.darts.LegWinResult.LegWinType;

public class DartMatchMarketsTest {

    static final int nIterations = 62500;
    static final double tolerance = 0.015;

    private DartMatchState startMatchState;
    private DartMatchState matchState;
    private DartMatchFacts matchFacts;

    void simulatePlayLeg(int currentLegNoPlusN, int currentSetNoPlusN, TeamId legWinner, int nDartsA, int nDartsB,
                    Colour checkoutColour, int checkoutScore, int no180sA, int no180sB, TeamId first180InLeg,
                    LegWinType legWinType, int lastSetLegsA, int lastSetLegsB, int matchScoreSetsOrLegsA,
                    int matchScoreSetsOrLegsB, TeamId matchWinner) {
        LegResult lr = new LegResult(legWinner, nDartsA, nDartsB, checkoutColour, checkoutScore, no180sA, no180sB,
                        first180InLeg);
        LegWinResult mss = matchState.legWon(lr.legWinner);
        matchFacts.updateFactsWithLegResults(lr, mss, currentLegNoPlusN, currentSetNoPlusN);
    }

    @Test
    public void test1() {
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(3);

        matchFacts = new DartMatchFacts();
        startMatchState = new DartMatchState(matchFormat);
        matchState = new DartMatchState(matchFormat);
        DartMatchMarketsFactory stats = new DartMatchMarketsFactory(matchState);
        simulatePlayLeg(0, 0, TeamId.A, 12, 14, Colour.GREEN, 125, 2, 1, TeamId.A, LegWinType.IsSetWinner, 1, 0, 1, 0,
                        TeamId.UNKNOWN);
        simulatePlayLeg(1, 0, TeamId.B, 12, 14, Colour.RED, 125, 1, 0, TeamId.A, LegWinType.IsSetWinner, 1, 0, 1, 1,
                        TeamId.UNKNOWN);
        simulatePlayLeg(2, 0, TeamId.B, 12, 14, Colour.GREEN, 125, 1, 0, TeamId.A, LegWinType.IsMatchWinner, 1, 0, 1, 2,
                        TeamId.B);
        stats.updateStats(matchState, matchFacts);
        // one iteration completed. Sanity check some basic numbers
        Markets markets = stats.generateMonteCarloMarkets();
        assertEquals(31, markets.size());
        assertEquals("Match Winner", markets.get("FT:ML").getMarketDescription());
        Market market = markets.get("FT:ML");
        assertTrue(market.isValid());
        assertEquals(market.get("A"), 0, 0.001);
        assertEquals(market.get("B"), 1, 0.001);
        matchState.setEqualTo(startMatchState);
        matchFacts.resetCountsForNextMatchIteration(0, 0, false, 0, 0);

        // match2
        simulatePlayLeg(0, 0, TeamId.A, 12, 14, Colour.RED, 80, 0, 0, TeamId.UNKNOWN, LegWinType.IsSetWinner, 1, 0, 1,
                        0, TeamId.UNKNOWN);
        simulatePlayLeg(1, 0, TeamId.A, 12, 14, Colour.GREEN, 125, 0, 0, TeamId.UNKNOWN, LegWinType.IsMatchWinner, 1, 0,
                        2, 0, TeamId.A);
        stats.updateStats(matchState, matchFacts);
        matchState.setEqualTo(startMatchState);
        matchFacts.resetCountsForNextMatchIteration(0, 0, false, 0, 0);
        // match 3
        simulatePlayLeg(0, 0, TeamId.B, 9, 14, Colour.RED, 140, 0, 0, TeamId.UNKNOWN, LegWinType.IsSetWinner, 1, 0, 0,
                        1, TeamId.UNKNOWN);
        simulatePlayLeg(1, 0, TeamId.A, 12, 9, Colour.GREEN, 170, 1, 1, TeamId.A, LegWinType.IsSetWinner, 1, 0, 1, 1,
                        TeamId.UNKNOWN);
        simulatePlayLeg(2, 0, TeamId.A, 12, 14, Colour.GREEN, 125, 2, 1, TeamId.B, LegWinType.IsMatchWinner, 1, 0, 2, 1,
                        TeamId.A);
        stats.updateStats(matchState, matchFacts);
        matchState.setEqualTo(startMatchState);
        matchFacts.resetCountsForNextMatchIteration(0, 0, false, 0, 0);
        // match 4
        simulatePlayLeg(0, 0, TeamId.B, 9, 14, Colour.RED, 130, 0, 0, TeamId.UNKNOWN, LegWinType.IsSetWinner, 1, 0, 0,
                        1, TeamId.UNKNOWN);
        simulatePlayLeg(1, 0, TeamId.A, 12, 12, Colour.GREEN, 150, 1, 2, TeamId.B, LegWinType.IsSetWinner, 1, 0, 1, 1,
                        TeamId.UNKNOWN);
        simulatePlayLeg(2, 0, TeamId.A, 12, 14, Colour.GREEN, 125, 2, 1, TeamId.A, LegWinType.IsMatchWinner, 1, 0, 2, 1,
                        TeamId.A);
        stats.updateStats(matchState, matchFacts);
        matchState.setEqualTo(startMatchState);
        matchFacts.resetCountsForNextMatchIteration(0, 0, false, 0, 0);
        // match5
        simulatePlayLeg(0, 0, TeamId.B, 9, 14, Colour.RED, 110, 0, 0, TeamId.UNKNOWN, LegWinType.IsSetWinner, 1, 0, 0,
                        1, TeamId.UNKNOWN);
        simulatePlayLeg(1, 0, TeamId.A, 12, 12, Colour.RED, 170, 1, 2, TeamId.B, LegWinType.IsSetWinner, 1, 0, 1, 1,
                        TeamId.UNKNOWN);
        simulatePlayLeg(2, 0, TeamId.B, 12, 14, Colour.GREEN, 125, 2, 1, TeamId.A, LegWinType.IsMatchWinner, 1, 0, 2, 1,
                        TeamId.A);
        stats.updateStats(matchState, matchFacts);
        markets = stats.generateMonteCarloMarkets();
        System.out.print(markets.toString()); // for debug

        // verify every selection for every market is correct
        assertEquals(markets.get("G:1180", "L1.1").get("No"), 0.8, 0.001);
        assertEquals(markets.get("G:1180", "L1.1").get("Yes"), 0.2, 0.001);
        assertEquals(markets.get("G:1180", "L2.1").get("No"), 0.2, 0.001);
        assertEquals(markets.get("G:1180", "L2.1").get("Yes"), 0.8, 0.001);
        assertEquals(markets.get("G:CHKC", "L1.1").get("Green"), 0.2, 0.001);
        assertEquals(markets.get("G:CHKC", "L1.1").get("Red"), 0.8, 0.001);
        assertEquals(markets.get("G:CHKC", "L2.1").get("Green"), 0.6, 0.001);
        assertEquals(markets.get("G:CHKC", "L2.1").get("Red"), 0.4, 0.001);
        assertEquals(markets.get("G:OUCHK40", "L1.1").get("Over 40.5"), 1.0, tolerance);
        assertEquals(markets.get("G:OUCHK40", "L1.1").get("Under 40.5"), 0.0, tolerance);
        checkOvunMarket(markets, "G:OUCHK", "L1.1", "125.5", .4);
        checkOvunMarket(markets, "G:OUCHK", "L2.1", "150.5", .4);
        checkOvunMarket(markets, "G:OU180", "L1.1", "0.5", .2);
        checkOvunMarket(markets, "G:OU180", "L2.1", "2.5", .4);
        assertEquals(markets.get("G:ML", "L1.1").get("A"), 0.4, 0.001);
        assertEquals(markets.get("G:ML", "L1.1").get("B"), 0.6, 0.001);
        assertEquals(markets.get("G:ML", "L2.1").get("A"), 0.8, 0.001);
        assertEquals(markets.get("G:ML", "L2.1").get("B"), 0.2, 0.001);
        assertEquals(markets.get("G:ML", "L3.1").get("A"), 0.5, 0.001);
        assertEquals(markets.get("G:ML", "L3.1").get("B"), 0.5, 0.001);
        assertEquals(markets.get("FT:9DFN").get("No"), 1.0, 0.001);
        assertEquals(markets.get("FT:9DFN").get("Yes"), 0.0, 0.001);
        assertEquals(markets.get("FT:170CHK").get("No"), 0.6, 0.001);
        assertEquals(markets.get("FT:170CHK").get("Yes"), 0.4, 0.001);
        checkOvunMarket(markets, "FT:OUCHK", "150.5", .4);
        checkOvunMarket(markets, "FT:A:OUCHK", "150.5", .4);
        checkOvunMarket(markets, "FT:B:OUCHK", "125.5", .5);
        checkHcapMarket(markets, "G:SPRD", "-0.5", .6);
        assertEquals(markets.get("FT:CS").get("1-2"), 0.4, 0.001);
        assertEquals(markets.get("FT:CS").get("2-0"), 0.2, 0.001);
        assertEquals(markets.get("FT:CS").get("2-1"), 0.4, 0.001);
        assertEquals(markets.get("FT:M180").get("A"), 0.4, 0.001);
        assertEquals(markets.get("FT:M180").get("B"), 0, 0.001);
        assertEquals(markets.get("FT:M180").get("Neither"), 0.6, 0.001);
        assertEquals(markets.get("FT:N180", "N1").get("A"), 0.4, 0.001);
        assertEquals(markets.get("FT:N180", "N1").get("B"), 0.4, 0.001);
        assertEquals(markets.get("FT:N180", "N1").get("Neither"), 0.2, 0.001);
        checkOvunMarket(markets, "FT:OU180", "5.5", .4);
        checkOvunMarket(markets, "FT:A:OU180", "3.5", .2);
        checkOvunMarket(markets, "FT:B:OU180", "2.5", .4);
        checkOvunMarket(markets, "G:OU", "2.5", .8);
        checkOvunMarket(markets, "G:A:OU", "1.5", .6);
        checkOvunMarket(markets, "G:B:OU", "1.5", .4);
        assertEquals(markets.get("P:OE").get("Even"), 0.2, 0.001);
        assertEquals(markets.get("P:OE").get("Odd"), 0.8, 0.001);
        assertEquals(markets.get("FT:ML").get("A"), 0.6, 0.001);
        assertEquals(markets.get("FT:ML").get("B"), 0.4, 0.001);
        assertEquals(markets.get("FT:A:HTRK").get("Yes"), 0.2, 0.001);
        assertEquals(markets.get("FT:A:HTRK").get("No"), 0.8, 0.001);
        assertEquals(markets.get("FT:B:HTRK").get("Yes"), 0, 0.001);
        assertEquals(markets.get("FT:B:HTRK").get("No"), 1, 0.001);
    }

    void checkOvunMarket(Markets markets, String mKey, String subType, Double overValue) {
        Market m = markets.get(mKey);
        assertEquals(subType, m.getLineId());
        assertEquals(m.get("Over"), overValue, 0.001);
        assertEquals(m.get("Under"), 1 - overValue, 0.001);
    }

    void checkOvunMarket(Markets markets, String mKey, String sequenceId, String subType, Double overValue) {
        Market m = markets.get(mKey, sequenceId);
        assertEquals(subType, m.getLineId());
        assertEquals(m.get("Over"), overValue, 0.001);
        assertEquals(m.get("Under"), 1 - overValue, 0.001);
    }

    void checkHcapMarket(Markets markets, String mKey, String subType, Double playerAValue) {
        Market m = markets.get(mKey);
        assertEquals(subType, m.getLineId());
        assertEquals(m.get("AH"), playerAValue, 0.001);
        assertEquals(m.get("BH"), 1 - playerAValue, 0.001);
    }

}
