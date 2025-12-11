package ats.algo.core.baseclasses;

/**
 * defines the parameter type - is it associated with one or other player or for the two combined (e.g. total points
 * expectation) or for the difference between them (e.g. Handicap). May be used as a hint to optimise param finding
 * 
 * Param type TRADER_CONTROL is used to indicate a parameter which is not directly used by the pricing model but instead
 * determines a setting which a trader can change and which affects system behaviour. e.g. setting the eventTier for a
 * match which affects which trading rules apply.
 * 
 * @author Geoff
 * 
 */
public enum MatchParamType {
    A,
    B,
    BOTHCOMBINED,
    BOTHDIFFERENCE,
    TRADER_CONTROL,
    ENUM
}
