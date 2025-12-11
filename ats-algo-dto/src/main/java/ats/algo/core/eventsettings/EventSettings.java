package ats.algo.core.eventsettings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, GeneralEventSetting> generalEventSettings;
    private Map<String, ChannelEventSetting> channelEventSettings;

    private static final String DEFAULT_EVENT_TIER_STR = "3";
    public static final int DEFAULT_EVENT_TIER = 3;

    private static final String DEFAULT_FLAG_FOR_USING_BOOKIE_PRICES_STR = "false";
    public static final boolean DEFAULT_FLAG_FOR_USING_BOOKIE_PRICES = false;

    public EventSettings() {
        generalEventSettings = new HashMap<String, GeneralEventSetting>();
        GeneralEventSetting eventTierSetting = new GeneralEventSetting(true, DEFAULT_EVENT_TIER_STR);
        GeneralEventSetting ignoreBookiePricesSetting =
                        new GeneralEventSetting(true, DEFAULT_FLAG_FOR_USING_BOOKIE_PRICES_STR);
        generalEventSettings.put("eventTier", eventTierSetting);
        generalEventSettings.put("ignoreBookiePrices", ignoreBookiePricesSetting);
    }

    public void updateEventSettings(Map<String, String> properties) {
        if (properties != null) {
            for (Entry<String, String> e : properties.entrySet()) {
                String key = e.getKey();
                String newValue = e.getValue();
                GeneralEventSetting oldSetting = generalEventSettings.get(key);
                if (oldSetting == null) {
                    GeneralEventSetting newSetting = new GeneralEventSetting(false, newValue);
                    generalEventSettings.put(key, newSetting);
                } else {
                    oldSetting.setValue(newValue);
                }
            }
        }
    }

    public Map<String, GeneralEventSetting> getGeneralEventSettings() {
        return generalEventSettings;
    }

    public void setGeneralEventSettings(Map<String, GeneralEventSetting> generalEventSettings) {
        this.generalEventSettings = generalEventSettings;
    }

    public Map<String, ChannelEventSetting> getChannelEventSettings() {
        return channelEventSettings;
    }

    public void setChannelEventSettings(Map<String, ChannelEventSetting> channelEventSettings) {
        this.channelEventSettings = channelEventSettings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channelEventSettings == null) ? 0 : channelEventSettings.hashCode());
        result = prime * result + ((generalEventSettings == null) ? 0 : generalEventSettings.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventSettings other = (EventSettings) obj;
        if (channelEventSettings == null) {
            if (other.channelEventSettings != null)
                return false;
        } else if (!channelEventSettings.equals(other.channelEventSettings))
            return false;
        if (generalEventSettings == null) {
            if (other.generalEventSettings != null)
                return false;
        } else if (!generalEventSettings.equals(other.generalEventSettings))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @JsonIgnore
    public boolean isIgnoreBookiePrices() {
        GeneralEventSetting ignoreBookiePrices = generalEventSettings.get("ignoreBookiePrices");
        String ignoreBookiePricesStr = ignoreBookiePrices.getValue();
        boolean useBookiePrices = DEFAULT_FLAG_FOR_USING_BOOKIE_PRICES;
        try {
            useBookiePrices = Boolean.parseBoolean(ignoreBookiePricesStr);
        } catch (Exception e) {
            /*
             * do nothing
             */
        }
        return useBookiePrices;
    }

    @JsonIgnore
    public String isIgnoreBookiePricesStr() {
        return generalEventSettings.get("ignoreBookiePrices").getValue();
    }

    @JsonIgnore
    public int getEventTier() {
        GeneralEventSetting eventTierSetting = generalEventSettings.get("eventTier");
        String eventTierStr = eventTierSetting.getValue();
        int eventTier = DEFAULT_EVENT_TIER;
        try {
            eventTier = Integer.parseInt(eventTierStr);
        } catch (Exception e) {
            /*
             * do nothing
             */
        }
        return eventTier;
    }

    @JsonIgnore
    public String getEventTierStr() {
        return generalEventSettings.get("eventTier").getValue();
    }

    /**
     * extract the set of sourceWeights from eventSettings. Expected to have name of the form SOURCEWEIGHT_<sourceName>
     * 
     * @return
     */
    @JsonIgnore
    public Map<String, Double> getSourceWeights() {
        Map<String, Double> sourceWeights = new HashMap<>();
        for (Entry<String, GeneralEventSetting> e : generalEventSettings.entrySet()) {
            String key = e.getKey().toUpperCase();
            if (key.contains("SOURCEWEIGHT")) {
                try {
                    int idx = key.indexOf('_');
                    String sourceName = key.substring(idx + 1).toUpperCase();
                    Double weight = Double.parseDouble(e.getValue().getValue());
                    sourceWeights.put(sourceName, weight);
                } catch (Exception ex) {
                    /*
                     * do nothing - ignore source weight if value is not parse able
                     */
                }
            }
        }
        return sourceWeights;
    }

    public static EventSettings generateEventSettingsForTesting(long eventTier) {
        EventSettings eventSettings = new EventSettings();
        String eventTierStr = String.valueOf(eventTier);
        GeneralEventSetting eventTierSetting = eventSettings.getGeneralEventSettings().get("eventTier");
        eventTierSetting.setValue(eventTierStr);
        return eventSettings;

    }

}
