package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.EventID;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.core.util.json.JsonUtil;

@Controller
public class ParamFindController {

    @RequestMapping(method = RequestMethod.POST, value = "/paramfind")
    @ResponseBody
    String paramFindPost(@RequestBody String object) {

        PostReply postReply;
        EventID eventID = JsonUtil.unmarshalJson(object, EventID.class);
        if (eventID == null) {
            postReply = new PostReply(false, "no eventID supplied");
            OutrightsServer.logger.error("no eventID supplied");
            return JsonUtil.marshalJson(postReply);
        }
        Competition competition = OutrightsServer.getCompetitions().get(eventID.getEventID());
        if (competition == null) {
            postReply = new PostReply(false, "invalid event id");
            OutrightsServer.logger.error("invalid event id" + eventID.getEventID());
            return JsonUtil.marshalJson(postReply);
        }
        Alert alert = OutrightsServer.outrights.paramFind(competition);
        competition.addAlert(alert);
        postReply = new PostReply(alert);
        return JsonUtil.marshalJson(postReply);
    }

}
