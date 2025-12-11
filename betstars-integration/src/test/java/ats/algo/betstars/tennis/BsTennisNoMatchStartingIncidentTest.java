package ats.algo.betstars.tennis;

import org.junit.Test;

import com.betstars.algo.ats.integration.BsTennisMatchParams;

import ats.algo.betstars.BsSimpleAlgoManagerTestBase;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class BsTennisNoMatchStartingIncidentTest extends BsSimpleAlgoManagerTestBase {

    @Test
    public void testNoMatchStartingIncident() {
        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 1234L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);
        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, null);
        matchIncident.setEventId(eventId);
        matchIncident.setPointWinner(TeamId.A);
        matchIncident.setServerSideAtStartOfMatch(TeamId.B);
        matchIncident.setServerPlayerAtStartOfMatch(0);
        String json = JsonUtil.marshalJson(matchIncident, true);

        algoManager.handleMatchIncident(matchIncident, true);
        System.out.println(json);
        System.out.print(publishedMatchState);
        /*
         * will see a NPE in the log
         */


    }


}
