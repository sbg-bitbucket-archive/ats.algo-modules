package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Markets;
import ats.algo.gateway.ppb.PpbPriceCalcGateway;
import ats.algo.gateway.ppb.PpbSavedState;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;

@SuppressWarnings("unused")
public class PpbPriceCalcRequestResponseTest {

    @Test
    public void testGeneratedJson() {
        MethodName.log();
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        TennisMatchState matchState = new TennisMatchState();
        PpbSavedState ppbSavedState = new PpbSavedState();
        TennisMatchParams tennisMatchParams = new TennisMatchParams();
        tennisMatchParams.setDoublesMatch(false);
        GenericMatchParams matchParams = PpbTennisMatchParams
                        .generatePpbGenericMatchParams(tennisMatchParams.generateGenericMatchParams(), matchFormat);
        PpbPriceCalcGateway ppbPriceCalcGateway = new PpbPriceCalcGateway();
        Markets markets = PpbPriceCalcGateway.generateMarkets(1, matchState, matchFormat);
        PpbTennisPriceCalcRequest ppbRequest = ppbPriceCalcGateway.generatePpbPriceCalcRequest(123L, "R_375",
                        matchFormat, matchState, matchParams, markets, ppbSavedState);
        String jsonStr = JsonSerializer.serialize(ppbRequest, true);
        // System.out.println(jsonStr);

    }

    /*
     * This is what the serialised request looks like 13 Apr 18 following the change to use ppbMarket {
     * "PpbTennisPriceCalcRequest" : { "eventId" : 123, "requestId" : "R_375", "ppbMatchFormat" : { "matchType" :
     * "singles", "surface" : "hard", "matchTeamsGender" : "male", "advantagePlayedNormalGame" : true,
     * "advantagePlayedTieBreakGame" : true, "numSets" : 3, "lastSetFormat" : "Tiebreaks in all sets",
     * "gamesInSetFirstTo" : 6, "tieBreakPlayedAfterXGames" : 6, "pointsInTieBreakFirstTo" : 7, "pointsInRaceSetFirstTo"
     * : 10, "tournamentType" : "atp_wta" }, "ppbMatchState" : { "currentServeNumber" : 1, "startOfMatchServer" :
     * "not_set", "nextPointServerGender" : "male", "nextPointIsPowerPoint" : false }, "ppbMatchParams" : {
     * "teamAPreMatchLine" : 0.5, "teamBPreMatchLine" : 0.5, "teamAPlayerRank" : 0, "teamBPlayerRank" : 0,
     * "teamAPlayerId" : "Unknown", "teamBPlayerId" : "Unknown", "usePlayerParameters" : false, "serveQuality" : 0,
     * "traderConfidence" : "medium", "aceQuality" : 0, "doubleFaultQualityA" : 0, "doubleFaultQualityB" : 0,
     * "biasPlayerA" : 0 }, "ppbMarkets" : { "markets" : { "FT:ML_M" : { "valid" : true, "type" : "FT:ML",
     * "marketDescription" : "Match betting", "sequenceId" : "M", "selections" : { "A" : 0.0, "B" : 0.0 },
     * "selectionNameOverOrA" : "A", "selectionNameUnderOrB" : "B", "marketGroup" : "NOT_SPECIFIED", "marketStatus" : {
     * "suspensionStatus" : "OPEN", "suspensionStatusRuleName" : "Default", "suspensionStatusReason" : "Default" },
     * "doNotResultThisMarket" : false, "filterMarketByClient" : true, "lineBase" : 0, "category" : "GENERAL" } } },
     * "requestTime" : 0, "versionId" : "PPB_1.0.0" } }
     * 
     * 
     */

}
