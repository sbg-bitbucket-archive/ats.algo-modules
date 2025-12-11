package example.algo.thirdparty.server;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MQHelper;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class ExampleServer {

    private static final Logger logger = LoggerFactory.getLogger(ExampleServer.class);
    private SupportedSportType sportId;
    private String brokerUser;
    private String brokerPassword;
    private String brokerUrl;
    private Session subscriberSession;
    private Session publisherSession;
    private MessageProducer responseProducer;
    private PriceCalcResponse priceCalcResponse;
    private ParamFindResponse paramFindResponse;

    /**
     * 
     * @param brokerUser
     * @param brokerPassword
     * @param brokerUrl
     * @param priceCalcResponse
     * @param paramFindResponse
     */
    public ExampleServer(SupportedSportType sportId, String brokerUser, String brokerPassword, String brokerUrl,
                    PriceCalcResponse priceCalcResponse, ParamFindResponse paramFindResponse) {
        try {
            this.sportId = sportId;
            this.brokerUser = brokerUser;
            this.brokerPassword = brokerPassword;
            this.brokerUrl = brokerUrl;
            this.priceCalcResponse = priceCalcResponse;
            this.paramFindResponse = paramFindResponse;
            /*
             * run test to verify serialize/deserialize is ok
             */
            String json1 = JsonSerializer.serialize(priceCalcResponse, true);
            String json2 = JsonSerializer.serialize(paramFindResponse, true);
            PriceCalcResponse priceCalcResponse2 = JsonSerializer.deserialize(json1, PriceCalcResponse.class);
            ParamFindResponse paramFindResponse2 = JsonSerializer.deserialize(json2, ParamFindResponse.class);
            String err = null;
            if (!priceCalcResponse.equals(priceCalcResponse2)) {
                err = "PriceCalcResponse fails serialisation/deserialisation test";
            }
            if (!paramFindResponse.equals(paramFindResponse2)) {
                err = "ParamFindResponse fails serialisation/deserialisation test";
            }
            if (err == null)
                System.out.println("Response deserialisation self-test passed ok.");
            else {
                System.out.println(err);
                System.out.println("CAnnot continue.  Exiting");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("unhandled exception.  can't continue");
            System.exit(1);
        }
    }

    public void handlePriceCalcMessage(Message message) {
        handleMessage(message, priceCalcResponse);
    }

    public void handleParamFindMessage(Message message) {
        handleMessage(message, paramFindResponse);
    }

    private void handleMessage(Message message, AbstractAlgoResponse response) {
        try {
            Destination replyTo = message.getJMSReplyTo();
            String uniqueRequestId = message.getJMSCorrelationID();
            /*
             * decode the message to ensure that we can read it ok, but aren't actually going to use any of the contents
             */

            @SuppressWarnings("unused")
            String request = MQHelper.decodeMessage(message);

            // printString("REQUEST: ", request);
            response.setUniqueRequestId(uniqueRequestId);
            String jsonResponse = JsonUtil.marshalJson(response, true);
            // printString("RESPONSE: ", jsonResponse);
            Message msg = MQHelper.generateMessage(publisherSession, jsonResponse, MQHelper.isCompressed(message));
            msg.setJMSCorrelationID(uniqueRequestId);
            logger.info("Sending response to: %s", replyTo.toString());
            responseProducer.send(replyTo, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void start() throws JMSException {
        logger.info("Connecting to ActiveMQ using URL " + brokerUrl + ", with userId: " + brokerUser + ", password: "
                        + brokerPassword);

        ActiveMQConnectionFactory connectionFactory =
                        new ActiveMQConnectionFactory(brokerUser, brokerPassword, brokerUrl);
        connectionFactory.setTrustAllPackages(true);

        Connection connection = connectionFactory.createConnection(brokerUser, brokerPassword);
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

        this.responseProducer = publisherSession.createProducer(null);
        connection.start();
    }

}
