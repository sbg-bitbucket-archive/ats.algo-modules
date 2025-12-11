package ats.algo.sport.snooker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;

public class SnookerStaticMarketFactory {
    private Markets staticMarkets = new Markets();
    private SnookerMatchState matchState;

    /*
     * AR-1053
     */
    SnookerStaticMarketFactory(SnookerMatchState matchState) {
        this.matchState = matchState;
        loadAllStaticMarket();
    }

    private void loadAllStaticMarket() {

        int frameNo = (matchState.getFramesA() + matchState.getFramesB() + 2);
        if (matchState.preMatch())
            frameNo = 1;
        String nextSequenceId = "F" + frameNo;

        Map<String, Double> potPlayerFirstSelections = new HashMap<>();
        potPlayerFirstSelections.put("Player A", 0.5);
        potPlayerFirstSelections.put("Player B", 0.5);
        if (matchState.getPotFirstBall() == TeamId.UNKNOWN)
            staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:PF", nextSequenceId,
                            "Frame " + frameNo + " First Player to pot", potPlayerFirstSelections)));

        Map<String, Double> potColorFirstSelections = new HashMap<>();
        potColorFirstSelections.put("YELLOW/GREEN/BROWN\"", 0.5);
        potColorFirstSelections.put("Blue/Pink", 0.3);
        potColorFirstSelections.put("Black ", 0.2);
        if (matchState.getPotFirstColor() == null)
            staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:CF", nextSequenceId,
                            "Frame " + frameNo + " First Colour ball to pot", potColorFirstSelections)));

        Map<String, Double> ovunSelections = new HashMap<>();
        ovunSelections.put("Over", 0.5);
        ovunSelections.put("Under", 0.5);
        String ovunLine = "100.5";
        staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:OU", nextSequenceId,
                        "Frame " + frameNo + " Total points", ovunSelections, ovunLine, MarketCategory.OVUN)));

        Map<String, Double> oddEvenSelections = new HashMap<>();
        oddEvenSelections.put("Odd", 0.5);
        oddEvenSelections.put("Even", 0.5);
        staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:OE", nextSequenceId,
                        "Frame " + frameNo + " Points ODD/EVEN", oddEvenSelections)));

        Map<String, Double> frameHas147Selections = new HashMap<>();
        frameHas147Selections.put("Yes", 0.02);
        frameHas147Selections.put("No", 0.98);
        staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:HAS147", nextSequenceId,
                        "Frame " + frameNo + " has 147", frameHas147Selections)));

        Map<String, Double> winningMarginSelections = new HashMap<>();
        winningMarginSelections.put("0-49", 0.35);
        winningMarginSelections.put("50-99", 0.45);
        winningMarginSelections.put("100-119", 0.157);
        winningMarginSelections.put("120-146", 0.132);
        winningMarginSelections.put("147+", 0.07);
        staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(new MarketInfo("P:WM", nextSequenceId,
                        "Frame " + frameNo + " winning margin", winningMarginSelections)));


        Map<String, Double> matchHas147Selections = new HashMap<>();
        matchHas147Selections.put("Yes", 0.04);
        matchHas147Selections.put("No", 0.96);
        if (matchState.preMatch())
            staticMarkets.addMarketWithFullKey(generateMarketFromMarketInfo(
                            new MarketInfo("FT:HAS147", "M", "Match has 147", matchHas147Selections)));

    }

    public Markets generatSnookerStaticMarkets() {
        return staticMarkets;

    }

    private Market generateMarketFromMarketInfo(MarketInfo marketInfo) {
        return marketInfo.generateMarket();
    }

    public class MarketInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String marketType;
        private String sequenceId;
        private String marketDescription;
        private Map<String, Double> selections;
        private String lineId;
        private MarketCategory marketCategory;

        public MarketInfo() {
            // required for JSON serialisation
        }

        public String getMarketType() {
            return marketType;
        }

        public void setMarketType(String marketType) {
            this.marketType = marketType;
        }

        public String getMarketDescription() {
            return marketDescription;
        }

        public void setMarketDescription(String marketDescription) {
            this.marketDescription = marketDescription;
        }

        public Map<String, Double> getSelections() {
            return selections;
        }

        public void setSelections(Map<String, Double> selections) {
            this.selections = selections;
        }

        public String getSequenceId() {
            return sequenceId;
        }

        public void setSequenceId(String sequenceId) {
            this.sequenceId = sequenceId;
        }

        public String getLineId() {
            return lineId;
        }

        public void setLineId(String lineId) {
            this.lineId = lineId;
        }

        public MarketCategory getMarketCategory() {
            return marketCategory;
        }

        public void setMarketCategory(MarketCategory marketCategory) {
            this.marketCategory = marketCategory;
        }


        public MarketInfo(String marketType, String sequenceId, String marketDescription,
                        Map<String, Double> selections, String lineId, MarketCategory marketCategory) {
            super();
            this.marketType = marketType;
            this.sequenceId = sequenceId;
            this.marketDescription = marketDescription;
            this.selections = selections;
            this.lineId = lineId;
            this.marketCategory = marketCategory;
        }

        public MarketInfo(String marketType, String sequenceId, String marketDescription,
                        Map<String, Double> selections) {
            this.marketDescription = marketDescription;
            this.marketType = marketType;
            this.sequenceId = sequenceId;
            this.selections = new HashMap<String, Double>(selections);
            this.marketCategory = MarketCategory.GENERAL;
            this.lineId = "";
        }

        public Market generateMarket() {
            Market market = new Market(marketCategory, MarketGroup.NOT_SPECIFIED, marketType, sequenceId,
                            marketDescription);
            selections.forEach((x, y) -> market.put(x, y));
            market.setValid(true);
            market.setLineId(lineId);
            return market;
        }

    }


}
