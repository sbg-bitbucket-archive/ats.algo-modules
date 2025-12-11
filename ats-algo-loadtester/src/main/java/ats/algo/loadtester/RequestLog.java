package ats.algo.loadtester;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import ats.algo.loadtester.FootballSaturdayLoadSimulator.RequestType;

class RequestLog {

    private HashMap<Long, Map<String, RequestLogEntry>> requestLog;

    RequestLog() {
        requestLog = new HashMap<Long, Map<String, RequestLogEntry>>();
    }

    public Set<Entry<Long, Map<String, RequestLogEntry>>> entrySet() {
        return requestLog.entrySet();
    }

    void addEvent(long eventId) {
        Map<String, RequestLogEntry> logEntry = new TreeMap<String, RequestLogEntry>();
        requestLog.put(eventId, logEntry);
    }

    void addLogEntry(long eventId, RequestType requestType, String requestId) {
        RequestLogEntry requestLogEntry = new RequestLogEntry(requestType);
        requestLog.get(eventId).put(requestId, requestLogEntry);
    }

    void recordMarketsReceived(long eventId, String requestId) {
        requestLog.get(eventId).get(requestId).setMarketsRcvd(true);
    }

    void recordParamFindResultsReceived(long eventId, String requestId) {
        requestLog.get(eventId).get(requestId).setParamfindResultsRcvd(true);
    }

    public void recordParamsReceived(long eventId, String requestId) {
        requestLog.get(eventId).get(requestId).setParamsRcvd(true);
    }

    public void checkExpectedDataReceived() {
        for (Entry<Long, Map<String, RequestLogEntry>> entry : requestLog.entrySet()) {
            long eventId = entry.getKey();
            for (Entry<String, RequestLogEntry> entry2 : entry.getValue().entrySet()) {
                String requestId = entry2.getKey();
                RequestLogEntry requestLogEntry = entry2.getValue();
                switch (requestLogEntry.getRequestType()) {
                    case START_IN_PLAY:
                    case MATCH_INCIDENT:
                        if (!requestLogEntry.isMarketsRcvd()) {
                            long now = System.currentTimeMillis();
                            /*
                             * should have response within 5 seconds
                             */
                            if (now - requestLogEntry.getRequestTime() > 5000) {
                                try {
                                    throw new Exception(
                                                    String.format("no response received to request %s for event %d ",
                                                                    requestId, eventId));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case TIMER:
                        long now = System.currentTimeMillis();
                        /*
                         * should have timer response at least every 20 secs
                         */
                        if (now - requestLogEntry.getMarketsResponseTime() > 20000) {
                            try {
                                throw new Exception(String.format("no timer based response received for event %d ",
                                                eventId));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case PARAM_FIND:
                    default:
                        break;

                }
            }
        }

    }
}
