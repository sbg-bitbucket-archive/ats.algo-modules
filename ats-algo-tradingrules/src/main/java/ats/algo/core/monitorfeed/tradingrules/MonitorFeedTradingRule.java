package ats.algo.core.monitorfeed.tradingrules;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;

public class MonitorFeedTradingRule extends AbstractTradingRule {

    private static final long serialVersionUID = 1L;

    private long[] incidentUpdatesTimeoutMs;
    private TraderAlertType[] incidentUpdatesTimeoutAlerts;
    private Boolean[] suspendWhenAlertingForIncidents;
    private long[] priceUpdatesTimeoutMs;
    private TraderAlertType[] priceUpdatesTimeoutAlerts;
    private Boolean[] suspendWhenAlertingForPrices;


    private MonitorFeedTradingRuleSuspensionMethod sportSpecificLogic;

    /**
     * Constructor for standard version of the rule
     * 
     * @param js the max allowed time between incidents before event is suspended. If set to 0 then not used
     * @param priceUpdatesTimeoutSecs the max allowed time between price updates before event is suspended. If set to 0
     *        then not used
     */
    public MonitorFeedTradingRule(int[] incidentUpdatesTimeoutSecs, int[] priceUpdatesTimeoutSecs,
                    MonitorFeedSpecs[] incidentFeedSpecs, MonitorFeedSpecs[] priceFeedSpecs) {
        super.setRuleName("Monitor incident and price feeds trading rule");
        super.setRuleType(TradingRuleType.MONITOR_FEED);
        this.incidentUpdatesTimeoutMs = new long[incidentUpdatesTimeoutSecs.length];
        this.suspendWhenAlertingForIncidents = new Boolean[incidentUpdatesTimeoutSecs.length];
        this.priceUpdatesTimeoutMs = new long[priceUpdatesTimeoutSecs.length];
        this.suspendWhenAlertingForPrices = new Boolean[priceUpdatesTimeoutSecs.length];
        for (int i = 0; i < incidentUpdatesTimeoutSecs.length; i++) {
            this.incidentUpdatesTimeoutMs[i] = ((long) incidentUpdatesTimeoutSecs[i]) * 1000L;
            this.suspendWhenAlertingForIncidents[i] = !incidentFeedSpecs[i].isAlertingOnly();
        }

        for (int i = 0; i < priceUpdatesTimeoutSecs.length; i++) {
            this.priceUpdatesTimeoutMs[i] = ((long) priceUpdatesTimeoutSecs[i]) * 1000L;
            this.suspendWhenAlertingForPrices[i] = !priceFeedSpecs[i].isAlertingOnly();
        }
        this.sportSpecificLogic = null;
    }

    /**
     * Constructor for standard version of the rule
     * 
     * @param incidentsTimeoutSecs the max allowed time between incidents before event is suspended. If set to 0 then
     *        not used
     * @param priceUpdatesTimeoutSecs the max allowed time between price updates before event is suspended. If set to 0
     *        then not used
     */
    public MonitorFeedTradingRule(int incidentsTimeoutSecs, int priceUpdatesTimeoutSecs) {
        super.setRuleName("Monitor incident and price feeds trading rule");
        super.setRuleType(TradingRuleType.MONITOR_FEED);
        this.incidentUpdatesTimeoutMs = new long[1];
        this.incidentUpdatesTimeoutMs[0] = ((long) incidentsTimeoutSecs) * 1000L;
        this.priceUpdatesTimeoutMs = new long[1];
        this.priceUpdatesTimeoutMs[0] = ((long) priceUpdatesTimeoutSecs) * 1000L;
        this.sportSpecificLogic = null;
    }

    /**
     * Constructor for the version using a sport specific method to determine whether to delay applying suspension rules
     * 
     * @param incidentsTimeoutSecs
     * @param priceUpdatesTimeoutSecs
     * @param method
     */
    public MonitorFeedTradingRule(int incidentsUpdatesTimeoutSecs, int priceUpdatesTimeoutSecs,
                    MonitorFeedTradingRuleSuspensionMethod sportSpecificLogic) {
        this(new int[] {incidentsUpdatesTimeoutSecs}, new int[] {priceUpdatesTimeoutSecs},
                        generateMonitorFeedSpecs(incidentsUpdatesTimeoutSecs),
                        generateMonitorFeedSpecs(priceUpdatesTimeoutSecs));
        this.sportSpecificLogic = sportSpecificLogic;
    }

    private static MonitorFeedSpecs[] generateMonitorFeedSpecs(int updatesTimeoutSecs) {
        MonitorFeedSpecs[] monitorFeedSpecs = new MonitorFeedSpecs[1];
        monitorFeedSpecs[0] = new MonitorFeedSpecs(updatesTimeoutSecs);
        return monitorFeedSpecs;
    }

