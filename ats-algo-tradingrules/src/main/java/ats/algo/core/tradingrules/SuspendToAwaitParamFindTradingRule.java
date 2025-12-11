package ats.algo.core.tradingrules;

import java.io.Serializable;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;

/**
 * container for the data structure that holds the details of each trading rule
 * 
 * @author
 *
 */
public abstract class SuspendToAwaitParamFindTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param ruleName
     * @param eventTier
     */
    public SuspendToAwaitParamFindTradingRule(String ruleName, Integer eventTier) {
        super(TradingRuleType.SUSPEND_TO_AWAIT_PARAM_FIND, ruleName, eventTier);
    }

    public abstract boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState);

}
