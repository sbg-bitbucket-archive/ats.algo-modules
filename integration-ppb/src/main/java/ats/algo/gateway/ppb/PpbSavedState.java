package ats.algo.gateway.ppb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ats.algo.requestresponse.ppb.PpbTennisPointDetails;
import ats.algo.sport.tennis.TennisMatchState;


public class PpbSavedState implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<PpbTennisPointDetails> points;
    private String ppbState;

    public PpbSavedState() {
        points = new ArrayList<PpbTennisPointDetails>();
    }



    /**
     * adds a matchIncident to the matchIncidents list
     * 
     * @param matchIncident
     */
    public void addPoint(TennisMatchState matchstate) {
        PpbTennisPointDetails point = PpbTennisPointDetails.generatePpbTennisPointDetails(matchstate);
        if (point != null)
            points.add(point);
    }



    public List<PpbTennisPointDetails> getPoints() {
        return points;
    }

    public void setPoints(List<PpbTennisPointDetails> points) {
        this.points = points;
    }

    public String getPpbState() {
        return ppbState;
    }

    public void setPpbState(String ppbState) {
        this.ppbState = ppbState;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((points == null) ? 0 : points.hashCode());
        result = prime * result + ((ppbState == null) ? 0 : ppbState.hashCode());
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
        PpbSavedState other = (PpbSavedState) obj;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        if (ppbState == null) {
            if (other.ppbState != null)
                return false;
        } else if (!ppbState.equals(other.ppbState))
            return false;
        return true;
    }



    @Override
    public String toString() {
        return "PpbSavedState [points=" + points + ", ppbState=" + ppbState + "]";
    }



}
