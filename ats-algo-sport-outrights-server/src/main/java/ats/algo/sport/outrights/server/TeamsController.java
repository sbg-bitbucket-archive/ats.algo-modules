package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.TeamObject;
import ats.core.util.json.JsonUtil;

@Controller
public class TeamsController {

    @GetMapping("/teams")
    @ResponseBody
    public String getTeams(
                    @RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        return JsonUtil.marshalJson(competition.getTeams());

    }

    @GetMapping("/team")
    @ResponseBody
    public String getTeam(@RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr,
                    @RequestParam(name = "teamID", required = true) String id) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Teams teams = competition.getTeams();
        TeamObject teamObject = new TeamObject(competition.getEventID(), competition.getName(), teams.get(id));
        return JsonUtil.marshalJson(teamObject);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/team")
    @ResponseBody
    String teamPost(@RequestBody String teamObject2) {
        PostReply teamPostReply;
        TeamObject teamObject = JsonUtil.unmarshalJson(teamObject2, TeamObject.class);
        Team team = teamObject.getTeam();
        if (team == null)
            teamPostReply = new PostReply(false, "no team supplied");
        else {
            Competition competition = OutrightsServer.getCompetitions().get(teamObject.getEventId());
            if (competition == null)
                teamPostReply = new PostReply(false, "invalid event id");
            else {
                /*
                 * TODO add checks here to validate ratings updates within acceptable ranges
                 */
                OutrightsServer.outrights.updateTeam(competition, team);
                teamPostReply = new PostReply(true, "ratings updated successfully");
            }
        }
        return JsonUtil.marshalJson(teamPostReply);
    }

}
