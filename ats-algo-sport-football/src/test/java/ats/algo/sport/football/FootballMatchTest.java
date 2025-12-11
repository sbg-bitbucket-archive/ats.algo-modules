package ats.algo.sport.football;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.football.FootballMatch;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;

public class FootballMatchTest {
    List<FootballMatchIncidentType> shootoutListStatusA = new ArrayList<FootballMatchIncidentType>();
    List<FootballMatchIncidentType> shootoutListStatusB = new ArrayList<FootballMatchIncidentType>();

    @Test
    /**
     * not really a test, but lets the playMatch method get exercised once, to allow any loops etc to be debugged
     */
    public void test() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        FootballMatchParams matchParams = new FootballMatchParams(matchFormat);
        matchParams.setGoalTotal(4, 0);
        matchParams.setGoalSupremacy(0, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        FootballMatch match = new FootballMatch(matchFormat, matchState, matchParams, false);
        try {
            match.playMatch();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testPenalty() {
        FootballShootoutInfo penaltyInfo = new FootballShootoutInfo(0, 0, TeamId.A, 0, true);

        assertTrue(penaltyInfo.getShootingNext() == TeamId.A);
        penaltyInfo.setShootoutInfo(1, 0, TeamId.A, 1, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.B);
        penaltyInfo.setShootoutInfo(2, 0, TeamId.A, 2, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.B);
        penaltyInfo.setShootoutInfo(2, 1, TeamId.A, 3, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.A);

        FootballShootoutInfo penaltyInfo2 = new FootballShootoutInfo(0, 0, TeamId.B, 0, true);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.B);
        penaltyInfo2.setShootoutInfo(1, 0, TeamId.B, 1, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.A);
        penaltyInfo2.setShootoutInfo(2, 0, TeamId.B, 2, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.A);
        penaltyInfo2.setShootoutInfo(2, 1, TeamId.B, 3, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.B);

        FootballShootoutInfo penaltyInfo3 = new FootballShootoutInfo(0, 0, TeamId.UNKNOWN, 0, true);
        assertTrue(penaltyInfo3.getShootingNext() == TeamId.UNKNOWN);
    }

    @Test
    public void testPenaltyOld() {
        FootballShootoutInfo penaltyInfo = new FootballShootoutInfo(0, 0, TeamId.A, 0, false);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.A);
        penaltyInfo.setShootoutInfo(1, 0, TeamId.A, 1, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.B);
        penaltyInfo.setShootoutInfo(2, 0, TeamId.A, 2, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.A);
        penaltyInfo.setShootoutInfo(2, 1, TeamId.A, 3, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo.getShootingNext() == TeamId.B);

        FootballShootoutInfo penaltyInfo2 = new FootballShootoutInfo(0, 0, TeamId.B, 0, false);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.B);
        penaltyInfo2.setShootoutInfo(1, 0, TeamId.B, 1, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.A);
        penaltyInfo2.setShootoutInfo(2, 0, TeamId.B, 2, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.B);
        penaltyInfo2.setShootoutInfo(2, 1, TeamId.B, 3, shootoutListStatusA, shootoutListStatusB);
        assertTrue(penaltyInfo2.getShootingNext() == TeamId.A);

        FootballShootoutInfo penaltyInfo3 = new FootballShootoutInfo(0, 0, TeamId.UNKNOWN, 0, false);
        assertTrue(penaltyInfo3.getShootingNext() == TeamId.UNKNOWN);
    }
}
