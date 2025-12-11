package ats.algo.sport.cricket.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.cricket.CricketMatchIncident;
import ats.algo.sport.cricket.CricketMatchIncident.CricketMatchIncidentType;

public class CricketSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public CricketSuspendToAwaitParamFindTradingRule() {
        super("CricketSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == CricketMatchIncident.class) {

            CricketMatchIncident cricketMatchIncident = (CricketMatchIncident) matchIncident;
            if (cricketMatchIncident.getIncidentSubType().equals(CricketMatchIncidentType.POSSIBLE_WICKET))
                return true;
            if (cricketMatchIncident.getIncidentSubType().equals(CricketMatchIncidentType.DISMISSAL))
                return true;
            if (cricketMatchIncident.getIncidentSubType().equals(CricketMatchIncidentType.POSSIBLE_BOUDARY))
                return true;
            if (cricketMatchIncident.getIncidentSubType().equals(CricketMatchIncidentType.BOUNDARY))
                return true;
            if (cricketMatchIncident.getIncidentSubType().equals(CricketMatchIncidentType.INTERRUPTED))
                return true;
            return false;
        } else
            return false;
    }

}
