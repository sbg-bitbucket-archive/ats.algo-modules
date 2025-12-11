package ats.algo.benchmarks;

class ResponseStat {
    int nIterations;
    String uniqueRequestId;
    long requestTime;
    long responseTime;
    long requestReceivedByCalcEngineTime;
    long responseIssuedByCalcEngineTime;

    public ResponseStat(int nIterations, String requestId, long requestTime, long responseTime,
                    long requestReceivedByCalcEngineTime, long responseIssuedByCalcEngineTime) {
        super();
        this.nIterations = nIterations;
        this.uniqueRequestId = requestId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.requestReceivedByCalcEngineTime = requestReceivedByCalcEngineTime;
        this.responseIssuedByCalcEngineTime = responseIssuedByCalcEngineTime;
    }

    public long latency() {
        return responseTime - requestTime;
    }

    public long calcEngineTime() {
        return responseIssuedByCalcEngineTime - requestReceivedByCalcEngineTime;
    }

    public long inFlightSendTimeMs() {
        if (requestReceivedByCalcEngineTime == 0)
            return 0;
        else
            return requestReceivedByCalcEngineTime - requestTime;
    }

    public long inFlightRcvTimeMs() {
        if (responseIssuedByCalcEngineTime == 0)
            return 0;
        else
            return responseTime - responseIssuedByCalcEngineTime;
    }

    public long inFlightTime() {
        return latency() - calcEngineTime();
    }

}
