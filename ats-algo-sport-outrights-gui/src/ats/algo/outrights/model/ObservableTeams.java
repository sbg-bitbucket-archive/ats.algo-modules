package ats.algo.outrights.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
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
public class ObservableTeams {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableTeam> observableTeams;

    public ObservableTeams() {
        observableTeams = FXCollections.observableArrayList();
    }

    public void update(Teams teams) {

        observableTeams.clear();
        for (Team team : teams.getTeams().values()) {
            ObservableTeam observableTeam = new ObservableTeam(team);
            observableTeams.add(observableTeam);
        }
    }

    public void update(Teams teams, String marketFilter) {

        observableTeams.clear();
    }

    public ObservableList<ObservableTeam> getData() {
        return observableTeams;
    }

    public class ObservableTeam {
        /*
         * Stuff to display
         */
        private StringProperty teamID;
        private StringProperty displayName;
        private StringProperty fiveThirtyEightName;
        private StringProperty ratingAttack;
        private StringProperty ratingDefense;
        @JsonIgnore
        private StringProperty teamUpdated;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableTeam(Team team) {
            this.displayName = new SimpleStringProperty(team.getDisplayName());
            this.teamID = new SimpleStringProperty(team.getTeamID());
            this.fiveThirtyEightName = new SimpleStringProperty(team.getFiveThirtyEightName());
            this.ratingAttack = new SimpleStringProperty(String.format("%.3f", team.getRatingAttack()));
            this.ratingDefense = new SimpleStringProperty(String.format("%.3f", team.getRatingDefense()));
            this.teamUpdated = new SimpleStringProperty("No");
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
        public ObservableTeam(String teamID, String displayName, String fiveThirtyEightName, double ratingAttack,
                        double ratingDefense) {
            this.displayName = new SimpleStringProperty(displayName);
            this.teamID = new SimpleStringProperty(teamID);
            this.fiveThirtyEightName = new SimpleStringProperty(fiveThirtyEightName);
            this.ratingAttack = new SimpleStringProperty(String.format("%.3f", ratingAttack));
            this.ratingDefense = new SimpleStringProperty(String.format("%.3f", ratingDefense));
            this.teamUpdated = new SimpleStringProperty("No");
        }

        public StringProperty displayNameProperty() {
            return displayName;
        }

        public StringProperty teamIDProperty() {
            return teamID;
        }

        public StringProperty fiveThirtyEightNameProperty() {
            return fiveThirtyEightName;
        }

        public StringProperty ratingAttackProperty() {
            return ratingAttack;
        }

        public StringProperty ratingDefenseProperty() {
            return ratingDefense;
        }

        public StringProperty teamUpdatedProperty() {
            return teamUpdated;
        }


        public StringProperty getTeamID() {
            return teamID;
        }

        public void setTeamID(String teamID) {
            this.teamID = new SimpleStringProperty(teamID);
        }

        public StringProperty getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = new SimpleStringProperty(displayName);
        }

        public StringProperty getFiveThirtyEightName() {
            return fiveThirtyEightName;
        }

        public StringProperty getTeamUpdated() {
            return teamUpdated;
        }

        public void setFiveThirtyEightName(String fiveThirtyEightName) {
            this.fiveThirtyEightName = new SimpleStringProperty(fiveThirtyEightName);
        }

        public StringProperty getRatingAttack() {
            return ratingAttack;
        }

        public void setRatingAttack(String ratingAttack) {
            this.ratingAttack = new SimpleStringProperty(ratingAttack);
        }

        public StringProperty getRatingDefense() {
            return ratingDefense;
        }

        public void setRatingDefense(String ratingDefense) {
            this.ratingDefense = new SimpleStringProperty(ratingDefense);
        }

        public void setTeamUpdated(String teamUpdated) {
            this.teamUpdated = new SimpleStringProperty(teamUpdated);
        }

        public Team getTeam() {
            Team team = new Team(teamID.getValue(), displayName.getValue(), fiveThirtyEightName.getValue(),
                            Double.valueOf(ratingAttack.getValue()), Double.valueOf(ratingDefense.getValue()));

            return team;
        }

        @Override
        public String toString() {
            return "ObservableTeam [teamID=" + teamID.getValue() + ", displayName=" + displayName.getValue()
                            + ", fiveThirtyEightName=" + fiveThirtyEightName.getValue() + ", ratingAttack="
                            + ratingAttack.getValue() + ", ratingDefense=" + ratingDefense.getValue() + "]";
        }


    }

}
