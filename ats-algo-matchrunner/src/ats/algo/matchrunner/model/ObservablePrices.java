package ats.algo.matchrunner.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.margining.Margining;
import ats.algo.margining.MarginingV10;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservablePrices {

    private Markets markets;

    private ObservableList<ObservablePrice> observablePrices;

    /**
     * 
     */
    public ObservablePrices() {
        observablePrices = FXCollections.observableArrayList();

    }

    public void setMarkets(Markets markets) {
        this.markets = markets;
    }

    public ObservableList<ObservablePrice> getData() {
        return observablePrices;
    }

    /**
     * adds the set of new rows to the table corresponding to the selections for this market
     * 
     * @param key
     * @param o
     */
    public void addPriceRows(String fullKey) {
        Market market = markets.getMarkets().get(fullKey);
        if (market != null) {
            boolean firstRow = true;
            for (Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                String selName = entry.getKey();
                ObservablePrice o = new ObservablePrice(firstRow, market, selName);
                observablePrices.add(o);
                firstRow = false;
            }
        }
    }

    /**
     * adds the set of new rows to the table corresponding to the selections for this market
     * 
     * @param key
     * @param o
     */
    public void addPriceRows(String fullKey, String selKey) {

        Market market = markets.getMarkets().get(fullKey);
        if (market != null) {
            for (Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                String selName = entry.getKey();
                if (selName.equals(selKey)) {
                    ObservablePrice o = new ObservablePrice(true, market, selName);
                    observablePrices.add(o);
                }
            }
        }
    }

    /**
     * adds the set of new rows to the table corresponding to the selections for this market
     * 
     * @param key
     * @param o
     */
    public void addPriceRows(String fullKey, String sequenceId, String selKey) {

        Market market = markets.get(fullKey, sequenceId);
        if (market != null) {
            for (Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                String selName = entry.getKey();
                if (selName.equals(selKey)) {
                    ObservablePrice o = new ObservablePrice(true, market, selName);
                    observablePrices.add(o);
                }
            }
        }
    }

    /**
     * remove all rows from observablePrices for this key
     * 
     * @param key
     */
    public void removePricesForKey(String fullKey) {
        Collection<ObservablePrice> c = new ArrayList<ObservablePrice>();
        for (ObservablePrice observablePrice : observablePrices) {
            if (observablePrice.getFullKey().equals(fullKey))
                c.add(observablePrice);
        }
        observablePrices.removeAll(c);
    }

    public class ObservablePrice {
        private String fullKey;
        private String marketType;
        private MarketCategory marketCategory;
        private MarketGroup marketGroup;
        private String sequenceId;

        private StringProperty priceType;
        private StringProperty priceName;
        private StringProperty priceGroup;
        private StringProperty priceLineId;
        private StringProperty priceSelection;
        private StringProperty priceValue;
        private StringProperty probValue;
        private StringProperty margin;
        private StringProperty priceSequenceId;

        public ObservablePrice(boolean firstRow, Market market, String selKey) {
            this.fullKey = market.getFullKey();
            this.marketType = market.getType();
            this.marketCategory = market.getCategory();
            this.sequenceId = market.getSequenceId();
            this.marketGroup = market.getMarketGroup();
            this.priceLineId = new SimpleStringProperty(market.getLineId());
            if (firstRow) {
                this.priceType = new SimpleStringProperty(market.getType());
                this.priceSequenceId = new SimpleStringProperty(market.getSequenceId());
                this.priceName = new SimpleStringProperty(market.getMarketDescription());
                this.priceGroup = new SimpleStringProperty(marketGroup.toString());
                this.priceLineId = new SimpleStringProperty(market.getLineId());
            } else {
                this.priceType = new SimpleStringProperty("");
                this.priceSequenceId = new SimpleStringProperty("");
                this.priceName = new SimpleStringProperty("");
                this.priceGroup = new SimpleStringProperty("");
                this.priceLineId = new SimpleStringProperty("");
            }
            Double prob = market.get(selKey);
            Double price = MarginingV10.addMargin(prob, market.size(), 0.035);
            // Double price = MarginingV12.addMargin(prob, market.size(), 0);
            String probStr = String.format("%.3f", prob);
            String priceStr = String.format("%.2f", price);
            String marginStr = String.format("%.3f", calcMargin(price, prob));
            this.priceSelection = new SimpleStringProperty(selKey);
            this.priceValue = new SimpleStringProperty(priceStr);
            this.probValue = new SimpleStringProperty(probStr);
            this.margin = new SimpleStringProperty(marginStr);
        }

        public String getMarketType() {
            return marketType;
        }

        public MarketCategory getMarketCategory() {
            return marketCategory;
        }

        public String getMarketLineId() {
            return priceLineId.get();
        }

        public String getMarketName() {
            return priceName.get();
        }

        public String getSelection() {
            return priceSelection.get();
        }

        public Double getPriceValue() {
            return Double.parseDouble(priceValue.get());
        }

        public Double getProbValue() {
            return Double.parseDouble(probValue.get());
        }

        public StringProperty priceKeyProperty() {
            return priceType;
        }

        public StringProperty priceNameProperty() {
            return priceName;
        }

        public StringProperty priceGroupProperty() {
            return priceGroup;
        }

        public StringProperty priceSelectionProperty() {
            return priceSelection;
        }

        public StringProperty priceValueProperty() {
            return priceValue;
        }

        public StringProperty priceProbProperty() {
            return probValue;
        }

        public StringProperty priceMarginProperty() {
            return margin;
        }

        public StringProperty priceSubTypeProperty() {
            return priceLineId;
        }

        public StringProperty priceSequenceIdProperty() {
            return priceSequenceId;
        }

        void setProb(double prob) {
            this.probValue.set(String.format("%.3f", prob));
            double price = getPriceValue();
            double margin = calcMargin(price, prob);
            this.margin.set(String.format("%.3f", margin));
        }

        private double calcMargin(double price, double prob) {
            return 1 / price - prob;
        }

        /**
         * called when user edits the price
         * 
         * @param newValue
         */
        public void updateValue(String newValue) {
            this.priceValue.set(newValue);
            calcInferredProbs();
        }

        public void updateSubType(String newValue) {
            this.priceLineId.set(newValue);
        }

        public String getSequenceId() {
            return sequenceId;
        }

        public MarketGroup getMarketGroup() {
            return marketGroup;
        }

        public String getFullKey() {
            return this.fullKey;
        }

    }

    public MarketPrices getMarketPrices() {
        MarketPrices marketPrices = new MarketPrices();
        marketPrices.setSourceWeight(1.0);
        MarketPrice marketPrice = null;
        String lastKey = null;
        String lastSequenceId = null;
        for (int i = 0; i < observablePrices.size(); i++) {
            ObservablePrice o = observablePrices.get(i);
            String key = o.getMarketType();
            String sequenceId = o.getSequenceId();
            if (!(key.equals(lastKey) && sequenceId.equals(lastSequenceId))) {

                marketPrice = new MarketPrice(o.getMarketType(), o.getMarketName(), o.getMarketCategory(),
                                o.getMarketLineId(), o.getSequenceId());
                marketPrices.addMarketPrice(marketPrice);
                lastKey = key;
                lastSequenceId = sequenceId;
            }
            marketPrice.put(o.getSelection(), o.getPriceValue());
        }
        return marketPrices;
    }

    /**
     * calculates the probabilities inferred by demargining the supplied prices and updates observablePrices
     */
    private void calcInferredProbs() {
        /*
         * extract the set of prices from observedMarkets
         */
        MarketPrices marketPrices = getMarketPrices();
        for (MarketPrice marketPrice : marketPrices) {
            double[] prices = new double[marketPrice.size()];
            String[] selKeys = new String[marketPrice.size()];
            int i = 0;
            for (Entry<String, Double> selEntry : marketPrice.entrySet()) {
                selKeys[i] = selEntry.getKey();
                prices[i] = selEntry.getValue();
                i++;
            }
            Margining margining = new Margining();
            margining.setMarginingAlgoToEntropy();
            double[] probs = margining.removeMargin(prices, 1.0);
            i = 0;
            /*
             * replace the prices in marketPrices with the probs
             */
            for (int j = 0; j < probs.length; j++) {
                marketPrice.put(selKeys[j], probs[j]);
            }
        }
        /*
         * update the observablePrices list
         */
        for (int k = 0; k < observablePrices.size(); k++) {
            ObservablePrice o = observablePrices.get(k);
            String mktKey2 = o.getMarketType();
            String selKey2 = o.getSelection();
            String sequenceId = o.getSequenceId();
            double prob = marketPrices.get(mktKey2, sequenceId).get(selKey2);
            o.setProb(prob);
        }

    }

}
