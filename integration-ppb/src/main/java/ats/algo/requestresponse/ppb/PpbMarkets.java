package ats.algo.requestresponse.ppb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.markets.Market;



public class PpbMarkets implements Serializable {

    private static final long serialVersionUID = 1L;

    Map<String, PpbMarket> markets;

    public PpbMarkets() {
        super();
    }

    public PpbMarkets(Map<String, Market> markets) {
        super();
        this.markets = generatePpbMarkets(markets);
    }



    public Map<String, PpbMarket> getMarkets() {
        return markets;
    }

    public void setMarkets(Map<String, PpbMarket> markets) {
        this.markets = markets;
    }

    /**
     * generates the set of PpbMarkets from the supplied set of standard algo markets
     * 
     * @param markets2
     * @return
     */
    public Map<String, PpbMarket> generatePpbMarkets(Map<String, Market> markets) {

        Map<String, PpbMarket> ppbMarkets = new HashMap<>(markets.size());
        for (Entry<String, Market> e : markets.entrySet()) {
            ppbMarkets.put(e.getKey(), PpbMarket.generatePpbMarket(e.getValue()));
        }
        return ppbMarkets;
    }


    /**
     * generates the set of standard algo markets from the PpbMarkets
     * 
     * @return
     */
    public Map<String, Market> generateAlgoMarkets() {
        Map<String, Market> algoMarkets = new HashMap<>(markets.size());
        for (Entry<String, PpbMarket> e : markets.entrySet()) {
            algoMarkets.put(e.getKey(), PpbMarket.generateAlgoMarket(e.getValue()));
        }
        return algoMarkets;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
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
        PpbMarkets other = (PpbMarkets) obj;
        if (markets == null) {
            if (other.markets != null)
                return false;
        } else if (!markets.equals(other.markets))
            return false;
        return true;
    }



}
