package ats.algo.integration;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ats.algo.algomanager.AlgoManager;
import ats.algo.core.baseclasses.MatchFormat;
import ats.core.AtsBean;
import ats.core.util.StringUtil;
import generated.ats.betsync.dto.NodeType;
import generated.ats.betsync.dto.Setting;

public class SettingChangedHandler extends AtsBean {

    private static final String EVENT_TIER = "eventTier";
    private static final String TIER_LEVEL = "tierLevel";
    private static final String FEED_WEIGHTINGS = "feedWeightings";
    private static final String IGNORE_BOOKIE_PRICES = "ignoreBookiePrices";
    public static final String PRICING_METHOD_SETTING_NAME = "pricingMethod";
    private AlgoManager algoManager;

    public SettingChangedHandler(AlgoManager algoManager) {
        this.algoManager = algoManager;
    }

    public List<Setting> createBetsyncSettings(long eventId, Map<String, String> properties) {
        List<Setting> settingsList = Lists.newArrayList();
        StringBuilder feedWeightBuilder = new StringBuilder();

        for (Entry<String, String> entry : properties.entrySet()) {
            String propname = entry.getKey();
            if (propname.equals(EVENT_TIER)) {
                settingsList.add(createSetting(eventId, "BOOKMAKING", TIER_LEVEL, entry.getValue()));
            } else if (propname.startsWith("SOURCEWEIGHT")) {
                if (feedWeightBuilder.length() > 0) {
                    feedWeightBuilder.append(";");
                }
                String sourceName = propname.substring(propname.indexOf('_') + 1);
                String weight = entry.getValue();
                feedWeightBuilder.append(sourceName).append("=").append(weight);
            }
        }

        if (feedWeightBuilder.length() > 0) {
            settingsList.add(createSetting(eventId, "RACING", FEED_WEIGHTINGS, feedWeightBuilder.toString()));
        }

        return settingsList;
    }

    public Map<String, String> extractInitialEventCreationSettings(List<Setting> settings) {
        Map<String, String> eventProperties = Maps.newLinkedHashMapWithExpectedSize(settings.size());
        for (Setting setting : settings) {
            String settingName = setting.getSettingName();
            String settingValue = setting.getSettingValue();
            if (TIER_LEVEL.equals(settingName)) {
                eventProperties.put(EVENT_TIER, settingValue);
            } else if (FEED_WEIGHTINGS.equals(settingName)) {
                populateFeedWeightingsEventSettings(settingValue, eventProperties);
            } else if ("competitionId".equals(settingName) || "competitionName".equals(settingName)
                            || "competitionParent".equals(settingName)) {
                eventProperties.put(settingName, settingValue);
            } else if (PRICING_METHOD_SETTING_NAME.equals(settingName)) {
                eventProperties.put(settingName, settingValue);
            }
        }
        return eventProperties;
    }

    public void handleChangedSetting(Setting setting, Collection<TradedEvent> matchEvents) {
        if (TIER_LEVEL.equals(setting.getSettingName())) {
            handleEventTierChange(setting, matchEvents);
        } else if ("MATCHFORMAT".equals(setting.getSettingCategory()) && "params".equals(setting.getSettingName())) {
            handleMatchFormatChange(setting, matchEvents);
        } else if (FEED_WEIGHTINGS.equals(setting.getSettingName())) {
            handleBookieWeightingsChange(setting, matchEvents);
        } else if (IGNORE_BOOKIE_PRICES.equals(setting.getSettingName())) {
            handleIgnoreBookiePricesChange(setting, matchEvents);
        } else if (PRICING_METHOD_SETTING_NAME.equals(setting.getSettingName())) {
            handlePricingMethodChange(setting, matchEvents);
        }
    }

    private Setting createSetting(long eventId, String category, String name, String value) {
        Setting s = new Setting();
        s.setNodeId(eventId);
        s.setNodeType(NodeType.EVENT);
        s.setSettingCategory(category);
        s.setSettingName(name);
        s.setSettingValue(value);
        return s;
    }

    private void handleBookieWeightingsChange(Setting setting, Collection<TradedEvent> matchEvents) {
        String settingValue = setting.getSettingValue();
        List<TradedEvent> eventsToUpdate = Lists.newArrayList();
        for (TradedEvent te : matchEvents) {
            if (te.isForResultingOnly() || te.isCompleted()) {
                continue;
            }
            try {
                if (te.isDescendantOf(setting.getPath()) && te.isNewBookieWeightings(settingValue)
                                && !te.justPublishedSetting(setting.getSettingName())) {
                    eventsToUpdate.add(te);
                }
            } catch (NullPointerException npe) {
                error(npe);
            }
        }

        if (eventsToUpdate.isEmpty()) {
            return;
        }

        Map<String, String> feedWeightings = newHashMap();
        populateFeedWeightingsEventSettings(settingValue, feedWeightings);

        for (TradedEvent te : eventsToUpdate) {
            try {
                algoManager.handleSetEventProperties(te.getEventId(), feedWeightings);
            } catch (Exception e) {
                error("Problem in handleBookieWeightingsChange " + te.getEventId(), e);
            }
        }
    }

