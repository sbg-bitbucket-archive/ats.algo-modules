package ats.algo.sport.tennis;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchResultMarkets;
import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.Selection;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.tennis.TennisMatchResultMarkets;

public class MatchResultMarketsTest {


    @Test
    public void resultMarkets() {
        MethodName.log();
        Map<String, Market> marketsList = new HashMap<>();
        Map<String, Selection> selectionsM1 = new HashMap<>();
        Map<String, Selection> selectionsM2 = new HashMap<>();

        Market market1 = new Market();
        market1.setType("FT:AAA");
        market1.setSequenceId("M");
        market1.setLineId("");

        Selection selection1M1 = new Selection(0.5);
        Selection selection2M1 = new Selection(0.5);
        selectionsM1.put("s1", selection1M1);
        selectionsM1.put("s2", selection2M1);

        market1.setSelections(selectionsM1);

        Market market2 = new Market();
        market2.setType("FT:ML");
        market2.setSequenceId("M");
        market2.setLineId("");

        Selection selection1M2 = new Selection(0.5);
        Selection selection2M2 = new Selection(0.5);
        selectionsM2.put("s1", selection1M2);
        selectionsM2.put("s2", selection2M2);

        market2.setSelections(selectionsM2);


        marketsList.put("FT:OU", market2);
        marketsList.put("FT:AAA", market1);

        Markets markets = new Markets();
        markets.setMarkets(marketsList);

        TennisMatchState matchState1 = new TennisMatchState();
        TennisMatchState matchState2 = new TennisMatchState();

        MatchResultMarkets matchResultMarkets = new TennisMatchResultMarkets();

        ResultedMarkets resultedMarkets = null;

        resultedMarkets = matchResultMarkets.resultMarkets(markets, matchState1, matchState2);
        assertTrue(resultedMarkets != null);


    }
}
