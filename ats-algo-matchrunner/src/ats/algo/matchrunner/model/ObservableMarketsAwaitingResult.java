package ats.algo.matchrunner.model;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Markets to convert the data to/from the data format needed for a table view so it can be displayed in
 * the GUI
 * 
 * @author Geoff
 * 
 */
public class ObservableMarketsAwaitingResult {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableMarketAwaitingResult> observableMarketsAwaitingResult;

    public ObservableMarketsAwaitingResult() {
        observableMarketsAwaitingResult = FXCollections.observableArrayList();
    }

    public void update(Markets marketsAwaitingResult) {

        observableMarketsAwaitingResult.clear();
        for (Market market : marketsAwaitingResult) {
            ObservableMarketAwaitingResult om = new ObservableMarketAwaitingResult(market);
            observableMarketsAwaitingResult.add(om);
        }
    }

    public ObservableList<ObservableMarketAwaitingResult> getData() {
        return observableMarketsAwaitingResult;
    }

    public class ObservableMarketAwaitingResult {

        private StringProperty resultedMarketType; // what gets displayed, which
                                                   // will be blank if not the
                                                   // first row
        private StringProperty resultedMarketSubType;
        private StringProperty resultedMarketSequenceId;
        private StringProperty resultedMarketDescription;
        private StringProperty resultedMarketActualLine;
        private StringProperty resultedMarketWinningSelection;



        public ObservableMarketAwaitingResult(Market market) {
            this.resultedMarketType = new SimpleStringProperty(market.getType());
            this.resultedMarketSubType = new SimpleStringProperty(market.getLineId());
            this.resultedMarketSequenceId = new SimpleStringProperty(market.getSequenceId());
            this.resultedMarketDescription = new SimpleStringProperty(market.getMarketDescription());
            this.resultedMarketActualLine = new SimpleStringProperty("");
            this.resultedMarketWinningSelection = new SimpleStringProperty("");
        }


        public StringProperty typeProperty() {
            return resultedMarketType;
        }

        public StringProperty subTypeProperty() {
            return resultedMarketSubType;
        }

        public StringProperty sequenceIdProperty() {
            return resultedMarketSequenceId;
        }

        public StringProperty descriptionProperty() {
            return resultedMarketDescription;
        }

        public StringProperty actualLine() {
            return resultedMarketActualLine;
        }

        public StringProperty winningSelection() {
            return resultedMarketWinningSelection;
        }

    }

}
