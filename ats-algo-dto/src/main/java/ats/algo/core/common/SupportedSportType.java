package ats.algo.core.common;

/**
 * List of sports supported by algoManager
 * 
 * @author Geoff
 *
 */
public enum SupportedSportType {
    AUSSIE_RULES,
    CRICKET,
    BASEBALL,
    BASKETBALL,
    BADMINTON,
    BANDY,
    BEACH_VOLLEYBALL,
    BOWLS,
    DARTS,
    FIELD_HOCKEY,
    AMERICAN_FOOTBALL,
    SOCCER,
    FLOORBALL,
    FUTSAL,
    ICE_HOCKEY,
    HANDBALL,
    ROLLER_HOCKEY,
    RUGBY_LEAGUE,
    RUGBY_UNION,
    SNOOKER,
    SQUASH,
    TABLE_TENNIS,
    TENNIS,
    TESTCRICKET,
    VOLLEYBALL,
    TEST_TENNISG,
    TEST_SPORT,
    FANTASY_EXAMPLE_SPORT,
    OUTRIGHTS;

    /**
     * returns the name as a String, used by trading rules to handle case where no sport is specified
     * 
     * @param supportedSportType
     * @return
     */
    public static String getName(SupportedSportType supportedSportType) {
        if (supportedSportType == null)
            return getNameForNotSportSpecific();
        else
            return supportedSportType.toString();
    }

    public static String getNameForNotSportSpecific() {
        return "NOT_SPORT_SPECIFIC";
    }
}
