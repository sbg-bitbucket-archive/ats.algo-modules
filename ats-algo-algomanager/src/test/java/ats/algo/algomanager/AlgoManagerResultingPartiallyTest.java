package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;

public class AlgoManagerResultingPartiallyTest extends AlgoManagerSimpleTestBase {


    public AlgoManagerResultingPartiallyTest() {
        PublishMarketsManager.publishAllMarkets = true;
    }


    @Test
    public void testFootballResultedMarkets() {
        MethodName.log();
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.publishResultedMarketsImmediately(true);
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        // System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incidentStart =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incidentStart.setEventId(eventId);
        incidentStart.setIncidentId("R1");
        algoManager.handleMatchIncident(incidentStart, true);
        ElapsedTimeMatchIncident incidentEnd =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        incidentEnd.setIncidentId("R2");
        incidentEnd.setEventId(eventId);
        algoManager.handleMatchIncident(incidentEnd, true);
        Sleep.sleep(6);

        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:PRT_M");

        // System.out.println(publishedResultedMarkets.getResultedMarkets().get("FT:PRT_M").toString());
        assertTrue(resultedMarket != null);
        assertTrue(!resultedMarket.isFullyResulted());
        assertTrue(resultedMarket.getLosingSelections().contains("PL"));
        assertTrue(resultedMarket.getVoidedSelections().contains("PV"));

        algoManager.handleMatchIncident(incidentStart, true);
        incidentStart.setIncidentId("R3");
        Sleep.sleep(6);

        algoManager.handleMatchIncident(incidentEnd, true);
        Sleep.sleep(6);
        incidentEnd.setIncidentId("R4");
        resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:PRT_M");
        assertTrue(resultedMarket.isFullyResulted());
        // System.out.println(publishedResultedMarkets.getResultedMarkets().get("FT:PRT_M").toString());
        assertTrue(resultedMarket.getWinningSelections().contains("W"));
    }


}
