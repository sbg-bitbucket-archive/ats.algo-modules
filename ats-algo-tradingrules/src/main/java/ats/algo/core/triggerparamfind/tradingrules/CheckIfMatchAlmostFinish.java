package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.baseclasses.MatchState;

@FunctionalInterface
public interface CheckIfMatchAlmostFinish {

    public boolean handle(MatchState matchState);
}
