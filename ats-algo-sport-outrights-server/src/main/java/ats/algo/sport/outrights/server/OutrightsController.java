package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.core.util.json.JsonUtil;

@Controller
public class OutrightsController {

    @GetMapping("/outrights")
    @ResponseBody
    public String getCompetitions() {
        Competitions competitions = OutrightsServer.getCompetitions();
        return JsonUtil.marshalJson(new CompetitionsList(competitions));
    }

    @GetMapping("/")
    @ResponseBody
    public String root() {
        return "<H1>Outrights Server<H1><BR>enter url + '/outrights' to see list of supported competitions in Json format";
    }

}
