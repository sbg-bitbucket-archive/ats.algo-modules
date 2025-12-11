package ats.algo.outrights.model;

import ats.algo.sport.outrights.calcengine.core.Teams;
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
@SuppressWarnings("restriction")
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
            ObservableCompetition observableTeam = new ObservableCompetition(competition);
            observableCompetitions.add(observableTeam);
        }
    }

    public void update(Teams teams, String marketFilter) {

        observableCompetitions.clear();
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

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableCompetition(CompetitionsListEntry competitionsListEntry) {
            this.eventID = new SimpleStringProperty(Long.toString(competitionsListEntry.getEventID()));
            this.competitionName = new SimpleStringProperty(competitionsListEntry.getName());
        }

        public StringProperty competitionNameProperty() {
            return competitionName;
        }

        public StringProperty eventIDProperty() {
            return eventID;
        }


    }

}
