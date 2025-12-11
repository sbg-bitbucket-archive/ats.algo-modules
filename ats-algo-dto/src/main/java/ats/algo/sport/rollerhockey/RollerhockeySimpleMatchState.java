package ats.algo.sport.rollerhockey;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;

public class RollerhockeySimpleMatchState extends SimpleMatchState {

    private static final long serialVersionUID = 1L;
    private String info;

    @JsonCreator
    public RollerhockeySimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted)

    {
        super(preMatch, matchCompleted);
        info = "simpleMatchState not yet implemented for" + this.getClass().getSimpleName();
        // TODO - replace info property by the properties needed for this sport.
        // c.f. e.g. TennisSimpleMatchState
    }

    public RollerhockeySimpleMatchState() {
        super();
    }

    public String getInfo() {
        return info;
    }
}


