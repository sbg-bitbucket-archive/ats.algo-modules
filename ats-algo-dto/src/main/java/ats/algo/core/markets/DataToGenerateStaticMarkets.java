package ats.algo.core.markets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataToGenerateStaticMarkets implements Serializable {


    private static final long serialVersionUID = 1L;

    private boolean initialisationCompleted;
    private Map<String, String> map;

    public DataToGenerateStaticMarkets() {
        super();
        map = new HashMap<String, String>();
        initialisationCompleted = false;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void add(String key, String value) {
        map.put(key, value);
    }

    public boolean isInitialisationCompleted() {
        return initialisationCompleted;
    }

    public void setInitialisationCompleted(boolean initialisationCompleted) {
        this.initialisationCompleted = initialisationCompleted;
    }

    /**
     * extracts any info we need out of the first set of markets that get generated and store for later use
     * 
     * @param markets
     */
    public void initialize(Markets markets) {
        if (markets != null) {
            Market market = markets.get("FT:SPRD");
            if (market != null) {
                int balancedLineIndex = market.getBalancedLineIndex();
                map.put("FT:SPRD", String.valueOf(balancedLineIndex));
            }
        }
        initialisationCompleted = true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (initialisationCompleted ? 1231 : 1237);
        result = prime * result + ((map == null) ? 0 : map.hashCode());
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
        DataToGenerateStaticMarkets other = (DataToGenerateStaticMarkets) obj;
        if (initialisationCompleted != other.initialisationCompleted)
            return false;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        return true;
    }
}
