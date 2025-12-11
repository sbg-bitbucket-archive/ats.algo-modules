package ats.algo.sport.tabletennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.tabletennis.TabletennisMatchFormat;
import ats.algo.sport.tabletennis.TabletennisMatchIncident;
import ats.algo.sport.tabletennis.TabletennisMatchIncidentResult;
import ats.algo.sport.tabletennis.TabletennisMatchIncidentResult.TabletennisMatchIncidentResultType;
import ats.algo.sport.tabletennis.TabletennisMatchState;
import ats.algo.sport.tabletennis.TabletennisMatchIncident.TabletennisMatchIncidentType;

public class TabletennisMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        TabletennisMatchFormat matchFormat = new TabletennisMatchFormat();
        matchFormat.setnGamesInMatch(3);
        TabletennisMatchState matchState = new TabletennisMatchState(matchFormat);
        TabletennisMatchIncident tabletennisMatchIncident = new TabletennisMatchIncident();
        TabletennisMatchIncidentResult outcome =
                        new TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType.POINTWON);
        for (int i = 0; i < 11; i++) {
            tabletennisMatchIncident.set(TabletennisMatchIncidentType.POINTWON, TeamId.A);
            outcome = (TabletennisMatchIncidentResult) matchState.updateStateForIncident(tabletennisMatchIncident,
                            false);
        }
        for (int i = 0; i < 11; i++) {
            tabletennisMatchIncident.set(TabletennisMatchIncidentType.POINTWON, TeamId.B);
            outcome = (TabletennisMatchIncidentResult) matchState.updateStateForIncident(tabletennisMatchIncident,
                            false);
        }

        assertFalse(matchState.isMatchCompleted());
        for (int i = 0; i < 11; i++) {
            tabletennisMatchIncident.set(TabletennisMatchIncidentType.POINTWON, TeamId.B);
            outcome = (TabletennisMatchIncidentResult) matchState.updateStateForIncident(tabletennisMatchIncident,
                            false);
        }
        assertEquals(TabletennisMatchIncidentResultType.MATCHWON, outcome.getTabletennisMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());

    }
}
