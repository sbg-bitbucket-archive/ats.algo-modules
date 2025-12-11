package ats.algo.ladbrokescoral;

/**
 * <p>
 * Used to define the relevant properties to tell the Algo Manager to use the Ladbrokescoral model for a particular
 * sport.
 * </p>
 * Note: this class is intended for use in test cases and matchrunner.
 */
public final class LadbrokesCoralSportInitialisation {

    /**
     * Turns on LadbrokesCoral aussie rules model
     */
    public static void initAussieRules() {
        System.setProperty("algo.aussie_rules.engine.class",
                        "com.ladbrokescoral.trading.algo.aussierules.AussieRulesMatchEngine");
        System.setProperty("algo.aussie_rules.params.class",
                        "com.ladbrokescoral.trading.algo.aussierules.AussieRulesMatchParams");
    }

    public static void clearInitAussieRules() {
        System.clearProperty("algo.aussie_rules.engine.class");
        System.clearProperty("algo.aussie_rules.params.class");
    }

    public static void initSoccer() {
        /**
         * url for dev environment
         */
        System.setProperty("urlForExternalModelsMqBroker",
                        "tcp://10.4.65.68:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0");
        /**
         * url for load test env
         */
        // System.setProperty("urlForExternalModelsMqBroker",
        // "tcp://iombenampsalg01:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0");

        System.setProperty("algo.soccer.externalModel", "true");
        System.setProperty("algo.soccer.clientTradingRules", "true");
        System.setProperty("algo.soccer.clientResultingOnly", "true");
        System.setProperty("algo.soccer.clientParamFinding", "true");
        System.setProperty("algo.soccer.tradingrules.class",
                        "ats.algo.sport.football.lc.tradingrules.LcFootballTradingRules");
    }

    public static void clearInitSoccer() {
        System.clearProperty("urlForExternalModelsMqBroker");
        System.clearProperty("algo.soccer.externalModel");
        System.clearProperty("algo.soccer.clientTradingRules");
        System.clearProperty("algo.soccer.clientResultingOnly");
        System.clearProperty("algo.soccer.clientParamFinding");
        System.clearProperty("algo.soccer.tradingrules.class");
    }
}
