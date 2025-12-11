package ats.algo.sport.outrights.modelfitting;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import ats.algo.genericsupportfunctions.Minimiser;
import ats.algo.genericsupportfunctions.Minimiser.MinimiserResults;
import ats.algo.sport.outrights.calcengine.core.FullMatchProbs;
import ats.algo.sport.outrights.calcengine.core.RatingsFactors;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.hyperparameters.HistoryMatchInfos;
import ats.algo.sport.outrights.hyperparameters.OutrightsHyperparamsfind;

public class CalcBestFitConstants {
    Map<String, HistoryMatchInfos> historyMap;
    AkaikeTest akaikeTest;
    Team manCity;
    Team arsenal;
    Team bournemouth;
    Team brighton;
    Team burnley;
    Team chelsea;
    Team crystalPalace;
    Team everton;
    Team huddersfield;
    Team leicesterCity;
    Team liverpool;
    Team manUnited;
    Team newcastle;
    Team southampton;
    Team stokeCity;
    Team swanseaCity;
    Team tottenham;
    Team watford;
    Team westBrom;
    Team westHam;
    List<FiveThirtyEightPrediction> predictions;
    static boolean l2NormOtherwiseL1Norm = true;
    static boolean compareProbsTrueOtherwiseComparePrices = true;
    static double baseProb = 0.0005;
    static double[] bestFourParams = new double[4];;
    static double[] bestTwoParams = new double[2];;
    static double[] bestThreeParams = new double[3];;
    static String league = "";

    void init() {
        manCity = new Team("T1", "manCity", null, 3.2, 0.5);
        arsenal = new Team("T1", "arsenal", null, 2.8, 0.9);
        bournemouth = new Team("T1", "bournemouth", null, 1.7, 1.2);
        brighton = new Team("T1", "brighton", null, 1.3, 1);
        burnley = new Team("T1", "burnley", null, 1.4, 0.8);
        chelsea = new Team("T1", "chelsea", null, 2.4, 0.7);
        crystalPalace = new Team("T1", "crystalPalace", null, 1.7, 1);
        everton = new Team("T1", "everton", null, 1.7, 1.2);
        huddersfield = new Team("T1", "huddersfield", null, 1.2, 1.1);
        leicesterCity = new Team("T1", "leicesterCity", null, 1.8, 1);
        liverpool = new Team("T1", "liverpool", null, 3, 0.6);
        manUnited = new Team("T1", "manUnited", null, 2.2, 0.7);
        newcastle = new Team("T1", "newcastle", null, 1.5, 1.1);
        southampton = new Team("T1", "southampton", null, 1.7, 1.1);
        stokeCity = new Team("T1", "stokeCity", null, 1.5, 1.2);
        swanseaCity = new Team("T1", "swanseaCity", null, 1.3, 1);
        tottenham = new Team("T1", "tottenham", null, 2.7, 0.5);
        watford = new Team("T1", "watford", null, 1.8, 1.1);
        westBrom = new Team("T1", "westBrom", null, 1.4, 1);
        westHam = new Team("T1", "westHam", null, 1.7, 1.2);
        predictions = new ArrayList<>();
        predictions.add(new FiveThirtyEightPrediction(leicesterCity, stokeCity, .57, .19, .24));
        predictions.add(new FiveThirtyEightPrediction(bournemouth, newcastle, .48, .26, .26));
        predictions.add(new FiveThirtyEightPrediction(liverpool, westHam, .85, .04, .10));
        predictions.add(new FiveThirtyEightPrediction(brighton, swanseaCity, .41, .27, .32));
        predictions.add(new FiveThirtyEightPrediction(burnley, southampton, .44, .26, .30));
        predictions.add(new FiveThirtyEightPrediction(watford, everton, .48, .27, .25));
        predictions.add(new FiveThirtyEightPrediction(westBrom, huddersfield, .49, .20, .30));
        predictions.add(new FiveThirtyEightPrediction(manUnited, chelsea, .42, .32, .26));
        predictions.add(new FiveThirtyEightPrediction(crystalPalace, tottenham, .18, .60, .22));
    }

    @Test
    public void test1() {
        MethodName.log();
        init();
        double[] x = new double[2];
        x[0] = 0.337;
        x[1] = 1.605;
        double err = calcErr(x, true);
        // System.out.printf("SumSqErrs: %.3f\n", err);
        assertEquals(0.009, err, 0.0005);
    }


