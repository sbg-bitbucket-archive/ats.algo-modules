package ats.algo.sport.examplesport;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.examplesport.ExampleMatchFormat;
import ats.algo.sport.examplesport.ExampleMatchIncident;
import ats.algo.sport.examplesport.ExampleMatchIncident.ExampleMatchIncidentType;
import ats.algo.sport.examplesport.ExampleMatchIncidentResult;
import ats.algo.sport.examplesport.ExampleMatchState;

public class ExampleMatchStateTest {

    ExampleMatchState matchState;

    @Test
    public void test() {
        MethodName.log();
        ExampleMatchFormat matchFormat = new ExampleMatchFormat();
        matchFormat.setNoLegsInMatch(3);
        matchState = new ExampleMatchState(matchFormat);
        ExampleMatchIncidentResult outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYA);
        assertEquals(ExampleMatchIncidentResult.LEGWONBYA, outcome);
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYB);
        assertEquals(ExampleMatchIncidentResult.LEGWONBYB, outcome);
        assertFalse(matchState.isMatchCompleted());
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYB);
        assertEquals(ExampleMatchIncidentResult.MATCHWONBYB, outcome);
        assertTrue(matchState.isMatchCompleted());
        matchFormat.setNoLegsInMatch(4);
        matchState = new ExampleMatchState(matchFormat);
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYA);
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYB);
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYA);
        assertEquals(ExampleMatchIncidentResult.LEGWONBYA, outcome);
        assertFalse(matchState.isMatchCompleted());
        outcome = getExampleMatchIncidentResult(ExampleMatchIncidentType.LEGWONBYB);
        assertEquals(ExampleMatchIncidentResult.DRAW, outcome);
        assertTrue(matchState.isMatchCompleted());
    }

    private ExampleMatchIncidentResult getExampleMatchIncidentResult(ExampleMatchIncidentType incidentType) {
        ExampleMatchIncident incident = new ExampleMatchIncident();
        incident.setExampleMatchIncidentType(incidentType);
        return (ExampleMatchIncidentResult) matchState.updateStateForIncident(incident, false);
    }
}
