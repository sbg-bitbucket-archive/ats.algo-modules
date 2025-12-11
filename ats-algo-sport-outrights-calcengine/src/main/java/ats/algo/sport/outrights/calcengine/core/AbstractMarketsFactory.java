package ats.algo.sport.outrights.calcengine.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ats.algo.montecarloframework.MarketsFactory;

public abstract class AbstractMarketsFactory extends MarketsFactory {

    protected Competition competition;
    protected AbstractFormat competitionFormat;
    protected Map<String, Integer> selectionsMap; // holds the mapping from selName to index in selections array
    protected String[] selections;

    public AbstractMarketsFactory(Competition competition) {
        super();
        this.competition = competition;
        competitionFormat = CompetitionProperties.competitionMatchFormatInstance(competition);
        Teams teams = competition.getTeams();
        selections = new String[teams.size()];
        int i = 0;
        for (Team team : teams.values())
            selections[i++] = team.getDisplayName();
        Arrays.sort(selections); // alphabetically
        selectionsMap = new HashMap<>(teams.size());
        for (int j = 0; j < selections.length; j++)
            selectionsMap.put(selections[j], j);
    }

    public abstract void updateStats(AbstractState simulationState);

    /**
     * make a copy if itself to be used by another instance in the Monte Carlo simulation
     * 
     * @return
     */
    public abstract AbstractMarketsFactory copy();

    /**
     * @param fcastStandings
     * 
     */
    public abstract FcastStandings generateFcastStandings(FcastStandings fcastStandings);

}
