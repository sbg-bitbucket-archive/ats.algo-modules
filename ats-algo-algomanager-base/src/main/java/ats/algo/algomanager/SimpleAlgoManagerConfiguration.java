package ats.algo.algomanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MQHelper;

/**
 * A simple single threaded configuration which directly wires the inputs and outputs between AlgoManager AlgoCalculator
 * and AlgoPAramFinder
 * 
 * @author Geoff
 *
 */

public class SimpleAlgoManagerConfiguration extends AlgoManagerConfiguration {

    private static final boolean USE_RESPONSE_QUEUES = false;

    AlgoCalculator algoCalculator;
    AlgoParamFinder algoParamFinder;
    String externalModelBrokerUrl;
    Session publisherSession;
    Session subscriberSession;
    MessageProducer priceCalcRequestProducer;
    MessageProducer paramFindRequestProducer;
    Destination priceCalcResponseQueue;
    Destination paramFindResponseQueue;

    long openEventId;
    String openUniqueRequestId;
    Connection connection;
    Signal signal;

    public SimpleAlgoManagerConfiguration() {
        super();
        algoCalculator = new AlgoCalculator();
        algoParamFinder = new AlgoParamFinder();
    }

    private boolean connectToBroker() {
        if (connection != null) {
            /*
             * connection already established
             */
            return true;
        }

        try {
            String brokerUser = "admin";
            String brokerPassword = "admin";
            info("Connecting to ActiveMQ using URL " + externalModelBrokerUrl + ", user " + brokerUser + ", password "
                            + brokerPassword);

            ActiveMQConnectionFactory connectionFactory =
                            new ActiveMQConnectionFactory(brokerUser, brokerPassword, externalModelBrokerUrl);
            connectionFactory.setTrustAllPackages(true);

            connection = connectionFactory.createConnection(brokerUser, brokerPassword);
            subscriberSession = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            publisherSession = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            /*
             * set the listeners for any legacy topic based responses
             */
            Topic priceCalcTopic = subscriberSession.createTopic("algoMgrPriceCalcResponse");
            MessageConsumer priceCalcTopicConsumer = subscriberSession.createConsumer(priceCalcTopic);
            priceCalcTopicConsumer.setMessageListener(this::handlePriceCalcMessage);
            Topic paramFindTopic = subscriberSession.createTopic("algoMgrParamFindResponse");
            MessageConsumer paramFindTopicConsumer = subscriberSession.createConsumer(paramFindTopic);
            paramFindTopicConsumer.setMessageListener(this::handleParamFindMessage);
            /*
             * set the listeners for replytoQueue based responses
             */
            priceCalcResponseQueue = publisherSession.createTemporaryQueue();
            MessageConsumer priceCalcResponseConsumer = publisherSession.createConsumer(priceCalcResponseQueue);
            priceCalcResponseConsumer.setMessageListener(this::handlePriceCalcMessage);
            paramFindResponseQueue = publisherSession.createTemporaryQueue();
            MessageConsumer paramFindResponseConsumer = publisherSession.createConsumer(paramFindResponseQueue);
            paramFindResponseConsumer.setMessageListener(this::handleParamFindMessage);
            connection.start();
            return true;
        } catch (Exception ex) {
            connection = null;
            error("Can't connect to Active MQ broker %s", ex);
            return false;
        }
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request) {
        /*
         * serialise all the data going to Algo calculator, to simulate what happens in the live config
         */
        PriceCalcRequest request2 = (PriceCalcRequest) jmsProxy(request);
        algoCalculator.setCalculationParams(request2);
        try {
            algoCalculator.call();
        } catch (Exception e) {
            error("algoCalculator call generated an exception");
            algoHandlePriceCalcError.handle(request.getEventId(), request.getUniqueRequestId(), e.getMessage());
            return;
        }
        Markets markets = (Markets) jmsProxy(algoCalculator.getCalculatedMarkets());
        MatchParams updatedMatchParams = (MatchParams) jmsProxy(algoCalculator.getUpdatedMatchParams());
        MatchEngineSavedState updatedMatchEngineSavedState =
                        (MatchEngineSavedState) jmsProxy(algoCalculator.getUpdatedMatchEngineSavedState());

        PriceCalcResponse response = new PriceCalcResponse(request.getUniqueRequestId(), markets,
                        updatedMatchParams.generateGenericMatchParams(), updatedMatchEngineSavedState, null);
        // response.setTimePriceCalcRequestReceived(startTime);
        // response.setTimePriceCalcResponseIssued(endTime);
        if (response.isFatalError())
            algoHandlePriceCalcError.handle(request.getEventId(), request.getUniqueRequestId(),
                            response.getFatalErrorCause());
        else
            algoHandlePriceCalcResponse.handle(request.getEventId(), response);
    }

