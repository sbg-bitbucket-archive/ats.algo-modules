package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.markets.ResultedMarket;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultedMarketListEntry {

    private String id;
    private String name;
    private ResultedMarket resultedMarket;

    public ResultedMarketListEntry() {}

    public ResultedMarketListEntry(ResultedMarket resultedMarket) {
        super();
        this.id = resultedMarket.getFullKey();
        this.name = resultedMarket.getMarketDescription();
        this.resultedMarket = resultedMarket;
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

    public ResultedMarket getResultedMarket() {
        return resultedMarket;
    }

    public void setResultedMarket(ResultedMarket resultedMarket) {
        this.resultedMarket = resultedMarket;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((resultedMarket == null) ? 0 : resultedMarket.hashCode());
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
        ResultedMarketListEntry other = (ResultedMarketListEntry) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (resultedMarket == null) {
            if (other.resultedMarket != null)
                return false;
        } else if (!resultedMarket.equals(other.resultedMarket))
            return false;
        return true;
    }

}
