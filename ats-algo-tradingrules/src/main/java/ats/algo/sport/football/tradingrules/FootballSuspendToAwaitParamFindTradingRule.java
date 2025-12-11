package ats.algo.sport.football.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;

public class FootballSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public FootballSuspendToAwaitParamFindTradingRule() {
        super("FootballSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == FootballMatchIncident.class) {

            FootballMatchIncident footballMatchIncident = (FootballMatchIncident) matchIncident;
            if (footballMatchIncident.getIncidentSubType().equals(FootballMatchIncidentType.GOAL))
                return true;
            if (footballMatchIncident.getIncidentSubType().equals(FootballMatchIncidentType.RED_CARD))
                return true;
            if (footballMatchIncident.getIncidentSubType().equals(FootballMatchIncidentType.PENALTY))
                return true;
            return false;
        } else
            return false;
    }

}
