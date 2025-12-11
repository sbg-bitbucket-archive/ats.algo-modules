package ats.algo.sport.outrights.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.FullMatchProbs;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.FixturesListEntry;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.TeamObject;
import ats.core.util.json.JsonUtil;

@Controller
public class FixturesController {

    @GetMapping("/fixtures")
    @ResponseBody
    public String getFixtures(
                    @RequestParam(name = "eventID", required = false, defaultValue = "123456") String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Fixtures fixtures = competition.getFixtures();

        FixturesList fixturesList = new FixturesList(competition.getEventID());
        for (Fixture fixture : fixtures) {
            FullMatchProbs probs = Outrights.calcFixtureProbs(competition, fixture);
            FixturesListEntry entry = new FixturesListEntry(fixture, probs.getProbHomeWin(), probs.getProbAwayWin(),
                            probs.getProbDraw());
            fixturesList.add(entry);
        }
        return JsonUtil.marshalJson(fixturesList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fixtures")
    @ResponseBody
    String teamPost(@RequestBody TeamObject teamObject) {
        PostReply postReply;
        // TODO - handle the data in the post
        postReply = new PostReply(true, "fixtures updated successfully");

        return JsonUtil.marshalJson(postReply);
    }

}
