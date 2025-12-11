package ats.algo.outrights.model;


import java.util.Collections;
import java.util.Comparator;

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
        Comparator<ObservableTeam> cmp = new Comparator<ObservableTeam>() {
            public int compare(ObservableTeam o1, ObservableTeam o2) {
                return o1.displayName.getValue().compareTo(o2.displayName.getValue());
            }
        };
        Collections.sort(observableTeams, cmp);
        return observableTeams;
    }

    public class ObservableTeam {
        /*
         * Stuff to display
         */
        private StringProperty teamID;
        private StringProperty displayName;
        private StringProperty fiveThirtyEightName;
        private StringProperty sportingIndexName;
        private StringProperty lsportsName;
        private StringProperty ratingAttack;
        private StringProperty ratingDefense;
        private StringProperty ratingsBiasAttack;
        private StringProperty ratingsBiasDefense;
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
            this.lsportsName = new SimpleStringProperty(team.getLsportsName());
            this.sportingIndexName = new SimpleStringProperty(team.getSportingIndexName());
            this.ratingAttack = new SimpleStringProperty(String.format("%.3f", team.getRatingAttack()));
            this.ratingDefense = new SimpleStringProperty(String.format("%.3f", team.getRatingDefense()));
            this.ratingsBiasAttack = new SimpleStringProperty(String.format("%.2f", team.getBiasAttack()));
            this.ratingsBiasDefense = new SimpleStringProperty(String.format("%.2f", team.getBiasDefense()));
            this.teamUpdated = new SimpleStringProperty("No");
        }

        /**
         * Constructor for rows other than the first row of a ObservableTeam
         */
        public ObservableTeam(String teamID, String displayName, String fiveThirtyEightName, String lsportsName,
                        String sportingIndexName, double ratingAttack, double ratingDefense) {
            this.displayName = new SimpleStringProperty(displayName);
            this.teamID = new SimpleStringProperty(teamID);
            this.fiveThirtyEightName = new SimpleStringProperty(fiveThirtyEightName);
            this.lsportsName = new SimpleStringProperty(lsportsName);
            this.sportingIndexName = new SimpleStringProperty(sportingIndexName);
            this.ratingAttack = new SimpleStringProperty(String.format("%.3f", ratingAttack));
            this.ratingDefense = new SimpleStringProperty(String.format("%.3f", ratingDefense));
            this.ratingsBiasAttack = new SimpleStringProperty(String.format("%.2f", ratingsBiasAttack));
            this.ratingsBiasDefense = new SimpleStringProperty(String.format("%.2f", ratingsBiasDefense));
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

        public StringProperty lsportsNameProperty() {
            return lsportsName;
        }

        public StringProperty sportingIndexNameProperty() {
            return sportingIndexName;
        }

        public StringProperty ratingAttackProperty() {
            return ratingAttack;
        }

        public StringProperty ratingDefenseProperty() {
            return ratingDefense;
        }

        public StringProperty ratingsBiasAttackProperty() {
            return ratingsBiasAttack;
        }

        public StringProperty ratingsBiasDefenseProperty() {
            return ratingsBiasDefense;
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

        public void setSportingIndexName(String sportingIndexName) {
            this.sportingIndexName = new SimpleStringProperty(sportingIndexName);
        }

        public void setLsportsName(String lsportsName) {
            this.lsportsName = new SimpleStringProperty(lsportsName);
        }

        public StringProperty getRatingAttack() {
            return ratingAttack;
        }

        public void setRatingAttack(String ratingAttack) {
            this.ratingAttack = new SimpleStringProperty(ratingAttack);
        }

        public StringProperty getRatingsBiasAttack() {
            return ratingsBiasAttack;
        }

        public StringProperty getRatingsBiasDefense() {
            return ratingsBiasDefense;
        }

        public void setRatingsBiasAttack(String ratingsBiasAttack) {
            this.ratingsBiasAttack = new SimpleStringProperty(ratingsBiasAttack);
        }

        public void setRatingsBiasDefense(String ratingsBiasDefense) {
            this.ratingsBiasDefense = new SimpleStringProperty(ratingsBiasDefense);
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
                            sportingIndexName.getValue(), lsportsName.getValue(),
                            Double.valueOf(ratingAttack.getValue()), Double.valueOf(ratingDefense.getValue()));
            team.setBiasAttack(Double.valueOf(ratingsBiasAttack.getValue()));
            team.setBiasDefense(Double.valueOf(ratingsBiasDefense.getValue()));
            return team;
        }

        @Override
        public String toString() {
            return "ObservableTeam [teamID=" + teamID + ", displayName=" + displayName + ", fiveThirtyEightName="
                            + fiveThirtyEightName + ", sportingIndexName=" + sportingIndexName + ", lsportsName="
                            + lsportsName + ", ratingAttack=" + ratingAttack + ", ratingDefense=" + ratingDefense
                            + ", ratingsBiasAttack=" + ratingsBiasAttack + ", ratingsBiasDefense=" + ratingsBiasDefense
                            + ", teamUpdated=" + teamUpdated + "]";
        }

    }

}
