package ats.algo.matchrunner.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * implements a two column key value table
 * 
 * @author Geoff
 * 
 */
public class ObservableMap {

    /**
     * function which returns the Map<String,String> on which this observable based
     * 
     * @author Geoff
     * 
     */


    /**
     * used to update the underlying object when the data is changed through the GUI the String returned is null if ok,
     * else contains an appropriate error msg
     * 
     * @author Geoff
     * 
     */
    @FunctionalInterface
    public interface NotifyMapChanged {
        public String notify(Map<String, String> map);
    }

    private NotifyMapChanged notifyMapChanged;

    private ObservableList<ObservableMapRow> observableMap = FXCollections.observableArrayList();
    private ObservableList<ObservableMapRow> observableMapPlayers = FXCollections.observableArrayList();

    /**
     * 
     * @param map the map<String,String> this object represents
     * @param notifyMapChanged the function that updates the underlying object when the map changes via the gui
     */
    public ObservableMap(NotifyMapChanged notifyMapChanged) {
        this.notifyMapChanged = notifyMapChanged;
    }

    public void updateDisplayedData(Map<String, String> generalMap) {
        observableMap.clear();
        for (Entry<String, String> entry : generalMap.entrySet()) {
            ObservableMapRow o = new ObservableMapRow(entry.getKey(), entry.getValue());
            observableMap.add(o);
        }

    }

    /**
     * updates the displayed data from the contents of the map
     * 
     * @param publishedMatchState
     */
    public void updateDisplayedData(Map<String, String> generalMap, Map<String, String> playersMap) {
        updateDisplayedData(generalMap);
        observableMapPlayers.clear();
        if (playersMap != null) {
            for (Entry<String, String> entry : playersMap.entrySet()) {
                ObservableMapRow o = new ObservableMapRow(entry.getKey(), entry.getValue());
                observableMapPlayers.add(o);
            }
        }
    }

    /**
     * updates the underlying map object from ObservableMap method executed following user change to data in the gui
     */
    private String updateUnderlyingMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (ObservableMapRow o : observableMap) {
            map.put(o.getDisplayKey(), o.getValue());
        }
        return notifyMapChanged.notify(map);
    }

    /**
     * returns the ObservableList
     * 
     * @return
     */
    public ObservableList<ObservableMapRow> getData() {
        return observableMap;
    }

    /**
     * returns the ObservableList
     * 
     * @return
     */
    public ObservableList<ObservableMapRow> getPlayerData() {
        return observableMapPlayers;
    }

    /**
     * a single row in ObservableMap
     * 
     * @author Geoff
     * 
     */
    public class ObservableMapRow {
        private final int index;
        private String errMsg;
        private final String key; // the key created when the object is created
                                  // - never changed
        private final StringProperty displayKey; // the name to display,
                                                 // initally the same as key,
                                                 // but may be changed
        private final StringProperty value;
        private String valueStr;

        /**
         * creates a row that can be added to ObservableMap
         * 
         * @param key
         * @param value
         */
        public ObservableMapRow(String key, String value) {
            this.key = key;
            this.displayKey = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
            this.valueStr = value;
            this.index = observableMap.size(); // bit of a fudged way of getting
                                               // an index. Relies on map being
                                               // LinkedHashMap
        }

        public StringProperty keyProperty() {
            return displayKey;
        }

        public StringProperty valueProperty() {
            return value;
        }

        /**
         * returns the text associated with this key for display purposes
         * 
         * @return
         */
        public String getDisplayKey() {
            return this.displayKey.get();
        }

        /**
         * returns the unchanging key for this row
         * 
         * @return
         */
        public String getKey() {
            return key;
        }

        /**
         * 
         * @param newValue
         */
        public void setDisplayKey(String newValue) {
            this.displayKey.set(newValue);
            updateUnderlyingMap();
        }

        /**
         * 
         * @param newValue
         */
        public void setValue(String newValue) {
            value.set(newValue);
            errMsg = updateUnderlyingMap();
            if (errMsg == null) {
                value.set(newValue);
                valueStr = newValue;
            } else {
                value.set(valueStr);
            }
        }

        /**
         * 
         * @return
         */
        public String getValue() {
            return this.value.get();
        }

        /**
         * returns the index associated with this element: 0,1,...
         * 
         * @return
         */
        public int getIndex() {
            return index;
        }

        public String getErrorMsg() {
            return errMsg;
        }

    }



}
