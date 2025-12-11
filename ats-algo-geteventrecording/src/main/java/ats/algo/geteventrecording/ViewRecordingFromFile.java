package ats.algo.geteventrecording;

import java.io.IOException;
import java.util.List;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.Time;

public class ViewRecordingFromFile {


    /*
     * http://iomsampss01:8083/betsync/rest/out/mostRecentHeaders/5
     * https://iomsampss01/betsync/out/matchRecording/4742483
     * 
     */

    public static void main(String[] args) {
        System.out.println("Utility to display the recording of an event from file");
        Recording recording = null;
        do {
            String fileName = ConsoleInput.readString("Enter fileName:", "C:\\AAtmp\\recording.json");

            try {
                recording = Recording.readFromFile(fileName);
            } catch (IOException e) {
                System.out.println("File read error");
                e.printStackTrace();
            }
        } while (recording == null);
        reviewRecording(recording);

    }

    static void reviewRecording(Recording recording) {
        List<RecordedItem> items = recording.getRecordedItemList();
        System.out.println(items.size() + " Recordings:");
        for (int i = 0; i < items.size(); i++) {
            int n = i + 1;
            RecordedItem item = items.get(i);
            String itemTime = Time.getDateAsString(item.getTimeRequestIssued());
            String actionDescription = String.format("%d: %s %s", n, itemTime, item.shortDescription());
            System.out.println(actionDescription);
        }
        int nextDefaultItemNo = 1;
        boolean finished = false;
        do {
            int i = ConsoleInput.readInt("Enter item no for full detail, or 0 to exit", nextDefaultItemNo, false);
            if (i == 0)
                finished = true;
            else {
                if (items.size() >= i + 1) {
                    nextDefaultItemNo = i + 1;
                }
                int itemIndex = i - 1;
                try {
                    RecordedItem item = items.get(itemIndex);
                    System.out.println(item.toString());
                } catch (IndexOutOfBoundsException ioobe) {
                    System.out.println("There is no " + (itemIndex + 1) + "th recording");
                }
            }
        } while (!finished);
    }

}