    public void calcBestFitConstants() {
        init();
        Minimiser minimiser = new Minimiser();
        double[] point = new double[2];
        point[0] = 0.0;
        point[1] = 1.0;
        double[] dels = new double[2];
        dels[0] = 0.001;
        dels[1] = 0.01;
        @SuppressWarnings("unused")
        MinimiserResults r = minimiser.minimise(point, dels, 0.0001, (double[] x) -> calcErr(x, l2NormOtherwiseL1Norm));
        // System.out.printf("success: %b, #iterations: %d, homeAdvantage: %.3f, meanAdj: %.3f\n", r.success,
        // r.nIterations, r.bestX[0], r.bestX[1]);
    }

    /*
     * Jin
     */

    public double[] calcBestFitConstants2() {
        init();
        Minimiser minimiser = new Minimiser();
        minimiser.setMaxIterations(400);
        double[] point = new double[2];
        point[0] = 0.0;
        point[1] = 1.0;
        double[] dels = new double[2];
        dels[0] = 0.001;
        dels[1] = 0.01;

        // try {
        // historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse("premier-league");
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        MinimiserResults r = minimiser.minimise(point, dels, 0.0001,
                        (double[] x) -> calcErr2(x, historyMap, l2NormOtherwiseL1Norm));
        // System.out.printf("success: %b, #iterations: %d, homeAdvantage: %.3f, meanAdj: %.3f\n", r.success,
        // r.nIterations, r.bestX[0], r.bestX[1]);

        akaikeTest.evaluate(point);

        return r.bestX;
    }

    public double[] calcBestFitConstants3() {
        init();
        Minimiser minimiser = new Minimiser();
        minimiser.setMaxIterations(400);
        double[] point = new double[3];
        point[0] = 0.0;
        point[1] = 1.0;
        point[2] = 0.1;
        double[] dels = new double[3];
        dels[0] = 0.001;
        dels[1] = 0.01;
        dels[2] = 0.005;
        // try {
        // historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse("premier-league");
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        MinimiserResults r = minimiser.minimise(point, dels, 0.0001,
                        (double[] x) -> calcErr3(x, historyMap, l2NormOtherwiseL1Norm));
        // System.out.printf("success: %b, #iterations: %d, homeAdvantage: %.3f, meanAdj: %.3f\n", r.success,
        // r.nIterations, r.bestX[0], r.bestX[1]);

        akaikeTest.evaluate(point);

        return r.bestX;
    }

    public double[] calcBestFitConstants4Params() {
        init();
        Minimiser minimiser = new Minimiser();
        minimiser.setMaxIterations(1000);
        double[] point = new double[4];
        point[0] = 1.0;
        point[1] = 1.0;
        point[2] = 1.0;
        point[3] = 1.0;
        double[] dels = new double[4];
        dels[0] = 0.01;
        dels[1] = 0.01;
        dels[2] = 0.01;
        dels[3] = 0.01;

        // try {
        // historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse("premier-league");
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }

        MinimiserResults r = minimiser.minimise(point, dels, 0.0001,
                        (double[] x) -> calcErr4(x, historyMap, l2NormOtherwiseL1Norm));

        // System.out.printf(
        // "success: %b, #iterations: %d, attackRatingFactor: %.3f, defenseRatingFactor: %.3f, homeAdjFactor: %.3f,
        // awayAdjFactor: %.3f",
        // r.success, r.nIterations, r.bestX[0], r.bestX[1], r.bestX[2], r.bestX[3]);

