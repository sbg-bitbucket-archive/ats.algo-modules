package ats.algo.sport.outrights.server.scrapers;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import ats.algo.sport.outrights.server.api.TeamDataUpdate;

public class FiveThirtyEightScraperTest {

    @Test
    public void test() {
        Map<String, TeamDataUpdate> data;
        try {
            data = FiveThirtyEightScraper.getTeamData("premier-league", true);
            System.out.println(data);
            for (Entry<String, TeamDataUpdate> e : data.entrySet()) {
                System.out.printf("key: %s, value: %s\n", e.getKey(), e.getValue().toString());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            fail();
        }
    }

    /*
     * Create copy pasteable code for ChampionsLeague
     */
    @Test
    public void test2() {
        Map<String, TeamDataUpdate> data;
        try {
            int i = 1;
            data = FiveThirtyEightScraper.getTeamData("championship", true);
            for (Entry<String, TeamDataUpdate> e : data.entrySet()) {
                String head = "teams.put(\"T" + i + "\", new Team(\"T" + i + "\",";
                String content = "\"" + e.getKey() + "\"" + ", " + "\"" + e.getKey() + "\"" + ","
                                + e.getValue().getRatingOffense() + ", " + e.getValue().getRatingDefense() + "));";
                System.out.printf(head + content + "\n");
                i++;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            fail();
        }
    }

}
