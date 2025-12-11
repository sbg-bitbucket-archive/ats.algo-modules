package ats.algo.sport.darts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * holds state info about a set of three darts thrown by a single player
 * 
 * @author Geoff
 *
 */
public class ThreeDartSet implements Serializable {
    private static final long serialVersionUID = 1L;
    public int noDartsThrown; // 0,1,2 or 3
    public DartTarget[] DartHit;

    public ThreeDartSet() {
        noDartsThrown = 0;
        DartHit = new DartTarget[3];
        for (int i = 0; i < 3; i++)
            DartHit[i] = new DartTarget(0, 0);
    }

    /**
     * returns true if hit T20 with the most recent dart
     * 
     * @param n dart no n (in range 1-3)
     * @return
     */
    @JsonIgnore
    public boolean hitT20(int n) {
        return (DartHit[n - 1].multiplier == 3) && (DartHit[n - 1].no == 20);
    }

    /**
     * makes a copy of itself
     * 
     * @return
     */
    public ThreeDartSet copy() {
        ThreeDartSet dest = new ThreeDartSet();
        dest.noDartsThrown = noDartsThrown;
        for (int i = 0; i < 3; i++) {
            dest.DartHit[i].multiplier = DartHit[i].multiplier;
            dest.DartHit[i].no = DartHit[i].no;
        }
        return dest;
    }

    /**
     * stores what this dart actually hit
     * 
     * @param dartNo 1-3
     * @param actual
     */
    @JsonIgnore
    public void setActual(int dartNo, DartTarget actual) {
        DartHit[dartNo - 1].multiplier = actual.multiplier;
        DartHit[dartNo - 1].no = actual.no;
        noDartsThrown = dartNo;
    }

    /**
     * returns the actual this dart hit
     * 
     * @param dartNo 1-3
     * @return
     */
    @JsonIgnore
    public DartTarget getActual(int dartNo) {
        DartTarget dt = new DartTarget(DartHit[dartNo - 1].multiplier, DartHit[dartNo - 1].no);
        return dt;
    }

    /**
     * returns true if scored 180 with this three dart set;
     * 
     * @return
     */
    @JsonIgnore
    public boolean is180() {
        if (noDartsThrown < 3)
            return false;
        int totalScore = 0;
        for (int i = 1; i <= 3; i++) {
            DartTarget r = getActual(i);
            totalScore += r.multiplier * r.no;
        }
        return (totalScore == 180);
    }

    /**
     * returns the total score for this three dart set
     * 
     * @return
     */
    @JsonIgnore
    public Object getTotal() {
        int totalScore = 0;
        for (int i = 1; i <= noDartsThrown; i++) {
            DartTarget r = getActual(i);
            totalScore += r.multiplier * r.no;
        }
        return totalScore;
    }
}

