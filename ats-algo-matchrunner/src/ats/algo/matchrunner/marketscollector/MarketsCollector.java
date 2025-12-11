package ats.algo.matchrunner.marketscollector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ats.algo.core.markets.Market;

import ats.algo.core.markets.Markets;

public class MarketsCollector {

    Map<String, CollectedMarket> collectedMarkets;

    public MarketsCollector() {
        collectedMarkets = new HashMap<String, CollectedMarket>();
    }

    public void addMarkets(Markets markets) {
        for (Market market : markets.getMarkets().values()) {
            String key = market.getFullKey();
            CollectedMarket collectedMarket = collectedMarkets.get(key);
            if (collectedMarket == null) {
                collectedMarket = new CollectedMarket(market);
                collectedMarkets.put(key, collectedMarket);
            } else
                collectedMarket.addSelections(market);
        }
    }

    public void exportMarkets(File file) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));

            output.write(CollectedMarket.csvHeader());
            for (CollectedMarket collectedMarket : collectedMarkets.values())
                output.write(collectedMarket.toCsvString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }

    }

}
