package ats.algo.sport.tennis;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;

public class LastIncidentDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private TennisMatchIncident lastIncident;
    private PlayerId servingPlayer;
    private TeamId servingTeam;
    private boolean powerPoint;
    private int pointNo;
    private int serveNo;
    private TennisPointResult pointResult;

    public LastIncidentDetails() {

    }

    public LastIncidentDetails copy() {
        LastIncidentDetails copy = new LastIncidentDetails();
        copy.setLastIncident(this.getLastIncident());
        copy.setServingPlayer(this.getServingPlayer());
        copy.setServingTeam(this.getServingTeam());
        copy.setPowerPoint(this.isPowerPoint());
        copy.setPointNo(this.getPointNo());
        copy.setServeNo(this.getServeNo());
        copy.setPointResult(this.getPointResult());
        return copy;
    }



    public TennisPointResult getPointResult() {
        return pointResult;
    }

    public void setPointResult(TennisPointResult pointResult) {
        this.pointResult = pointResult;
    }

    public int getServeNo() {
        return serveNo;
    }

    public void setServeNo(int serveNo) {
        this.serveNo = serveNo;
    }

    public int getPointNo() {
        return pointNo;
    }

    public void setPointNo(int pointNo) {
        this.pointNo = pointNo;
    }

    public TennisMatchIncident getLastIncident() {
        return lastIncident;
    }

    public void setLastIncident(TennisMatchIncident lastIncident) {
        this.lastIncident = lastIncident;
    }

    public PlayerId getServingPlayer() {
        return servingPlayer;
    }

    public void setServingPlayer(PlayerId servingPlayer) {
        this.servingPlayer = servingPlayer;
    }

    public TeamId getServingTeam() {
        return servingTeam;
    }

    public void setServingTeam(TeamId servingTeam) {
        this.servingTeam = servingTeam;
    }

    public boolean isPowerPoint() {
        return powerPoint;
    }

    public void setPowerPoint(boolean powerPoint) {
        this.powerPoint = powerPoint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lastIncident == null) ? 0 : lastIncident.hashCode());
        result = prime * result + pointNo;
        result = prime * result + ((pointResult == null) ? 0 : pointResult.hashCode());
        result = prime * result + (powerPoint ? 1231 : 1237);
        result = prime * result + serveNo;
        result = prime * result + ((servingPlayer == null) ? 0 : servingPlayer.hashCode());
        result = prime * result + ((servingTeam == null) ? 0 : servingTeam.hashCode());
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
        LastIncidentDetails other = (LastIncidentDetails) obj;
        if (lastIncident == null) {
            if (other.lastIncident != null)
                return false;
        } else if (!lastIncident.equals(other.lastIncident))
            return false;
        if (pointNo != other.pointNo)
            return false;
        if (pointResult != other.pointResult)
            return false;
        if (powerPoint != other.powerPoint)
            return false;
        if (serveNo != other.serveNo)
            return false;
        if (servingPlayer != other.servingPlayer)
            return false;
        if (servingTeam != other.servingTeam)
            return false;
        return true;
    }



}
