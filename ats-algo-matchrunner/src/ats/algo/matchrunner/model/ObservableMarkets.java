package ats.algo.matchrunner.model;

import java.util.Map.Entry;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.margining.MarginingV10;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Markets class to convert the data to/from the data format needed for a tree list view so it can be
 * displayed in the GUI
 * 
 * @author Geoff
 * 
 */
public class ObservableMarkets {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableMarket> observableMarkets;

    public ObservableMarkets() {
        observableMarkets = FXCollections.observableArrayList();
    }

    public void update(Markets markets) {

        observableMarkets.clear();
        for (Market market : markets) {
            if (!market.isValid()) {
                ObservableMarket om = new ObservableMarket(market, "market marked as invalid");
                observableMarkets.add(om);
            } else {
                addObservableMarket(market);
            }
        }
    }

    public void update(Markets markets, String marketFilter) {

        observableMarkets.clear();
        for (Market market : markets) {
            if (!market.isValid()) {
                ObservableMarket om = new ObservableMarket(market, "market marked as invalid");
                observableMarkets.add(om);
            } else {
                addObservableMarket(market, marketFilter);
            }
        }
    }

    private void addObservableMarket(Market market) {
        boolean firstRow = true;
        for (Entry<String, Double> selEntry : market.getSelectionsProbs().entrySet()) {
            String selectionName = selEntry.getKey();
            Double prob = selEntry.getValue();
            // if (market.getCategory() != MarketCategory.GENERAL) {
            // if (selectionName == "Over" || selectionName == "A")
            // selectionName = market.getSelectionNameOverOrA();
            // if (selectionName == "Under" || selectionName == "B")
            // selectionName = market.getSelectionNameUnderOrB();
            // }
            ObservableMarket om;
            if (firstRow)
                om = new ObservableMarket(firstRow, market, selectionName, prob);
            else
                om = new ObservableMarket(firstRow, market.getType(), market.getSequenceId(), selectionName,
                                market.getSelectionsSuspensionStatus(selectionName), market.size(), prob);
            observableMarkets.add(om);
            firstRow = false;
        }
    }

