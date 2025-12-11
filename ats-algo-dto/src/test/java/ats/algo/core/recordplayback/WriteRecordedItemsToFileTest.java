package ats.algo.core.recordplayback;

import ats.algo.core.recordplayback.RecordedItem.RecordedItemType;

public class WriteRecordedItemsToFileTest {

    public static final String archiveDirectory = "c:/aatmp";

    public static void main(String[] args) {
        long eventId = 123456;
        RecordingHeader hdr = new RecordingHeader();
        hdr.setEventId(eventId);
        RecordedItem item1 = new RecordedItem();
        item1.setEventId(eventId);
        item1.setUniqueRequestId("R-1");
        item1.setRecordedItemType(RecordedItemType.PRICE_CALC);
        RecordedItem item2 = new RecordedItem();
        item2.setEventId(eventId);
        item2.setRecordedItemType(RecordedItemType.PARAM_FIND);
        item2.setUniqueRequestId("R-2");
        try {
            hdr.writeHeaderToFile(archiveDirectory);
            item1.writeItemToFile(archiveDirectory);
            item2.writeItemToFile(archiveDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
