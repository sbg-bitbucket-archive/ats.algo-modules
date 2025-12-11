package ats.algo.tradingruletests;


import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.basketball.BasketballMatchFormat;

import static org.junit.Assert.*;
import org.junit.Test;

public class TriggerParamFindTradingRulesTest extends AlgoManagerSimpleTestBase {

    static long eventId = 12345L;

    @Test
    public void test() {
        MatchFormat matchFormat = new BasketballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
        publishedMatchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        MarketPricesList marketPricesList = getTestMarketPricesTest1(1.91, 1.77);
        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        assertTrue(result.isParamFindScheduled());
        /*
         * this should not result in a param find because prematch and prices are the same
         */
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        assertFalse(result.isParamFindScheduled());

        MarketPricesList marketPricesList2 = getTestMarketPricesTest1(12, 1.05);
        /*
         * this pf should proceed but only after 15 secs - prices have changed
         */
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList2);
        assertFalse(result.isParamFindScheduled());
        this.publishedParamFinderResults = null;
        int sleepSecs = (int) (TriggerParamFindTradingRule.MIN_TIME_BETWEEN_PARAM_FINDS / 1000) + 3;
        Sleep.sleep(sleepSecs);
        assertTrue(publishedParamFinderResults != null || algoManager.paramFindInProgress(eventId));

    }

    private MarketPricesList getTestMarketPricesTest1(double ftmlA, double ftmlB) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FTOT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", ftmlA);
        b1.put("B", ftmlB);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FTOT:OU", "Total point", MarketCategory.OVUN, "166");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "1.5");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        bet365.addMarketPrice(b3);
        return marketPricesList;
    }

}
