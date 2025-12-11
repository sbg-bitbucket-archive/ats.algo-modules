package ats.algo.outrights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Markets class to convert the data to/from the data format needed for a tree list view so it can be
 * displayed in the GUI
 * 
 * @author Rob
 * 
 */
public class ObservableFcastStandings {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableFcastStanding> observableStandings;

    public ObservableFcastStandings() {
        observableStandings = FXCollections.observableArrayList();
    }

    public void update(FcastStandings standings) {

        observableStandings.clear();
        for (FcastStanding standing : standings.getOrderedStandings()) {
            ObservableFcastStanding observableTeam = new ObservableFcastStanding(standing);
            observableStandings.add(observableTeam);
        }
    }

    public void update(FcastStandings standings, String marketFilter) {
        observableStandings.clear();
    }

    public ObservableList<ObservableFcastStanding> getData() {
        return observableStandings;
    }

    public class ObservableFcastStanding {
        /*
         * Stuff to display
         */
        private StringProperty teamID;
        private StringProperty played;
        private StringProperty won;
        private StringProperty drawn;
        private StringProperty lost;
        private StringProperty goalsFor;
        private StringProperty goalsAgainst;
        private StringProperty goalDiff;
        private StringProperty points;
        private StringProperty targetPoints;
        @JsonIgnore
        private StringProperty standingUpdate;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableFcastStanding(FcastStanding standing) {
            this.teamID = new SimpleStringProperty(standing.getTeamId());
            this.played = new SimpleStringProperty(String.valueOf(standing.getPlayed()));
            this.won = new SimpleStringProperty(String.format("%.1f", standing.getWon()));
            this.drawn = new SimpleStringProperty(String.format("%.1f", standing.getDrawn()));
            this.lost = new SimpleStringProperty(String.format("%.1f", standing.getLost()));
            this.goalsFor = new SimpleStringProperty(String.format("%.1f", standing.getGoalsFor()));
            this.goalsAgainst = new SimpleStringProperty(String.format("%.1f", standing.getGoalsAgainst()));
            this.goalDiff = new SimpleStringProperty(String.format("%.1f", standing.getGoalsDiff()));
            this.points = new SimpleStringProperty(String.format("%.1f", standing.getPoints()));
            this.targetPoints = new SimpleStringProperty(String.format("%.2f", standing.getTargetPoints()));
            this.standingUpdate = new SimpleStringProperty("No");
        }

        /**
         * Constructor for rows other than the first row of a market
         * 
         * @param key
         * @param sequenceId
         * @param selectionName
         * @param nSelections
         * @param prob
         */
        public ObservableFcastStanding(String teamID, double played, double won, double drawn, double lost,
                        double goalsFor, double goalsAgainst, double goalDiff, double points, double targetPoints) {
            this.teamID = new SimpleStringProperty(teamID);
            this.played = new SimpleStringProperty(String.format("%.1f", played));
            this.won = new SimpleStringProperty(String.format("%.1f", won));
            this.drawn = new SimpleStringProperty(String.format("%.1f", drawn));
            this.lost = new SimpleStringProperty(String.format("%.1f", lost));
            this.goalsFor = new SimpleStringProperty(String.format("%.1f", goalsFor));
            this.goalsAgainst = new SimpleStringProperty(String.format("%.1f", goalsAgainst));
            this.goalDiff = new SimpleStringProperty(String.format("%.1f", goalDiff));
            this.points = new SimpleStringProperty(String.format("%.1f", points));
            this.targetPoints = new SimpleStringProperty(String.format("%.2f", targetPoints));
            this.standingUpdate = new SimpleStringProperty("No");
        }

        public StringProperty playedProperty() {
            return played;
        }

        public StringProperty teamIDProperty() {
            return teamID;
        }

        public StringProperty wonProperty() {
            return won;
        }

        public StringProperty drawnProperty() {
            return drawn;
        }

        public StringProperty lostProperty() {
            return lost;
        }

        public StringProperty goalsForProperty() {
            return goalsFor;
        }

        public StringProperty goalsAgainstProperty() {
            return goalsAgainst;
        }

        public StringProperty goalDiffProperty() {
            return goalDiff;
        }

        public StringProperty pointsProperty() {
            return points;
        }

        public StringProperty targetPointsProperty() {
            return targetPoints;
        }

        public StringProperty standingUpdateProperty() {
            return standingUpdate;
        }

        public String getTeamID() {
            return teamID.getValue();
        }

        public double getPlayed() {
            return Double.valueOf(played.getValue());
        }

        public double getWon() {
            return Double.valueOf(won.getValue());
        }

        public double getDrawn() {
            return Double.valueOf(drawn.getValue());
        }

        public double getLost() {
            return Double.valueOf(lost.getValue());
        }

        public double getGoalsFor() {
            return Double.valueOf(goalsFor.getValue());
        }

        public double getGoalsAgainst() {
            return Double.valueOf(goalsAgainst.getValue());
        }

        public double getGoalDiff() {
            return Double.valueOf(goalDiff.getValue());
        }

        public double getpoints() {
            return Double.valueOf(points.getValue());
        }

        public double getTargetpoints() {
            return Double.valueOf(targetPoints.getValue());
        }

        public String getStangdingUpdate() {
            return standingUpdate.getValue();
        }

        public void setPlayed(String played) {
            this.played = new SimpleStringProperty(played);
        }

        public void setWon(String won) {
            this.won = new SimpleStringProperty(won);
        }

        public void setDrawn(String drawn) {
            this.drawn = new SimpleStringProperty(drawn);
        }

        public void setLost(String lost) {
            this.lost = new SimpleStringProperty(lost);
        }


        public void setGoalsFor(String goalsFor) {
            this.goalsFor = new SimpleStringProperty(goalsFor);
        }

        public void setGoalDiff(String goalDiff) {
            this.goalDiff = new SimpleStringProperty(goalDiff);
        }

        public void setGoalsAgainst(String goalsAgainst) {
            this.goalsAgainst = new SimpleStringProperty(goalsAgainst);
        }

        public void setpoints(String points) {
            this.points = new SimpleStringProperty(points);;
        }

        public void setTargetPoints(String targetPoints) {
            this.targetPoints = new SimpleStringProperty(targetPoints);;
        }

        public void setStandingUpdate(String standingUpdate) {
            this.standingUpdate = new SimpleStringProperty(standingUpdate);;
        }


        @Override
        public String toString() {
            return "ObservableFCastStanding [fteamID=" + teamID.getValue() + ", fplayed=" + played.getValue()
                            + ", fwon=" + won.getValue() + ", fdrawn=" + drawn.getValue() + ", fgoalsFor="
                            + goalsFor.getValue() + ", fgoalsAgainst=" + goalsAgainst.getValue() + ", fpoints="
                            + points.getValue() + ", ftargetpoints=" + targetPoints.getValue() + "]";
        }

    }

}
