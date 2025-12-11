package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competitions;
import ats.core.util.json.JsonUtil;

@Controller
public class OutrightsController {

    @GetMapping("/outrights")
    @ResponseBody
    public String getOutrightCompetitions() {
        Competitions competitions = OutrightsServer.getCompetitions();
        return JsonUtil.marshalJson(competitions.generateCompetitionsList());
    }

    @GetMapping("/")
    @ResponseBody
    public String root() {
        return "<H1>Outrights Server<H1><BR>enter url + '/outrights' to see list of supported outright competitions in Json format";
    }

}
