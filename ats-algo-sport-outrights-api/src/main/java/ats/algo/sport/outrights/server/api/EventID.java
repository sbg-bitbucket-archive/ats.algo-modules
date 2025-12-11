package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Wrapper for EventID for API PUT methods that need to send the eventID
 * 
 * @author gicha
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventID {

    private long eventID;

    public EventID() {

    }

    public EventID(long eventID) {
        super();
        this.eventID = eventID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }


}
