package ats.algo.sport.outrights.server;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.calcengine.core.Teams;

@Controller
public class FcastStandingsCsvController {

    @GetMapping("/fcaststandingscsv")
    @ResponseBody
    public String getStandingsCsv(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        List<FcastStanding> f = competition.getFcastStandings().finishOrder();
        StringBuilder b = new StringBuilder();
        Teams teams = competition.getTeams();
        b.append("team,played,won,drawn,lost,goalsfor,goalsAgainst,goalsDiff,points<BR>");
        f.forEach((FcastStanding s) -> {
            String team = teams.get(s.getTeamId()).getDisplayName();
            b.append(team).append(",");
            b.append(s.getPlayed()).append(",");
            b.append(String.format("%.1f", s.getWon())).append(",");
            b.append(String.format("%.1f", s.getDrawn())).append(",");
            b.append(String.format("%.1f", s.getLost())).append(",");
            b.append(String.format("%.1f", s.getGoalsFor())).append(",");
            b.append(String.format("%.1f", s.getGoalsAgainst())).append(",");
            b.append(String.format("%.1f", s.getGoalsDiff())).append(",");
            b.append(String.format("%.1f", s.getPoints())).append("<BR>");
        });
        return b.toString();
    }

    /*
     * no longer support updates to standings. All changes have to be made to fixtures.
     */
    // @RequestMapping(method = RequestMethod.POST, value = "/standings")
    // @ResponseBody
    // String teamPost(@RequestBody TeamObject teamObject) {
    // PostReply postReply;
    // postReply = new PostReply(true, "standings updated successfully");
    //
    // return JsonUtil.marshalJson(postReply);
    // }

}
