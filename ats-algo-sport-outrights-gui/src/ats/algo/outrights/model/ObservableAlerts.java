package ats.algo.outrights.model;


import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;
import ats.algo.sport.outrights.server.api.Alerts;
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
public class ObservableAlerts {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableAlert> observableAlerts;

    public ObservableAlerts() {
        observableAlerts = FXCollections.observableArrayList();
    }

    public void update(Alerts alerts) {

        observableAlerts.clear();
        for (Alert alert : alerts.getAlerts()) {
            ObservableAlert observableAlert = new ObservableAlert(alert);
            observableAlerts.add(observableAlert);
        }
    }

    public void update(Teams teams, String marketFilter) {

        observableAlerts.clear();
    }

    public ObservableList<ObservableAlert> getData() {
        return observableAlerts;
    }

    public class ObservableAlert {
        /*
         * Stuff to display
         */
        private StringProperty alertType;
        private StringProperty id;
        private StringProperty dateTime;
        private StringProperty eventId;
        private StringProperty acknowledged;
        private StringProperty description;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableAlert(Alert alert) {
            this.alertType = new SimpleStringProperty(String.valueOf(alert.getAlertType()));
            this.dateTime = new SimpleStringProperty(String.valueOf(alert.getDateTime()));
            this.eventId = new SimpleStringProperty(String.valueOf(alert.getEventId()));
            this.acknowledged = new SimpleStringProperty(String.valueOf(alert.isAcknowledged()));
            this.description = new SimpleStringProperty(alert.getDescription());
            this.id = new SimpleStringProperty(String.valueOf(alert.getId()));
        }


        public StringProperty alertTypeProperty() {
            return alertType;
        }

        public StringProperty dateTimeProperty() {
            return dateTime;
        }

        public StringProperty eventIdProperty() {
            return eventId;
        }

        public StringProperty descriptionProperty() {
            return description;
        }

        public StringProperty acknowledgedProperty() {
            return acknowledged;
        }


        public void setAlertType(String alertType) {
            this.alertType = new SimpleStringProperty(alertType);
        }

        public StringProperty getAlertType() {
            return alertType;
        }

        public StringProperty getId() {
            return id;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = new SimpleStringProperty(dateTime);
        }

        public StringProperty getDateTime() {
            return dateTime;
        }

        public StringProperty getEventId() {
            return eventId;
        }

        public StringProperty getAcknowledged() {
            return acknowledged;
        }

        public void setEventId(String eventId) {
            this.eventId = new SimpleStringProperty(eventId);
        }

        public void setDescription(String description) {
            this.description = new SimpleStringProperty(description);
        }

        public void setAcknowledged(String acknowledged) {
            this.acknowledged = new SimpleStringProperty(acknowledged);
        }

        public Alert getAlert() {
            AlertType type = AlertType.INFO;
            switch (alertType.getValue()) {
                case "ERROR":
                    type = AlertType.ERROR;
                    break;
                case "INFO":
                    type = AlertType.INFO;
                    break;
                case "WARNING":
                    type = AlertType.WARNING;
                    break;
                default:
                    break;
            }
            Alert alert = new Alert(type, Long.valueOf(eventId.getValue()), description.getValue());
            alert.setAcknowledged(Boolean.valueOf(getAcknowledged().getValue()));
            alert.setId(Integer.valueOf(getId().getValue()));
            alert.setDateTime(getDateTime().getValue());
            return alert;

        }


        @Override
        public String toString() {
            return "ObservableAlert [alertType=" + alertType + ", dateTime=" + dateTime + ", eventId=" + eventId
                            + ", acknowledged=" + acknowledged + ", description=" + description + "]";
        }

    }


}
