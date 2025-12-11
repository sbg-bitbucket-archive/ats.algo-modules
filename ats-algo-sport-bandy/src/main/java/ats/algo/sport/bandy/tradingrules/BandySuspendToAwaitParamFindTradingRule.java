package ats.algo.sport.bandy.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.bandy.BandyMatchIncident;
import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchIncidentType;

public class BandySuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public BandySuspendToAwaitParamFindTradingRule() {
        super("bandySuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchIncident> clazz = matchIncident.getClass();

        if (clazz == BandyMatchIncident.class) {

            BandyMatchIncident BandyMatchIncident = (BandyMatchIncident) matchIncident;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.GOAL))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.POSSIBLE_GOAL))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.POSSIBLE_PENALTY))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.PENALTY))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.CORNER))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.BLUE_CARD))
                return true;
            if (BandyMatchIncident.getIncidentSubType().equals(BandyMatchIncidentType.RED_CARD))
                return true;
            return false;
        } else
            return false;
    }

}
