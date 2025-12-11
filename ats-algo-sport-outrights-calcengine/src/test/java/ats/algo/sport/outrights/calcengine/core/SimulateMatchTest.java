package ats.algo.sport.outrights.calcengine.core;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ats.algo.sport.outrights.calcengine.core.AbstractFormat;
import ats.algo.sport.outrights.calcengine.core.AbstractParams;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.MatchProbs;
import ats.algo.sport.outrights.calcengine.core.OutrightsCompetition;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsFactory;
import ats.algo.sport.outrights.calcengine.leagues.LeagueState;
import ats.algo.sport.outrights.calcengine.leagues.LeagueFormat;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;

public class SimulateMatchTest {

    @Test
    public void playMatchTest() {
        Competition competition = TestCompetition.generate();
        AbstractFormat format = CompetitionProperties.competitionMatchFormatInstance(competition);
        LeagueState state = new LeagueState(competition, (LeagueFormat) format);
        AbstractParams params = new AbstractParams();
        LeagueMarketsFactory marketsFactory = new LeagueMarketsFactory(competition);
        Map<Long, MatchProbs> atsProbs = new HashMap<>();
        OutrightsCompetition match = new OutrightsCompetition(format, state, params, atsProbs, marketsFactory);
        try {
            match.playMatch();
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
        LeagueState simState = (LeagueState) match.getSimulationState();
        Standings standings = simState.getSimulationStandings();
        List<Standing> finishOrder = standings.finishOrder();
        for (Standing standing : finishOrder)
            System.out.println(standing);
    }

}
