package ats.algo.core.markets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class MarketGroupAwaitingResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Market baseMarket;
    private Map<String, Market> derivedMarkets; // full market key
    private Map<String, Market> nonDerivedMarkets; // full market key
    private Map<String, Market> allMarketsForGroup;

    MarketGroupAwaitingResult(Market market) {
        baseMarket = market;
        derivedMarkets = new HashMap<String, Market>();
        nonDerivedMarkets = new HashMap<String, Market>();
        allMarketsForGroup = new HashMap<String, Market>();
    }

    /*
     * Retrive all markets wait to be resulted within this group
     */
    Map<String, Market> getAllMarkets() {
        allMarketsForGroup.clear();
        allMarketsForGroup.put(baseMarket.getFullKey(), baseMarket);

        for (Map.Entry<String, Market> derived : derivedMarkets.entrySet())
            allMarketsForGroup.put(derived.getKey(), derived.getValue());

        for (Map.Entry<String, Market> derived : nonDerivedMarkets.entrySet()) {
            allMarketsForGroup.put(derived.getKey(), derived.getValue());
        }

        return allMarketsForGroup;
    }

    Market getBaseMarket() {
        return baseMarket;
    }

    Map<String, Market> getDerivedMarkets() {
        return derivedMarkets;
    }

    Map<String, Market> getNonDerivedMarkets() {
        return nonDerivedMarkets;
    }

    void addDerivedMarket(Market market) {
        derivedMarkets.put(market.getFullKey(), market);
    }

    void addNonDerivedMarket(Market market) {
        nonDerivedMarkets.put(market.getFullKey(), market);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baseMarket == null) ? 0 : baseMarket.hashCode());
        result = prime * result + ((derivedMarkets == null) ? 0 : derivedMarkets.hashCode());
        result = prime * result + ((nonDerivedMarkets == null) ? 0 : nonDerivedMarkets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketGroupAwaitingResult other = (MarketGroupAwaitingResult) obj;
        if (baseMarket == null) {
            if (other.baseMarket != null)
                return false;
        } else if (!baseMarket.equals(other.baseMarket))
            return false;
        if (derivedMarkets == null) {
            if (other.derivedMarkets != null)
                return false;
        } else if (!derivedMarkets.equals(other.derivedMarkets))
            return false;
        if (nonDerivedMarkets == null) {
            if (other.nonDerivedMarkets != null)
                return false;
        } else if (!nonDerivedMarkets.equals(other.nonDerivedMarkets))
            return false;
        return true;
    }
}

