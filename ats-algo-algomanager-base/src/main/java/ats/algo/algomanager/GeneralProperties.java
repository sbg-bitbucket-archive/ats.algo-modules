package ats.algo.algomanager;

import java.util.HashMap;
import java.util.Map;
import ats.core.AtsBean;

/**
 * Container for all properties that may be set via system properties
 * 
 * @author gicha
 *
 */

public class GeneralProperties extends AtsBean {

    private String urlForExternalModelsMqBroker;
    private boolean onlyPublishMarketsFollowingParamChange;
    private boolean onlyPublishCornersAndCardsMarket;
    private boolean overrideRequirementForInPlayPfToReleaseMarkets;
    private boolean publishResultedMarketsImmediately;
    private boolean autosyncMatchStateToFeedOnMismatch;
    private boolean transitionToInplayAlert;
    private int elapsedTimeBeforeConsecutiveAlertTriggered;
    private boolean usingParamFindTradingRules;
    private boolean updateParamMapWithPropertiesChange;
    private boolean publishOutputOfRequestsAndResponses;
    private boolean doNotVoidMarketsOnAbandonEvent;
    private boolean logAlgoStatistics;
    private boolean useMarginChart;

    public static GeneralProperties initialiseFromSystemProperties() {
        GeneralProperties p = new GeneralProperties();
        p.onlyPublishMarketsFollowingParamChange =
                        algoManagerBooleanProperty("onlyPublishMarketsFollowingParamChange", true);
        p.onlyPublishCornersAndCardsMarket = algoManagerBooleanProperty("onlyPublishCornersAndCardsMarket", false);
        p.overrideRequirementForInPlayPfToReleaseMarkets =
                        algoManagerBooleanProperty("overrideRequirementForInPlayPfToReleaseMarkets", false);
        p.publishResultedMarketsImmediately = algoManagerBooleanProperty("publishResultedMarketsImmediately", false);
        p.autosyncMatchStateToFeedOnMismatch = algoManagerBooleanProperty("autosyncMatchStateToFeedOnMismatch", true);
        p.transitionToInplayAlert = algoManagerBooleanProperty("transitionToInplayAlert", false);
        p.usingParamFindTradingRules = algoManagerBooleanProperty("usingParamFindTradingRules", true);
        p.updateParamMapWithPropertiesChange = algoManagerBooleanProperty("updateParamMapWithPropertiesChange", true);
        p.publishOutputOfRequestsAndResponses =
                        algoManagerBooleanProperty("publishOutputOfRequestsAndResponses", false);
        p.urlForExternalModelsMqBroker =
                        algoManagerStringProperty("urlForExternalModelsMqBroker", "tcp://localhost:61616");
        p.doNotVoidMarketsOnAbandonEvent = algoManagerBooleanProperty("doNotVoidMarketsOnAbandonEvent", false);
        p.logAlgoStatistics = algoManagerBooleanProperty("logAlgoStatistics", false);
        p.useMarginChart = algoManagerBooleanProperty("useMarginChart", true);
        return p;

    }

    public Map<String, String> propertiesAsMap() {
        Map<String, String> map = new HashMap<>(10);
        map.put("overrideRequirementForInPlayPfToReleaseMarkets",
                        String.valueOf(overrideRequirementForInPlayPfToReleaseMarkets));
        map.put("urlForExternalModelsMqBroker", urlForExternalModelsMqBroker);
        map.put("onlyPublishMarketsFollowingParamChange", String.valueOf(onlyPublishMarketsFollowingParamChange));
        map.put("onlyPublishCornersAndCardsMarket", String.valueOf(onlyPublishCornersAndCardsMarket));
        map.put("publishResultedMarketsImmediately", String.valueOf(publishResultedMarketsImmediately));
        map.put("autosyncMatchStateToFeedOnMismatch", String.valueOf(autosyncMatchStateToFeedOnMismatch));
        map.put("transitionToInplayAlert", String.valueOf(transitionToInplayAlert));
        map.put("usingParamFindTradingRules", String.valueOf(usingParamFindTradingRules));
        map.put("updateParamMapWithPropertiesChange", String.valueOf(updateParamMapWithPropertiesChange));
        map.put("publishOutputOfRequestsAndResponses", String.valueOf(publishOutputOfRequestsAndResponses));
        map.put("resultMarketsOnAnAbandonEvent", String.valueOf(doNotVoidMarketsOnAbandonEvent));
        map.put("logAlgoStatistics", String.valueOf(logAlgoStatistics));
        map.put("useMarginChart", String.valueOf(useMarginChart));
        return map;
    }

    public void logProperties() {
        Map<String, String> map = propertiesAsMap();
        map.entrySet().stream().forEach(e -> info(e.getKey() + "= " + e.getValue()));
    }


    /**
     * checks for a system property with specified name. If present and has value "true" or "false" then return
     * appropriate boolean value. If not present or value is anything else then return default value
     * 
     * @param propertyName
     * @param defaultValue
     * @return
     */
    private static boolean algoManagerBooleanProperty(String propertyName, boolean defaultValue) {
        boolean result = defaultValue;
        /*
         * Sets the property to determine whether to suspend markets when transitioning between PM and IP
         */
        String propertyValue = System.getProperty(propertyName);

        if (propertyValue != null) {
            propertyValue = propertyValue.toLowerCase();
            if (propertyValue.equals("true"))
                result = true;
            else if (propertyValue.equals("false"))
                result = false;
        }

        return result;
    }

