package ats.algo.core.request;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchEngineSavedState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;
import ats.core.util.json.JsonUtil;
import ats.algo.sport.tennis.TennisMatchIncidentResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;

public class SerialisationTest {

    @Test
    public void testPriceCalcRequest() {
        String className = "TestClass";
        CalcRequestCause calcRequestCause = CalcRequestCause.MATCH_INCIDENT;
        MatchFormat matchFormat = new TennisMatchFormat();
        MatchState matchState = new TennisMatchState(matchFormat);
        MatchParams matchParams = new TennisMatchParams();
        MatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        TennisMatchIncidentResult result =
                        new TennisMatchIncidentResult(true, true, 1, TennisMatchIncidentResultType.GAMEWONA);
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        PriceCalcRequest request = new PriceCalcRequest(123L, EventSettings.generateEventSettingsForTesting(2),
                        className, calcRequestCause, matchFormat, matchState, matchParams.generateGenericMatchParams(),
                        matchIncident, result, savedState, System.currentTimeMillis());
        String json = JsonSerializer.serialize(request, true);
        System.out.println(json);
        PriceCalcRequest request2 = JsonSerializer.deserialize(json, PriceCalcRequest.class);
        assertEquals(request.getClass(), request2.getClass());
        assertEquals(request.getMatchFormat(), request2.getMatchFormat());
        assertEquals(request.getMatchState(), request2.getMatchState());
        // for (Entry <String,MatchParam>
        // e:request.getMatchParams().getParamMap().entrySet()) {
        // MatchParam p1 = e.getValue();
        // MatchParam p2 =
        // request2.getMatchParams().getParamMap().get(e.getKey());
        // System.out.println(p1.getClass().toString() + p1);
        // System.out.println(p2.getClass().toString() + p2);
        // assertEquals(p1,p2);
        //
        // }
        assertEquals(request.getMatchParams(), request2.getMatchParams());
        assertEquals(request.getMatchIncident(), request2.getMatchIncident());
        assertEquals(request.getMatchIncidentResult(), request2.getMatchIncidentResult());
        assertEquals(request.getMatchEngineSavedState(), request2.getMatchEngineSavedState());
        assertEquals(request.getUniqueRequestId(), request2.getUniqueRequestId());
        assertEquals(request.getRequestTime(), request2.getRequestTime());
        assertEquals(request, request2);
    }

    @Test
    public void testPriceCalcResponse() {
        Markets markets = new Markets();
        MatchParams matchParams = new TennisMatchParams();
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        PriceCalcResponse response = new PriceCalcResponse("uniqueReqID", markets,
                        matchParams.generateGenericMatchParams(), savedState, null);
        String json = JsonSerializer.serialize(response, true);
        System.out.println(json);
        PriceCalcResponse response2 = JsonSerializer.deserialize(json, PriceCalcResponse.class);
        assertEquals(response, response2);
        assertEquals(response.getUniqueRequestId(), response2.getUniqueRequestId());
    }

    @Test
    public void testEventSettings() {
        EventSettings eventSettings = EventSettings.generateEventSettingsForTesting(2);
        String json = JsonUtil.marshalJson(eventSettings);
        System.out.println(json);
        EventSettings eventSettings2 = JsonUtil.unmarshalJson(json, EventSettings.class);
        assertEquals(eventSettings, eventSettings2);
    }

    @Test
    public void testParamFindRequest() {
        String className = "TestClass"; // any class will do
        MatchFormat matchFormat = new TennisMatchFormat();
        MatchState matchState = new TennisMatchState(matchFormat);
        MatchParams matchParams = new TennisMatchParams();
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        MarketPricesList marketPricesList = new MarketPricesList();
        String j = JsonSerializer.serialize(marketPricesList, true);
        System.out.println(j);
        ParamFindRequest request = new ParamFindRequest(123L, EventSettings.generateEventSettingsForTesting(2),
                        CalcModelType.EXTERNAL_MODEL, className, matchFormat, matchState,
                        matchParams.generateGenericMatchParams(), marketPricesList, savedState, 0);
        String json = JsonSerializer.serialize(request, true);
        System.out.println(json);
        ParamFindRequest request2 = JsonSerializer.deserialize(json, ParamFindRequest.class);
        assertEquals(request.getClass(), request2.getClass());
        assertEquals(request.getMatchFormat(), request2.getMatchFormat());
        assertEquals(request.getMatchState(), request2.getMatchState());
        for (Entry<String, MatchParam> e : request.getMatchParams().getParamMap().entrySet()) {
            MatchParam p1 = e.getValue();
            MatchParam p2 = request2.getMatchParams().getParamMap().get(e.getKey());
            // System.out.println(p1.getClass().toString() + p1);
            // System.out.println(p2.getClass().toString() + p2);
            assertEquals(p1, p2);

        }
        assertEquals(request.getMatchParams(), request2.getMatchParams());
        assertEquals(request.getMatchEngineSavedState(), request2.getMatchEngineSavedState());
        assertEquals(request.getMarketPricesList(), request2.getMarketPricesList());
        assertEquals(request.getUniqueRequestId(), request2.getUniqueRequestId());
        assertEquals(request.getRequestTime(), request2.getRequestTime());
        assertEquals(request, request2);
    }

}
