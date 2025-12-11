package ats.algo.betstars;

/**
 * <p>
 * Used to define the relevant properties to tell the Algo Manager to use the Betstars model for a particular sport.
 * </p>
 * Note: this class is intended for use in test cases and matchrunner.
 */
public final class BetstarsSportInitialisation {

    static {
        initCommonProperties();
    }

    /**
     * Turns on Betstars tennis model
     */
    public static void initTennis() {
        System.setProperty("algo.tennis.engine.class", "bean:tennisBsMatchEngine");
        System.setProperty("algo.tennis.params.class", "com.betstars.algo.ats.integration.BsTennisMatchParams");
        System.setProperty("algo.tennis.tradingrules.class",
                        "ats.algo.sport.tennis.bs.tradingrules.BsTennisTradingRules");
    }

    private static void initCommonProperties() {
        System.setProperty("algomgr.springBridgeComponentScanPackage", "com.betstars.algo");
        // Are these even needed ?
        System.setProperty("betstars.algo.setting.path", ".");
        System.setProperty("algomgr.springBridgeContext", "bs-beans");
    }

    public static void initSoccer() {
        /**
         * url for dev environment
         */
        System.setProperty("urlForExternalModelsMqBroker",
                        "tcp://iomsampss01.amelco.lan:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0");
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
                        "ats.algo.sport.football.bs.tradingrules.BsFootballTradingRules");

    }
}
