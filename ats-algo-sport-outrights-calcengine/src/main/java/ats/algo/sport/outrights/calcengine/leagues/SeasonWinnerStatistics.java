package ats.algo.sport.outrights.calcengine.leagues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.montecarloframework.MarketsFactory.TwoWayStatistic;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class SeasonWinnerStatistics {
    /*
     * keys are teamIdA, teamIdB
     */
    HashMap<String, Map<String, MarketsFactory.TwoWayStatistic>> map;

    public SeasonWinnerStatistics(MarketsFactory marketsFactory, Teams teams) {
        map = new HashMap<String, Map<String, MarketsFactory.TwoWayStatistic>>();
        for (Team team : teams.getTeams().values())
            map.put(team.getTeamID(), new HashMap<String, MarketsFactory.TwoWayStatistic>());
        for (Team teamA : teams.getTeams().values())
            for (Team teamB : teams.getTeams().values()) {
                String nameA = teamA.getDisplayName();
                String nameB = teamB.getDisplayName();
                String idA = teamA.getTeamID();
                String idB = teamB.getTeamID();
                if (nameA.compareTo(nameB) < 0) {
                    String key = "C:MTW" + ":" + idA + ":" + idB;
                    String mktName = "Season winner " + nameA + " v " + nameB;
                    TwoWayStatistic s = marketsFactory.new TwoWayStatistic(key, mktName, MarketGroup.NOT_SPECIFIED,
                                    true, "M", nameA, nameB);
                    map.get(idA).put(idB, s);
                }
            }

    }

    public void incrementStats(List<Standing> finishOrder) {
        for (Entry<String, Map<String, MarketsFactory.TwoWayStatistic>> e1 : map.entrySet()) {
            String idA = e1.getKey();
            for (Entry<String, MarketsFactory.TwoWayStatistic> e2 : e1.getValue().entrySet()) {
                String idB = e2.getKey();
                /*
                 * determine which team finished higher in the standings
                 */
                boolean aFinishedHigher = false;
                for (Standing standing : finishOrder) {
                    String teamId = standing.getTeamId();
                    if (teamId == idA) {
                        aFinishedHigher = true;
                        break;
                    } else if (teamId == idB)
                        break;
                }
                e2.getValue().increment(aFinishedHigher);
            }
        }
    }
}
