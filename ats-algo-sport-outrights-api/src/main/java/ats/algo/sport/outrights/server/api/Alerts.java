package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Alerts {

    private List<Alert> alerts;

    public Alerts() {
        alerts = new ArrayList<>();
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }

    public void addAlerts(List<Alert> alerts) {
        alerts.forEach(a -> addAlert(a));
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }
}
