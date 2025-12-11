package ats.algo.sport.outrights.calcengine.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;

class PfMetrics {
    double distance;
    Map<String, Double> ratingsAdjustments;

    private static final double RATING_ADJ_FACTOR = 0.015;

    PfMetrics(Competition competition) {
        Map<String, FcastStanding> fcastStandings = competition.getFcastStandings().getStandings();
        ratingsAdjustments = new HashMap<>(fcastStandings.size());
        distance = 0.0;
        fcastStandings.forEach((teamId, entry) -> {
            double targetPoints = entry.getTargetPoints();
            if (targetPoints != 0.0) {
                double x = entry.getTargetPoints() - entry.getPoints();
                ratingsAdjustments.put(teamId, x * RATING_ADJ_FACTOR);
                distance += x * x;
            }
        });
        int n = ratingsAdjustments.size();
        if (n > 0)
            distance = Math.sqrt(distance / n);
        else
            distance = 0.0;

    }

}


public class ParamFindEngine {
    private static final double OPTIMUM_METRIC_TARGET = 0.1;
    private static final double ACCEPTABLE_METRIC_TARGET = 0.3;
    private static final int MAX_ITERATIONS = 8;
    private static final Logger logger = LoggerFactory.getLogger(ParamFindEngine.class);

    private Competition competition;
    PfMetrics pfMetrics;
    Alert pfResult;

    public ParamFindEngine(Competition competition) {
        this.competition = competition;
    }

    public void calculate() {
        logger.info("doing baseline calc");
        CalcEngine calcEngine = new CalcEngine(competition);
        calcEngine.setIgnoreBias(true); // don't use bias when param finding)
        calcEngine.calculate();
        int nIterations = 0;
        pfMetrics = new PfMetrics(competition);
        double startDistance = pfMetrics.distance;
        logIteration(nIterations, pfMetrics);
        while (pfMetrics.distance > OPTIMUM_METRIC_TARGET && nIterations <= MAX_ITERATIONS) {
            competition.getTeams().adjustRatings(pfMetrics.ratingsAdjustments);
            calcEngine.calculate();
            nIterations++;
            pfMetrics = new PfMetrics(competition);
            logIteration(nIterations, pfMetrics);
        }
        String s = String.format("Distance metric before pf : %.3f, after pf : %.3f (target %.3f)", startDistance,
                        pfMetrics.distance, ACCEPTABLE_METRIC_TARGET);
        if (pfMetrics.distance < ACCEPTABLE_METRIC_TARGET) {
            String description = "Success.  " + s;
            pfResult = new Alert(AlertType.INFO, competition.getEventID(), description);
            logger.info(description);
        } else {
            String description = "Failure.  " + s;
            pfResult = new Alert(AlertType.WARNING, competition.getEventID(), description);
            logger.warn(description);
        }
    }

    private void logIteration(int nIterations, PfMetrics metrics) {
        logger.info(String.format("iteration no: %d.  Distance: %.3f (target: %.3f) ", nIterations, metrics.distance,
                        OPTIMUM_METRIC_TARGET));
        // System.out.printf(String.format("iteration no: %d. Distance: %.3f (target:
        // %.3f) \n", nIterations,
        // metrics.distance, METRIC_TARGET));
    }


    public PfMetrics getPfMetrics() {
        return pfMetrics;
    }

    public Alert getPfResult() {
        return pfResult;
    }



}
