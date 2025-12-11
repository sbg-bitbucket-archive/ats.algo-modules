package ats.algo.inplayGS;

import java.util.Arrays;

public class OddsLadder {

    private double[] oddsLadder;

    public OddsLadder() {};


    public double[] getOddsLadder() {
        return oddsLadder;
    }


    public void setOddsLadder(double[] oddsLadder) {
        this.oddsLadder = oddsLadder;
    }


    public OddsLadder(double[] oddsLadder) {
        super();
        this.oddsLadder = oddsLadder;
    }

    public double getPriceFromOddsLadder(double rawPrice) {
        if (rawPrice < 1)
            throw new IllegalArgumentException("Invalid input price:" + rawPrice);
        if (rawPrice <= oddsLadder[0])
            return oddsLadder[0];
        for (int i = 0; i < oddsLadder.length - 1; i++) {
            if (rawPrice > oddsLadder[i] && rawPrice <= oddsLadder[i + 1])
                return oddsLadder[i + 1];
        }
        return oddsLadder[oddsLadder.length - 1];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(oddsLadder);
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
        OddsLadder other = (OddsLadder) obj;
        if (!Arrays.equals(oddsLadder, other.oddsLadder))
            return false;
        return true;
    }



}
