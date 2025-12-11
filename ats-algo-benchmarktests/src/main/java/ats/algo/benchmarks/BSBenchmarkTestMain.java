package ats.algo.benchmarks;

import java.util.LinkedHashMap;

public class BSBenchmarkTestMain {

    private static final String MQ_URL_BS_BENCHMARK =
                    "tcp://iombenampsalg01:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";
    private static final String MQ_URL_BS_QA_CORE =
                    "tcp://iomsampss01.amelco.lan:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0";

    public static void main(String[] args) {
        System.setProperty("cluster.processNodeId", "BENCHMARK_TEST");
        System.setProperty("algomgr.jms.doNotUseReplyQueuesForExternalRequests", "true");

        LinkedHashMap<String, String> urls = new LinkedHashMap<>();
        urls.put("BS_BENCHMARK", MQ_URL_BS_BENCHMARK);
        urls.put("BS_QA_CORE", MQ_URL_BS_QA_CORE);

        RunBenchmark bt = new RunBenchmark();
        bt.runTest(urls, "bs_football_test_pcr.json", "bs_football_test_pfr.json", true);
        System.clearProperty("cluster.processNodeId");
        System.clearProperty("algomgr.jms.doNotUseReplyQueuesForExternalRequests");
    }

}
