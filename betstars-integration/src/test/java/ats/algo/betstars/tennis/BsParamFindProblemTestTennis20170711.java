package ats.algo.betstars.tennis;

import org.junit.Ignore;

import ats.algo.betstars.BetstarsSportInitialisation;
import ats.algo.betstars.BsSimpleAlgoManagerTestBase;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.core.util.json.JsonUtil;

public class BsParamFindProblemTestTennis20170711 extends BsSimpleAlgoManagerTestBase {

    // Test for ticket https://jira.amelco.co.uk/browse/RSP-10598


    String matchformatStr =
                    "{\"TennisMatchFormat\":{\"matchFormatType\":\"TennisMatchFormat\",\"sportType\":\"TENNIS\",\"doublesMatch\":false,\"sex\":\"MEN\",\"surface\":\"HARD\",\"tournamentLevel\":\"ITF\",\"setsPerMatch\":3,\"finalSetType\":\"NORMAL_WITH_TIE_BREAK\",\"noAdvantageGameFormat\":false}}";

    String matchStateStr =
                    "{\"TennisSimpleMatchState\":{\"matchStateType\":\"TennisSimpleMatchState\",\"preMatch\":false,\"matchCompleted\":false,\"setsA\":0,\"setsB\":1,\"gamesA\":2,\"gamesB\":3,\"pointsA\":1,\"pointsB\":1,\"onServeNow\":\"B\",\"scoreMap\":{\"scoreInSet2\":{\"A\":0,\"B\":0},\"scoreInSet1\":{\"A\":2,\"B\":6},\"scoreInSet3\":{\"A\":0,\"B\":0}},\"currentSetNo\":2,\"eventId\":5830265}}";

    String matchParamsStr =
                    "{\"GenericMatchParams\":{\"eventId\":5830265,\"requestId\":\"1041475288\",\"requestTime\":0,\"paramMap\":{\"eventTier\":{\"class\":\"TraderControl\",\"matchParameterType\":\"TRADER_CONTROL\",\"gaussian\":{\"mean\":4.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":1.0,\"maxAllowedParamValue\":4.0,\"displayAsPercentage\":false},\"priceSourceSelector\":{\"class\":\"TraderControl\",\"matchParameterType\":\"TRADER_CONTROL\",\"gaussian\":{\"mean\":100.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.0,\"maxAllowedParamValue\":127.0,\"displayAsPercentage\":false},\"onServePctA\":{\"class\":\"Standard\",\"matchParameterType\":\"A\",\"gaussian\":{\"mean\":0.4947722899022156,\"stdDevn\":0.1,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.45,\"maxAllowedParamValue\":0.99,\"displayAsPercentage\":true},\"onServePctB\":{\"class\":\"Standard\",\"matchParameterType\":\"B\",\"gaussian\":{\"mean\":0.66377069518165,\"stdDevn\":0.1,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.45,\"maxAllowedParamValue\":0.99,\"displayAsPercentage\":true},\"bsBoostA\":{\"class\":\"Standard\",\"matchParameterType\":\"A\",\"gaussian\":{\"mean\":0.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":-0.5,\"maxAllowedParamValue\":0.5,\"displayAsPercentage\":true},\"bsBoostB\":{\"class\":\"Standard\",\"matchParameterType\":\"B\",\"gaussian\":{\"mean\":0.0,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":-0.5,\"maxAllowedParamValue\":0.5,\"displayAsPercentage\":true},\"bsSecondServeReduction\":{\"class\":\"Standard\",\"matchParameterType\":\"BOTHCOMBINED\",\"gaussian\":{\"mean\":-0.12,\"stdDevn\":0.0,\"bias\":0.0},\"marketGroup\":\"NOT_SPECIFIED\",\"minAllowedParamValue\":0.0,\"maxAllowedParamValue\":0.25,\"displayAsPercentage\":true}},\"originatingClassName\":\"com.betstars.algo.ats.integration.BsTennisMatchParams\"}}";

