package ats.algo.sport.tennis.bs.tradingrules;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.tradingrules.PropertyChangeTraderRules;

public class BsTennisPropertyChangesTradingRule extends PropertyChangeTraderRules {

    private static final long serialVersionUID = 1L;

    public BsTennisPropertyChangesTradingRule() {
        super("BsTennisPropertyChangesTradingRule");
    }

    @Override
    public Map<String, String> shouldUpdateProperties(MatchIncident matchIncident, MatchState matchState,
                    MatchState previousMatchState, EventSettings eventSettings) {

        if (previousMatchState.preMatch() && !matchState.preMatch()) {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("SOURCEWEIGHT_Pinnacle", "0");
            properties.put("SOURCEWEIGHT_Unibet", "20");
            properties.put("SOURCEWEIGHT_MarathonBet", "10");
            properties.put("SOURCEWEIGHT_Ladbrokes", "0");
            properties.put("SOURCEWEIGHT_WilliamHill", "10");
            properties.put("SOURCEWEIGHT_BetVictor", "5");
            properties.put("SOURCEWEIGHT_PaddyPower", "1");
            properties.put("SOURCEWEIGHT_Bwin", "1");
            properties.put("SOURCEWEIGHT_1XBet", "1");
            return properties;
        }

        return null;
    }

}
