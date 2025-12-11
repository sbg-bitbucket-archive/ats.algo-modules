package ats.algo.core.common;

import ats.algo.core.baseclasses.MatchIncident;

class IncidentWrapperForTest {
    private MatchIncident matchIncident;

    public MatchIncident getMatchIncident() {
        return matchIncident;
    }

    public void setMatchIncident(MatchIncident matchIncident) {
        this.matchIncident = matchIncident;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchIncident == null) ? 0 : matchIncident.hashCode());
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
        IncidentWrapperForTest other = (IncidentWrapperForTest) obj;
        if (matchIncident == null) {
            if (other.matchIncident != null)
                return false;
        } else if (!matchIncident.equals(other.matchIncident))
            return false;
        return true;
    }


}
