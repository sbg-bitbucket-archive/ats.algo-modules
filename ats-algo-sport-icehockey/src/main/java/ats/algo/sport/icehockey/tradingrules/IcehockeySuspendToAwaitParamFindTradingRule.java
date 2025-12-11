package ats.algo.sport.icehockey.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.icehockey.IcehockeyMatchIncident;
import ats.algo.sport.icehockey.IcehockeyMatchIncident.IcehockeyMatchIncidentType;

public class IcehockeySuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public IcehockeySuspendToAwaitParamFindTradingRule() {
        super("IcehockeySuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == IcehockeyMatchIncident.class) {

            IcehockeyMatchIncident icehockeyMatchIncident = (IcehockeyMatchIncident) matchIncident;
            if (icehockeyMatchIncident.getIncidentSubType().equals(IcehockeyMatchIncidentType.GOAL))
                return true;
            return false;
        } else
            return false;
    }

}
