package ats.algo.core.tradingrules;

import java.util.ArrayList;
import java.util.Map;

import ats.algo.core.abandonIncident.tradingrules.AbandonIncidentTradingRule;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleResult;
import ats.algo.core.resetbias.tradingrules.ResetBiasTradingRule;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.tradingrules.TraderAlertTradingRule;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.generic.tradingrules.GenericTradingRuleSet;
import ats.core.AtsBean;

/**
 * for internal use only
 * 
 * @author Geoff
 *
 */
public class TradingRulesManager extends AtsBean {

    private TradingRulesRepository tradingRulesRepository;
    private TriggerParamFindTradingRule defaultTriggerParamFindTradingRule;

    public TradingRulesManager() {
        tradingRulesRepository = new TradingRulesRepository();
        defaultTriggerParamFindTradingRule =
                        TriggerParamFindTradingRule.generatePriceDistanceVariant(this.getClass().getName(), null);
    }

    /**
     * sets the trading rules associated with a particular sport, or that apply across all sports
     *
     * @param supportedSportType null if applies to all sports
     * @param tradingRuleArray
     */
    public void setTradingRules(SupportedSportType supportedSportType, AbstractTradingRule[] tradingRuleArray) {
        tradingRulesRepository.setTradingRules(supportedSportType, tradingRuleArray);
    }

    /**
     * sets the trading rules associated with a particular sport, or that apply across all sports
     *
     * @param supportedSportType null if applies to all sports
     * @param tradingRuleSet
     */
    public void setTradingRules(SupportedSportType supportedSportType, TradingRules tradingRuleSet) {
        tradingRulesRepository.setTradingRules(supportedSportType, tradingRuleSet);
    }

    public void setGenericTradingRules(TradingRules genericTradingRuleSet) {
        tradingRulesRepository.setTradingRules(null, genericTradingRuleSet);

    }

