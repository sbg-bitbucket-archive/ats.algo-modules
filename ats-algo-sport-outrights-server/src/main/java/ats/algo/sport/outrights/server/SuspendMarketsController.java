package ats.algo.sport.outrights.server;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.EventID;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.core.util.json.JsonUtil;

@Controller
public class SuspendMarketsController {

    @GetMapping("/issuspended")
    @ResponseBody
    public String getFixtures(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Boolean suspended = competition.isSuspendMarkets();
        return JsonUtil.marshalJson(suspended);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/suspendmarkets")
    @ResponseBody
    String suspendPost(@RequestBody String object) {
        return suspendMarkets(object, true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/unsuspendmarkets")
    @ResponseBody
    String unsuspendPost(@RequestBody String object) {
        return suspendMarkets(object, false);
    }

    private String suspendMarkets(String object, boolean shouldSuspend) {

        PostReply postReply;
        EventID eventID = JsonUtil.unmarshalJson(object, EventID.class);
        if (eventID == null) {
            postReply = new PostReply(false, "no eventID supplied");
            OutrightsServer.logger.error("no eventID supplied");
            return JsonUtil.marshalJson(postReply);
        }
        Competition competition = OutrightsServer.getCompetitions().get(eventID.getEventID());
        if (competition == null) {
            postReply = new PostReply(false, "Invalid event id");
            OutrightsServer.logger.error("invalid event id" + eventID.getEventID());
            return JsonUtil.marshalJson(postReply);

        }
        competition.setSuspendMarkets(shouldSuspend);
        postReply = new PostReply(true, "Suspend all markets set to: " + shouldSuspend);

        return JsonUtil.marshalJson(postReply);
    }

}
