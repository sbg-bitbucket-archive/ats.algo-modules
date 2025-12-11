package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class StandingsTest {
    @Test
    public void standingsTest() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        Standings standings = competition.generateStandings();
        assertEquals("T11", standings.topTeamID());
        List<Standing> order = standings.finishOrder();
        assertEquals("T11", order.get(0).getTeamId());
        assertEquals("T15", order.get(order.size() - 1).getTeamId());
    }

}
