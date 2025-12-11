package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;

public class CompetitionAlertsTest {

    @Test
    public void test() {
        MethodName.log();
        CompetitionAlerts alerts = new CompetitionAlerts();
        alerts.add(new Alert(AlertType.ERROR, 11111L, "test alert 1"));
        alerts.add(new Alert(AlertType.ERROR, 11111L, "test alert 2"));
        List<Alert> list = alerts.getList();
        Alert alert = list.get(1);
        // System.out.println(alert);
        assertEquals("test alert 2", alert.getDescription());
        int alertNum = alert.getId();
        alerts.updateAlert(alertNum, true);
        // System.out.println(alert);
        assertEquals(true, alert.isAcknowledged());
    }

}
