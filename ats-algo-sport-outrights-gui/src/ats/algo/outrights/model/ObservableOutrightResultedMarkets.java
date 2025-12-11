package ats.algo.outrights.model;

import ats.algo.core.markets.ResultedMarket;
import ats.algo.sport.outrights.server.api.ResultedMarketListEntry;
import ats.algo.sport.outrights.server.api.ResultedMarketsList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Resultedarkets class to convert the data to/from the data format needed for a table view so it can be
 * displayed in the GUI
 * 
 * @author Geoff
 * 
 */
public class ObservableOutrightResultedMarkets {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableOutrightResultedMarket> observableOutrightResultedMarkets;

    public ObservableOutrightResultedMarkets() {
        observableOutrightResultedMarkets = FXCollections.observableArrayList();
    }


    public void update(ResultedMarketsList resultedMarketsList) {

        observableOutrightResultedMarkets.clear();
        for (ResultedMarketListEntry resultedMarketListEntry : resultedMarketsList.getEntries()) {
            ObservableOutrightResultedMarket orm =
                            new ObservableOutrightResultedMarket(resultedMarketListEntry.getResultedMarket());
            observableOutrightResultedMarkets.add(orm);
        }
    }

    public ObservableList<ObservableOutrightResultedMarket> getData() {
        return observableOutrightResultedMarkets;
    }

    public class ObservableOutrightResultedMarket {

        private StringProperty resultedMarketType; // what gets displayed, which
                                                   // will be blank if not the
                                                   // first row
        private StringProperty resultedMarketSubType;
        private StringProperty resultedMarketSequenceId;
        private StringProperty resultedMarketDescription;
        private StringProperty resultedMarketActualLine;
        private StringProperty resultedMarketWinningSelection;
        private StringProperty resultedMarketLosingSelection;
        private StringProperty resultedMarketFullyResulted;

        public ObservableOutrightResultedMarket(ResultedMarket resultedMarket) {
            this.resultedMarketType = new SimpleStringProperty(resultedMarket.getType());
            this.resultedMarketSubType = new SimpleStringProperty(resultedMarket.getLineId());
            this.resultedMarketSequenceId = new SimpleStringProperty(resultedMarket.getSequenceId());
            this.resultedMarketDescription = new SimpleStringProperty(resultedMarket.getMarketDescription());
            int actualLine = resultedMarket.getActualOutcome();
            String actualLineStr;
            if (actualLine == -9999) // use -9999 to indicate null value
                actualLineStr = "";
            else
                actualLineStr = String.format("%d", resultedMarket.getActualOutcome());
            this.resultedMarketActualLine = new SimpleStringProperty(actualLineStr);

            this.resultedMarketWinningSelection =
                            new SimpleStringProperty(resultedMarket.getWinningSelections().toString());
            this.resultedMarketLosingSelection =
                            new SimpleStringProperty(resultedMarket.getLosingSelections().toString());
            this.resultedMarketFullyResulted =
                            new SimpleStringProperty(String.valueOf(resultedMarket.isFullyResulted()));
        }

        public StringProperty resultedMarketTypeProperty() {
            return resultedMarketType;
        }

        public StringProperty resultedMarketSubTypeProperty() {
            return resultedMarketSubType;
        }

        public StringProperty resultedMarketSequenceIdProperty() {
            return resultedMarketSequenceId;
        }

        public StringProperty resultedMarketDescriptionProperty() {
            return resultedMarketDescription;
        }

        public StringProperty resultedMarketActualLine() {
            return resultedMarketActualLine;
        }

        public StringProperty resultedMarketWinningSelection() {
            return resultedMarketWinningSelection;
        }

        public StringProperty resultedMarketLosingSelection() {
            return resultedMarketLosingSelection;
        }

        public StringProperty resultedMarketFullyResulted() {
            return resultedMarketFullyResulted;
        }

    }

}
