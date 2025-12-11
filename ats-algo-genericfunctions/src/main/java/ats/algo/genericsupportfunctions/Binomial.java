package ats.algo.genericsupportfunctions;

public class Binomial {

    private int[][] coeff;

    public static final int MAX_N = 20;

    public Binomial(int maxN) {
        coeff = new int[maxN + 1][maxN + 1];
        coeff[0][0] = 1;
        for (int i = 1; i <= maxN; i++) {
            coeff[i][0] = 1;
            coeff[i][i] = 1;
        }
        for (int n = 1; n <= maxN; n++)
            for (int k = 1; k < n; k++)
                coeff[n][k] = coeff[n - 1][k - 1] + coeff[n - 1][k];
    }

    /**
     * calculates the binomial coefficient = n!/r!(n-r)! Assumes n, r are small enough for result to fit into an integer
     * 
     * @param n
     * @param r
     * @return
     */
    public int coeff(int n, int r) {
        return coeff[n][r];
    }

}
