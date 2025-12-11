package ats.algo.sport.floorball.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.floorball.FloorballMatchIncident;
import ats.algo.sport.floorball.FloorballMatchIncident.FloorballMatchIncidentType;

public class FloorballSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public FloorballSuspendToAwaitParamFindTradingRule() {
        super("FloorballSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == FloorballMatchIncident.class) {

            FloorballMatchIncident floorballMatchIncident = (FloorballMatchIncident) matchIncident;
            if (floorballMatchIncident.getIncidentSubType().equals(FloorballMatchIncidentType.GOAL)
                            || floorballMatchIncident.getIncidentSubType()
                                            .equals(FloorballMatchIncidentType.MAJOR_PENALTY_START)
                            || floorballMatchIncident.getIncidentSubType()
                                            .equals(FloorballMatchIncidentType.TEN_MINS_PENALTY_START)
                            || floorballMatchIncident.getIncidentSubType()
                                            .equals(FloorballMatchIncidentType.MINOR_PENALTY_START))
                return true;
            return false;
        } else
            return false;
    }

}
