package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.TeamId;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.gateway.ppb.PpbPriceCalcGateway;
import ats.algo.sport.tennis.TennisMatchEngineSavedState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncidentResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;

public class PpbPriceCalcGatewayTest {

    @Test
    @Ignore
    public void successfulConnectionTest() {
        MethodName.log();
        PpbPriceCalcGateway ppcg = new PpbPriceCalcGateway();
        String className = "TestClass";
        CalcRequestCause calcRequestCause = CalcRequestCause.NEW_MATCH;
        MatchFormat matchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        MatchState matchState = new TennisMatchState(matchFormat);
        AlgoMatchParams matchParams = new TennisMatchParams();
        MatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        TennisMatchIncidentResult result =
                        new TennisMatchIncidentResult(true, true, 1, TennisMatchIncidentResultType.PREMATCH);
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        PriceCalcRequest request = new PriceCalcRequest(123L, EventSettings.generateEventSettingsForTesting(2),
                        className, calcRequestCause, matchFormat, matchState, matchParams.generateGenericMatchParams(),
                        matchIncident, result, savedState, System.currentTimeMillis());

        PriceCalcResponse response = ppcg.calculate(request);
        assertEquals(request.getUniqueRequestId(), response.getUniqueRequestId());

    }

    @Test
    @Ignore
    public void failedConnectionTest() {
        MethodName.log();

        PpbPriceCalcGateway ppcg = new PpbPriceCalcGateway();
        PpbPriceCalcGateway.setUrlPrematch("http://20.109.188.192:12348/setup");
        PpbPriceCalcGateway.setUrlInplay("http://20.109.188.192:12348/inrunning");
        String className = "TestClass";
        CalcRequestCause calcRequestCause = CalcRequestCause.NEW_MATCH;
        MatchFormat matchFormat = new TennisMatchFormat();
        MatchState matchState = new TennisMatchState(matchFormat);
        AlgoMatchParams matchParams = new TennisMatchParams();
        MatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        TennisMatchIncidentResult result =
                        new TennisMatchIncidentResult(true, true, 1, TennisMatchIncidentResultType.PREMATCH);
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        PriceCalcRequest request = new PriceCalcRequest(123L, EventSettings.generateEventSettingsForTesting(2),
                        className, calcRequestCause, matchFormat, matchState, matchParams.generateGenericMatchParams(),
                        matchIncident, result, savedState, System.currentTimeMillis());

        PriceCalcResponse response = ppcg.calculate(request);
        // assertTrue(response.getError()!=null);
        assertEquals(request.getUniqueRequestId(), response.getUniqueRequestId());

    }

}
