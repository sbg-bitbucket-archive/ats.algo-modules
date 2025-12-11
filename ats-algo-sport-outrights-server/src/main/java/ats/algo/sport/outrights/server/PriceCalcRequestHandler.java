package ats.algo.sport.outrights.server;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.request.AbstractAlgoRequest;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.MQHelper;
import ats.algo.sport.outrights.calcengine.core.Outrights;
import ats.core.util.json.JsonUtil;

public class PriceCalcRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(PriceCalcRequestHandler.class);

    private static final SupportedSportType sportId = SupportedSportType.OUTRIGHTS;
    private static final String brokerUser = "admin";
    private static final String brokerPassword = "admin";
    private static final String brokerUrl = "tcp://localhost:61616/";

    private Outrights outrights;
    private Session subscriberSession;
    private Session publisherSession;
    private MessageProducer priceCalcResponseProducer;
    private MessageProducer paramFindResponseProducer;

    PriceCalcRequestHandler(Outrights outrights) {
        this.outrights = outrights;
    }

    public void handlePriceCalcMessage(Message message) {

        PriceCalcRequest request = (PriceCalcRequest) getRequest(message, PriceCalcRequest.class);
        logger.info("price calc request received for uniqueRequestId: %s", request.getUniqueRequestId());
        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        publishResponse(response, priceCalcResponseProducer, MQHelper.isCompressed(message));
        logger.info("price calc response issued for uniqueRequestId: %s", response.getUniqueRequestId());
    }

    public void handleParamFindMessage(Message message) {
        logger.info("param find request received");
        ParamFindRequest request = (ParamFindRequest) getRequest(message, ParamFindRequest.class);
        ParamFindResponse response = outrights.handleParamFindRequest(request);
        publishResponse(response, paramFindResponseProducer, MQHelper.isCompressed(message));
        logger.info("param find response issued");
    }

    private AbstractAlgoRequest getRequest(Message message, Class<? extends AbstractAlgoRequest> clazz) {
        AbstractAlgoRequest request = null;
        try {
            String json = MQHelper.decodeMessage(message);
            request = JsonUtil.unmarshalJson(json, clazz);
        } catch (UnsupportedEncodingException | JMSException | DataFormatException e) {
            logger.error("Can't read request");
            logger.error(e.toString());
        }
        return request;
    }

    private void publishResponse(AbstractAlgoResponse response, MessageProducer messageProducer,
                    boolean compressResponse) {
        try {
            String json = JsonUtil.marshalJson(response);
            Message msg = MQHelper.generateMessage(publisherSession, json, compressResponse);
            messageProducer.send(msg);
        } catch (JMSException | UnsupportedEncodingException e) {
            logger.error("Can't encode response");
            logger.error(e.toString());
        }
    }

    public void listenAndServe() {
        logger.info("Connecting to ActiveMQ using URL " + brokerUrl + ", with userId: " + brokerUser + ", password: "
                        + brokerPassword);
        ActiveMQConnectionFactory connectionFactory =
                        new ActiveMQConnectionFactory(brokerUser, brokerPassword, brokerUrl);
        connectionFactory.setTrustAllPackages(true);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection(brokerUser, brokerPassword);
            subscriberSession = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            publisherSession = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            String priceCalcQueueName = "priceCalcQueue." + sportId.toString();
            String paramFindQueueName = "paramFindQueue." + sportId.toString();
            logger.info("Monitoring price calc queue: " + priceCalcQueueName);
            logger.info("Monitoring param find queue: " + paramFindQueueName);

            Queue priceCalcQueue = subscriberSession.createQueue(priceCalcQueueName);
            MessageConsumer priceCalcConsumer = subscriberSession.createConsumer(priceCalcQueue);
            priceCalcConsumer.setMessageListener(this::handlePriceCalcMessage);

            Queue paramFindQueue = subscriberSession.createQueue(paramFindQueueName);
            MessageConsumer paramFindConsumer = subscriberSession.createConsumer(paramFindQueue);
            paramFindConsumer.setMessageListener(this::handleParamFindMessage);

            this.priceCalcResponseProducer =
                            publisherSession.createProducer(publisherSession.createTopic("algoMgrPriceCalcResponse"));
            this.paramFindResponseProducer =
                            publisherSession.createProducer(publisherSession.createTopic("algoMgrParamFindResponse"));
            connection.start();
            logger.info("MQ connection established");
        } catch (JMSException e) {
            logger.error("Can't start MQ connection.  Server will not respond to PriceCalcRequests");
        }

    }

}
