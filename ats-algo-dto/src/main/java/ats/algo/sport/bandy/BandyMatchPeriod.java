package ats.algo.sport.bandy;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Geoff
 * 
 */
public enum BandyMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_HALF,
    AT_HALF_TIME,
    IN_SECOND_HALF,
    AT_FULL_TIME,
    IN_EXTRA_TIME_FIRST_HALF,
    AT_EXTRA_TIME_HALF_TIME,
    IN_EXTRA_TIME_SECOND_HALF,
    AT_EXTRA_TIME_END,
    IN_SHOOTOUT,
    AT_SHOOTOUT_END,
    MATCH_COMPLETED
}
