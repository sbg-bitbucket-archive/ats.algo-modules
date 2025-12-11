package ats.algo.core.common;

/**
 * defines the possible reasons why the calculate() method in MatchEngine might have been invoked
 * 
 * @author Geoff
 *
 */
public enum CalcRequestCause {
    NEW_MATCH,
    MATCH_INCIDENT,
    PARAMS_CHANGED_BY_TRADER,
    PARAMS_CHANGED_FOLLOWING_PARAM_FIND,
    PARAM_FIND,
    TIMER,
    EVENT_TIER_CHANGE,
    MATCH_RESULT,
    MATCH_ABANDONED
}
