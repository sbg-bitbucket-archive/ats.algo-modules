package ats.algo.sport.outrights.hyperparameters;

import java.util.Map;

import Jama.Matrix;

public class hyperParamsFinder {
    /**
     * This class holds the gradient descent method for finding the best parameters for the cost function:
     * 
     * a*O_rate_home+b*D_rate_away+c = Home_Goal
     * 
     * d*O_rate_away+e*D_rate_home+f = Away_Goal
     **/
    Map<String, HistoryMatchInfos> historyMap;
    double stepLength = 0.001;
    double a = 1.0, b = -1.0, c = 1;
    int matchNumbers;
    double[] eh_c_g;
    double[][] eh;
    double[][] am;

    double[][] DDtrans;// eh* eh^transpose

    public hyperParamsFinder(Map<String, HistoryMatchInfos> historyMap) {
        this.historyMap = historyMap;
        this.matchNumbers = historyMap.size();
        this.eh_c_g = new double[this.matchNumbers];
        this.eh = new double[3][this.matchNumbers];
        this.DDtrans = new double[3][3];
        am = new double[3][this.matchNumbers];

        int i = 0;
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            this.am[0][i] = home_o_rate;
            this.am[1][i] = away_d_rate;
            this.am[2][i] = 1.0;
            i++;
        }
    }

    /**
     * X * H = G = > X * H * H^T = G* H^T X = (G*H^T)*(H*H^T)^-1
     **/
    public void calcLeastSquareLinearAlgebraMethod() {
        double[][] HO_AD = new double[3][this.matchNumbers]; // size: 3 rows * match cols
        double[][] GOAL = new double[1][this.matchNumbers]; // cols = matchs
        int i = 0;
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            double home_goal = (double) entry.getValue().getFtHomeGoal();
            HO_AD[0][i] = home_o_rate;
            HO_AD[1][i] = away_d_rate;
            HO_AD[2][i] = 1.0;
            GOAL[0][i] = home_goal;
            i++;
        }

        Matrix hoad = new Matrix(HO_AD);
        Matrix goal = new Matrix(GOAL);

        Matrix hht = hoad.times(hoad.transpose());
        Matrix ght = goal.times(hoad.transpose());
        Matrix xBestFit = ght.times(hht.inverse());
        // System.out.println("best fit "+" best fit params are:" + xBestFit.get(0, 0)+" "+xBestFit.get(0, 1)+"
        // "+xBestFit.get(0, 2));

        double cost = 0;
        for (int k = 0; k < this.matchNumbers; k++) {
            cost += Math.abs(am[0][k] * xBestFit.get(0, 0) + am[1][k] * xBestFit.get(0, 1) + xBestFit.get(0, 2)
                            - GOAL[0][k]);
            // System.out.println(am[0][k] - am[1][k] + home_adj- homeGoal[k]);
        }
        System.out.println("least square cost: " + cost + " best fit params are:" + xBestFit.get(0, 0) + " "
                        + xBestFit.get(0, 1) + " " + xBestFit.get(0, 2));

    }



    public static double MEAN_ADJ = -1.605;

    public double getMeanGoalHome(double home_O, double away_d) {
        return home_O + away_d + MEAN_ADJ;
    }

    public void costG() {
        double cost = 0;
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            double home_goal = (double) entry.getValue().getFtHomeGoal();
            double home_goal_g = getMeanGoalHome(home_o_rate, away_d_rate);

            cost += Math.abs(home_goal - home_goal_g);
        }
        System.out.println("cost for homeAttack + awayDefense - MEAN_ADJ is :" + cost + " Geoff's adj param is: "
                        + MEAN_ADJ);
    }

    public void cost4Params() {
        double cost = 0;
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            double home_goal = (double) entry.getValue().getFtHomeGoal();
            // attackRatingFactor: 0.857, defenseRatingFactor: 1.146, homeAdjFactor: -1.311, awayAdjFactor: -1.655
            double home_goal_g = 0.857 * home_o_rate + 1.146 * away_d_rate - 1.311;
            cost += Math.abs(home_goal - home_goal_g);
        }
        System.out.println("cost for 4 params model is :" + cost + " 4 param are: "
                        + "attackRatingFactor: 0.857, defenseRatingFactor: 1.146, homeAdjFactor: -1.311, awayAdjFactor: -1.655");
    }


    public void calc() {
        double cost = -1;
        double homeGoal[] = new double[this.matchNumbers];
        int i = 0;
        // gradient with regard to a and b
        double home_goal = 0;
        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            home_goal = (double) entry.getValue().getFtHomeGoal();
            this.eh_c_g[i] = a * home_o_rate + b * away_d_rate + c - home_goal;
            homeGoal[i] = home_goal;
            this.eh[0][i] = home_o_rate;
            this.eh[1][i] = away_d_rate;
            this.eh[2][i] = 1.0; // c row
            i++;
        }

        for (int iter = 0; iter < 100; iter++) {
            cost = 0;
            double[] gradient_ab = new double[3];

            for (int j = 0; j < this.matchNumbers; j++) {
                gradient_ab[0] += eh_c_g[j] * eh[0][j];
                gradient_ab[1] += eh_c_g[j] * eh[1][j];
                gradient_ab[2] += eh_c_g[j] * eh[2][j];
            }

            this.a = a - gradient_ab[0] * stepLength;
            this.b = b - gradient_ab[1] * stepLength;
            this.c = c - gradient_ab[2] * stepLength;

            // optimise c
            // optimise c
            for (int k = 0; k < this.matchNumbers; k++)
                cost += Math.abs(eh_c_g[k]);
            // double sum_eh_c_g= DoubleStream.of(eh_c_g).parallel().sum();
            // cost = sum_eh_c_g + c - home_goal;
            System.out.println("current cost: " + cost + " best fit params are:" + a + " " + b + " " + c);
            // this.c = c - sum_eh_c_g*stepLength;
        }
    }

    /**
     * This method try to find the best params for the cost function
     * 
     * meanGoalsHome = homeAttack + awayDefense + (homeAdvantageRatingsAdjustment- MEAN_ADJ)
     * 
     * matrix am = [homeAttack, awayDefense, 1, 1]*matchNumbers rows
     * 
     * params to find is [1, 1, h_adj, mean_adj]^T
     * 
     * X*am - Home_Goals = diff_vector
     * 
     * => (X*A - G)(X*A - G)^T = X*A*A^T*X^T - X*A*G^T - G*A^T*X^T => d f(X)/ dx = (A*A^T)*X^T - 2*A*G^T
     * 
     */
    public void calc1() {
        double home_adj = 0.0; // we are treating (homeAdvantageRatingsAdjustment- MEAN_ADJ) to be this home_adj

        double cost = 0;
        double homeGoal[] = new double[this.matchNumbers];
        am = new double[3][this.matchNumbers];

        int i = 0;
        double home_goal = 0;

        for (Map.Entry<String, HistoryMatchInfos> entry : historyMap.entrySet()) {
            double home_o_rate = (double) entry.getValue().getORateHome();
            double away_d_rate = (double) entry.getValue().getDRateAway();
            home_goal = (double) entry.getValue().getFtHomeGoal();
            this.eh_c_g[i] = home_o_rate + away_d_rate;

            homeGoal[i] = home_goal;
            // filling the o d array
            this.am[0][i] = home_o_rate;
            this.am[1][i] = away_d_rate;
            this.am[2][i] = 1.0;
            i++;
        }

        double[][] AAT = multyTranspose(this.am); // A * A^Transpose
        double[] GAT = multyTwoMatrix(homeGoal, this.am, true); // Goal * A ^ T

        for (int iter = 0; iter < 100; iter++) {
            double[] grad = new double[3];
            /**
             * Grad = AAT*X^T - 2*GAT*X^T
             */
            double[] currentX = new double[3];
            currentX[0] = 1.0;
            currentX[1] = 1.0;
            currentX[2] = home_adj;
            double[] AATXT = multyTwoMatrix(AAT, currentX, true);

            for (int k = 0; k < grad.length; k++)
                grad[k] = AATXT[k] - GAT[k];


            home_adj = home_adj - grad[2] * stepLength;
            cost = 0;
            for (int k = 0; k < this.matchNumbers; k++) {
                cost += Math.abs(am[0][k] + am[1][k] + home_adj - homeGoal[k]);
                // System.out.println(am[0][k] - am[1][k] + home_adj- homeGoal[k]);
            }
            // this.c = c - sum_eh_c_g*stepLength;
        }
        System.out.println("gradient descent cost: " + cost + " best fit params are:" + home_adj);
    }



    private double[] multyTwoMatrix(double[][] aAT, double[] x, boolean secondTranspose) {
        double[] outMatrix = null;
        if (secondTranspose) {
            outMatrix = new double[aAT.length];
            for (int i = 0; i < outMatrix.length; i++) {
                double temp = 0;
                for (int j = 0; j < x.length; j++) {
                    temp += x[j] * aAT[i][j];
                    // System.out.println(i + " "+ j);
                }
                outMatrix[i] = temp;
            }
        }
        return outMatrix;
    }

    private double[] multyTwoMatrix(double[] homeGoal, double[][] am2, boolean secondTranspose) {
        /**
         * Home goal is a row vector
         */
        double[] outMatrix = null;
        if (secondTranspose) {
            outMatrix = new double[am2.length];
            for (int i = 0; i < outMatrix.length; i++) {
                double temp = 0;
                for (int j = 0; j < am2[0].length; j++) {
                    temp += homeGoal[j] * am2[i][j];
                    // System.out.println(i + " "+ j);
                }
                outMatrix[i] = temp;
            }
        }


        return outMatrix;
    }



    private double[][] multyTranspose(double[][] dDtrans2) {
        // Return a matrix = in matrix * in ^ T
        double[][] returnMatrix = new double[dDtrans2.length][dDtrans2.length];
        int width = dDtrans2[0].length;

        for (int row = 0; row < dDtrans2.length; row++)
            for (int col = 0; col < dDtrans2.length; col++) {
                double value = 0;
                for (int i = 0; i < width; i++)
                    value += dDtrans2[row][i] * dDtrans2[col][i];

                returnMatrix[row][col] = value;
            }
        return returnMatrix;
    }



}
