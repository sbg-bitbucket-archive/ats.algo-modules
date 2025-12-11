package ats.algo.sport.examplesport;

import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;

public class ExampleMatchEngine extends MonteCarloMatchEngine {

    public ExampleMatchEngine(ExampleMatchFormat matchFormat) {
        super(null);
        matchParams = new ExampleMatchParams();
        matchParams.setDefaultParams(matchFormat);
        matchState = new ExampleMatchState(matchFormat);
        match = new ExampleMatch(matchFormat, (ExampleMatchState) matchState, (ExampleMatchParams) matchParams);
        initialiseConcurrentCalculationManager();
    }

}
