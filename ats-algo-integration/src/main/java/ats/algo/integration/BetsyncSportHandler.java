package ats.algo.integration;

import java.util.Map;

import ats.algo.algomanager.EventStateBlob;

public interface BetsyncSportHandler {
    void handleEvent(TradedEvent event, EventStateBlob eventState, Map<String, String> eventProperties);
}
