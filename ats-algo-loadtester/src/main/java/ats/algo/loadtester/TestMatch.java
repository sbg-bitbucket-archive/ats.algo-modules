package ats.algo.loadtester;

import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPricesList;

abstract class TestMatch {

    protected long eventId;
    protected int noIncidentsGenerated;
    private boolean marketsPublished;
    private boolean paramsPublished;
    private boolean fatalErrorNotified;
    private boolean matchCompleted;
    private boolean paramFindResultsPublished;
    private long cumulativeLatency;
    private long timeIncidentIssued;
    private String matchDescription;
    private String lastRequestId;
    private int nParamFindResultsPublished;

    public TestMatch(long eventId, String matchDescription) {
        matchCompleted = false;
        noIncidentsGenerated = 0;
        fatalErrorNotified = false;
        this.eventId = eventId;
        this.matchDescription = matchDescription;
        nParamFindResultsPublished = 0;
    }

    boolean isMatchCompleted() {
        return matchCompleted;
    }

    String getMatchDescription() {
        return matchDescription;
    }

    void setMatchCompleted(boolean matchCompleted) {
        this.matchCompleted = matchCompleted;
    }

    String getLastRequestId() {
        return lastRequestId;
    }

    int getNoIncidentsGenerated() {
        return noIncidentsGenerated;
    }

    void resetNoIncidentsGenerated() {
        noIncidentsGenerated = 0;
    }

    boolean isMarketsPublished() {
        return marketsPublished;
    }

    boolean isParamFindResultsPublished() {
        return paramFindResultsPublished;
    }

    void setParamFindResultsPublished(boolean paramFindResultsPublished) {
        this.paramFindResultsPublished = paramFindResultsPublished;
    }

    boolean isFatalErrorNotified() {
        return fatalErrorNotified;
    }

    void setFatalErrorNotified(boolean fatalErrorNotified) {
        this.fatalErrorNotified = fatalErrorNotified;
    }

    public int getnParamFindResultsPublished() {
        return nParamFindResultsPublished;
    }

    public void setnParamFindResultsPublished(int nParamFindResultsPublished) {
        this.nParamFindResultsPublished = nParamFindResultsPublished;
    }

    void recordPublishedMarketsReceived(String requestId) {
        this.marketsPublished = true;
        this.lastRequestId = requestId;
        cumulativeLatency += (System.currentTimeMillis() - timeIncidentIssued);
    }

    void recordPublishedParamsReceived(String requestId) {
        this.paramsPublished = true;
        this.lastRequestId = requestId;
        cumulativeLatency += (System.currentTimeMillis() - timeIncidentIssued);
    }

    void recordParamFindResultsReceived(String requestId) {
        this.paramFindResultsPublished = true;
        this.lastRequestId = requestId;
        this.nParamFindResultsPublished++;
    }

    void recordIncidentIssued() {
        this.marketsPublished = false;
        this.paramsPublished = false;
        timeIncidentIssued = System.currentTimeMillis();
    }

    String getSummaryStats() {
        double averageLatency = cumulativeLatency / noIncidentsGenerated;
        averageLatency /= 1000;
        return String.format("Incidents processed: %d, Average latency/incident: %.2f seconds", noIncidentsGenerated,
                        averageLatency);
    }

    abstract MatchIncident getNextIncident();

    abstract SupportedSportType getSupportedSport();

    abstract MatchFormat getMatchFormat();

    abstract AlgoMatchParams getNextMatchParams();

    abstract MarketPricesList getNextMarketPricesList();

    public boolean isParamsPublished() {
        return paramsPublished;
    }

    public void setParamsPublished(boolean paramsPublished) {
        this.paramsPublished = paramsPublished;
    }

    abstract TestMatch newInstance(long eventId);

}
