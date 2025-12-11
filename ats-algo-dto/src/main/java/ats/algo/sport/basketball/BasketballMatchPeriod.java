package ats.algo.sport.basketball;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Jin
 * 
 */
public enum BasketballMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_QUARTER,
    AT_FIRST_QUARTER_END,
    IN_SECOND_QUARTER,
    AT_SECOND_QUARTER_END,
    IN_THIRD_QUARTER,
    AT_THIRD_QUARTER_END,
    IN_FOURTH_QUARTER,

    IN_FIRST_HALF,
    AT_HALF_TIME,
    IN_SECOND_HALF,

    AT_FULL_TIME,
    IN_EXTRA_TIME,
    AT_EXTRA_PERIOD_END,
    MATCH_COMPLETED,

}
