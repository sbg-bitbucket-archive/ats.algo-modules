package ats.algo.sport.outrights.calcengine.leagues;

import java.util.List;

import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
import ats.algo.sport.outrights.calcengine.core.AbstractState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Teams;

public class LeagueMarketsFactory extends AbstractMarketsFactory {

    private LeagueFormat leagueFormat;

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

    public LeagueMarketsFactory(Competition competition) {
        super(competition);
        leagueFormat = (LeagueFormat) super.competitionFormat;
        relegated = new NWayStatistic2("C:R", "Relegated", true, "M", selections);
        leagueWinner = new NWayStatistic2("C:LW", "Premier League Winner", true, "M", selections);
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
    }

    public void updateStats(AbstractState simulationState) {
        Standings standings = ((LeagueState) simulationState).getSimulationStandings();
        PlayoffsMgr playoffsMgr = ((LeagueState) simulationState).getPlayoffsMgr();
        List<Standing> finishOrder = standings.finishOrder();
        Teams teams = competition.getTeams();
        LeagueFormat leagueFormat = (LeagueFormat) competitionFormat;
        int nTeams = teams.size();

        String topAtXmasTeamId = ((LeagueState) simulationState).getTopAtXmasTeamId();

        if (topAtXmasTeamId != null) {
            String teamName = teams.get(topAtXmasTeamId).getDisplayName();
            int selectionIndex = selectionsMap.get(teamName);
            topAtXmas.increment(selectionIndex);
            topAtXmas.incrementNoTrials();
        }

        for (int finishPosn = 0; finishPosn < finishOrder.size(); finishPosn++) {
            String teamId = finishOrder.get(finishPosn).getTeamId();
            String teamName = teams.get(teamId).getDisplayName();
            int selectionIndex = selectionsMap.get(teamName);

            if (finishPosn >= nTeams - leagueFormat.getnRelegated()) {
                relegated.increment(selectionIndex);
            } else {
                toStayUp.increment(selectionIndex);
            }

            if (finishPosn == 0) {
                leagueWinner.increment(selectionIndex);
            }

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

    }

    @Override
    public AbstractMarketsFactory copy() {
        LeagueMarketsFactory cc = new LeagueMarketsFactory(competition);
        return cc;
    }

}
