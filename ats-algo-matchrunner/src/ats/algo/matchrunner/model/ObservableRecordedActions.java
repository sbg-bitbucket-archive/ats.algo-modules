package ats.algo.matchrunner.model;

import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * implements a two column table for displaying the list of actions
 * 
 * @author Geoff
 * 
 */
public class ObservableRecordedActions {

    private ObservableList<ObservableAction> observableActions = FXCollections.observableArrayList();

    /**
     * 
     * @param getFn the function that returns the Map<String,String> to be displayed
     * @param setFn the function that updates the underlying object when the map changes via the gui
     */
    public ObservableRecordedActions(Recording recording) {
        for (int i = 1; i <= recording.size(); i++) {
            RecordedItem item = recording.get(i - 1);
            ObservableAction o = new ObservableAction(i, item.shortDescription());
            observableActions.add(o);
        }
    }

    /**
     * returns the ObservableList
     * 
     * @return
     */
    public ObservableList<ObservableAction> getData() {
        return observableActions;
    }

    /**
     * a single row in ObservableMap
     * 
     * @author Geoff
     * 
     */
    public class ObservableAction {
        private final StringProperty itemNo;
        private final StringProperty itemShortDescription;

        /**
         * creates a row that can be added to ObservableMap
         * 
         * @param key
         * @param value
         */
        public ObservableAction(int no, String value) {
            this.itemNo = new SimpleStringProperty(String.format("%d", no));
            this.itemShortDescription = new SimpleStringProperty(value);
        }

        public StringProperty getItemNo() {
            return itemNo;
        }

        public StringProperty getItemShortDescription() {
            return itemShortDescription;
        }
    }



}
