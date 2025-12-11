package ats.algo.sport.outrights.server.scrapers;

import ats.algo.genericsupportfunctions.MethodName;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import ats.algo.sport.outrights.server.OutrightsScrapingTimer;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;

public class OutrightsTimerTest {
    @Test
    public void test() throws Exception {
        MethodName.log();
        Callable<Map<String, TeamDataUpdate>> callable = new OutrightsScrapingTimer("premier league");
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Future<Map<String, TeamDataUpdate>> future = exec.submit(callable);

        try {
            future.get();
        } catch (Exception e) {
            // System.out.println(e);
        }

    }
}
