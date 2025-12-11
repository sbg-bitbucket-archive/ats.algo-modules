package ats.algo.sport.rugbyleague.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.RugbyLeagueMatchIncidentType;

public class RugbyLeagueSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public RugbyLeagueSuspendToAwaitParamFindTradingRule() {
        super("rugbyleagueSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == RugbyLeagueMatchIncident.class) {

            RugbyLeagueMatchIncident rugbyleagueMatchIncident = (RugbyLeagueMatchIncident) matchIncident;
            if (rugbyleagueMatchIncident.getIncidentSubType().equals(RugbyLeagueMatchIncidentType.TRY))
                return true;
            if (rugbyleagueMatchIncident.getIncidentSubType().equals(RugbyLeagueMatchIncidentType.PENALTY_GOAL))
                return true;
            return false;
        } else
            return false;
    }

}