    protected void populateFeedWeightingsEventSettings(String settingValue, Map<String, String> feedWeightings) {
        String[] tokenisedBookieNameWeightPairs = settingValue.split(";");

        for (int i = 0; i < tokenisedBookieNameWeightPairs.length; i++) {
            String[] bookieNameWeightingPair = tokenisedBookieNameWeightPairs[i].split("=");
            feedWeightings.put("SOURCEWEIGHT_" + bookieNameWeightingPair[0], bookieNameWeightingPair[1]);
        }
    }

    private void handleMatchFormatChange(Setting setting, Collection<TradedEvent> matchEvents) {
        String path = setting.getPath();
        Map<String, String> splitStringsMap = StringUtil.splitStringsMap(setting.getSettingValue(), ";");
        Set<Entry<String, String>> formatEntrySet = splitStringsMap.entrySet();
        for (TradedEvent te : matchEvents) {
            try {
                if (!te.isForResultingOnly() && !te.isCompleted() && te.isDescendantOf(path)) {
                    if (!te.isPrematch()) {
                        info("Leaving match format as is for inplay %s", te);
                        continue;
                    }
                    MatchFormat matchFormat = te.getMatchFormat();
                    Map<String, String> matchFormatMap = matchFormat.getAsMap();
                    updateFormatValues(formatEntrySet, matchFormatMap);
                    matchFormat.setFromMap(matchFormatMap);

                    String tier = setting.getTier();
                    if (null == tier) {
                        tier = "3"; // set to default if not specified
                    }
                    algoManager.handleResetEvent(te.getEventId(), matchFormat, Long.parseLong(tier));
                }
            } catch (Exception e) {
                error("Problem in handleResetEvent " + te.getEventId(), e);
            }
        }
    }

    protected void updateFormatValues(Set<Entry<String, String>> formatEntrySet, Map<String, String> matchFormatMap) {
        formatEntrySet.forEach(entry -> {
            String key = entry.getKey();
            if (matchFormatMap.containsKey(key)) {
                matchFormatMap.put(key, entry.getValue());
            }
        });
    }

    private void handleIgnoreBookiePricesChange(Setting setting, Collection<TradedEvent> matchEvents) {
        Map<String, String> algoManagerTierProperty = Maps.newHashMapWithExpectedSize(1);
        algoManagerTierProperty.put(IGNORE_BOOKIE_PRICES, setting.getSettingValue());
        setEventPropertiesInAlgoManager(setting.getSettingName(), setting.getPath(), matchEvents,
                        algoManagerTierProperty);
    }

    private void handleEventTierChange(Setting setting, Collection<TradedEvent> matchEvents) {
        Map<String, String> algoManagerTierProperty = Maps.newHashMapWithExpectedSize(1);
        algoManagerTierProperty.put(EVENT_TIER, setting.getSettingValue());
        setEventPropertiesInAlgoManager(setting.getSettingName(), setting.getPath(), matchEvents,
                        algoManagerTierProperty);
    }

    private void handlePricingMethodChange(Setting setting, Collection<TradedEvent> matchEvents) {
        for (TradedEvent te : matchEvents) {
            if (te.isForResultingOnly() || te.isCompleted() || !te.isDescendantOf(setting.getPath())) {
                continue;
            }
            te.setPricingMethod(setting.getSettingValue());
            if (!te.isMirroredPricingInAts()) {
                try {
                    info("%s method of pricing is now AlgoManager, will request republishing of data", te);
                    algoManager.handleRepublishData(te.getEventId());
                } catch (Exception ex) {
                    error("Problem republishing event %s data", ex, te.getEventId());
                }
            }
        }
    }

    private void setEventPropertiesInAlgoManager(String settingName, String path, Collection<TradedEvent> matchEvents,
                    Map<String, String> propertiesMap) {
        for (TradedEvent te : matchEvents) {
            if (te.isForResultingOnly() || te.isCompleted()) {
                continue;
            }
            try {
                if (te.isDescendantOf(path) && !te.justPublishedSetting(settingName)) {
                    algoManager.handleSetEventProperties(te.getEventId(), propertiesMap);
                }
            } catch (Exception e) {
                error(e);
            }
        }
    }
}
