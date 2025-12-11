package ats.algo.paramfindproblemanalysis;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.core.util.json.JsonUtil;

public class ParamFindProblemTestTennis20170629 extends AlgoManagerSimpleTestBase {


    private String matchformatStr =
                    "{\"TennisMatchFormat\":{\"matchFormatType\":\"TennisMatchFormat\",\"sportType\":\"TENNIS\",\"doublesMatch\":false,\"sex\":\"MEN\",\"surface\":\"HARD\",\"tournamentLevel\":\"ATP\",\"setsPerMatch\":3,\"finalSetType\":\"NORMAL_WITH_TIE_BREAK\",\"noAdvantageGameFormat\":false}}";
    private String matchStateStr =
                    " {\"TennisSimpleMatchState\":{\"matchStateType\":\"TennisSimpleMatchState\",\"preMatch\":false,\"matchCompleted\":false,\"setsA\":0,\"setsB\":0,\"gamesA\":0,\"gamesB\":0,\"pointsA\":0,\"pointsB\":0,\"scoreMap\":{\"scoreInSet2\":{\"A\":0,\"B\":0},\"scoreInSet1\":{\"A\":0,\"B\":0},\"scoreInSet3\":{\"A\":0,\"B\":0}},\"currentSetNo\":1,\"eventId\":5518645}}";
    private String matchParamsStr =
                    "{\"GenericMatchParams\":{\"eventId\":5518645,\"requestId\":\"1a8e902a-4508-4934-a6fa-413baf1cc220\",\"requestTime\":1498736936003,\"paramMap\":{\"eventTier\":{\"class\":\"TraderControl\",\"matchParameterType\":\"TRADER_CONTROL\",\"gaussian\":{\"mean\":3.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":1.0,\"maxAllowedParamValue\":7.0,\"displayAsPercentage\":false},\"priceSourceSelector\":{\"class\":\"TraderControl\",\"matchParameterType\":\"TRADER_CONTROL\",\"gaussian\":{\"mean\":1.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.0,\"maxAllowedParamValue\":1.0,\"displayAsPercentage\":false},\"onServePctA\":{\"class\":\"Standard\",\"matchParameterType\":\"A\",\"gaussian\":{\"mean\":0.74,\"stdDevn\":0.0578,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.45,\"maxAllowedParamValue\":0.99,\"displayAsPercentage\":true},\"onServePctB\":{\"class\":\"Standard\",\"matchParameterType\":\"B\",\"gaussian\":{\"mean\":0.53,\"stdDevn\":0.0615,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.45,\"maxAllowedParamValue\":0.99,\"displayAsPercentage\":true}},\"originatingClassName\":\"ats.algo.sport.tennis.TennisMatchParams\"}}";
    private String marketPricesListStr =
                    "   {\"MarketPricesList\":{\"eventId\":0,\"generateDetailedParamFindLog\":false,\"marketPricesList\":{\"Bet365\":{\"marketPrices\":{\"FT:ML_M\":{\"valid\":true,\"type\":\"FT:ML\",\"marketDescription\":\"FT:ML||M|GENERAL\",\"category\":\"GENERAL\",\"lineId\":\"\",\"selections\":{\"A\":1.02,\"B\":13.0},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"G:ML_S3.8\":{\"valid\":true,\"type\":\"G:ML\",\"marketDescription\":\"G:ML|null|S3.8|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":1.286,\"B\":3.5},\"sequenceId\":\"S3.8\",\"marketWeight\":1.0}},\"sourceWeight\":1.0}}}}";



    private long eventId = 123L;


    public void test() {
        TennisMatchFormat matchFormat = JsonUtil.unmarshalJson(matchformatStr, TennisMatchFormat.class);
        GenericMatchParams gMatchParams = JsonUtil.unmarshalJson(matchParamsStr, GenericMatchParams.class);
        MarketPricesList marketPricesList = JsonUtil.unmarshalJson(marketPricesListStr, MarketPricesList.class);
        TennisSimpleMatchState matchState = JsonUtil.unmarshalJson(matchStateStr, TennisSimpleMatchState.class);
        System.out.println("\nMATCH FORMAT: \n" + JsonUtil.marshalJson(matchFormat, true));
        System.out.println("\nMATCH PARAMS: \n" + JsonUtil.marshalJson(gMatchParams, true));
        System.out.println("\nMATCH STATE: \n" + JsonUtil.marshalJson(matchState, true));
        System.out.println("\nMARKETPRICESLIST: \n" + JsonUtil.marshalJson(marketPricesList, true));
        ConsoleInput.readString("Hit return to continue", "");
        JsonSerializer.print(matchState);
        gMatchParams.setEventId(eventId);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat);
        algoManager.handleMatchIncident(TennisMatchIncident.generateTennisIncidentForMatchStarting(eventId, null),
                        true);
        algoManager.handleSetMatchParams(gMatchParams);
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(this.publishedParamFinderResults);
    }

    public static void main(String[] args) {
        ParamFindProblemTestTennis20170629 Test = new ParamFindProblemTestTennis20170629();
        Test.test();
    }

}

