package ats.algo.sport.outrights.server;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.RatingsList;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.algo.sport.outrights.server.scrapers.FiveThirtyEightScraper;
import ats.core.util.json.JsonUtil;

@Controller
public class UpdateDataController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDataController.class);

    @GetMapping("/updateratings")
    @ResponseBody
    public String getUpdate(
                    @RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Map<String, TeamDataUpdate> teamData;
        try {
            teamData = FiveThirtyEightScraper.getTeamData(competition.getFiveThirtyEightName(), true);
        } catch (IOException e) {
            logger.error("Can't scrape fivethirtyeight website for competition: "
                            + competition.getFiveThirtyEightName());
            logger.error(e.toString());
            return null;
        }

        OutrightsServer.outrights.teamDataUpdate(competition, teamData);

        RatingsList list = new RatingsList(eventID, competition.getName(), competition.getTeams());
        return JsonUtil.marshalJson(list);
    }

}
