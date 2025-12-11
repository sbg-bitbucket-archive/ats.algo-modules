package ats.algo.sport.outrights.server;
/*
 * This class will be called from the Outrights server class
 */

import java.util.Map;
import java.util.concurrent.Callable;

import ats.algo.sport.outrights.server.api.TeamDataUpdate;
import ats.algo.sport.outrights.server.scrapers.FiveThirtyEightScraper;
import ats.core.AtsBean;

public class OutrightsScrapingTimer extends AtsBean implements Callable<Map<String, TeamDataUpdate>> {
    /*
     * This field could be changed to a Map of String for scraping all leagues.
     */
    private String leagueName;

    public OutrightsScrapingTimer(String leagueName) {
        this.leagueName = leagueName;
    }

    @Override
    public Map<String, TeamDataUpdate> call() throws Exception {

        Map<String, TeamDataUpdate> teamMap = FiveThirtyEightScraper.getTeamData(leagueName, true);
        return teamMap;
    }



}
