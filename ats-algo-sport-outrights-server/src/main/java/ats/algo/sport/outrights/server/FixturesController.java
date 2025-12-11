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
import ats.core.util.json.JsonUtil;

@Controller
public class FixturesController {

    @GetMapping("/fixtures")
    @ResponseBody
    public String getFixtures(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return null;
        Fixtures fixtures = competition.getFixtures();

        FixturesList fixturesList = new FixturesList(competition.getEventID());
        for (Fixture fixture : fixtures) {
            FullMatchProbs probs;
            if (fixture.isProbsSourcedfromATS()) {
                probs = competition.getFixtureProbs(fixture.getEventID());
            } else
                probs = Outrights.calcFixtureProbs(competition, fixture);
            FixturesListEntry entry;
            if (probs == null)
                entry = new FixturesListEntry(fixture, 0.0, 0.0, 0.0);
            else
                entry = new FixturesListEntry(fixture, probs.getProbHomeWin(), probs.getProbAwayWin(),
                                probs.getProbDraw());
            fixturesList.add(entry);
        }
        return JsonUtil.marshalJson(fixturesList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fixtures")
    @ResponseBody
    String fixturesPost(@RequestBody String fixtureObject) {

        PostReply postReply;
        FixturesList fixturesList = JsonUtil.unmarshalJson(fixtureObject, FixturesList.class);
        if (fixturesList == null) {
            postReply = new PostReply(false, "Update failed.  No fixture list supplied");
            OutrightsServer.logger.error("Update failed.  No fixture list supplied");
        } else {
            Competition competition = OutrightsServer.getCompetitions().get(fixturesList.getEventID());
            if (competition == null) {
                postReply = new PostReply(false, "Update failed.  invalid event id" + fixturesList.getEventID());
                OutrightsServer.logger.error("Update failed.  invalid event id" + fixturesList.getEventID());
            } else {
                fixturesList.getFixturesList()
                                .forEach(t -> OutrightsServer.outrights.updateFixture(competition, t.getFixture()));
            }
            postReply = new PostReply(true, "fixtures updated successfully");
            OutrightsServer.logger.info("fixtures updated successfully for eventID:" + fixturesList.getEventID());
        }

        return JsonUtil.marshalJson(postReply);
    }
}
