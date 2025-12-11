package ats.algo.core;

/**
 * Defines the categories that both Markets and Parameters can belong to Used both by Param Finding and by TradingRules
 * 
 * @author Geoff
 *
 */
public enum MarketGroup {
    NOT_SPECIFIED,
    GOALS,
    CORNERS,
    BOOKINGS,
    INDIVIDUAL,
    PENALTY,
    GOAL_DISTRIBUTION
}
