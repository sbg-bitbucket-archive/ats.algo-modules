package ats.algo.integration;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import ats.algo.algomanager.AlgoManager;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.core.AtsBean;
import generated.ats.betsync.dto.Attribute;
import generated.ats.betsync.dto.NotificationMessage;

public class TraderAlertBuilder extends AtsBean {

    private AlgoManager algoManager;

    public void setAlgoManager(AlgoManager algoManager) {
        this.algoManager = algoManager;
    }

    public NotificationMessage createTraderAlertNotification(TradedEvent tradedEvent, TraderAlert traderAlert) {
        NotificationMessage alertNotification =
                        tradedEvent.createDisplayVariable("algoManagerTraderAlert", traderAlert.getAlertText());
        List<Attribute> attributes = alertNotification.getAttributes();

        TraderAlertType traderAlertType = traderAlert.getTraderAlertType();

        if (!(traderAlert.getAlertAttributes() == null)) {
            info("EventID: " + tradedEvent.getEventId() + ". Adding already defined attributes to Alert");
            addAlgoAttributes(traderAlert, attributes);
        } else {

            switch (traderAlertType) {
                case CONSECUTIVE_FAILED_PARAMS:
                    int millis = algoManager.getElapsedTimeBeforeConsecutiveAlertTriggered();
                    describeAndDisplay(TraderAlertType.CONSECUTIVE_FAILED_PARAMS,
                                    "Elapsed time since last param find = " + TimeUnit.MILLISECONDS.toMinutes(millis),
                                    "Check input prices and match state", attributes);
                    break;
                case FIRST_INPLAY_PF:
                    describeAndDisplay(TraderAlertType.FIRST_INPLAY_PF, "First Successful inplay PF",
                                    "Review price source selector", attributes);
                    break;
                case MATCH_STATE_MISMATCH:
                    describeAndDisplay(TraderAlertType.MATCH_STATE_MISMATCH, "Event Mismatch Alert",
                                    "A Mismatch is happening", attributes);
                    break;
                case MATCH_STATE_MISMATCH_CLEARING:
                    describeAndDisplay(TraderAlertType.MATCH_STATE_MISMATCH_CLEARING, "Event Mismatch Clearing Alert",
                                    "The Mismatch is currently being cleared by trader", attributes);
                    break;
                case MATCH_STATE_MISMATCH_CLEARED:
                    describeAndDisplay(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED, "Event Mismatch Cleared Alert",
                                    "The Mismatch has been cleared", attributes);
                    break;
                case ABANDONED_EVENT:
                    describeAndDisplay(TraderAlertType.ABANDONED_EVENT, "Abandoned Event",
                                    "This event has been abandoned", attributes);
                    break;
                case EVENT_INPLAY:
                    describeAndDisplay(TraderAlertType.EVENT_INPLAY, "Inplay Event",
                                    "This event has transitioned inplay", attributes);
                    break;
                case FEED_DISCONNECTED:
                    describeAndDisplay(TraderAlertType.FEED_DISCONNECTED, "Disconnected Feed",
                                    "This event has a disconnected feed", attributes);
                    break;
                case RAIN:
                    describeAndDisplay(TraderAlertType.RAIN, "Rain Delay", "This event has been dalyed due to rain",
                                    attributes);
                    break;
                case CHALLENGE:
                    describeAndDisplay(TraderAlertType.CHALLENGE, "Challenge",
                                    "This event has been dalyed due to a challenge", attributes);
                    break;
                case HEAT_DELAY:
                    describeAndDisplay(TraderAlertType.HEAT_DELAY, "Heat Delay",
                                    "This event has been dalyed due to a heat delay", attributes);
                    break;
                case MEDICAL_TIMEOUT:
                    describeAndDisplay(TraderAlertType.MEDICAL_TIMEOUT, "Medical Timeout",
                                    "This event has been dalyed due to a medical timeout", attributes);
                    break;
                case ON_COURT_COACHING:
                    describeAndDisplay(TraderAlertType.ON_COURT_COACHING, "Oncourt Coaching",
                                    "This event has been dalyed due to on court coaching", attributes);
                    break;
                case TOILET_BREAK:
                    describeAndDisplay(TraderAlertType.TOILET_BREAK, "Toilet Break",
                                    "This event has been dalyed due to a toilet break", attributes);
                    break;
                case INPUT_PRICES_MISSING_WARNING:
                    describeAndDisplay(TraderAlertType.INPUT_PRICES_MISSING_WARNING, "Prices missing warning",
                                    "This event has not scheduled a param find in 3 minutes", attributes);
                    break;
                case INPUT_PRICES_MISSING_DANGER:
                    describeAndDisplay(TraderAlertType.INPUT_PRICES_MISSING_DANGER, "Prices missing - DANGER",
                                    "This event has not scheduled a param find in 5 minutes", attributes);
                    break;
                case MATCHFORMAT_WARNING:
                    describeAndDisplay(TraderAlertType.MATCHFORMAT_WARNING, "Match Format Incorrect",
                                    "This event has had match format insert incorrectly", attributes);
                    break;
                case INPUT_INCIDENT_MISSING_DANGER:
                    describeAndDisplay(TraderAlertType.INPUT_INCIDENT_MISSING_DANGER, "Incidents missing - DANGER",
                                    "This event has not received an incident for 2 minutes ", attributes);
                    break;
                case INPUT_INCIDENT_MISSING_WARNING:
                    describeAndDisplay(TraderAlertType.INPUT_PRICES_MISSING_WARNING, "Incidents missing warning",
                                    "This event has not received an incident for 1 minute", attributes);
                    break;
                default:
                    break;
            }
        }

        info("EventID = %s, Alert Notification to be published = %s", tradedEvent.getEventId(),
                        alertNotification.getName());
        alertNotification.setSeverity(BigInteger.ONE);
        return alertNotification;
    }

    protected void describeAndDisplay(TraderAlertType alertType, String title, String description,
                    List<Attribute> attributes) {
        title(title, attributes);
        description(description, attributes);
        display(attributes);
        alertType(alertType, attributes);
    }

    protected void title(String title, List<Attribute> attributes) {
        attributes.add(attrib("Title", title));
    }

    protected void description(String description, List<Attribute> attributes) {
        attributes.add(attrib("Description", description));
    }

    protected void alertType(TraderAlertType alertType, List<Attribute> attributes) {
        attributes.add(attrib("alertType", alertType.toString()));
    }

    protected void display(List<Attribute> attributes) {
        attributes.add(attrib("Display", "true"));
    }

    protected void addAlgoAttributes(TraderAlert traderAlert, List<Attribute> attributes) {
        Map<String, String> alertAttribute = traderAlert.getAlertAttributes();
        for (Entry<String, String> attribute : alertAttribute.entrySet()) {
            attributes.add(attrib(attribute.getKey(), attribute.getValue()));
        }
        attributes.add(attrib("Algo Alert Type", traderAlert.getTraderAlertType().toString()));
        attributes.add(attrib("Description", traderAlert.getAlertText()));

    }

    private Attribute attrib(String name, String value) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setValue(value);
        return attribute;
    }
}
