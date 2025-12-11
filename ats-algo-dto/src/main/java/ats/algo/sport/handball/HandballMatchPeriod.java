package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Geoff
 * 
 */
public enum HandballMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_HALF,
    AT_HALF_TIME,
    IN_SECOND_HALF,
    AT_FULL_TIME,
    IN_EXTRATIME_PERIOD,
    IN_SHOOTOUT_PERIOD,
    AT_SHOOTOUT_PERIOD_END,
    AT_EXTRATIME_PERIOD_END,
    MATCH_COMPLETED
}
