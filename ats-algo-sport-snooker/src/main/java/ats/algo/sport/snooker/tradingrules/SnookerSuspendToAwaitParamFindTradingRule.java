package ats.algo.sport.snooker.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.snooker.SnookerMatchIncident;
import ats.algo.sport.snooker.SnookerMatchIncident.SnookerMatchIncidentType;

public class SnookerSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public SnookerSuspendToAwaitParamFindTradingRule() {
        super("snookerSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == SnookerMatchIncident.class) {

            SnookerMatchIncident snookerMatchIncident = (SnookerMatchIncident) matchIncident;
            if (snookerMatchIncident.getIncidentSubType().equals(SnookerMatchIncidentType.FRAMEWON))
                return true;
            return false;
        } else
            return false;
    }

}
