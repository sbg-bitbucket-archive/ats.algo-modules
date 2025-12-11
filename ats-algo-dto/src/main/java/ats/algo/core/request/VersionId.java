package ats.algo.core.request;


/**
 * version history:
 * 
 * 1.0.0 - first version
 * 
 * 1.0.1 - add Boolean "requestParamFind" to PriceCalcResponse
 * 
 * 1.0.2 - refactor subType to lineId in ResultedMarket
 * 
 * @author gicha
 * 
 *         1.1.0 - various changes - c.f. email of 6/6/17
 * 
 *         1.2.0 - 16/7/17:
 * 
 *         1. add "doNotSchedulePriceCalc" property to ParamFindResponse *
 * 
 *         2. add "timeStamp" to MarketPrice
 * 
 *         3. Standardise on use of subclass property for polymorphic classes
 *
 *         1.2.1 - 16/8/17 add triggerParamFindAsap boolean to PriceCalcResponse
 *
 *         1.2.2 - 09/09/17 two changes:
 *
 *         1. xxxMatchStateFromFeed in xxxMatchIncident now derives from common base class MatchStateFromFeed - changes
 *         json representation
 * 
 *         2. new property now serialized as part of PriceCalcRequest and ParamFindRequest: matchEngineClassName
 * 
 *         1.2.3 - 10/11/17:
 *
 *         1. Add optional EventSettings property to PriceCalcResponse 2. Add serverId field to PriceCalcResponse
 * 
 * 
 *         2.0.0 - 2/12/17:
 *
 *         Tidy up API: PriceCalcRequest requestId - remove ParamFindRequest requestId - remove MatchParams requestId -
 *         remove requestTime - remove Markets eventId - remove requestId - remove requestTime - remove eventTier -
 *         remove supportedSportType - remove
 * 
 *         MarketPricesList eventId - remove requestId - remove
 * 
 *         MatchIncident requestId -> incidentId
 * 
 *         MatchState eventId - remove requestId -> incidentId
 * 
 *         ResultedMarkets eventId - remove requestId -> incidentId incidentId - add
 * 
 *         ParamFindResults eventId - remove requestId - remove requestTime - remove
 * 
 *         Modify CalcReqestCause - add MATCH_RESULT
 * 
 *         Add new property to PriceCalcRequest: - MatchResult
 * 
 * 
 *         2.0.1 - 09/02/2018
 * 
 *         Add new ResultedMarketsMatchIncident class required to support Abelson feed
 * 
 *
 *         2.0.2 - 15/02/2018
 *
 *         Add new marketGroupsToSuspend property to PramFindResult
 * 
 *         2.1.0 - 09/03/2018
 *
 *         change selections property of Market from Map<String,double> to Map<String,Selection
 *
 *         2.1.1 - 22/03/2018
 *
 *         add voidedSelections field to the ResultedMarket class
 *
 */
public class VersionId {
    public static final String ID = "2.1.1";


}
