package ats.algo.paramfindproblemanalysis;

import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class TennisParamFindProblemTest extends AlgoManagerSimpleTestBase {

    long eventId = 123L;

    // @Test
    public void test1() {
        MethodName.log();

        algoManager.onlyPublishMarketsFollowingParamChange(false);
        MatchFormat matchFormat = new TennisMatchFormat(5, FinalSetType.ADVANTAGE_SET, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat);
        algoManager.handleMatchIncident(getStartingMatchIncident(), true);
        for (int i = 0; i < 24; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.A), true);
        for (int i = 0; i < 24; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.B), true);
        for (int i = 0; i < 20; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.A), true);
        for (int i = 0; i < 20; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.B), true);
        for (int i = 0; i < 4; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.A), true);
        // System.out.println(this.publishedMatchState);
        // TennisMatchParams matchParams = (TennisMatchParams) this.publishedMatchParams;

        // algoManager.handleSupplyMarketPrices(getMarketPricesForTest1(eventId, 1.88));
        // System.out.println(this.publishedParamFinderResults);
        // assertEquals(ParamFindResultsStatus.RED, this.publishedParamFinderResults.getParamFindResultsStatus());


    }

    private MatchIncident getPointMatchIncident(TeamId id) {
        TennisMatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, id);
        incident.setEventId(eventId);
        return incident;
    }

    private TennisMatchIncident getStartingMatchIncident() {
        TennisMatchIncident incident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 0, null);
        incident.setEventId(eventId);
        return incident;
    }


    @SuppressWarnings("unused")
    private MarketPricesList getMarketPricesForTest1(long eventId, double probAh) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(0.5);

        MarketPrice tmtg = new MarketPrice("FT:AH", "Unknown market", MarketCategory.OVUN, "22.5");
        tmtg.put("AH", probAh);
        tmtg.put("BH", 1.98);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Bet365", m);
        return marketPricesList;
    }
}
