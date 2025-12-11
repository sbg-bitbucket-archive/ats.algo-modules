package ats.algo.requestresponse.ppb;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.LastIncidentDetails;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.algo.sport.tennis.TennisMatchState;

public class PpbTennisPointDetails implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int pointNumber;
    private String servingTeam;
    private boolean serverWins;
    private int serveNumber;
    private String pointResult;
    private boolean powerPoint;

    // constructor for testing regard
    public PpbTennisPointDetails() {
        this.pointNumber = 1;
        this.servingTeam = "a";
        this.serverWins = true;
        this.serveNumber = 1;
        this.pointResult = "rally";
        this.powerPoint = false;

    }

    public static PpbTennisPointDetails generatePpbTennisPointDetails(TennisMatchState tennisMatchState) {
        /*
         * TennisMatchState need to record last point result "ace","rally","double fault"
         * 
         * PpbTennisPointDetails is refering to the points details
         * 
         */
        PpbTennisPointDetails.Builder builder = new PpbTennisPointDetails.Builder();
        LastIncidentDetails lastIncidentDetails = tennisMatchState.getLastIncidentDetails();
        if (lastIncidentDetails.getLastIncident() == null) {
            return null;// if it is not a point won incident
        } else if (lastIncidentDetails.getLastIncident().getIncidentSubType() == TennisMatchIncidentType.POINT_WON
                        || lastIncidentDetails.getLastIncident()
                                        .getIncidentSubType() == TennisMatchIncidentType.PENALTY_POINT_WON) {

            builder.pointNumber(lastIncidentDetails.getPointNo());

            switch (lastIncidentDetails.getServingTeam()) {
                case A:
                    builder.servingTeam("a");
                    break;
                case B:
                    builder.servingTeam("b");
                    break;
                default:
                    if (tennisMatchState.getGameNo() == 1) {
                        lastIncidentDetails.setServingTeam(
                                        lastIncidentDetails.getLastIncident().getServerSideAtStartOfMatch());
                        switch (lastIncidentDetails.getServingTeam()) {
                            case A:
                                builder.servingTeam("a");
                                break;
                            case B:
                                builder.servingTeam("b");
                                break;
                            default:
                                throw new IllegalArgumentException(
                                                "Serving team still not known - something very wrong.");
                        }

                    } else {
                        throw new IllegalArgumentException("serving team not known");
                    }
            }

            TennisMatchIncident tennisMatchIncident2 = lastIncidentDetails.getLastIncident();
            // if (tennisMatchIncident2.getIncidentSubType() == TennisMatchIncidentType.POINT_WON) {
            TennisPointResult pointResult = lastIncidentDetails.getPointResult();
            if (pointResult == null)
                pointResult = TennisPointResult.UNKNOWN;

            if (pointResult == TennisPointResult.DOUBLE_FAULT) {
                builder.serverWins(false);
            } else {

                switch (lastIncidentDetails.getServingTeam()) {
                    case A:
                        if (tennisMatchIncident2.getPointWinner() == TeamId.A)
                            builder.serverWins(true);
                        else if (tennisMatchIncident2.getPointWinner() == TeamId.B)
                            builder.serverWins(false);
                        break;
                    case B:
                        if (tennisMatchIncident2.getPointWinner() == TeamId.A)
                            builder.serverWins(false);
                        else if (tennisMatchIncident2.getPointWinner() == TeamId.B)
                            builder.serverWins(true);
                        break;
                    default:
                        // builder.serverWins(null);
                        break;
                }
                // }
            }
            builder.serveNumber(tennisMatchState.getLastIncidentDetails().getServeNo());


            if (tennisMatchState.isPowerPointCurrentPoint())
                builder.powerPoint(true);
            else
                builder.powerPoint(false);


            builder.pointResult(pointResult.toString().toLowerCase());

            return builder.build();
        }
        return null;// if it is not a point won incident
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public String getServingTeam() {
        return servingTeam;
    }

    public boolean isServerWins() {
        return serverWins;
    }

    public int getServeNumber() {
        return serveNumber;
    }

    public String getPointResult() {
        return pointResult;
    }

    public boolean isPowerPoint() {
        return powerPoint;
    }

    public static class Builder {
        private int pointNumber;
        private String servingTeam;
        private boolean serverWins;
        private int serveNumber;
        private String pointResult;
        private boolean powerPoint;

        public Builder pointNumber(int val) {
            pointNumber = val;
            return this;
        }

        public Builder servingTeam(String val) {
            servingTeam = val;
            return this;
        }

        public Builder serverWins(boolean val) {
            serverWins = val;
            return this;
        }

        public Builder serveNumber(int val) {
            serveNumber = val;
            return this;
        }

        public Builder pointResult(String val) {
            pointResult = val;
            return this;
        }

        public Builder powerPoint(boolean val) {
            powerPoint = val;
            return this;
        }

        public PpbTennisPointDetails build() {
            return new PpbTennisPointDetails(this);
        }

    }

    private PpbTennisPointDetails(Builder builder) {
        pointNumber = builder.pointNumber;
        servingTeam = builder.servingTeam;
        serverWins = builder.serverWins;
        serveNumber = builder.serveNumber;
        pointResult = builder.pointResult;
        powerPoint = builder.powerPoint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + pointNumber;
        result = prime * result + ((pointResult == null) ? 0 : pointResult.hashCode());
        result = prime * result + (powerPoint ? 1231 : 1237);
        result = prime * result + serveNumber;
        result = prime * result + (serverWins ? 1231 : 1237);
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
        PpbTennisPointDetails other = (PpbTennisPointDetails) obj;
        if (pointNumber != other.pointNumber)
            return false;
        if (pointResult == null) {
            if (other.pointResult != null)
                return false;
        } else if (!pointResult.equals(other.pointResult))
            return false;
        if (powerPoint != other.powerPoint)
            return false;
        if (serveNumber != other.serveNumber)
            return false;
        if (serverWins != other.serverWins)
            return false;
        if (servingTeam == null) {
            if (other.servingTeam != null)
                return false;
        } else if (!servingTeam.equals(other.servingTeam))
            return false;
        return true;
    }


}
