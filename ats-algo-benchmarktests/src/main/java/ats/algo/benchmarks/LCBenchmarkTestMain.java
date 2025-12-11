package ats.algo.benchmarks;

import java.util.LinkedHashMap;

public class LCBenchmarkTestMain {

    // TODO James to fix url
    private static final String MQ_URL_LC_TEST1 =
                    "tcp://iombenampsalg01:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";

    public static void main(String[] args) {
        System.setProperty("cluster.processNodeId", "BENCHMARK_TEST");

        LinkedHashMap<String, String> urls = new LinkedHashMap<>();
        urls.put("LC_TEST1", MQ_URL_LC_TEST1);

        RunBenchmark bt = new RunBenchmark();
        bt.runTest(urls, "lc_football_test_pcr.json", "lc_football_test_pfr.json", true);
        System.clearProperty("cluster.processNodeId");
    }

}
