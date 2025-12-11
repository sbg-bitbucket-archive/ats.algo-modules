package ats.algo.algomanager;

import java.util.Set;
import java.util.TreeSet;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;

public class CollectMarkets {
    private Set<String> allMarketKeys;

    public CollectMarkets() {
        setAllMarketKeys(new TreeSet<String>());
    }

    public void addToSet(Markets markets) {
        if (markets != null)
            for (Market market : markets) {
                getAllMarketKeys().add(market.getFullKey());
            }
    }

    public String toString() {
        String s = "All market keys (" + getAllMarketKeys().size() + "): [\n";
        for (String key : getAllMarketKeys()) {
            s += key + "\n";
        }
        s += "]";
        return s;
    }

    public Set<String> getAllMarketKeys() {
        return allMarketKeys;
    }

    public void setAllMarketKeys(Set<String> allMarketKeys) {
        this.allMarketKeys = allMarketKeys;
    }
}

