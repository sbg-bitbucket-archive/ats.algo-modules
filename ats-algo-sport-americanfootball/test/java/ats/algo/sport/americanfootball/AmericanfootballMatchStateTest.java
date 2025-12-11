package ats.algo.sport.americanfootball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.americanfootball.AmericanfootballMatchPeriod;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;

public class AmericanfootballMatchStateTest {
    
     @Test
    public void testFourQuarters() {
        MethodName.print();
        AmericanfootballMatchFormat matchFormat = new AmericanfootballMatchFormat();
        matchFormat.setExtraTimeMinutes(15);
        matchFormat.setNormalTimeMinutes(60);
        
        AmericanfootballMatchState matchState = new AmericanfootballMatchState(matchFormat);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(AmericanfootballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        AmericanfootballMatchPeriod outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.IN_FIRST_QUATER, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getElapsedTimeSecs());

        
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 900);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.AT_FIRST_QUATER_END, outcome);
        assertEquals(900, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());
        /* Second quarter*/
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 900);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.IN_SECOND_QUATER, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(900, matchState.getElapsedTimeSecs());

        
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1800);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.AT_SECOND_QUATER_END, outcome);
        assertEquals(1800, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());
        
        /* Third quarter*/
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1800);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.IN_THIRD_QUATER, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1800, matchState.getElapsedTimeSecs());

        
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.AT_THIRD_QUATER_END, outcome);
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());

        /* Fourth quarter*/
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.IN_FOURTH_QUATER, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(2700, matchState.getElapsedTimeSecs());

        
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3600);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.AT_FULL_TIME, outcome);
        assertEquals(3600, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());
        
        /* Extra time quarter*/
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 3600);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
       
        assertEquals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1,  matchState.getOvertimeNo()); // First extra time period
        assertEquals(3600, matchState.getElapsedTimeSecs());
        
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 4500);
        outcome = (AmericanfootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        assertEquals(AmericanfootballMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(4500, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());
        
 
     //   assertEquals(AmericanfootballMatchPeriod.MATCH_COMPLETED, outcome);  
        
    }
    
    @Ignore @Test
    public void testIncrementSimulationElapsedTime() {
        MethodName.print();
        AmericanfootballMatchFormat matchFormat = new AmericanfootballMatchFormat();
        AmericanfootballMatchState matchState = new AmericanfootballMatchState(matchFormat);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;       
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident);
        matchState.incrementSimulationElapsedTime(1600);       //10 secs before half time         
        assertEquals(AmericanfootballMatchPeriod.IN_FIRST_QUATER, matchState.getMatchPeriod());
        assertEquals(1600, matchState.getElapsedTimeSecs());
        assertEquals(1600, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(300);   
        /**
         * roll over to second half
         */
        assertEquals(AmericanfootballMatchPeriod.IN_SECOND_QUATER, matchState.getMatchPeriod());
        // HIGH LIGHT, 1800 will be recored when switching period.
        // this is different from football matchstate test
        assertEquals(1800, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(30); 
        assertEquals(1830, matchState.getElapsedTimeSecs());
        assertEquals(30, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(1800);    //10 seconds before match end
        assertEquals(AmericanfootballMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
    }
    
  
    
    
        
        
}