    @Override
    public boolean establishExternalModelConnection(String url) {
        externalModelBrokerUrl = url;
        if (null != externalModelBrokerUrl) {
            info("externalModelBrokerUrl is " + externalModelBrokerUrl + ", will connect to external model broker");
            return connectToBroker();
        } else {
            error("not valid url", url);
            return false;
        }
    }

    @Override
    public void scheduleExternalPriceCalc(PriceCalcRequest request) {
        /*
         * this method needs to: serialise request to json, send to JMS broker. wait for the response
         */
        String json = JsonSerializer.serialize(request, true);

        /*
         * verify that we can deserialize back to the original object.
         */
        PriceCalcRequest request2 = JsonSerializer.deserialize(json, PriceCalcRequest.class);
        if (!request.equals(request2))
            throw new IllegalArgumentException("deserialized object does not equal original");

        String queue = "priceCalcQueue." + request.getMatchFormat().getSportType().toString();

        try {
            openEventId = request.getEventId();
            openUniqueRequestId = request.getUniqueRequestId();
            signal = new Signal();
            priceCalcRequestProducer = publisherSession.createProducer(publisherSession.createQueue(queue));
            publishJsonRequest(openUniqueRequestId, this.priceCalcResponseQueue, json, priceCalcRequestProducer);

        } catch (Exception ex) {
            error("Problem with external price calc %s", ex, request);
        }
        while (!signal.hasDataToProcess()) {
            sleep();
        }
        Message jmsMessage = signal.getJmsMessage();
        PriceCalcResponse response = null;
        try {
            if (jmsMessage instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) jmsMessage;
                response = (PriceCalcResponse) om.getObject();
            } else if (jmsMessage instanceof TextMessage || jmsMessage instanceof BytesMessage) {
                String json2 = MQHelper.decodeMessage(jmsMessage);
                if (MQHelper.isCompressed(jmsMessage))
                    debug("Decompressed received message.  Size %d -> %d", MQHelper.getMessageLength(jmsMessage),
                                    json.length());
                response = JsonSerializer.deserialize(json2, PriceCalcResponse.class);
            } else
                warn("Received unrecognised response %s", jmsMessage);
            String uniqueRequestId = response.getUniqueRequestId();
            info("Deserialized response for uniqueRequestId: " + uniqueRequestId);
            if (openUniqueRequestId.equals(uniqueRequestId)) {
                info("Handle price calc response :", response);
                if (response.isFatalError())
                    algoHandlePriceCalcError.handle(openEventId, request.getUniqueRequestId(),
                                    response.getFatalErrorCause());
                algoHandlePriceCalcResponse.handle(openEventId, response);
            } else {
                warn("unexpected response received for uniqueRequestId: " + uniqueRequestId + ".  Ignored");
            }
        } catch (Exception ex) {
            error("Problem with external price calc %s", ex);
        }
    }

    public void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scheduleParamFind(ParamFindRequest request) {
        ParamFindRequest request2 = (ParamFindRequest) jmsProxy(request);
        algoParamFinder.setParamFindParams(request2);
        try {
            algoParamFinder.call();
        } catch (Exception e) {
            error("algoParamFinder call unexpectedly interrupted");
            algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(), e.getMessage());
            return;
        }
        ParamFindResults paramFindResults = (ParamFindResults) jmsProxy(algoParamFinder.getParamFindResults());
        MatchParams matchParams = (MatchParams) jmsProxy(algoParamFinder.getCalculatedMatchParams());
        ParamFindResponse response = new ParamFindResponse(request.getUniqueRequestId(), paramFindResults,
                        matchParams.generateGenericMatchParams());
        if (response.isFatalError())
            algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(),
                            response.getFatalErrorCause());
        else
            algoHandleParamFindResponse.handle(request.getEventId(), response);
    }

    @Override
    public void scheduleExternalParamFind(ParamFindRequest request) {

        String json = JsonSerializer.serialize(request, true);

        /*
         * verify that we can deserialize back to the orginal object.
         */
        checkSerializationOk(request, json);

        String queue = "paramFindQueue." + request.getMatchFormat().getSportType().toString();
        try {
            openEventId = request.getEventId();
            openUniqueRequestId = request.getUniqueRequestId();
            signal = new Signal();
            paramFindRequestProducer = publisherSession.createProducer(publisherSession.createQueue(queue));
            publishJsonRequest(openUniqueRequestId, this.paramFindResponseQueue, json, paramFindRequestProducer);
        } catch (Exception ex) {
            error("Problem with external param find %s", ex, request);
        }
        while (!signal.hasDataToProcess()) {
            sleep();
        }
        Message jmsMessage = signal.getJmsMessage();
        try {
            TextMessage tm = (TextMessage) jmsMessage;
            json = tm.getText();
            ParamFindResponse paramFindResponse = JsonSerializer.deserialize(json, ParamFindResponse.class);
            String uniqueRequestId = paramFindResponse.getUniqueRequestId();
            info("Successfully deserialized response for uniqueRequestId: %s" + uniqueRequestId);
            if (openUniqueRequestId.equals(uniqueRequestId)) {
                info("Handle param find response :", paramFindResponse);
                if (paramFindResponse.isFatalError())
                    algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(),
                                    paramFindResponse.getFatalErrorCause());
                else
                    algoHandleParamFindResponse.handle(openEventId, paramFindResponse);
            } else {
                warn("unexpected response received for uniqueRequestId: " + uniqueRequestId + ".  Ignored");
            }
        } catch (Exception ex) {
            error("Problem with external param find calc %s", ex);
        }

    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {
        /*
         * serialise all the data going to Algo calculator, to simulate what happens in the live config
         */

        PriceCalcRequest request2 = (PriceCalcRequest) jmsProxy(request);
        AbstractPriceCalculator calculator = null;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            calculator = (AbstractPriceCalculator) constructor.newInstance();
        } catch (Exception e) {
            String errMsg = String.format("Can't create instance of class: %s ", clazz);
            error(errMsg);
            algoHandlePriceCalcError.handle(request.getEventId(), request.getUniqueRequestId(), errMsg);
            return;
        }
        PriceCalcResponse response = calculator.calculate(request2);
        if (response.isFatalError())
            algoHandlePriceCalcError.handle(request2.getEventId(), response.getUniqueRequestId(),
                            response.getFatalErrorCause());
        else {
            algoHandlePriceCalcResponse.handle(request2.getEventId(), response);
        }

    }

    @Override
    public void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {
        /*
         * serialise all the data going to Algo calculator, to simulate what happens in the live config
         */

        ParamFindRequest request2 = (ParamFindRequest) jmsProxy(request);
        AbstractParamFinder paramFinder;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            paramFinder = (AbstractParamFinder) constructor.newInstance();
        } catch (Exception e) {
            String errMsg = String.format("Can't create instance of class: %s ", clazz);
            error(errMsg);
            algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(), errMsg);
            return;
        }
        ParamFindResponse response = paramFinder.calculate(request2);
        if (response.isFatalError()) {
            algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(),
                            response.getFatalErrorCause());

        } else
            algoHandleParamFindResponse.handle(request2.getEventId(), response);
    }

    private void checkSerializationOk(ParamFindRequest request, String json) {
        ParamFindRequest request2 = JsonSerializer.deserialize(json, ParamFindRequest.class);
        assertEquals(request.getClass(), request2.getClass());
        assertEquals(request.getMatchFormat(), request2.getMatchFormat());
        assertEquals(request.getMatchState(), request2.getMatchState());
        for (Entry<String, MatchParam> e : request.getMatchParams().getParamMap().entrySet()) {
            MatchParam p1 = e.getValue();
            MatchParam p2 = request2.getMatchParams().getParamMap().get(e.getKey());
            // System.out.println(p1.getClass().toString() + p1);
            // System.out.println(p2.getClass().toString() + p2);
            assertEquals(p1, p2);

        }
        assertEquals(request.getMatchParams(), request2.getMatchParams());
        assertEquals(request.getMatchEngineSavedState(), request2.getMatchEngineSavedState());
        assertEquals(request.getMarketPricesList(), request2.getMarketPricesList());
        assertEquals(request.getUniqueRequestId(), request2.getUniqueRequestId());
        assertEquals(request.getRequestTime(), request2.getRequestTime());
        assertEquals(request, request2);
    }

    private void assertEquals(Object o1, Object o2) {
        if (o1 != null)
            if (!o1.equals(o2)) {
                error("Json Serialization error with object:" + o1.getClass().getName());
                error("Original version: " + o1.toString());
                error("Version following serialization/deserialization: " + o2.toString());
                error("Cannot continue.  Exiting");
                System.exit(1);
            }

    }

    /**
     * serialises the supplied object then deserializes and returns the desierialized version. Intended to catch any
     * errors that would affect when deployed in AlgoManager JMS config
     * 
     * @param o
     * @return
     */
    private Object jmsProxy(Object o) {
        if (o == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(o);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream ois;
        Object deserializedObject = null;
        try {
            ois = new ObjectInputStream(inputStream);
            deserializedObject = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR.  Object won't serialize: \n" + o.toString());
            e.printStackTrace();
            throw new IllegalArgumentException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deserializedObject;
    }

    @Override
    public int getPriceCalcQueueSize() {
        return 0;
    }

    @Override
    public int getParamFindQueueSize() {
        return 0;
    }

    @Override
    public int getNoAlgoCalculators() {
        return 1;
    }

    @Override
    public int getNoAlgoParamFinders() {
        return 1;
    }

    public void handlePriceCalcMessage(Message jmsMessage) {
        signal.setJmsMessage(jmsMessage);
        signal.setHasDataToProcess(true);
    }

    public void handleParamFindMessage(Message jmsMessage) {
        signal.setJmsMessage(jmsMessage);
        signal.setHasDataToProcess(true);

    }

    private void publishJsonRequest(String uniqueRequestId, Destination replyTo, String json,
                    MessageProducer producer) {
        try {
            TextMessage msg = publisherSession.createTextMessage();
            msg.setText(json);
            if (USE_RESPONSE_QUEUES) {
                msg.setJMSCorrelationID(uniqueRequestId);
                msg.setJMSReplyTo(replyTo);
            }
            producer.send(msg);
        } catch (Exception ex) {
            error("Problem with publishing json request", ex);
        }
    }

    class Signal {

        protected boolean hasDataToProcess;
        protected Message jmsMessage;

        public Signal() {
            reset();
        }

        public void reset() {
            hasDataToProcess = false;
        }

        public synchronized boolean hasDataToProcess() {
            return this.hasDataToProcess;
        }

        public synchronized void setHasDataToProcess(boolean hasData) {
            this.hasDataToProcess = hasData;
        }

        public void setJmsMessage(Message jmsMessage) {
            this.jmsMessage = jmsMessage;
        }

        public Message getJmsMessage() {
            return jmsMessage;
        }
    }

    @Override
    public void abandonPriceCalc(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

    @Override
    public void abandonParamFind(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }
}
