package ats.algo.sport.outrights.calcengine.leagues;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.markets.Market;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.montecarloframework.MarketsFactory.Statistic;
import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;

/**
 * unlike other statistics this is NOT used to generate a market. Instead it collects the statistics required to
 * generatethe FcastStandings at the end of a simulation run
 * 
 * @author gicha
 *
 */

class StandingStatistic {
    String teamId;
    long played;
    long won;
    long drawn;
    long goalsFor;
    long goalsAgainst;
    long points;

    StandingStatistic(String teamId) {
        this.teamId = teamId;
        reset();
    }

    void reset() {
        played = 0L;
        won = 0L;
        drawn = 0L;
        goalsFor = 0L;
        goalsAgainst = 0L;
        points = 0L;
    }

    void increment(Standing standing) {
        played += standing.getPlayed();
        won += standing.getWon();
        drawn += standing.getDrawn();
        goalsFor += standing.getGoalsFor();
        goalsAgainst += standing.getGoalsAgainst();
        points += standing.getPoints();
    }

    public void consolidate(StandingStatistic other) {
        played += other.played;
        won += other.won;
        drawn += other.drawn;
        goalsFor += other.goalsFor;
        goalsAgainst += other.goalsAgainst;
        points += other.points;

    }

    public FcastStanding generateFcastStanding(int count) {
        FcastStanding f = new FcastStanding(teamId);
        f.setPlayed((int) (this.played / count));
        f.setWon(GCMath.round(((double) this.won) / count, 1));
        f.setDrawn(GCMath.round(((double) this.drawn) / count, 1));
        f.setGoalsFor(GCMath.round(((double) this.goalsFor) / count, 1));
        f.setGoalsAgainst(GCMath.round(((double) this.goalsAgainst) / count, 1));
        f.setPoints(GCMath.round(((double) this.points) / count, 1));
        return f;
    }
}


public class FcastStandingsStatistic extends Statistic {

    private Map<String, StandingStatistic> standingStatistics;
    int count;

    public FcastStandingsStatistic(MarketsFactory marketFactory, Teams teams) {
        marketFactory.super("OR:PTS", "Outrights Standings statistics", MarketGroup.NOT_SPECIFIED, true, "M");
        standingStatistics = new HashMap<>(teams.size());
        for (Team team : teams.getTeams().values()) {
            StandingStatistic standingStatistic = new StandingStatistic(team.getTeamID());
            standingStatistics.put(team.getTeamID(), standingStatistic);
        }
        reset();
    }

    @Override
    public Market generateMarket() {
        /*
         * this statistic does not generate a market
         */
        return null;
    }

    @Override
    public void consolidate(Statistic statistic) {
        FcastStandingsStatistic fss = (FcastStandingsStatistic) statistic;
        for (Entry<String, StandingStatistic> e : fss.standingStatistics.entrySet()) {
            StandingStatistic s = standingStatistics.get(e.getKey());
            s.consolidate(e.getValue());
        }
        count += fss.count;
    }

    @Override
    public void reset() {
        for (StandingStatistic s : standingStatistics.values())
            s.reset();
        count = 0;
    }

    public void incrementStats(Standings standings) {
        for (Entry<String, Standing> e : standings.entrySet()) {
            StandingStatistic s = standingStatistics.get(e.getKey());
            s.increment(e.getValue());
        }
        count++;
    }

    public FcastStandings generateFcastStandings() {
        FcastStandings fcastStandings = new FcastStandings();
        for (Entry<String, StandingStatistic> e : standingStatistics.entrySet()) {
            FcastStanding fcastStanding = e.getValue().generateFcastStanding(count);
            fcastStandings.put(e.getKey(), fcastStanding);

        }
        return fcastStandings;
    }

}
