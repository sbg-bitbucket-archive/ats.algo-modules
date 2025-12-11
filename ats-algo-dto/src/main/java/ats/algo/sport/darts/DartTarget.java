package ats.algo.sport.darts;

import java.io.Serializable;

/**
 * Defines what was aimed at or hit
 * 
 * @author Geoff
 *
 */
public class DartTarget implements Serializable {
    private static final long serialVersionUID = 1L;
    public int multiplier; // 0, 1,2 or 3 Multiplier =0 means bounce out incident.
    public int no; // -3(Bounce out first two darts), -2(Bounce out second darts), -1(Bounce out first darts), 0-20 or
                   // 25

    public DartTarget() {

    }

    public DartTarget(int multiplier, int no) {
        if (multiplier > 3 || multiplier < 0 || no < -3 || no > 25)
            throw new IllegalArgumentException("multiplier not in range 0-3");
        this.multiplier = multiplier;
        this.no = no;
    }
}
