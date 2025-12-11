package ats.algo.sport.outrights.calcengine.leagues;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.Selection;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class LeagueMarketFactoryTest {

    @Test
    public void updateStatsTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        LeagueMarketsFactory factory = new LeagueMarketsFactory(competition);
        LeagueState simulationState =
                        new LeagueState(competition, new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3));
        factory.updateStats(simulationState);
        Markets markets = factory.generateMonteCarloMarkets();
        Market market = markets.get("C:TH");
        // System.out.println(market);
        assertEquals(1.0, market.get("Arsenal"), 0.001);
        // System.out.println(market.size());
        assertEquals(0.0, market.get("Bournemouth"), 0.001);
        market = markets.get("C:R");
        // System.out.println(market);
        assertEquals(0.0, market.get("Southampton"), 0.001);
        market = markets.get("C:SU");
        // System.out.println(market);
        assertEquals(1.0, market.get("Chelsea"), 0.001);
        assertEquals(0.0, market.get("West Brom"), 0.001);

        market = markets.get("C:LW");
        // System.out.println(market);
        for (Entry<String, Selection> selection : market.getSelections().entrySet()) {
            if (selection.getValue().getProb() == 1.0) {
                // System.out.println(selection);
            }
        }

        market = markets.get("C:XMST");
        // System.out.println(market);
    }

}
