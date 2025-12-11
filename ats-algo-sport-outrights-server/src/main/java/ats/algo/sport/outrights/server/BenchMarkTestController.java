package ats.algo.sport.outrights.server;

import org.springframework.web.bind.annotation.RestController;

import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Competitions;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class BenchMarkTestController {

    @RequestMapping("/benchmark")
    public String getBenchmarkData() {
        Outrights outrights = new Outrights();
        Competitions competitions = outrights.getCompetitions();
        int n = competitions.getCompetitions().size();
        double[] elapsedTimeSecs = new double[n];
        long[] eventIDs = new long[n];
        String[] names = new String[n];
        int m = 0;
        for (Competition competition : competitions.values()) {
            StopWatch s = new StopWatch();
            s.start();
            OutrightsServer.outrights.calculate(competition);
            s.stop();
            names[m] = competition.getOutrightsCompetitionID();
            eventIDs[m] = competition.getEventID();
            elapsedTimeSecs[m++] = s.getElapsedTimeSecs();
            System.out.printf("Completed in %.1f secs\n", s.getElapsedTimeSecs());
        }

        StringBuilder b = new StringBuilder();
        b.append("Competitions Benchmark Test:<BR>");
        int processors = Runtime.getRuntime().availableProcessors();
        b.append("CPU Cores : " + processors + " <BR>");
        b.append("Memory (Max) : " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB <BR>");
        b.append("Memory (total) : " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB<BR>");
        b.append("Memory (free) : " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB<BR>");
        for (int i = 0; i < eventIDs.length; i++) {
            b.append("EventID: ").append(String.valueOf(eventIDs[i]));
            b.append(", outrightsIdentifier: ").append(names[i]);
            b.append(", calcTimeSecs: ").append(String.format("%.1f secs", elapsedTimeSecs[i]));
            b.append("<BR>");
        }

        return b.toString();
    }

}
