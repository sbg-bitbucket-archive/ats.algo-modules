package ats.algo.sport.outrights.server;


import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ats.algo.sport.outrights.calcengine.core.ATSFixture;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;
import ats.algo.sport.outrights.server.api.EventID;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.dbaccess.OutrightsDbConnector;
import ats.core.util.json.JsonUtil;

@Controller
public class UpdateAtsDataController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateAtsDataController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/updateatsdata")
    @ResponseBody
    String updateRatingsPost(@RequestBody String object) throws SQLException {

        PostReply postReply;
        EventID eventID = JsonUtil.unmarshalJson(object, EventID.class);
        if (eventID == null) {
            postReply = new PostReply(false, "no eventID supplied");
            OutrightsServer.logger.error("no eventID supplied");
            return JsonUtil.marshalJson(postReply);
        }
        Competition competition = OutrightsServer.getCompetitions().get(eventID.getEventID());
        if (competition == null) {
            postReply = new PostReply(false, "Invalid event id");
            OutrightsServer.logger.error("invalid event id" + eventID.getEventID());
            return JsonUtil.marshalJson(postReply);

        }
        Alert alert = updateLatestEventIds(competition);
        competition.addAlert(alert);
        postReply = new PostReply(alert);
        return JsonUtil.marshalJson(postReply);
    }

    private Alert updateLatestEventIds(Competition competition) throws SQLException {

        List<ATSFixture> atsFixtures;
        OutrightsDbConnector dbConnector = new OutrightsDbConnector();
        OutrightsConfiguration outrightsConfiguration = OutrightsServer.getOutrightsConfiguration();

        /**
         * Following config could be removed in server environment.
         */
        // outrightsConfiguration.setAtsDbUsername("ats_owner");
        // outrightsConfiguration.setAtsDbPassword("ats_owner");
        // outrightsConfiguration.setAtsDbUrl(
        // "jdbc:postgresql://enamwuatdbs01:5432/ats_whitelabel?tcpKeepAlive=true&prepareThreshold=0");
        /***/
        dbConnector.establishConnection(outrightsConfiguration.getAtsDbUrl(), outrightsConfiguration.getAtsDbUsername(),
                        outrightsConfiguration.getAtsDbPassword());

        String atsCompetitionID = competition.getAtsCompetitionID();
        Teams teams = competition.getTeams();
        try {
            atsFixtures = dbConnector.getATSFixtures(atsCompetitionID, teams);
        } catch (SQLException e) {
            logger.error("Can't get eventId's from d/b for competition: " + competition.getName());
            logger.error(e.toString());
            return new Alert(AlertType.ERROR, competition.getEventID(),
                            "Attempt to get eventIds from ATS failed for competition: " + competition.getName()
                                            + ".  c.f Outrights server log for error details");
        }
        dbConnector.closeConnection();
        OutrightsServer.outrights.updateEventIds(competition, atsFixtures);
        return new Alert(AlertType.INFO, competition.getEventID(),
                        "Successfully updated eventIds for competition: " + competition.getName());
    }

    public static void main(String[] args) throws SQLException {
        UpdateAtsDataController atsDataController = new UpdateAtsDataController();
        atsDataController.updateLatestEventIds(new Competition());
    }
}
