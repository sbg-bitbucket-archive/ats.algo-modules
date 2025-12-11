package ats.algo.sport.outrights.server;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.TargetPointsList;
import ats.core.util.json.JsonUtil;

@Controller
public class TargetPointsController {

    @RequestMapping(method = RequestMethod.POST, value = "/targetpoints")
    @ResponseBody
    String targetPointsPost(@RequestBody String targetPointsObject) {

        PostReply postReply;
        TargetPointsList targetPointsList = JsonUtil.unmarshalJson(targetPointsObject, TargetPointsList.class);
        if (targetPointsList == null) {
            postReply = new PostReply(false, "Update failed.  No target points list supplied");
            OutrightsServer.logger.error("Update failed.  No target points list supplied");
        } else {
            Competition competition = OutrightsServer.getCompetitions().get(targetPointsList.getEventID());
            if (competition == null) {
                postReply = new PostReply(false, "Update failed.  invalid event id" + targetPointsList.getEventID());
                OutrightsServer.logger.error("Update failed.  invalid event id" + targetPointsList.getEventID());
            } else {
                targetPointsList.getEntries().forEach(targetPointsEntry -> OutrightsServer.outrights
                                .updateTargetPoints(competition, targetPointsEntry));

                postReply = new PostReply(true, "target points updated successfully");

                OutrightsServer.logger.info(
                                "target points updated successfully for eventID:" + targetPointsList.getEventID());
            }
        }
        return JsonUtil.marshalJson(postReply);
    }
}
