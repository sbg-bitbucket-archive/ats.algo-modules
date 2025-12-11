package ats.algo.core.markets;

import java.io.Serializable;

public class DerivedMarketDetails {



    @FunctionalInterface
    public interface DerivedMarketResulter extends Serializable {
        public ResultedMarket result(Market marketToResult, ResultedMarket resultedBaseMarket);
    }

    private DerivedMarketSpec derivedMarketSpec;
    private DerivedMarketResulter derivedMarketResulter;

    /**
     * Container for properties relevant to derived markets
     * 
     * @param derivedMarketSpec - the spec that generated this derived market
     * @param derivedMarketResulter = the method to use to result this derived market
     */
    public DerivedMarketDetails(DerivedMarketSpec derivedMarketSpec, DerivedMarketResulter derivedMarketResulter) {
        super();
        this.derivedMarketSpec = derivedMarketSpec;
        this.derivedMarketResulter = derivedMarketResulter;
    }



    public DerivedMarketSpec getDerivedMarketSpec() {
        return derivedMarketSpec;
    }



    public DerivedMarketResulter getDerivedMarketResulter() {
        return derivedMarketResulter;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((derivedMarketSpec == null) ? 0 : derivedMarketSpec.hashCode());
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
        DerivedMarketDetails other = (DerivedMarketDetails) obj;
        if (derivedMarketSpec == null) {
            if (other.derivedMarketSpec != null)
                return false;
        } else if (!derivedMarketSpec.equals(other.derivedMarketSpec))
            return false;
        return true;
    }



}