        return r.bestX;
    }

    @Test
    public void printTable() {
        MethodName.log();
        // System.out.printf(
        // "expectedGoalsA, expectedGoalsB, probAWins, probBWins, probDraw, totalGoals, probOver, goalsAcap,
        // probAWinsHcap\n");
        for (double hG = 0.5; hG < 5.9; hG = hG + 1.0)
            for (double aG = 0.5; aG <= hG; aG = aG + 1.0) {
                @SuppressWarnings("unused")
                FullMatchProbs p = new FullMatchProbs(hG, aG);
                // System.out.printf("%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f\n", hG, aG,
                // p.getProbHomeWin(),
                // p.getProbAwayWin(), p.getProbDraw(), (double) p.getTotalGoals() + 0.5,
                // p.getProbUnderTotalGoals(), (double) p.getGoalsHcap() + 0.5, p.getProbHomeWinsHcap());
            }
    }

    public double calcErr(double[] x, boolean selectL2NormOtherwiseL1Norm) {
        Double sumSqErrs = 0.0;
        double sumSqErr = 0;
        // System.out.printf("homeTeam, awayTeam, h538, hGC, a538, aGC, d538, dGC, sumSqErr\n");
        for (FiveThirtyEightPrediction q : predictions) {
            RatingsFactors f = new RatingsFactors(1.0, 1.0, x[0] - x[1], -x[1], -x[1], 0.1);
            FullMatchProbs p = new FullMatchProbs(q.homeTeam, q.awayTeam, f, false);
            if (selectL2NormOtherwiseL1Norm) {
                sumSqErr = sq(q.probHomeWin - p.getProbHomeWin()) + sq(q.probAwayWin - p.getProbAwayWin())
                                + sq(q.probDraw - p.getProbDraw());
            } else {
                sumSqErr = Math.abs(q.probHomeWin - p.getProbHomeWin()) + Math.abs(q.probAwayWin - p.getProbAwayWin())
                                + Math.abs(q.probDraw - p.getProbDraw());
            }
            // System.out.printf("%s,%s,%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f\n", q.homeTeam.getDisplayName(),
            // q.awayTeam.getDisplayName(), q.probHomeWin, p.getProbHomeWin(), q.probAwayWin,
            // p.getProbAwayWin(), q.probDraw, p.getProbDraw(), sumSqErr);
            sumSqErrs += sumSqErr;
        }
        return sumSqErrs;

    }

    public double calcErr2(double[] x, Map<String, HistoryMatchInfos> historyMap, boolean selectL2NormOtherwiseL1Norm) {
        Double sumSqErrs = 0.0;
        // System.out.printf("homeTeam, awayTeam, h538, hGC, a538, aGC, d538, dGC, sumSqErr\n");
        predictions = generatePredictionsFromData(historyMap);
        double sumSqErr = 0.0;
        for (FiveThirtyEightPrediction q : predictions) {
            RatingsFactors f = new RatingsFactors(1.0, 1.0, x[0] - x[1], -x[1], -x[1], 0.1);
            FullMatchProbs p = new FullMatchProbs(q.homeTeam, q.awayTeam, f, false);
            if (selectL2NormOtherwiseL1Norm) {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = sq(q.probHomeWin - p.getProbHomeWin()) + sq(q.probAwayWin - p.getProbAwayWin())
                                    + sq(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = sq(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + sq(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + sq(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
            } else {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = Math.abs(q.probHomeWin - p.getProbHomeWin())
                                    + Math.abs(q.probAwayWin - p.getProbAwayWin())
                                    + Math.abs(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = Math.abs(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + Math.abs(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + Math.abs(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
            }
            // System.out.printf("%s, %s,%s,%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f\n", q.date,
            // q.homeTeam.getDisplayName(), q.awayTeam.getDisplayName(), q.probHomeWin, p.getProbHomeWin(),
            // q.probAwayWin, p.getProbAwayWin(), q.probDraw, p.getProbDraw(), sumSqErr);
            sumSqErrs += sumSqErr;
        }
        return sumSqErrs;

    }

    /**
     * Three params model
     */
    public double calcErr3(double[] x, Map<String, HistoryMatchInfos> historyMap, boolean selectL2NormOtherwiseL1Norm) {
        Double sumSqErrs = 0.0;
        // System.out.printf("homeTeam, awayTeam, h538, hGC, a538, aGC, d538, dGC, sumSqErr\n");
        predictions = generatePredictionsFromData(historyMap);
        double sumSqErr = 0.0;
        for (FiveThirtyEightPrediction q : predictions) {
            RatingsFactors f = new RatingsFactors(1.0, 1.0, x[0] - x[1], -x[1], -x[1], 0.1, x[2]);

            FullMatchProbs p = new FullMatchProbs(q.homeTeam, q.awayTeam, f, false);

            if (selectL2NormOtherwiseL1Norm) {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = sq(q.probHomeWin - p.getProbHomeWin()) + sq(q.probAwayWin - p.getProbAwayWin())
                                    + sq(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = sq(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + sq(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + sq(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
            } else {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = Math.abs(q.probHomeWin - p.getProbHomeWin())
                                    + Math.abs(q.probAwayWin - p.getProbAwayWin())
                                    + Math.abs(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = Math.abs(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + Math.abs(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + Math.abs(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
            }
            // System.out.printf("%s, %s,%s,%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f\n", q.date,
            // q.homeTeam.getDisplayName(), q.awayTeam.getDisplayName(), q.probHomeWin, p.getProbHomeWin(),
            // q.probAwayWin, p.getProbAwayWin(), q.probDraw, p.getProbDraw(), sumSqErr);
            sumSqErrs += sumSqErr;
        }
        return sumSqErrs;

    }

    /**
     * This method evaluate 4 params model a*home_o+b*away_d+c = home a*away_o+b*home_d+d = away
     */
    public double calcErr4(double[] x, Map<String, HistoryMatchInfos> historyMap, boolean selectL2NormOtherwiseL1Norm) {
        Double sumSqErrs = 0.0;
        // System.out.printf("homeTeam, awayTeam, h538, hGC, a538, aGC, d538, dGC, sumSqErr\n");
        predictions = generatePredictionsFromData(historyMap);
        double sumSqErr = 0;
        for (FiveThirtyEightPrediction q : predictions) {
            RatingsFactors f = new RatingsFactors(x[0], x[1], x[2], x[3], 0, 0.1);
            FullMatchProbs p = new FullMatchProbs(q.homeTeam, q.awayTeam, f, false);
            if (selectL2NormOtherwiseL1Norm) {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = sq(q.probHomeWin - p.getProbHomeWin()) + sq(q.probAwayWin - p.getProbAwayWin())
                                    + sq(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = sq(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + sq(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + sq(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));

            } else {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = Math.abs(q.probHomeWin - p.getProbHomeWin())
                                    + Math.abs(q.probAwayWin - p.getProbAwayWin())
                                    + Math.abs(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = Math.abs(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + Math.abs(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + Math.abs(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));

            }

            sumSqErrs += sumSqErr;
            // System.out.printf("%s, %s,%s,%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f\n", q.date,
            // q.homeTeam.getDisplayName(), q.awayTeam.getDisplayName(), q.probHomeWin, p.getProbHomeWin(),
            // q.probAwayWin, p.getProbAwayWin(), q.probDraw, p.getProbDraw(), sumSqErr);

        }
        return sumSqErrs;

    }

    public double calcErrCostOnly(double[] x, Map<String, HistoryMatchInfos> historyMap,
                    boolean selectL2NormOtherwiseL1Norm, String writeToFile) {

        double sumSqErrs = 0.0;
        double sumSqErr = 0.0;
        int matchNo = 1;
        PrintWriter pw;
        try {
            String filename = writeToFile + ".csv";
            pw = new PrintWriter(new File(filename));
            StringBuilder sb = new StringBuilder();


            predictions = generatePredictionsFromData(historyMap);
            Iterator<Map.Entry<String, HistoryMatchInfos>> it = historyMap.entrySet().iterator();
            String title1 = "Match Date and Team, ";
            String title2 = "Final Score, ";
            String title3 = "365 Probs Home Win,";
            String title4 = "Our Probs Home Win,";
            String title5 = "365 Probs Away Win,";
            String title6 = "Our Probs Away Win,";
            String title7 = "Sum W/L/D Differences,";
            String title8 = "365 Loglikelihood,";
            String title9 = "Our loglihood";
            sb.append((title1 + title2 + title3 + title4 + title5 + title6 + title7 + title8 + title9) + ('\n'));

            double avgHomeWin365 = 0.0;
            double avgAwayWin365 = 0.0;

            double sstotH = 0.0;
            double ssresH = 0.0;

            double sstotA = 0.0;
            double ssresA = 0.0;

            for (FiveThirtyEightPrediction q : predictions) {
                avgHomeWin365 += q.probHomeWin;
                avgAwayWin365 += q.probAwayWin;
            }
            avgHomeWin365 = avgHomeWin365 / predictions.size();
            avgAwayWin365 = avgAwayWin365 / predictions.size();

            for (FiveThirtyEightPrediction q : predictions) {

                Map.Entry<String, HistoryMatchInfos> entry = it.next();

                RatingsFactors f = new RatingsFactors(x[0], x[1], x[2], x[3], 0, 0.1);
                FullMatchProbs p = new FullMatchProbs(q.homeTeam, q.awayTeam, f, false);
                /*
                 * Always use L1 norm in the final comparasion
                 */
                // if (false) {
                // if (compareProbsTrueOtherwiseComparePrices)
                // sumSqErr = sq(q.probHomeWin - p.getProbHomeWin()) + sq(q.probAwayWin - p.getProbAwayWin())
                // + sq(q.probDraw - p.getProbDraw());
                // else
                // sumSqErr = sq(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                // + sq(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                // + sq(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
                // } else {
                if (compareProbsTrueOtherwiseComparePrices)
                    sumSqErr = Math.abs(q.probHomeWin - p.getProbHomeWin())
                                    + Math.abs(q.probAwayWin - p.getProbAwayWin())
                                    + Math.abs(q.probDraw - p.getProbDraw());
                else
                    sumSqErr = Math.abs(1. / (q.probHomeWin + baseProb) - 1. / (p.getProbHomeWin() + baseProb))
                                    + Math.abs(1. / (q.probAwayWin + baseProb) - 1. / (p.getProbAwayWin() + baseProb))
                                    + Math.abs(1. / (q.probDraw + baseProb) - 1. / (p.getProbDraw() + baseProb));
                // }


                ssresH += sq(q.probHomeWin - p.getProbHomeWin());
                ssresA += sq(q.probAwayWin - p.getProbAwayWin());
                sstotH += sq(q.probHomeWin - avgHomeWin365);
                sstotA += sq(q.probAwayWin - avgAwayWin365);
                double theta365 = 0.0;
                double thetaUs = 0.0;
                HistoryMatchInfos matchInfos = entry.getValue();
                /**
                 * Log-likelihood is calculated as:
                 * 
                 * theta^hat = arg-max(theta; epsilon)
                 * 
                 * sum((M-theta)^2) the smaller the better, where theta is lose prob/ draw prob/ win prob according to
                 * the actual outcome
                 * 
                 */
                if (matchInfos.getFtHomeGoal() > matchInfos.getFtAwayGoal()) {
                    theta365 = q.probHomeWin;
                    thetaUs = p.getProbHomeWin();
                } else if (entry.getValue().getFtHomeGoal() < entry.getValue().getFtAwayGoal()) {
                    theta365 = q.probAwayWin;
                    thetaUs = p.getProbAwayWin();
                } else {
                    theta365 = q.probDraw;
                    thetaUs = p.getProbDraw();
                }
                // double llhBet365 = sq(1-theta365);
                // double llhUs = sq(1-thetaUs);

                double llhBet365 = -Math.log(theta365);
                double llhUs = -Math.log(thetaUs);


                StringBuilder nameKey = new StringBuilder(q.getDate() + "-" + q.getHomeTeam().getDisplayName() + "-"
                                + q.getAwayTeam().getDisplayName());
                sb.append(nameKey.append(',')
                                .append(entry.getValue().getFtHomeGoal() + "--" + entry.getValue().getFtAwayGoal())
                                .append(',').append(round(q.getProbHomeWin(), 3)).append(',')
                                .append(round(p.getProbHomeWin(), 3)).append(',').append(round(q.getProbAwayWin(), 3))
                                .append(',').append(round(p.getProbAwayWin(), 3)).append(',').append(round(sumSqErr, 3))
                                .append(',').append(round(llhBet365, 3)).append(',').append(round(llhUs, 3))
                                .append('\n'));



                // // System.out.printf("Match Number: %s, %s, %s,%s,%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, Error: %.2f\n",
                // // matchNo, q.date,
                // // q.homeTeam.getDisplayName(), q.awayTeam.getDisplayName(), q.probHomeWin, p.getProbHomeWin(),
                // // q.probAwayWin, p.getProbAwayWin(), q.probDraw, p.getProbDraw(), sumSqErr);
                sumSqErrs += sumSqErr;
                matchNo++;
            }

            pw.write(sb.toString());
            pw.close();
            @SuppressWarnings("unused")
            double coefHomeWin = 1 - ssresH / sstotH;
            @SuppressWarnings("unused")
            double coefAwayWin = 1 - ssresA / sstotA;
            // System.out.println(
            // "Coefficient of determinations are:" + round(coefHomeWin, 3) + " " + round(coefAwayWin, 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if (selectL2NormOtherwiseL1Norm)
        // return Math.sqrt(sumSqErrs) / matchNo;
        // else
        return sumSqErrs / matchNo;

    }

    private List<FiveThirtyEightPrediction> generatePredictionsFromData(Map<String, HistoryMatchInfos> historyMap) {
        ArrayList<FiveThirtyEightPrediction> predictions = new ArrayList<>();
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {

            HistoryMatchInfos value = entry.getValue();
            Team home = new Team("T1", "home: " + value.getHomeTeamName(), null, value.getORateHome(),
                            value.getDRateHome());
            Team away = new Team("T1", "away: " + value.getAwayTeamName(), null, value.getORateAway(),
                            value.getDRateAway());
            String date = value.getDate().toString();
            predictions.add(new FiveThirtyEightPrediction(date, home, away, value.getHomeWinProb365(),
                            value.getAwayWinProb365(), value.getDrawProb365()));
        }
        return predictions;
    }

    private static double sq(double x) {
        return x * x;
    }

    @SuppressWarnings("unused")
    private void residualErrors(String leagueName) {
        /*
         * 4 params model
         */
        double[] x = new double[4];
        // bestFourParams[0]=;
        // bestFourParams[0]=;
        // bestFourParams[0]=;
        // bestFourParams[0]=;
        x[0] = bestFourParams[0];
        x[1] = bestFourParams[1];
        x[2] = bestFourParams[2];
        x[3] = bestFourParams[3];
        // try {
        // historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(leagueName);
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        // System.out.println("4 params model cost: " + round(
        // calcErrCostOnly(x, historyMap, l2NormOtherwiseL1Norm,
        // league + " 4-params-model-" + "L2 Norm-" + l2NormOtherwiseL1Norm
        // + "-Compare Probs-" + compareProbsTrueOtherwiseComparePrices),
        // 3));

        // homeAdvantage: 0.351, meanAdj: 1.669
        x[0] = 1.0;
        x[1] = 1.0;
        bestTwoParams[0] = 0.323;
        bestTwoParams[1] = 1.618;
        x[2] = -bestTwoParams[1] + bestTwoParams[0];
        x[3] = -bestTwoParams[1];
        // System.out.println("2 param model cost (homeAdvantage: " + round(bestTwoParams[0], 3) + ", meanAdj: "
        // + round(bestTwoParams[1], 3) + "): "
        // + round(calcErrCostOnly(x, historyMap, l2NormOtherwiseL1Norm,
        // league + " 2-params-model-" + "L2 Norm-" + l2NormOtherwiseL1Norm
        // + "-Compare Probs-" + compareProbsTrueOtherwiseComparePrices),
        // 3));

        // x[0] = 1.0;
        // x[1] = 1.0;
        // x[2] = -1.238652894066mo668;
        // x[3] = -1.238652894066668;
        // // System.out.println("1 param model (least square obtained param) cost: "+ calcErrCostOnly(x, historyMap));

    }

    public static void main(String[] args) {
        CalcBestFitConstants calc = new CalcBestFitConstants();
        l2NormOtherwiseL1Norm = true;
        compareProbsTrueOtherwiseComparePrices = true;
        league = "jin";// "Championship"; //"premier-league"
        calc.loadMap(league, true);
        // calc.loadMap("Championship","premier-league");
        calc.akaike();
        bestTwoParams = calc.calcBestFitConstants2();
        bestThreeParams = calc.calcBestFitConstants3();
        bestFourParams = calc.calcBestFitConstants4Params();
        // System.out.println("--------------");
        // System.out.println("Two " + bestTwoParams[0] + " " + bestTwoParams[1]);
        // System.out.println("Three " + bestThreeParams[0] + " " + bestThreeParams[1] + " " + bestThreeParams[2]);
        // System.out.println("Four " + bestFourParams[0] + " " + bestFourParams[1] + " " + bestFourParams[2] + " "
        // + bestFourParams[3]);
        // calc.residualErrors(league);
    }


    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void akaike() {
        this.akaikeTest = new AkaikeTest(historyMap);
    }

    private void loadMap(String league, boolean use538probs) {
        try {
            historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(league, use538probs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * experimenting
     */
    @SuppressWarnings("unused")
    private void loadMap(String league, String league2) {
        try {
            historyMap = OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(league, false);
            // Map<String, HistoryMatchInfos> map2 =
            // OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(league2);
            historyMap.putAll(OutrightsHyperparamsfind.loadDataForMatchPredictionTestUse(league2, false));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
