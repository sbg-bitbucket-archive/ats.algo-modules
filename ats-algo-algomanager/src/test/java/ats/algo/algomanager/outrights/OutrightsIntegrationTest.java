package ats.algo.algomanager.outrights;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.OutrightsMatchFormat;
import ats.algo.sport.outrights.OutrightsMatchParams;

public class OutrightsIntegrationTest extends AlgoManagerSimpleTestBase {

    /**
     * This test requires the following:
     * 
     * - MQ running on the local machine at default address of 127.0.0.1
     * 
     * - an instance of OutrightsServer running and connected to the MQ server
     * 
     * EVENT_ID must match what the server is expecting for a valid outrights competition
     * 
     */
    public OutrightsIntegrationTest() {
        System.setProperty("algo.outrights.externalModel", "true");
        System.setProperty("algo.outrights.clientResultingOnly", "true");
    }

    private static final long EVENT_ID = 111111L;

    public static void main(String[] args) {
        OutrightsIntegrationTest o = new OutrightsIntegrationTest();
        o.beforeEachTest();
        o.outrightsIntegrationTest();
        o.afterEachTest();
    }

    private void outrightsIntegrationTest() {
        MatchFormat outrightsMatchFormat = new OutrightsMatchFormat(CompetitionType.PREMIER_LEAGUE);
        algoManager.handleNewEventCreation(SupportedSportType.OUTRIGHTS, EVENT_ID, outrightsMatchFormat);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 999111L, new FootballMatchFormat());
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 999112L, new FootballMatchFormat());
        /*
         * change a football match param so not the same for both events and so markets get published
         */
        FootballMatchParams footballMatchParams = new FootballMatchParams();
        footballMatchParams.setGoalSupremacy(.5, 0);
        footballMatchParams.setEventId(999111L);
        algoManager.handleSetMatchParams(footballMatchParams.generateGenericMatchParams());
        footballMatchParams.setGoalSupremacy(.2, 0);
        footballMatchParams.setEventId(999112L);
        algoManager.handleSetMatchParams(footballMatchParams.generateGenericMatchParams());
        /*
         * change an outrights match param to get the markets published
         */
        OutrightsMatchParams outrightsMatchParams = new OutrightsMatchParams();
        outrightsMatchParams.getDummyParam().setGaussian(new Gaussian(0.7, 0));
        outrightsMatchParams.setEventId(123456L);
        algoManager.handleSetMatchParams(outrightsMatchParams.generateGenericMatchParams());
        Sleep.sleep(10);
        System.out.println(publishedMarkets);
        Sleep.sleep(110);
        System.clearProperty("algo.outrights.externalModel");

    }
}
