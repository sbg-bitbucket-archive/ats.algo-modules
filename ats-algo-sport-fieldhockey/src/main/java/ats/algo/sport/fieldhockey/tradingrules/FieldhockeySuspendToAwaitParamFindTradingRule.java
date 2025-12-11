package ats.algo.sport.fieldhockey.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident.FieldhockeyMatchIncidentType;

public class FieldhockeySuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public FieldhockeySuspendToAwaitParamFindTradingRule() {
        super("FieldhockeySuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == FieldhockeyMatchIncident.class) {

            FieldhockeyMatchIncident fieldhockeyMatchIncident = (FieldhockeyMatchIncident) matchIncident;
            if (fieldhockeyMatchIncident.getIncidentSubType().equals(FieldhockeyMatchIncidentType.GOAL)
                            || fieldhockeyMatchIncident.getIncidentSubType()
                                            .equals(FieldhockeyMatchIncidentType.PENALTY_STROKE)
                            || fieldhockeyMatchIncident.getIncidentSubType()
                                            .equals(FieldhockeyMatchIncidentType.PENALTY_CORNER)
                            || fieldhockeyMatchIncident.getIncidentSubType()
                                            .equals(FieldhockeyMatchIncidentType.TWO_MINS_PENALTY_START)
                            || fieldhockeyMatchIncident.getIncidentSubType()
                                            .equals(FieldhockeyMatchIncidentType.FIVE_MINS_PENALTY_START))
                return true;
            return false;
        } else
            return false;
    }

}
