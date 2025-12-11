package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.core.util.json.JsonUtil;

@Controller
public class FcastStandingsController {

    @GetMapping("/fcaststandings")
    @ResponseBody
    public String getStandings(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        FcastStandings f = competition.getFcastStandings().sortOrder();
        return JsonUtil.marshalJson(f);
    }

    /*
     * no longer support updates to standings. All changes have to be made to fixtures.
     */
    // @RequestMapping(method = RequestMethod.POST, value = "/standings")
    // @ResponseBody
    // String teamPost(@RequestBody TeamObject teamObject) {
    // PostReply postReply;
    // // TODO - handle the data in the post
    // postReply = new PostReply(true, "standings updated successfully");
    //
    // return JsonUtil.marshalJson(postReply);
    // }

}
