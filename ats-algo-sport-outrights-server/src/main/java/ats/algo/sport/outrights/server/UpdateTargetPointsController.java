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
import ats.algo.sport.outrights.server.api.TargetPointsEntry;
import ats.algo.sport.outrights.server.scrapers.SportingIndexScraper;
import ats.core.util.json.JsonUtil;

@Controller
public class UpdateTargetPointsController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateTargetPointsController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/updatetargetpoints")
    @ResponseBody
    String updateTargetPointsPost(@RequestBody String object) {

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
        Alert alert = updateLatestTargetPoints(competition);
        competition.addAlert(alert);
        postReply = new PostReply(alert);
        return JsonUtil.marshalJson(postReply);
    }

    private Alert updateLatestTargetPoints(Competition competition) {
        Map<String, TargetPointsEntry> items;
        try {
            items = SportingIndexScraper.scrape(competition.getFiveThirtyEightName(), competition.getTeams());
        } catch (IOException e) {
            logger.error("Can't scrape sporting index website for competition: "
                            + competition.getFiveThirtyEightName());
            logger.error(e.toString());
            return new Alert(AlertType.ERROR, competition.getEventID(),
                            "Attempt to update target points failed for competition: "
                                            + competition.getFiveThirtyEightName()
                                            + ".  c.f Outrights server log for error details");
        }
        if (items == null) {
            return new Alert(AlertType.ERROR, competition.getEventID(),
                            "Can't scrape sporting index website for competition: "
                                            + competition.getFiveThirtyEightName()
                                            + ".  c.f Outrights server log for error details");
        } else
            items.forEach((k, v) -> {
                OutrightsServer.outrights.updateTargetPoints(competition, v);
            });
        return new Alert(AlertType.INFO, competition.getEventID(),
                        "Successfully updated target points for competition: " + competition.getFiveThirtyEightName());

    }

}
