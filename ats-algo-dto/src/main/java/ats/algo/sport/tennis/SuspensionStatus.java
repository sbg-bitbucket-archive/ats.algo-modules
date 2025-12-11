package ats.algo.sport.tennis;

import java.io.Serializable;

import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class SuspensionStatus implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean delaySuspension;
    private TennisMatchIncidentType reasonOfDelaySuspension;
    private long timeStampOfIncident;
    private boolean cacheSuspensionStatus;
    private static final String PROPERTY_FOR_CACHE_SUSPENSION_STATUS = "cacheSuspensionStatus";


    public SuspensionStatus() {
        this.delaySuspension = false;
        this.timeStampOfIncident = -1;
        this.reasonOfDelaySuspension = null;
        this.cacheSuspensionStatus = false;
        String clients = System.getProperty(PROPERTY_FOR_CACHE_SUSPENSION_STATUS);
        if (clients != null)
            if (clients.toLowerCase().equals("true"))
                cacheSuspensionStatus = true;
    }

    /*
     * Purely for unit tests
     */
    public boolean isCacheSuspensionStatus() {
        return cacheSuspensionStatus;
    }

    /*
     * Purely for unit tests
     */
    public void setCacheSuspensionStatus(boolean cacheSuspensionStatus) {
        this.cacheSuspensionStatus = cacheSuspensionStatus;
    }

    public void updateSuspension(TennisMatchIncident matchIncident) {
        TennisMatchIncidentType tennisIncidentType = ((TennisMatchIncident) matchIncident).getIncidentSubType();
        if (!cacheSuspensionStatus) {
            delaySuspension = false;
            timeStampOfIncident = -1;
        }
        switch (tennisIncidentType) {
            case RAIN:
                reasonOfDelaySuspension = TennisMatchIncidentType.RAIN;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            case TOILET_BREAK:
                reasonOfDelaySuspension = TennisMatchIncidentType.TOILET_BREAK;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            case HEAT_DELAY:
                reasonOfDelaySuspension = TennisMatchIncidentType.HEAT_DELAY;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            case MEDICAL_TIMEOUT:
                reasonOfDelaySuspension = TennisMatchIncidentType.MEDICAL_TIMEOUT;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            case ON_COURT_COACHING:
                reasonOfDelaySuspension = TennisMatchIncidentType.ON_COURT_COACHING;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            case CHALLENGE:// FIXME: LOGIC INCOMPLETED
                reasonOfDelaySuspension = TennisMatchIncidentType.CHALLENGE;
                delaySuspension = true;
                timeStampOfIncident = System.currentTimeMillis();
                break;
            default:
                break;
        }

    }

    public boolean isDelaySuspension() {
        return delaySuspension;
    }

    public void setDelaySuspension(boolean delaySuspension) {
        this.delaySuspension = delaySuspension;
    }

    public TennisMatchIncidentType getReasonOfDelaySuspension() {
        return reasonOfDelaySuspension;
    }

    public void setReasonOfDelaySuspension(TennisMatchIncidentType reasonOfDelaySuspension) {
        this.reasonOfDelaySuspension = reasonOfDelaySuspension;
    }

    public long getTimeStampOfIncident() {
        return timeStampOfIncident;
    }

    public void setTimeStampOfIncident(long timeStampOfIncident) {
        this.timeStampOfIncident = timeStampOfIncident;
    }


    public SuspensionStatus copy() {
        SuspensionStatus copy = new SuspensionStatus();
        copy.setDelaySuspension(this.isDelaySuspension());
        copy.setReasonOfDelaySuspension(this.getReasonOfDelaySuspension());
        copy.setTimeStampOfIncident(this.getTimeStampOfIncident());
        return copy;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (delaySuspension ? 1231 : 1237);
        result = prime * result + ((reasonOfDelaySuspension == null) ? 0 : reasonOfDelaySuspension.hashCode());
        result = prime * result + (int) (timeStampOfIncident ^ (timeStampOfIncident >>> 32));
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
        SuspensionStatus other = (SuspensionStatus) obj;
        if (delaySuspension != other.delaySuspension)
            return false;
        if (reasonOfDelaySuspension != other.reasonOfDelaySuspension)
            return false;
        if (timeStampOfIncident != other.timeStampOfIncident)
            return false;
        return true;
    }



}
