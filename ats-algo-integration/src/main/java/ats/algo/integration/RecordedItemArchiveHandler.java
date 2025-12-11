package ats.algo.integration;

import ats.algo.core.recordplayback.RecordedItem;

public interface RecordedItemArchiveHandler {

    void startArchive(TradedEvent event);

    void handleArchive(long eventId, RecordedItem recordedItem);

    void finishArchive(Long eventId);

}
