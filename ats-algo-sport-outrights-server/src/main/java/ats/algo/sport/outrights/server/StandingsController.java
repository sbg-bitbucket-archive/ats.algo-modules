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
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.core.util.json.JsonUtil;

@Controller
public class StandingsController {

    @GetMapping("/standings")
    @ResponseBody
    public String getStandings(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;


        return JsonUtil.marshalJson(competition.generateStandingsList());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/standings")
    @ResponseBody
    String fixturesPost(@RequestBody String standingsObject) {

        PostReply postReply;
        StandingsList standingsList = JsonUtil.unmarshalJson(standingsObject, StandingsList.class);
        if (standingsList == null) {
            postReply = new PostReply(false, "Update failed.  No standings list supplied");
            OutrightsServer.logger.error("Update failed.  No standings list supplied");
        } else {
            Competition competition = OutrightsServer.getCompetitions().get(standingsList.getEventID());
            if (competition == null) {
                postReply = new PostReply(false, "Update failed.  invalid event id" + standingsList.getEventID());
                OutrightsServer.logger.error("Update failed.  invalid event id" + standingsList.getEventID());
            } else {
                standingsList.getStandingsList().forEach(t -> OutrightsServer.outrights.updateStanding(competition, t));
            }
            postReply = new PostReply(true, "standings updated successfully");
            OutrightsServer.logger.info("standings updated successfully for eventID:" + standingsList.getEventID());
        }

        return JsonUtil.marshalJson(postReply);
    }

}
