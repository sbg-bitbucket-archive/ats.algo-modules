package ats.algo.matchengineframework;

import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsAwaitingResult;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.tradingrules.TradingRules;

/**
 * Provides the top level interface to the pricing model for a particular sport. Apart from getters and setters there is
 * one method "calculate()" that does all the work.
 * 
 * Before the calculate method is invoked the following properties will be set by the framework: calcRequestCause,
 * matchState, matchParams, calcRequestCause, matchIncident, matchIncidentResult, matchEngineSavedState
 * 
 * The following properties must be set by calculate method: calculatedMarkets
 * 
 * If CalcRequestCause is not PARAM_FIND then the following properties may (but do no have to) be updated by the
 * calculate method: matchParams, matchEngineSavedState.
 * 
 * If CalcRequestCause is PARAM_FIND then any changes to matchParams, matchEngineSavedState are ignored
 * 
 * All the state info that the calculate() method needs to use must be represented within one of the above objects.
 * Successive calls to the calculate method may be executed on different instances of the MatchEngine.
 * 
 * @author Geoff
 * 
 */
public abstract class MatchEngine {

    protected MatchState matchState;
    protected MatchParams matchParams;
    protected TradingRules tradingRules;
    private CalcRequestCause calcRequestCause;
    private MatchIncident matchIncident;
    private MatchIncidentResult matchIncidentResult;
    protected MatchEngineSavedState matchEngineSavedState;
    private final SupportedSportType supportedSportType;
    protected long eventTier;

    protected Markets calculatedMarkets;

    private boolean paramFindStdDevns;

    /**
     * Class constructor.
     * 
     * @param supportedSportType - identifies the sport this model represents
     */
    public MatchEngine(SupportedSportType supportedSportType) {
        this.matchEngineSavedState = new MatchEngineSavedState();
        paramFindStdDevns = false;
        this.supportedSportType = supportedSportType;
    }

    /**
     * Returns the skill parameters being used by the engine.
     * 
     * @return matchParams - possibly updated by the calculate method
     */
    public MatchParams getMatchParams() {
        return this.matchParams;
    }

    /**
     * returns a copy of the object holding the match state being used by the engine.
     * 
     * @return
     */
    public MatchState getMatchState() {
        return this.matchState;
    };

    /**
     * Expected to be for Algo Framework use only. Sets the state of the instance of matchState being used by the engine
     * to the state of the supplied object.
     * 
     * @param matchState
     */
    public void setMatchState(MatchState matchState) {
        this.matchState.setEqualTo(matchState);
    }

    /**
     * Expected to be for Algo Framework use only. Sets the state of the instance of matchParams being used by the
     * engine to the state of the supplied object
     * 
     * @param matchParams
     */
    public void setMatchParams(MatchParams matchParams) {
        this.matchParams.setEqualTo(matchParams);
    }

    /**
     * gets the matchIncident that lead to the calculation request. Only relevant for CalcRequestCause of type
     * MATCH_INCIDENT
     * 
     * @return the matchIncident, or null if CalcRequestCause is not MATCH_INCIDENT
     */
    public MatchIncident getMatchIncident() {
        return matchIncident;
    }

    /**
     * Expected to be for Algo Framework use only. Sets the matchIncident
     * 
     * @param matchIncident
     */
    public void setMatchIncident(MatchIncident matchIncident) {
        this.matchIncident = matchIncident;
    }

    /**
     * Gets the result of the MatchIncident given the MatchState. e.g. if MatchIncident is POINT_WON,
     * MATCHINCIDENTRESULT might be SET_WON. May be set to null if the MatchIncident is of a type that does not affect
     * any sport-specific properties of MatchState - for example a datafeed matchIncident.
     * 
     * @return
     */
    public MatchIncidentResult getMatchIncidentResult() {
        return matchIncidentResult;
    }