    String marketPricesListStr =
                    "{\"MarketPricesList\":{\"eventId\":5830265,\"generateDetailedParamFindLog\":false,\"marketPricesList\":{\"MarathonBet\":{\"marketPrices\":{\"FT:ML_M\":{\"valid\":true,\"type\":\"FT:ML\",\"marketDescription\":\"FT:ML|null|M|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":31.0,\"B\":1.001},\"sequenceId\":\"M\",\"marketWeight\":4.0},\"FT:OU_M\":{\"valid\":false,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|16.5|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"16.5\",\"selections\":{\"Over\":1.67,\"Under\":2.18},\"sequenceId\":\"M\",\"marketWeight\":4.0},\"FT:SPRD_M\":{\"valid\":false,\"type\":\"FT:SPRD\",\"marketDescription\":\"FT:SPRD|7.5|M|HCAP\",\"category\":\"HCAP\",\"lineId\":\"7.5\",\"selections\":{\"AH\":1.67,\"BH\":2.18},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"G:ML_S2.7\":{\"valid\":true,\"type\":\"G:ML\",\"marketDescription\":\"G:ML|null|S2.7|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":2.09,\"B\":1.727},\"sequenceId\":\"S2.7\",\"marketWeight\":4.0}},\"sourceWeight\":10.0},\"PaddyPower\":{\"marketPrices\":{\"FT:ML_M\":{\"valid\":false,\"type\":\"FT:ML\",\"marketDescription\":\"FT:ML|null|M|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":67.0,\"B\":1.01},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"G:ML_S2.7\":{\"valid\":false,\"type\":\"G:ML\",\"marketDescription\":\"G:ML|null|S2.7|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":1.84,\"B\":1.84},\"sequenceId\":\"S2.7\",\"marketWeight\":1.0}},\"sourceWeight\":0.0},\"Unibet\":{\"marketPrices\":{\"FT:ML_M\":{\"valid\":true,\"type\":\"FT:ML\",\"marketDescription\":\"FT:ML|null|M|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":18.0,\"B\":1.01},\"sequenceId\":\"M\",\"marketWeight\":4.0},\"FT:OU_M\":{\"valid\":false,\"type\":\"FT:OU\",\"marketDescription\":\"FT:OU|16.5|M|OVUN\",\"category\":\"OVUN\",\"lineId\":\"16.5\",\"selections\":{\"Over\":1.6,\"Under\":2.2},\"sequenceId\":\"M\",\"marketWeight\":4.0},\"FT:SPRD_M\":{\"valid\":false,\"type\":\"FT:SPRD\",\"marketDescription\":\"FT:SPRD|6.5|M|HCAP\",\"category\":\"HCAP\",\"lineId\":\"6.5\",\"selections\":{\"AH\":2.8,\"BH\":1.39},\"sequenceId\":\"M\",\"marketWeight\":1.0},\"G:ML_S2.7\":{\"valid\":true,\"type\":\"G:ML\",\"marketDescription\":\"G:ML|null|S2.7|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":2.1,\"B\":1.66},\"sequenceId\":\"S2.7\",\"marketWeight\":4.0}},\"sourceWeight\":20.0},\"WilliamHill\":{\"marketPrices\":{\"FT:ML_M\":{\"valid\":true,\"type\":\"FT:ML\",\"marketDescription\":\"FT:ML|null|M|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":26.0,\"B\":1.005},\"sequenceId\":\"M\",\"marketWeight\":4.0},\"G:ML_S2.7\":{\"valid\":true,\"type\":\"G:ML\",\"marketDescription\":\"G:ML|null|S2.7|GENERAL\",\"category\":\"GENERAL\",\"selections\":{\"A\":2.05,\"B\":1.7},\"sequenceId\":\"S2.7\",\"marketWeight\":4.0}},\"sourceWeight\":10.0}}}}";
    long eventId = 123L;

    static {
        BetstarsSportInitialisation.initTennis();
    }

    @Ignore
    public void test() {

        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat matchFormat = JsonUtil.unmarshalJson(matchformatStr, TennisMatchFormat.class);
        JsonSerializer.print(matchFormat);

        GenericMatchParams gMatchParams = JsonUtil.unmarshalJson(matchParamsStr, GenericMatchParams.class);
        JsonSerializer.print(gMatchParams);

        MarketPricesList marketPricesList = JsonUtil.unmarshalJson(marketPricesListStr, MarketPricesList.class);
        JsonSerializer.print(marketPricesList);

        TennisSimpleMatchState matchState = JsonUtil.unmarshalJson(matchStateStr, TennisSimpleMatchState.class);
        JsonSerializer.print(matchState);

        gMatchParams.setEventId(eventId);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat);

        // Match State setup

        algoManager.handleMatchIncident(getStartingMatchIncident(TeamId.A), true);

        for (int i = 0; i < 24; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.B), true);
        for (int i = 0; i < 12; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.B), true);
        for (int i = 0; i < 8; i++)
            algoManager.handleMatchIncident(getPointMatchIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointMatchIncident(TeamId.A), true);
        algoManager.handleMatchIncident(getPointMatchIncident(TeamId.B), true);

        // Score here should be 0-1, 2-3, 15-15

        algoManager.handleSetMatchParams(gMatchParams);
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(this.publishedParamFinderResults);
    }

    public static void main(String[] args) {
        // BsParamFindProblemTestTennis20170711 Test = new BsParamFindProblemTestTennis20170711();
        // Test.test();
    }

    private TennisMatchIncident getStartingMatchIncident(TeamId teamId) {
        TennisMatchIncident incident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, teamId, 0, null);
        incident.setEventId(eventId);
        return incident;
    }

    private MatchIncident getPointMatchIncident(TeamId id) {
        TennisMatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, id);
        incident.setEventId(eventId);
        return incident;
    }

}
