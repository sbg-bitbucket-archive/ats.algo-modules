package ats.algo.requestresponse.ppb;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class PpbTennisPointDetailsTest {

    @Test
    @Ignore
    public void test() {
        MethodName.log();
        TennisMatchState tennisMatchState = new TennisMatchState();
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        tennisMatchState.updateStateForIncident(tennisMatchIncident, false);
        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        tennisMatchState.updateStateForIncident(tennisMatchIncident, false);

        PpbTennisPointDetails ppbTennisPointDetails =
                        PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        String json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println(json);
        PpbTennisPointDetails ppbTennisPointDetails2 = JsonUtil.unmarshalJson(json, PpbTennisPointDetails.class);
        assertEquals(ppbTennisPointDetails, ppbTennisPointDetails2);
    }


    @Test
    public void testPointServerTieBreak() {
        MethodName.log();
        TennisMatchState tennisMatchState = new TennisMatchState();
        TennisMatchIncident tennisPointWonB =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        tennisMatchState.updateStateForIncident(tennisPointWonB, true);
        tennisPointWonB = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        for (int i = 0; i < 20; i++)
            tennisMatchState.updateStateForIncident(tennisPointWonB, true); // games 0-5

        TennisMatchIncident tennisMatchIncidentWonA =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        for (int i = 0; i < 20; i++)
            tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true); // games 5-5

        for (int i = 0; i < 4; i++)
            tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);
        for (int i = 0; i < 4; i++)
            tennisMatchState.updateStateForIncident(tennisPointWonB, true); // games 6-6
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());
        PpbTennisPointDetails ppbTennisPointDetails =
                        PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        @SuppressWarnings("unused")
        String json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);
        assertTrue(tennisMatchState.getOnServeNow() == TeamId.A);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 1-0
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);
        assertTrue(tennisMatchState.getOnServeNow() == TeamId.B);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 2-0
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);


        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 3-0
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);


        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 4-0
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);


        // ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        // json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // // System.out.println("tennisMatchIncidentWonA-------------- " + json);
        // // PpbTennisPointDetails ppbTennisPointDetails2 = JsonUtil.unmarshalJson(json, PpbTennisPointDetails.class);
        // // assertEquals(ppbTennisPointDetails, ppbTennisPointDetails2);
        //
        //
        //
        // DatafeedMatchIncident tennisMatchIncidentBetStop =
        // new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_STOP, 0);
        // tennisMatchState.updateStateForIncident(tennisMatchIncidentBetStop, true);
        // // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // // + " serving now " + tennisMatchState.getOnServeNow());
        //
        // ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        // json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // // System.out.println("tennisMatchIncidentBetStop-------------- " + json);
        //
        //
        // tennisMatchState.updateStateForIncident(tennisPointWonB, true);// 30-50
        // // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // // + " serving now " + tennisMatchState.getOnServeNow());
        // // System.out.println("Points NO " + tennisMatchState.getLastIncidentDetails().getPointNo());
        // ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        // json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // // System.out.println("tennisPointWonB-------------- " + json);
        //
        //
        // DatafeedMatchIncident tennisMatchIncidentBetStar =
        // new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_START, 0);
        // tennisMatchState.updateStateForIncident(tennisMatchIncidentBetStar, true);
        // // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // // + " serving now " + tennisMatchState.getOnServeNow());
        //
        // ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        // json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // // System.out.println("-------------- " + json);
        //
        // TennisMatchIncident tennisMatchIncidentFault =
        // new TennisMatchIncident(0, TennisMatchIncidentType.FAULT, TeamId.A, 1);
        // tennisMatchState.updateStateForIncident(tennisMatchIncidentFault, true);
        // // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // // + " serving now " + tennisMatchState.getOnServeNow());
        //
        // ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        // json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // // System.out.println("-------------- " + json);

    }



    @Test
    public void testPointServer() {
        MethodName.log();
        TennisMatchState tennisMatchState = new TennisMatchState();
        TennisMatchIncident tennisPointWonB =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        tennisMatchState.updateStateForIncident(tennisPointWonB, true);
        tennisPointWonB = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        tennisMatchState.updateStateForIncident(tennisPointWonB, true);// 15-0
        tennisMatchState.updateStateForIncident(tennisPointWonB, true); // 30-0
        TennisMatchIncident tennisMatchIncidentWonA =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 30-15
        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 30-30
        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 30-40
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());
        PpbTennisPointDetails ppbTennisPointDetails =
                        PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        @SuppressWarnings("unused")
        String json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);


        tennisMatchState.updateStateForIncident(tennisMatchIncidentWonA, true);// 30-50
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());
        // System.out.println("Points NO " + tennisMatchState.getLastIncidentDetails().getPointNo());
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentWonA-------------- " + json);
        // PpbTennisPointDetails ppbTennisPointDetails2 = JsonUtil.unmarshalJson(json, PpbTennisPointDetails.class);
        // assertEquals(ppbTennisPointDetails, ppbTennisPointDetails2);



        DatafeedMatchIncident tennisMatchIncidentBetStop =
                        new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_STOP, 0);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentBetStop, true);
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());

        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisMatchIncidentBetStop-------------- " + json);


        tennisMatchState.updateStateForIncident(tennisPointWonB, true);// 30-50
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());
        // System.out.println("Points NO " + tennisMatchState.getLastIncidentDetails().getPointNo());
        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("tennisPointWonB-------------- " + json);


        DatafeedMatchIncident tennisMatchIncidentBetStar =
                        new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_START, 0);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentBetStar, true);
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());

        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("-------------- " + json);

        TennisMatchIncident tennisMatchIncidentFault =
                        new TennisMatchIncident(0, TennisMatchIncidentType.FAULT, TeamId.A, 1);
        tennisMatchState.updateStateForIncident(tennisMatchIncidentFault, true);
        // System.out.println("Game A " + tennisMatchState.getGamesA() + " Game B " + tennisMatchState.getGamesB()
        // + " serving now " + tennisMatchState.getOnServeNow());

        ppbTennisPointDetails = PpbTennisPointDetails.generatePpbTennisPointDetails(tennisMatchState);
        json = JsonUtil.marshalJson(ppbTennisPointDetails, true);
        // System.out.println("-------------- " + json);

    }

}
