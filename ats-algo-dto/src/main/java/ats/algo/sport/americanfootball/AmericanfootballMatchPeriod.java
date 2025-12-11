package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Geoff
 * 
 */
public enum AmericanfootballMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_QUARTER,
    IN_SECOND_QUARTER,
    IN_THIRD_QUARTER,
    IN_FOURTH_QUARTER,
    AT_FIRST_QUARTER_END,
    AT_SECOND_QUARTER_END,
    AT_THIRD_QUARTER_END,
    AT_FULL_TIME,
    IN_EXTRA_TIME_15MINUTES,
    AT_EXTRA_TIME_END,
    MATCH_COMPLETED
}
