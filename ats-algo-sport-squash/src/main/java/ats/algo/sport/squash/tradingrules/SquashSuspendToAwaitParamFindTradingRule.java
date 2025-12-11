package ats.algo.sport.squash.tradingrules;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.sport.squash.SquashMatchIncidentResult;
import ats.algo.sport.squash.SquashMatchIncidentResult.SquashMatchIncidentResultType;
import ats.algo.sport.squash.SquashMatchState;

public class SquashSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

    private static final long serialVersionUID = 1L;

    public SquashSuspendToAwaitParamFindTradingRule() {
        super("squashSuspendToAwaitParamFindTradingRule", null);
    }

    @Override
    public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {

        Class<? extends MatchState> clazz = matchState.getClass();

        if (clazz == SquashMatchState.class) {

            SquashMatchState squashMatchState = (SquashMatchState) matchState;
            SquashMatchIncidentResult currentMatchStateIncidentResult = squashMatchState.getCurrentMatchState();

            if (currentMatchStateIncidentResult.getSquashMatchIncidentResultType()
                            .equals(SquashMatchIncidentResultType.GAMEWON))
                return true;
            return false;
        } else
            return false;
    }

}
