package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.List;

import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;

public class CompetitionAlerts {

    private List<Alert> alerts;

    public CompetitionAlerts() {
        alerts = new ArrayList<>();
    }

    public void add(Alert alert) {
        alerts.add(alert);
    }

    public List<Alert> getList() {
        return alerts;
    }

    public void updateAlert(int id, boolean acknowledged) {
        for (Alert alert : alerts) {
            if (alert.getId() == id) {
                alert.setAcknowledged(acknowledged);
                if (alert.getAlertType().equals(AlertType.ERROR)) {
                    alert.setAlertType(AlertType.ERROR_ACK);
                }
                return;
            }
        }
    }



}
