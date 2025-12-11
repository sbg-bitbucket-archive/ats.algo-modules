package ats.algo.extractdatafromlog;

import ats.algo.genericsupportfunctions.JsonProperty;

class LogLineData {
    String time;
    String type;
    String eventId;
    String requestId;
    String data;
    String moreInfo;

    LogLineData(String type, String data) {
        this.type = type;
        this.data = extractData(data);
        this.time = extractTime(data);
        this.eventId = extractEventId(data);
        this.requestId = extractRequestId(data);

        switch (type) {
            case "PriceCalcRequest":
                moreInfo = moreInfoPriceCalcRequest(data);
                break;
            case "ParamFindResponse":
                moreInfo = moreInfoParamFindResponse(data);
                break;
            default:
                moreInfo = "-";
                break;
        }
    }

    private String moreInfoPriceCalcRequest(String data) {
        String moreInfo = "";
        String s1 = JsonProperty.getStringProperty(data, "calcRequestCause");
        if (s1 != null) {
            moreInfo = s1;
            if (s1.equals("MATCH_INCIDENT")) {
                String s2 = JsonProperty.getStringProperty(data, "incidentSubType");
                if (s2 != null)
                    moreInfo = moreInfo + ": " + s2;
            }
        }
        return moreInfo;
    }



    private String moreInfoParamFindResponse(String data) {
        String moreInfo = "";
        String s1 = JsonProperty.getStringProperty(data, "paramFindResults");
        if (s1 != null)
            moreInfo = s1;
        return moreInfo;
    }



    private String extractRequestId(String data) {
        int ix1 = data.indexOf("equestId: ");
        if (ix1 >= 0) {
            int ix2 = data.indexOf(".", ix1);
            if (ix2 >= 0)
                return data.substring(ix1 + 10, ix2);
        }
        return "-";
    }

    private String extractData(String data) {
        if (data.length() > 250)
            return data.substring(0, 250);
        else
            return data;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(time);
        b.append("\t");
        b.append(type);
        b.append("\t");
        b.append(moreInfo);
        b.append("\t");
        b.append(eventId);
        b.append("\t");
        b.append(requestId);
        b.append("\t");
        b.append(data);
        b.append("\n");
        return b.toString();
    }

    private String extractEventId(String data) {
        int ix = data.indexOf("EventId: ");
        if (ix > 0)
            return "E" + data.substring(ix + 9, ix + 16);
        ix = data.indexOf("EventId:");
        if (ix > 0)
            return "E" + data.substring(ix + 8, ix + 15);
        ix = data.indexOf("eventID");
        if (ix > 0)
            return "E" + data.substring(ix + 8, ix + 15);
        return "-";
    }

    private String extractTime(String data) {
        String timeStr = data.substring(11, 23);
        return timeStr.replace(",", ".");
    }
}
