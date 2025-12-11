package ats.algo.betstars;

/**
 * <p>
 * Used to define the relevant properties to tell the Algo Manager to use the Betstars model for a particular sport.
 * </p>
 * Note: this class is intended for use in test cases and matchrunner.
 */
public final class BetstarsSportInitialisation {


    /**
     * Turns on Betstars tennis model
     */
    public static void initTennis() {
        initCommonProperties();
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
        initCommonProperties();
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

    public static void closeTennis() {
        clearCommonProperties();
        System.clearProperty("algo.tennis.engine.class");
        System.clearProperty("algo.tennis.params.class");
        System.clearProperty("algo.tennis.tradingrules.class");

    }

    private static void clearCommonProperties() {
        System.clearProperty("algomgr.springBridgeComponentScanPackage");
        // Are these even needed ?
        System.clearProperty("betstars.algo.setting.path");
        System.clearProperty("algomgr.springBridgeContext");

    }
}
