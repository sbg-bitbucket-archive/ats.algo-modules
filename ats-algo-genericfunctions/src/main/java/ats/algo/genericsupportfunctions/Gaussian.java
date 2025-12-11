package ats.algo.genericsupportfunctions;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * defines a normally distributed RV
 * 
 * @author Geoff
 *
 */
public class Gaussian implements Serializable {

    private static final long serialVersionUID = 1L;

    private double mean;
    private double stdDevn;
    private double bias;
    private transient boolean applyBias;

    // For Json
    public Gaussian() {
        this.bias = 0;
        this.applyBias = false;
    }

    /**
     *
     * @param mean
     * @param stdDevn
     */
    public Gaussian(double mean, double stdDevn) {
        this.mean = mean;
        this.stdDevn = stdDevn;
        this.bias = 0;
        this.applyBias = false;
    }

    /**
     * returns the mean value of the Gaussian distribution, adding the bias if necessary
     * 
     * @return
     */
    public double getMean() {
        return mean;
    }

    /**
     * returns the mean value of the Gaussian distribution, adding the bias if necessary
     * 
     * @return
     */
    @JsonIgnore
    public double getBiasAdjustedMean() {
        double adjustedMean = mean;
        if (applyBias)
            adjustedMean += bias;
        return adjustedMean;
    }

    /**
     * sets the mean value of the Gaussian distribution
     * 
     * @param mean
     */
    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * returns the standard deviation of the gaussian distribution
     * 
     * @return
     */
    public double getStdDevn() {
        return stdDevn;
    }


    /**
     * gets the bias which may be added to the mean when getting nextRandom
     * 
     * @return
     */
    public double getBias() {
        return bias;
    }

    /**
     * sets the bias which may be added to the mean when getting nextRandom
     * 
     * @param bias
     */
    public void setBias(double bias) {
        this.bias = bias;
    }

    @JsonIgnore
    public boolean isApplyBias() {
        return applyBias;
    }

    @JsonIgnore
    public void setApplyBias(boolean applyBias) {
        this.applyBias = applyBias;
    }

    @JsonIgnore
    public void setProperties(double mean, double stdDevn, double bias) {
        this.mean = mean;
        this.stdDevn = stdDevn;
        this.bias = bias;
    }

    /**
     * sets the standard deviation of the Gaussian distribution
     * 
     * @param stdDevn
     */
    public void setStdDevn(double stdDevn) {
        this.stdDevn = stdDevn;
    }

    @JsonIgnore
    public void setEqualTo(Gaussian other) {
        this.mean = other.getMean();
        this.stdDevn = other.getStdDevn();
        this.bias = other.getBias();
        this.applyBias = other.isApplyBias();
    }

    /**
     * returns a copy of itself
     * 
     * @return
     */
    public Gaussian copy() {
        Gaussian cc = new Gaussian();
        cc.setEqualTo(this);
        return cc;
    }

    /**
     * returns a number distributed according to this distn
     * 
     * @return
     */
    public double nextRandom() {
        double mean = getBiasAdjustedMean();
        if (stdDevn == 0.0)
            return mean;
        else
            return RandomNoGenerator.nextNormal(mean, stdDevn);
    }

    /**
     * return standard Gaussian pdf
     * 
     * @param x
     * @return
     */
    public double pdf(double x) {
        double z = (x - mean) / stdDevn;
        return Math.exp(-z * z / 2) / Math.sqrt(2 * Math.PI);
    }

    /**
     * return standard gaussian cdf
     * 
     * @param x
     *
     */
    public double cdf(double x) {
        double z = (x - mean) / stdDevn;
        if (z < -8.0)
            return 0.0;
        if (z > 8.0)
            return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * Math.exp(-z * z / 2) / Math.sqrt(2 * Math.PI);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (applyBias ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(bias);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mean);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stdDevn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Gaussian other = (Gaussian) obj;

        if (Double.doubleToLongBits(bias) != Double.doubleToLongBits(other.bias))
            return false;
        if (Double.doubleToLongBits(mean) != Double.doubleToLongBits(other.mean))
            return false;
        if (Double.doubleToLongBits(stdDevn) != Double.doubleToLongBits(other.stdDevn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[mean=" + mean + ", stdDevn=" + stdDevn + ", bias=" + bias + "]";
    }



}