    /**
     * applies the trading rules to each of the supplied markets
     *
     * @param matchState
     * @param markets
     */
    public void applyUpdateMarketsPostPriceCalcRules(long eventTier, SupportedSportType sportType,
                    MatchState matchState, Markets markets, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {

        for (Market market : markets) {
            /*
             * get the set of rules that are relevant to this market and apply them in the correct order
             */
            ArrayList<SetSuspensionStatusTradingRule> rules = tradingRulesRepository.getMarketSuspensionRules(sportType,
                            eventTier, market.getMarketGroup());

            for (SetSuspensionStatusTradingRule rule : rules) {
                rule.applyRule(matchState, market, priceCalcCause, triggerParamFindData);
            }
        }
    }



    public void applyDerivedMarketRules(int eventTier, SupportedSportType sportType, MatchState matchState,
                    Markets markets) {
        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(sportType,
                        TradingRuleType.CREATE_DERIVED_MARKETS, eventTier);
        for (Market market : markets) {
            /*
             * reset status of all markets to the default
             */
            market.getDerivedMarketsInfo().reset();
            /*
             * get the set of rules that are relevant to this market and apply them in the correct order
             */
            for (AbstractTradingRule rule : rules) {
                ((DerivedMarketTradingRule) rule).applyDerivedMarketRule(matchState, market);
            }
            ArrayList<AbstractTradingRule> rules2 = tradingRulesRepository.getApplicableRules(sportType,
                            TradingRuleType.SET_MARKET_SUSPENSION_STATUS, eventTier);
            boolean suspendSingleSelectionOn = true;
            for (AbstractTradingRule rule : rules2) {
                if (SetSuspensionStatusTradingRule.class.isAssignableFrom(rule.getClass())) {
                    suspendSingleSelectionOn = suspendSingleSelectionOn
                                    && ((SetSuspensionStatusTradingRule) rule).isSuspendOnlyOneSelectionMarkets();
                }
            }
            // System.out.println( ((TradingRules) rules.get(0)).suspendWhenOnlyOneSelection);
            if (market.getSelectionsProbs().size() == 1 && suspendSingleSelectionOn)
                market.getMarketStatus().setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
        }

    }

    public void applyResultingAbandonRules(int eventTier, SupportedSportType sportType, MatchState matchState,
                    Markets markets) {
        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(sportType,
                        TradingRuleType.ABANDON_MATCH_RESULTING_AND_SUSPENSION_MARKETS_RULE, eventTier);
        for (AbstractTradingRule rule : rules) {
            ((AbandonIncidentTradingRule) rule).applyPublishSuspendMarketRule(markets);

        }
    }

    /**
     * applies any RESET_BIAS_FOLLOWING_PARAM_FIND trading rules for this sport to matchParams
     * 
     * @param sportType
     * @param matchState
     * @param matchParams may be modified on exit - bias may be reset for each param
     */
    public void applyResetBiasFollowingParamFindRules(SupportedSportType sportType, MatchState matchState,
                    MatchParams matchParams) {
        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(sportType,
                        TradingRuleType.RESET_BIAS_FOLLOWING_PARAM_FIND, null);
        for (AbstractTradingRule rule : rules) {
            ((ResetBiasTradingRule) rule).applyResetBiasRuleToMatchParams(matchState, matchParams);
        }
    }

    /**
     * applies any TRIGGER_PARAM_FIND trading rules. If any rule returns a non-null MarketPricesList then no further
     * rules are triggered and that set of MarketPrices is returned
     * 
     * if there are no sport specific trading rules set up then an instance of the DefaultTriggerParamFindTradingRule is
     * executed
     * 
     * note - this method assumes that there is not a param find currently in progress, so shld check for that before
     * calling
     * 
     * @param sportType
     * @param matchState
     * @param markets
     * @param isBiasedParams
     * @param initiateParamFindData
     * @param eventSettings
     * @param triggerParamFindAsap
     * @param eventSuspendDueToDisaster, if eventSuspendDueToDisaster is true, trigger param find always when reconnect
     * @return a MarketPricesList object if a param find should be scheduled, otherwise null
     */
    public MarketPricesList applyTriggerParamFindRequiredRules(SupportedSportType sportType, MatchState matchState,
                    Markets markets, boolean isBiasedParams, TriggerParamFindData initiateParamFindData,
                    EventSettings eventSettings, boolean triggerParamFindAsap, boolean eventSuspendDueToDisaster) {
        int eventTier = (int) eventSettings.getEventTier();
        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(sportType,
                        TradingRuleType.TRIGGER_PARAM_FIND, eventTier);
        if (rules.size() == 0) {
            /*
             * no sport-specific rules found, so use the default rule
             */
            rules.add(defaultTriggerParamFindTradingRule);
        }
        for (AbstractTradingRule rule : rules) {
            MarketPricesList marketPricesList;
            if (eventSuspendDueToDisaster) {
                info("EventID: " + matchState.getEventId() + ". Param finding triggered after connection disaster.");
                marketPricesList = ((TriggerParamFindTradingRule) rule).shouldRequestParamFindAfterDiaster(matchState,
                                markets, isBiasedParams, initiateParamFindData, eventSettings, triggerParamFindAsap);
            } else
                marketPricesList = ((TriggerParamFindTradingRule) rule).shouldRequestParamFind(matchState, markets,
                                isBiasedParams, initiateParamFindData, eventSettings, triggerParamFindAsap);



            if (marketPricesList != null)
                return marketPricesList;
        }
        return null;
    }



    public Map<String, ArrayList<AbstractTradingRule>> getRulesList() {
        return this.tradingRulesRepository.getTradingRules();
    }

    public void setDefaultTradingRules(TradingRulesManager tradingRulesManager) {
        tradingRulesManager.setGenericTradingRules(new GenericTradingRuleSet());
    }

    /**
     * returns the rules as a json string
     *
     * @return
     */
    public String getRulesAsJson() {
        String json = JsonSerializer.serialize(this.tradingRulesRepository.getTradingRules().entrySet(), true);
        return json;
    }

    /**
     * 
     * @param supportedSport
     * @param now
     * @param timeOfLastMatchIncident
     * @param timeOfLastPriceUpdate
     * @return null if no action to be taken, else the suspension status. Non null will be returned if any rule returns
     *         a result.
     */
    public MonitorFeedTradingRuleResult applyEventSuspensionRules(SupportedSportType supportedSport, long now,
                    boolean eventLevelSuspensionInForce, long timeOfLastMatchIncident, long timeOfLastPriceUpdate,
                    MatchState matchState, EventSettings eventSettings) {

        ArrayList<AbstractTradingRule> rules =
                        tradingRulesRepository.getApplicableRules(supportedSport, TradingRuleType.MONITOR_FEED, null);

        for (AbstractTradingRule rule : rules) {
            MonitorFeedTradingRuleResult result = ((MonitorFeedTradingRule) rule).applyRule(now,
                            timeOfLastMatchIncident, timeOfLastPriceUpdate, matchState, eventSettings);
            if (result != null)
                return result;
        }
        return null;

    }

    public TraderAlert applyTraderAlertRules(SupportedSportType supportedSport, MatchState matchState,
                    MatchIncident matchIncident) {

        ArrayList<AbstractTradingRule> rules =
                        tradingRulesRepository.getApplicableRules(supportedSport, TradingRuleType.TRADER_ALERT, null);

        for (AbstractTradingRule rule : rules) {
            TraderAlert result = ((TraderAlertTradingRule) rule).shouldCreateTraderAlertFromIncident(matchIncident,
                            matchState);
            if (result != null)
                return result;
        }
        return null;
    }

    public TraderAlert applyTraderAlertRules(SupportedSportType supportedSport, MatchState currentMatchState,
                    MatchState previousMatchState, MatchIncident matchIncident, MatchFormat matchFormat) {

        ArrayList<AbstractTradingRule> rules =
                        tradingRulesRepository.getApplicableRules(supportedSport, TradingRuleType.TRADER_ALERT, null);

        for (AbstractTradingRule rule : rules) {
            TraderAlert result = ((TraderAlertTradingRule) rule).shouldCreateTraderAlertFromIncident(matchIncident,
                            currentMatchState, previousMatchState, matchFormat);
            if (result != null)
                return result;
        }
        return null;
    }

    public boolean applySuspendToAwaitParamFindRules(SupportedSportType supportedSport, MatchState matchState,
                    MatchIncident matchIncident) {
        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(supportedSport,
                        TradingRuleType.SUSPEND_TO_AWAIT_PARAM_FIND, null);
        for (AbstractTradingRule rule : rules) {
            boolean result = ((SuspendToAwaitParamFindTradingRule) rule).shouldSuspendToAwaitParamFind(matchIncident,
                            matchState);
            if (result)
                return result;
        }
        return false;
    }

    public Map<String, String> applySetPropertyChangeTradingRules(SupportedSportType supportedSport,
                    MatchState matchState, MatchState previousMatchState, MatchIncident matchIncident,
                    EventSettings eventSettings) {

        ArrayList<AbstractTradingRule> rules = tradingRulesRepository.getApplicableRules(supportedSport,
                        TradingRuleType.PROPERTY_CHANGES, null);

        for (AbstractTradingRule rule : rules) {
            Map<String, String> properties = ((PropertyChangeTraderRules) rule).shouldUpdateProperties(matchIncident,
                            matchState, previousMatchState, eventSettings);
            if (properties != null)
                return properties;
        }
        return null;
    }
}


