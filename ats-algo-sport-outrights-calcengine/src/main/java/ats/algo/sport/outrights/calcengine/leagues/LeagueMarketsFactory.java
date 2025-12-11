package ats.algo.sport.outrights.calcengine.leagues;

import java.util.Comparator;
import java.util.List;

import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
import ats.algo.sport.outrights.calcengine.core.AbstractState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class LeagueMarketsFactory extends AbstractMarketsFactory {

    private LeagueFormat leagueFormat;

    private NWayStatistic rockBottom;
    private NWayStatistic2 relegated;
    private NWayStatistic2 leagueWinner;// > Premier League Winner
    private NWayStatistic2 finishOutsideTop4;// > outside Top 4 finish
    private NWayStatistic2 topHalfFinish;// > Top half finish
    private NWayStatistic2 topFourFinish;// > Top four finish
    private NWayStatistic2 bottomHalfFinish;// > Bottom half finish
    private NWayStatistic2 inPlayoffs;// > in promotion playoffs
    private NWayStatistic2 promoted;

    private NWayStatistic2 topAtXmas;// >
    private NWayStatistic2 toStayUp;// > To Stay up

    private FcastStandingsStatistic leagueStatistic; // Outrights specific statistics

    private int nTeams;

    private TotalPointsStatistics totalPointsStatistics;
    private SeasonWinnerStatistics seasonWinnerStatistics;

    protected List<Standing> finishOrder;

    public LeagueMarketsFactory(Competition competition) {
        super(competition);
        super.setAllowGenerateAllSelection(true);
        nTeams = competition.getCompetitionType().getLeagueTeamsNumber();
        leagueFormat = (LeagueFormat) super.competitionFormat;
        relegated = new NWayStatistic2("C:R", "Relegated", true, "M", selections);
        rockBottom = new NWayStatistic("C:RB", "Rock Bottom", true, "M", selections);
        leagueWinner = new NWayStatistic2("C:LW", "League Winner", true, "M", selections);


        finishOutsideTop4 = new NWayStatistic2("C:O4", "outside Top 4 finish", true, "M", selections);
        topHalfFinish = new NWayStatistic2("C:TH", "Top half finish", true, "M", selections);
        topFourFinish = new NWayStatistic2("C:T4", "Top four finish", true, "M", selections);
        bottomHalfFinish = new NWayStatistic2("C:BH", "Bottom half finish", true, "M", selections);
        topAtXmas = new NWayStatistic2("C:XMST", "Winner by Xmas", true, "M", selections);
        toStayUp = new NWayStatistic2("C:SU", "To Stay Up", true, "M", selections);
        if (leagueFormat.getnPromotedUnconditionally() + leagueFormat.getnPromotedViaPlayoff() > 0)
            promoted = new NWayStatistic2("C:PR", "Promoted", true, "M", selections);
        if (leagueFormat.getnPromotedViaPlayoff() > 0)
            inPlayoffs = new NWayStatistic2("C:PO", "In play-offs", true, "M", selections);

        Teams teams = competition.getTeams();
        leagueStatistic = new FcastStandingsStatistic(this, teams);
        totalPointsStatistics = new TotalPointsStatistics(this, teams);
        seasonWinnerStatistics = new SeasonWinnerStatistics(this, teams);
    }


    public void updateStats(AbstractState simulationState) {
        Standings standings = ((LeagueState) simulationState).getSimulationStandings();
        PlayoffsMgr playoffsMgr = ((LeagueState) simulationState).getPlayoffsMgr();
        finishOrder = standings.finishOrder();
        Teams teams = competition.getTeams();
        LeagueFormat leagueFormat = (LeagueFormat) competitionFormat;

        String topAtXmasTeamId = ((LeagueState) simulationState).getTopAtXmasTeamId();

        if (topAtXmasTeamId != null) {
            String teamName = teams.get(topAtXmasTeamId).getDisplayName();
            int selectionIndex = selectionsMap.get(teamName);
            topAtXmas.increment(selectionIndex);
            topAtXmas.incrementNoTrials();
        }

        seasonWinnerStatistics.incrementStats(finishOrder);
        totalPointsStatistics.incrementStats(finishOrder);

        for (int finishPosn = 0; finishPosn < finishOrder.size(); finishPosn++) {
            String teamId = finishOrder.get(finishPosn).getTeamId();
            String teamName = teams.get(teamId).getDisplayName();
            int selectionIndex = selectionsMap.get(teamName);

            if (finishPosn >= nTeams - leagueFormat.getnRelegated()) {
                relegated.increment(selectionIndex);
            } else {
                toStayUp.increment(selectionIndex);
            }

            if (finishPosn == 0)
                leagueWinner.increment(selectionIndex);


            if (finishPosn >= 4) {
                finishOutsideTop4.increment(selectionIndex);
            } else {
                topFourFinish.increment(selectionIndex);
            }

            if (finishPosn < nTeams / 2) {
                topHalfFinish.increment(selectionIndex);
            } else {
                bottomHalfFinish.increment(selectionIndex);
            }
            if (finishPosn == nTeams - 1)
                rockBottom.increment(selectionIndex);

            if (promoted != null) {
                if (finishPosn < leagueFormat.getnPromotedUnconditionally())
                    promoted.increment(selectionIndex);
                else if (playoffsMgr.isPromoted(teamId))
                    promoted.increment(selectionIndex);
            }
            if (inPlayoffs != null) {
                if (playoffsMgr.isInPlayoffs(teamId))
                    inPlayoffs.increment(selectionIndex);
            }

        }
        bottomHalfFinish.incrementNoTrials();
        topHalfFinish.incrementNoTrials();
        topFourFinish.incrementNoTrials();
        finishOutsideTop4.incrementNoTrials();
        leagueWinner.incrementNoTrials();
        relegated.incrementNoTrials();
        toStayUp.incrementNoTrials();
        if (promoted != null)
            promoted.incrementNoTrials();
        if (inPlayoffs != null)
            inPlayoffs.incrementNoTrials();
        leagueStatistic.incrementStats(standings);

    }


    @Override
    public AbstractMarketsFactory copy() {
        LeagueMarketsFactory cc = new LeagueMarketsFactory(competition);
        return cc;
    }

    @Override
    public FcastStandings generateFcastStandings(FcastStandings oldStandings) {
        FcastStandings newStandings = leagueStatistic.generateFcastStandings();
        if (oldStandings != null) {
            /*
             * copy the targetPoints property from the old standings to the new ones.
             */
            newStandings.getStandings().forEach((teamId, newStanding) -> {
                FcastStanding oldStanding = oldStandings.getStandings().get(teamId);
                newStanding.setTargetPoints(oldStanding.getTargetPoints());
            });
        }
        return newStandings;
    }

    public class KeyComparator implements Comparator<String> {

        @Override
        public int compare(String name1, String name2) {
            char[] c1 = name1.toLowerCase().toCharArray();
            char[] c2 = name2.toLowerCase().toCharArray();

            for (int i = 0; i < c1.length; i++) {
                if (c1[i] < c2[i])
                    return 1;
                if (c1[i] > c2[i])
                    return -1;
                // if ==0 continue loop
            }

            return 0; // all the same
        }

    }

}
