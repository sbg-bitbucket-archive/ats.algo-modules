package ats.algo.core.tradingrules;

import java.io.Serializable;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.eventsettings.EventSettings;

public abstract class PropertyChangeTraderRules extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    public PropertyChangeTraderRules(String ruleName) {
        super(TradingRuleType.PROPERTY_CHANGES, ruleName, null);

    }

    public abstract Map<String, String> shouldUpdateProperties(MatchIncident matchIncident, MatchState matchState,
                    MatchState previousMatchState, EventSettings eventSettings);
}
