package ats.algo.sport.afl;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Jin
 * 
 */
public enum AflMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_PERIOD,
    AT_FIRST_PERIOD_END,
    IN_SECOND_PERIOD,
    AT_SECOND_PERIOD_END,
    IN_THIRD_PERIOD,
    AT_THIRD_PERIOD_END,
    IN_FOURTH_PERIOD,

    IN_FIRST_HALF,
    AT_HALF_TIME,
    IN_SECOND_HALF,

    AT_FULL_TIME,
    IN_EXTRA_TIME,
    AT_EXTRA_PERIOD_END,
    MATCH_COMPLETED,

}
