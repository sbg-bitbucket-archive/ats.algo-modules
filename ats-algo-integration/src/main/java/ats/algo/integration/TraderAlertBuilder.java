package ats.algo.integration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlertAttributes;
import ats.core.AtsBean;
import generated.ats.betsync.dto.Attribute;
import generated.ats.betsync.dto.NotificationMessage;

public class TraderAlertBuilder extends AtsBean {

    public NotificationMessage createTraderAlertNotification(TradedEvent tradedEvent, TraderAlert traderAlert) {
        NotificationMessage alertNotification = tradedEvent.createDisplayVariable("algoManagerTraderAlert",
                        Long.toString(System.currentTimeMillis()));

        if (!(traderAlert.getAlertAttributes() == null)) {
            addAlgoAttributes(traderAlert, alertNotification);

        }

        info("EventID = %s, Alert Notification to be published = %s", tradedEvent.getEventId(),
                        alertNotification.getName());
        alertNotification.setSeverity(BigInteger.ONE);
        return alertNotification;
    }

    protected void addAlgoAttributes(TraderAlert traderAlert, NotificationMessage alertNotification) {
        List<Attribute> attributes = alertNotification.getAttributes();
        Map<String, TraderAlertAttributes> traderAlertAttributes = traderAlert.getAlertAttributes();
        for (Entry<String, TraderAlertAttributes> traderAlertAttribute : traderAlertAttributes.entrySet()) {
            if (traderAlertAttribute.getValue().isMap()) {
                Map<String, String> map = traderAlertAttribute.getValue().getParamAttributes();
                attributes.add(attribWithMap(traderAlertAttribute.getKey(), map));
            } else {
                if (traderAlertAttribute.getKey().equals("Alert Text")) {
                    alertNotification.setDescription(traderAlertAttribute.getValue().getAttributes());
                }
                attributes.add(attrib(traderAlertAttribute.getKey(), traderAlertAttribute.getValue().getAttributes()));
            }
        }
        attributes.add(attrib("Algo Alert Type", traderAlert.getTraderAlertType().toString()));

    }

    private Attribute attrib(String name, String value) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setValue(value);
        return attribute;
    }

    private Attribute attribWithMap(String name, Map<String, String> paramAttributesFromResults) {
        Attribute attribute = new Attribute();
        List<Attribute> paramAttributes = new ArrayList<Attribute>();
        for (Entry<String, String> paramDetails : paramAttributesFromResults.entrySet()) {
            Attribute paramAttribute = new Attribute();
            paramAttribute.setName(paramDetails.getKey());
            paramAttribute.setValue(paramDetails.getValue());
            paramAttributes.add(paramAttribute);
        }
        attribute.setName(name);
        attribute.setSubAttributes(paramAttributes);
        return attribute;
    }
}
