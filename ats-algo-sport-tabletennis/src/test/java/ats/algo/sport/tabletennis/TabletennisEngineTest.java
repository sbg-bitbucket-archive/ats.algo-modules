package ats.algo.sport.tabletennis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.tabletennis.TabletennisMatchEngine;
import ats.algo.sport.tabletennis.TabletennisMatchFormat;
import ats.algo.sport.tabletennis.TabletennisMatchParams;

public class TabletennisEngineTest {

    @Test
    public void test() {
        TabletennisMatchFormat matchFormat = new TabletennisMatchFormat();
        matchFormat.setnGamesInMatch(3);
        TabletennisMatchEngine matchEngine = new TabletennisMatchEngine(matchFormat);
        TabletennisMatchParams matchParams = (TabletennisMatchParams) matchEngine.getMatchParams();
        double p = 0.6;
        double p1 = 0.65;
        matchParams.setOnServePctA(p, 0);
        matchParams.setOnServePctB(p1, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();

        // System.out.println(markets.get("FT:ML").get("A"));
        // System.out.println(markets.get("FT:ML").get("B"));
        // System.out.println(markets.get("FT:PSPRD").get("A"));
        // System.out.println(markets.get("FT:PSPRD").get("B"));

        assertEquals(0.3555, markets.get("FT:ML").get("A"), 0.02);
        assertEquals(0.6444, markets.get("FT:ML").get("B"), 0.02);
        assertEquals(0.4713, markets.get("FT:PSPRD").get("AH"), 0.02);
        assertEquals(0.5286, markets.get("FT:PSPRD").get("BH"), 0.02);
    }
}
