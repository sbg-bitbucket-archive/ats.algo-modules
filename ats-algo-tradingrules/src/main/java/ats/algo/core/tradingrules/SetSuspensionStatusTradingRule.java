package ats.algo.core.tradingrules;

import java.io.Serializable;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;

/**
 * container for the data structure that holds the details of each trading rule
 * 
 * @author
 *
 */
public abstract class SetSuspensionStatusTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * these properties define what the rule applies to. If a property is set to null then the rule applies to all. e.g.
     * eventTier = null means applies to all eventTiers
     */

    private MarketGroup marketGroup;
    private boolean suspendOnlyOneSelectionMarkets = true;

    /**
     * default constructor
     */
    public SetSuspensionStatusTradingRule() {}

    /**
     * Deprecated - marketTier property no longer does anything. Use other form of constructor
     * 
     * @param ruleType The type of rule - e.g. OPEN_SUSPENDED or LINES_TO_DISPLAY
     * @param ruleName The name that will appear in logs and in the MarketStatus object when the rule is applied - must
     *        be set
     * @param eventTier the eventTier this rule applies to - null means applies to all tiers;
     * @param marketGroup - the marketGroup this rule applies to - null means applies to all marketGroups;
     * @param marketTier - marketTier this rule applies to - null means applies to all tiers;
     */
    @Deprecated
    protected SetSuspensionStatusTradingRule(TradingRuleType ruleType, String ruleName, Integer eventTier,
                    MarketGroup marketGroup, Integer marketTier) {
        this(ruleType, ruleName, eventTier, marketGroup);

    }

    /**
     * 
     * @param ruleType The type of rule - e.g. OPEN_SUSPENDED or LINES_TO_DISPLAY
     * @param ruleName The name that will appear in logs and in the MarketStatus object when the rule is applied - must
     *        be set
     * @param eventTier the eventTier this rule applies to - null means applies to all tiers;
     * @param marketGroup - the marketGroup this rule applies to - null means applies to all marketGroups;
     */

    protected SetSuspensionStatusTradingRule(TradingRuleType ruleType, String ruleName, Integer eventTier,
                    MarketGroup marketGroup) {
        super(ruleType, ruleName, eventTier);
        this.marketGroup = marketGroup;
    }

    /**
     * gets the MarketGroup this rule applies to - if null applies to all market groups
     * 
     * @return
     */
    public MarketGroup getMarketGroup() {
        return marketGroup;
    }

    /**
     * sets the MarketGroup this rule applies to - if null applies to all market groups
     * 
     * @param marketGroup
     */
    public void setMarketGroup(MarketGroup marketGroup) {
        this.marketGroup = marketGroup;
    }



    public boolean isSuspendOnlyOneSelectionMarkets() {
        return suspendOnlyOneSelectionMarkets;
    }

    public void setSuspendOnlyOneSelectionMarkets(boolean suspendOnlyOneSelectionMarkets) {
        this.suspendOnlyOneSelectionMarkets = suspendOnlyOneSelectionMarkets;
    }

    /**
     * applies this rule to the supplied market
     * 
     * @param matchState the current matchState
     * @param market - the market to which the rule is to be applied. MarketStatus is updated on exit
     */
    public abstract void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((marketGroup == null) ? 0 : marketGroup.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SetSuspensionStatusTradingRule other = (SetSuspensionStatusTradingRule) obj;
        if (marketGroup != other.marketGroup)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "UpdateMarketsPostPriceCalcTradingRule [marketGroup=" + marketGroup + ", getRuleType()=" + getRuleType()
                        + ", getRuleName()=" + getRuleName() + ", getEventTier()=" + getEventTier() + ","
                        + ", isSuspendOnlyOneSelectionMarkets()=" + isSuspendOnlyOneSelectionMarkets()
                        + super.toString() + "]";
    }



}
