package ats.algo.outrights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
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
@SuppressWarnings("restriction")
public class ObservableStandings {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableStanding> observableStandings;

    public ObservableStandings() {
        observableStandings = FXCollections.observableArrayList();
    }

    public void update(Standings standings) {

        observableStandings.clear();
        for (Standing standing : standings.getStandings().values()) {
            ObservableStanding observableTeam = new ObservableStanding(standing);
            observableStandings.add(observableTeam);
        }
    }

    public void update(Standings standings, String marketFilter) {
        observableStandings.clear();
    }

    public ObservableList<ObservableStanding> getData() {
        return observableStandings;
    }

    public class ObservableStanding {
        /*
         * Stuff to display
         */
        private StringProperty teamID;
        private StringProperty played;
        private StringProperty won;
        private StringProperty drawn;
        private StringProperty goalsFor;
        private StringProperty goalsAgainst;
        private StringProperty points;
        @JsonIgnore
        private StringProperty standingUpdate;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableStanding(Standing standing) {
            this.teamID = new SimpleStringProperty(standing.getTeamId());
            this.played = new SimpleStringProperty(String.valueOf(standing.getPlayed()));
            this.won = new SimpleStringProperty(String.valueOf(standing.getWon()));
            this.drawn = new SimpleStringProperty(String.valueOf(standing.getDrawn()));
            this.goalsFor = new SimpleStringProperty(String.valueOf(standing.getGoalsFor()));
            this.goalsAgainst = new SimpleStringProperty(String.valueOf(standing.getGoalsAgainst()));
            this.points = new SimpleStringProperty(String.valueOf(standing.getPoints()));
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
        public ObservableStanding(String teamID, int played, int won, int drawn, int goalsFor, int goalsAgainst,
                        int points) {
            this.teamID = new SimpleStringProperty(teamID);
            this.played = new SimpleStringProperty(String.valueOf(played));
            this.won = new SimpleStringProperty(String.valueOf(won));
            this.drawn = new SimpleStringProperty(String.valueOf(drawn));
            this.goalsFor = new SimpleStringProperty(String.valueOf(goalsFor));
            this.goalsAgainst = new SimpleStringProperty(String.valueOf(goalsAgainst));
            this.points = new SimpleStringProperty(String.valueOf(points));
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

        public StringProperty goalsForProperty() {
            return goalsFor;
        }

        public StringProperty goalsAgainstProperty() {
            return goalsAgainst;
        }

        public StringProperty pointsProperty() {
            return points;
        }

        public StringProperty standingUpdateProperty() {
            return standingUpdate;
        }

        public String getTeamID() {
            return teamID.getValue();
        }

        public int getPlayed() {
            return Integer.valueOf(played.getValue());
        }

        public int getWon() {
            return Integer.valueOf(won.getValue());
        }

        public int getDrawn() {
            return Integer.valueOf(drawn.getValue());
        }

        public int getGoalsFor() {
            return Integer.valueOf(goalsFor.getValue());
        }

        public int getGoalsAgainst() {
            return Integer.valueOf(goalsAgainst.getValue());
        }

        public int getPoints() {
            return Integer.valueOf(points.getValue());
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

        public void setGoalsFor(String goalsFor) {
            this.goalsFor = new SimpleStringProperty(goalsFor);
        }

        public void setGoalsAgainst(String goalsAgainst) {
            this.goalsAgainst = new SimpleStringProperty(goalsAgainst);
        }

        public void setPoints(String points) {
            this.points = new SimpleStringProperty(points);;
        }

        public void setStandingUpdate(String standingUpdate) {
            this.standingUpdate = new SimpleStringProperty(standingUpdate);;
        }

        public Standing getStanding() {
            Standing standing = new Standing(teamID.getValue(), getPlayed(), getWon(), getDrawn(), getGoalsFor(),
                            getGoalsAgainst(), getPoints());

            return standing;
        }

        @Override
        public String toString() {
            return "ObservableStanding [teamID=" + teamID.getValue() + ", played=" + played.getValue() + ", won="
                            + won.getValue() + ", drawn=" + drawn.getValue() + ", goalsFor=" + goalsFor.getValue()
                            + ", goalsAgainst=" + goalsAgainst.getValue() + ", points=" + points.getValue() + "]";
        }

    }

}
