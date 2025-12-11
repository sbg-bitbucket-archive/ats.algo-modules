package ats.algo.core.monitorfeed.tradingrules;

import ats.algo.core.baseclasses.MatchState;



@FunctionalInterface
public interface MonitorFeedTradingRuleSuspensionMethod {
    /**
     * provides the means of incorporating sport-specific logic into the MonitorFeed trading rule
     * 
     * @param matchState
     * @return -1: suspend immediately, 0: apply normal rules unconditionally; 1: any other value: apply normal rules if
     *         now > value
     * 
     */
    public MonitorFeedTradingRuleSuspensionMethodResult handle(long now, MatchState matchState);
}
