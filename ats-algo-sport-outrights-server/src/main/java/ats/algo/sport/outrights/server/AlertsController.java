package ats.algo.sport.outrights.server;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.Alerts;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.core.util.json.JsonUtil;

@Controller
public class AlertsController {

    @GetMapping("/alerts")
    @ResponseBody
    public String getAlerts() {
        Alerts alerts = OutrightsServer.getAlerts();
        return JsonUtil.marshalJson(alerts);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/alert")
    @ResponseBody
    String fixturesPost(@RequestBody String alertObject) {

        PostReply postReply;
        Alert alert = JsonUtil.unmarshalJson(alertObject, Alert.class);
        if (alert == null) {
            postReply = new PostReply(false, "Update failed.  No alert supplied");
            OutrightsServer.logger.error("Update failed.  No alert supplied");
        } else {
            Competition competition = OutrightsServer.getCompetitions().get(alert.getEventId());
            if (competition == null) {
                postReply = new PostReply(false, "Update failed.  invalid event id" + alert.getEventId());
                OutrightsServer.logger.error("Update failed.  invalid event id" + alert.getEventId());
            } else {
                competition.updateAlert(alert);
            }
            postReply = new PostReply(true, "Alert updated successfully");
            OutrightsServer.logger.info("Alert updated successfully for eventID:" + alert.getEventId());
        }

        return JsonUtil.marshalJson(postReply);
    }
}
