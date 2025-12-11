package ats.algo.algomanager;

import java.io.Serializable;

import ats.algo.genericsupportfunctions.CopySerializableObject;

/**
 * Container for holding state information about an event for use by AlgoManager in jumping to a particular point in the
 * match.
 * 
 * @author Geoff
 *
 */
public class EventStateBlob implements Serializable {
    private static final long serialVersionUID = 1L;

    private String incidentId;

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public EventStateBlob copy() {
        EventStateBlob cc = CopySerializableObject.copy(this);
        return cc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incidentId == null) ? 0 : incidentId.hashCode());
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
        EventStateBlob other = (EventStateBlob) obj;
        if (incidentId == null) {
            if (other.incidentId != null)
                return false;
        } else if (!incidentId.equals(other.incidentId))
            return false;
        return true;
    }



}
