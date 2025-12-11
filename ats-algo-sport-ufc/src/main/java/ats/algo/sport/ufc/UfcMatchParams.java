package ats.algo.sport.ufc;

import java.io.Serializable;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParamType;
import ats.algo.core.baseclasses.AlgoMatchParams;

/**
 * provides an example of a AlgoMatchParams class that implements parameters roundAToWin and roundBToWin
 *
 * @author Rob
 *
 */
public class UfcMatchParams extends AlgoMatchParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private MatchParam round1AToWin;
    private MatchParam round2AToWin;
    private MatchParam round1BToWin;
    private MatchParam round2BToWin;
    private MatchParam round3AToWin;
    private MatchParam round4AToWin;
    private MatchParam round3BToWin;
    private MatchParam round4BToWin;
    private MatchParam round5AToWin;
    private MatchParam decisionAToWin;
    private MatchParam round5BToWin;
    private MatchParam decisionBToWin;
    private MatchParam draw;
    private int roundsPerMatch;
    /*
     * define the standard getters and setters for the params you define so they can be used elsewhere within your
     * model. getters and setters should be either package level access only or marked with @JsonIgnore or both
     */


    /**
     * Constructor. Should create instances of each param you want to define
     */
    public UfcMatchParams() {
        this(5);// Default rounds per match set to 5
    }

    public UfcMatchParams(int roundsPerMatch) {
        super();
        this.roundsPerMatch = roundsPerMatch;
        setDefaultParams();
    }


    public MatchParam getRound1AToWin() {
        return round1AToWin;
    }


    public void setRound1AToWin(MatchParam round1aToWin) {
        round1AToWin = round1aToWin;
    }


    public MatchParam getRound2AToWin() {
        return round2AToWin;
    }


    public void setRound2AToWin(MatchParam round2aToWin) {
        round2AToWin = round2aToWin;
    }


    public MatchParam getRound1BToWin() {
        return round1BToWin;
    }


    public void setRound1BToWin(MatchParam round1bToWin) {
        round1BToWin = round1bToWin;
    }


    public MatchParam getRound2BToWin() {
        return round2BToWin;
    }


    public void setRound2BToWin(MatchParam round2bToWin) {
        round2BToWin = round2bToWin;
    }


    public MatchParam getRound3AToWin() {
        return round3AToWin;
    }


    public void setRound3AToWin(MatchParam round3aToWin) {
        round3AToWin = round3aToWin;
    }


    public MatchParam getRound4AToWin() {
        return round4AToWin;
    }


    public void setRound4AToWin(MatchParam round4aToWin) {
        round4AToWin = round4aToWin;
    }


    public MatchParam getRound3BToWin() {
        return round3BToWin;
    }


    public void setRound3BToWin(MatchParam round3bToWin) {
        round3BToWin = round3bToWin;
    }


    public MatchParam getRound4BToWin() {
        return round4BToWin;
    }


    public void setRound4BToWin(MatchParam round4bToWin) {
        round4BToWin = round4bToWin;
    }


    public MatchParam getRound5AToWin() {
        return round5AToWin;
    }


    public void setRound5AToWin(MatchParam round5aToWin) {
        round5AToWin = round5aToWin;
    }


    public MatchParam getDecisionAToWin() {
        return decisionAToWin;
    }


    public void setDecisionAToWin(MatchParam decisionAToWin) {
        this.decisionAToWin = decisionAToWin;
    }


    public MatchParam getRound5BToWin() {
        return round5BToWin;
    }


    public void setRound5BToWin(MatchParam round5bToWin) {
        round5BToWin = round5bToWin;
    }


    public MatchParam getDecisionBToWin() {
        return decisionBToWin;
    }


    public void setDecisionBToWin(MatchParam decisionBToWin) {
        this.decisionBToWin = decisionBToWin;
    }


    public MatchParam getDraw() {
        return draw;
    }


    public void setDraw(MatchParam draw) {
        this.draw = draw;
    }


    @Override
    public void setDefaultParams(MatchFormat matchFormat) {
        /*
         * add any entries you need for params that you define. In this example default params do not depend on the
         * match format
         */
        setDefaultParams();
    }

    private void setDefaultParams() {
        switch (roundsPerMatch) {
            case 5:
                round4AToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.06, 0.01, 0.0, 1, true);
                round5AToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.05, 0.01, 0.0, 1, true);
                round4BToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.02, 0.01, 0.0, 1, true);
                round5BToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.01, 0.01, 0.0, 1, true);
            case 3:
                decisionAToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.4, 0.01, 0.0, 1, true);
                round1AToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.09, 0.01, 0.0, 1, true);
                round2AToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.08, 0.01, 0.0, 1, true);
                round3AToWin = new MatchParam(MatchParamType.A, MarketGroup.NOT_SPECIFIED, 0.07, 0.01, 0.0, 1, true);
                round1BToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.02, 0.01, 0.0, 1, true);
                round2BToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.02, 0.01, 0.0, 1, true);
                round3BToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.02, 0.01, 0.0, 1, true);
                decisionBToWin = new MatchParam(MatchParamType.B, MarketGroup.NOT_SPECIFIED, 0.157, 0.001, 0.0, 1,
                                true);
                draw = new MatchParam(MatchParamType.BOTHCOMBINED, MarketGroup.NOT_SPECIFIED, 0.003, 0.001, 0.0, 1,
                                true);
        }
        updateParamMap();
    }

    protected void updateParamMap() {
        super.updateParamMap();
        super.paramMap.put("round1AToWin", round1AToWin);
        super.paramMap.put("round2AToWin", round2AToWin);
        super.paramMap.put("round3AToWin", round3AToWin);
        if (roundsPerMatch == 5) {
            super.paramMap.put("round4AToWin", round4AToWin);
            super.paramMap.put("round5AToWin", round5AToWin);
        }
        super.paramMap.put("decisionAToWin", decisionAToWin);
        super.paramMap.put("round1BToWin", round1BToWin);
        super.paramMap.put("round2BToWin", round2BToWin);
        super.paramMap.put("round3BToWin", round3BToWin);
        if (roundsPerMatch == 5) {
            super.paramMap.put("round4BToWin", round4BToWin);
            super.paramMap.put("round5BToWin", round5BToWin);
        }
        super.paramMap.put("decisionBToWin", decisionBToWin);
        super.paramMap.put("draw", draw);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((decisionAToWin == null) ? 0 : decisionAToWin.hashCode());
        result = prime * result + ((decisionBToWin == null) ? 0 : decisionBToWin.hashCode());
        result = prime * result + ((draw == null) ? 0 : draw.hashCode());
        result = prime * result + ((round1AToWin == null) ? 0 : round1AToWin.hashCode());
        result = prime * result + ((round1BToWin == null) ? 0 : round1BToWin.hashCode());
        result = prime * result + ((round2AToWin == null) ? 0 : round2AToWin.hashCode());
        result = prime * result + ((round2BToWin == null) ? 0 : round2BToWin.hashCode());
        result = prime * result + ((round3AToWin == null) ? 0 : round3AToWin.hashCode());
        result = prime * result + ((round3BToWin == null) ? 0 : round3BToWin.hashCode());
        result = prime * result + ((round4AToWin == null) ? 0 : round4AToWin.hashCode());
        result = prime * result + ((round4BToWin == null) ? 0 : round4BToWin.hashCode());
        result = prime * result + ((round5AToWin == null) ? 0 : round5AToWin.hashCode());
        result = prime * result + ((round5BToWin == null) ? 0 : round5BToWin.hashCode());
        result = prime * result + roundsPerMatch;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UfcMatchParams other = (UfcMatchParams) obj;
        if (decisionAToWin == null) {
            if (other.decisionAToWin != null)
                return false;
        } else if (!decisionAToWin.equals(other.decisionAToWin))
            return false;
        if (decisionBToWin == null) {
            if (other.decisionBToWin != null)
                return false;
        } else if (!decisionBToWin.equals(other.decisionBToWin))
            return false;
        if (draw == null) {
            if (other.draw != null)
                return false;
        } else if (!draw.equals(other.draw))
            return false;
        if (round1AToWin == null) {
            if (other.round1AToWin != null)
                return false;
        } else if (!round1AToWin.equals(other.round1AToWin))
            return false;
        if (round1BToWin == null) {
            if (other.round1BToWin != null)
                return false;
        } else if (!round1BToWin.equals(other.round1BToWin))
            return false;
        if (round2AToWin == null) {
            if (other.round2AToWin != null)
                return false;
        } else if (!round2AToWin.equals(other.round2AToWin))
            return false;
        if (round2BToWin == null) {
            if (other.round2BToWin != null)
                return false;
        } else if (!round2BToWin.equals(other.round2BToWin))
            return false;
        if (round3AToWin == null) {
            if (other.round3AToWin != null)
                return false;
        } else if (!round3AToWin.equals(other.round3AToWin))
            return false;
        if (round3BToWin == null) {
            if (other.round3BToWin != null)
                return false;
        } else if (!round3BToWin.equals(other.round3BToWin))
            return false;
        if (round4AToWin == null) {
            if (other.round4AToWin != null)
                return false;
        } else if (!round4AToWin.equals(other.round4AToWin))
            return false;
        if (round4BToWin == null) {
            if (other.round4BToWin != null)
                return false;
        } else if (!round4BToWin.equals(other.round4BToWin))
            return false;
        if (round5AToWin == null) {
            if (other.round5AToWin != null)
                return false;
        } else if (!round5AToWin.equals(other.round5AToWin))
            return false;
        if (round5BToWin == null) {
            if (other.round5BToWin != null)
                return false;
        } else if (!round5BToWin.equals(other.round5BToWin))
            return false;
        if (roundsPerMatch != other.roundsPerMatch)
            return false;
        return true;
    }

}
