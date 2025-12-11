package ats.algo.core.baseclasses;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.FootballShootoutInfo;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.core.util.json.JsonUtil;

public class LoggingUsingJsonTest {

    /*
     * disable these tests - not generally needed and not testing any AlgoFramework stuff
     */
    // @Test
    public void testToStringvsJson() {
        int nIterations = 100000;
        FootballMatchState matchState = new FootballMatchState();
        String json = JsonUtil.marshalJson(matchState);
        System.out.println(json);;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < nIterations; i++) {
            @SuppressWarnings("unused")
            String s = matchState.toString();
        }
        long t2 = System.currentTimeMillis();

        for (int i = 0; i < nIterations; i++) {
            @SuppressWarnings("unused")
            String s = JsonUtil.marshalJson(matchState);
        }
        long t3 = System.currentTimeMillis();
        System.out.printf("toString: %d, json: %d", t2 - t1, t3 - t2);
        assertTrue(t3 - t2 < t2 - t1);
    }

    // @Test
    public void test2() {
        TeamSheet teamsheet = TeamSheet.generateDefaultTeamSheet();
        String json = JsonUtil.marshalJson(teamsheet, true);
        // System.out.println(json);

        TestClass test = new TestClass(TeamSheet.generateDefaultTeamSheet());
        json = JsonUtil.marshalJson(test, true);
        // System.out.println(json);
        FootballShootoutInfo shootoutInfo = new FootballShootoutInfo(0, 0, TeamId.UNKNOWN, 0, false);

        FootballSimpleMatchState sms = new FootballSimpleMatchState(true, false, false, null, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, null, teamsheet, shootoutInfo, false);
        json = JsonUtil.marshalJson(sms, true);
        System.out.println(json);

        MatchState matchState = new FootballMatchState().generateSimpleMatchState();
        json = JsonUtil.marshalJson(matchState, true);
        // System.out.println(json);
    }



    class TestClass {
        private TeamSheet teamSheet;

        public TestClass(@JsonProperty("teamSheet") TeamSheet teamSheet) {
            super();
            this.teamSheet = teamSheet;
        }

        public TeamSheet getTeamSheet() {
            return teamSheet;
        }



    }
}
