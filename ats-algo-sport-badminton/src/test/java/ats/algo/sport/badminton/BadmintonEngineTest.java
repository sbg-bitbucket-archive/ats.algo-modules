package ats.algo.sport.badminton;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.badminton.BadmintonMatchEngine;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchParams;

public class BadmintonEngineTest {

    @Test
    public void test() {
        BadmintonMatchFormat matchFormat = new BadmintonMatchFormat();
        matchFormat.setnGamesInMatch(3);
        BadmintonMatchEngine matchEngine = new BadmintonMatchEngine(matchFormat);
        BadmintonMatchParams matchParams = (BadmintonMatchParams) matchEngine.getMatchParams();
        double p = 0.50;
        matchParams.setOnServePctA(p, 0);
        // matchParams.setOnServePctB(p, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();

        // System.out.println(markets.get("FT:ML").get("A"));
        // System.out.println(markets.get("FT:ML").get("B"));
        // System.out.println(markets.get("FT:PSPRD").get("A"));
        // System.out.println(markets.get("FT:PSPRD").get("B"));
        System.out.println(markets);
        assertEquals(0.4985, markets.get("FT:ML").get("A"), 0.02);
        assertEquals(0.5015, markets.get("FT:ML").get("B"), 0.02);
        assertEquals(0.4987, markets.get("FT:PSPRD").get("AH"), 0.02);
        assertEquals(0.5013, markets.get("FT:PSPRD").get("BH"), 0.02);
    }
}
