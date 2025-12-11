package ats.algo.core.eventsettings;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.genericsupportfunctions.JsonSerializer;

public class EventSettingsTest {

    @Test
    public void test() {
        EventSettings eventSettings = new EventSettings();
        Map<String, GeneralEventSetting> generalEventSettings = new HashMap<String, GeneralEventSetting>();
        generalEventSettings.put("eventTier", new GeneralEventSetting(true, "1"));
        generalEventSettings.put("playerAName", new GeneralEventSetting(false, "Raphael Nadal"));
        generalEventSettings.put("playerBName", new GeneralEventSetting(false, "Andrew Murray"));
        generalEventSettings.put("startTime", new GeneralEventSetting(false, "2017-06-011T13:00:00"));
        eventSettings.setGeneralEventSettings(generalEventSettings);
        Map<String, ChannelEventSetting> channelEventSettings = new HashMap<String, ChannelEventSetting>();
        eventSettings.setChannelEventSettings(channelEventSettings);
        ChannelEventSetting channelEventSetting = new ChannelEventSetting(true);
        channelEventSetting.setValue("Channel1", "marginAlgo1");
        channelEventSetting.setValue("Channel2", "marginAlgo2");
        channelEventSettings.put("marginAlgo", channelEventSetting);
        System.out.println(eventSettings);
        String json = JsonSerializer.serialize(eventSettings, true);
        System.out.println(json);
        EventSettings eventSettings2 = JsonSerializer.deserialize(json, EventSettings.class);
        assertEquals(eventSettings, eventSettings2);


    }

}
