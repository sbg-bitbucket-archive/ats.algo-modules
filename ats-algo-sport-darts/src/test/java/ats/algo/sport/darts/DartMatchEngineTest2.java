package ats.algo.sport.darts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.darts.DartMatchEngine;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.DartMatchState;

public class DartMatchEngineTest2 {

    @Test
    /*
     * test 1-11 format
     */
    public void test1() {
        double tolerance = 0.015;
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(11);
        DartMatchEngine matchEngine = new DartMatchEngine(matchFormat);
        DartMatchState matchState = (DartMatchState) matchEngine.getMatchState();
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        matchEngine.setMatchState(matchState);
        DartMatchParams matchParams = (DartMatchParams) matchEngine.getMatchParams();
        matchParams.setSkillA(1.0, 0);
        matchParams.setSkillB(1.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals("", markets.get("G:OUCHK40", "L1.1").getLineId());
        assertEquals("40.5", markets.get("G:OUCHK", "L1.1").getLineId());
        assertEquals("40.5", markets.get("G:OUCHK", "L2.1").getLineId());
        assertEquals("0.5", markets.get("G:OU180", "L1.1").getLineId());
        assertEquals("0.5", markets.get("G:OU180", "L2.1").getLineId());
        assertEquals("-0.5", markets.get("G:SPRD").getLineId());
        assertEquals("3.5", markets.get("FT:OU180").getLineId());
        assertEquals("1.5", markets.get("FT:A:OU180").getLineId());
        assertEquals("1.5", markets.get("FT:B:OU180").getLineId());
        assertEquals("9.5", markets.get("G:OU").getLineId());
        assertEquals("5.5", markets.get("G:A:OU").getLineId());
        assertEquals("5.5", markets.get("G:B:OU").getLineId());
        assertEquals(0.65, markets.get("G:1180", "L1.1").get("No"), tolerance);
        assertEquals(0.64, markets.get("G:1180", "L2.1").get("No"), tolerance);
        assertEquals(0.43, markets.get("G:CHKC", "L1.1").get("Green"), tolerance);
        assertEquals(0.43, markets.get("G:CHKC", "L2.1").get("Green"), tolerance);
        assertEquals(0.48, markets.get("G:OUCHK40", "L1.1").get("Over 40.5"), tolerance);
        assertEquals(0.48, markets.get("G:OUCHK", "L1.1").get("Over"), tolerance);
        assertEquals(0.47, markets.get("G:OUCHK", "L2.1").get("Over"), tolerance);
        assertEquals(0.35, markets.get("G:OU180", "L1.1").get("Over"), tolerance);
        assertEquals(0.36, markets.get("G:OU180", "L2.1").get("Over"), tolerance);
        assertEquals(0.66, markets.get("G:ML", "L1.1").get("A"), tolerance);
        assertEquals(0.34, markets.get("G:ML", "L2.1").get("A"), tolerance);
        assertEquals(0.66, markets.get("G:ML", "L3.1").get("A"), tolerance);
        assertEquals(1.00, markets.get("FT:9DFN").get("No"), tolerance);
        assertEquals(1.00, markets.get("FT:170CHK").get("No"), tolerance);
        Market market = markets.get("FT:OUCHK").getMarketForLineId("116.5");
        assertEquals(0.5, market.get("Over"), tolerance);
        market = markets.get("FT:A:OUCHK").getMarketForLineId("96.5");
        assertEquals(0.5, market.get("Over"), tolerance);
        market = markets.get("FT:B:OUCHK").getMarketForLineId("95.5");
        assertEquals(0.5, market.get("Over"), tolerance);
        assertEquals(0.54, markets.get("G:SPRD").get("AH"), tolerance);
        assertEquals(0.84, markets.get("FT:A:HTRK").get("No"), tolerance);
        assertEquals(0.86, markets.get("FT:B:HTRK").get("No"), tolerance);
        assertEquals(0.01, markets.get("FT:CS").get("0-6"), tolerance);
        assertEquals(0.03, markets.get("FT:CS").get("1-6"), tolerance);
        assertEquals(0.09, markets.get("FT:CS").get("2-6"), tolerance);
        assertEquals(0.08, markets.get("FT:CS").get("3-6"), tolerance);
        assertEquals(0.16, markets.get("FT:CS").get("4-6"), tolerance);
        assertEquals(0.09, markets.get("FT:CS").get("5-6"), tolerance);
        assertEquals(0.01, markets.get("FT:CS").get("6-0"), tolerance);
        assertEquals(0.06, markets.get("FT:CS").get("6-1"), tolerance);
        assertEquals(0.06, markets.get("FT:CS").get("6-2"), tolerance);
        assertEquals(0.15, markets.get("FT:CS").get("6-3"), tolerance);
        assertEquals(0.09, markets.get("FT:CS").get("6-4"), tolerance);
        assertEquals(0.17, markets.get("FT:CS").get("6-5"), tolerance);
        assertEquals(0.39, markets.get("FT:M180").get("A"), tolerance);
        assertEquals(0.49, markets.get("FT:N180", "N1").get("A"), tolerance);
        assertEquals(0.54, markets.get("FT:OU180").get("Over"), tolerance);
        assertEquals(0.59, markets.get("FT:A:OU180").get("Over"), tolerance);
        assertEquals(0.58, markets.get("FT:B:OU180").get("Over"), tolerance);
        assertEquals(0.51, markets.get("G:OU").get("Over"), tolerance);
        assertEquals(0.54, markets.get("G:A:OU").get("Over"), tolerance);
        assertEquals(0.46, markets.get("G:B:OU").get("Over"), tolerance);
        assertEquals(0.43, markets.get("P:OE").get("Even"), tolerance);
        assertEquals(0.54, markets.get("FT:ML").get("A"), tolerance);
    }

    Markets markets;
    double tolerance;

    @Test
    /*
     * test 3-5 format
     */
    public void test2() {
        tolerance = 0.015;
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(3);
        matchFormat.setnLegsOrSetsInMatch(5);
        DartMatchEngine matchEngine = new DartMatchEngine(matchFormat);
        DartMatchState matchState = (DartMatchState) matchEngine.getMatchState();
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.B);
        matchEngine.setMatchState(matchState);
        DartMatchParams matchParams = (DartMatchParams) matchEngine.getMatchParams();
        matchParams.setSkillA(1.05, 0);
        matchParams.setSkillB(0.95, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(0.74, markets.get("G:ML", "L1.2").get("A"), tolerance);
        assertEquals(0.23, markets.get("G:ML", "L1.3").get("A"), tolerance);
        assertEquals(0.64, markets.get("G:1180", "L1.1").get("No"), tolerance);
        assertEquals(0.64, markets.get("G:1180", "L1.2").get("No"), tolerance);
        assertEquals(0.43, markets.get("G:CHKC", "L1.1").get("Green"), tolerance);
        assertEquals(0.44, markets.get("G:CHKC", "L1.2").get("Green"), tolerance);
        assertEquals(0.44, markets.get("G:ML", "L1.1").get("A"), tolerance);
        assertEquals(0.99, markets.get("FT:9DFN").get("No"), tolerance);
        assertEquals(1.00, markets.get("FT:170CHK").get("No"), tolerance);
        assertEquals(0.72, markets.get("FT:A:HTRK").get("No"), tolerance);
        assertEquals(0.94, markets.get("FT:B:HTRK").get("No"), tolerance);
        assertEquals(0.53, markets.get("FT:M180").get("A"), tolerance);
        assertEquals(0.57, markets.get("FT:N180", "N1").get("A"), tolerance);
        assertEquals(0.05, markets.get("FT:CS").get("0-3"), tolerance);
        assertEquals(0.09, markets.get("FT:CS").get("1-3"), tolerance);
        assertEquals(0.12, markets.get("FT:CS").get("2-3"), tolerance);
        assertEquals(0.23, markets.get("FT:CS").get("3-0"), tolerance);
        assertEquals(0.29, markets.get("FT:CS").get("3-1"), tolerance);
        assertEquals(0.22, markets.get("FT:CS").get("3-2"), tolerance);
        assertEquals(0.38, markets.get("FT:OE").get("Even"), tolerance);
        assertEquals(0.74, markets.get("FT:ML").get("A"), tolerance);
        assertEquals(0.14, markets.get("P:CS", "S1").get("0-2"), tolerance);
        assertEquals(0.29, markets.get("P:CS", "S1").get("1-2"), tolerance);
        assertEquals(0.33, markets.get("P:CS", "S1").get("2-0"), tolerance);
        assertEquals(0.23, markets.get("P:CS", "S1").get("2-1"), tolerance);
        assertEquals(0.14, markets.get("P:CS", "S1").get("0-2"), tolerance);
        assertEquals(0.21, markets.get("P:CS", "S2").get("1-2"), tolerance);
        assertEquals(0.33, markets.get("P:CS", "S2").get("2-0"), tolerance);
        assertEquals(0.32, markets.get("P:CS", "S2").get("2-1"), tolerance);
        assertEquals(0.47, markets.get("P:OE", "S1").get("Even"), tolerance);
        assertEquals(0.47, markets.get("P:OE", "S2").get("Even"), tolerance);
        assertEquals(0.56, markets.get("P:ML", "S1").get("A"), tolerance);
        assertEquals(0.65, markets.get("P:ML", "S2").get("A"), tolerance);
        assertEquals(0.49, markets.get("G:OUCHK40", "L1.1").get("Over 40.5"), tolerance);
        checkMkt(.48, "G:OUCHK", "L1.1", "40.5", "Over");
        checkMkt(.47, "G:OUCHK", "L1.2", "40.5", "Over");
        checkMkt(.36, "G:OU180", "L1.1", "0.5", "Over");
        checkMkt(.36, "G:OU180", "L1.2", "0.5", "Over");
        checkMkt(.51, "FT:OUCHK", "119.5", "Over");
        checkMkt(.53, "FT:A:OUCHK", "100.5", "Over");
        checkMkt(.50, "FT:B:OUCHK", "94.5", "Over");
        checkMkt(.86, "FT:SPRD", "1.5", "AH");
        checkMkt(.43, "FT:OU180", "4.5", "Over");
        checkMkt(.45, "FT:A:OU180", "2.5", "Over");
        checkMkt(.54, "FT:B:OU180", "1.5", "Over");
        checkMkt(.34, "FT:OUS", "4.5", "Over");
        checkMkt(.74, "FT:A:OUS", "2.5", "Over");
        checkMkt(.48, "FT:B:OUS", "1.5", "Over");
        checkMkt(.56, "P:SPRD", "S1", "0.5", "AH");
        checkMkt(.65, "P:SPRD", "S2", "0.5", "AH");
        checkMkt(.67, "P:OU180", "S1", "0.5", "Over");
        checkMkt(.53, "P:OU", "S1", "2.5", "Over");
        checkMkt(.53, "P:OU", "S2", "2.5", "Over");
    }

    private void checkMkt(double expVal, String mktCode, String lineId, String selKey) {
        Market market = markets.get(mktCode).getMarketForLineId(lineId);
        assertEquals(expVal, market.get(selKey), tolerance);
    }

    private void checkMkt(double expVal, String mktCode, String sequenceId, String lineId, String selKey) {
        Market market = markets.get(mktCode, sequenceId).getMarketForLineId(lineId);
        assertEquals(expVal, market.get(selKey), tolerance);
    }

}
