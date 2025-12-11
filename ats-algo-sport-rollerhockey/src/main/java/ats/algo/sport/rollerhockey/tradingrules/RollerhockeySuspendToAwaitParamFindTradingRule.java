package ats.algo.sport.rollerhockey.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident.RollerhockeyMatchIncidentType;

public class RollerhockeySuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public RollerhockeySuspendToAwaitParamFindTradingRule() {
        super("RollerhockeySuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == RollerhockeyMatchIncident.class) {

            RollerhockeyMatchIncident rollerhockeyMatchIncident = (RollerhockeyMatchIncident) matchIncident;
            if (rollerhockeyMatchIncident.getIncidentSubType().equals(RollerhockeyMatchIncidentType.GOAL)
                            || rollerhockeyMatchIncident.getIncidentSubType()
                                            .equals(RollerhockeyMatchIncidentType.TWO_MINS_PENALTY_START)
                            || rollerhockeyMatchIncident.getIncidentSubType()
                                            .equals(RollerhockeyMatchIncidentType.FIVE_MINS_PENALTY_START))
                return true;
            return false;
        } else
            return false;
    }

}
