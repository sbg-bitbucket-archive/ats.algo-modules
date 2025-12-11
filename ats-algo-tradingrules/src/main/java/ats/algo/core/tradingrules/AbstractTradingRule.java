package ats.algo.core.tradingrules;

import java.io.Serializable;

import ats.core.AtsBean;

/**
 * base class for supporting trading rules
 * 
 * @author gicha
 *
 */
public class AbstractTradingRule extends AtsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private TradingRuleType ruleType;
    private String ruleName;
    private Integer eventTier;

    public AbstractTradingRule() {
        super();
    }

    public AbstractTradingRule(TradingRuleType ruleType, String ruleName, Integer eventTier) {
        super();
        if (ruleType == null)
            throw new IllegalArgumentException("rule type must be set");
        if (ruleName == null)
            throw new IllegalArgumentException("rule name must be set");
        this.ruleType = ruleType;
        this.ruleName = ruleName;
        this.eventTier = eventTier;
    }

    public TradingRuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(TradingRuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getEventTier() {
        return eventTier;
    }

    public void setEventTier(Integer eventTier) {
        this.eventTier = eventTier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventTier == null) ? 0 : eventTier.hashCode());
        result = prime * result + ((ruleName == null) ? 0 : ruleName.hashCode());
        result = prime * result + ((ruleType == null) ? 0 : ruleType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractTradingRule other = (AbstractTradingRule) obj;
        if (eventTier == null) {
            if (other.eventTier != null)
                return false;
        } else if (!eventTier.equals(other.eventTier))
            return false;
        if (ruleName == null) {
            if (other.ruleName != null)
                return false;
        } else if (!ruleName.equals(other.ruleName))
            return false;
        if (ruleType != other.ruleType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AbstractTradingRule [ruleType=" + ruleType + ", ruleName=" + ruleName + ", eventTier=" + eventTier
                        + "]";
    }



}
