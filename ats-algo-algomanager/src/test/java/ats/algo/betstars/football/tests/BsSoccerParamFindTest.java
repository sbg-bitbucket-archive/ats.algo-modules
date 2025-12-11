package ats.algo.betstars.football.tests;


import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.sport.football.FootballMatchFormat;
import ats.core.util.json.JsonUtil;

public class BsSoccerParamFindTest extends BsSoccerTestBase {
    String marketPricesListStr =
                    "{\"MarketPricesList\":{\"eventId\":5797598,\"generateDetailedParamFindLog\":false,\"marketPricesList\":{\"188bet\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.03,\"BH\":1.87},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":1.98,\"B\":3.15,\"Draw\":3.6},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.89,\"Under\":1.99},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.14,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:AXB_P1\":{\"valid\":true,\"type\":\"P:AXB\",\"marketDescription\":\"P:AXB|null|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.45,\"B\":3.8,\"Draw\":2.23},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1\",\"selections\":{\"Over\":1.73,\"Under\":2.17},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"Bet365\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":1.975,\"BH\":1.875},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.875,\"Under\":1.975},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.05,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1.25|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1.25\",\"selections\":{\"Over\":2.08,\"Under\":1.73},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"sbobet\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.03,\"BH\":1.89},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":1.94,\"B\":3.45,\"Draw\":3.6},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|2.75|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"2.75\",\"selections\":{\"Over\":1.9,\"Under\":2.0},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"P:AHCAP_P1\":{\"valid\":true,\"type\":\"P:AHCAP\",\"marketDescription\":\"P:AHCAP|-0.25|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.25\",\"selections\":{\"AH\":2.14,\"BH\":1.75},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:AXB_P1\":{\"valid\":true,\"type\":\"P:AXB\",\"marketDescription\":\"P:AXB|null|P1|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.57,\"B\":3.95,\"Draw\":2.09},\"sequenceId\":\"P1\",\"marketWeight\":1.0},\"P:OU_P1\":{\"valid\":true,\"type\":\"P:OU\",\"marketDescription\":\"P:OU|1|P1|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1\",\"selections\":{\"Over\":1.8,\"Under\":2.08},\"sequenceId\":\"P1\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"victor\":{\"marketPrices\":{\"FT:AHCAP_M\":{\"valid\":true,\"type\":\"FT:AHCAP\",\"marketDescription\":\"FT:AHCAP|-0.5|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"-0.5\",\"selections\":{\"AH\":2.02,\"BH\":1.88},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:AXB_M\":{\"valid\":true,\"type\":\"FT:AXB\",\"marketDescription\":\"FT:AXB|null|M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"null\",\"selections\":{\"A\":2.05,\"B\":3.6,\"Draw\":3.5},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"FT:OU_M\":{\"valid\":true,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|1.5|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"1.5\",\"selections\":{\"Over\":1.2,\"Under\":4.4},\"sequenceId\":\"M\",\"marketWeight\":1.0}},\"sourceWeight\":0.0}}}}";
    long eventId = 123L;


    public void test() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        this.publishedMarkets = null;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        waitForPublishedObject(publishedMatchParams);
        GenericMatchParams matchParams = (GenericMatchParams) this.publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams);
        MarketPricesList marketPricesList = JsonUtil.unmarshalJson(marketPricesListStr, MarketPricesList.class);
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(this.publishedParamFinderResults);
    }

    private void waitForPublishedObject(Object o) {
        while (o == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("tick");
        }

    }

    public static void main(String[] args) {
        BsSoccerParamFindTest Test = new BsSoccerParamFindTest();
        Test.test();
    }

}
