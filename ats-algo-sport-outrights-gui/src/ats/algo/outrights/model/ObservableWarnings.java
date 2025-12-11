package ats.algo.outrights.model;

import java.util.Map.Entry;

import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;
import ats.algo.sport.outrights.server.api.Warnings;
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
public class ObservableWarnings {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableWarning> observableWarnings;

    public ObservableWarnings() {
        observableWarnings = FXCollections.observableArrayList();
    }

    public void update(Warnings warnings) {

        observableWarnings.clear();
        for (Entry<Long, CompetitionWarnings> entry : warnings.getWarnings().entrySet()) {
            ObservableWarning observableWarning = new ObservableWarning(entry);
            observableWarnings.add(observableWarning);
        }
    }

    public ObservableList<ObservableWarning> getData() {
        return observableWarnings;
    }

    public class ObservableWarning {
        /*
         * Stuff to display
         */
        private StringProperty eventIDWarnings;
        private StringProperty stateOk;
        private StringProperty warningMessages;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableWarning(Entry<Long, CompetitionWarnings> competitionWarnings) {
            this.eventIDWarnings = new SimpleStringProperty(String.valueOf(competitionWarnings.getKey()));
            this.stateOk = new SimpleStringProperty(String.valueOf(competitionWarnings.getValue().isStateOk()));
            this.warningMessages =
                            new SimpleStringProperty(competitionWarnings.getValue().getWarningMessages().toString());
        }

        public StringProperty warningMessagesProperty() {
            return warningMessages;
        }

        public StringProperty stateOkProperty() {
            return stateOk;
        }

        public StringProperty getEventIDWarnings() {
            return eventIDWarnings;
        }

        @Override
        public String toString() {
            return "ObservableWarning [eventIDWarnings=" + eventIDWarnings.getValue() + ", stateOk="
                            + stateOk.getValue() + ", warningMessages=" + warningMessages.getValue() + "]";
        }

    }

}
