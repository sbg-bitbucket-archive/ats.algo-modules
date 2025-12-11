package ats.algo.sport.baseball;


import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.baseball.BaseballMatchIncident;
import ats.algo.sport.baseball.BaseballMatchState;
import ats.algo.sport.baseball.BaseballMatchIncident.BaseballMatchIncidentType;

public class BaseballMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchState matchState = new BaseballMatchState(matchFormat);
        BaseballMatchIncident baseballMatchIncident = new BaseballMatchIncident();
        baseballMatchIncident.set(BaseballMatchIncidentType.RUN1, TeamId.A);

        matchState.updateStateForIncident(baseballMatchIncident, false);

        baseballMatchIncident.set(BaseballMatchIncidentType.RUN2, TeamId.A);

        matchState.updateStateForIncident(baseballMatchIncident, false);

        baseballMatchIncident.set(BaseballMatchIncidentType.OUT3, TeamId.A);

        matchState.updateStateForIncident(baseballMatchIncident, false);

        baseballMatchIncident.set(BaseballMatchIncidentType.RUN2, TeamId.B);

        matchState.updateStateForIncident(baseballMatchIncident, false);

        baseballMatchIncident.set(BaseballMatchIncidentType.RUN3, TeamId.B);

        matchState.updateStateForIncident(baseballMatchIncident, false);


        baseballMatchIncident.set(BaseballMatchIncidentType.OUT3, TeamId.B);

        matchState.updateStateForIncident(baseballMatchIncident, false);

        for (int i = 0; i < 9; i++) {
            baseballMatchIncident.set(BaseballMatchIncidentType.OUT3, TeamId.A);

            matchState.updateStateForIncident(baseballMatchIncident, false);
            baseballMatchIncident.set(BaseballMatchIncidentType.OUT3, TeamId.B);

            matchState.updateStateForIncident(baseballMatchIncident, false);


        }

        // System.out.println(matchState);
        assertTrue(matchState.isMatchCompleted());


    }
}
