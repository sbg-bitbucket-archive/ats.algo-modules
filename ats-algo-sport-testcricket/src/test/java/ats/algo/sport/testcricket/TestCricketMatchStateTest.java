package ats.algo.sport.testcricket;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.testcricket.TestCricketMatchIncident.CricketMatchIncidentType;

public class TestCricketMatchStateTest {

    @Test
    public void testOnFieldPlayerName() {
        // CricketMatchFormat matchFormat = new CricketMatchFormat();
        // CricketMatchState matchState = new CricketMatchState(matchFormat);
        // CricketMatchIncident snookerMatchIncident=new CricketMatchIncident();
        // CricketMatchIncidentResult outcome = (CricketMatchIncidentResult)
        // matchState
        // .updateStateForIncident(snookerMatchIncident);
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        // assertFalse(matchState.isMatchCompleted());
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        // assertTrue (matchState.isMatchCompleted());
        // matchState = new CricketMatchState(matchFormat);
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        // assertFalse (matchState.isMatchCompleted());
        // outcome = (CricketMatchIncidentResult) matchState
        // .updateStateForIncident(snookerMatchIncident);
        TestCricketMatchFormat matchFormat = new TestCricketMatchFormat();
        TestCricketMatchState matchState = new TestCricketMatchState(matchFormat);
        TestCricketMatchIncident matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.BATFIRST);
        matchIncident.setTeamId(TeamId.A);
        matchState.setPlayerOut(true);
        matchState.updateStateForIncident(matchIncident, false);
        String name = matchState.isPlayerOut() ? matchState.getOnFieldPlayerName()[0]
                        : matchState.getOnFieldPlayerName()[1];
        System.out.println("Status 0 : who are on fields and who is batting( A and B) : "
                        + matchState.getOnFieldPlayerName()[0] + "--" + matchState.getOnFieldPlayerName()[1] + "--"
                        + matchState.isPlayerABating());
        System.out.println("Next player out is A (true) or B(false) : " + matchState.isPlayerOut() + "-" + name);
        matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.WICKET_BOWLED);
        matchIncident.setTeamId(TeamId.A);
        matchIncident.setCricketPlayerName(matchState.getOnFieldPlayerName()[0]);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.setPlayerOut(false);
        name = matchState.isPlayerOut() ? matchState.getOnFieldPlayerName()[0] : matchState.getOnFieldPlayerName()[1];
        System.out.println("Status 1 : who are on fields and who is batting( A and B) : "
                        + matchState.getOnFieldPlayerName()[0] + "--" + matchState.getOnFieldPlayerName()[1] + "--"
                        + matchState.isPlayerABating());
        System.out.println("Next player out is A (true) or B(false) : " + matchState.isPlayerOut() + "-" + name);
        matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.WICKET_OTHER);
        matchIncident.setTeamId(TeamId.A);

        matchIncident.setCricketPlayerName(matchState.getOnFieldPlayerName()[1]);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.setPlayerOut(true);
        name = matchState.isPlayerOut() ? matchState.getOnFieldPlayerName()[0] : matchState.getOnFieldPlayerName()[1];
        System.out.println("Status 2 : who are on fields and who is batting( A and B) : "
                        + matchState.getOnFieldPlayerName()[0] + "--" + matchState.getOnFieldPlayerName()[1] + "--"
                        + matchState.isPlayerABating());
        System.out.println("Next player out is A (true) or B(false) : " + matchState.isPlayerOut() + "-" + name);
        matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.WICKET_CAUGHT);
        matchIncident.setTeamId(TeamId.A);
        matchIncident.setCricketPlayerName(matchState.getOnFieldPlayerName()[0]);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.setPlayerOut(false);
        name = matchState.isPlayerOut() ? matchState.getOnFieldPlayerName()[0] : matchState.getOnFieldPlayerName()[1];
        System.out.println("Status 3 : who are on fields and who is batting( A and B) : "
                        + matchState.getOnFieldPlayerName()[0] + "--" + matchState.getOnFieldPlayerName()[1] + "--"
                        + matchState.isPlayerABating());
        System.out.println("Next player out is A (true) or B(false) : " + matchState.isPlayerOut() + "-" + name);
        matchIncident = new TestCricketMatchIncident();
        matchIncident.set(CricketMatchIncidentType.WICKET_LBW);
        matchIncident.setTeamId(TeamId.A);
        matchIncident.setCricketPlayerName(matchState.getOnFieldPlayerName()[1]);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.setPlayerOut(true);
        name = matchState.isPlayerOut() ? matchState.getOnFieldPlayerName()[0] : matchState.getOnFieldPlayerName()[1];
        System.out.println("Status 4 : who are on fields and who is batting( A and B) : "
                        + matchState.getOnFieldPlayerName()[0] + "--" + matchState.getOnFieldPlayerName()[1] + "--"
                        + matchState.isPlayerABating());
        System.out.println("Next player out is A (true) or B(false) : " + matchState.isPlayerOut() + "-" + name);
    }
}
