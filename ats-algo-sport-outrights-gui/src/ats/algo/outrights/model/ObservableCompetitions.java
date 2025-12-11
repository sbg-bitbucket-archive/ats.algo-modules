package ats.algo.outrights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.CompetitionsListEntry;
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
public class ObservableCompetitions {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableCompetition> observableCompetitions;

    public ObservableCompetitions() {
        observableCompetitions = FXCollections.observableArrayList();
    }

    public void update(CompetitionsList list) {

        observableCompetitions.clear();
        for (CompetitionsListEntry competition : list.getCompetitionsList()) {
            ObservableCompetition observableCompetition = new ObservableCompetition(competition);
            observableCompetitions.add(observableCompetition);
        }
    }

    public ObservableList<ObservableCompetition> getData() {
        return observableCompetitions;
    }

    public class ObservableCompetition {
        /*
         * Stuff to display
         */
        private StringProperty eventID;
        private StringProperty competitionName;
        private StringProperty outrightsCompetitionID;
        private StringProperty atsCompetitionID;
        @JsonIgnore
        private StringProperty competitionUpdated;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableCompetition(CompetitionsListEntry competitionsListEntry) {
            this.eventID = new SimpleStringProperty(Long.toString(competitionsListEntry.getEventID()));
            this.competitionName = new SimpleStringProperty(competitionsListEntry.getName());
            this.outrightsCompetitionID = new SimpleStringProperty("TODO - REMOVE");
            this.atsCompetitionID = new SimpleStringProperty(competitionsListEntry.getAtsCompetitionID());
            this.competitionUpdated = new SimpleStringProperty("No");
        }

        public StringProperty competitionNameProperty() {
            return competitionName;
        }

        public StringProperty eventIDProperty() {
            return eventID;
        }

        public StringProperty outrightsCompetitionIDProperty() {
            return outrightsCompetitionID;
        }

        public StringProperty atsCompetitionIDProperty() {
            return atsCompetitionID;
        }

        public void setCompetitionName(String competitionName) {
            this.competitionName = new SimpleStringProperty(competitionName);
        }

        public void setOutrightsCompetitionID(String outrightsCompetitionID) {
            this.outrightsCompetitionID = new SimpleStringProperty(outrightsCompetitionID);
        }

        public void setAtsCompetitionID(String atsCompetitionID) {
            this.atsCompetitionID = new SimpleStringProperty(atsCompetitionID);
        }

        public void setEventID(String eventID) {
            this.eventID = new SimpleStringProperty(eventID);
        }

        public StringProperty competitionUpdatedProperty() {
            return competitionUpdated;
        }

        public void setCompetitionUpdated(String competitionUpdated) {
            this.competitionUpdated = new SimpleStringProperty(competitionUpdated);
        }

        public CompetitionsListEntry getCompetitionsListEntry() {
            CompetitionsListEntry competitionsListEntry = new CompetitionsListEntry(Long.valueOf(eventID.getValue()),
                            competitionName.getValue(), atsCompetitionID.getValue());
            return competitionsListEntry;

        }

        @Override
        public String toString() {
            return "ObservableCompetition [eventID=" + eventID + ", competitionName=" + competitionName
                            + ", outrightsCompetitionID=" + outrightsCompetitionID + ", atsCompetitionID="
                            + atsCompetitionID + "]";
        }

    }

}
