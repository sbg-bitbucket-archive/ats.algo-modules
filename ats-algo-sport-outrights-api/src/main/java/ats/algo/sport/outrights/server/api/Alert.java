package ats.algo.sport.outrights.server.api;

import java.time.LocalDateTime;

import ats.core.util.json.JsonUtil;

public class Alert {

    private static AlertId alertId;
    private AlertType alertType;
    private String dateTime;
    private long eventId;
    private boolean acknowledged;
    private String description;
    private int id;

    static {
        alertId = new AlertId();
    }

    public Alert() {
        super();
    }

    public Alert(AlertType alertType, long eventId, String description) {
        super();
        this.alertType = alertType;
        this.dateTime = LocalDateTime.now().toString();
        this.eventId = eventId;
        this.description = description;
        this.acknowledged = false;
        this.id = Alert.alertId.nextId();
    }


    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
