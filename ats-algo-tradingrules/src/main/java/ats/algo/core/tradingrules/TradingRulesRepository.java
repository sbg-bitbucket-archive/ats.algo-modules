package ats.algo.core.tradingrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.SupportedSportType;

/**
 * For internal use only - Holds the set of trading rules
 */
class TradingRulesRepository {

    Map<String, ArrayList<AbstractTradingRule>> tradingRules;

    TradingRulesRepository() {
        tradingRules = new HashMap<String, ArrayList<AbstractTradingRule>>();
    }

    /**
     * sets the trading rules associated with a particular sport, or that apply across all sports
     * 
     * @param supportedSportType null if applies to all sports
     * @param tradingRuleSet
     */
    void setTradingRules(SupportedSportType supportedSportType, TradingRules tradingRuleSet) {
        String key = SupportedSportType.getName(supportedSportType);
        tradingRules.put(key, tradingRuleSet.getTradingRules());
    }

    /**
     * sets the trading rules associated with a particular sport, or that apply across all sports
     * 
     * @param supportedSportType null if applies to all sports
     * @param tradingRuleArray
     */
    void setTradingRules(SupportedSportType supportedSportType, AbstractTradingRule[] tradingRuleArray) {
        ArrayList<AbstractTradingRule> tradingRuleList = new ArrayList<AbstractTradingRule>(tradingRuleArray.length);
        for (int i = 0; i < tradingRuleArray.length; i++)
            tradingRuleList.add(tradingRuleArray[i]);
        String key = SupportedSportType.getName(supportedSportType);
        tradingRules.put(key, tradingRuleList);
    }

    /**
     * gets the set of rules which should be applied to markets with the specified properties and returns them as a list
     * 
     * @param sportType
     * @param eventTier
     * @param marketGroup
     * @return
     */
    ArrayList<SetSuspensionStatusTradingRule> getMarketSuspensionRules(SupportedSportType sportType, long eventTier,
                    MarketGroup marketGroup) {

        ArrayList<SetSuspensionStatusTradingRule> destRules = new ArrayList<SetSuspensionStatusTradingRule>();
        ArrayList<AbstractTradingRule> sourceRules = tradingRules.get(SupportedSportType.getNameForNotSportSpecific());
        if (sourceRules != null) {
            for (AbstractTradingRule rule : sourceRules) {
                if (rule.getRuleType() == TradingRuleType.SET_MARKET_SUSPENSION_STATUS)
                    if (includeRule(eventTier, marketGroup, (SetSuspensionStatusTradingRule) rule))
                        destRules.add((SetSuspensionStatusTradingRule) rule);
            }
        }
        sourceRules = tradingRules.get(SupportedSportType.getName(sportType));
        if (sourceRules != null) {
            for (AbstractTradingRule rule : sourceRules) {
                if (rule.getRuleType() == TradingRuleType.SET_MARKET_SUSPENSION_STATUS)
                    if (includeRule(eventTier, marketGroup, (SetSuspensionStatusTradingRule) rule))
                        destRules.add((SetSuspensionStatusTradingRule) rule);
            }
        }
        return destRules;
    }

    /**
     * returns the list of rules that match the params.
     * 
     * @param sportType
     * @param tradingRuleType
     * @param eventTier may be null. If so rules for all eventTiers are returned
     * @return list of applicable rules - may be empty
     */
    public ArrayList<AbstractTradingRule> getApplicableRules(SupportedSportType sportType,
                    TradingRuleType tradingRuleType, Integer eventTier) {
        ArrayList<AbstractTradingRule> destRules = new ArrayList<AbstractTradingRule>();
        ArrayList<AbstractTradingRule> sourceRules = tradingRules.get(SupportedSportType.getNameForNotSportSpecific());
        if (sourceRules != null) {
            for (AbstractTradingRule rule : sourceRules) {
                if (rule.getRuleType() == tradingRuleType && eventTiersMatch(eventTier, rule))
                    destRules.add(rule);
            }
        }
        sourceRules = tradingRules.get(SupportedSportType.getName(sportType));
        if (sourceRules != null) {
            for (AbstractTradingRule rule : sourceRules) {
                if (rule.getRuleType() == tradingRuleType && eventTiersMatch(eventTier, rule))
                    destRules.add(rule);
            }
        }
        return destRules;
    }

    private boolean eventTiersMatch(Integer eventTier, AbstractTradingRule rule) {
        if (rule.getEventTier() == null || eventTier == null)
            return true;
        return (rule.getEventTier().equals(eventTier));
    }

    private boolean includeRule(long eventTier, MarketGroup marketGroup, SetSuspensionStatusTradingRule rule) {
        Integer ruleEventTier = rule.getEventTier();
        MarketGroup ruleMarketGroup = rule.getMarketGroup();
        boolean addRule = true;
        addRule &= ruleEventTier == null || ruleEventTier == eventTier;
        addRule &= ruleMarketGroup == null || ruleMarketGroup == marketGroup;
        return addRule;
    }

    // private void printRules() {
    // for (Entry<String, ArrayList<TradingRule> > e: tradingRules.entrySet()) {
    // System.out.printf ("TradingRuleSet for : %s\n", e.getKey());
    // for (TradingRule rule : e.getValue())
    // System.out.printf(" " + rule.toString() +"\n");
    // }
    // }

    public Map<String, ArrayList<AbstractTradingRule>> getTradingRules() {
        return tradingRules;
    }



}
