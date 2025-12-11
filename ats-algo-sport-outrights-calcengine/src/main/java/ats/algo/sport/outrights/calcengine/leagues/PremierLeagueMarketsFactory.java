package ats.algo.sport.outrights.calcengine.leagues;

import java.util.HashMap;
import java.util.Map;

import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
import ats.algo.sport.outrights.calcengine.core.AbstractState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class PremierLeagueMarketsFactory extends LeagueMarketsFactory {

    static final String MAN_CITY = "Manchester City";
    private String[] selectionsExManCity;
    private Map<String, Integer> selectionsMapExManCity = new HashMap<>();

    private NWayStatistic2 leagueWinnerWithOutManC;// > Premier League Winner

    public PremierLeagueMarketsFactory(Competition competition) {
        super(competition);
        boolean teamToBeRemovedExist = checkTeamInList(MAN_CITY, selections);
        if (teamToBeRemovedExist) {
            selectionsExManCity = removeTeam(selections, MAN_CITY);
            for (int j = 0; j < selectionsExManCity.length; j++) {
                selectionsMapExManCity.put(selectionsExManCity[j], j);
            }
            leagueWinnerWithOutManC = new NWayStatistic2("C:WOM", "Winner Without Man City", teamToBeRemovedExist, "M",
                            selectionsExManCity);
        }
    }

    private boolean checkTeamInList(String teamToBeRemoved2, String[] selections) {
        for (String ele : selections)
            if (teamToBeRemoved2.equals(ele))
                return true;
        return false;
    }

    private String[] removeTeam(String[] selections, String string) {
        String[] out = new String[selections.length - 1];
        int i = 0;
        for (String ele : selections) {
            if (!ele.equals(string)) {
                out[i] = ele;
                i++;
            }
        }
        return out;
    }

    public void updateStats(AbstractState simulationState) {
        super.updateStats(simulationState);
        Teams teams = competition.getTeams();
        /*
         * n.b. relies on super.updateStats having set finishOrder
         */

        for (int finishPosn = 0; finishPosn < finishOrder.size(); finishPosn++) {
            String teamId = finishOrder.get(finishPosn).getTeamId();
            String teamName = teams.get(teamId).getDisplayName();
            if (!teamName.equals(MAN_CITY)) {
                /*
                 * increment with the first team in the finish order which is not Man C
                 */
                int selectionIndex2 = selectionsMapExManCity.get(teamName);
                leagueWinnerWithOutManC.increment(selectionIndex2);
                break;
            }
        }
        leagueWinnerWithOutManC.incrementNoTrials();


    }

    @Override
    public AbstractMarketsFactory copy() {
        PremierLeagueMarketsFactory cc = new PremierLeagueMarketsFactory(competition);
        return cc;
    }

}
