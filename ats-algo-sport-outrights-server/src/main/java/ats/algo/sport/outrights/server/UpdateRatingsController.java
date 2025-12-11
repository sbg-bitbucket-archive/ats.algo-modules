package ats.algo.sport.outrights.server;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;
import ats.algo.sport.outrights.server.api.EventID;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.algo.sport.outrights.server.scrapers.FiveThirtyEightScraper;
import ats.core.util.json.JsonUtil;

@Controller
public class UpdateRatingsController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRatingsController.class);

    // @GetMapping("/updateratings")
    // @ResponseBody
    // public String getUpdate(
    // @RequestParam(name = "eventID", required = false, defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String
    // eventIdStr) {
    // long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
    // Competition competition = OutrightsServer.getFromEventId(eventID);
    // if (competition == null)
    // return "<BR>Invalid event id<BR>";
    // if (getLatestRatings(competition))
    // return "<BR>Ratings successfully updated<BR>";
    // else
    // return "<BR>Ratings update failed<BR>";
    // }



    @RequestMapping(method = RequestMethod.POST, value = "/updateratings")
    @ResponseBody
    String updateRatingsPost(@RequestBody String object) {

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
        Alert alert = updateLatestRatings(competition);
        competition.addAlert(alert);
        postReply = new PostReply(alert);

        return JsonUtil.marshalJson(postReply);
    }

    private Alert updateLatestRatings(Competition competition) {
        Map<String, TeamDataUpdate> teamData;
        try {
            teamData = FiveThirtyEightScraper.getTeamData(competition.getFiveThirtyEightName(), true);
        } catch (IOException e) {
            logger.error("Can't scrape fivethirtyeight website for competition: "
                            + competition.getFiveThirtyEightName());
            logger.error(e.toString());
            return new Alert(AlertType.ERROR, competition.getEventID(),
                            "Attempt to update ratings failed for competition: " + competition.getFiveThirtyEightName()
                                            + ".  c.f Outrights server log for error details");
        }
        OutrightsServer.outrights.teamDataUpdate(competition, teamData);
        return new Alert(AlertType.INFO, competition.getEventID(),
                        "Successfully updated ratings for competition: " + competition.getFiveThirtyEightName());
    }

}
