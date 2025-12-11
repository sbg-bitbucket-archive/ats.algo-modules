package ats.algo.sport.outrights.server;

import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;
import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.algo.sport.outrights.server.api.Warnings;
import ats.core.util.json.JsonUtil;

@Controller
public class WarningsController {

    @GetMapping("/warnings")
    @ResponseBody
    public String getWarnings() {
        Competitions competitions = OutrightsServer.getCompetitions();
        Warnings list = new Warnings();
        for (Entry<Long, Competition> e : competitions.getCompetitions().entrySet()) {
            Competition competition = e.getValue();
            CompetitionWarnings competitionWarnings =
                            OutrightsServer.outrights.checkCompetitionStateForErrors(competition);
            list.put(e.getKey(), competitionWarnings);
        }
        return JsonUtil.marshalJson(list);
    }

}
