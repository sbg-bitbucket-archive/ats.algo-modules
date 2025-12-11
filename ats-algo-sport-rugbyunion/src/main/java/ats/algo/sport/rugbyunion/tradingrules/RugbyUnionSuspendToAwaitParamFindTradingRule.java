package ats.algo.sport.rugbyunion.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident.RugbyUnionMatchIncidentType;

public class RugbyUnionSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public RugbyUnionSuspendToAwaitParamFindTradingRule() {
        super("rugbyunionSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == RugbyUnionMatchIncident.class) {

            RugbyUnionMatchIncident rugbyunionMatchIncident = (RugbyUnionMatchIncident) matchIncident;
            if (rugbyunionMatchIncident.getIncidentSubType().equals(RugbyUnionMatchIncidentType.TRY))
                return true;
            if (rugbyunionMatchIncident.getIncidentSubType().equals(RugbyUnionMatchIncidentType.PENALTY_GOAL))
                return true;
            return false;
        } else
            return false;
    }

}
