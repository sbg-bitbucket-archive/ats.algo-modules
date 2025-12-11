package ats.algo.algomanager;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmartParamFindTest extends AlgoManagerSimpleTestBase {

    static long eventId = 12345L;

    @Test
    public void test2() {
        MatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices2());
        assertTrue(result.isParamFindScheduled());
        System.out.println(this.publishedResultedMarkets);
        System.out.println(this.publishedResultedMarkets);
        System.out.println(this.publishedMarkets);
        System.out.println(publishedMarkets);
        System.out.println(this.publishedMarkets);

    }

    @Test
    public void test1() {
        algoManager.setTradingRules(SupportedSportType.TENNIS, testTradingRules());
        MatchFormat matchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat);
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(2.26, 1.59, "2.5"));
        assertTrue(result.isParamFindScheduled());
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(2.26, 1.59, "2.5"));
        assertFalse(result.isParamFindScheduled());
        /*
         * move the prices a little bit - should not be enough to trigger a pf
         */
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(2.3, 1.5, "2.5"));
        assertFalse(result.isParamFindScheduled());
        /*
         * move the prices a lot =- shld trigger pf. Need to wait
         */
        int sleepSecs = (int) (TriggerParamFindTradingRule.MIN_TIME_BETWEEN_PARAM_FINDS / 1000) + 1;
        Sleep.sleep(sleepSecs);
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(1.59, 2.26, "-2.5"));
        assertTrue(result.isParamFindScheduled());
        /*
         * test set bias. Change timeout to 3 secs so we don't have to wait too long
         */
        rule.setTimeBetweenParamFinds(3000);
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        matchParams.getOnServePctA1().setBias(0.1);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(1.59, 2.26, "-2.5"));
        assertFalse(result.isParamFindScheduled());
        /*
         * feed in different prices. Because bias non zero should not trigger param find
         */
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1(3.5, 1.2, "3.5"));
        assertFalse(result.isParamFindScheduled());
        /*
         * wait for the timer to trigger a param find
         */
        assertFalse(algoManager.paramFindInProgress(eventId));
        sleep(sleepSecs);
        assertTrue(algoManager.paramFindInProgress(eventId));
    }

    private void sleep(int t) {
        try {
            for (int i = 0; i < t; i++) {
                Thread.sleep(1000);
                System.out.printf("Waiting %d of %d\n", i, t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    TriggerParamFindTradingRule rule;

    private TradingRules testTradingRules() {
        TradingRules rules = new TradingRules();
        rule = TriggerParamFindTradingRule.generatePriceDistanceVariant("Test rule", null);
        rules.addRule(rule);
        return rules;
    }

    private MarketPricesList getTestMarketPrices1(double p1, double p2, String hcap) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", p1);
        b1.put("B", p2);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "22.5");
        b2.put("Over", 1.82);
        b2.put("Under", 1.92);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, hcap);
        b3.put("AH", 1.79);
        b3.put("BH", 1.96);
        bet365.addMarketPrice(b3);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPrices2() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 2.151);
        b1.put("B", 2.920);
        b1.put("Draw", 3.407);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.85);
        b2.put("Under", 1.99);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

}
