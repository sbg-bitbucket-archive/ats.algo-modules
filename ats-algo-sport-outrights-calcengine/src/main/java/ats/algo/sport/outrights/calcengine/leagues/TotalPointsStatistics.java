package ats.algo.sport.outrights.calcengine.leagues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class TotalPointsStatistics {
    /*
     * keys are teamIdA, teamIdB
     */
    Map<String, MarketsFactory.TotalStatistic> map;

    public TotalPointsStatistics(MarketsFactory marketsFactory, Teams teams) {
        map = new HashMap<String, MarketsFactory.TotalStatistic>();
        int n = teams.getTeams().size();
        int maxPoints = 3 * (n - 1) * (n - 1);
        for (Team team : teams.getTeams().values()) {
            String name = team.getDisplayName();
            String id = team.getTeamID();
            String key = "C:OU" + ":" + id;
            MarketsFactory.TotalStatistic s = marketsFactory.new TotalStatistic(key, "Season points " + name,
                            MarketGroup.NOT_SPECIFIED, true, "M", maxPoints);
            map.put(id, s);
        }
    }

    public void incrementStats(List<Standing> standings) {
        for (Standing standing : standings) {
            map.get(standing.getTeamId()).increment(standing.getPoints());
        }
    }
}
