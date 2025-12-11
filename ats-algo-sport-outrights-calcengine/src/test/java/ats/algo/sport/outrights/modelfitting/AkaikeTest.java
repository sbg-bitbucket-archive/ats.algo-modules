package ats.algo.sport.outrights.modelfitting;

import java.util.Collections;
import java.util.Map;

import ats.algo.sport.outrights.hyperparameters.HistoryMatchInfos;

public class AkaikeTest {

    Map<String, HistoryMatchInfos> dataMap;

    public <T extends Collections> AkaikeTest(Map<String, HistoryMatchInfos> historyMap) {
        this.dataMap = historyMap;
    }

    public void evaluate(double[] point) {
        // FIXME: the accurate calc should be based on the Likelihood of the model

    }


}