    private void addObservableMarket(Market market, String marketFilter) {
        boolean firstRow = true;
        for (Entry<String, Double> selEntry : market.getSelectionsProbs().entrySet()) {
            String selectionName = selEntry.getKey();
            Double prob = selEntry.getValue();
            /*
             * FIXME temp remove this for fix
             */
            // if (market.getCategory() != MarketCategory.GENERAL) {
            // if (selectionName == "Over" || selectionName == "A")
            // selectionName = market.getSelectionNameOverOrA();
            // if (selectionName == "Under" || selectionName == "B")
            // selectionName = market.getSelectionNameUnderOrB();
            // }
            ObservableMarket om;
            if (firstRow)
                om = new ObservableMarket(firstRow, market, selectionName, prob);
            else
                om = new ObservableMarket(firstRow, market.getType(), market.getSequenceId(), selectionName,
                                market.getSelectionsSuspensionStatus(selectionName), market.size(), prob);
            if (marketFilter.contains(",") || marketFilter.contains("*")) {
                String[] exactMarketFilter = marketFilter.split(",");
                String newMarketFilter;
                for (int i = 0; i < exactMarketFilter.length; i++) {
                    if (exactMarketFilter[i].contains("*")) {
                        newMarketFilter = exactMarketFilter[i].replace("*", "").toUpperCase();

                        if (om.getFullKey().contains(newMarketFilter) || om.getFullKey().contains(newMarketFilter)
                                        || om.getSelection().contains(newMarketFilter)
                                        || om.getSelection().contains(newMarketFilter)
                                        || market.getMarketDescription().contains(newMarketFilter)
                                        || market.getATSSubType().contains(newMarketFilter)
                                        || market.getMarketGroup().toString().contains(newMarketFilter)
                                        || market.getCategory().toString().contains(newMarketFilter)
                                        || market.getSelectionsProbs().values().toString().contains(newMarketFilter)) {
                            observableMarkets.add(om);
                        }
                    } else {
                        newMarketFilter = exactMarketFilter[i].toUpperCase();
                        if (market.getType().equals(newMarketFilter) || (om.getFullKey().equals(newMarketFilter)
                                        || om.getFullKey().equals(newMarketFilter)
                                        || om.getSelection().equals(newMarketFilter)
                                        || om.getSelection().equals(newMarketFilter)
                                        || market.getMarketDescription().equals(newMarketFilter)
                                        || market.getATSSubType().equals(newMarketFilter)
                                        || market.getMarketGroup().toString().equals(newMarketFilter)
                                        || market.getCategory().toString().equals(newMarketFilter)
                                        || market.getSelectionsProbs().values().toString().equals(newMarketFilter)))
                            observableMarkets.add(om);
                    }

                }
            } else if (om.getFullKey().contains(marketFilter) || om.getFullKey().contains(marketFilter.toUpperCase())
                            || om.getSelection().contains(marketFilter)
                            || om.getSelection().contains(marketFilter.toUpperCase())
                            || market.getMarketDescription().contains(marketFilter)
                            || market.getATSSubType().contains(marketFilter.toUpperCase())
                            || market.getMarketGroup().toString().contains(marketFilter.toUpperCase())
                            || market.getCategory().toString().contains(marketFilter.toUpperCase())
                            || market.getSelectionsProbs().values().toString().contains(marketFilter)) {
                observableMarkets.add(om);
            } else if (marketFilter.equals("SU") || marketFilter.equals("SD") || marketFilter.equals("OP")) {
                switch (marketFilter) {
                    case "SU":
                        if (market.getMarketStatus().getSuspensionStatus()
                                        .equals(SuspensionStatus.SUSPENDED_UNDISPLAY)) {
                            observableMarkets.add(om);
                        }
                        break;
                    case "SD":
                        if (market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.SUSPENDED_DISPLAY)) {
                            observableMarkets.add(om);
                        }
                        break;
                    case "OP":
                        if (market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.OPEN)) {
                            observableMarkets.add(om);
                        }
                        break;
                    default:
                        observableMarkets.add(om);
                        break;
                }
            }
            firstRow = false;
        }
    }

    public ObservableList<ObservableMarket> getData() {
        return observableMarkets;
    }

    public class ObservableMarket {
        /*
         * key and sequenceId are not displayed but are set for all rows
         */
        private String fullKey;
        private String shortKey;
        public boolean firstRow;
        /*
         * Stuff to display
         */
        private StringProperty marketType;
        private StringProperty marketCategory;
        private StringProperty marketGroup;
        private StringProperty marketName;
        private StringProperty marketLineId;
        private StringProperty marketSelection;
        private StringProperty marketProb;
        private StringProperty marketPrice;
        private StringProperty marketMargin;
        private StringProperty marketState;
        private StringProperty selectionState;
        private StringProperty marketSequenceId;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableMarket(Market market, String msg) {
            this.fullKey = market.getFullKey();
            this.shortKey = market.getShortKey();
            setMarketDetails(market);
            this.marketSelection = new SimpleStringProperty(msg);
            this.marketMargin = new SimpleStringProperty("");
            this.marketPrice = new SimpleStringProperty("");
            this.marketProb = new SimpleStringProperty("");
            this.marketState = new SimpleStringProperty("");
            this.selectionState = new SimpleStringProperty("");
            this.marketSequenceId = new SimpleStringProperty("");
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
        public ObservableMarket(Boolean firstRow, String fullKey, String shortKey, String selectionName,
                        SuspensionStatus suspensionStatus, int nSelections, double prob) {
            this.firstRow = firstRow;
            this.fullKey = fullKey;
            this.shortKey = shortKey;
            this.marketType = new SimpleStringProperty("");
            this.marketCategory = new SimpleStringProperty("");
            this.marketLineId = new SimpleStringProperty("");
            this.marketName = new SimpleStringProperty("");
            String statusStr = "";
            switch (suspensionStatus) {
                case OPEN:
                    statusStr = " OP";
                    break;
                case SUSPENDED_DISPLAY:
                    statusStr = " SD";
                    break;
                case SUSPENDED_UNDISPLAY:
                    statusStr = " SU";
                    break;
                case ACTIVE_UNDISPLAY:
                    statusStr = " AU";
                    break;
                default:
                    statusStr = "??";
                    break;
            }
            this.selectionState = new SimpleStringProperty(statusStr);
            this.marketSequenceId = new SimpleStringProperty("");
            this.marketSelection = new SimpleStringProperty(selectionName);
            double price = MarginingV10.addMargin(prob, nSelections, 0.035);
            // double price = MarginingV12.addMargin(prob, nSelections, 0);
            this.marketPrice = new SimpleStringProperty(String.format("%.3f", price));
            double margin = calcMargin(price, prob);
            this.marketMargin = new SimpleStringProperty(String.format("%.3f", margin));
            String probStr = String.format("%.3f", prob);
            this.marketProb = new SimpleStringProperty(probStr);
        }

        private double calcMargin(double price, double prob) {
            return 1 / price - prob;
        }

        /**
         * constructor for first row of a market
         * 
         * @param market
         * @param selectionName
         * @param prob
         */
        public ObservableMarket(Boolean firstRow, Market market, String selectionName, Double prob) {
            this.firstRow = firstRow;
            this.fullKey = market.getFullKey();
            this.shortKey = market.getShortKey();
            int nSelections = market.size();
            setMarketDetails(market, selectionName);
            this.marketSelection = new SimpleStringProperty(selectionName);
            String probStr = String.format("%.3f", prob);
            this.marketProb = new SimpleStringProperty(probStr);
            double price = MarginingV10.addMargin(prob, nSelections, 0.035);
            // double price = MarginingV12.addMargin(prob, nSelections, 0);
            this.marketPrice = new SimpleStringProperty(String.format("%.3f", price));
            double margin = calcMargin(price, prob);
            this.marketMargin = new SimpleStringProperty(String.format("%.3f", margin));

        }

        private void setMarketDetails(Market market) {
            this.marketType = new SimpleStringProperty(market.getType());
            MarketCategory marketCategory = market.getCategory();
            this.marketCategory = new SimpleStringProperty(marketCategory.toString());
            if (market.getLineId() != null)
                this.marketLineId = new SimpleStringProperty(market.getLineId().toString());
            else
                this.marketLineId = new SimpleStringProperty("");

            this.marketName = new SimpleStringProperty(market.getMarketDescription());
            this.marketGroup = new SimpleStringProperty(market.getMarketGroup().toString());
            this.marketSequenceId = new SimpleStringProperty(market.getSequenceId());
            MarketStatus status = market.getMarketStatus();
            String statusStr = "";
            switch (status.getSuspensionStatus()) {
                case OPEN:
                    statusStr = " OP";
                    break;
                case SUSPENDED_DISPLAY:
                    statusStr = " SD";
                    break;
                case SUSPENDED_UNDISPLAY:
                    statusStr = " SU";
                    break;
                case ACTIVE_UNDISPLAY:
                    statusStr = " AU";
                    break;
                default:
                    statusStr = "??";
                    break;
            }
            this.marketState = new SimpleStringProperty(statusStr);
            this.selectionState = new SimpleStringProperty(statusStr);
        }

        private void setMarketDetails(Market market, String selectionName) {
            this.marketType = new SimpleStringProperty(market.getType());
            MarketCategory marketCategory = market.getCategory();
            this.marketCategory = new SimpleStringProperty(marketCategory.toString());
            if (market.getLineId() != null)
                this.marketLineId = new SimpleStringProperty(market.getLineId().toString());
            else
                this.marketLineId = new SimpleStringProperty("");
            this.marketName = new SimpleStringProperty(market.getMarketDescription());
            this.marketGroup = new SimpleStringProperty(market.getMarketGroup().toString());
            this.marketSequenceId = new SimpleStringProperty(market.getSequenceId());
            MarketStatus marketStatus = market.getMarketStatus();
            String statusStr = "";
            switch (marketStatus.getSuspensionStatus()) {
                case OPEN:
                    statusStr = " OP";
                    break;
                case SUSPENDED_DISPLAY:
                    statusStr = " SD";
                    break;
                case SUSPENDED_UNDISPLAY:
                    statusStr = " SU";
                    break;
                case ACTIVE_UNDISPLAY:
                    statusStr = " AU";
                    break;
                default:
                    statusStr = "??";
                    break;
            }
            this.marketState = new SimpleStringProperty(statusStr);
            statusStr = "";
            switch (market.getSelectionsSuspensionStatus(selectionName)) {
                case OPEN:
                    statusStr = " OP";
                    break;
                case SUSPENDED_DISPLAY:
                    statusStr = " SD";
                    break;
                case SUSPENDED_UNDISPLAY:
                    statusStr = " SU";
                    break;
                case ACTIVE_UNDISPLAY:
                    statusStr = " AU";
                    break;
                default:
                    statusStr = "??";
                    break;
            }
            this.selectionState = new SimpleStringProperty(statusStr);
        }

        public StringProperty marketKeyProperty() {
            return marketType;
        }

        public StringProperty marketTypeProperty() {
            return marketCategory;
        }

        public StringProperty marketGroupProperty() {
            return marketGroup;
        }

        public StringProperty marketNameProperty() {
            return marketName;
        }

        public StringProperty marketSubTypeProperty() {
            return marketLineId;
        }

        public StringProperty marketSelectionProperty() {
            return marketSelection;
        }

        public StringProperty marketPriceProperty() {
            return marketPrice;
        }

        public StringProperty marketMarginProperty() {
            return marketMargin;
        }

        public StringProperty marketProbProperty() {
            return marketProb;
        }

        public String getSelection() {
            return marketSelection.get();
        }

        public String getFullKey() {
            return fullKey;
        }

        public String getShortKey() {
            return shortKey;
        }

        public StringProperty marketStateProperty() {
            if (marketState == null)
                marketState = null;
            return marketState;
        }

        public StringProperty selectionStateProperty() {
            if (selectionState == null)
                selectionState = null;
            return selectionState;
        }

        public StringProperty marketSequenceIdProperty() {
            return marketSequenceId;
        }
    }

}
