package ats.algo.core.triggerparamfind.tradingrules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.comparetomarket.CompareToMarket;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Markets;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.PriceSourceWeights;
import ats.algo.core.tradingrules.TradingRuleType;

/**
 * can be used to write rules that affect the param finding process
 * 
 * @author Geoff
 *
 */
public class TriggerParamFindTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, List<String>> feedsSpecialMarketsParamFindingTriggerList;
    private Map<String, Integer> marketTypesRequiredPreMatch;
    private Map<String, Integer> marketTypesRequiredInPlay;
    private Set<String> marketTypeGroupsRequiredPreMatch; // Group - need them both
    private Set<String> marketTypeGroupsRequiredInPlay;
    private Set<String> marketTypeListRequiredPreMatch; // List - just need one of them
    private Set<String> marketTypeListRequiredInPlay;
    private Set<String> marketTypesToFilterOutPreMatch;
    protected Set<String> marketTypesToFilterOutInPlay;
    private Map<String, Integer> bookiePriorityForPreMatchParamFinding;
    private Map<String, Integer> bookiePriorityForInPlayParamFinding;
    private int numberOfBookiesRequiredPrematch;
    private int numberOfBookiesRequiredInplay;
    private int maxNumberOfBookiesWantedPrematch;
    private int maxNumberOfBookiesWantedInplay;
    private boolean useAllBookies;
    protected Map<String, Double> marketWeights;
    private boolean triggerOnPriceDistance;
    private long timeBetweenParamFinds;
    double pricesDistance; // available externally for unitTesting only
    public static final long MIN_TIME_BETWEEN_PARAM_FINDS = 5000; // 5 secs
    private static final String PROPERTY_FOR_OVERRIDE_OF_TIME_FOR_PARAM_FINDS = "paramFindTimeOverride";
    private static final String PROPERTY_FOR_OVERRIDE_OF_PRICES_CHANGE = "pricesChangeOverride";
    private boolean paramFindTimeOverride;
    private boolean pricesChangeOverride;

    public TriggerParamFindTradingRule(String ruleName, Integer eventTier) {
        super(TradingRuleType.TRIGGER_PARAM_FIND, ruleName, eventTier);
        marketTypesRequiredPreMatch = new HashMap<String, Integer>();
        marketTypesRequiredInPlay = new HashMap<String, Integer>();
        marketTypeGroupsRequiredPreMatch = new HashSet<String>();
        marketTypeGroupsRequiredInPlay = new HashSet<String>();
        marketTypeListRequiredPreMatch = new HashSet<String>();
        marketTypeListRequiredInPlay = new HashSet<String>();
        marketTypesToFilterOutPreMatch = new HashSet<String>();
        marketTypesToFilterOutInPlay = new HashSet<String>();
        bookiePriorityForPreMatchParamFinding = new HashMap<String, Integer>();
        bookiePriorityForInPlayParamFinding = new HashMap<String, Integer>();
        marketWeights = new HashMap<String, Double>();
        triggerOnPriceDistance = true; // set the default to true;
        timeBetweenParamFinds = 120000; // set to the default;
        useAllBookies = false;
        maxNumberOfBookiesWantedPrematch = 999;
        maxNumberOfBookiesWantedInplay = 999;
        feedsSpecialMarketsParamFindingTriggerList = new HashMap<String, List<String>>();
        String stringForParamFindTimeOverride = System.getProperty(PROPERTY_FOR_OVERRIDE_OF_TIME_FOR_PARAM_FINDS);
        paramFindTimeOverride = false;
        if (stringForParamFindTimeOverride != null) {
            try {
                paramFindTimeOverride = Boolean.parseBoolean(stringForParamFindTimeOverride);
                info(ruleName + ". Property override. Set priceThresholdOverride to = " + paramFindTimeOverride);
            } catch (NumberFormatException e) {
                paramFindTimeOverride = false;
            }
        }
        String stringForPricesChangeOverride = System.getProperty(PROPERTY_FOR_OVERRIDE_OF_PRICES_CHANGE);
        pricesChangeOverride = false;
        if (stringForPricesChangeOverride != null) {
            try {
                pricesChangeOverride = Boolean.parseBoolean(stringForPricesChangeOverride);
                info(ruleName + ". Property override. Set priceThresholdOverride to = " + pricesChangeOverride);
            } catch (NumberFormatException e) {
                pricesChangeOverride = false;
            }
        }

    }

    public void feedsSpecialMarketsParamFindingTrigger(String source, String markets) {
        List<String> marketList = new LinkedList<>();
        marketList.addAll(Arrays.asList(markets.split(",")));
        feedsSpecialMarketsParamFindingTriggerList.put(source, marketList);
    }

    /**
     * Generates a TriggerParamFindTradingRule where param finds are triggered based on the distance between our current
     * market prices and competitor prices. Intended for use with our own pricing models
     * 
     * @param ruleName
     * @param eventTier
     * @return
     */
    public static TriggerParamFindTradingRule generatePriceDistanceVariant(String ruleName, Integer eventTier) {
        TriggerParamFindTradingRule rule = new TriggerParamFindTradingRule(ruleName, eventTier);
        rule.triggerOnPriceDistance = true;
        return rule;
    }

    /**
     * Generates a TriggerParamFindTradingRule where param finds are triggered based on time since the last param find.
     * Intended for use with third party pricing models which are doing their own param finding
     * 
     * @param ruleName
     * @param timeBetweenParamFindsSecs
     * @param eventTier
     * @return
     */
    public static TriggerParamFindTradingRule generateTimerVariant(String ruleName, int timeBetweenParamFindsSecs,
                    Integer eventTier) {
        TriggerParamFindTradingRule rule = new TriggerParamFindTradingRule(ruleName, eventTier);
        rule.triggerOnPriceDistance = false;
        rule.timeBetweenParamFinds = ((long) timeBetweenParamFindsSecs) * 1000;
        return rule;
    }



    public void setTriggerOnPriceDistance(boolean triggerOnPriceDistance) {
        this.triggerOnPriceDistance = triggerOnPriceDistance;
    }



    @SuppressWarnings("unused")
    private static boolean triggerParamFindsImmediately = false;

    /**
     * provided for use by MAtchRunner and unit tests. Should not be used otherwise
     * 
     * @param triggerParamFindsImmediately
     */
    static void setTriggerParamFindsImmediately(boolean triggerParamFindsImmediately) {
        TriggerParamFindTradingRule.triggerParamFindsImmediately = triggerParamFindsImmediately;
    }

    /*
     * to support unit testing
     */
    public void setTimeBetweenParamFinds(long timeBetweenParamFinds) {
        this.timeBetweenParamFinds = timeBetweenParamFinds;
    }

    /**
     * add market(s) to list of those required pre-match
     * 
     * @param marketTypeList a comma separated list of the types that are required e.g. "FT:ML" or "FT:ML,FT:OU". Do NOT
     *        include any spaces in the string.
     * @param count no of required instances of these types that must be available
     */
    public void addMarketTypeRequiredPreMatch(String marketTypeList, int count) {
        marketTypesRequiredPreMatch.put(marketTypeList, count);
    }

    /**
     * add market(s) to list of those required pre-match
     * 
     * @param marketTypeList a comma separated list of the types that are required e.g. "FT:ML" or "FT:ML,FT:OU". Do NOT
     *        include any spaces in the string.
     * @param count no of required instances of these types that must be available
     */
    public void addMarketTypeRequiredInPlay(String marketTypeList, int count) {
        marketTypesRequiredInPlay.put(marketTypeList, count);
    }

    /**
     * resets to an empty list
     */
    public void clearMarketTypesRequiredPreMatch() {
        marketTypesRequiredPreMatch.clear();
    }

    /**
     * resets to an empty list
     */
    public void clearMarketTypesRequiredInPlay() {
        marketTypesRequiredInPlay.clear();
    }

    /**
     * adds a market weight to the list. Only needed to set weight to something other than the default (1.0)
     * 
     * @param type
     * @param weight
     */
    public void addMarketWeight(String type, double weight) {
        marketWeights.put(type, weight);
    }

    /**
     * clear any current marketWeights
     */
    public void clearMarketWeights() {
        marketWeights.clear();
    }

    /**
     * add group of market type(s) that SHOULD be used for param finding unless one or more of the market group is
     * missing.
     * 
     * @param type
     */
    public void addMarketTypeGroupsRequiredPreMatch(String groupOfMarketTypes) {

        if (groupOfMarketTypes.contains(",")) {
            /*
             * a list of market types that can be used interchangeably
             */
            String[] types = groupOfMarketTypes.split(",");
            for (String type : types) {
                // the interchangeable market types should share the same
                // 'data' map
                marketTypeGroupsRequiredPreMatch.add(type);
            }
        } else {
            marketTypeGroupsRequiredPreMatch.add(groupOfMarketTypes);
        }
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypeGroupsRequiredPreMatch() {
        marketTypeGroupsRequiredPreMatch.clear();
    }

    /**
     * add group of market type(s) that SHOULD be used for param finding unless one or more of the market group is
     * missing.
     * 
     * @param type
     */
    public void addMarketTypeGroupsRequiredInPlay(String groupOfMarketTypes) {

        if (groupOfMarketTypes.contains(",")) {
            /*
             * a list of market types that can be used interchangeably
             */
            String[] types = groupOfMarketTypes.split(",");
            for (String type : types) {
                // the interchangeable market types should share the same
                // 'data' map
                marketTypeGroupsRequiredInPlay.add(type);
            }
        } else {
            marketTypeGroupsRequiredInPlay.add(groupOfMarketTypes);
        }
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypeGroupsRequiredInPlay() {
        marketTypeGroupsRequiredInPlay.clear();
    }

    /**
     * add List of market type(s) that SHOULD be used for param finding unless one or more of the market group is
     * missing.
     * 
     * @param type
     */
    public void addMarketTypeListRequiredPreMatch(String groupOfMarketTypes) {

        if (groupOfMarketTypes.contains(",")) {
            /*
             * a list of market types that can be used interchangeably
             */
            String[] types = groupOfMarketTypes.split(",");
            for (String type : types) {
                // the interchangeable market types should share the same
                // 'data' map
                marketTypeListRequiredPreMatch.add(type);
            }
        } else {
            marketTypeListRequiredPreMatch.add(groupOfMarketTypes);
        }
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypeListRequiredPreMatch() {
        marketTypeListRequiredPreMatch.clear();
    }

    /**
     * add List of market type(s) that SHOULD be used for param finding unless one or more of the market group is
     * missing.
     * 
     * @param type
     */
    public void addMarketTypeListRequiredInPlay(String groupOfMarketTypes) {

        if (groupOfMarketTypes.contains(",")) {
            /*
             * a list of market types that can be used interchangeably
             */
            String[] types = groupOfMarketTypes.split(",");
            for (String type : types) {
                // the interchangeable market types should share the same
                // 'data' map
                marketTypeListRequiredInPlay.add(type);
            }
        } else {
            marketTypeListRequiredInPlay.add(groupOfMarketTypes);
        }
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypeListRequiredInPlay() {
        marketTypeListRequiredInPlay.clear();
    }

    /**
     * add a market type that should NOT be used for param finding
     * 
     * @param type
     */
    public void addMarketTypeToFilterOutPreMatch(String type) {
        marketTypesToFilterOutPreMatch.add(type);
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypesToFilterOutPreMatch() {
        marketTypesToFilterOutPreMatch.clear();
    }

    /**
     * add a market type that should NOT be used for param finding
     * 
     * @param type
     */
    public void addMarketTypeToFilterOutInPlay(String type) {
        marketTypesToFilterOutInPlay.add(type);
    }

    /**
     * clear any current market types
     */
    public void clearMarketTypesToFilterOutInPlay() {
        marketTypesToFilterOutInPlay.clear();
    }

    /**
     * add bookie priority list for param finding prematch
     * 
     * @param type
     */
    public void addBookiePriorityForPreMatchParamFinding(String type, Integer priority) {
        bookiePriorityForPreMatchParamFinding.put(type, priority);
    }

    /**
     * clear bookie priority list for param finding prematch
     */
    public void clearBookiePriorityForPreMatchParamFinding() {
        bookiePriorityForPreMatchParamFinding.clear();
    }

    /**
     * add bookie priority list for param finding inplay
     * 
     * @param type
     */
    public void addBookiePriorityForInPlayParamFinding(String type, Integer priority) {
        bookiePriorityForInPlayParamFinding.put(type, priority);
    }

    /**
     * clear bookie priority list for param finding inplay
     */
    public void clearBookiePriorityForInPlayParamFinding() {
        bookiePriorityForInPlayParamFinding.clear();
    }

    public int getNumberOfBookiesRequiredPrematch() {
        return numberOfBookiesRequiredPrematch;
    }

    public void setNumberOfBookiesRequiredPrematch(int numberOfBookiesRequiredPrematch) {
        this.numberOfBookiesRequiredPrematch = numberOfBookiesRequiredPrematch;
    }

    public int getNumberOfBookiesRequiredInplay() {
        return numberOfBookiesRequiredInplay;
    }

    public void setNumberOfBookiesRequiredInplay(int numberOfBookiesRequiredInplay) {
        this.numberOfBookiesRequiredInplay = numberOfBookiesRequiredInplay;
    }

    public int getMaxNumberOfBookiesWantedPrematch() {
        return maxNumberOfBookiesWantedPrematch;
    }

    public void setMaxNumberOfBookiesWantedPrematch(int maxNumberOfBookiesWantedPrematch) {
        this.maxNumberOfBookiesWantedPrematch = maxNumberOfBookiesWantedPrematch;
    }

    public int getMaxNumberOfBookiesWantedInplay() {
        return maxNumberOfBookiesWantedInplay;
    }

    public void setMaxNumberOfBookiesWantedInplay(int maxNumberOfBookiesWantedInplay) {
        this.maxNumberOfBookiesWantedInplay = maxNumberOfBookiesWantedInplay;
    }

    public boolean useAllBookies() {
        return useAllBookies;
    }

    public void setUseAllBookies(boolean useAllBookies) {
        this.useAllBookies = useAllBookies;
    }

    /**
     * This method may be overridden for sports where we need a non-standard set of rules
     * 
     * @param matchState
     * @param markets
     * @param isBiasedParams
     * @param triggerParamFindData
     * @param eventSettings
     * @param triggerParamFindAsap
     * @return if no action to be taken, else the set of market prices to be used for the param find
     */
    public MarketPricesList shouldRequestParamFind(MatchState matchState, Markets markets, boolean isBiasedParams,
                    TriggerParamFindData triggerParamFindData, EventSettings eventSettings,
                    boolean triggerParamFindAsap) {
        long timeElapsedSinceLastParamFind = triggerParamFindData.timeElapsedSinceLastParamFind();

        if (timeElapsedSinceLastParamFind < MIN_TIME_BETWEEN_PARAM_FINDS && !paramFindTimeOverride) {
            /**
             * need to allow a certain time between param finds to allow the results of the priceCalc following a param
             * find to be completed before deciding whether to kick off another
             */
            triggerParamFindData.setLogEntry("Time elapsed since last param find <" + MIN_TIME_BETWEEN_PARAM_FINDS);
            return null;
        }
        MarketPricesList marketPricesList;
        if (matchState.isMatchCompleted()) {
            triggerParamFindData.setLogEntry("Match completed");
            return null;
        }

        boolean triggerParamFindDuetoSpecialRequirement = false;
        if (matchState.preMatch()) {
            marketPricesList = triggerParamFindData.getMarketPricesCache().getMostRecentPrices();
            triggerParamFindDuetoSpecialRequirement = checkIfContainSpecialFeedsProvider(marketPricesList);
            String errMsg = processPricesPreMatch(marketPricesList, triggerParamFindData.getPriceSourceWeights(),
                            eventSettings, matchState);
            if (errMsg != null) {
                triggerParamFindData.setLogEntry("pre-match prices check failed. " + errMsg);
                return null;
            }
            if (marketPricesList.pricesSame(triggerParamFindData.getLastUsedMarketPricesList())) {
                /*
                 * prices haven't moved since last param find so don't trigger param find
                 */
                triggerParamFindData.setLogEntry("Prices unchanged since last param find.");
                return null;
            }
        } else {
            marketPricesList = triggerParamFindData.getMarketPricesCache().getPricesSinceTime(
                            triggerParamFindData.getMatchIncidentResultCache().getLastIncidentTime());
            triggerParamFindDuetoSpecialRequirement = checkIfContainSpecialFeedsProvider(marketPricesList);
            String errMsg = processPricesInPlay(marketPricesList, matchState,
                            triggerParamFindData.getPriceSourceWeights(), eventSettings);
            if (errMsg != null) {
                triggerParamFindData.setLogEntry("In play prices check failed. " + errMsg);
                return null;
            }
        }

        boolean triggerPf = false;
        if (this.triggerOnPriceDistance) {
            if (triggerParamFindAsap) {
                triggerPf = true;
                triggerParamFindData.setLogEntry(
                                "triggerParamFindAsap set to true so trigger as soon as we have the required markets");
            } else if (isBiasedParams) {
                /*
                 * can't do the comparison locally if params biased so go off the clock
                 */
                pricesDistance = CompareToMarket.getPricesDistance(marketPricesList, markets);
                double threshold = CompareToMarket.getSmartParamFindKickoffThreshold();
                if(pricesDistance > threshold){    //No need for paramfind if prices are within threshold
                    triggerPf = timeElapsedSinceLastParamFind > timeBetweenParamFinds;
                    triggerParamFindData.setLogEntry(String.format(
                                    "Biased params so trigger based on time.  TriggerPf: %b.  Time elapsed since last pf: %d. Threshold: %d",
                                    triggerPf, timeElapsedSinceLastParamFind, timeBetweenParamFinds));
                }
            } else if (triggerParamFindData.lastParamFindRedOrAmber()) {
                /*
                 * if last pf failed then wait a reasonable period before trying again
                 */
                triggerPf = timeElapsedSinceLastParamFind > timeBetweenParamFinds;
                triggerParamFindData.setLogEntry(String.format(
                                "Last pf red or amber so trigger based on time.  TriggerPf: %b.  Time elapsed since last pf: %d. Threshold: %d",
                                triggerPf, timeElapsedSinceLastParamFind, timeBetweenParamFinds));
            } else {
                /*
                 * triggering on priceDistance
                 */
                pricesDistance = CompareToMarket.getPricesDistance(marketPricesList, markets);
                double threshold = CompareToMarket.getSmartParamFindKickoffThreshold();

                triggerPf = pricesDistance > threshold;
                triggerParamFindData.setLogEntry(String.format(
                                "Trigger on prices distance. TriggerPf: %b. PricesDistance: %.4f. Threshold: %.4f.",
                                triggerPf, pricesDistance, threshold));
            }
        } else {
            /*
             * trigger after specified time has elapsed since last param find
             */
            triggerPf = timeElapsedSinceLastParamFind > timeBetweenParamFinds;

            if (paramFindTimeOverride) {
                if (matchState.preMatch()) {
                    triggerPf = true;
                    triggerParamFindData.setLogEntry(String.format(
                                    "Trigger due to time override of PF.  TriggerPf: %b.  Time elapsed since last pf: %d.",
                                    triggerPf, timeElapsedSinceLastParamFind));
                } else if (pricesChangeOverride
                                && !marketPricesList.pricesSame(triggerParamFindData.getLastUsedMarketPricesList())) {
                    triggerPf = true;
                    triggerParamFindData.setLogEntry(String.format(
                                    "Trigger due to prices override of PF.  TriggerPf: %b.  Prices have changed since last param find",
                                    triggerPf, timeElapsedSinceLastParamFind));
                } else {
                    triggerParamFindData.setLogEntry(String.format(
                                    "Trigger based on time between pfs.  TriggerPf: %b.  Time elapsed since last pf: %d. Threshold: %d",
                                    triggerPf, timeElapsedSinceLastParamFind, timeBetweenParamFinds));
                }
            } else {
                triggerParamFindData.setLogEntry(String.format(
                                "Trigger based on time between pfs.  TriggerPf: %b.  Time elapsed since last pf: %d. Threshold: %d",
                                triggerPf, timeElapsedSinceLastParamFind, timeBetweenParamFinds));
            }
            if (triggerParamFindDuetoSpecialRequirement) {
                triggerPf = true;
                triggerParamFindData.setLogEntry(String.format("Trigger based on Special Requirement Markets List."));
            }

        }
        if (triggerPf) {
            triggerParamFindData.setLastUsedMarketPricesList(marketPricesList);
            return marketPricesList;
        } else {
            return null;
        }
    }

    /**
     * This method will not be overridden for sports when recover from diaster
     * 
     * @param matchState
     * @param markets
     * @param isBiasedParams
     * @param triggerParamFindData
     * @param eventSettings
     * @param triggerParamFindAsap
     * @return if no action to be taken, else the set of market prices to be used for the param find
     */
    public MarketPricesList shouldRequestParamFindAfterDiaster(MatchState matchState, Markets markets,
                    boolean isBiasedParams, TriggerParamFindData triggerParamFindData, EventSettings eventSettings,
                    boolean triggerParamFindAsap) {
        /*
         * Ignore last param find check
         */
        MarketPricesList marketPricesList;
        if (matchState.isMatchCompleted()) {
            triggerParamFindData.setLogEntry("Match completed");
            return null;
        }

        if (matchState.preMatch()) {
            marketPricesList = triggerParamFindData.getMarketPricesCache().getMostRecentPrices();
            String errMsg = processPricesPreMatch(marketPricesList, triggerParamFindData.getPriceSourceWeights(),
                            eventSettings, matchState);
            if (errMsg != null) {
                triggerParamFindData.setLogEntry("pre-match prices check failed. " + errMsg);
                return null;
            }

        } else {
            marketPricesList = triggerParamFindData.getMarketPricesCache().getPricesSinceTime(
                            triggerParamFindData.getMatchIncidentResultCache().getLastIncidentTime());
            String errMsg = processPricesInPlay(marketPricesList, matchState,
                            triggerParamFindData.getPriceSourceWeights(), eventSettings);
            if (errMsg != null) {
                triggerParamFindData.setLogEntry("In play prices check failed. " + errMsg);
                return null;
            }
        }

        /* Ignore distance check */
        triggerParamFindData.setLastUsedMarketPricesList(marketPricesList);
        return marketPricesList;
    }

    /**
     * Verifies that any required market types are present and may filter out any that we don't want to use (by setting
     * valid flag to false) for the param find
     * 
     * @param marketPricesList - may be modified on exit;
     * @param priceSourceWeights
     * @return true if required market types are present, else false
     */
    protected String processPricesPreMatch(MarketPricesList marketPricesList, PriceSourceWeights priceSourceWeights,
                    EventSettings eventSettings, MatchState matchState) {

        marketPricesList.setWeights(priceSourceWeights, this.marketWeights);
        marketPricesList.filterOutZeroWeightSources();
        marketPricesList.filterOutUnwantedPrices(marketTypesToFilterOutPreMatch);

        boolean triggerParamFindDuetoSpecialRequirement = checkIfContainSpecialFeedsProvider(marketPricesList);
        String errMsg = processAutoControlPricesPreMatch(marketPricesList, priceSourceWeights, eventSettings);

        if (errMsg != null) {
            return errMsg;
        }

        if (eventSettings.isIgnoreBookiePrices()) {
            errMsg = "Flag set to ignore bookie prices. Don't param find from input prices.";
            return errMsg;
        }

        String missingTypes = marketTypesNotInPricesList(marketTypesRequiredPreMatch, marketPricesList);
        if (missingTypes != null) {
            if (triggerParamFindDuetoSpecialRequirement) {
                debug("EventID: " + matchState.getEventId()
                                + ". Trigger Param find still, missing prematch market types: " + missingTypes + ".  "
                                + marketPricesList.toString());
                return null;
            } else {
                debug("EventID: " + matchState.getEventId() + ". Missing prematch market types: " + missingTypes + ".  "
                                + marketPricesList.toString());
                return "Missing prematch market types: " + missingTypes + ".";
            }
        } else
            return null;
    }

    /**
     * Verifies that any required market types are present and filter out any that we dn't want to use for the param
     * find
     * 
     * @param marketPricesList - may be modified on exit;
     * @param priceSourceWeights
     * @return true if required market types are present, else false
     */
    protected String processPricesInPlay(MarketPricesList marketPricesList, MatchState matchState,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings) {

        marketPricesList.setWeights(priceSourceWeights, this.marketWeights);
        marketPricesList.filterOutZeroWeightSources();
        marketPricesList.filterOutUnwantedPrices(marketTypesToFilterOutInPlay);
        boolean triggerParamFindDuetoSpecialRequirement = checkIfContainSpecialFeedsProvider(marketPricesList);
        String errMsg = processAutoControlPricesInPlay(marketPricesList, priceSourceWeights, eventSettings);

        if (errMsg != null) {
            return errMsg;
        }

        if (eventSettings.isIgnoreBookiePrices()) {
            errMsg = "Flag set to ignore bookie prices. Don't param find from input prices.";
            return errMsg;
        }

        String missingTypes = marketTypesNotInPricesList(marketTypesRequiredInPlay, marketPricesList);
        if (missingTypes != null) {
            if (triggerParamFindDuetoSpecialRequirement) {
                debug("EventID: " + matchState.getEventId()
                                + ". Trigger Param find still, missing inplay market types: " + missingTypes + ".  "
                                + marketPricesList.toString());
                return null;
            } else {
                debug("EventID: " + matchState.getEventId() + ". Missing inplay market types: " + missingTypes + ".  "
                                + marketPricesList.toString());
                return "Missing inplay market types: " + missingTypes + ".";
            }
        } else
            return null;
    }

    /**
     * determines whether the required number of instances of each required marketType are present in marketPricesList
     * 
     * @param marketTypesRequired
     * @param marketPricesList
     * @return list of marketTypesRequired not in marketPricesList, or null if all present
     */
    public static String marketTypesNotInPricesList(Map<String, Integer> marketTypesRequired,
                    MarketPricesList marketPricesList) {
        /*
         * in opList outer map is mktKey, inner map is key: sequenceId value: priceCount
         */
        Map<String, Map<String, Integer>> opList = new HashMap<>(marketTypesRequired.size());
        for (Entry<String, Integer> e : marketTypesRequired.entrySet()) {
            Map<String, Integer> data = new HashMap<>();

            String marketType = e.getKey();
            if (marketType.contains(",")) {
                /*
                 * a list of market types that can be used interchangeably, e.g.for football at least one of Match
                 * Result and Asian Handicap must be provided
                 */
                String[] types = marketType.split(",");
                for (String type : types) {
                    // the interchangeable market types should share the same
                    // 'data' map
                    opList.put(type, data);
                }
            } else {
                opList.put(marketType, data);
            }
        }
        /*
         * generate the structure which contains for each required mktType the number of prices supplied for each
         * sequenceId
         */

        for (MarketPrices marketPrices : marketPricesList.values()) {
            for (MarketPrice marketPrice : marketPrices) {
                if (marketPrice.isValid()) {
                    String mType = marketPrice.getType();
                    Map<String, Integer> data = opList.get(mType);
                    if (data != null) {
                        String sequenceId = marketPrice.getSequenceId();
                        Integer i = data.get(sequenceId);
                        if (i == null)
                            data.put(sequenceId, 1);
                        else {
                            i++;
                            data.put(sequenceId, i);
                        }
                    }
                }

            }
        }
        /*
         * check whether we have enough prices for same sequenceId to meet requirement
         */
        List<String> opStrings = new ArrayList<>();
        for (Entry<String, Integer> e : marketTypesRequired.entrySet()) {
            String mktType = e.getKey();
            int reqdPriceCount = e.getValue();
            boolean pricesOk = false;
            StringBuilder buf = new StringBuilder();

            if (mktType.contains(",")) {
                int totalMatchingPrices = 0;
                String[] types = mktType.split(",");

                for (String type : types) {
                    Map<String, Integer> data = opList.get(type);
                    if (data.size() > 0) {
                        for (Entry<String, Integer> e2 : data.entrySet()) {
                            totalMatchingPrices += e2.getValue();
                        }

                        if (totalMatchingPrices >= reqdPriceCount) {
                            pricesOk = true;
                        } else {
                            buf.append(mktType + ": " + reqdPriceCount + " required, " + totalMatchingPrices
                                            + " provided.  ");
                        }
                    } else {
                        buf.append(mktType + ": " + reqdPriceCount + " required, 0 provided.");
                    }
                }
            } else {
                Map<String, Integer> data = opList.get(mktType);
                if (data.size() > 0) {
                    for (Entry<String, Integer> e2 : data.entrySet()) {
                        String sequenceId = e2.getKey();
                        int nPrices = e2.getValue();
                        if (nPrices >= reqdPriceCount) {
                            pricesOk = true;
                        } else {
                            buf.append(mktType + "_" + sequenceId + ": " + reqdPriceCount + " required, " + nPrices
                                            + " provided.");
                        }
                    }
                } else
                    buf.append(mktType + ": " + reqdPriceCount + " required, 0 provided.");
            }

            if (!pricesOk)
                opStrings.add(buf.toString());
        }
        /*
         * if opStrings contains anything then one or more of the supplied prices has failed the test
         */
        String opString = null;
        if (opStrings.size() > 0) {
            opString = "";
            for (String s : opStrings)
                opString += s;
        }
        return (opString);
    }

    protected String processAutoControlPricesPreMatch(MarketPricesList marketPricesList,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings) {

        String[] arrayForMarketGroup;
        String[] arrayForMarketList;

        if (bookiePriorityForPreMatchParamFinding.isEmpty()) {
            checkGroupsOfMarketsToFilterOrKeep(marketTypeGroupsRequiredPreMatch, marketPricesList);
        } else {
            Map<String, Integer> orderedBookiePriorityGroup =
                            sortBookiePriorityMap(bookiePriorityForPreMatchParamFinding);

            if (!(marketTypeGroupsRequiredPreMatch.isEmpty())) {
                arrayForMarketGroup = marketTypeGroupsRequiredPreMatch.toArray(new String[0]);
            } else {
                arrayForMarketGroup = null;
            }
            if (!(marketTypeListRequiredPreMatch.isEmpty())) {
                arrayForMarketList = marketTypeListRequiredPreMatch.toArray(new String[0]);
            } else {
                arrayForMarketList = null;
            }

            String errMsg = filterBookiesByPriorityListing(orderedBookiePriorityGroup, numberOfBookiesRequiredPrematch,
                            arrayForMarketGroup, arrayForMarketList, marketPricesList,
                            maxNumberOfBookiesWantedPrematch);

            if (errMsg != null) {
                return errMsg;
            }
        }

        return null;
    }

    /**
     * 
     * A copy of processAutoControlPricesInPlay but removed the marketTypeListRequiredInPlay check, this method is to be
     * used in the specific client trigger paramfind trading rule, since we dont want to change the scope of the fields
     * in this class
     * 
     */
    protected String processAutoControlPricesInPlay2(MarketPricesList marketPricesList,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings,
                    Set<String> marketTypesRequiredAtPeriod, Set<String> marketGroupsRequiredAtPeriod) {

        String[] arrayForMarketGroup;
        String[] arrayForMarketList;

        if (bookiePriorityForInPlayParamFinding.isEmpty()) {
            checkGroupsOfMarketsToFilterOrKeep(marketTypeGroupsRequiredInPlay, marketPricesList);
        } else {
            Map<String, Integer> orderedBookiePriorityGroup =
                            sortBookiePriorityMap(bookiePriorityForInPlayParamFinding);

            if (!(marketGroupsRequiredAtPeriod.isEmpty())) {
                arrayForMarketGroup = marketGroupsRequiredAtPeriod.toArray(new String[0]);
            } else {
                arrayForMarketGroup = null;
            }

            if (!(marketTypesRequiredAtPeriod.isEmpty())) {
                arrayForMarketList = marketTypesRequiredAtPeriod.toArray(new String[0]);
            } else {
                arrayForMarketList = null;
            }

            String errMsg = filterBookiesByPriorityListing(orderedBookiePriorityGroup, 1, arrayForMarketGroup,
                            arrayForMarketList, marketPricesList, maxNumberOfBookiesWantedInplay);

            if (errMsg != null) {
                return errMsg;
            }
        }

        return null;
    }

    protected String processAutoControlPricesInPlay(MarketPricesList marketPricesList,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings) {

        String[] arrayForMarketGroup;
        String[] arrayForMarketList;

        if (bookiePriorityForInPlayParamFinding.isEmpty()) {
            checkGroupsOfMarketsToFilterOrKeep(marketTypeGroupsRequiredInPlay, marketPricesList);
        } else {
            Map<String, Integer> orderedBookiePriorityGroup =
                            sortBookiePriorityMap(bookiePriorityForInPlayParamFinding);

            if (!(marketTypeGroupsRequiredInPlay.isEmpty())) {
                arrayForMarketGroup = marketTypeGroupsRequiredInPlay.toArray(new String[0]);
            } else {
                arrayForMarketGroup = null;
            }
            if (!(marketTypeListRequiredInPlay.isEmpty())) {
                arrayForMarketList = marketTypeListRequiredInPlay.toArray(new String[0]);
            } else {
                arrayForMarketList = null;
            }

            String errMsg = filterBookiesByPriorityListing(orderedBookiePriorityGroup, numberOfBookiesRequiredInplay,
                            arrayForMarketGroup, arrayForMarketList, marketPricesList, maxNumberOfBookiesWantedInplay);

            if (errMsg != null) {
                return errMsg;
            }
        }

        return null;
    }


    private String filterBookiesByPriorityListing(Map<String, Integer> orderedBookiePriorityGroup,
                    int minNumberOfBookiesNeeded, String[] arrayForMarketGroup, String[] arrayForMarketList,
                    MarketPricesList marketPricesList, int maxNumberOfBookiesWanted) {

        int numberOfMarketsNeeded = 0;

        if (arrayForMarketGroup != null) {
            numberOfMarketsNeeded = arrayForMarketGroup.length;
        }

        Set<String> acceptedBookies = new HashSet<String>();

        boolean allMarketsWithinBookie = false;

        bookieLoop: for (Entry<String, Integer> bookie : orderedBookiePriorityGroup.entrySet()) {
            marketPricesLoop: for (Entry<String, MarketPrices> marketPrices : marketPricesList.entrySet()) {
                if (marketPrices.getKey().equals(bookie.getKey()) && marketPrices.getValue().getSourceWeight() != 0.0) {
                    // Checking the specific bookie to see if it has all the markets needed from the group.
                    int numMarketFromRequiredGroups = 0;
                    for (MarketPrice market : marketPrices.getValue()) {
                        if (arrayForMarketGroup != null) {
                            for (int i = 0; i < arrayForMarketGroup.length; i++) {
                                if (market.getType().equals(arrayForMarketGroup[i]) && market.isValid()) {
                                    numMarketFromRequiredGroups++;
                                    break;
                                }
                            }
                        }
                    }
                    if (numMarketFromRequiredGroups == numberOfMarketsNeeded) {
                        if (arrayForMarketList != null) {
                            // Checking the specific bookie to see if it has any market from the list.
                            if (!allMarketsWithinBookie) {
                                for (MarketPrice market : marketPrices.getValue()) {
                                    for (int i = 0; i < arrayForMarketList.length; i++) {
                                        if (market.getType().equals(arrayForMarketList[i]) && market.isValid()) {
                                            acceptedBookies.add(marketPrices.getKey());
                                            break marketPricesLoop;
                                        }
                                    }
                                }
                            }
                        } else {
                            // No need to check the list as it is empty.;
                            acceptedBookies.add(marketPrices.getKey());
                        }
                    } else {
                        // Breaking out of this bookie as we didn't get the required markets from the group.
                        break;
                    }
                }
            }
            if (maxNumberOfBookiesWanted == acceptedBookies.size()
                            || (acceptedBookies.size() >= minNumberOfBookiesNeeded && useAllBookies)) {
                break bookieLoop;
            }
        }

        if (!(acceptedBookies.size() >= minNumberOfBookiesNeeded)) {
            return ("Did not have enough correct sources for param find.");
        }

        if (useAllBookies) {
            return null;
        }

        for (Entry<String, MarketPrices> marketPrices : marketPricesList.entrySet()) {
            boolean bookieInAcceptedList = false;
            for (String bookieIncludedInParamFind : acceptedBookies) {
                if (bookieIncludedInParamFind.equals(marketPrices.getKey())) {
                    bookieInAcceptedList = true;
                }
            }
            if (!bookieInAcceptedList) {
                marketPrices.getValue().setSourceWeight(0.0);
                for (MarketPrice marketPrice : marketPrices.getValue()) {
                    marketPrice.setValid(false);
                }
            }
        }
        return null;

    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortBookiePriorityMap(Map<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(/* Collections.reverseOrder() */)).collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void checkGroupsOfMarketsToFilterOrKeep(Set<String> marketTypeGroupsRequired,
                    MarketPricesList marketPricesList) {


        int numOfMarkets = marketTypeGroupsRequired.size();


        boolean isInList = false;

        String[] array = marketTypeGroupsRequired.toArray(new String[0]);

        for (int i = 0; i < array.length; i++) {
            for (Entry<String, MarketPrices> marketPricesEntry : marketPricesList.entrySet()) {
                if (marketPricesEntry.getValue().getSourceWeight() > 0.0) {
                    for (MarketPrice marketPrice : marketPricesEntry.getValue()) {
                        if (marketPrice.getType().equals(array[i]) && marketPrice.isValid()) {
                            isInList = true;
                            break;
                        }
                    }
                }
                if (isInList) {
                    numOfMarkets = numOfMarkets - 1;
                    isInList = false;
                }
            }

        }

        if (numOfMarkets < marketTypeGroupsRequired.size() && numOfMarkets > 0) {
            for (int i = 0; i < array.length; i++) {
                for (MarketPrices marketPrices : marketPricesList.values()) {
                    for (MarketPrice marketPrice : marketPrices) {
                        if (marketPrice.getType().equals(array[i])) {
                            marketPrice.setValid(false);
                            break;
                        }
                    }
                }
            }

        }

        // FIXME: the above codes need to be tided up, shift the underneath code into other functions maybe.
        checkIfContainSpecialFeedsProvider(marketPricesList);

    }

    private boolean checkIfContainSpecialFeedsProvider(MarketPricesList marketPricesList) {
        boolean containsSpecialFeeds = false;
        if (feedsSpecialMarketsParamFindingTriggerList != null)
            for (Map.Entry<String, MarketPrices> marketsFromASource : marketPricesList.getMarketPricesList().entrySet())
                for (Map.Entry<String, List<String>> entry : feedsSpecialMarketsParamFindingTriggerList.entrySet()) {
                    if (entry.getKey().equals(marketsFromASource.getKey()))// check if from same source
                    {
                        for (Map.Entry<String, MarketPrice> check : marketsFromASource.getValue().getMarketPrices()
                                        .entrySet()) // get the map of a market

                            if (entry.getValue().contains(check.getValue().getType())) {
                                check.getValue().setValid(true);
                                containsSpecialFeeds = true;
                            }
                    }
                }

        return containsSpecialFeeds;
    }

}
