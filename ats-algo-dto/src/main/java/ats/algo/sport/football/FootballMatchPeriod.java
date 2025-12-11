package ats.algo.sport.football;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Geoff
 * 
 */
public enum FootballMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_HALF,
    AT_HALF_TIME,
    IN_SECOND_HALF,
    AT_FULL_TIME,
    IN_EXTRA_TIME_FIRST_HALF,
    IN_EXTRA_TIME_HALF_TIME,
    IN_EXTRA_TIME_SECOND_HALF,
    IN_SHOOTOUT,
    MATCH_COMPLETED
}
