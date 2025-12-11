package ats.algo.outrights.model;

import ats.algo.core.markets.Market;
import ats.algo.sport.outrights.server.api.MarketListEntry;
import ats.algo.sport.outrights.server.api.MarketsList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Markets class to convert the data to/from the data format needed for a tree list view so it can be
 * displayed in the GUI
 * 
 * @author Rob
 * 
 */
@SuppressWarnings("restriction")
public class ObservableMarkets {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableMarket> ObservableMarkets;

    public ObservableMarkets() {
        ObservableMarkets = FXCollections.observableArrayList();
    }

    public void update(MarketsList marketsList) {

        ObservableMarkets.clear();
        for (MarketListEntry marketListEntry : marketsList.getEntries()) {
            ObservableMarket observableTeam = new ObservableMarket(marketListEntry, marketsList.getEventId());
            ObservableMarkets.add(observableTeam);
        }
    }

    public void update(MarketsList marketsList, String marketFilter) {
        ObservableMarkets.clear();
    }

    public ObservableList<ObservableMarket> getData() {
        return ObservableMarkets;
    }

    public class ObservableMarket {
        /*
         * Stuff to display
         */
        private StringProperty id;
        private StringProperty name;
        private StringProperty eventID;


        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableMarket(MarketListEntry marketListEntry, long eventIdMarket) {
            this.eventID = new SimpleStringProperty(String.valueOf(eventIdMarket));
            this.id = new SimpleStringProperty(marketListEntry.getId());
            this.name = new SimpleStringProperty(marketListEntry.getName());
        }

        /**
         * Constructor for rows other than the first row of a market
         * 
         * @param key
         * @param sequenceId
         * @param selectionName
         * @param nSelections
         * @param prob
         */
        public ObservableMarket(String id, String name) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
        }


        public StringProperty eventIDProperty() {
            return eventID;
        }


        public StringProperty iDProperty() {
            return id;
        }


        public StringProperty nameProperty() {
            return name;
        }

        public String getId() {
            return id.getValue();
        }

        public String getName() {
            return name.getValue();
        }

        public long getEventId() {
            return Long.valueOf(eventID.getValue());
        }


        public MarketListEntry getMarketListEntry() {
            Market market = new Market();
            market.setType(id.getValue());
            market.setMarketDescription(name.getValue());
            MarketListEntry marketListEntry = new MarketListEntry(market);
            return marketListEntry;
        }

    }

}
