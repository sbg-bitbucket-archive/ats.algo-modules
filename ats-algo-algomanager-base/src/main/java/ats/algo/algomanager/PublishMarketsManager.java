package ats.algo.algomanager;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.markets.Market;

public class PublishMarketsManager implements Serializable {

    private int noParamsChangesSinceMatchCreated;

    public static boolean publishAllMarkets = false; // flag for unit testing use only


    private Set<MarketGroup> set;

    public PublishMarketsManager() {
        set = EnumSet.noneOf(MarketGroup.class);
    }


    private static final long serialVersionUID = 1L;



    /**
     * called when params get updated. Base version just increments the number of param changes. May be overridden to
     * give sport specific behaviour as needed
     * 
     * @param oldParams
     * @param newParams
     */
    public void paramsChanged(GenericMatchParams oldParams, GenericMatchParams newParams, boolean fromParamFind) {

        boolean paramsChanged = false;
        boolean differentParamMap = false;
        for (Entry<String, MatchParam> e : newParams.getParamMap().entrySet()) {
            MatchParam oldMatchParam = oldParams.getParamMap().get(e.getKey());
            if (oldMatchParam != null) {
                MatchParam newMatchParam = e.getValue();
                if (!oldMatchParam.equals(newMatchParam) || fromParamFind) {
                    paramsChanged = true;
                    set.add(oldMatchParam.getMarketGroup());
                }
            } else {
                differentParamMap = true;
            }
        }
        if (paramsChanged || fromParamFind || differentParamMap)
            noParamsChangesSinceMatchCreated++;
    }

    public boolean shouldPublishMarket(Market market) {
        if (publishAllMarkets)
            return true;
        else
            return set.contains(market.getMarketGroup()) || set.contains(MarketGroup.NOT_SPECIFIED);
    }

    public boolean shouldPublishAnyMarkets() {
        return noParamsChangesSinceMatchCreated > 0;
    }

    public PublishMarketsManager copy() {
        PublishMarketsManager cc = new PublishMarketsManager();
        cc.noParamsChangesSinceMatchCreated = this.noParamsChangesSinceMatchCreated;
        cc.set.addAll(this.set);
        return cc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + noParamsChangesSinceMatchCreated;
        result = prime * result + ((set == null) ? 0 : set.hashCode());
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
        PublishMarketsManager other = (PublishMarketsManager) obj;
        if (noParamsChangesSinceMatchCreated != other.noParamsChangesSinceMatchCreated)
            return false;
        if (set == null) {
            if (other.set != null)
                return false;
        } else if (!set.equals(other.set))
            return false;
        return true;
    }



}
