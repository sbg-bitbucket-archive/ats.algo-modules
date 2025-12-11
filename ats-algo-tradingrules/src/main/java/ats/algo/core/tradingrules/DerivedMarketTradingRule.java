package ats.algo.core.tradingrules;

import java.io.Serializable;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.Market;

public class DerivedMarketTradingRule extends AbstractTradingRule implements Serializable {

    private static final long serialVersionUID = 1L;
    private String marketType;
    private DerivedMarketSpec derivedMarketSpec;

    /**
     * for json use only
     */
    public DerivedMarketTradingRule() {

    }

    /**
     * 
     * @param eventTier the eventTier this rule applies to. If null then applies to all event tiers
     * @param marketType the ATS market type code used to generate the derived Market(s)
     * @param derivedMarketSpec the spec for the market(s) to be created
     */
    public DerivedMarketTradingRule(Integer eventTier, String marketType, DerivedMarketSpec derivedMarketSpec) {
        super(TradingRuleType.CREATE_DERIVED_MARKETS, derivedMarketSpec.getClass().getCanonicalName(), eventTier);
        this.marketType = marketType;
        this.derivedMarketSpec = derivedMarketSpec;
    }

    /**
     * gets the ATS market type
     * 
     * @return
     */
    public String getMarketType() {
        return marketType;
    }

    /**
     * sets the ATS market type
     * 
     * @param marketType
     */
    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    /**
     * gets the spec to be used for creating the derived markets
     * 
     * @return
     */
    public DerivedMarketSpec getDerivedMarketSpec() {
        return derivedMarketSpec;
    }

    /**
     * sets the spec to be used for creating the derived markets
     * 
     * @param derivedMarketSpec
     */
    public void setDerivedMarketSpec(DerivedMarketSpec derivedMarketSpec) {
        this.derivedMarketSpec = derivedMarketSpec;
    }

    @Override
    public String toString() {
        return "DerivedMarketTradingRule [marketType=" + marketType + ", derivedMarketSpec=" + derivedMarketSpec + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((derivedMarketSpec == null) ? 0 : derivedMarketSpec.hashCode());
        result = prime * result + ((marketType == null) ? 0 : marketType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DerivedMarketTradingRule other = (DerivedMarketTradingRule) obj;
        if (derivedMarketSpec == null) {
            if (other.derivedMarketSpec != null)
                return false;
        } else if (!derivedMarketSpec.equals(other.derivedMarketSpec))
            return false;
        if (marketType == null) {
            if (other.marketType != null)
                return false;
        } else if (!marketType.equals(other.marketType))
            return false;
        return true;
    }

    public void applyDerivedMarketRule(MatchState matchState, Market market) {
        if (market.getType().equals(marketType)) {
            boolean applyRule = false;
            DerivedMarketSpecApplicability applicability = derivedMarketSpec.getApplicability();
            if (matchState.preMatch())
                applyRule = (applicability == DerivedMarketSpecApplicability.PRE_MATCH_ONLY)
                                || (applicability == DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY);
            else
                applyRule = (applicability == DerivedMarketSpecApplicability.IN_PLAY_ONLY)
                                || (applicability == DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY);
            if (applyRule)
                market.getDerivedMarketsInfo().getDerivedMarketSpecs().add(derivedMarketSpec);

        }
    }
}