    /**
     * Expected to be for Algo Framework use only. Sets the MatchIncidentResult
     * 
     * @param matchIncidentResult
     */
    public void setMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        this.matchIncidentResult = matchIncidentResult;
    }

    /**
     * Expected to be for Algo Framework use only. Controls the behaviour of param finding. If set to true then param
     * finding will try to find best fit values of stdDevns as well as means in MatchParams. By default is set to false.
     * Should be set to true if pricing model makes of of the stdDevn property of each MatchParam
     * 
     * @return
     */
    public boolean isParamFindStdDevns() {
        return paramFindStdDevns;
    }

    /**
     * 
     * Controls the behaviour of param finding. If set to true then param finding will try to find best fit values of
     * stdDevns as well as means in MatchParams. By default is set to false. Should be set to true if pricing model
     * makes of of the stdDevn property of each MatchParam
     *
     * @param paramFindStdDevns
     */
    public void setParamFindStdDevns(boolean paramFindStdDevns) {
        this.paramFindStdDevns = paramFindStdDevns;
    }

    public void setEventTier(long eventTier) {
        this.eventTier = eventTier;
    }

    public long getEventTier() {
        return eventTier;
    }

    /**
     * Before the calculate method is invoked the following properties will be set by the framework: calcRequestCause,
     * matchState, matchParams, calcRequestCause, matchIncident, matchIncidentResult, matchEngineSavedState
     * 
     * The following properties must be set by calculate method: calculatedMarkets
     * 
     * If CalcRequestCause is not PARAM_FIND then the following properties may (but do not have to) be updated by the
     * calculate method: matchParams, matchEngineSavedState.
     * 
     * If CalcRequestCause is PARAM_FIND then any changes to matchParams, matchEngineSavedState are ignored
     * 
     * 
     * @return
     */
    public abstract void calculate();

    /**
     * Expected to be for Algo Framework use only. Results the set of supplied markets based on the current matchState
     * should be overridden by any sport that results markets
     * 
     * @return
     */
    public abstract ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState,
                    MatchState currentMatchState);

    /**
     * Expected to be for Algo Framework use only. Results the set of supplied markets given that the match has been
     * abandoned. The default behaviour is to void all markets that have not yet been resulted.
     * 
     * May be overridden for sports where other behaviour is required.
     * 
     * @param markets *
     * @param mostRecentMatchState
     * @return
     */
    public ResultedMarkets resultMarketsForAbandonedEvent(Markets markets, MatchState mostRecentMatchState) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        for (Market market : markets) {
            ResultedMarket resultedMarket = voidMarket(market);
            resultedMarkets.addMarket(resultedMarket);

        }
        return resultedMarkets;
    }

    protected ResultedMarket voidMarket(Market market) {
        ResultedMarket resultedMarket = new ResultedMarket(market.getType(), market.getLineId(), market.getCategory(),
                        market.getSequenceId(), true, market.getMarketDescription(), "", 0);
        return resultedMarket;
    }

    /**
     * Expected to be for Algo Framework use only. Should be overridden to deliver a sport specific set of trading
     * rules. If not overridden will return an empty set
     * 
     * @return
     */
    public TradingRules getTradingRuleSet() {
        return new TradingRules();
    }

    /**
     * Expected to be for Algo Framework use only. Gets the sport type associated with this matchEngine
     * 
     * @return
     */
    public SupportedSportType getSupportedSportType() {
        return supportedSportType;
    }

    /**
     * Gets the object which may be used to store any state info between successive calls to the calculate method
     * 
     * @return
     */
    public MatchEngineSavedState getMatchEngineSavedState() {
        return matchEngineSavedState;
    }

    /**
     * Sets the object which may be used to store any state info between successive calls to the calculate method
     * 
     * @param matchEngineSavedState
     */
    public void setMatchEngineSavedState(MatchEngineSavedState matchEngineSavedState) {
        this.matchEngineSavedState = matchEngineSavedState;
    }

    /**
     * gets the reason why the calculate method was invoked
     * 
     * @return
     */
    public CalcRequestCause getCalcRequestCause() {
        return calcRequestCause;
    }

    /**
     * Expected to be for Algo Framework use only. Sets the reason why the calculate method was invoked
     * 
     * @param calcRequestCause
     */
    public void setCalcRequestCause(CalcRequestCause calcRequestCause) {
        this.calcRequestCause = calcRequestCause;
    }

    /**
     * Expected to be for Algo Framework use only. Gets the set of markets generated by the calculate method
     * 
     * @return
     */
    public Markets getCalculatedMarkets() {
        return calculatedMarkets;
    }

    /**
     * Sets the set of markets generated by the calculate method
     * 
     * @param markets
     */
    public void setCalculatedMarkets(Markets markets) {
        this.calculatedMarkets = markets;
    }

    /**
     * Expected to be for Algo Framework use only. Gets the matchParams that may have been updated by the calculate
     * method
     * 
     * @return
     */
    public MatchParams getUpdatedMatchParams() {
        return matchParams;
    }

    /*
     * This method gets called if the class is instantiated via a system property . Can be overriden
     */
    public void setMatchFormat(MatchFormat matchFormat) {

    }

    /**
     * May be overridden by clients who want to take responsibility for their own param finding
     * 
     * @param paramFindRequest container to hold all the data that may be required by the param finding process
     * @return
     */
    public ParamFindResponse paramFind(ParamFindRequest paramFindRequest) {
        return null;
    }

    // FIXME: TO BE IMPLEMENTED HERE THAN IN INDIVIDUAL SPORTS ENGINES
    public void cleanUpMarketAwaitingResultSelections(MarketsAwaitingResult marketsAwaitingResult,
                    ResultedMarket resultedBaseMarket) {
        // TODO Auto-generated method stub
    }

    /**
     * May be overridden to provide sport-specific preprocessing before executing the generic param find
     * 
     * @param matchParams On entry contains the input matchPArams. On exit may be modified to update some or all param
     *        values
     * @param marketPricesList On entry the marketPrices to param find against. On exit some prices may have been
     *        modified or removed
     * @return
     */
    public Set<MarketGroup> preprocessParamFind(MatchParams matchParams, MarketPricesList marketPricesList) {
        return null;
    }


}
