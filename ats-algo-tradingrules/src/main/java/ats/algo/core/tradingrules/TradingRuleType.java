
package ats.algo.core.tradingrules;


/**
 * the various possible types of trading rule
 * 
 * @author Geoff
 *
 */
public enum TradingRuleType {
    SET_MARKET_SUSPENSION_STATUS,
    RESET_BIAS_FOLLOWING_PARAM_FIND,
    CREATE_DERIVED_MARKETS,
    TRIGGER_PARAM_FIND,
    MONITOR_FEED,
    TRADER_ALERT,
    SUSPEND_TO_AWAIT_PARAM_FIND,
    PROPERTY_CHANGES,
    ABANDON_MATCH_RESULTING_AND_SUSPENSION_MARKETS_RULE
}
