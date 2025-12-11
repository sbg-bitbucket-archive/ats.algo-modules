package ats.algo.sport.floorball;

import ats.algo.core.baseclasses.MatchIncidentResult;

/**
 * 
 * @author Geoff
 * 
 */
public enum FloorballMatchPeriod implements MatchIncidentResult {
    PREMATCH,
    IN_FIRST_PERIOD,
    AT_FIRST_PERIOD_END,
    IN_SECOND_PERIOD,
    AT_SECOND_PERIOD_END,
    IN_THIRD_PERIOD,
    AT_FULL_TIME,
    IN_EXTRA_TIME,
    AT_EXTRA_PERIOD_END,
    IN_SHOOTOUT,
    AT_SHOOTOUT_END,
    MATCH_COMPLETED,


}
