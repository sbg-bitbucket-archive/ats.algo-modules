package ats.algo.sport.outrights.server;

import org.springframework.web.bind.annotation.RestController;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Selection;
import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FullMatchProbs;
import ats.algo.sport.outrights.calcengine.core.Standing;

import java.util.Map.Entry;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.FixturesListEntry;

@RestController
public class DemoDataController {

    @RequestMapping("/demo")
    public String getDemoData(@RequestParam(name = "eventID", required = false,
                    defaultValue = OutrightsServer.DEFAULT_EVENT_ID) String eventIdStr) {
        long eventID = OutrightsServer.convertEventIdStr(eventIdStr);
        Competition competition = OutrightsServer.getFromEventId(eventID);
        if (competition == null)
            return "Unknown eventID";
        /*
         * update the calcs
         */
        OutrightsServer.outrights.calculate(competition);
        StringBuilder b = new StringBuilder();
        b.append("Outrights Server<BR>");
        /*
         * throw away code - for demo purposes only
         */
        Teams teams = competition.getTeams();
        b.append("TEAMS:<BR>");
        for (Team team : teams.list()) {
            b.append(team.toString()).append("<BR>");
        }
        b.append("<BR>CURRENT STANDINGS<BR>");
        for (Standing standing : competition.generateStandings().finishOrder()) {
            b.append(standing.toString()).append("<BR>");
        }
        b.append("<BR>FORECAST STANDINGS<BR>");
        for (FcastStanding fcastStanding : competition.getFcastStandings().finishOrder()) {
            b.append(fcastStanding.toString()).append("<BR>");
        }
        b.append("<BR>ALERTS:<BR>");
        for (Alert alert : competition.getAlerts())
            b.append(alert.toString()).append("<BR>");

        b.append("<BR>MARKETS<BR>");
        for (Market market : competition.getMarkets()) {
            b.append("<BR>" + market.getFullKey() + ": " + market.getMarketDescription() + "<BR>");
            for (Entry<String, Selection> e : market.getSelections().entrySet()) {
                Selection selection = e.getValue();
                double prob = selection.getProb();
                double price = 10000;
                if (prob != 0.0)
                    price = 1 / prob;
                b.append("----").append(e.getKey())
                                .append(String.format(":  prob %.2f, 100%% price %.1f.", prob, price)).append("<BR>");
            }
        }

        b.append("<BR>FIXTURES:<BR>");
        for (Fixture fixture : competition.getFixtures()) {
            FixturesListEntry entry;
            if (fixture.canCalcProbs()) {
                FullMatchProbs probs = Outrights.calcFixtureProbs(competition, fixture);
                entry = new FixturesListEntry(fixture, probs.getProbHomeWin(), probs.getProbAwayWin(),
                                probs.getProbDraw());
            } else
                entry = new FixturesListEntry(fixture, -1.0, -1.0, -1.0);
            b.append(entry.toString()).append("<BR>");
        }
        return b.toString();
    }

}
