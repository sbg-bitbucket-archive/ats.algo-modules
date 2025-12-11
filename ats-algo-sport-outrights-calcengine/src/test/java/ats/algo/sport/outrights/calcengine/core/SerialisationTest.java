package ats.algo.sport.outrights.calcengine.core;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.FixturesListEntry;
import ats.algo.sport.outrights.server.api.MarketListEntry;
import ats.algo.sport.outrights.server.api.MarketsList;
import ats.algo.sport.outrights.server.api.RatingsList;
import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.core.util.json.JsonUtil;

public class SerialisationTest {

    @Test
    public void testAll() {
        MethodName.log();
        Competition competition = TestCompetition.generate();
        Fixture fixture = competition.getFixtures().get(0);
        testJsonSerialisation(fixture);
        testJsonSerialisation(competition.getTeams());
        competition.setEventID(123456L);
        Competitions competitions = new Competitions();
        competitions.add(competition);
        testJsonSerialisation(competition.generateStandings());

        testJsonSerialisation(competition.getFixtures());
        FixturesList list = new FixturesList(123456L);
        list.add(new FixturesListEntry(competition.getFixtures().get(0), 0.2, 0.6, 0.4));
        testJsonSerialisation(list);
        MarketsList list2 = new MarketsList(123456L);
        Market market = new Market();
        list2.add(new MarketListEntry(market));
        testJsonSerialisation(list2);
        RatingsList list3 = new RatingsList(123456L, "test", competition.getTeams());
        testJsonSerialisation(list3);
        TeamDataUpdate update = new TeamDataUpdate(2.1, 3.6, 4, 3, 2, 1);
        testJsonSerialisation(update);
        PostReply reply = new PostReply(true, "test");
        testJsonSerialisation(reply);
        testJsonSerialisation(competitions);
        testJsonSerialisation(competitions.generateCompetitionsList());
    }

    @Test
    public void testAllCompetitions() {
        MethodName.log();
        Outrights o = new Outrights();
        testJsonSerialisation(o.getCompetitions());

    }

    private void testJsonSerialisation(Object object) {
        String json = JsonUtil.marshalJson(object);
        // System.out.println(json);
        Object object2 = JsonUtil.unmarshalJson(json, object.getClass());
        assertEquals(object, object2);

    }

}
