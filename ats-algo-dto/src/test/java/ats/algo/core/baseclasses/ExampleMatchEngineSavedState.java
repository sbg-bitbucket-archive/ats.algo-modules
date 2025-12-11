package ats.algo.core.baseclasses;

import java.io.Serializable;

public class ExampleMatchEngineSavedState extends MatchEngineSavedState implements Serializable {

    private static final long serialVersionUID = 1L;

    private MatchIncident matchIncident;
    private MatchFormat matchFormat;

    MatchIncident getMatchIncident() {
        return matchIncident;
    }

    void setMatchIncident(MatchIncident matchIncident) {
        this.matchIncident = matchIncident;
    }

    MatchFormat getMatchFormat() {
        return matchFormat;
    }

    void setMatchFormat(MatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchIncident == null) ? 0 : matchIncident.hashCode());
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
        ExampleMatchEngineSavedState other = (ExampleMatchEngineSavedState) obj;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (matchIncident == null) {
            if (other.matchIncident != null)
                return false;
        } else if (!matchIncident.equals(other.matchIncident))
            return false;
        return true;
    }


}
