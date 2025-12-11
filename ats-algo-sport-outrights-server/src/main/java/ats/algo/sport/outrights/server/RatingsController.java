package ats.algo.sport.outrights.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.RatingsList;
import ats.algo.sport.outrights.server.api.RatingsListEntry;
import ats.core.util.json.JsonUtil;

@Controller
public class RatingsController {

    @GetMapping("/ratings")
    @ResponseBody
    public String getRatings(
                    @RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        RatingsList list = new RatingsList(eventID, competition.getName(), competition.getTeams());
        return JsonUtil.marshalJson(list);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ratings")
    @ResponseBody
    String teamPost(@RequestBody RatingsList ratingsList) {
        long eventID = ratingsList.getEventID();
        Competition competition = OutrightsServer.outrights.getCompetitions().get(eventID);
        PostReply reply;
        if (competition == null)
            reply = new PostReply(false, "no such eventID");
        else {
            List<Team> teams = new ArrayList<Team>(ratingsList.getRatings().size());
            for (RatingsListEntry entry : ratingsList.getRatings()) {
                teams.add(new Team(entry.getTeamID(), null, null, entry.getRatingAttack(), entry.getRatingDefense()));
            }
            OutrightsServer.outrights.updateTeams(competition, teams);
            reply = new PostReply(true, "ratings updated successfully");
        }
        return JsonUtil.marshalJson(reply);

    }

}
