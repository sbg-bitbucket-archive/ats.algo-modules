package ats.algo.sport.football.tradingrules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.tradingrules.PriceSourceWeights;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;

public class FootballTriggerParamFindTradingRule extends TriggerParamFindTradingRule {

    private static final long serialVersionUID = 1L;
    private Map<String, String> marketTypesRequiredAtPeriod = new HashMap<String, String>();
    private Map<String, String> marketGroupsRequiredAtPeriod = new HashMap<String, String>();

    /**
     * constructor for use by subclasses
     * 
     * @param ruleName
     */
    protected FootballTriggerParamFindTradingRule(String ruleName) {
        super(ruleName, null);
    }

    public FootballTriggerParamFindTradingRule() {
        super("Football TriggerParamFindingTradingRule", null);
    }

    public void addMarketTypesRequiredAtPeriod(String period, String marketTypeCode) {
        if (marketTypesRequiredAtPeriod.get(period) != null) {
            String list = marketTypesRequiredAtPeriod.get(period);
            marketTypesRequiredAtPeriod.put(period, list + marketTypeCode + ",");
        } else {
            marketTypesRequiredAtPeriod.put(period, marketTypeCode + ",");
        }
    }


    public void addMarketGroupsRequiredAtPeriod(String period, String marketTypeCode) {
        if (marketGroupsRequiredAtPeriod.get(period) != null) {
            if (marketTypeCode == null)
                marketGroupsRequiredAtPeriod.put(period, null);
            else {
                String list = marketGroupsRequiredAtPeriod.get(period);
                marketGroupsRequiredAtPeriod.put(period, list + marketTypeCode + ",");
            }
        } else {
            marketGroupsRequiredAtPeriod.put(period, marketTypeCode + ",");
        }
    }

    @Override
    protected String processPricesPreMatch(MarketPricesList marketPricesList, PriceSourceWeights priceSourceWeights,
                    EventSettings eventSettings, MatchState matchState) {
        return super.processPricesPreMatch(marketPricesList, priceSourceWeights, eventSettings, matchState);
    }

    @Override
    protected String processPricesInPlay(MarketPricesList marketPricesList, MatchState matchState,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings) {

        if (isInExtraTime((FootballMatchState) matchState)) {
            FootballMatchPeriod period = ((FootballMatchState) matchState).getMatchPeriod();
            marketPricesList.setWeights(priceSourceWeights, this.marketWeights);
            marketPricesList.filterOutZeroWeightSources();
            marketPricesList.filterOutUnwantedPrices(marketTypesToFilterOutInPlay);

            // change to Set<String>
            HashSet<String> requiredMarketTypeAtPeriod = filterMarketsForPeriod(marketTypesRequiredAtPeriod, period);
            HashSet<String> requiredMarketGroupsAtPeriod = filterMarketsForPeriod(marketGroupsRequiredAtPeriod, period);

            if (requiredMarketTypeAtPeriod == null && requiredMarketGroupsAtPeriod == null)
                return null;

            String errMsg1 = super.processAutoControlPricesInPlay2(marketPricesList, priceSourceWeights, eventSettings,
                            requiredMarketTypeAtPeriod, requiredMarketGroupsAtPeriod);
            if (errMsg1 != null) {
                return errMsg1;
            }
            return null;
        } else
            return super.processPricesInPlay(marketPricesList, matchState, priceSourceWeights, eventSettings);

    }


    private HashSet<String> filterMarketsForPeriod(Map<String, String> marketTypesRequiredAtPeriod2,
                    FootballMatchPeriod period) {
        String key = period.name().toString();
        String value = marketTypesRequiredAtPeriod2.get(key);

        if (value == null)
            return null;
        String[] markets = value.split(",");
        HashSet<String> marketListSet = new HashSet<String>(Arrays.asList(markets));
        return marketListSet;
    }

    private boolean isInExtraTime(FootballMatchState matchState) {
        return matchState.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF)
                        || matchState.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)
                        || matchState.getMatchPeriod().equals(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF);
    }

}
