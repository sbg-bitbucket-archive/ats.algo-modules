package ats.algo.loadtester;

import ats.algo.loadtester.FootballSaturdayLoadSimulator.RequestType;

class RequestLogEntry {
    private RequestType requestType;
    private long requestTime;
    private long marketsResponseTime;
    private boolean marketsRcvd;
    private boolean paramfindResultsRcvd;
    private boolean paramsRcvd;

    RequestLogEntry(RequestType requestType) {
        this.requestType = requestType;
        marketsRcvd = false;
        paramfindResultsRcvd = false;
        paramsRcvd = false;
        requestTime = System.currentTimeMillis();
    }



    RequestType getRequestType() {
        return requestType;
    }

    boolean isMarketsRcvd() {
        return marketsRcvd;
    }

    void setMarketsRcvd(boolean marketsRcvd) {
        this.marketsRcvd = marketsRcvd;
        this.marketsResponseTime = System.currentTimeMillis();

    }

    boolean isParamfindResultsRcvd() {
        return paramfindResultsRcvd;
    }

    void setParamfindResultsRcvd(boolean paramfindResultsRcvd) {
        this.paramfindResultsRcvd = paramfindResultsRcvd;
    }

    boolean isParamsRcvd() {
        return paramsRcvd;
    }

    void setParamsRcvd(boolean paramsRcvd) {
        this.paramsRcvd = paramsRcvd;
    }

    long getRequestTime() {
        return requestTime;
    }

    long getMarketsResponseTime() {
        return marketsResponseTime;
    }


}
