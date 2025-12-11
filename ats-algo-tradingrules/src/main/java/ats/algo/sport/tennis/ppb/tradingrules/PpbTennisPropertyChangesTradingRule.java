package ats.algo.sport.tennis.ppb.tradingrules;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.tradingrules.PropertyChangeTraderRules;

public class PpbTennisPropertyChangesTradingRule extends PropertyChangeTraderRules {

    private static final long serialVersionUID = 1L;

    public PpbTennisPropertyChangesTradingRule() {
        super("PpbTennisPropertyChangesTradingRule");
    }

    @Override
    public Map<String, String> shouldUpdateProperties(MatchIncident matchIncident, MatchState matchState,
                    MatchState previousMatchState, EventSettings eventSettings) {

        if (previousMatchState.preMatch() && !matchState.preMatch()) {
            Map<String, String> properties = new HashMap<String, String>();
            long eventTier = eventSettings.getEventTier();

            if (!(eventTier % 2 == 0)) {
                properties.put("eventTier", Long.toString((eventTier + 1)));
                return properties;
            }
        }

        return null;
    }

}