    public MonitorFeedTradingRule(MonitorFeedSpecs[] incidentFeedsUpdateTimeOut,
                    MonitorFeedSpecs[] pricingFeedsUpdateTimeOut,
                    MonitorFeedTradingRuleSuspensionMethod sportSpecificLogic) {
        this(getFeedsUpdateTimeArray(incidentFeedsUpdateTimeOut), getFeedsUpdateTimeArray(pricingFeedsUpdateTimeOut),
                        incidentFeedsUpdateTimeOut, pricingFeedsUpdateTimeOut);
        this.sportSpecificLogic = sportSpecificLogic;
        if (pricingFeedsUpdateTimeOut[0].getTraderAlertType() != null) {
            priceUpdatesTimeoutAlerts = generateTimeoutAlertsArray(pricingFeedsUpdateTimeOut);

        }
        if (incidentFeedsUpdateTimeOut[0].getTraderAlertType() != null) {
            incidentUpdatesTimeoutAlerts = generateTimeoutAlertsArray(incidentFeedsUpdateTimeOut);

        }
    }


    private TraderAlertType[] generateTimeoutAlertsArray(MonitorFeedSpecs[] feedsUpdateTimeOut) {
        TraderAlertType[] alerts = new TraderAlertType[feedsUpdateTimeOut.length];
        for (int i = 0; i < feedsUpdateTimeOut.length; i++)
            alerts[i] = feedsUpdateTimeOut[i].getTraderAlertType();
        return alerts;
    }

    private static int[] getFeedsUpdateTimeArray(MonitorFeedSpecs[] feedsUpdateTimeOut) {
        int[] timeArray = new int[feedsUpdateTimeOut.length];
        for (int i = 0; i < feedsUpdateTimeOut.length; i++)
            timeArray[i] = feedsUpdateTimeOut[i].getTimeoutSecs();
        return timeArray;
    }

    /**
     * 
     * @param now
     * @param timeOfLastMatchIncident time last incident was received
     * @param timeOfLastPriceUpdate time last price update was received
     * @param matchState current match state
     * @return result object if event should be suspended, else null
     */
    public MonitorFeedTradingRuleResult applyRule(long now, long timeOfLastMatchIncident, long timeOfLastPriceUpdate,
                    MatchState matchState, EventSettings eventSettings) {

        MonitorFeedTradingRuleResult result = null;
        if (sportSpecificLogic != null) {
            /*
             * decide whether any sport specific reasons to delay tests, e.g. rainDelays, half time
             */
            MonitorFeedTradingRuleSuspensionMethodResult methodResult = sportSpecificLogic.handle(now, matchState);
            if (methodResult != null) {
                switch (methodResult.getResultType()) {
                    case DO_NOT_SUSPEND:
                        return new MonitorFeedTradingRuleResult(false, methodResult.getReason());
                    case SUSPEND_IMMEDIATELY:
                        return new MonitorFeedTradingRuleResult(true, methodResult.getReason());
                    case APPLY_STANDARD_RULES:
                        break;
                }
            }
        }
        /*
         * apply the standard rules to decide whether to suspend
         */

        boolean incidentTimeoutExceeeded =
                        incidentUpdatesTimeoutMs[0] > 0 && now - timeOfLastMatchIncident > incidentUpdatesTimeoutMs[0];

        int index = findPriceUpdatesTimeoutIndex(now - timeOfLastPriceUpdate, priceUpdatesTimeoutMs);

        TraderAlert alertPrices = null;
        TraderAlert alertIncidents = null;
        boolean priceUpdatesTimeoutExceeded = priceUpdatesTimeoutMs[index] > 0
                        && now - timeOfLastPriceUpdate > priceUpdatesTimeoutMs[index] && timeOfLastPriceUpdate > 0;

        if (priceUpdatesTimeoutExceeded) {
            alertPrices = null;
            if (priceUpdatesTimeoutAlerts != null) {
                alertPrices = new TraderAlert(priceUpdatesTimeoutAlerts[index], "Pricing time out alert", null);
            }

            if (incidentTimeoutExceeeded) {
                alertIncidents = null;
                if (incidentUpdatesTimeoutAlerts != null) {
                    alertIncidents = new TraderAlert(incidentUpdatesTimeoutAlerts[0], "Incident time out alert", null);
                }

                result = new MonitorFeedTradingRuleResult(suspendWhenAlertingForIncidents[0],
                                "MonitorFeedTradingRule - incident updates timeout exceeded.", alertIncidents,
                                alertPrices);
            } else {
                if (eventSettings.isIgnoreBookiePrices()) {
                    suspendWhenAlertingForPrices[index] = false;
                }
                result = new MonitorFeedTradingRuleResult(suspendWhenAlertingForPrices[index],
                                "MonitorFeedTradingRule - prices updates timeout exceeded.", alertIncidents,
                                alertPrices);
            }
        } else if (incidentTimeoutExceeeded) {
            alertIncidents = null;
            if (incidentUpdatesTimeoutAlerts != null) {
                alertIncidents = new TraderAlert(incidentUpdatesTimeoutAlerts[0], "Incident time out alert", null);
            }

            result = new MonitorFeedTradingRuleResult(suspendWhenAlertingForIncidents[0],
                            "MonitorFeedTradingRule - incident updates timeout exceeded.", alertIncidents, alertPrices);
        }
        return result;
    }

    // find the point that current timeout belongs
    private int findPriceUpdatesTimeoutIndex(long l, long[] priceUpdatesTimeoutMs2) {
        if (priceUpdatesTimeoutMs2.length == 1)
            return 0;
        else {
            int index = 0;
            for (int i = priceUpdatesTimeoutMs2.length - 1; i >= 0; i--) {
                if (priceUpdatesTimeoutMs2[i] < l) {
                    index = i;
                    break;
                }
            }
            return index;
        }
    }

}
