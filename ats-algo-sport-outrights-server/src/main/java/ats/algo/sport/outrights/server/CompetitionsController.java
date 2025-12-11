package ats.algo.sport.outrights.server;

import java.util.function.Consumer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.core.util.json.JsonUtil;

@Controller
public class CompetitionsController {

    @RequestMapping(method = RequestMethod.POST, value = "/competitions")
    @ResponseBody
    String competitionsPost(@RequestBody String competitionObject) {

        PostReply postReply;
        CompetitionsList competitionsList = JsonUtil.unmarshalJson(competitionObject, CompetitionsList.class);
        if (competitionsList == null) {
            postReply = new PostReply(false, "no competitionsList supplied");
            OutrightsServer.logger.error("no competitionsList supplied");
        } else {
            competitionsList.getCompetitionsList().forEach(competitionsListEntry -> {
                Competition competition = OutrightsServer.getFromEventId(competitionsListEntry.getEventID());
                updateIfNotNull(competitionsListEntry.getAtsCompetitionID(), id -> competition.setAtsCompetitionID(id));
                updateIfNotNull(competitionsListEntry.getName(), name -> competition.setName(name));
                OutrightsServer.logger.info(
                                "Competition updated successfully for eventID: " + competitionsListEntry.getEventID());
            });
            String feedback = "All Competitions in list updated successfully.  # competitions updated: "
                            + competitionsList.getCompetitionsList().size();
            postReply = new PostReply(true, feedback);
            OutrightsServer.logger.info(feedback);
        }
        return JsonUtil.marshalJson(postReply);
    }

    private void updateIfNotNull(String s, Consumer<String> f) {
        if (s != null)
            f.accept(s);
    }

}
