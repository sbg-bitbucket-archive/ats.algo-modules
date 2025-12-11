package ats.algo.core.baseclasses;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchStateFromFeed;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;
import ats.core.util.json.JsonUtil;

public class MatchIncidentTest {



    @Test
    public void test() {
        MatchIncident incident = new ElapsedTimeMatchIncident();
        MatchStateFromFeed stateFromFeed = new TennisMatchStateFromFeed(2, 1, 6, 5, 3, 0, TeamId.A);
        incident.setMatchStateFromFeed(stateFromFeed);
        WrapperForMatchIncidentTest wrapper = new WrapperForMatchIncidentTest();
        wrapper.setIncident(incident);
        String json = JsonUtil.marshalJson(wrapper, true);
        System.out.println(json);
        WrapperForMatchIncidentTest wrapper2 = JsonUtil.unmarshalJson(json, WrapperForMatchIncidentTest.class);
        assertEquals(wrapper.getIncident(), wrapper2.getIncident());
    }

    @Test
    public void test2() {
        MatchIncident incident = new ElapsedTimeMatchIncident();
        MatchStateFromFeed stateFromFeed =
                        new FootballMatchStateFromFeed(2, 1, 6, 4, 5, 3, 0, 1, FootballMatchPeriod.IN_FIRST_HALF);
        incident.setMatchStateFromFeed(stateFromFeed);
        WrapperForMatchIncidentTest wrapper = new WrapperForMatchIncidentTest();
        wrapper.setIncident(incident);
        String json = JsonUtil.marshalJson(wrapper, true);
        System.out.println(json);
        WrapperForMatchIncidentTest wrapper2 = JsonUtil.unmarshalJson(json, WrapperForMatchIncidentTest.class);
        assertEquals(wrapper.getIncident(), wrapper2.getIncident());
    }

}
