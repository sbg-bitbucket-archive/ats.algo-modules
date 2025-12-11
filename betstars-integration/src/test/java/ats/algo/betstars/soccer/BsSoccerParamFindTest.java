package ats.algo.betstars.soccer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.football.FootballMatchFormat;
import ats.core.util.json.JsonUtil;

public class BsSoccerParamFindTest extends BsSimpleAlgoManagerSoccerTestBase {
    String marketPricesListStr =
                    "{\"MarketPricesList\":{\"eventId\":5797598,\"generateDetailedParamFindLog\":false,\"marketPricesList\":{\"188bet\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.03,\"BH\":1.87},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":1.98,\"B\":3.15,\"Draw\":3.6},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.89,\"Under\":1.99},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.14,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:AXB_P1\":{\"valid\":true,\"type\":\"P:AXB\",\"marketDescription\":\"P:AXB|null|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.45,\"B\":3.8,\"Draw\":2.23},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1\",\"selections\":{\"Over\":1.73,\"Under\":2.17},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"Bet365\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":1.975,\"BH\":1.875},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.875,\"Under\":1.975},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.05,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1.25|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1.25\",\"selections\":{\"Over\":2.08,\"Under\":1.73},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"sbobet\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.03,\"BH\":1.89},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":1.94,\"B\":3.45,\"Draw\":3.6},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.9,\"Under\":2.0},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.14,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:AXB_P1\":{\"valid\":true,\"type\":\"P:AXB\",\"marketDescription\":\"P:AXB|null|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.57,\"B\":3.95,\"Draw\":2.09},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1\",\"selections\":{\"Over\":1.8,\"Under\":2.08},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"victor\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.02,\"BH\":1.88},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.05,\"B\":3.6,\"Draw\":3.5},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|1.5|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1.5\",\"selections\":{\"Over\":1.2,\"Under\":4.4},\"sequenceId\":\"M\",\"marketWeight\":1.0}},\"sourceWeight\":0.0}}}}";
    long eventId = 123L;

    public void test() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        this.publishedMarkets = null;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        GenericMatchParams matchParams = (GenericMatchParams) this.publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams);
        MarketPricesList marketPricesList = JsonUtil.unmarshalJson(marketPricesListStr, MarketPricesList.class);
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(this.publishedParamFinderResults);
    }

    public void testForParamFindTradingRules() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        this.publishedMarkets = null;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        GenericMatchParams matchParams = (GenericMatchParams) this.publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams);
        algoManager.handleClearParamFindData(eventId);

        MarketPricesList marketPricesList = getTestMarketPricesForAutoControl(false, false);
        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        assertFalse(result.isParamFindScheduled());

        result = null;
        marketPricesList = null;
        algoManager.handleClearParamFindData(eventId);

        marketPricesList = getTestMarketPricesForAutoControl(true, false);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        assertFalse(result.isParamFindScheduled());

        result = null;
        marketPricesList = null;
        algoManager.handleClearParamFindData(eventId);

        marketPricesList = getTestMarketPricesForAutoControl(true, true);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        assertTrue(result.isParamFindScheduled());

        System.out.println(this.publishedParamFinderResults);
    }

    private MarketPricesList getTestMarketPricesForAutoControl(boolean useSevenSources, boolean useMatchWinner) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices IBC = new MarketPrices();
        IBC.setSourceWeight(1);
        marketPricesList.put("IBCbet", IBC);
        MarketPrice IBC1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        IBC1.put("Over", 1.60);
        IBC1.put("Under", 2.30);
        IBC.addMarketPrice(IBC1);
        MarketPrice IBC2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        IBC2.put("A", 2.30);
        IBC2.put("B", 2.87);
        IBC2.put("Draw", 3.50);
        IBC.addMarketPrice(IBC2);

        MarketPrices SBO = new MarketPrices();
        SBO.setSourceWeight(1);
        marketPricesList.put("sbobet", SBO);
        MarketPrice SBO1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        SBO1.put("Over", 1.60);
        SBO1.put("Under", 2.30);
        SBO.addMarketPrice(SBO1);
        MarketPrice SBO2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        SBO2.put("A", 2.30);
        SBO2.put("B", 2.87);
        SBO2.put("Draw", 3.50);
        SBO.addMarketPrice(SBO2);

        MarketPrices ISN = new MarketPrices();
        ISN.setSourceWeight(1);
        marketPricesList.put("BETISN", ISN);
        MarketPrice ISN1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        ISN1.put("Over", 1.60);
        ISN1.put("Under", 2.30);
        ISN.addMarketPrice(ISN1);
        MarketPrice ISN2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        ISN2.put("A", 2.30);
        ISN2.put("B", 2.87);
        ISN2.put("Draw", 3.50);
        ISN.addMarketPrice(ISN2);

        MarketPrices singbet = new MarketPrices();
        singbet.setSourceWeight(1);
        marketPricesList.put("singbet", singbet);
        MarketPrice singbet1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        singbet1.put("Over", 1.60);
        singbet1.put("Under", 2.30);
        singbet.addMarketPrice(singbet1);
        MarketPrice singbet2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        singbet2.put("A", 2.30);
        singbet2.put("B", 2.87);
        singbet2.put("Draw", 3.50);
        singbet.addMarketPrice(singbet2);

        MarketPrices PinnacleSports = new MarketPrices();
        PinnacleSports.setSourceWeight(1);
        marketPricesList.put("PinnacleSports", PinnacleSports);
        MarketPrice PinnacleSports1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        PinnacleSports1.put("Over", 1.60);
        PinnacleSports1.put("Under", 2.30);
        PinnacleSports.addMarketPrice(PinnacleSports1);
        MarketPrice PinnacleSports2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        PinnacleSports2.put("A", 2.30);
        PinnacleSports2.put("B", 2.87);
        PinnacleSports2.put("Draw", 3.50);
        PinnacleSports.addMarketPrice(PinnacleSports2);

        MarketPrices SkyBet = new MarketPrices();
        SkyBet.setSourceWeight(1);
        marketPricesList.put("Skybet", SkyBet);
        MarketPrice SkyBet1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
        SkyBet1.put("Over", 1.60);
        SkyBet1.put("Under", 2.30);
        SkyBet.addMarketPrice(SkyBet1);
        MarketPrice SkyBet2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        SkyBet2.put("A", 2.30);
        SkyBet2.put("B", 2.87);
        SkyBet2.put("Draw", 3.50);
        SkyBet.addMarketPrice(SkyBet2);

        // If true will have enough sources based on param find.
        // Also using priority 9 in the param find list as we this automatically
        // check if priority is working.
        if (useSevenSources) {

            MarketPrices BetVictor = new MarketPrices();
            BetVictor.setSourceWeight(1);
            marketPricesList.put("victor", BetVictor);
            MarketPrice BetVictor1 = new MarketPrice("FT:OU", "Totals", MarketCategory.OVUN, "2.5");
            BetVictor1.put("Over", 1.60);
            BetVictor1.put("Under", 2.30);
            BetVictor.addMarketPrice(BetVictor1);
            if (useMatchWinner) {
                MarketPrice BetVictor2 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
                BetVictor2.put("A", 2.30);
                BetVictor2.put("B", 2.87);
                BetVictor2.put("Draw", 3.50);
                BetVictor.addMarketPrice(BetVictor2);
            }
        }

        return marketPricesList;
    }

    public static void main(String[] args) {
        BsSoccerParamFindTest Test = new BsSoccerParamFindTest();
        Test.testForParamFindTradingRules();
        System.exit(0);
    }

}
