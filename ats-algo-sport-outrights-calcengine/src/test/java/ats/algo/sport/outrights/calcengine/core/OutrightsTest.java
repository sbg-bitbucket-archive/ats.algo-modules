package ats.algo.sport.outrights.calcengine.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsMatchParams;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.Fixtures;

public class OutrightsTest {

    @Test
    public void handlePriceCalcRequestTest() {
        Outrights outrights = new Outrights();
        Fixtures fixtures = outrights.getCompetitions().get(123456L).getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getHomeTeamID().equals("T6")) {
                /*
                 * Crystal Palace home match - set eventID to 5555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }

        MatchParams matchParams = new OutrightsMatchParams();
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, matchParams.generateGenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId("REQ_ID_001");

        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        System.out.println(response);
        assertEquals(0.40, response.getMarkets().get("C:BH").getSelections().get("Crystal Palace").getProb(), 0.01);
        OutrightsWatchList watchList = (OutrightsWatchList) response.getMatchEngineSavedState();
        assertEquals(1, watchList.getWatchListEntries().size());
        assertEquals("FT:CS", watchList.getWatchListEntries().get(55555L));
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market.put("0-1", 1.0); // Crystal palace sure to lose so prob in bottom half shld go up
        incident.addMarket(55555L, market);
        request.updateUniqueRequestId("REQ_ID_002");
        response = outrights.handlePriceCalcRequest(request);
        System.out.println(response);
        assertEquals(0.92, response.getMarkets().get("C:BH").getSelections().get("Crystal Palace").getProb(), 0.01);
    }

    // @Test
    /*
     * don't need this test except on special occasions
     */
    public void testCalcAllLeagues() {
        try {
            Outrights outrights = new Outrights();
            Competitions competitions = outrights.getCompetitions();
            for (Competition competition : competitions.values()) {
                System.out.println("Calculating " + competition.getName());
                StopWatch s = new StopWatch();
                s.start();
                CalcEngine calcEngine = new CalcEngine(competition);
                calcEngine.calculate();
                s.stop();
                System.out.printf("Completed in %.1f secs\n", s.getElapsedTimeSecs());
            }
        } catch (Exception e) {

            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getWarningsTest() {
        try {
            Outrights outrights = new Outrights();
            for (Competition competition : outrights.getCompetitions().values()) {
                System.out.println("Checking: " + competition.getName());
                CompetitionWarnings warnings = outrights.checkCompetitionStateForErrors(competition);
                System.out.println(warnings);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
