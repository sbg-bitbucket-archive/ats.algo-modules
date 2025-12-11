package ats.algo.sport.outrights.server.scrapers;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.competitionsdata.ChampionshipLeague1819;
import ats.algo.sport.outrights.competitionsdata.LaLiga1819;
import ats.algo.sport.outrights.competitionsdata.PremierLeague1819;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;

public class SportingIndexScraperTest {

    // @Test
    public void testPremierLeague() {
        MethodName.log();
        Competition competition = PremierLeague1819.generate();
        testScrape(competition.getTeams(), competition.getFiveThirtyEightName());
    }

    // @Test
    public void testChampionshipLeague() {
        MethodName.log();
        Competition competition = ChampionshipLeague1819.generate();
        testScrape(competition.getTeams(), competition.getFiveThirtyEightName());
    }

    // @Test
    public void testLaligaLeague() {
        MethodName.log();
        Competition competition = LaLiga1819.generate();
        testScrape(competition.getTeams(), competition.getFiveThirtyEightName());
    }

    public void testScrape(Teams teams, String siName) {
        Map<String, TargetPointsEntry> items = null;
        try {
            items = SportingIndexScraper.scrape(siName, teams);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        for (Map.Entry<String, TargetPointsEntry> ele : items.entrySet()) {
            System.out.println(ele.toString());
        }
        /*
         * can't check the actual value since may change over time. Just check that the target is non-zero
         */
        assertTrue(items.get("T1").getTargetPoints() > 0);
    }
}
