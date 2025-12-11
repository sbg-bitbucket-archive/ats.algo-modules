package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.TeamObject;
import ats.core.util.json.JsonUtil;

@Controller
public class StandingsController {

    @GetMapping("/standings")
    @ResponseBody
    public String getStandings(
                    @RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        return JsonUtil.marshalJson(competition.getStandings());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/standings")
    @ResponseBody
    String teamPost(@RequestBody TeamObject teamObject) {
        PostReply postReply;
        // TODO - handle the data in the post
        postReply = new PostReply(true, "standings updated successfully");

        return JsonUtil.marshalJson(postReply);
    }

}