    /**
     * checks for a system property with specified name. If present and has integer then return appropriate integer
     * value. If not present or value is anything else then return default value
     * 
     * @param propertyName
     * @param defaultValue
     * @return
     */

    // FIXME - will eventually be included but isn't used now.

    @SuppressWarnings("unused")
    private static int algoManagerIntegerProperty(String propertyName, int defaultValue) {
        int result = defaultValue;
        /*
         * Sets the property to determine how long between param finds to alert traders to no param finds.
         */
        String propertyValue = System.getProperty(propertyName);

        if (propertyValue != null) {
            result = Integer.parseInt(propertyValue);
        } else {
            result = defaultValue;
        }

        return result;
    }

    private static String algoManagerStringProperty(String propertyName, String defaultValue) {
        String value = defaultValue;
        String systemPropertyValue = System.getProperty(propertyName);
        if (systemPropertyValue != null)
            value = systemPropertyValue;
        return value;
    }

    public String getUrlForExternalModelsMqBroker() {
        return urlForExternalModelsMqBroker;
    }

    public void setUrlForExternalModelsMqBroker(String urlForExternalModelsMqBroker) {
        this.urlForExternalModelsMqBroker = urlForExternalModelsMqBroker;
    }

    public boolean isOnlyPublishMarketsFollowingParamChange() {
        return onlyPublishMarketsFollowingParamChange;
    }

    public void setOnlyPublishMarketsFollowingParamChange(boolean onlyPublishMarketsFollowingParamChange) {
        this.onlyPublishMarketsFollowingParamChange = onlyPublishMarketsFollowingParamChange;
    }

    public boolean isOnlyPublishCornersAndCardsMarket() {
        return onlyPublishCornersAndCardsMarket;
    }

    public void setOnlyPublishCornersAndCardsMarket(boolean onlyPublishCornersAndCardsMarket) {
        this.onlyPublishCornersAndCardsMarket = onlyPublishCornersAndCardsMarket;
    }

    public boolean isOverrideRequirementForInPlayPfToReleaseMarkets() {
        return overrideRequirementForInPlayPfToReleaseMarkets;
    }

    public void setOverrideRequirementForInPlayPfToReleaseMarkets(
                    boolean overrideRequirementForInPlayPfToReleaseMarkets) {
        this.overrideRequirementForInPlayPfToReleaseMarkets = overrideRequirementForInPlayPfToReleaseMarkets;
    }

    public boolean isPublishResultedMarketsImmediately() {
        return publishResultedMarketsImmediately;
    }

    public void setPublishResultedMarketsImmediately(boolean publishResultedMarketsImmediately) {
        this.publishResultedMarketsImmediately = publishResultedMarketsImmediately;
    }

    public boolean isAutosyncMatchStateToFeedOnMismatch() {
        return autosyncMatchStateToFeedOnMismatch;
    }

    public void setAutosyncMatchStateToFeedOnMismatch(boolean autosyncMatchStateToFeedOnMismatch) {
        this.autosyncMatchStateToFeedOnMismatch = autosyncMatchStateToFeedOnMismatch;
    }

    public boolean isTransitionToInplayAlert() {
        return transitionToInplayAlert;
    }

    public void setTransitionToInplayAlert(boolean transitionToInplayAlert) {
        this.transitionToInplayAlert = transitionToInplayAlert;
    }

    public int getElapsedTimeBeforeConsecutiveAlertTriggered() {
        return elapsedTimeBeforeConsecutiveAlertTriggered;
    }

    public void setElapsedTimeBeforeConsecutiveAlertTriggered(int elapsedTimeBeforeConsecutiveAlertTriggered) {
        this.elapsedTimeBeforeConsecutiveAlertTriggered = elapsedTimeBeforeConsecutiveAlertTriggered;
    }

    public boolean isUsingParamFindTradingRules() {
        return usingParamFindTradingRules;
    }

    public void setUsingParamFindTradingRules(boolean usingParamFindTradingRules) {
        this.usingParamFindTradingRules = usingParamFindTradingRules;
    }

    public boolean isUpdateParamMapWithPropertiesChange() {
        return updateParamMapWithPropertiesChange;
    }

    public void setUpdateParamMapWithPropertiesChange(boolean updateParamMapWithPropertiesChange) {
        this.updateParamMapWithPropertiesChange = updateParamMapWithPropertiesChange;
    }

    public boolean isPublishOutputOfRequestsAndResponses() {
        return publishOutputOfRequestsAndResponses;
    }

    public void setPublishOutputOfRequestsAndResponses(boolean publishOutputOfRequestsAndResponses) {
        this.publishOutputOfRequestsAndResponses = publishOutputOfRequestsAndResponses;
    }

    public boolean isDoNotVoidMarketsOnAbandonEvent() {
        return doNotVoidMarketsOnAbandonEvent;
    }

    public void setDoNotVoidMarketsOnAbandonEvent(boolean doNotVoidMarketsOnAbandonEvent) {
        this.doNotVoidMarketsOnAbandonEvent = doNotVoidMarketsOnAbandonEvent;
    }

    public boolean shouldLogAlgoStatistics() {
        return logAlgoStatistics;
    }

    public void setLogAlgoStatistics(boolean logAlgoStatistics) {
        this.logAlgoStatistics = logAlgoStatistics;
    }

    public boolean isUseMarginChart() {
        return useMarginChart;
    }

    public void setUseMarginChart(boolean useMarginChart) {
        this.useMarginChart = useMarginChart;
    }
}
