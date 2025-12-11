package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.competitionsdata.TestCompetitionForParamFind;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;
import ats.algo.sport.outrights.server.api.TargetPointsList;

public class ParamFindTest {

    @Test
    public void testNoBias() {
        MethodName.log();
        test(0.0);
    }

    @Test
    public void testWithBias() {
        MethodName.log();
        test(0.5);
    }

    public void test(double bias) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        Competition competition = TestCompetitionForParamFind.generate();

        TargetPointsList list = new TargetPointsList(20);
        list.add(new TargetPointsEntry("T1", 89.75));
        list.add(new TargetPointsEntry("T4", 88.25));
        list.add(new TargetPointsEntry("T5", 77.75));
        list.add(new TargetPointsEntry("T3", 74.25));
        list.add(new TargetPointsEntry("T6", 70.75));
        list.add(new TargetPointsEntry("T2", 70.75));
        list.add(new TargetPointsEntry("T14", 49.25));
        list.add(new TargetPointsEntry("T9", 48.25));
        list.add(new TargetPointsEntry("T18", 50.25));
        list.add(new TargetPointsEntry("T10", 44.75));
        list.add(new TargetPointsEntry("T12", 45.25));
        list.add(new TargetPointsEntry("T8", 49.25));
        list.add(new TargetPointsEntry("T17", 40.25));
        list.add(new TargetPointsEntry("T15", 39.25));
        list.add(new TargetPointsEntry("T11", 37.25));
        list.add(new TargetPointsEntry("T13", 43.25));
        list.add(new TargetPointsEntry("T20", 41.25));
        list.add(new TargetPointsEntry("T7", 37.25));
        list.add(new TargetPointsEntry("T16", 29.75));
        list.add(new TargetPointsEntry("T19", 28.5));
        Map<String, FcastStanding> fcastStandings = competition.getFcastStandings().getStandings();
        list.getEntries().forEach(entry -> {
            String id = entry.getTeamID();
            fcastStandings.get(id).setTargetPoints(entry.getTargetPoints());
        });
        // System.out.println(competition.getFcastStandings());
        Team team = competition.getTeams().getTeams().get("T1");
        team.setBiasAttack(bias);
        team.setBiasDefense(bias / 2);
        ParamFindEngine pfEngine = new ParamFindEngine(competition);
        StopWatch sw = new StopWatch();
        sw.start();
        pfEngine.calculate();
        sw.stop();
        // System.out.println(pfEngine.getPfResult());

        // System.out.printf("PF Time: %.2f\n", sw.getElapsedTimeSecs());
        competition.getTeams().getTeams().forEach((k, v) -> {
            // System.out.println(v);
        });
        competition.getTeams().getTeams().forEach((k, v) -> {
            // System.out.printf("%s,%.3f,%.3f\n", v.getTeamID(), v.getRatingAttack(), v.getRatingDefense());
        });
        // System.out.println(competition.getFcastStandings());
        PfMetrics metrics = pfEngine.getPfMetrics();
        assertTrue(metrics.distance < 0.1);
        team = competition.getTeams().getTeams().get("T1");
        assertEquals(3.028, team.getRatingAttack(), 0.01);
        assertEquals(bias, team.getBiasAttack(), 0.001);
        assertEquals(bias / 2, team.getBiasDefense(), 0.001);
    }

}
