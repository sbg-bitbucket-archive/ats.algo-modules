package ats.algo.core.comparetomarket;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.genericsupportfunctions.JsonSerializer;

public class TestParamFindResults {

    @Test
    public void test() {
        ParamFindResults paramFindResults = new ParamFindResults();
        paramFindResults.setFunctionValueAtMinimum(0.23);
        paramFindResults.setMinFunctionValueAchievable(0.68);
        paramFindResults.setAxisRotationReqd(true);
        paramFindResults.setShouldSuspendMarkets(true);
        paramFindResults.setnIterations(100);
        ParamFindResultsDescription description = new ParamFindResultsDescription();
        paramFindResults.setParamFindResultsDescription(description);
        description.setResultSummary("This is the results summary string - may be up to 80 chars long");
        description.addResultDetailRow("This is the first row of detail");
        description.addResultDetailRow("This is the second row of detail");
        description.addResultDetailRow("There may be many rows of detail");
        paramFindResults.setDetailedLog("This is a detailed log msg");
        String json = JsonSerializer.serialize(paramFindResults, true);
        System.out.println(json);
        ParamFindResults paramFindResults2 = JsonSerializer.deserialize(json, ParamFindResults.class);
        assertEquals(paramFindResults, paramFindResults2);
    }



}
