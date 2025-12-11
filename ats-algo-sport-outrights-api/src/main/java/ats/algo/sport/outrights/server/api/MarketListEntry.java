package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.markets.Market;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketListEntry {

    private String id;
    private String name;
    private Market market;

    public MarketListEntry() {}

    public MarketListEntry(Market market) {
        super();
        this.id = market.getFullKey();
        this.name = market.getMarketDescription();
        this.market = market;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((market == null) ? 0 : market.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        MarketListEntry other = (MarketListEntry) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (market == null) {
            if (other.market != null)
                return false;
        } else if (!market.equals(other.market))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


}
