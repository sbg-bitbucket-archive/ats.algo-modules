package ats.algo.benchmarks;

import java.util.LinkedHashMap;

public class AmelcoBenchmarkTestMain {

    private static final String MQ_URL_PUBLIC_DEMO =
                    "tcp://enamdemoalgo01:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";
    private static final String MQ_URL_BUBA =
                    "tcp://ngpambialgos01:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0&jms.prefetchPolicy.all=1";
    private static final String MQ_URL_LOCAL_HOST =
                    "tcp://localhost:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";

    public static void main(String[] args) {
        System.setProperty("cluster.processNodeId", "BENCHMARK_TEST");
        LinkedHashMap<String, String> urls = new LinkedHashMap<>();
        urls.put("PUBLIC_DEMO", MQ_URL_PUBLIC_DEMO);
        urls.put("BUBA", MQ_URL_BUBA);
        urls.put("LOCAL_HOST", MQ_URL_LOCAL_HOST);

        RunBenchmark bt = new RunBenchmark();
        bt.runTest(urls, "amelco_football_test_pcr.json", null, true);
        System.clearProperty("cluster.processNodeId");
    }

}
