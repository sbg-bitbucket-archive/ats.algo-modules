package ats.algo.sport.outrights;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class OutrightsSimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;

    private OutrightsMatchPeriod matchPeriod;


    @JsonCreator
    public OutrightsSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("matchPeriod") OutrightsMatchPeriod matchPeriod) {

        super(preMatch, matchCompleted);
        this.matchPeriod = matchPeriod;

    }

    public OutrightsSimpleMatchState() {
        super();
        matchPeriod = OutrightsMatchPeriod.PRE_COMPETITION;
    }



    public OutrightsMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    public void setMatchPeriod(OutrightsMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchPeriod == null) ? 0 : matchPeriod.hashCode());
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
        OutrightsSimpleMatchState other = (OutrightsSimpleMatchState) obj;
        if (matchPeriod != other.matchPeriod)
            return false;
        return true;
    }

}

