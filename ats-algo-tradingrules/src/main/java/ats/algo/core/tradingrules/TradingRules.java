package ats.algo.core.tradingrules;

import java.util.ArrayList;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;

import ats.core.AtsBean;

/**
 * holds the set of rules that apply to a particular sport (or that apply across all sports)
 * 
 * @author Geoff
 *
 */
public class TradingRules extends AtsBean {

    protected ArrayList<AbstractTradingRule> tradingRulesForSport;

    public ArrayList<AbstractTradingRule> getTradingRules() {
        return tradingRulesForSport;
    }

    @JsonIgnore
    public Set<Class<? extends AbstractTradingRule>> getRuleTypes() {
        Set<Class<? extends AbstractTradingRule>> rulesTypes = Sets.newHashSet();
        for (AbstractTradingRule tr : tradingRulesForSport) {
            rulesTypes.add(tr.getClass());
        }
        return rulesTypes;
    }

    public void setTradingRules(ArrayList<AbstractTradingRule> tradingRules) {
        tradingRulesForSport = tradingRules;
    }

    public TradingRules() {
        tradingRulesForSport = new ArrayList<AbstractTradingRule>();
    }

    /**
     * add a new rule for the specified sport
     * 
     * @param supportedSportType if null rule applies to all sports
     * @param tradingRule
     */
    public void addRule(AbstractTradingRule tradingRule) {
        tradingRulesForSport.add(tradingRule);
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tradingRulesForSport == null) ? 0 : tradingRulesForSport.hashCode());
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
        TradingRules other = (TradingRules) obj;
        if (tradingRulesForSport == null) {
            if (other.tradingRulesForSport != null)
                return false;
        } else if (!tradingRulesForSport.equals(other.tradingRulesForSport))
            return false;
        return true;
    }

    @Override
    public String toString() {
        String s = "TradingRules [\n";
        for (AbstractTradingRule rule : tradingRulesForSport) {
            s += "    " + rule.toString() + "\n";
        }
        s += "]";
        return s;
    }
}
