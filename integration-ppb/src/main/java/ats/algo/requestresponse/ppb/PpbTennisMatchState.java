package ats.algo.requestresponse.ppb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.GenderId;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.PlayerId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class PpbTennisMatchState implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int currentServeNumber;
    private String startOfMatchServer;
    private String nextPointServerGender;
    private boolean nextPointIsPowerPoint;
    private int currentSet;
    private int currentGame;
    private List<PpbTennisPointDetails> points;

    // private String servingTeam;

    public PpbTennisMatchState() {

    }

    public static PpbTennisMatchState generatePpbTennisMatchState(TennisMatchState matchState,
                    List<PpbTennisPointDetails> points) {
        /*
         * creates a new object with properties set from the supplied params. I think to support this we will need to
         * add firstServerInMatch to TennisMatchState?
         */
        PpbTennisMatchState.Builder builder = new PpbTennisMatchState.Builder();
        builder.currentGame(matchState.getGameNo());
        builder.currentSet(matchState.getSetNo());
        int serveNo = matchState.getServeNumber();
        builder.currentServeNumber(serveNo);
        boolean forceServerToBeSet = false;

        if (matchState.getStartOfMatchServer() == PlayerId.A1 || matchState.getStartOfMatchServer() == PlayerId.A2)
            builder.startOfMatchServer("a");
        else if (matchState.getStartOfMatchServer() == PlayerId.B1 || matchState.getStartOfMatchServer() == PlayerId.B2)
            builder.startOfMatchServer("b");
        else {
            builder.startOfMatchServer("not_set");
            forceServerToBeSet = true;
        }

        if (matchState.getSequenceIdForPoint(0).equals("S1.1.1") && matchState.getLastIncidentDetails() != null) {
            TennisMatchIncident tennisMatchIncident = matchState.getDoubletennisMatchIncident();
            if (tennisMatchIncident != null) {
                if (tennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.SERVING_ORDER)) {
                    if (tennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.A) {
                        builder.startOfMatchServer("a");

                    } else if (tennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.B) {
                        builder.startOfMatchServer("b");
                    } else {
                        // Do Nothing and leave serve as what it was.
                    }
                } else if (tennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.MATCH_STARTING)) {
                    if (tennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                        builder.startOfMatchServer("a");

                    } else if (tennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                        builder.startOfMatchServer("b");
                    } else {
                        // Do Nothing and leave serve as what it was.
                    }

                }
            }
        } else if (!(matchState.getSequenceIdForPoint(0).equals("S1.1.1"))
                        && matchState.getLastIncidentDetails() != null && !matchState.isPreMatch()
                        && forceServerToBeSet) {
            builder.startOfMatchServer(points.get(0).getServingTeam());
        }

        MatchFormat matchForamt = matchState.getMatchFormat();
        if (((TennisMatchFormat) matchForamt).getSex() == Sex.MIXED) {
            EnumMap<PlayerId, GenderId> genderMap = matchState.getGenderMap();
            PlayerId playerId = matchState.getOnServePlayerNow();
            GenderId genderId = genderMap.get(playerId);

            if (genderId == null) {
                genderId = GenderId.UNKNOWN;
            }
            switch (genderId) {
                case MALE:
                    builder.nextPointServerGender("male");
                    break;
                case FEMALE:
                    builder.nextPointServerGender("female");
                    break;
                default:
                    builder.nextPointServerGender("unknown");
                    break;
            }
        } else if (((TennisMatchFormat) matchForamt).getSex() == Sex.MEN)
            builder.nextPointServerGender("male");
        else if (((TennisMatchFormat) matchForamt).getSex() == Sex.WOMEN)
            builder.nextPointServerGender("female");

        TennisMatchIncident tennisMatchIncident2 = matchState.getDoubletennisMatchIncident();

        if (tennisMatchIncident2 != null)
            // if (tennisMatchIncident2.getIncidentSubType() == TennisMatchIncidentType.POINT_WON)
            // if (matchState.getLastIncidentDetails().getLastIncident()
            // .getIncidentSubType() == TennisMatchIncidentType.POINT_WON)
            builder.points(points);

        // if (matchState.getOnServeNow() == TeamId.A)
        // builder.servingTeam("a");
        // else if (matchState.getOnServeNow() == TeamId.B)
        // builder.servingTeam("b");
        // else
        // builder.servingTeam("not_set");

        if (matchState.isPowerPointCurrentPoint() && matchState.getLastIncidentDetails().getLastIncident()
                        .getIncidentSubType() != TennisMatchIncidentType.POINT_WON)
            builder.nextPointIsPowerPoint(true);
        else
            builder.nextPointIsPowerPoint(false);



        return builder.build();
    }



    public int getCurrentServeNumber() {
        return currentServeNumber;
    }

    public int getCurrentGame() {
        return currentGame;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public String getStartOfMatchServer() {
        return startOfMatchServer;
    }

    public String getNextPointServerGender() {
        return nextPointServerGender;
    }

    public boolean isNextPointIsPowerPoint() {
        return nextPointIsPowerPoint;
    }

    public List<PpbTennisPointDetails> getPoints() {
        return points;
    }


    // public String getServingTeam() {
    // return servingTeam;
    // }

    public static class Builder {
        private int currentServeNumber;
        private String startOfMatchServer;
        private String nextPointServerGender;
        private boolean nextPointIsPowerPoint;
        private int currentSet;
        private int currentGame;
        private List<PpbTennisPointDetails> points;

        // private String servingTeam;

        public Builder nextPointIsPowerPoint(boolean val) {
            nextPointIsPowerPoint = val;
            return this;
        }

        public Builder currentServeNumber(int val) {
            currentServeNumber = val;
            return this;
        }

        public Builder currentSet(int val) {
            currentSet = val;
            return this;
        }

        public Builder currentGame(int val) {
            currentGame = val;
            return this;
        }

        public Builder startOfMatchServer(String val) {
            startOfMatchServer = val;
            return this;
        }

        public Builder nextPointServerGender(String val) {
            nextPointServerGender = val;
            return this;
        }

        public Builder points(List<PpbTennisPointDetails> val) {
            points = copyPbbTennisPointsList(val);
            return this;
        }


        // public Builder servingTeam(String val) {
        // servingTeam = val;
        // return this;
        // }

        public PpbTennisMatchState build() {
            return new PpbTennisMatchState(this);
        }

    }

    private PpbTennisMatchState(Builder builder) {
        currentServeNumber = builder.currentServeNumber;
        startOfMatchServer = builder.startOfMatchServer;
        nextPointServerGender = builder.nextPointServerGender;
        points = builder.points;
        nextPointIsPowerPoint = builder.nextPointIsPowerPoint;
        currentGame = builder.currentGame;
        currentSet = builder.currentSet;
        // servingTeam = builder.servingTeam;
    }


    private static List<PpbTennisPointDetails> copyPbbTennisPointsList(List<PpbTennisPointDetails> pointsIn) {
        List<PpbTennisPointDetails> points = new ArrayList<PpbTennisPointDetails>();

        if (pointsIn == null)
            points = null;
        else {
            Iterator<PpbTennisPointDetails> pointsIterator = pointsIn.iterator();
            while (pointsIterator.hasNext()) {
                PpbTennisPointDetails pointDetails = pointsIterator.next();
                points.add(pointDetails);
            }
        }
        return points;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentGame;
        result = prime * result + currentServeNumber;
        result = prime * result + currentSet;
        result = prime * result + (nextPointIsPowerPoint ? 1231 : 1237);
        result = prime * result + ((nextPointServerGender == null) ? 0 : nextPointServerGender.hashCode());
        result = prime * result + ((points == null) ? 0 : points.hashCode());
        result = prime * result + ((startOfMatchServer == null) ? 0 : startOfMatchServer.hashCode());
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
        PpbTennisMatchState other = (PpbTennisMatchState) obj;
        if (currentGame != other.currentGame)
            return false;
        if (currentServeNumber != other.currentServeNumber)
            return false;
        if (currentSet != other.currentSet)
            return false;
        if (nextPointIsPowerPoint != other.nextPointIsPowerPoint)
            return false;
        if (nextPointServerGender == null) {
            if (other.nextPointServerGender != null)
                return false;
        } else if (!nextPointServerGender.equals(other.nextPointServerGender))
            return false;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        if (startOfMatchServer == null) {
            if (other.startOfMatchServer != null)
                return false;
        } else if (!startOfMatchServer.equals(other.startOfMatchServer))
            return false;
        return true;
    }


}
